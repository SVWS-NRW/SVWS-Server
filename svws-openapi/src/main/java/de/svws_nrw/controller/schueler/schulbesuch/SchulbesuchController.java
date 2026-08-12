package de.svws_nrw.controller.schueler.schulbesuch;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerSchulbesuchPatchRequest;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerSchulbesuchService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;

public final class SchulbesuchController {

	private final SchuelerSchulbesuchService schuelerSchulbesuchService;

	/**
	 * Constructor
	 *
	 * @param schuelerSchulbesuchService schulbesuchService
	 */
	public SchulbesuchController(final SchuelerSchulbesuchService schuelerSchulbesuchService) {
		this.schuelerSchulbesuchService = schuelerSchulbesuchService;
	}

	/**
	 * Ruft alle Schulbesuch-Entitäten für den Schüler mit der gegebenen ID ab.
	 *
	 * @param idSchueler idSchueler
	 * @return eine Response mit der Liste aller Schulbesuch-Entitäten
	 */
	public Response getByIdSchueler(final long idSchueler) {
		return Response.ok(schuelerSchulbesuchService.getById(idSchueler)).build();
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
	public Response patch(final long id, final SchuelerSchulbesuchPatchRequest dto) {
		BeanValidator.validate(dto);
		final var patched = schuelerSchulbesuchService.patch(id, dto);
		return Responses.ok(patched);
	}

}
