package de.svws_nrw.controller.schule.teilstandort;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Teilstandort;
import de.svws_nrw.data.Responses;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schule.teilstandort.TeilstandortCreateRequest;
import de.svws_nrw.service.schule.teilstandort.TeilstandortPatchRequest;
import de.svws_nrw.service.schule.teilstandort.TeilstandortService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public final class TeilstandortControllerImpl implements TeilstandortController {

	private final TeilstandortService service;

	/**
	 * Initialisiert einen neuen Controller
	 *
	 * @param teilstandortService {@link TeilstandortService}
	 */
	public TeilstandortControllerImpl(final TeilstandortService teilstandortService) {
		this.service = teilstandortService;
	}

	/**
	 *
	 * Ermittelt alle Teilstandorte
	 *
	 * @return die Response
	 */
	@Override
	public Response getAll() {
		final List<Teilstandort> daten = service.getAll();

		return Response.ok(daten).type(MediaType.APPLICATION_JSON).build();
	}

	@Override
	public Response create(final TeilstandortCreateRequest request) {
		if (request == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Der Request darf nicht null sein.");
		}
		BeanValidator.validate(request);

		final Teilstandort created = service.create(request);

		return Responses.created(created);
	}

	@Override
	public Response patch(final String id, final TeilstandortPatchRequest patch) {
		if (patch == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Der Patch darf nicht null sein.");
		}
		BeanValidator.validate(patch);

		final Teilstandort updated = service.patch(id, patch);

		return Responses.ok(updated);
	}

	@Override
	public Response delete(final List<String> ids) {
		final List<SimpleOperationResponse> deleted = service.deleteMultiple(ids);

		return Responses.ok(deleted);
	}

}
