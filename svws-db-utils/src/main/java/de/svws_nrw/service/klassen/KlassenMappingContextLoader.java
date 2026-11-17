package de.svws_nrw.service.klassen;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.mapper.klassen.KlassenMapper;
import de.svws_nrw.mapper.klassen.KlassenMappingContext;
import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.klassen.KlassenleitungenRepository;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.schule.SchuljahresabschnittService;

/**
 * Lädt die Daten, die der {@link KlassenMapper} für das Mapping von Klassen benötigt, und stellt sie als
 * {@link KlassenMappingContext} bereit. Jede Art von Daten wird für alle Klassen gemeinsam geladen:
 * <ul>
 *   <li>die Klassenleitungen mit einer einzigen Abfrage,</li>
 *   <li>die Schüler (optional) mit einer einzigen Zuordnungs- und einer einzigen Schüler-Abfrage,</li>
 *   <li>die Schuljahresabschnitte mit einer einzigen Abfrage und die Klassen der Nachbarabschnitte je Abschnitt nur einmal und nur, wenn eine Klasse
 *       eine Vorgänger- bzw. Folgeklasse hat.</li>
 * </ul>
 */
public final class KlassenMappingContextLoader {

	private final KlassenRepository klassenRepository;
	private final KlassenleitungenRepository klassenleitungenRepository;
	private final KlassenSchuelerLoader schuelerLoader;
	private final EigeneSchuleService eigeneSchuleService;
	private final SchuljahresabschnittService schuljahresabschnittService;

	/**
	 * Erstellt einen neuen Loader.
	 *
	 * @param klassenRepository             das Repository für die Klassen
	 * @param klassenleitungenRepository    das Repository für die Klassenleitungen
	 * @param schuelerLoader                der Loader für die Schüler der Klassen
	 * @param eigeneSchuleService           der Service für die Daten der eigenen Schule
	 * @param schuljahresabschnittService   der Service für die Schuljahresabschnitte
	 */
	public KlassenMappingContextLoader(
			final KlassenRepository klassenRepository,
			final KlassenleitungenRepository klassenleitungenRepository,
			final KlassenSchuelerLoader schuelerLoader,
			final EigeneSchuleService eigeneSchuleService,
			final SchuljahresabschnittService schuljahresabschnittService) {
		this.klassenRepository = klassenRepository;
		this.klassenleitungenRepository = klassenleitungenRepository;
		this.schuelerLoader = schuelerLoader;
		this.eigeneSchuleService = eigeneSchuleService;
		this.schuljahresabschnittService = schuljahresabschnittService;
	}

	/**
	 * Lädt die Mapping-Kontexte für die übergebenen Klassen.
	 *
	 * @param klassen       die Klassen
	 * @param mitSchueler   gibt an, ob die Schüler der Klassen mitgeladen werden sollen
	 *
	 * @return die Mapping-Kontexte mit der Klassen-ID als Schlüssel
	 */
	public Map<Long, KlassenMappingContext> load(final List<DTOKlassen> klassen, final boolean mitSchueler) {
		if (klassen.isEmpty()) {
			return Collections.emptyMap();
		}
		final var abschnitte = loadAbschnitte(klassen);
		final var klassenNachAbschnitt = loadKlassenDerNachbarabschnitte(klassen, abschnitte);
		final var schulform = eigeneSchuleService.getSchulform();
		final var idsKlassen = klassen.stream()
				.map(k -> k.ID)
				.toList();
		final var klassenleitungen = klassenleitungenRepository.getMapKlassenleitungen(idsKlassen);
		final Map<Long, List<Schueler>> schueler = mitSchueler
				? schuelerLoader.load(idsKlassen)
				: Collections.emptyMap();
		return klassen.stream()
				.collect(Collectors.toMap(
						k -> k.ID,
						k -> {
							final var abschnitt = abschnitte.get(k.Schuljahresabschnitts_ID);
							return new KlassenMappingContext(
									schulform,
									abschnitt,
									klassenNachAbschnitt.get(abschnitt.idVorigerAbschnitt),
									klassenNachAbschnitt.get(abschnitt.idFolgeAbschnitt),
									klassenleitungen.get(k.ID),
									schueler.get(k.ID)
							);
						}));
	}

	/**
	 * Lädt die Schuljahresabschnitte der Klassen mit einer einzigen Abfrage, jeden Abschnitt nur einmal.
	 *
	 * @param klassen   die Klassen
	 *
	 * @return die Schuljahresabschnitte mit der Abschnitts-ID als Schlüssel
	 */
	private Map<Long, Schuljahresabschnitt> loadAbschnitte(final List<DTOKlassen> klassen) {
		final var idsAbschnitte = klassen.stream()
				.map(k -> k.Schuljahresabschnitts_ID)
				.distinct()
				.toList();

		return schuljahresabschnittService.getMapByIds(idsAbschnitte);
	}

	/**
	 * Lädt die Klassen der vorigen und folgenden Abschnitte der Klassen, jeden Nachbarabschnitt nur einmal. Die Klassen des vorigen
	 * Abschnitts werden nur geladen, wenn eine Klasse eine Vorgängerklasse hat, die des folgenden Abschnitts nur, wenn eine Klasse
	 * eine Folgeklasse hat.
	 *
	 * @param klassen      die Klassen
	 * @param abschnitte   die Schuljahresabschnitte der Klassen mit der Abschnitts-ID als Schlüssel
	 *
	 * @return die Klassen mit dem Kürzel als Schlüssel je Abschnitts-ID der Nachbarabschnitte
	 */
	private Map<Long, Map<String, DTOKlassen>> loadKlassenDerNachbarabschnitte(final List<DTOKlassen> klassen,
			final Map<Long, Schuljahresabschnitt> abschnitte) {
		return Stream.concat(
				getIdsNachbarabschnitte(klassen, abschnitte, k -> k.VKlasse != null, a -> a.idVorigerAbschnitt != null, a -> a.idVorigerAbschnitt),
				getIdsNachbarabschnitte(klassen, abschnitte, k -> k.FKlasse != null, a -> a.idFolgeAbschnitt != null, a -> a.idFolgeAbschnitt))
				.distinct()
				.collect(Collectors.toMap(Function.identity(), this::getKlassenByKuerzel));
	}

	private static Stream<Long> getIdsNachbarabschnitte(final List<DTOKlassen> klassen, final Map<Long, Schuljahresabschnitt> abschnitte,
			final Predicate<DTOKlassen> hatNachbarklasse, final Predicate<Schuljahresabschnitt> hatNachbarabschnitt,
			final ToLongFunction<Schuljahresabschnitt> getIdNachbarabschnitt) {
		return klassen.stream()
				.filter(hatNachbarklasse)
				.map(k -> abschnitte.get(k.Schuljahresabschnitts_ID))
				.filter(hatNachbarabschnitt)
				.mapToLong(getIdNachbarabschnitt)
				.boxed();
	}

	/**
	 * Bestimmt die Klassen eines Schuljahresabschnitts mit dem Kürzel als Schlüssel.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 *
	 * @return die Klassen mit dem Kürzel als Schlüssel
	 */
	private Map<String, DTOKlassen> getKlassenByKuerzel(final Long idSchuljahresabschnitt) {
		return klassenRepository.getListBySchuljahresabschnitt(idSchuljahresabschnitt).stream()
				.collect(Collectors.toMap(k -> k.Klasse, Function.identity(), (a, b) -> a));
	}

}
