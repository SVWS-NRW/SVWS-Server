package de.svws_nrw.controller.uv.lerngruppen;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.core.data.uv.UvLerngruppeCreateRequest;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppePatchRequest;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppeService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die UV-Lerngruppen gebündelt.
 */
public final class UvLerngruppeControllerImpl implements UvLerngruppeController {

	/** Der zugehörige Service */
	private final UvLerngruppeService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvLerngruppeControllerImpl(final UvLerngruppeService service) {
		this.service = service;
	}

	@Override
	public Response create(final UvLerngruppeCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response patch(final UvLerngruppePatchRequest patch) {
		return Responses.ok(service.patch(patch));
	}

	@Override
	public Response patchMultiple(final Collection<UvLerngruppePatchRequest> patches) {
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
