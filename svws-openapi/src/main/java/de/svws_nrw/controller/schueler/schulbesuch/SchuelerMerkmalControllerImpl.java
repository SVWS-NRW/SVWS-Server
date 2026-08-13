package de.svws_nrw.controller.schueler.schulbesuch;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerMerkmalCreateRequest;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerMerkmalPatchRequest;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerMerkmalService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

public final class SchuelerMerkmalControllerImpl implements SchuelerMerkmalController {

	private final SchuelerMerkmalService service;

	/**
	 * Erstellt einen neuen SchuelerMerkmalController mit dem angegebenen Service.
	 *
	 * @param service der SchuelerMerkmalService
	 */
	public SchuelerMerkmalControllerImpl(final SchuelerMerkmalService service) {
		this.service = service;
	}

	@Override
	public Response create(final SchuelerMerkmalCreateRequest dto) {
		BeanValidator.validate(dto);
		final var created = this.service.create(dto);
		return Responses.created(created);
	}

	@Override
	public Response patch(final long id, final SchuelerMerkmalPatchRequest dto) {
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
