package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import java.util.List;

import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingSchuelerLeistungsdaten}: hält die Sortierkonfiguration des Reporting-Typs (Registry + Standardsortierung).
 * Die fertige {@link ReportingSortierung}-Instanz wird über {@link ReportingSchuelerLeistungsdaten#SORTIERUNG} verwendet.
 */
public final class ReportingSchuelerLeistungsdatenSortierung {

	/** Die Sortierkonfiguration für {@link ReportingSchuelerLeistungsdaten}. */
	public static final ReportingSortierung<ReportingSchuelerLeistungsdaten> SORTIERUNG =
			ReportingSortierung.<ReportingSchuelerLeistungsdaten>builder()
					.registry(buildRegistry())
					.standard(List.of(
							ReportingTypesUtils.methodeToString(ReportingSchuelerLeistungsdaten::fach) + "."
									+ ReportingTypesUtils.methodeToString(ReportingFach::sortierungEintrag),
							ReportingTypesUtils.methodeToString(ReportingSchuelerLeistungsdaten::id)))
					.build();

	private ReportingSchuelerLeistungsdatenSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingSchuelerLeistungsdaten. Initialisierung nicht möglich.");
	}

	private static SortierungRegistry<ReportingSchuelerLeistungsdaten> buildRegistry() {
		final SortierungRegistry<ReportingSchuelerLeistungsdaten> reg = new SortierungRegistry<>();

		// Grundlegende Attribute
		reg.registiereComparable(ReportingSchuelerLeistungsdaten::id);
		reg.registiereComparable(ReportingSchuelerLeistungsdaten::abifach);
		reg.registiereComparable(ReportingSchuelerLeistungsdaten::aufZeugnis);
		reg.registiereComparable(ReportingSchuelerLeistungsdaten::fehlstundenGesamt);
		reg.registiereComparable(ReportingSchuelerLeistungsdaten::fehlstundenUnentschuldigt);
		reg.registiereString(ReportingSchuelerLeistungsdaten::geholtJahrgangAbgeschlossen);
		reg.registiereComparable(ReportingSchuelerLeistungsdaten::gewichtungAllgemeinbildend);
		reg.registiereComparable(ReportingSchuelerLeistungsdaten::istEpochal);
		reg.registiereComparable(ReportingSchuelerLeistungsdaten::istGemahnt);
		reg.registiereComparable(ReportingSchuelerLeistungsdaten::istZP10oderZKEF);
		reg.registiereString(ReportingSchuelerLeistungsdaten::kursart);
		reg.registiereString(ReportingSchuelerLeistungsdaten::mahndatum);
		reg.registiereComparable(ReportingSchuelerLeistungsdaten::schulnummerExtern);
		reg.registiereString(ReportingSchuelerLeistungsdaten::textFachbezogeneLernentwicklung);
		reg.registiereString(ReportingSchuelerLeistungsdaten::umfangLernstandsbericht);
		reg.registiereComparable(ReportingSchuelerLeistungsdaten::wochenstundenSchueler);

		// Verschachtelte Attribute (Fach)
		reg.importiereRegistryEintraege(ReportingTypesUtils.methodeToString(ReportingSchuelerLeistungsdaten::fach) + ".",
				ReportingFach.SORTIERUNG.registry(), ReportingSchuelerLeistungsdaten::fach);
		// Verschachtelte Attribute (Fachlehrer)
		reg.importiereRegistryEintraege(ReportingTypesUtils.methodeToString(ReportingSchuelerLeistungsdaten::fachlehrer) + ".",
				ReportingLehrer.SORTIERUNG.registry(), ReportingSchuelerLeistungsdaten::fachlehrer);

		return reg;
	}
}
