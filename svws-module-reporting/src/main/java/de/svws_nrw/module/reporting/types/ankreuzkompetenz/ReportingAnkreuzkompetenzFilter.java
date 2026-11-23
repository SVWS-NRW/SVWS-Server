package de.svws_nrw.module.reporting.types.ankreuzkompetenz;

import de.svws_nrw.module.reporting.filterung.FilterRegistry;
import de.svws_nrw.module.reporting.filterung.ReportingFilterung;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingAnkreuzkompetenz}: hält die Filterkonfiguration des Reporting-Typs (Registry).
 * Die fertige {@link ReportingFilterung}-Instanz wird über {@link ReportingAnkreuzkompetenz#FILTER} verwendet.
 */
public final class ReportingAnkreuzkompetenzFilter {

	/** Die Filterkonfiguration für {@link ReportingAnkreuzkompetenz}. */
	public static final ReportingFilterung<ReportingAnkreuzkompetenz> FILTER =
			ReportingFilterung.<ReportingAnkreuzkompetenz>builder()
					.registry(buildRegistry())
					.build();

	private ReportingAnkreuzkompetenzFilter() {
		throw new IllegalStateException("Begleit-Klasse zur Filterung von ReportingAnkreuzkompetenz. Initialisierung nicht möglich.");
	}

	private static FilterRegistry<ReportingAnkreuzkompetenz> buildRegistry() {
		final FilterRegistry<ReportingAnkreuzkompetenz> reg = new FilterRegistry<>();

		// Grundlegende Attribute
		reg.registriereAttribut(ReportingAnkreuzkompetenz::id);
		reg.registriereAttribut(ReportingAnkreuzkompetenz::abschnitt);
		reg.registriereAttribut(ReportingAnkreuzkompetenz::fachSortierung);
		reg.registriereAttribut(ReportingAnkreuzkompetenz::floskelText);
		reg.registriereAttribut(ReportingAnkreuzkompetenz::istAktiv);
		reg.registriereAttribut(ReportingAnkreuzkompetenz::istASV);
		reg.registriereAttribut(ReportingAnkreuzkompetenz::istSichtbar);
		reg.registriereAttribut(ReportingAnkreuzkompetenz::unterkategorie);
		reg.registriereAttribut(ReportingAnkreuzkompetenz::kompetenzText);
		reg.registriereAttribut(ReportingAnkreuzkompetenz::schulgliederung);
		reg.registriereAttribut(ReportingAnkreuzkompetenz::sortierung);

		// Attribute des zugeordneten Faches mit Prefix "fach" aus der Fach-Registry übernehmen.
		reg.importiereRegistryEintraege(ReportingTypesUtils.methodeToString(ReportingAnkreuzkompetenz::fach) + ".",
				ReportingFach.FILTER.registry(), ReportingAnkreuzkompetenz::fach);

		return reg;
	}
}
