package de.svws_nrw.module.pdf.reptypes.gost.laufbahnplanung;

/**
 * Stellteine Aufzählung bereit, um die Rückmeldungen der Laufbahnprüfung zur GOst zu kategorisieren.
 */
public enum RepGostLaufbahnplanungErgebnismeldungKategorie {

	/** Meldung vom Typ FEHLER */
	FEHLER("Fehler"),

	/** Meldung vom Typ HINWEIS */
	HINWEIS("Hinweis");

	/** Kategorie der Rückmeldung */
	public final String kategorie;


	/**
	 * Erstellt einen Eintrag für die ENU;
	 *
	 * @param kategorie Kategorie der Meldung.
	 */
	RepGostLaufbahnplanungErgebnismeldungKategorie(final String kategorie) {
		this.kategorie = kategorie;
	}
}
