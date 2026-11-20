package de.svws_nrw.controller.schule.katalog.fahrschuelerart;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schule.katalog.fahrschuelerart.FahrschuelerartCreateRequest;
import de.svws_nrw.service.schule.katalog.fahrschuelerart.FahrschuelerartPatchRequest;
import de.svws_nrw.service.schule.katalog.fahrschuelerart.FahrschuelerartService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des Controllers für den schulspezifischen Katalog der Fahrschülerarten.
 */
public final class FahrschuelerartControllerImpl implements FahrschuelerartController {

	private final FahrschuelerartService service;

	/**
	 * Erstellt einen neuen Controller.
	 *
	 * @param service   der Service zur Verarbeitung der Fahrschülerarten-Fachlogik
	 */
	public FahrschuelerartControllerImpl(final FahrschuelerartService service) {
		this.service = service;
	}

	@Override
	public Response getAll() {
		return Responses.ok(service.getAll());
	}

	@Override
	public Response create(final FahrschuelerartCreateRequest request) {
		BeanValidator.validate(request);
		return Responses.created(service.create(request));
	}

	@Override
	public Response patch(final long id, final FahrschuelerartPatchRequest patch) {
		BeanValidator.validate(patch);
		return Responses.ok(service.patch(id, patch));
	}

	@Override
	public Response deleteMultiple(final List<Long> ids) {
		return Responses.ok(service.deleteMultiple(ids));
	}
}
