package de.svws_nrw.module.reporting.types.schueler.erzieher;

import de.svws_nrw.core.data.erzieher.Erzieherart;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Erzieher-Art und erweitert die Klasse {@link ReportingErzieherArt}.
 */
public class ProxyReportingErzieherArt extends ReportingErzieherArt {


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingErzieherArt}.
	 *
	 * @param erzieherart Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingErzieherArt(final Erzieherart erzieherart) {
		super(ersetzeNullBlankTrim(erzieherart.bezeichnung),
				erzieherart.id,
				erzieherart.sortierung
		);
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
