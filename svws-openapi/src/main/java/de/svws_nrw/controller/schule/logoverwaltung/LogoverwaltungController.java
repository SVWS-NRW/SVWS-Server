package de.svws_nrw.controller.schule.logoverwaltung;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schule.logoverwaltung.LogoCreateRequest;
import de.svws_nrw.service.schule.logoverwaltung.LogoPatchRequest;
import de.svws_nrw.service.schule.logoverwaltung.LogoverwaltungService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;

public final class LogoverwaltungController {

	private final LogoverwaltungService service;

	/**
	 * @param service {@link LogoverwaltungService}
	 */
	public LogoverwaltungController(final LogoverwaltungService service) {
		this.service = service;
	}

	/**
	 * Ruft alle Logo-Entitäten ab.
	 *
	 * @return eine Response mit der Liste aller Logo-Entitäten
	 */
	public Response getAll() {
		return Responses.ok(service.getAll());
	}

	/**
	 * Erstellt eine neue Logo-Entität.
	 * <p>
	 * Das Request-DTO wird vor der Verarbeitung validiert.
	 * </p>
	 *
	 * @param createRequest das Request-Objekt mit den Daten für die neue Logo-Entität
	 * @return eine Response mit der erstellten Logo-Entität
	 * @throws ValidationException wenn die DTO-Validierung fehlschlägt
	 */
	public Response create(final LogoCreateRequest createRequest) {
		BeanValidator.validate(createRequest);
		final var created = service.create(createRequest);
		return Responses.created(created);
	}

	/**
	 * Aktualisiert eine bestehende Logo-Entität teilweise.
	 * Das Request-DTO wird vor der Verarbeitung validiert.
	 *
	 * @param id die ID der zu aktualisierenden Logo-Entität
	 * @param patchRequest das Request-Objekt mit den zu aktualisierenden Feldern
	 *
	 * @return eine Response mit der aktualisierten Logo-Entität
	 *
	 * @throws ValidationException wenn die DTO-Validierung fehlschlägt
	 */
	public Response patch(final long id, final LogoPatchRequest patchRequest) {
		BeanValidator.validate(patchRequest);
		final var patched = this.service.patch(id, patchRequest);
		return Responses.ok(patched);
	}

	/**
	 * Löscht eine Logo-Entität anhand ihrer ID.
	 *
	 * @param id die ID der zu löschenden Logo-Entität
	 *
	 * @return eine Response mit dem Löschergebnis
	 */
	public Response delete(final Long id) {
		final var response = service.delete(id);
		return Responses.ok(response);
	}

	/**
	 * Löscht mehrere Logo-Entitäten anhand ihrer IDs.
	 *
	 * @param ids eine Liste von IDs der zu löschenden Logo-Entitäten
	 * @return eine Response mit den Löschergebnissen
	 */
	public Response delete(final List<Long> ids) {
		final var responses = service.delete(ids);
		return Responses.ok(responses);
	}


}
