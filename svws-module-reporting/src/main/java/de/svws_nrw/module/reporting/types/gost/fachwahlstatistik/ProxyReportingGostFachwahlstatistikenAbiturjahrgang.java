package de.svws_nrw.module.reporting.types.gost.fachwahlstatistik;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostFachwahlstatistikenAbiturjahrgang und erweitert die Klasse
 * {@link ReportingGostFachwahlstatistikenAbiturjahrgang}.
 */
public class ProxyReportingGostFachwahlstatistikenAbiturjahrgang extends ReportingGostFachwahlstatistikenAbiturjahrgang {

	/** Repository für das Reporting. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostFachwahlstatistikenAbiturjahrgang}.
	 *
	 * @param reportingRepository Repository für das Reporting
	 * @param abiturjahr Das Abiturjahr des Jahrgangs, dessen Fachwahlstatistiken enthalten sind.
	 */
	public ProxyReportingGostFachwahlstatistikenAbiturjahrgang(final ReportingRepository reportingRepository, final int abiturjahr) {
		super(abiturjahr, new ArrayList<>());

		this.reportingRepository = reportingRepository;

		// Hole die Fachwahlstatistiken über das GOSt-Repository
		final List<GostStatistikFachwahl> gostFachwahlStatistiken =
				this.reportingRepository.repositoryGost().fachwahlen(super.abiturjahr()).stream().filter(Objects::nonNull).toList();

		// Erstelle die Reporting-Fachwahlstatistiken
		final List<ReportingGostFachwahlstatistik> reportingGostFachwahlstatistiken = new ArrayList<>();
		for (final GostStatistikFachwahl gostFachwahlStatistik : gostFachwahlStatistiken) {
			reportingGostFachwahlstatistiken.add(new ProxyReportingGostFachwahlstatistik(this.reportingRepository, gostFachwahlStatistik));
		}
		setFachwahlstatistiken(reportingGostFachwahlstatistiken);
	}



	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	@JsonIgnore
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}
}
