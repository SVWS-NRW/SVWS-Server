package de.svws_nrw.service.uv.stundentafeln;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.uv.UvStundentafelFach;
import de.svws_nrw.db.dto.current.uv.DTOUvStundentafelFach;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.stundentafeln.UvStundentafelFachRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Stundentafel-Fächer.
 */
public final class UvStundentafelFachService {

	private final UvStundentafelFachRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 * @param uvStundentafelFachRepository   das Repository
	 */
	public UvStundentafelFachService(final UvStundentafelFachRepository uvStundentafelFachRepository) {
		this.repository = uvStundentafelFachRepository;
	}

	private static UvStundentafelFach toApi(final DTOUvStundentafelFach dto) {
		final var daten = new UvStundentafelFach();
		daten.id = dto.ID;
		daten.idStundentafel = dto.Stundentafel_ID;
		daten.abschnitt = dto.Abschnitt;
		daten.idFach = dto.Fach_ID;
		daten.wochenstunden = dto.Wochenstunden;
		daten.davonErgaenzungsstunden = dto.DavonErgaenzungsstunden;
		return daten;
	}

	/**
	 * Ermittelt das UV-Stundentafel-Fach anhand der ID.
	 * @param id   die ID
	 * @return das UV-Stundentafel-Fach
	 */
	public UvStundentafelFach get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein UvStundentafelFach mit der ID %d gefunden.".formatted(id));
		}
		return list.getFirst();
	}

	/**
	 * Ermittelt die UV-Stundentafel-Fächer anhand der IDs.
	 * @param ids   die IDs
	 * @return die Liste
	 */
	public List<UvStundentafelFach> getList(final Collection<Long> ids) {
		final var result = repository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvStundentafelFaecher zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		return result.stream().map(UvStundentafelFachService::toApi).toList();
	}

	/**
	 * Liefert alle UV-Stundentafel-Fächer.
	 *
	 * @return die Liste aller UV-Stundentafel-Fächer
	 */
	public List<UvStundentafelFach> getAll() {
		return repository.getAll().stream().map(UvStundentafelFachService::toApi).toList();
	}

	/**
	 * Liefert alle UV-Stundentafel-Fächer, die zu den übergebenen Stundentafel-IDs gehören.
	 *
	 * @param idsStundentafel   die IDs der Stundentafeln
	 *
	 * @return die Liste der UV-Stundentafel-Fächer
	 */
	public List<UvStundentafelFach> getListByStundentafelIds(final Collection<Long> idsStundentafel) {
		return repository.getListByStundentafelIds(idsStundentafel).stream().map(UvStundentafelFachService::toApi).toList();
	}

	/**
	 * Erstellt ein neues UV-Stundentafel-Fach.
	 * @param createRequest   der Create-Request
	 * @return das neue UV-Stundentafel-Fach
	 */
	public UvStundentafelFach create(final UvStundentafelFachCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue UV-Stundentafel-Fächer.
	 * @param createRequests   die Create-Requests
	 * @return die neuen UV-Stundentafel-Fächer
	 */
	public List<UvStundentafelFach> createMultiple(final Collection<UvStundentafelFachCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			long nextId = repository.getNextID();
			final List<DTOUvStundentafelFach> entities = new ArrayList<>();
			for (final UvStundentafelFachCreateRequest request : createRequests) {
				final double davon = (request.davonErgaenzungsstunden == null) ? 0.0 : request.davonErgaenzungsstunden;
				final var neu = new DTOUvStundentafelFach(nextId++, request.idStundentafel, request.abschnitt, request.idFach,
						request.wochenstunden, davon);
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
	 * @return das gepatchte UV-Stundentafel-Fach
	 */
	public UvStundentafelFach patch(final UvStundentafelFachPatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches aus.
	 * @param patches   die Patches
	 * @return die Liste der gepatchten UV-Stundentafel-Fächer
	 */
	public List<UvStundentafelFach> patchMultiple(final Collection<UvStundentafelFachPatchRequest> patches) {
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
			}
			repository.update(entities);
			repository.flush();
			return getList(ids);
		});
	}

	private static void applyPatch(final DTOUvStundentafelFach dto, final UvStundentafelFachPatchRequest patch) {
		patch.abschnitt.ifPresent(val -> dto.Abschnitt = val);
		patch.idFach.ifPresent(val -> dto.Fach_ID = val);
		patch.wochenstunden.ifPresent(val -> dto.Wochenstunden = val);
		patch.davonErgaenzungsstunden.ifPresent(val -> dto.DavonErgaenzungsstunden = val);
	}

	/**
	 * Löscht das UV-Stundentafel-Fach.
	 * @param id   die ID
	 * @return das gelöschte UV-Stundentafel-Fach
	 */
	public UvStundentafelFach delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere UV-Stundentafel-Fächer.
	 * @param ids   die IDs
	 * @return die Liste der gelöschten UV-Stundentafel-Fächer
	 */
	public List<UvStundentafelFach> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = repository.findListByIds(ids);
			final var result = entities.stream().map(UvStundentafelFachService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

}
