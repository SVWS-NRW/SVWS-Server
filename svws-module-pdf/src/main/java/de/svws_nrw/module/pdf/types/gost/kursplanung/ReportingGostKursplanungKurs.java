package de.svws_nrw.module.pdf.types.gost.kursplanung;

import de.svws_nrw.module.pdf.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.pdf.types.schueler.ReportingSchueler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungKurs.</p>
 * <p>Sie enthält die Daten zu einem Kurs der Kursplanung der gymnasialen Oberstufe, unter anderem Anzahlen, Schüler und Lehrer.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostKursplanungKurs {

	/** Anzahl der Schülerinnen und Schüler für das Fach des Kurses erstes oder zweites Abiturfach ist. */
	private int anzahlAB12;

	/** Anzahl der Schülerinnen und Schüler für das Fach des Kurses drittes Abiturfach ist. */
	private int anzahlAB3;

	/** Anzahl der Schülerinnen und Schüler für das Fach des Kurses viertes Abiturfach ist. */
	private int anzahlAB4;

	/** Anzahl der Dummy-Schüler */
	private int anzahlDummy;

	/** Anzahl der Schülerinnen und Schüler mit Status extern. */
	private int anzahlExterne;

	/** Anzahl der Klausurschreiber. */
	private int anzahlKlausurschreiber;

	/** Anzahl der Schülerinnen und Schüler im Kurs. */
	private int anzahlSchueler;

	/** Bezeichnung des Kurses. */
	private String bezeichnung;

	/** Farbe des Faches des Kurses im Web-Client */
	private String farbeClientRGB;

	/** Halbjahr der Oberstufe für den Kurs gemäß Blockungsergebnis. */
	private String gostHalbjahr;

	/** ID des Kurses */
	private long id;

	/** Kursart des Kurses. */
	private String kursart;

	/** Eine Liste der Lehrkräfte des Kurses. */
	private List<ReportingLehrer> lehrkraefte;

	/** Eine Liste vom Typ Schiene, die die Schienen beinhaltet, in denen der Kurs gemäß Blockungsergebnis liegt. */
	private List<ReportingGostKursplanungSchiene> schienen;

	/** Eine Liste vom Typ Kursschueler, die alle Schülerinnen und Schüler des Kurses enthält. */
	private List<ReportingSchueler> schueler;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param anzahlAB12				Anzahl der Schülerinnen und Schüler für das Fach des Kurses erstes oder zweites Abiturfach ist.
	 * @param anzahlAB3					Anzahl der Schülerinnen und Schüler für das Fach des Kurses drittes Abiturfach ist.
	 * @param anzahlAB4					Anzahl der Schülerinnen und Schüler für das Fach des Kurses viertes Abiturfach ist.
	 * @param anzahlDummy 				Anzahl der Dummy-Schüler.
	 * @param anzahlExterne				Anzahl der Schülerinnen und Schüler mit Status extern.
	 * @param anzahlKlausurschreiber	Anzahl der Klausurschreiber.
	 * @param anzahlSchueler			Anzahl der Schülerinnen und Schüler im Kurs.
	 * @param bezeichnung				Bezeichnung des Kurses.
	 * @param farbeClientRGB 			Farbe des Faches des Kurses im Web-Client.
	 * @param gostHalbjahr				Halbjahr der Oberstufe für den Kurs gemäß Blockungsergebnis.
	 * @param id						ID des Kurses.
	 * @param kursart					Kursart des Kurses.
	 * @param lehrkraefte				Liste der Lehrkräfte des Kurses.
	 * @param schienen                  Liste vom Typ Schiene, die die Schienen beinhaltet, in denen der Kurs gemäß Blockungsergebnis liegt.
	 * @param schueler					Liste der Schüler des Kurses.
	 */
	public ReportingGostKursplanungKurs(final int anzahlAB12, final int anzahlAB3, final int anzahlAB4, final int anzahlDummy, final int anzahlExterne, final int anzahlKlausurschreiber, final int anzahlSchueler, final String bezeichnung, final String farbeClientRGB, final String gostHalbjahr, final long id, final String kursart, final List<ReportingLehrer> lehrkraefte, final List<ReportingGostKursplanungSchiene> schienen, final List<ReportingSchueler> schueler) {
		this.anzahlAB12 = anzahlAB12;
		this.anzahlAB3 = anzahlAB3;
		this.anzahlAB4 = anzahlAB4;
		this.anzahlDummy = anzahlDummy;
		this.anzahlExterne = anzahlExterne;
		this.anzahlKlausurschreiber = anzahlKlausurschreiber;
		this.anzahlSchueler = anzahlSchueler;
		this.bezeichnung = bezeichnung;
		this.farbeClientRGB = farbeClientRGB;
		this.gostHalbjahr = gostHalbjahr;
		this.id = id;
		this.kursart = kursart;
		this.lehrkraefte = lehrkraefte;
		this.schienen = schienen;
		this.schueler = schueler;
	}



	// ##### Berechnete Felder #####

	/**
	 * Auflistung der Lehrkräfte des Kurses als kommaseparierte Liste der Kürzel.
	 * @return Kommaseparierte Liste der Lehrkräfte
	 */
	public String lehrkraefteAuflistung() {
		return this.lehrkraefte.stream().map(ReportingLehrer::kuerzel).collect(Collectors.joining(","));
	}




	// ##### Getter und Setter #####

	/**
	 * Anzahl der Schülerinnen und Schüler für das Fach des Kurses erstes oder zweites Abiturfach ist.
	 * @return Inhalt des Feldes anzahlAB12
	 */
	public int anzahlAB12() {
		return anzahlAB12;
	}

	/**
	 * Anzahl der Schülerinnen und Schüler für das Fach des Kurses erstes oder zweites Abiturfach ist, wird gesetzt.
	 * @param anzahlAB12 Neuer Wert für das Feld anzahlAB12
	 */
	public void setAnzahlAB12(final int anzahlAB12) {
		this.anzahlAB12 = anzahlAB12;
	}

	/**
	 * Anzahl der Schülerinnen und Schüler für das Fach des Kurses drittes Abiturfach ist.
	 * @return Inhalt des Feldes anzahlAB3
	 */
	public int anzahlAB3() {
		return anzahlAB3;
	}

	/**
	 * Anzahl der Schülerinnen und Schüler für das Fach des Kurses drittes Abiturfach ist, wird gesetzt.
	 * @param anzahlAB3 Neuer Wert für das Feld anzahlAB3
	 */
	public void setAnzahlAB3(final int anzahlAB3) {
		this.anzahlAB3 = anzahlAB3;
	}

	/**
	 * Anzahl der Schülerinnen und Schüler für das Fach des Kurses viertes Abiturfach ist.
	 * @return Inhalt des Feldes anzahlAB4
	 */
	public int anzahlAB4() {
		return anzahlAB4;
	}

	/**
	 * Anzahl der Schülerinnen und Schüler für das Fach des Kurses viertes Abiturfach ist, wird gesetzt.
	 * @param anzahlAB4 Neuer Wert für das Feld anzahlAB4
	 */
	public void setAnzahlAB4(final int anzahlAB4) {
		this.anzahlAB4 = anzahlAB4;
	}

	/**
	 * Anzahl der Dummy-Schüler
	 * @return Inhalt des Feldes anzahlDummy
	 */
	public int anzahlDummy() {
		return anzahlDummy;
	}

	/**
	 * Anzahl der Dummy-Schüler wird gesetzt.
	 * @param anzahlDummy Neuer Wert für das Feld anzahlDummy
	 */
	public void setAnzahlDummy(final int anzahlDummy) {
		this.anzahlDummy = anzahlDummy;
	}

	/**
	 * Anzahl der Schülerinnen und Schüler mit Status extern.
	 * @return Inhalt des Feldes anzahlExterne
	 */
	public int anzahlExterne() {
		return anzahlExterne;
	}

	/**
	 * Anzahl der Schülerinnen und Schüler mit Status extern wird gesetzt.
	 * @param anzahlExterne Neuer Wert für das Feld anzahlExterne
	 */
	public void setAnzahlExterne(final int anzahlExterne) {
		this.anzahlExterne = anzahlExterne;
	}

	/**
	 * Anzahl der Klausurschreiber.
	 * @return Inhalt des Feldes anzahlKlausurschreiber
	 */
	public int anzahlKlausurschreiber() {
		return anzahlKlausurschreiber;
	}

	/**
	 * Anzahl der Klausurschreiber wird gesetzt.
	 * @param anzahlKlausurschreiber Neuer Wert für das Feld anzahlKlausurschreiber
	 */
	public void setAnzahlKlausurschreiber(final int anzahlKlausurschreiber) {
		this.anzahlKlausurschreiber = anzahlKlausurschreiber;
	}

	/**
	 * Anzahl der Schülerinnen und Schüler im Kurs.
	 * @return Inhalt des Feldes anzahlSchueler
	 */
	public int anzahlSchueler() {
		return anzahlSchueler;
	}

	/**
	 * Anzahl der Schülerinnen und Schüler im Kurs wird gesetzt.
	 * @param anzahlSchueler Neuer Wert für das Feld anzahlSchueler
	 */
	public void setAnzahlSchueler(final int anzahlSchueler) {
		this.anzahlSchueler = anzahlSchueler;
	}

	/**
	 * Bezeichnung des Kurses.
	 * @return Inhalt des Feldes bezeichnung
	 */
	public String bezeichnung() {
		return bezeichnung;
	}

	/**
	 * Bezeichnung des Kurses wird gesetzt.
	 * @param bezeichnung Neuer Wert für das Feld bezeichnung
	 */
	public void setBezeichnung(final String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Farbe des Faches des Kurses im Web-Client
	 * @return Inhalt des Feldes farbeClientRGB
	 */
	public String farbeClientRGB() {
		return farbeClientRGB;
	}

	/**
	 * Farbe des Faches des Kurses im Web-Client wird gesetzt.
	 * @param farbeClientRGB Neuer Wert für das Feld farbeClientRGB
	 */
	public void setFarbeClientRGB(final String farbeClientRGB) {
		this.farbeClientRGB = farbeClientRGB;
	}

	/**
	 * Halbjahr der Oberstufe für den Kurs gemäß Blockungsergebnis.
	 * @return Inhalt des Feldes gostHalbjahr
	 */
	public String gostHalbjahr() {
		return gostHalbjahr;
	}

	/**
	 * Halbjahr der Oberstufe für den Kurs gemäß Blockungsergebnis wird gesetzt.
	 * @param gostHalbjahr Neuer Wert für das Feld gostHalbjahr
	 */
	public void setGostHalbjahr(final String gostHalbjahr) {
		this.gostHalbjahr = gostHalbjahr;
	}

	/**
	 * ID des Kurses
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * ID des Kurses wird gesetzt.
	 * @param id Neuer Wert für das Feld id
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * Kursart des Kurses.
	 * @return Inhalt des Feldes kursart
	 */
	public String kursart() {
		return kursart;
	}

	/**
	 * Kursart des Kurses wird gesetzt.
	 * @param kursart Neuer Wert für das Feld kursart
	 */
	public void setKursart(final String kursart) {
		this.kursart = kursart;
	}

	/**
	 * Liste der Lehrkräfte des Kurses.
	 * @return Inhalt des Feldes lehrkraefte
	 */
	public List<ReportingLehrer> lehrkraefte() {
		return lehrkraefte;
	}

	/**
	 * Liste der Lehrkräfte des Kurses wird gesetzt.
	 * @param lehrkraefte Neuer Wert für das Feld lehrkraefte
	 */
	public void setLehrkraefte(final List<ReportingLehrer> lehrkraefte) {
		this.lehrkraefte = lehrkraefte;
	}

	/**
	 * Eine Liste vom Typ Schiene, die die Schienen beinhaltet, in denen der Kurs gemäß Blockungsergebnis liegt.
	 * @return Inhalt des Feldes schienen
	 */
	public List<ReportingGostKursplanungSchiene> schienen() {
		return schienen;
	}

	/**
	 * Eine Liste vom Typ Schiene, die die Schienen beinhaltet, in denen der Kurs gemäß Blockungsergebnis liegt, wird gesetzt.
	 * @param schienen Neuer Wert für das Feld schienen
	 */
	public void setSchienen(final List<ReportingGostKursplanungSchiene> schienen) {
		this.schienen = schienen;
	}

	/**
	 * Eine Liste vom Typ Kursschueler, die alle Schülerinnen und Schüler des Kurses enthält.
	 * @return Inhalt des Feldes schueler
	 */
	public List<ReportingSchueler> schueler() {
		return schueler;
	}

	/**
	 * Eine Liste vom Typ Kursschueler, die alle Schülerinnen und Schüler des Kurses enthält, wird gesetzt.
	 * @param schueler Neuer Wert für das Feld schueler
	 */
	public void setSchueler(final List<ReportingSchueler> schueler) {
		this.schueler = schueler;
	}
}
