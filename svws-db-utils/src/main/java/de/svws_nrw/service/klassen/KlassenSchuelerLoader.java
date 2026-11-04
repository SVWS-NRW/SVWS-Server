package de.svws_nrw.service.klassen;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.mapper.schueler.SchuelerMapper;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;

/**
 * Lädt die Schüler von Klassen. Die Zuordnung der Schüler zu den Klassen erfolgt über die aktuellen Lernabschnitte
 * (WechselNr = 0). Gelöschte Schüler werden nicht berücksichtigt. Die Schüler einer Klasse sind aufsteigend nach ihrer ID sortiert.
 */
public final class KlassenSchuelerLoader {

	private final SchuelerLernabschnittRepository lernabschnittRepository;
	private final SchuelerRepository schuelerRepository;
	private final SchuelerMapper schuelerMapper;

	/**
	 * Erstellt einen neuen Loader.
	 *
	 * @param lernabschnittRepository   das Repository für die Lernabschnitte der Schüler
	 * @param schuelerRepository        das Repository für die Schüler
	 * @param schuelerMapper            der Mapper für die Konvertierung der Schüler
	 */
	public KlassenSchuelerLoader(final SchuelerLernabschnittRepository lernabschnittRepository, final SchuelerRepository schuelerRepository,
			final SchuelerMapper schuelerMapper) {
		this.lernabschnittRepository = lernabschnittRepository;
		this.schuelerRepository = schuelerRepository;
		this.schuelerMapper = schuelerMapper;
	}

	/**
	 * Bestimmt die nicht gelöschten Schüler der Klassen mit jeweils einer Abfrage für die Zuordnung und für die Schüler.
	 *
	 * @param idsKlassen   die IDs der Klassen
	 *
	 * @return die nach Schüler-ID sortierten Schüler je Klassen-ID, Klassen ohne Schüler sind nicht enthalten
	 */
	public Map<Long, List<Schueler>> load(final List<Long> idsKlassen) {
		final Map<Long, List<Long>> idsSchuelerJeKlasse = lernabschnittRepository.getMapKlassenSchuelerAktuell(idsKlassen);
		final Map<Long, Schueler> schueler = loadNichtGeloeschte(idsSchuelerJeKlasse);
		return idsSchuelerJeKlasse.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream()
						.map(schueler::get)
						.filter(Objects::nonNull)
						.sorted(Comparator.comparingLong((final Schueler s) -> s.id))
						.toList()));
	}

	/**
	 * Lädt alle Schüler der Zuordnung, die nicht gelöscht sind.
	 *
	 * @param idsSchuelerJeKlasse   die Schüler-IDs je Klassen-ID
	 *
	 * @return die Schüler mit der Schüler-ID als Schlüssel
	 */
	private Map<Long, Schueler> loadNichtGeloeschte(final Map<Long, List<Long>> idsSchuelerJeKlasse) {
		final List<Long> idsSchueler = idsSchuelerJeKlasse.values().stream()
				.flatMap(List::stream)
				.distinct()
				.toList();
		return schuelerRepository.findListByIds(idsSchueler).stream()
				.filter(Objects::nonNull)
				.filter(s -> Boolean.FALSE.equals(s.hatLoeschvermerk))
				.collect(Collectors.toMap(s -> s.ID, schuelerMapper::toSchueler));
	}

}
