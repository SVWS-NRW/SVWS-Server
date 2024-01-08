package de.svws_nrw.module.reporting.types.gost.kursplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungBlockungsergebnis.</p>
 * <p>Sie enthält die Daten zu einem Blockungsergebnis, d. h. zu den Anzahlen, den Schienen und Kursen.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostKursplanungBlockungsergebnis {

	/** Das Kalenderjahr, in dem die Abiturprüfung des Blockungsergebnisses stattfindet */
	private int abiturjahr;

	/** Anzahl der Dummy-Schüler im Ergebnis */
	private int anzahlDummy;

	/** Anzahl der externen Schüler im Ergebnis */
	private int anzahlExterne;

	/** Maximale Anzahl an Kursen pro Schiene über alle Schienen */
	private int anzahlMaxKurseProSchiene;

	/** Anzahl der Schienen */
	private int anzahlSchienen;

	/** Anzahl der Schüler im Ergebnis */
	private int anzahlSchueler;

	/** Bezeichnung des Blockungsergebnisses */
	private String bezeichnung;

	/** Das Halbjahr der gymnasialen Oberstufe des Blockungsergebnisses */
	private String gostHalbjahr;

	/** ID des Blockungsergebnisses */
	private long id;

	/** Eine Liste vom Typ Kurs, die alle Kurse des Blockungsergebnisses beinhaltet. */
	private List<ReportingGostKursplanungKurs> kurse = new ArrayList<>();

	/** Eine Liste vom Typ Schiene, die alle Schienen des Blockungsergebnisses beinhaltet. */
	private List<ReportingGostKursplanungSchiene> schienen = new ArrayList<>();

	/** Eine Liste vom Typ Schüler, die alle Schüler des Blockungsergebnisses beinhaltet. */
	private List<ReportingSchueler> schueler = new ArrayList<>();



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param abiturjahr Das Kalenderjahr, in dem die Abiturprüfung des Blockungsergebnisses stattfindet
	 * @param anzahlDummy Anzahl der Dummy-Schüler im Ergebnis
	 * @param anzahlExterne Anzahl der externen Schüler im Ergebnis
	 * @param anzahlMaxKurseProSchiene Maximale Anzahl an Kursen pro Schiene über alle Schienen
	 * @param anzahlSchienen Anzahl der Schienen
	 * @param anzahlSchueler Anzahl der Schüler im Ergebnis
	 * @param bezeichnung Bezeichnung des Blockungsergebnisses
	 * @param gostHalbjahr Das Halbjahr der gymnasialen Oberstufe des Blockungsergebnisses
	 * @param id ID des Blockungsergebnisses
	 * @param kurse Eine Liste vom Typ Kurs, die alle Kurse des Blockungsergebnisses beinhaltet.
	 * @param schienen Eine Liste vom Typ Schiene, die alle Schienen des Blockungsergebnisses beinhaltet.
	 * @param schueler Eine Liste vom Typ Schüler, die alle Schüler des Blockungsergebnisses beinhaltet.
	 */
	public ReportingGostKursplanungBlockungsergebnis(final int abiturjahr, final int anzahlDummy, final int anzahlExterne, final int anzahlMaxKurseProSchiene, final int anzahlSchienen, final int anzahlSchueler, final String bezeichnung, final String gostHalbjahr, final long id, final List<ReportingGostKursplanungKurs> kurse, final List<ReportingGostKursplanungSchiene> schienen, final List<ReportingSchueler> schueler) {
		this.abiturjahr = abiturjahr;
		this.anzahlDummy = anzahlDummy;
		this.anzahlExterne = anzahlExterne;
		this.anzahlMaxKurseProSchiene = anzahlMaxKurseProSchiene;
		this.anzahlSchienen = anzahlSchienen;
		this.anzahlSchueler = anzahlSchueler;
		this.bezeichnung = bezeichnung;
		this.gostHalbjahr = gostHalbjahr;
		this.id = id;
		this.kurse = kurse;
		this.schienen = schienen;
		this.schueler = schueler;
	}


    // ##### Getter und Setter #####

	/**
	 * Das Kalenderjahr, in dem die Abiturprüfung des Blockungsergebnisses stattfindet
	 * @return Inhalt des Feldes abiturjahr
	 */
	public int abiturjahr() {
		return abiturjahr;
	}

	/**
	 * Das Kalenderjahr, in dem die Abiturprüfung des Blockungsergebnisses stattfindet, wird gesetzt.
	 * @param abiturjahr Neuer Wert für das Feld abiturjahr
	 */
	public void setAbiturjahr(final int abiturjahr) {
		this.abiturjahr = abiturjahr;
	}

	/**
	 * Anzahl der Dummy-Schüler im Ergebnis
	 * @return Inhalt des Feldes anzahlDummy
	 */
	public int anzahlDummy() {
		return anzahlDummy;
	}

	/**
	 * Anzahl der Dummy-Schüler im Ergebnis wird gesetzt.
	 * @param anzahlDummy Neuer Wert für das Feld anzahlDummy
	 */
	public void setAnzahlDummy(final int anzahlDummy) {
		this.anzahlDummy = anzahlDummy;
	}

	/**
	 * Anzahl der externen Schüler im Ergebnis
	 * @return Inhalt des Feldes anzahlExterne
	 */
	public int anzahlExterne() {
		return anzahlExterne;
	}

	/**
	 * Anzahl der externen Schüler im Ergebnis wird gesetzt.
	 * @param anzahlExterne Neuer Wert für das Feld anzahlExterne
	 */
	public void setAnzahlExterne(final int anzahlExterne) {
		this.anzahlExterne = anzahlExterne;
	}

	/**
	 * Maximale Anzahl an Kursen über alle Schienen
	 * @return Inhalt des Feldes anzahlMaxKurseProSchiene
	 */
	public int anzahlMaxKurseProSchiene() {
		return anzahlMaxKurseProSchiene;
	}

	/**
	 * Maximale Anzahl an Kursen über alle Schienen wird gesetzt.
	 * @param anzahlMaxKurseProSchiene Neuer Wert für das Feld anzahlMaxKurseProSchiene
	 */
	public void setAnzahlMaxKurseProSchiene(final int anzahlMaxKurseProSchiene) {
		this.anzahlMaxKurseProSchiene = anzahlMaxKurseProSchiene;
	}

	/**
	 * Anzahl der Schienen
	 * @return Inhalt des Feldes anzahlSchienen
	 */
	public int anzahlSchienen() {
		return anzahlSchienen;
	}

	/**
	 * Anzahl der Schienen wird gesetzt.
	 * @param anzahlSchienen Neuer Wert für das Feld anzahlSchienen
	 */
	public void setAnzahlSchienen(final int anzahlSchienen) {
		this.anzahlSchienen = anzahlSchienen;
	}

	/**
	 * Anzahl der Schüler im Ergebnis
	 * @return Inhalt des Feldes anzahlSchueler
	 */
	public int anzahlSchueler() {
		return anzahlSchueler;
	}

	/**
	 * Anzahl der Schüler im Ergebnis wird gesetzt.
	 * @param anzahlSchueler Neuer Wert für das Feld anzahlSchueler
	 */
	public void setAnzahlSchueler(final int anzahlSchueler) {
		this.anzahlSchueler = anzahlSchueler;
	}

	/**
	 * Bezeichnung des Blockungsergebnisses
	 * @return Inhalt des Feldes bezeichnung
	 */
	public String bezeichnung() {
		return bezeichnung;
	}

	/**
	 * Bezeichnung des Blockungsergebnisses wird gesetzt.
	 * @param bezeichnung Neuer Wert für das Feld bezeichnung
	 */
	public void setBezeichnung(final String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Das Halbjahr der gymnasialen Oberstufe des Blockungsergebnisses
	 * @return Inhalt des Feldes gostHalbjahr
	 */
	public String gostHalbjahr() {
		return gostHalbjahr;
	}

	/**
	 * Das Halbjahr der gymnasialen Oberstufe des Blockungsergebnisses wird gesetzt.
	 * @param gostHalbjahr Neuer Wert für das Feld gostHalbjahr
	 */
	public void setGostHalbjahr(final String gostHalbjahr) {
		this.gostHalbjahr = gostHalbjahr;
	}

	/**
	 * ID des Blockungsergebnisses
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * ID des Blockungsergebnisses wird gesetzt.
	 * @param id Neuer Wert für das Feld id
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * Eine Liste vom Typ Kurs, die alle Kurse des Blockungsergebnisses beinhaltet.
	 * @return Inhalt des Feldes kurse
	 */
	public List<ReportingGostKursplanungKurs> kurse() {
		return kurse;
	}

	/**
	 * Eine Liste vom Typ Kurs, die alle Kurse des Blockungsergebnisses beinhaltet, wird gesetzt.
	 * @param kurse Neuer Wert für das Feld kurse
	 */
	public void setKurse(final List<ReportingGostKursplanungKurs> kurse) {
		this.kurse = kurse;
	}

	/**
	 * Eine Liste vom Typ Schiene, die alle Schienen des Blockungsergebnisses beinhaltet.
	 * @return Inhalt des Feldes schienen
	 */
	public List<ReportingGostKursplanungSchiene> schienen() {
		return schienen;
	}

	/**
	 * Eine Liste vom Typ Schiene, die alle Schienen des Blockungsergebnisses beinhaltet, wird gesetzt.
	 * @param schienen Neuer Wert für das Feld schienen
	 */
	public void setSchienen(final List<ReportingGostKursplanungSchiene> schienen) {
		this.schienen = schienen;
	}

	/**
	 * Eine Liste vom Typ Schüler, die alle Schüler des Blockungsergebnisses beinhaltet.
	 * @return Inhalt des Feldes schienen
	 */
	public List<ReportingSchueler> schueler() {
		return schueler;
	}

	/**
	 * Eine Liste vom Typ Schüler, die alle Schüler des Blockungsergebnisses beinhaltet, wird gesetzt.
	 * @param schueler Neuer Wert für das Feld schienen
	 */
	public void setSchueler(final List<ReportingSchueler> schueler) {
		this.schueler = schueler;
	}
}
