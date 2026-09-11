package de.svws_nrw.service.uv.lehrer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrer;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrerCreateRequest;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittLehrer;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittLehrerPK;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.lehrer.UvPlanungsabschnittLehrerRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Planungsabschnitt-Lehrer-Zuordnungen (Tabelle UV_PlanungsabschnittLehrer).
 */
public final class UvPlanungsabschnittLehrerService {

	private final UvPlanungsabschnittLehrerRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param uvPlanungsabschnittLehrerRepository   das Repository
	 */
	public UvPlanungsabschnittLehrerService(final UvPlanungsabschnittLehrerRepository uvPlanungsabschnittLehrerRepository) {
		this.repository = uvPlanungsabschnittLehrerRepository;
	}

	private static UvPlanungsabschnittLehrer toApi(final DTOUvPlanungsabschnittLehrer dto) {
		final var daten = new UvPlanungsabschnittLehrer();
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		daten.idLehrer = dto.Lehrer_ID;
		return daten;
	}

	/**
	 * Liefert alle Planungsabschnitt-Lehrer-Zuordnungen eines Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Zuordnungen
	 */
	public List<UvPlanungsabschnittLehrer> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return repository.getListByPlanungsabschnitt(idPlanungsabschnitt).stream().map(UvPlanungsabschnittLehrerService::toApi).toList();
	}

	/**
	 * Erstellt eine neue Planungsabschnitt-Lehrer-Zuordnung.
	 *
	 * @param createRequest   der Create-Request
	 *
	 * @return die neue Zuordnung
	 */
	public UvPlanungsabschnittLehrer create(final UvPlanungsabschnittLehrerCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue Planungsabschnitt-Lehrer-Zuordnungen.
	 *
	 * @param createRequests   die Create-Requests
	 *
	 * @return die neuen Zuordnungen
	 */
	public List<UvPlanungsabschnittLehrer> createMultiple(final Collection<UvPlanungsabschnittLehrerCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			final List<DTOUvPlanungsabschnittLehrer> entities = new ArrayList<>();
			for (final UvPlanungsabschnittLehrerCreateRequest request : createRequests) {
				entities.add(new DTOUvPlanungsabschnittLehrer(request.idPlanungsabschnitt, request.idLehrer));
			}
			repository.update(entities);
			repository.flush();
			return entities.stream().map(e -> toApi(repository.getById(new DTOUvPlanungsabschnittLehrerPK(e.Planungsabschnitt_ID, e.Lehrer_ID)))).toList();
		});
	}

	/**
	 * Löscht eine Planungsabschnitt-Lehrer-Zuordnung.
	 *
	 * @param id   die zusammengesetzte ID
	 *
	 * @return die gelöschte Zuordnung
	 */
	public UvPlanungsabschnittLehrer delete(final DTOUvPlanungsabschnittLehrerPK id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere Planungsabschnitt-Lehrer-Zuordnungen.
	 *
	 * @param ids   die Liste der zusammengesetzten IDs
	 *
	 * @return die gelöschten Zuordnungen
	 */
	public List<UvPlanungsabschnittLehrer> deleteMultiple(final Collection<DTOUvPlanungsabschnittLehrerPK> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final List<DTOUvPlanungsabschnittLehrer> entities = new ArrayList<>();
			for (final DTOUvPlanungsabschnittLehrerPK id : ids) {
				entities.add(repository.getById(id));
			}
			final var result = entities.stream().map(UvPlanungsabschnittLehrerService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

}
