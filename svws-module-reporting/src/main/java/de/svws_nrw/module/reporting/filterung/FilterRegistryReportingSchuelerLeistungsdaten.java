package de.svws_nrw.module.reporting.filterung;

import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLeistungsdaten;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Registry zur Definition erlaubter Filterattribute für {@link ReportingSchuelerLeistungsdaten}
 * sowie Hilfsmethoden zum Erzeugen passender Predicates.
 */
public final class FilterRegistryReportingSchuelerLeistungsdaten {

	private FilterRegistryReportingSchuelerLeistungsdaten() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zur Filterung von Daten für das Reporting. Initialisierung nicht möglich.");
	}

	/**
	 * Stellt die {@link FilterRegistry} für die Klasse {@link ReportingSchuelerLeistungsdaten} öffentlich zur Verfügung.
	 * Diese Methode wird vom {@link ReportingContext} via Reflection aufgerufen.
	 *
	 * @return Die konfigurierte Instanz von {@link FilterRegistry} für {@link ReportingSchuelerLeistungsdaten}.
	 */
	public static FilterRegistry<ReportingSchuelerLeistungsdaten> filterRegistry() {
		return filterRegistryReportingSchuelerLeistungsdaten();
	}

	/**
	 * Erstellt und konfiguriert eine {@link FilterRegistry} für die Klasse {@link ReportingSchuelerLeistungsdaten}.
	 * Hier werden alle Attribute registriert, auf die ein Filter in den ReportingParametern angewendet werden kann.
	 *
	 * @return Die konfigurierte Instanz von {@link FilterRegistry} für {@link ReportingSchuelerLeistungsdaten}.
	 */
	private static FilterRegistry<ReportingSchuelerLeistungsdaten> filterRegistryReportingSchuelerLeistungsdaten() {
		final FilterRegistry<ReportingSchuelerLeistungsdaten> reg = new FilterRegistry<>();

		// Grundlegende Attribute
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::id);
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::abifach);
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::aufZeugnis);
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::fehlstundenGesamt);
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::fehlstundenUnentschuldigt);
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::geholtJahrgangAbgeschlossen);
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::gewichtungAllgemeinbildend);
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::istEpochal);
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::istGemahnt);
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::istZP10oderZKEF);
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::kursart);
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::mahndatum);
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::schulnummerExtern);
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::textFachbezogeneLernentwicklung);
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::umfangLernstandsbericht);
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::wochenstundenSchueler);

		// Noten-Attribute
		reg.registriereAttribut(ReportingTypesUtils.methodeToString(ReportingSchuelerLeistungsdaten::note),
				l -> (l.note() == null) ? null : l.note().name());
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::noteKuerzel);
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::notePunkte);
		reg.registriereAttribut(ReportingTypesUtils.methodeToString(ReportingSchuelerLeistungsdaten::noteBerufsabschluss),
				l -> (l.noteBerufsabschluss() == null) ? null : l.noteBerufsabschluss().name());
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::noteBerufsabschlussKuerzel);
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::noteBerufsabschlussPunkte);
		reg.registriereAttribut(ReportingTypesUtils.methodeToString(ReportingSchuelerLeistungsdaten::noteQuartal),
				l -> (l.noteQuartal() == null) ? null : l.noteQuartal().name());
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::noteQuartalKuerzel);
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::noteQuartalPunkte);

		// Zuweisungs-Attribut (Kursart der Schüler-Zuweisung)
		reg.registriereAttribut(ReportingSchuelerLeistungsdaten::zuweisungKursart);

		// Attribute des zugeordneten Faches mit Prefix "fach" aus der Fach-Registry übernehmen.
		reg.importiereRegistryEintraege(ReportingTypesUtils.methodeToString(ReportingSchuelerLeistungsdaten::fach) + ".",
				FilterRegistryReportingFach.filterRegistry(), ReportingSchuelerLeistungsdaten::fach);

		return reg;
	}
}
