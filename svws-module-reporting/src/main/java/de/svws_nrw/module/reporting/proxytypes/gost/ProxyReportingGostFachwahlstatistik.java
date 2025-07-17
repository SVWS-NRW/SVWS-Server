package de.svws_nrw.module.reporting.proxytypes.gost;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.ReportingGostFachwahlstatistik;
import de.svws_nrw.module.reporting.types.gost.ReportingGostFachwahlstatistikHalbjahr;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostFachwahlstatistik und erweitert die Klasse
 * {@link ReportingGostFachwahlstatistikHalbjahr}.
 */
public class ProxyReportingGostFachwahlstatistik extends ReportingGostFachwahlstatistik {

	/** Repository für das Reporting. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostFachwahlstatistik}.
	 *
	 * @param reportingRepository Repository für das Reporting
	 * @param gostStatistikFachwahl Wahlstatistik für ein Fach der GOSt über alle Halbjahre.
	 */
	public ProxyReportingGostFachwahlstatistik(final ReportingRepository reportingRepository, final GostStatistikFachwahl gostStatistikFachwahl) {
		super(gostStatistikFachwahl.abiturjahr,
				null,
				new ArrayList<>());

		this.reportingRepository = reportingRepository;

		// Für die Daten des Faches wird mindestens der Abschnitt EF1 benötigt. Wenn dieser nicht existiert, dann kann die Statistik nicht existieren.
		// Da in der GOSt konstante Fachbedingungen gelten müssen, kann hier die EF1 verwendet werden.
		final ReportingSchuljahresabschnitt abschnittEF1 = reportingRepository.schuljahresabschnitt(gostStatistikFachwahl.abiturjahr - 4, 1);
		super.fach = abschnittEF1.fach(gostStatistikFachwahl.id);

		final List<ReportingGostFachwahlstatistikHalbjahr> reportingGostFachwahlstatistiken = new ArrayList<>();
		for (final GostHalbjahr gostHalbjahr : GostHalbjahr.values()) {
			reportingGostFachwahlstatistiken.add(new ProxyReportingGostFachwahlstatistikHalbjahr(this.reportingRepository, gostHalbjahr,
					gostStatistikFachwahl));
		}
		super.setFachwahlstatistikHalbjahre(reportingGostFachwahlstatistiken);
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
