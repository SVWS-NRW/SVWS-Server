package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungFachStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Registry zur Definition erlaubter Sortierattribute für {@link ReportingStundenplanungFachStundenplan}
 * sowie Hilfsmethoden zum Erzeugen passender Comparatoren.
 */
public final class SortierungRegistryReportingStundenplanungFachStundenplan {

	/**
	 * Erstellt einen Comparator für {@link ReportingStundenplanungFachStundenplan} anhand angegebener Attribute.
	 *
	 * @param attribute Die Sortierattribute (optional inklusive Richtungsangabe)
	 * @param validierungsfehler Liste zum Sammeln unbekannter Attribute (optional)
	 * @return Comparator für {@link ReportingStundenplanungFachStundenplan}
	 */
	public static Comparator<ReportingStundenplanungFachStundenplan> buildComparator(final List<String> attribute, final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistry(), attribute, validierungsfehler);
	}

	/**
	 * Erstellt einen Comparator für {@link ReportingStundenplanungFachStundenplan} anhand der Standardsortierung.
	 *
	 * @param validierungsfehler Liste zum Sammeln unbekannter Attribute (optional)
	 * @return Comparator für {@link ReportingStundenplanungFachStundenplan}
	 */
	public static Comparator<ReportingStundenplanungFachStundenplan> buildComparatorStandard(final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistry(), standardsortierung(), validierungsfehler);
	}

	/**
	 * Erstellt eine Liste von Strings, die die Attribute der Standardsortierung.
	 * für {@link ReportingStundenplanungFachStundenplan} repräsentieren.
	 *
	 * @return Eine Liste von Attributnamen in der Reihenfolge der Standardsortierung.
	 */
	public static List<String> standardsortierung() {
		final SortierungRegistry<ReportingStundenplanungFachStundenplan> reg = new SortierungRegistry<>();
		final ArrayList<String> standard = new ArrayList<>();
		standard.add(reg.methodeToString(ReportingStundenplanungFachStundenplan::fach) + "." + reg.methodeToString(ReportingFach::sortierung));
		standard.add(reg.methodeToString(ReportingStundenplanungFachStundenplan::fach) + "." + reg.methodeToString(ReportingFach::kuerzel));
		standard.add(reg.methodeToString(ReportingStundenplanungFachStundenplan::fach) + "." + reg.methodeToString(ReportingFach::id));
		return standard;
	}

	/**
	 * Stellt die {@link SortierungRegistry} für die Klasse {@link ReportingStundenplanungFachStundenplan} öffentlich zur Verfügung
	 *
	 * @return Die konfigurierte Instanz von {@link SortierungRegistry} für {@link ReportingStundenplanungFachStundenplan}.
	 */
	public static SortierungRegistry<ReportingStundenplanungFachStundenplan> sortierungRegistry() {
		return sortierungRegistryReportingStundenplanungFachStundenplan();
	}

	private static SortierungRegistry<ReportingStundenplanungFachStundenplan> sortierungRegistryReportingStundenplanungFachStundenplan() {
		final SortierungRegistry<ReportingStundenplanungFachStundenplan> reg = new SortierungRegistry<>();

		// Sortierattribute für den eingebetteten Stundenplan
		reg.registiereComparable(
				reg.methodeToString(ReportingStundenplanungFachStundenplan::stundenplan) + "." + reg.methodeToString(ReportingStundenplanungStundenplan::id),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::id)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungFachStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::beschreibung),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::beschreibung)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungFachStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::gueltigAb),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigAb)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungFachStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::gueltigBis),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigBis)
						.toFunction());
		reg.registiereComparable(
				reg.methodeToString(ReportingStundenplanungFachStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::wochenperiodizitaet),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::wochenperiodizitaet)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungFachStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ reg.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungFachStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ reg.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang)
						.toFunction());

		// Sortierattribute für das eingebettete Fach
		reg.registiereComparable(reg.methodeToString(ReportingStundenplanungFachStundenplan::fach) + "." + reg.methodeToString(ReportingFach::id),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::fach)
						.then(ReportingFach::id)
						.toFunction());
		reg.registiereComparable(reg.methodeToString(ReportingStundenplanungFachStundenplan::fach) + "." + reg.methodeToString(ReportingFach::sortierung),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::fach)
						.then(ReportingFach::sortierung)
						.toFunction());
		reg.registiereString(reg.methodeToString(ReportingStundenplanungFachStundenplan::fach) + "." + reg.methodeToString(ReportingFach::kuerzel),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::fach)
						.then(ReportingFach::kuerzel)
						.toFunction());
		reg.registiereString(reg.methodeToString(ReportingStundenplanungFachStundenplan::fach) + "." + reg.methodeToString(ReportingFach::bezeichnung),
				FunktionBuilder.start(ReportingStundenplanungFachStundenplan::fach)
						.then(ReportingFach::bezeichnung)
						.toFunction());

		return reg;
	}
}
