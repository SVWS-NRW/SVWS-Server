package de.svws_nrw.service.uv.lehrer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.uv.UvLehrerAnrechnungsstunden;
import de.svws_nrw.db.dto.current.uv.DTOUvLehrerAnrechnungsstunden;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.lehrer.UvLehrerAnrechnungsstundenRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die Lehrer der Unterrichtsverteilung
 */
public final class UvLehrerAnrechnungsstundenService {

	/** Das Repository für die Lehrer der Unterrichtsverteilung */
	private final UvLehrerAnrechnungsstundenRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 * @param uvLehrerRepository   das Repository für die Lehrer der Unterrichtsverteilung
	 */
	public UvLehrerAnrechnungsstundenService(final UvLehrerAnrechnungsstundenRepository uvLehrerRepository) {
		this.repository = uvLehrerRepository;
	}

	private static UvLehrerAnrechnungsstunden toApi(final DTOUvLehrerAnrechnungsstunden dto) {
		final var daten = new UvLehrerAnrechnungsstunden();
		daten.id = dto.ID;
		daten.idLehrer = dto.Lehrer_ID;
		daten.anrechnungsgrundKrz = dto.AnrechnungsgrundKrz;
		daten.anzahlStunden = dto.AnzahlStunden;
		daten.gueltigVon = dto.GueltigVon;
		daten.gueltigBis = dto.GueltigBis;
		return daten;
	}

	/**
	 * Ermittelt den Lehrer der Unterrichtsverteilung anhand der übergebenen ID.
	 * @param id   die ID des Lehrers der Unterrichtsverteilung
	 * @return der Lehrer der Unterrichtsverteilung
	 */
	public UvLehrerAnrechnungsstunden get(final long id) {
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
	public List<UvLehrerAnrechnungsstunden> getList(final Collection<Long> ids) {
		final List<DTOUvLehrerAnrechnungsstunden> entities = repository.findListByIds(new ArrayList<>(ids));
		if (entities.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvLehrer zu den IDs gefunden (%d von %d).".formatted(entities.size(), ids.size()));
		}
		return entities.stream().map(UvLehrerAnrechnungsstundenService::toApi).toList();
	}

	/**
	 * Liefert alle Anrechnungsstunden eines bestimmten UV-Lehrers.
	 *
	 * @param idLehrer   die ID des UV-Lehrers
	 * @return die Liste der Anrechnungsstunden
	 */
	public List<UvLehrerAnrechnungsstunden> getListByLehrerId(final long idLehrer) {
		final var dtos = repository.getListByLehrerId(idLehrer);
		return dtos.stream().map(UvLehrerAnrechnungsstundenService::toApi).toList();
	}

	/**
	 * Liefert alle Anrechnungsstunden für die übergebenen UV-Lehrer-IDs.
	 *
	 * @param idsLehrer   die IDs der UV-Lehrer
	 * @return die Liste der Anrechnungsstunden
	 */
	public List<UvLehrerAnrechnungsstunden> getListByLehrerIds(final Collection<Long> idsLehrer) {
		final var dtos = repository.getListByLehrerIds(idsLehrer);
		return dtos.stream().map(UvLehrerAnrechnungsstundenService::toApi).toList();
	}

	/**
	 * Liefert die Anrechnungsstunden für die übergebenen UV-Lehrer-IDs gruppiert nach UV-Lehrer-ID.
	 *
	 * @param idsLehrer   die IDs der UV-Lehrer
	 * @return die Map mit den Anrechnungsstunden, gruppiert nach UV-Lehrer-ID
	 */
	public Map<Long, List<UvLehrerAnrechnungsstunden>> getMapByLehrerIds(final Collection<Long> idsLehrer) {
		final var map = repository.getMapByLehrerIds(idsLehrer);
		final Map<Long, List<UvLehrerAnrechnungsstunden>> result = new java.util.HashMap<>();
		for (final var entry : map.entrySet()) {
			result.put(entry.getKey(), entry.getValue().stream().map(UvLehrerAnrechnungsstundenService::toApi).toList());
		}
		return result;
	}

	private void applyPatch(final DTOUvLehrerAnrechnungsstunden daten, final UvLehrerAnrechnungsstundenPatchRequest patch) {
		patch.anrechnungsgrundKrz.ifPresent(val -> daten.AnrechnungsgrundKrz = val);
		patch.anzahlStunden.ifPresent(val -> daten.AnzahlStunden = val);
		patch.gueltigVon.ifPresent(val -> daten.GueltigVon = val);
		patch.gueltigBis.ifPresent(val -> daten.GueltigBis = val);
	}

	/**
	 * Erstellt einen neuen Lehrer der Unterrichtsverteilung mit einer neuen ID
	 * und mithilfe des Create-Requests.
	 * @param patch   der Create-Request
	 * @return der neue Lehrer der Unterrichtsverteilung
	 */
	public UvLehrerAnrechnungsstunden create(final UvLehrerAnrechnungsstundenCreateRequest patch) {
		return createMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Erstellt neue Lehrer der Unterrichtsverteilung mit neuen IDs und mithilfe der Create-Requests.
	 * @param createRequests   die Create-Requests
	 * @return die neuen Lehrer der Unterrichtsverteilung
	 */
	public List<UvLehrerAnrechnungsstunden> createMultiple(final Collection<UvLehrerAnrechnungsstundenCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			// Erstelle die neuen Entitäten als Grundlage für den Patch-Vorgang
			long nextId = repository.getNextID();
			final List<DTOUvLehrerAnrechnungsstunden> entities = new ArrayList<>();
			for (final UvLehrerAnrechnungsstundenCreateRequest request : createRequests) {
				final var neu =
						new DTOUvLehrerAnrechnungsstunden(nextId++, request.idLehrer, request.anrechnungsgrundKrz, request.anzahlStunden, request.gueltigVon);
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
	public UvLehrerAnrechnungsstunden patch(final UvLehrerAnrechnungsstundenPatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches auf mehrere Lehrer der Unterrichtsverteilung aus.
	 * @param patches   eine Map mit den Patches, welche jeweils ihren IDs zugeordnet werden.
	 * @return die Liste mit den gepatchten Lehrern der Unterrichtsverteilung
	 */
	public List<UvLehrerAnrechnungsstunden> patchMultiple(final Collection<UvLehrerAnrechnungsstundenPatchRequest> patches) {
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
	public UvLehrerAnrechnungsstunden delete(final long id) {
		final List<UvLehrerAnrechnungsstunden> result = deleteMultiple(List.of(id));
		return result.getFirst();
	}


	/**
	 * Löscht mehrere Lehrer der Unterrichtsverteilung mit den angegebenen IDs aus der Datenbank.
	 * @param ids   die IDs
	 * @return die entfernten Lehrer der Unterrichtsverteilung
	 */
	public List<UvLehrerAnrechnungsstunden> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = repository.findListByIds(ids);
			final var result = entities.stream().map(UvLehrerAnrechnungsstundenService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

	private void checkBeforePersist(final DTOUvLehrerAnrechnungsstunden dto) {
		if ((dto.GueltigBis != null) && (dto.GueltigVon.compareTo(dto.GueltigBis) > 0)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Datum 'gueltigBis' darf nicht vor dem Datum 'gueltigVon' liegen.");
		}
	}

}
