package de.svws_nrw.controller.uv.unterrichte;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.unterrichte.UvUnterrichtCreateRequest;
import de.svws_nrw.service.uv.unterrichte.UvUnterrichtPatchRequest;
import de.svws_nrw.service.uv.unterrichte.UvUnterrichtService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die UV-Unterrichte gebündelt.
 */
public final class UvUnterrichtControllerImpl implements UvUnterrichtController {

	/** Der zugehörige Service */
	private final UvUnterrichtService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvUnterrichtControllerImpl(final UvUnterrichtService service) {
		this.service = service;
	}

	@Override
	public Response create(final UvUnterrichtCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response patch(final UvUnterrichtPatchRequest patch) {
		return Responses.ok(service.patch(patch));
	}

	@Override
	public Response patchMultiple(final Collection<UvUnterrichtPatchRequest> patches) {
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
