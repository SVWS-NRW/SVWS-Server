package de.svws_nrw.service.schule.katalog.haltestelle;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Haltestelle;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.haltestelle.HaltestelleMapper;
import de.svws_nrw.repo.schule.kataloge.haltestelle.HaltestelleRepository;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import jakarta.ws.rs.core.Response;

/**
 * Service-Klasse für die Verwaltung von Haltestellen.
 * <p>
 * Stellt CRUD-Operationen für Haltestellen bereit und führt Validierungen durch.
 */
public final class HaltestelleService {

	private static final String BEZEICHNUNG_EXISTS = "Die Bezeichnung %s ist bereits vorhanden.";

	private static final String ID_NOT_FOUND = "Es wurde keine Haltestelle mit der ID %d gefunden.";

	private final HaltestelleRepository repository;
	private final HaltestelleMapper mapper;

	/**
	 * Erstellt einen neuen HaltestelleService.
	 *
	 * @param repository das Repository für den Datenbankzugriff auf Haltestellen
	 * @param mapper     der Mapper zur Konvertierung zwischen Domain- und API-Modellen
	 */
	public HaltestelleService(final HaltestelleRepository repository, final HaltestelleMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	/**
	 * Gibt alle Haltestellen zurück, aufsteigend nach ID sortiert. Bei jeder Haltestelle wird
	 * angegeben, ob sie von Schülern referenziert wird.
	 *
	 * @return eine Liste aller Haltestellen
	 */
	public List<Haltestelle> getAll() {
		final var entities = repository.getAll();
		final var referencedIds = repository.getReferencedIds(entities.stream().map(e -> e.id).toList());
		return entities.stream()
				.map(e -> map(e, referencedIds))
				.sorted(Comparator.comparingLong(h -> h.id))
				.toList();
	}

	private Haltestelle map(final DTOHaltestellen entity, final Set<Long> referencedIds) {
		final var haltestelle = mapper.toApi(entity);
		haltestelle.referenziertInAnderenTabellen = referencedIds.contains(entity.id);
		return haltestelle;
	}

	/**
	 * Erstellt eine neue Haltestelle. Die Bezeichnung muss (ohne Beachtung der Groß-/Kleinschreibung) eindeutig sein.
	 *
	 * @param dto die Daten der neuen Haltestelle
	 *
	 * @return die erstellte Haltestelle
	 *
	 * @throws ApiOperationException mit {@code 400 BAD_REQUEST}, wenn die Bezeichnung bereits vergeben ist
	 */
	public Haltestelle create(final HaltestelleCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			validateUniqueBezeichnung(dto.bezeichnung, null);
			final var created = repository.create(mapper.toDomain(dto));
			return mapper.toApi(created);
		});
	}

	/**
	 * Aktualisiert eine bestehende Haltestelle teilweise (PATCH). Nur die im Request gesetzten Felder werden geändert.
	 *
	 * @param id  die ID der zu aktualisierenden Haltestelle
	 * @param dto die zu ändernden Felder
	 *
	 * @return die aktualisierte Haltestelle
	 *
	 * @throws ApiOperationException mit {@code 404 NOT_FOUND}, wenn keine Haltestelle zu der ID existiert, und mit
	 *                               {@code 400 BAD_REQUEST}, wenn die Bezeichnung bereits bei einer anderen Haltestelle vergeben ist
	 */
	public Haltestelle patch(final long id, final HaltestellePatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var entity = repository.findById(id)
					.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, ID_NOT_FOUND.formatted(id)));
			dto.bezeichnung.ifPresent(bezeichnung -> validateUniqueBezeichnung(bezeichnung, id));
			mapper.patch(dto, entity);
			return mapper.toApi(entity);
		});
	}

	/**
	 * Löscht mehrere Haltestellen anhand ihrer IDs. Nicht gefundene IDs werden als Fehler in der Antwort markiert.
	 * Referenzierte Haltestellen werden nicht gelöscht, sondern mit einer Fehlermeldung markiert
	 *
	 * @param idsToDelete die IDs der zu löschenden Haltestellen
	 *
	 * @return eine Liste mit dem Ergebnis je ID, sortiert nach ID
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() ->
				BulkDeleteUtils.deleteWithReferenceCheck(
						idsToDelete,
						repository,
						e -> e.id,
						"Haltestelle"
				)
		);
	}

	private void validateUniqueBezeichnung(final String bezeichnung, final Long idToExclude) {
		final boolean bereitsVergeben = (idToExclude == null)
				? repository.bezeichnungExists(bezeichnung)
				: repository.bezeichnungIstBereitsVergebenExceptId(bezeichnung, idToExclude);
		if (bereitsVergeben) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, BEZEICHNUNG_EXISTS.formatted(bezeichnung));
		}
	}

}
