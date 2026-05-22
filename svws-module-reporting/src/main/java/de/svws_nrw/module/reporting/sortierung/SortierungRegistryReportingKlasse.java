package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Registry zur Definition erlaubter Sortierattribute für {@link ReportingKlasse}
 * sowie Hilfsmethoden zum Erzeugen passender Comparatoren.
 */
public final class SortierungRegistryReportingKlasse {

	private SortierungRegistryReportingKlasse() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zur Sortierung von Daten für das Reporting. Initialisierung nicht möglich.");
	}

	/**
	 * Erstellt einen {@link Comparator} für die Klasse {@link ReportingKlasse} basierend
	 * auf den angegebenen Attributen. Dafür wird eine Liste von Attributnamen verwendet, die
	 * die Sortierreihenfolge beschreiben. Die Liste für Validierungsfehler kann während
	 * der Erstellung des Comparators gefüllt werden.
	 *
	 * @param attribute Eine Liste von Attributnamen, die die Sortierreihenfolge definieren.
	 * @param validierungsfehler Eine Liste von Validierungsfehlern, die während der Verarbeitung eventuell auftreten und zurückgegeben werden können. Kann
	 *                           null sein, dann werden keine Fehler protokolliert.
	 * @return Ein {@link Comparator} für die Klasse {@link ReportingKlasse}, basierend auf
	 *         den angegebenen Attributen.
	 */
	public static Comparator<ReportingKlasse> buildComparator(final List<String> attribute, final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistryReportingKlasse(), attribute, validierungsfehler);
	}

	/**
	 * Erstellt einen {@link Comparator} für die Klasse {@link ReportingKlasse} basierend
	 * auf den angegebenen Attributen der Standardsortierung. Die Liste für Validierungsfehler kann während der Erstellung des Comparators gefüllt werden.
	 *
	 * @param validierungsfehler Eine Liste von Validierungsfehlern, die während der Verarbeitung eventuell auftreten und zurückgegeben werden können. Kann
	 *                           null sein, dann werden keine Fehler protokolliert.
	 * @return Ein {@link Comparator} für die Klasse {@link ReportingKlasse}, basierend auf
	 *         den angegebenen Attributen.
	 */
	public static Comparator<ReportingKlasse> buildComparatorStandard(final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistryReportingKlasse(), standardsortierung(), validierungsfehler);
	}

	/**
	 * Erstellt eine Liste von Strings, die die Attribute der Standardsortierung.
	 * für {@link ReportingKlasse} repräsentieren.
	 * @return Eine Liste von Attributnamen in der Reihenfolge der Standardsortierung.
	 */
	public static List<String> standardsortierung() {
		final ArrayList<String> standardSort = new ArrayList<>();
		standardSort.add(ReportingTypesUtils.methodeToString(ReportingKlasse::sortierung));
		standardSort.add(ReportingTypesUtils.methodeToString(ReportingKlasse::kuerzel));
		standardSort.add(ReportingTypesUtils.methodeToString(ReportingKlasse::id));
		return standardSort;
	}

	/**
	 * Stellt die {@link SortierungRegistry} für die Klasse {@link ReportingKlasse} öffentlich zur Verfügung
	 *
	 * @return Die konfigurierte Instanz von {@link SortierungRegistry} für {@link ReportingKlasse}.
	 */
	public static SortierungRegistry<ReportingKlasse> sortierungRegistry() {
		return sortierungRegistryReportingKlasse();
	}

	/**
	 * Erstellt und konfiguriert ein {@link SortierungRegistry} für die Klasse {@link ReportingKlasse}.
	 *
	 * @return Die konfigurierte Instanz von {@link SortierungRegistry} für {@link ReportingKlasse}.
	 */
	private static SortierungRegistry<ReportingKlasse> sortierungRegistryReportingKlasse() {
		final SortierungRegistry<ReportingKlasse> reg = new SortierungRegistry<>();

		// Grundlegende Attribute
		reg.registiereComparable(ReportingKlasse::id);
		reg.registiereComparable(ReportingKlasse::sortierung);
		reg.registiereString(ReportingKlasse::kuerzel);
		reg.registiereString(ReportingKlasse::beschreibung);
		reg.registiereString(ReportingKlasse::parallelitaet);
		reg.registiereString(ReportingKlasse::auflistungKlassenlehrerkuerzel);
		reg.registiereString(ReportingKlasse::teilstandort);

		// IDs und Kennungen (als Comparable)
		reg.registiereComparable(ReportingKlasse::idKlassenart);
		reg.registiereComparable(ReportingKlasse::idSchulgliederung);
		reg.registiereComparable(ReportingKlasse::idJahrgang);
		reg.registiereComparable(ReportingKlasse::idFachklasse);
		reg.registiereComparable(ReportingKlasse::idFolgeklasse);
		reg.registiereComparable(ReportingKlasse::idVorgaengerklasse);
		reg.registiereString(ReportingKlasse::kuerzelFolgeklasse);
		reg.registiereString(ReportingKlasse::kuerzelVorgaengerklasse);

		// Verschachtelte Attribute
		reg.registiereString(ReportingTypesUtils.methodeToString(ReportingKlasse::jahrgang) + "." + ReportingTypesUtils.methodeToString(ReportingJahrgang::kuerzel),
				FunktionBuilder.start(ReportingKlasse::jahrgang)
						.then(ReportingJahrgang::kuerzel)
						.toFunction());
		reg.registiereString(ReportingTypesUtils.methodeToString(ReportingKlasse::jahrgang) + "." + ReportingTypesUtils.methodeToString(ReportingJahrgang::kuerzel),
				FunktionBuilder.start(ReportingKlasse::jahrgang)
						.then(ReportingJahrgang::kuerzel)
						.toFunction());

		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingKlasse::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz),
				FunktionBuilder.start(ReportingKlasse::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz)
						.toFunction());
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingKlasse::schuljahresabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang),
				FunktionBuilder.start(ReportingKlasse::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang)
						.toFunction());

		return reg;
	}
}
