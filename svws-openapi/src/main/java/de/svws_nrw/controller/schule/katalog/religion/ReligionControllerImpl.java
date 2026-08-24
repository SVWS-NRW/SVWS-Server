package de.svws_nrw.controller.schule.katalog.religion;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schule.katalog.religion.ReligionCreateRequest;
import de.svws_nrw.service.schule.katalog.religion.ReligionPatchRequest;
import de.svws_nrw.service.schule.katalog.religion.ReligionService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des Controllers für den schulinternen Religionen-Katalog.
 */
public final class ReligionControllerImpl implements ReligionController {

	private final ReligionService religionService;

	/**
	 * Initialisiert einen neuen Controller.
	 *
	 * @param religionService {@link ReligionService}
	 */
	public ReligionControllerImpl(final ReligionService religionService) {
		this.religionService = religionService;
	}

	@Override
	public Response getAll() {
		final List<ReligionEintrag> daten = religionService.getAll();

		return Responses.ok(daten);
	}

	@Override
	public Response create(final ReligionCreateRequest request) {
		BeanValidator.validate(request);

		final ReligionEintrag created = religionService.create(request);

		return Responses.created(created);
	}

	@Override
	public Response patch(final long id, final ReligionPatchRequest patch) {
		BeanValidator.validate(patch);

		final ReligionEintrag updated = religionService.patch(id, patch);

		return Responses.ok(updated);
	}

	@Override
	public Response delete(final List<Long> ids) {
		final List<SimpleOperationResponse> deleted = religionService.delete(ids);

		return Responses.ok(deleted);
	}

}
