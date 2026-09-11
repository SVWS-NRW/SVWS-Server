package de.svws_nrw.controller.uv.schienen;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.core.data.uv.UvSchieneCreateRequest;
import de.svws_nrw.service.uv.schienen.UvSchienePatchRequest;
import de.svws_nrw.service.uv.schienen.UvSchieneService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die UV-Schienen gebündelt.
 */
public final class UvSchieneControllerImpl implements UvSchieneController {

	/** Der zugehörige Service */
	private final UvSchieneService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvSchieneControllerImpl(final UvSchieneService service) {
		this.service = service;
	}

	@Override
	public Response create(final UvSchieneCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response patch(final UvSchienePatchRequest patch) {
		return Responses.ok(service.patch(patch));
	}

	@Override
	public Response patchMultiple(final Collection<UvSchienePatchRequest> patches) {
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
