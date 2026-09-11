package de.svws_nrw.service.uv.lerngruppen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.uv.UvLerngruppenSchiene;
import de.svws_nrw.core.data.uv.UvLerngruppenSchieneCreateRequest;
import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppeSchiene;
import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppeSchienePK;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.lerngruppen.UvLerngruppenSchieneRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Lerngruppen-Schienen-Zuordnungen (Tabelle UV_LerngruppeSchiene).
 */
public final class UvLerngruppenSchieneService {

	private final UvLerngruppenSchieneRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param uvLerngruppenSchieneRepository   das Repository
	 */
	public UvLerngruppenSchieneService(final UvLerngruppenSchieneRepository uvLerngruppenSchieneRepository) {
		this.repository = uvLerngruppenSchieneRepository;
	}

	private static UvLerngruppenSchiene toApi(final DTOUvLerngruppeSchiene dto) {
		final var daten = new UvLerngruppenSchiene();
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		daten.idLerngruppe = dto.Lerngruppe_ID;
		daten.idSchiene = dto.Schiene_ID;
		return daten;
	}

	/**
	 * Liefert alle Lerngruppen-Schienen-Zuordnungen eines Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Zuordnungen
	 */
	public List<UvLerngruppenSchiene> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return repository.getListByPlanungsabschnitt(idPlanungsabschnitt).stream().map(UvLerngruppenSchieneService::toApi).toList();
	}

	/**
	 * Erstellt eine neue Lerngruppen-Schienen-Zuordnung.
	 *
	 * @param createRequest   der Create-Request
	 *
	 * @return die neue Zuordnung
	 */
	public UvLerngruppenSchiene create(final UvLerngruppenSchieneCreateRequest createRequest) {
		return transactional(() -> {
			final var neu = new DTOUvLerngruppeSchiene(createRequest.idLerngruppe, createRequest.idSchiene, createRequest.idPlanungsabschnitt);
			repository.update(neu);
			repository.flush();
			return toApi(repository.getById(new DTOUvLerngruppeSchienePK(neu.Lerngruppe_ID, neu.Schiene_ID)));
		});
	}

	/**
	 * Löscht eine Lerngruppen-Schienen-Zuordnung.
	 *
	 * @param id   die zusammengesetzte ID
	 *
	 * @return die gelöschte Zuordnung
	 */
	public UvLerngruppenSchiene delete(final DTOUvLerngruppeSchienePK id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere Lerngruppen-Schienen-Zuordnungen.
	 *
	 * @param ids   die Liste der zusammengesetzten IDs
	 *
	 * @return die gelöschten Zuordnungen
	 */
	public List<UvLerngruppenSchiene> deleteMultiple(final Collection<DTOUvLerngruppeSchienePK> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final List<DTOUvLerngruppeSchiene> entities = new ArrayList<>();
			for (final DTOUvLerngruppeSchienePK id : ids) {
				entities.add(repository.getById(id));
			}
			final var result = entities.stream().map(UvLerngruppenSchieneService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

}
