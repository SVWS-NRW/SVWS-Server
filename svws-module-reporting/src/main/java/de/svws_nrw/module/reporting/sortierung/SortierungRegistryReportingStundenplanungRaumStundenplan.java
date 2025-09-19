package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungRaumStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungRaum;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Registry zur Definition erlaubter Sortierattribute für {@link ReportingStundenplanungRaumStundenplan}
 * sowie Hilfsmethoden zum Erzeugen passender Comparatoren.
 */
public final class SortierungRegistryReportingStundenplanungRaumStundenplan {

	/**
	 * Erstellt einen Comparator für {@link ReportingStundenplanungRaumStundenplan} anhand angegebener Attribute.
	 *
	 * @param attribute Die Sortierattribute (optional inkl. Richtungsangabe)
	 * @param validierungsfehler Liste zum Sammeln unbekannter Attribute (optional)
	 * @return Comparator für {@link ReportingStundenplanungRaumStundenplan}
	 */
	public static Comparator<ReportingStundenplanungRaumStundenplan> buildComparator(final List<String> attribute, final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistry(), attribute, validierungsfehler);
	}

	/**
	 * Erstellt einen Comparator für {@link ReportingStundenplanungRaumStundenplan} anhand der Standardsortierung.
	 *
	 * @param validierungsfehler Liste zum Sammeln unbekannter Attribute (optional)
	 * @return Comparator für {@link ReportingStundenplanungRaumStundenplan}
	 */
	public static Comparator<ReportingStundenplanungRaumStundenplan> buildComparatorStandard(final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistry(), standardsortierung(), validierungsfehler);
	}

	/**
	 * Erstellt eine Liste von Strings, die die Attribute der Standardsortierung.
	 * für {@link ReportingStundenplanungRaumStundenplan} repräsentieren.
	 *
	 * @return Eine Liste von Attributnamen in der Reihenfolge der Standardsortierung.
	 */
	public static List<String> standardsortierung() {
		final SortierungRegistry<ReportingStundenplanungRaumStundenplan> reg = new SortierungRegistry<>();
		final ArrayList<String> standard = new ArrayList<>();
		standard.add(reg.methodeToString(ReportingStundenplanungRaumStundenplan::raum) + "." + reg.methodeToString(ReportingStundenplanungRaum::kuerzel));
		standard.add(reg.methodeToString(ReportingStundenplanungRaumStundenplan::raum) + "." + reg.methodeToString(ReportingStundenplanungRaum::id));
		return standard;
	}

	/**
	 * Stellt die {@link SortierungRegistry} für die Klasse {@link ReportingStundenplanungRaumStundenplan} öffentlich zur Verfügung
	 *
	 * @return Die konfigurierte Instanz von {@link SortierungRegistry} für {@link ReportingStundenplanungRaumStundenplan}.
	 */
	public static SortierungRegistry<ReportingStundenplanungRaumStundenplan> sortierungRegistry() {
		return sortierungRegistryReportingStundenplanungRaumStundenplan();
	}

	private static SortierungRegistry<ReportingStundenplanungRaumStundenplan> sortierungRegistryReportingStundenplanungRaumStundenplan() {
		final SortierungRegistry<ReportingStundenplanungRaumStundenplan> reg = new SortierungRegistry<>();

		// Sortierattribute für den eingebetteten Stundenplan
		reg.registiereComparable(
				reg.methodeToString(ReportingStundenplanungRaumStundenplan::stundenplan) + "." + reg.methodeToString(ReportingStundenplanungStundenplan::id),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::id)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungRaumStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::beschreibung),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::beschreibung)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungRaumStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::gueltigAb),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigAb)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungRaumStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::gueltigBis),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigBis)
						.toFunction());
		reg.registiereComparable(
				reg.methodeToString(ReportingStundenplanungRaumStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::wochenperiodizitaet),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::wochenperiodizitaet)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungRaumStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ reg.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungRaumStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ reg.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang)
						.toFunction());

		// Sortierattribute für den eingebetteten Raum
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungRaumStundenplan::raum) + "." + reg.methodeToString(ReportingStundenplanungRaum::kuerzel),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::raum)
						.then(ReportingStundenplanungRaum::kuerzel)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungRaumStundenplan::raum) + "." + reg.methodeToString(ReportingStundenplanungRaum::beschreibung),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::raum)
						.then(ReportingStundenplanungRaum::beschreibung)
						.toFunction());
		reg.registiereComparable(reg.methodeToString(ReportingStundenplanungRaumStundenplan::raum) + "." + reg.methodeToString(ReportingStundenplanungRaum::id),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::raum)
						.then(ReportingStundenplanungRaum::id)
						.toFunction());
		reg.registiereComparable(
				reg.methodeToString(ReportingStundenplanungRaumStundenplan::raum) + "." + reg.methodeToString(ReportingStundenplanungRaum::kapazitaet),
				FunktionBuilder.start(ReportingStundenplanungRaumStundenplan::raum)
						.then(ReportingStundenplanungRaum::kapazitaet)
						.toFunction());

		return reg;
	}
}
