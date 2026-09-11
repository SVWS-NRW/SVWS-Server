package de.svws_nrw.service.uv.raeume;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.uv.UvRaumgruppe;
import de.svws_nrw.db.dto.current.uv.DTOUvRaumgruppe;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.raeume.UvRaumgruppeRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Raumgruppen (Tabelle UV_Raumgruppen).
 */
public final class UvRaumgruppeService {

	private final UvRaumgruppeRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 * @param uvRaumgruppeRepository   das Repository für die UV-Raumgruppen
	 */
	public UvRaumgruppeService(final UvRaumgruppeRepository uvRaumgruppeRepository) {
		this.repository = uvRaumgruppeRepository;
	}

	private static UvRaumgruppe toApi(final DTOUvRaumgruppe dto) {
		final var daten = new UvRaumgruppe();
		daten.id = dto.ID;
		daten.bezeichnung = dto.Bezeichnung;
		daten.gueltigVon = dto.GueltigVon;
		daten.gueltigBis = dto.GueltigBis;
		daten.beschreibung = dto.Beschreibung;
		return daten;
	}

	/**
	 * Ermittelt die Raumgruppe anhand der ID.
	 * @param id   die ID
	 * @return die Raumgruppe
	 */
	public UvRaumgruppe get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine UvRaumgruppe mit der ID %d gefunden.".formatted(id));
		}
		return list.getFirst();
	}

	/**
	 * Ermittelt die Raumgruppen anhand der IDs.
	 * @param ids   die IDs
	 * @return die Liste der Raumgruppen
	 */
	public List<UvRaumgruppe> getList(final Collection<Long> ids) {
		final var result = repository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvRaumgruppen zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		return result.stream().map(UvRaumgruppeService::toApi).toList();
	}

	/**
	 * Liefert alle Raumgruppen.
	 * @return die Liste aller Raumgruppen
	 */
	public List<UvRaumgruppe> getAll() {
		return repository.getAll().stream().map(UvRaumgruppeService::toApi).toList();
	}

	/**
	 * Erstellt eine neue Raumgruppe.
	 * @param createRequest   der Create-Request
	 * @return die neue Raumgruppe
	 */
	public UvRaumgruppe create(final UvRaumgruppeCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue Raumgruppen.
	 * @param createRequests   die Create-Requests
	 * @return die neuen Raumgruppen
	 */
	public List<UvRaumgruppe> createMultiple(final Collection<UvRaumgruppeCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			long nextId = repository.getNextID();
			final List<DTOUvRaumgruppe> entities = new ArrayList<>();
			for (final UvRaumgruppeCreateRequest request : createRequests) {
				final var neu = new DTOUvRaumgruppe(nextId++, request.bezeichnung, request.gueltigVon);
				neu.GueltigBis = request.gueltigBis;
				neu.Beschreibung = request.beschreibung;
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
	 * Führt einen Patch für die Raumgruppe aus.
	 * @param patch   der Patch
	 * @return die gepatchte Raumgruppe
	 */
	public UvRaumgruppe patch(final UvRaumgruppePatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches auf Raumgruppen aus.
	 * @param patches   die Patches
	 * @return die Liste mit den gepatchten Raumgruppen
	 */
	public List<UvRaumgruppe> patchMultiple(final Collection<UvRaumgruppePatchRequest> patches) {
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

	private static void applyPatch(final DTOUvRaumgruppe dto, final UvRaumgruppePatchRequest patch) {
		patch.bezeichnung.ifPresent(val -> dto.Bezeichnung = val);
		patch.gueltigVon.ifPresent(val -> dto.GueltigVon = val);
		patch.gueltigBis.ifPresent(val -> dto.GueltigBis = val);
		patch.beschreibung.ifPresent(val -> dto.Beschreibung = val);
	}

	/**
	 * Löscht die Raumgruppe mit der ID.
	 * @param id   die ID
	 * @return die gelöschte Raumgruppe
	 */
	public UvRaumgruppe delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere Raumgruppen anhand ihrer IDs.
	 * @param ids   die IDs
	 * @return die Liste der gelöschten Raumgruppen
	 */
	public List<UvRaumgruppe> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = repository.findListByIds(ids);
			final var result = entities.stream().map(UvRaumgruppeService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

	private void checkBeforePersist(final DTOUvRaumgruppe dto) {
		if ((dto.GueltigBis != null) && (dto.GueltigVon.compareTo(dto.GueltigBis) > 0)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Datum 'gueltigBis' darf nicht vor dem Datum 'gueltigVon' liegen.");
		}
	}

}
