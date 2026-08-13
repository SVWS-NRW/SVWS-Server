package de.svws_nrw.controller.schueler.schulbesuch;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerBisherigeSchuleCreateRequest;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerBisherigeSchulePatchRequest;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerBisherigeSchuleService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

public final class SchuelerBisherigeSchuleControllerImpl implements SchuelerBisherigeSchuleController {

	private final SchuelerBisherigeSchuleService service;

	/**
	 * Erstellt einen neuen BisherigeSchulenController mit dem angegebenen Service.
	 *
	 * @param service der BisherigeSchulenService
	 */
	public SchuelerBisherigeSchuleControllerImpl(final SchuelerBisherigeSchuleService service) {
		this.service = service;
	}

	@Override
	public Response create(final SchuelerBisherigeSchuleCreateRequest dto) {
		BeanValidator.validate(dto);
		final var created = this.service.create(dto);
		return Responses.created(created);
	}

	@Override
	public Response patch(final long id, final SchuelerBisherigeSchulePatchRequest dto) {
		BeanValidator.validate(dto);
		final var patched = this.service.patch(id, dto);
		return Responses.ok(patched);
	}

	@Override
	public Response delete(final List<Long> ids) {
		final var responses = this.service.delete(ids);
		return Responses.ok(responses);
	}

}
