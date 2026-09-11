package de.svws_nrw.service.uv.unterrichte;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.uv.UvUnterricht;
import de.svws_nrw.db.dto.current.uv.DTOUvUnterricht;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.unterrichte.UvUnterrichtRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Unterrichte (Tabelle UV_Unterrichte).
 */
public final class UvUnterrichtService {

	private final UvUnterrichtRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository   das Repository
	 */
	public UvUnterrichtService(final UvUnterrichtRepository repository) {
		this.repository = repository;
	}

	private static UvUnterricht toApi(final DTOUvUnterricht dto) {
		final var daten = new UvUnterricht();
		daten.id = dto.ID;
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		daten.idLerngruppe = dto.Lerngruppe_ID;
		daten.idZeitrasterEintrag = dto.ZeitrasterEintrag_ID;
		return daten;
	}

	/**
	 * Ermittelt die Unterricht-Einheit anhand der ID.
	 *
	 * @param id   die ID
	 *
	 * @return die Unterricht-Einheit
	 */
	public UvUnterricht get(final long id) {
		final var dto = repository.findById(id);
		if (dto.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine UvUnterricht-Einheit mit der ID %d gefunden.".formatted(id));
		}
		return toApi(dto.get());
	}

	/**
	 * Ermittelt die Unterricht-Einheiten anhand der IDs.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Liste
	 */
	public List<UvUnterricht> getList(final Collection<Long> ids) {
		final var result = repository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvUnterrichte zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		return result.stream().map(UvUnterrichtService::toApi).toList();
	}

	/**
	 * Liefert alle Unterricht-Einheiten eines Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Unterricht-Einheiten
	 */
	public List<UvUnterricht> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return repository.getListByPlanungsabschnitt(idPlanungsabschnitt).stream().map(UvUnterrichtService::toApi).toList();
	}

	/**
	 * Erstellt eine neue Unterricht-Einheit.
	 *
	 * @param createRequest   der Create-Request
	 *
	 * @return die neue Unterricht-Einheit
	 */
	public UvUnterricht create(final UvUnterrichtCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue Unterricht-Einheiten.
	 *
	 * @param createRequests   die Create-Requests
	 *
	 * @return die neuen Unterricht-Einheiten
	 */
	public List<UvUnterricht> createMultiple(final Collection<UvUnterrichtCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			long nextId = repository.getNextID();
			final List<DTOUvUnterricht> entities = new ArrayList<>();
			for (final UvUnterrichtCreateRequest request : createRequests) {
				final DTOUvUnterricht dto = new DTOUvUnterricht(nextId++, request.idPlanungsabschnitt, request.idLerngruppe);
				dto.ZeitrasterEintrag_ID = request.idZeitrasterEintrag;
				entities.add(dto);
			}
			repository.update(entities);
			repository.flush();
			return getList(entities.stream().map(e -> e.ID).toList());
		});
	}

	/**
	 * Führt einen Patch auf einer Unterricht-Einheit aus.
	 *
	 * @param patch   der Patch
	 *
	 * @return die gepatchte Unterricht-Einheit
	 */
	public UvUnterricht patch(final UvUnterrichtPatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches auf Unterricht-Einheiten aus.
	 *
	 * @param patches   die Patches
	 *
	 * @return die gepatchten Unterricht-Einheiten
	 */
	public List<UvUnterricht> patchMultiple(final Collection<UvUnterrichtPatchRequest> patches) {
		if (patches.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			final var ids = patches.stream().map(p -> p.id).toList();
			final var entities = repository.findListByIds(ids);
			if (entities.size() != patches.size()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle angefragten Datensätze konnten gefunden werden.");
			}
			for (final var patch : patches) {
				final var entity = repository.getById(patch.id);
				applyPatch(entity, patch);
			}
			repository.update(entities);
			repository.flush();
			return getList(ids);
		});
	}

	private static void applyPatch(final DTOUvUnterricht dto, final UvUnterrichtPatchRequest patch) {
		patch.idLerngruppe.ifPresent(val -> dto.Lerngruppe_ID = val);
		patch.idZeitrasterEintrag.ifPresent(val -> dto.ZeitrasterEintrag_ID = val);
	}

	/**
	 * Löscht eine Unterricht-Einheit.
	 *
	 * @param id   die ID
	 *
	 * @return die gelöschte Unterricht-Einheit
	 */
	public UvUnterricht delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere Unterricht-Einheiten.
	 *
	 * @param ids   die IDs
	 *
	 * @return die gelöschten Unterricht-Einheiten
	 */
	public List<UvUnterricht> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = repository.findListByIds(ids);
			final var result = entities.stream().map(UvUnterrichtService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

}
