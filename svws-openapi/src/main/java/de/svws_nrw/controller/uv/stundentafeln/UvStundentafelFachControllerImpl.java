package de.svws_nrw.controller.uv.stundentafeln;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelFachCreateRequest;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelFachPatchRequest;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelFachService;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des Controllers für die UV-Stundentafel-Fächer.
 */
public final class UvStundentafelFachControllerImpl implements UvStundentafelFachController {

	private final UvStundentafelFachService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvStundentafelFachControllerImpl(final UvStundentafelFachService service) {
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
	public Response create(final UvStundentafelFachCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response createMultiple(final Collection<UvStundentafelFachCreateRequest> createRequests) {
		return Responses.created(service.createMultiple(createRequests));
	}

	@Override
	public Response patch(final UvStundentafelFachPatchRequest patch) {
		return Responses.ok(service.patch(patch));
	}

	@Override
	public Response patchMultiple(final Collection<UvStundentafelFachPatchRequest> patches) {
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
