package de.svws_nrw.module.reporting.signing;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import de.svws_nrw.base.compression.GZip;
import de.svws_nrw.core.utils.encoding.Base45;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schule.ReportingSchule;
import de.svws_nrw.module.reporting.utils.ReportingBarcodeUtils;
import de.svws_nrw.module.reporting.utils.ReportingUhr;
import de.svws_nrw.oauth.OAuthHttpClientFactory;
import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.benutzer.BenutzerServiceFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import de.svws_nrw.service.signature.Signature;
import de.svws_nrw.service.signature.SignatureService;
import de.svws_nrw.service.signature.SignatureServiceFactory;
import de.svws_nrw.service.signature.SignatureStatus;

/**
 * Erzeugt die gerenderten QR-Codes der signierten Schulbescheinigung. Die mehrstufige Pipeline
 * (Ausstellungsdaten ermitteln, XSchule-XML erzeugen, in einem einzigen Batch signieren, QR-Codes als SVG rendern)
 * ist hier gebündelt, damit das aufrufende Repository nur das Caching der Ergebnisse verantwortet.
 * <p>Die Factory ist die Meldestelle für eine gescheiterte Signatur- oder QR-Erzeugung, denn nur sie kann unterscheiden, woran es lag: Scheitern Signierung
 * oder Rendering trotz vorliegender Ausgangsdaten, entsteht je Schüler ein Ausgabeproblem, und die Bescheinigung erscheint ohne Signatur.</p>
 * <p>Fehler der Ausgangsdaten fängt sie nicht. Ein nicht geladener Schüler erhält nur einen Fehlereintrag - er ist ausgefiltert oder nicht ladbar, und
 * beides meldet eine andere Stelle. Ein Fehler beim Aufbau der Ausstellungsdaten propagiert, weil die Schulstammdaten längst geladen sind und ein Scheitern
 * dort einen inkonsistenten Zustand bezeichnet.</p>
 */
public final class SchulbescheinigungQrFactory {

	private final ReportingContext reportingContext;

	/** Liefert den Signier-Service. Wird erst bei Bedarf innerhalb der Fehlerbehandlung ausgewertet, damit z. B. eine fehlende
	 * Konfiguration des Dienstes nicht zum Absturz, sondern zu einer Fehlermeldung je Schüler führt. */
	private final Supplier<SignatureService> signatureServiceSupplier;

	/** Die Uhr, aus der das Ausstellungsdatum der Bescheinigung bezogen wird. */
	private final Clock clock;

	/**
	 * Erstellt eine neue Factory, die den Signier-Service über die Standard-Factories und das Ausstellungsdatum über die Standard-Uhr des Reportings
	 * bezieht.
	 *
	 * @param reportingContext Der zentrale Reporting-Context mit Zugriff auf Schüler-Repository, Logger und Datenbankverbindung.
	 */
	public SchulbescheinigungQrFactory(final ReportingContext reportingContext) {
		this(reportingContext, SchulbescheinigungQrFactory::erzeugeSignatureService, ReportingUhr.standard());
	}

	/**
	 * Erstellt eine neue Factory mit einem injizierten Signier-Service und einer injizierten Uhr. Dient primär der Testbarkeit: Nur mit einer festen Uhr
	 * ist der signierte Inhalt reproduzierbar.
	 *
	 * @param reportingContext         Der zentrale Reporting-Context.
	 * @param signatureServiceSupplier Lieferant für den zu verwendenden Signier-Service.
	 * @param clock                    Die Uhr, aus der das Ausstellungsdatum bezogen wird.
	 */
	SchulbescheinigungQrFactory(final ReportingContext reportingContext, final Supplier<SignatureService> signatureServiceSupplier, final Clock clock) {
		this.reportingContext = reportingContext;
		this.signatureServiceSupplier = signatureServiceSupplier;
		this.clock = clock;
	}

	/**
	 * Erzeugt für alle übergebenen Schüler die QR-Daten der Schulbescheinigung in einem einzigen Signier-Batch. Für jede ID wird
	 * ein Eintrag geliefert (im Fehlerfall mit gesetzter Fehlermeldung), damit niemals {@code null} in der Cache-Map landet.
	 *
	 * @param idsSchueler Die IDs der zu verarbeitenden Schüler.
	 *
	 * @return Map von der Schüler-ID auf die zugehörigen QR-Daten.
	 */
	public Map<Long, SchulbescheinigungQrDaten> erzeuge(final List<Long> idsSchueler) {
		final Map<Long, SchulbescheinigungQrDaten> ergebnis = new HashMap<>();

		// Gemeinsame Schul- und Abschnittsdaten einmalig ermitteln. Ein Fehler dabei propagiert, siehe Klassenkommentar.
		final SchulbescheinigungAusstellungsdaten ausstellungsdaten = ermittleAusstellungsdaten();

		// Je Schüler das XSchule-XML erzeugen. Konnte für keinen Schüler ein XML erzeugt werden, ist ein Aufruf des Signierdienstes überflüssig.
		final Map<Object, byte[]> xmlBytesById = erzeugeXml(idsSchueler, ausstellungsdaten, ergebnis);
		if (xmlBytesById.isEmpty()) {
			return ergebnis;
		}

		// Genau ein Batch-Aufruf an den Signierdienst und anschließend je Schüler die QR-Codes rendern.
		rendereQrCodes(idsSchueler, xmlBytesById, signiere(xmlBytesById), ergebnis);
		return ergebnis;
	}

	/**
	 * Ermittelt die für alle Schüler identischen Ausstellungsdaten der Schulbescheinigung. Kein Schritt hier wird gefangen: Die Schulstammdaten sind längst
	 * geladen, ein Scheitern bezeichnet deshalb einen inkonsistenten Zustand - etwa einen fehlenden aktuellen Schuljahresabschnitt. Der Fehler propagiert und
	 * wird vom Lade-Fallback des aufrufenden Repositories je Schüler festgehalten.
	 *
	 * @return Die Ausstellungsdaten der Schulbescheinigung.
	 */
	private SchulbescheinigungAusstellungsdaten ermittleAusstellungsdaten() {
		final ReportingSchule schule = this.reportingContext.repositorySchule().schule();
		final String bildungsgangEnddatum = (schule.aktuellerSchuljahresabschnitt().schuljahr() + 1) + "-07-31";
		return new SchulbescheinigungAusstellungsdaten(schule, schule.ort(), LocalDate.now(this.clock).toString(), bildungsgangEnddatum);
	}

	/**
	 * Erzeugt je Schüler das XSchule-XML und sammelt es unter der Schüler-ID als zu signierende Daten. Schüler, deren XML nicht
	 * erzeugt werden konnte, erhalten einen Fehlereintrag in {@code ergebnis} und fehlen in der Rückgabe.
	 * <p>Ein Schüler ohne geladene Daten erhält nur den Fehlereintrag und kein Ausgabeproblem, denn das meldet der Datenzugriff oder es ist eine
	 * Auswahlentscheidung. Scheitert der XML-Bau trotz geladener Daten, ist es ein Erzeugungsfehler dieser Factory und wird gemeldet.</p>
	 *
	 * @param idsSchueler       Die IDs der zu verarbeitenden Schüler.
	 * @param ausstellungsdaten Die gemeinsamen Ausstellungsdaten der Schulbescheinigung.
	 * @param ergebnis          Die Ergebnis-Map, in die im Fehlerfall die Fehlermeldungen eingetragen werden.
	 *
	 * @return Map von der Schüler-ID auf die XML-Bytes der erfolgreich erzeugten Schulbescheinigungen.
	 */
	private Map<Object, byte[]> erzeugeXml(final List<Long> idsSchueler, final SchulbescheinigungAusstellungsdaten ausstellungsdaten,
			final Map<Long, SchulbescheinigungQrDaten> ergebnis) {
		final Map<Object, byte[]> xmlBytesById = new HashMap<>();
		for (final Long id : idsSchueler) {
			final ReportingSchueler schueler = this.reportingContext.repositorySchueler().schueler(id);
			if (schueler == null) {
				ergebnis.put(id, new SchulbescheinigungQrDaten(null, null, "Zum Schüler liegen keine geladenen Daten vor."));
				continue;
			}
			try {
				final String xml = SchulbescheinigungXmlFactory.erzeugeXml(schueler,
						ausstellungsdaten.schule(), ausstellungsdaten.ausstellungOrt(), ausstellungsdaten.ausstellungDatum(),
						ausstellungsdaten.bildungsgangEnddatum());
				xmlBytesById.put(id, xml.getBytes(StandardCharsets.UTF_8));
			} catch (final Exception e) {
				final String grund = "Die Schulbescheinigung konnte nicht erzeugt werden: " + e.getMessage();
				ergebnis.put(id, new SchulbescheinigungQrDaten(null, null, grund));
				meldeFehlendeSignatur(id, grund, e);
			}
		}
		return xmlBytesById;
	}

	/**
	 * Signiert die XSchule-XML-Dokumente aller Schüler in genau einem Batch-Aufruf. Die Schüler-ID dient dabei als
	 * Zuordnungsschlüssel; dies ist möglich, da der Signier-Service diese IDs nicht nach außen gibt. Ist der Signierdienst nicht
	 * erreichbar, wird für jeden Schüler eine Fehler-Signatur erzeugt, sodass die Render-Phase die einzige Stelle bleibt, die die
	 * Ergebnis-Map befüllt.
	 * <p>Die auslösende Exception wird im Ergebnis mitgeführt und hier nicht protokolliert: Sie erreicht über die Render-Phase die Meldung jedes betroffenen
	 * Schülers, und dort protokolliert sie die Meldefassade mit Ursachenkette und Stacktrace.</p>
	 *
	 * @param xmlBytesById Map von der Schüler-ID auf die zu signierenden XML-Bytes.
	 *
	 * @return Die Signaturen je Schüler-ID (bei Dienstausfall mit Status {@link SignatureStatus#ERROR}) samt der auslösenden Exception.
	 */
	private Signaturergebnis signiere(final Map<Object, byte[]> xmlBytesById) {
		try {
			return new Signaturergebnis(this.signatureServiceSupplier.get().sign(xmlBytesById), null);
		} catch (final Exception e) {
			final Signature fehlerSignatur = new Signature(null, SignatureStatus.ERROR, "Der Signierdienst ist nicht erreichbar: " + e.getMessage());
			final Map<Object, Signature> signaturen = new HashMap<>();
			for (final Object id : xmlBytesById.keySet()) {
				signaturen.put(id, fehlerSignatur);
			}
			return new Signaturergebnis(signaturen, e);
		}
	}

	/**
	 * Rendert je Schüler die beiden QR-Codes der Schulbescheinigung und trägt sie in {@code ergebnis} ein. Schüler, deren XML zuvor
	 * nicht erzeugt werden konnte, werden übersprungen (ihre Fehlermeldung wurde bereits hinterlegt).
	 * <p>Hier wird der Befund gemeldet: Wer diese Schleife erreicht, hat geladene Ausgangsdaten und ein erzeugtes XML, also ist jeder Fehler ein Signatur-
	 * oder Renderfehler.</p>
	 *
	 * @param idsSchueler      Die IDs der zu verarbeitenden Schüler.
	 * @param xmlBytesById     Map von der Schüler-ID auf die XML-Bytes (für QR1).
	 * @param signaturergebnis Die Signaturen je Schüler-ID (für QR2) samt der Exception eines gescheiterten Signier-Batches.
	 * @param ergebnis         Die Ergebnis-Map, in die die gerenderten QR-Daten eingetragen werden.
	 */
	private void rendereQrCodes(final List<Long> idsSchueler, final Map<Object, byte[]> xmlBytesById,
			final Signaturergebnis signaturergebnis, final Map<Long, SchulbescheinigungQrDaten> ergebnis) {
		// Kapazitätsprüfung == Render-Aufruf, identisches EC-Level.
		for (final Long id : idsSchueler) {
			final byte[] xmlBytes = xmlBytesById.get(id);
			if (xmlBytes == null) {
				// XML-Erzeugung ist fehlgeschlagen, die Fehlermeldung wurde bereits hinterlegt.
				continue;
			}
			final QrBauergebnis bau = baue(xmlBytes, signaturergebnis.signaturen().get(id), signaturergebnis.fehler());
			ergebnis.put(id, bau.daten());
			// Maßgeblich ist der fehlende Signatur-Code und nicht das Vorhandensein einer Fehlermeldung: Ein Signierergebnis kann einen Fehlerstatus ohne
			// eigenen Text tragen, und der Befund darf nicht daran hängen, ob der Dienst einen geliefert hat.
			if (bau.daten().qr2Svg() == null) {
				meldeFehlendeSignatur(id, bau.daten().fehlermeldung(), bau.fehler());
			}
		}
	}

	/**
	 * Meldet, dass die Schulbescheinigung dieses Schülers ohne Signatur-QR-Code ausgegeben wird, obwohl ihre Ausgangsdaten geladen sind. Die Bescheinigung
	 * entsteht weiterhin und zeigt an der Stelle des Codes den Fehlertext; ihr fehlt die Signatur als Teildatum
	 * ({@link ReportingProblemauswirkung#TEILDATEN_FEHLEN}), und weil der Inhalt vorliegt und allein seine signierte Darstellung scheitert, ist die Ursache
	 * {@link ReportingProblemursache#NICHT_DARSTELLBAR}. Die Fassade dedupliziert den Befund, sodass er je Schüler genau einmal zählt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 * @param grund      Der Sachverhalt für das Log.
	 * @param fehler     Der auslösende Fehler oder {@code null}, wenn nur ein Fehlertext vorliegt - etwa aus der Antwort des Signierdienstes.
	 */
	private void meldeFehlendeSignatur(final long idSchueler, final String grund, final Exception fehler) {
		this.reportingContext.meldeAusgabeproblem(ReportingProblemursache.NICHT_DARSTELLBAR, ReportingProblemauswirkung.TEILDATEN_FEHLEN,
				ReportingProblemSchluessel.fuer(SchulbescheinigungQrDaten.class, idSchueler),
				"Die Schulbescheinigung des Schülers %d wird ohne Signatur ausgegeben: %s".formatted(idSchueler, grund), fehler);
	}

	/**
	 * Rendert die beiden QR-Codes einer Schulbescheinigung als SVG. QR1 (Inhalt) wird immer erzeugt; QR2 (Signatur) nur bei
	 * erfolgreicher Signierung. Render-Fehler (z. B. Kapazitätsüberschreitung) werden gefangen und als Fehlermeldung abgelegt.
	 * <p>Der auslösende Fehler bleibt im Ergebnis erhalten, damit die Meldung des Ausgabeproblems ihn samt Ursachenkette und Stacktrace protokollieren kann.</p>
	 *
	 * @param xmlBytes       Das signierte XSchule-XML als Bytes (für QR1).
	 * @param signatur       Die Signatur des Schülers (für QR2); darf {@code null} sein.
	 * @param signaturfehler Die Exception, an der der Signier-Batch gescheitert ist, oder {@code null} - etwa wenn der Dienst mit einem Fehlerstatus
	 *                       geantwortet hat, ohne zu werfen.
	 *
	 * @return Die QR-Daten mit beiden SVGs (Erfolg) oder mit gesetzter Fehlermeldung, samt dem auslösenden Fehler.
	 */
	private static QrBauergebnis baue(final byte[] xmlBytes, final Signature signatur, final Exception signaturfehler) {
		final String qr1Svg;
		try {
			final String qr1Inhalt = SchulbescheinigungQrEinstellungen.PRAEFIX_QR1 + Base45.encode(GZip.encode(xmlBytes));
			qr1Svg = ReportingBarcodeUtils.erzeuge2DCodeQRCode(
					qr1Inhalt, SchulbescheinigungQrEinstellungen.QR_BREITE_MM, SchulbescheinigungQrEinstellungen.QR_HOEHE_MM,
					SchulbescheinigungQrEinstellungen.EC_QR1);
		} catch (final Exception e) {
			return new QrBauergebnis(new SchulbescheinigungQrDaten(null, null, "Der Inhalt-QR-Code konnte nicht erzeugt werden: " + e.getMessage()), e);
		}

		if ((signatur == null) || (signatur.status() != SignatureStatus.OK)) {
			return new QrBauergebnis(new SchulbescheinigungQrDaten(qr1Svg, null, signaturfehlertext(signatur)), signaturfehler);
		}

		try {
			final String qr2Inhalt = SchulbescheinigungQrEinstellungen.PRAEFIX_QR2 + Base45.encode(GZip.encode(signatur.content()));
			final String qr2Svg = ReportingBarcodeUtils.erzeuge2DCodeQRCode(
					qr2Inhalt, SchulbescheinigungQrEinstellungen.QR_BREITE_MM, SchulbescheinigungQrEinstellungen.QR_HOEHE_MM,
					SchulbescheinigungQrEinstellungen.EC_QR2);
			return new QrBauergebnis(new SchulbescheinigungQrDaten(qr1Svg, qr2Svg, null), null);
		} catch (final Exception e) {
			return new QrBauergebnis(new SchulbescheinigungQrDaten(qr1Svg, null, "Der Signatur-QR-Code konnte nicht erzeugt werden: " + e.getMessage()), e);
		}
	}

	/**
	 * Bildet den Fehlertext einer nicht erfolgreichen Signatur. Er ist nie leer: Ein Signierergebnis darf einen Status ungleich {@link SignatureStatus#OK} ohne
	 * eigene Fehlermeldung tragen, und ohne Ersatztext bliebe offen, warum die Signatur fehlt.
	 *
	 * @param signatur Die Signatur des Schülers oder {@code null}, wenn der Dienst zu ihm kein Ergebnis geliefert hat.
	 *
	 * @return Der Fehlertext, nie {@code null} und nie leer.
	 */
	private static String signaturfehlertext(final Signature signatur) {
		if (signatur == null) {
			return "Es wurde kein Signierergebnis geliefert.";
		}
		if ((signatur.errorMessage() == null) || signatur.errorMessage().isBlank()) {
			return "Die Signierung wurde mit dem Status %s ohne Fehlermeldung beendet.".formatted(signatur.status());
		}
		return signatur.errorMessage();
	}

	/**
	 * Erzeugt eine {@link SignatureService}-Instanz für das Signieren der Schulbescheinigungen. Die benötigte Datenbankverbindung
	 * wird von den Repository-Factories request-scoped bezogen; die URI des Signierdienstes liefert die {@link SignatureServiceFactory}.
	 *
	 * @return Der Service zum Signieren der XSchule-Dokumente.
	 */
	private static SignatureService erzeugeSignatureService() {
		return SignatureServiceFactory.getNewInstance(
				OAuthHttpClientFactory.getNewInstance(),
				EigeneSchuleServiceFactory.getNewInstance(EigeneSchuleRepositoryFactory.getNewInstance()),
				BenutzerServiceFactory.getNewInstance(BenutzerRepositoryFactory.getNewInstance()))
				.getSignatureService();
	}

	/**
	 * Gemeinsame Ausstellungsdaten einer Schulbescheinigung, die für alle Schüler eines Reportlaufs identisch sind.
	 *
	 * @param schule               Die Schule, die die Bescheinigung ausstellt.
	 * @param ausstellungOrt       Der Ausstellungsort.
	 * @param ausstellungDatum     Das Ausstellungsdatum im ISO-Format.
	 * @param bildungsgangEnddatum Das Enddatum des Bildungsgangs im ISO-Format.
	 */
	private record SchulbescheinigungAusstellungsdaten(ReportingSchule schule, String ausstellungOrt, String ausstellungDatum,
			String bildungsgangEnddatum) {
	}

	/**
	 * Das Ergebnis des Signier-Batches: die Signaturen je Schüler-ID und - bei einem Wurf des Dienstes - die auslösende Exception. Die {@link Signature} des
	 * Dienstes führt nur einen Fehlertext; ohne diesen Träger ginge die Exception zwischen Batch und je-Schüler-Meldung verloren.
	 *
	 * @param signaturen Die Signaturen je Schüler-ID; bei Dienstausfall mit Status {@link SignatureStatus#ERROR}.
	 * @param fehler     Die Exception, an der der Batch gescheitert ist, oder {@code null}.
	 */
	private record Signaturergebnis(Map<Object, Signature> signaturen, Exception fehler) {
	}

	/**
	 * Das Ergebnis des QR-Renderings eines Schülers: die QR-Daten für Cache und Vorlage sowie der auslösende Fehler, falls das Rendern oder Signieren
	 * gescheitert ist.
	 *
	 * @param daten  Die QR-Daten des Schülers, nie {@code null}.
	 * @param fehler Der Fehler, an dem Signatur oder Rendering gescheitert sind, oder {@code null}.
	 */
	private record QrBauergebnis(SchulbescheinigungQrDaten daten, Exception fehler) {
	}

}
