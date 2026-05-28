package de.svws_nrw.module.reporting.types.lerngruppen;

import java.util.List;

import de.svws_nrw.module.reporting.sortierung.FunktionBuilder;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingKlassenunterricht}: hält die Sortierkonfiguration des Reporting-Typs (Registry + Standardsortierung).
 * Die fertige {@link ReportingSortierung}-Instanz wird über {@link ReportingKlassenunterricht#SORTIERUNG} verwendet.
 */
public final class ReportingKlassenunterrichtSortierung {

	/** Die Sortierkonfiguration für {@link ReportingKlassenunterricht}. */
	public static final ReportingSortierung<ReportingKlassenunterricht> SORTIERUNG =
			ReportingSortierung.<ReportingKlassenunterricht>builder()
					.registry(buildRegistry())
					.standard(List.of(
							ReportingTypesUtils.methodeToString(ReportingKlassenunterricht::klasse) + "."
									+ ReportingTypesUtils.methodeToString(ReportingKlasse::sortierungEintrag),
							ReportingTypesUtils.methodeToString(ReportingKlassenunterricht::klasse) + "."
									+ ReportingTypesUtils.methodeToString(ReportingKlasse::kuerzel),
							ReportingTypesUtils.methodeToString(ReportingKlassenunterricht::fach) + "."
									+ ReportingTypesUtils.methodeToString(ReportingFach::sortierungEintrag),
							ReportingTypesUtils.methodeToString(ReportingKlassenunterricht::fach) + "."
									+ ReportingTypesUtils.methodeToString(ReportingFach::kuerzel),
							ReportingTypesUtils.methodeToString(ReportingKlassenunterricht::id)))
					.build();

	private ReportingKlassenunterrichtSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingKlassenunterricht. Initialisierung nicht möglich.");
	}

	private static SortierungRegistry<ReportingKlassenunterricht> buildRegistry() {
		final SortierungRegistry<ReportingKlassenunterricht> reg = new SortierungRegistry<>();

		// Grundlegende Attribute des Klassenunterrichts
		reg.registiereComparable(ReportingKlassenunterricht::id);
		reg.registiereString(ReportingKlassenunterricht::kuerzel);
		reg.registiereComparable(ReportingKlassenunterricht::sortierungEintrag);
		reg.registiereComparable(ReportingKlassenunterricht::wochenstundenSchueler);

		// Verschachtelte Attribute: Fach
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingKlassenunterricht::fach) + "."
						+ ReportingTypesUtils.methodeToString(ReportingFach::kuerzel),
				FunktionBuilder.start(ReportingKlassenunterricht::fach)
						.then(ReportingFach::kuerzel)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingKlassenunterricht::fach) + "."
						+ ReportingTypesUtils.methodeToString(ReportingFach::bezeichnung),
				FunktionBuilder.start(ReportingKlassenunterricht::fach)
						.then(ReportingFach::bezeichnung)
						.toFunction());
		reg.registiereComparable(
				ReportingTypesUtils.methodeToString(ReportingKlassenunterricht::fach) + "."
						+ ReportingTypesUtils.methodeToString(ReportingFach::sortierungEintrag),
				FunktionBuilder.start(ReportingKlassenunterricht::fach)
						.then(ReportingFach::sortierungEintrag)
						.toFunction());

		// Verschachtelte Attribute: Bewertender Lehrer
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingKlassenunterricht::bewertenderLehrer) + "."
						+ ReportingTypesUtils.methodeToString(ReportingLehrer::nachname),
				FunktionBuilder.start(ReportingKlassenunterricht::bewertenderLehrer)
						.then(ReportingLehrer::nachname)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingKlassenunterricht::bewertenderLehrer) + "."
						+ ReportingTypesUtils.methodeToString(ReportingLehrer::vorname),
				FunktionBuilder.start(ReportingKlassenunterricht::bewertenderLehrer)
						.then(ReportingLehrer::vorname)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingKlassenunterricht::bewertenderLehrer) + "."
						+ ReportingTypesUtils.methodeToString(ReportingLehrer::kuerzel),
				FunktionBuilder.start(ReportingKlassenunterricht::bewertenderLehrer)
						.then(ReportingLehrer::kuerzel)
						.toFunction());

		// Verschachteltes Attribut: Jahrgang
		reg.registiereString(
				"jahrgang." + ReportingTypesUtils.methodeToString(ReportingJahrgang::kuerzel),
				ku -> (ku.jahrgaenge().isEmpty() || (ku.jahrgaenge().getFirst() == null)) ? null : ku.jahrgaenge().getFirst().kuerzel());

		// Importiere alle Klasse-Attribute unter dem Prefix "klasse."
		reg.importiereRegistryEintraege(
				ReportingTypesUtils.methodeToString(ReportingKlassenunterricht::klasse) + ".",
				ReportingKlasse.SORTIERUNG.registry(),
				ReportingKlassenunterricht::klasse);

		return reg;
	}
}
