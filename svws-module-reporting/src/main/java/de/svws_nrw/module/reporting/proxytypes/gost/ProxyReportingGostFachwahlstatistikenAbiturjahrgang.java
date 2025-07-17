package de.svws_nrw.module.reporting.proxytypes.gost;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.gost.DataGostAbiturjahrgangFachwahlen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.ReportingGostFachwahlstatistik;
import de.svws_nrw.module.reporting.types.gost.ReportingGostFachwahlstatistikenAbiturjahrgang;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;

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

		try {
			// Hole die Fachwahlstatistiken mithilfe der zugehörigen Data-Klasse
			final DataGostAbiturjahrgangFachwahlen gostAbiturjahrgangFachwahlen =
					new DataGostAbiturjahrgangFachwahlen(reportingRepository.conn(), super.abiturjahr());
			final List<GostStatistikFachwahl> gostFachwahlStatistiken = gostAbiturjahrgangFachwahlen.getFachwahlen().stream().filter(Objects::nonNull).toList();

			// Erstelle die Reporting-Fachwahlstatistiken
			final List<ReportingGostFachwahlstatistik> reportingGostFachwahlstatistiken = new ArrayList<>();
			for (final GostStatistikFachwahl gostFachwahlStatistik : gostFachwahlStatistiken) {
				reportingGostFachwahlstatistiken.add(new ProxyReportingGostFachwahlstatistik(this.reportingRepository, gostFachwahlStatistik));
			}
			setFachwahlstatistiken(reportingGostFachwahlstatistiken);
		} catch (final ApiOperationException e) {
			ReportingExceptionUtils.putStacktraceInLog(
					"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der GOSt-Fachwahlstatistik.", e,
					reportingRepository.logger(), LogLevel.INFO, 0);
		}
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
