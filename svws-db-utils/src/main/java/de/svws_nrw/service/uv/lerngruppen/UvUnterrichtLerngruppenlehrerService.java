package de.svws_nrw.service.uv.lerngruppen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.uv.UvUnterrichtLerngruppenlehrer;
import de.svws_nrw.db.dto.current.uv.DTOUvUnterrichteLerngruppenlehrer;
import de.svws_nrw.db.dto.current.uv.DTOUvUnterrichteLerngruppenlehrerPK;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.unterrichte.UvUnterrichtLerngruppenlehrerRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Unterricht-Lerngruppenlehrer-Zuordnungen (Tabelle UV_Unterrichte_Lerngruppenlehrer).
 */
public final class UvUnterrichtLerngruppenlehrerService {

	private final UvUnterrichtLerngruppenlehrerRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository   das Repository
	 */
	public UvUnterrichtLerngruppenlehrerService(final UvUnterrichtLerngruppenlehrerRepository repository) {
		this.repository = repository;
	}

	private static UvUnterrichtLerngruppenlehrer toApi(final DTOUvUnterrichteLerngruppenlehrer dto) {
		final var daten = new UvUnterrichtLerngruppenlehrer();
		daten.idUnterricht = dto.Unterricht_ID;
		daten.idLerngruppenLehrer = dto.LerngruppenLehrer_ID;
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		return daten;
	}

	/**
	 * Liefert alle Unterricht-Lerngruppenlehrer-Zuordnungen eines Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Zuordnungen
	 */
	public List<UvUnterrichtLerngruppenlehrer> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return repository.getListByPlanungsabschnitt(idPlanungsabschnitt).stream().map(UvUnterrichtLerngruppenlehrerService::toApi).toList();
	}

	/**
	 * Erstellt eine neue Unterricht-Lerngruppenlehrer-Zuordnung.
	 *
	 * @param createRequest   der Create-Request
	 *
	 * @return die neue Zuordnung
	 */
	public UvUnterrichtLerngruppenlehrer create(final UvUnterrichtLerngruppenlehrerCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue Unterricht-Lerngruppenlehrer-Zuordnungen.
	 *
	 * @param createRequests   die Create-Requests
	 *
	 * @return die neuen Zuordnungen
	 */
	public List<UvUnterrichtLerngruppenlehrer> createMultiple(final Collection<UvUnterrichtLerngruppenlehrerCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			final List<DTOUvUnterrichteLerngruppenlehrer> entities = new ArrayList<>();
			for (final UvUnterrichtLerngruppenlehrerCreateRequest request : createRequests) {
				entities.add(new DTOUvUnterrichteLerngruppenlehrer(request.idUnterricht, request.idLerngruppenLehrer, request.idPlanungsabschnitt));
			}
			repository.update(entities);
			repository.flush();
			return entities.stream().map(UvUnterrichtLerngruppenlehrerService::toApi).toList();
		});
	}

	/**
	 * Löscht eine Unterricht-Lerngruppenlehrer-Zuordnung.
	 *
	 * @param id   die zusammengesetzte ID
	 *
	 * @return die gelöschte Zuordnung
	 */
	public UvUnterrichtLerngruppenlehrer delete(final DTOUvUnterrichteLerngruppenlehrerPK id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere Unterricht-Lerngruppenlehrer-Zuordnungen.
	 *
	 * @param ids   die IDs
	 *
	 * @return die gelöschten Zuordnungen
	 */
	public List<UvUnterrichtLerngruppenlehrer> deleteMultiple(final Collection<DTOUvUnterrichteLerngruppenlehrerPK> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final List<DTOUvUnterrichteLerngruppenlehrer> entities = new ArrayList<>();
			for (final DTOUvUnterrichteLerngruppenlehrerPK id : ids) {
				entities.add(repository.getById(id));
			}
			final var result = entities.stream().map(UvUnterrichtLerngruppenlehrerService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

}
