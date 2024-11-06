package de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung;

import java.util.ArrayList;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurtermin und erweitert die Klasse {@link ReportingGostKlausurplanungKlausurtermin}.
 */
public class ProxyReportingGostKlausurplanungKlausurtermin extends ReportingGostKlausurplanungKlausurtermin {

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKlausurplanungKlausurtermin}.
	 * Das Hinzufügen dieser Elemente erfolgt in der Proxy-Klassen bei ihrer Erzeugung.
	 *
	 * @param gostKlausurtermin	Der GostKlausurtermin mit den Daten zum Klausurtermin.
	 */
	public ProxyReportingGostKlausurplanungKlausurtermin(final GostKlausurtermin gostKlausurtermin) {
		super(gostKlausurtermin.bemerkung,
				gostKlausurtermin.bezeichnung,
				gostKlausurtermin.datum,
				GostHalbjahr.fromID(gostKlausurtermin.halbjahr),
				gostKlausurtermin.id,
				gostKlausurtermin.istHaupttermin,
				new ArrayList<>(),
				new ArrayList<>(),
				gostKlausurtermin.nachschreiberZugelassen,
				gostKlausurtermin.quartal,
				new ArrayList<>(),
				gostKlausurtermin.startzeit);

		// Kursklausuren werden bei deren Erzeugung dem Klausurtermin hinzugefügt.
		// Schülerklausuren werden bei deren Erzeugung dem Klausurtermin hinzugefügt.
		// Klausurräume werden gesammelt für alle Termine erzeugt.
	}

}
