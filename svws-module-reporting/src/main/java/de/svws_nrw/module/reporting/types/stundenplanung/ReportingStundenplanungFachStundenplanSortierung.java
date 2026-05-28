package de.svws_nrw.module.reporting.types.stundenplanung;

import java.util.List;

import de.svws_nrw.module.reporting.sortierung.FunktionBuilder;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingStundenplanungFachStundenplan}: hält die Sortierkonfiguration des Reporting-Typs (Registry + Standardsortierung).
 * Die fertige {@link ReportingSortierung}-Instanz wird über {@link ReportingStundenplanungFachStundenplan#SORTIERUNG} verwendet.
 */
public final class ReportingStundenplanungFachStundenplanSortierung {

	/** Die Sortierkonfiguration für {@link ReportingStundenplanungFachStundenplan}. */
	public static final ReportingSortierung<ReportingStundenplanungFachStundenplan> SORTIERUNG =
			ReportingSortierung.<ReportingStundenplanungFachStundenplan>builder()
					.registry(buildRegistry())
					.standard(List.of(
							ReportingTypesUtils.methodeToString(ReportingStundenplanungFachStundenplan::fach) + "."
									+ ReportingTypesUtils.methodeToString(ReportingFach::sortierungEintrag),
							ReportingTypesUtils.methodeToString(ReportingStundenplanungFachStundenplan::fach) + "."
									+ ReportingTypesUtils.methodeToString(ReportingFach::kuerzel),
							ReportingTypesUtils.methodeToString(ReportingStundenplanungFachStundenplan::fach) + "."
									+ ReportingTypesUtils.methodeToString(ReportingFach::id)))
					.build();

	private ReportingStundenplanungFachStundenplanSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingStundenplanungFachStundenplan. Initialisierung nicht möglich.");
	}

	private static SortierungRegistry<ReportingStundenplanungFachStundenplan> buildRegistry() {
		final SortierungRegistry<ReportingStundenplanungFachStundenplan> reg = new SortierungRegistry<>();

		// Sortierattribute für den eingebetteten Stundenplan
		reg.registiereComparable(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungFachStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::id),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::id)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungFachStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::beschreibung),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::beschreibung)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungFachStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::gueltigAb),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigAb)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungFachStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::gueltigBis),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigBis)
						.toFunction());
		reg.registiereComparable(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungFachStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::wochenperiodizitaet),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::wochenperiodizitaet)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungFachStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungFachStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang)
						.toFunction());

		// Sortierattribute für das eingebettete Fach
		reg.registiereComparable(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungFachStundenplan::fach) + "."
						+ ReportingTypesUtils.methodeToString(ReportingFach::id),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::fach)
						.then(ReportingFach::id)
						.toFunction());
		reg.registiereComparable(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungFachStundenplan::fach) + "."
						+ ReportingTypesUtils.methodeToString(ReportingFach::sortierungEintrag),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::fach)
						.then(ReportingFach::sortierungEintrag)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungFachStundenplan::fach) + "."
						+ ReportingTypesUtils.methodeToString(ReportingFach::kuerzel),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::fach)
						.then(ReportingFach::kuerzel)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungFachStundenplan::fach) + "."
						+ ReportingTypesUtils.methodeToString(ReportingFach::bezeichnung),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::fach)
						.then(ReportingFach::bezeichnung)
						.toFunction());

		return reg;
	}
}
