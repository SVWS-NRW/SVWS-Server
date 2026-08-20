package de.svws_nrw.service.lehrer.lehrbefaehigung;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigung;
import de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigungAnerkennung;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.lehrbefaehigung.LehrerLehrbefaehigungMapper;
import de.svws_nrw.repo.lehrer.lehramt.LehrerLehramtRepository;
import de.svws_nrw.repo.lehrer.lehrbefaehigung.LehrerLehramtLehrbefaehigungRepository;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;

/**
 * Ein Service für den Zugriff auf die Lehrbefähigungen von Lehrern
 */
public final class LehrerLehrbefaehigungService {

	private final LehrerLehramtLehrbefaehigungRepository repo;
	private final LehrerLehramtRepository lehrerLehramtRepository;
	private final LehrerLehrbefaehigungMapper mapper;

	/**
	 * constructor
	 *
	 * @param repo {@link LehrerLehramtLehrbefaehigungRepository}
	 * @param lehrerLehramtRepository {@link LehrerLehramtRepository}
	 * @param mapper {@link LehrerLehrbefaehigungMapper}
	 */
	public LehrerLehrbefaehigungService(final LehrerLehramtLehrbefaehigungRepository repo, final LehrerLehramtRepository lehrerLehramtRepository,
			final LehrerLehrbefaehigungMapper mapper) {
		this.repo = repo;
		this.lehrerLehramtRepository = lehrerLehramtRepository;
		this.mapper = mapper;
	}

	/**
	 * Gibt die Liste aller Lehrbefaehigungseinträge zurück.
	 *
	 * @return Liste aller Lehrbefaehigungseinträge.
	 */
	public List<LehrerLehrbefaehigungEintrag> getAll() {
		final var entities = repo.getAll();
		return entities.stream()
				.map(mapper::toApi)
				.toList();
	}

	/**
	 * Gibt eine Map mit der Zuordnung der Lehrbefaehigung zu den Lehrämtern mit den übergebenen IDs zurück.
	 *
	 * @param idsLehraemter   die IDs der Lehrämter
	 *
	 * @return die Zuordnung
	 */
	public Map<Long, List<LehrerLehrbefaehigungEintrag>> getLehrerLehrbefaehigungByIdLehramt(final Collection<Long> idsLehraemter) {
		return repo.getLehrerLehrbefaehigungByIdLehramt(idsLehraemter)
				.entrySet().stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						entry -> entry.getValue().stream().map(mapper::toApi).toList()
				));
	}

	/**
	 * Gibt alle Lehrbefaehigungen zum Lehramt mit der angegebenen ID zurück.
	 *
	 * @param idLehramt   die ID des Lehramtes
	 * @return Liste der zugehörigen {@link LehrerLehrbefaehigungEintrag}-Objekte, leer wenn keine vorhanden
	 */
	public List<LehrerLehrbefaehigungEintrag> getByIdLehramt(final long idLehramt) {
		return repo.getByIdLehramt(idLehramt).stream()
				.map(mapper::toApi)
				.toList();
	}

	/**
	 * Legt eine neue LehrerLehrbefaehigung an.
	 * Validiert vor dem Anlegen die Existenz der referenzierten Ids.
	 *
	 * @param dto der {@link LehrerLehrbefaehigungCreateRequest} mit den Pflichtfeldern
	 * @return der {@link LehrerLehrbefaehigungEintrag} der neu angelegten LehrerLehrbefaehigung
	 * @throws ApiOperationException mit {@code 400 BAD_REQUEST} wenn die Ids unbekannt sind
	 */
	public LehrerLehrbefaehigungEintrag create(final LehrerLehrbefaehigungCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			this.validateCreate(dto);
			final var lehrbefaehigung = mapper.toDomain(dto);
			final var created = repo.create(lehrbefaehigung);
			return mapper.toApi(created);
		});
	}

	/**
	 * Aktualisiert eine bestehende Lehrbefaehigung partiell anhand der im {@link LehrerLehrbefaehigungPatchRequest}
	 * gesetzten Felder. Felder mit {@code undefined}-Wert bleiben unverändert.
	 *
	 * @param id  die ID
	 * @param dto der {@link LehrerLehrbefaehigungPatchRequest}
	 * @return der aktualisierte {@link LehrerLehrbefaehigungEintrag}
	 * @throws ApiOperationException mit {@code 400 BAD_REQUEST} wenn die Ids unbekannt sind
	 */
	public LehrerLehrbefaehigungEintrag patch(final long id, final LehrerLehrbefaehigungPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var entity = repo.getById(id);
			validatePatch(dto);
			mapper.patch(dto, entity);
			return this.mapper.toApi(entity);
		});
	}

	/**
	 * Löscht die Lehrbefaehigungen mit den angegebenen IDs.
	 * Nicht gefundene IDs werden stillschweigend ignoriert.
	 * Jeder Eintrag in der Rückgabeliste enthält die ID und ob die Löschung erfolgreich war.
	 *
	 * @param idsToDelete Liste der zu löschenden Lehrbefaehigung-IDs
	 * @return Liste von {@link SimpleOperationResponse}-Einträgen, aufsteigend nach ID sortiert
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() ->
				BulkDeleteUtils.delete(
						idsToDelete,
						repo,
						e -> e.id,
						"Lehrbefähigung"
				)
		);
	}


	private void validatePatch(final LehrerLehrbefaehigungPatchRequest dto) {
		dto.idLehramt.ifPresent(this::validateIdLehramt);
		dto.idLehrbefaehigung.ifPresent(this::validateIdLehrbefaehigung);
		dto.idAnerkennungsgrund.ifPresent(this::validateIdAnerkennungsgrund);
	}

	private void validateCreate(final LehrerLehrbefaehigungCreateRequest dto) {
		validateIdLehramt(dto.idLehramt);
		validateIdLehrbefaehigung(dto.idLehrbefaehigung);
		if (dto.idAnerkennungsgrund != null) {
			validateIdAnerkennungsgrund(dto.idAnerkennungsgrund);
		}
	}

	private void validateIdAnerkennungsgrund(final @NotNull Long idAnerkennungsgrund) {
		if (LehrerLehrbefaehigungAnerkennung.data().getEintragByID(idAnerkennungsgrund) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Kein Anerkennungsgrund für die ID %d gefunden.".formatted(idAnerkennungsgrund));
		}
	}

	private void validateIdLehrbefaehigung(final @NotNull Long idLehrbefaehigung) {
		if (LehrerLehrbefaehigung.data().getEintragByID(idLehrbefaehigung) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Keine Lehrbefähigung für die ID %d gefunden.".formatted(idLehrbefaehigung));
		}
	}

	private void validateIdLehramt(final @NotNull Long idLehramt) {
		if (!lehrerLehramtRepository.existsById(idLehramt)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Kein Lehramt für die ID %d gefunden.".formatted(idLehramt));
		}
	}

}
