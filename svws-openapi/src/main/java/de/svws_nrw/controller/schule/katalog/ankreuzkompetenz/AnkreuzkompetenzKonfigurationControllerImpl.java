package de.svws_nrw.controller.schule.katalog.ankreuzkompetenz;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzKonfigurationPatchRequest;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzKonfigurationService;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des Controllers für die Konfiguration der Ankreuzkompetenzen.
 */
public final class AnkreuzkompetenzKonfigurationControllerImpl implements AnkreuzkompetenzKonfigurationController {

	private final AnkreuzkompetenzKonfigurationService ankreuzkompetenzKonfigurationService;

	/**
	 * Initialisiert einen neuen Controller.
	 *
	 * @param ankreuzkompetenzKonfigurationService   der Service {@link AnkreuzkompetenzKonfigurationService}
	 */
	public AnkreuzkompetenzKonfigurationControllerImpl(final AnkreuzkompetenzKonfigurationService ankreuzkompetenzKonfigurationService) {
		this.ankreuzkompetenzKonfigurationService = ankreuzkompetenzKonfigurationService;
	}

	@Override
	public Response get() {
		return Responses.ok(ankreuzkompetenzKonfigurationService.get());
	}

	@Override
	public Response patch(final AnkreuzkompetenzKonfigurationPatchRequest patch) {
		return Responses.ok(ankreuzkompetenzKonfigurationService.patch(patch));
	}

}
