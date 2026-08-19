package de.svws_nrw.controller.lehrer.fachrichtung;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.lehrer.fachrichtung.LehrerFachrichtungCreateRequest;
import de.svws_nrw.service.lehrer.fachrichtung.LehrerFachrichtungPatchRequest;
import de.svws_nrw.service.lehrer.fachrichtung.LehrerFachrichtungService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

public final class LehrerFachrichtungControllerImpl implements LehrerFachrichtungController {

	private final LehrerFachrichtungService service;

	/**
	 * @param service {@link LehrerFachrichtungService}
	 */
	public LehrerFachrichtungControllerImpl(final LehrerFachrichtungService service) {
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
	public Response create(final LehrerFachrichtungCreateRequest dto) {
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
	public Response patch(final long id, final LehrerFachrichtungPatchRequest dto) {
		BeanValidator.validate(dto);
		final var patched = this.service.patch(id, dto);
		return Responses.ok(patched);
	}

}
