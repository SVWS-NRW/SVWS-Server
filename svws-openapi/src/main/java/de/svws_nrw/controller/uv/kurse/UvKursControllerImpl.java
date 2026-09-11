package de.svws_nrw.controller.uv.kurse;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.core.data.uv.UvKursCreateRequest;
import de.svws_nrw.service.uv.kurse.UvKursPatchRequest;
import de.svws_nrw.service.uv.kurse.UvKursService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die UV-Kurse gebündelt.
 */
public final class UvKursControllerImpl implements UvKursController {

	/** Der zugehörige Service */
	private final UvKursService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvKursControllerImpl(final UvKursService service) {
		this.service = service;
	}

	@Override
	public Response create(final UvKursCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response patch(final UvKursPatchRequest patch) {
		return Responses.ok(service.patch(patch));
	}

	@Override
	public Response patchMultiple(final Collection<UvKursPatchRequest> patches) {
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
