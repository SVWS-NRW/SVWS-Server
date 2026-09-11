package de.svws_nrw.service.uv.schienen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.uv.UvSchiene;
import de.svws_nrw.core.data.uv.UvSchieneCreateRequest;
import de.svws_nrw.db.dto.current.uv.DTOUvSchiene;
import de.svws_nrw.db.dto.current.uv.DTOUvSchienenConstraintJahrgang;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.schienen.UvSchieneConstraintJahrgangRepository;
import de.svws_nrw.repo.uv.schienen.UvSchieneRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Schienen (Tabelle UV_Schienen).
 */
public final class UvSchieneService {

	private final UvSchieneRepository repository;
	private final UvSchieneConstraintJahrgangRepository jahrgangRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param uvSchieneRepository           das Repository für die Schienen
	 * @param uvSchieneJahrgangRepository   das Repository für die Jahrgang-Constraints
	 */
	public UvSchieneService(final UvSchieneRepository uvSchieneRepository,
			final UvSchieneConstraintJahrgangRepository uvSchieneJahrgangRepository) {
		this.repository = uvSchieneRepository;
		this.jahrgangRepository = uvSchieneJahrgangRepository;
	}

	private UvSchiene toApi(final DTOUvSchiene dto) {
		final var daten = new UvSchiene();
		daten.id = dto.ID;
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		daten.nummer = dto.Nummer;
		daten.bezeichnung = dto.Bezeichnung;
		daten.idsJahrgaengeErlaubt = jahrgangRepository.getListBySchieneId(dto.ID)
				.stream().map(c -> c.Jahrgang_ID).toList();
		return daten;
	}

	private List<UvSchiene> toApiList(final List<DTOUvSchiene> dtos) {
		if (dtos.isEmpty()) {
			return List.of();
		}
		final List<Long> schieneIds = dtos.stream().map(d -> d.ID).toList();
		final Map<Long, List<Long>> jahrgaengeBySchiene = jahrgangRepository.getListBySchieneIds(schieneIds)
				.stream()
				.collect(Collectors.groupingBy(c -> c.Schiene_ID, Collectors.mapping(c -> c.Jahrgang_ID, Collectors.toList())));
		return dtos.stream().map(dto -> {
			final var daten = new UvSchiene();
			daten.id = dto.ID;
			daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
			daten.nummer = dto.Nummer;
			daten.bezeichnung = dto.Bezeichnung;
			daten.idsJahrgaengeErlaubt = jahrgaengeBySchiene.getOrDefault(dto.ID, List.of());
			return daten;
		}).toList();
	}

	/**
	 * Ermittelt die Schiene anhand der ID.
	 *
	 * @param id   die ID
	 *
	 * @return die Schiene
	 */
	public UvSchiene get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine UvSchiene mit der ID %d gefunden.".formatted(id));
		}
		return list.getFirst();
	}

	/**
	 * Ermittelt die Schienen anhand der IDs.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Liste
	 */
	public List<UvSchiene> getList(final Collection<Long> ids) {
		final var result = repository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvSchienen zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		return toApiList(result);
	}

	/**
	 * Liefert alle Schienen eines Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Schienen
	 */
	public List<UvSchiene> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return toApiList(repository.getListByPlanungsabschnitt(idPlanungsabschnitt));
	}

	/**
	 * Erstellt eine neue Schiene.
	 *
	 * @param createRequest   der Create-Request
	 *
	 * @return die neue Schiene
	 */
	public UvSchiene create(final UvSchieneCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue Schienen.
	 *
	 * @param createRequests   die Create-Requests
	 *
	 * @return die neuen Schienen
	 */
	public List<UvSchiene> createMultiple(final Collection<UvSchieneCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			long nextId = repository.getNextID();
			final List<DTOUvSchiene> entities = new ArrayList<>();
			for (final UvSchieneCreateRequest request : createRequests) {
				final var dto = buildNewDto(nextId++, request);
				entities.add(dto);
				updateJahrgangConstraints(dto.ID, dto.Planungsabschnitt_ID, request.idsJahrgaengeErlaubt);
			}
			repository.update(entities);
			repository.flush();
			return getList(entities.stream().map(e -> e.ID).toList());
		});
	}

	private static DTOUvSchiene buildNewDto(final long id, final UvSchieneCreateRequest request) {
		return new DTOUvSchiene(id, request.idPlanungsabschnitt, request.nummer,
				(request.bezeichnung != null) ? request.bezeichnung : "");
	}

	/**
	 * Führt einen Patch auf einer Schiene aus.
	 *
	 * @param patch   der Patch
	 *
	 * @return die gepatchte Schiene
	 */
	public UvSchiene patch(final UvSchienePatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches auf Schienen aus.
	 *
	 * @param patches   die Patches
	 *
	 * @return die gepatchten Schienen
	 */
	public List<UvSchiene> patchMultiple(final Collection<UvSchienePatchRequest> patches) {
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

	private void applyPatch(final DTOUvSchiene dto, final UvSchienePatchRequest patch) {
		patch.nummer.ifPresent(val -> dto.Nummer = val);
		patch.bezeichnung.ifPresent(val -> {
			if ((val != null) && (val.length() > Schema.tab_UV_Schienen.col_Bezeichnung.datenlaenge())) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung ist zu lang.");
			}
			dto.Bezeichnung = val;
		});
		patch.idsJahrgaengeErlaubt.ifPresent(val -> updateJahrgangConstraints(dto.ID, dto.Planungsabschnitt_ID, val));
	}

	private void updateJahrgangConstraints(final long idSchiene, final long idPlanungsabschnitt, final List<Long> newJahrgangIds) {
		final List<DTOUvSchienenConstraintJahrgang> existing = jahrgangRepository.getListBySchieneId(idSchiene);
		final Set<Long> oldSet = existing.stream().map(c -> c.Jahrgang_ID).collect(Collectors.toSet());
		final Set<Long> newSet = new HashSet<>(newJahrgangIds);
		final List<DTOUvSchienenConstraintJahrgang> toDelete = existing.stream()
				.filter(c -> !newSet.contains(c.Jahrgang_ID)).toList();
		final List<DTOUvSchienenConstraintJahrgang> toCreate = newSet.stream()
				.filter(id -> !oldSet.contains(id))
				.map(id -> new DTOUvSchienenConstraintJahrgang(idSchiene, id, idPlanungsabschnitt))
				.toList();
		jahrgangRepository.delete(new ArrayList<>(toDelete));
		jahrgangRepository.create(new ArrayList<>(toCreate));
	}

	/**
	 * Löscht eine Schiene.
	 *
	 * @param id   die ID
	 *
	 * @return die gelöschte Schiene
	 */
	public UvSchiene delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere Schienen.
	 *
	 * @param ids   die IDs
	 *
	 * @return die gelöschten Schienen
	 */
	public List<UvSchiene> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final var entities = repository.findListByIds(ids);
			final var result = toApiList(entities);
			repository.delete(entities);
			return result;
		});
	}

}
