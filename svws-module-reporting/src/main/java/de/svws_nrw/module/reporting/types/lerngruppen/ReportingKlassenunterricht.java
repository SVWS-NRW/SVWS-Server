package de.svws_nrw.module.reporting.types.lerngruppen;

import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLeistungsdaten;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Konkrete Klasse für einen Klassenunterricht im Rahmen des Reportings.
 * Repräsentiert Unterricht in einem Fach, der im Klassenverband stattfindet, d. h. ohne einen Kurseintrag.
 */
public class ReportingKlassenunterricht extends ReportingLerngruppe {

	/** Der Lehrer, der diesen Unterricht bewertet. */
	private final ReportingLehrer bewertenderLehrer;

	/** Der Jahrgang der Klasse, in der dieser Unterricht stattfindet. */
	private final ReportingJahrgang jahrgang;

	/** Eine Map, die die Leistungsdaten zu diesem Unterricht zur ID des Schülers speichert. */
	private final Map<Long, ReportingSchuelerLeistungsdaten> mapSchuelerLeistungsdaten = new HashMap<>();

	/**
	 * Erstellt einen Klassenunterricht aus einer Klasse und Fachinformationen.
	 *
	 * @param klasse Die Klasse, in der der Unterricht stattfindet
	 * @param fach Das unterrichtete Fach
	 * @param bewertenderLehrer Der Lehrer, der diesen Unterricht bewertet.
	 * @param fachlehrer Liste der Fachlehrer des Klassenunterrichts.
	 * @param wochenstundenFachlehrer Map der Wochenstunden pro Lehrer
	 * @param schueler Die Liste der Schüler, die dem Unterricht zugeordnet werden sollen. Ist die Liste null/empty, dann werden alle Schüler der Klasse gesetzt.
	 * @param wochenstundenSchueler Wochenstunden für die Schüler
	 * @param mapSchuelerLeistungsdaten Eine Map, die die Leistungsdaten zu diesem Unterricht zur ID des Schülers speichert
	 */
	public ReportingKlassenunterricht(final @NotNull ReportingKlasse klasse, final @NotNull ReportingFach fach, final ReportingLehrer bewertenderLehrer,
			final List<ReportingLehrer> fachlehrer, final Map<Long, Double> wochenstundenFachlehrer, final List<ReportingSchueler> schueler,
			final int wochenstundenSchueler, final Map<Long, ReportingSchuelerLeistungsdaten> mapSchuelerLeistungsdaten) {
		super(klasse, fach, fachlehrer, wochenstundenFachlehrer, schueler, wochenstundenSchueler);

		this.bewertenderLehrer = bewertenderLehrer;
		this.jahrgang = klasse.jahrgang();

		if (mapSchuelerLeistungsdaten != null)
			this.mapSchuelerLeistungsdaten.putAll(mapSchuelerLeistungsdaten);

		this.schueler().forEach(s -> this.mapSchuelerLeistungsdaten.computeIfAbsent(s.id(),
				id -> s.aktiverLernabschnittInSchuljahresabschnitt(this.schuljahresabschnitt())
						.leistungsdatenKlassenunterrichtZurIdFachIdLehrer(bewertenderLehrer.id(), fach.id())));

		final List<Long> idsSchueler = this.schueler().stream().map(ReportingSchueler::id).toList();
		this.mapSchuelerLeistungsdaten.keySet().removeIf(id -> !idsSchueler.contains(id));
	}

	/**
	 * Der Lehrer, der gemäß Leistungsdaten den Unterricht bewertet. Da Klassenunterrichte nur über die Leistungsdaten definiert sind, ist dies auch der erste
	 * Lehrer aus der Liste der Fachlehrer.
	 *
	 * @return Der bewertende Lehrer.
	 */
	public ReportingLehrer bewertenderLehrer() {
		return bewertenderLehrer;
	}

	/**
	 * Gibt den Jahrgang der Klasse zurück, in der dieser Unterricht stattfindet.
	 *
	 * @return Liste mit dem Jahrgang der Klasse (oder leere Liste)
	 */
	@Override
	public List<ReportingJahrgang> jahrgaenge() {
		return (jahrgang == null) ? List.of() : List.of(jahrgang);
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
