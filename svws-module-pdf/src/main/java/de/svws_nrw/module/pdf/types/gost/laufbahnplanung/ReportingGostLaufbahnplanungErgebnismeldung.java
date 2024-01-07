package de.svws_nrw.module.pdf.types.gost.laufbahnplanung;


/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ SchuelerGostLaufbahnplanungErgebnismeldung.</p>
 * <p>Sie enthält die Daten einer Ergebnismeldung der Laufbahnplanung der gymnasialen Oberstufe.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostLaufbahnplanungErgebnismeldung {

	/** Interner Code der Meldung aus der Prüfung, wenn vorhanden, sonst leer, */
	private String code;

	/** Kategorie der Meldung, bspw. Fehler oder Hinweis. */
	private ReportingGostLaufbahnplanungErgebnismeldungKategorie kategorie;

	/** Text zur Meldung für den Benutzer */
	private String meldung;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param code			Interner Code der Meldung aus der Prüfung, wenn vorhanden, sonst leer,
	 * @param kategorie 	Kategorie der Meldung, bspw. Fehler oder Hinweis.
	 * @param meldung		Text zur Meldung für den Benutzer
	 */
	public ReportingGostLaufbahnplanungErgebnismeldung(final String code, final ReportingGostLaufbahnplanungErgebnismeldungKategorie kategorie, final String meldung) {
		this.code = code;
		this.kategorie = kategorie;
		this.meldung = meldung;
	}



	// ##### Getter und Setter #####

	/**
	 * Interner Code der Meldung aus der Prüfung, wenn vorhanden, sonst leer,
	 * @return Inhalt des Feldes code
	 */
	public String code() {
		return code;
	}

	/**
	 * Interner Code der Meldung aus der Prüfung, wenn vorhanden, sonst leer, wird gesetzt.
	 * @param code Neuer Wert für das Feld code
	 */
	public void setCode(final String code) {
		this.code = code;
	}

	/**
	 * Kategorie der Meldung, bspw. Fehler oder Hinweis.
	 * @return Inhalt des Feldes kategorie
	 */
	public ReportingGostLaufbahnplanungErgebnismeldungKategorie kategorie() {
		return kategorie;
	}

	/**
	 * Kategorie der Meldung, bspw Fehler oder Hinweis wird gesetzt.
	 * @param kategorie Neuer Wert für das Feld kategorie
	 */
	public void setKategorie(final ReportingGostLaufbahnplanungErgebnismeldungKategorie kategorie) {
		this.kategorie = kategorie;
	}

	/**
	 * Text zur Meldung für den Benutzer
	 * @return Inhalt des Feldes meldung
	 */
	public String meldung() {
		return meldung;
	}

	/**
	 * Text zur Meldung für den Benutzer wird gesetzt.
	 * @param meldung Neuer Wert für das Feld meldung
	 */
	public void setMeldung(final String meldung) {
		this.meldung = meldung;
	}


}
