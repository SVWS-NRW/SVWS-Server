package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Registry zur Definition erlaubter Sortierattribute für {@link ReportingKurs}
 * sowie Hilfsmethoden zum Erzeugen passender Comparatoren.
 */
public final class SortierungRegistryReportingKurs {

	private SortierungRegistryReportingKurs() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zur Sortierung von Daten für das Reporting. Initialisierung nicht möglich.");
	}

	/**
	 * Erstellt einen {@link Comparator} für die Klasse {@link ReportingKurs} basierend
	 * auf den angegebenen Attributen. Dafür wird eine Liste von Attributnamen verwendet, die
	 * die Sortierreihenfolge beschreiben. Die Liste für Validierungsfehler kann während
	 * der Erstellung des Comparators gefüllt werden.
	 *
	 * @param attribute Eine Liste von Attributnamen, die die Sortierreihenfolge definieren.
	 * @param validierungsfehler Eine Liste von Validierungsfehlern, die während der Verarbeitung eventuell auftreten und zurückgegeben werden können. Kann
	 *                           null sein, dann werden keine Fehler protokolliert.
	 * @return Ein {@link Comparator} für die Klasse {@link ReportingKurs}, basierend auf
	 *         den angegebenen Attributen.
	 */
	public static Comparator<ReportingKurs> buildComparator(final List<String> attribute, final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistryReportingKurs(), attribute, validierungsfehler);
	}

	/**
	 * Erstellt einen {@link Comparator} für die Klasse {@link ReportingKurs} basierend
	 * auf den angegebenen Attributen der Standardsortierung. Die Liste für Validierungsfehler kann während der Erstellung des Comparators gefüllt werden.
	 *
	 * @param validierungsfehler Eine Liste von Validierungsfehlern, die während der Verarbeitung eventuell auftreten und zurückgegeben werden können. Kann
	 *                           null sein, dann werden keine Fehler protokolliert.
	 * @return Ein {@link Comparator} für die Klasse {@link ReportingKurs}, basierend auf
	 *         den angegebenen Attributen.
	 */
	public static Comparator<ReportingKurs> buildComparatorStandard(final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistryReportingKurs(), standardsortierung(), validierungsfehler);
	}

	/**
	 * Erstellt eine Liste von Strings, die die Attribute der Standardsortierung.
	 * für {@link ReportingKurs} repräsentieren.
	 *
	 * @return Eine Liste von Attributnamen in der Reihenfolge der Standardsortierung.
	 */
	public static List<String> standardsortierung() {
		final ArrayList<String> standardSort = new ArrayList<>();
		standardSort.add(ReportingTypesUtils.methodeToString(ReportingKurs::fach) + "." + ReportingTypesUtils.methodeToString(ReportingFach::sortierung));
		standardSort.add(ReportingTypesUtils.methodeToString(ReportingKurs::kursartAllg));
		standardSort.add(ReportingTypesUtils.methodeToString(ReportingKurs::kuerzel));
		standardSort.add(ReportingTypesUtils.methodeToString(ReportingKurs::id));
		return standardSort;
	}

	/**
	 * Stellt die {@link SortierungRegistry} für die Klasse {@link ReportingKurs} öffentlich zur Verfügung
	 *
	 * @return Die konfigurierte Instanz von {@link SortierungRegistry} für {@link ReportingKurs}.
	 */
	public static SortierungRegistry<ReportingKurs> sortierungRegistry() {
		return sortierungRegistryReportingKurs();
	}

	/**
	 * Erstellt und konfiguriert ein {@link SortierungRegistry} für die Klasse {@link ReportingKurs}.
	 *
	 * @return Die konfigurierte Instanz von {@link SortierungRegistry} für {@link ReportingKurs}.
	 */
	private static SortierungRegistry<ReportingKurs> sortierungRegistryReportingKurs() {
		final SortierungRegistry<ReportingKurs> reg = new SortierungRegistry<>();

		// Grundlegende Attribute
		reg.registiereComparable(ReportingKurs::id);
		reg.registiereString(ReportingKurs::kuerzel);
		reg.registiereString(ReportingKurs::bezeichnungZeugnis);
		reg.registiereString(ReportingKurs::kursartAllg);
		reg.registiereComparable(ReportingKurs::sortierung);
		reg.registiereComparable(ReportingKurs::istEpochalunterricht);
		reg.registiereComparable(ReportingKurs::istSichtbar);
		reg.registiereComparable(ReportingKurs::wochenstunden);
		reg.registiereComparable(ReportingKurs::schulnummer);

		// Verschachtelte Attribute: Fach, Kursleitung, Schuljahresabschnitt
		reg.registiereString(ReportingTypesUtils.methodeToString(ReportingKurs::fach) + "." + ReportingTypesUtils.methodeToString(ReportingFach::bezeichnung),
				FunktionBuilder.start(ReportingKurs::fach)
						.then(ReportingFach::bezeichnung)
						.toFunction());
		reg.registiereString(ReportingTypesUtils.methodeToString(ReportingKurs::fach) + "." + ReportingTypesUtils.methodeToString(ReportingFach::kuerzel),
				FunktionBuilder.start(ReportingKurs::fach)
						.then(ReportingFach::kuerzel)
						.toFunction());
		reg.registiereComparable(ReportingTypesUtils.methodeToString(ReportingKurs::fach) + "." + ReportingTypesUtils.methodeToString(ReportingFach::sortierung),
				FunktionBuilder.start(ReportingKurs::fach)
						.then(ReportingFach::sortierung)
						.toFunction());
		reg.registiereString(ReportingTypesUtils.methodeToString(ReportingKurs::kursleitung) + "." + ReportingTypesUtils.methodeToString(ReportingLehrer::nachname),
				FunktionBuilder.start(ReportingKurs::kursleitung)
						.then(ReportingLehrer::nachname)
						.toFunction());
		reg.registiereString(ReportingTypesUtils.methodeToString(ReportingKurs::kursleitung) + "." + ReportingTypesUtils.methodeToString(ReportingLehrer::vorname),
				FunktionBuilder.start(ReportingKurs::kursleitung)
						.then(ReportingLehrer::vorname)
						.toFunction());
		reg.registiereString(ReportingTypesUtils.methodeToString(ReportingKurs::kursleitung) + "." + ReportingTypesUtils.methodeToString(ReportingLehrer::kuerzel),
				FunktionBuilder.start(ReportingKurs::kursleitung)
						.then(ReportingLehrer::kuerzel)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingKurs::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz),
				FunktionBuilder.start(ReportingKurs::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingKurs::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang),
				FunktionBuilder.start(ReportingKurs::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang)
						.toFunction());

		return reg;
	}
}
