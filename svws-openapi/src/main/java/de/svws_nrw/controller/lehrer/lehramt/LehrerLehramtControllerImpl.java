package de.svws_nrw.controller.lehrer.lehramt;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.lehrer.lehramt.LehrerLehramtCreateRequest;
import de.svws_nrw.service.lehrer.lehramt.LehrerLehramtPatchRequest;
import de.svws_nrw.service.lehrer.lehramt.LehrerLehramtService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/** LehrerLehramtControllerImpl */
public final class LehrerLehramtControllerImpl implements LehrerLehramtController {

	private final LehrerLehramtService service;

	/**
	 * @param service {@link LehrerLehramtService}
	 */
	public LehrerLehramtControllerImpl(final LehrerLehramtService service) {
		this.service = service;
	}

	@Override
	public Response create(final LehrerLehramtCreateRequest dto) {
		BeanValidator.validate(dto);
		final var created = service.create(dto);
		return Responses.created(created);
	}

	@Override
	public Response patch(final long id, final LehrerLehramtPatchRequest dto) {
		BeanValidator.validate(dto);
		final var patched = this.service.patch(id, dto);
		return Responses.ok(patched);
	}

	@Override
	public Response delete(final List<Long> ids) {
		final var responses = service.delete(ids);
		return Responses.ok(responses);
	}

}
