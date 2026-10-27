package de.svws_nrw.controller.schule.katalog.lernplattform;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schule.katalog.lernplattform.LernplattformCreateRequest;
import de.svws_nrw.service.schule.katalog.lernplattform.LernplattformPatchRequest;
import de.svws_nrw.service.schule.katalog.lernplattform.LernplattformService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des Controllers für den schulspezifischen Katalog der Lernplattformen.
 */
public final class LernplattformControllerImpl implements LernplattformController {

	private final LernplattformService lernplattformService;

	/**
	 * Erstellt einen neuen Controller.
	 *
	 * @param lernplattformService   der Service zur Verarbeitung der Lernplattform-Fachlogik
	 */
	public LernplattformControllerImpl(final LernplattformService lernplattformService) {
		this.lernplattformService = lernplattformService;
	}

	@Override
	public Response getAll() {
		return Responses.ok(lernplattformService.getAll());
	}

	@Override
	public Response create(final LernplattformCreateRequest request) {
		BeanValidator.validate(request);
		return Responses.created(lernplattformService.create(request));
	}

	@Override
	public Response patch(final long id, final LernplattformPatchRequest patch) {
		BeanValidator.validate(patch);
		return Responses.ok(lernplattformService.patch(id, patch));
	}

	@Override
	public Response deleteMultiple(final List<Long> ids) {
		return Responses.ok(lernplattformService.deleteMultiple(ids));
	}

}
