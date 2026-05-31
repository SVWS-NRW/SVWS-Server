package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.module.reporting.sortierung.FunktionBuilder;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingGostKlausurplanungKursklausur}: hält die Sortierkonfiguration des Reporting-Typs (Registry + Standardsortierung).
 * Die fertige {@link ReportingSortierung}-Instanz wird über {@link ReportingGostKlausurplanungKursklausur#SORTIERUNG} verwendet.
 */
public final class ReportingGostKlausurplanungKursklausurSortierung {

	/** Die Sortierkonfiguration für {@link ReportingGostKlausurplanungKursklausur}. */
	public static final ReportingSortierung<ReportingGostKlausurplanungKursklausur> SORTIERUNG =
			ReportingSortierung.<ReportingGostKlausurplanungKursklausur>builder()
					.registry(buildRegistry())
					.standard(buildStandard())
					.build();

	private ReportingGostKlausurplanungKursklausurSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingGostKlausurplanungKursklausur. Initialisierung nicht möglich.");
	}

	private static List<String> buildStandard() {
		final ArrayList<String> standard = new ArrayList<>();
		ReportingGostKlausurplanungKlausurtermin.SORTIERUNG.standardsortierung()
				.forEach(attribut -> standard.add(ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungKursklausur::klausurtermin) + "." + attribut));
		ReportingKurs.SORTIERUNG.standardsortierung()
				.forEach(attribut -> standard.add(ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungKursklausur::kurs) + "." + attribut));
		return standard;
	}

	private static SortierungRegistry<ReportingGostKlausurplanungKursklausur> buildRegistry() {
		final SortierungRegistry<ReportingGostKlausurplanungKursklausur> reg = new SortierungRegistry<>();

		// Eigene Attribute der Kursklausur
		reg.registiereComparable(ReportingGostKlausurplanungKursklausur::auswahlzeit);
		reg.registiereString(ReportingGostKlausurplanungKursklausur::bemerkung);
		reg.registiereComparable(ReportingGostKlausurplanungKursklausur::dauer);
		reg.registiereComparable(ReportingGostKlausurplanungKursklausur::id);
		reg.registiereComparable(ReportingGostKlausurplanungKursklausur::istAudioNotwendig);
		reg.registiereComparable(ReportingGostKlausurplanungKursklausur::istMdlPruefung);
		reg.registiereComparable(ReportingGostKlausurplanungKursklausur::istVideoNotwendig);
		reg.registiereString(ReportingGostKlausurplanungKursklausur::startuhrzeit);

		// Importiere Klausurterminsortierungen unter Prefix "klausurtermin"
		reg.importiereRegistryEintraege(
				ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungKursklausur::klausurtermin) + ".",
				ReportingGostKlausurplanungKlausurtermin.SORTIERUNG.registry(),
				FunktionBuilder.start(ReportingGostKlausurplanungKursklausur::klausurtermin).toFunction()
		);

		// Importiere Kurssortierungen unter Prefix "kurs"
		reg.importiereRegistryEintraege(
				ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungKursklausur::kurs) + ".",
				ReportingKurs.SORTIERUNG.registry(),
				FunktionBuilder.start(ReportingGostKlausurplanungKursklausur::kurs).toFunction()
		);

		return reg;
	}
}
