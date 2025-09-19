package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SortierungRegistryReportingSchueler {

	/**
	 * Erstellt einen {@link Comparator} für die Klasse {@link ReportingSchueler} basierend
	 * auf den angegebenen Attributen. Dafür wird eine Liste von Attributnamen verwendet, die
	 * die Sortierreihenfolge beschreiben. Die Liste für Validierungsfehler kann während
	 * der Erstellung des Comparators gefüllt werden.
	 *
	 * @param attribute Eine Liste von Attributnamen, die die Sortierreihenfolge definieren.
	 * @param validierungsfehler Eine Liste von Validierungsfehlern, die während der Verarbeitung eventuell auftreten und zurückgegeben werden können. Kann
	 * null sein, dann werden keine Fehler protokolliert.
	 * @return Ein {@link Comparator} für die Klasse {@link ReportingSchueler}, basierend auf
	 *         den angegebenen Attributen.
	 */
	public static Comparator<ReportingSchueler> buildComparator(final List<String> attribute, final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistryReportingSchueler(), attribute, validierungsfehler);
	}

	/**
	 * Erstellt einen {@link Comparator} für die Klasse {@link ReportingSchueler} basierend
	 * auf den angegebenen Attributen der Standardsortierung. Die Liste für Validierungsfehler kann während der Erstellung des Comparators gefüllt werden.
	 *
	 * @param validierungsfehler Eine Liste von Validierungsfehlern, die während der Verarbeitung eventuell auftreten und zurückgegeben werden können. Kann
	 * null sein, dann werden keine Fehler protokolliert.
	 * @return Ein {@link Comparator} für die Klasse {@link ReportingSchueler}, basierend auf den angegebenen Attributen.
	 */
	public static Comparator<ReportingSchueler> buildComparatorStandard(final List<String> validierungsfehler) {
		return ComparatorBuilder.build(sortierungRegistryReportingSchueler(), standardsortierung(), validierungsfehler);
	}

	/**
	 * Erstellt eine Liste von Strings, die die Attribute der Standardsortierung.
	 * für {@link ReportingSchueler} repräsentieren.
	 *
	 * @return Eine Liste von Attributnamen in der Reihenfolge der Standardsortierung.
	 */
	public static List<String> standardsortierung() {
		final SortierungRegistry<ReportingSchueler> reg = new SortierungRegistry<>();
		final ArrayList<String> standard = new ArrayList<>();
		standard.add(reg.methodeToString(ReportingSchueler::nachname));
		standard.add(reg.methodeToString(ReportingSchueler::vorname));
		standard.add(reg.methodeToString(ReportingSchueler::vornamen));
		standard.add(reg.methodeToString(ReportingSchueler::geburtsdatum));
		standard.add(reg.methodeToString(ReportingSchueler::id));
		return standard;
	}

	/**
	 * Stellt die {@link SortierungRegistry} für die Klasse {@link ReportingSchueler} öffentlich zur Verfügung
	 *
	 * @return Die konfigurierte Instanz von {@link SortierungRegistry} für {@link ReportingSchueler}.
	 */
	public static SortierungRegistry<ReportingSchueler> sortierungRegistry() {
		return sortierungRegistryReportingSchueler();
	}

	/**
	 * Erstellt und konfiguriert ein {@link SortierungRegistry} für die Klasse {@link ReportingSchueler}.
	 * Dabei werden verschiedene Attribute registriert, die sowohl flache Felder
	 * als auch verschachtelte Strukturen in untergeordneten Objekten abdecken. Für einige
	 * Attribute werden auch Fallback-Mechanismen definiert.
	 *
	 * @return Die konfigurierte Instanz von {@link SortierungRegistry} für {@link ReportingSchueler}.
	 */
	private static SortierungRegistry<ReportingSchueler> sortierungRegistryReportingSchueler() {

		final SortierungRegistry<ReportingSchueler> reg = new SortierungRegistry<>();

		// Registrierung grundlegender Personenattribute (aus ReportingPerson)
		reg.registiereString(reg.methodeToString(ReportingSchueler::nachname), ReportingSchueler::nachname);
		reg.registiereString(reg.methodeToString(ReportingSchueler::vorname), ReportingSchueler::vorname);
		reg.registiereString(reg.methodeToString(ReportingSchueler::vornamen), ReportingSchueler::vornamen);
		reg.registiereString(reg.methodeToString(ReportingSchueler::anrede), ReportingSchueler::anrede);
		reg.registiereString(reg.methodeToString(ReportingSchueler::titel), ReportingSchueler::titel);
		reg.registiereString(reg.methodeToString(ReportingSchueler::geburtsdatum), ReportingSchueler::geburtsdatum);
		reg.registiereString(reg.methodeToString(ReportingSchueler::geburtsname), ReportingSchueler::geburtsname);
		reg.registiereString(reg.methodeToString(ReportingSchueler::geburtsort), ReportingSchueler::geburtsort);
		reg.registiereComparable(reg.methodeToString(ReportingSchueler::geschlecht), ReportingSchueler::geschlecht);
		reg.registiereString(reg.methodeToString(ReportingSchueler::emailPrivat), ReportingSchueler::emailPrivat);
		reg.registiereString(reg.methodeToString(ReportingSchueler::emailSchule), ReportingSchueler::emailSchule);
		reg.registiereString(reg.methodeToString(ReportingSchueler::telefonPrivat), ReportingSchueler::telefonPrivat);
		reg.registiereString(reg.methodeToString(ReportingSchueler::telefonPrivatMobil), ReportingSchueler::telefonPrivatMobil);
		reg.registiereString(reg.methodeToString(ReportingSchueler::strassenname), ReportingSchueler::strassenname);
		reg.registiereString(reg.methodeToString(ReportingSchueler::hausnummer), ReportingSchueler::hausnummer);
		reg.registiereString(reg.methodeToString(ReportingSchueler::hausnummerZusatz), ReportingSchueler::hausnummerZusatz);
		reg.registiereString(reg.methodeToString(ReportingSchueler::wohnortname), ReportingSchueler::wohnortname);
		reg.registiereString(reg.methodeToString(ReportingSchueler::wohnortsteilname), ReportingSchueler::wohnortsteilname);

		// Registrierung schul-/schülerspezifischer Attribute (aus ReportingSchueler)
		reg.registiereComparable(reg.methodeToString(ReportingSchueler::id), ReportingSchueler::id);
		reg.registiereComparable(reg.methodeToString(ReportingSchueler::status), ReportingSchueler::status);
		reg.registiereComparable(reg.methodeToString(ReportingSchueler::haltestelleID), ReportingSchueler::haltestelleID);
		reg.registiereComparable(reg.methodeToString(ReportingSchueler::fahrschuelerArtID), ReportingSchueler::fahrschuelerArtID);
		reg.registiereComparable(reg.methodeToString(ReportingSchueler::zuzugsjahr), ReportingSchueler::zuzugsjahr);
		reg.registiereString(reg.methodeToString(ReportingSchueler::religionanmeldung), ReportingSchueler::religionanmeldung);
		reg.registiereString(reg.methodeToString(ReportingSchueler::religionabmeldung), ReportingSchueler::religionabmeldung);
		reg.registiereString(reg.methodeToString(ReportingSchueler::verkehrspracheFamilie), ReportingSchueler::verkehrspracheFamilie);
		reg.registiereComparable(reg.methodeToString(ReportingSchueler::erhaeltSchuelerBAFOEG), ReportingSchueler::erhaeltSchuelerBAFOEG);
		reg.registiereComparable(reg.methodeToString(ReportingSchueler::erhaeltMeisterBAFOEG), ReportingSchueler::erhaeltMeisterBAFOEG);
		reg.registiereComparable(reg.methodeToString(ReportingSchueler::hatMigrationshintergrund), ReportingSchueler::hatMigrationshintergrund);
		reg.registiereComparable(reg.methodeToString(ReportingSchueler::hatMasernimpfnachweis), ReportingSchueler::hatMasernimpfnachweis);
		reg.registiereComparable(reg.methodeToString(ReportingSchueler::istDuplikat), ReportingSchueler::istDuplikat);
		reg.registiereComparable(reg.methodeToString(ReportingSchueler::keineAuskunftAnDritte), ReportingSchueler::keineAuskunftAnDritte);
		reg.registiereComparable(reg.methodeToString(ReportingSchueler::istVolljaehrig), ReportingSchueler::istVolljaehrig);
		reg.registiereComparable(reg.methodeToString(ReportingSchueler::istSchulpflichtErfuellt), ReportingSchueler::istSchulpflichtErfuellt);
		reg.registiereComparable(reg.methodeToString(ReportingSchueler::istBerufsschulpflichtErfuellt), ReportingSchueler::istBerufsschulpflichtErfuellt);


		// Importiere alle Klasse-Attribute unter dem Prefix "auswahlLernabschnitt.klasse"
		reg.importiereRegistryEintraege(
				reg.methodeToString(ReportingSchueler::auswahlLernabschnitt) + "." + reg.methodeToString(ReportingSchuelerLernabschnitt::klasse) + ".",
				SortierungRegistryReportingKlasse.sortierungRegistry(),
				FunktionBuilder.start(ReportingSchueler::auswahlLernabschnitt).then(ReportingSchuelerLernabschnitt::klasse).toFunction());

		// Verschachtelte Attribute über den ausgewählten Lernabschnitt
		reg.registiereString(
				reg.methodeToString(ReportingSchueler::auswahlLernabschnitt) + "." + reg.methodeToString(ReportingSchuelerLernabschnitt::jahrgang) + "."
						+ reg.methodeToString(ReportingJahrgang::kuerzel),
				FunktionBuilder.start(ReportingSchueler::auswahlLernabschnitt)
						.then(ReportingSchuelerLernabschnitt::jahrgang)
						.then(ReportingJahrgang::kuerzel)
						.toFunction()
		);

		return reg;
	}
}
