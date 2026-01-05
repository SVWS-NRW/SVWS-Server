package de.svws_nrw.module.reporting.filterung;

import de.svws_nrw.module.reporting.types.fach.ReportingFach;

/**
 * Registry zur Definition erlaubter Filterattribute für {@link ReportingFach}
 * sowie Hilfsmethoden zum Erzeugen passender Predicates.
 */
public final class FilterRegistryReportingFach {

	/**
	 * Stellt die {@link FilterRegistry} für die Klasse {@link ReportingFach} öffentlich zur Verfügung.
	 * Diese Methode wird vom {@link de.svws_nrw.module.reporting.repositories.ReportingRepository} via Reflection aufgerufen.
	 *
	 * @return Die konfigurierte Instanz von {@link FilterRegistry} für {@link ReportingFach}.
	 */
	public static FilterRegistry<ReportingFach> filterRegistry() {
		return filterRegistryReportingFach();
	}

	/**
	 * Erstellt und konfiguriert eine {@link FilterRegistry} für die Klasse {@link ReportingFach}.
	 * Hier werden alle Attribute registriert, auf die ein Filter in den ReportingParametern angewendet werden kann.
	 *
	 * @return Die konfigurierte Instanz von {@link FilterRegistry} für {@link ReportingFach}.
	 */
	private static FilterRegistry<ReportingFach> filterRegistryReportingFach() {
		final FilterRegistry<ReportingFach> reg = new FilterRegistry<>();

		// Grundlegende Attribute
		reg.registriereAttribut("id", ReportingFach::id);
		reg.registriereAttribut("kuerzel", ReportingFach::kuerzel);
		reg.registriereAttribut("bezeichnung", ReportingFach::bezeichnung);
		reg.registriereAttribut("bezeichnungZeugnis", ReportingFach::bezeichnungZeugnis);
		reg.registriereAttribut("bezeichnungUeberweisungszeugnis", ReportingFach::bezeichnungUeberweisungszeugnis);
		reg.registriereAttribut("sortierung", ReportingFach::sortierung);

		// Fachspezifische Attribute
		reg.registriereAttribut("aufgabenfeld", ReportingFach::aufgabenfeld);
		reg.registriereAttribut("bilingualeSprache", ReportingFach::bilingualeSprache);
		reg.registriereAttribut("aufZeugnis", ReportingFach::aufZeugnis);
		reg.registriereAttribut("fachgruppe", f -> (f.fachgruppe() == null) ? null : f.fachgruppe().name());
		reg.registriereAttribut("istPruefungsordnungsRelevant", ReportingFach::istPruefungsordnungsRelevant);
		reg.registriereAttribut("istGostFach", ReportingFach::istGostFach);
		reg.registriereAttribut("istSichtbar", ReportingFach::istSichtbar);
		reg.registriereAttribut("istFremdsprache", ReportingFach::istFremdsprache);
		reg.registriereAttribut("istFremdSpracheNeuEinsetzend", ReportingFach::istFremdSpracheNeuEinsetzend);
		reg.registriereAttribut("istNachpruefungErlaubt", ReportingFach::istNachpruefungErlaubt);
		reg.registriereAttribut("istSchriftlichBA", ReportingFach::istSchriftlichBA);
		reg.registriereAttribut("istSchriftlichZK", ReportingFach::istSchriftlichZK);

		// Attribut für das Statistik-Fach (ASD-Kürzel)
		reg.registriereAttribut("statistikKuerzel", f -> (f.statistikfach() == null) ? null : f.statistikfach().kuerzelASD());

		return reg;
	}
}
