package de.svws_nrw.module.pdf.reptypes.gost.kursplanung;

import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.module.pdf.reptypes.RepSchueler;

/**
 *  Die Klasse enthält die Grunddaten eines Schülers für die GOSt-Kursplanung in einer Kursliste.
 *  Sie ist abgeleitet von der Druckklasse DruckSchueler.
 */
public class RepGostKursplanungKursSchueler extends RepSchueler {

	/** Gibt an, ob der Kurs schriftlich belegt ist. */
	public boolean istSchriftlich;

	/** Nummer des Abiturfaches, sofern das Fach des Kurses ein Abiturfach des Schülers ist. */
	public String abiturfach;


	/**
	 * Erstellt einen neuen Kursschüler für einen Kurs der Kursplanung
	 *
	 * @param schuelerStammdaten	Stammdaten des Schülers
	 * @param istSchriftlich		Angabe, ob der Kurs schriftlich belegt ist.
	 * @param abiturfach			Abiturfach, falls das Fach des Kurses Abiturfach ist.
	 */
	public RepGostKursplanungKursSchueler(final SchuelerStammdaten schuelerStammdaten, final boolean istSchriftlich, final String abiturfach) {
		super(schuelerStammdaten);
		this.istSchriftlich = istSchriftlich;
		this.abiturfach = abiturfach;
	}
}
