package de.svws_nrw.module.reporting.types.gost.kursplanung;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.gost.fachwahlstatistik.ReportingGostFachwahlstatistikHalbjahr;

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

		final ReportingFach reportingFach = (super.reportingGostFachwahlstatistik == null) ? null : super.reportingGostFachwahlstatistik.fach();

		// Ohne zugeordnetes Fach können keine Kursgrößendifferenzen ermittelt werden; die Werte bleiben auf dem Default (-1) aus dem Super-Konstruktor.
		if (reportingFach == null) {
			return;
		}

		super.differenzKursgroessenLK = kursdifferenzOderRueckfall(ergebnisManager, reportingFach.id(), GostKursart.LK);
		super.differenzKursgroessenGK = kursdifferenzOderRueckfall(ergebnisManager, reportingFach.id(), GostKursart.GK);
		super.differenzKursgroessenZK = kursdifferenzOderRueckfall(ergebnisManager, reportingFach.id(), GostKursart.ZK);
		super.differenzKursgroessenPJK = kursdifferenzOderRueckfall(ergebnisManager, reportingFach.id(), GostKursart.PJK);
		super.differenzKursgroessenVTF = kursdifferenzOderRueckfall(ergebnisManager, reportingFach.id(), GostKursart.VTF);
	}


	/**
	 * Ermittelt die Kursgrößendifferenz zu Fach und Kursart über den Ergebnis-Manager. Eine nicht vorhandene Fach-Kursart-Kombination meldet der Manager
	 * mit einer {@link DeveloperNotificationException}; allein sie wird in den Rückfallwert -1 übersetzt, den die Vorlage als Platzhalter zeigt. Jeder
	 * andere Fehler propagiert - er wäre sonst still als fehlende Kombination gedeutet.
	 *
	 * @param ergebnisManager Der Manager des Blockungsergebnisses.
	 * @param idFach          Die ID des Fachs.
	 * @param kursart         Die Kursart, deren Differenz ermittelt werden soll.
	 *
	 * @return Die Kursgrößendifferenz oder -1, falls die Fach-Kursart-Kombination im Ergebnis nicht vorkommt.
	 */
	private static int kursdifferenzOderRueckfall(final GostBlockungsergebnisManager ergebnisManager, final long idFach, final GostKursart kursart) {
		try {
			return ergebnisManager.getOfFachOfKursartKursdifferenz(idFach, kursart.id);
		} catch (@SuppressWarnings("unused") final DeveloperNotificationException e) {
			return -1;
		}
	}
}
