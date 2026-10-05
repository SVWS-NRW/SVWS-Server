package de.svws_nrw.service.schulwechsel.dokument;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.SchulwechselDokument;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schulwechsel.dokument.SchulwechselDokumentMapper;
import de.svws_nrw.repo.schulwechsel.dokument.SchulwechselDokumentRepository;
import de.svws_nrw.service.utils.BulkDeleteUtils;

public class SchulwechselDokumentService {

	private final SchulwechselDokumentRepository repository;
	private final SchulwechselDokumentMapper mapper;

	/**
	 * constructor
	 *
	 * @param repository {@link SchulwechselDokumentRepository}
	 * @param mapper {@link SchulwechselDokumentMapper}
	 */
	public SchulwechselDokumentService(final SchulwechselDokumentRepository repository, final SchulwechselDokumentMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	/**
	 * Gibt alle SchulwechselDokumente zurück.
	 *
	 * @return eine Liste aller SchulwechselDokumente
	 */
	public List<SchulwechselDokument> getAll() {
		return this.repository
				.getAll()
				.stream()
				.map(this.mapper::toApi)
				.toList();
	}

	/**
	 * Gibt ein Dokument anhand seiner ID zurück.
	 *
	 * @param id die ID des Dokuments
	 * @return das Dokument als API-Modell
	 * @throws ApiOperationException wenn kein Dokument mit der ID existiert
	 */
	public SchulwechselDokument getById(final long id) {
		return this.mapper.toApi(this.repository.getById(id));
	}

	/**
	 * Erstellt ein neues SchulwechselDokument.
	 * Validiert die Eingabedaten und erstellt das SchulwechselDokument in einer Transaktion.
	 *
	 * @param dto die Daten für das neue SchulwechselDokument
	 * @return das erstellte SchulwechselDokument
	 *
	 */
	public SchulwechselDokument create(final SchulwechselDokumentCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var schulwechselDokument = this.mapper.toDomain(dto);
			final var created = this.repository.create(schulwechselDokument);
			return this.mapper.toApi(created);
		});
	}


	/**
	 * Aktualisiert ein bestehendes SchulwechselDokument teilweise (PATCH).
	 * Nur die im Request angegebenen Felder werden aktualisiert.
	 * Die Operation wird in einer Transaktion ausgeführt.
	 *
	 * @param id  die ID des zu aktualisierenden SchulwechselDokuments
	 * @param dto die zu aktualisierenden Felder
	 * @return das aktualisierte SchulwechselDokument
	 */
	public SchulwechselDokument patch(final long id, final SchulwechselDokumentPatchRequest dto) {
		return TransactionSupport.transactional(
				() -> {
					final var entity = repository.getById(id);
					mapper.patch(dto, entity);
					return this.mapper.toApi(entity);
				}
		);
	}

	/**
	 * Löscht die Schulwechseldokumente mit den angegebenen IDs.
	 * Nicht gefundene IDs werden stillschweigend ignoriert.
	 * Jeder Eintrag in der Rückgabeliste enthält die ID und ob die Löschung erfolgreich war.
	 *
	 * @param idsToDelete Liste der zu löschenden SchulwechselDokument-IDs
	 * @return Liste von {@link SimpleOperationResponse}-Einträgen, aufsteigend nach ID sortiert
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() ->
				BulkDeleteUtils.delete(
						idsToDelete,
						repository,
						e -> e.id,
						"SchulwechselDokument"
				)
		);
	}

}
