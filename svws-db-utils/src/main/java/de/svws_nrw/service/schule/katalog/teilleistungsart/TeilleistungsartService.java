package de.svws_nrw.service.schule.katalog.teilleistungsart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.kataloge.Teilleistungsart;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.data.util.ValidationUtils;
import de.svws_nrw.db.dto.current.schild.schueler.DTOTeilleistungsarten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.schule.kataloge.teilleistungsart.TeilleistungsartRepository;
import jakarta.ws.rs.core.Response;

public final class TeilleistungsartService {

	private static final String CONSTRAINT_BEZEICHNUNG_MESSAGE = "Die Bezeichnung %s wird bereits verwendet.";

	/**
	 *  Dataaccess per Repository
	 */
	private final TeilleistungsartRepository repository;

	/**
	 * Initialisierung eines neuen Services
	 *
	 * @param repository {@link TeilleistungsartRepository}
	 */
	public TeilleistungsartService(final TeilleistungsartRepository repository) {
		this.repository = repository;
	}

	/**
	 * Liefert system-bekannte {@link Teilleistungsart}en
	 *
	 * @return Liste von DTO's
	 */
	public List<Teilleistungsart> getAll() {
		final var entities = repository.getAll();
		final var ids = entities.stream().map(e -> e.ID).toList();
		final var referencedIds = repository.getReferencedIds(ids);

		return entities.stream()
				.map(e -> toApi(e, referencedIds))
				.toList();
	}

	/**
	 * Speichert eine Teilleistungsart ab und gibt die gespeicherte Resource zurück
	 *
	 * @param input {@link Teilleistungsart}
	 * @return erstellte {@link Teilleistungsart}
	 */
	public Teilleistungsart create(final TeilleistungsartCreateRequest input) {
		return TransactionSupport.transactional(() -> {
			validateCreate(input);
			final var persisted = repository.create(createDomain(input));
			return toApi(persisted);
		});

	}

	/**
	 * Apply Partial Update.
	 *
	 * @param id id der entity
	 * @param patch partielles update
	 * @return aktualisierte {@link Teilleistungsart}
	 */
	public Teilleistungsart patch(final long id, final TeilleistungsartPatchRequest patch) {
		return TransactionSupport.transactional(() -> applyPatch(id, patch));
	}

	private Teilleistungsart applyPatch(final long id, final TeilleistungsartPatchRequest patch) {
		final DTOTeilleistungsarten persisted = repository.getById(id);
		validatePatch(persisted, patch);
		mapPatch(persisted, patch);

		return toApi(persisted);

	}

	/**
	 * Löscht alle nicht referenzierten {@link Teilleistungsart} und gibt ein Log als {@link SimpleOperationResponse} zurück.
	 * Referenzierte Ressourcen werden hierbei nicht gelöscht, sondern in den Logs markiert.
	 *
	 * @param idsToDelete Liste zu löschender {@link Teilleistungsart} anhand der ID.
	 * @return Liste {@link SimpleOperationResponse} - Aktions-logs als Response
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() -> {
			final var referencedIds = repository.getReferencedIds(idsToDelete);
			final var entitiesToDelete = repository.findListByIds(idsToDelete);


			final List<SimpleOperationResponse> referencedLogs = createReferencedLogs(entitiesToDelete, referencedIds);
			final List<SimpleOperationResponse> unreferencedLogs =
					deleteUnreferenced(entitiesToDelete, referencedIds).stream()
							.map(del -> createResponseLog(del.ID, true, null))
							.toList();

			final List<SimpleOperationResponse> resultLogs = new ArrayList<>(referencedLogs);
			resultLogs.addAll(unreferencedLogs);
			resultLogs.sort(Comparator.comparingLong(o -> o.id));

			return resultLogs;
		});
	}

	private List<SimpleOperationResponse> createReferencedLogs(final List<DTOTeilleistungsarten> entitiesToDelete, final Set<Long> referencedIds) {

		return entitiesToDelete.stream()
				.filter(t -> referencedIds.contains(t.ID))
				.map(t -> createResponseLog(t.ID, false, String.format("Teilleistungsart mit der Bezeichnung %s ist referenziert", t.Bezeichnung)))
				.toList();
	}

	private static SimpleOperationResponse createResponseLog(final long id, final boolean success, final String message) {
		final var log = new SimpleOperationResponse();
		log.id = id;
		log.success = success;
		if (message != null) {
			log.log = Collections.singletonList(message);
		}

		return log;
	}

	private List<DTOTeilleistungsarten> deleteUnreferenced(final List<DTOTeilleistungsarten> entitiesToCheck, final Set<Long> referencedIds) {
		final var unreferencedEntities = entitiesToCheck.stream()
				.filter(e -> !referencedIds.contains(e.ID))
				.toList();

		if (unreferencedEntities.isEmpty()) {
			return List.of();
		}

		return repository.delete(unreferencedEntities);
	}


	private void validateCreate(final TeilleistungsartCreateRequest input) {
		final String bezeichnung = input.bezeichnung;

		if (repository.existsBy(bezeichnung)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, CONSTRAINT_BEZEICHNUNG_MESSAGE.formatted(bezeichnung));
		}
	}

	private void validatePatch(final DTOTeilleistungsarten persistedEntity, final TeilleistungsartPatchRequest input) {
		input.bezeichnung.ifPresent(bezeichnung -> {
			if (ValidationUtils.isRelevantUpdate(persistedEntity.Bezeichnung, bezeichnung) && repository.existsBy(bezeichnung)) {
				throw new ApiOperationException(Response.Status.BAD_REQUEST, CONSTRAINT_BEZEICHNUNG_MESSAGE.formatted(bezeichnung));
			}
		});
	}

	private Teilleistungsart toApi(final DTOTeilleistungsarten input) {
		return toApi(input, repository.getReferencedIds(List.of(input.ID)));
	}

	private Teilleistungsart toApi(final DTOTeilleistungsarten input, final Set<Long> referencedIds) {
		final var dto = new Teilleistungsart();
		dto.id = input.ID;
		dto.bezeichnung = input.Bezeichnung;
		dto.istSichtbar = Boolean.TRUE.equals(input.Sichtbar);
		dto.sortierung = Objects.requireNonNullElse(input.Sortierung, 32000);
		dto.referenziertInAnderenTabellen = referencedIds.contains(input.ID);
		return dto;
	}

	private DTOTeilleistungsarten createDomain(final TeilleistungsartCreateRequest input) {
		final var id = repository.getNextID();
		final var entity = new DTOTeilleistungsarten(id);
		entity.Bezeichnung = input.bezeichnung;
		entity.Sichtbar = input.istSichtbar;
		entity.Sortierung = input.sortierung;

		return entity;
	}

	private void mapPatch(final DTOTeilleistungsarten persisted, final TeilleistungsartPatchRequest input) {
		input.bezeichnung.ifPresent(bezeichnung -> persisted.Bezeichnung = bezeichnung);
		input.sortierung.ifPresent(sortierung -> persisted.Sortierung = sortierung);
		input.istSichtbar.ifPresent(sichtbar -> persisted.Sichtbar = sichtbar);
	}
}
