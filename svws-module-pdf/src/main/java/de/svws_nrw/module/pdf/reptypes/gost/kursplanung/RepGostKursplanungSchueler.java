package de.svws_nrw.module.pdf.reptypes.gost.kursplanung;

import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.module.pdf.reptypes.RepSchueler;

import java.util.List;

/**
 * Die Klasse enthält die Grunddaten eines Schülers und dessen Daten für die GOSt-Laufbahnplanung.
 * Sie ist abgeleitet von der Druckklasse DruckSchueler.
 */

public class RepGostKursplanungSchueler extends RepSchueler {

	/** Eine Liste vom Typ Kurse, die alle Kurse des Schülers und deren Daten enthält. */
	public List<RepGostKursplanungSchuelerKurs> Kurse;


	/**
	 * Erstellt einen Schüler zum Bereich der Kursplanung
	 *
	 * @param schuelerStammdaten	Stammdaten des Schülers
	 * @param kurse					Liste der Kurse, die der Schüler belegt.
	 */
	public RepGostKursplanungSchueler(final SchuelerStammdaten schuelerStammdaten, final List<RepGostKursplanungSchuelerKurs> kurse) {
		super(schuelerStammdaten);
		Kurse = kurse;
	}


	/**
	 * Gibt eine Liste der IDs der Kurse zurück, um Überprüfungen durchführen zu können.
	 * @return Liste der IDs der Kurse
	 */
	public List<Long> getListeKurseIDs() {
		return Kurse.stream().map(k -> k.id).toList();
	}
}
