package de.svws_nrw.controller.gost.klausuren;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.gost.klausuren.GostKlausurenKursklausurService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden API-Zugriffe für GOSt-Kursklausuren gebündelt.
 */
public final class GostKlausurenKursklausurControllerImpl implements GostKlausurenKursklausurController {

	private final GostKlausurenKursklausurService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service der zugehörige Service
	 */
	public GostKlausurenKursklausurControllerImpl(final GostKlausurenKursklausurService service) {
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
	public Response delete(final long id) {
		return Responses.ok(service.delete(id));
	}

	@Override
	public Response deleteMultiple(final Collection<Long> ids) {
		return Responses.ok(service.deleteMultiple(ids));
	}

}
