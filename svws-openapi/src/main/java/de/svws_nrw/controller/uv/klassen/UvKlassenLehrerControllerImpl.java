package de.svws_nrw.controller.uv.klassen;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.klassen.UvKlassenLehrerCreateRequest;
import de.svws_nrw.service.uv.klassen.UvKlassenLehrerPatchRequest;
import de.svws_nrw.service.uv.klassen.UvKlassenLehrerService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die UV-Klassen-Lehrer-Zuordnungen gebündelt.
 */
public final class UvKlassenLehrerControllerImpl implements UvKlassenLehrerController {

	/** Der zugehörige Service */
	private final UvKlassenLehrerService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvKlassenLehrerControllerImpl(final UvKlassenLehrerService service) {
		this.service = service;
	}

	@Override
	public Response create(final UvKlassenLehrerCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response patch(final UvKlassenLehrerPatchRequest patch) {
		return Responses.ok(service.patch(patch));
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
