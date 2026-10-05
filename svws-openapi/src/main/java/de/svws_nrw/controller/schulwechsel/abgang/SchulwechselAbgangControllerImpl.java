package de.svws_nrw.controller.schulwechsel.abgang;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schulwechsel.abgang.SchulwechselAbgangCreateRequest;
import de.svws_nrw.service.schulwechsel.abgang.SchulwechselAbgangPatchRequest;
import de.svws_nrw.service.schulwechsel.abgang.SchulwechselAbgangService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Controller für CRUD-Operationen auf {@code SchulwechselAbgang}-Einträgen.
 */
public final class SchulwechselAbgangControllerImpl implements SchulwechselAbgangController {

	private final SchulwechselAbgangService service;

	/**
	 * Erstellt einen neuen {@code SchulwechselAbgangControllerImpl}.
	 *
	 * @param service der zugehörige Service
	 */
	public SchulwechselAbgangControllerImpl(final SchulwechselAbgangService service) {
		this.service = service;
	}

	/**
	 * Erstellt einen neuen Wechselvorgang (Abgang).
	 *
	 * @param dto die Eingabedaten
	 * @return Response mit dem erstellten Wechselvorgang (Abgang)
	 */
	@Override
	public Response create(final SchulwechselAbgangCreateRequest dto) {
		BeanValidator.validate(dto);
		return Responses.created(this.service.create(dto));
	}

	/**
	 * Gibt einen Wechselvorgang (Abgang) anhand seiner ID zurück.
	 *
	 * @param id die ID des Abgangs
	 * @return Response mit dem gefundenen Wechselvorgang (Abgang)
	 */
	@Override
	public Response getById(final long id) {
		return Responses.ok(this.service.getById(id));
	}

	/**
	 * Gibt alle Wechselvorgänge (Abgang) zurück.
	 *
	 * @return Response mit der Liste aller Wechselvorgänge (Abgang)
	 */
	@Override
	public Response getAll() {
		return Responses.ok(this.service.getAll());
	}

	/**
	 * Aktualisiert einen bestehenden Wechselvorgang (Abgang) teilweise.
	 *
	 * @param id  die ID des Wechselvorgangs (Abgang)
	 * @param dto die zu aktualisierenden Felder
	 * @return Response mit dem aktualisierten Wechselvorgang (Abgang)
	 */
	@Override
	public Response patch(final long id, final SchulwechselAbgangPatchRequest dto) {
		BeanValidator.validate(dto);
		return Responses.ok(this.service.patch(id, dto));
	}

	/**
	 * Löscht mehrere Wechselvorgänge (Abgang) anhand ihrer IDs.
	 *
	 * @param ids die zu löschenden IDs
	 * @return Response mit den Löschergebnissen
	 */
	@Override
	public Response delete(final List<Long> ids) {
		return Responses.ok(this.service.delete(ids));
	}
}
