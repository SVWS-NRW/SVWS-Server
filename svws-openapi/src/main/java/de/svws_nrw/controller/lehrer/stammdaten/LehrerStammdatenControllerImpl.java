package de.svws_nrw.controller.lehrer.stammdaten;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.lehrer.stammdaten.LehrerStammdatenCreateRequest;
import de.svws_nrw.service.lehrer.stammdaten.LehrerStammdatenPatchRequest;
import de.svws_nrw.service.lehrer.stammdaten.LehrerStammdatenService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Die Implementierung des {@link LehrerStammdatenController}. Sie enthält keine Fachlogik, sondern validiert die
 * Requests und delegiert an den {@link LehrerStammdatenService}.
 */
public final class LehrerStammdatenControllerImpl implements LehrerStammdatenController {

	private final LehrerStammdatenService service;

	/**
	 * Erstellt einen neuen Controller.
	 *
	 * @param service   der Service für die Lehrer-Stammdaten
	 */
	public LehrerStammdatenControllerImpl(final LehrerStammdatenService service) {
		this.service = service;
	}

	@Override
	public Response get(final long id) {
		return Responses.ok(service.getById(id));
	}

	@Override
	public Response getList(final List<Long> ids) {
		return Responses.ok(service.getList(ids));
	}

	@Override
	public Response create(final LehrerStammdatenCreateRequest request) {
		BeanValidator.validate(request);

		return Responses.created(service.create(request));
	}

	@Override
	public Response patch(final long id, final LehrerStammdatenPatchRequest request) {
		BeanValidator.validate(request);

		return Responses.ok(service.patch(id, request));
	}

}
