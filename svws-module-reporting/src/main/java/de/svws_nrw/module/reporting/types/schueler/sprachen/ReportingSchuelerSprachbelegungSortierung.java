package de.svws_nrw.module.reporting.types.schueler.sprachen;

import java.util.List;

import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingSchuelerSprachbelegung}: hält die Sortierkonfiguration des Reporting-Typs
 * (Registry + Standardsortierung). Die fertige {@link ReportingSortierung}-Instanz wird über
 * {@link ReportingSchuelerSprachbelegung#SORTIERUNG} verwendet.
 */
public final class ReportingSchuelerSprachbelegungSortierung {

	/** Die Sortierkonfiguration für {@link ReportingSchuelerSprachbelegung}. */
	public static final ReportingSortierung<ReportingSchuelerSprachbelegung> SORTIERUNG =
			ReportingSortierung.<ReportingSchuelerSprachbelegung>builder()
					.registry(buildRegistry())
					.standard(List.of(
							ReportingTypesUtils.methodeToString(ReportingSchuelerSprachbelegung::belegungVonJahrgang),
							ReportingTypesUtils.methodeToString(ReportingSchuelerSprachbelegung::reihenfolge),
							ReportingTypesUtils.methodeToString(ReportingSchuelerSprachbelegung::sprache)))
					.build();

	private ReportingSchuelerSprachbelegungSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingSchuelerSprachbelegung. Initialisierung nicht möglich.");
	}

	private static SortierungRegistry<ReportingSchuelerSprachbelegung> buildRegistry() {
		final SortierungRegistry<ReportingSchuelerSprachbelegung> reg = new SortierungRegistry<>();

		reg.registiereString(ReportingSchuelerSprachbelegung::belegungVonJahrgang);
		reg.registiereString(ReportingSchuelerSprachbelegung::belegungBisJahrgang);
		reg.registiereComparable(ReportingSchuelerSprachbelegung::reihenfolge);
		reg.registiereString(ReportingSchuelerSprachbelegung::sprache);

		return reg;
	}
}
