package de.svws_nrw.module.reporting.proxytypes.schueler.sprachen;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.asd.types.fach.Sprachreferenzniveau;
import de.svws_nrw.module.reporting.proxytypes.fach.ProxyReportingStatistikFach;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.schueler.sprachen.ReportingSchuelerSprachbelegung;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ SchuelerSprachbelegung und erweitert die Klasse {@link ReportingSchuelerSprachbelegung}.
 */
public class ProxyReportingSchuelerSprachbelegung extends ReportingSchuelerSprachbelegung {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchuelerSprachbelegung}.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param sprachbelegung Daten-Objekt der Fachbelegungen aus der Datenbank
	 */
	public ProxyReportingSchuelerSprachbelegung(final ReportingRepository reportingRepository, final Sprachbelegung sprachbelegung) {
		super(sprachbelegung.belegungBisAbschnitt,
				sprachbelegung.belegungBisJahrgang,
				sprachbelegung.belegungVonAbschnitt,
				sprachbelegung.belegungVonJahrgang,
				sprachbelegung.hatGraecum,
				sprachbelegung.hatHebraicum,
				sprachbelegung.hatKleinesLatinum,
				sprachbelegung.hatLatinum,
				null,
				sprachbelegung.reihenfolge,
				ersetzeNullBlankTrim(sprachbelegung.sprache),
				null
		);
		this.reportingRepository = reportingRepository;
		if (sprachbelegung.referenzniveau != null)
			super.referenzniveau = Sprachreferenzniveau.data().getWertBySchluessel(sprachbelegung.referenzniveau);
		super.statistikfach =
				new ProxyReportingStatistikFach(sprachbelegung.sprache, this.reportingRepository.aktuellerSchuljahresabschnitt().schuljahr(), false);
	}


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}
}
