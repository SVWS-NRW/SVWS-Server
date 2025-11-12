package de.svws_nrw.module.reporting.types.lerngruppen;

import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import jakarta.validation.constraints.NotNull;

/**
 * Konkrete Klasse für einen Unterricht eines Kurses im Rahmen des Reportings.
 * Repräsentiert Unterricht in einem Kurs.
 */
public class ReportingKursunterricht extends ReportingKurs {

	/** Der Lehrer, der diesen Unterricht bewertet. */
	private final ReportingLehrer bewertenderLehrer;


	/**
	 * Erstellt einen Kursunterricht aus einer Klasse und Fachinformationen.
	 *
	 * @param kurs Der Kurs, in dem der Unterricht stattfindet
	 * @param bewertenderLehrer Der Lehrer, der diesen Unterricht bewertet.
	 */
	public ReportingKursunterricht(final @NotNull ReportingKurs kurs, final ReportingLehrer bewertenderLehrer) {
		super(kurs.id(), kurs.schuljahresabschnitt(), kurs.kuerzel(), kurs.fach(), kurs.kurslehrer(), kurs.wochenstundenLehrkraefte(), kurs.schueler(),
				kurs.sortierung(), kurs.wochenstunden(), kurs.bezeichnungZeugnis(), kurs.istEpochalunterricht(), kurs.istSichtbar(), kurs.jahrgaenge(),
				kurs.kursartAllg(), kurs.schienen(), kurs.idsSchueler(), kurs.schulnummer());

		this.bewertenderLehrer = bewertenderLehrer;
	}

	/**
	 * Der Lehrer, der gemäß Leistungsdaten den Unterricht bewertet. Da Klassenunterrichte nur über die Leistungsdaten definiert sind, ist dies der erste
	 * Lehrer aus der Liste der Fachlehrer.
	 *
	 * @return Der bewertende Lehrer.
	 */
	public ReportingLehrer bewertenderLehrer() {
		return this.bewertenderLehrer;
	}
}
