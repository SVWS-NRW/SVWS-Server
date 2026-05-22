package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungSchuelerklausur;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SortierungRegistryReportingGostKlausurplanungSchuelerklausur {

	private SortierungRegistryReportingGostKlausurplanungSchuelerklausur() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zur Sortierung von Daten für das Reporting. Initialisierung nicht möglich.");
	}

	/**
	 * Erstellt einen {@link Comparator} für die Klasse {@link ReportingGostKlausurplanungSchuelerklausur} basierend
	 * auf den angegebenen Attributen.
	 *
	 * @param attribute Eine Liste von Attributnamen, die die Sortierreihenfolge definieren.
	 * @param validierungsfehler Liste, die unbekannte Attribute während der Validierung aufnimmt (optional).
	 * @return Comparator entsprechend der Attribute für {@link ReportingGostKlausurplanungSchuelerklausur}.
	 */
	public static Comparator<ReportingGostKlausurplanungSchuelerklausur> buildComparator(final List<String> attribute,
			final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistry(), attribute, validierungsfehler);
	}

	/**
	 * Erstellt einen {@link Comparator} für die Klasse {@link ReportingGostKlausurplanungSchuelerklausur} basierend
	 * auf der Standardsortierung.
	 *
	 * @param validierungsfehler Liste, die unbekannte Attribute während der Validierung aufnimmt (optional).
	 * @return Comparator entsprechend der Standardsortierung für {@link ReportingGostKlausurplanungSchuelerklausur}.
	 */
	public static Comparator<ReportingGostKlausurplanungSchuelerklausur> buildComparatorStandard(final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistry(), standardsortierung(), validierungsfehler);
	}

	/**
	 * Erstellt eine Liste von Strings, die die Attribute der Standardsortierung.
	 * für {@link ReportingGostKlausurplanungSchuelerklausur} repräsentieren.
	 *
	 * @return Eine Liste von Attributnamen in der Reihenfolge der Standardsortierung.
	 */
	public static List<String> standardsortierung() {
		final ArrayList<String> standard = new ArrayList<>();
		SortierungRegistryReportingSchueler.standardsortierung()
				.forEach(attribut -> standard.add(ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungSchuelerklausur::schueler) + "." + attribut));
		SortierungRegistryReportingGostKlausurplanungKlausurtermin.standardsortierung()
				.forEach(attribut -> standard.add(ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungSchuelerklausur::klausurtermin) + "." + attribut));
		return standard;
	}

	/**
	 * Stellt die {@link SortierungRegistry} für die Klasse {@link ReportingGostKlausurplanungSchuelerklausur} öffentlich zur Verfügung
	 *
	 * @return Die konfigurierte Instanz von {@link SortierungRegistry} für {@link ReportingGostKlausurplanungSchuelerklausur}.
	 */
	public static SortierungRegistry<ReportingGostKlausurplanungSchuelerklausur> sortierungRegistry() {
		return sortierungRegistryReportingGostKlausurplanungSchuelerklausur();
	}

	private static SortierungRegistry<ReportingGostKlausurplanungSchuelerklausur> sortierungRegistryReportingGostKlausurplanungSchuelerklausur() {

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
				SortierungRegistryReportingSchueler.sortierungRegistry(),
				FunktionBuilder.start(ReportingGostKlausurplanungSchuelerklausur::schueler).toFunction()
		);

		// Importiere Klausurterminsortierungen unter Prefix "klausurtermin"
		reg.importiereRegistryEintraege(
				ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungSchuelerklausur::klausurtermin) + ".",
				SortierungRegistryReportingGostKlausurplanungKlausurtermin.sortierungRegistry(),
				FunktionBuilder.start(ReportingGostKlausurplanungSchuelerklausur::klausurtermin).toFunction()
		);

		return reg;
	}
}
