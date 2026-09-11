package de.svws_nrw.service.uv.lehrer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.uv.UvLehrerPflichtstundensoll;
import de.svws_nrw.db.dto.current.uv.DTOUvLehrerPflichtstundensoll;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.lehrer.UvLehrerPflichtstundensollRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die Lehrer der Unterrichtsverteilung
 */
public final class UvLehrerPflichtstundensollService {

	/** Das Repository für die Lehrer der Unterrichtsverteilung */
	private final UvLehrerPflichtstundensollRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 * @param uvLehrerRepository   das Repository für die Lehrer der Unterrichtsverteilung
	 */
	public UvLehrerPflichtstundensollService(final UvLehrerPflichtstundensollRepository uvLehrerRepository) {
		this.repository = uvLehrerRepository;
	}

	private static UvLehrerPflichtstundensoll toApi(final DTOUvLehrerPflichtstundensoll dto) {
		final var daten = new UvLehrerPflichtstundensoll();
		daten.id = dto.ID;
		daten.idLehrer = dto.Lehrer_ID;
		daten.pflichtstdSoll = dto.PflichtstdSoll;
		daten.gueltigVon = dto.GueltigVon;
		daten.gueltigBis = dto.GueltigBis;
		return daten;
	}

	/**
	 * Ermittelt den Lehrer der Unterrichtsverteilung anhand der übergebenen ID.
	 * @param id   die ID des Lehrers der Unterrichtsverteilung
	 * @return der Lehrer der Unterrichtsverteilung
	 */
	public UvLehrerPflichtstundensoll get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein UvLehrer mit der ID %d gefunden.".formatted(id));
		}
		return list.getFirst();
	}

	/**
	 * Ermittelt die Lehrer der Unterrichtsverteilung anhand der übergebenen IDs.
	 * @param ids   die IDs der Lehrer der Unterrichtsverteilung
	 * @return die Liste der Lehrer der Unterrichtsverteilung
	 */
	public List<UvLehrerPflichtstundensoll> getList(final Collection<Long> ids) {
		final var result = repository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvLehrer zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		return result.stream().map(UvLehrerPflichtstundensollService::toApi).toList();
	}

	/**
	 * Liefert alle Pflichtstunden eines bestimmten UV-Lehrers.
	 *
	 * @param idLehrer   die ID des UV-Lehrers
	 * @return die Liste der Pflichtstunden
	 */
	public List<UvLehrerPflichtstundensoll> getListByLehrerId(final long idLehrer) {
		final var dtos = repository.getListByLehrerId(idLehrer);
		return dtos.stream().map(UvLehrerPflichtstundensollService::toApi).toList();
	}

	/**
	 * Liefert alle Pflichtstunden für die übergebenen UV-Lehrer-IDs.
	 *
	 * @param idsLehrer   die IDs der UV-Lehrer
	 * @return die Liste der Pflichtstunden
	 */
	public List<UvLehrerPflichtstundensoll> getListByLehrerIds(final Collection<Long> idsLehrer) {
		final var dtos = repository.getListByLehrerIds(idsLehrer);
		return dtos.stream().map(UvLehrerPflichtstundensollService::toApi).toList();
	}

	/**
	 * Liefert die Pflichtstunden für die übergebenen UV-Lehrer-IDs gruppiert nach UV-Lehrer-ID.
	 *
	 * @param idsLehrer   die IDs der UV-Lehrer
	 * @return die Map mit den Pflichtstunden, gruppiert nach UV-Lehrer-ID
	 */
	public Map<Long, List<UvLehrerPflichtstundensoll>> getMapByLehrerIds(final Collection<Long> idsLehrer) {
		final var map = repository.getMapByLehrerIds(idsLehrer);
		final Map<Long, List<UvLehrerPflichtstundensoll>> result = new java.util.HashMap<>();
		for (final var entry : map.entrySet()) {
			result.put(entry.getKey(), entry.getValue().stream().map(UvLehrerPflichtstundensollService::toApi).toList());
		}
		return result;
	}

	private void applyPatch(final DTOUvLehrerPflichtstundensoll daten, final UvLehrerPflichtstundensollPatchRequest patch) {
		patch.pflichtstdSoll.ifPresent(val -> daten.PflichtstdSoll = val);
		patch.gueltigVon.ifPresent(val -> daten.GueltigVon = val);
		patch.gueltigBis.ifPresent(val -> daten.GueltigBis = val);
	}

	/**
	 * Erstellt einen neuen Lehrer der Unterrichtsverteilung mit einer neuen ID
	 * und mithilfe des Create-Requests.
	 * @param patch   der Create-Request
	 * @return der neue Lehrer der Unterrichtsverteilung
	 */
	public UvLehrerPflichtstundensoll create(final UvLehrerPflichtstundensollCreateRequest patch) {
		return createMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Erstellt neue Lehrer der Unterrichtsverteilung mit neuen IDs und mithilfe der Create-Requests.
	 * @param createRequests   die Create-Requests
	 * @return die neuen Lehrer der Unterrichtsverteilung
	 */
	public List<UvLehrerPflichtstundensoll> createMultiple(final Collection<UvLehrerPflichtstundensollCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			// Erstelle die neuen Entitäten als Grundlage für den Patch-Vorgang
			long nextId = repository.getNextID();
			final List<DTOUvLehrerPflichtstundensoll> entities = new ArrayList<>();
			for (final UvLehrerPflichtstundensollCreateRequest request : createRequests) {
				final var neu = new DTOUvLehrerPflichtstundensoll(nextId++, request.idLehrer, request.pflichtstdSoll, request.gueltigVon);
				neu.GueltigBis = request.gueltigBis;
				checkBeforePersist(neu);
				entities.add(neu);
			}
			// Persistiere das Ergebnis und gebe die Core-DTOs zurück
			repository.update(entities);
			repository.flush();
			final var ids = entities.stream().map(e -> e.ID).toList();
			return getList(ids);
		});
	}

	/**
	 * Führt einen Patch für den Lehrer der Unterrichtsverteilung mit der angegebenen ID aus.
	 * @param patch   der Patch
	 * @return der gepatchte Lehrer der Unterrichtsverteilung
	 */
	public UvLehrerPflichtstundensoll patch(final UvLehrerPflichtstundensollPatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches auf mehrere Lehrer der Unterrichtsverteilung aus.
	 * @param patches   eine Map mit den Patches, welche jeweils ihren IDs zugeordnet werden.
	 * @return die Liste mit den gepatchten Lehrern der Unterrichtsverteilung
	 */
	public List<UvLehrerPflichtstundensoll> patchMultiple(final Collection<UvLehrerPflichtstundensollPatchRequest> patches) {
		if (patches.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			// Bestimme die Entitäten aus der Datenbank
			final var entities = repository.findListByIds(patches.stream().map(p -> p.id).toList());
			if (entities.size() != patches.size()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle angefragten Datensätze konnten gefunden werden.");
			}
			// Führe die Patches aus
			for (final var patch : patches) {
				final var entity = repository.getById(patch.id);
				applyPatch(entity, patch);
				checkBeforePersist(entity);
			}
			// Persistiere das Ergebnis und gebe die Core-DTOs zurück
			repository.update(entities);
			repository.flush();
			return getList(patches.stream().map(p -> p.id).toList());
		});
	}

	/**
	 * Löscht den Lehrer der Unterrichtsverteilung mit der angegebenen ID aus der Datenbank.
	 * @param id   die ID
	 * @return der entfernte Lehrer der Unterrichtsverteilung
	 */
	public UvLehrerPflichtstundensoll delete(final long id) {
		final List<UvLehrerPflichtstundensoll> result = deleteMultiple(List.of(id));
		return result.getFirst();
	}


	/**
	 * Löscht mehrere Lehrer der Unterrichtsverteilung mit den angegebenen IDs aus der Datenbank.
	 * @param ids   die IDs
	 * @return die entfernten Lehrer der Unterrichtsverteilung
	 */
	public List<UvLehrerPflichtstundensoll> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = repository.findListByIds(ids);
			final var result = entities.stream().map(UvLehrerPflichtstundensollService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

	private void checkBeforePersist(final DTOUvLehrerPflichtstundensoll dto) {
		if ((dto.GueltigBis != null) && (dto.GueltigVon.compareTo(dto.GueltigBis) > 0)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Datum 'gueltigBis' darf nicht vor dem Datum 'gueltigVon' liegen.");
		}
	}

}
