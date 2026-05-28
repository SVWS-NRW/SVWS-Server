package de.svws_nrw.module.reporting.types.lerngruppen;

import de.svws_nrw.module.reporting.filterung.FilterRegistry;
import de.svws_nrw.module.reporting.filterung.ReportingFilterung;

/**
 * Begleit-Datei zu {@link ReportingKurs}: hält die Filterkonfiguration des Reporting-Typs (Registry).
 * Die fertige {@link ReportingFilterung}-Instanz wird über {@link ReportingKurs#FILTER} verwendet.
 */
public final class ReportingKursFilter {

	/** Die Filterkonfiguration für {@link ReportingKurs}. */
	public static final ReportingFilterung<ReportingKurs> FILTER =
			ReportingFilterung.<ReportingKurs>builder()
					.registry(buildRegistry())
					.build();

	private ReportingKursFilter() {
		throw new IllegalStateException("Begleit-Klasse zur Filterung von ReportingKurs. Initialisierung nicht möglich.");
	}

	private static FilterRegistry<ReportingKurs> buildRegistry() {
		final FilterRegistry<ReportingKurs> reg = new FilterRegistry<>();

		// Grundlegende Attribute
		reg.registriereAttribut(ReportingKurs::id);
		reg.registriereAttribut(ReportingKurs::kuerzel);
		reg.registriereAttribut(ReportingKurs::bezeichnungZeugnis);
		reg.registriereAttribut(ReportingKurs::sortierungEintrag);

		// Kursspezifische Attribute
		reg.registriereAttribut(ReportingKurs::kursartAllg);
		reg.registriereAttribut(ReportingKurs::istSichtbar);
		reg.registriereAttribut(ReportingKurs::istEpochalunterricht);
		reg.registriereAttribut(ReportingKurs::schulnummer);
		reg.registriereAttribut(ReportingKurs::wochenstunden);

		// Attribute des zugeordneten Faches
		reg.registriereAttribut("fachId", k -> (k.fach() == null) ? null : k.fach().id());
		reg.registriereAttribut("fachKuerzel", k -> (k.fach() == null) ? null : k.fach().kuerzel());

		return reg;
	}
}
