package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import de.svws_nrw.module.reporting.filterung.FilterRegistry;
import de.svws_nrw.module.reporting.filterung.ReportingFilterung;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingSchuelerLeistungsdaten}: hält die Filterkonfiguration des Reporting-Typs (Registry).
 * Die fertige {@link ReportingFilterung}-Instanz wird über {@link ReportingSchuelerLeistungsdaten#FILTER} verwendet.
 */
public final class ReportingSchuelerLeistungsdatenFilter {

	/** Die Filterkonfiguration für {@link ReportingSchuelerLeistungsdaten}. */
	public static final ReportingFilterung<ReportingSchuelerLeistungsdaten> FILTER =
			ReportingFilterung.<ReportingSchuelerLeistungsdaten>builder()
					.registry(buildRegistry())
					.build();

	private ReportingSchuelerLeistungsdatenFilter() {
		throw new IllegalStateException("Begleit-Klasse zur Filterung von ReportingSchuelerLeistungsdaten. Initialisierung nicht möglich.");
	}

	private static FilterRegistry<ReportingSchuelerLeistungsdaten> buildRegistry() {
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
				ReportingFach.FILTER.registry(), ReportingSchuelerLeistungsdaten::fach);

		return reg;
	}
}
