package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.List;

import de.svws_nrw.module.reporting.diagnose.ReportingAuswahlergebnis;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;

/**
 * Wählt die Hauptdaten einer Stundenplan-Sichtweise zu den übergebenen IDs aus. Sichtweisen mit Repository-Daten (Klassen, Lehrkräfte, Schüler) laden über
 * die Auswahl des jeweiligen Repositories; Fächer und Räume stammen aus dem bereits geladenen Stundenplan und werden gegen dessen Bestand aufgelöst.
 *
 * @param <H> Der Reporting-Typ der Hauptdaten dieser Sichtweise, z. B. {@code ReportingKlasse}.
 */
@FunctionalInterface
interface HtmlContextStundenplanAuswahl<H> {

	/**
	 * Wählt die Hauptdaten zu den übergebenen IDs aus, ohne bei fehlenden IDs abzubrechen.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param stundenplan      Der geladene Stundenplan.
	 * @param ids              Die übergebenen Hauptdaten-IDs.
	 *
	 * @return Das Auswahlergebnis.
	 */
	ReportingAuswahlergebnis<H> waehle(ReportingContext reportingContext, ReportingStundenplanungStundenplan stundenplan, List<Long> ids);

}
