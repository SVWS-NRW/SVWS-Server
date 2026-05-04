package de.svws_nrw.controller.schueler.schulbesuch;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerMerkmalCreateRequest;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerMerkmalPatchRequest;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerMerkmalService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;

public class SchuelerMerkmalController {

	private final SchuelerMerkmalService service;

	/**
	 * Erstellt einen neuen SchuelerMerkmalController mit dem angegebenen Service.
	 *
	 * @param service der SchuelerMerkmalService
	 */
	public SchuelerMerkmalController(final SchuelerMerkmalService service) {
		this.service = service;
	}

	/**
	 * Erstellt eine neue SchülerMerkmal-Entität.
	 * <p>
	 * Das Request-DTO wird vor der Verarbeitung validiert.
	 * </p>
	 *
	 * @param dto das Request-Objekt mit den Daten für das neue Merkmal
	 * @return eine Response mit der erstellten SchülerMerkmal-Entität
	 * @throws ApiOperationException wenn die Validierung fehlschlägt
	 */
	public Response create(final SchuelerMerkmalCreateRequest dto) {
		BeanValidator.validate(dto);
		final var created = this.service.create(dto);
		return Responses.created(created);
	}

	/**
	 * Aktualisiert eine bestehende SchülerMerkmal-Entität teilweise.
	 * <p>
	 * Das Request-DTO wird vor der Verarbeitung validiert.
	 * </p>
	 *
	 * @param id die ID der zu aktualisierenden SchülerMerkmal-Entität
	 * @param dto das Request-Objekt mit den zu aktualisierenden Feldern
	 * @return eine Response mit der aktualisierten SchülerMerkmal-Entität
	 * @throws ValidationException wenn die DTO-Validierung fehlschlägt
	 */
	public Response patch(final long id, final SchuelerMerkmalPatchRequest dto) {
		BeanValidator.validate(dto);
		final var patched = this.service.patch(id, dto);
		return Responses.ok(patched);
	}

	/**
	 * Löscht mehrere SchülerMerkmal-Einträge anhand ihrer IDs.
	 *
	 * @param ids die Liste der IDs der zu löschenden SchülerMerkmal-Einträge
	 * @return eine Response mit den Löschergebnissen
	 */
	public Response delete(final List<Long> ids) {
		final var responses = this.service.delete(ids);
		return Responses.ok(responses);
	}
}
