package de.svws_nrw.service.uv.schueler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.uv.UvSchuelergruppeSchueler;
import de.svws_nrw.core.data.uv.UvSchuelergruppeSchuelerCreateRequest;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeSchueler;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeSchuelerPK;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.schueler.UvSchuelergruppeSchuelerRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die UV-Schülergruppe-Schüler-Zuordnungen (Tabelle UV_Schuelergruppen_Schueler).
 */
public final class UvSchuelergruppeSchuelerService {

	private final UvSchuelergruppeSchuelerRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository   das Repository
	 */
	public UvSchuelergruppeSchuelerService(final UvSchuelergruppeSchuelerRepository repository) {
		this.repository = repository;
	}

	private static UvSchuelergruppeSchueler toApi(final DTOUvSchuelergruppeSchueler dto) {
		final var daten = new UvSchuelergruppeSchueler();
		daten.idSchuelergruppe = dto.Schuelergruppe_ID;
		daten.idSchueler = dto.Schueler_ID;
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		return daten;
	}

	/**
	 * Liefert eine Schülergruppen-Schüler-Zuordnung anhand ihrer zusammengesetzten ID.
	 *
	 * @param id   die zusammengesetzte ID
	 *
	 * @return die Zuordnung
	 */
	public UvSchuelergruppeSchueler get(final DTOUvSchuelergruppeSchuelerPK id) {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die UV-Schülergruppe-Schüler-Zuordnung darf nicht null sein.");
		}
		return repository.findById(id)
				.map(UvSchuelergruppeSchuelerService::toApi)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND,
						"Keine Zuordnung für Schülergruppe %d und Schüler %d gefunden.".formatted(id.Schuelergruppe_ID, id.Schueler_ID)));
	}

	/**
	 * Liefert alle Schülergruppen-Schüler-Zuordnungen eines Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Zuordnungen
	 */
	public List<UvSchuelergruppeSchueler> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return repository.getListByPlanungsabschnitt(idPlanungsabschnitt).stream().map(UvSchuelergruppeSchuelerService::toApi).toList();
	}

	/**
	 * Erstellt eine neue Schülergruppen-Schüler-Zuordnung.
	 *
	 * @param createRequest   der Create-Request
	 *
	 * @return die neue Zuordnung
	 */
	public UvSchuelergruppeSchueler create(final UvSchuelergruppeSchuelerCreateRequest createRequest) {
		return createMultiple(List.of(createRequest)).getFirst();
	}

	/**
	 * Erstellt mehrere neue Schülergruppen-Schüler-Zuordnungen.
	 *
	 * @param createRequests   die Create-Requests
	 *
	 * @return die neuen Zuordnungen
	 */
	public List<UvSchuelergruppeSchueler> createMultiple(final Collection<UvSchuelergruppeSchuelerCreateRequest> createRequests) {
		if (createRequests.isEmpty()) {
			return new ArrayList<>();
		}
		return transactional(() -> {
			final List<DTOUvSchuelergruppeSchueler> entities = new ArrayList<>();
			for (final UvSchuelergruppeSchuelerCreateRequest request : createRequests) {
				entities.add(new DTOUvSchuelergruppeSchueler(request.idSchuelergruppe, request.idSchueler, request.idPlanungsabschnitt));
			}
			repository.update(entities);
			repository.flush();
			return entities.stream()
					.map(entity -> get(new DTOUvSchuelergruppeSchuelerPK(entity.Schuelergruppe_ID, entity.Schueler_ID)))
					.toList();
		});
	}

	/**
	 * Löscht eine Schülergruppen-Schüler-Zuordnung.
	 *
	 * @param id   die zusammengesetzte ID
	 *
	 * @return die gelöschte Zuordnung
	 */
	public UvSchuelergruppeSchueler delete(final DTOUvSchuelergruppeSchuelerPK id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere Schülergruppen-Schüler-Zuordnungen.
	 *
	 * @param ids   die Liste der zusammengesetzten IDs
	 *
	 * @return die gelöschten Zuordnungen
	 */
	public List<UvSchuelergruppeSchueler> deleteMultiple(final Collection<DTOUvSchuelergruppeSchuelerPK> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final List<DTOUvSchuelergruppeSchueler> entities = new ArrayList<>();
			for (final DTOUvSchuelergruppeSchuelerPK id : ids) {
				entities.add(repository.getById(id));
			}
			final var result = entities.stream().map(UvSchuelergruppeSchuelerService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

}
