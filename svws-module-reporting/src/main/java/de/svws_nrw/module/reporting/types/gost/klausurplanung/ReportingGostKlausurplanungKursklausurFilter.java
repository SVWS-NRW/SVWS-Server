package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import de.svws_nrw.module.reporting.filterung.FilterRegistry;
import de.svws_nrw.module.reporting.filterung.ReportingFilterung;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingGostKlausurplanungKursklausur}: hält die Filterkonfiguration des Reporting-Typs (Registry).
 * Die fertige {@link ReportingFilterung}-Instanz wird über {@link ReportingGostKlausurplanungKursklausur#FILTER} verwendet.
 */
public final class ReportingGostKlausurplanungKursklausurFilter {

	/** Die Filterkonfiguration für {@link ReportingGostKlausurplanungKursklausur}. */
	public static final ReportingFilterung<ReportingGostKlausurplanungKursklausur> FILTER =
			ReportingFilterung.<ReportingGostKlausurplanungKursklausur>builder()
					.registry(buildRegistry())
					.build();

	private ReportingGostKlausurplanungKursklausurFilter() {
		throw new IllegalStateException("Begleit-Klasse zur Filterung von ReportingGostKlausurplanungKursklausur. Initialisierung nicht möglich.");
	}

	private static FilterRegistry<ReportingGostKlausurplanungKursklausur> buildRegistry() {
		final FilterRegistry<ReportingGostKlausurplanungKursklausur> reg = new FilterRegistry<>();

		// Grundlegende Attribute
		reg.registriereAttribut(ReportingGostKlausurplanungKursklausur::id);
		reg.registriereAttribut(ReportingGostKlausurplanungKursklausur::dauer);
		reg.registriereAttribut(ReportingGostKlausurplanungKursklausur::istMdlPruefung);
		reg.registriereAttribut(ReportingGostKlausurplanungKursklausur::istAudioNotwendig);
		reg.registriereAttribut(ReportingGostKlausurplanungKursklausur::istVideoNotwendig);

		// Übernehme die Kurs-Filterattribute unter dem Prefix "kurs."
		reg.importiereRegistryEintraege(
				ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungKursklausur::kurs) + ".",
				ReportingKurs.FILTER.registry(),
				ReportingGostKlausurplanungKursklausur::kurs);

		return reg;
	}
}
