package de.svws_nrw.controller.schueler.schulbesuch;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerSchulbesuchPatchRequest;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerSchulbesuchService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

public final class SchuelerSchulbesuchControllerImpl implements SchuelerSchulbesuchController {

	private final SchuelerSchulbesuchService schuelerSchulbesuchService;

	/**
	 * Constructor
	 *
	 * @param schuelerSchulbesuchService schulbesuchService
	 */
	public SchuelerSchulbesuchControllerImpl(final SchuelerSchulbesuchService schuelerSchulbesuchService) {
		this.schuelerSchulbesuchService = schuelerSchulbesuchService;
	}

	@Override
	public Response getByIdSchueler(final long idSchueler) {
		return Response.ok(schuelerSchulbesuchService.getById(idSchueler)).build();
	}

	@Override
	public Response patch(final long id, final SchuelerSchulbesuchPatchRequest dto) {
		BeanValidator.validate(dto);
		final var patched = schuelerSchulbesuchService.patch(id, dto);
		return Responses.ok(patched);
	}

}
