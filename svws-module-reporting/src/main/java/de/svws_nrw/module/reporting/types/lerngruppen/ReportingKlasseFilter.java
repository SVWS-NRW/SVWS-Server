package de.svws_nrw.module.reporting.types.lerngruppen;

import de.svws_nrw.module.reporting.filterung.FilterRegistry;
import de.svws_nrw.module.reporting.filterung.ReportingFilterung;

/**
 * Begleit-Datei zu {@link ReportingKlasse}: hält die Filterkonfiguration des Reporting-Typs (Registry).
 * Die fertige {@link ReportingFilterung}-Instanz wird über {@link ReportingKlasse#FILTER} verwendet.
 */
public final class ReportingKlasseFilter {

	/** Die Filterkonfiguration für {@link ReportingKlasse}. */
	public static final ReportingFilterung<ReportingKlasse> FILTER =
			ReportingFilterung.<ReportingKlasse>builder()
					.registry(buildRegistry())
					.build();

	private ReportingKlasseFilter() {
		throw new IllegalStateException("Begleit-Klasse zur Filterung von ReportingKlasse. Initialisierung nicht möglich.");
	}

	private static FilterRegistry<ReportingKlasse> buildRegistry() {
		final FilterRegistry<ReportingKlasse> reg = new FilterRegistry<>();

		// Grundlegende Attribute
		reg.registriereAttribut(ReportingKlasse::id);
		reg.registriereAttribut(ReportingKlasse::kuerzel);
		reg.registriereAttribut(ReportingKlasse::parallelitaet);
		reg.registriereAttribut(ReportingKlasse::teilstandort);
		reg.registriereAttribut(ReportingKlasse::beschreibung);

		// IDs verwandter Entitäten
		reg.registriereAttribut(ReportingKlasse::idJahrgang);
		reg.registriereAttribut(ReportingKlasse::idKlassenart);
		reg.registriereAttribut(ReportingKlasse::idSchulgliederung);

		// Attribute des zugeordneten Jahrgangs
		reg.registriereAttribut("jahrgangId", k -> (k.jahrgang() == null) ? null : k.jahrgang().id());
		reg.registriereAttribut("jahrgangKuerzel", k -> (k.jahrgang() == null) ? null : k.jahrgang().kuerzel());

		return reg;
	}
}
