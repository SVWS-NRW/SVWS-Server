package de.svws_nrw.controller.schueler.liste;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schueler.liste.SchuelerListeEintragService;
import jakarta.ws.rs.core.Response;

/**
 * Die Implementierung des {@link SchuelerListeEintragController}, welche ohne Fachlogik an den
 * {@link SchuelerListeEintragService} delegiert.
 */
public final class SchuelerListeEintragControllerImpl implements SchuelerListeEintragController {

	private final SchuelerListeEintragService service;

	/**
	 * Erstellt einen neuen Controller.
	 *
	 * @param service   der {@link SchuelerListeEintragService}
	 */
	public SchuelerListeEintragControllerImpl(final SchuelerListeEintragService service) {
		this.service = service;
	}

	@Override
	public Response getListeAktuell(final boolean nurAktive) {
		return Responses.ok(service.getListeAktuell(nurAktive));
	}

	@Override
	public Response getListe(final long idSchuljahresabschnitt, final boolean nurAktive) {
		return Responses.ok(service.getListe(idSchuljahresabschnitt, nurAktive));
	}

}
