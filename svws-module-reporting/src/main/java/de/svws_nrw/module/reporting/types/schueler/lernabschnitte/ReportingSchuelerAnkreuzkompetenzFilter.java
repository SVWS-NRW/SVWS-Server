package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import de.svws_nrw.module.reporting.filterung.FilterRegistry;
import de.svws_nrw.module.reporting.filterung.ReportingFilterung;
import de.svws_nrw.module.reporting.types.ankreuzkompetenz.ReportingAnkreuzkompetenz;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingSchuelerAnkreuzkompetenz}: hält die Filterkonfiguration des Reporting-Typs (Registry).
 * Die fertige {@link ReportingFilterung}-Instanz wird über {@link ReportingSchuelerAnkreuzkompetenz#FILTER} verwendet.
 */
public final class ReportingSchuelerAnkreuzkompetenzFilter {

	/** Die Filterkonfiguration für {@link ReportingSchuelerAnkreuzkompetenz}. */
	public static final ReportingFilterung<ReportingSchuelerAnkreuzkompetenz> FILTER =
			ReportingFilterung.<ReportingSchuelerAnkreuzkompetenz>builder()
					.registry(buildRegistry())
					.build();

	private ReportingSchuelerAnkreuzkompetenzFilter() {
		throw new IllegalStateException("Begleit-Klasse zur Filterung von ReportingSchuelerAnkreuzkompetenz. Initialisierung nicht möglich.");
	}

	private static FilterRegistry<ReportingSchuelerAnkreuzkompetenz> buildRegistry() {
		final FilterRegistry<ReportingSchuelerAnkreuzkompetenz> reg = new FilterRegistry<>();

		// Grundlegende Attribute
		reg.registriereAttribut(ReportingSchuelerAnkreuzkompetenz::id);
		reg.registriereAttribut(ReportingSchuelerAnkreuzkompetenz::stufe1);
		reg.registriereAttribut(ReportingSchuelerAnkreuzkompetenz::stufe2);
		reg.registriereAttribut(ReportingSchuelerAnkreuzkompetenz::stufe3);
		reg.registriereAttribut(ReportingSchuelerAnkreuzkompetenz::stufe4);
		reg.registriereAttribut(ReportingSchuelerAnkreuzkompetenz::stufe5);

		// Attribute der zugeordneten Katalog-Kompetenz mit Prefix "ankreuzkompetenz" aus deren Registry übernehmen.
		reg.importiereRegistryEintraege(ReportingTypesUtils.methodeToString(ReportingSchuelerAnkreuzkompetenz::ankreuzkompetenz) + ".",
				ReportingAnkreuzkompetenz.FILTER.registry(), ReportingSchuelerAnkreuzkompetenz::ankreuzkompetenz);

		return reg;
	}
}
