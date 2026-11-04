package de.svws_nrw.service.klassen;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
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
 *   <li>die Schuljahresabschnitte und die Klassen der Nachbarabschnitte je Abschnitt nur einmal.</li>
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
		final var klassenNachAbschnitt = loadKlassenDerNachbarabschnitte(abschnitte.values());
		final var schulform = eigeneSchuleService.getSchulform();
		final var idsKlassen = klassen.stream()
				.map(k -> k.ID)
				.toList();
		final var klassenleitungen = klassenleitungenRepository.getMapKlassenleitungen(idsKlassen);
		final Map<Long, List<Schueler>> schueler = mitSchueler ? schuelerLoader.load(idsKlassen) : Collections.emptyMap();
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
	 * Lädt die Schuljahresabschnitte der Klassen, jeden Abschnitt nur einmal.
	 *
	 * @param klassen   die Klassen
	 *
	 * @return die Schuljahresabschnitte mit der Abschnitts-ID als Schlüssel
	 */
	private Map<Long, Schuljahresabschnitt> loadAbschnitte(final List<DTOKlassen> klassen) {
		return klassen.stream()
				.map(k -> k.Schuljahresabschnitts_ID)
				.distinct()
				.collect(Collectors.toMap(Function.identity(), schuljahresabschnittService::getById));
	}

	/**
	 * Lädt die Klassen der vorigen und folgenden Abschnitte der übergebenen Schuljahresabschnitte, jeden Nachbarabschnitt nur einmal.
	 *
	 * @param abschnitte   die Schuljahresabschnitte
	 *
	 * @return die Klassen mit dem Kürzel als Schlüssel je Abschnitts-ID der Nachbarabschnitte
	 */
	private Map<Long, Map<String, DTOKlassen>> loadKlassenDerNachbarabschnitte(final Collection<Schuljahresabschnitt> abschnitte) {
		return abschnitte.stream()
				.flatMap(a -> Stream.of(a.idVorigerAbschnitt, a.idFolgeAbschnitt))
				.filter(Objects::nonNull)
				.distinct()
				.collect(Collectors.toMap(Function.identity(), this::getKlassenByKuerzel));
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
