package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungSchuelerStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Registry zur Definition erlaubter Sortierattribute für {@link ReportingStundenplanungSchuelerStundenplan}
 * sowie Hilfsmethoden zum Erzeugen passender Comparatoren.
 */
public final class SortierungRegistryReportingStundenplanungSchuelerStundenplan {

	/**
	 * Erstellt einen {@link Comparator} für die Klasse {@link ReportingStundenplanungSchuelerStundenplan} basierend
	 * auf den angegebenen Attributen.
	 *
	 * @param attribute Eine Liste von Attributnamen, die die Sortierreihenfolge definieren.
	 * @param validierungsfehler Liste, die unbekannte Attribute während der Validierung aufnimmt (optional).
	 * @return Comparator entsprechend der Attribute für {@link ReportingStundenplanungSchuelerStundenplan}.
	 */
	public static Comparator<ReportingStundenplanungSchuelerStundenplan> buildComparator(final List<String> attribute, final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistry(), attribute, validierungsfehler);
	}

	/**
	 * Erstellt einen {@link Comparator} für die Klasse {@link ReportingStundenplanungSchuelerStundenplan} basierend
	 * auf der Standardsortierung.
	 *
	 * @param validierungsfehler Liste, die unbekannte Attribute während der Validierung aufnimmt (optional).
	 * @return Comparator entsprechend der Standardsortierung für {@link ReportingStundenplanungSchuelerStundenplan}.
	 */
	public static Comparator<ReportingStundenplanungSchuelerStundenplan> buildComparatorStandard(final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistry(), standardsortierung(), validierungsfehler);
	}

	/**
	 * Erstellt eine Liste von Strings, die die Attribute der Standardsortierung.
	 * für {@link ReportingStundenplanungSchuelerStundenplan} repräsentieren.
	 *
	 * @return Eine Liste von Attributnamen in der Reihenfolge der Standardsortierung.
	 */
	public static List<String> standardsortierung() {
		final SortierungRegistry<ReportingStundenplanungSchuelerStundenplan> reg = new SortierungRegistry<>();
		final ArrayList<String> standard = new ArrayList<>();
		SortierungRegistryReportingSchueler.standardsortierung()
				.forEach(attribut -> standard.add(reg.methodeToString(ReportingStundenplanungSchuelerStundenplan::schueler) + "." + attribut));
		return standard;
	}

	/**
	 * Stellt die {@link SortierungRegistry} für die Klasse {@link ReportingStundenplanungSchuelerStundenplan} öffentlich zur Verfügung
	 *
	 * @return Die konfigurierte Instanz von {@link SortierungRegistry} für {@link ReportingStundenplanungSchuelerStundenplan}.
	 */
	public static SortierungRegistry<ReportingStundenplanungSchuelerStundenplan> sortierungRegistry() {
		return sortierungRegistryReportingReportingStundenplanungSchuelerStundenplan();
	}

	private static SortierungRegistry<ReportingStundenplanungSchuelerStundenplan> sortierungRegistryReportingReportingStundenplanungSchuelerStundenplan() {
		final SortierungRegistry<ReportingStundenplanungSchuelerStundenplan> reg = new SortierungRegistry<>();

		// Sortierattribute für den eingebetteten Stundenplan
		reg.registiereComparable(
				reg.methodeToString(ReportingStundenplanungSchuelerStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::id),
				FunktionBuilder.start(ReportingStundenplanungSchuelerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::id)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungSchuelerStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::beschreibung),
				FunktionBuilder.start(ReportingStundenplanungSchuelerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::beschreibung)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungSchuelerStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::gueltigAb),
				FunktionBuilder.start(ReportingStundenplanungSchuelerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigAb)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungSchuelerStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::gueltigBis),
				FunktionBuilder.start(ReportingStundenplanungSchuelerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigBis)
						.toFunction());
		reg.registiereComparable(
				reg.methodeToString(ReportingStundenplanungSchuelerStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::wochenperiodizitaet),
				FunktionBuilder.start(ReportingStundenplanungSchuelerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::wochenperiodizitaet)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungSchuelerStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ reg.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz),
				FunktionBuilder.start(ReportingStundenplanungSchuelerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungSchuelerStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ reg.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang),
				FunktionBuilder.start(ReportingStundenplanungSchuelerStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang)
						.toFunction());

		// Importiere alle Schüler-Attribute unter dem Prefix "schueler"
		reg.importiereRegistryEintraege(reg.methodeToString(ReportingStundenplanungSchuelerStundenplan::schueler) + ".",
				SortierungRegistryReportingSchueler.sortierungRegistry(), ReportingStundenplanungSchuelerStundenplan::schueler);

		return reg;
	}
}
