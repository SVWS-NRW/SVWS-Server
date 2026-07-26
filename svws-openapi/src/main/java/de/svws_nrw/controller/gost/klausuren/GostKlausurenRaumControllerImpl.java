package de.svws_nrw.controller.gost.klausuren;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.gost.klausuren.GostKlausurenRaumCreateRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenRaumPatchRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenRaumService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden API-Zugriffe für GOSt-Klausurräume gebündelt.
 */
public final class GostKlausurenRaumControllerImpl implements GostKlausurenRaumController {

	private final GostKlausurenRaumService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service der zugehörige Service
	 */
	public GostKlausurenRaumControllerImpl(final GostKlausurenRaumService service) {
		this.service = service;
	}

	@Override
	public Response get(final long id) {
		return Responses.ok(service.get(id));
	}

	@Override
	public Response getListByIds(final Collection<Long> ids) {
		return Responses.ok(service.getListByIds(ids));
	}

	@Override
	public Response create(final GostKlausurenRaumCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response patch(final GostKlausurenRaumPatchRequest patchRequest) {
		return Responses.ok(service.patch(patchRequest));
	}

	@Override
	public Response delete(final long id) {
		return Responses.ok(service.delete(id));
	}

}
