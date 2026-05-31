package de.svws_nrw.module.reporting.types.lerngruppen;

import java.util.List;

import de.svws_nrw.module.reporting.sortierung.FunktionBuilder;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingKurs}: hält die Sortierkonfiguration des Reporting-Typs (Registry + Standardsortierung).
 * Die fertige {@link ReportingSortierung}-Instanz wird über {@link ReportingKurs#SORTIERUNG} verwendet.
 */
public final class ReportingKursSortierung {

	/** Die Sortierkonfiguration für {@link ReportingKurs}. */
	public static final ReportingSortierung<ReportingKurs> SORTIERUNG =
			ReportingSortierung.<ReportingKurs>builder()
					.registry(buildRegistry())
					.standard(List.of(
							"jahrgang." + ReportingTypesUtils.methodeToString(ReportingJahrgang::sortierungEintrag),
							ReportingTypesUtils.methodeToString(ReportingKurs::fach) + "."
									+ ReportingTypesUtils.methodeToString(ReportingFach::sortierungEintrag),
							ReportingTypesUtils.methodeToString(ReportingKurs::kursartAllg),
							ReportingTypesUtils.methodeToString(ReportingKurs::kuerzel),
							ReportingTypesUtils.methodeToString(ReportingKurs::id)))
					.build();

	private ReportingKursSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingKurs. Initialisierung nicht möglich.");
	}

	private static SortierungRegistry<ReportingKurs> buildRegistry() {
		final SortierungRegistry<ReportingKurs> reg = new SortierungRegistry<>();

		// Grundlegende Attribute
		reg.registiereComparable(ReportingKurs::id);
		reg.registiereString(ReportingKurs::kuerzel);
		reg.registiereString(ReportingKurs::bezeichnungZeugnis);
		reg.registiereString(ReportingKurs::kursartAllg);
		reg.registiereComparable(ReportingKurs::sortierungEintrag);
		reg.registiereComparable(ReportingKurs::istEpochalunterricht);
		reg.registiereComparable(ReportingKurs::istSichtbar);
		reg.registiereComparable(ReportingKurs::wochenstunden);
		reg.registiereComparable(ReportingKurs::schulnummer);

		// Verschachtelte Attribute: Fach, Jahrgang, Kursleitung, Schuljahresabschnitt
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingKurs::fach) + "." + ReportingTypesUtils.methodeToString(ReportingFach::bezeichnung),
				FunktionBuilder.start(ReportingKurs::fach)
						.then(ReportingFach::bezeichnung)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingKurs::fach) + "." + ReportingTypesUtils.methodeToString(ReportingFach::kuerzel),
				FunktionBuilder.start(ReportingKurs::fach)
						.then(ReportingFach::kuerzel)
						.toFunction());
		reg.registiereComparable(
				ReportingTypesUtils.methodeToString(ReportingKurs::fach) + "." + ReportingTypesUtils.methodeToString(ReportingFach::sortierungEintrag),
				FunktionBuilder.start(ReportingKurs::fach)
						.then(ReportingFach::sortierungEintrag)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingKurs::kursleitung) + "." + ReportingTypesUtils.methodeToString(ReportingLehrer::nachname),
				FunktionBuilder.start(ReportingKurs::kursleitung)
						.then(ReportingLehrer::nachname)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingKurs::kursleitung) + "." + ReportingTypesUtils.methodeToString(ReportingLehrer::vorname),
				FunktionBuilder.start(ReportingKurs::kursleitung)
						.then(ReportingLehrer::vorname)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingKurs::kursleitung) + "." + ReportingTypesUtils.methodeToString(ReportingLehrer::kuerzel),
				FunktionBuilder.start(ReportingKurs::kursleitung)
						.then(ReportingLehrer::kuerzel)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingKurs::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz),
				FunktionBuilder.start(ReportingKurs::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingKurs::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang),
				FunktionBuilder.start(ReportingKurs::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang)
						.toFunction());

		// Verschachtelte Attribute: Erster Jahrgang aus der Liste der Jahrgänge
		reg.importiereRegistryEintraege("jahrgang.", ReportingJahrgang.SORTIERUNG.registry(),
				k -> ((k.jahrgaenge() == null) || k.jahrgaenge().isEmpty()) ? null : k.jahrgaenge().getFirst());

		return reg;
	}
}
