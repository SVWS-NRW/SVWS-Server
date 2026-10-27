package de.svws_nrw.service.schule.katalog.lernplattform;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Lernplattform;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.svws.auth.DTOLernplattformen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.lernplattform.LernplattformMapper;
import de.svws_nrw.repo.schule.kataloge.lernplattform.LernplattformRepository;
import de.svws_nrw.service.lehrer.lernplattform.LehrerLernplattformService;
import de.svws_nrw.service.schueler.lernplattform.SchuelerLernplattformService;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import jakarta.ws.rs.core.Response;

/**
 * Service-Klasse für die Verwaltung des Katalogs der Lernplattformen.
 * <p>
 * Stellt CRUD-Operationen für Lernplattformen bereit und führt Validierungen durch.
 */
public final class LernplattformService {

	private static final String BEZEICHNUNG_WIRD_BEREITS_VERWENDET = "Die Bezeichnung %s wird bereits verwendet.";

	private final LernplattformRepository repository;
	private final LernplattformMapper mapper;
	private final SchuelerLernplattformService schuelerLernplattformService;
	private final LehrerLernplattformService lehrerLernplattformService;

	/**
	 * Erstellt einen neuen {@link LernplattformService}.
	 *
	 * @param repository                    das Repository für den Lernplattformen-Katalog
	 * @param mapper                        der Mapper zwischen Datenbank-DTO und Core-DTO
	 * @param schuelerLernplattformService  der Service für die Zuordnungen von Schülern zu Lernplattformen
	 * @param lehrerLernplattformService    der Service für die Zuordnungen von Lehrern zu Lernplattformen
	 */
	public LernplattformService(final LernplattformRepository repository, final LernplattformMapper mapper,
			final SchuelerLernplattformService schuelerLernplattformService, final LehrerLernplattformService lehrerLernplattformService) {
		this.repository = repository;
		this.mapper = mapper;
		this.schuelerLernplattformService = schuelerLernplattformService;
		this.lehrerLernplattformService = lehrerLernplattformService;
	}

	/**
	 * Bestimmt alle Lernplattformen des Katalogs inklusive des Flags, ob sie in anderen Tabellen referenziert werden.
	 *
	 * @return die nach ID aufsteigend sortierten Lernplattformen
	 */
	public List<Lernplattform> getAll() {
		final List<DTOLernplattformen> entities = repository.getAll();
		final Set<Long> referencedIds = repository.getReferencedIds(entities.stream().map(e -> e.id).toList());
		return entities.stream()
				.sorted(Comparator.comparingLong(e -> e.id))
				.map(e -> toApi(e, referencedIds))
				.toList();
	}

	/**
	 * Erstellt eine neue Lernplattform und legt für alle Schüler und Lehrer Zuordnungen ohne Einwilligungen
	 * und ohne Zugangsdaten an. Für Erzieher werden keine Zuordnungen erzeugt. Die Operation läuft in einer Transaktion.
	 *
	 * @param dto   die Daten für die neue Lernplattform
	 *
	 * @return die erstellte Lernplattform
	 *
	 * @throws ApiOperationException   wenn die Bezeichnung bereits verwendet wird
	 */
	public Lernplattform create(final LernplattformCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			validateUniqueBezeichnung(dto.bezeichnung, null);
			final DTOLernplattformen created = repository.create(mapper.toDomain(dto));
			repository.flush();
			schuelerLernplattformService.addForLernplattform(created.id);
			lehrerLernplattformService.addForLernplattform(created.id);
			return toApi(created, repository.getReferencedIds(List.of(created.id)));
		});
	}

	/**
	 * Aktualisiert eine bestehende Lernplattform teilweise (PATCH). Nur die im Request angegebenen Felder
	 * werden aktualisiert. Die Operation läuft in einer Transaktion.
	 *
	 * @param id    die ID der zu aktualisierenden Lernplattform
	 * @param dto   die zu aktualisierenden Felder
	 *
	 * @return die aktualisierte Lernplattform
	 *
	 * @throws ApiOperationException   wenn die Bezeichnung bereits verwendet wird oder die Lernplattform nicht gefunden wurde
	 */
	public Lernplattform patch(final long id, final LernplattformPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final DTOLernplattformen entity = repository.findById(id)
					.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, "Die Lernplattform mit der ID %d wurde nicht gefunden.".formatted(id)));
			dto.bezeichnung.ifPresent(bezeichnung -> validateUniqueBezeichnung(bezeichnung, id));
			mapper.patch(dto, entity);
			return toApi(entity, repository.getReferencedIds(List.of(id)));
		});
	}

	/**
	 * Löscht mehrere Lernplattformen anhand ihrer IDs. Auch referenzierte Lernplattformen werden gelöscht, die
	 * Zuordnungen und Zugangsdaten werden dabei über die Fremdschlüssel (ON DELETE CASCADE) mit entfernt. Nicht
	 * gefundene IDs werden mit einer Fehlermeldung markiert. Die Operation läuft in einer Transaktion.
	 *
	 * @param idsToDelete   die IDs der zu löschenden Lernplattformen
	 *
	 * @return die Antworten mit dem Status jeder Löschoperation, aufsteigend nach ID sortiert
	 */
	public List<SimpleOperationResponse> deleteMultiple(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() ->
				BulkDeleteUtils.delete(idsToDelete, repository, e -> e.id, "Lernplattform"));
	}

	private Lernplattform toApi(final DTOLernplattformen entity, final Set<Long> referencedIds) {
		final Lernplattform lernplattform = mapper.toApi(entity);
		lernplattform.referenziertInAnderenTabellen = referencedIds.contains(entity.id);
		return lernplattform;
	}

	private void validateUniqueBezeichnung(final String bezeichnung, final Long idToExclude) {
		final boolean exists = (idToExclude == null)
				? repository.bezeichnungIsAlreadyUsedCreate(bezeichnung)
				: repository.bezeichnungIsAlreadyUsedPatch(bezeichnung, idToExclude);
		if (exists) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, BEZEICHNUNG_WIRD_BEREITS_VERWENDET.formatted(bezeichnung));
		}
	}

}
