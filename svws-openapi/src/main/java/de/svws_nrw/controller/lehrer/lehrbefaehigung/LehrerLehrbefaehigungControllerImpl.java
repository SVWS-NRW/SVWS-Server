package de.svws_nrw.controller.lehrer.lehrbefaehigung;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.lehrer.lehrbefaehigung.LehrerLehrbefaehigungCreateRequest;
import de.svws_nrw.service.lehrer.lehrbefaehigung.LehrerLehrbefaehigungPatchRequest;
import de.svws_nrw.service.lehrer.lehrbefaehigung.LehrerLehrbefaehigungService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

public final class LehrerLehrbefaehigungControllerImpl implements LehrerLehrbefaehigungController {

	private final LehrerLehrbefaehigungService service;

	/**
	 * @param service {@link LehrerLehrbefaehigungService}
	 */
	public LehrerLehrbefaehigungControllerImpl(final LehrerLehrbefaehigungService service) {
		this.service = service;
	}

	@Override
	public Response getAll() {
		return Responses.ok(service.getAll());
	}

	@Override
	public Response getByIdLehramt(final Long idLehramt) {
		return Responses.ok(service.getByIdLehramt(idLehramt));
	}

	@Override
	public Response create(final LehrerLehrbefaehigungCreateRequest dto) {
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
	public Response patch(final long id, final LehrerLehrbefaehigungPatchRequest dto) {
		BeanValidator.validate(dto);
		final var patched = this.service.patch(id, dto);
		return Responses.ok(patched);
	}

}
