package de.svws_nrw.module.reporting.types.lehrer;

import de.svws_nrw.asd.data.schule.Schulleitung;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ LehrerLeitungsfunktion und erweitert die Klasse {@link ReportingLehrerLeitungsfunktion}.
 */
public class ProxyReportingLehrerLeitungsfunktion extends ReportingLehrerLeitungsfunktion {

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingLehrerLeitungsfunktion}.
	 *
	 * @param schulleitung Die Schulleitungsdaten.
	 */
	public ProxyReportingLehrerLeitungsfunktion(final Schulleitung schulleitung) {
		super(schulleitung.datumBeginnLeitungsfunktion, schulleitung.bezeichnung, schulleitung.datumEndeLeitungsfunktion, schulleitung.idLeitungsfunktion);
	}


	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return    true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj);
	}
}
