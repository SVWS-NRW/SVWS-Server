package de.svws_nrw.service.schule.katalog.religion;

import java.util.List;
import java.util.Set;

import de.svws_nrw.asd.types.schule.Religion;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.katalog.DTOReligion;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.religion.ReligionMapper;
import de.svws_nrw.repo.schule.kataloge.religion.ReligionRepository;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import jakarta.ws.rs.core.Response;

public class ReligionService {

	private final ReligionRepository repository;
	private final ReligionMapper mapper;
	private final EigeneSchuleService eigeneSchuleService;

	private static final String BEZEICHNUNG_WIRD_BEREITS_VERWENDET = "Die Bezeichnung %s wird bereits verwendet.";

	/**
	 * constructor
	 *
	 * @param repository {@link ReligionRepository}
	 * @param mapper {@link ReligionMapper}
	 * @param eigeneSchuleService {@link EigeneSchuleService}
	 */
	public ReligionService(final ReligionRepository repository, final ReligionMapper mapper, final EigeneSchuleService eigeneSchuleService) {
		this.repository = repository;
		this.mapper = mapper;
		this.eigeneSchuleService = eigeneSchuleService;
	}

	/**
	 * Gibt die Liste aller Religionen aus dem Schulkatalog zurück.
	 *
	 * @return Liste aller Religionen aus dem Schulkatalog.
	 */
	public List<ReligionEintrag> getAll() {
		final var schuljahr = eigeneSchuleService.getSchuljahr();
		final var entities = repository.getAll();
		final var referencedIds = repository.getReferencedIds(
				entities.stream()
						.map(r -> r.id)
						.toList()
		);

		return entities.stream()
				.map(e -> map(e, schuljahr, referencedIds))
				.toList();
	}

	private ReligionEintrag map(final DTOReligion entity, final int schuljahr, final Set<Long> referencedIds) {
		final var eintrag = mapper.toApi(entity, schuljahr);
		eintrag.referenziertInAnderenTabellen = referencedIds.contains(entity.id);
		return eintrag;
	}

	/**
	 * Legt eine neue Religion im schulinternen Katalog an.
	 * Validiert vor dem Anlegen die Eindeutigkeit der Bezeichnung
	 * sowie die Existenz der referenzierten {@code idReligion} im CoreType-Katalog.
	 *
	 * @param dto der {@link ReligionCreateRequest} mit den Pflichtfeldern
	 * @return der {@link ReligionEintrag} der neu angelegten Religion
	 * @throws ApiOperationException mit {@code 400 BAD_REQUEST} wenn die Bezeichnung bereits vergeben oder die {@code idReligion} unbekannt ist
	 */
	public ReligionEintrag create(final ReligionCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			this.validateCreate(dto);
			final var religion = mapper.toDomain(dto);
			final var created = repository.create(religion);
			final var schuljahr = eigeneSchuleService.getSchuljahr();
			return mapper.toApi(created, schuljahr);
		});
	}

	/**
	 * Aktualisiert ein bestehende Religion partiell anhand der im {@link ReligionPatchRequest}
	 * gesetzten Felder. Felder mit {@code undefined}-Wert bleiben unverändert.
	 * Ist {@code idReligion} gesetzt, werden die CoreType-abhängigen Felder neu aufgelöst.
	 *
	 * @param id  die ID der zu aktualisierenden Religion
	 * @param dto der {@link ReligionPatchRequest} mit den zu ändernden Feldern
	 * @return der aktualisierte {@link ReligionEintrag}
	 * @throws ApiOperationException mit {@code 404 NOT_FOUND} wenn keine Religion zur {@code id} existiert,
	 *                               mit {@code 400 BAD_REQUEST} wenn Bezeichnung bereits vergeben oder die {@code idReligion} unbekannt ist
	 */
	public ReligionEintrag patch(final long id, final ReligionPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			validatePatch(dto, id);
			final var entity = repository.getById(id);
			mapper.patch(dto, entity);
			final var schuljahr = eigeneSchuleService.getSchuljahr();
			return this.mapper.toApi(entity, schuljahr);
		});
	}

	/**
	 * Löscht die Religionen mit den angegebenen IDs.
	 * Referenzierte Religionen werden nicht gelöscht, sondern mit einer Fehlermeldung markiert.
	 * Nicht gefundene IDs werden als Fehler zurückgegeben.
	 *
	 * @param idsToDelete Liste der zu löschenden Religion-IDs
	 * @return Liste von {@link SimpleOperationResponse}-Einträgen
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() ->
				BulkDeleteUtils.deleteWithReferenceCheck(
						idsToDelete,
						repository,
						e -> e.id,
						"Religion"
				)
		);
	}

	private void validatePatch(final ReligionPatchRequest dto, final long id) {
		dto.bezeichnung.ifPresent(bezeichnung -> validateUniqueBezeichnung(id, bezeichnung));
		dto.idReligion.ifPresent(this::validateIdReligion);
	}

	private void validateCreate(final ReligionCreateRequest dto) {
		validateUniqueBezeichnung(null, dto.bezeichnung);
		validateIdReligion(dto.idReligion);
	}

	private void validateUniqueBezeichnung(final Long id, final String bezeichnung) {
		final boolean istBereitsVergeben = (id == null)
				? repository.bezeichnungIstBereitsVergeben(bezeichnung)
				: repository.bezeichnungIstBereitsVergebenExceptId(bezeichnung, id);

		if (istBereitsVergeben) {
			throw new ApiOperationException(
					Response.Status.BAD_REQUEST,
					BEZEICHNUNG_WIRD_BEREITS_VERWENDET.formatted(bezeichnung)
			);
		}
	}

	private void validateIdReligion(final Long idReligion) {
		if (Religion.data().getEintragByID(idReligion) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Keine Religion für die id %d gefunden".formatted(idReligion));
		}
	}

}
