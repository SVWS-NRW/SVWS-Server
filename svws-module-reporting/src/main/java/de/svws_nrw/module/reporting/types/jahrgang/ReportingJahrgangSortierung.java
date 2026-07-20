package de.svws_nrw.module.reporting.types.jahrgang;

import java.util.List;

import de.svws_nrw.module.reporting.sortierung.FunktionBuilder;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingJahrgang}: hält die Sortierkonfiguration des Reporting-Typs (Registry + Standardsortierung).
 * Die fertige {@link ReportingSortierung}-Instanz wird über {@link ReportingJahrgang#SORTIERUNG} verwendet.
 */
public final class ReportingJahrgangSortierung {

	/** Die Sortierkonfiguration für {@link ReportingJahrgang}. */
	public static final ReportingSortierung<ReportingJahrgang> SORTIERUNG =
			ReportingSortierung.<ReportingJahrgang>builder()
					.registry(buildRegistry())
					.standard(List.of(
							ReportingTypesUtils.methodeToString(ReportingJahrgang::sortierungEintrag),
							ReportingTypesUtils.methodeToString(ReportingJahrgang::kuerzel),
							ReportingTypesUtils.methodeToString(ReportingJahrgang::id)))
					.build();

	private ReportingJahrgangSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingJahrgang. Initialisierung nicht möglich.");
	}

	private static SortierungRegistry<ReportingJahrgang> buildRegistry() {
		final SortierungRegistry<ReportingJahrgang> reg = new SortierungRegistry<>();

		// Grundlegende Attribute
		reg.registiereComparable(ReportingJahrgang::id);
		reg.registiereComparable(ReportingJahrgang::sortierungEintrag);
		reg.registiereString(ReportingJahrgang::kuerzel);
		reg.registiereString(ReportingJahrgang::bezeichnung);
		reg.registiereComparable(ReportingJahrgang::idSchulgliederung);
		reg.registiereComparable(ReportingJahrgang::idJahrgang);
		reg.registiereComparable(ReportingJahrgang::istSichtbar);
		reg.registiereComparable(ReportingJahrgang::anzahlRestabschnitte);
		reg.registiereComparable(ReportingJahrgang::gueltigVon);
		reg.registiereComparable(ReportingJahrgang::gueltigBis);
		reg.registiereComparable(ReportingJahrgang::idFolgejahrgang);

		// Verschachtelte Attribute: Schuljahresabschnitt
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingJahrgang::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz),
				FunktionBuilder.start(ReportingJahrgang::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingJahrgang::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang),
				FunktionBuilder.start(ReportingJahrgang::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang)
						.toFunction());

		return reg;
	}
}
