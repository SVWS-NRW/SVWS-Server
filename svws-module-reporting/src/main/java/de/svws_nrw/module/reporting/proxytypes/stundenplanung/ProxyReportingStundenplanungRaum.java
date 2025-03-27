package de.svws_nrw.module.reporting.proxytypes.stundenplanung;

import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungRaum;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Raum und erweitert die Klasse {@link ReportingStundenplanungRaum}.
 */
public class ProxyReportingStundenplanungRaum extends ReportingStundenplanungRaum {

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingStundenplanungRaum}.
	 *
	 * @param raum 				Der Raum aus dem Stundenplan.
	 * @param stundenplan 	Der Stundenplan, zu dem der Raum mit seiner ID gehört.
	 */
	public ProxyReportingStundenplanungRaum(final StundenplanRaum raum, final ReportingStundenplanungStundenplan stundenplan) {
		super(ersetzeNullBlankTrim(raum.beschreibung),
				raum.id,
				null,
				stundenplan.id(),
				raum.groesse,
				ersetzeNullBlankTrim(raum.kuerzel));
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
