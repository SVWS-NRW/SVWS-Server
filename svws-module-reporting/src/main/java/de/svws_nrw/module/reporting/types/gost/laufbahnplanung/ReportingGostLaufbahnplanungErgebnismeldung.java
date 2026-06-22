package de.svws_nrw.module.reporting.types.gost.laufbahnplanung;


import de.svws_nrw.module.reporting.types.ReportingBaseType;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ SchuelerGostLaufbahnplanungErgebnismeldung.
 */
public class ReportingGostLaufbahnplanungErgebnismeldung extends ReportingBaseType {

	/** Interner Code der Meldung aus der Prüfung, wenn vorhanden, sonst leer, */
	protected String code;

	/** Kategorie der Meldung, bspw. Fehler oder Hinweis. */
	protected ReportingGostLaufbahnplanungErgebnismeldungKategorie kategorie;

	/** Text zur Meldung für den Benutzer */
	protected String meldung;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param code			Interner Code der Meldung aus der Prüfung, wenn vorhanden, sonst leer,
	 * @param kategorie 	Kategorie der Meldung, bspw. Fehler oder Hinweis.
	 * @param meldung		Text zur Meldung für den Benutzer
	 */
	public ReportingGostLaufbahnplanungErgebnismeldung(final String code, final ReportingGostLaufbahnplanungErgebnismeldungKategorie kategorie,
			final String meldung) {
		this.code = ersetzeNullBlankTrim(code);
		this.kategorie = kategorie;
		this.meldung = ersetzeNullBlankTrim(meldung);
	}



	// ##### Getter #####

	/**
	 * Interner Code der Meldung aus der Prüfung, wenn vorhanden, sonst leer,
	 *
	 * @return Inhalt des Feldes code; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String code() {
		return code;
	}

	/**
	 * Kategorie der Meldung, bspw. Fehler oder Hinweis.
	 *
	 * @return Inhalt des Feldes kategorie; kann {@code null} sein, wenn keine Kategorie zugeordnet ist.
	 */
	public ReportingGostLaufbahnplanungErgebnismeldungKategorie kategorie() {
		return kategorie;
	}

	/**
	 * Text zur Meldung für den Benutzer
	 *
	 * @return Inhalt des Feldes meldung; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String meldung() {
		return meldung;
	}

}
