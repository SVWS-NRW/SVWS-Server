package de.svws_nrw.service.lehrer.funktion;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.lehrer.LehrerFunktion;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.katalog.DTOLeitungsfunktion;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerFunktion;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.LehrerFunktionMapper;
import de.svws_nrw.repo.RepositoryException;
import de.svws_nrw.repo.lehrer.funktion.LehrerFunktionRepository;
import de.svws_nrw.repo.lehrer.leitungsfunktion.LehrerLeitungsfunktionRepository;
import de.svws_nrw.repo.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenRepository;
import jakarta.ws.rs.core.Response;

public final class LehrerFunktionService {

	private static final String LEHRERFUNKTION_NOT_FOUND_BY_ID = "Keine Lehrerfunktion mit der ID %d gefunden.";
	private final LehrerFunktionRepository repo;
	private final LehrerPersonalabschnittsdatenRepository abschnittsdatenRepo;
	private final LehrerLeitungsfunktionRepository leitungsfunktionRepo;
	private final LehrerFunktionMapper mapper;

	/**
	 * Erstellt einen neuen {@code LehrerFunktionService}.
	 *
	 * @param repo                  das Repository für {@link DTOLehrerFunktion}-Einträge
	 * @param abschnittsdatenRepo   das Repository für {@link DTOLehrerAbschnittsdaten}-Einträge
	 * @param leitungsfunktionRepo  das Repository für {@link DTOLeitungsfunktion}-Einträge
	 * @param mapper                der Mapper zur Konvertierung zwischen Entity und API-Modell
	 */
	public LehrerFunktionService(final LehrerFunktionRepository repo,
			final LehrerPersonalabschnittsdatenRepository abschnittsdatenRepo,
			final LehrerLeitungsfunktionRepository leitungsfunktionRepo,
			final LehrerFunktionMapper mapper) {
		this.repo = repo;
		this.abschnittsdatenRepo = abschnittsdatenRepo;
		this.leitungsfunktionRepo = leitungsfunktionRepo;
		this.mapper = mapper;
	}

	/**
	 * Gibt eine {@link LehrerFunktion} anhand ihrer ID zurück.
	 *
	 * @param id die ID der Lehrerfunktion
	 * @return die Lehrerfunktion als API-Modell
	 */
	public LehrerFunktion get(final long id) {
		try {
			final var entity = this.repo.getById(id);
			return this.mapper.toApi(entity);
		} catch (final RepositoryException e) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, e,
					LEHRERFUNKTION_NOT_FOUND_BY_ID.formatted(id));
		}
	}

	/**
	 * Gibt alle {@link LehrerFunktion}en zurück.
	 *
	 * @return Liste aller Lehrerfunktionen als API-Modelle
	 */
	public List<LehrerFunktion> getAll() {
		return this.repo.getAll().stream()
				.map(this.mapper::toApi)
				.toList();
	}

	/**
	 * Gibt alle {@link LehrerFunktion}en zu einem Lehrerabschnitt zurück.
	 *
	 * @param idAbschnitt die ID der Lehrerabschnittsdaten
	 * @return Liste der zugehörigen Lehrerfunktionen als API-Modelle
	 */
	public List<LehrerFunktion> getListByIdAbschnitt(final long idAbschnitt) {
		return this.repo.findAllByIdAbschnitt(idAbschnitt).stream()
				.map(this.mapper::toApi)
				.toList();
	}

	/**
	 * Ermittelt die LehrerFunktion-Einträge gruppiert nach den IDs der Lehrerabschnittsdaten.
	 *
	 * @param idsLehrerAbschnittsdaten die IDs der Lehrerabschnittsdaten
	 * @return Map von Lehrerabschnittsdaten-ID auf Liste der zugehörigen LehrerFunktion
	 */
	public Map<Long, List<LehrerFunktion>> getListByIdLehrerAbschnittsdaten(final Collection<Long> idsLehrerAbschnittsdaten) {
		return repo.getListByIdLehrerAbschnittsdaten(idsLehrerAbschnittsdaten).entrySet().stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						entry -> entry.getValue().stream().map(this.mapper::toApi).toList()
				));
	}

	/**
	 * Erstellt eine neue {@link LehrerFunktion}.
	 *
	 * @param dto die Daten für den neuen Eintrag
	 * @return die erstellte Lehrerfunktion als API-Modell
	 */
	public LehrerFunktion create(final LehrerFunktionCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			validateCreate(dto);
			final var entity = this.mapper.toDomain(dto);
			final var created = this.repo.create(entity);
			return this.mapper.toApi(created);
		});
	}

	private void validateCreate(final LehrerFunktionCreateRequest dto) {
		if (this.abschnittsdatenRepo.findById(dto.idAbschnittsdaten).isEmpty()) {
			throw new ApiOperationException(Response.Status.NOT_FOUND,
					"Keine Lehrerabschnittsdaten mit der ID %d gefunden.".formatted(dto.idAbschnittsdaten));
		}
		if (this.leitungsfunktionRepo.findById(dto.idFunktion).isEmpty()) {
			throw new ApiOperationException(Response.Status.NOT_FOUND,
					"Keine Leitungsfunktion mit der ID %d gefunden.".formatted(dto.idFunktion));
		}
		if (this.repo.existsByIdAbschnittAndIdFunktion(dto.idAbschnittsdaten, dto.idFunktion)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST,
					"Die Lehrerfunktion mit der ID %d im Abschnitt mit der ID %d existiert bereits."
							.formatted(dto.idFunktion, dto.idAbschnittsdaten));
		}
	}

	/**
	 * Erstellt mehrere neue {@link LehrerFunktion}en.
	 *
	 * @param dtos die Daten für die neuen Einträge
	 * @return die erstellten Lehrerfunktionen als API-Modelle
	 */
	public List<LehrerFunktion> createMultiple(final Collection<LehrerFunktionCreateRequest> dtos) {
		return TransactionSupport.transactional(() -> {
			validateCreateMultiple(dtos);
			final var entities = dtos.stream()
					.map(this.mapper::toDomain)
					.toList();
			return this.repo.create(entities).stream()
					.map(this.mapper::toApi)
					.toList();
		});
	}

	private void validateCreateMultiple(final Collection<LehrerFunktionCreateRequest> dtos) {
		final var seen = new HashSet<AbschnittFunktionKey>();
		dtos.forEach(dto -> {
			assertNoDuplicateKeys(dto.idAbschnittsdaten, dto.idFunktion, seen);
			validateCreate(dto);
		});
	}

	/**
	 * Aktualisiert eine bestehende {@link LehrerFunktion} teilweise (PATCH).
	 *
	 * @param id  die ID der zu aktualisierenden Lehrerfunktion
	 * @param dto die zu aktualisierenden Felder
	 * @return die aktualisierte Lehrerfunktion als API-Modell
	 */
	public LehrerFunktion patch(final long id, final LehrerFunktionPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			try {
				final var entity = this.repo.getById(id);
				validatePatch(entity, dto);
				return patchEntity(dto, entity);
			} catch (final RepositoryException e) {
				throw new ApiOperationException(Response.Status.NOT_FOUND, e,
						LEHRERFUNKTION_NOT_FOUND_BY_ID.formatted(id));
			}
		});
	}

	private void validatePatch(final DTOLehrerFunktion entity, final LehrerFunktionPatchRequest dto) {
		dto.idFunktion.ifPresent(idFunktion -> {
			if (this.leitungsfunktionRepo.findById(idFunktion).isEmpty()) {
				throw new ApiOperationException(Response.Status.NOT_FOUND,
						"Keine Leitungsfunktion mit der ID %d gefunden.".formatted(idFunktion));
			}
			if (this.repo.existsByIdAbschnittAndIdFunktionExcludingId(entity.idAbschnittsdaten, idFunktion, entity.id)) {
				throw new ApiOperationException(Response.Status.BAD_REQUEST,
						"Die Lehrerfunktion mit der ID %d im Abschnitt mit der ID %d existiert bereits."
								.formatted(idFunktion, entity.idAbschnittsdaten));
			}
		});
	}

	/**
	 * Aktualisiert mehrere bestehende {@link LehrerFunktion}en teilweise (PATCH).
	 *
	 * @param dtos die Liste der zu aktualisierenden Einträge
	 * @return die aktualisierten Lehrerfunktionen als API-Modelle
	 */
	public List<LehrerFunktion> patchMultiple(final List<LehrerFunktionBatchPatchRequest> dtos) {
		return TransactionSupport.transactional(() -> {
			final var idsToPatch = dtos.stream()
					.map(dto -> dto.id)
					.toList();

			final var toPatchById = this.repo.findMapByIds(idsToPatch);
			validatePatchMultiple(toPatchById, dtos);

			return dtos.stream()
					.map(dto -> patchEntity(dto, toPatchById.get(dto.id)))
					.toList();
		});
	}

	private LehrerFunktion patchEntity(final LehrerFunktionPatchRequest dto, final DTOLehrerFunktion toPatch) {
		this.mapper.patch(dto, toPatch);
		return this.mapper.toApi(toPatch);
	}

	private void validatePatchMultiple(final Map<Long, DTOLehrerFunktion> entityMap, final List<LehrerFunktionBatchPatchRequest> dtos) {
		final var seen = new HashSet<AbschnittFunktionKey>();
		dtos.forEach(dto -> {
			final var entity = entityMap.get(dto.id);
			assertNoDuplicateKeys(entity.idAbschnittsdaten, dto.idFunktion.get(), seen);
			validatePatch(entity, dto);
		});
	}

	private static void assertNoDuplicateKeys(final Long idAbschnittsdaten, final Long idFunktion, final HashSet<AbschnittFunktionKey> seen) {
		final var key = new AbschnittFunktionKey(idAbschnittsdaten, idFunktion);
		if (!seen.add(key)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST,
					"Die Lehrerfunktion mit der ID %d im Abschnitt mit der ID %d ist in der Anfrage mehrfach vorhanden."
							.formatted(idFunktion, idAbschnittsdaten));
		}
	}

	/**
	 * Löscht eine {@link LehrerFunktion} anhand ihrer ID.
	 *
	 * @param id die ID des zu löschenden Eintrags
	 * @return eine {@link SimpleOperationResponse} mit dem Ergebnis der Operation
	 */
	public SimpleOperationResponse delete(final long id) {
		return TransactionSupport.transactional(() -> {
			try {
				final var entity = this.repo.getById(id);
				this.repo.delete(entity);
				return SimpleOperationResponse.ofSuccess(id);
			} catch (final RepositoryException e) {
				return SimpleOperationResponse.ofError(id,
						LEHRERFUNKTION_NOT_FOUND_BY_ID.formatted(id));
			}
		});
	}

	/**
	 * Löscht mehrere {@link LehrerFunktion}en anhand ihrer IDs.
	 *
	 * @param ids die IDs der zu löschenden Einträge
	 * @return Liste von {@link SimpleOperationResponse} mit dem Ergebnis je Eintrag
	 */
	public List<SimpleOperationResponse> deleteMultiple(final Collection<Long> ids) {
		return TransactionSupport.transactional(() -> {
			final var entities = this.repo.findListByIds(ids);
			final var deletedEntities = repo.delete(entities);

			final var deletedIds = deletedEntities.stream()
					.map(e -> e.id)
					.toList();

			return ids.stream()
					.map(id -> deletedIds.contains(id)
							? SimpleOperationResponse.ofSuccess(id)
							: SimpleOperationResponse.ofError(id,
									LEHRERFUNKTION_NOT_FOUND_BY_ID.formatted(id)))
					.toList();
		});
	}


	private record AbschnittFunktionKey(long idAbschnittsdaten, long idFunktion) {
	}
}
