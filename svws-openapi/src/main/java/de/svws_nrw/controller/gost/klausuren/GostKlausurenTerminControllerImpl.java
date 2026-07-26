package de.svws_nrw.controller.gost.klausuren;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.gost.klausuren.GostKlausurenTerminCreateRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenTerminService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden API-Zugriffe für GOSt-Klausurtermine gebündelt.
 */
public final class GostKlausurenTerminControllerImpl implements GostKlausurenTerminController {

	private final GostKlausurenTerminService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service der zugehörige Service
	 */
	public GostKlausurenTerminControllerImpl(final GostKlausurenTerminService service) {
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
	public Response create(final GostKlausurenTerminCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
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
