package de.svws_nrw.module.reporting.types.schueler.sprachen;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.asd.types.fach.Sprachreferenzniveau;
import de.svws_nrw.module.reporting.types.fach.ProxyReportingStatistikFach;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ SchuelerSprachbelegung und erweitert die Klasse {@link ReportingSchuelerSprachbelegung}.
 */
public class ProxyReportingSchuelerSprachbelegung extends ReportingSchuelerSprachbelegung {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchuelerSprachbelegung}.
	 *
	 * @param reportingContext Repository für das Reporting.
	 * @param sprachbelegung Daten-Objekt der Fachbelegungen aus der Datenbank
	 */
	public ProxyReportingSchuelerSprachbelegung(final ReportingContext reportingContext, final Sprachbelegung sprachbelegung) {
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
		this.reportingContext = reportingContext;
		if (sprachbelegung.referenzniveau != null) {
			super.referenzniveau = Sprachreferenzniveau.data().getWertBySchluessel(sprachbelegung.referenzniveau);
		}
		super.statistikfach =
				new ProxyReportingStatistikFach(sprachbelegung.sprache, this.reportingContext.repositorySchule().aktuellerSchuljahresabschnitt().schuljahr(), false);
	}


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingContext reportingContext() {
		return reportingContext;
	}
}
