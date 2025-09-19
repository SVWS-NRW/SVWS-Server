package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungLehrerStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Registry zur Definition erlaubter Sortierattribute für {@link ReportingStundenplanungLehrerStundenplan}
 * sowie Hilfsmethoden zum Erzeugen passender Comparatoren.
 */
public final class SortierungRegistryReportingStundenplanungLehrerStundenplan {

	/**
	 * Erstellt einen Comparator für {@link ReportingStundenplanungLehrerStundenplan} anhand angegebener Attribute.
	 *
	 * @param attribute Die Sortierattribute (optional inkl. Richtungsangabe wie ":desc" oder führendem "-")
	 * @param validierungsfehler Liste zum Sammeln unbekannter Attribute (optional)
	 * @return Comparator für {@link ReportingStundenplanungLehrerStundenplan}
	 */
	public static Comparator<ReportingStundenplanungLehrerStundenplan> buildComparator(final List<String> attribute, final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistry(), attribute, validierungsfehler);
	}

	/**
	 * Erstellt einen Comparator für {@link ReportingStundenplanungLehrerStundenplan} anhand der Standardsortierung.
	 *
	 * @param validierungsfehler Liste zum Sammeln unbekannter Attribute (optional)
	 * @return Comparator für {@link ReportingStundenplanungLehrerStundenplan}
	 */
	public static Comparator<ReportingStundenplanungLehrerStundenplan> buildComparatorStandard(final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistry(), standardsortierung(), validierungsfehler);
	}

	/**
	 * Erstellt eine Liste von Strings, die die Attribute der Standardsortierung.
	 * für {@link ReportingStundenplanungLehrerStundenplan} repräsentieren.
	 *
	 * @return Eine Liste von Attributnamen in der Reihenfolge der Standardsortierung.
	 */
	public static List<String> standardsortierung() {
		final SortierungRegistry<ReportingStundenplanungLehrerStundenplan> reg = new SortierungRegistry<>();
		final ArrayList<String> standard = new ArrayList<>();
		SortierungRegistryReportingLehrer.standardsortierung()
				.forEach(attribut -> standard.add(reg.methodeToString(ReportingStundenplanungLehrerStundenplan::lehrer) + "." + attribut));
		return standard;
	}

	/**
	 * Stellt die {@link SortierungRegistry} für die Klasse {@link ReportingStundenplanungLehrerStundenplan} öffentlich zur Verfügung
	 *
	 * @return Die konfigurierte Instanz von {@link SortierungRegistry} für {@link ReportingStundenplanungLehrerStundenplan}.
	 */
	public static SortierungRegistry<ReportingStundenplanungLehrerStundenplan> sortierungRegistry() {
		return sortierungRegistryReportingStundenplanungLehrerStundenplan();
	}

	private static SortierungRegistry<ReportingStundenplanungLehrerStundenplan> sortierungRegistryReportingStundenplanungLehrerStundenplan() {
		final SortierungRegistry<ReportingStundenplanungLehrerStundenplan> reg = new SortierungRegistry<>();

		// Sortierattribute für den eingebetteten Stundenplan
		reg.registiereComparable(
				reg.methodeToString(ReportingStundenplanungLehrerStundenplan::stundenplan) + "." + reg.methodeToString(ReportingStundenplanungStundenplan::id),
				FunktionBuilder.start(ReportingStundenplanungLehrerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::id)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungLehrerStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::beschreibung),
				FunktionBuilder.start(ReportingStundenplanungLehrerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::beschreibung)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungLehrerStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::gueltigAb),
				FunktionBuilder.start(ReportingStundenplanungLehrerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigAb)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungLehrerStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::gueltigBis),
				FunktionBuilder.start(ReportingStundenplanungLehrerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigBis)
						.toFunction());
		reg.registiereComparable(
				reg.methodeToString(ReportingStundenplanungLehrerStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::wochenperiodizitaet),
				FunktionBuilder.start(ReportingStundenplanungLehrerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::wochenperiodizitaet)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungLehrerStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ reg.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz),
				FunktionBuilder.start(ReportingStundenplanungLehrerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungLehrerStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ reg.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang),
				FunktionBuilder.start(ReportingStundenplanungLehrerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang)
						.toFunction());

		// Importiere alle Lehrer-Attribute unter dem Prefix "lehrer"
		reg.importiereRegistryEintraege(reg.methodeToString(ReportingStundenplanungLehrerStundenplan::lehrer) + ".",
				SortierungRegistryReportingLehrer.sortierungRegistry(), ReportingStundenplanungLehrerStundenplan::lehrer);

		return reg;
	}
}
