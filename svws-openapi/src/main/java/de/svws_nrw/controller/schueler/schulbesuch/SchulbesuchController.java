package de.svws_nrw.controller.schueler.schulbesuch;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schueler.schulbesuch.SchulbesuchPatchRequest;
import de.svws_nrw.service.schueler.schulbesuch.SchulbesuchService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;

public final class SchulbesuchController {

	private final SchulbesuchService schulbesuchService;

	/**
	 * Constructor
	 *
	 * @param schulbesuchService schulbesuchService
	 */
	public SchulbesuchController(final SchulbesuchService schulbesuchService) {
		this.schulbesuchService = schulbesuchService;
	}

	/**
	 * Ruft alle Schulbesuch-Entitäten für den Schüler mit der gegebenen ID ab.
	 *
	 * @param idSchueler idSchueler
	 * @return eine Response mit der Liste aller Schulbesuch-Entitäten
	 */
	public Response getByIdSchueler(final long idSchueler) {
		return Response.ok(schulbesuchService.getById(idSchueler)).build();
	}

	/**
	 * Aktualisiert eine bestehende SchulbesuchPatchRequest-Entität teilweise.
	 * <p>
	 * Das Request-DTO wird vor der Verarbeitung validiert.
	 * </p>
	 *
	 * @param id die ID der zu aktualisierenden Schulbesuch-Entität
	 * @param dto das Request-Objekt mit den zu aktualisierenden Feldern
	 * @return eine Response mit der aktualisierten Schulbesuch-Entität
	 * @throws ValidationException wenn die DTO-Validierung fehlschlägt
	 */
	public Response patch(final long id, final SchulbesuchPatchRequest dto) {
		BeanValidator.validate(dto);
		final var patched = schulbesuchService.patch(id, dto);
		return Responses.ok(patched);
	}

	/**
	 * Getter
	 * @return SchulbesuchService
	 */
	public SchulbesuchService getSchulbesuchService() {
		return schulbesuchService;
	}
}
