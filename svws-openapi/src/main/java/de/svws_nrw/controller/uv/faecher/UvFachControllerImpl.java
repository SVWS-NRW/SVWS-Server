package de.svws_nrw.controller.uv.faecher;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.faecher.UvFachCreateRequest;
import de.svws_nrw.service.uv.faecher.UvFachPatchRequest;
import de.svws_nrw.service.uv.faecher.UvFachService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die UV-Fächer gebündelt.
 */
public final class UvFachControllerImpl implements UvFachController {

	/** Der zugehörige Service */
	private final UvFachService service;


	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvFachControllerImpl(final UvFachService service) {
		this.service = service;
	}

	@Override
	public Response get(final long id) {
		final var daten = service.get(id);
		return Responses.ok(daten);
	}

	@Override
	public Response getAll() {
		final var daten = service.getAll();
		return Responses.ok(daten);
	}

	@Override
	public Response create(final UvFachCreateRequest createRequest) {
		final var daten = service.create(createRequest);
		return Responses.created(daten);
	}

	@Override
	public Response createMultiple(final Collection<UvFachCreateRequest> createRequests) {
		final var daten = service.createMultiple(createRequests);
		return Responses.created(daten);
	}

	@Override
	public Response patch(final UvFachPatchRequest patch) {
		final var daten = service.patch(patch);
		return Responses.ok(daten);
	}

	@Override
	public Response patchMultiple(final Collection<UvFachPatchRequest> patches) {
		final var daten = service.patchMultiple(patches);
		return Responses.ok(daten);
	}

	@Override
	public Response delete(final long id) {
		final var daten = service.delete(id);
		return Responses.ok(daten);
	}

	@Override
	public Response deleteMultiple(final Collection<Long> ids) {
		final var daten = service.deleteMultiple(ids);
		return Responses.ok(daten);
	}

}
