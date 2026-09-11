package de.svws_nrw.controller.uv;

import de.svws_nrw.core.data.uv.UvGrunddatenBundle;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.UvGrunddatenServiceFactory;
import jakarta.ws.rs.core.Response;

/**
 * Default-Implementierung des {@link UvGrunddatenBundleController}.
 */
public final class UvGrunddatenBundleControllerImpl implements UvGrunddatenBundleController {

	private final UvGrunddatenServiceFactory serviceFactory;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param serviceFactory                die Service-Factory für die UV-Grunddaten
	 */
	public UvGrunddatenBundleControllerImpl(final UvGrunddatenServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	@Override
	public Response getGrunddatenBundle() {
		final UvGrunddatenBundle result = serviceFactory.getUvGrunddatenBundleService().getGrunddatenBundle();
		return Responses.ok(result);
	}

}
