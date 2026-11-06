package de.svws_nrw.controller.kurse;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.kurse.KursService;
import jakarta.ws.rs.core.Response;

/**
 * Die Implementierung des {@link KursController}, welche ohne Fachlogik an den {@link KursService} delegiert.
 */
public final class KursControllerImpl implements KursController {

	private final KursService service;

	/**
	 * Erstellt einen neuen Controller.
	 *
	 * @param service   der {@link KursService}
	 */
	public KursControllerImpl(final KursService service) {
		this.service = service;
	}

	@Override
	public Response get(final long id) {
		return Responses.ok(service.getById(id));
	}

	@Override
	public Response getListBySchuljahresabschnitt(final Long idSchuljahresabschnitt) {
		return Responses.ok(service.getListBySchuljahresabschnitt(idSchuljahresabschnitt, true));
	}

}
