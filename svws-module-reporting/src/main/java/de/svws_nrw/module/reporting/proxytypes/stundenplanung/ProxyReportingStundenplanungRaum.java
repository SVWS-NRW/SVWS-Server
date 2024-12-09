package de.svws_nrw.module.reporting.proxytypes.stundenplanung;

import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungRaum;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Raum und erweitert die Klasse {@link ReportingStundenplanungRaum}.
 */
public class ProxyReportingStundenplanungRaum extends ReportingStundenplanungRaum {

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingStundenplanungRaum}.
	 *
	 * @param raum 				Der Raum aus dem Stundenplan.
	 * @param idStundenplan 	Optional: Die des Stundenplanes, zu dem der Raum mit seiner ID gehört, sonst null.
	 */
	public ProxyReportingStundenplanungRaum(final StundenplanRaum raum, final Long idStundenplan) {
		super(ersetzeNullDurchEmpty(raum.beschreibung),
				raum.id,
				null,
				idStundenplan,
				raum.groesse,
				ersetzeNullDurchEmpty(raum.kuerzel));
	}
}
