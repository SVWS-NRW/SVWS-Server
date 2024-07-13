package de.svws_nrw.module.reporting.types.schueler.gost.kursplanung;

import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungKurs;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungKursbelegung.</p>
 * <p>Sie enthält die Daten zur Belegung eines Kurses durch einen Schüler im Rahmen der Kursplanung der gymnasialen Oberstufe.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 *  einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingSchuelerGostKursplanungKursbelegung {

	/** Nummer des Abiturfaches, sofern das Fach des Kurses ein Abiturfach des Schülers ist. */
	protected String abiturfach;

	/** Gibt an, ob der Kurs schriftlich belegt ist. */
	protected boolean istSchriftlich;

	/** Der Kurs, der vom Schüler belegt wird. */
	protected ReportingGostKursplanungKurs kurs;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param abiturfach			Abiturfach, falls das Fach des Kurses Abiturfach ist.
	 * @param istSchriftlich		Angabe, ob der Kurs schriftlich belegt ist.
	 * @param kurs 					Der Kurs, der vom Schüler belegt wird.
	 */
	public ReportingSchuelerGostKursplanungKursbelegung(final String abiturfach, final boolean istSchriftlich, final ReportingGostKursplanungKurs kurs) {
		this.abiturfach = abiturfach;
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
