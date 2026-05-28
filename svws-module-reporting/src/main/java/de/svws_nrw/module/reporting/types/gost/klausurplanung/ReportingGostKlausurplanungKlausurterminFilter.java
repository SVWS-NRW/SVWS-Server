package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import de.svws_nrw.module.reporting.filterung.FilterRegistry;
import de.svws_nrw.module.reporting.filterung.ReportingFilterung;

/**
 * Begleit-Datei zu {@link ReportingGostKlausurplanungKlausurtermin}: hält die Filterkonfiguration des Reporting-Typs (Registry).
 * Die fertige {@link ReportingFilterung}-Instanz wird über {@link ReportingGostKlausurplanungKlausurtermin#FILTER} verwendet.
 */
public final class ReportingGostKlausurplanungKlausurterminFilter {

	/** Die Filterkonfiguration für {@link ReportingGostKlausurplanungKlausurtermin}. */
	public static final ReportingFilterung<ReportingGostKlausurplanungKlausurtermin> FILTER =
			ReportingFilterung.<ReportingGostKlausurplanungKlausurtermin>builder()
					.registry(buildRegistry())
					.build();

	private ReportingGostKlausurplanungKlausurterminFilter() {
		throw new IllegalStateException("Begleit-Klasse zur Filterung von ReportingGostKlausurplanungKlausurtermin. Initialisierung nicht möglich.");
	}

	private static FilterRegistry<ReportingGostKlausurplanungKlausurtermin> buildRegistry() {
		final FilterRegistry<ReportingGostKlausurplanungKlausurtermin> reg = new FilterRegistry<>();

		// Grundlegende Attribute
		reg.registriereAttribut(ReportingGostKlausurplanungKlausurtermin::gostHalbjahr);
		reg.registriereAttribut(ReportingGostKlausurplanungKlausurtermin::quartal);
		reg.registriereAttribut(ReportingGostKlausurplanungKlausurtermin::istHaupttermin);
		reg.registriereAttribut(ReportingGostKlausurplanungKlausurtermin::datum);
		reg.registriereAttribut(ReportingGostKlausurplanungKlausurtermin::bezeichnung);
		reg.registriereAttribut(ReportingGostKlausurplanungKlausurtermin::nachschreiberZugelassen);

		return reg;
	}
}
