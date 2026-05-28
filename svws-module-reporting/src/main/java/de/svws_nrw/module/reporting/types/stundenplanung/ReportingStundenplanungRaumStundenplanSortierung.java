package de.svws_nrw.module.reporting.types.stundenplanung;

import java.util.List;

import de.svws_nrw.module.reporting.sortierung.FunktionBuilder;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingStundenplanungRaumStundenplan}: hält die Sortierkonfiguration des Reporting-Typs (Registry + Standardsortierung).
 * Die fertige {@link ReportingSortierung}-Instanz wird über {@link ReportingStundenplanungRaumStundenplan#SORTIERUNG} verwendet.
 */
public final class ReportingStundenplanungRaumStundenplanSortierung {

	/** Die Sortierkonfiguration für {@link ReportingStundenplanungRaumStundenplan}. */
	public static final ReportingSortierung<ReportingStundenplanungRaumStundenplan> SORTIERUNG =
			ReportingSortierung.<ReportingStundenplanungRaumStundenplan>builder()
					.registry(buildRegistry())
					.standard(List.of(
							ReportingTypesUtils.methodeToString(ReportingStundenplanungRaumStundenplan::raum) + "."
									+ ReportingTypesUtils.methodeToString(ReportingStundenplanungRaum::kuerzel),
							ReportingTypesUtils.methodeToString(ReportingStundenplanungRaumStundenplan::raum) + "."
									+ ReportingTypesUtils.methodeToString(ReportingStundenplanungRaum::id)))
					.build();

	private ReportingStundenplanungRaumStundenplanSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingStundenplanungRaumStundenplan. Initialisierung nicht möglich.");
	}

	private static SortierungRegistry<ReportingStundenplanungRaumStundenplan> buildRegistry() {
		final SortierungRegistry<ReportingStundenplanungRaumStundenplan> reg = new SortierungRegistry<>();

		// Sortierattribute für den eingebetteten Stundenplan
		reg.registiereComparable(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungRaumStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::id),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::id)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungRaumStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::beschreibung),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::beschreibung)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungRaumStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::gueltigAb),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigAb)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungRaumStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::gueltigBis),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigBis)
						.toFunction());
		reg.registiereComparable(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungRaumStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::wochenperiodizitaet),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::wochenperiodizitaet)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungRaumStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungRaumStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang)
						.toFunction());

		// Sortierattribute für den eingebetteten Raum
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungRaumStundenplan::raum) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungRaum::kuerzel),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::raum)
						.then(ReportingStundenplanungRaum::kuerzel)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungRaumStundenplan::raum) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungRaum::beschreibung),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::raum)
						.then(ReportingStundenplanungRaum::beschreibung)
						.toFunction());
		reg.registiereComparable(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungRaumStundenplan::raum) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungRaum::id),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::raum)
						.then(ReportingStundenplanungRaum::id)
						.toFunction());
		reg.registiereComparable(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungRaumStundenplan::raum) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungRaum::kapazitaet),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::raum)
						.then(ReportingStundenplanungRaum::kapazitaet)
						.toFunction());

		return reg;
	}
}
