package de.svws_nrw.module.reporting.types.gost.kursplanung;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungKurs.
 */
public class ReportingGostKursplanungKurs {

	/** Anzahl der Schülerinnen und Schüler für das Fach des Kurses erstes oder zweites Abiturfach ist. */
	protected int anzahlAB12;

	/** Anzahl der Schülerinnen und Schüler für das Fach des Kurses drittes Abiturfach ist. */
	protected int anzahlAB3;

	/** Anzahl der Schülerinnen und Schüler für das Fach des Kurses viertes Abiturfach ist. */
	protected int anzahlAB4;

	/** Anzahl der Dummy-Schüler */
	protected int anzahlDummy;

	/** Anzahl der Schülerinnen und Schüler mit Status extern. */
	protected int anzahlExterne;

	/** Anzahl der Schülerinnen und Schüler im Kurs. */
	protected int anzahlSchueler;

	/** Anzahl der Schüler des Kurses, die das Fach schriftlich belegt haben. */
	protected int anzahlSchuelerSchriftlich;

	/** Bezeichnung des Kurses. */
	protected String bezeichnung;

	/** Das Fach des Kurses */
	protected ReportingFach fach;

	/** Die Fachwahl-Statistik zum Fach und zur Kursart des Kurses für das GOSt-Halbjahr des Kurses */
	protected ReportingGostKursplanungFachwahlstatistik fachwahlstatistik;

	/** Halbjahr der Oberstufe für den Kurs gemäß Blockungsergebnis. */
	protected GostHalbjahr gostHalbjahr;

	/** Kursart des Kurses. */
	protected GostKursart gostKursart;

	/** ID des Kurses */
	protected long id;

	/** Eine Liste der Lehrkräfte des Kurses. */
	protected List<ReportingLehrer> lehrkraefte;

	/** Eine Liste vom Typ Schiene, die die Schienen beinhaltet, in denen der Kurs gemäß Blockungsergebnis liegt. */
	protected List<ReportingGostKursplanungSchiene> schienen;

	/** Eine Liste vom Typ Kursschueler, die alle Schülerinnen und Schüler des Kurses enthält. */
	protected List<ReportingSchueler> schueler;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param anzahlAB12				Anzahl der Schülerinnen und Schüler für das Fach des Kurses erstes oder zweites Abiturfach ist.
	 * @param anzahlAB3					Anzahl der Schülerinnen und Schüler für das Fach des Kurses drittes Abiturfach ist.
	 * @param anzahlAB4					Anzahl der Schülerinnen und Schüler für das Fach des Kurses viertes Abiturfach ist.
	 * @param anzahlDummy 				Anzahl der Dummy-Schüler.
	 * @param anzahlExterne				Anzahl der Schülerinnen und Schüler mit Status extern.
	 * @param anzahlSchuelerSchriftlich	Anzahl der Klausurschreiber.
	 * @param anzahlSchueler			Anzahl der Schülerinnen und Schüler im Kurs.
	 * @param bezeichnung				Bezeichnung des Kurses.
	 * @param fach			 			Das Fach des Kurses.
	 * @param fachwahlstatistik			Die Fachwahl-Statistik zum Fach und zur Kursart des Kurses für das GOSt-Halbjahr des Kurses
	 * @param gostHalbjahr				Halbjahr der Oberstufe für den Kurs gemäß Blockungsergebnis.
	 * @param gostKursart				Kursart des Kurses.
	 * @param id						ID des Kurses.
	 * @param lehrkraefte				Liste der Lehrkräfte des Kurses.
	 * @param schienen                  Liste vom Typ Schiene, die die Schienen beinhaltet, in denen der Kurs gemäß Blockungsergebnis liegt.
	 * @param schueler					Liste der Schüler des Kurses.
	 */
	public ReportingGostKursplanungKurs(final int anzahlAB12, final int anzahlAB3, final int anzahlAB4, final int anzahlDummy, final int anzahlExterne,
			final int anzahlSchueler, final int anzahlSchuelerSchriftlich, final String bezeichnung, final ReportingFach fach,
			final ReportingGostKursplanungFachwahlstatistik fachwahlstatistik, final GostHalbjahr gostHalbjahr,
			final GostKursart gostKursart, final long id, final List<ReportingLehrer> lehrkraefte, final List<ReportingGostKursplanungSchiene> schienen,
			final List<ReportingSchueler> schueler) {
		this.anzahlAB12 = anzahlAB12;
		this.anzahlAB3 = anzahlAB3;
		this.anzahlAB4 = anzahlAB4;
		this.anzahlDummy = anzahlDummy;
		this.anzahlExterne = anzahlExterne;
		this.anzahlSchueler = anzahlSchueler;
		this.anzahlSchuelerSchriftlich = anzahlSchuelerSchriftlich;
		this.bezeichnung = bezeichnung;
		this.fach = fach;
		this.fachwahlstatistik = fachwahlstatistik;
		this.gostHalbjahr = gostHalbjahr;
		this.gostKursart = gostKursart;
		this.id = id;
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

	/**
	 * Auflistung der Schienen des Kurses als kommaseparierte Liste der Schienennummern.
	 * @return Kommaseparierte Liste der Schienen
	 */
	public String schienenAuflistung() {
		return this.schienen.stream().map(s -> String.valueOf(s.nummer())).collect(Collectors.joining(","));
	}


	// ##### Getter #####

	/**
	 * Anzahl der Schülerinnen und Schüler für das Fach des Kurses erstes oder zweites Abiturfach ist.
	 * @return Inhalt des Feldes anzahlAB12
	 */
	public int anzahlAB12() {
		return anzahlAB12;
	}

	/**
	 * Anzahl der Schülerinnen und Schüler für das Fach des Kurses drittes Abiturfach ist.
	 * @return Inhalt des Feldes anzahlAB3
	 */
	public int anzahlAB3() {
		return anzahlAB3;
	}

	/**
	 * Anzahl der Schülerinnen und Schüler für das Fach des Kurses viertes Abiturfach ist.
	 * @return Inhalt des Feldes anzahlAB4
	 */
	public int anzahlAB4() {
		return anzahlAB4;
	}

	/**
	 * Anzahl der Dummy-Schüler
	 * @return Inhalt des Feldes anzahlDummy
	 */
	public int anzahlDummy() {
		return anzahlDummy;
	}

	/**
	 * Anzahl der Schülerinnen und Schüler mit Status extern.
	 * @return Inhalt des Feldes anzahlExterne
	 */
	public int anzahlExterne() {
		return anzahlExterne;
	}

	/**
	 * Anzahl der Schülerinnen und Schüler im Kurs.
	 * @return Inhalt des Feldes anzahlSchueler
	 */
	public int anzahlSchueler() {
		return anzahlSchueler;
	}

	/**
	 * Anzahl der Schüler des Kurses, die das Fach schriftlich belegt haben.
	 * @return Inhalt des Feldes anzahlSchuelerSchriftlich
	 */
	public int anzahlSchuelerSchriftlich() {
		return anzahlSchuelerSchriftlich;
	}

	/**
	 * Bezeichnung des Kurses.
	 * @return Inhalt des Feldes bezeichnung
	 */
	public String bezeichnung() {
		return bezeichnung;
	}

	/**
	 * Das Fach des Kurses
	 * @return Inhalt des Feldes fach
	 */
	public ReportingFach fach() {
		return fach;
	}

	/**
	 * Die Fachwahl-Statistik zum Fach und zur Kursart des Kurses für das GOSt-Halbjahr des Kurses
	 * @return Inhalt des Feldes fachwahlstatistik
	 */
	public ReportingGostKursplanungFachwahlstatistik fachwahlstatistik() {
		return fachwahlstatistik;
	}

	/**
	 * Halbjahr der Oberstufe für den Kurs gemäß Blockungsergebnis.
	 * @return Inhalt des Feldes gostHalbjahr
	 */
	public GostHalbjahr gostHalbjahr() {
		return gostHalbjahr;
	}

	/**
	 * Kursart des Kurses.
	 * @return Inhalt des Feldes gostKursart
	 */
	public GostKursart gostKursart() {
		return gostKursart;
	}

	/**
	 * ID des Kurses
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Liste der Lehrkräfte des Kurses.
	 * @return Inhalt des Feldes lehrkraefte
	 */
	public List<ReportingLehrer> lehrkraefte() {
		return lehrkraefte;
	}

	/**
	 * Eine Liste vom Typ Schiene, die die Schienen beinhaltet, in denen der Kurs gemäß Blockungsergebnis liegt.
	 * @return Inhalt des Feldes schienen
	 */
	public List<ReportingGostKursplanungSchiene> schienen() {
		return schienen;
	}

	/**
	 * Eine Liste vom Typ Kursschueler, die alle Schülerinnen und Schüler des Kurses enthält.
	 * @return Inhalt des Feldes schueler
	 */
	public List<ReportingSchueler> schueler() {
		return schueler;
	}

}
