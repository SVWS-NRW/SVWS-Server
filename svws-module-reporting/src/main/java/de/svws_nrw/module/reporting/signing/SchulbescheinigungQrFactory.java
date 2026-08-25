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
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Erzeugt die gerenderten QR-Codes der signierten Schulbescheinigung. Die mehrstufige Pipeline
 * (Ausstellungsdaten ermitteln, XSchule-XML erzeugen, in einem einzigen Batch signieren, QR-Codes als SVG rendern)
 * ist hier gebündelt, damit das aufrufende Repository nur das Caching der Ergebnisse verantwortet.
 * <p>Die Factory ist die Meldestelle für eine gescheiterte Signatur- oder QR-Erzeugung, denn nur sie kann unterscheiden, woran es lag: Scheitern Signierung
 * oder Rendering trotz vorliegender Ausgangsdaten, entsteht je Schüler ein Ausgabeproblem, und die Bescheinigung erscheint ohne Signatur. Ein Ausfall des
 * Signierdienstes, ein Anmeldefehler oder ein Stapel ohne eine einzige erfolgreiche Signatur brechen dagegen die gesamte Erstellung statustragend ab - ohne
 * Dienst entsteht keine signierte Bescheinigung.</p>
 * <p>Fehler der Ausgangsdaten fängt sie nicht. Ein nicht geladener Schüler erhält nur einen Fehlereintrag - er ist ausgefiltert oder nicht ladbar, und
 * beides meldet eine andere Stelle. Ein Fehler beim Aufbau der Ausstellungsdaten propagiert, weil die Schulstammdaten längst geladen sind und ein Scheitern
 * dort einen inkonsistenten Zustand bezeichnet.</p>
 */
public final class SchulbescheinigungQrFactory {

	private final ReportingContext reportingContext;

	/** Liefert den Signier-Service. Er wird erst beim Signieren selbst bezogen, damit z. B. eine fehlende Konfiguration des Dienstes nicht schon beim
	 * Erzeugen der Factory auffällt, sondern innerhalb der Fehlerbehandlung des Signierens - und dort wie ein Ausfall des Dienstes zum statustragenden
	 * Abbruch führt. */
	private final Supplier<SignatureService> signatureServiceSupplier;

	/** Die Uhr, aus der das Ausstellungsdatum der Bescheinigung bezogen wird. */
	private final Clock clock;

	/**
	 * Erstellt eine neue Factory, die den Signier-Service über die Standard-Factories und das Ausstellungsdatum über die Standard-Uhr des Reportings
	 * bezieht.
	 *
	 * @param reportingContext Der zentrale Reporting-Context mit Zugriff auf Schul- und Schüler-Repository und die Meldefassade für Ausgabeprobleme.
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
	 * ein Eintrag geliefert (im Fehlerfall mit dem Zustand DATENFEHLER oder SIGNIERFEHLER), damit niemals {@code null} in der Cache-Map landet.
	 *
	 * @param idsSchueler Die IDs der zu verarbeitenden Schüler.
	 *
	 * @return Map von der Schüler-ID auf die zugehörigen QR-Daten.
	 *
	 * @throws ApiOperationException Mit Status 400 bei einem erkennbaren Anmeldefehler am Signierdienst, mit Status 403 bei einer fehlenden Berechtigung
	 *                               zum Signieren, mit Status 500 bei dessen Ausfall oder einem Stapel ohne eine einzige verwertbare Signatur.
	 */
	public Map<Long, SchulbescheinigungQrDaten> erzeuge(final List<Long> idsSchueler) throws ApiOperationException {
		final Map<Long, SchulbescheinigungQrDaten> ergebnis = new HashMap<>();

		// Gemeinsame Schul- und Abschnittsdaten einmalig ermitteln. Ein Fehler dabei propagiert, siehe Klassenkommentar.
		final SchulbescheinigungAusstellungsdaten ausstellungsdaten = ermittleAusstellungsdaten();

		// Je Schüler das XSchule-XML erzeugen. Konnte für keinen Schüler ein XML erzeugt werden, ist ein Aufruf des Signierdienstes überflüssig.
		final Map<Object, byte[]> xmlBytesById = erzeugeXml(idsSchueler, ausstellungsdaten, ergebnis);
		if (xmlBytesById.isEmpty()) {
			return ergebnis;
		}

		// Genau ein Batch-Aufruf an den Signierdienst und anschließend je Schüler die QR-Codes rendern. Ein Dienst- oder Anmeldefehler bricht hier ab.
		rendereQrCodes(idsSchueler, xmlBytesById, signiere(xmlBytesById), ergebnis);
		return ergebnis;
	}

	/**
	 * Ermittelt die für alle Schüler identischen Ausstellungsdaten der Schulbescheinigung. Kein Schritt hier wird gefangen: Die Schulstammdaten sind längst
	 * geladen, ein Scheitern bezeichnet deshalb einen inkonsistenten Zustand - etwa einen fehlenden aktuellen Schuljahresabschnitt. Der Fehler propagiert
	 * und beendet die Ausgabe.
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
	 * @param ergebnis          Die Ergebnis-Map, in die im Fehlerfall die Fehlereinträge eingetragen werden.
	 *
	 * @return Map von der Schüler-ID auf die XML-Bytes der erfolgreich erzeugten Schulbescheinigungen.
	 */
	private Map<Object, byte[]> erzeugeXml(final List<Long> idsSchueler, final SchulbescheinigungAusstellungsdaten ausstellungsdaten,
			final Map<Long, SchulbescheinigungQrDaten> ergebnis) {
		final Map<Object, byte[]> xmlBytesById = new HashMap<>();
		for (final Long id : idsSchueler) {
			final ReportingSchueler schueler = this.reportingContext.repositorySchueler().schueler(id);
			if (schueler == null) {
				// Bewusst still: Ein nicht geladener Schüler ist ausgefiltert oder bereits von der Auswahl als ausgelassener Datensatz gemeldet - eine
				// zweite Meldung mit anderem Schlüssel würde denselben Entfall doppelt zählen. Der Eintrag hält nur den Cache-Vertrag "niemals null".
				ergebnis.put(id, new SchulbescheinigungQrDaten(null, null, SchulbescheinigungSignaturzustand.DATENFEHLER));
				continue;
			}
			try {
				final String xml = SchulbescheinigungXmlFactory.erzeugeXml(schueler,
						ausstellungsdaten.schule(), ausstellungsdaten.ausstellungOrt(), ausstellungsdaten.ausstellungDatum(),
						ausstellungsdaten.bildungsgangEnddatum());
				xmlBytesById.put(id, xml.getBytes(StandardCharsets.UTF_8));
			} catch (final Exception e) {
				// Fehlende oder fehlerhafte Pflichtdaten des Schülers: Die Bescheinigung erscheint mit den Fehlerbildern und dem Datenfehler-Text.
				ergebnis.put(id, new SchulbescheinigungQrDaten(null, null, SchulbescheinigungSignaturzustand.DATENFEHLER));
				this.reportingContext.meldeAusgabeproblem(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.TEILDATEN_FEHLEN,
						ReportingProblemSchluessel.fuer(SchulbescheinigungQrDaten.class, id),
						"Die Schulbescheinigung des Schülers %d wird ohne digitale Signatur ausgestellt: Das XSchule-XML konnte nicht erzeugt werden."
								.formatted(id), e);
			}
		}
		return xmlBytesById;
	}

	/**
	 * Signiert die XSchule-XML-Dokumente aller Schüler in genau einem Batch-Aufruf. Die Schüler-ID dient dabei als
	 * Zuordnungsschlüssel; dies ist möglich, da der Signier-Service diese IDs nicht nach außen gibt.
	 * <p>Ein Fehler des Aufrufs bricht die gesamte Erstellung ab: Ohne Dienst entsteht keine signierte Bescheinigung. Ein am Status der Ursachenkette
	 * erkennbarer Anmeldefehler ergibt {@code 400} - die Diagnose ist fachlich korrekt und weitersagbar -, eine fehlende Berechtigung wird mit
	 * {@code 403} durchgereicht, alles Übrige ergibt {@code 500}. Ein Stapel ohne eine einzige verwertbare Signatur gilt ebenso als Dienstproblem. Die
	 * Meldungen nach außen sind fest formuliert; die Originalursache samt Kette protokolliert die Abschlussgrenze im Server-Log.</p>
	 *
	 * @param xmlBytesById Map von der Schüler-ID auf die zu signierenden XML-Bytes; nie leer.
	 *
	 * @return Die Signaturen je Schüler-ID; mindestens eine davon verwertbar im Sinne von {@link #istErfolgreicheSignatur(Signature)}.
	 *
	 * @throws ApiOperationException Mit Status 400 bei einem erkennbaren Anmeldefehler, mit Status 403 bei einer fehlenden Berechtigung, sonst mit
	 *                               Status 500.
	 */
	private Map<Object, Signature> signiere(final Map<Object, byte[]> xmlBytesById) throws ApiOperationException {
		final Map<Object, Signature> signaturen;
		try {
			signaturen = this.signatureServiceSupplier.get().sign(xmlBytesById);
		} catch (final Exception e) {
			if (istAnmeldefehler(e)) {
				throw new ApiOperationException(Status.BAD_REQUEST, e,
						"### FEHLER: Die Anmeldung am Signierdienst ist fehlgeschlagen; die Schulbescheinigungen werden nicht erstellt. "
								+ "Die hinterlegten Zugangsdaten für den Signierdienst sind zu prüfen.");
			}
			if (statusInUrsachenkette(e, Status.FORBIDDEN)) {
				// Ein 403 kann auch lokal aus der Kompetenzprüfung des Services stammen und ist deshalb kein sicherer Anmeldefehler des Dienstes.
				throw new ApiOperationException(Status.FORBIDDEN, e,
						"### FEHLER: Die Berechtigung zum Erstellen digitaler Signaturen fehlt; die Schulbescheinigungen werden nicht erstellt.");
			}
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e,
					"### FEHLER: Der Signierdienst ist nicht erreichbar oder antwortet fehlerhaft; die Schulbescheinigungen werden nicht erstellt.");
		}
		if (signaturen.values().stream().noneMatch(SchulbescheinigungQrFactory::istErfolgreicheSignatur)) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"### FEHLER: Der Signierdienst hat keine einzige Signatur erstellt; die Schulbescheinigungen werden nicht erstellt.");
		}
		return signaturen;
	}

	/**
	 * Prüft, ob die Ursachenkette des Fehlers einen Anmeldefehler am Signierdienst bezeichnet. Erkannt wird ausschließlich eine statustragende
	 * {@link ApiOperationException} mit {@code UNAUTHORIZED}; auf Meldungstexte wird bewusst nicht geprüft, und ein nicht eindeutig
	 * zuordenbarer Fehler gilt nicht als Anmeldefehler. {@code FORBIDDEN} zählt nicht dazu: Der Service wirft es auch lokal für die fehlende
	 * Benutzerkompetenz; ein Berechtigungsproblem wird mit seinem eigenen Status durchgereicht.
	 *
	 * @param fehler Der Fehler des Signier-Aufrufs.
	 *
	 * @return true, wenn die Kette einen Anmeldefehler trägt, sonst false.
	 */
	private static boolean istAnmeldefehler(final Throwable fehler) {
		return statusInUrsachenkette(fehler, Status.UNAUTHORIZED);
	}

	/**
	 * Prüft, ob die Ursachenkette eine {@link ApiOperationException} mit dem übergebenen Status trägt.
	 *
	 * @param fehler Der Fehler des Signier-Aufrufs.
	 * @param status Der gesuchte Status.
	 *
	 * @return true, wenn die Kette den Status trägt, sonst false.
	 */
	private static boolean statusInUrsachenkette(final Throwable fehler, final Status status) {
		for (Throwable glied = fehler; glied != null; glied = glied.getCause()) {
			if ((glied instanceof final ApiOperationException aoe) && (aoe.getStatus() == status)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Prüft, ob eine Signatur des Dienstes verwertbar ist: Status {@link SignatureStatus#OK} und ein nicht leerer Inhalt. Dasselbe Kriterium entscheidet
	 * über den Gesamtabbruch des Stapels und über den Einzelfehler beim QR-Bau - mit zweierlei Maß könnte ein Stapel aus leeren OK-Antworten den Abbruch
	 * umgehen und ausschließlich unsignierte Bescheinigungen erzeugen.
	 *
	 * @param signatur Die Signatur des Dienstes oder {@code null}.
	 *
	 * @return true, wenn die Signatur verwertbar ist, sonst false.
	 */
	private static boolean istErfolgreicheSignatur(final Signature signatur) {
		return (signatur != null) && (signatur.status() == SignatureStatus.OK) && (signatur.content() != null) && (signatur.content().length > 0);
	}

	/**
	 * Rendert je Schüler die beiden QR-Codes der Schulbescheinigung und trägt sie in {@code ergebnis} ein. Schüler, deren XML zuvor
	 * nicht erzeugt werden konnte, werden übersprungen (ihr Fehlereintrag wurde bereits hinterlegt).
	 * <p>Hier wird der Befund gemeldet: Wer diese Schleife erreicht, hat geladene Ausgangsdaten und ein erzeugtes XML, also ist jeder Fehler ein Signatur-
	 * oder Renderfehler.</p>
	 *
	 * @param idsSchueler      Die IDs der zu verarbeitenden Schüler.
	 * @param xmlBytesById     Map von der Schüler-ID auf die XML-Bytes (für QR1).
	 * @param signaturen       Die Signaturen je Schüler-ID (für QR2).
	 * @param ergebnis         Die Ergebnis-Map, in die die gerenderten QR-Daten eingetragen werden.
	 */
	private void rendereQrCodes(final List<Long> idsSchueler, final Map<Object, byte[]> xmlBytesById,
			final Map<Object, Signature> signaturen, final Map<Long, SchulbescheinigungQrDaten> ergebnis) {
		// Kapazitätsprüfung == Render-Aufruf, identisches EC-Level.
		for (final Long id : idsSchueler) {
			final byte[] xmlBytes = xmlBytesById.get(id);
			if (xmlBytes == null) {
				// XML-Erzeugung ist fehlgeschlagen; Zustand und Meldung sind bereits hinterlegt.
				continue;
			}
			final QrBauergebnis bau = baue(xmlBytes, signaturen.get(id));
			ergebnis.put(id, bau.daten());
			if (!bau.daten().istSigniert()) {
				this.reportingContext.meldeAusgabeproblem(bau.ursache(), ReportingProblemauswirkung.TEILDATEN_FEHLEN,
						ReportingProblemSchluessel.fuer(SchulbescheinigungQrDaten.class, id),
						"Die Schulbescheinigung des Schülers %d wird ohne digitale Signatur ausgestellt: %s".formatted(id, bau.grund()), bau.fehler());
			}
		}
	}


	/**
	 * Rendert die beiden QR-Codes einer Schulbescheinigung als SVG. Nur wenn beide gelingen, ist der Zustand SIGNIERT; in jedem Fehlerfall entfallen beide
	 * Codes gemeinsam, denn ein halber Signaturblock sähe bei der Prüfung wie ein defektes Dokument aus. Ein Signierergebnis mit Status {@code OK}, aber
	 * ohne Inhalt zählt als Signierfehler.
	 * <p>Der Grund und der auslösende Fehler verbleiben im Bauergebnis für die Meldung über die Fassade; in die Vorlagendaten gelangen sie nicht. Der Grund
	 * nennt, welcher der beiden Codes scheiterte - den Inhalt, einen langen Base45-Block, nennt weder er noch die Meldung der Codeerzeugung.</p>
	 *
	 * @param xmlBytes Das XSchule-XML als Bytes (für QR1).
	 * @param signatur Die Signatur des Schülers (für QR2); darf {@code null} sein.
	 *
	 * @return Das Bauergebnis mit den QR-Daten sowie Ursache, Grund und Fehler eines Fehlschlags.
	 */
	private static QrBauergebnis baue(final byte[] xmlBytes, final Signature signatur) {
		if (!istErfolgreicheSignatur(signatur)) {
			return new QrBauergebnis(new SchulbescheinigungQrDaten(null, null, SchulbescheinigungSignaturzustand.SIGNIERFEHLER),
					ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, signaturfehlertext(signatur), null);
		}

		final String qr1Svg;
		try {
			final String qr1Inhalt = SchulbescheinigungQrEinstellungen.PRAEFIX_QR1 + Base45.encode(GZip.encode(xmlBytes));
			qr1Svg = ReportingBarcodeUtils.erzeuge2DCodeQRCode(
					qr1Inhalt, SchulbescheinigungQrEinstellungen.QR_BREITE_MM, SchulbescheinigungQrEinstellungen.QR_HOEHE_MM,
					SchulbescheinigungQrEinstellungen.EC_QR1);
		} catch (final Exception e) {
			return nichtDarstellbar("Der QR-Code mit den Bescheinigungsdaten konnte nicht erzeugt werden.", e);
		}
		final String qr2Svg;
		try {
			final String qr2Inhalt = SchulbescheinigungQrEinstellungen.PRAEFIX_QR2 + Base45.encode(GZip.encode(signatur.content()));
			qr2Svg = ReportingBarcodeUtils.erzeuge2DCodeQRCode(
					qr2Inhalt, SchulbescheinigungQrEinstellungen.QR_BREITE_MM, SchulbescheinigungQrEinstellungen.QR_HOEHE_MM,
					SchulbescheinigungQrEinstellungen.EC_QR2);
		} catch (final Exception e) {
			return nichtDarstellbar("Der QR-Code mit der Signatur konnte nicht erzeugt werden.", e);
		}
		return new QrBauergebnis(new SchulbescheinigungQrDaten(qr1Svg, qr2Svg, SchulbescheinigungSignaturzustand.SIGNIERT), null, null, null);
	}

	/**
	 * Bildet das Bauergebnis eines QR-Codes, der sich nicht darstellen lässt. Beide Codes entfallen gemeinsam; der Grund nennt den gescheiterten.
	 *
	 * @param grund  Der Grund für die Meldung über die Fassade.
	 * @param fehler Der auslösende Fehler.
	 *
	 * @return Das Bauergebnis ohne QR-Daten.
	 */
	private static QrBauergebnis nichtDarstellbar(final String grund, final Exception fehler) {
		return new QrBauergebnis(new SchulbescheinigungQrDaten(null, null, SchulbescheinigungSignaturzustand.SIGNIERFEHLER),
				ReportingProblemursache.NICHT_DARSTELLBAR, grund, fehler);
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
	 * Das Ergebnis des QR-Renderings eines Schülers: die QR-Daten für Cache und Vorlage sowie Ursache, Grund und auslösender Fehler eines Fehlschlags für
	 * die Meldung über die Fassade.
	 *
	 * @param daten   Die QR-Daten des Schülers, nie {@code null}.
	 * @param ursache Die Ursache des Fehlschlags oder {@code null} bei Erfolg.
	 * @param grund   Der Sachverhalt für das Log oder {@code null} bei Erfolg.
	 * @param fehler  Der Fehler, an dem das Rendering gescheitert ist, oder {@code null}.
	 */
	private record QrBauergebnis(SchulbescheinigungQrDaten daten, ReportingProblemursache ursache, String grund, Exception fehler) {
	}

}
