package de.svws_nrw.controller.schueler.liste;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schueler.liste.SchuelerListeService;
import jakarta.ws.rs.core.Response;

/**
 * Die Implementierung des {@link SchuelerListeController}, welche ohne Fachlogik an den
 * {@link SchuelerListeService} delegiert.
 */
public final class SchuelerListeControllerImpl implements SchuelerListeController {

	private final SchuelerListeService service;

	/**
	 * Erstellt einen neuen Controller.
	 *
	 * @param service   der {@link SchuelerListeService}
	 */
	public SchuelerListeControllerImpl(final SchuelerListeService service) {
		this.service = service;
	}

	@Override
	public Response getBySchuljahresabschnitt(final long idSchuljahresabschnitt) {
		return Responses.ok(service.getBySchuljahresabschnitt(idSchuljahresabschnitt));
	}

}
