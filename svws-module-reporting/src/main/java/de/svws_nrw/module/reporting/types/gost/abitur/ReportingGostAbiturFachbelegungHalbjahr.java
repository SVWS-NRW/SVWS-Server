package de.svws_nrw.module.reporting.types.gost.abitur;

import de.svws_nrw.asd.types.Note;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostAbiturFachbelegungHalbjahr.
 */
public class ReportingGostAbiturFachbelegungHalbjahr extends ReportingBaseType {

	/** Das einstellige Kürzel der bilingualen Sprache, sofern das Fach bilingual unterrichtet wurde. */
	protected String bilingualeSprache;

	/** Gibt an, ob die Belegung für den Block I gewertet wird oder nicht - nicht alle Kursbelegungen müssen laut Prüfungsordnung in die Abiturnote einfliessen */
	protected Boolean block1gewertet;

	/** Gibt an, ob die Belegung des Kurses auf dem Abiturzeugnis angezeigt werden soll oder nicht. Eine Belegung kann auf Wunsch des Prüflings bei nicht gewerteten Kursen nicht auf dem Zeugnis erscheinen. */
	protected Boolean block1kursAufZeugnis;

	/** Die Anzahl der Fehlstunden. */
	protected int fehlstundenGesamt;

	/** Die Anzahl der unentschuldigten Fehlstunden. */
	protected int fehlstundenUnentschuldigt;

	/** Das Kürzel des Halbjahres der Fachbelegung */
	protected String halbjahrKuerzel;

	/** Angabe, ob das schriftlich belegt wurde oder nicht. */
	protected boolean istSchriftlich;

	/** Das Kürzel der Kursart der gymnasialen Oberstufe dieser Fachbelegung  */
	protected String kursartKuerzel;

	/** Die Lehrkraft, welche die Note erteilt. */
	protected ReportingLehrer lehrer;

	/** Die erteilte Note. */
	protected Note note;

	/** Die Wochenstundenzahl, mir der das Fach belegt wurde */
	protected int wochenstunden;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param bilingualeSprache Das einstellige Kürzel der bilingualen Sprache, sofern das Fach bilingual unterrichtet wurde.
	 * @param block1gewertet Gibt an, ob die Belegung für den Block I gewertet wird oder nicht - nicht alle Kursbelegungen müssen laut Prüfungsordnung in die Abiturnote einfliessen
	 * @param block1kursAufZeugnis Gibt an, ob die Belegung des Kurses auf dem Abiturzeugnis angezeigt werden soll oder nicht. Eine Belegung kann auf Wunsch des Prüflings bei nicht gewerteten Kursen nicht auf dem Zeugnis erscheinen.
	 * @param fehlstundenGesamt Die Anzahl der Fehlstunden.
	 * @param fehlstundenUnentschuldigt Die Anzahl der unentschuldigten Fehlstunden.
	 * @param halbjahrKuerzel Das Kürzel des Halbjahres der Fachbelegung
	 * @param istSchriftlich Angabe, ob das schriftlich belegt wurde oder nicht.
	 * @param kursartKuerzel Das Kürzel der Kursart der gymnasialen Oberstufe dieser Fachbelegung
	 * @param lehrer Die Lehrkraft, welche die Note erteilt.
	 * @param note Die erteilte Note.
	 * @param wochenstunden Die Wochenstundenzahl, mir der das Fach belegt wurde
	 */
	public ReportingGostAbiturFachbelegungHalbjahr(final String bilingualeSprache, final Boolean block1gewertet, final Boolean block1kursAufZeugnis,
			final int fehlstundenGesamt, final int fehlstundenUnentschuldigt, final String halbjahrKuerzel, final boolean istSchriftlich,
			final String kursartKuerzel, final ReportingLehrer lehrer, final Note note, final int wochenstunden) {
		this.bilingualeSprache = bilingualeSprache;
		this.block1gewertet = block1gewertet;
		this.block1kursAufZeugnis = block1kursAufZeugnis;
		this.fehlstundenGesamt = fehlstundenGesamt;
		this.fehlstundenUnentschuldigt = fehlstundenUnentschuldigt;
		this.halbjahrKuerzel = halbjahrKuerzel;
		this.istSchriftlich = istSchriftlich;
		this.kursartKuerzel = kursartKuerzel;
		this.lehrer = lehrer;
		this.note = note;
		this.wochenstunden = wochenstunden;
	}



	// ##### Getter #####

	/**
	 * Das einstellige Kürzel der bilingualen Sprache, sofern das Fach bilingual unterrichtet wurde.
	 *
	 * @return Inhalt des Feldes bilingualeSprache
	 */
	public String bilingualeSprache() {
		return bilingualeSprache;
	}

	/**
	 * Gibt an, ob die Belegung für den Block I gewertet wird oder nicht - nicht alle Kursbelegungen müssen laut Prüfungsordnung in die Abiturnote einfliessen
	 *
	 * @return Inhalt des Feldes block1gewertet
	 */
	public Boolean block1gewertet() {
		return block1gewertet;
	}

	/**
	 * Gibt an, ob die Belegung des Kurses auf dem Abiturzeugnis angezeigt werden soll oder nicht. Eine Belegung kann auf Wunsch des Prüflings bei nicht gewerteten Kursen nicht auf dem Zeugnis erscheinen.
	 *
	 * @return Inhalt des Feldes block1kursAufZeugnis
	 */
	public Boolean block1kursAufZeugnis() {
		return block1kursAufZeugnis;
	}

	/**
	 * Die Anzahl der Fehlstunden.
	 *
	 * @return Inhalt des Feldes fehlstundenGesamt
	 */
	public int fehlstundenGesamt() {
		return fehlstundenGesamt;
	}

	/**
	 * Die Anzahl der unentschuldigten Fehlstunden.
	 *
	 * @return Inhalt des Feldes fehlstundenUnentschuldigt
	 */
	public int fehlstundenUnentschuldigt() {
		return fehlstundenUnentschuldigt;
	}

	/**
	 * Das Kürzel des Halbjahres der Fachbelegung
	 *
	 * @return Inhalt des Feldes halbjahrKuerzel
	 */
	public String halbjahrKuerzel() {
		return halbjahrKuerzel;
	}

	/**
	 * Angabe, ob das schriftlich belegt wurde oder nicht.
	 *
	 * @return Inhalt des Feldes istSchriftlich
	 */
	public boolean istSchriftlich() {
		return istSchriftlich;
	}

	/**
	 * Das Kürzel der Kursart der gymnasialen Oberstufe dieser Fachbelegung
	 *
	 * @return Inhalt des Feldes kursartKuerzel
	 */
	public String kursartKuerzel() {
		return kursartKuerzel;
	}

	/**
	 * Die Lehrkraft, welche die Note erteilt.
	 *
	 * @return Inhalt des Feldes lehrer
	 */
	public ReportingLehrer lehrer() {
		return lehrer;
	}

	/**
	 * Die erteilte Note.
	 *
	 * @return Inhalt des Feldes note
	 */
	public Note note() {
		return note;
	}

	/**
	 * Die Wochenstundenzahl, mir der das Fach belegt wurde
	 *
	 * @return Inhalt des Feldes wochenstunden
	 */
	public int wochenstunden() {
		return wochenstunden;
	}

}
