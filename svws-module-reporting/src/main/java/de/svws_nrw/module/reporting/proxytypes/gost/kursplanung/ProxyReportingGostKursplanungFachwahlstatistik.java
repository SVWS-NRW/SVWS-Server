package de.svws_nrw.module.reporting.proxytypes.gost.kursplanung;

import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungFachwahlstatistik;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungFachwahlstatistik und erweitert die Klasse {@link ReportingGostKursplanungFachwahlstatistik}.
 */
public class ProxyReportingGostKursplanungFachwahlstatistik extends ReportingGostKursplanungFachwahlstatistik {


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKursplanungFachwahlstatistik}.
	 *
	 * @param reportingRepository Repository für das Reporting
	 * @param gostHalbjahr Das GostHalbjahr, für die die fachwahlstatistik erstellt wird.
	 * @param gostStatistikFachwahl Wahlstatistik für ein Fach der GOSt über alle Halbjahre.
	 * @param ergebnisManager Der Manager des Blockungsergebnisses aus der Kursplanung, zu dem die Fachwahlstatistik gehört.
	 */
	public ProxyReportingGostKursplanungFachwahlstatistik(final ReportingRepository reportingRepository, final GostHalbjahr gostHalbjahr,
			final GostStatistikFachwahl gostStatistikFachwahl, final GostBlockungsergebnisManager ergebnisManager) {
		super(-1,
				-1,
				-1,
				-1,
				-1,
				null,
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

		// Für die Daten des Faches wird mindestens der Abschnitt EF1 benötigt. Wenn dieser nicht existiert, dann kann die Statistik nicht existieren.
		// Da in der GOSt konstante Fachbedingungen gelten müssen, kann hier die EF1 verwendet werden.
		final ReportingSchuljahresabschnitt abschnittEF1 = reportingRepository.schuljahresabschnitt(gostStatistikFachwahl.abiturjahr - 4, 1);
		final ReportingFach reportingFach = abschnittEF1.fach(gostStatistikFachwahl.id);

		super.fach = reportingFach;

		int kursgroessendifferenzLK = -1;
		int kursgroessendifferenzGK = -1;
		int kursgroessendifferenzZK = -1;
		int kursgroessendifferenzPJK = -1;
		int kursgroessendifferenzVTF = -1;

		try {
			kursgroessendifferenzLK = ergebnisManager.getOfFachOfKursartKursdifferenz(reportingFach.id(), GostKursart.LK.id);
		} catch (@SuppressWarnings("unused") final Exception ignored) {
			// DeveloperNotificationException wird ignoriert. Hier wurde eine Differenz zu einer nicht vorhandenen Fach-Kursart-Kombination abgefragt.
		}
		try {
			kursgroessendifferenzGK = ergebnisManager.getOfFachOfKursartKursdifferenz(reportingFach.id(), GostKursart.GK.id);
		} catch (@SuppressWarnings("unused") final Exception ignored) {
			// DeveloperNotificationException wird ignoriert. Hier wurde eine Differenz zu einer nicht vorhandenen Fach-Kursart-Kombination abgefragt.
		}
		try {
			kursgroessendifferenzZK = ergebnisManager.getOfFachOfKursartKursdifferenz(reportingFach.id(), GostKursart.ZK.id);
		} catch (@SuppressWarnings("unused") final Exception ignored) {
			// DeveloperNotificationException wird ignoriert. Hier wurde eine Differenz zu einer nicht vorhandenen Fach-Kursart-Kombination abgefragt.
		}
		try {
			kursgroessendifferenzPJK = ergebnisManager.getOfFachOfKursartKursdifferenz(reportingFach.id(), GostKursart.PJK.id);
		} catch (@SuppressWarnings("unused") final Exception ignored) {
			// DeveloperNotificationException wird ignoriert. Hier wurde eine Differenz zu einer nicht vorhandenen Fach-Kursart-Kombination abgefragt.
		}
		try {
			kursgroessendifferenzVTF = ergebnisManager.getOfFachOfKursartKursdifferenz(reportingFach.id(), GostKursart.VTF.id);
		} catch (@SuppressWarnings("unused") final Exception ignored) {
			// DeveloperNotificationException wird ignoriert. Hier wurde eine Differenz zu einer nicht vorhandenen Fach-Kursart-Kombination abgefragt.
		}

		super.differenzKursgroessenLK = kursgroessendifferenzLK;
		super.differenzKursgroessenGK = kursgroessendifferenzGK;
		super.differenzKursgroessenZK = kursgroessendifferenzZK;
		super.differenzKursgroessenPJK = kursgroessendifferenzPJK;
		super.differenzKursgroessenVTF = kursgroessendifferenzVTF;
	}
}
