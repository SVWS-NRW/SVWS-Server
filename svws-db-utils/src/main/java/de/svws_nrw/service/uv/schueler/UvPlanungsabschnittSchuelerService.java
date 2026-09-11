package de.svws_nrw.service.uv.schueler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.uv.UvPlanungsabschnittSchueler;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittSchueler;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittSchuelerPK;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.schueler.UvPlanungsabschnittSchuelerRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Planungsabschnitt-Schüler-Zuordnungen (Tabelle UV_PlanungsabschnittSchueler).
 */
public final class UvPlanungsabschnittSchuelerService {

	private final UvPlanungsabschnittSchuelerRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository   das Repository
	 */
	public UvPlanungsabschnittSchuelerService(final UvPlanungsabschnittSchuelerRepository repository) {
		this.repository = repository;
	}

	private static UvPlanungsabschnittSchueler toApi(final DTOUvPlanungsabschnittSchueler dto) {
		final var daten = new UvPlanungsabschnittSchueler();
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		daten.idSchueler = dto.Schueler_ID;
		daten.idJahrgang = dto.Jahrgang_ID;
		daten.idKlasse = dto.Klasse_ID;
		return daten;
	}

	private List<UvPlanungsabschnittSchueler> enrich(final List<UvPlanungsabschnittSchueler> uvList) {
		if (uvList.isEmpty()) {
			return uvList;
		}
		final Map<Long, DTOSchueler> schuelerMap = repository.getSchuelerByIds(uvList.stream().map(l -> l.idSchueler).toList()).stream()
				.collect(Collectors.toMap(l -> l.ID, Function.identity()));
		uvList.forEach(uv -> uv.daten = DataSchuelerliste.mapToSchueler(schuelerMap.get(uv.idSchueler), null));
		return uvList;
	}

	/**
	 * Ermittelt die Zuordnung anhand der zusammengesetzten ID.
	 *
	 * @param id   die zusammengesetzte ID
	 *
	 * @return die Zuordnung
	 */
	public UvPlanungsabschnittSchueler get(final DTOUvPlanungsabschnittSchuelerPK id) {
		return enrich(List.of(toApi(repository.getById(id)))).getFirst();
	}

	/**
	 * Ermittelt die Zuordnungen anhand der IDs.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Liste
	 */
	public List<UvPlanungsabschnittSchueler> getList(final Collection<DTOUvPlanungsabschnittSchuelerPK> ids) {
		final List<DTOUvPlanungsabschnittSchueler> result = new ArrayList<>();
		for (final DTOUvPlanungsabschnittSchuelerPK id : ids) {
			result.add(repository.getById(id));
		}
		return enrich(result.stream().map(UvPlanungsabschnittSchuelerService::toApi).toList());
	}

	/**
	 * Liefert alle Planungsabschnitt-Schüler-Zuordnungen eines Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Zuordnungen
	 */
	public List<UvPlanungsabschnittSchueler> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return enrich(repository.getListByPlanungsabschnitt(idPlanungsabschnitt).stream().map(UvPlanungsabschnittSchuelerService::toApi).toList());
	}

	/**
	 * Erstellt eine neue Planungsabschnitt-Schüler-Zuordnung.
	 *
	 * @param createRequest   der Create-Request
	 *
	 * @return die neue Zuordnung
	 */
	public UvPlanungsabschnittSchueler create(final UvPlanungsabschnittSchuelerCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue Planungsabschnitt-Schüler-Zuordnungen.
	 *
	 * @param createRequests   die Create-Requests
	 *
	 * @return die neuen Zuordnungen
	 */
	public List<UvPlanungsabschnittSchueler> createMultiple(final Collection<UvPlanungsabschnittSchuelerCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			final List<DTOUvPlanungsabschnittSchueler> entities = new ArrayList<>();
			for (final UvPlanungsabschnittSchuelerCreateRequest request : createRequests) {
				final DTOUvPlanungsabschnittSchueler dto =
						new DTOUvPlanungsabschnittSchueler(request.idPlanungsabschnitt, request.idSchueler, request.idJahrgang);
				dto.Klasse_ID = request.idKlasse;
				entities.add(dto);
			}
			repository.update(entities);
			repository.flush();
			return getList(entities.stream().map(e -> new DTOUvPlanungsabschnittSchuelerPK(e.Planungsabschnitt_ID, e.Schueler_ID)).toList());
		});
	}

	/**
	 * Führt einen Patch auf einer Planungsabschnitt-Schüler-Zuordnung aus.
	 *
	 * @param patch   der Patch
	 *
	 * @return die gepatchte Zuordnung
	 */
	public UvPlanungsabschnittSchueler patch(final UvPlanungsabschnittSchuelerPatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches auf Planungsabschnitt-Schüler-Zuordnungen aus.
	 *
	 * @param patches   die Patches
	 *
	 * @return die gepatchten Zuordnungen
	 */
	public List<UvPlanungsabschnittSchueler> patchMultiple(final Collection<UvPlanungsabschnittSchuelerPatchRequest> patches) {
		if (patches.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			final var ids = patches.stream().map(p -> new DTOUvPlanungsabschnittSchuelerPK(p.idPlanungsabschnitt, p.idSchueler)).toList();
			final List<DTOUvPlanungsabschnittSchueler> entities = new ArrayList<>();
			for (final DTOUvPlanungsabschnittSchuelerPK id : ids) {
				entities.add(repository.getById(id));
			}
			for (final var patch : patches) {
				final var entity = repository.getById(new DTOUvPlanungsabschnittSchuelerPK(patch.idPlanungsabschnitt, patch.idSchueler));
				applyPatch(entity, patch);
			}
			repository.update(entities);
			repository.flush();
			return getList(ids);
		});
	}

	private static void applyPatch(final DTOUvPlanungsabschnittSchueler dto, final UvPlanungsabschnittSchuelerPatchRequest patch) {
		patch.idJahrgang.ifPresent(val -> dto.Jahrgang_ID = val);
		if (patch.idKlasse.isPresent()) {
			dto.Klasse_ID = patch.idKlasse.orElse(null);
		}
	}

	/**
	 * Löscht eine Planungsabschnitt-Schüler-Zuordnung.
	 *
	 * @param id   die zusammengesetzte ID
	 *
	 * @return die gelöschte Zuordnung
	 */
	public UvPlanungsabschnittSchueler delete(final DTOUvPlanungsabschnittSchuelerPK id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere Planungsabschnitt-Schüler-Zuordnungen.
	 *
	 * @param ids   die Liste der zusammengesetzten IDs
	 *
	 * @return die gelöschten Zuordnungen
	 */
	public List<UvPlanungsabschnittSchueler> deleteMultiple(final Collection<DTOUvPlanungsabschnittSchuelerPK> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final List<DTOUvPlanungsabschnittSchueler> entities = new ArrayList<>();
			for (final DTOUvPlanungsabschnittSchuelerPK id : ids) {
				entities.add(repository.getById(id));
			}
			final var result = enrich(entities.stream().map(UvPlanungsabschnittSchuelerService::toApi).toList());
			repository.delete(entities);
			return result;
		});
	}

}
