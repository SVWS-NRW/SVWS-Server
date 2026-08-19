package de.svws_nrw.service.schule.katalog.ortsteil;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.ortsteil.OrtsteilMapper;
import de.svws_nrw.repo.schule.kataloge.ort.OrtRepository;
import de.svws_nrw.repo.schule.kataloge.ortsteil.OrtsteilRepository;
import de.svws_nrw.service.utils.ReferencedDeleteUtils;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;

public class OrtsteilService {

	private final OrtsteilRepository ortsteilRepository;
	private final OrtRepository ortRepository;
	private final OrtsteilMapper mapper;

	private static final String NAME_ORTSTEIL_WIRD_BEREITS_VERWENDET = "Der Name des Ortsteil %s wird bereits verwendet.";

	/**
	 * constructor
	 *
	 * @param ortsteilRepository {@link OrtsteilRepository}
	 * @param ortRepository {@link OrtRepository}
	 * @param mapper {@link OrtsteilMapper}
	 */
	public OrtsteilService(final OrtsteilRepository ortsteilRepository, final OrtRepository ortRepository, final OrtsteilMapper mapper) {
		this.ortsteilRepository = ortsteilRepository;
		this.ortRepository = ortRepository;
		this.mapper = mapper;
	}

	/**
	 * Gibt die Liste aller Ortsteile aus dem Schulkatalog zurück.
	 *
	 * @return Liste aller Ortsteile aus dem Schulkatalog.
	 */
	public List<OrtsteilKatalogEintrag> getAll() {
		final var entities = ortsteilRepository.getAll();
		final var referencedIds = ortsteilRepository.getReferencedIds(
				entities.stream()
						.map(e -> e.id)
						.toList()
		);
		final var orteById = ortRepository.findMapByIds(
				entities.stream()
						.map(o -> o.idOrt)
						.collect(Collectors.toSet())
		);

		return entities.stream()
				.map(e -> map(e, orteById, referencedIds))
				.toList();
	}

	private OrtsteilKatalogEintrag map(final DTOOrtsteil e, final Map<Long, DTOOrt> orteById, final Set<Long> referencedIds) {
		final var eintrag = mapper.toApi(e, orteById.get(e.idOrt));
		eintrag.referenziertInAnderenTabellen = referencedIds.contains(e.id);
		return eintrag;
	}

	/**
	 * Legt einen neuen Ortsteil im schulinternen Katalog an.
	 * Validiert vor dem Anlegen die Eindeutigkeit des Namen des Ortsteils und die id des zugehrigen Ortes
	 *
	 * @param dto der {@link OrtsteilCreateRequest} mit den Pflichtfeldern
	 * @return der {@link OrtsteilKatalogEintrag} des neu angelegten Orts
	 * @throws ApiOperationException mit {@code 400 BAD_REQUEST} wenn der Name des Ortsteils bereits vergeben oder die {@code idOrt} unbekannt ist
	 */
	public OrtsteilKatalogEintrag create(final OrtsteilCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			validateUniqueOrtsteilname(null, dto.ortsteil, dto.idOrt);
			final var ort = validateAndGetOrt(dto.idOrt);
			final var ortsteil = mapper.toDomain(dto);
			final var created = ortsteilRepository.create(ortsteil);
			return mapper.toApi(created, ort);
		});
	}

	/**
	 * Aktualisiert ein bestehenden Ortsteil partiell anhand der im {@link OrtsteilPatchRequest}
	 * gesetzten Felder. Felder mit {@code undefined}-Wert bleiben unverändert.
	 *
	 * @param id  die ID des zu aktualisierenden Ortsteil
	 * @param dto der {@link OrtsteilPatchRequest} mit den zu ändernden Feldern
	 * @return der aktualisierte {@link OrtsteilKatalogEintrag}
	 * @throws ApiOperationException mit {@code 404 NOT_FOUND} wenn kein Ort zur {@code id} existiert,
	 *                               mit {@code 400 BAD_REQUEST} wenn Ortsname bereits vergeben oder die {@code idOrt} unbekannt ist
	 */
	public OrtsteilKatalogEintrag patch(final long id, final OrtsteilPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var entity = ortsteilRepository.getById(id);
			final DTOOrt ort = validateAndGetOrt(dto.idOrt.orElse(entity.idOrt));
			dto.ortsteil.ifPresent(ortsteil -> validateUniqueOrtsteilname(entity.id, ortsteil, ort.id));
			mapper.patch(dto, entity);
			return mapper.toApi(entity, ort);
		});
	}

	/**
	 * Löscht die Ortsteile mit den angegebenen IDs.
	 * Referenzierte Ortsteile werden nicht gelöscht, sondern mit einer Fehlermeldung markiert.
	 * Nicht gefundene IDs werden als Fehler zurückgegeben.
	 *
	 * @param idsToDelete Liste der zu löschenden Ortsteil-IDs
	 * @return Liste von {@link SimpleOperationResponse}-Einträgen, aufsteigend nach ID sortiert
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() ->
				ReferencedDeleteUtils.delete(
						idsToDelete,
						ortsteilRepository,
						e -> e.id,
						"Ortsteil"
				)
		);
	}

	private DTOOrt validateAndGetOrt(final @NotNull Long idOrt) {
		return ortRepository.findById(idOrt)
				.orElseThrow(() -> new ApiOperationException(Response.Status.BAD_REQUEST, "Kein Ort für die ID %d gefunden".formatted(idOrt)));
	}

	private void validateUniqueOrtsteilname(final Long idOrtsteil, final String ortsteilname, final Long idOrt) {
		final boolean isUnique = (idOrtsteil == null)
				? ortsteilRepository.ortsteilnameIsUniqueForIdOrtCreate(ortsteilname, idOrt)
				: ortsteilRepository.ortsteilnameIsUniqueForIdOrtPatch(ortsteilname, idOrt, idOrtsteil);

		if (!isUnique) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, NAME_ORTSTEIL_WIRD_BEREITS_VERWENDET.formatted(ortsteilname));
		}
	}

}
