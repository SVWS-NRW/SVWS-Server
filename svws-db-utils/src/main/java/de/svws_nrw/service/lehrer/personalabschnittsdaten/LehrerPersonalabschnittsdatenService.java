package de.svws_nrw.service.lehrer.personalabschnittsdaten;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenMapper;
import de.svws_nrw.mapper.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenMappingContext;
import de.svws_nrw.repo.RepositoryException;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.repo.schule.kataloge.schule.SchuleRepository;
import de.svws_nrw.service.lehrer.anrechnung.LehrerAnrechnungsstundeService;
import de.svws_nrw.service.lehrer.mehrleistung.LehrerMehrleistungService;
import de.svws_nrw.service.lehrer.minderleistung.LehrerMinderleistungService;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.apache.commons.collections4.CollectionUtils;
import org.openapitools.jackson.nullable.JsonNullable;

public final class LehrerPersonalabschnittsdatenService {

	private final LehrerPersonalabschnittsdatenRepository repo;
	private final LehrerRepository lehrerRepo;
	private final SchuleRepository schulenRepo;
	private final SchuljahresabschnitteRepository schuljahresabschnitteRepo;

	private final LehrerAnrechnungsstundeService anrechnungsService;
	private final LehrerMehrleistungService mehrleistungService;
	private final LehrerMinderleistungService minderleistungService;
	private final LehrerFunktionService funktionService;

	private final LehrerPersonalabschnittsdatenMapper mapper;

	private static final String DATEN_NOT_FOUND_BY_ID = "Keine Personalabschnittsdaten mit der ID %d gefunden.";


	/**
	 * @param repos {@link LehrerPersonalabschnittsdatenRepos}
	 * @param subServices {@link LehrerPersonalabschnittsdatenSubServices}
	 * @param mapper {@link LehrerPersonalabschnittsdatenMapper}
	 */
	public LehrerPersonalabschnittsdatenService(
			final LehrerPersonalabschnittsdatenRepos repos,
			final LehrerPersonalabschnittsdatenSubServices subServices,
			final LehrerPersonalabschnittsdatenMapper mapper) {
		this.repo = repos.lehrerPersonalabschnittsdatenRepo();
		this.lehrerRepo = repos.lehrerRepo();
		this.schulenRepo = repos.schulenRepo();
		this.schuljahresabschnitteRepo = repos.schuljahresabschnitteRepo();
		this.anrechnungsService = subServices.anrechnungsService();
		this.mehrleistungService = subServices.mehrleistungService();
		this.minderleistungService = subServices.minderleistungService();
		this.funktionService = subServices.funktionService();
		this.mapper = mapper;
	}

	/**
	 * Gibt eine {@link LehrerPersonalabschnittsdaten} anhand ihrer ID zurück.
	 *
	 * @param id id
	 *
	 * @return {@link LehrerPersonalabschnittsdaten}
	 */
	public LehrerPersonalabschnittsdaten get(final long id) {
		final var entity = repo.findById(id)
				.orElseThrow(() -> new ApiOperationException(
						Status.NOT_FOUND,
						DATEN_NOT_FOUND_BY_ID.formatted(id)
				));
		return toApi(entity);
	}

	/**
	 * Gibt die {@link LehrerPersonalabschnittsdaten} für den Lehrer mit der gegebenen ID zurück.
	 *
	 * @param idLehrer idLehrer
	 *
	 * @return {@link List<LehrerPersonalabschnittsdaten>}
	 */
	public List<LehrerPersonalabschnittsdaten> getByIdLehrer(final long idLehrer) {
		final var entities = repo.findByIdLehrer(idLehrer);
		return toApi(entities);
	}

	/**
	 * Gibt alle {@link LehrerPersonalabschnittsdaten} zu übergebenen IDs zurück.
	 *
	 * @param ids für das Auflösen benötigte Identifier
	 *
	 * @return Liste aller {@link LehrerPersonalabschnittsdaten} als API-Modelle
	 */
	public List<LehrerPersonalabschnittsdaten> getList(final List<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return List.of();
		}
		final var entities = repo.findListByIds(ids);
		return toApi(entities);
	}

	/**
	 * Erstellt eine neue {@link LehrerPersonalabschnittsdaten}.
	 *
	 * @param dto die Daten für den neuen Eintrag
	 *
	 * @return die erstellten LehrerPersonalabschnittsdaten als API-Modell
	 */
	public LehrerPersonalabschnittsdaten create(final LehrerPersonalabschnittsdatenCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var entity = validateAndResolveCreate(dto);
			final var created = repo.create(entity);
			return toApi(created);
		});
	}

	/**
	 * Erstellt mehrere neue {@link LehrerPersonalabschnittsdaten}.
	 *
	 * @param dtos die Daten für die neuen Einträge
	 *
	 * @return die erstellten LehrerPersonalabschnittsdaten als API-Modelle
	 */
	public List<LehrerPersonalabschnittsdaten> createMultiple(final Collection<LehrerPersonalabschnittsdatenCreateRequest> dtos) {
		return TransactionSupport.transactional(() -> {
			final var entities = dtos.stream()
					.map(this::validateAndResolveCreate)
					.toList();
			final var created = this.repo.create(entities);
			return toApi(created);
		});
	}

	/**
	 * Aktualisiert eine bestehende {@link LehrerPersonalabschnittsdaten} teilweise (PATCH).
	 *
	 * @param id die ID der zu aktualisierenden LehrerPersonalabschnittsdaten
	 * @param dto die zu aktualisierenden Felder
	 *
	 * @return die aktualisierten LehrerPersonalabschnittsdaten als API-Modell
	 */
	public LehrerPersonalabschnittsdaten patch(final long id, final LehrerPersonalabschnittsdatenPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var entity = this.repo.findById(id)
					.orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, DATEN_NOT_FOUND_BY_ID.formatted(id)));
			applyPatchableFields(dto, entity);
			return toApi(entity);
		});
	}

	/**
	 * Aktualisiert mehrere bestehende {@link LehrerPersonalabschnittsdaten} teilweise (PATCH).
	 *
	 * @param dtos die Liste der zu aktualisierenden Einträge
	 *
	 * @return die aktualisierten LehrerPersonalabschnittsdaten als API-Modelle
	 */
	public List<LehrerPersonalabschnittsdaten> patchMultiple(final List<LehrerPersonalabschnittsdatenBatchPatchRequest> dtos) {
		return TransactionSupport.transactional(() -> {
			final var idsToPatch = dtos.stream()
					.map(dto -> dto.id)
					.toList();

			final var toPatchById = this.repo.findMapByIds(idsToPatch);

			final var patchedEntities = dtos.stream()
					.map(dto -> {
						final var entity = toPatchById.get(dto.id);
						if (entity == null) {
							throw new ApiOperationException(Response.Status.NOT_FOUND,
									DATEN_NOT_FOUND_BY_ID.formatted(dto.id));
						}
						applyPatchableFields(dto, entity);
						return entity;
					})
					.toList();

			return toApi(patchedEntities);
		});
	}

	/**
	 * Löscht eine {@link LehrerPersonalabschnittsdaten} anhand ihrer ID.
	 *
	 * @param id die ID des zu löschenden Eintrags
	 *
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
						DATEN_NOT_FOUND_BY_ID.formatted(id));
			}
		});
	}

	/**
	 * Löscht mehrere {@link LehrerPersonalabschnittsdaten} anhand ihrer IDs.
	 *
	 * @param ids die IDs der zu löschenden Einträge
	 *
	 * @return Liste von {@link SimpleOperationResponse} mit dem Ergebnis je Eintrag
	 */
	public List<SimpleOperationResponse> deleteMultiple(final Collection<Long> ids) {
		return TransactionSupport.transactional(() -> {
			final var entities = this.repo.findListByIds(ids);
			repo.delete(entities);

			final var foundIds = entities.stream().map(e -> e.ID).collect(Collectors.toSet());

			return ids.stream()
					.map(id -> foundIds.contains(id)
							? SimpleOperationResponse.ofSuccess(id)
							: SimpleOperationResponse.ofError(id, DATEN_NOT_FOUND_BY_ID.formatted(id)))
					.toList();
		});
	}

	private DTOLehrerAbschnittsdaten validateAndResolveCreate(final LehrerPersonalabschnittsdatenCreateRequest dto) {
		if (!lehrerRepo.existsById(dto.idLehrer)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Kein Lehrer für die ID %d gefunden".formatted(dto.idLehrer));
		}
		if (!schuljahresabschnitteRepo.existsById(dto.idSchuljahresabschnitt)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Kein Schuljahresabschnitt für die ID %d gefunden".formatted(dto.idSchuljahresabschnitt));
		}
		final var entity = new DTOLehrerAbschnittsdaten(repo.getNextID(), dto.idLehrer, dto.idSchuljahresabschnitt);
		applyPatchableFields(dto, entity);
		return entity;
	}

	private void applyPatchableFields(final LehrerPersonalabschnittsdatenPatchable dto, final DTOLehrerAbschnittsdaten entity) {
		applyIfPresent(dto.getPflichtstundensoll(), v -> entity.PflichtstdSoll = v);
		applyIfPresent(dto.getIdRechtsverhaeltnis(), v -> applyPatchIdRechtsverhaeltnis(v, entity));
		applyIfPresent(dto.getIdBeschaeftigungsart(), v -> applyPatchIdBeschaeftigungsart(v, entity));
		applyIfPresent(dto.getIdEinsatzstatus(), v -> applyPatchIdEinsatzstatus(v, entity));
		applyIfPresent(dto.getStammschulnummer(), v -> applyPatchStammschulnummer(v, entity));
	}

	private static <T> void applyIfPresent(final JsonNullable<T> nullable, final Consumer<T> setter) {
		if (nullable.isPresent()) {
			setter.accept(nullable.get());
		}
	}

	private void applyPatchIdRechtsverhaeltnis(final Long idRechtsverhaeltnis, final DTOLehrerAbschnittsdaten entity) {
		if (idRechtsverhaeltnis == null) {
			entity.Rechtsverhaeltnis = null;
			return;
		}
		try {
			entity.Rechtsverhaeltnis = LehrerRechtsverhaeltnis.data().getEintragByID(idRechtsverhaeltnis).kuerzel;
		} catch (final Exception e) {
			throw new ApiOperationException(Status.BAD_REQUEST, e, "Kein gültigen Rechtsverhältniseintrag zur id %d gefunden".formatted(idRechtsverhaeltnis));
		}
	}

	private void applyPatchIdBeschaeftigungsart(final Long idBeschaeftigungsart, final DTOLehrerAbschnittsdaten entity) {
		if (idBeschaeftigungsart == null) {
			entity.Beschaeftigungsart = null;
			return;
		}
		try {
			entity.Beschaeftigungsart = LehrerBeschaeftigungsart.data().getEintragByID(idBeschaeftigungsart).kuerzel;
		} catch (final Exception e) {
			throw new ApiOperationException(Status.BAD_REQUEST, e, "Keine gültige Beschäftigungsart zur id %d gefunden".formatted(idBeschaeftigungsart));
		}
	}

	private void applyPatchIdEinsatzstatus(final Long idEinsatzstatus, final DTOLehrerAbschnittsdaten entity) {
		if (idEinsatzstatus == null) {
			entity.Einsatzstatus = null;
			return;
		}
		try {
			entity.Einsatzstatus = LehrerEinsatzstatus.data().getEintragByID(idEinsatzstatus).kuerzel;
		} catch (final Exception e) {
			throw new ApiOperationException(Status.BAD_REQUEST, e, "Kein gültigen Einsatzstatuseintrag zur id %d gefunden".formatted(idEinsatzstatus));
		}
	}

	private void applyPatchStammschulnummer(final String schulnummer, final DTOLehrerAbschnittsdaten entity) {
		if (schulnummer == null) {
			entity.StammschulNr = null;
			return;
		}
		if (!schulenRepo.existsBySchulnummer(schulnummer)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Keine Schule für die Schulnummer %s gefunden".formatted(schulnummer));
		}
		entity.StammschulNr = schulnummer;
	}

	private LehrerPersonalabschnittsdaten toApi(final DTOLehrerAbschnittsdaten entity) {
		return toApi(List.of(entity)).getFirst();
	}

	private List<LehrerPersonalabschnittsdaten> toApi(final List<DTOLehrerAbschnittsdaten> entities) {
		final var ctxById = ContextLoader.load(entities, anrechnungsService, mehrleistungService, minderleistungService, funktionService);

		final var abschnittIds = entities.stream()
				.map(e -> e.Schuljahresabschnitts_ID)
				.toList();

		final var schuljahrByIdAbschnitt = getSchuljahrByIdAbschnitt(abschnittIds);

		return entities.stream()
				.map(entity -> {
					final var schuljahr = schuljahrByIdAbschnitt.getOrDefault(entity.Schuljahresabschnitts_ID, null);
					final var ctx = ctxById.getOrDefault(entity.ID, LehrerPersonalabschnittsdatenMappingContext.empty());
					return mapper.toApi(entity, schuljahr, ctx);
				})
				.toList();
	}

	private Map<Long, Integer> getSchuljahrByIdAbschnitt(final List<Long> abschnittIds) {
		if (CollectionUtils.isEmpty(abschnittIds)) {
			return Collections.emptyMap();
		}

		final var unique = abschnittIds.stream().distinct().toList();
		final var result = schuljahresabschnitteRepo.findListByIds(unique).stream()
				.collect(Collectors.toMap(
						dto -> dto.ID,
						dto -> dto.Jahr
				));
		validateAbschnitteForAllIdsWhereFound(unique, result);
		return result;
	}

	private void validateAbschnitteForAllIdsWhereFound(final List<Long> unique, final Map<Long, Integer> result) {
		final var missing = unique.stream()
				.filter(id -> !result.containsKey(id))
				.toList();

		if (!missing.isEmpty()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Keine Schuljahresabschnitte gefunden für IDs: " + missing);
		}
	}

	private static final class ContextLoader {

		private ContextLoader() {
		}

		static Map<Long, LehrerPersonalabschnittsdatenMappingContext> load(
				final List<DTOLehrerAbschnittsdaten> entities,
				final LehrerAnrechnungsstundeService anrechnungsService,
				final LehrerMehrleistungService mehrleistungService,
				final LehrerMinderleistungService minderleistungService,
				final LehrerFunktionService funktionService) {

			if (CollectionUtils.isEmpty(entities)) {
				return Collections.emptyMap();
			}
			final var ids = entities.stream().map(e -> e.ID).toList();
			final var anrechnungenById = anrechnungsService.getListByIdLehrerAbschnittsdaten(ids);
			final var mehrleistungenById = mehrleistungService.getListByIdLehrerAbschnittsdaten(ids);
			final var minderleistungenById = minderleistungService.getListByIdLehrerAbschnittsdaten(ids);
			final var funktionenById = funktionService.getListByIdLehrerAbschnittsdaten(ids);

			return ids.stream()
					.collect(Collectors.toMap(
							Function.identity(),
							id -> new LehrerPersonalabschnittsdatenMappingContext(
									anrechnungenById.getOrDefault(id, Collections.emptyList()),
									mehrleistungenById.getOrDefault(id, Collections.emptyList()),
									minderleistungenById.getOrDefault(id, Collections.emptyList()),
									funktionenById.getOrDefault(id, Collections.emptyList())
							)
					));
		}
	}
}
