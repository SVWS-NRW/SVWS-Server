package de.svws_nrw.controller.schule.schuljahresabschnitt;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des Controllers für das Anlegen von Schuljahresabschnitten.
 */
public final class SchuljahresabschnittControllerImpl implements SchuljahresabschnittController {

	private final SchuljahresabschnittService service;

	/**
	 * Erstellt einen neuen {@code SchuljahresabschnittController} mit dem angegebenen Service.
	 *
	 * @param service   der {@link SchuljahresabschnittService}
	 */
	public SchuljahresabschnittControllerImpl(final SchuljahresabschnittService service) {
		this.service = service;
	}

	@Override
	public Response createVorErstem() {
		return Responses.created(service.createVorErstem());
	}

	@Override
	public Response createNachLetztem() {
		return Responses.created(service.createNachLetztem());
	}

}
