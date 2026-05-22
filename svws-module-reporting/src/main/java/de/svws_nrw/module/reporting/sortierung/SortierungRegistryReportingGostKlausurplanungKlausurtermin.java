package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungSchuelerklausur;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SortierungRegistryReportingGostKlausurplanungKlausurtermin {

	private SortierungRegistryReportingGostKlausurplanungKlausurtermin() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zur Sortierung von Daten für das Reporting. Initialisierung nicht möglich.");
	}

	/**
	 * Erstellt einen {@link Comparator} für die Klasse {@link ReportingGostKlausurplanungKlausurtermin} basierend
	 * auf den angegebenen Attributen.
	 *
	 * @param attribute Eine Liste von Attributnamen, die die Sortierreihenfolge definieren.
	 * @param validierungsfehler Liste, die unbekannte Attribute während der Validierung aufnimmt (optional).
	 * @return Comparator entsprechend der Attribute für {@link ReportingGostKlausurplanungKlausurtermin}.
	 */
	public static Comparator<ReportingGostKlausurplanungKlausurtermin> buildComparator(final List<String> attribute,
			final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistry(), attribute, validierungsfehler);
	}

	/**
	 * Erstellt einen {@link Comparator} für die Klasse {@link ReportingGostKlausurplanungKlausurtermin} basierend
	 * auf der Standardsortierung.
	 *
	 * @param validierungsfehler Liste, die unbekannte Attribute während der Validierung aufnimmt (optional).
	 * @return Comparator entsprechend der Standardsortierung für {@link ReportingGostKlausurplanungSchuelerklausur}.
	 */
	public static Comparator<ReportingGostKlausurplanungKlausurtermin> buildComparatorStandard(final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistry(), standardsortierung(), validierungsfehler);
	}

	/**
	 * Erstellt eine Liste von Strings, die die Attribute der Standardsortierung.
	 * für {@link ReportingGostKlausurplanungKlausurtermin} repräsentieren.
	 *
	 * @return Eine Liste von Attributnamen in der Reihenfolge der Standardsortierung.
	 */
	public static List<String> standardsortierung() {
		final ArrayList<String> standard = new ArrayList<>();
		standard.add(ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungKlausurtermin::datum));
		standard.add(ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungKlausurtermin::startuhrzeit));
		standard.add(ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungKlausurtermin::bezeichnung));
		standard.add(ReportingTypesUtils.methodeToString(ReportingGostKlausurplanungKlausurtermin::id));
		return standard;
	}

	/**
	 * Stellt die {@link SortierungRegistry} für die Klasse {@link ReportingGostKlausurplanungKlausurtermin} öffentlich zur Verfügung
	 *
	 * @return Die konfigurierte Instanz von {@link SortierungRegistry} für {@link ReportingGostKlausurplanungKlausurtermin}.
	 */
	public static SortierungRegistry<ReportingGostKlausurplanungKlausurtermin> sortierungRegistry() {
		return sortierungRegistryReportingGostKlausurplanungKlausurtermin();
	}

	private static SortierungRegistry<ReportingGostKlausurplanungKlausurtermin> sortierungRegistryReportingGostKlausurplanungKlausurtermin() {

		final SortierungRegistry<ReportingGostKlausurplanungKlausurtermin> reg = new SortierungRegistry<>();

		// Grundlegende Attribute des Klausurtermins
		reg.registiereString(ReportingGostKlausurplanungKlausurtermin::bemerkung);
		reg.registiereString(ReportingGostKlausurplanungKlausurtermin::bezeichnung);
		reg.registiereString(ReportingGostKlausurplanungKlausurtermin::datum);
		reg.registiereComparable(ReportingGostKlausurplanungKlausurtermin::gostHalbjahr);
		reg.registiereComparable(ReportingGostKlausurplanungKlausurtermin::id);
		reg.registiereComparable(ReportingGostKlausurplanungKlausurtermin::istHaupttermin);
		reg.registiereComparable(ReportingGostKlausurplanungKlausurtermin::nachschreiberZugelassen);
		reg.registiereComparable(ReportingGostKlausurplanungKlausurtermin::quartal);
		reg.registiereString(ReportingGostKlausurplanungKlausurtermin::startuhrzeit);

		// Nützliche aggregierte Darstellung der Räume und Stunden
		reg.registiereString(ReportingGostKlausurplanungKlausurtermin::raeumeUndStunden);

		return reg;
	}
}
