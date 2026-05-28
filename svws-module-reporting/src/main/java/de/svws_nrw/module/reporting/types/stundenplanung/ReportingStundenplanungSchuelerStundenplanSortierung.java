package de.svws_nrw.module.reporting.types.stundenplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.module.reporting.sortierung.FunktionBuilder;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingStundenplanungSchuelerStundenplan}: hält die Sortierkonfiguration des Reporting-Typs (Registry + Standardsortierung).
 * Die fertige {@link ReportingSortierung}-Instanz wird über {@link ReportingStundenplanungSchuelerStundenplan#SORTIERUNG} verwendet.
 */
public final class ReportingStundenplanungSchuelerStundenplanSortierung {

	/** Die Sortierkonfiguration für {@link ReportingStundenplanungSchuelerStundenplan}. */
	public static final ReportingSortierung<ReportingStundenplanungSchuelerStundenplan> SORTIERUNG =
			ReportingSortierung.<ReportingStundenplanungSchuelerStundenplan>builder()
					.registry(buildRegistry())
					.standard(buildStandard())
					.build();

	private ReportingStundenplanungSchuelerStundenplanSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingStundenplanungSchuelerStundenplan. Initialisierung nicht möglich.");
	}

	private static List<String> buildStandard() {
		final ArrayList<String> standard = new ArrayList<>();
		ReportingSchueler.SORTIERUNG.standardsortierung()
				.forEach(attribut -> standard.add(ReportingTypesUtils.methodeToString(ReportingStundenplanungSchuelerStundenplan::schueler) + "." + attribut));
		return standard;
	}

	private static SortierungRegistry<ReportingStundenplanungSchuelerStundenplan> buildRegistry() {
		final SortierungRegistry<ReportingStundenplanungSchuelerStundenplan> reg = new SortierungRegistry<>();

		// Sortierattribute für den eingebetteten Stundenplan
		reg.registiereComparable(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungSchuelerStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::id),
				FunktionBuilder.start(ReportingStundenplanungSchuelerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::id)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungSchuelerStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::beschreibung),
				FunktionBuilder.start(ReportingStundenplanungSchuelerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::beschreibung)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungSchuelerStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::gueltigAb),
				FunktionBuilder.start(ReportingStundenplanungSchuelerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigAb)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungSchuelerStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::gueltigBis),
				FunktionBuilder.start(ReportingStundenplanungSchuelerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigBis)
						.toFunction());
		reg.registiereComparable(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungSchuelerStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::wochenperiodizitaet),
				FunktionBuilder.start(ReportingStundenplanungSchuelerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::wochenperiodizitaet)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungSchuelerStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz),
				FunktionBuilder.start(ReportingStundenplanungSchuelerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungSchuelerStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang),
				FunktionBuilder.start(ReportingStundenplanungSchuelerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang)
						.toFunction());

		// Importiere alle Schüler-Attribute unter dem Prefix "schueler"
		reg.importiereRegistryEintraege(ReportingTypesUtils.methodeToString(ReportingStundenplanungSchuelerStundenplan::schueler) + ".",
				ReportingSchueler.SORTIERUNG.registry(), ReportingStundenplanungSchuelerStundenplan::schueler);

		return reg;
	}
}
