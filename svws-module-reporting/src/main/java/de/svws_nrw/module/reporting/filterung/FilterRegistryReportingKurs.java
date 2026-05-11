package de.svws_nrw.module.reporting.filterung;

import de.svws_nrw.module.reporting.repositories.ReportingRepository;
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
	 * Diese Methode wird vom {@link ReportingRepository} via Reflection aufgerufen.
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
		reg.registriereAttribut("id", ReportingKurs::id);
		reg.registriereAttribut("kuerzel", ReportingKurs::kuerzel);
		reg.registriereAttribut("bezeichnungZeugnis", ReportingKurs::bezeichnungZeugnis);
		reg.registriereAttribut("sortierung", ReportingKurs::sortierung);

		// Kursspezifische Attribute
		reg.registriereAttribut("kursartAllg", ReportingKurs::kursartAllg);
		reg.registriereAttribut("istSichtbar", ReportingKurs::istSichtbar);
		reg.registriereAttribut("istEpochalunterricht", ReportingKurs::istEpochalunterricht);
		reg.registriereAttribut("schulnummer", ReportingKurs::schulnummer);
		reg.registriereAttribut("wochenstunden", ReportingKurs::wochenstunden);

		// Attribute des zugeordneten Faches
		reg.registriereAttribut("fachId", k -> (k.fach() == null) ? null : k.fach().id());
		reg.registriereAttribut("fachKuerzel", k -> (k.fach() == null) ? null : k.fach().kuerzel());

		return reg;
	}
}
