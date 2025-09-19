package de.svws_nrw.module.reporting.proxytypes.schueler.schulbesuch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.core.data.kataloge.KatalogEntlassgrund;
import de.svws_nrw.core.data.kataloge.SchulEintrag;
import de.svws_nrw.module.reporting.proxytypes.schule.ProxyReportingSchulkatalogEintragNRW;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.schueler.schulbesuch.ReportingSchuelerSchulbesuch;
import de.svws_nrw.module.reporting.types.schueler.schulbesuch.ReportingSchuelerSchulbesuchSchule;

/**
 * Proxy-Klasse für die Darstellung von Schulbesuchsdaten eines Schülers für das Reporting.
 * Diese Klasse erweitert die ReportingSchuelerSchulbesuch und wird über die Übergabe
 * eines SchuelerSchulbesuchsdaten-Objektes gefüllt.
 */
public class ProxyReportingSchuelerSchulbesuch extends ReportingSchuelerSchulbesuch {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Erstellt eine neue Proxy-Instanz aus einem SchuelerSchulbesuchsdaten-Objekt.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param schulbesuchsdaten Das SchuelerSchulbesuchsdaten-Objekt, aus dem die Proxy-Instanz erstellt wird
	 */
	public ProxyReportingSchuelerSchulbesuch(final ReportingRepository reportingRepository, final SchuelerSchulbesuchsdaten schulbesuchsdaten) {
		super(
				createReportingSchulkatalogEintragNRW(reportingRepository, schulbesuchsdaten.idVorherigeSchule),
				ersetzeNullBlankTrim(schulbesuchsdaten.vorigeAllgHerkunft),
				ersetzeNullBlankTrim(schulbesuchsdaten.vorigeEntlassdatum),
				ersetzeNullBlankTrim(schulbesuchsdaten.vorigeEntlassjahrgang),
				ersetzeNullBlankTrim(schulbesuchsdaten.vorigeArtLetzteVersetzung),
				ersetzeNullBlankTrim(schulbesuchsdaten.vorigeBemerkung),
				createEndlassgrund(reportingRepository, schulbesuchsdaten.vorigeEntlassgrundID),
				schulbesuchsdaten.vorigeAbschlussartID,
				ersetzeNullBlankTrim(schulbesuchsdaten.entlassungDatum),
				ersetzeNullBlankTrim(schulbesuchsdaten.entlassungJahrgang),
				createEndlassgrund(reportingRepository, schulbesuchsdaten.entlassungGrundID),
				schulbesuchsdaten.entlassungAbschlussartID,
				createReportingSchulkatalogEintragNRW(reportingRepository, schulbesuchsdaten.idAufnehmendeSchule),
				ersetzeNullBlankTrim(schulbesuchsdaten.aufnehmendWechseldatum),
				schulbesuchsdaten.aufnehmendBestaetigt,
				schulbesuchsdaten.grundschuleEinschulungsjahr,
				schulbesuchsdaten.grundschuleEinschulungsartID,
				schulbesuchsdaten.idGrundschuleJahreEingangsphase,
				ersetzeNullBlankTrim(schulbesuchsdaten.kuerzelGrundschuleUebergangsempfehlung),
				schulbesuchsdaten.sekIWechsel,
				ersetzeNullBlankTrim(schulbesuchsdaten.sekIErsteSchulform),
				schulbesuchsdaten.sekIIWechsel,
				schulbesuchsdaten.idDauerKindergartenbesuch,
				schulbesuchsdaten.idKindergarten,
				schulbesuchsdaten.verpflichtungSprachfoerderkurs,
				schulbesuchsdaten.teilnahmeSprachfoerderkurs,
				convertAlleSchulen(reportingRepository, schulbesuchsdaten.alleSchulen)
		);
		this.reportingRepository = reportingRepository;
	}

	private static ProxyReportingSchulkatalogEintragNRW createReportingSchulkatalogEintragNRW(final ReportingRepository reportingRepository,
			final Long idSchule) {
		if (idSchule == null)
			return null;

		final SchulEintrag schulEintrag = reportingRepository.katalogSchulen().get(idSchule);
		if (schulEintrag == null)
			return null;

		return new ProxyReportingSchulkatalogEintragNRW(reportingRepository, schulEintrag);
	}

	private static KatalogEntlassgrund createEndlassgrund(final ReportingRepository reportingRepository, final Long idEntlassgrund) {
		if (idEntlassgrund == null)
			return null;

		return reportingRepository.katalogEntlassgruende().get(idEntlassgrund);
	}

	/**
	 * Konvertiert die Liste der SchuelerSchulbesuchSchule-Objekte in ReportingSchuelerSchulbesuchSchule-Objekte.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param alleSchulen Die Liste der SchuelerSchulbesuchSchule-Objekte
	 * @return Eine Liste von ReportingSchuelerSchulbesuchSchule-Objekten
	 */
	private static List<ReportingSchuelerSchulbesuchSchule> convertAlleSchulen(final ReportingRepository reportingRepository,
			final List<SchuelerSchulbesuchSchule> alleSchulen) {
		if ((alleSchulen == null) || alleSchulen.isEmpty()) {
			return new ArrayList<>();
		}

		return alleSchulen.stream()
				.map(schule -> new ProxyReportingSchuelerSchulbesuchSchule(reportingRepository, schule)).collect(Collectors.toList());
	}

	// ##### Getter #####

	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}
}
