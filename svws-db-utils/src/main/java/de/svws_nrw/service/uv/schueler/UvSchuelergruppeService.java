package de.svws_nrw.service.uv.schueler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.uv.UvSchuelergruppe;
import de.svws_nrw.core.data.uv.UvSchuelergruppeCreateRequest;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppe;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeConstraintJahrgang;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeConstraintSchuelergruppe;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.schueler.UvSchuelergruppeConstraintJahrgangRepository;
import de.svws_nrw.repo.uv.schueler.UvSchuelergruppeConstraintSchuelergruppeRepository;
import de.svws_nrw.repo.uv.schueler.UvSchuelergruppeRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Schülergruppen (Tabelle UV_Schuelergruppen).
 */
public final class UvSchuelergruppeService {

	private final UvSchuelergruppeRepository repository;
	private final UvSchuelergruppeConstraintJahrgangRepository jahrgangRepository;
	private final UvSchuelergruppeConstraintSchuelergruppeRepository gruppeRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository          das Repository für die Schülergruppen
	 * @param jahrgangRepository  das Repository für die Jahrgang-Constraints
	 * @param gruppeRepository    das Repository für die Gruppen-Constraints
	 */
	public UvSchuelergruppeService(final UvSchuelergruppeRepository repository,
			final UvSchuelergruppeConstraintJahrgangRepository jahrgangRepository,
			final UvSchuelergruppeConstraintSchuelergruppeRepository gruppeRepository) {
		this.repository = repository;
		this.jahrgangRepository = jahrgangRepository;
		this.gruppeRepository = gruppeRepository;
	}

	private UvSchuelergruppe toApi(final DTOUvSchuelergruppe dto) {
		final var daten = new UvSchuelergruppe();
		daten.id = dto.ID;
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		daten.bezeichnung = dto.Bezeichnung;
		daten.idsJahrgaengeErlaubt = jahrgangRepository.getListBySchuelergruppe(dto.ID).stream().map(c -> c.Jahrgang_ID).toList();
		daten.idsGruppenErlaubt = gruppeRepository.getListBySchuelergruppe(dto.ID).stream().map(c -> c.Schuelergruppe_Vaild_ID).toList();
		return daten;
	}

	private List<UvSchuelergruppe> toApiList(final List<DTOUvSchuelergruppe> dtos) {
		if (dtos.isEmpty()) {
			return List.of();
		}
		final long idPlanungsabschnitt = dtos.getFirst().Planungsabschnitt_ID;
		final Map<Long, List<Long>> jahrgaengeByGruppe = jahrgangRepository.getListByPlanungsabschnitt(idPlanungsabschnitt).stream()
				.collect(Collectors.groupingBy(c -> c.Schuelergruppe_ID, Collectors.mapping(c -> c.Jahrgang_ID, Collectors.toList())));
		final Map<Long, List<Long>> gruppenByGruppe = gruppeRepository.getListByPlanungsabschnitt(idPlanungsabschnitt).stream()
				.collect(Collectors.groupingBy(c -> c.Schuelergruppe_ID, Collectors.mapping(c -> c.Schuelergruppe_Vaild_ID, Collectors.toList())));
		return dtos.stream().map(dto -> {
			final var daten = new UvSchuelergruppe();
			daten.id = dto.ID;
			daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
			daten.bezeichnung = dto.Bezeichnung;
			daten.idsJahrgaengeErlaubt = jahrgaengeByGruppe.getOrDefault(dto.ID, List.of());
			daten.idsGruppenErlaubt = gruppenByGruppe.getOrDefault(dto.ID, List.of());
			return daten;
		}).toList();
	}

	/**
	 * Ermittelt die Schülergruppe anhand der ID.
	 *
	 * @param id   die ID
	 *
	 * @return die Schülergruppe
	 */
	public UvSchuelergruppe get(final long id) {
		final var dto = repository.findById(id);
		if (dto.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine UvSchuelergruppe mit der ID %d gefunden.".formatted(id));
		}
		return toApi(dto.get());
	}

	/**
	 * Ermittelt die Schülergruppen anhand der IDs.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Liste der Schülergruppen
	 */
	public List<UvSchuelergruppe> getList(final Collection<Long> ids) {
		final var result = repository.findListByIds(ids);
		if (result.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle UvSchuelergruppen zu den IDs gefunden (%d von %d).".formatted(result.size(), ids.size()));
		}
		return toApiList(result);
	}

	/**
	 * Liefert alle Schülergruppen eines Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Schülergruppen
	 */
	public List<UvSchuelergruppe> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return toApiList(repository.getListByPlanungsabschnitt(idPlanungsabschnitt));
	}

	/**
	 * Erstellt eine neue Schülergruppe.
	 *
	 * @param createRequest   der Create-Request
	 *
	 * @return die neue Schülergruppe
	 */
	public UvSchuelergruppe create(final UvSchuelergruppeCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue Schülergruppen.
	 *
	 * @param createRequests   die Create-Requests
	 *
	 * @return die neuen Schülergruppen
	 */
	public List<UvSchuelergruppe> createMultiple(final Collection<UvSchuelergruppeCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			final List<UvSchuelergruppeCreateRequest> requests = new ArrayList<>(createRequests);
			long nextId = repository.getNextID();
			final List<DTOUvSchuelergruppe> entities = new ArrayList<>();
			for (final UvSchuelergruppeCreateRequest request : requests) {
				entities.add(new DTOUvSchuelergruppe(nextId++, request.idPlanungsabschnitt, request.bezeichnung));
			}
			repository.update(entities);
			for (int i = 0; i < entities.size(); i++) {
				final DTOUvSchuelergruppe dto = entities.get(i);
				final UvSchuelergruppeCreateRequest request = requests.get(i);
				updateJahrgangConstraints(dto.ID, dto.Planungsabschnitt_ID, request.idsJahrgaengeErlaubt);
				updateGruppenConstraints(dto.ID, dto.Planungsabschnitt_ID, request.idsGruppenErlaubt);
			}
			repository.flush();
			return getList(entities.stream().map(e -> e.ID).toList());
		});
	}

	/**
	 * Führt einen Patch für eine Schülergruppe aus.
	 *
	 * @param patch   der Patch
	 *
	 * @return die gepatchte Schülergruppe
	 */
	public UvSchuelergruppe patch(final UvSchuelergruppePatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches auf Schülergruppen aus.
	 *
	 * @param patches   die Patches
	 *
	 * @return die Liste mit den gepatchten Schülergruppen
	 */
	public List<UvSchuelergruppe> patchMultiple(final Collection<UvSchuelergruppePatchRequest> patches) {
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

	private void applyPatch(final DTOUvSchuelergruppe dto, final UvSchuelergruppePatchRequest patch) {
		patch.bezeichnung.ifPresent(val -> dto.Bezeichnung = val);
		patch.idsJahrgaengeErlaubt.ifPresent(val -> updateJahrgangConstraints(dto.ID, dto.Planungsabschnitt_ID, val));
		patch.idsGruppenErlaubt.ifPresent(val -> updateGruppenConstraints(dto.ID, dto.Planungsabschnitt_ID, val));
	}

	private void updateJahrgangConstraints(final long idSchuelergruppe, final long idPlanungsabschnitt, final List<Long> newIds) {
		if (newIds == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Attribut 'idsJahrgaengeErlaubt' darf nicht null sein.");
		}
		final List<DTOUvSchuelergruppeConstraintJahrgang> existing = jahrgangRepository.getListBySchuelergruppe(idSchuelergruppe);
		final Set<Long> oldSet = existing.stream().map(c -> c.Jahrgang_ID).collect(Collectors.toSet());
		final Set<Long> newSet = new HashSet<>(newIds);
		final List<DTOUvSchuelergruppeConstraintJahrgang> toDelete = existing.stream()
				.filter(c -> !newSet.contains(c.Jahrgang_ID)).toList();
		final List<DTOUvSchuelergruppeConstraintJahrgang> toCreate = newSet.stream()
				.filter(id -> !oldSet.contains(id))
				.map(id -> new DTOUvSchuelergruppeConstraintJahrgang(idSchuelergruppe, id, idPlanungsabschnitt))
				.toList();
		jahrgangRepository.delete(new ArrayList<>(toDelete));
		jahrgangRepository.create(new ArrayList<>(toCreate));
	}

	private void updateGruppenConstraints(final long idSchuelergruppe, final long idPlanungsabschnitt, final List<Long> newIds) {
		if (newIds == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Attribut 'idsGruppenErlaubt' darf nicht null sein.");
		}
		final List<DTOUvSchuelergruppeConstraintSchuelergruppe> existing = gruppeRepository.getListBySchuelergruppe(idSchuelergruppe);
		final Set<Long> oldSet = existing.stream().map(c -> c.Schuelergruppe_Vaild_ID).collect(Collectors.toSet());
		final Set<Long> newSet = new HashSet<>(newIds);
		final List<DTOUvSchuelergruppeConstraintSchuelergruppe> toDelete = existing.stream()
				.filter(c -> !newSet.contains(c.Schuelergruppe_Vaild_ID)).toList();
		final List<DTOUvSchuelergruppeConstraintSchuelergruppe> toCreate = newSet.stream()
				.filter(id -> !oldSet.contains(id))
				.map(id -> new DTOUvSchuelergruppeConstraintSchuelergruppe(idSchuelergruppe, id, idPlanungsabschnitt))
				.toList();
		gruppeRepository.delete(new ArrayList<>(toDelete));
		gruppeRepository.create(new ArrayList<>(toCreate));
	}

	/**
	 * Löscht die Schülergruppe mit der ID.
	 *
	 * @param id   die ID
	 *
	 * @return die gelöschte Schülergruppe
	 */
	public UvSchuelergruppe delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere Schülergruppen anhand ihrer IDs.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Liste der gelöschten Schülergruppen
	 */
	public List<UvSchuelergruppe> deleteMultiple(final Collection<Long> ids) {
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
