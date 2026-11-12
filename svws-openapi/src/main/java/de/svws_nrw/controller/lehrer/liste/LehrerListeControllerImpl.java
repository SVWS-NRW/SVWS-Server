package de.svws_nrw.controller.lehrer.liste;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.lehrer.liste.LehrerListeService;
import jakarta.ws.rs.core.Response;

/**
 * Die Implementierung des {@link LehrerListeController}, welche ohne Fachlogik an den
 * {@link LehrerListeService} delegiert.
 */
public final class LehrerListeControllerImpl implements LehrerListeController {

	private final LehrerListeService service;

	/**
	 * Erstellt einen neuen Controller.
	 *
	 * @param service   der {@link LehrerListeService}
	 */
	public LehrerListeControllerImpl(final LehrerListeService service) {
		this.service = service;
	}

	@Override
	public Response getList() {
		return Responses.ok(service.getList(true));
	}

	@Override
	public Response getListBySchuljahresabschnitt(final long idSchuljahresabschnitt) {
		return Responses.ok(service.getListBySchuljahresabschnitt(idSchuljahresabschnitt, true));
	}

	@Override
	public Response delete(final List<Long> idsLehrer) {
		return Responses.ok(service.delete(idsLehrer));
	}

}
