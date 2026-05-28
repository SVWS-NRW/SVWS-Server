package de.svws_nrw.module.reporting.types.lerngruppen;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingKursunterricht}: hält die Sortierkonfiguration des Reporting-Typs (Registry + Standardsortierung).
 * Die fertige {@link ReportingSortierung}-Instanz wird über {@link ReportingKursunterricht#SORTIERUNG} verwendet.
 *
 * <p>Da {@link ReportingKursunterricht} von {@link ReportingKurs} erbt, werden alle Registry-Einträge unverändert aus
 * {@link ReportingKurs#SORTIERUNG} übernommen. Die Standardsortierung wird der Standardsortierung von
 * {@link ReportingKurs} der Jahrgang vorangestellt, damit Kursunterrichte primär nach Jahrgang gruppiert erscheinen.</p>
 */
public final class ReportingKursunterrichtSortierung {

	/** Die Sortierkonfiguration für {@link ReportingKursunterricht}. */
	public static final ReportingSortierung<ReportingKursunterricht> SORTIERUNG =
			ReportingSortierung.<ReportingKursunterricht>builder()
					.registry(buildRegistry())
					.standard(buildStandard())
					.build();

	private ReportingKursunterrichtSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingKursunterricht. Initialisierung nicht möglich.");
	}

	private static SortierungRegistry<ReportingKursunterricht> buildRegistry() {
		final SortierungRegistry<ReportingKursunterricht> reg = new SortierungRegistry<>();

		// Importiere alle Kurs-Attribute ohne Prefix (ReportingKursunterricht erbt von ReportingKurs).
		reg.importiereRegistryEintraege("", ReportingKurs.SORTIERUNG.registry(), ku -> ku);

		return reg;
	}

	private static List<String> buildStandard() {
		final List<String> standard = new ArrayList<>();
		standard.add("jahrgang." + ReportingTypesUtils.methodeToString(ReportingJahrgang::sortierungEintrag));
		standard.addAll(ReportingKurs.SORTIERUNG.standardsortierung());
		return standard;
	}
}
