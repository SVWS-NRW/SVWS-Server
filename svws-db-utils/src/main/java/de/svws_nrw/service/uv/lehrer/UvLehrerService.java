package de.svws_nrw.service.uv.lehrer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.uv.UvLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.uv.DTOUvLehrer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.uv.lehrer.UvLehrerRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die Lehrer der Unterrichtsverteilung
 */
public final class UvLehrerService {

	/** Das Repository für die Lehrer der Unterrichtsverteilung */
	private final UvLehrerRepository repository;
	private final LehrerRepository lehrerRepository;

	/**
	 * Erstellt einen neuen Service.
	 * @param uvLehrerRepository   das Repository für die Lehrer der Unterrichtsverteilung
	 * @param lehrerRepository   das Repository für die Stammdaten der Lehrer
	 */
	public UvLehrerService(final UvLehrerRepository uvLehrerRepository, final LehrerRepository lehrerRepository) {
		this.repository = uvLehrerRepository;
		this.lehrerRepository = lehrerRepository;
	}

	private UvLehrer toApi(final DTOUvLehrer dto) {
		final var daten = new UvLehrer();
		daten.id = dto.ID;
		daten.idKLehrer = dto.K_Lehrer_ID;
		daten.kuerzel = (dto.Kuerzel != null) ? dto.Kuerzel : "";
		daten.nachname = dto.Nachname;
		daten.vorname = dto.Vorname;
		return daten;
	}

	/**
	 * Ermittelt den Lehrer der Unterrichtsverteilung anhand der übergebenen ID.
	 * @param id   die ID des Lehrers der Unterrichtsverteilung
	 * @return der Lehrer der Unterrichtsverteilung
	 */
	public UvLehrer get(final long id) {
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
	public List<UvLehrer> getList(final Collection<Long> ids) {
		final var result = repository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvLehrer zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		return mapAll(result);
	}

	private List<UvLehrer> mapAll(final Collection<DTOUvLehrer> result) {
		final Map<Long, DTOLehrer> mapKLehrer = lehrerRepository.findMapByIds(result.stream().map(l -> l.K_Lehrer_ID).toList());
		return result.stream().map(dto -> {
			final UvLehrer uv = toApi(dto);
			final DTOLehrer l = mapKLehrer.get(uv.idKLehrer);
			if (l != null) {
				uv.kuerzel = l.Kuerzel;
				uv.nachname = l.Nachname;
				uv.vorname = l.Vorname;
				uv.datumZugang = l.DatumZugang;
				uv.datumAbgang = l.DatumAbgang;
			}
			return uv;
		}).toList();
	}

	/**
	 * Retrieves a complete list of teachers from the Unterrichtsverteilung (UV) system.
	 * The returned list is mapped from DTO entities to the API representation.
	 *
	 * @return a list of teachers in the API-specific format
	 */
	public List<UvLehrer> getAll() {
		final var result = repository.getAll();
		return mapAll(result);
	}

	private void applyPatch(final DTOUvLehrer daten, final UvLehrerPatchRequest patch) {
		patch.idKLehrer.ifPresent(val -> daten.K_Lehrer_ID = val);
		patch.kuerzel.ifPresent(val -> daten.Kuerzel = val);
		patch.vorname.ifPresent(val -> daten.Vorname = val);
		patch.nachname.ifPresent(val -> daten.Nachname = val);
	}

	private void applyCreateRequest(final DTOUvLehrer daten, final UvLehrerCreateRequest createRequest) {
		daten.K_Lehrer_ID = createRequest.idKLehrer;
		daten.Kuerzel = createRequest.kuerzel;
		daten.Vorname = createRequest.vorname;
		daten.Nachname = createRequest.nachname;
	}

	/**
	 * Erstellt einen neuen Lehrer der Unterrichtsverteilung mit einer neuen ID
	 * und mithilfe des Create-Requests.
	 * @param patch   der Create-Request
	 * @return der neue Lehrer der Unterrichtsverteilung
	 */
	public UvLehrer create(final UvLehrerCreateRequest patch) {
		return createMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Erstellt neue Lehrer der Unterrichtsverteilung mit neuen IDs und mithilfe der Create-Requests.
	 * @param createRequests   die Create-Requests
	 * @return die neuen Lehrer der Unterrichtsverteilung
	 */
	public List<UvLehrer> createMultiple(final Collection<UvLehrerCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			// Erstelle die neuen Entitäten als Grundlage für den Patch-Vorgang
			long nextId = repository.getNextID();
			final List<DTOUvLehrer> entities = new ArrayList<>();
			for (final UvLehrerCreateRequest request : createRequests) {
				final var neu = new DTOUvLehrer(nextId++);
				applyCreateRequest(neu, request);
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
	public UvLehrer patch(final UvLehrerPatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches auf mehrere Lehrer der Unterrichtsverteilung aus.
	 * @param patches   eine Map mit den Patches, welche jeweils ihren IDs zugeordnet werden.
	 * @return die Liste mit den gepatchten Lehrern der Unterrichtsverteilung
	 */
	public List<UvLehrer> patchMultiple(final Collection<UvLehrerPatchRequest> patches) {
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
	public UvLehrer delete(final long id) {
		final List<UvLehrer> result = deleteMultiple(List.of(id));
		return result.getFirst();
	}


	/**
	 * Löscht mehrere Lehrer der Unterrichtsverteilung mit den angegebenen IDs aus der Datenbank.
	 * @param ids   die IDs
	 * @return die entfernten Lehrer der Unterrichtsverteilung
	 */
	public List<UvLehrer> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = repository.findListByIds(ids);
			final var result = entities.stream().map(this::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

	private void checkBeforePersist(final DTOUvLehrer dto) {
		if (dto.K_Lehrer_ID != null) {
			dto.Kuerzel = null;
			dto.Vorname = null;
			dto.Nachname = null;
		} else {
			if ((dto.Kuerzel == null) || dto.Kuerzel.isBlank()) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Das Attribut Kuerzel muss angegeben werden.");
			}
		}
	}


}
