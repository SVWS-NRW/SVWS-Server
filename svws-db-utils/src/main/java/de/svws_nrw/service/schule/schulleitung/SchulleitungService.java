package de.svws_nrw.service.schule.schulleitung;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import de.svws_nrw.asd.data.schule.Schulleitung;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOSchulleitung;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.schulleitung.SchulleitungMapper;
import de.svws_nrw.repo.lehrer.leitungsfunktion.LehrerLeitungsfunktionRepository;
import de.svws_nrw.repo.schule.schulleitung.SchulleitungRepository;
import jakarta.ws.rs.core.Response;

/**
 * Service-Klasse für die Verwaltung von Merkmalen.
 * Stellt CRUD-Operationen für Schulleitung bereit und führt Validierungen durch.
 */
public final class SchulleitungService {

	private final SchulleitungRepository repository;
	private final LehrerLeitungsfunktionRepository lehrerLeitungsfunktionRepository;
	private final SchulleitungMapper mapper;

	/**
	 * Erstellt einen neuen SchulleitungService.
	 *
	 * @param repository                  das Repository für den Datenbankzugriff auf Merkmale
	 * @param lehrerLeitungsfunktionRepository  das Repository für den Datenbankzugriff auf Leitungsfunktionen
	 * @param mapper                      der Mapper zur Konvertierung zwischen Domain- und API-Modellen
	 */
	public SchulleitungService(
			final SchulleitungRepository repository,
			final LehrerLeitungsfunktionRepository lehrerLeitungsfunktionRepository,
			final SchulleitungMapper mapper) {
		this.repository = repository;
		this.lehrerLeitungsfunktionRepository = lehrerLeitungsfunktionRepository;
		this.mapper = mapper;
	}

	/**
	 * Gibt alle Schulleitungseinträge zurück.
	 *
	 * @return Liste aller Einträge
	 */
	public List<Schulleitung> getAll() {
		return this.repository.getAll()
				.stream()
				.map(this.mapper::toApi)
				.toList();
	}

	/**
	 * Gibt alle Schulleitungseinträge für einen bestimmten Lehrer zurück.
	 *
	 * @param  idLehrer die ID des Lehrers
	 * @return Liste der Einträge, leer wenn keine vorhanden
	 */
	public List<Schulleitung> getAllByIdLehrer(final long idLehrer) {
		return this.repository.getAllByIdLehrer(idLehrer)
				.stream()
				.map(this.mapper::toApi)
				.toList();
	}

	/**
	 * Erstellt einen neuen Schulleitungseintrag.
	 * Validiert die Eingabedaten und erstellt den Eintrag in einer Transaktion.
	 * @param  dto die Daten für den neuen Eintrag
	 * @return der erstellte Eintrag als API-Modell
	 * @throws ApiOperationException wenn das Enddatum vor dem Startdatum liegt oder die Leitungsfunktion nicht existiert
	 */
	public Schulleitung create(final SchulleitungCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			this.validateCreate(dto);
			final var entity = this.mapper.toDomain(dto);
			final var created = this.repository.create(entity);
			return this.mapper.toApi(created);
		});
	}

	/**
	 * Aktualisiert einen bestehenden Schulleitungseintrag teilweise (PATCH).
	 * Nur die im Request angegebenen Felder werden aktualisiert.
	 *
	 * @param id  die ID des zu aktualisierenden Eintrags
	 * @param dto die zu aktualisierenden Felder
	 * @return der aktualisierte Eintrag als API-Modell
	 * @throws ApiOperationException wenn die Leitungsfunktion nicht gefunden wurde, das Enddatum vor dem Startdatum liegt oder der Eintrag nicht gefunden wurde
	 */
	public Schulleitung patch(final long id, final SchulleitungPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var entity = this.repository.getById(id);
			this.validateAndResolvePatch(entity, dto);
			this.mapper.patch(dto, entity);
			return this.mapper.toApi(entity);
		});
	}

	/**
	 * Löscht mehrere Schulleitungseinträge anhand ihrer IDs.
	 * Die Operation wird in einer Transaktion ausgeführt.
	 *
	 * @param idsToDelete die Liste der IDs der zu löschenden Einträge
	 * @return eine Liste von Antworten mit dem Status jeder Löschoperation, sortiert nach ID
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsToDelete) {
		return TransactionSupport.transactional(() -> {
			final var entitiesToDelete = this.repository.findListByIds(idsToDelete);
			return this.repository.delete(entitiesToDelete)
					.stream()
					.map(entry -> createResponseLog(entry.ID))
					.sorted(Comparator.comparingLong(response -> response.id))
					.toList();
		});
	}

	private void validateCreate(final SchulleitungCreateRequest dto) {
		this.validateLeitungsfunktion(dto.idLeitungsfunktion);
		this.validateDatumBis(dto.datumEndeLeitungsfunktion, dto.datumBeginnLeitungsfunktion);
	}

	private void validateAndResolvePatch(final DTOSchulleitung entity,
			final SchulleitungPatchRequest dto) {
		dto.idLeitungsfunktion.ifPresent(this::validateLeitungsfunktion);

		final var effectiveDatumVon = dto.datumBeginnLeitungsfunktion.orElse(entity.Von);
		final var effectiveDatumBis = dto.datumEndeLeitungsfunktion.orElse(entity.Bis);
		this.validateDatumBis(effectiveDatumBis, effectiveDatumVon);
	}

	private void validateLeitungsfunktion(final long idLeitungsfunktion) {
		if (this.lehrerLeitungsfunktionRepository.findById(idLeitungsfunktion).isEmpty()) {
			throw new ApiOperationException(
					Response.Status.BAD_REQUEST,
					"Die Leitungsfunktion mit der ID %d existiert nicht".formatted(idLeitungsfunktion)
			);
		}
	}

	private void validateDatumBis(final String datumBis, final String datumVon) {
		if ((datumVon != null) && (datumBis != null)) {
			final var von = LocalDate.parse(datumVon);
			final var bis = LocalDate.parse(datumBis);
			if (bis.isBefore(von)) {
				throw new ApiOperationException(
						Response.Status.BAD_REQUEST,
						"Das Enddatum %s darf nicht vor dem Startdatum %s liegen".formatted(bis, von)
				);
			}
		}
	}

	private static SimpleOperationResponse createResponseLog(final long id) {
		final var log = new SimpleOperationResponse();
		log.id = id;
		log.success = true;
		return log;
	}


}
