package de.svws_nrw.service.uv.stundentafeln;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.uv.UvStundentafel;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.db.dto.current.uv.DTOUvStundentafel;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.stundentafeln.UvStundentafelRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Stundentafeln (Tabelle UV_Stundentafeln).
 */
public final class UvStundentafelService {

	private final UvStundentafelRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 * @param uvStundentafelRepository   das Repository
	 */
	public UvStundentafelService(final UvStundentafelRepository uvStundentafelRepository) {
		this.repository = uvStundentafelRepository;
	}

	private static UvStundentafel toApi(final DTOUvStundentafel dto) {
		final var daten = new UvStundentafel();
		daten.id = dto.ID;
		daten.idJahrgang = dto.Jahrgang_ID;
		daten.gueltigVon = dto.GueltigVon;
		daten.gueltigBis = dto.GueltigBis;
		daten.bezeichnung = dto.Bezeichnung;
		daten.beschreibung = dto.Beschreibung;
		return daten;
	}

	/**
	 * Ermittelt die UV-Stundentafel anhand der übergebenen ID.
	 * @param id   die ID
	 * @return die UV-Stundentafel
	 */
	public UvStundentafel get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine UvStundentafel mit der ID %d gefunden.".formatted(id));
		}
		return list.getFirst();
	}

	/**
	 * Ermittelt die UV-Stundentafeln anhand der übergebenen IDs.
	 * @param ids   die IDs
	 * @return die Liste der UV-Stundentafeln
	 */
	public List<UvStundentafel> getList(final Collection<Long> ids) {
		final var result = repository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvStundentafeln zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		return result.stream().map(UvStundentafelService::toApi).toList();
	}

	/**
	 * Liefert alle UV-Stundentafeln.
	 *
	 * @return die Liste aller UV-Stundentafeln
	 */
	public List<UvStundentafel> getAll() {
		return repository.getAll().stream().map(UvStundentafelService::toApi).toList();
	}

	/**
	 * Erstellt eine neue UV-Stundentafel.
	 * @param createRequest   der Create-Request
	 * @return die neue UV-Stundentafel
	 */
	public UvStundentafel create(final UvStundentafelCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue UV-Stundentafeln.
	 * @param createRequests   die Create-Requests
	 * @return die neuen UV-Stundentafeln
	 */
	public List<UvStundentafel> createMultiple(final Collection<UvStundentafelCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			long nextId = repository.getNextID();
			final List<DTOUvStundentafel> entities = new ArrayList<>();
			for (final UvStundentafelCreateRequest request : createRequests) {
				if ((request.gueltigVon == null) || !DateUtils.isValidDate(request.gueltigVon)) {
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Das Attribut gueltigVon hat einen ungültigen Datumswert: %s".formatted(request.gueltigVon));
				}
				if ((request.gueltigBis != null) && !DateUtils.isValidDate(request.gueltigBis)) {
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Das Attribut gueltigBis hat einen ungültigen Datumswert: %s".formatted(request.gueltigBis));
				}
				final var neu = new DTOUvStundentafel(nextId++, request.idJahrgang, request.bezeichnung, request.gueltigVon);
				neu.GueltigBis = request.gueltigBis;
				neu.Beschreibung = request.beschreibung;
				checkBeforePersist(neu);
				entities.add(neu);
			}
			repository.update(entities);
			repository.flush();
			return getList(entities.stream().map(e -> e.ID).toList());
		});
	}

	/**
	 * Führt einen Patch für die UV-Stundentafel aus.
	 * @param patch   der Patch
	 * @return die gepatchte UV-Stundentafel
	 */
	public UvStundentafel patch(final UvStundentafelPatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches auf UV-Stundentafeln aus.
	 * @param patches   die Patches
	 * @return die Liste mit den gepatchten UV-Stundentafeln
	 */
	public List<UvStundentafel> patchMultiple(final Collection<UvStundentafelPatchRequest> patches) {
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

	private static void applyPatch(final DTOUvStundentafel dto, final UvStundentafelPatchRequest patch) {
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
		patch.beschreibung.ifPresent(val -> dto.Beschreibung = val);
	}

	/**
	 * Löscht die UV-Stundentafel mit der angegebenen ID.
	 * @param id   die ID
	 * @return die gelöschte UV-Stundentafel
	 */
	public UvStundentafel delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere UV-Stundentafeln anhand ihrer IDs.
	 * @param ids   die IDs
	 * @return die Liste der gelöschten UV-Stundentafeln
	 */
	public List<UvStundentafel> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = repository.findListByIds(ids);
			final var result = entities.stream().map(UvStundentafelService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

	private static void checkBeforePersist(final DTOUvStundentafel dto) {
		if ((dto.GueltigBis != null) && (dto.GueltigVon.compareTo(dto.GueltigBis) > 0)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Datum 'gueltigBis' darf nicht vor dem Datum 'gueltigVon' liegen.");
		}
	}

}
