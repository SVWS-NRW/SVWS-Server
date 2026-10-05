package de.svws_nrw.service.schulwechsel.abgang;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.SchulwechselAbgang;
import de.svws_nrw.core.types.schule.StatusSchulwechselAbgang;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schulwechsel.abgang.SchulwechselAbgangMapper;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schulwechsel.abgang.SchulwechselAbgangRepository;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import jakarta.ws.rs.core.Response;

public class SchulwechselAbgangService {

	private final SchulwechselAbgangRepository repository;
	private final SchulwechselAbgangMapper mapper;

	private final SchuelerRepository schuelerRepository;

	/**
	 * constructor
	 *
	 * @param repository {@link SchulwechselAbgangRepository}
	 * @param mapper {@link SchulwechselAbgangMapper}
	 * @param schuelerRepository {@link SchuelerRepository}
	 */
	public SchulwechselAbgangService(final SchulwechselAbgangRepository repository, final SchuelerRepository schuelerRepository, final SchulwechselAbgangMapper mapper) {
		this.repository = repository;
		this.schuelerRepository = schuelerRepository;
		this.mapper = mapper;
	}

	/**
	 * Gibt alle SchulwechselAbgang-Elemente zurück.
	 *
	 * @return eine Liste aller SchulwechselAbgang-Elemente
	 */
	public List<SchulwechselAbgang> getAll() {
		return this.repository
				.getAll()
				.stream()
				.map(this.mapper::toApi)
				.toList();
	}

	/**
	 * Gibt ein SchulwechselAbgang-Element anhand seiner ID zurück.
	 *
	 * @param id die ID des SchulwechselAbgang-Elements
	 * @return das Dokument als API-Modell
	 * @throws ApiOperationException wenn kein SchulwechselAbgang-Element mit der ID existiert
	 */
	public SchulwechselAbgang getById(final long id) {
		return this.mapper.toApi(this.repository.getById(id));
	}

	/**
	 * Erstellt ein neues SchulwechselAbgang-Element.
	 * Validiert die Eingabedaten und erstellt das SchulwechselAbgang-Element in einer Transaktion.
	 *
	 * @param dto die Daten für das neue SchulwechselAbgang-Element
	 * @return das erstellte SchulwechselAbgang-Element
	 *
	 */
	public SchulwechselAbgang create(final SchulwechselAbgangCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			validateIdSchueler(dto.idSchueler);
			validateStatus(dto.idStatus);
			final var schulwechselAbgang = this.mapper.toDomain(dto);
			final var created = this.repository.create(schulwechselAbgang);
			return this.mapper.toApi(created);
		});
	}


	/**
	 * Aktualisiert ein bestehendes SchulwechselAbgang-Element teilweise (PATCH).
	 * Nur die im Request angegebenen Felder werden aktualisiert.
	 * Die Operation wird in einer Transaktion ausgeführt.
	 *
	 * @param id  die ID des zu aktualisierenden SchulwechselAbgang-Elements
	 * @param dto die zu aktualisierenden Felder
	 * @return das aktualisierte SchulwechselAbgang-Element
	 */
	public SchulwechselAbgang patch(final long id, final SchulwechselAbgangPatchRequest dto) {
		return TransactionSupport.transactional(
				() -> {
					dto.idStatus.ifPresent(this::validateStatus);
					final var entity = repository.getById(id);
					mapper.patch(dto, entity);
					return this.mapper.toApi(entity);
				}
		);
	}

	/**
	 * Löscht die SchulwechselAbgang-Elemente mit den angegebenen IDs.
	 * Nicht gefundene IDs werden stillschweigend ignoriert.
	 * Jeder Eintrag in der Rückgabeliste enthält die ID und ob die Löschung erfolgreich war.
	 *
	 * @param idsToDelete Liste der zu löschenden SchulwechselAbgang-IDs
	 * @return Liste von {@link SimpleOperationResponse}-Einträgen, aufsteigend nach ID sortiert
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() ->
				BulkDeleteUtils.delete(
						idsToDelete,
						repository,
						e -> e.id,
						"SchulwechselAbgang"
				)
		);
	}

	private void validateIdSchueler(final Long idSchueler) {
		if (idSchueler == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Schüler-ID darf nicht null sein.");
		}
		schuelerRepository.findById(idSchueler)
				.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, String.format("Es wurde kein Schüler mit der ID %s in der Datenbank gefunden.", idSchueler)));
	}

	private void validateStatus(final Integer idStatus) {
		if (StatusSchulwechselAbgang.getByIdOrNull(idStatus) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST,
					String.format("Ungültiger Status mit der ID %s.", idStatus));
		}
	}

}
