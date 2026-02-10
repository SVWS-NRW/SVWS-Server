package de.svws_nrw.module.reporting.types.lerngruppen;

import java.util.HashMap;
import java.util.Map;

import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLeistungsdaten;
import jakarta.validation.constraints.NotNull;

/**
 * Konkrete Klasse für einen Unterricht eines Kurses im Rahmen des Reportings.
 * Repräsentiert Unterricht in einem Kurs.
 */
public class ReportingKursunterricht extends ReportingKurs {

	/** Der Lehrer, der diesen Unterricht bewertet. */
	private final ReportingLehrer bewertenderLehrer;

	/** Eine Map, die die Leistungsdaten zu diesem Unterricht zur ID des Schülers speichert. */
	private final Map<Long, ReportingSchuelerLeistungsdaten> mapSchuelerLeistungsdaten = new HashMap<>();

	/**
	 * Erstellt einen Kursunterricht aus einer Klasse und Fachinformationen.
	 *
	 * @param kurs Der Kurs, in dem der Unterricht stattfindet
	 * @param bewertenderLehrer Der Lehrer, der diesen Unterricht bewertet.
	 * @param mapSchuelerLeistungsdaten Eine Map, die die Leistungsdaten zu diesem Unterricht zur ID des Schülers speichert.
	 */
	public ReportingKursunterricht(final @NotNull ReportingKurs kurs, final ReportingLehrer bewertenderLehrer,
			final Map<Long, ReportingSchuelerLeistungsdaten> mapSchuelerLeistungsdaten) {
		super(kurs.id(), kurs.schuljahresabschnitt(), kurs.kuerzel(), kurs.fach(), kurs.kurslehrer(), kurs.wochenstundenLehrkraefte(), kurs.schueler(),
				kurs.sortierung(), kurs.wochenstunden(), kurs.bezeichnungZeugnis(), kurs.istEpochalunterricht(), kurs.istSichtbar(), kurs.jahrgaenge(),
				kurs.kursartAllg(), kurs.schienen(), kurs.idsSchueler(), kurs.schulnummer());

		this.bewertenderLehrer = bewertenderLehrer;
		if (mapSchuelerLeistungsdaten != null)
			this.mapSchuelerLeistungsdaten.putAll(mapSchuelerLeistungsdaten);

		this.schueler().forEach(s -> this.mapSchuelerLeistungsdaten.computeIfAbsent(s.id(),
				id -> s.aktiverLernabschnittInSchuljahresabschnitt(this.schuljahresabschnitt())
						.leistungsdatenZurIdKurs(this.id())));

		this.mapSchuelerLeistungsdaten.keySet().removeIf(id -> !this.idsSchueler().contains(id));
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

	/**
	 * Liefert die Leistungsdaten eines Schülers anhand der übergebenen Schüler-ID zurück.
	 *
	 * @param idSchueler Die eindeutige ID des Schülers, dessen Leistungsdaten abgefragt werden sollen. Wenn null übergeben wird, wird null zurückgegeben.
	 *
	 * @return Die Leistungsdaten des Schülers als {@link ReportingSchuelerLeistungsdaten} oder null wenn keine Daten vorhanden sind oder null übergeben wurde.
	 */
	@Override
	public ReportingSchuelerLeistungsdaten leistungsdatenBySchueler(final Long idSchueler) {
		if ((idSchueler == null) || mapSchuelerLeistungsdaten.isEmpty())
			return null;
		return this.mapSchuelerLeistungsdaten.get(idSchueler);
	}

}
