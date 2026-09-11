package de.svws_nrw.controller.uv.zeitraster;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.zeitraster.UvZeitrasterEintragCreateRequest;
import de.svws_nrw.service.uv.zeitraster.UvZeitrasterEintragPatchRequest;
import de.svws_nrw.service.uv.zeitraster.UvZeitrasterEintragService;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des Controllers für die UV-Zeitraster-Einträge.
 */
public final class UvZeitrasterEintragControllerImpl implements UvZeitrasterEintragController {

	private final UvZeitrasterEintragService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvZeitrasterEintragControllerImpl(final UvZeitrasterEintragService service) {
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
	public Response getByZeitrasterId(final long idZeitraster) {
		return Responses.ok(service.getListByZeitrasterId(idZeitraster));
	}

	@Override
	public Response create(final UvZeitrasterEintragCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response createMultiple(final Collection<UvZeitrasterEintragCreateRequest> createRequests) {
		return Responses.created(service.createMultiple(createRequests));
	}

	@Override
	public Response patch(final UvZeitrasterEintragPatchRequest patch) {
		return Responses.ok(service.patch(patch));
	}

	@Override
	public Response patchMultiple(final Collection<UvZeitrasterEintragPatchRequest> patches) {
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
