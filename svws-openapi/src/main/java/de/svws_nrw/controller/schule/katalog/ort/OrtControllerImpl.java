package de.svws_nrw.controller.schule.katalog.ort;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schule.katalog.ort.OrtCreateRequest;
import de.svws_nrw.service.schule.katalog.ort.OrtPatchRequest;
import de.svws_nrw.service.schule.katalog.ort.OrtService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

public final class OrtControllerImpl implements OrtController {

	private final OrtService ortService;

	/**
	 * Initialisiert einen neuen Controller.
	 *
	 * @param ortService {@link OrtService}
	 */
	public OrtControllerImpl(final OrtService ortService) {
		this.ortService = ortService;
	}

	@Override
	public Response getAll() {
		final List<OrtKatalogEintrag> daten = ortService.getAll();

		return Responses.ok(daten);
	}

	@Override
	public Response create(final OrtCreateRequest request) {
		BeanValidator.validate(request);

		final OrtKatalogEintrag created = ortService.create(request);

		return Responses.created(created);
	}

	@Override
	public Response patch(final long id, final OrtPatchRequest patch) {
		BeanValidator.validate(patch);

		final OrtKatalogEintrag updated = ortService.patch(id, patch);

		return Responses.ok(updated);
	}

	@Override
	public Response delete(final List<Long> ids) {
		final List<SimpleOperationResponse> deleted = ortService.delete(ids);

		return Responses.ok(deleted);
	}

}
