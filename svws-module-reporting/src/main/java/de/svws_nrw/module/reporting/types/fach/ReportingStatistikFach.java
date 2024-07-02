package de.svws_nrw.module.reporting.types.fach;

import de.svws_nrw.core.types.fach.Fachgruppe;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ Statistik-Fach.</p>
 * <p>Sie enthält die Grunddaten eines Statistik-Faches.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingStatistikFach {

	/** Der ASD-Jahrgang, ab dem das Fach zulässig ist (z. B. bei Fremdsprachen) */
	private String abJahrgang;

	/** Das Aufgabenfeld, welchem das Fach ggf. zugeordnet ist (1, 2 oder 3) */
	private Integer aufgabenfeld;

	/** Die textuelle Beschreibung des Faches */
	private String bezeichnung;

	/** Gibt an, ob das Fach bei Export der amtlichen Schulstatistik berücksichtigt werden soll oder nicht. */
	private boolean exportASD;

	/** Das Fach aus dem Fachkatalog der Schule, wenn dieses Fach dieses Statistikfach besitzt und das Fachkürzel mit dem Statistikfachkürzel übereinstimmt. Andernfalls null. */
	private ReportingFach fach;

	/** Die zugeordnete Fachgruppe */
	private Fachgruppe fachgruppe;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	private Integer gueltigBis;

	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	private Integer gueltigVon;

	/** Die RGB-Farbe des Faches für html */
	private String htmlFarbeRGB;

	/** Die ID des Katalog-Eintrags. */
	private long idFachkatalog;

	/** Gibt an, ob das Fach außerhalb des regulären Fachunterrichts unterrichtet wird. */
	private boolean istAusRegUFach;

	/** Gibt an, ob es sich bei dem Fach um einen Ersatz für eine Pflichtfremdsprache handelt (siehe auch istHKFS) */
	private boolean istErsatzPflichtFS;

	/** Gibt an, ob es sich um eine Fremdsprache handelt */
	private boolean istFremdsprache;

	/** Gibt an, ob es sich um ein Fach der Herkunftssprache handelt (Unterrichts in der Herkunftssprache oder Herkunftssprache anstelle einer Pflichtfremdsprache) */
	private boolean istHKFS;

	/** Gibt an, ob das Religionsfach konfessionell kooperativ unterrichtet wird oder nicht - Teil des Kürzels für die amtliche Schulstatistik */
	private boolean istKonfKoop;

	/** Das atomare Kürzel des Faches (z.B. bei Fremdsprachen - für das Sprachenkürzel) - Teil des Kürzels für die amtliche Schulstatistik */
	private String kuerzel;

	/** Das eindeutige Kürzel des Faches entsprechend der Vorgaben der amtlichen Schulstatistik */
	private String kuerzelASD;

	/** Gibt an, ob das Fach nur in der Sekundarstufe II unterrichtet wird. */
	private boolean nurSII;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param abJahrgang Der ASD-Jahrgang, ab dem das Fach zulässig ist (z.B. bei Fremdsprachen)
	 * @param aufgabenfeld Das Aufgabenfeld, welchem das Fach ggf. zugeordnet ist (1, 2 oder 3)
	 * @param bezeichnung Die textuelle Beschreibung des Faches
	 * @param exportASD Gibt an, ob das Fach bei Export der amtlichen Schulstatistik berücksichtigt werden soll oder nicht.
	 * @param fach Das Fach aus dem Fachkatalog der Schule, wenn dieses Fach dieses Statistikfach besitzt und das Fachkürzel mit dem Statistikfachkürzel übereinstimmt. Andernfalls null.
	 * @param fachgruppe Die zugeordnete Fachgruppe
	 * @param gueltigBis Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 * @param gueltigVon Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 * @param htmlFarbeRGB Die RGB-Farbe des Faches für html
	 * @param idFachkatalog Die ID des Katalog-Eintrags.
	 * @param istAusRegUFach Gibt an, ob das Fach außerhalb des regulären Fachunterrichts unterrichtet wird.
	 * @param istErsatzPflichtFS Gibt an, ob es sich bei dem Fach um einen Ersatz für eine Pflichtfremdsprache handelt (siehe auch istHKFS)
	 * @param istFremdsprache Gibt an, ob es sich um eine Fremdsprache handelt
	 * @param istHKFS Gibt an, ob es sich um ein Fach der Herkunftssprache handelt (Unterrichts in der Herkunftssprache oder Herkunftssprache anstelle einer Pflichtfremdsprache)
	 * @param istKonfKoop Gibt an, ob das Religionsfach konfessionell kooperativ unterrichtet wird oder nicht - Teil des Kürzels für die amtliche Schulstatistik
	 * @param kuerzel Das atomare Kürzel des Faches (z.B. bei Fremdsprachen - für das Sprachenkürzel) - Teil des Kürzels für die amtliche Schulstatistik
	 * @param kuerzelASD Das eindeutige Kürzel des Faches entsprechend der Vorgaben der amtlichen Schulstatistik
	 * @param nurSII Gibt an, ob das Fach nur in der Sekundarstufe II unterrichtet wird.
	 */
	public ReportingStatistikFach(final String abJahrgang, final Integer aufgabenfeld, final String bezeichnung, final boolean exportASD,
			final ReportingFach fach, final Fachgruppe fachgruppe, final Integer gueltigBis, final Integer gueltigVon, final String htmlFarbeRGB,
			final long idFachkatalog, final boolean istAusRegUFach, final boolean istErsatzPflichtFS, final boolean istFremdsprache, final boolean istHKFS,
			final boolean istKonfKoop, final String kuerzel, final String kuerzelASD, final boolean nurSII) {
		this.abJahrgang = abJahrgang;
		this.aufgabenfeld = aufgabenfeld;
		this.bezeichnung = bezeichnung;
		this.exportASD = exportASD;
		this.fach = fach;
		this.fachgruppe = fachgruppe;
		this.gueltigBis = gueltigBis;
		this.gueltigVon = gueltigVon;
		this.htmlFarbeRGB = htmlFarbeRGB;
		this.idFachkatalog = idFachkatalog;
		this.istAusRegUFach = istAusRegUFach;
		this.istErsatzPflichtFS = istErsatzPflichtFS;
		this.istFremdsprache = istFremdsprache;
		this.istHKFS = istHKFS;
		this.istKonfKoop = istKonfKoop;
		this.kuerzel = kuerzel;
		this.kuerzelASD = kuerzelASD;
		this.nurSII = nurSII;
	}



	// ##### Getter und Setter #####
	/**
	 * Der ASD-Jahrgang, ab dem das Fach zulässig ist (z.B. bei Fremdsprachen)
	 * @return Inhalt des Feldes abJahrgang
	 */
	public String abJahrgang() {
		return abJahrgang;
	}

	/**
	 * Der ASD-Jahrgang, ab dem das Fach zulässig ist (z. B. bei Fremdsprachen), wird gesetzt.
	 * @param abJahrgang Neuer Wert für das Feld abJahrgang
	 */
	public void setAbJahrgang(final String abJahrgang) {
		this.abJahrgang = abJahrgang;
	}

	/**
	 * Das Aufgabenfeld, welchem das Fach ggf. zugeordnet ist (1, 2 oder 3)
	 * @return Inhalt des Feldes aufgabenfeld
	 */
	public Integer aufgabenfeld() {
		return aufgabenfeld;
	}

	/**
	 * Das Aufgabenfeld, welchem das Fach ggf zugeordnet ist (1, 2 oder 3) wird gesetzt.
	 * @param aufgabenfeld Neuer Wert für das Feld aufgabenfeld
	 */
	public void setAufgabenfeld(final Integer aufgabenfeld) {
		this.aufgabenfeld = aufgabenfeld;
	}

	/**
	 * Die textuelle Beschreibung des Faches
	 * @return Inhalt des Feldes bezeichnung
	 */
	public String bezeichnung() {
		return bezeichnung;
	}

	/**
	 * Die textuelle Beschreibung des Faches wird gesetzt.
	 * @param bezeichnung Neuer Wert für das Feld bezeichnung
	 */
	public void setBezeichnung(final String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Gibt an, ob das Fach bei Export der amtlichen Schulstatistik berücksichtigt werden soll oder nicht.
	 * @return Inhalt des Feldes exportASD
	 */
	public boolean exportASD() {
		return exportASD;
	}

	/**
	 * Gibt an, ob das Fach bei Export der amtlichen Schulstatistik berücksichtigt werden soll oder nicht wird gesetzt.
	 * @param exportASD Neuer Wert für das Feld exportASD
	 */
	public void setExportASD(final boolean exportASD) {
		this.exportASD = exportASD;
	}

	/**
	 * Das Fach aus dem Fachkatalog der Schule, wenn dieses Fach dieses Statistikfach besitzt und das Fachkürzel mit dem Statistikfachkürzel übereinstimmt. Andernfalls null.
	 * @return Inhalt des Feldes fach
	 */
	public ReportingFach fach() {
		return fach;
	}

	/**
	 * Das Fach aus dem Fachkatalog der Schule, wenn dieses Fach dieses Statistikfach besitzt und das Fachkürzel mit dem Statistikfachkürzel übereinstimmt, wird gesetzt.
	 * @param fach Neuer Wert für das Feld fach
	 */
	public void setFach(final ReportingFach fach) {
		this.fach = fach;
	}

	/**
	 * Das Kürzel der zugeordneten Fachgruppe
	 * @return Inhalt des Feldes fachgruppe
	 */
	public Fachgruppe fachgruppe() {
		return fachgruppe;
	}

	/**
	 * Das Kürzel der zugeordneten Fachgruppe wird gesetzt.
	 * @param fachgruppe Neuer Wert für das Feld fachgruppe
	 */
	public void setFachgruppe(final Fachgruppe fachgruppe) {
		this.fachgruppe = fachgruppe;
	}

	/**
	 * Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 * @return Inhalt des Feldes gueltigBis
	 */
	public Integer gueltigBis() {
		return gueltigBis;
	}

	/**
	 * Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt wird gesetzt.
	 * @param gueltigBis Neuer Wert für das Feld gueltigBis
	 */
	public void setGueltigBis(final Integer gueltigBis) {
		this.gueltigBis = gueltigBis;
	}

	/**
	 * Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 * @return Inhalt des Feldes gueltigVon
	 */
	public Integer gueltigVon() {
		return gueltigVon;
	}

	/**
	 * Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt wird gesetzt.
	 * @param gueltigVon Neuer Wert für das Feld gueltigVon
	 */
	public void setGueltigVon(final Integer gueltigVon) {
		this.gueltigVon = gueltigVon;
	}

	/**
	 * Die RGB-Farbe des Faches für html
	 * @return Inhalt des Feldes htmlFarbeRGB
	 */
	public String htmlFarbeRGB() {
		return htmlFarbeRGB;
	}

	/**
	 * Die RGB-Farbe des Faches für html wird gesetzt.
	 * @param htmlFarbeRGB Neuer Wert für das Feld htmlFarbeRGB
	 */
	public void setHtmlFarbeRGB(final String htmlFarbeRGB) {
		this.htmlFarbeRGB = htmlFarbeRGB;
	}

	/**
	 * Die ID des Katalog-Eintrags.
	 * @return Inhalt des Feldes idFachkatalog
	 */
	public long idFachkatalog() {
		return idFachkatalog;
	}

	/**
	 * Die ID des Katalog-Eintrags wird gesetzt.
	 * @param idFachkatalog Neuer Wert für das Feld idFachkatalog
	 */
	public void setIdFachkatalog(final long idFachkatalog) {
		this.idFachkatalog = idFachkatalog;
	}

	/**
	 * Gibt an, ob das Fach außerhalb des regulären Fachunterrichts unterrichtet wird.
	 * @return Inhalt des Feldes istAusRegUFach
	 */
	public boolean istAusRegUFach() {
		return istAusRegUFach;
	}

	/**
	 * Gibt an, ob das Fach außerhalb des regulären Fachunterrichts unterrichtet wird, wird gesetzt.
	 * @param istAusRegUFach Neuer Wert für das Feld istAusRegUFach
	 */
	public void setIstAusRegUFach(final boolean istAusRegUFach) {
		this.istAusRegUFach = istAusRegUFach;
	}

	/**
	 * Gibt an, ob es sich bei dem Fach um einen Ersatz für eine Pflichtfremdsprache handelt (siehe auch istHKFS)
	 * @return Inhalt des Feldes istErsatzPflichtFS
	 */
	public boolean istErsatzPflichtFS() {
		return istErsatzPflichtFS;
	}

	/**
	 * Gibt an, ob es sich bei dem Fach um einen Ersatz für eine Pflichtfremdsprache handelt (siehe auch istHKFS) wird gesetzt.
	 * @param istErsatzPflichtFS Neuer Wert für das Feld istErsatzPflichtFS
	 */
	public void setIstErsatzPflichtFS(final boolean istErsatzPflichtFS) {
		this.istErsatzPflichtFS = istErsatzPflichtFS;
	}

	/**
	 * Gibt an, ob es sich um eine Fremdsprache handelt
	 * @return Inhalt des Feldes istFremdsprache
	 */
	public boolean istFremdsprache() {
		return istFremdsprache;
	}

	/**
	 * Gibt an, ob es sich um eine Fremdsprache handelt wird gesetzt.
	 * @param istFremdsprache Neuer Wert für das Feld istFremdsprache
	 */
	public void setIstFremdsprache(final boolean istFremdsprache) {
		this.istFremdsprache = istFremdsprache;
	}

	/**
	 * Gibt an, ob es sich um ein Fach der Herkunftssprache handelt (Unterrichts in der Herkunftssprache oder Herkunftssprache anstelle einer Pflichtfremdsprache)
	 * @return Inhalt des Feldes istHKFS
	 */
	public boolean istHKFS() {
		return istHKFS;
	}

	/**
	 * Gibt an, ob es sich um ein Fach der Herkunftssprache handelt (Unterrichts in der Herkunftssprache oder Herkunftssprache anstelle einer Pflichtfremdsprache) wird gesetzt.
	 * @param istHKFS Neuer Wert für das Feld istHKFS
	 */
	public void setIstHKFS(final boolean istHKFS) {
		this.istHKFS = istHKFS;
	}

	/**
	 * Gibt an, ob das Religionsfach konfessionell kooperativ unterrichtet wird oder nicht - Teil des Kürzels für die amtliche Schulstatistik
	 * @return Inhalt des Feldes istKonfKoop
	 */
	public boolean istKonfKoop() {
		return istKonfKoop;
	}

	/**
	 * Gibt an, ob das Religionsfach konfessionell kooperativ unterrichtet wird oder nicht - Teil des Kürzels für die amtliche Schulstatistik wird gesetzt.
	 * @param istKonfKoop Neuer Wert für das Feld istKonfKoop
	 */
	public void setIstKonfKoop(final boolean istKonfKoop) {
		this.istKonfKoop = istKonfKoop;
	}

	/**
	 * Das atomare Kürzel des Faches (z.B. bei Fremdsprachen - für das Sprachenkürzel) - Teil des Kürzels für die amtliche Schulstatistik
	 * @return Inhalt des Feldes kuerzel
	 */
	public String kuerzel() {
		return kuerzel;
	}

	/**
	 * Das atomare Kürzel des Faches (zB bei Fremdsprachen - für das Sprachenkürzel) - Teil des Kürzels für die amtliche Schulstatistik wird gesetzt.
	 * @param kuerzel Neuer Wert für das Feld kuerzel
	 */
	public void setKuerzel(final String kuerzel) {
		this.kuerzel = kuerzel;
	}

	/**
	 * Das eindeutige Kürzel des Faches entsprechend der Vorgaben der amtlichen Schulstatistik
	 * @return Inhalt des Feldes kuerzelASD
	 */
	public String kuerzelASD() {
		return kuerzelASD;
	}

	/**
	 * Das eindeutige Kürzel des Faches entsprechend der Vorgaben der amtlichen Schulstatistik wird gesetzt.
	 * @param kuerzelASD Neuer Wert für das Feld kuerzelASD
	 */
	public void setKuerzelASD(final String kuerzelASD) {
		this.kuerzelASD = kuerzelASD;
	}

	/**
	 * Gibt an, ob das Fach nur in der Sekundarstufe II unterrichtet wird.
	 * @return Inhalt des Feldes nurSII
	 */
	public boolean nurSII() {
		return nurSII;
	}

	/**
	 * Gibt an, ob das Fach nur in der Sekundarstufe II unterrichtet wird wird gesetzt.
	 * @param nurSII Neuer Wert für das Feld nurSII
	 */
	public void setNurSII(final boolean nurSII) {
		this.nurSII = nurSII;
	}
}
