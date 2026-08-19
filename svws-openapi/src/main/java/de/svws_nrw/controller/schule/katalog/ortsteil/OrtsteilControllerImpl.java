package de.svws_nrw.controller.schule.katalog.ortsteil;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schule.katalog.ortsteil.OrtsteilCreateRequest;
import de.svws_nrw.service.schule.katalog.ortsteil.OrtsteilPatchRequest;
import de.svws_nrw.service.schule.katalog.ortsteil.OrtsteilService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

public final class OrtsteilControllerImpl implements OrtsteilController {

	private final OrtsteilService ortsteilService;

	/**
	 * Initialisiert einen neuen Controller.
	 *
	 * @param ortsteilService {@link OrtsteilService}
	 */
	public OrtsteilControllerImpl(final OrtsteilService ortsteilService) {
		this.ortsteilService = ortsteilService;
	}

	@Override
	public Response getAll() {
		final List<OrtsteilKatalogEintrag> daten = ortsteilService.getAll();

		return Responses.ok(daten);
	}

	@Override
	public Response create(final OrtsteilCreateRequest request) {
		BeanValidator.validate(request);

		final OrtsteilKatalogEintrag created = ortsteilService.create(request);

		return Responses.created(created);
	}

	@Override
	public Response patch(final long id, final OrtsteilPatchRequest patch) {
		BeanValidator.validate(patch);

		final OrtsteilKatalogEintrag updated = ortsteilService.patch(id, patch);

		return Responses.ok(updated);
	}

	@Override
	public Response delete(final List<Long> ids) {
		final List<SimpleOperationResponse> deleted = ortsteilService.delete(ids);

		return Responses.ok(deleted);
	}

}
