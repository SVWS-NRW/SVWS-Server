package de.svws_nrw.module.reporting.filterung;

import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;

/**
 * Registry zur Definition erlaubter Filterattribute für {@link ReportingKurs}
 * sowie Hilfsmethoden zum Erzeugen passender Predicates.
 */
public final class FilterRegistryReportingKurs {

	private FilterRegistryReportingKurs() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zur Filterung von Daten für das Reporting. Initialisierung nicht möglich.");
	}

	/**
	 * Stellt die {@link FilterRegistry} für die Klasse {@link ReportingKurs} öffentlich zur Verfügung.
	 * Diese Methode wird vom {@link ReportingContext} via Reflection aufgerufen.
	 *
	 * @return Die konfigurierte Instanz von {@link FilterRegistry} für {@link ReportingKurs}.
	 */
	public static FilterRegistry<ReportingKurs> filterRegistry() {
		return filterRegistryReportingKurs();
	}

	/**
	 * Erstellt und konfiguriert eine {@link FilterRegistry} für die Klasse {@link ReportingKurs}.
	 * Hier werden alle Attribute registriert, auf die ein Filter in den ReportingParametern angewendet werden kann.
	 *
	 * @return Die konfigurierte Instanz von {@link FilterRegistry} für {@link ReportingKurs}.
	 */
	private static FilterRegistry<ReportingKurs> filterRegistryReportingKurs() {
		final FilterRegistry<ReportingKurs> reg = new FilterRegistry<>();

		// Grundlegende Attribute
		reg.registriereAttribut(ReportingKurs::id);
		reg.registriereAttribut(ReportingKurs::kuerzel);
		reg.registriereAttribut(ReportingKurs::bezeichnungZeugnis);
		reg.registriereAttribut(ReportingKurs::sortierung);

		// Kursspezifische Attribute
		reg.registriereAttribut(ReportingKurs::kursartAllg);
		reg.registriereAttribut(ReportingKurs::istSichtbar);
		reg.registriereAttribut(ReportingKurs::istEpochalunterricht);
		reg.registriereAttribut(ReportingKurs::schulnummer);
		reg.registriereAttribut(ReportingKurs::wochenstunden);

		// Attribute des zugeordneten Faches
		reg.registriereAttribut("fachId", k -> (k.fach() == null) ? null : k.fach().id());
		reg.registriereAttribut("fachKuerzel", k -> (k.fach() == null) ? null : k.fach().kuerzel());

		return reg;
	}
}
