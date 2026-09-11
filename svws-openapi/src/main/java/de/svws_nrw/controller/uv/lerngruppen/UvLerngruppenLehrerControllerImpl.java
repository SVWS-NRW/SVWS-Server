package de.svws_nrw.controller.uv.lerngruppen;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.core.data.uv.UvLerngruppenLehrerCreateRequest;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppenLehrerPatchRequest;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppenLehrerService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die UV-Lerngruppen-Lehrer-Zuordnungen gebündelt.
 */
public final class UvLerngruppenLehrerControllerImpl implements UvLerngruppenLehrerController {

	/** Der zugehörige Service */
	private final UvLerngruppenLehrerService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvLerngruppenLehrerControllerImpl(final UvLerngruppenLehrerService service) {
		this.service = service;
	}

	@Override
	public Response create(final UvLerngruppenLehrerCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response createMultiple(final Collection<UvLerngruppenLehrerCreateRequest> createRequests) {
		return Responses.created(service.createMultiple(createRequests));
	}

	@Override
	public Response patch(final UvLerngruppenLehrerPatchRequest patch) {
		return Responses.ok(service.patch(patch));
	}

	@Override
	public Response patchMultiple(final Collection<UvLerngruppenLehrerPatchRequest> patches) {
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
