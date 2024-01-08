package de.svws_nrw.module.reporting.types.gost.kursplanung;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungSchiene.</p>
 * <p>Sie enthält die Daten zu einer Schiene Kursplanung der gymnasialen Oberstufe, unter anderem Anzahlen, Kurse und Kollisionen.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostKursplanungSchiene {

	/** Anzahl der Dummy-Schüler in der Schiene */
	private int anzahlDummy;

	/** Anzahl der externen Schüler in der Schiene */
	private int anzahlExterne;

	/** Anzahl der Schüler in der Schiene */
	private int anzahlSchueler;

	/** Bezeichnung der Schiene */
	private String bezeichnung;

	/** Gibt an, ob in der Schiene Schüler mit Kurskollisionen vorhanden sind. */
	private boolean hatKollisionen;

	/** ID der Schiene */
	private long id;

	/** Eine Liste mit IDs von Kursen in der Schiene, die eine Kollision enthalten. */
	private List<Long> idsKurseMitKollisionen;

	/** Eine Liste mit IDs von Schülern in der Schiene, die eine Kollision enthalten. */
	private List<Long> idsSchuelerMitKollisionen;

	/** Eine Liste vom Typ Kurs, die alle Kurse der Schiene und deren Daten enthält. */
	private List<ReportingGostKursplanungKurs> kurse = new ArrayList<>();

	/** Die Nummer der Schiene. */
	private int nummer;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param anzahlDummy				Anzahl der Dummy-Schüler in der Schiene
	 * @param anzahlExterne				Anzahl der externen Schüler in der Schiene
	 * @param anzahlSchueler			Anzahl der Schüler in der Schien
	 * @param bezeichnung				Bezeichnung der Schiene
	 * @param hatKollisionen			Gibt an, ob in der Schiene Schüler mit Kurskollisionen vorhanden sind.
	 * @param id						ID der Schiene
	 * @param idsKurseMitKollisionen 	Eine Liste mit IDs der Kurse in der Schiene, die eine Kollision enthalten.
	 * @param idsSchuelerMitKollisionen	Eine Liste mit IDs der Schüler in der Schiene, die eine Kollision enthalten.
	 * @param kurse						Eine Liste vom Typ Kurse, die alle Kurse der Schiene und deren Daten enthält.
	 * @param nummer					Die Nummer der Schiene.
	 */
	public ReportingGostKursplanungSchiene(final int anzahlDummy, final int anzahlExterne, final int anzahlSchueler, final String bezeichnung, final boolean hatKollisionen, final long id, final List<Long> idsKurseMitKollisionen, final List<Long> idsSchuelerMitKollisionen, final List<ReportingGostKursplanungKurs> kurse, final int nummer) {
		this.anzahlDummy = anzahlDummy;
		this.anzahlExterne = anzahlExterne;
		this.anzahlSchueler = anzahlSchueler;
		this.hatKollisionen = hatKollisionen;
		this.bezeichnung = bezeichnung;
		this.id = id;
		this.idsKurseMitKollisionen = idsKurseMitKollisionen;
		this.idsSchuelerMitKollisionen = idsSchuelerMitKollisionen;
		this.kurse = kurse;
		this.nummer = nummer;
	}



	// ##### Getter und Setter #####

	/**
	 * Anzahl der Dummy-Schüler in der Schiene
	 * @return Inhalt des Feldes anzahlDummy
	 */
	public int anzahlDummy() {
		return anzahlDummy;
	}

	/**
	 * Anzahl der Dummy-Schüler in der Schiene wird gesetzt.
	 * @param anzahlDummy Neuer Wert für das Feld anzahlDummy
	 */
	public void setAnzahlDummy(final int anzahlDummy) {
		this.anzahlDummy = anzahlDummy;
	}

	/**
	 * Anzahl der externen Schüler in der Schiene
	 * @return Inhalt des Feldes anzahlExterne
	 */
	public int anzahlExterne() {
		return anzahlExterne;
	}

	/**
	 * Anzahl der externen Schüler in der Schiene wird gesetzt.
	 * @param anzahlExterne Neuer Wert für das Feld anzahlExterne
	 */
	public void setAnzahlExterne(final int anzahlExterne) {
		this.anzahlExterne = anzahlExterne;
	}

	/**
	 * Anzahl der Schüler in der Schiene
	 * @return Inhalt des Feldes anzahlSchueler
	 */
	public int anzahlSchueler() {
		return anzahlSchueler;
	}

	/**
	 * Anzahl der Schüler in der Schiene wird gesetzt.
	 * @param anzahlSchueler Neuer Wert für das Feld anzahlSchueler
	 */
	public void setAnzahlSchueler(final int anzahlSchueler) {
		this.anzahlSchueler = anzahlSchueler;
	}

	/**
	 * Bezeichnung der Schiene
	 * @return Inhalt des Feldes bezeichnung
	 */
	public String bezeichnung() {
		return bezeichnung;
	}

	/**
	 * Bezeichnung der Schiene wird gesetzt.
	 * @param bezeichnung Neuer Wert für das Feld bezeichnung
	 */
	public void setBezeichnung(final String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Gibt an, ob in der Schiene Schüler mit Kurskollisionen vorhanden sind.
	 * @return Inhalt des Feldes hatKollisionen
	 */
	public boolean hatKollisionen() {
		return hatKollisionen;
	}

	/**
	 * Gibt an, ob in der Schiene Schüler mit Kurskollisionen vorhanden sind, wird gesetzt.
	 * @param hatKollisionen Neuer Wert für das Feld hatKollisionen
	 */
	public void setHatKollisionen(final boolean hatKollisionen) {
		this.hatKollisionen = hatKollisionen;
	}

	/**
	 * ID der Schiene
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * ID der Schiene wird gesetzt.
	 * @param id Neuer Wert für das Feld id
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * Eine Liste mit IDs von Kursen in der Schiene, die eine Kollision enthalten.
	 * @return Inhalt des Feldes idsKurseMitKollisionen
	 */
	public List<Long> idsKurseMitKollisionen() {
		return idsKurseMitKollisionen;
	}

	/**
	 * Eine Liste mit IDs von Kursen in der Schiene, die eine Kollision enthalten wird gesetzt.
	 * @param idsKurseMitKollisionen Neuer Wert für das Feld idsKurseMitKollisionen
	 */
	public void setIdsKurseMitKollisionen(final List<Long> idsKurseMitKollisionen) {
		this.idsKurseMitKollisionen = idsKurseMitKollisionen;
	}

	/**
	 * Eine Liste mit IDs von Schülern in der Schiene, die eine Kollision enthalten.
	 * @return Inhalt des Feldes idsSchuelerMitKollisionen
	 */
	public List<Long> idsSchuelerMitKollisionen() {
		return idsSchuelerMitKollisionen;
	}

	/**
	 * Eine Liste mit IDs von Schülern in der Schiene, die eine Kollision enthalten wird gesetzt.
	 * @param idsSchuelerMitKollisionen Neuer Wert für das Feld idsSchuelerMitKollisionen
	 */
	public void setIdsSchuelerMitKollisionen(final List<Long> idsSchuelerMitKollisionen) {
		this.idsSchuelerMitKollisionen = idsSchuelerMitKollisionen;
	}

	/**
	 * Eine Liste vom Typ Kurse, die alle Kurse der Schiene und deren Daten enthält.
	 * @return Inhalt des Feldes kurse
	 */
	public List<ReportingGostKursplanungKurs> kurse() {
		return kurse;
	}

	/**
	 * Eine Liste vom Typ Kurse, die alle Kurse der Schiene und deren Daten enthält, wird gesetzt.
	 * @param kurse Neuer Wert für das Feld kurse
	 */
	public void setKurse(final List<ReportingGostKursplanungKurs> kurse) {
		this.kurse = kurse;
	}

	/**
	 * Die Nummer der Schiene.
	 * @return Inhalt des Feldes nummer
	 */
	public int nummer() {
		return nummer;
	}

	/**
	 * Die Nummer der Schiene wird gesetzt.
	 * @param nummer Neuer Wert für das Feld nummer
	 */
	public void setNummer(final int nummer) {
		this.nummer = nummer;
	}

}
