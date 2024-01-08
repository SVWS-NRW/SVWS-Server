package de.svws_nrw.module.reporting.types.gost.laufbahnplanung;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ SchuelerGostLaufbahnplanungErgebnismeldungKategorie.</p>
 * <p>Sie stellt eine Aufzählung bereit, um die Rückmeldungen der Laufbahnprüfung zur GOSt zu kategorisieren.</p>
 */
public enum ReportingGostLaufbahnplanungErgebnismeldungKategorie {

	/** Meldung vom Typ FEHLER */
	FEHLER("Fehler"),

	/** Meldung vom Typ HINWEIS */
	HINWEIS("Hinweis");

	/** Kategorie der Rückmeldung */
	private final String kategorie;


	/**
	 * Erstellt einen Eintrag für die ENU;
	 * @param kategorie Kategorie der Meldung.
	 */
	ReportingGostLaufbahnplanungErgebnismeldungKategorie(final String kategorie) {
		this.kategorie = kategorie;
	}


	/**
	 * Gibt die Kategorie der Meldung der Laufbahnplanung zurück.
	 * @return Inhalt des Feldes kategorie
	 */
	public String kategorie() {
		return kategorie;
	}
}
