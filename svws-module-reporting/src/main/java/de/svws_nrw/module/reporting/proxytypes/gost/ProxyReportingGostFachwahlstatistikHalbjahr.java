package de.svws_nrw.module.reporting.proxytypes.gost;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.ReportingGostFachwahlstatistikHalbjahr;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostFachwahlstatistikHalbjahr und erweitert die Klasse
 * {@link ReportingGostFachwahlstatistikHalbjahr}.
 */
public class ProxyReportingGostFachwahlstatistikHalbjahr extends ReportingGostFachwahlstatistikHalbjahr {

	/** Repository für das Reporting. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostFachwahlstatistikHalbjahr}.
	 *
	 * @param reportingRepository Repository für das Reporting
	 * @param gostHalbjahr Das GostHalbjahr, für die die Fachwahlstatistik erstellt wird.
	 * @param gostStatistikFachwahl Wahlstatistik für ein Fach der GOSt über alle Halbjahre.
	 */
	public ProxyReportingGostFachwahlstatistikHalbjahr(final ReportingRepository reportingRepository, final GostHalbjahr gostHalbjahr,
			final GostStatistikFachwahl gostStatistikFachwahl) {
		super(gostStatistikFachwahl.abiturjahr,
				null,
				gostHalbjahr,
				gostStatistikFachwahl.fachwahlen[gostHalbjahr.id].wahlenLK,
				gostStatistikFachwahl.fachwahlen[gostHalbjahr.id].wahlenGK,
				gostStatistikFachwahl.fachwahlen[gostHalbjahr.id].wahlenGKMuendlich,
				gostStatistikFachwahl.fachwahlen[gostHalbjahr.id].wahlenGKSchriftlich,
				gostStatistikFachwahl.wahlenAB3,
				gostStatistikFachwahl.wahlenAB4,
				gostStatistikFachwahl.fachwahlen[gostHalbjahr.id].wahlenZK,
				gostStatistikFachwahl.fachwahlen[gostHalbjahr.id].wahlenGK,
				gostStatistikFachwahl.fachwahlen[gostHalbjahr.id].wahlenGK);
		// Hinweis: Die Klasse gostStatistikFachwahl unterscheidet bei der Anzahl der Wahlen nicht zwischen GK, PJK, VTF. Es gibt nur die wahlenGK.

		this.reportingRepository = reportingRepository;

		// Für die Daten des Faches wird mindestens der Abschnitt EF1 benötigt. Wenn dieser nicht existiert, dann kann die Statistik nicht existieren.
		// Da in der GOSt konstante Fachbedingungen gelten müssen, kann hier die EF1 verwendet werden.
		final ReportingSchuljahresabschnitt abschnittEF1 = reportingRepository.schuljahresabschnitt(gostStatistikFachwahl.abiturjahr - 4, 1);
		super.fach = abschnittEF1.fach(gostStatistikFachwahl.id);
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
