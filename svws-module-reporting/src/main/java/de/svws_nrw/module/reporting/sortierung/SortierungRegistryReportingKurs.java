package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Registry zur Definition erlaubter Sortierattribute für {@link ReportingKurs}
 * sowie Hilfsmethoden zum Erzeugen passender Comparatoren.
 */
public final class SortierungRegistryReportingKurs {

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
		final SortierungRegistry<ReportingKurs> reg = new SortierungRegistry<>();
		final ArrayList<String> standardSort = new ArrayList<>();
		standardSort.add(reg.methodeToString(ReportingKurs::fach) + "." + reg.methodeToString(ReportingKurs::sortierung));
		standardSort.add(reg.methodeToString(ReportingKurs::kursartAllg));
		standardSort.add(reg.methodeToString(ReportingKurs::kuerzel));
		standardSort.add(reg.methodeToString(ReportingKurs::id));
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
		reg.registiereComparable(reg.methodeToString(ReportingKurs::id), ReportingKurs::id);
		reg.registiereString(reg.methodeToString(ReportingKurs::kuerzel), ReportingKurs::kuerzel);
		reg.registiereString(reg.methodeToString(ReportingKurs::bezeichnungZeugnis), ReportingKurs::bezeichnungZeugnis);
		reg.registiereString(reg.methodeToString(ReportingKurs::kursartAllg), ReportingKurs::kursartAllg);
		reg.registiereComparable(reg.methodeToString(ReportingKurs::sortierung), ReportingKurs::sortierung);
		reg.registiereComparable(reg.methodeToString(ReportingKurs::istEpochalunterricht), ReportingKurs::istEpochalunterricht);
		reg.registiereComparable(reg.methodeToString(ReportingKurs::istSichtbar), ReportingKurs::istSichtbar);
		reg.registiereComparable(reg.methodeToString(ReportingKurs::wochenstunden), ReportingKurs::wochenstunden);
		reg.registiereComparable(reg.methodeToString(ReportingKurs::schulnummer), ReportingKurs::schulnummer);

		// Verschachtelte Attribute: Fach, Kursleitung, Schuljahresabschnitt
		reg.registiereString(reg.methodeToString(ReportingKurs::fach) + "." + reg.methodeToString(ReportingFach::bezeichnung),
				FunktionBuilder.start(ReportingKurs::fach)
						.then(ReportingFach::bezeichnung)
						.toFunction());
		reg.registiereString(reg.methodeToString(ReportingKurs::fach) + "." + reg.methodeToString(ReportingFach::kuerzel),
				FunktionBuilder.start(ReportingKurs::fach)
						.then(ReportingFach::kuerzel)
						.toFunction());
		reg.registiereString(reg.methodeToString(ReportingKurs::kursleitung) + "." + reg.methodeToString(ReportingLehrer::nachname),
				FunktionBuilder.start(ReportingKurs::kursleitung)
						.then(ReportingLehrer::nachname)
						.toFunction());
		reg.registiereString(reg.methodeToString(ReportingKurs::kursleitung) + "." + reg.methodeToString(ReportingLehrer::vorname),
				FunktionBuilder.start(ReportingKurs::kursleitung)
						.then(ReportingLehrer::vorname)
						.toFunction());
		reg.registiereString(reg.methodeToString(ReportingKurs::kursleitung) + "." + reg.methodeToString(ReportingLehrer::kuerzel),
				FunktionBuilder.start(ReportingKurs::kursleitung)
						.then(ReportingLehrer::kuerzel)
						.toFunction());
		reg.registiereString(reg.methodeToString(ReportingKurs::schuljahresabschnitt) + "." + reg.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz),
				FunktionBuilder.start(ReportingKurs::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz)
						.toFunction());
		reg.registiereString(reg.methodeToString(ReportingKurs::schuljahresabschnitt) + "." + reg.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang),
				FunktionBuilder.start(ReportingKurs::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang)
						.toFunction());


		return reg;
	}
}
