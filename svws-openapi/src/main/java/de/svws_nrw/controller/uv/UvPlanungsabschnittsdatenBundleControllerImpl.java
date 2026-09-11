package de.svws_nrw.controller.uv;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.UvPlanungsabschnittServiceFactory;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die UV-Planungsabschnitte gebündelt.
 */
public final class UvPlanungsabschnittsdatenBundleControllerImpl implements UvPlanungsabschnittsdatenBundleController {

	private final UvPlanungsabschnittServiceFactory serviceFactory;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param serviceFactory   die zugehörige ServiceFactory
	 */
	public UvPlanungsabschnittsdatenBundleControllerImpl(final UvPlanungsabschnittServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	@Override
	public Response getPlanungsabschnittsdatenBundle(final long idPlanungsabschnitt) {
		return Responses.ok(serviceFactory.getUvPlanungsabschnittsdatenBundleService().getPlanungsabschnittsdatenBundle(idPlanungsabschnitt));
	}

}
