package de.svws_nrw.controller.uv.schueler;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.core.data.uv.UvSchuelergruppeCreateRequest;
import de.svws_nrw.service.uv.schueler.UvSchuelergruppePatchRequest;
import de.svws_nrw.service.uv.schueler.UvSchuelergruppeService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die UV-Schülergruppen gebündelt.
 */
public final class UvSchuelergruppeControllerImpl implements UvSchuelergruppeController {

	/** Der zugehörige Service */
	private final UvSchuelergruppeService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvSchuelergruppeControllerImpl(final UvSchuelergruppeService service) {
		this.service = service;
	}

	@Override
	public Response create(final UvSchuelergruppeCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response createMultiple(final Collection<UvSchuelergruppeCreateRequest> createRequests) {
		return Responses.created(service.createMultiple(createRequests));
	}

	@Override
	public Response patch(final UvSchuelergruppePatchRequest patch) {
		return Responses.ok(service.patch(patch));
	}

	@Override
	public Response patchMultiple(final Collection<UvSchuelergruppePatchRequest> patches) {
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
