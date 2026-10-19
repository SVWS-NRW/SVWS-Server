package de.svws_nrw.module.reporting.types.schueler.gost.kursplanung;

import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungKurs;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungKursbelegung und erweitert die Klasse {@link ReportingSchuelerGostKursplanungKursbelegung}.
 */
public class ProxyReportingSchuelerGostKursplanungKursbelegung extends ReportingSchuelerGostKursplanungKursbelegung {

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchuelerGostKursplanungKursbelegung}.
	 *
	 * @param abiturfach			Abiturfach, falls das Fach des Kurses Abiturfach ist.
	 * @param hatGkl				Angabe, ob der Schüler in diesem Kurs einen GKL gewählt hat.
	 * @param hatGueltigeFachwahl 	Angabe, ob die Kursbelegung eine gültige Fachwahl im Abiturjahrgang hat.
	 * @param istSchriftlich		Angabe, ob der Kurs schriftlich belegt ist.
	 * @param kurs 					Der Kurs, der vom Schüler belegt wird.
	 */
	public ProxyReportingSchuelerGostKursplanungKursbelegung(final String abiturfach, final boolean hatGkl, final boolean hatGueltigeFachwahl,
			final boolean istSchriftlich, final ReportingGostKursplanungKurs kurs) {
		super(ersetzeNullBlankTrim(abiturfach), hatGkl, hatGueltigeFachwahl, istSchriftlich, kurs);
	}
}
