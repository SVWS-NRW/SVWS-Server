package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.List;

import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;

/**
 * Erzeugt den Haupt-Context eines Stundenplan-Datenaufbaus. Alle fünf Stundenplan-Contexts teilen diese Form, so dass in der Registry die
 * Konstruktor-Referenz des jeweiligen Contexts genügt.
 *
 * @param <T> Der Reporting-Typ der Context-Daten, z. B. {@code ReportingStundenplanungKlasseStundenplan}.
 */
@FunctionalInterface
interface HtmlContextStundenplanErzeuger<T> {

	/**
	 * Erzeugt den Haupt-Context aus dem geladenen Stundenplan und den übergebenen Hauptdaten-IDs.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param stundenplan      Der geladene Stundenplan.
	 * @param ids              Die übergebenen Hauptdaten-IDs (Fächer, Klassen, Lehrkräfte, Räume oder Schüler).
	 *
	 * @return Der erzeugte Haupt-Context.
	 */
	HtmlContext<T> erzeuge(ReportingContext reportingContext, ReportingStundenplanungStundenplan stundenplan, List<Long> ids);

}
