package de.svws_nrw.module.reporting.filterung;

import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

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
		reg.registriereAttribut(ReportingSchueler::id);
		reg.registriereAttribut(ReportingSchueler::nachname);
		reg.registriereAttribut(ReportingSchueler::vorname);
		reg.registriereAttribut(ReportingTypesUtils.methodeToString(ReportingSchueler::geschlecht),
				s -> (s.geschlecht() == null) ? null : s.geschlecht().name());

		// Status-/Eigenschaftsattribute
		reg.registriereAttribut(ReportingTypesUtils.methodeToString(ReportingSchueler::status),
				s -> (s.status() == null) ? null : s.status().name());
		reg.registriereAttribut(ReportingSchueler::istVolljaehrig);
		reg.registriereAttribut(ReportingSchueler::istDuplikat);
		reg.registriereAttribut(ReportingSchueler::hatMigrationshintergrund);
		reg.registriereAttribut(ReportingSchueler::externeSchulNr);
		reg.registriereAttribut(ReportingSchueler::anmeldedatum);
		reg.registriereAttribut(ReportingSchueler::aufnahmedatum);

		return reg;
	}
}
