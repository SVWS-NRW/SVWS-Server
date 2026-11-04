package de.svws_nrw.controller.klassen;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.klassen.KlassenService;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des {@link KlassenController}. Enthält keine Fachlogik und delegiert ausschließlich an den {@link KlassenService}.
 */
public final class KlassenControllerImpl implements KlassenController {

	private final KlassenService service;

	/**
	 * Erstellt einen neuen Controller.
	 *
	 * @param service   der Service für den lesenden Zugriff auf Klassen
	 */
	public KlassenControllerImpl(final KlassenService service) {
		this.service = service;
	}

	@Override
	public Response get(final long id) {
		return Responses.ok(service.getById(id));
	}

	@Override
	public Response getListBySchuljahresabschnitt(final long idSchuljahresabschnitt) {
		return Responses.ok(service.getListBySchuljahresabschnitt(idSchuljahresabschnitt, true));
	}

	@Override
	public Response getList(final List<Long> ids) {
		return Responses.ok(service.getListByIdsMitSchueler(ids));
	}

}
