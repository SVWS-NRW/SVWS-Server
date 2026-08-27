package de.svws_nrw.service.schueler.stammdaten;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.types.schule.Verkehrssprache;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.stammdaten.SchuelerStammdatenMapper;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schule.kataloge.fahrschuelerart.FahrschuelerartRepository;
import de.svws_nrw.repo.schule.kataloge.haltestelle.HaltestelleRepository;
import de.svws_nrw.repo.schule.kataloge.ort.OrtRepository;
import de.svws_nrw.repo.schule.kataloge.ortsteil.OrtsteilRepository;
import de.svws_nrw.repo.schule.kataloge.religion.ReligionRepository;
import de.svws_nrw.service.schueler.foto.SchuelerFoto;
import de.svws_nrw.service.schueler.foto.SchuelerFotoService;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import jakarta.annotation.Nonnull;
import jakarta.ws.rs.core.Response;
import org.openapitools.jackson.nullable.JsonNullable;

public final class SchuelerStammdatenService {

	private final SchuelerRepository repository;
	private final ReligionRepository religionRepository;
	private final OrtRepository ortRepository;
	private final OrtsteilRepository ortsteilRepository;
	private final FahrschuelerartRepository fahrschuelerartRepository;
	private final HaltestelleRepository haltestelleRepository;
	private final SchuelerStammdatenMapper mapper;
	private final SchuelerFotoService schuelerFotoService;

	private static final String DATEN_NOT_FOUND_BY_ID = "Keine SchuelerStammdaten mit der ID %d gefunden.";

	/**
	 * constructor
	 *
	 * @param repositories {@link SchuelerStammdatenRepositories}
	 * @param mapper {@link SchuelerStammdatenMapper}
	 * @param schuelerFotoService {@link SchuelerFotoService}
	 */
	public SchuelerStammdatenService(
			final SchuelerStammdatenRepositories repositories,
			final SchuelerStammdatenMapper mapper,
			final SchuelerFotoService schuelerFotoService) {
		this.repository = repositories.schuelerRepository();
		this.religionRepository = repositories.religionRepository();
		this.ortRepository = repositories.ortRepository();
		this.ortsteilRepository = repositories.ortsteilRepository();
		this.fahrschuelerartRepository = repositories.fahrschuelerartRepository();
		this.haltestelleRepository = repositories.haltestelleRepository();
		this.mapper = mapper;
		this.schuelerFotoService = schuelerFotoService;
	}

	/**
	 * Gibt eine {@link SchuelerStammdaten} anhand ihrer ID zurück.
	 *
	 * @param id id
	 *
	 * @return {@link SchuelerStammdaten}
	 */
	public SchuelerStammdaten get(final long id) {
		final var entity = repository.findById(id)
				.orElseThrow(() -> new ApiOperationException(
						Response.Status.NOT_FOUND,
						DATEN_NOT_FOUND_BY_ID.formatted(id)
				));
		return toApi(entity);
	}

	/**
	 * Gibt alle {@link SchuelerStammdaten} zu übergebenen IDs zurück.
	 *
	 * @param ids für das Auflösen benötigte Identifier
	 *
	 * @return Liste aller {@link SchuelerStammdaten} als API-Modelle
	 */
	public List<SchuelerStammdaten> getList(final List<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return List.of();
		}
		final var entities = repository.findListByIds(ids);
		return toApi(entities);
	}

	/**
	 * Legt einen neuen Schüler anhand der übergebenen Importdaten an.
	 * <p>
	 * Validiert vor dem Anlegen Geschlecht, Schülerstatus und Religion.
	 *
	 * @param dto die {@link SchuelerImportData} mit den Pflichtfeldern des neuen Schülers
	 * @return die {@link SchuelerStammdaten} des neu angelegten Schülers
	 * @throws ApiOperationException mit {@code 400 BAD_REQUEST} wenn Geschlecht, Status oder Religion ungültig sind
	 */
	public SchuelerStammdaten create(final SchuelerImportData dto) {
		return TransactionSupport.transactional(() -> {
			this.validateCreate(dto);
			final var guId = String.format("{%s}", UUID.randomUUID());
			final var schueler = mapper.toDomain(dto, guId);
			final var created = repository.create(schueler);
			return toApi(created);
		});
	}

	/**
	 * Aktualisiert die Stammdaten eines bestehenden Schülers anhand der übergebenen Patch-Daten.
	 * <p>
	 * Es werden nur die im {@link SchuelerStammdatenPatchRequest} gesetzten Felder überschrieben.
	 * Validiert alle gesetzten Felder vor der Persistierung.
	 *
	 * @param id           die ID des zu aktualisierenden Schülers
	 * @param patchRequest der {@link SchuelerStammdatenPatchRequest} mit den zu ändernden Feldern
	 * @return die aktualisierten {@link SchuelerStammdaten}
	 * @throws ApiOperationException mit {@code 404 NOT_FOUND} wenn kein Schüler zur ID gefunden wurde
	 * @throws ApiOperationException mit {@code 400 BAD_REQUEST} wenn ein gesetztes Feld ungültig ist
	 */
	public SchuelerStammdaten patch(final long id, final SchuelerStammdatenPatchRequest patchRequest) {
		return TransactionSupport.transactional(() -> {
			final var entityToPatch = repository.findById(id)
					.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, DATEN_NOT_FOUND_BY_ID.formatted(id)));
			validatePatch(patchRequest);
			patch(patchRequest, entityToPatch);
			return toApi(entityToPatch);
		});
	}

	private void patch(final SchuelerStammdatenPatchRequest patchRequest, final DTOSchueler entityToPatch) {
		mapper.patch(patchRequest, entityToPatch);
		patchRequest.foto.ifPresent(foto -> schuelerFotoService.upsertOrDelete(entityToPatch.ID, foto));
	}

	/**
	 * Aktualisiert mehrere bestehende SchuelerStammdaten teilweise (PATCH).
	 *
	 * @param dtos die Liste der zu aktualisierenden Einträge
	 *
	 * @return die aktualisierten SchuelerStammdaten als API-Modelle
	 */
	public List<SchuelerStammdaten> patchMultiple(final List<SchuelerStammdatenBatchPatchRequest> dtos) {
		return TransactionSupport.transactional(() -> {
			final var idsToPatch = dtos.stream()
					.map(dto -> dto.id)
					.toList();

			final var toPatchById = this.repository.findMapByIds(idsToPatch);
			final var patchedEntities = dtos.stream()
					.map(dto -> validateAndPatch(dto, toPatchById))
					.toList();

			return toApi(patchedEntities);
		});
	}

	@Nonnull
	private DTOSchueler validateAndPatch(final SchuelerStammdatenBatchPatchRequest dto, final Map<Long, DTOSchueler> toPatchById) {
		final var entity = toPatchById.get(dto.id);
		if (entity == null) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, DATEN_NOT_FOUND_BY_ID.formatted(dto.id));
		}
		validatePatch(dto);
		patch(dto, entity);
		return entity;
	}

	/**
	 * Löscht die Schüler mit den angegebenen IDs.
	 * Nicht gefundene IDs werden stillschweigend ignoriert.
	 * Jeder Eintrag in der Rückgabeliste enthält die ID und ob die Löschung erfolgreich war.
	 *
	 * @param idsToDelete Liste der zu löschenden Schüler-IDs
	 * @return Liste von {@link SimpleOperationResponse}-Einträgen, aufsteigend nach ID sortiert
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() ->
				BulkDeleteUtils.delete(
						idsToDelete,
						repository,
						e -> e.ID,
						"Schueler"
				)
		);
	}

	// mapping

	private SchuelerStammdaten toApi(final DTOSchueler entity) {
		return toApi(List.of(entity)).getFirst();
	}

	private List<SchuelerStammdaten> toApi(final List<DTOSchueler> entities) {
		final var ids = entities.stream().map(s -> s.ID).toList();
		final Map<Long, SchuelerFoto> fotoDtosBySchuelerId = schuelerFotoService
				.getBySchuelerIds(ids)
				.stream()
				.collect(Collectors.toMap(SchuelerFoto::idSchueler, sf -> sf));

		return entities.stream()
				.map(schueler -> {
					final var schuelerStammdaten = mapper.toApi(schueler);
					final SchuelerFoto foto = fotoDtosBySchuelerId.get(schuelerStammdaten.id);
					schuelerStammdaten.foto = (foto != null) ? foto.fotoBase64() : null;
					return schuelerStammdaten;
				})
				.toList();
	}

	// Validation

	private void validateCreate(final SchuelerImportData dto) {
		validateIdGeschlecht(dto.idGeschlecht());
		validateIdSchuelerStatus(dto.idSchuelerStatus());
		validateIdReligion(dto.idReligion());
	}

	private void validatePatch(final SchuelerStammdatenPatchRequest patchRequest) {
		patchRequest.geschlecht.ifPresent(this::validateIdGeschlecht);
		validateWohnortAndOrtsteil(patchRequest.wohnortID, patchRequest.ortsteilID);
		patchRequest.religionID.ifPresent(this::validateIdReligion);
		patchRequest.idStaatsangehoerigkeit.ifPresent(this::validateIdNationalitaet);
		patchRequest.idStaatsangehoerigkeit2.ifPresent(this::validateIdNationalitaet);
		patchRequest.idVerkehrspracheFamilie.ifPresent(this::validateIdVerkehrssprache);
		patchRequest.status.ifPresent(this::validateIdSchuelerStatus);
		patchRequest.fahrschuelerArtID.ifPresent(this::validateIdFahrschuelerart);
		patchRequest.haltestelleID.ifPresent(this::validateIdHaltestelle);
	}

	private void validateIdSchuelerStatus(final int idSchuelerStatus) {
		if (SchuelerStatus.data().getEintragByID((long) idSchuelerStatus) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Kein Status zur ID %d gefunden".formatted(idSchuelerStatus));
		}
	}

	private void validateIdGeschlecht(final int idGeschlecht) {
		if (Geschlecht.fromValue(idGeschlecht) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Kein Geschlecht zur ID %d gefunden".formatted(idGeschlecht));
		}
	}

	private void validateIdReligion(final Long idReligion) {
		if (idReligion == null) {
			return;
		}
		if (!religionRepository.existsById(idReligion)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Kein Religionseintrag zur id %d gefunden.".formatted(idReligion));
		}
	}

	private void validateIdNationalitaet(final Long idNationalitaet) {
		if (idNationalitaet == null) {
			return;
		}
		if (Nationalitaeten.data().getWertByIDOrNull(idNationalitaet) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Es wurde keine Nationalität mit der ID %d gefunden.".formatted(idNationalitaet));
		}
	}

	private void validateIdVerkehrssprache(final Long idVerkehrssprache) {
		if (idVerkehrssprache == null) {
			return;
		}
		if (Verkehrssprache.data().getWertByIDOrNull(idVerkehrssprache) == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Es wurde keine Verkehrssprache mit der ID %d gefunden.".formatted(idVerkehrssprache));
		}
	}

	private void validateIdFahrschuelerart(final Long idFahrschuelerart) {
		if (idFahrschuelerart == null) {
			return;
		}
		if (!fahrschuelerartRepository.existsById(idFahrschuelerart)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Es wurde keine Fahrschülerart mit der ID %d gefunden.".formatted(idFahrschuelerart));
		}
	}

	private void validateIdHaltestelle(final Long idHaltestelle) {
		if (idHaltestelle == null) {
			return;
		}
		if (!haltestelleRepository.existsById(idHaltestelle)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Es wurde keine Haltestelle mit der ID %d gefunden.".formatted(idHaltestelle));
		}
	}

	private void validateWohnortAndOrtsteil(final JsonNullable<Long> idOrt, final JsonNullable<Long> idOrtsteil) {
		if (!idOrt.isPresent() || (idOrt.get() == null)) {
			validateOrtsteilOhneOrtNichtGesetzt(idOrtsteil);
			return;
		}
		if (!ortExists(idOrt.get())) {
			throw new ApiOperationException(Response.Status.NOT_FOUND,
					"Kein Ort zur ID %d gefunden.".formatted(idOrt.get()));
		}
		validateOrtsteilMatchesToOrt(idOrt.get(), idOrtsteil.get());
	}

	private void validateOrtsteilOhneOrtNichtGesetzt(final JsonNullable<Long> idOrtsteil) {
		if (idOrtsteil.isPresent() && (idOrtsteil.get() != null)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST,
					"Ein Ortsteil kann nicht ohne Ort angegeben werden.");
		}
	}

	private void validateOrtsteilMatchesToOrt(final Long idOrt, final Long idOrtsteil) {
		if (!ortsteilIsNullOrMatchesToOrt(idOrt, idOrtsteil)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST,
					"Die Kombination von Ort und Ortsteil ist nicht zulässig. Der Ortsteil ist dem Ort nicht zugeordnet.");
		}
	}

	private boolean ortsteilIsNullOrMatchesToOrt(final Long idOrt, final Long idOrtsteil) {
		if (idOrtsteil == null) {
			return true;
		}
		final var ortsteil = ortsteilRepository.findById(idOrtsteil)
				.orElseThrow(() -> new ApiOperationException(
						Response.Status.NOT_FOUND,
						"Kein Ortsteil zur ID %d gefunden.".formatted(idOrtsteil)
				));
		return Objects.equals(ortsteil.idOrt, idOrt);
	}

	private boolean ortExists(final Long idOrt) {
		return ortRepository.existsById(idOrt);
	}

}
