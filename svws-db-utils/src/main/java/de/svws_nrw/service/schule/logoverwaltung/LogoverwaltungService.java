package de.svws_nrw.service.schule.logoverwaltung;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Logo;
import de.svws_nrw.core.types.reporting.ReportingBildDefinition;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schule.DTOLogo;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.logoverwaltung.LogoverwaltungMapper;
import de.svws_nrw.repo.schule.logoverwaltung.LogoverwaltungRepository;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.Strings;
import org.openapitools.jackson.nullable.JsonNullable;

import static de.svws_nrw.data.TransactionSupport.transactional;

public class LogoverwaltungService {

	private static final Set<String> ALLOWED_IMAGE_MIME_TYPES = Set.of("image/png", "image/gif", "image/jpeg", "image/svg+xml", "image/tiff");
	private static final String NOT_FOUND_MESSAGE = "Es wurde kein Logo mit der ID %d gefunden.";
	private static final double MAX_LOGO_SIZE_KB = 2 * 1024d;

	private final LogoverwaltungRepository repository;
	private final LogoverwaltungMapper mapper;

	private final EigeneSchuleService eigeneSchuleService;
	private final Clock clock;

	/**
	 * Erstellt einen neuen Service für die Logoverwaltung.
	 *
	 * @param repository das Repository für die Logoverwaltung
	 * @param mapper    der Mapper für die Logoverwaltung
	 * @param eigeneSchuleService der Service für die Schule
	 * @param clock die Clock für die Zeitangaben
	 */
	public LogoverwaltungService(final LogoverwaltungRepository repository,
			final LogoverwaltungMapper mapper,
			final EigeneSchuleService eigeneSchuleService,
			final Clock clock) {
		this.repository = repository;
		this.mapper = mapper;
		this.eigeneSchuleService = eigeneSchuleService;
		this.clock = clock;
	}

	/**
	 * Gibt das Logo mit der angegebenen ID zurück.
	 *
	 * @param id die ID des Logos
	 *
	 * @return das Logo mit der angegebenen ID
	 */
	public Logo getById(final Long id) {
		final var entity = repository.findById(id)
				.orElseThrow(() -> new ApiOperationException(
						Response.Status.NOT_FOUND,
						NOT_FOUND_MESSAGE.formatted(id))
				);
		return toApi(entity);
	}

	/**
	 * Gibt eine Liste von allen verfügbaren Logos zurück.
	 *
	 * @return die Liste der Logos
	 */
	public List<Logo> getAll() {
		return repository.getAll().stream()
				.map(this::toApi)
				.toList();
	}

	/**
	 * Erstellt ein neues Logo.
	 *
	 * @param createRequest das CreateRequest-Objekt
	 * @return das erstellte Logo
	 */
	public Logo create(final LogoCreateRequest createRequest) {
		return transactional(() -> {
			prepareCreate(createRequest);
			validateCreate(createRequest);

			var entity = mapper.toDomain(createRequest);
			entity = repository.create(entity);
			return toApi(entity);
		});
	}

	/**
	 * Aktualisiert ein Logo.
	 *
	 * @param patchRequest das PatchRequest-Objekt
	 * @param id           die ID des Logos
	 *
	 * @return das aktualisierte Logo
	 */
	public Logo patch(final Long id, final LogoPatchRequest patchRequest) {
		return transactional(() -> {
			preparePatch(patchRequest);
			validatePatch(patchRequest);

			var entity = repository.findById(id)
					.orElseThrow(() -> new ApiOperationException(
							Response.Status.NOT_FOUND,
							NOT_FOUND_MESSAGE.formatted(id))
					);
			applyPatch(entity, patchRequest);
			entity = repository.update(entity);

			return toApi(entity);
		});
	}

	/**
	 * Löscht ein Logo mit der übergebenen ID.
	 *
	 * @param id die ID der Logos das gelöscht werden sollen
	 *
	 * @return {@link SimpleOperationResponse}
	 */
	public SimpleOperationResponse delete(final Long id) {
		return transactional(() -> repository.findById(id)
				.map(entity -> {
					final var deleted = repository.delete(entity);
					return SimpleOperationResponse.ofSuccess(deleted.id);
				})
				.orElse(SimpleOperationResponse.ofError(id, NOT_FOUND_MESSAGE.formatted(id))));
	}

	/**
	 * Löscht mehrere Logos.
	 *
	 * @param ids die IDs der Logos die gelöscht werden sollen
	 *
	 * @return eine Liste der gelöschten Logos
	 */
	public List<SimpleOperationResponse> delete(final List<Long> ids) {
		return TransactionSupport.transactional(() ->
				BulkDeleteUtils.delete(
						ids,
						repository,
						e -> e.id,
						"Logoverwaltung"
				)
		);
	}

	private Logo toApi(final DTOLogo entity) {
		final var dto = mapper.toApi(entity);
		// fix DATA-URL, falls möglich
		dto.logoBase64 = DataUrlResolver.resolve(dto.logoBase64)
				.map(DataUrl::value)
				.orElse(dto.logoBase64);
		return dto;
	}

	private void applyPatch(final DTOLogo entity, final LogoPatchRequest patchRequest) {
		patchRequest.logoBase64.ifPresent(value -> {
			if (isLogoDifferent(entity.logoBase64, value)) {
				entity.hinzugefuegtAm = LocalDate.now(clock).format(DateTimeFormatter.ISO_LOCAL_DATE);
			}
			entity.logoBase64 = value;
		});
	}

	private void validateCreate(final LogoCreateRequest createRequest) {
		validateKennung(createRequest.kennung);
		validateLogoBase64(createRequest.logoBase64);
	}

	private void validatePatch(final LogoPatchRequest patchRequest) {
		validateLogoBase64(patchRequest.logoBase64.get());
	}

	private void validateKennung(final String kennung) {
		final var bildDefinition = resolveReportingBildDefinition(kennung);
		if (bildDefinition == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Der angegebene Wert für 'kennung' ist nicht zulässig.");
		}

		final var kennungAlreadyExists = repository.existsByKennung(bildDefinition);
		if (kennungAlreadyExists) {
			throw new ApiOperationException(
					Response.Status.BAD_REQUEST,
					"Es existiert bereits ein Logo mit der Kennung %s. Es kann kein weiteres Logo mit gleicher Kennung hinterlegt werden.".formatted(kennung)
			);
		}
	}

	private ReportingBildDefinition resolveReportingBildDefinition(final String kennung) {
		return ReportingBildDefinition.getByKennungAndSchulform(kennung, eigeneSchuleService.getSchulform());
	}

	private static void validateLogoBase64(final String logoBase64) {
		final var resolvedDataUrl = DataUrlResolver.resolve(logoBase64)
				.orElseThrow(() -> new ApiOperationException(
						Response.Status.BAD_REQUEST,
						"Der übergebene Base64-String enthält keine validen Daten."
				));

		if (!resolvedDataUrl.hasAnyMimeTypeOf(ALLOWED_IMAGE_MIME_TYPES)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Der MIME-Type des Logos ist nicht zulässig.");
		}

		if (resolvedDataUrl.sizeInKB() > MAX_LOGO_SIZE_KB) {
			throw new ApiOperationException(Response.Status.REQUEST_ENTITY_TOO_LARGE, "Das Logo überschreitet die maximale Größe von 2MB.");
		}
	}

	private static boolean isLogoDifferent(final String logoOld, final String logoNew) {
		return !Strings.CS.equals(logoOld, logoNew);
	}

	private static void prepareCreate(final LogoCreateRequest createRequest) {
		createRequest.logoBase64 = DataUrlResolver.resolve(createRequest.logoBase64)
				.map(DataUrl::payload)
				.orElse(createRequest.logoBase64);
	}

	private static void preparePatch(final LogoPatchRequest patchRequest) {
		patchRequest.logoBase64.ifPresent(value ->
				patchRequest.logoBase64 = JsonNullable.of(DataUrlResolver.resolve(value)
						.map(DataUrl::payload)
						.orElse(value))
		);
	}
}
