package de.svws_nrw.module.reporting.types.schueler.gost.kursplanung;

import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungKurs;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungKurs.
 */
public class ReportingSchuelerGostKursplanungKursbelegung {

	/** Nummer des Abiturfaches, sofern das Fach des Kurses ein Abiturfach des Schülers ist. */
	protected String abiturfach;

	/** Gibt an, ob die Kursbelegung eine gültige Fachwahl im Abiturjahrgang hat. */
	protected boolean hatGueltigeFachwahl;

	/** Gibt an, ob der Kurs schriftlich belegt ist. */
	protected boolean istSchriftlich;

	/** Der Kurs, der vom Schüler belegt wird. */
	protected ReportingGostKursplanungKurs kurs;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param abiturfach			Abiturfach, falls das Fach des Kurses Abiturfach ist.
	 * @param hatGueltigeFachwahl 	Angabe, ob die Kursbelegung eine gültige Fachwahl im Abiturjahrgang hat.
	 * @param istSchriftlich		Angabe, ob der Kurs schriftlich belegt ist.
	 * @param kurs 					Der Kurs, der vom Schüler belegt wird.
	 */
	public ReportingSchuelerGostKursplanungKursbelegung(final String abiturfach, final boolean hatGueltigeFachwahl, final boolean istSchriftlich,
			final ReportingGostKursplanungKurs kurs) {
		this.abiturfach = abiturfach;
		this.hatGueltigeFachwahl = hatGueltigeFachwahl;
		this.istSchriftlich = istSchriftlich;
		this.kurs = kurs;
	}



	// ##### Getter #####

	/**
	 * Nummer des Abiturfaches, sofern das Fach des Kurses ein Abiturfach des Schülers ist.
	 * @return Inhalt des Feldes abiturfach
	 */
	public String abiturfach() {
		return abiturfach;
	}

	/**
	 * Gibt an, ob die Kursbelegung eine gültige Fachwahl im Abiturjahrgang hat.
	 * @return Inhalt des Feldes hatGueltigeFachwahl
	 */
	public boolean hatGueltigeFachwahl() {
		return hatGueltigeFachwahl;
	}

	/**
	 * Gibt an, ob der Kurs schriftlich belegt ist.
	 * @return Inhalt des Feldes istSchriftlich
	 */
	public boolean istSchriftlich() {
		return istSchriftlich;
	}

	/**
	 * Der Kurs, der vom Schüler belegt wird.
	 * @return Inhalt des Feldes kurs
	 */
	public ReportingGostKursplanungKurs kurs() {
		return kurs;
	}

}
