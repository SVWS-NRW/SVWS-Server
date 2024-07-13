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
	protected String aufgabenfeld;

	/** Gibt an, ob das Fach auf einem Zeugnis erscheinen soll. */
	protected boolean aufZeugnis;

	/** Die Bezeichnung des Faches */
	protected String bezeichnung;

	/** Die Bezeichnung des Faches auf Überweisungszeugnissen */
	protected String bezeichnungUeberweisungszeugnis;

	/** Die Bezeichnung des Faches auf allgemeinen Zeugnissen */
	protected String bezeichnungZeugnis;

	/** Die Sprache (das einstellige Fremdsprachenkürzel) an, in der das Fach unterrichtet wird, sofern es sich um ein bilinguales Sachfach handelt. */
	protected String bilingualeSprache;

	/** Die Fachgruppe, der das Fach angehört. */
	protected Fachgruppe fachgruppe;

	/** Gibt an, ob das Fach ggf. bei der Aggregation von Leistungen aus früheren Lernabschnitten/Jahrgängen für eine Abschlussberechnung berücksichtigt wird, sofern es im aktuellen Abschnitt nicht belegt wurde. */
	protected boolean holeAusAltenLernabschnitten;

	/** Die ID des Faches. */
	protected long id;

	/** Gibt an, ob das Fach bei der Berechnung der FHR berücksichtigt wird oder nicht (Berufskolleg). */
	protected boolean istFHRFach;

	/** Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht */
	protected boolean istFremdsprache;

	/** Gibt an, ob das Fach eine neu einsetzende Fremdsprache ist. */
	protected boolean istFremdSpracheNeuEinsetzend;

	/** Gibt an, ob es sich um ein Fach der Oberstufe handelt oder nicht. */
	protected boolean istGostFach;

	/** Gibt an, ob eine Nachprüfung in diesem Fach möglich ist. */
	protected boolean istNachpruefungErlaubt;

	/** Gibt an, ob es sich um ein Fach handelt, welches relevant für die Prüfungsordnung ist oder nicht (z.B. bei Belegprüfungen). */
	protected boolean istPruefungsordnungsRelevant;

	/** Gibt an, ob das Fach als schriftliches Fach für den Berufsabschluss gewertet wird (Berufskolleg). */
	protected boolean istSchriftlichBA;

	/** Gibt an, ob das Fach ein schriftliches Fach für die zentralen Klausuren ist oder nicht. */
	protected boolean istSchriftlichZK;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	protected boolean istSichtbar;

	/** Das eindeutige Kürzel des Faches */
	protected String kuerzel;

	/** Gibt die maximale Anzahl an Zeichen an, doe in Fachbemerkungen genutzt werden dürfen. */
	protected int maxZeichenInFachbemerkungen;

	/** Die Sortierreihenfolge des Fächerlisten-Eintrags. */
	protected int sortierung;

	/** Das Statistik-Kürzel des Faches */
	protected ReportingStatistikFach statistikfach;

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
	public ReportingFach(final String aufgabenfeld, final boolean aufZeugnis, final String bezeichnung, final String bezeichnungUeberweisungszeugnis,
			final String bezeichnungZeugnis, final String bilingualeSprache, final Fachgruppe fachgruppe, final boolean holeAusAltenLernabschnitten,
			final long id, final boolean istFHRFach, final boolean istFremdsprache, final boolean istFremdSpracheNeuEinsetzend, final boolean istGostFach,
			final boolean istNachpruefungErlaubt, final boolean istPruefungsordnungsRelevant, final boolean istSchriftlichBA, final boolean istSchriftlichZK,
			final boolean istSichtbar, final String kuerzel, final int maxZeichenInFachbemerkungen, final int sortierung,
			final ReportingStatistikFach statistikfach) {
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
		return GostFachbereich.compareFach(ZulaessigesFach.getByKuerzelASD(fach1.statistikfach().kuerzelASD()),
				ZulaessigesFach.getByKuerzelASD(fach2.statistikfach().kuerzelASD()));
	}



	// ##### Getter #####
	/**
	 * Das Aufgabenfeld am Berufskolleg, zu welchem das Fach gehört
	 * @return Inhalt des Feldes aufgabenfeld
	 */
	public String aufgabenfeld() {
		return aufgabenfeld;
	}

	/**
	 * Gibt an, ob das Fach auf einem Zeugnis erscheinen soll.
	 * @return Inhalt des Feldes aufZeugnis
	 */
	public boolean aufZeugnis() {
		return aufZeugnis;
	}

	/**
	 * Die Bezeichnung des Faches
	 * @return Inhalt des Feldes bezeichnung
	 */
	public String bezeichnung() {
		return bezeichnung;
	}

	/**
	 * Die Bezeichnung des Faches auf Überweisungszeugnissen
	 * @return Inhalt des Feldes bezeichnungUeberweisungszeugnis
	 */
	public String bezeichnungUeberweisungszeugnis() {
		return bezeichnungUeberweisungszeugnis;
	}

	/**
	 * Die Bezeichnung des Faches auf allgemeinen Zeugnissen
	 * @return Inhalt des Feldes bezeichnungZeugnis
	 */
	public String bezeichnungZeugnis() {
		return bezeichnungZeugnis;
	}

	/**
	 * Die Sprache (das einstellige Fremdsprachenkürzel) an, in der das Fach unterrichtet wird, sofern es sich um ein bilinguales Sachfach handelt.
	 * @return Inhalt des Feldes bilingualeSprache
	 */
	public String bilingualeSprache() {
		return bilingualeSprache;
	}

	/**
	 * Die Fachgruppe, der das Fach angehört.
	 * @return Fachgruppe des Faches
	 */
	public Fachgruppe fachgruppe() {
		return fachgruppe;
	}

	/**
	 * Gibt an, ob das Fach ggf. bei der Aggregation von Leistungen aus früheren Lernabschnitten/Jahrgängen für eine Abschlussberechnung berücksichtigt wird, sofern es im aktuellen Abschnitt nicht belegt wurde.
	 * @return Inhalt des Feldes holeAusAltenLernabschnitten
	 */
	public boolean holeAusAltenLernabschnitten() {
		return holeAusAltenLernabschnitten;
	}

	/**
	 * Die ID des Faches.
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Gibt an, ob das Fach bei der Berechnung der FHR berücksichtigt wird oder nicht (Berufskolleg).
	 * @return Inhalt des Feldes istFHRFach
	 */
	public boolean istFHRFach() {
		return istFHRFach;
	}

	/**
	 * Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht
	 * @return Inhalt des Feldes istFremdsprache
	 */
	public boolean istFremdsprache() {
		return istFremdsprache;
	}

	/**
	 * Gibt an, ob das Fach eine neu einsetzende Fremdsprache ist.
	 * @return Inhalt des Feldes istFremdSpracheNeuEinsetzend
	 */
	public boolean istFremdSpracheNeuEinsetzend() {
		return istFremdSpracheNeuEinsetzend;
	}

	/**
	 * Gibt an, ob es sich um ein Fach der Oberstufe handelt oder nicht.
	 * @return Inhalt des Feldes istGostFach
	 */
	public boolean istGostFach() {
		return istGostFach;
	}

	/**
	 * Gibt an, ob eine Nachprüfung in diesem Fach möglich ist.
	 * @return Inhalt des Feldes istNachpruefungErlaubt
	 */
	public boolean istNachpruefungErlaubt() {
		return istNachpruefungErlaubt;
	}

	/**
	 * Gibt an, ob es sich um ein Fach handelt, welches relevant für die Prüfungsordnung ist oder nicht (z.B. bei Belegprüfungen).
	 * @return Inhalt des Feldes istPruefungsordnungsRelevant
	 */
	public boolean istPruefungsordnungsRelevant() {
		return istPruefungsordnungsRelevant;
	}

	/**
	 * Gibt an, ob das Fach als schriftliches Fach für den Berufsabschluss gewertet wird (Berufskolleg).
	 * @return Inhalt des Feldes istSchriftlichBA
	 */
	public boolean istSchriftlichBA() {
		return istSchriftlichBA;
	}

	/**
	 * Gibt an, ob das Fach ein schriftliches Fach für die zentralen Klausuren ist oder nicht.
	 * @return Inhalt des Feldes istSchriftlichZK
	 */
	public boolean istSchriftlichZK() {
		return istSchriftlichZK;
	}

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 * @return Inhalt des Feldes istSichtbar
	 */
	public boolean istSichtbar() {
		return istSichtbar;
	}

	/**
	 * Das eindeutige Kürzel des Faches
	 * @return Inhalt des Feldes kuerzel
	 */
	public String kuerzel() {
		return kuerzel;
	}

	/**
	 * Gibt die maximale Anzahl an Zeichen an, doe in Fachbemerkungen genutzt werden dürfen.
	 * @return Inhalt des Feldes maxZeichenInFachbemerkungen
	 */
	public int maxZeichenInFachbemerkungen() {
		return maxZeichenInFachbemerkungen;
	}

	/**
	 * Die Sortierreihenfolge des Fächerlisten-Eintrags.
	 * @return Inhalt des Feldes sortierung
	 */
	public int sortierung() {
		return sortierung;
	}

	/**
	 * Das Statistik-Fach des Faches
	 * @return Inhalt des Feldes statistikfach
	 */
	public ReportingStatistikFach statistikfach() {
		return statistikfach;
	}

}
