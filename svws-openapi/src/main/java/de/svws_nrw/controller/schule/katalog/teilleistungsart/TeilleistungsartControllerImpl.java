package de.svws_nrw.controller.schule.katalog.teilleistungsart;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.kataloge.Teilleistungsart;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schule.katalog.teilleistungsart.TeilleistungsartCreateRequest;
import de.svws_nrw.service.schule.katalog.teilleistungsart.TeilleistungsartPatchRequest;
import de.svws_nrw.service.schule.katalog.teilleistungsart.TeilleistungsartService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

public final class TeilleistungsartControllerImpl implements TeilleistungsartController {

	private final TeilleistungsartService teilLeistungsartenService;

	/**
	 * Initialisiert einen neuen Controller
	 *
	 * @param teilLeistungsartenService {@link TeilleistungsartService}
	 */
	public TeilleistungsartControllerImpl(final TeilleistungsartService teilLeistungsartenService) {
		this.teilLeistungsartenService = teilLeistungsartenService;
	}

	@Override
	public Response getAll() {
		final List<Teilleistungsart> daten = teilLeistungsartenService.getAll();

		return Responses.ok(daten);
	}


	@Override
	public Response create(final TeilleistungsartCreateRequest input) {
		BeanValidator.validate(input);

		final Teilleistungsart created = teilLeistungsartenService.create(input);

		return Responses.created(created);
	}

	@Override
	public Response delete(final List<Long> ids) {
		final List<SimpleOperationResponse> deleted = teilLeistungsartenService.delete(ids);

		return Responses.ok(deleted);
	}

	@Override
	public Response patch(final long id, final TeilleistungsartPatchRequest patch) {
		BeanValidator.validate(patch);

		final Teilleistungsart updated = teilLeistungsartenService.patch(id, patch);

		return Responses.ok(updated);
	}

}
