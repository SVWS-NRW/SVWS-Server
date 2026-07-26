package de.svws_nrw.controller.gost.klausuren;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurPatchRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden API-Zugriffe für GOSt-Schülerklausuren gebündelt.
 */
public final class GostKlausurenSchuelerklausurControllerImpl implements GostKlausurenSchuelerklausurController {

	private final GostKlausurenSchuelerklausurService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service der zugehörige Service
	 */
	public GostKlausurenSchuelerklausurControllerImpl(final GostKlausurenSchuelerklausurService service) {
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
	public Response patch(final GostKlausurenSchuelerklausurPatchRequest patchRequest) {
		return Responses.ok(service.patch(patchRequest));
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
