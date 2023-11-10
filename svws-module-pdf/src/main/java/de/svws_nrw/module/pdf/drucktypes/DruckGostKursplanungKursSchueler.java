package de.svws_nrw.module.pdf.drucktypes;

import de.svws_nrw.core.data.schueler.SchuelerStammdaten;

/**
 *  Die Klasse enthält die Grunddaten eines Schülers für die GOSt-Kursplanung in einer Kursliste.
 *  Sie ist abgeleitet von der Druckklasse DruckSchueler.
 */
public class DruckGostKursplanungKursSchueler extends DruckSchueler {

	/**
	 * Weist die Daten zu den Feldern der Elternklasse zu, wenn ein Elternklassenobjekt übergeben wird.
	 *
	 * @param schuelerStammdaten Daten in Form eines Objektes der Elternklasse
	 */
	public DruckGostKursplanungKursSchueler(final SchuelerStammdaten schuelerStammdaten) {
		super(schuelerStammdaten);
	}


	/** Kursbelegung des Schülers, d. h. schriftlich oder mündlich. */
	public String belegung = "";

	/** Nummer des Abiturfaches, sofern das Fach des Kurses ein Abiturfach des Schülers ist. */
	public String abiturfach = "";

}
