package de.svws_nrw.service.uv.zeitraster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.uv.UvPlanungsabschnittZeitraster;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittZeitraster;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittZeitrasterPK;
import de.svws_nrw.db.dto.current.uv.DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.zeitraster.UvPlanungsabschnittZeitrasterConstraintJahrgangRepository;
import de.svws_nrw.repo.uv.zeitraster.UvPlanungsabschnittZeitrasterRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Planungsabschnitt-Zeitraster-Zuordnungen
 * (Tabelle UV_Planungsabschnitte_Zeitraster).
 */
public final class UvPlanungsabschnittZeitrasterService {

	private final UvPlanungsabschnittZeitrasterRepository repository;
	private final UvPlanungsabschnittZeitrasterConstraintJahrgangRepository jahrgangRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository           das Repository für die Zeitraster-Zuordnungen
	 * @param jahrgangRepository   das Repository für die Jahrgang-Constraints
	 */
	public UvPlanungsabschnittZeitrasterService(final UvPlanungsabschnittZeitrasterRepository repository,
			final UvPlanungsabschnittZeitrasterConstraintJahrgangRepository jahrgangRepository) {
		this.repository = repository;
		this.jahrgangRepository = jahrgangRepository;
	}

	private UvPlanungsabschnittZeitraster toApi(final DTOUvPlanungsabschnittZeitraster dto) {
		final var daten = new UvPlanungsabschnittZeitraster();
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		daten.idZeitraster = dto.Zeitraster_ID;
		daten.idsJahrgaenge = jahrgangRepository.getListByPlanungsabschnittAndZeitraster(dto.Planungsabschnitt_ID, dto.Zeitraster_ID)
				.stream().map(c -> c.Jahrgang_ID).toList();
		return daten;
	}

	private List<UvPlanungsabschnittZeitraster> toApiList(final List<DTOUvPlanungsabschnittZeitraster> dtos) {
		if (dtos.isEmpty()) {
			return List.of();
		}
		final long idPlanungsabschnitt = dtos.getFirst().Planungsabschnitt_ID;
		final List<DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang> allConstraints =
				jahrgangRepository.getListByPlanungsabschnitt(idPlanungsabschnitt);
		final Map<Long, List<Long>> jahrgaengeByZeitraster = allConstraints.stream()
				.collect(Collectors.groupingBy(c -> c.Zeitraster_ID, Collectors.mapping(c -> c.Jahrgang_ID, Collectors.toList())));
		return dtos.stream().map(dto -> {
			final var daten = new UvPlanungsabschnittZeitraster();
			daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
			daten.idZeitraster = dto.Zeitraster_ID;
			daten.idsJahrgaenge = jahrgaengeByZeitraster.getOrDefault(dto.Zeitraster_ID, List.of());
			return daten;
		}).toList();
	}

	/**
	 * Ermittelt eine Zeitraster-Zuordnung anhand des zusammengesetzten Schlüssels.
	 *
	 * @param pk   der zusammengesetzte Primärschlüssel
	 *
	 * @return die Zeitraster-Zuordnung
	 */
	public UvPlanungsabschnittZeitraster get(final DTOUvPlanungsabschnittZeitrasterPK pk) {
		final var dto = repository.findById(pk);
		if (dto.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Keine Zeitraster-Zuordnung für Planungsabschnitt %d und Zeitraster %d gefunden."
							.formatted(pk.Planungsabschnitt_ID, pk.Zeitraster_ID));
		}
		return toApi(dto.get());
	}

	/**
	 * Liefert alle Zeitraster-Zuordnungen eines Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Zeitraster-Zuordnungen
	 */
	public List<UvPlanungsabschnittZeitraster> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return toApiList(repository.getListByPlanungsabschnitt(idPlanungsabschnitt));
	}

	/**
	 * Erstellt eine neue Zeitraster-Zuordnung.
	 *
	 * @param createRequest   der Create-Request
	 *
	 * @return die neue Zeitraster-Zuordnung
	 */
	public UvPlanungsabschnittZeitraster create(final UvPlanungsabschnittZeitrasterCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue Zeitraster-Zuordnungen.
	 *
	 * @param createRequests   die Create-Requests
	 *
	 * @return die neuen Zeitraster-Zuordnungen
	 */
	public List<UvPlanungsabschnittZeitraster> createMultiple(final Collection<UvPlanungsabschnittZeitrasterCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			final List<DTOUvPlanungsabschnittZeitraster> entities = new ArrayList<>();
			for (final UvPlanungsabschnittZeitrasterCreateRequest request : createRequests) {
				final var dto = new DTOUvPlanungsabschnittZeitraster(request.idPlanungsabschnitt, request.idZeitraster);
				entities.add(dto);
				updateJahrgangConstraints(request.idPlanungsabschnitt, request.idZeitraster, request.idsJahrgaenge);
			}
			repository.create(entities);
			repository.flush();
			return entities.stream().map(this::toApi).toList();
		});
	}

	/**
	 * Führt einen Patch auf einer Zeitraster-Zuordnung aus.
	 *
	 * @param patch   der Patch
	 *
	 * @return die gepatchte Zeitraster-Zuordnung
	 */
	public UvPlanungsabschnittZeitraster patch(final UvPlanungsabschnittZeitrasterPatchRequest patch) {
		return transactional(() -> {
			final var pk = new DTOUvPlanungsabschnittZeitrasterPK(patch.idPlanungsabschnitt, patch.idZeitraster);
			final var dto = repository.findById(pk);
			if (dto.isEmpty()) {
				throw new ApiOperationException(Status.NOT_FOUND,
						"Keine Zeitraster-Zuordnung für Planungsabschnitt %d und Zeitraster %d gefunden."
								.formatted(patch.idPlanungsabschnitt, patch.idZeitraster));
			}
			applyPatch(dto.get(), patch);
			repository.flush();
			return toApi(dto.get());
		});
	}

	private void applyPatch(final DTOUvPlanungsabschnittZeitraster dto, final UvPlanungsabschnittZeitrasterPatchRequest patch) {
		patch.idsJahrgaenge.ifPresent(val -> updateJahrgangConstraints(dto.Planungsabschnitt_ID, dto.Zeitraster_ID, val));
	}

	private void updateJahrgangConstraints(final long idPlanungsabschnitt, final long idZeitraster, final List<Long> newJahrgangIds) {
		final List<DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang> existing =
				jahrgangRepository.getListByPlanungsabschnittAndZeitraster(idPlanungsabschnitt, idZeitraster);
		final Set<Long> oldSet = existing.stream().map(c -> c.Jahrgang_ID).collect(Collectors.toSet());
		final Set<Long> newSet = new HashSet<>(newJahrgangIds);
		final List<DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang> toDelete = existing.stream()
				.filter(c -> !newSet.contains(c.Jahrgang_ID)).toList();
		final List<DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang> toCreate = newSet.stream()
				.filter(id -> !oldSet.contains(id))
				.map(id -> new DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang(idPlanungsabschnitt, idZeitraster, id))
				.toList();
		jahrgangRepository.delete(new ArrayList<>(toDelete));
		jahrgangRepository.create(new ArrayList<>(toCreate));
	}

	/**
	 * Löscht eine Zeitraster-Zuordnung.
	 *
	 * @param pk   der zusammengesetzte Primärschlüssel
	 *
	 * @return die gelöschte Zeitraster-Zuordnung
	 */
	public UvPlanungsabschnittZeitraster delete(final DTOUvPlanungsabschnittZeitrasterPK pk) {
		return deleteMultiple(pk.Planungsabschnitt_ID, List.of(pk.Zeitraster_ID)).getFirst();
	}

	/**
	 * Löscht mehrere Zeitraster-Zuordnungen eines Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 * @param zeitrasterIds         die IDs der zu löschenden Zeitraster
	 *
	 * @return die gelöschten Zeitraster-Zuordnungen
	 */
	public List<UvPlanungsabschnittZeitraster> deleteMultiple(final long idPlanungsabschnitt, final Collection<Long> zeitrasterIds) {
		if (zeitrasterIds == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final List<DTOUvPlanungsabschnittZeitrasterPK> pks = zeitrasterIds.stream()
					.map(zrId -> new DTOUvPlanungsabschnittZeitrasterPK(idPlanungsabschnitt, zrId))
					.toList();
			final List<DTOUvPlanungsabschnittZeitraster> entities = new ArrayList<>();
			for (final DTOUvPlanungsabschnittZeitrasterPK pk : pks) {
				final var dto = repository.findById(pk);
				if (dto.isEmpty()) {
					throw new ApiOperationException(Status.NOT_FOUND,
							"Keine Zeitraster-Zuordnung für Planungsabschnitt %d und Zeitraster %d gefunden."
									.formatted(pk.Planungsabschnitt_ID, pk.Zeitraster_ID));
				}
				entities.add(dto.get());
			}
			final var result = entities.stream().map(this::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

}
