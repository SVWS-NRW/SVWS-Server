package de.svws_nrw.service.schule.katalog.fahrschuelerart;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Fahrschuelerart;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFahrschuelerart;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.fahrschuelerart.FahrschuelerartMapper;
import de.svws_nrw.repo.schule.kataloge.fahrschuelerart.FahrschuelerartRepository;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import jakarta.ws.rs.core.Response;

/**
 * Service-Klasse für die Verwaltung des Katalogs der Fahrschülerarten.
 * <p>
 * Stellt CRUD-Operationen für Fahrschülerarten bereit und führt Validierungen durch.
 */
public final class FahrschuelerartService {

	private static final String BEZEICHNUNG_FAHRSCHUELERART = "Fahrschülerart";
	private static final String BEZEICHNUNG_WIRD_BEREITS_VERWENDET = "Die Bezeichnung %s ist bereits vorhanden.";
	private static final String DATEN_NOT_FOUND_BY_ID = "Es wurde keine Fahrschülerart mit der ID %d gefunden.";
	private static final String IDS_ZUM_LOESCHEN_UNGUELTIG = "Die Liste der zu löschenden IDs darf nicht null sein und keine null-Werte enthalten.";
	private final FahrschuelerartRepository repository;
	private final FahrschuelerartMapper mapper;

	/**
	 * Erstellt einen neuen {@link FahrschuelerartService}.
	 *
	 * @param repository   das Repository für den Fahrschülerarten-Katalog
	 * @param mapper       der Mapper zwischen Datenbank-DTO und Core-DTO
	 */
	public FahrschuelerartService(
			final FahrschuelerartRepository repository,
			final FahrschuelerartMapper mapper
	) {
		this.repository = repository;
		this.mapper = mapper;
	}

	/**
	 * Bestimmt alle Fahrschülerarten des Katalogs inklusive des Flags, ob sie in anderen Tabellen referenziert werden.
	 *
	 * @return die nach ID aufsteigend sortierten Fahrschülerarten
	 */
	public List<Fahrschuelerart> getAll() {
		final var entities = repository.getAll();
		final var ids = entities.stream()
				.map(e -> e.id)
				.toList();
		final var referencedIds = repository.getReferencedIds(ids);
		return entities.stream()
				.sorted(Comparator.comparingLong(e -> e.id))
				.map(e -> toApi(e, referencedIds))
				.toList();
	}

	/**
	 * Erstellt eine neue Fahrschülerart.
	 * Die Operation läuft in einer Transaktion.
	 *
	 * @param dto   die Daten für die neue Fahrschülerart
	 *
	 * @return die erstellte Fahrschülerart
	 *
	 * @throws ApiOperationException   wenn die Bezeichnung bereits verwendet wird
	 */
	public Fahrschuelerart create(final FahrschuelerartCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			validateUniqueBezeichnung(dto.bezeichnung, null);
			final var created = repository.create(mapper.toDomain(dto));
			repository.flush();
			return toApiWithReferences(created);
		});
	}

	/**
	 * Aktualisiert eine bestehende Fahrschülerart teilweise (PATCH). Nur die im Request angegebenen Felder
	 * werden aktualisiert. Die Operation läuft in einer Transaktion.
	 *
	 * @param id    die ID der zu aktualisierenden Fahrschülerart
	 * @param dto   die zu aktualisierenden Felder
	 *
	 * @return die aktualisierte Fahrschülerart
	 *
	 * @throws ApiOperationException   wenn die Bezeichnung bereits verwendet wird oder die Fahrschülerart nicht gefunden wurde
	 */
	public Fahrschuelerart patch(final long id, final FahrschuelerartPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var entity = findById(id);
			dto.bezeichnung.ifPresent(bezeichnung -> validateUniqueBezeichnung(bezeichnung, entity.id));
			mapper.patch(dto, entity);
			return toApiWithReferences(entity);
		});
	}

	/**
	 * Löscht mehrere Fahrschülerarten anhand ihrer IDs. Es erfolgt keine Prüfung, ob die Fahrschülerarten in anderen
	 * Tabellen referenziert werden. Nicht gefundene IDs werden mit einer Fehlermeldung markiert. Die Operation läuft
	 * in einer Transaktion.
	 *
	 * @param idsToDelete   die IDs der zu löschenden Fahrschülerarten
	 *
	 * @return die Antworten mit dem Status jeder Löschoperation, aufsteigend nach ID sortiert
	 */
	public List<SimpleOperationResponse> deleteMultiple(final List<Long> idsToDelete) {
		assertIdsZumLoeschenValid(idsToDelete);
		return TransactionSupport.transactional(() ->
				BulkDeleteUtils.delete(idsToDelete, repository, e -> e.id, BEZEICHNUNG_FAHRSCHUELERART));
	}

	private static void assertIdsZumLoeschenValid(final List<Long> ids) {
		if ((ids == null) || containsNull(ids)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, IDS_ZUM_LOESCHEN_UNGUELTIG);
		}
	}

	private static boolean containsNull(final List<Long> ids) {
		return ids.stream()
				.anyMatch(Objects::isNull);
	}

	private DTOFahrschuelerart findById(final long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND,
						DATEN_NOT_FOUND_BY_ID.formatted(id)));
	}

	private Fahrschuelerart toApi(final DTOFahrschuelerart entity, final Set<Long> referencedIds) {
		final var fahrschuelerart = mapper.toApi(entity);
		fahrschuelerart.referenziertInAnderenTabellen = referencedIds.contains(entity.id);
		return fahrschuelerart;
	}

	private Fahrschuelerart toApiWithReferences(final DTOFahrschuelerart entity) {
		return toApi(entity, repository.getReferencedIds(List.of(entity.id)));
	}

	private void validateUniqueBezeichnung(final String bezeichnung, final Long idToExclude) {
		final var exists = (idToExclude == null)
				? repository.bezeichnungIsAlreadyUsedCreate(bezeichnung)
				: repository.bezeichnungIsAlreadyUsedPatch(bezeichnung, idToExclude);
		if (exists) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, BEZEICHNUNG_WIRD_BEREITS_VERWENDET.formatted(bezeichnung));
		}
	}
}
