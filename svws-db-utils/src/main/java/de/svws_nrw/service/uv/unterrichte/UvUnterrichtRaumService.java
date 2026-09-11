package de.svws_nrw.service.uv.unterrichte;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.uv.UvUnterrichtRaum;
import de.svws_nrw.db.dto.current.uv.DTOUvUnterrichtRaum;
import de.svws_nrw.db.dto.current.uv.DTOUvUnterrichtRaumPK;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.unterrichte.UvUnterrichtRaumRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Unterricht-Raum-Zuordnungen (Tabelle UV_Unterrichte_Raeume).
 */
public final class UvUnterrichtRaumService {

	private final UvUnterrichtRaumRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository   das Repository
	 */
	public UvUnterrichtRaumService(final UvUnterrichtRaumRepository repository) {
		this.repository = repository;
	}

	private static UvUnterrichtRaum toApi(final DTOUvUnterrichtRaum dto) {
		final var daten = new UvUnterrichtRaum();
		daten.idUnterricht = dto.Unterricht_ID;
		daten.idRaum = dto.Raum_ID;
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		return daten;
	}

	/**
	 * Liefert alle Unterricht-Raum-Zuordnungen eines Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Zuordnungen
	 */
	public List<UvUnterrichtRaum> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return repository.getListByPlanungsabschnitt(idPlanungsabschnitt).stream().map(UvUnterrichtRaumService::toApi).toList();
	}

	/**
	 * Erstellt eine neue Unterricht-Raum-Zuordnung.
	 *
	 * @param createRequest   der Create-Request
	 *
	 * @return die neue Zuordnung
	 */
	public UvUnterrichtRaum create(final UvUnterrichtRaumCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue Unterricht-Raum-Zuordnungen.
	 *
	 * @param createRequests   die Create-Requests
	 *
	 * @return die neuen Zuordnungen
	 */
	public List<UvUnterrichtRaum> createMultiple(final Collection<UvUnterrichtRaumCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			final List<DTOUvUnterrichtRaum> entities = new ArrayList<>();
			for (final UvUnterrichtRaumCreateRequest request : createRequests) {
				entities.add(new DTOUvUnterrichtRaum(request.idUnterricht, request.idRaum, request.idPlanungsabschnitt));
			}
			repository.update(entities);
			repository.flush();
			return entities.stream().map(UvUnterrichtRaumService::toApi).toList();
		});
	}

	/**
	 * Löscht eine Unterricht-Raum-Zuordnung.
	 *
	 * @param id   die zusammengesetzte ID
	 *
	 * @return die gelöschte Zuordnung
	 */
	public UvUnterrichtRaum delete(final DTOUvUnterrichtRaumPK id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere Unterricht-Raum-Zuordnungen.
	 *
	 * @param ids   die IDs
	 *
	 * @return die gelöschten Zuordnungen
	 */
	public List<UvUnterrichtRaum> deleteMultiple(final Collection<DTOUvUnterrichtRaumPK> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final List<DTOUvUnterrichtRaum> entities = new ArrayList<>();
			for (final DTOUvUnterrichtRaumPK id : ids) {
				entities.add(repository.getById(id));
			}
			final var result = entities.stream().map(UvUnterrichtRaumService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

}
