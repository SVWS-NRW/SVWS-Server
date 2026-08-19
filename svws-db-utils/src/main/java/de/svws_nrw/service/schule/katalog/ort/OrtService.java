package de.svws_nrw.service.schule.katalog.ort;

import java.util.List;
import java.util.Set;

import de.svws_nrw.asd.data.schule.LaenderKatalogEintrag;
import de.svws_nrw.asd.types.schule.Laender;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.ort.OrtMapper;
import de.svws_nrw.repo.schule.kataloge.ort.OrtRepository;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.utils.DeleteUtils;
import jakarta.ws.rs.core.Response;

public class OrtService {

	private final OrtRepository ortRepository;
	private final OrtMapper mapper;
	private final EigeneSchuleService eigeneSchuleService;

	private static final String ORTSNAME_WIRD_BEREITS_VERWENDET = "Der Ortsname %s wird bereits verwendet.";


	/**
	 * constructor
	 *
	 * @param ortRepository {@link OrtRepository}
	 * @param mapper {@link OrtMapper}
	 * @param eigeneSchuleService {@link EigeneSchuleService}
	 */
	public OrtService(final OrtRepository ortRepository, final OrtMapper mapper, final EigeneSchuleService eigeneSchuleService) {
		this.ortRepository = ortRepository;
		this.mapper = mapper;
		this.eigeneSchuleService = eigeneSchuleService;
	}

	/**
	 * Gibt die Liste aller Orte aus dem Schulkatalog zurück.
	 *
	 * @return Liste aller Orte aus dem Schulkatalog.
	 */
	public List<OrtKatalogEintrag> getAll() {
		final var schuljahr = eigeneSchuleService.getSchuljahr();
		final var entities = ortRepository.getAll();
		final var referencedIds = ortRepository.getReferencedIds(
				entities.stream()
						.map(e -> e.id)
						.toList()
		);

		return entities.stream()
				.map(e -> map(e, schuljahr, referencedIds))
				.toList();
	}

	private OrtKatalogEintrag map(final DTOOrt e, final int schuljahr, final Set<Long> referencedIds) {
		final var eintrag = mapper.toApi(e, schuljahr);
		eintrag.referenziertInAnderenTabellen = referencedIds.contains(e.id);
		return eintrag;
	}


	/**
	 * Legt einen neuen Ort im schulinternen Katalog an.
	 * Validiert vor dem Anlegen die Eindeutigkeit des Ortsnamens
	 * sowie die Existenz der referenzierten {@code idBundesland} im CoreType-Katalog.
	 *
	 * @param dto der {@link OrtCreateRequest} mit den Pflichtfeldern
	 * @return der {@link OrtKatalogEintrag} des neu angelegten Orts
	 * @throws ApiOperationException mit {@code 400 BAD_REQUEST} wenn der Ortsname bereits vergeben oder die {@code idFachklasse} unbekannt ist
	 */
	public OrtKatalogEintrag create(final OrtCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			this.validateCreate(dto);
			final var ort = mapper.toDomain(dto);
			final var created = ortRepository.create(ort);
			final var schuljahr = eigeneSchuleService.getSchuljahr();
			return mapper.toApi(created, schuljahr);
		});
	}

	/**
	 * Aktualisiert ein bestehenden Ort partiell anhand der im {@link OrtPatchRequest}
	 * gesetzten Felder. Felder mit {@code undefined}-Wert bleiben unverändert.
	 * Ist {@code idBundesland} gesetzt, werden die CoreType-abhängigen Felder neu aufgelöst.
	 *
	 * @param id  die ID des zu aktualisierenden Orts
	 * @param dto der {@link OrtPatchRequest} mit den zu ändernden Feldern
	 * @return der aktualisierte {@link OrtKatalogEintrag}
	 * @throws ApiOperationException mit {@code 404 NOT_FOUND} wenn kein Ort zur {@code id} existiert,
	 *                               mit {@code 400 BAD_REQUEST} wenn Ortsname bereits vergeben oder die {@code idBundesland} unbekannt ist
	 */
	public OrtKatalogEintrag patch(final long id, final OrtPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var entity = ortRepository.getById(id);
			validateAndResolvePatch(dto, entity, id);
			mapper.patch(dto, entity);
			final var schuljahr = eigeneSchuleService.getSchuljahr();
			return this.mapper.toApi(entity, schuljahr);
		});
	}

	/**
	 * Löscht die Orte mit den angegebenen IDs.
	 * Referenzierte Orte werden nicht gelöscht, sondern mit einer Fehlermeldung markiert.
	 * Nicht gefundene IDs werden als Fehler zurückgegeben.
	 *
	 * @param idsToDelete Liste der zu löschenden Ort-IDs
	 * @return Liste von {@link SimpleOperationResponse}-Einträgen, aufsteigend nach ID sortiert
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() ->
				DeleteUtils.delete(
						idsToDelete,
						ortRepository::getReferencedIds,
						ortRepository::findListByIds,
						ortRepository::delete,
						e -> e.id,
						"Ort"
				)
		);
	}

	private void validateAndResolvePatch(final OrtPatchRequest dto, final DTOOrt entity, final long id) {
		final String plz = dto.plz.orElse(entity.plz);
		dto.ortsname.ifPresent(ortsname -> validateUniqueOrtsname(id, ortsname, plz));
		dto.idBundesland.ifPresent(idBundesland -> validateAndResolveIdBundesland(idBundesland, entity));
	}

	private void validateCreate(final OrtCreateRequest dto) {
		validateUniqueOrtsname(null, dto.ortsname, dto.plz);
		if (dto.idBundesland != null) {
			validateIdBundesland(dto.idBundesland);
		}
	}

	private void validateUniqueOrtsname(final Long idOrt, final String ortsname, final String plz) {
		final boolean isUnique = (idOrt == null)
				? ortRepository.ortsnameIsUniqueForPlzCreate(ortsname, plz)
				: ortRepository.ortsnameIsUniqueForPlzPatch(ortsname, plz, idOrt);

		if (!isUnique) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, ORTSNAME_WIRD_BEREITS_VERWENDET.formatted(ortsname));
		}
	}

	private LaenderKatalogEintrag validateIdBundesland(final Long idBundesland) {
		final var bundesland = Laender.data().getEintragByID(idBundesland);
		if (bundesland == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Kein Bundesland für die id %d gefunden".formatted(idBundesland));
		}
		return bundesland;
	}

	private void validateAndResolveIdBundesland(final Long idBundesland, final DTOOrt entity) {
		final var bundesland = this.validateIdBundesland(idBundesland);
		entity.schluesselBundesland = bundesland.schluessel;
	}

}
