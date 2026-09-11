package de.svws_nrw.service.uv.lerngruppen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.uv.UvLerngruppenLehrer;
import de.svws_nrw.core.data.uv.UvLerngruppenLehrerCreateRequest;
import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppenLehrer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.lerngruppen.UvLerngruppenLehrerRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Lerngruppen-Lehrer-Zuordnungen (Tabelle UV_LerngruppenLehrer).
 */
public final class UvLerngruppenLehrerService {

	private final UvLerngruppenLehrerRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param uvLerngruppenLehrerRepository   das Repository
	 */
	public UvLerngruppenLehrerService(final UvLerngruppenLehrerRepository uvLerngruppenLehrerRepository) {
		this.repository = uvLerngruppenLehrerRepository;
	}

	private static UvLerngruppenLehrer toApi(final DTOUvLerngruppenLehrer dto) {
		final var daten = new UvLerngruppenLehrer();
		daten.id = dto.ID;
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		daten.idLerngruppe = dto.Lerngruppe_ID;
		daten.idLehrer = dto.Lehrer_ID;
		daten.reihenfolge = dto.Reihenfolge;
		daten.wochenstunden = dto.Wochenstunden;
		daten.wochenstundenAngerechnet = dto.WochenstundenAngerechnet;
		return daten;
	}

	/**
	 * Ermittelt die Lerngruppen-Lehrer-Zuordnung anhand der ID.
	 *
	 * @param id   die ID
	 *
	 * @return die Zuordnung
	 */
	public UvLerngruppenLehrer get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine UvLerngruppenLehrer-Zuordnung mit der ID %d gefunden.".formatted(id));
		}
		return list.getFirst();
	}

	/**
	 * Ermittelt die Lerngruppen-Lehrer-Zuordnungen anhand der IDs.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Liste
	 */
	public List<UvLerngruppenLehrer> getList(final Collection<Long> ids) {
		final var result = repository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvLerngruppenLehrer-Zuordnungen zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		return result.stream().map(UvLerngruppenLehrerService::toApi).toList();
	}

	/**
	 * Liefert alle Lerngruppen-Lehrer-Zuordnungen eines Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Zuordnungen
	 */
	public List<UvLerngruppenLehrer> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return repository.getListByPlanungsabschnitt(idPlanungsabschnitt).stream().map(UvLerngruppenLehrerService::toApi).toList();
	}

	/**
	 * Erstellt eine neue Lerngruppen-Lehrer-Zuordnung.
	 *
	 * @param createRequest   der Create-Request
	 *
	 * @return die neue Zuordnung
	 */
	public UvLerngruppenLehrer create(final UvLerngruppenLehrerCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue Lerngruppen-Lehrer-Zuordnungen.
	 *
	 * @param createRequests   die Create-Requests
	 *
	 * @return die neuen Zuordnungen
	 */
	public List<UvLerngruppenLehrer> createMultiple(final Collection<UvLerngruppenLehrerCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			long nextId = repository.getNextID();
			final List<DTOUvLerngruppenLehrer> entities = new ArrayList<>();
			for (final UvLerngruppenLehrerCreateRequest request : createRequests) {
				entities.add(buildNewDto(nextId++, request));
			}
			repository.update(entities);
			repository.flush();
			return getList(entities.stream().map(e -> e.ID).toList());
		});
	}

	private static DTOUvLerngruppenLehrer buildNewDto(final long id, final UvLerngruppenLehrerCreateRequest request) {
		return new DTOUvLerngruppenLehrer(id, request.idPlanungsabschnitt, request.idLerngruppe, request.idLehrer,
				request.reihenfolge, request.wochenstunden, request.wochenstundenAngerechnet);
	}

	/**
	 * Führt einen Patch auf einer Lerngruppen-Lehrer-Zuordnung aus.
	 *
	 * @param patch   der Patch
	 *
	 * @return die gepatchte Zuordnung
	 */
	public UvLerngruppenLehrer patch(final UvLerngruppenLehrerPatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches auf Lerngruppen-Lehrer-Zuordnungen aus.
	 *
	 * @param patches   die Patches
	 *
	 * @return die gepatchten Zuordnungen
	 */
	public List<UvLerngruppenLehrer> patchMultiple(final Collection<UvLerngruppenLehrerPatchRequest> patches) {
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

	private static void applyPatch(final DTOUvLerngruppenLehrer dto, final UvLerngruppenLehrerPatchRequest patch) {
		patch.idLehrer.ifPresent(val -> dto.Lehrer_ID = val);
		patch.reihenfolge.ifPresent(val -> dto.Reihenfolge = val);
		patch.wochenstunden.ifPresent(val -> dto.Wochenstunden = val);
		patch.wochenstundenAngerechnet.ifPresent(val -> dto.WochenstundenAngerechnet = val);
	}

	/**
	 * Löscht eine Lerngruppen-Lehrer-Zuordnung.
	 *
	 * @param id   die ID
	 *
	 * @return die gelöschte Zuordnung
	 */
	public UvLerngruppenLehrer delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere Lerngruppen-Lehrer-Zuordnungen.
	 *
	 * @param ids   die IDs
	 *
	 * @return die gelöschten Zuordnungen
	 */
	public List<UvLerngruppenLehrer> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = repository.findListByIds(ids);
			final var result = entities.stream().map(UvLerngruppenLehrerService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

}
