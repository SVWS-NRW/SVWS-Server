package de.svws_nrw.module.reporting.proxytypes.stundenplanung;

import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungZeitrasterstunde;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Raum und erweitert die Klasse {@link ReportingStundenplanungZeitrasterstunde}.
 */
public class ProxyReportingStundenplanungZeitrasterstunde extends ReportingStundenplanungZeitrasterstunde {

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingStundenplanungZeitrasterstunde}.
	 *
	 * @param zeitraster		Der Raum aus dem Stundenplan.
	 * @param idStundenplan 	Optional: Die des Stundenplanes, zu dem der Raum mit seiner ID gehört, sonst null.
	 */
	public ProxyReportingStundenplanungZeitrasterstunde(final StundenplanZeitraster zeitraster, final Long idStundenplan) {
		super(zeitraster.id,
				idStundenplan,
				zeitraster.stundenbeginn,
				zeitraster.stundenende,
				zeitraster.unterrichtstunde,
				Wochentag.fromIDorException(zeitraster.wochentag));
	}
}
