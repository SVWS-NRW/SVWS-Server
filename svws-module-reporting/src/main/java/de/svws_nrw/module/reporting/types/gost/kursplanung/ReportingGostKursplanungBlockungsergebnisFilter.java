package de.svws_nrw.module.reporting.types.gost.kursplanung;

import de.svws_nrw.module.reporting.filterung.FilterRegistry;
import de.svws_nrw.module.reporting.filterung.ReportingFilterung;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingGostKursplanungBlockungsergebnis}: hält die Filterkonfiguration des Reporting-Typs (Registry).
 * Die fertige {@link ReportingFilterung}-Instanz wird über {@link ReportingGostKursplanungBlockungsergebnis#FILTER} verwendet.
 */
public final class ReportingGostKursplanungBlockungsergebnisFilter {

	/** Die Filterkonfiguration für {@link ReportingGostKursplanungBlockungsergebnis}. */
	public static final ReportingFilterung<ReportingGostKursplanungBlockungsergebnis> FILTER =
			ReportingFilterung.<ReportingGostKursplanungBlockungsergebnis>builder()
					.registry(buildRegistry())
					.build();

	private ReportingGostKursplanungBlockungsergebnisFilter() {
		throw new IllegalStateException(
				"Begleit-Klasse zur Filterung von ReportingGostKursplanungBlockungsergebnis. Initialisierung nicht möglich.");
	}

	private static FilterRegistry<ReportingGostKursplanungBlockungsergebnis> buildRegistry() {
		final FilterRegistry<ReportingGostKursplanungBlockungsergebnis> reg = new FilterRegistry<>();

		reg.registriereAttribut(ReportingGostKursplanungBlockungsergebnis::id);
		reg.registriereAttribut(ReportingGostKursplanungBlockungsergebnis::abiturjahr);
		reg.registriereAttribut(ReportingGostKursplanungBlockungsergebnis::bezeichnung);
		reg.registriereAttribut(ReportingTypesUtils.methodeToString(ReportingGostKursplanungBlockungsergebnis::gostHalbjahr),
				e -> (e.gostHalbjahr() == null) ? null : e.gostHalbjahr().name());
		reg.registriereAttribut(ReportingGostKursplanungBlockungsergebnis::anzahlSchienen);
		reg.registriereAttribut(ReportingGostKursplanungBlockungsergebnis::anzahlSchueler);
		reg.registriereAttribut(ReportingGostKursplanungBlockungsergebnis::anzahlDummy);
		reg.registriereAttribut(ReportingGostKursplanungBlockungsergebnis::anzahlExterne);
		reg.registriereAttribut(ReportingGostKursplanungBlockungsergebnis::anzahlMaxKurseProSchiene);

		return reg;
	}
}
