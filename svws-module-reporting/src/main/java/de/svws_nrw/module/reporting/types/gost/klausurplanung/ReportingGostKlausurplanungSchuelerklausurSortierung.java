package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.module.reporting.sortierung.FunktionBuilder;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingGostKlausurplanungSchuelerklausur}: hält die Sortierkonfiguration des Reporting-Typs (Registry + Standardsortierung).
 * Die fertige {@link ReportingSortierung}-Instanz wird über {@link ReportingGostKlausurplanungSchuelerklausur#SORTIERUNG} verwendet.
 */
public final class ReportingGostKlausurplanungSchuelerklausurSortierung {

	/** Die Sortierkonfiguration für {@link ReportingGostKlausurplanungSchuelerklausur}. */
	public static final ReportingSortierung<ReportingGostKlausurplanungSchuelerklausur> SORTIERUNG =
			ReportingSortierung.<ReportingGostKlausurplanungSchuelerklausur>builder()
					.registry(buildRegistry())
					.standard(buildStandard())
					.build();

	private ReportingGostKlausurplanungSchuelerklausurSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingGostKlausurplanungSchuelerklausur. Initialisierung nicht möglich.");
	}

	private static List<String> buildStandard() {
		final ArrayList<String> standard = new ArrayList<>();
		ReportingSchueler.SORTIERUNG.standardsortierung()
				.forEach(attribut -> standard.add(ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungSchuelerklausur::schueler) + "." + attribut));
		ReportingGostKlausurplanungKlausurtermin.SORTIERUNG.standardsortierung()
				.forEach(attribut -> standard.add(ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungSchuelerklausur::klausurtermin) + "." + attribut));
		return standard;
	}

	private static SortierungRegistry<ReportingGostKlausurplanungSchuelerklausur> buildRegistry() {
		final SortierungRegistry<ReportingGostKlausurplanungSchuelerklausur> reg = new SortierungRegistry<>();

		// Eigene Attribute der Schülerklausur
		reg.registiereString(ReportingGostKlausurplanungSchuelerklausur::bemerkung);
		reg.registiereComparable(ReportingGostKlausurplanungSchuelerklausur::id);
		reg.registiereComparable(ReportingGostKlausurplanungSchuelerklausur::idSchuelerklausurtermin);
		reg.registiereComparable(ReportingGostKlausurplanungSchuelerklausur::nummerTerminfolge);
		reg.registiereString(ReportingGostKlausurplanungSchuelerklausur::startuhrzeit);

		// Importiere Schülersortierungen unter Prefix "schueler"
		reg.importiereRegistryEintraege(
				ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungSchuelerklausur::schueler) + ".",
				ReportingSchueler.SORTIERUNG.registry(),
				FunktionBuilder.start(ReportingGostKlausurplanungSchuelerklausur::schueler).toFunction()
		);

		// Importiere Klausurterminsortierungen unter Prefix "klausurtermin"
		reg.importiereRegistryEintraege(
				ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungSchuelerklausur::klausurtermin) + ".",
				ReportingGostKlausurplanungKlausurtermin.SORTIERUNG.registry(),
				FunktionBuilder.start(ReportingGostKlausurplanungSchuelerklausur::klausurtermin).toFunction()
		);

		return reg;
	}
}
