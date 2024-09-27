package de.svws_nrw.module.reporting.types.gost.laufbahnplanung;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ SchuelerGostLaufbahnplanungErgebnismeldungKategorie.
 */
public enum ReportingGostLaufbahnplanungErgebnismeldungKategorie {

	/** Meldung vom Typ FEHLER */
	FEHLER("Fehler"),

	/** Meldung vom Typ HINWEIS */
	HINWEIS("Hinweis");

	/** Kategorie der Rückmeldung */
	protected final String kategorie;


	/**
	 * Erstellt einen Eintrag für die ENU;
	 * @param kategorie Kategorie der Meldung.
	 */
	ReportingGostLaufbahnplanungErgebnismeldungKategorie(final String kategorie) {
		this.kategorie = kategorie;
	}


	// ##### Getter #####

	/**
	 * Gibt die Kategorie der Meldung der Laufbahnplanung zurück.
	 * @return Inhalt des Feldes kategorie
	 */
	public String kategorie() {
		return kategorie;
	}
}
