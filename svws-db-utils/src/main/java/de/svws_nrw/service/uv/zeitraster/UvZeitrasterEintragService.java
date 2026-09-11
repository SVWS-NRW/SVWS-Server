package de.svws_nrw.service.uv.zeitraster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.uv.UvZeitrasterEintrag;
import de.svws_nrw.db.dto.current.uv.DTOUvZeitrasterEintrag;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.zeitraster.UvZeitrasterEintragRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Zeitraster-Einträge.
 */
public final class UvZeitrasterEintragService {

	private final UvZeitrasterEintragRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 * @param uvZeitrasterEintragRepository   das Repository
	 */
	public UvZeitrasterEintragService(final UvZeitrasterEintragRepository uvZeitrasterEintragRepository) {
		this.repository = uvZeitrasterEintragRepository;
	}

	private static UvZeitrasterEintrag toApi(final DTOUvZeitrasterEintrag dto) {
		final var daten = new UvZeitrasterEintrag();
		daten.id = dto.ID;
		daten.idZeitraster = dto.Zeitraster_ID;
		daten.wochentag = dto.Tag;
		daten.stunde = dto.Stunde;
		daten.beginn = (dto.Beginn == null) ? 0 : dto.Beginn;
		daten.ende = (dto.Ende == null) ? 0 : dto.Ende;
		return daten;
	}

	/**
	 * Ermittelt den UV-Zeitraster-Eintrag anhand der ID.
	 * @param id   die ID
	 * @return der UV-Zeitraster-Eintrag
	 */
	public UvZeitrasterEintrag get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein UvZeitrasterEintrag mit der ID %d gefunden.".formatted(id));
		}
		return list.getFirst();
	}

	/**
	 * Ermittelt die UV-Zeitraster-Einträge anhand der IDs.
	 * @param ids   die IDs
	 * @return die Liste
	 */
	public List<UvZeitrasterEintrag> getList(final Collection<Long> ids) {
		final var result = repository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvZeitrasterEintraege zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		return result.stream().map(UvZeitrasterEintragService::toApi).toList();
	}

	/**
	 * Liefert alle UV-Zeitraster-Einträge.
	 *
	 * @return die Liste aller UV-Zeitraster-Einträge
	 */
	public List<UvZeitrasterEintrag> getAll() {
		return repository.getAll().stream().map(UvZeitrasterEintragService::toApi).toList();
	}

	/**
	 * Liefert alle UV-Zeitraster-Einträge zu einem bestimmten Zeitraster.
	 *
	 * @param idZeitraster die ID des Zeitrasters
	 * @return die Liste der Zeitraster-Einträge
	 */
	public List<UvZeitrasterEintrag> getListByZeitrasterId(final long idZeitraster) {
		return repository.getListByZeitrasterId(idZeitraster).stream().map(UvZeitrasterEintragService::toApi).toList();
	}

	/**
	 * Liefert alle UV-Zeitraster-Einträge zu den übergebenen Zeitraster-IDs.
	 *
	 * @param idsZeitraster   die IDs der Zeitraster
	 *
	 * @return die Liste der Zeitraster-Einträge
	 */
	public List<UvZeitrasterEintrag> getListByZeitrasterIds(final Collection<Long> idsZeitraster) {
		return repository.getListByZeitrasterIds(idsZeitraster).stream().map(UvZeitrasterEintragService::toApi).toList();
	}

	/**
	 * Erstellt einen neuen UV-Zeitraster-Eintrag.
	 * @param createRequest   der Create-Request
	 * @return der neue UV-Zeitraster-Eintrag
	 */
	public UvZeitrasterEintrag create(final UvZeitrasterEintragCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue UV-Zeitraster-Einträge.
	 * @param createRequests   die Create-Requests
	 * @return die neuen UV-Zeitraster-Einträge
	 */
	public List<UvZeitrasterEintrag> createMultiple(final Collection<UvZeitrasterEintragCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			long nextId = repository.getNextID();
			final List<DTOUvZeitrasterEintrag> entities = new ArrayList<>();
			for (final UvZeitrasterEintragCreateRequest request : createRequests) {
				final var neu = new DTOUvZeitrasterEintrag(nextId++, request.idZeitraster, request.wochentag, request.stunde,
						request.beginn, request.ende);
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
	 * @return der gepatchte UV-Zeitraster-Eintrag
	 */
	public UvZeitrasterEintrag patch(final UvZeitrasterEintragPatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches aus.
	 * @param patches   die Patches
	 * @return die Liste der gepatchten UV-Zeitraster-Einträge
	 */
	public List<UvZeitrasterEintrag> patchMultiple(final Collection<UvZeitrasterEintragPatchRequest> patches) {
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

	private static void applyPatch(final DTOUvZeitrasterEintrag dto, final UvZeitrasterEintragPatchRequest patch) {
		patch.wochentag.ifPresent(val -> dto.Tag = val);
		patch.stunde.ifPresent(val -> dto.Stunde = val);
		patch.beginn.ifPresent(val -> dto.Beginn = val);
		patch.ende.ifPresent(val -> dto.Ende = val);
	}

	/**
	 * Löscht den UV-Zeitraster-Eintrag.
	 * @param id   die ID
	 * @return der gelöschte UV-Zeitraster-Eintrag
	 */
	public UvZeitrasterEintrag delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere UV-Zeitraster-Einträge.
	 * @param ids   die IDs
	 * @return die Liste der gelöschten UV-Zeitraster-Einträge
	 */
	public List<UvZeitrasterEintrag> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = repository.findListByIds(ids);
			final var result = entities.stream().map(UvZeitrasterEintragService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

}
