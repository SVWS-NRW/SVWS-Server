package de.svws_nrw.controller.schueler.schulbesuch;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerBisherigeSchuleCreateRequest;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerBisherigeSchulePatchRequest;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerBisherigeSchuleService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;

public final class BisherigeSchuleController {

	private final SchuelerBisherigeSchuleService service;

	/**
	 * Erstellt einen neuen BisherigeSchulenController mit dem angegebenen Service.
	 *
	 * @param service der BisherigeSchulenService
	 */
	public BisherigeSchuleController(final SchuelerBisherigeSchuleService service) {
		this.service = service;
	}

	/**
	 * Erstellt eine neue BisherigeSchule-Entität.
	 * <p>
	 * Das Request-DTO wird vor der Verarbeitung validiert.
	 * </p>
	 *
	 * @param dto das Request-Objekt mit den Daten für das neue Merkmal
	 * @return eine Response mit der erstellten BisherigeSchule-Entität
	 * @throws ApiOperationException wenn die Validierung fehlschlägt
	 */
	public Response create(final SchuelerBisherigeSchuleCreateRequest dto) {
		BeanValidator.validate(dto);
		final var created = this.service.create(dto);
		return Responses.created(created);
	}

	/**
	 * Aktualisiert eine bestehende BisherigeSchule-Entität teilweise.
	 * <p>
	 * Das Request-DTO wird vor der Verarbeitung validiert.
	 * </p>
	 *
	 * @param id die ID der zu aktualisierenden BisherigeSchule-Entität
	 * @param dto das Request-Objekt mit den zu aktualisierenden Feldern
	 * @return eine Response mit der aktualisierten BisherigeSchule-Entität
	 * @throws ValidationException wenn die DTO-Validierung fehlschlägt
	 */
	public Response patch(final long id, final SchuelerBisherigeSchulePatchRequest dto) {
		BeanValidator.validate(dto);
		final var patched = this.service.patch(id, dto);
		return Responses.ok(patched);
	}

	/**
	 * Löscht mehrere Einträge bisheriger Schulen anhand ihrer IDs.
	 *
	 * @param ids die Liste der IDs der zu löschenden Einträge bisheriger Schulen
	 * @return eine Response mit den Löschergebnissen
	 */
	public Response delete(final List<Long> ids) {
		final var responses = this.service.delete(ids);
		return Responses.ok(responses);
	}

}
