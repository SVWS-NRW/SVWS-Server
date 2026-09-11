package de.svws_nrw.service.uv.zeitraster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.uv.UvZeitraster;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.db.dto.current.uv.DTOUvZeitraster;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.zeitraster.UvZeitrasterRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Zeitraster.
 */
public final class UvZeitrasterService {

	private final UvZeitrasterRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 * @param uvZeitrasterRepository   das Repository
	 */
	public UvZeitrasterService(final UvZeitrasterRepository uvZeitrasterRepository) {
		this.repository = uvZeitrasterRepository;
	}

	private static UvZeitraster toApi(final DTOUvZeitraster dto) {
		final var daten = new UvZeitraster();
		daten.id = dto.ID;
		daten.gueltigVon = dto.GueltigVon;
		daten.gueltigBis = dto.GueltigBis;
		daten.bezeichnung = dto.Bezeichnung;
		return daten;
	}

	/**
	 * Ermittelt das UV-Zeitraster anhand der ID.
	 * @param id   die ID
	 * @return das UV-Zeitraster
	 */
	public UvZeitraster get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein UvZeitraster mit der ID %d gefunden.".formatted(id));
		}
		return list.getFirst();
	}

	/**
	 * Ermittelt die UV-Zeitraster anhand der IDs.
	 * @param ids   die IDs
	 * @return die Liste
	 */
	public List<UvZeitraster> getList(final Collection<Long> ids) {
		final var result = repository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvZeitraster zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		return result.stream().map(UvZeitrasterService::toApi).toList();
	}

	/**
	 * Liefert alle UV-Zeitraster.
	 *
	 * @return die Liste aller UV-Zeitraster
	 */
	public List<UvZeitraster> getAll() {
		return repository.getAll().stream().map(UvZeitrasterService::toApi).toList();
	}

	/**
	 * Erstellt ein neues UV-Zeitraster.
	 * @param createRequest   der Create-Request
	 * @return das neue UV-Zeitraster
	 */
	public UvZeitraster create(final UvZeitrasterCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue UV-Zeitraster.
	 * @param createRequests   die Create-Requests
	 * @return die neuen UV-Zeitraster
	 */
	public List<UvZeitraster> createMultiple(final Collection<UvZeitrasterCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			long nextId = repository.getNextID();
			final List<DTOUvZeitraster> entities = new ArrayList<>();
			for (final UvZeitrasterCreateRequest request : createRequests) {
				if ((request.gueltigVon == null) || !DateUtils.isValidDate(request.gueltigVon)) {
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Das Attribut gueltigVon hat einen ungültigen Datumswert: %s".formatted(request.gueltigVon));
				}
				if ((request.gueltigBis != null) && !DateUtils.isValidDate(request.gueltigBis)) {
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Das Attribut gueltigBis hat einen ungültigen Datumswert: %s".formatted(request.gueltigBis));
				}
				final var neu = new DTOUvZeitraster(nextId++, request.gueltigVon, request.bezeichnung);
				neu.GueltigBis = request.gueltigBis;
				checkBeforePersist(neu);
				entities.add(neu);
			}
			repository.update(entities);
			repository.flush();
			return getList(entities.stream().map(e -> e.ID).toList());
		});
	}

	/**
	 * Führt einen Patch aus.
	 * @param patch   der Patch
	 * @return das gepatchte UV-Zeitraster
	 */
	public UvZeitraster patch(final UvZeitrasterPatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches aus.
	 * @param patches   die Patches
	 * @return die Liste der gepatchten UV-Zeitraster
	 */
	public List<UvZeitraster> patchMultiple(final Collection<UvZeitrasterPatchRequest> patches) {
		if (patches.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			final var ids = patches.stream().map(p -> p.id).toList();
			final var entities = repository.findListByIds(ids);
			if (entities.size() != patches.size()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle angefragten Datensätze konnten gefunden werden.");
			}
			for (final var p : patches) {
				final var entity = repository.getById(p.id);
				applyPatch(entity, p);
				checkBeforePersist(entity);
			}
			repository.update(entities);
			repository.flush();
			return getList(ids);
		});
	}

	private static void applyPatch(final DTOUvZeitraster dto, final UvZeitrasterPatchRequest patch) {
		patch.bezeichnung.ifPresent(val -> dto.Bezeichnung = val);
		patch.gueltigVon.ifPresent(val -> {
			if ((val == null) || !DateUtils.isValidDate(val)) {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Das Attribut gueltigVon hat einen ungültigen Datumswert: %s".formatted(val));
			}
			dto.GueltigVon = val;
		});
		patch.gueltigBis.ifPresent(val -> {
			if ((val != null) && !DateUtils.isValidDate(val)) {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Das Attribut gueltigBis hat einen ungültigen Datumswert: %s".formatted(val));
			}
			dto.GueltigBis = val;
		});
	}

	/**
	 * Löscht das UV-Zeitraster.
	 * @param id   die ID
	 * @return das gelöschte UV-Zeitraster
	 */
	public UvZeitraster delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere UV-Zeitraster.
	 * @param ids   die IDs
	 * @return die Liste der gelöschten UV-Zeitraster
	 */
	public List<UvZeitraster> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = repository.findListByIds(ids);
			final var result = entities.stream().map(UvZeitrasterService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

	private static void checkBeforePersist(final DTOUvZeitraster dto) {
		if ((dto.GueltigBis != null) && (dto.GueltigVon.compareTo(dto.GueltigBis) > 0)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Datum 'gueltigBis' darf nicht vor dem Datum 'gueltigVon' liegen.");
		}
	}

}
