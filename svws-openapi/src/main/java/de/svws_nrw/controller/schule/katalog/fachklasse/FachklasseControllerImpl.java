package de.svws_nrw.controller.schule.katalog.fachklasse;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schule.katalog.fachklasse.FachklasseEintragCreateRequest;
import de.svws_nrw.service.schule.katalog.fachklasse.FachklasseEintragPatchRequest;
import de.svws_nrw.service.schule.katalog.fachklasse.FachklasseService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

public final class FachklasseControllerImpl implements FachklasseController {

	private final FachklasseService service;

	/**
	 * @param service {@link FachklasseService}
	 */
	public FachklasseControllerImpl(final FachklasseService service) {
		this.service = service;
	}

	@Override
	public Response getAll() {
		return Responses.ok(service.getAll());
	}

	@Override
	public Response create(final FachklasseEintragCreateRequest dto) {
		BeanValidator.validate(dto);
		final var created = service.create(dto);
		return Responses.created(created);
	}

	@Override
	public Response delete(final List<Long> ids) {
		final var responses = service.delete(ids);
		return Responses.ok(responses);
	}

	@Override
	public Response patch(final long id, final FachklasseEintragPatchRequest dto) {
		BeanValidator.validate(dto);
		final var patched = this.service.patch(id, dto);
		return Responses.ok(patched);
	}

}
