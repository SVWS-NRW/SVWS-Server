package de.svws_nrw.service.schule.katalog.merkmal;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Merkmal;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schule.DTOMerkmale;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.merkmal.MerkmalMapper;
import de.svws_nrw.repo.schule.kataloge.merkmal.MerkmalRepository;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import jakarta.ws.rs.core.Response;

/**
 * Service-Klasse für die Verwaltung von Merkmalen.
 * <p>
 * Stellt CRUD-Operationen für Merkmale bereit und führt Validierungen durch.
 */
public final class MerkmalService {

	private static final String BEZEICHNUNG_WIRD_BEREITS_VERWENDET = "Die Bezeichnung %s wird bereits verwendet.";
	private static final String KUERZEL_WIRD_BEREITS_VERWENDET = "Das Kürzel %s wird bereits verwendet.";
	private static final String MINDESTENS_EIN_MERKMALTYP_NOTWENDIG = "Mindestens ein Merkmaltyp (Schule oder Schüler) muss ausgewählt sein";

	private final MerkmalRepository repository;
	private final MerkmalMapper mapper;

	/**
	 * Erstellt einen neuen MerkmalService.
	 *
	 * @param repository das Repository für den Datenbankzugriff auf Merkmale
	 * @param mapper     der Mapper zur Konvertierung zwischen Domain- und API-Modellen
	 */
	public MerkmalService(final MerkmalRepository repository, final MerkmalMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	/**
	 * Gibt alle Merkmale zurück.
	 *
	 * @return eine Liste aller Merkmale
	 */
	public List<Merkmal> getAll() {
		return this.repository
				.getAll()
				.stream()
				.map(this.mapper::toApi)
				.toList();
	}

	/**
	 * Gibt ein Merkmal zum Kürzel zurück
	 *
	 * @param kuerzel  kuerzel
	 *
	 * @return ein Merkmal
	 */
	public Merkmal getByKuerzel(final String kuerzel) {
		return this.repository.getByKuerzel(kuerzel)
				.map(this.mapper::toApi)
				.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, "Kein Merkmal zum Kürzel %s gefunden".formatted(kuerzel)));
	}

	/**
	 * Gibt ein Merkmal zur ID zurück
	 *
	 * @param id id
	 *
	 * @return ein Merkmal
	 */
	public Merkmal getById(final Long id) {
		return this.repository.findById(id)
				.map(this.mapper::toApi)
				.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, "Kein Merkmal zur ID %d gefunden".formatted(id)));
	}

	/**
	 * Erstellt ein neues Merkmal.
	 * Validiert die Eingabedaten und erstellt das Merkmal in einer Transaktion.
	 *
	 * @param dto die Daten für das neue Merkmal
	 * @return das erstellte Merkmal
	 * @throws ApiOperationException wenn Kürzel oder Bezeichnung bereits existieren
	 * 								 oder keine Merkmaltypen ausgewählt sind
	 */
	public Merkmal create(final MerkmalCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			this.validateCreate(dto);
			final var merkmal = this.mapper.toDomain(dto);
			final var created = this.repository.create(merkmal);
			return this.mapper.toApi(created);
		});
	}

	/**
	 * Aktualisiert ein bestehendes Merkmal teilweise (PATCH).
	 * Nur die im Request angegebenen Felder werden aktualisiert.
	 * Die Operation wird in einer Transaktion ausgeführt.
	 *
	 * @param id  die ID des zu aktualisierenden Merkmals
	 * @param dto die zu aktualisierenden Felder
	 * @return das aktualisierte Merkmal
	 * @throws ApiOperationException wenn Kürzel oder Bezeichnung bereits existieren,
	 *                               keine Merkmaltypen ausgewählt sind
	 *                               oder das Merkmal nicht gefunden wurde
	 */
	public Merkmal patch(final long id, final MerkmalPatchRequest dto) {
		return TransactionSupport.transactional(
				() -> {
					final var entity = repository.getById(id);
					validatePatch(entity, dto, id);
					mapper.patch(dto, entity);
					return this.mapper.toApi(entity);
				}
		);
	}


	/**
	 * Löscht mehrere Merkmale anhand ihrer IDs.
	 * Die Operation wird in einer Transaktion ausgeführt.
	 *
	 * @param idsToDelete die Liste der IDs der zu löschenden Merkmale
	 * @return eine Liste von Antworten mit dem Status jeder Löschoperation, sortiert nach ID
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() ->
				BulkDeleteUtils.delete(
						idsToDelete,
						repository,
						e -> e.id,
						"Merkmal"
				)
		);
	}

	private void validateCreate(final MerkmalCreateRequest dto) {
		validateAtLeastOneMerkmalType(dto.istSchulmerkmal, dto.istSchuelermerkmal);
		validateUniqueBezeichnung(dto.bezeichnung, null);
		validateUniqueKuerzel(dto.kuerzel, null);
	}

	private void validatePatch(final DTOMerkmale entity, final MerkmalPatchRequest dto, final long id) {
		final var istSchulmerkmal = dto.istSchulmerkmal.orElse(entity.istSchulmerkmal);
		final var istSchuelermerkmal = dto.istSchuelermerkmal.orElse(entity.istSchuelermerkmal);
		validateAtLeastOneMerkmalType(istSchulmerkmal, istSchuelermerkmal);

		dto.bezeichnung.ifPresent(bezeichnung -> validateUniqueBezeichnung(bezeichnung, id));
		dto.kuerzel.ifPresent(kuerzel -> validateUniqueKuerzel(kuerzel, id));
	}

	private void validateUniqueBezeichnung(final String bezeichnung, final Long idToExclude) {
		final var exists = (idToExclude == null)
				? repository.bezeichnungIsAlreadyUsedCreate(bezeichnung)
				: repository.bezeichnungIsAlreadyUsedPatch(bezeichnung, idToExclude);

		if (exists) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, BEZEICHNUNG_WIRD_BEREITS_VERWENDET.formatted(bezeichnung));
		}
	}

	private void validateUniqueKuerzel(final String kuerzel, final Long idToExclude) {
		final var exists = (idToExclude == null)
				? repository.kuerzelIsAlreadyUsedCreate(kuerzel)
				: repository.kuerzelIsAlreadyUsedPatch(kuerzel, idToExclude);

		if (exists) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, KUERZEL_WIRD_BEREITS_VERWENDET.formatted(kuerzel));
		}
	}

	private void validateAtLeastOneMerkmalType(final boolean istSchulmerkmal, final boolean istSchuelermerkmal) {
		if (!istSchulmerkmal && !istSchuelermerkmal) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, MINDESTENS_EIN_MERKMALTYP_NOTWENDIG);
		}
	}

}
