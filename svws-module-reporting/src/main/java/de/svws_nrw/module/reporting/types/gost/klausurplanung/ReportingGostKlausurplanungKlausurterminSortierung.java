package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import java.util.List;

import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingGostKlausurplanungKlausurtermin}: hält die Sortierkonfiguration des Reporting-Typs (Registry + Standardsortierung).
 * Die fertige {@link ReportingSortierung}-Instanz wird über {@link ReportingGostKlausurplanungKlausurtermin#SORTIERUNG} verwendet.
 */
public final class ReportingGostKlausurplanungKlausurterminSortierung {

	/** Die Sortierkonfiguration für {@link ReportingGostKlausurplanungKlausurtermin}. */
	public static final ReportingSortierung<ReportingGostKlausurplanungKlausurtermin> SORTIERUNG =
			ReportingSortierung.<ReportingGostKlausurplanungKlausurtermin>builder()
					.registry(buildRegistry())
					.standard(List.of(
							ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungKlausurtermin::datum),
							ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungKlausurtermin::startuhrzeit),
							ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungKlausurtermin::bezeichnung),
							ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungKlausurtermin::id)))
					.build();

	private ReportingGostKlausurplanungKlausurterminSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingGostKlausurplanungKlausurtermin. Initialisierung nicht möglich.");
	}

	private static SortierungRegistry<ReportingGostKlausurplanungKlausurtermin> buildRegistry() {
		final SortierungRegistry<ReportingGostKlausurplanungKlausurtermin> reg = new SortierungRegistry<>();

		// Grundlegende Attribute des Klausurtermins
		reg.registiereString(ReportingGostKlausurplanungKlausurtermin::bemerkung);
		reg.registiereString(ReportingGostKlausurplanungKlausurtermin::bezeichnung);
		reg.registiereString(ReportingGostKlausurplanungKlausurtermin::datum);
		reg.registiereComparable(ReportingGostKlausurplanungKlausurtermin::gostHalbjahr);
		reg.registiereComparable(ReportingGostKlausurplanungKlausurtermin::id);
		reg.registiereComparable(ReportingGostKlausurplanungKlausurtermin::istHaupttermin);
		reg.registiereComparable(ReportingGostKlausurplanungKlausurtermin::nachschreiberZugelassen);
		reg.registiereComparable(ReportingGostKlausurplanungKlausurtermin::quartal);
		reg.registiereString(ReportingGostKlausurplanungKlausurtermin::startuhrzeit);

		// Nützliche aggregierte Darstellung der Räume und Stunden
		reg.registiereString(ReportingGostKlausurplanungKlausurtermin::raeumeUndStunden);

		return reg;
	}
}
