package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungSchuelerklausur;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SortierungRegistryReportingGostKlausurplanungKlausurtermin {

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
		final SortierungRegistry<ReportingGostKlausurplanungKlausurtermin> reg = new SortierungRegistry<>();
		final ArrayList<String> standard = new ArrayList<>();
		standard.add(reg.methodeToString(ReportingGostKlausurplanungKlausurtermin::datum));
		standard.add(reg.methodeToString(ReportingGostKlausurplanungKlausurtermin::startuhrzeit));
		standard.add(reg.methodeToString(ReportingGostKlausurplanungKlausurtermin::bezeichnung));
		standard.add(reg.methodeToString(ReportingGostKlausurplanungKlausurtermin::id));
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
		reg.registiereString(reg.methodeToString(ReportingGostKlausurplanungKlausurtermin::bemerkung),
				ReportingGostKlausurplanungKlausurtermin::bemerkung);
		reg.registiereString(reg.methodeToString(ReportingGostKlausurplanungKlausurtermin::bezeichnung),
				ReportingGostKlausurplanungKlausurtermin::bezeichnung);
		reg.registiereString(reg.methodeToString(ReportingGostKlausurplanungKlausurtermin::datum),
				ReportingGostKlausurplanungKlausurtermin::datum);
		reg.registiereComparable(reg.methodeToString(ReportingGostKlausurplanungKlausurtermin::gostHalbjahr),
				ReportingGostKlausurplanungKlausurtermin::gostHalbjahr);
		reg.registiereComparable(reg.methodeToString(ReportingGostKlausurplanungKlausurtermin::id),
				ReportingGostKlausurplanungKlausurtermin::id);
		reg.registiereComparable(reg.methodeToString(ReportingGostKlausurplanungKlausurtermin::istHaupttermin),
				ReportingGostKlausurplanungKlausurtermin::istHaupttermin);
		reg.registiereComparable(reg.methodeToString(ReportingGostKlausurplanungKlausurtermin::nachschreiberZugelassen),
				ReportingGostKlausurplanungKlausurtermin::nachschreiberZugelassen);
		reg.registiereComparable(reg.methodeToString(ReportingGostKlausurplanungKlausurtermin::quartal),
				ReportingGostKlausurplanungKlausurtermin::quartal);
		reg.registiereString(reg.methodeToString(ReportingGostKlausurplanungKlausurtermin::startuhrzeit),
				ReportingGostKlausurplanungKlausurtermin::startuhrzeit);

		// Nützliche aggregierte Darstellung der Räume und Stunden
		reg.registiereString(reg.methodeToString(ReportingGostKlausurplanungKlausurtermin::raeumeUndStunden),
				ReportingGostKlausurplanungKlausurtermin::raeumeUndStunden);

		return reg;
	}
}
