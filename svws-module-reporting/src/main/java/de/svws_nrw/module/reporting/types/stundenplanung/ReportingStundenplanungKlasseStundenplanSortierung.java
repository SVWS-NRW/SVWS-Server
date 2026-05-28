package de.svws_nrw.module.reporting.types.stundenplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.module.reporting.sortierung.FunktionBuilder;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingStundenplanungKlasseStundenplan}: hält die Sortierkonfiguration des Reporting-Typs (Registry + Standardsortierung).
 * Die fertige {@link ReportingSortierung}-Instanz wird über {@link ReportingStundenplanungKlasseStundenplan#SORTIERUNG} verwendet.
 */
public final class ReportingStundenplanungKlasseStundenplanSortierung {

	/** Die Sortierkonfiguration für {@link ReportingStundenplanungKlasseStundenplan}. */
	public static final ReportingSortierung<ReportingStundenplanungKlasseStundenplan> SORTIERUNG =
			ReportingSortierung.<ReportingStundenplanungKlasseStundenplan>builder()
					.registry(buildRegistry())
					.standard(buildStandard())
					.build();

	private ReportingStundenplanungKlasseStundenplanSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingStundenplanungKlasseStundenplan. Initialisierung nicht möglich.");
	}

	private static List<String> buildStandard() {
		final ArrayList<String> standard = new ArrayList<>();
		ReportingKlasse.SORTIERUNG.standardsortierung()
				.forEach(attribut -> standard.add(ReportingTypesUtils.methodeToString(ReportingStundenplanungKlasseStundenplan::klasse) + "." + attribut));
		return standard;
	}

	private static SortierungRegistry<ReportingStundenplanungKlasseStundenplan> buildRegistry() {
		final SortierungRegistry<ReportingStundenplanungKlasseStundenplan> reg = new SortierungRegistry<>();

		// Sortierattribute für den eingebetteten Stundenplan
		reg.registiereComparable(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungKlasseStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::id),
				FunktionBuilder.start(ReportingStundenplanungKlasseStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::id)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungKlasseStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::beschreibung),
				FunktionBuilder.start(ReportingStundenplanungKlasseStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::beschreibung)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungKlasseStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::gueltigAb),
				FunktionBuilder.start(ReportingStundenplanungKlasseStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigAb)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungKlasseStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::gueltigBis),
				FunktionBuilder.start(ReportingStundenplanungKlasseStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigBis)
						.toFunction());
		reg.registiereComparable(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungKlasseStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::wochenperiodizitaet),
				FunktionBuilder.start(ReportingStundenplanungKlasseStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::wochenperiodizitaet)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungKlasseStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz),
				FunktionBuilder.start(ReportingStundenplanungKlasseStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingStundenplanungKlasseStundenplan::stundenplan) + "."
						+ ReportingTypesUtils.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang),
				FunktionBuilder.start(ReportingStundenplanungKlasseStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang)
						.toFunction());

		// Importiere alle Klasse-Attribute unter dem Prefix "klasse"
		reg.importiereRegistryEintraege(ReportingTypesUtils.methodeToString(ReportingStundenplanungKlasseStundenplan::klasse) + ".",
				ReportingKlasse.SORTIERUNG.registry(), ReportingStundenplanungKlasseStundenplan::klasse);

		return reg;
	}
}
