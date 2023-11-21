package de.svws_nrw.module.pdf.reptypes.gost.laufbahnplanung;


/**
 *
 */
public class RepGostLaufbahnplanungErgebnismeldung {

	/** Kategorie der Meldung, bspw. Fehler oder Hinweis. */
	public RepGostLaufbahnplanungErgebnismeldungKategorie kategorie;

	/** Interner Code der Meldung aus der Prüfung, wenn vorhanden, sonst leer, */
	public String code;

	/** Text zur Meldung für den Benutzer */
	public String meldung;


	/**
	 * Erstellt eine Ergebnismeldung zur Laufbahnplanungsprüfung zur GOSt.
	 *
	 * @param kategorie 	Kategorie der Meldung, bspw. Fehler oder Hinweis.
	 * @param code			Interner Code der Meldung aus der Prüfung, wenn vorhanden, sonst leer,
	 * @param meldung		Text zur Meldung für den Benutzer
	 */
	public RepGostLaufbahnplanungErgebnismeldung(final RepGostLaufbahnplanungErgebnismeldungKategorie kategorie, final String code, final String meldung) {
		this.kategorie = kategorie;
		this.code = code;
		this.meldung = meldung;
	}
}
