package de.svws_nrw.module.reporting.types.gost.laufbahnplanung;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostLaufbahnplanungErgebnismeldung und erweitert die Klasse {@link ReportingGostLaufbahnplanungErgebnismeldung}.
 */
public class ProxyReportingGostLaufbahnplanungErgebnismeldung extends ReportingGostLaufbahnplanungErgebnismeldung {

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostLaufbahnplanungErgebnismeldung}.
	 *
	 * @param code			Interner Code der Meldung aus der Prüfung, wenn vorhanden, sonst leer,
	 * @param kategorie 	Kategorie der Meldung, bspw. Fehler oder Hinweis.
	 * @param meldung		Text zur Meldung für den Benutzer
	 */
	public ProxyReportingGostLaufbahnplanungErgebnismeldung(final String code, final ReportingGostLaufbahnplanungErgebnismeldungKategorie kategorie,
			final String meldung) {
		super(ersetzeNullBlankTrim(code),
				kategorie,
				ersetzeNullBlankTrim(meldung));
	}
}
