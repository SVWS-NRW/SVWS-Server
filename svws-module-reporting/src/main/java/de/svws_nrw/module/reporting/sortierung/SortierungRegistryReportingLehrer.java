package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Registry zur Definition erlaubter Sortierattribute für {@link ReportingLehrer}
 * sowie Hilfsmethoden zum Erzeugen passender Comparatoren.
 */
public final class SortierungRegistryReportingLehrer {

	private SortierungRegistryReportingLehrer() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zur Sortierung von Daten für das Reporting. Initialisierung nicht möglich.");
	}

	/**
	 * Erstellt einen {@link Comparator} für die Klasse {@link ReportingLehrer} basierend
	 * auf den angegebenen Attributen. Dafür wird eine Liste von Attributnamen verwendet, die
	 * die Sortierreihenfolge beschreiben. Die Liste für Validierungsfehler kann während
	 * der Erstellung des Comparators gefüllt werden.
	 *
	 * @param attribute Eine Liste von Attributnamen, die die Sortierreihenfolge definieren.
	 * @param validierungsfehler Eine Liste von Validierungsfehlern, die während der Verarbeitung eventuell auftreten und zurückgegeben werden können. Kann
	 *                           null sein, dann werden keine Fehler protokolliert.
	 * @return Ein {@link Comparator} für die Klasse {@link ReportingLehrer}, basierend auf
	 *         den angegebenen Attributen.
	 */
	public static Comparator<ReportingLehrer> buildComparator(final List<String> attribute, final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistryReportingLehrer(), attribute, validierungsfehler);
	}

	/**
	 * Erstellt einen {@link Comparator} für die Klasse {@link ReportingLehrer} basierend
	 * auf den angegebenen Attributen der Standardsortierung. Die Liste für Validierungsfehler kann während der Erstellung des Comparators gefüllt werden.
	 *
	 * @param validierungsfehler Eine Liste von Validierungsfehlern, die während der Verarbeitung eventuell auftreten und zurückgegeben werden können. Kann
	 *                           null sein, dann werden keine Fehler protokolliert.
	 * @return Ein {@link Comparator} für die Klasse {@link ReportingLehrer}, basierend auf
	 *         den angegebenen Attributen.
	 */
	public static Comparator<ReportingLehrer> buildComparatorStandard(final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistryReportingLehrer(), standardsortierung(), validierungsfehler);
	}

	/**
	 * Erstellt eine Liste von Strings, die die Attribute der Standardsortierung.
	 * für {@link ReportingLehrer} repräsentieren.
	 *

	 * @return Eine Liste von Attributnamen in der Reihenfolge der Standardsortierung.
	 */
	public static List<String> standardsortierung() {
		final ArrayList<String> standardSort = new ArrayList<>();
		standardSort.add(ReportingTypesUtils.methodeToString(ReportingLehrer::nachname));
		standardSort.add(ReportingTypesUtils.methodeToString(ReportingLehrer::vorname));
		standardSort.add(ReportingTypesUtils.methodeToString(ReportingLehrer::vornamen));
		standardSort.add(ReportingTypesUtils.methodeToString(ReportingLehrer::geburtsdatum));
		standardSort.add(ReportingTypesUtils.methodeToString(ReportingLehrer::id));
		return standardSort;
	}

	/**
	 * Stellt die {@link SortierungRegistry} für die Klasse {@link ReportingLehrer} öffentlich zur Verfügung
	 *
	 * @return Die konfigurierte Instanz von {@link SortierungRegistry} für {@link ReportingLehrer}.
	 */
	public static SortierungRegistry<ReportingLehrer> sortierungRegistry() {
		return sortierungRegistryReportingLehrer();
	}

	/**
	 * Erstellt und konfiguriert ein {@link SortierungRegistry} für die Klasse {@link ReportingLehrer}.
	 *
	 * @return Die konfigurierte Instanz von {@link SortierungRegistry} für {@link ReportingLehrer}.
	 */
	private static SortierungRegistry<ReportingLehrer> sortierungRegistryReportingLehrer() {
		final SortierungRegistry<ReportingLehrer> reg = new SortierungRegistry<>();

		// Personenattribute (von ReportingPerson geerbt)
		reg.registiereString(ReportingLehrer::nachname);
		reg.registiereString(ReportingLehrer::vorname);
		reg.registiereString(ReportingLehrer::vornamen);
		reg.registiereString(ReportingLehrer::anrede);
		reg.registiereString(ReportingLehrer::titel);
		reg.registiereString(ReportingLehrer::geburtsdatum);
		reg.registiereString(ReportingLehrer::geburtsname);
		reg.registiereString(ReportingLehrer::geburtsort);
		reg.registiereComparable(ReportingLehrer::geschlecht);
		reg.registiereString(ReportingLehrer::emailPrivat);
		reg.registiereString(ReportingLehrer::emailSchule);
		reg.registiereString(ReportingLehrer::telefonPrivat);
		reg.registiereString(ReportingLehrer::telefonPrivatMobil);
		reg.registiereString(ReportingLehrer::strassenname);
		reg.registiereString(ReportingLehrer::hausnummer);
		reg.registiereString(ReportingLehrer::hausnummerZusatz);
		reg.registiereString(ReportingLehrer::wohnortname);
		reg.registiereString(ReportingLehrer::wohnortsteilname);

		// Lehrerspezifische Attribute
		reg.registiereComparable(ReportingLehrer::id);
		reg.registiereString(ReportingLehrer::kuerzel);
		reg.registiereString(ReportingLehrer::amtsbezeichnung);
		reg.registiereComparable(ReportingLehrer::personalTyp);

		return reg;
	}
}
