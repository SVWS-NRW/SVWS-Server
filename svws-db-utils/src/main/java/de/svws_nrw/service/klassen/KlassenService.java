package de.svws_nrw.service.klassen;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.klassen.KlassenMapper;
import de.svws_nrw.mapper.klassen.KlassenMappingContext;
import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.ws.rs.core.Response;

/**
 * Service-Klasse für den lesenden Zugriff auf Klassen.
 * <p>
 * Der Service bestimmt die Klassen und prüft die Eingaben. Das Laden der Daten, die für das Mapping benötigt werden
 * (Klassenleitungen, Schüler, Schuljahresabschnitte, Nachbarklassen), übernimmt der {@link KlassenMappingContextLoader}.
 */
public final class KlassenService {

	private static final String ID_NICHT_NULL = "Die ID für die Klasse darf nicht null sein.";
	private static final String IDS_NICHT_NULL = "Die IDs für die Klassen dürfen nicht null sein.";
	private static final String KLASSE_NICHT_GEFUNDEN = "Keine Klasse zur ID %d gefunden.";
	private static final String KLASSEN_NICHT_ALLE_GEFUNDEN = "Es wurden nicht alle Klassen zu den IDs gefunden.";
	private static final String ABSCHNITT_ID_NICHT_NULL = "Die ID für den Schuljahresabschnitt darf nicht null sein.";

	private final KlassenRepository klassenRepository;
	private final SchuljahresabschnittService schuljahresabschnittService;
	private final KlassenMappingContextLoader kontextLoader;
	private final KlassenMapper klassenMapper;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param klassenRepository             das Repository für die Klassen
	 * @param schuljahresabschnittService   der Service für die Schuljahresabschnitte
	 * @param kontextLoader                 der Loader für die Daten, die für das Mapping der Klassen benötigt werden
	 * @param klassenMapper                 der Mapper für die Konvertierung der Klassen
	 */
	public KlassenService(
			final KlassenRepository klassenRepository,
			final SchuljahresabschnittService schuljahresabschnittService,
			final KlassenMappingContextLoader kontextLoader,
			final KlassenMapper klassenMapper) {
		this.klassenRepository = klassenRepository;
		this.schuljahresabschnittService = schuljahresabschnittService;
		this.kontextLoader = kontextLoader;
		this.klassenMapper = klassenMapper;
	}

	/**
	 * Bestimmt die Daten einer Klasse inklusive der Schüler der Klasse.
	 *
	 * @param id   die ID der Klasse
	 *
	 * @return die Daten der Klasse
	 */
	public KlassenDaten getById(final Long id) {
		return toKlassenDaten(List.of(getDtoById(id)), true).getFirst();
	}

	/**
	 * Bestimmt die Daten aller Klassen eines Schuljahresabschnitts.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param mitSchueler              gibt an, ob die Schüler der Klassen mitgeliefert werden sollen
	 *
	 * @return die Daten der Klassen, bzw. eine leere Liste, wenn der Schuljahresabschnitt keine Klassen enthält
	 */
	public List<KlassenDaten> getListBySchuljahresabschnitt(final Long idSchuljahresabschnitt, final boolean mitSchueler) {
		if (idSchuljahresabschnitt == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, ABSCHNITT_ID_NICHT_NULL);
		}
		// Der Abschnitt wird auch dann geprüft, wenn er keine Klassen enthält. Der Loader lädt ihn später erneut, was durch den Cache der Verbindung nichts kostet.
		schuljahresabschnittService.getById(idSchuljahresabschnitt);
		return toKlassenDaten(klassenRepository.getListBySchuljahresabschnitt(idSchuljahresabschnitt), mitSchueler);
	}

	/**
	 * Bestimmt die Daten der Klassen zu den übergebenen IDs inklusive der Schüler der Klassen. Die Klassen werden in der
	 * Reihenfolge der IDs zurückgegeben, wobei doppelte IDs nur einmal berücksichtigt werden.
	 *
	 * @param ids   die IDs der Klassen
	 *
	 * @return die Daten der Klassen mit Schülern, bzw. eine leere Liste, wenn keine IDs übergeben wurden
	 */
	public List<KlassenDaten> getListByIdsMitSchueler(final List<Long> ids) {
		return toKlassenDaten(getDtosByIds(ids), true);
	}

	/**
	 * Bestimmt die Klasse zu der ID.
	 *
	 * @param id   die ID der Klasse
	 *
	 * @return die Klasse
	 */
	private DTOKlassen getDtoById(final Long id) {
		if (id == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, ID_NICHT_NULL);
		}
		return klassenRepository.findById(id)
				.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, KLASSE_NICHT_GEFUNDEN.formatted(id)));
	}

	/**
	 * Bestimmt die Klassen zu den übergebenen IDs. Die Klassen werden in der Reihenfolge der IDs zurückgegeben,
	 * wobei doppelte IDs nur einmal berücksichtigt werden.
	 *
	 * @param ids   die IDs der Klassen
	 *
	 * @return die Klassen, bzw. eine leere Liste, wenn keine IDs übergeben wurden
	 */
	private List<DTOKlassen> getDtosByIds(final List<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, IDS_NICHT_NULL);
		}
		final var idsDistinct = new LinkedHashSet<>(ids);
		if (idsDistinct.isEmpty()) {
			return Collections.emptyList();
		}
		final Map<Long, DTOKlassen> klassen = klassenRepository.findMapByIds(idsDistinct);
		if (!klassen.keySet().containsAll(idsDistinct)) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, KLASSEN_NICHT_ALLE_GEFUNDEN);
		}
		return idsDistinct.stream()
				.map(klassen::get)
				.toList();
	}

	/**
	 * Konvertiert die Klassen in die Core-DTOs, wobei die benötigten Daten für alle Klassen gemeinsam geladen werden.
	 *
	 * @param klassen       die Klassen
	 * @param mitSchueler   gibt an, ob die Schüler der Klassen mitgeliefert werden sollen
	 *
	 * @return die Core-DTOs in der Reihenfolge der übergebenen Klassen
	 */
	private List<KlassenDaten> toKlassenDaten(final List<DTOKlassen> klassen, final boolean mitSchueler) {
		final Map<Long, KlassenMappingContext> kontexte = kontextLoader.load(klassen, mitSchueler);
		return klassen.stream()
				.map(klasse -> klassenMapper.toKlassenDaten(klasse, kontexte.get(klasse.ID)))
				.toList();
	}

}
