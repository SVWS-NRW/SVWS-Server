package de.svws_nrw.module.reporting.proxytypes.gost.kursplanung;

import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.gost.ReportingGostFachwahlstatistikHalbjahr;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungFachwahlstatistik;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungFachwahlstatistik und erweitert die Klasse {@link ReportingGostKursplanungFachwahlstatistik}.
 */
public class ProxyReportingGostKursplanungFachwahlstatistik extends ReportingGostKursplanungFachwahlstatistik {

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKursplanungFachwahlstatistik}.
	 *
	 * @param reportingGostFachwahlstatistik Wahlstatistik für ein Fach der GOSt über alle Halbjahre.
	 * @param ergebnisManager Der Manager des Blockungsergebnisses aus der Kursplanung, zu dem die Fachwahlstatistik gehört.
	 */
	public ProxyReportingGostKursplanungFachwahlstatistik(final ReportingGostFachwahlstatistikHalbjahr reportingGostFachwahlstatistik,
			final GostBlockungsergebnisManager ergebnisManager) {
		super(-1,
				-1,
				-1,
				-1,
				-1,
				reportingGostFachwahlstatistik);

		int kursgroessendifferenzLK = -1;
		int kursgroessendifferenzGK = -1;
		int kursgroessendifferenzZK = -1;
		int kursgroessendifferenzPJK = -1;
		int kursgroessendifferenzVTF = -1;

		final ReportingFach reportingFach = super.reportingGostFachwahlstatistik.fach();

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
