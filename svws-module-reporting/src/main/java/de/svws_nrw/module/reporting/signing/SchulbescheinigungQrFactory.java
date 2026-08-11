package de.svws_nrw.module.reporting.signing;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import de.svws_nrw.base.compression.GZip;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.utils.encoding.Base45;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.schule.ReportingSchule;
import de.svws_nrw.module.reporting.utils.ReportingBarcodeUtils;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import de.svws_nrw.module.reporting.utils.ReportingUhr;
import de.svws_nrw.oauth.OAuthHttpClientFactory;
import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.benutzer.BenutzerServiceFactory;
import de.svws_nrw.service.schule.SchuleServiceFactory;
import de.svws_nrw.service.signature.Signature;
import de.svws_nrw.service.signature.SignatureService;
import de.svws_nrw.service.signature.SignatureServiceFactory;
import de.svws_nrw.service.signature.SignatureStatus;

/**
 * Erzeugt die gerenderten QR-Codes der signierten Schulbescheinigung. Die mehrstufige Pipeline
 * (Ausstellungsdaten ermitteln, XSchule-XML erzeugen, in einem einzigen Batch signieren, QR-Codes als SVG rendern)
 * ist hier gebündelt, damit das aufrufende Repository nur das Caching der Ergebnisse verantwortet.
 * Alle Fehler werden abgefangen und je Schüler als Fehlermeldung in den {@link SchulbescheinigungQrDaten} abgelegt, sodass
 * niemals {@code null} zurückgegeben wird.
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

		// Gemeinsame Schul- und Abschnittsdaten einmalig ermitteln. Schlägt dies fehl, erhalten alle Schüler eine Fehlermeldung.
		final SchulbescheinigungAusstellungsdaten ausstellungsdaten = ermittleAusstellungsdaten(idsSchueler, ergebnis);
		if (ausstellungsdaten == null) {
			return ergebnis;
		}

		// Je Schüler das XSchule-XML erzeugen. Konnte für keinen Schüler ein XML erzeugt werden, ist ein Aufruf des Signierdienstes überflüssig.
		final Map<Object, byte[]> xmlBytesById = erzeugeXml(idsSchueler, ausstellungsdaten, ergebnis);
		if (xmlBytesById.isEmpty()) {
			return ergebnis;
		}

		// Genau ein Batch-Aufruf an den Signierdienst und anschließend je Schüler die QR-Codes rendern.
		final Map<Object, Signature> signaturen = signiere(xmlBytesById);
		rendereQrCodes(idsSchueler, xmlBytesById, signaturen, ergebnis);
		return ergebnis;
	}

	/**
	 * Ermittelt die für alle Schüler identischen Ausstellungsdaten der Schulbescheinigung. Schlägt dies fehl, wird für jeden Schüler
	 * ein Fehlereintrag in {@code ergebnis} hinterlegt und {@code null} zurückgegeben.
	 *
	 * @param idsSchueler Die IDs der zu verarbeitenden Schüler (für die Fehlerbehandlung).
	 * @param ergebnis    Die Ergebnis-Map, in die im Fehlerfall die Fehlermeldungen eingetragen werden.
	 *
	 * @return Die Ausstellungsdaten oder {@code null}, falls sie nicht ermittelt werden konnten.
	 */
	private SchulbescheinigungAusstellungsdaten ermittleAusstellungsdaten(final List<Long> idsSchueler,
			final Map<Long, SchulbescheinigungQrDaten> ergebnis) {
		try {
			final ReportingSchule schule = this.reportingContext.repositorySchule().schule();
			final String bildungsgangEnddatum = (schule.aktuellerSchuljahresabschnitt().schuljahr() + 1) + "-07-31";
			return new SchulbescheinigungAusstellungsdaten(schule, schule.ort(), LocalDate.now(this.clock).toString(), bildungsgangEnddatum);
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					"INFO: Schul- bzw. Abschnittsdaten für die Schulbescheinigung konnten nicht ermittelt werden.", e,
					this.reportingContext.logger(), LogLevel.INFO, 0);
			for (final Long id : idsSchueler) {
				ergebnis.put(id, new SchulbescheinigungQrDaten(null, null,
						"Schul- bzw. Abschnittsdaten konnten nicht ermittelt werden: " + e.getMessage()));
			}
			return null;
		}
	}

	/**
	 * Erzeugt je Schüler das XSchule-XML und sammelt es unter der Schüler-ID als zu signierende Daten. Schüler, deren XML nicht
	 * erzeugt werden konnte, erhalten einen Fehlereintrag in {@code ergebnis} und fehlen in der Rückgabe.
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
			try {
				final String xml = SchulbescheinigungXmlFactory.erzeugeXml(this.reportingContext.repositorySchueler().schueler(id),
						ausstellungsdaten.schule(), ausstellungsdaten.ausstellungOrt(), ausstellungsdaten.ausstellungDatum(),
						ausstellungsdaten.bildungsgangEnddatum());
				xmlBytesById.put(id, xml.getBytes(StandardCharsets.UTF_8));
			} catch (final Exception e) {
				ergebnis.put(id, new SchulbescheinigungQrDaten(null, null, "Die Schulbescheinigung konnte nicht erzeugt werden: " + e.getMessage()));
			}
		}
		return xmlBytesById;
	}

	/**
	 * Signiert die XSchule-XML-Dokumente aller Schüler in genau einem Batch-Aufruf. Die Schüler-ID dient dabei als
	 * Zuordnungsschlüssel; dies ist möglich, da der Signier-Service diese IDs nicht nach außen gibt. Ist der Signierdienst nicht
	 * erreichbar, wird für jeden Schüler eine Fehler-Signatur erzeugt, sodass die Render-Phase die einzige Stelle bleibt, die die
	 * Ergebnis-Map befüllt.
	 *
	 * @param xmlBytesById Map von der Schüler-ID auf die zu signierenden XML-Bytes.
	 *
	 * @return Map von der Schüler-ID auf die zugehörige Signatur (bei Dienstausfall mit Status {@link SignatureStatus#ERROR}).
	 */
	private Map<Object, Signature> signiere(final Map<Object, byte[]> xmlBytesById) {
		try {
			return this.signatureServiceSupplier.get().sign(xmlBytesById);
		} catch (final Exception e) {
			ReportingExceptionUtils.logException("INFO: Der Signierdienst für die Schulbescheinigung ist nicht erreichbar.", e,
					this.reportingContext.logger(), LogLevel.INFO, 0);
			final Signature fehlerSignatur = new Signature(null, SignatureStatus.ERROR, "Der Signierdienst ist nicht erreichbar: " + e.getMessage());
			final Map<Object, Signature> signaturen = new HashMap<>();
			for (final Object id : xmlBytesById.keySet()) {
				signaturen.put(id, fehlerSignatur);
			}
			return signaturen;
		}
	}

	/**
	 * Rendert je Schüler die beiden QR-Codes der Schulbescheinigung und trägt sie in {@code ergebnis} ein. Schüler, deren XML zuvor
	 * nicht erzeugt werden konnte, werden übersprungen (ihre Fehlermeldung wurde bereits hinterlegt).
	 *
	 * @param idsSchueler  Die IDs der zu verarbeitenden Schüler.
	 * @param xmlBytesById Map von der Schüler-ID auf die XML-Bytes (für QR1).
	 * @param signaturen   Map von der Schüler-ID auf die Signatur (für QR2).
	 * @param ergebnis     Die Ergebnis-Map, in die die gerenderten QR-Daten eingetragen werden.
	 */
	private static void rendereQrCodes(final List<Long> idsSchueler, final Map<Object, byte[]> xmlBytesById,
			final Map<Object, Signature> signaturen, final Map<Long, SchulbescheinigungQrDaten> ergebnis) {
		// Kapazitätsprüfung == Render-Aufruf, identisches EC-Level.
		for (final Long id : idsSchueler) {
			final byte[] xmlBytes = xmlBytesById.get(id);
			if (xmlBytes == null) {
				// XML-Erzeugung ist fehlgeschlagen, die Fehlermeldung wurde bereits hinterlegt.
				continue;
			}
			ergebnis.put(id, baue(xmlBytes, signaturen.get(id)));
		}
	}

	/**
	 * Rendert die beiden QR-Codes einer Schulbescheinigung als SVG. QR1 (Inhalt) wird immer erzeugt; QR2 (Signatur) nur bei
	 * erfolgreicher Signierung. Render-Fehler (z. B. Kapazitätsüberschreitung) werden gefangen und als Fehlermeldung abgelegt.
	 *
	 * @param xmlBytes Das signierte XSchule-XML als Bytes (für QR1).
	 * @param signatur Die Signatur des Schülers (für QR2); darf {@code null} sein.
	 *
	 * @return Die QR-Daten mit beiden SVGs (Erfolg) oder mit gesetzter Fehlermeldung.
	 */
	private static SchulbescheinigungQrDaten baue(final byte[] xmlBytes, final Signature signatur) {
		final String qr1Svg;
		try {
			final String qr1Inhalt = SchulbescheinigungQrEinstellungen.PRAEFIX_QR1 + Base45.encode(GZip.encode(xmlBytes));
			qr1Svg = ReportingBarcodeUtils.erzeuge2DCodeQRCode(
					qr1Inhalt, SchulbescheinigungQrEinstellungen.QR_BREITE_MM, SchulbescheinigungQrEinstellungen.QR_HOEHE_MM,
					SchulbescheinigungQrEinstellungen.EC_QR1);
		} catch (final Exception e) {
			return new SchulbescheinigungQrDaten(null, null, "Der Inhalt-QR-Code konnte nicht erzeugt werden: " + e.getMessage());
		}

		if ((signatur == null) || (signatur.status() != SignatureStatus.OK)) {
			final String fehler = (signatur == null) ? "Es wurde kein Signierergebnis geliefert." : signatur.errorMessage();
			return new SchulbescheinigungQrDaten(qr1Svg, null, fehler);
		}

		try {
			final String qr2Inhalt = SchulbescheinigungQrEinstellungen.PRAEFIX_QR2 + Base45.encode(GZip.encode(signatur.content()));
			final String qr2Svg = ReportingBarcodeUtils.erzeuge2DCodeQRCode(
					qr2Inhalt, SchulbescheinigungQrEinstellungen.QR_BREITE_MM, SchulbescheinigungQrEinstellungen.QR_HOEHE_MM,
					SchulbescheinigungQrEinstellungen.EC_QR2);
			return new SchulbescheinigungQrDaten(qr1Svg, qr2Svg, null);
		} catch (final Exception e) {
			return new SchulbescheinigungQrDaten(qr1Svg, null, "Der Signatur-QR-Code konnte nicht erzeugt werden: " + e.getMessage());
		}
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
				SchuleServiceFactory.getNewInstance(EigeneSchuleRepositoryFactory.getNewInstance()),
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

}
