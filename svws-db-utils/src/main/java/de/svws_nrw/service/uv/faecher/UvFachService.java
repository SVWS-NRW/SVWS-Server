package de.svws_nrw.service.uv.faecher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.uv.UvFach;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.db.dto.current.uv.DTOUvFach;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.faecher.UvFachRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Fächer (Tabelle UV_Faecher).
 */
public final class UvFachService {

	private final UvFachRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 * @param uvFachRepository   das Repository für die UV-Fächer
	 */
	public UvFachService(final UvFachRepository uvFachRepository) {
		this.repository = uvFachRepository;
	}

	private static UvFach toApi(final DTOUvFach dto) {
		final var daten = new UvFach();
		daten.id = dto.ID;
		daten.idFach = dto.Fach_ID;
		daten.gueltigVon = dto.GueltigVon;
		daten.gueltigBis = dto.GueltigBis;
		return daten;
	}

	/**
	 * Ermittelt das UV-Fach anhand der übergebenen ID.
	 * @param id   die ID des UV-Fachs
	 * @return das UV-Fach
	 */
	public UvFach get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein UvFach mit der ID %d gefunden.".formatted(id));
		}
		return list.getFirst();
	}

	/**
	 * Ermittelt die UV-Fächer anhand der übergebenen IDs.
	 * @param ids   die IDs der UV-Fächer
	 * @return die Liste der UV-Fächer
	 */
	public List<UvFach> getList(final Collection<Long> ids) {
		final var result = repository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvFaecher zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		return result.stream().map(UvFachService::toApi).toList();
	}

	/**
	 * Liefert alle UV-Fächer.
	 *
	 * @return die Liste aller UV-Fächer
	 */
	public List<UvFach> getAll() {
		return repository.getAll().stream().map(UvFachService::toApi).toList();
	}

	/**
	 * Erstellt ein neues UV-Fach mithilfe des Create-Requests.
	 * @param createRequest   der Create-Request
	 * @return das neue UV-Fach
	 */
	public UvFach create(final UvFachCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue UV-Fächer mithilfe der Create-Requests.
	 * @param createRequests   die Create-Requests
	 * @return die neuen UV-Fächer
	 */
	public List<UvFach> createMultiple(final Collection<UvFachCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			long nextId = repository.getNextID();
			final List<DTOUvFach> entities = new ArrayList<>();
			for (final UvFachCreateRequest request : createRequests) {
				if ((request.gueltigVon == null) || !DateUtils.isValidDate(request.gueltigVon)) {
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Das Attribut gueltigVon hat einen ungültigen Datumswert: %s".formatted(request.gueltigVon));
				}
				final var neu = new DTOUvFach(nextId++, request.idFach, request.gueltigVon);
				neu.GueltigBis = request.gueltigBis;
				checkBeforePersist(neu);
				entities.add(neu);
			}
			repository.update(entities);
			repository.flush();
			final var ids = entities.stream().map(e -> e.ID).toList();
			return getList(ids);
		});
	}

	/**
	 * Führt einen Patch für das UV-Fach aus.
	 * @param patch   der Patch
	 * @return das gepatchte UV-Fach
	 */
	public UvFach patch(final UvFachPatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches auf UV-Fächern aus.
	 * @param patches   die Patches
	 * @return die Liste mit den gepatchten UV-Fächern
	 */
	public List<UvFach> patchMultiple(final Collection<UvFachPatchRequest> patches) {
		if (patches.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			final var entities = repository.findListByIds(patches.stream().map(p -> p.id).toList());
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
			return getList(patches.stream().map(p -> p.id).toList());
		});
	}

	private static void applyPatch(final DTOUvFach dto, final UvFachPatchRequest patch) {
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
	 * Löscht das UV-Fach mit der angegebenen ID.
	 * @param id   die ID
	 * @return das gelöschte UV-Fach
	 */
	public UvFach delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere UV-Fächer anhand ihrer IDs.
	 * @param ids   die IDs
	 * @return die Liste der gelöschten UV-Fächer
	 */
	public List<UvFach> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = repository.findListByIds(ids);
			final var result = entities.stream().map(UvFachService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

	private void checkBeforePersist(final DTOUvFach dto) {
		if ((dto.GueltigBis != null) && (dto.GueltigVon.compareTo(dto.GueltigBis) > 0)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Datum 'gueltigBis' darf nicht vor dem Datum 'gueltigVon' liegen.");
		}
		// Prüfe auf überschneidende Zeiträume mit existierenden Einträgen zur gleichen Fach_ID
		final List<DTOUvFach> existing = repository.getListByFachId(dto.Fach_ID);
		for (final DTOUvFach other : existing) {
			if (other.ID == dto.ID) {
				continue;
			}
			if (DateUtils.intervallUeberlappt(dto.GueltigVon, dto.GueltigBis, other.GueltigVon, other.GueltigBis)) {
				throw new ApiOperationException(Status.CONFLICT,
						"Es existiert bereits ein UV-Fach mit der Fach-ID %d mit überschneidendem Gültigkeitszeitraum (ID: %d, gültig von %s bis %s)."
								.formatted(dto.Fach_ID, other.ID, other.GueltigVon, (other.GueltigBis == null) ? "unbegrenzt" : other.GueltigBis));
			}
		}
	}

}
