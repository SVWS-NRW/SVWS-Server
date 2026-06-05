package de.svws_nrw.controller.schule.schulleitung;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schule.schulleitung.SchulleitungCreateRequest;
import de.svws_nrw.service.schule.schulleitung.SchulleitungPatchRequest;
import de.svws_nrw.service.schule.schulleitung.SchulleitungService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;

public final class SchulleitungController {

	private final SchulleitungService service;

	/**
	 * Constructor
	 *
	 * @param service service
	 */
	public SchulleitungController(final SchulleitungService service) {
		this.service = service;
	}

	/**
	 * Gibt alle Schulleitungseinträge zurück.
	 *
	 * @return eine Response mit der Liste aller Einträge
	 */
	public Response getAll() {
		final var result = this.service.getAll();
		return Responses.ok(result);
	}

	/**
	 * Gibt alle Schulleitungseinträge für einen bestimmten Lehrer zurück.
	 *
	 * @param idLehrer die ID des Lehrers
	 * @return eine Response mit der Liste der Einträge
	 */
	public Response getAllByIdLehrer(final long idLehrer) {
		final var result = this.service.getAllByIdLehrer(idLehrer);
		return Responses.ok(result);
	}

	/**
	 * Erstellt einen neuen Schulleitungseintrag.
	 *
	 * @param dto das Request-Objekt mit den Daten für den neuen Eintrag
	 * @return eine Response mit dem erstellten Eintrag
	 * @throws ApiOperationException wenn die Validierung fehlschlägt
	 */
	public Response create(final SchulleitungCreateRequest dto) {
		BeanValidator.validate(dto);
		final var created = this.service.create(dto);
		return Responses.created(created);
	}

	/**
	 * Aktualisiert einen bestehenden Schulleitungseintrag teilweise (PATCH).
	 *
	 * @param id  die ID des zu aktualisierenden Eintrags
	 * @param dto das Request-Objekt mit den zu aktualisierenden Feldern
	 * @return eine Response mit dem aktualisierten Eintrag
	 * @throws ValidationException   wenn die DTO-Validierung fehlschlägt
	 */
	public Response patch(final long id, final SchulleitungPatchRequest dto) {
		BeanValidator.validate(dto);
		final var patched = this.service.patch(id, dto);
		return Responses.ok(patched);
	}

	/**
	 * Löscht mehrere Schulleitungseinträge anhand ihrer IDs.
	 *
	 * @param ids die Liste der IDs der zu löschenden Einträge
	 * @return eine Response mit den Löschergebnissen
	 */
	public Response delete(final List<Long> ids) {
		final var responses = this.service.delete(ids);
		return Responses.ok(responses);
	}

}
