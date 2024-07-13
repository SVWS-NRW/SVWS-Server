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
	protected int anzahlDummy;

	/** Anzahl der externen Schüler in der Schiene */
	protected int anzahlExterne;

	/** Anzahl der Schüler in der Schiene */
	protected int anzahlSchueler;

	/** Bezeichnung der Schiene */
	protected String bezeichnung;

	/** Gibt an, ob in der Schiene Schüler mit Kurskollisionen vorhanden sind. */
	protected boolean hatKollisionen;

	/** ID der Schiene */
	protected long id;

	/** Eine Liste mit IDs von Kursen in der Schiene, die eine Kollision enthalten. */
	protected List<Long> idsKurseMitKollisionen;

	/** Eine Liste mit IDs von Schülern in der Schiene, die eine Kollision enthalten. */
	protected List<Long> idsSchuelerMitKollisionen;

	/** Eine Liste vom Typ Kurs, die alle Kurse der Schiene und deren Daten enthält. */
	protected List<ReportingGostKursplanungKurs> kurse = new ArrayList<>();

	/** Die Nummer der Schiene. */
	protected int nummer;


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
	public ReportingGostKursplanungSchiene(final int anzahlDummy, final int anzahlExterne, final int anzahlSchueler, final String bezeichnung,
			final boolean hatKollisionen, final long id, final List<Long> idsKurseMitKollisionen, final List<Long> idsSchuelerMitKollisionen,
			final List<ReportingGostKursplanungKurs> kurse, final int nummer) {
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



	// ##### Getter #####

	/**
	 * Anzahl der Dummy-Schüler in der Schiene
	 * @return Inhalt des Feldes anzahlDummy
	 */
	public int anzahlDummy() {
		return anzahlDummy;
	}

	/**
	 * Anzahl der externen Schüler in der Schiene
	 * @return Inhalt des Feldes anzahlExterne
	 */
	public int anzahlExterne() {
		return anzahlExterne;
	}

	/**
	 * Anzahl der Schüler in der Schiene
	 * @return Inhalt des Feldes anzahlSchueler
	 */
	public int anzahlSchueler() {
		return anzahlSchueler;
	}

	/**
	 * Bezeichnung der Schiene
	 * @return Inhalt des Feldes bezeichnung
	 */
	public String bezeichnung() {
		return bezeichnung;
	}

	/**
	 * Gibt an, ob in der Schiene Schüler mit Kurskollisionen vorhanden sind.
	 * @return Inhalt des Feldes hatKollisionen
	 */
	public boolean hatKollisionen() {
		return hatKollisionen;
	}

	/**
	 * ID der Schiene
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Eine Liste mit IDs von Kursen in der Schiene, die eine Kollision enthalten.
	 * @return Inhalt des Feldes idsKurseMitKollisionen
	 */
	public List<Long> idsKurseMitKollisionen() {
		return idsKurseMitKollisionen;
	}

	/**
	 * Eine Liste mit IDs von Schülern in der Schiene, die eine Kollision enthalten.
	 * @return Inhalt des Feldes idsSchuelerMitKollisionen
	 */
	public List<Long> idsSchuelerMitKollisionen() {
		return idsSchuelerMitKollisionen;
	}

	/**
	 * Eine Liste vom Typ Kurse, die alle Kurse der Schiene und deren Daten enthält.
	 * @return Inhalt des Feldes kurse
	 */
	public List<ReportingGostKursplanungKurs> kurse() {
		return kurse;
	}

	/**
	 * Die Nummer der Schiene.
	 * @return Inhalt des Feldes nummer
	 */
	public int nummer() {
		return nummer;
	}

}
