package de.svws_nrw.service.uv.raeume;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.uv.UvRaum;
import de.svws_nrw.db.dto.current.uv.DTOUvRaum;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.raeume.UvRaumRepository;
import de.svws_nrw.repo.uv.raeume.UvRaumgruppeRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Räume (Tabelle UV_Raeume).
 */
public final class UvRaumService {

	private final UvRaumRepository repository;
	private final UvRaumgruppeRepository raumgruppeRepository;

	/**
	 * Erstellt einen neuen Service.
	 * @param uvRaumRepository         das Repository für die UV-Räume
	 * @param uvRaumgruppeRepository   das Repository für die UV-Raumgruppen
	 */
	public UvRaumService(final UvRaumRepository uvRaumRepository, final UvRaumgruppeRepository uvRaumgruppeRepository) {
		this.repository = uvRaumRepository;
		this.raumgruppeRepository = uvRaumgruppeRepository;
	}

	private static UvRaum toApi(final DTOUvRaum dto) {
		final var daten = new UvRaum();
		daten.id = dto.ID;
		daten.kuerzel = dto.Kuerzel;
		daten.beschreibung = dto.Beschreibung;
		daten.groesse = dto.Groesse;
		daten.idRaumgruppe = dto.Raumgruppe_ID;
		daten.gueltigVon = dto.GueltigVon;
		daten.gueltigBis = dto.GueltigBis;
		return daten;
	}

	/**
	 * Ermittelt den Raum anhand der ID.
	 * @param id   die ID
	 * @return der Raum
	 */
	public UvRaum get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein UvRaum mit der ID %d gefunden.".formatted(id));
		}
		return list.getFirst();
	}

	/**
	 * Ermittelt die Räume anhand der IDs.
	 * @param ids   die IDs
	 * @return die Liste der Räume
	 */
	public List<UvRaum> getList(final Collection<Long> ids) {
		final var result = repository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvRaeume zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		return result.stream().map(UvRaumService::toApi).toList();
	}

	/**
	 * Liefert alle Räume.
	 * @return die Liste aller Räume
	 */
	public List<UvRaum> getAll() {
		return repository.getAll().stream().map(UvRaumService::toApi).toList();
	}

	/**
	 * Erstellt einen neuen Raum.
	 * @param createRequest   der Create-Request
	 * @return der neue Raum
	 */
	public UvRaum create(final UvRaumCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue Räume.
	 * @param createRequests   die Create-Requests
	 * @return die neuen Räume
	 */
	public List<UvRaum> createMultiple(final Collection<UvRaumCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			long nextId = repository.getNextID();
			final List<DTOUvRaum> entities = new ArrayList<>();
			for (final UvRaumCreateRequest request : createRequests) {
				validateRaumgruppe(request.idRaumgruppe);
				final var neu = new DTOUvRaum(nextId++, request.kuerzel, request.groesse, request.gueltigVon);
				neu.Beschreibung = request.beschreibung;
				neu.Raumgruppe_ID = request.idRaumgruppe;
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
	 * Führt einen Patch für den Raum aus.
	 * @param patch   der Patch
	 * @return der gepatchte Raum
	 */
	public UvRaum patch(final UvRaumPatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches auf Räumen aus.
	 * @param patches   die Patches
	 * @return die Liste mit den gepatchten Räumen
	 */
	public List<UvRaum> patchMultiple(final Collection<UvRaumPatchRequest> patches) {
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

	private void applyPatch(final DTOUvRaum dto, final UvRaumPatchRequest patch) {
		patch.kuerzel.ifPresent(val -> dto.Kuerzel = val);
		patch.beschreibung.ifPresent(val -> dto.Beschreibung = val);
		patch.groesse.ifPresent(val -> dto.Groesse = val);
		patch.idRaumgruppe.ifPresent(val -> {
			validateRaumgruppe(val);
			dto.Raumgruppe_ID = val;
		});
		patch.gueltigVon.ifPresent(val -> dto.GueltigVon = val);
		patch.gueltigBis.ifPresent(val -> dto.GueltigBis = val);
	}

	private void validateRaumgruppe(final Long idRaumgruppe) {
		if ((idRaumgruppe != null) && raumgruppeRepository.findById(idRaumgruppe).isEmpty()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Es wurde keine Raumgruppe mit der ID " + idRaumgruppe + " gefunden.");
		}
	}

	/**
	 * Löscht den Raum mit der ID.
	 * @param id   die ID
	 * @return der gelöschte Raum
	 */
	public UvRaum delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere Räume anhand ihrer IDs.
	 * @param ids   die IDs
	 * @return die Liste der gelöschten Räume
	 */
	public List<UvRaum> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = repository.findListByIds(ids);
			final var result = entities.stream().map(UvRaumService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

	private void checkBeforePersist(final DTOUvRaum dto) {
		if ((dto.GueltigBis != null) && (dto.GueltigVon.compareTo(dto.GueltigBis) > 0)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Datum 'gueltigBis' darf nicht vor dem Datum 'gueltigVon' liegen.");
		}
	}

}
