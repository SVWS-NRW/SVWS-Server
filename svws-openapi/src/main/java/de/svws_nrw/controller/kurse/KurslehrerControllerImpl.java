package de.svws_nrw.controller.kurse;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.kurse.KurslehrerCreateRequest;
import de.svws_nrw.service.kurse.KurslehrerPatchRequest;
import de.svws_nrw.service.kurse.KurslehrerService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Die Implementierung des {@link KurslehrerController}, welche ohne Fachlogik an den {@link KurslehrerService} delegiert.
 */
public final class KurslehrerControllerImpl implements KurslehrerController {

	private final KurslehrerService service;

	/**
	 * Erstellt einen neuen Controller.
	 *
	 * @param service   der {@link KurslehrerService}
	 */
	public KurslehrerControllerImpl(final KurslehrerService service) {
		this.service = service;
	}

	@Override
	public Response getListByIdKurs(final long idKurs) {
		return Responses.ok(service.getListByIdKurs(idKurs));
	}

	@Override
	public Response create(final long idKurs, final KurslehrerCreateRequest request) {
		BeanValidator.validate(request);
		return Responses.created(service.create(idKurs, request));
	}

	@Override
	public Response patch(final long idKurs, final long idLehrer, final KurslehrerPatchRequest request) {
		BeanValidator.validate(request);
		return Responses.ok(service.patch(idKurs, idLehrer, request));
	}

	@Override
	public Response delete(final long idKurs, final List<Long> idsLehrer) {
		return Responses.ok(service.delete(idKurs, idsLehrer));
	}

}
