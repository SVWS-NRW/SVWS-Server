package de.svws_nrw.module.reporting.types.lerngruppen;

import java.util.List;

import de.svws_nrw.module.reporting.sortierung.FunktionBuilder;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingKlasse}: hält die Sortierkonfiguration des Reporting-Typs (Registry + Standardsortierung).
 * Die fertige {@link ReportingSortierung}-Instanz wird über {@link ReportingKlasse#SORTIERUNG} verwendet.
 */
public final class ReportingKlasseSortierung {

	/** Die Sortierkonfiguration für {@link ReportingKlasse}. */
	public static final ReportingSortierung<ReportingKlasse> SORTIERUNG =
			ReportingSortierung.<ReportingKlasse>builder()
					.registry(buildRegistry())
					.standard(List.of(
							ReportingTypesUtils.methodeToString(ReportingKlasse::sortierungEintrag),
							ReportingTypesUtils.methodeToString(ReportingKlasse::kuerzel),
							ReportingTypesUtils.methodeToString(ReportingKlasse::id)))
					.build();

	private ReportingKlasseSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingKlasse. Initialisierung nicht möglich.");
	}

	private static SortierungRegistry<ReportingKlasse> buildRegistry() {
		final SortierungRegistry<ReportingKlasse> reg = new SortierungRegistry<>();

		// Grundlegende Attribute
		reg.registiereComparable(ReportingKlasse::id);
		reg.registiereComparable(ReportingKlasse::sortierungEintrag);
		reg.registiereString(ReportingKlasse::kuerzel);
		reg.registiereString(ReportingKlasse::beschreibung);
		reg.registiereString(ReportingKlasse::parallelitaet);
		reg.registiereString(ReportingKlasse::auflistungKlassenlehrerkuerzel);
		reg.registiereString(ReportingKlasse::teilstandort);

		// IDs und Kennungen (als Comparable)
		reg.registiereComparable(ReportingKlasse::idKlassenart);
		reg.registiereComparable(ReportingKlasse::idSchulgliederung);
		reg.registiereComparable(ReportingKlasse::idJahrgang);
		reg.registiereComparable(ReportingKlasse::idFachklasse);
		reg.registiereComparable(ReportingKlasse::idFolgeklasse);
		reg.registiereComparable(ReportingKlasse::idVorgaengerklasse);
		reg.registiereString(ReportingKlasse::kuerzelFolgeklasse);
		reg.registiereString(ReportingKlasse::kuerzelVorgaengerklasse);

		// Verschachtelte Attribute
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingKlasse::jahrgang) + "." + ReportingTypesUtils.methodeToString(ReportingJahrgang::kuerzel),
				FunktionBuilder.start(ReportingKlasse::jahrgang)
						.then(ReportingJahrgang::kuerzel)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingKlasse::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz),
				FunktionBuilder.start(ReportingKlasse::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingKlasse::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang),
				FunktionBuilder.start(ReportingKlasse::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang)
						.toFunction());

		return reg;
	}
}
