package de.svws_nrw.module.reporting.types.fach;

import java.util.List;

import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingFach}: hält die Sortierkonfiguration des Reporting-Typs (Registry + Standardsortierung).
 * Die fertige {@link ReportingSortierung}-Instanz wird über {@link ReportingFach#SORTIERUNG} verwendet.
 */
public final class ReportingFachSortierung {

	/** Die Sortierkonfiguration für {@link ReportingFach}. */
	public static final ReportingSortierung<ReportingFach> SORTIERUNG =
			ReportingSortierung.<ReportingFach>builder()
					.registry(buildRegistry())
					.standard(List.of(
							ReportingTypesUtils.methodeToString(ReportingFach::sortierungEintrag),
							ReportingTypesUtils.methodeToString(ReportingFach::kuerzel),
							ReportingTypesUtils.methodeToString(ReportingFach::id)))
					.build();

	private ReportingFachSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingFach. Initialisierung nicht möglich.");
	}

	private static SortierungRegistry<ReportingFach> buildRegistry() {
		final SortierungRegistry<ReportingFach> reg = new SortierungRegistry<>();

		// Grundlegende Attribute
		reg.registiereComparable(ReportingFach::id);
		reg.registiereString(ReportingFach::kuerzel);
		reg.registiereString(ReportingFach::bezeichnung);
		reg.registiereString(ReportingFach::bezeichnungZeugnis);
		reg.registiereString(ReportingFach::bezeichnungUeberweisungszeugnis);
		reg.registiereComparable(ReportingFach::sortierungEintrag);

		// Fachspezifische Attribute
		reg.registiereString(ReportingFach::aufgabenfeld);
		reg.registiereString(ReportingFach::bilingualeSprache);
		reg.registiereComparable(ReportingFach::aufZeugnis);
		reg.registiereComparable(ReportingFach::fachgruppe);
		reg.registiereComparable(ReportingFach::istPruefungsordnungsRelevant);
		reg.registiereComparable(ReportingFach::istGostFach);
		reg.registiereComparable(ReportingFach::istSichtbar);

		// Spezielle Sortierung für die gymnasiale Oberstufe.
		reg.registiereComparable("gostSortierung", fach -> {
			if (fach.statistikfach() == null) {
				return Integer.MAX_VALUE;
			}
			return GostFachbereich.getSortierung(Fach.getBySchluesselOrDefault(fach.statistikfach().kuerzelASD()));
		});

		return reg;
	}
}
