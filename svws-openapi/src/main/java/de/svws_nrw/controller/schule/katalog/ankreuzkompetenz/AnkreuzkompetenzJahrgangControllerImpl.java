package de.svws_nrw.controller.schule.katalog.ankreuzkompetenz;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.AnkreuzkompetenzJahrgangszuordnung;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzJahrgangCreateRequest;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzJahrgangService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des Controllers für den schulinternen Ankreuzkompetenz-Jahrgang-Katalog.
 */
public final class AnkreuzkompetenzJahrgangControllerImpl implements AnkreuzkompetenzJahrgangController {

	private final AnkreuzkompetenzJahrgangService ankreuzkompetenzJahrgangService;

	/**
	 * Initialisiert einen neuen Controller.
	 *
	 * @param ankreuzkompetenzJahrgangService {@link AnkreuzkompetenzJahrgangService}
	 */
	public AnkreuzkompetenzJahrgangControllerImpl(final AnkreuzkompetenzJahrgangService ankreuzkompetenzJahrgangService) {
		this.ankreuzkompetenzJahrgangService = ankreuzkompetenzJahrgangService;
	}

	@Override
	public Response createMultiple(final List<AnkreuzkompetenzJahrgangCreateRequest> request) {
		request.forEach(BeanValidator::validate);

		final List<AnkreuzkompetenzJahrgangszuordnung> created = ankreuzkompetenzJahrgangService.createMultiple(request);

		return Responses.created(created);
	}

	@Override
	public Response deleteMultiple(final List<Long> ids) {
		final List<SimpleOperationResponse> deleted = ankreuzkompetenzJahrgangService.delete(ids);

		return Responses.ok(deleted);
	}

}
