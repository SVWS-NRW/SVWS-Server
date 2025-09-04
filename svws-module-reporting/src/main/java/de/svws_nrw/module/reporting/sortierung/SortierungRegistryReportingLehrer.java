package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Registry zur Definition erlaubter Sortierattribute für {@link ReportingLehrer}
 * sowie Hilfsmethoden zum Erzeugen passender Comparatoren.
 */
public final class SortierungRegistryReportingLehrer {

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
		final SortierungRegistry<ReportingLehrer> reg = new SortierungRegistry<>();
		final ArrayList<String> standardSort = new ArrayList<>();
		standardSort.add(reg.methodeToString(ReportingLehrer::nachname));
		standardSort.add(reg.methodeToString(ReportingLehrer::vorname));
		standardSort.add(reg.methodeToString(ReportingLehrer::vornamen));
		standardSort.add(reg.methodeToString(ReportingLehrer::geburtsdatum));
		standardSort.add(reg.methodeToString(ReportingLehrer::id));
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
		reg.registiereString(reg.methodeToString(ReportingLehrer::nachname), ReportingLehrer::nachname);
		reg.registiereString(reg.methodeToString(ReportingLehrer::vorname), ReportingLehrer::vorname);
		reg.registiereString(reg.methodeToString(ReportingLehrer::vornamen), ReportingLehrer::vornamen);
		reg.registiereString(reg.methodeToString(ReportingLehrer::anrede), ReportingLehrer::anrede);
		reg.registiereString(reg.methodeToString(ReportingLehrer::titel), ReportingLehrer::titel);
		reg.registiereString(reg.methodeToString(ReportingLehrer::geburtsdatum), ReportingLehrer::geburtsdatum);
		reg.registiereString(reg.methodeToString(ReportingLehrer::geburtsname), ReportingLehrer::geburtsname);
		reg.registiereString(reg.methodeToString(ReportingLehrer::geburtsort), ReportingLehrer::geburtsort);
		reg.registiereComparable(reg.methodeToString(ReportingLehrer::geschlecht), ReportingLehrer::geschlecht);
		reg.registiereString(reg.methodeToString(ReportingLehrer::emailPrivat), ReportingLehrer::emailPrivat);
		reg.registiereString(reg.methodeToString(ReportingLehrer::emailSchule), ReportingLehrer::emailSchule);
		reg.registiereString(reg.methodeToString(ReportingLehrer::telefonPrivat), ReportingLehrer::telefonPrivat);
		reg.registiereString(reg.methodeToString(ReportingLehrer::telefonPrivatMobil), ReportingLehrer::telefonPrivatMobil);
		reg.registiereString(reg.methodeToString(ReportingLehrer::strassenname), ReportingLehrer::strassenname);
		reg.registiereString(reg.methodeToString(ReportingLehrer::hausnummer), ReportingLehrer::hausnummer);
		reg.registiereString(reg.methodeToString(ReportingLehrer::hausnummerZusatz), ReportingLehrer::hausnummerZusatz);
		reg.registiereString(reg.methodeToString(ReportingLehrer::wohnortname), ReportingLehrer::wohnortname);
		reg.registiereString(reg.methodeToString(ReportingLehrer::wohnortsteilname), ReportingLehrer::wohnortsteilname);

		// Lehrerspezifische Attribute
		reg.registiereComparable(reg.methodeToString(ReportingLehrer::id), ReportingLehrer::id);
		reg.registiereString(reg.methodeToString(ReportingLehrer::kuerzel), ReportingLehrer::kuerzel);
		reg.registiereString(reg.methodeToString(ReportingLehrer::amtsbezeichnung), ReportingLehrer::amtsbezeichnung);
		reg.registiereComparable(reg.methodeToString(ReportingLehrer::personalTyp), ReportingLehrer::personalTyp);

		return reg;
	}
}
