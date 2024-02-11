package de.svws_nrw.module.reporting.types.gost.abitur;

import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostAbiturFachbelegungHalbjahr.</p>
 * <p>Sie enthält die Daten zur Belegung eines Faches in einem Halbjahr durch einen Schüler im Rahmen des Abiturs der gymnasialen Oberstufe.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 *  einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostAbiturFachbelegungHalbjahr {

	/** Das einstellige Kürzel der bilingualen Sprache, sofern das Fach bilingual unterrichtet wurde. */
	private String bilingualeSprache;

	/** Gibt an, ob die Belegung für den Block I gewertet wird oder nicht - nicht alle Kursbelegungen müssen laut Prüfungsordnung in die Abiturnote einfliessen */
	private Boolean block1gewertet;

	/** Gibt an, ob die Belegung des Kurses auf dem Abiturzeugnis angezeigt werden soll oder nicht. Eine Belegung kann auf Wunsch des Prüflings bei nicht gewerteten Kursen nicht auf dem Zeugnis erscheinen. */
	private Boolean block1kursAufZeugnis;

	/** Die Anzahl der Fehlstunden. */
	private int fehlstundenGesamt;

	/** Die Anzahl der unentschuldigten Fehlstunden. */
	private int fehlstundenUnentschuldigt;

	/** Das Kürzel des Halbjahres der Fachbelegung */
	private String halbjahrKuerzel;

	/** Angabe, ob das schriftlich belegt wurde oder nicht. */
	private boolean istSchriftlich;

	/** Das Kürzel der Kursart der gymnasialen Oberstufe dieser Fachbelegung  */
	private String kursartKuerzel;

	/** Die Lehrkraft, welche die Note erteilt. */
	private ReportingLehrer lehrer;

	/** Das Notenkürzel der erteilten Note. Das Kürzel ist ein leerer String, falls keine Note in den Leistungsdaten gesetzt ist. Der Wert null ist nur zulässig, wenn Fachwahlen vorliegen, für die keine Leistungsdaten hinterlegt sind. */
	private String notenkuerzel;

	/** Die Wochenstundenzahl, mir der das Fach belegt wurde */
	private int wochenstunden;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param bilingualeSprache Das einstellige Kürzel der bilingualen Sprache, sofern das Fach bilingual unterrichtet wurde.
	 * @param block1gewertet Gibt an, ob die Belegung für den Block I gewertet wird oder nicht - nicht alle Kursbelegungen müssen laut Prüfungsordnung in die Abiturnote einfliessen
	 * @param block1kursAufZeugnis Gibt an, ob die Belegung des Kurses auf dem Abiturzeugnis angezeigt werden soll oder nicht. Eine Belegung kann auf Wunsch des Prüflings bei nicht gewerteten Kursen nicht auf dem Zeugnis erscheinen.
	 * @param fehlstundenGesamt Die Anzahl der Fehlstunden.
	 * @param fehlstundenUnentschuldigt Die Anzahl der unentschuldigten Fehlstunden.
	 * @param halbjahrKuerzel Das Kürzel des Halbjahres der Fachbelegung
	 * @param istSchriftlich Angabe, ob das schriftlich belegt wurde oder nicht.
	 * @param kursartKuerzel Das Kürzel der Kursart der gymnasialen Oberstufe dieser Fachbelegung
	 * @param lehrer Die Lehrkraft, welche die Note erteilt.
	 * @param notenkuerzel Das Notenkürzel der erteilten Note. Das Kürzel ist ein leerer String, falls keine Note in den Leistungsdaten gesetzt ist. Der Wert null ist nur zulässig, wenn Fachwahlen vorliegen, für die keine Leistungsdaten hinterlegt sind.
	 * @param wochenstunden Die Wochenstundenzahl, mir der das Fach belegt wurde
	 */
	public ReportingGostAbiturFachbelegungHalbjahr(final String bilingualeSprache, final Boolean block1gewertet, final Boolean block1kursAufZeugnis, final int fehlstundenGesamt, final int fehlstundenUnentschuldigt, final String halbjahrKuerzel, final boolean istSchriftlich, final String kursartKuerzel, final ReportingLehrer lehrer, final String notenkuerzel, final int wochenstunden) {
		this.bilingualeSprache = bilingualeSprache;
		this.block1gewertet = block1gewertet;
		this.block1kursAufZeugnis = block1kursAufZeugnis;
		this.fehlstundenGesamt = fehlstundenGesamt;
		this.fehlstundenUnentschuldigt = fehlstundenUnentschuldigt;
		this.halbjahrKuerzel = halbjahrKuerzel;
		this.istSchriftlich = istSchriftlich;
		this.kursartKuerzel = kursartKuerzel;
		this.lehrer = lehrer;
		this.notenkuerzel = notenkuerzel;
		this.wochenstunden = wochenstunden;
	}



	// ##### Getter und Setter #####

	/**
	 * Das einstellige Kürzel der bilingualen Sprache, sofern das Fach bilingual unterrichtet wurde.
	 * @return Inhalt des Feldes bilingualeSprache
	 */
	public String bilingualeSprache() {
		return bilingualeSprache;
	}

	/**
	 * Das einstellige Kürzel der bilingualen Sprache, sofern das Fach bilingual unterrichtet wurde wird gesetzt.
	 * @param bilingualeSprache Neuer Wert für das Feld bilingualeSprache
	 */
	public void setBilingualeSprache(final String bilingualeSprache) {
		this.bilingualeSprache = bilingualeSprache;
	}

	/**
	 * Gibt an, ob die Belegung für den Block I gewertet wird oder nicht - nicht alle Kursbelegungen müssen laut Prüfungsordnung in die Abiturnote einfliessen
	 * @return Inhalt des Feldes block1gewertet
	 */
	public Boolean block1gewertet() {
		return block1gewertet;
	}

	/**
	 * Gibt an, ob die Belegung für den Block I gewertet wird oder nicht - nicht alle Kursbelegungen müssen laut Prüfungsordnung in die Abiturnote einfliessen wird gesetzt.
	 * @param block1gewertet Neuer Wert für das Feld block1gewertet
	 */
	public void setBlock1gewertet(final Boolean block1gewertet) {
		this.block1gewertet = block1gewertet;
	}

	/**
	 * Gibt an, ob die Belegung des Kurses auf dem Abiturzeugnis angezeigt werden soll oder nicht. Eine Belegung kann auf Wunsch des Prüflings bei nicht gewerteten Kursen nicht auf dem Zeugnis erscheinen.
	 * @return Inhalt des Feldes block1kursAufZeugnis
	 */
	public Boolean block1kursAufZeugnis() {
		return block1kursAufZeugnis;
	}

	/**
	 * Gibt an, ob die Belegung des Kurses auf dem Abiturzeugnis angezeigt werden soll oder nicht Eine Belegung kann auf Wunsch des Prüflings bei nicht gewerteten Kursen nicht auf dem Zeugnis erscheinen wird gesetzt.
	 * @param block1kursAufZeugnis Neuer Wert für das Feld block1kursAufZeugnis
	 */
	public void setBlock1kursAufZeugnis(final Boolean block1kursAufZeugnis) {
		this.block1kursAufZeugnis = block1kursAufZeugnis;
	}

	/**
	 * Die Anzahl der Fehlstunden.
	 * @return Inhalt des Feldes fehlstundenGesamt
	 */
	public int fehlstundenGesamt() {
		return fehlstundenGesamt;
	}

	/**
	 * Die Anzahl der Fehlstunden wird gesetzt.
	 * @param fehlstundenGesamt Neuer Wert für das Feld fehlstundenGesamt
	 */
	public void setFehlstundenGesamt(final int fehlstundenGesamt) {
		this.fehlstundenGesamt = fehlstundenGesamt;
	}

	/**
	 * Die Anzahl der unentschuldigten Fehlstunden.
	 * @return Inhalt des Feldes fehlstundenUnentschuldigt
	 */
	public int fehlstundenUnentschuldigt() {
		return fehlstundenUnentschuldigt;
	}

	/**
	 * Die Anzahl der unentschuldigten Fehlstunden wird gesetzt.
	 * @param fehlstundenUnentschuldigt Neuer Wert für das Feld fehlstundenUnentschuldigt
	 */
	public void setFehlstundenUnentschuldigt(final int fehlstundenUnentschuldigt) {
		this.fehlstundenUnentschuldigt = fehlstundenUnentschuldigt;
	}

	/**
	 * Das Kürzel des Halbjahres der Fachbelegung
	 * @return Inhalt des Feldes halbjahrKuerzel
	 */
	public String halbjahrKuerzel() {
		return halbjahrKuerzel;
	}

	/**
	 * Das Kürzel des Halbjahres der Fachbelegung wird gesetzt.
	 * @param halbjahrKuerzel Neuer Wert für das Feld halbjahrKuerzel
	 */
	public void setHalbjahrKuerzel(final String halbjahrKuerzel) {
		this.halbjahrKuerzel = halbjahrKuerzel;
	}

	/**
	 * Angabe, ob das schriftlich belegt wurde oder nicht.
	 * @return Inhalt des Feldes istSchriftlich
	 */
	public boolean istSchriftlich() {
		return istSchriftlich;
	}

	/**
	 * Angabe, ob das schriftlich belegt wurde oder nicht wird gesetzt.
	 * @param istSchriftlich Neuer Wert für das Feld istSchriftlich
	 */
	public void setIstSchriftlich(final boolean istSchriftlich) {
		this.istSchriftlich = istSchriftlich;
	}

	/**
	 * Das Kürzel der Kursart der gymnasialen Oberstufe dieser Fachbelegung
	 * @return Inhalt des Feldes kursartKuerzel
	 */
	public String kursartKuerzel() {
		return kursartKuerzel;
	}

	/**
	 * Das Kürzel der Kursart der gymnasialen Oberstufe dieser Fachbelegung wird gesetzt.
	 * @param kursartKuerzel Neuer Wert für das Feld kursartKuerzel
	 */
	public void setKursartKuerzel(final String kursartKuerzel) {
		this.kursartKuerzel = kursartKuerzel;
	}

	/**
	 * Die Lehrkraft, welche die Note erteilt.
	 * @return Inhalt des Feldes lehrer
	 */
	public ReportingLehrer lehrer() {
		return lehrer;
	}

	/**
	 * Die Lehrkraft, welche die Note erteilt wird gesetzt.
	 * @param lehrer Neuer Wert für das Feld lehrer
	 */
	public void setLehrer(final ReportingLehrer lehrer) {
		this.lehrer = lehrer;
	}

	/**
	 * Das Notenkürzel der erteilten Note. Das Kürzel ist ein leerer String, falls keine Note in den Leistungsdaten gesetzt ist. Der Wert null ist nur zulässig, wenn Fachwahlen vorliegen, für die keine Leistungsdaten hinterlegt sind.
	 * @return Inhalt des Feldes notenkuerzel
	 */
	public String notenkuerzel() {
		return notenkuerzel;
	}

	/**
	 * Das Notenkürzel der erteilten Note Das Kürzel ist ein leerer String, falls keine Note in den Leistungsdaten gesetzt ist Der Wert null ist nur zulässig, wenn Fachwahlen vorliegen, für die keine Leistungsdaten hinterlegt sind wird gesetzt.
	 * @param notenkuerzel Neuer Wert für das Feld notenkuerzel
	 */
	public void setNotenkuerzel(final String notenkuerzel) {
		this.notenkuerzel = notenkuerzel;
	}

	/**
	 * Die Wochenstundenzahl, mir der das Fach belegt wurde
	 * @return Inhalt des Feldes wochenstunden
	 */
	public int wochenstunden() {
		return wochenstunden;
	}

	/**
	 * Die Wochenstundenzahl, mir der das Fach belegt wurde wird gesetzt.
	 * @param wochenstunden Neuer Wert für das Feld wochenstunden
	 */
	public void setWochenstunden(final int wochenstunden) {
		this.wochenstunden = wochenstunden;
	}
}
