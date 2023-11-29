package de.svws_nrw.module.pdf.reptypes.gost.kursplanung;

import java.util.List;

/**
 * Die Klasse enthält die Grunddaten einer Schiene und deren Daten für die GOSt-Kursplanung.
 */

public class RepGostKursplanungSchiene {

	/**
	 * ID der Schiene
	 */
	public Long id;

	/**
	 * Bezeichnung der Schiene
	 */
	public String bezeichnung;

	/**
	 * Anzahl der Schüler in der Schiene
	 */
	public int anzahlSchueler;

	/**
	 * Anzahl der externen Schüler in der Schiene
	 */
	public int anzahlExterne;

	/**
	 * Anzahl der Dummy-Schüler in der Schiene
	 */
	public int anzahlDummy;

	/**
	 * Gibt an, ob in der Schiene Schüler mit Kurskollisionen vorhanden sind.
	 */
	public boolean hatKollisionen;

	/** Eine Liste vom Typ Kurse, die alle Kurse der Schiene und deren Daten enthält. */
	public List<RepGostKursplanungKurs> Kurse;

	/** Eine Liste mit IDs von Kursen in der Schiene, die eine Kollision enthalten. */
	public List<Long> KurseIDsMitKollisionen;

	/** Eine Liste mit IDs von Schülern in der Schiene, die eine Kollision enthalten. */
	public List<Long> SchuelerIDsMitKollisionen;


	/**
	 * Erstellt eine neue Schiene der GOSt-Kursplanung
	 *
	 * @param id						ID der Schiene
	 * @param bezeichnung				Bezeichnung der Schiene
	 * @param anzahlSchueler			Anzahl der Schüler in der Schien
	 * @param anzahlExterne				Anzahl der externen Schüler in der Schiene
	 * @param anzahlDummy				Anzahl der Dummy-Schüler in der Schiene
	 * @param hatKollisionen			Gibt an, ob in der Schiene Schüler mit Kurskollisionen vorhanden sind.
	 * @param kurse						Eine Liste vom Typ Kurse, die alle Kurse der Schiene und deren Daten enthält.
	 * @param kurseIDsMitKollisionen 	Eine Liste mit IDs der Kurse in der Schiene, die eine Kollision enthalten.
	 * @param schuelerIDsMitKollisionen	Eine Liste mit IDs der Schüler in der Schiene, die eine Kollision enthalten.
	 */
	public RepGostKursplanungSchiene(final Long id,
									 final String bezeichnung,
									 final int anzahlSchueler,
									 final int anzahlExterne,
									 final int anzahlDummy,
									 final boolean hatKollisionen,
									 final List<RepGostKursplanungKurs> kurse,
									 final List<Long> kurseIDsMitKollisionen,
									 final List<Long> schuelerIDsMitKollisionen) {
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.anzahlSchueler = anzahlSchueler;
		this.anzahlExterne = anzahlExterne;
		this.anzahlDummy = anzahlDummy;
		this.hatKollisionen = hatKollisionen;
		this.Kurse = kurse;
		this.KurseIDsMitKollisionen = kurseIDsMitKollisionen;
		this.SchuelerIDsMitKollisionen = schuelerIDsMitKollisionen;
	}


	/**
	 * Gibt eine Liste der IDs der Kurse zurück, um Überprüfungen durchführen zu können.
	 * @return Liste der IDs der Kurse
	 */
	public List<Long> getListeKurseIDs() {
		return Kurse.stream().map(k -> k.id).toList();
	}
}
