package de.svws_nrw.controller.schule.katalog.einwilligungsart;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schule.katalog.einwilligungsart.EinwilligungsartCreateRequest;
import de.svws_nrw.service.schule.katalog.einwilligungsart.EinwilligungsartPatchRequest;
import de.svws_nrw.service.schule.katalog.einwilligungsart.EinwilligungsartService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des Controllers für den schulspezifischen Katalog der Einwilligungsarten.
 */
public final class EinwilligungsartControllerImpl implements EinwilligungsartController {

	private final EinwilligungsartService einwilligungsartService;

	/**
	 * Erstellt einen neuen Controller.
	 *
	 * @param einwilligungsartService   der Service zur Verarbeitung der Einwilligungsart-Fachlogik
	 */
	public EinwilligungsartControllerImpl(final EinwilligungsartService einwilligungsartService) {
		this.einwilligungsartService = einwilligungsartService;
	}

	@Override
	public Response getAll() {
		return Responses.ok(einwilligungsartService.getAll());
	}

	@Override
	public Response create(final EinwilligungsartCreateRequest request) {
		BeanValidator.validate(request);
		return Responses.created(einwilligungsartService.create(request));
	}

	@Override
	public Response patch(final long id, final EinwilligungsartPatchRequest patch) {
		BeanValidator.validate(patch);
		return Responses.ok(einwilligungsartService.patch(id, patch));
	}

	@Override
	public Response deleteMultiple(final List<Long> ids) {
		return Responses.ok(einwilligungsartService.deleteMultiple(ids));
	}

}
