package de.svws_nrw.service.schule.katalog.einwilligungsart;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import de.svws_nrw.asd.types.schule.Einwilligungsschluessel;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Einwilligungsart;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.katalog.DTOEinwilligungsart;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.einwilligungsart.EinwilligungsartMapper;
import de.svws_nrw.repo.schule.kataloge.einwilligungsart.EinwilligungsartRepository;
import de.svws_nrw.service.lehrer.einwilligung.LehrerEinwilligungService;
import de.svws_nrw.service.schueler.einwilligung.SchuelerEinwilligungService;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import jakarta.ws.rs.core.Response;

/**
 * Service-Klasse für die Verwaltung des Katalogs der Einwilligungsarten.
 * <p>
 * Stellt CRUD-Operationen für Einwilligungsarten bereit und führt Validierungen durch.
 */
public final class EinwilligungsartService {

	private static final String BEZEICHNUNG_WIRD_BEREITS_VERWENDET = "Die Bezeichnung %s wird bereits verwendet.";
	private static final String SCHLUESSEL_WIRD_BEREITS_VERWENDET = "Der Schlüssel %s wird bereits verwendet.";

	private final EinwilligungsartRepository repository;
	private final EinwilligungsartMapper mapper;
	private final SchuelerEinwilligungService schuelerEinwilligungService;
	private final LehrerEinwilligungService lehrerEinwilligungService;

	/**
	 * Erstellt einen neuen {@link EinwilligungsartService}.
	 *
	 * @param repository                    das Repository für den Einwilligungsarten-Katalog
	 * @param mapper                        der Mapper zwischen Datenbank-DTO und Core-DTO
	 * @param schuelerEinwilligungService   der Service für die Einwilligungen von Schülern
	 * @param lehrerEinwilligungService     der Service für die Einwilligungen von Lehrern
	 */
	public EinwilligungsartService(
			final EinwilligungsartRepository repository,
			final EinwilligungsartMapper mapper,
			final SchuelerEinwilligungService schuelerEinwilligungService,
			final LehrerEinwilligungService lehrerEinwilligungService
	) {
		this.repository = repository;
		this.mapper = mapper;
		this.schuelerEinwilligungService = schuelerEinwilligungService;
		this.lehrerEinwilligungService = lehrerEinwilligungService;
	}

	/**
	 * Bestimmt alle Einwilligungsarten des Katalogs inklusive des Flags, ob sie in anderen Tabellen referenziert werden.
	 *
	 * @return die nach ID aufsteigend sortierten Einwilligungsarten
	 */
	public List<Einwilligungsart> getAll() {
		final var entities = repository.getAll();
		final var referencedIds = repository.getReferencedIds(entities.stream().map(e -> e.id).toList());
		return entities.stream()
				.sorted(Comparator.comparingLong(e -> e.id))
				.map(e -> toApi(e, referencedIds))
				.toList();
	}

	/**
	 * Erstellt eine neue Einwilligungsart und legt für alle Schüler bzw. alle Lehrer, abhängig vom Personentyp, Einwilligungen
	 * an, die weder erteilt noch abgefragt sind. Die Operation läuft in einer Transaktion.
	 *
	 * @param dto   die Daten für die neue Einwilligungsart
	 *
	 * @return die erstellte Einwilligungsart
	 *
	 * @throws ApiOperationException   wenn der Personentyp unbekannt oder nicht unterstützt ist, die Bezeichnung oder der Schlüssel
	 *                                 für den Personentyp bereits verwendet wird oder der Schlüssel unbekannt ist
	 */
	public Einwilligungsart create(final EinwilligungsartCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var personTyp = validateCreate(dto);
			final var created = repository.create(toEntity(dto, personTyp));
			repository.flush();
			addEinwilligungen(created.id, personTyp);
			return toApiWithReferences(created);
		});
	}

	/**
	 * Aktualisiert eine bestehende Einwilligungsart teilweise (PATCH). Nur die im Request angegebenen Felder
	 * werden aktualisiert. Die Operation läuft in einer Transaktion.
	 *
	 * @param id    die ID der zu aktualisierenden Einwilligungsart
	 * @param dto   die zu aktualisierenden Felder
	 *
	 * @return die aktualisierte Einwilligungsart
	 *
	 * @throws ApiOperationException   wenn die Bezeichnung oder der Schlüssel für den Personentyp bereits verwendet wird,
	 *                                 der Schlüssel unbekannt ist oder die Einwilligungsart nicht gefunden wurde
	 */
	public Einwilligungsart patch(final long id, final EinwilligungsartPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var entity = findById(id);
			validatePatch(dto, entity);
			mapper.patch(dto, entity);
			return toApiWithReferences(entity);
		});
	}

	/**
	 * Löscht mehrere Einwilligungsarten anhand ihrer IDs. Auch referenzierte Einwilligungsarten werden gelöscht, die
	 * Einwilligungen werden dabei über die Fremdschlüssel (ON DELETE CASCADE) mit entfernt. Nicht gefundene IDs werden
	 * mit einer Fehlermeldung markiert. Die Operation läuft in einer Transaktion.
	 *
	 * @param idsToDelete   die IDs der zu löschenden Einwilligungsarten
	 *
	 * @return die Antworten mit dem Status jeder Löschoperation, aufsteigend nach ID sortiert
	 */
	public List<SimpleOperationResponse> deleteMultiple(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() ->
				BulkDeleteUtils.delete(idsToDelete, repository, e -> e.id, "Einwilligungsart"));
	}

	private PersonTyp validateCreate(final EinwilligungsartCreateRequest dto) {
		final var personTyp = validatePersonTyp(dto.idPersonTyp);
		validateUniqueBezeichnung(dto.bezeichnung, personTyp, null);
		validateSchluessel(dto.schluessel, personTyp, null);
		return personTyp;
	}

	private DTOEinwilligungsart toEntity(final EinwilligungsartCreateRequest dto, final PersonTyp personTyp) {
		final var entity = mapper.toDomain(dto);
		entity.personTyp = personTyp;
		return entity;
	}

	private DTOEinwilligungsart findById(final long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND,
						"Die Einwilligungsart mit der ID %d wurde nicht gefunden.".formatted(id)));
	}

	private void validatePatch(final EinwilligungsartPatchRequest dto, final DTOEinwilligungsart entity) {
		dto.bezeichnung.ifPresent(bezeichnung -> validateUniqueBezeichnung(bezeichnung, entity.personTyp, entity.id));
		dto.schluessel.ifPresent(schluessel -> validateSchluessel(schluessel, entity.personTyp, entity.id));
	}

	private Einwilligungsart toApiWithReferences(final DTOEinwilligungsart entity) {
		return toApi(entity, repository.getReferencedIds(List.of(entity.id)));
	}

	private Einwilligungsart toApi(final DTOEinwilligungsart entity, final Set<Long> referencedIds) {
		final var einwilligungsart = mapper.toApi(entity);
		einwilligungsart.referenziertInAnderenTabellen = referencedIds.contains(entity.id);
		return einwilligungsart;
	}

	private void addEinwilligungen(final long idEinwilligungsart, final PersonTyp personTyp) {
		if (personTyp == PersonTyp.SCHUELER) {
			schuelerEinwilligungService.addForEinwilligungsart(idEinwilligungsart);
		} else if (personTyp == PersonTyp.LEHRER) {
			lehrerEinwilligungService.addForEinwilligungsart(idEinwilligungsart);
		}
	}

	private static PersonTyp validatePersonTyp(final Integer idPersonTyp) {
		final var personTyp = (idPersonTyp == null) ? null : PersonTyp.getByID(idPersonTyp);
		if (personTyp == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Für die idPersonTyp %d existiert kein PersonTyp.".formatted(idPersonTyp));
		}
		if (personTyp == PersonTyp.ERZIEHER) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Der PersonTyp Erzieher wird derzeit nicht unterstützt.");
		}
		return personTyp;
	}

	private void validateUniqueBezeichnung(
			final String bezeichnung,
			final PersonTyp personTyp,
			final Long idToExclude
	) {
		final var exists = (idToExclude == null)
				? repository.bezeichnungIsAlreadyUsedCreate(bezeichnung, personTyp)
				: repository.bezeichnungIsAlreadyUsedPatch(bezeichnung, personTyp, idToExclude);
		if (exists) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, BEZEICHNUNG_WIRD_BEREITS_VERWENDET.formatted(bezeichnung));
		}
	}

	private void validateSchluessel(
			final String schluessel,
			final PersonTyp personTyp,
			final Long idToExclude
	) {
		if ((schluessel == null) || schluessel.isBlank()) {
			return;
		}
		if (Einwilligungsschluessel.data().getWertBySchluessel(schluessel) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST,
					"Zum angegebenen Schlüssel %s wurde keine passende Einwilligungsart gefunden.".formatted(schluessel));
		}
		final var exists = (idToExclude == null)
				? repository.schluesselIsAlreadyUsedCreate(schluessel, personTyp)
				: repository.schluesselIsAlreadyUsedPatch(schluessel, personTyp, idToExclude);
		if (exists) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, SCHLUESSEL_WIRD_BEREITS_VERWENDET.formatted(schluessel));
		}
	}

}
