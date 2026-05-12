package de.svws_nrw.module.reporting.types.schueler.schulbesuch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.core.data.kataloge.KatalogEntlassgrund;
import de.svws_nrw.core.data.kataloge.SchulEintrag;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.schule.ProxyReportingSchulkatalogEintragNRW;

/**
 * Proxy-Klasse für die Darstellung von Schulbesuchsdaten eines Schülers für das Reporting.
 * Diese Klasse erweitert die ReportingSchuelerSchulbesuch und wird über die Übergabe
 * eines SchuelerSchulbesuchsdaten-Objektes gefüllt.
 */
public class ProxyReportingSchuelerSchulbesuch extends ReportingSchuelerSchulbesuch {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;

	/**
	 * Erstellt eine neue Proxy-Instanz aus einem SchuelerSchulbesuchsdaten-Objekt.
	 *
	 * @param reportingContext Repository für das Reporting.
	 * @param schulbesuchsdaten Das SchuelerSchulbesuchsdaten-Objekt, aus dem die Proxy-Instanz erstellt wird
	 */
	public ProxyReportingSchuelerSchulbesuch(final ReportingContext reportingContext, final SchuelerSchulbesuchsdaten schulbesuchsdaten) {
		super(
				createReportingSchulkatalogEintragNRW(reportingContext, schulbesuchsdaten.idVorherigeSchule),
				ersetzeNullBlankTrim(schulbesuchsdaten.vorigeAllgHerkunft),
				ersetzeNullBlankTrim(schulbesuchsdaten.vorigeEntlassdatum),
				ersetzeNullBlankTrim(schulbesuchsdaten.vorigeEntlassjahrgang),
				ersetzeNullBlankTrim(schulbesuchsdaten.vorigeArtLetzteVersetzung),
				ersetzeNullBlankTrim(schulbesuchsdaten.vorigeBemerkung),
				createEndlassgrund(reportingContext, schulbesuchsdaten.vorigeEntlassgrundID),
				schulbesuchsdaten.vorigeAbschlussartID,
				ersetzeNullBlankTrim(schulbesuchsdaten.entlassungDatum),
				schulbesuchsdaten.idEntlassjahrgang,
				createEndlassgrund(reportingContext, schulbesuchsdaten.entlassungGrundID),
				schulbesuchsdaten.entlassungAbschlussartID,
				createReportingSchulkatalogEintragNRW(reportingContext, schulbesuchsdaten.idAufnehmendeSchule),
				ersetzeNullBlankTrim(schulbesuchsdaten.aufnehmendWechseldatum),
				schulbesuchsdaten.aufnehmendBestaetigt,
				schulbesuchsdaten.grundschuleEinschulungsjahr,
				schulbesuchsdaten.grundschuleEinschulungsartID,
				schulbesuchsdaten.idGrundschuleJahreEingangsphase,
				schulbesuchsdaten.idGrundschuleUebergangsempfehlung,
				schulbesuchsdaten.sekIWechsel,
				ersetzeNullBlankTrim(schulbesuchsdaten.sekIErsteSchulform),
				schulbesuchsdaten.sekIIWechsel,
				schulbesuchsdaten.idDauerKindergartenbesuch,
				schulbesuchsdaten.idKindergarten,
				schulbesuchsdaten.verpflichtungSprachfoerderkurs,
				schulbesuchsdaten.teilnahmeSprachfoerderkurs,
				convertAlleSchulen(reportingContext, schulbesuchsdaten.alleSchulen)
		);
		this.reportingContext = reportingContext;
	}

	private static ProxyReportingSchulkatalogEintragNRW createReportingSchulkatalogEintragNRW(final ReportingContext reportingContext,
			final Long idSchule) {
		if (idSchule == null) {
			return null;
		}

		final SchulEintrag schulEintrag = reportingContext.repositoryKataloge().schulen().get(idSchule);
		if (schulEintrag == null) {
			return null;
		}

		return new ProxyReportingSchulkatalogEintragNRW(reportingContext, schulEintrag);
	}

	private static KatalogEntlassgrund createEndlassgrund(final ReportingContext reportingContext, final Long idEntlassgrund) {
		if (idEntlassgrund == null) {
			return null;
		}

		return reportingContext.repositoryKataloge().entlassgruende().get(idEntlassgrund);
	}

	/**
	 * Konvertiert die Liste der SchuelerSchulbesuchSchule-Objekte in ReportingSchuelerSchulbesuchSchule-Objekte.
	 *
	 * @param reportingContext Repository für das Reporting.
	 * @param alleSchulen Die Liste der SchuelerSchulbesuchSchule-Objekte
	 * @return Eine Liste von ReportingSchuelerSchulbesuchSchule-Objekten
	 */
	private static List<ReportingSchuelerSchulbesuchSchule> convertAlleSchulen(final ReportingContext reportingContext,
			final List<SchuelerSchulbesuchSchule> alleSchulen) {
		if ((alleSchulen == null) || alleSchulen.isEmpty()) {
			return new ArrayList<>();
		}

		return alleSchulen.stream()
				.map(schule -> new ProxyReportingSchuelerSchulbesuchSchule(reportingContext, schule)).collect(Collectors.toList());
	}

	// ##### Getter #####

	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingContext reportingContext() {
		return reportingContext;
	}
}
