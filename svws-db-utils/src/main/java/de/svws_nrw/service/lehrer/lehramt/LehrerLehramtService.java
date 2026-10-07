package de.svws_nrw.service.lehrer.lehramt;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.types.lehrer.LehrerLehramtAnerkennung;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramt;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.lehramt.LehrerLehramtMapper;
import de.svws_nrw.mapper.lehrer.lehramt.LehrerLehramtMappingContext;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.lehrer.lehramt.LehrerLehramtRepository;
import de.svws_nrw.service.lehrer.fachrichtung.LehrerFachrichtungService;
import de.svws_nrw.service.lehrer.lehrbefaehigung.LehrerLehrbefaehigungService;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import jakarta.ws.rs.core.Response;

/**
 * Ein Service für den Zugriff auf die Lehrämter von Lehrern
 */
public final class LehrerLehramtService {

	private final LehrerLehramtRepository lehramtRepository;
	private final LehrerRepository lehrerRepository;
	private final LehrerFachrichtungService lehrerFachrichtungenService;
	private final LehrerLehrbefaehigungService lehrerLehrbefaehigungenService;
	private final LehrerLehramtMapper mapper;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param lehramtRepository              {@link LehrerLehramtRepository}
	 * @param lehrerRepository               {@link LehrerRepository}
	 * @param lehrerFachrichtungenService    {@link LehrerFachrichtungService}
	 * @param lehrerLehrbefaehigungenService {@link LehrerLehrbefaehigungService}
	 * @param mapper                         {@link LehrerLehramtMapper}
	 */
	public LehrerLehramtService(
			final LehrerLehramtRepository lehramtRepository,
			final LehrerRepository lehrerRepository,
			final LehrerFachrichtungService lehrerFachrichtungenService,
			final LehrerLehrbefaehigungService lehrerLehrbefaehigungenService,
			final LehrerLehramtMapper mapper
	) {
		this.lehramtRepository = lehramtRepository;
		this.lehrerRepository = lehrerRepository;
		this.lehrerFachrichtungenService = lehrerFachrichtungenService;
		this.lehrerLehrbefaehigungenService = lehrerLehrbefaehigungenService;
		this.mapper = mapper;
	}

	/**
	 * Gibt alle Lehrämter des Lehrers mit der angegebenen ID zurück.
	 *
	 * @param idLehrer   die ID des Lehrers
	 * @return Liste der zugehörigen {@link LehrerLehramtEintrag}-Objekte, leer wenn keine vorhanden oder die ID null ist
	 */
	public List<LehrerLehramtEintrag> getByIdLehrer(final Long idLehrer) {
		if (idLehrer == null) {
			return Collections.emptyList();
		}
		return mapListToApi(lehramtRepository.findByIdsLehrer(List.of(idLehrer)));
	}

	/**
	 * Gibt eine Map mit der Zuordnung der Lehrämter zu den Lehrern mit den übergebenen IDs zurück.
	 *
	 * @param idsLehrer   die IDs der Lehrer
	 * @return die Zuordnung, leer wenn die Liste null oder leer ist
	 */
	public Map<Long, List<LehrerLehramtEintrag>> getMapByIdLehrer(final List<Long> idsLehrer) {
		if ((idsLehrer == null) || idsLehrer.isEmpty()) {
			return Collections.emptyMap();
		}
		return mapListToApi(lehramtRepository.findByIdsLehrer(idsLehrer))
				.stream()
				.collect(Collectors.groupingBy(la -> la.idLehrer));
	}

	/**
	 * Legt ein neues Lehramt an.
	 * Validiert vor dem Anlegen die Existenz der referenzierten IDs.
	 *
	 * @param dto   der {@link LehrerLehramtCreateRequest} mit den Pflichtfeldern
	 * @return der {@link LehrerLehramtEintrag} des neu angelegten Lehramtes
	 * @throws ApiOperationException mit {@code 400 BAD_REQUEST} wenn die IDs unbekannt sind
	 */
	public LehrerLehramtEintrag create(final LehrerLehramtCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			this.validateCreate(dto);
			final var lehramt = mapper.toDomain(dto);
			final var created = lehramtRepository.create(lehramt);
			return mapSingleToApi(created);
		});
	}

	/**
	 * Aktualisiert ein bestehendes Lehramt partiell anhand der im {@link LehrerLehramtPatchRequest}
	 * gesetzten Felder. Felder mit {@code undefined}-Wert bleiben unverändert.
	 *
	 * @param id    die ID des zu aktualisierenden Lehramtes
	 * @param dto   der {@link LehrerLehramtPatchRequest}
	 * @return der aktualisierte {@link LehrerLehramtEintrag}
	 * @throws ApiOperationException mit {@code 400 BAD_REQUEST} wenn die IDs unbekannt sind
	 */
	public LehrerLehramtEintrag patch(final long id, final LehrerLehramtPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var entity = lehramtRepository.getById(id);
			validatePatch(dto);
			mapper.patch(dto, entity);
			return mapSingleToApi(entity);
		});
	}

	/**
	 * Löscht die Lehrämter mit den angegebenen IDs.
	 * Nicht gefundene IDs werden stillschweigend ignoriert.
	 * Jeder Eintrag in der Rückgabeliste enthält die ID und ob die Löschung erfolgreich war.
	 *
	 * @param idsToDelete   Liste der zu löschenden Lehramt-IDs
	 * @return Liste von {@link SimpleOperationResponse}-Einträgen, aufsteigend nach ID sortiert
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() ->
				BulkDeleteUtils.delete(
						idsToDelete,
						lehramtRepository,
						e -> e.id,
						"Lehramt"
				)
		);
	}

	private void validateCreate(final LehrerLehramtCreateRequest dto) {
		validateIdLehrer(dto.idLehrer);
		validateIdLehramt(dto.idKatalogLehramt);
		if (dto.idAnerkennungsgrund != null) {
			validateIdAnerkennungsgrund(dto.idAnerkennungsgrund);
		}
	}

	private void validatePatch(final LehrerLehramtPatchRequest dto) {
		dto.idLehrer.ifPresent(this::validateIdLehrer);
		dto.idKatalogLehramt.ifPresent(this::validateIdLehramt);
		dto.idAnerkennungsgrund.ifPresent(this::validateIdAnerkennungsgrund);
	}

	private void validateIdAnerkennungsgrund(final Long idAnerkennungsgrund) {
		if (LehrerLehramtAnerkennung.data().getEintragByID(idAnerkennungsgrund) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Kein Anerkennungsgrund für die ID %d gefunden.".formatted(idAnerkennungsgrund));
		}
	}

	private void validateIdLehramt(final Long idLehramt) {
		if (LehrerLehramt.data().getEintragByID(idLehramt) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Kein Lehramt für die ID %d gefunden.".formatted(idLehramt));
		}
	}

	private void validateIdLehrer(final Long idLehrer) {
		if (!lehrerRepository.existsById(idLehrer)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Kein Lehrer für die ID %d gefunden.".formatted(idLehrer));
		}
	}

	private LehrerLehramtEintrag mapSingleToApi(final DTOLehrerPersonaldatenLehramt lehramt) {
		final var lehrbefaehigungen = lehrerLehrbefaehigungenService.getByIdLehramt(lehramt.id);
		final var fachrichtungen = lehrerFachrichtungenService.getByIdLehramt(lehramt.id);
		return mapper.toApi(
				lehramt,
				new LehrerLehramtMappingContext(fachrichtungen, lehrbefaehigungen)
		);
	}

	private List<LehrerLehramtEintrag> mapListToApi(final List<DTOLehrerPersonaldatenLehramt> lehraemter) {
		if (lehraemter.isEmpty()) {
			return List.of();
		}
		final var idsLehraemter = lehraemter.stream()
				.map(lehramt -> lehramt.id)
				.toList();
		final var lehrbefaehigungenByIdLehramt = lehrerLehrbefaehigungenService.getLehrerLehrbefaehigungByIdLehramt(idsLehraemter);
		final var fachrichtungenByIdLehramt = lehrerFachrichtungenService.getLehrerFachrichtungenByIdLehramt(idsLehraemter);
		return lehraemter.stream()
				.map(lehramt -> mapper.toApi(
						lehramt,
						new LehrerLehramtMappingContext(
								fachrichtungenByIdLehramt.getOrDefault(lehramt.id, List.of()),
								lehrbefaehigungenByIdLehramt.getOrDefault(lehramt.id, List.of())
						)
				))
				.toList();
	}
}
