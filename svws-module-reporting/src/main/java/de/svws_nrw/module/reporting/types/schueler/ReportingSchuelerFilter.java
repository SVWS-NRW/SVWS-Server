package de.svws_nrw.module.reporting.types.schueler;

import de.svws_nrw.module.reporting.filterung.FilterRegistry;
import de.svws_nrw.module.reporting.filterung.ReportingFilterung;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingSchueler}: hält die Filterkonfiguration des Reporting-Typs (Registry).
 * Die fertige {@link ReportingFilterung}-Instanz wird über {@link ReportingSchueler#FILTER} verwendet.
 */
public final class ReportingSchuelerFilter {

	/** Die Filterkonfiguration für {@link ReportingSchueler}. */
	public static final ReportingFilterung<ReportingSchueler> FILTER =
			ReportingFilterung.<ReportingSchueler>builder()
					.registry(buildRegistry())
					.build();

	private ReportingSchuelerFilter() {
		throw new IllegalStateException("Begleit-Klasse zur Filterung von ReportingSchueler. Initialisierung nicht möglich.");
	}

	private static FilterRegistry<ReportingSchueler> buildRegistry() {
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
