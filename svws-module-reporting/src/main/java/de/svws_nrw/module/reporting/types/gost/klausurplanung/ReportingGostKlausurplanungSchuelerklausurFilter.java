package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import de.svws_nrw.module.reporting.filterung.FilterRegistry;
import de.svws_nrw.module.reporting.filterung.ReportingFilterung;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingGostKlausurplanungSchuelerklausur}: hält die Filterkonfiguration des Reporting-Typs (Registry).
 * Die fertige {@link ReportingFilterung}-Instanz wird über {@link ReportingGostKlausurplanungSchuelerklausur#FILTER} verwendet.
 */
public final class ReportingGostKlausurplanungSchuelerklausurFilter {

	/** Die Filterkonfiguration für {@link ReportingGostKlausurplanungSchuelerklausur}. */
	public static final ReportingFilterung<ReportingGostKlausurplanungSchuelerklausur> FILTER =
			ReportingFilterung.<ReportingGostKlausurplanungSchuelerklausur>builder()
					.registry(buildRegistry())
					.build();

	private ReportingGostKlausurplanungSchuelerklausurFilter() {
		throw new IllegalStateException("Begleit-Klasse zur Filterung von ReportingGostKlausurplanungSchuelerklausur. Initialisierung nicht möglich.");
	}

	private static FilterRegistry<ReportingGostKlausurplanungSchuelerklausur> buildRegistry() {
		final FilterRegistry<ReportingGostKlausurplanungSchuelerklausur> reg = new FilterRegistry<>();

		// Grundlegende Attribute der Schülerklausur
		reg.registriereAttribut(ReportingGostKlausurplanungSchuelerklausur::id);
		reg.registriereAttribut(ReportingGostKlausurplanungSchuelerklausur::idSchuelerklausurtermin);
		reg.registriereAttribut(ReportingGostKlausurplanungSchuelerklausur::nummerTerminfolge);

		// Übernehme die Schüler-Filterattribute unter dem Prefix "schueler."
		reg.importiereRegistryEintraege(
				ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungSchuelerklausur::schueler) + ".",
				ReportingSchueler.FILTER.registry(),
				ReportingGostKlausurplanungSchuelerklausur::schueler);

		// Übernehme die Klausurtermin-Filterattribute unter dem Prefix "klausurtermin."
		reg.importiereRegistryEintraege(
				ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungSchuelerklausur::klausurtermin) + ".",
				ReportingGostKlausurplanungKlausurtermin.FILTER.registry(),
				ReportingGostKlausurplanungSchuelerklausur::klausurtermin);

		return reg;
	}
}
