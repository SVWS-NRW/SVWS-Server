package de.svws_nrw.service.schueler.schulbesuch;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchMerkmal;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Merkmal;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerMerkmale;
import de.svws_nrw.db.dto.current.schild.schule.DTOMerkmale;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.schulbesuch.SchuelerMerkmalMapper;
import de.svws_nrw.repo.schueler.schulbesuch.SchuelerMerkmalRepository;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalService;
import jakarta.ws.rs.core.Response;

public class SchuelerMerkmalService {

	private final SchuelerMerkmalRepository repository;
	private final MerkmalService merkmalService;
	private final SchuelerMerkmalMapper mapper;


	/**
	 * Erstellt einen neuen {@code SchuelerMerkmalService}.
	 *
	 * @param repository	das Repository für {@link DTOSchuelerMerkmale}-Einträge
	 * @param merkmalService	das Repository für {@link DTOMerkmale}-Einträge
	 * @param mapper	der Mapper zur Konvertierung zwischen Entity und API-Modell
	 */
	public SchuelerMerkmalService(
			final SchuelerMerkmalRepository repository,
			final MerkmalService merkmalService,
			final SchuelerMerkmalMapper mapper) {
		this.repository = repository;
		this.merkmalService = merkmalService;
		this.mapper = mapper;
	}


	/**
	 * Gibt alle Merkmale des Schulbesuchs eines Schülers als API-Modelle zurück.
	 *
	 * @param idSchueler die ID des Schülers
	 * @return Liste der Merkmale, leer wenn keine vorhanden
	 */
	public List<SchuelerSchulbesuchMerkmal> getAllByIdSchueler(final long idSchueler) {
		final var merkmaleByKuerzel = this.merkmalService.getAll()
				.stream()
				.collect(Collectors.toMap(m -> m.kuerzel, m -> m));
		return this.repository
				.getAllByIdSchueler(idSchueler)
				.stream()
				.map(s -> this.mapApi(s, merkmaleByKuerzel))
				.toList();
	}

	private SchuelerSchulbesuchMerkmal mapApi(final DTOSchuelerMerkmale entity, final Map<String, Merkmal> merkmaleByKuerzel) {
		final var idMerkmal = Optional.ofNullable(entity.kuerzelMerkmal)
				.map(merkmaleByKuerzel::get)
				.map(m -> m.id)
				.orElse(null);
		return this.mapper.toApi(entity, idMerkmal);
	}

	/**
	 * Erstellt ein neuen Eintrag eines Schulbesuchmerkmals.
	 * Validiert die Eingabedaten und erstellt den Eintrag in einer Transaktion.
	 *
	 * @param dto die Daten für den neuen Eintrag
	 * @return der erstellte Eintrag
	 *
	 * @throws ApiOperationException wenn das Enddatum vor dem Startdatum liegt oder
	 * 								 wenn idMerkmal keinem bekannten Merkmal entspricht
	 */
	public SchuelerSchulbesuchMerkmal create(final SchuelerMerkmalCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var schuelermerkmal = this.validateCreate(dto);
			final var created = this.repository.create(schuelermerkmal);
			return this.mapper.toApi(created, dto.idMerkmal);
		});
	}

	/**
	 * Aktualisiert einen bestehenden Eintrag einer Schülermerkmals teilweise (PATCH).
	 * Nur die im Request angegebenen Felder werden aktualisiert.
	 * Die Operation wird in einer Transaktion ausgeführt.
	 * <p>
	 * Schule und Entlassart werden anhand ihrer IDs validiert und aufgelöst,
	 * bevor der Mapper die übrigen Felder auf die Entity anwendet.
	 * </p>
	 *
	 * @param id  die ID des zu aktualisierenden Eintrags
	 * @param dto die zu aktualisierenden Felder
	 * @return der aktualisierte Eintrag als API-Modell
	 * @throws ApiOperationException wenn die Schule oder Entlassart nicht gefunden wurde,
	 *                               das Enddatum vor dem Startdatum liegt
	 *                               oder der Eintrag nicht gefunden wurde
	 */
	public SchuelerSchulbesuchMerkmal patch(final long id, final SchuelerMerkmalPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var entity = repository.getById(id);
			validateAndResolvePatch(entity, dto);
			mapper.patch(dto, entity);

			final var idMerkmal = dto.idMerkmal.orElseGet(
					() -> merkmalService.getByKuerzel(entity.kuerzelMerkmal).id
			);

			return this.mapper.toApi(entity, idMerkmal);
		});
	}

	/**
	 * Löscht mehrere Einträge von Schülermerkmalen anhand ihrer IDs.
	 * Die Operation wird in einer Transaktion ausgeführt.
	 *
	 * @param idsToDelete die Liste der IDs der zu löschenden Einträge
	 * @return eine Liste von Antworten mit dem Status jeder Löschoperation, sortiert nach ID
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() -> {
			final var entitiesToDelete = repository.findListByIds(idsToDelete);

			return repository.delete(entitiesToDelete)
					.stream()
					.map(entry -> createResponseLog(entry.id))
					.sorted(Comparator.comparingLong(response -> response.id))
					.toList();
		});
	}

	private static SimpleOperationResponse createResponseLog(final long id) {
		final var log = new SimpleOperationResponse();
		log.id = id;
		log.success = true;
		return log;
	}

	private void validateAndResolvePatch(final DTOSchuelerMerkmale entity, final SchuelerMerkmalPatchRequest dto) {
		dto.idMerkmal.ifPresent(idMerkmal -> entity.kuerzelMerkmal = this.resolveMerkmal(idMerkmal));
		final var effectiveDatumVon = dto.datumVon.orElse(entity.datumVon);
		final var effectiveDatumBis = dto.datumBis.orElse(entity.datumBis);
		this.validateDatumBis(effectiveDatumBis, effectiveDatumVon);
	}

	private DTOSchuelerMerkmale validateCreate(final SchuelerMerkmalCreateRequest dto) {
		this.validateDatumBis(dto.datumBis, dto.datumVon);
		final var kuerzelMerkmal = this.resolveMerkmal(dto.idMerkmal);
		return this.mapper.toDomain(dto, kuerzelMerkmal);
	}

	private String resolveMerkmal(final long idMerkmal) {
		return this.merkmalService.getById(idMerkmal).kuerzel;
	}

	private void validateDatumBis(final String datumBis, final String datumVon) {
		if ((datumVon != null) && (datumBis != null)) {
			final var von = LocalDate.parse(datumVon);
			final var bis = LocalDate.parse(datumBis);
			if (bis.isBefore(von)) {
				throw new ApiOperationException(Response.Status.BAD_REQUEST, "Das Enddatum %s darf nicht vor dem Startdatum %s liegen".formatted(bis, von));
			}
		}
	}

}
