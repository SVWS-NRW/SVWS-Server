package de.svws_nrw.controller.uv.klassen;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.klassen.UvKlasseCreateRequest;
import de.svws_nrw.service.uv.klassen.UvKlassePatchRequest;
import de.svws_nrw.service.uv.klassen.UvKlasseService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die UV-Klassen gebündelt.
 */
public final class UvKlasseControllerImpl implements UvKlasseController {

	/** Der zugehörige Service */
	private final UvKlasseService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvKlasseControllerImpl(final UvKlasseService service) {
		this.service = service;
	}

	@Override
	public Response create(final UvKlasseCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response patch(final UvKlassePatchRequest patch) {
		return Responses.ok(service.patch(patch));
	}

	@Override
	public Response patchMultiple(final Collection<UvKlassePatchRequest> patches) {
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
