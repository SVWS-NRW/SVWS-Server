package de.svws_nrw.service.uv.klassen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.uv.UvKlassenLehrer;
import de.svws_nrw.db.dto.current.uv.DTOUvKlassenLehrer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.klassen.UvKlassenLehrerRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Klassen-Lehrer-Zuordnungen (Tabelle UV_KlassenLehrer).
 */
public final class UvKlassenLehrerService {

	private final UvKlassenLehrerRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param uvKlassenLehrerRepository   das Repository
	 */
	public UvKlassenLehrerService(final UvKlassenLehrerRepository uvKlassenLehrerRepository) {
		this.repository = uvKlassenLehrerRepository;
	}

	private static UvKlassenLehrer toApi(final DTOUvKlassenLehrer dto) {
		final var daten = new UvKlassenLehrer();
		daten.id = dto.ID;
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		daten.idKlasse = dto.Klasse_ID;
		daten.idLehrer = dto.Lehrer_ID;
		daten.reihenfolge = dto.Reihenfolge;
		return daten;
	}

	/**
	 * Ermittelt die Klassen-Lehrer-Zuordnung anhand der ID.
	 *
	 * @param id   die ID
	 *
	 * @return die Zuordnung
	 */
	public UvKlassenLehrer get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine UvKlassenLehrer-Zuordnung mit der ID %d gefunden.".formatted(id));
		}
		return list.getFirst();
	}

	/**
	 * Ermittelt die Klassen-Lehrer-Zuordnungen anhand der IDs.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Liste
	 */
	public List<UvKlassenLehrer> getList(final Collection<Long> ids) {
		final var result = repository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvKlassenLehrer-Zuordnungen zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		return result.stream().map(UvKlassenLehrerService::toApi).toList();
	}

	/**
	 * Liefert alle Klassen-Lehrer-Zuordnungen eines Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Zuordnungen
	 */
	public List<UvKlassenLehrer> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return repository.getListByPlanungsabschnitt(idPlanungsabschnitt).stream().map(UvKlassenLehrerService::toApi).toList();
	}

	/**
	 * Erstellt eine neue Klassen-Lehrer-Zuordnung.
	 *
	 * @param createRequest   der Create-Request
	 *
	 * @return die neue Zuordnung
	 */
	public UvKlassenLehrer create(final UvKlassenLehrerCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue Klassen-Lehrer-Zuordnungen.
	 *
	 * @param createRequests   die Create-Requests
	 *
	 * @return die neuen Zuordnungen
	 */
	public List<UvKlassenLehrer> createMultiple(final Collection<UvKlassenLehrerCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			long nextId = repository.getNextID();
			final List<DTOUvKlassenLehrer> entities = new ArrayList<>();
			for (final UvKlassenLehrerCreateRequest request : createRequests) {
				entities.add(buildNewDto(nextId++, request));
			}
			repository.update(entities);
			repository.flush();
			return getList(entities.stream().map(e -> e.ID).toList());
		});
	}

	private static DTOUvKlassenLehrer buildNewDto(final long id, final UvKlassenLehrerCreateRequest request) {
		return new DTOUvKlassenLehrer(id, request.idPlanungsabschnitt, request.idKlasse, request.idLehrer, request.reihenfolge);
	}

	/**
	 * Führt einen Patch auf einer Klassen-Lehrer-Zuordnung aus.
	 *
	 * @param patch   der Patch
	 *
	 * @return die gepatchte Zuordnung
	 */
	public UvKlassenLehrer patch(final UvKlassenLehrerPatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches auf Klassen-Lehrer-Zuordnungen aus.
	 *
	 * @param patches   die Patches
	 *
	 * @return die gepatchten Zuordnungen
	 */
	public List<UvKlassenLehrer> patchMultiple(final Collection<UvKlassenLehrerPatchRequest> patches) {
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

	private static void applyPatch(final DTOUvKlassenLehrer dto, final UvKlassenLehrerPatchRequest patch) {
		patch.idLehrer.ifPresent(val -> dto.Lehrer_ID = val);
		patch.reihenfolge.ifPresent(val -> dto.Reihenfolge = val);
	}

	/**
	 * Löscht eine Klassen-Lehrer-Zuordnung.
	 *
	 * @param id   die ID
	 *
	 * @return die gelöschte Zuordnung
	 */
	public UvKlassenLehrer delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere Klassen-Lehrer-Zuordnungen.
	 *
	 * @param ids   die IDs
	 *
	 * @return die gelöschten Zuordnungen
	 */
	public List<UvKlassenLehrer> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = repository.findListByIds(ids);
			final var result = entities.stream().map(UvKlassenLehrerService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

}
