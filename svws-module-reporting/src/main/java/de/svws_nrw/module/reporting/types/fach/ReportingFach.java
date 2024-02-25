package de.svws_nrw.module.reporting.types.fach;

import de.svws_nrw.core.types.fach.Fachgruppe;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.gost.GostFachbereich;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ Fach.</p>
 * <p>Sie enthält die Grunddaten eines Faches.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingFach {

	/** Das Aufgabenfeld am Berufskolleg, zu welchem das Fach gehört */
	private String aufgabenfeld;

	/** Gibt an, ob das Fach auf einem Zeugnis erscheinen soll. */
	private boolean aufZeugnis;

	/** Die Bezeichnung des Faches */
	private String bezeichnung;

	/** Die Bezeichnung des Faches auf Überweisungszeugnissen */
	private String bezeichnungUeberweisungszeugnis;

	/** Die Bezeichnung des Faches auf allgemeinen Zeugnissen */
	private String bezeichnungZeugnis;

	/** Die Sprache (das einstellige Fremdsprachenkürzel) an, in der das Fach unterrichtet wird, sofern es sich um ein bilinguales Sachfach handelt. */
	private String bilingualeSprache;

	/** Die Fachgruppe, der das Fach angehört. */
	private Fachgruppe fachgruppe;

	/** Gibt an, ob das Fach ggf. bei der Aggregation von Leistungen aus früheren Lernabschnitten/Jahrgängen für eine Abschlussberechnung berücksichtigt wird, sofern es im aktuellen Abschnitt nicht belegt wurde. */
	private boolean holeAusAltenLernabschnitten;

	/** Die ID des Faches. */
	private long id;

	/** Gibt an, ob das Fach bei der Berechnung der FHR berücksichtigt wird oder nicht (Berufskolleg). */
	private boolean istFHRFach;

	/** Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht */
	private boolean istFremdsprache;

	/** Gibt an, ob das Fach eine neu einsetzende Fremdsprache ist. */
	private boolean istFremdSpracheNeuEinsetzend;

	/** Gibt an, ob es sich um ein Fach der Oberstufe handelt oder nicht. */
	private boolean istGostFach;

	/** Gibt an, ob eine Nachprüfung in diesem Fach möglich ist. */
	private boolean istNachpruefungErlaubt;

	/** Gibt an, ob es sich um ein Fach handelt, welches relevant für die Prüfungsordnung ist oder nicht (z.B. bei Belegprüfungen). */
	private boolean istPruefungsordnungsRelevant;

	/** Gibt an, ob das Fach als schriftliches Fach für den Berufsabschluss gewertet wird (Berufskolleg). */
	private boolean istSchriftlichBA;

	/** Gibt an, ob das Fach ein schriftliches Fach für die zentralen Klausuren ist oder nicht. */
	private boolean istSchriftlichZK;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	private boolean istSichtbar;

	/** Das eindeutige Kürzel des Faches */
	private String kuerzel;

	/** Gibt die maximale Anzahl an Zeichen an, doe in Fachbemerkungen genutzt werden dürfen. */
	private int maxZeichenInFachbemerkungen;

	/** Die Sortierreihenfolge des Fächerlisten-Eintrags. */
	private int sortierung;

	/** Das Statistik-Kürzel des Faches */
	private ZulaessigesFach statistikfach;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param aufgabenfeld Das Aufgabenfeld am Berufskolleg, zu welchem das Fach gehört
	 * @param aufZeugnis Gibt an, ob das Fach auf einem Zeugnis erscheinen soll.
	 * @param bezeichnung Die Bezeichnung des Faches
	 * @param bezeichnungUeberweisungszeugnis Die Bezeichnung des Faches auf Überweisungszeugnissen
	 * @param bezeichnungZeugnis Die Bezeichnung des Faches auf allgemeinen Zeugnissen
	 * @param bilingualeSprache Die Sprache (das einstellige Fremdsprachenkürzel) an, in der das Fach unterrichtet wird, sofern es sich um ein bilinguales Sachfach handelt.
	 * @param fachgruppe Die Fachgruppe, der das Fach angehört.
	 * @param holeAusAltenLernabschnitten Gibt an, ob das Fach ggf. bei der Aggregation von Leistungen aus früheren Lernabschnitten/Jahrgängen für eine Abschlussberechnung berücksichtigt wird, sofern es im aktuellen Abschnitt nicht belegt wurde.
	 * @param id Die ID des Faches.
	 * @param istFHRFach Gibt an, ob das Fach bei der Berechnung der FHR berücksichtigt wird oder nicht (Berufskolleg).
	 * @param istFremdsprache Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht
	 * @param istFremdSpracheNeuEinsetzend Gibt an, ob das Fach eine neu einsetzende Fremdsprache ist.
	 * @param istGostFach Gibt an, ob es sich um ein Fach der Oberstufe handelt oder nicht.
	 * @param istNachpruefungErlaubt Gibt an, ob eine Nachprüfung in diesem Fach möglich ist.
	 * @param istPruefungsordnungsRelevant Gibt an, ob es sich um ein Fach handelt, welches relevant für die Prüfungsordnung ist oder nicht (z.B. bei Belegprüfungen).
	 * @param istSchriftlichBA Gibt an, ob das Fach als schriftliches Fach für den Berufsabschluss gewertet wird (Berufskolleg).
	 * @param istSchriftlichZK Gibt an, ob das Fach ein schriftliches Fach für die zentralen Klausuren ist oder nicht.
	 * @param istSichtbar Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 * @param kuerzel Das eindeutige Kürzel des Faches
	 * @param maxZeichenInFachbemerkungen Gibt die maximale Anzahl an Zeichen an, doe in Fachbemerkungen genutzt werden dürfen.
	 * @param sortierung Die Sortierreihenfolge des Fächerlisten-Eintrags.
	 * @param statistikfach Das Statistik-Fach des Faches
	 */
	public ReportingFach(final String aufgabenfeld, final boolean aufZeugnis, final String bezeichnung, final String bezeichnungUeberweisungszeugnis, final String bezeichnungZeugnis, final String bilingualeSprache, final Fachgruppe fachgruppe, final boolean holeAusAltenLernabschnitten, final long id, final boolean istFHRFach, final boolean istFremdsprache, final boolean istFremdSpracheNeuEinsetzend, final boolean istGostFach, final boolean istNachpruefungErlaubt, final boolean istPruefungsordnungsRelevant, final boolean istSchriftlichBA, final boolean istSchriftlichZK, final boolean istSichtbar, final String kuerzel, final int maxZeichenInFachbemerkungen, final int sortierung, final ZulaessigesFach statistikfach) {
		this.aufgabenfeld = aufgabenfeld;
		this.aufZeugnis = aufZeugnis;
		this.bezeichnung = bezeichnung;
		this.bezeichnungUeberweisungszeugnis = bezeichnungUeberweisungszeugnis;
		this.bezeichnungZeugnis = bezeichnungZeugnis;
		this.bilingualeSprache = bilingualeSprache;
		this.fachgruppe = fachgruppe;
		this.holeAusAltenLernabschnitten = holeAusAltenLernabschnitten;
		this.id = id;
		this.istFHRFach = istFHRFach;
		this.istFremdsprache = istFremdsprache;
		this.istFremdSpracheNeuEinsetzend = istFremdSpracheNeuEinsetzend;
		this.istGostFach = istGostFach;
		this.istNachpruefungErlaubt = istNachpruefungErlaubt;
		this.istPruefungsordnungsRelevant = istPruefungsordnungsRelevant;
		this.istSchriftlichBA = istSchriftlichBA;
		this.istSchriftlichZK = istSchriftlichZK;
		this.istSichtbar = istSichtbar;
		this.kuerzel = kuerzel;
		this.maxZeichenInFachbemerkungen = maxZeichenInFachbemerkungen;
		this.sortierung = sortierung;
		this.statistikfach = statistikfach;
	}



	/**
	 * Vergleicht zwei ReportingFächer hinsichtlich ihrer Fachsortierung in der GOSt.
	 * @param fach1	Erstes ReportingFach
	 * @param fach2 Zweites ReportingFach
	 * @return int-Wert des Vergleiches gemäß {@link Comparable#compareTo(Object)}
	 */
	public static int compareToGost(final ReportingFach fach1, final ReportingFach fach2) {
		return GostFachbereich.compareFach(fach1.statistikfach(), fach2.statistikfach());
	}



	// ##### Getter und Setter #####
	/**
	 * Das Aufgabenfeld am Berufskolleg, zu welchem das Fach gehört
	 * @return Inhalt des Feldes aufgabenfeld
	 */
	public String aufgabenfeld() {
		return aufgabenfeld;
	}

	/**
	 * Das Aufgabenfeld am Berufskolleg, zu welchem das Fach gehört wird gesetzt.
	 * @param aufgabenfeld Neuer Wert für das Feld aufgabenfeld
	 */
	public void setAufgabenfeld(final String aufgabenfeld) {
		this.aufgabenfeld = aufgabenfeld;
	}

	/**
	 * Gibt an, ob das Fach auf einem Zeugnis erscheinen soll.
	 * @return Inhalt des Feldes aufZeugnis
	 */
	public boolean aufZeugnis() {
		return aufZeugnis;
	}

	/**
	 * Gibt an, ob das Fach auf einem Zeugnis erscheinen soll wird gesetzt.
	 * @param aufZeugnis Neuer Wert für das Feld aufZeugnis
	 */
	public void setAufZeugnis(final boolean aufZeugnis) {
		this.aufZeugnis = aufZeugnis;
	}

	/**
	 * Die Bezeichnung des Faches
	 * @return Inhalt des Feldes bezeichnung
	 */
	public String bezeichnung() {
		return bezeichnung;
	}

	/**
	 * Die Bezeichnung des Faches wird gesetzt.
	 * @param bezeichnung Neuer Wert für das Feld bezeichnung
	 */
	public void setBezeichnung(final String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Die Bezeichnung des Faches auf Überweisungszeugnissen
	 * @return Inhalt des Feldes bezeichnungUeberweisungszeugnis
	 */
	public String bezeichnungUeberweisungszeugnis() {
		return bezeichnungUeberweisungszeugnis;
	}

	/**
	 * Die Bezeichnung des Faches auf Überweisungszeugnissen wird gesetzt.
	 * @param bezeichnungUeberweisungszeugnis Neuer Wert für das Feld bezeichnungUeberweisungszeugnis
	 */
	public void setBezeichnungUeberweisungszeugnis(final String bezeichnungUeberweisungszeugnis) {
		this.bezeichnungUeberweisungszeugnis = bezeichnungUeberweisungszeugnis;
	}

	/**
	 * Die Bezeichnung des Faches auf allgemeinen Zeugnissen
	 * @return Inhalt des Feldes bezeichnungZeugnis
	 */
	public String bezeichnungZeugnis() {
		return bezeichnungZeugnis;
	}

	/**
	 * Die Bezeichnung des Faches auf allgemeinen Zeugnissen wird gesetzt.
	 * @param bezeichnungZeugnis Neuer Wert für das Feld bezeichnungZeugnis
	 */
	public void setBezeichnungZeugnis(final String bezeichnungZeugnis) {
		this.bezeichnungZeugnis = bezeichnungZeugnis;
	}

	/**
	 * Die Sprache (das einstellige Fremdsprachenkürzel) an, in der das Fach unterrichtet wird, sofern es sich um ein bilinguales Sachfach handelt.
	 * @return Inhalt des Feldes bilingualeSprache
	 */
	public String bilingualeSprache() {
		return bilingualeSprache;
	}

	/**
	 * Die Sprache (das einstellige Fremdsprachenkürzel) an, in der das Fach unterrichtet wird, sofern es sich um ein bilinguales Sachfach handelt wird gesetzt.
	 * @param bilingualeSprache Neuer Wert für das Feld bilingualeSprache
	 */
	public void setBilingualeSprache(final String bilingualeSprache) {
		this.bilingualeSprache = bilingualeSprache;
	}

	/**
	 * Die Fachgruppe, der das Fach angehört.
	 * @return Fachgruppe des Faches
	 */
	public Fachgruppe fachgruppe() {
		return fachgruppe;
	}

	/**
	 * Die Fachgruppe, der das Fach angehört, wird gesetzt.
	 * @param fachgruppe Neuer Wert für das Feld fachgruppe
	 */
	public void setFachgruppe(final Fachgruppe fachgruppe) {
		this.fachgruppe = fachgruppe;
	}

	/**
	 * Gibt an, ob das Fach ggf. bei der Aggregation von Leistungen aus früheren Lernabschnitten/Jahrgängen für eine Abschlussberechnung berücksichtigt wird, sofern es im aktuellen Abschnitt nicht belegt wurde.
	 * @return Inhalt des Feldes holeAusAltenLernabschnitten
	 */
	public boolean holeAusAltenLernabschnitten() {
		return holeAusAltenLernabschnitten;
	}

	/**
	 * Gibt an, ob das Fach ggf bei der Aggregation von Leistungen aus früheren Lernabschnitten/Jahrgängen für eine Abschlussberechnung berücksichtigt wird, sofern es im aktuellen Abschnitt nicht belegt wurde wird gesetzt.
	 * @param holeAusAltenLernabschnitten Neuer Wert für das Feld holeAusAltenLernabschnitten
	 */
	public void setHoleAusAltenLernabschnitten(final boolean holeAusAltenLernabschnitten) {
		this.holeAusAltenLernabschnitten = holeAusAltenLernabschnitten;
	}

	/**
	 * Die ID des Faches.
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Die ID des Faches wird gesetzt.
	 * @param id Neuer Wert für das Feld id
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * Gibt an, ob das Fach bei der Berechnung der FHR berücksichtigt wird oder nicht (Berufskolleg).
	 * @return Inhalt des Feldes istFHRFach
	 */
	public boolean istFHRFach() {
		return istFHRFach;
	}

	/**
	 * Gibt an, ob das Fach bei der Berechnung der FHR berücksichtigt wird oder nicht (Berufskolleg) wird gesetzt.
	 * @param istFHRFach Neuer Wert für das Feld istFHRFach
	 */
	public void setIstFHRFach(final boolean istFHRFach) {
		this.istFHRFach = istFHRFach;
	}

	/**
	 * Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht
	 * @return Inhalt des Feldes istFremdsprache
	 */
	public boolean istFremdsprache() {
		return istFremdsprache;
	}

	/**
	 * Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht wird gesetzt.
	 * @param istFremdsprache Neuer Wert für das Feld istFremdsprache
	 */
	public void setIstFremdsprache(final boolean istFremdsprache) {
		this.istFremdsprache = istFremdsprache;
	}

	/**
	 * Gibt an, ob das Fach eine neu einsetzende Fremdsprache ist.
	 * @return Inhalt des Feldes istFremdSpracheNeuEinsetzend
	 */
	public boolean istFremdSpracheNeuEinsetzend() {
		return istFremdSpracheNeuEinsetzend;
	}

	/**
	 * Gibt an, ob das Fach eine neu einsetzende Fremdsprache ist wird gesetzt.
	 * @param istFremdSpracheNeuEinsetzend Neuer Wert für das Feld istFremdSpracheNeuEinsetzend
	 */
	public void setIstFremdSpracheNeuEinsetzend(final boolean istFremdSpracheNeuEinsetzend) {
		this.istFremdSpracheNeuEinsetzend = istFremdSpracheNeuEinsetzend;
	}

	/**
	 * Gibt an, ob es sich um ein Fach der Oberstufe handelt oder nicht.
	 * @return Inhalt des Feldes istGostFach
	 */
	public boolean istGostFach() {
		return istGostFach;
	}

	/**
	 * Gibt an, ob es sich um ein Fach der Oberstufe handelt oder nicht wird gesetzt.
	 * @param istGostFach Neuer Wert für das Feld istGostFach
	 */
	public void setIstGostFach(final boolean istGostFach) {
		this.istGostFach = istGostFach;
	}

	/**
	 * Gibt an, ob eine Nachprüfung in diesem Fach möglich ist.
	 * @return Inhalt des Feldes istNachpruefungErlaubt
	 */
	public boolean istNachpruefungErlaubt() {
		return istNachpruefungErlaubt;
	}

	/**
	 * Gibt an, ob eine Nachprüfung in diesem Fach möglich ist wird gesetzt.
	 * @param istNachpruefungErlaubt Neuer Wert für das Feld istNachpruefungErlaubt
	 */
	public void setIstNachpruefungErlaubt(final boolean istNachpruefungErlaubt) {
		this.istNachpruefungErlaubt = istNachpruefungErlaubt;
	}

	/**
	 * Gibt an, ob es sich um ein Fach handelt, welches relevant für die Prüfungsordnung ist oder nicht (z.B. bei Belegprüfungen).
	 * @return Inhalt des Feldes istPruefungsordnungsRelevant
	 */
	public boolean istPruefungsordnungsRelevant() {
		return istPruefungsordnungsRelevant;
	}

	/**
	 * Gibt an, ob es sich um ein Fach handelt, welches relevant für die Prüfungsordnung ist oder nicht (zB bei Belegprüfungen) wird gesetzt.
	 * @param istPruefungsordnungsRelevant Neuer Wert für das Feld istPruefungsordnungsRelevant
	 */
	public void setIstPruefungsordnungsRelevant(final boolean istPruefungsordnungsRelevant) {
		this.istPruefungsordnungsRelevant = istPruefungsordnungsRelevant;
	}

	/**
	 * Gibt an, ob das Fach als schriftliches Fach für den Berufsabschluss gewertet wird (Berufskolleg).
	 * @return Inhalt des Feldes istSchriftlichBA
	 */
	public boolean istSchriftlichBA() {
		return istSchriftlichBA;
	}

	/**
	 * Gibt an, ob das Fach als schriftliches Fach für den Berufsabschluss gewertet wird (Berufskolleg) wird gesetzt.
	 * @param istSchriftlichBA Neuer Wert für das Feld istSchriftlichBA
	 */
	public void setIstSchriftlichBA(final boolean istSchriftlichBA) {
		this.istSchriftlichBA = istSchriftlichBA;
	}

	/**
	 * Gibt an, ob das Fach ein schriftliches Fach für die zentralen Klausuren ist oder nicht.
	 * @return Inhalt des Feldes istSchriftlichZK
	 */
	public boolean istSchriftlichZK() {
		return istSchriftlichZK;
	}

	/**
	 * Gibt an, ob das Fach ein schriftliches Fach für die zentralen Klausuren ist oder nicht wird gesetzt.
	 * @param istSchriftlichZK Neuer Wert für das Feld istSchriftlichZK
	 */
	public void setIstSchriftlichZK(final boolean istSchriftlichZK) {
		this.istSchriftlichZK = istSchriftlichZK;
	}

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 * @return Inhalt des Feldes istSichtbar
	 */
	public boolean istSichtbar() {
		return istSichtbar;
	}

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht wird gesetzt.
	 * @param istSichtbar Neuer Wert für das Feld istSichtbar
	 */
	public void setIstSichtbar(final boolean istSichtbar) {
		this.istSichtbar = istSichtbar;
	}

	/**
	 * Das eindeutige Kürzel des Faches
	 * @return Inhalt des Feldes kuerzel
	 */
	public String kuerzel() {
		return kuerzel;
	}

	/**
	 * Das eindeutige Kürzel des Faches wird gesetzt.
	 * @param kuerzel Neuer Wert für das Feld kuerzel
	 */
	public void setKuerzel(final String kuerzel) {
		this.kuerzel = kuerzel;
	}

	/**
	 * Gibt die maximale Anzahl an Zeichen an, doe in Fachbemerkungen genutzt werden dürfen.
	 * @return Inhalt des Feldes maxZeichenInFachbemerkungen
	 */
	public int maxZeichenInFachbemerkungen() {
		return maxZeichenInFachbemerkungen;
	}

	/**
	 * Gibt die maximale Anzahl an Zeichen an, doe in Fachbemerkungen genutzt werden dürfen wird gesetzt.
	 * @param maxZeichenInFachbemerkungen Neuer Wert für das Feld maxZeichenInFachbemerkungen
	 */
	public void setMaxZeichenInFachbemerkungen(final int maxZeichenInFachbemerkungen) {
		this.maxZeichenInFachbemerkungen = maxZeichenInFachbemerkungen;
	}

	/**
	 * Die Sortierreihenfolge des Fächerlisten-Eintrags.
	 * @return Inhalt des Feldes sortierung
	 */
	public int sortierung() {
		return sortierung;
	}

	/**
	 * Die Sortierreihenfolge des Fächerlisten-Eintrags wird gesetzt.
	 * @param sortierung Neuer Wert für das Feld sortierung
	 */
	public void setSortierung(final int sortierung) {
		this.sortierung = sortierung;
	}

	/**
	 * Das Statistik-Fach des Faches
	 * @return Inhalt des Feldes statistikfach
	 */
	public ZulaessigesFach statistikfach() {
		return statistikfach;
	}

	/**
	 * Das Statistik-Fach des Faches wird gesetzt.
	 * @param statistikfach Neuer Wert für das Feld statistikfach
	 */
	public void setStatistikfach(final ZulaessigesFach statistikfach) {
		this.statistikfach = statistikfach;
	}

}
