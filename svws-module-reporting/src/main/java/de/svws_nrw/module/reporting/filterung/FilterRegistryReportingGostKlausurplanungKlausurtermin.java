package de.svws_nrw.module.reporting.filterung;

import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;

/**
 * Registry zur Definition erlaubter Filterattribute für {@link ReportingGostKlausurplanungKlausurtermin}
 * sowie Hilfsmethoden zum Erzeugen passender Predicates.
 */
public final class FilterRegistryReportingGostKlausurplanungKlausurtermin {

	private FilterRegistryReportingGostKlausurplanungKlausurtermin() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zur Filterung von Daten für das Reporting. Initialisierung nicht möglich.");
	}

	/**
	 * Stellt die {@link FilterRegistry} für die Klasse {@link ReportingGostKlausurplanungKlausurtermin} öffentlich zur Verfügung.
	 * Diese Methode wird vom {@link ReportingContext} via Reflection aufgerufen.
	 *
	 * @return Die konfigurierte Instanz von {@link FilterRegistry} für {@link ReportingGostKlausurplanungKlausurtermin}.
	 */
	public static FilterRegistry<ReportingGostKlausurplanungKlausurtermin> filterRegistry() {
		return filterRegistryReportingGostKlausurplanungKlausurtermin();
	}

	/**
	 * Erstellt und konfiguriert eine {@link FilterRegistry} für die Klasse {@link ReportingGostKlausurplanungKlausurtermin}.
	 * Hier werden alle Attribute registriert, auf die ein Filter in den ReportingParametern angewendet werden kann.
	 *
	 * @return Die konfigurierte Instanz von {@link FilterRegistry} für {@link ReportingGostKlausurplanungKlausurtermin}.
	 */
	private static FilterRegistry<ReportingGostKlausurplanungKlausurtermin> filterRegistryReportingGostKlausurplanungKlausurtermin() {
		final FilterRegistry<ReportingGostKlausurplanungKlausurtermin> reg = new FilterRegistry<>();

		// Grundlegende Attribute
		reg.registriereAttribut(ReportingGostKlausurplanungKlausurtermin::gostHalbjahr);
		reg.registriereAttribut(ReportingGostKlausurplanungKlausurtermin::quartal);
		reg.registriereAttribut(ReportingGostKlausurplanungKlausurtermin::istHaupttermin);
		reg.registriereAttribut(ReportingGostKlausurplanungKlausurtermin::datum);
		reg.registriereAttribut(ReportingGostKlausurplanungKlausurtermin::bezeichnung);
		reg.registriereAttribut(ReportingGostKlausurplanungKlausurtermin::nachschreiberZugelassen);

		return reg;
	}
}
