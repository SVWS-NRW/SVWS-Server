package de.svws_nrw.module.reporting.filterung;

import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Registry zur Definition erlaubter Filterattribute für {@link ReportingFach}
 * sowie Hilfsmethoden zum Erzeugen passender Predicates.
 */
public final class FilterRegistryReportingFach {

	private FilterRegistryReportingFach() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zur Filterung von Daten für das Reporting. Initialisierung nicht möglich.");
	}

	/**
	 * Stellt die {@link FilterRegistry} für die Klasse {@link ReportingFach} öffentlich zur Verfügung.
	 * Diese Methode wird vom {@link ReportingContext} via Reflection aufgerufen.
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
		reg.registriereAttribut(ReportingFach::id);
		reg.registriereAttribut(ReportingFach::kuerzel);
		reg.registriereAttribut(ReportingFach::bezeichnung);
		reg.registriereAttribut(ReportingFach::bezeichnungZeugnis);
		reg.registriereAttribut(ReportingFach::bezeichnungUeberweisungszeugnis);
		reg.registriereAttribut(ReportingFach::sortierung);

		// Fachspezifische Attribute
		reg.registriereAttribut(ReportingFach::aufgabenfeld);
		reg.registriereAttribut(ReportingFach::bilingualeSprache);
		reg.registriereAttribut(ReportingFach::aufZeugnis);
		reg.registriereAttribut(ReportingTypesUtils.methodeToString(ReportingFach::fachgruppe), f -> (f.fachgruppe() == null) ? null : f.fachgruppe().name());
		reg.registriereAttribut(ReportingFach::istPruefungsordnungsRelevant);
		reg.registriereAttribut(ReportingFach::istGostFach);
		reg.registriereAttribut(ReportingFach::istSichtbar);
		reg.registriereAttribut(ReportingFach::istFremdsprache);
		reg.registriereAttribut(ReportingFach::istFremdSpracheNeuEinsetzend);
		reg.registriereAttribut(ReportingFach::istNachpruefungErlaubt);
		reg.registriereAttribut(ReportingFach::istSchriftlichBA);
		reg.registriereAttribut(ReportingFach::istSchriftlichZK);

		// Attribut für das Statistik-Fach (ASD-Kürzel)
		reg.registriereAttribut("statistikKuerzel", f -> (f.statistikfach() == null) ? null : f.statistikfach().kuerzelASD());

		return reg;
	}
}
