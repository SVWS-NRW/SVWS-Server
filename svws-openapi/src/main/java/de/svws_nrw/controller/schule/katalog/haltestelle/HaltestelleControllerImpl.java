package de.svws_nrw.controller.schule.katalog.haltestelle;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Haltestelle;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schule.katalog.haltestelle.HaltestelleCreateRequest;
import de.svws_nrw.service.schule.katalog.haltestelle.HaltestellePatchRequest;
import de.svws_nrw.service.schule.katalog.haltestelle.HaltestelleService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des Controllers für den schulinternen Haltestellen-Katalog.
 */
public final class HaltestelleControllerImpl implements HaltestelleController {

	private final HaltestelleService haltestelleService;

	/**
	 * Initialisiert einen neuen Controller.
	 *
	 * @param haltestelleService {@link HaltestelleService}
	 */
	public HaltestelleControllerImpl(final HaltestelleService haltestelleService) {
		this.haltestelleService = haltestelleService;
	}

	@Override
	public Response getAll() {
		final List<Haltestelle> daten = haltestelleService.getAll();

		return Responses.ok(daten);
	}

	@Override
	public Response create(final HaltestelleCreateRequest request) {
		BeanValidator.validate(request);

		final Haltestelle created = haltestelleService.create(request);

		return Responses.created(created);
	}

	@Override
	public Response patch(final long id, final HaltestellePatchRequest patch) {
		BeanValidator.validate(patch);

		final Haltestelle updated = haltestelleService.patch(id, patch);

		return Responses.ok(updated);
	}

	@Override
	public Response delete(final List<Long> ids) {
		final List<SimpleOperationResponse> deleted = haltestelleService.delete(ids);

		return Responses.ok(deleted);
	}

}
