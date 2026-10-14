package de.svws_nrw.controller.lehrer.personaldaten;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.lehrer.personaldaten.LehrerPersonaldatenBatchPatchRequest;
import de.svws_nrw.service.lehrer.personaldaten.LehrerPersonaldatenPatchRequest;
import de.svws_nrw.service.lehrer.personaldaten.LehrerPersonaldatenService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/** LehrerPersonaldatenControllerImpl */
public final class LehrerPersonaldatenControllerImpl implements LehrerPersonaldatenController {

	private final LehrerPersonaldatenService service;

	/**
	 * contructor
	 *
	 * @param service LehrerPersonaldatenService
	 */
	public LehrerPersonaldatenControllerImpl(final LehrerPersonaldatenService service) {
		this.service = service;
	}

	@Override
	public Response get(final long id) {
		return Responses.ok(service.getById(id));
	}

	@Override
	public Response patch(final long id, final LehrerPersonaldatenPatchRequest dto) {
		BeanValidator.validate(dto);
		return Responses.ok(service.patch(id, dto));
	}

	@Override
	public Response patchMultiple(final List<LehrerPersonaldatenBatchPatchRequest> dtos) {
		dtos.forEach(BeanValidator::validate);
		return Responses.ok(service.patchMultiple(dtos));
	}
}
