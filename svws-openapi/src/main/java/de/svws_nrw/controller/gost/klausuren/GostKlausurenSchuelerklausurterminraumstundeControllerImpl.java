package de.svws_nrw.controller.gost.klausuren;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurterminraumstundeService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden API-Zugriffe für GOSt-Schülerklausurtermin-Raumstunden-Zuordnungen gebündelt.
 */
public final class GostKlausurenSchuelerklausurterminraumstundeControllerImpl
		implements GostKlausurenSchuelerklausurterminraumstundeController {

	private final GostKlausurenSchuelerklausurterminraumstundeService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service der zugehörige Service
	 */
	public GostKlausurenSchuelerklausurterminraumstundeControllerImpl(final GostKlausurenSchuelerklausurterminraumstundeService service) {
		this.service = service;
	}

	@Override
	public Response get(final long idSchuelerklausurtermin, final long idRaumstunde) {
		return Responses.ok(service.get(idSchuelerklausurtermin, idRaumstunde));
	}

	@Override
	public Response getListBySchuelerklausurterminIds(final Collection<Long> ids) {
		return Responses.ok(service.getListBySchuelerklausurterminIds(ids));
	}

	@Override
	public Response delete(final long idSchuelerklausurtermin, final long idRaumstunde) {
		return Responses.ok(service.delete(idSchuelerklausurtermin, idRaumstunde));
	}

}
