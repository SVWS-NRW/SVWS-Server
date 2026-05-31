package de.svws_nrw.module.reporting.types.gost.kursplanung;

import de.svws_nrw.module.reporting.filterung.FilterRegistry;
import de.svws_nrw.module.reporting.filterung.ReportingFilterung;

/**
 * Begleit-Datei zu {@link ReportingGostKursplanungSchiene}: hält die Filterkonfiguration des Reporting-Typs (Registry).
 * Die fertige {@link ReportingFilterung}-Instanz wird über {@link ReportingGostKursplanungSchiene#FILTER} verwendet.
 */
public final class ReportingGostKursplanungSchieneFilter {

	/** Die Filterkonfiguration für {@link ReportingGostKursplanungSchiene}. */
	public static final ReportingFilterung<ReportingGostKursplanungSchiene> FILTER =
			ReportingFilterung.<ReportingGostKursplanungSchiene>builder()
					.registry(buildRegistry())
					.build();

	private ReportingGostKursplanungSchieneFilter() {
		throw new IllegalStateException("Begleit-Klasse zur Filterung von ReportingGostKursplanungSchiene. Initialisierung nicht möglich.");
	}

	private static FilterRegistry<ReportingGostKursplanungSchiene> buildRegistry() {
		final FilterRegistry<ReportingGostKursplanungSchiene> reg = new FilterRegistry<>();

		reg.registriereAttribut(ReportingGostKursplanungSchiene::id);
		reg.registriereAttribut(ReportingGostKursplanungSchiene::nummer);
		reg.registriereAttribut(ReportingGostKursplanungSchiene::bezeichnung);
		reg.registriereAttribut(ReportingGostKursplanungSchiene::anzahlSchueler);
		reg.registriereAttribut(ReportingGostKursplanungSchiene::anzahlDummy);
		reg.registriereAttribut(ReportingGostKursplanungSchiene::anzahlExterne);
		reg.registriereAttribut(ReportingGostKursplanungSchiene::hatKollisionen);

		return reg;
	}
}
