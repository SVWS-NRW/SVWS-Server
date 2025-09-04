package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungKlasseStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Registry zur Definition erlaubter Sortierattribute für {@link ReportingStundenplanungKlasseStundenplan}
 * sowie Hilfsmethoden zum Erzeugen passender Comparatoren.
 */
public final class SortierungRegistryReportingStundenplanungKlasseStundenplan {

	/**
	 * Erstellt einen Comparator für {@link ReportingStundenplanungKlasseStundenplan} anhand angegebener Attribute.
	 *
	 * @param attribute Die Sortierattribute (optional inkl. Richtungsangabe)
	 * @param validierungsfehler Liste zum Sammeln unbekannter Attribute (optional)
	 * @return Comparator für {@link ReportingStundenplanungKlasseStundenplan}
	 */
	public static Comparator<ReportingStundenplanungKlasseStundenplan> buildComparator(final List<String> attribute, final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistry(), attribute, validierungsfehler);
	}

	/**
	 * Erstellt einen Comparator für {@link ReportingStundenplanungKlasseStundenplan} anhand der Standardsortierung.
	 *
	 * @param validierungsfehler Liste zum Sammeln unbekannter Attribute (optional)
	 * @return Comparator für {@link ReportingStundenplanungKlasseStundenplan}
	 */
	public static Comparator<ReportingStundenplanungKlasseStundenplan> buildComparatorStandard(final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistry(), standardsortierung(), validierungsfehler);
	}

	/**
	 * Erstellt eine Liste von Strings, die die Attribute der Standardsortierung.
	 * für {@link ReportingStundenplanungKlasseStundenplan} repräsentieren.
	 *
	 * @return Eine Liste von Attributnamen in der Reihenfolge der Standardsortierung.
	 */
	public static List<String> standardsortierung() {
		final SortierungRegistry<ReportingStundenplanungKlasseStundenplan> reg = new SortierungRegistry<>();
		final ArrayList<String> standard = new ArrayList<>();
		SortierungRegistryReportingKlasse.standardsortierung()
				.forEach(attribut -> standard.add(reg.methodeToString(ReportingStundenplanungKlasseStundenplan::klasse) + "." + attribut));
		return standard;
	}

	/**
	 * Stellt die {@link SortierungRegistry} für die Klasse {@link ReportingStundenplanungKlasseStundenplan} öffentlich zur Verfügung
	 *
	 * @return Die konfigurierte Instanz von {@link SortierungRegistry} für {@link ReportingStundenplanungKlasseStundenplan}.
	 */
	public static SortierungRegistry<ReportingStundenplanungKlasseStundenplan> sortierungRegistry() {
		return sortierungRegistryReportingStundenplanungKlasseStundenplan();
	}

	private static SortierungRegistry<ReportingStundenplanungKlasseStundenplan> sortierungRegistryReportingStundenplanungKlasseStundenplan() {
		final SortierungRegistry<ReportingStundenplanungKlasseStundenplan> reg = new SortierungRegistry<>();

		// Sortierattribute für den eingebetteten Stundenplan
		reg.registiereComparable(
				reg.methodeToString(ReportingStundenplanungKlasseStundenplan::stundenplan) + "." + reg.methodeToString(ReportingStundenplanungStundenplan::id),
				FunktionBuilder.start(ReportingStundenplanungKlasseStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::id)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungKlasseStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::beschreibung),
				FunktionBuilder.start(ReportingStundenplanungKlasseStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::beschreibung)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungKlasseStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::gueltigAb),
				FunktionBuilder.start(ReportingStundenplanungKlasseStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigAb)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungKlasseStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::gueltigBis),
				FunktionBuilder.start(ReportingStundenplanungKlasseStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::gueltigBis)
						.toFunction());
		reg.registiereComparable(
				reg.methodeToString(ReportingStundenplanungKlasseStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::wochenperiodizitaet),
				FunktionBuilder.start(ReportingStundenplanungKlasseStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::wochenperiodizitaet)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungKlasseStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ reg.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz),
				FunktionBuilder.start(ReportingStundenplanungKlasseStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittKurz)
						.toFunction());
		reg.registiereString(
				reg.methodeToString(ReportingStundenplanungKlasseStundenplan::stundenplan) + "."
						+ reg.methodeToString(ReportingStundenplanungStundenplan::schuljahresabschnitt) + "."
						+ reg.methodeToString(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang),
				FunktionBuilder.start(ReportingStundenplanungKlasseStundenplan::stundenplan)
						.then(ReportingStundenplanungStundenplan::schuljahresabschnitt)
						.then(ReportingSchuljahresabschnitt::textSchuljahresabschnittLang)
						.toFunction());

		// Importiere alle Klasse-Attribute unter dem Prefix "klasse"
		reg.importiereRegistryEintraege(reg.methodeToString(ReportingStundenplanungKlasseStundenplan::klasse) + ".",
				SortierungRegistryReportingKlasse.sortierungRegistry(), ReportingStundenplanungKlasseStundenplan::klasse);

		return reg;
	}
}
