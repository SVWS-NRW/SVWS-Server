package de.svws_nrw.controller.uv.zeitraster;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.zeitraster.UvZeitrasterCreateRequest;
import de.svws_nrw.service.uv.zeitraster.UvZeitrasterPatchRequest;
import de.svws_nrw.service.uv.zeitraster.UvZeitrasterService;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des Controllers für die UV-Zeitraster.
 */
public final class UvZeitrasterControllerImpl implements UvZeitrasterController {

	private final UvZeitrasterService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvZeitrasterControllerImpl(final UvZeitrasterService service) {
		this.service = service;
	}

	@Override
	public Response get(final long id) {
		return Responses.ok(service.get(id));
	}

	@Override
	public Response getAll() {
		return Responses.ok(service.getAll());
	}

	@Override
	public Response create(final UvZeitrasterCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response createMultiple(final Collection<UvZeitrasterCreateRequest> createRequests) {
		return Responses.created(service.createMultiple(createRequests));
	}

	@Override
	public Response patch(final UvZeitrasterPatchRequest patch) {
		return Responses.ok(service.patch(patch));
	}

	@Override
	public Response patchMultiple(final Collection<UvZeitrasterPatchRequest> patches) {
		return Responses.ok(service.patchMultiple(patches));
	}

	@Override
	public Response delete(final long id) {
		return Responses.ok(service.delete(id));
	}

	@Override
	public Response deleteMultiple(final Collection<Long> ids) {
		return Responses.ok(service.deleteMultiple(ids));
	}

}
