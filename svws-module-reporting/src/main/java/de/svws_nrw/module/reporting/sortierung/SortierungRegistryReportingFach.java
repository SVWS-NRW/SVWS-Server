package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Registry zur Definition erlaubter Sortierattribute für {@link ReportingFach}
 * sowie Hilfsmethoden zum Erzeugen passender Comparatoren.
 */
public final class SortierungRegistryReportingFach {

	/**
	 * Erstellt einen {@link Comparator} für die Klasse {@link ReportingFach} basierend
	 * auf den angegebenen Attributen.
	 *
	 * @param attribute Eine Liste von Attributnamen, die die Sortierreihenfolge definieren.
	 * @param validierungsfehler Eine Liste von Validierungsfehlern, die während der Verarbeitung eventuell auftreten.
	 * @return Ein {@link Comparator} für die Klasse {@link ReportingFach}.
	 */
	public static Comparator<ReportingFach> buildComparator(final List<String> attribute, final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistry(), attribute, validierungsfehler);
	}

	/**
	 * Erstellt einen {@link Comparator} für die Klasse {@link ReportingFach} basierend
	 * auf der Standardsortierung.
	 *
	 * @param validierungsfehler Eine Liste von Validierungsfehlern.
	 * @return Ein {@link Comparator} für die Klasse {@link ReportingFach}.
	 */
	public static Comparator<ReportingFach> buildComparatorStandard(final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistry(), standardsortierung(), validierungsfehler);
	}

	/**
	 * Erstellt eine Liste von Strings, die die Attribute der Standardsortierung repräsentieren.
	 *
	 * @return Eine Liste von Attributnamen in der Reihenfolge der Standardsortierung.
	 */
	public static List<String> standardsortierung() {
		final SortierungRegistry<ReportingFach> reg = new SortierungRegistry<>();
		final ArrayList<String> standardSort = new ArrayList<>();
		standardSort.add(reg.methodeToString(ReportingFach::sortierung));
		standardSort.add(reg.methodeToString(ReportingFach::kuerzel));
		standardSort.add(reg.methodeToString(ReportingFach::id));
		return standardSort;
	}

	/**
	 * Erstellt eine Liste von Strings, die die Attribute der GOSt-Standardsortierung repräsentieren.
	 *
	 * @return Eine Liste von Attributnamen in der Reihenfolge der GOSt-Standardsortierung.
	 */
	public static List<String> standardsortierungGost() {
		final SortierungRegistry<ReportingFach> reg = new SortierungRegistry<>();
		final ArrayList<String> standardSort = new ArrayList<>();
		standardSort.add("gostSortierung");
		standardSort.add(reg.methodeToString(ReportingFach::sortierung));
		standardSort.add(reg.methodeToString(ReportingFach::id));
		return standardSort;
	}

	/**
	 * Stellt die {@link SortierungRegistry} für die Klasse {@link ReportingFach} öffentlich zur Verfügung.
	 *
	 * @return Die konfigurierte Instanz von {@link SortierungRegistry} für {@link ReportingFach}.
	 */
	public static SortierungRegistry<ReportingFach> sortierungRegistry() {
		return sortierungRegistryReportingFach();
	}

	/**
	 * Erstellt und konfiguriert ein {@link SortierungRegistry} für die Klasse {@link ReportingFach}.
	 *
	 * @return Die konfigurierte Instanz von {@link SortierungRegistry} für {@link ReportingFach}.
	 */
	private static SortierungRegistry<ReportingFach> sortierungRegistryReportingFach() {
		final SortierungRegistry<ReportingFach> reg = new SortierungRegistry<>();

		// Grundlegende Attribute
		reg.registiereComparable(reg.methodeToString(ReportingFach::id), ReportingFach::id);
		reg.registiereString(reg.methodeToString(ReportingFach::kuerzel), ReportingFach::kuerzel);
		reg.registiereString(reg.methodeToString(ReportingFach::bezeichnung), ReportingFach::bezeichnung);
		reg.registiereString(reg.methodeToString(ReportingFach::bezeichnungZeugnis), ReportingFach::bezeichnungZeugnis);
		reg.registiereString(reg.methodeToString(ReportingFach::bezeichnungUeberweisungszeugnis), ReportingFach::bezeichnungUeberweisungszeugnis);
		reg.registiereComparable(reg.methodeToString(ReportingFach::sortierung), ReportingFach::sortierung);

		// Fachspezifische Attribute
		reg.registiereString(reg.methodeToString(ReportingFach::aufgabenfeld), ReportingFach::aufgabenfeld);
		reg.registiereString(reg.methodeToString(ReportingFach::bilingualeSprache), ReportingFach::bilingualeSprache);
		reg.registiereComparable(reg.methodeToString(ReportingFach::aufZeugnis), ReportingFach::aufZeugnis);
		reg.registiereComparable(reg.methodeToString(ReportingFach::fachgruppe), ReportingFach::fachgruppe);
		reg.registiereComparable(reg.methodeToString(ReportingFach::istPruefungsordnungsRelevant), ReportingFach::istPruefungsordnungsRelevant);
		reg.registiereComparable(reg.methodeToString(ReportingFach::istGostFach), ReportingFach::istGostFach);
		reg.registiereComparable(reg.methodeToString(ReportingFach::istSichtbar), ReportingFach::istSichtbar);

		// Spezielle Sortierung für die gymnasiale Oberstufe.
		reg.registiereComparable("gostSortierung", fach -> {
			if (fach.statistikfach() == null)
				return Integer.MAX_VALUE;
			return GostFachbereich.getSortierung(Fach.getBySchluesselOrDefault(fach.statistikfach().kuerzelASD()));
		});

		return reg;
	}
}
