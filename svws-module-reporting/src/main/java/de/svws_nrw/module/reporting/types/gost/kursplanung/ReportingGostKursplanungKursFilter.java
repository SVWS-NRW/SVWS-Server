package de.svws_nrw.module.reporting.types.gost.kursplanung;

import de.svws_nrw.module.reporting.filterung.FilterRegistry;
import de.svws_nrw.module.reporting.filterung.ReportingFilterung;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingGostKursplanungKurs}: hält die Filterkonfiguration des Reporting-Typs (Registry).
 * Die fertige {@link ReportingFilterung}-Instanz wird über {@link ReportingGostKursplanungKurs#FILTER} verwendet.
 */
public final class ReportingGostKursplanungKursFilter {

	/** Die Filterkonfiguration für {@link ReportingGostKursplanungKurs}. */
	public static final ReportingFilterung<ReportingGostKursplanungKurs> FILTER =
			ReportingFilterung.<ReportingGostKursplanungKurs>builder()
					.registry(buildRegistry())
					.build();

	private ReportingGostKursplanungKursFilter() {
		throw new IllegalStateException("Begleit-Klasse zur Filterung von ReportingGostKursplanungKurs. Initialisierung nicht möglich.");
	}

	private static FilterRegistry<ReportingGostKursplanungKurs> buildRegistry() {
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
