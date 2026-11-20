package de.svws_nrw.service.schueler.kaoa;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schueler.SchuelerKAoADaten;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerKAoADaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.kaoa.SchuelerKAoAMapper;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.kaoa.SchuelerKAoARepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.ws.rs.core.Response.Status;

/**
 * Dieser Service bündelt die Fachlogik für die KAoA-Daten der Schüler. Jeder Eintrag gehört zu einem Lernabschnitt
 * (WechselNr = 0) des Schülers und wird gegen die KAoA-Kataloge geprüft.
 */
public final class SchuelerKAoAService {

	/** Die Wechsel-Nummer des aktuellen Lernabschnitts eines Schuljahresabschnitts. */
	private static final int WECHSEL_NR_AKTUELL = 0;

	private final SchuelerKAoARepository repository;
	private final SchuelerRepository schuelerRepository;
	private final SchuelerLernabschnittRepository lernabschnittRepository;
	private final SchuljahresabschnittService schuljahresabschnittService;
	private final SchuelerKAoAMapper mapper;

	/**
	 * Erstellt einen neuen {@link SchuelerKAoAService}.
	 *
	 * @param repository                    das Repository für die KAoA-Daten der Schüler
	 * @param schuelerRepository            das Repository für die Schüler
	 * @param lernabschnittRepository       das Repository für die Lernabschnitte der Schüler
	 * @param schuljahresabschnittService   der Service für die Schuljahresabschnitte
	 * @param mapper                        der Mapper zwischen Datenbank-DTO und Core-DTO
	 */
	public SchuelerKAoAService(
			final SchuelerKAoARepository repository,
			final SchuelerRepository schuelerRepository,
			final SchuelerLernabschnittRepository lernabschnittRepository,
			final SchuljahresabschnittService schuljahresabschnittService,
			final SchuelerKAoAMapper mapper) {
		this.repository = repository;
		this.schuelerRepository = schuelerRepository;
		this.lernabschnittRepository = lernabschnittRepository;
		this.schuljahresabschnittService = schuljahresabschnittService;
		this.mapper = mapper;
	}

	/**
	 * Bestimmt alle KAoA-Daten eines Schülers über alle seine Lernabschnitte.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die KAoA-Daten sortiert nach ihrer ID, leer wenn keine vorhanden sind
	 */
	public List<SchuelerKAoADaten> getListByIdSchueler(final long idSchueler) {
		assertSchuelerExists(idSchueler);
		final var lernabschnitte = loadLernabschnitteById(idSchueler);
		final var entities = repository.findListByLernabschnitte(lernabschnitte.keySet());
		return toApi(entities, lernabschnitte);
	}

	/**
	 * Legt KAoA-Daten für einen Schüler an. Die Daten werden gegen die KAoA-Kataloge des Schuljahres geprüft.
	 * Die Operation läuft in einer Transaktion.
	 *
	 * @param idSchueler   die ID des Schülers
	 * @param request      die Daten der neuen KAoA-Daten
	 *
	 * @return die angelegten KAoA-Daten
	 */
	public SchuelerKAoADaten create(final long idSchueler, final SchuelerKAoACreateRequest request) {
		return TransactionSupport.transactional(() -> {
			assertSchuelerExists(idSchueler);
			final var abschnitt = schuljahresabschnittService.getById(request.idSchuljahresabschnitt);
			final var entity = createEntity(idSchueler, abschnitt, request);
			repository.create(entity);
			return toApi(entity, abschnitt);
		});
	}

	/**
	 * Ändert KAoA-Daten eines Schülers teilweise (PATCH). Nur die im Request gesetzten Felder werden übernommen. Geprüft wird der
	 * Zustand nach dem Zusammenführen von bestehenden Daten und Patch. Die Operation läuft in einer Transaktion.
	 *
	 * @param idSchueler   die ID des Schülers
	 * @param id           die ID der KAoA-Daten
	 * @param request      die zu ändernden Felder
	 *
	 * @return die aktualisierten KAoA-Daten
	 */
	public SchuelerKAoADaten patch(final long idSchueler, final long id, final SchuelerKAoAPatchRequest request) {
		return TransactionSupport.transactional(() -> {
			final var eintrag = loadEintrag(idSchueler, id);
			final var zielAbschnitt = patchEintrag(idSchueler, eintrag, request);
			return toApi(eintrag.entity(), zielAbschnitt);
		});
	}

	/**
	 * Löscht KAoA-Daten eines Schülers. Die Operation läuft in einer Transaktion.
	 *
	 * @param idSchueler   die ID des Schülers
	 * @param id           die ID der KAoA-Daten
	 *
	 * @return die Antwort mit der ID des gelöschten Eintrags
	 */
	public SimpleOperationResponse delete(final long idSchueler, final long id) {
		return TransactionSupport.transactional(() -> {
			repository.delete(loadEintrag(idSchueler, id).entity());
			return createDeleteResponse(id);
		});
	}

	private Map<Long, DTOSchuelerLernabschnittsdaten> loadLernabschnitteById(final long idSchueler) {
		return lernabschnittRepository.findListBySchuelerIDs(List.of(idSchueler)).stream()
				.collect(Collectors.toMap(l -> l.ID, Function.identity()));
	}

	private DTOSchuelerKAoADaten createEntity(final long idSchueler, final Schuljahresabschnitt abschnitt, final SchuelerKAoACreateRequest request) {
		final var lernabschnitt = getLernabschnitt(idSchueler, abschnitt.id);
		final var daten = mapper.toApi(request);
		SchuelerKAoAValidator.validate(daten, abschnitt.schuljahr);
		return mapper.toDomain(lernabschnitt.ID, daten);
	}

	private Schuljahresabschnitt patchEintrag(final long idSchueler, final KAoAEintrag eintrag, final SchuelerKAoAPatchRequest request) {
		final var aktuellerAbschnitt = schuljahresabschnittService.getById(eintrag.lernabschnitt().Schuljahresabschnitts_ID);
		final var daten = applyPatch(eintrag.entity(), aktuellerAbschnitt, request);
		final var zielAbschnitt = determineZielAbschnitt(request, daten, aktuellerAbschnitt);
		SchuelerKAoAValidator.validate(daten, zielAbschnitt.schuljahr);
		mapper.patch(determineIdLernabschnitt(idSchueler, eintrag, request, zielAbschnitt), daten, eintrag.entity());
		return zielAbschnitt;
	}

	private SchuelerKAoADaten applyPatch(final DTOSchuelerKAoADaten entity, final Schuljahresabschnitt abschnitt, final SchuelerKAoAPatchRequest request) {
		final var daten = toApi(entity, abschnitt);
		mapper.patchDaten(request, daten);
		return daten;
	}

	private Schuljahresabschnitt determineZielAbschnitt(final SchuelerKAoAPatchRequest request, final SchuelerKAoADaten daten,
			final Schuljahresabschnitt aktuellerAbschnitt) {
		if (!isAbschnittGeaendert(request)) {
			return aktuellerAbschnitt;
		}
		return schuljahresabschnittService.getById(daten.idSchuljahresabschnitt);
	}

	private long determineIdLernabschnitt(final long idSchueler, final KAoAEintrag eintrag, final SchuelerKAoAPatchRequest request,
			final Schuljahresabschnitt zielAbschnitt) {
		if (!isAbschnittGeaendert(request)) {
			return eintrag.entity().idLernabschnitt;
		}
		return getLernabschnitt(idSchueler, zielAbschnitt.id).ID;
	}

	private SchuelerKAoADaten toApi(final DTOSchuelerKAoADaten entity, final Schuljahresabschnitt abschnitt) {
		SchuelerKAoAValidator.validateAsdJahrgang(entity.jahrgang, abschnitt.schuljahr);
		return mapper.toApi(entity, abschnitt);
	}

	private static SimpleOperationResponse createDeleteResponse(final long id) {
		final var response = new SimpleOperationResponse();
		response.id = id;
		response.success = true;
		return response;
	}

	private void assertSchuelerExists(final long idSchueler) {
		if (!schuelerRepository.existsById(idSchueler)) {
			throw new ApiOperationException(Status.NOT_FOUND, "Ein Schüler mit der ID %d wurde nicht gefunden.".formatted(idSchueler));
		}
	}

	private KAoAEintrag loadEintrag(final long idSchueler, final long id) {
		final var entity = repository.findById(id).orElseThrow(() -> createEintragNotFoundException(idSchueler, id));
		final var lernabschnitt = lernabschnittRepository.findById(entity.idLernabschnitt)
				.filter(l -> l.Schueler_ID == idSchueler)
				.orElseThrow(() -> createEintragNotFoundException(idSchueler, id));
		return new KAoAEintrag(entity, lernabschnitt);
	}

	private static ApiOperationException createEintragNotFoundException(final long idSchueler, final long id) {
		return new ApiOperationException(Status.NOT_FOUND,
				"Zum Schüler mit der ID %d wurden keine KAoA-Daten mit der ID %d gefunden.".formatted(idSchueler, id));
	}

	private DTOSchuelerLernabschnittsdaten getLernabschnitt(final long idSchueler, final long idSchuljahresabschnitt) {
		return lernabschnittRepository.findListBySchuelerAndSchuljahresabschnitt(idSchueler, idSchuljahresabschnitt).stream()
				.filter(l -> Objects.equals(l.WechselNr, WECHSEL_NR_AKTUELL))
				.findFirst()
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND,
						"Zum Schüler mit der ID %d wurden keine Lernabschnittsdaten im Schuljahresabschnitt mit der ID %d gefunden."
								.formatted(idSchueler, idSchuljahresabschnitt)));
	}

	private static boolean isAbschnittGeaendert(final SchuelerKAoAPatchRequest request) {
		return request.idSchuljahresabschnitt.isPresent();
	}

	private List<SchuelerKAoADaten> toApi(final List<DTOSchuelerKAoADaten> entities, final Map<Long, DTOSchuelerLernabschnittsdaten> lernabschnitte) {
		if (entities.isEmpty()) {
			return List.of();
		}
		final var abschnittByLernabschnitt = loadAbschnittByLernabschnitt(entities, lernabschnitte);
		return entities.stream()
				.map(e -> toApi(e, abschnittByLernabschnitt.get(e.idLernabschnitt)))
				.sorted(Comparator.comparingLong(k -> k.id))
				.toList();
	}

	private Map<Long, Schuljahresabschnitt> loadAbschnittByLernabschnitt(final List<DTOSchuelerKAoADaten> entities,
			final Map<Long, DTOSchuelerLernabschnittsdaten> lernabschnitte) {
		final var abschnitte = schuljahresabschnittService.getList().stream()
				.collect(Collectors.toMap(a -> a.id, Function.identity()));
		return entities.stream()
				.map(e -> e.idLernabschnitt)
				.distinct()
				.collect(Collectors.toMap(
						Function.identity(),
						idLernabschnitt -> getAbschnitt(abschnitte, lernabschnitte.get(idLernabschnitt).Schuljahresabschnitts_ID)));
	}

	private static Schuljahresabschnitt getAbschnitt(final Map<Long, Schuljahresabschnitt> abschnitte, final long idSchuljahresabschnitt) {
		final var abschnitt = abschnitte.get(idSchuljahresabschnitt);
		if (abschnitt == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Schuljahresabschnitt mit der ID %d gefunden.".formatted(idSchuljahresabschnitt));
		}
		return abschnitt;
	}

	/**
	 * Ein KAoA-Eintrag zusammen mit dem Lernabschnitt, zu dem er gehört.
	 *
	 * @param entity          die KAoA-Daten aus der Datenbank
	 * @param lernabschnitt   der Lernabschnitt, zu dem die KAoA-Daten gehören
	 */
	private record KAoAEintrag(DTOSchuelerKAoADaten entity, DTOSchuelerLernabschnittsdaten lernabschnitt) {
	}

}
