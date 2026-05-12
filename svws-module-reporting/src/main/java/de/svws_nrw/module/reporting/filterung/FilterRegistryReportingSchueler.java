package de.svws_nrw.module.reporting.filterung;

import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

/**
 * Registry zur Definition erlaubter Filterattribute für {@link ReportingSchueler}
 * sowie Hilfsmethoden zum Erzeugen passender Predicates.
 */
public final class FilterRegistryReportingSchueler {

	private FilterRegistryReportingSchueler() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zur Filterung von Daten für das Reporting. Initialisierung nicht möglich.");
	}

	/**
	 * Stellt die {@link FilterRegistry} für die Klasse {@link ReportingSchueler} öffentlich zur Verfügung.
	 * Diese Methode wird vom {@link ReportingContext} via Reflection aufgerufen.
	 *
	 * @return Die konfigurierte Instanz von {@link FilterRegistry} für {@link ReportingSchueler}.
	 */
	public static FilterRegistry<ReportingSchueler> filterRegistry() {
		return filterRegistryReportingSchueler();
	}

	/**
	 * Erstellt und konfiguriert eine {@link FilterRegistry} für die Klasse {@link ReportingSchueler}.
	 * Hier werden alle Attribute registriert, auf die ein Filter in den ReportingParametern angewendet werden kann.
	 *
	 * @return Die konfigurierte Instanz von {@link FilterRegistry} für {@link ReportingSchueler}.
	 */
	private static FilterRegistry<ReportingSchueler> filterRegistryReportingSchueler() {
		final FilterRegistry<ReportingSchueler> reg = new FilterRegistry<>();

		// Grundlegende Attribute
		reg.registriereAttribut("id", ReportingSchueler::id);
		reg.registriereAttribut("nachname", ReportingSchueler::nachname);
		reg.registriereAttribut("vorname", ReportingSchueler::vorname);
		reg.registriereAttribut("geschlecht", s -> (s.geschlecht() == null) ? null : s.geschlecht().name());

		// Status-/Eigenschaftsattribute
		reg.registriereAttribut("status", s -> (s.status() == null) ? null : s.status().name());
		reg.registriereAttribut("istVolljaehrig", ReportingSchueler::istVolljaehrig);
		reg.registriereAttribut("istDuplikat", ReportingSchueler::istDuplikat);
		reg.registriereAttribut("hatMigrationshintergrund", ReportingSchueler::hatMigrationshintergrund);
		reg.registriereAttribut("externeSchulNr", ReportingSchueler::externeSchulNr);
		reg.registriereAttribut("anmeldedatum", ReportingSchueler::anmeldedatum);
		reg.registriereAttribut("aufnahmedatum", ReportingSchueler::aufnahmedatum);

		return reg;
	}
}
