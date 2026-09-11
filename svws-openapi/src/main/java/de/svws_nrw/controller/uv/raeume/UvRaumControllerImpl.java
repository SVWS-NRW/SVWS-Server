package de.svws_nrw.controller.uv.raeume;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.raeume.UvRaumCreateRequest;
import de.svws_nrw.service.uv.raeume.UvRaumPatchRequest;
import de.svws_nrw.service.uv.raeume.UvRaumService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die UV-Räume gebündelt.
 */
public final class UvRaumControllerImpl implements UvRaumController {

	private final UvRaumService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 * @param service   der zugehörige Service
	 */
	public UvRaumControllerImpl(final UvRaumService service) {
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
	public Response create(final UvRaumCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response createMultiple(final Collection<UvRaumCreateRequest> createRequests) {
		return Responses.created(service.createMultiple(createRequests));
	}

	@Override
	public Response patch(final UvRaumPatchRequest patch) {
		return Responses.ok(service.patch(patch));
	}

	@Override
	public Response patchMultiple(final Collection<UvRaumPatchRequest> patches) {
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
