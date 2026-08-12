package de.svws_nrw.controller.schule.kataloge.fachklasse;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schule.katalog.fachklasse.FachklasseEintragCreateRequest;
import de.svws_nrw.service.schule.katalog.fachklasse.FachklasseEintragPatchRequest;
import de.svws_nrw.service.schule.katalog.fachklasse.FachklasseService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;

public final class FachklasseController {

	private final FachklasseService service;

	/**
	 * @param service {@link FachklasseService}
	 */
	public FachklasseController(final FachklasseService service) {
		this.service = service;
	}

	/**
	 * Ruft alle Fachklassen-Entitäten ab.
	 *
	 * @return eine Response mit der Liste aller Fachklassen-Entitäten
	 */
	public Response getAll() {
		return Responses.ok(service.getAll());
	}

	/**
	 * Erstellt eine neue Fachklassen-Entität.
	 * <p>
	 * Das Request-DTO wird vor der Verarbeitung validiert.
	 * </p>
	 *
	 * @param dto das Request-Objekt mit den Daten für die neue Fachklasse
	 * @return eine Response mit der erstellten Fachklassen-Entität
	 * @throws ValidationException wenn die DTO-Validierung fehlschlägt
	 */
	public Response create(final FachklasseEintragCreateRequest dto) {
		BeanValidator.validate(dto);
		final var created = service.create(dto);
		return Responses.created(created);
	}

	/**
	 * Löscht mehrere Fachklassen-Entitäten anhand ihrer IDs.
	 *
	 * @param ids eine Liste von IDs der zu löschenden Fachklassen-Entitäten
	 * @return eine Response mit den Löschergebnissen
	 */
	public Response delete(final List<Long> ids) {
		final var responses = service.delete(ids);
		return Responses.ok(responses);
	}

	/**
	 * Aktualisiert eine bestehende Fachklassen-Entität teilweise.
	 * <p>
	 * Das Request-DTO wird vor der Verarbeitung validiert.
	 * </p>
	 *
	 * @param id die ID der zu aktualisierenden Fachklassen-Entität
	 * @param dto das Request-Objekt mit den zu aktualisierenden Feldern
	 * @return eine Response mit der aktualisierten Fachklassen-Entität
	 * @throws ValidationException wenn die DTO-Validierung fehlschlägt
	 */
	public Response patch(final long id, final FachklasseEintragPatchRequest dto) {
		BeanValidator.validate(dto);
		final var patched = this.service.patch(id, dto);
		return Responses.ok(patched);
	}

}
