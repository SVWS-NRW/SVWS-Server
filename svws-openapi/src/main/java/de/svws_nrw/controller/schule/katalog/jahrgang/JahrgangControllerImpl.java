package de.svws_nrw.controller.schule.katalog.jahrgang;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schule.katalog.jahrgang.JahrgangCreateRequest;
import de.svws_nrw.service.schule.katalog.jahrgang.JahrgangPatchRequest;
import de.svws_nrw.service.schule.katalog.jahrgang.JahrgangService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des Controllers für den schulinternen Jahrgangs-Katalog.
 */
public final class JahrgangControllerImpl implements JahrgangController {

	private final JahrgangService jahrgangService;

	/**
	 * Initialisiert einen neuen Controller.
	 *
	 * @param jahrgangService {@link JahrgangService}
	 */
	public JahrgangControllerImpl(final JahrgangService jahrgangService) {
		this.jahrgangService = jahrgangService;
	}

	@Override
	public Response getAll() {
		final List<JahrgangsDaten> daten = jahrgangService.getAll();

		return Responses.ok(daten);
	}

	@Override
	public Response create(final JahrgangCreateRequest request) {
		BeanValidator.validate(request);

		final JahrgangsDaten created = jahrgangService.create(request);

		return Responses.created(created);
	}

	@Override
	public Response patch(final long id, final JahrgangPatchRequest request) {
		BeanValidator.validate(request);

		final JahrgangsDaten patched = jahrgangService.patch(id, request);

		return Responses.ok(patched);
	}

	@Override
	public Response delete(final List<Long> ids) {
		final List<SimpleOperationResponse> deleted = jahrgangService.delete(ids);

		return Responses.ok(deleted);
	}

}
