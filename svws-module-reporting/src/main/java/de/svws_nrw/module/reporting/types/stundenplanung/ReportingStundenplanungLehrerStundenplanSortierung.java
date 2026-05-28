package de.svws_nrw.module.reporting.types.stundenplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.module.reporting.sortierung.FunktionBuilder;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingStundenplanungLehrerStundenplan}: hält die Sortierkonfiguration des Reporting-Typs (Registry + Standardsortierung).
 * Die fertige {@link ReportingSortierung}-Instanz wird über {@link ReportingStundenplanungLehrerStundenplan#SORTIERUNG} verwendet.
 */
public final class ReportingStundenplanungLehrerStundenplanSortierung {

	/** Die Sortierkonfiguration für {@link ReportingStundenplanungLehrerStundenplan}. */
	public static final ReportingSortierung<ReportingStundenplanungLehrerStundenplan> SORTIERUNG =
			ReportingSortierung.<ReportingStundenplanungLehrerStundenplan>builder()
					.registry(buildRegistry())
					.standard(buildStandard())
					.build();

	private ReportingStundenplanungLehrerStundenplanSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingStundenplanungLehrerStundenplan. Initialisierung nicht möglich.");
	}

	private static List<String> buildStandard() {
		final ArrayList<String> standard = new ArrayList<>();
		ReportingLehrer.SORTIERUNG.standardsortierung()
				.forEach(attribut -> standard.add(ReportingTypesUtils.methodeToString(ReportingStundenplanungLehrerStundenplan::lehrer) + "." + attribut));
		return standard;
	}

	private static SortierungRegistry<ReportingStundenplanungLehrerStundenplan> buildRegistry() {
		final SortierungRegistry<ReportingStundenplanungLehrerStundenplan> reg = new SortierungRegistry<>();

		// Sortierattribute für den eingebetteten Stundenplan
		reg.registiereComparable(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungLehrerStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::id),
				FunktionBuilder.start(ReportingStundenplanungLehrerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::id)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungLehrerStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::beschreibung),
				FunktionBuilder.start(ReportingStundenplanungLehrerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::beschreibung)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungLehrerStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::gueltigAb),
				FunktionBuilder.start(ReportingStundenplanungLehrerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigAb)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungLehrerStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::gueltigBis),
				FunktionBuilder.start(ReportingStundenplanungLehrerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigBis)
						.toFunction());
		reg.registiereComparable(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungLehrerStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::wochenperiodizitaet),
				FunktionBuilder.start(ReportingStundenplanungLehrerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::wochenperiodizitaet)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungLehrerStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz),
				FunktionBuilder.start(ReportingStundenplanungLehrerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungLehrerStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang),
				FunktionBuilder.start(ReportingStundenplanungLehrerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang)
						.toFunction());

		// Importiere alle Lehrer-Attribute unter dem Prefix "lehrer"
		reg.importiereRegistryEintraege(ReportingTypesUtils.methodeToString(ReportingStundenplanungLehrerStundenplan::lehrer) + ".",
				ReportingLehrer.SORTIERUNG.registry(), ReportingStundenplanungLehrerStundenplan::lehrer);

		return reg;
	}
}
