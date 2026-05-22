package de.svws_nrw.module.reporting.sortierung;

import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SortierungRegistryReportingSchueler {

	private SortierungRegistryReportingSchueler() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zur Sortierung von Daten für das Reporting. Initialisierung nicht möglich.");
	}

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
		final ArrayList<String> standard = new ArrayList<>();
		standard.add(ReportingTypesUtils.methodeToString(ReportingSchueler::nachname));
		standard.add(ReportingTypesUtils.methodeToString(ReportingSchueler::vorname));
		standard.add(ReportingTypesUtils.methodeToString(ReportingSchueler::vornamen));
		standard.add(ReportingTypesUtils.methodeToString(ReportingSchueler::geburtsdatum));
		standard.add(ReportingTypesUtils.methodeToString(ReportingSchueler::id));
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
		reg.registiereString(ReportingSchueler::nachname);
		reg.registiereString(ReportingSchueler::vorname);
		reg.registiereString(ReportingSchueler::vornamen);
		reg.registiereString(ReportingSchueler::anrede);
		reg.registiereString(ReportingSchueler::titel);
		reg.registiereString(ReportingSchueler::geburtsdatum);
		reg.registiereString(ReportingSchueler::geburtsname);
		reg.registiereString(ReportingSchueler::geburtsort);
		reg.registiereComparable(ReportingSchueler::geschlecht);
		reg.registiereString(ReportingSchueler::emailPrivat);
		reg.registiereString(ReportingSchueler::emailSchule);
		reg.registiereString(ReportingSchueler::telefonPrivat);
		reg.registiereString(ReportingSchueler::telefonPrivatMobil);
		reg.registiereString(ReportingSchueler::strassenname);
		reg.registiereString(ReportingSchueler::hausnummer);
		reg.registiereString(ReportingSchueler::hausnummerZusatz);
		reg.registiereString(ReportingSchueler::wohnortname);
		reg.registiereString(ReportingSchueler::wohnortsteilname);

		// Registrierung schul-/schülerspezifischer Attribute (aus ReportingSchueler)
		reg.registiereComparable(ReportingSchueler::id);
		reg.registiereComparable(ReportingSchueler::status);
		reg.registiereComparable(ReportingSchueler::haltestelleID);
		reg.registiereComparable(ReportingSchueler::fahrschuelerArtID);
		reg.registiereComparable(ReportingSchueler::zuzugsjahr);
		reg.registiereString(ReportingSchueler::religionanmeldung);
		reg.registiereString(ReportingSchueler::religionabmeldung);
		reg.registiereString(ReportingSchueler::verkehrspracheFamilie);
		reg.registiereComparable(ReportingSchueler::erhaeltSchuelerBAFOEG);
		reg.registiereComparable(ReportingSchueler::erhaeltMeisterBAFOEG);
		reg.registiereComparable(ReportingSchueler::hatMigrationshintergrund);
		reg.registiereComparable(ReportingSchueler::hatMasernimpfnachweis);
		reg.registiereComparable(ReportingSchueler::istDuplikat);
		reg.registiereComparable(ReportingSchueler::keineAuskunftAnDritte);
		reg.registiereComparable(ReportingSchueler::istVolljaehrig);
		reg.registiereComparable(ReportingSchueler::istSchulpflichtErfuellt);
		reg.registiereComparable(ReportingSchueler::istBerufsschulpflichtErfuellt);


		// Importiere alle Klasse-Attribute unter dem Prefix "auswahlLernabschnitt.klasse"
		reg.importiereRegistryEintraege(
				ReportingTypesUtils.methodeToString(ReportingSchueler::auswahlLernabschnitt) + "." + ReportingTypesUtils.methodeToString(ReportingSchuelerLernabschnitt::klasse) + ".",
				SortierungRegistryReportingKlasse.sortierungRegistry(),
				FunktionBuilder.start(ReportingSchueler::auswahlLernabschnitt).then(ReportingSchuelerLernabschnitt::klasse).toFunction());

		// Verschachtelte Attribute über den ausgewählten Lernabschnitt
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingSchueler::auswahlLernabschnitt) + "." + ReportingTypesUtils.methodeToString(ReportingSchuelerLernabschnitt::jahrgang) + "."
						+ ReportingTypesUtils.methodeToString(ReportingJahrgang::kuerzel),
				FunktionBuilder.start(ReportingSchueler::auswahlLernabschnitt)
						.then(ReportingSchuelerLernabschnitt::jahrgang)
						.then(ReportingJahrgang::kuerzel)
						.toFunction()
		);

		return reg;
	}
}
