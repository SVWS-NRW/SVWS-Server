package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLeistungsdaten;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Registry zur Definition erlaubter Sortierattribute für {@link ReportingSchuelerLeistungsdaten}
 * sowie Hilfsmethoden zum Erzeugen passender Comparatoren.
 */
public final class SortierungRegistryReportingSchuelerLeistungsdaten {

	private SortierungRegistryReportingSchuelerLeistungsdaten() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zur Sortierung von Daten für das Reporting. Initialisierung nicht möglich.");
	}

	/**
	 * Erstellt einen {@link Comparator} für die Klasse {@link ReportingSchuelerLeistungsdaten} basierend
	 * auf den angegebenen Attributen. Dafür wird eine Liste von Attributnamen verwendet, die
	 * die Sortierreihenfolge beschreiben. Die Liste für Validierungsfehler kann während
	 * der Erstellung des Comparators gefüllt werden.
	 *
	 * @param attribute Eine Liste von Attributnamen, die die Sortierreihenfolge definieren.
	 * @param validierungsfehler Eine Liste von Validierungsfehlern, die während der Verarbeitung eventuell auftreten und zurückgegeben werden können. Kann
	 *                           null sein, dann werden keine Fehler protokolliert.
	 * @return Ein {@link Comparator} für die Klasse {@link ReportingSchuelerLeistungsdaten}, basierend auf
	 *         den angegebenen Attributen.
	 */
	public static Comparator<ReportingSchuelerLeistungsdaten> buildComparator(final List<String> attribute, final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistry(), attribute, validierungsfehler);
	}

	/**
	 * Erstellt einen {@link Comparator} für die Klasse {@link ReportingSchuelerLeistungsdaten} basierend
	 * auf den angegebenen Attributen der Standardsortierung. Die Liste für Validierungsfehler kann während der Erstellung des Comparators gefüllt werden.
	 *
	 * @param validierungsfehler Eine Liste von Validierungsfehlern, die während der Verarbeitung eventuell auftreten und zurückgegeben werden können. Kann
	 *                           null sein, dann werden keine Fehler protokolliert.
	 * @return Ein {@link Comparator} für die Klasse {@link ReportingSchuelerLeistungsdaten}, basierend auf
	 *         den angegebenen Attributen.
	 */
	public static Comparator<ReportingSchuelerLeistungsdaten> buildComparatorStandard(final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistry(), standardsortierung(), validierungsfehler);
	}

	/**
	 * Erstellt eine Liste von Strings, die die Attribute der Standardsortierung.
	 * für {@link ReportingSchuelerLeistungsdaten} repräsentieren.
	 * @return Eine Liste von Attributnamen in der Reihenfolge der Standardsortierung.
	 */
	public static List<String> standardsortierung() {
		final ArrayList<String> standardSort = new ArrayList<>();
		standardSort.add(ReportingTypesUtils.methodeToString(ReportingSchuelerLeistungsdaten::fach) + "."
				+ ReportingTypesUtils.methodeToString(de.svws_nrw.module.reporting.types.fach.ReportingFach::sortierung));
		standardSort.add(ReportingTypesUtils.methodeToString(ReportingSchuelerLeistungsdaten::id));
		return standardSort;
	}

	/**
	 * Stellt die {@link SortierungRegistry} für die Klasse {@link ReportingSchuelerLeistungsdaten} öffentlich zur Verfügung
	 *
	 * @return Die konfigurierte Instanz von {@link SortierungRegistry} für {@link ReportingSchuelerLeistungsdaten}.
	 */
	public static SortierungRegistry<ReportingSchuelerLeistungsdaten> sortierungRegistry() {
		return sortierungRegistryReportingSchuelerLeistungsdaten();
	}

	/**
	 * Erstellt und konfiguriert ein {@link SortierungRegistry} für die Klasse {@link ReportingSchuelerLeistungsdaten}.
	 *
	 * @return Die konfigurierte Instanz von {@link SortierungRegistry} für {@link ReportingSchuelerLeistungsdaten}.
	 */
	private static SortierungRegistry<ReportingSchuelerLeistungsdaten> sortierungRegistryReportingSchuelerLeistungsdaten() {
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
				SortierungRegistryReportingFach.sortierungRegistry(), ReportingSchuelerLeistungsdaten::fach);

		return reg;
	}
}
