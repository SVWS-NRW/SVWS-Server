package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Registry zur Definition erlaubter Sortierattribute für {@link ReportingKlasse}
 * sowie Hilfsmethoden zum Erzeugen passender Comparatoren.
 */
public final class SortierungRegistryReportingKlasse {

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
		final SortierungRegistry<ReportingKlasse> reg = new SortierungRegistry<>();
		final ArrayList<String> standardSort = new ArrayList<>();
		standardSort.add(reg.methodeToString(ReportingKlasse::sortierung));
		standardSort.add(reg.methodeToString(ReportingKlasse::kuerzel));
		standardSort.add(reg.methodeToString(ReportingKlasse::id));
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
		reg.registiereComparable(reg.methodeToString(ReportingKlasse::id), ReportingKlasse::id);
		reg.registiereComparable(reg.methodeToString(ReportingKlasse::sortierung), ReportingKlasse::sortierung);
		reg.registiereString(reg.methodeToString(ReportingKlasse::kuerzel), ReportingKlasse::kuerzel);
		reg.registiereString(reg.methodeToString(ReportingKlasse::beschreibung), ReportingKlasse::beschreibung);
		reg.registiereString(reg.methodeToString(ReportingKlasse::parallelitaet), ReportingKlasse::parallelitaet);
		reg.registiereString(reg.methodeToString(ReportingKlasse::auflistungKlassenleitung), ReportingKlasse::auflistungKlassenleitung);
		reg.registiereString(reg.methodeToString(ReportingKlasse::teilstandort), ReportingKlasse::teilstandort);

		// IDs und Kennungen (als Comparable)
		reg.registiereComparable(reg.methodeToString(ReportingKlasse::idKlassenart), ReportingKlasse::idKlassenart);
		reg.registiereComparable(reg.methodeToString(ReportingKlasse::idSchulgliederung), ReportingKlasse::idSchulgliederung);
		reg.registiereComparable(reg.methodeToString(ReportingKlasse::idJahrgang), ReportingKlasse::idJahrgang);
		reg.registiereComparable(reg.methodeToString(ReportingKlasse::idFachklasse), ReportingKlasse::idFachklasse);
		reg.registiereComparable(reg.methodeToString(ReportingKlasse::idFolgeklasse), ReportingKlasse::idFolgeklasse);
		reg.registiereComparable(reg.methodeToString(ReportingKlasse::idVorgaengerklasse), ReportingKlasse::idVorgaengerklasse);
		reg.registiereString(reg.methodeToString(ReportingKlasse::kuerzelFolgeklasse), ReportingKlasse::kuerzelFolgeklasse);
		reg.registiereString(reg.methodeToString(ReportingKlasse::kuerzelVorgaengerklasse), ReportingKlasse::kuerzelVorgaengerklasse);

		// Verschachtelte Attribute
		reg.registiereString(reg.methodeToString(ReportingKlasse::jahrgang) + "." + reg.methodeToString(ReportingJahrgang::kuerzel),
				FunktionBuilder.start(ReportingKlasse::jahrgang)
						.then(ReportingJahrgang::kuerzel)
						.toFunction());
		reg.registiereString(reg.methodeToString(ReportingKlasse::jahrgang) + "." + reg.methodeToString(ReportingJahrgang::kuerzel),
				FunktionBuilder.start(ReportingKlasse::jahrgang)
						.then(ReportingJahrgang::kuerzel)
						.toFunction());

		reg.registiereString(reg.methodeToString(ReportingKlasse::schuljahresabschnitt) + "." + reg.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz),
				FunktionBuilder.start(ReportingKlasse::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz)
						.toFunction());
		reg.registiereString(reg.methodeToString(ReportingKlasse::schuljahresabschnitt) + "." + reg.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang),
				FunktionBuilder.start(ReportingKlasse::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang)
						.toFunction());

		return reg;
	}
}
