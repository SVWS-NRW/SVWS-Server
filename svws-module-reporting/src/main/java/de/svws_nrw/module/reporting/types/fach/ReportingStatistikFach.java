package de.svws_nrw.module.reporting.types.fach;

import de.svws_nrw.asd.types.fach.Fachgruppe;
import de.svws_nrw.module.reporting.types.ReportingBaseType;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Statistik-Fach.
 */
public class ReportingStatistikFach extends ReportingBaseType {

	/** Der ASD-Jahrgang, ab dem das Fach zulässig ist (z. B. bei Fremdsprachen) */
	protected String abJahrgang;

	/** Das Aufgabenfeld, welchem das Fach ggf. zugeordnet ist (1, 2 oder 3) */
	protected Integer aufgabenfeld;

	/** Die textuelle Beschreibung des Faches */
	protected String bezeichnung;

	/** Gibt an, ob das Fach bei Export der amtlichen Schulstatistik berücksichtigt werden soll oder nicht. */
	protected boolean exportASD;

	/** Das Fach, dessen Statistikfach dieses Statistikfach ist und das mit ihm im Kürzel übereinstimmt. */
	protected ReportingFach fach;

	/** Die zugeordnete Fachgruppe */
	protected Fachgruppe fachgruppe;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	protected Integer gueltigBis;

	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	protected Integer gueltigVon;

	/** Die RGB-Farbe des Faches für html */
	protected String htmlFarbeRGB;

	/** Die ID des Katalog-Eintrags. */
	protected long idFachkatalog;

	/** Gibt an, ob das Fach außerhalb des regulären Fachunterrichts unterrichtet wird. */
	protected boolean istAusRegUFach;

	/** Gibt an, ob es sich bei dem Fach um einen Ersatz für eine Pflichtfremdsprache handelt (siehe auch istHKFS) */
	protected boolean istErsatzPflichtFS;

	/** Gibt an, ob es sich um eine Fremdsprache handelt */
	protected boolean istFremdsprache;

	/** Gibt an, ob es sich um ein Fach der Herkunftssprache handelt (Unterrichts in der Herkunftssprache oder Herkunftssprache anstelle einer Pflichtfremdsprache) */
	protected boolean istHKFS;

	/** Gibt an, ob das Religionsfach konfessionell kooperativ unterrichtet wird oder nicht - Teil des Kürzels für die amtliche Schulstatistik */
	protected boolean istKonfKoop;

	/** Das atomare Kürzel des Faches (z.B. bei Fremdsprachen - für das Sprachenkürzel) - Teil des Kürzels für die amtliche Schulstatistik */
	protected String kuerzel;

	/** Das eindeutige Kürzel des Faches entsprechend der Vorgaben der amtlichen Schulstatistik */
	protected String kuerzelASD;

	/** Gibt an, ob das Fach nur in der Sekundarstufe II unterrichtet wird. */
	protected boolean nurSII;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param abJahrgang Der ASD-Jahrgang, ab dem das Fach zulässig ist (z.B. bei Fremdsprachen)
	 * @param aufgabenfeld Das Aufgabenfeld, welchem das Fach ggf. zugeordnet ist (1, 2 oder 3)
	 * @param bezeichnung Die textuelle Beschreibung des Faches
	 * @param exportASD Gibt an, ob das Fach bei Export der amtlichen Schulstatistik berücksichtigt werden soll oder nicht.
	 * @param fach Das Fach, dessen Statistikfach dieses Statistikfach ist und das mit ihm im Kürzel übereinstimmt.
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
		setFach(fach);
	}



	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	public int hashCode() {
		return 31 + Long.hashCode(idFachkatalog);
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof final ReportingFach other))
			return false;
		return (idFachkatalog == other.id);
	}


	// ##### Getter #####
	/**
	 * Der ASD-Jahrgang, ab dem das Fach zulässig ist (z.B. bei Fremdsprachen)
	 *
	 * @return Inhalt des Feldes abJahrgang
	 */
	public String abJahrgang() {
		return abJahrgang;
	}

	/**
	 * Das Aufgabenfeld, welchem das Fach ggf. zugeordnet ist (1, 2 oder 3)
	 *
	 * @return Inhalt des Feldes aufgabenfeld
	 */
	public Integer aufgabenfeld() {
		return aufgabenfeld;
	}

	/**
	 * Die textuelle Beschreibung des Faches
	 *
	 * @return Inhalt des Feldes bezeichnung
	 */
	public String bezeichnung() {
		return bezeichnung;
	}

	/**
	 * Gibt an, ob das Fach bei Export der amtlichen Schulstatistik berücksichtigt werden soll oder nicht.
	 *
	 * @return Inhalt des Feldes exportASD
	 */
	public boolean exportASD() {
		return exportASD;
	}

	/**
	 * Sofern es ein Fach gibt, dessen Statistikfach dieses Statistikfach ist und die in ihrem Kürzel übereinstimmen, so wird dieses Fach bei seiner
	 * Initialisierung hier abgelegt.
	 *
	 * @return Inhalt des Feldes fach oder null, wenn kein Fach hinterlegt ist.
	 */
	public ReportingFach fach() {
		return fach;
	}

	/**
	 * Die zugeordnete Fachgruppe
	 *
	 * @return Inhalt des Feldes fachgruppe
	 */
	public Fachgruppe fachgruppe() {
		return fachgruppe;
	}

	/**
	 * Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 *
	 * @return Inhalt des Feldes gueltigBis
	 */
	public Integer gueltigBis() {
		return gueltigBis;
	}

	/**
	 * Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 *
	 * @return Inhalt des Feldes gueltigVon
	 */
	public Integer gueltigVon() {
		return gueltigVon;
	}

	/**
	 * Die RGB-Farbe des Faches für html
	 *
	 * @return Inhalt des Feldes htmlFarbeRGB
	 */
	public String htmlFarbeRGB() {
		return htmlFarbeRGB;
	}

	/**
	 * Die ID des Katalog-Eintrags.
	 *
	 * @return Inhalt des Feldes idFachkatalog
	 */
	public long idFachkatalog() {
		return idFachkatalog;
	}

	/**
	 * Gibt an, ob das Fach außerhalb des regulären Fachunterrichts unterrichtet wird.
	 *
	 * @return Inhalt des Feldes istAusRegUFach
	 */
	public boolean istAusRegUFach() {
		return istAusRegUFach;
	}

	/**
	 * Gibt an, ob es sich bei dem Fach um einen Ersatz für eine Pflichtfremdsprache handelt (siehe auch istHKFS)
	 *
	 * @return Inhalt des Feldes istErsatzPflichtFS
	 */
	public boolean istErsatzPflichtFS() {
		return istErsatzPflichtFS;
	}

	/**
	 * Gibt an, ob es sich um eine Fremdsprache handelt
	 *
	 * @return Inhalt des Feldes istFremdsprache
	 */
	public boolean istFremdsprache() {
		return istFremdsprache;
	}

	/**
	 * Gibt an, ob es sich um ein Fach der Herkunftssprache handelt (Unterrichts in der Herkunftssprache oder Herkunftssprache anstelle einer Pflichtfremdsprache)
	 *
	 * @return Inhalt des Feldes istHKFS
	 */
	public boolean istHKFS() {
		return istHKFS;
	}

	/**
	 * Gibt an, ob das Religionsfach konfessionell kooperativ unterrichtet wird oder nicht - Teil des Kürzels für die amtliche Schulstatistik
	 *
	 * @return Inhalt des Feldes istKonfKoop
	 */
	public boolean istKonfKoop() {
		return istKonfKoop;
	}

	/**
	 * Das atomare Kürzel des Faches (z.B. bei Fremdsprachen - für das Sprachenkürzel) - Teil des Kürzels für die amtliche Schulstatistik
	 *
	 * @return Inhalt des Feldes kuerzel
	 */
	public String kuerzel() {
		return kuerzel;
	}

	/**
	 * Das eindeutige Kürzel des Faches entsprechend der Vorgaben der amtlichen Schulstatistik
	 *
	 * @return Inhalt des Feldes kuerzelASD
	 */
	public String kuerzelASD() {
		return kuerzelASD;
	}

	/**
	 * Gibt an, ob das Fach nur in der Sekundarstufe II unterrichtet wird.
	 *
	 * @return Inhalt des Feldes nurSII
	 */
	public boolean nurSII() {
		return nurSII;
	}


	/**
	 * Setzt das Fach zu diesem Statistikfach, sofern das Fach dieses Statistikfach hat und die Kürzel übereinstimmen.
	 * Andernfalls wird null gesetzt.
	 *
	 * @param fach Das Fach, dessen Statistikfach dieses Statistikfach ist und das mit ihm im Kürzel übereinstimmt.
	 */
	public void setFach(final ReportingFach fach) {
		if ((fach == null) || (fach.kuerzel() == null) || fach.kuerzel().isEmpty() || !fach.kuerzel().equals(this.kuerzel()))
			this.fach = null;
		else
			this.fach = fach;
	}
}
