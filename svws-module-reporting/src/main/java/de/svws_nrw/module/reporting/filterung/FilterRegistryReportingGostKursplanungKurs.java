package de.svws_nrw.module.reporting.filterung;

import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungKurs;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Registry zur Definition erlaubter Filterattribute für {@link ReportingGostKursplanungKurs}
 * sowie Hilfsmethoden zum Erzeugen passender Predicates.
 */
public final class FilterRegistryReportingGostKursplanungKurs {

	private FilterRegistryReportingGostKursplanungKurs() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zur Filterung von Daten für das Reporting. Initialisierung nicht möglich.");
	}

	/**
	 * Stellt die {@link FilterRegistry} für die Klasse {@link ReportingGostKursplanungKurs} öffentlich zur Verfügung.
	 * Diese Methode wird vom {@link ReportingContext} via Reflection aufgerufen.
	 *
	 * @return Die konfigurierte Instanz von {@link FilterRegistry} für {@link ReportingGostKursplanungKurs}.
	 */
	public static FilterRegistry<ReportingGostKursplanungKurs> filterRegistry() {
		return filterRegistryReportingGostKursplanungKurs();
	}

	/**
	 * Erstellt und konfiguriert eine {@link FilterRegistry} für die Klasse {@link ReportingGostKursplanungKurs}.
	 * Hier werden alle Attribute registriert, auf die ein Filter in den ReportingParametern angewendet werden kann.
	 *
	 * @return Die konfigurierte Instanz von {@link FilterRegistry} für {@link ReportingGostKursplanungKurs}.
	 */
	private static FilterRegistry<ReportingGostKursplanungKurs> filterRegistryReportingGostKursplanungKurs() {
		final FilterRegistry<ReportingGostKursplanungKurs> reg = new FilterRegistry<>();

		// Grundlegende Attribute
		reg.registriereAttribut(ReportingGostKursplanungKurs::id);
		reg.registriereAttribut(ReportingGostKursplanungKurs::bezeichnung);
		reg.registriereAttribut(ReportingTypesUtils.methodeToString(ReportingGostKursplanungKurs::gostHalbjahr),
				k -> (k.gostHalbjahr() == null) ? null : k.gostHalbjahr().name());
		reg.registriereAttribut(ReportingTypesUtils.methodeToString(ReportingGostKursplanungKurs::gostKursart),
				k -> (k.gostKursart() == null) ? null : k.gostKursart().name());

		// Anzahlen
		reg.registriereAttribut(ReportingGostKursplanungKurs::anzahlSchueler);
		reg.registriereAttribut(ReportingGostKursplanungKurs::anzahlSchuelerSchriftlich);
		reg.registriereAttribut(ReportingGostKursplanungKurs::anzahlAB12);
		reg.registriereAttribut(ReportingGostKursplanungKurs::anzahlAB3);
		reg.registriereAttribut(ReportingGostKursplanungKurs::anzahlAB4);
		reg.registriereAttribut(ReportingGostKursplanungKurs::anzahlDummy);
		reg.registriereAttribut(ReportingGostKursplanungKurs::anzahlExterne);

		// Attribute des zugeordneten Faches
		reg.registriereAttribut("fachId", k -> (k.fach() == null) ? null : k.fach().id());
		reg.registriereAttribut("fachKuerzel", k -> (k.fach() == null) ? null : k.fach().kuerzel());

		return reg;
	}
}
