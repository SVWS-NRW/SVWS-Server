package de.svws_nrw.controller.schueler.stammdaten;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schueler.stammdaten.SchuelerImportData;
import de.svws_nrw.service.schueler.stammdaten.SchuelerStammdatenBatchPatchRequest;
import de.svws_nrw.service.schueler.stammdaten.SchuelerStammdatenPatchRequest;
import de.svws_nrw.service.schueler.stammdaten.SchuelerStammdatenService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

public final class SchuelerStammdatenControllerImpl implements SchuelerStammdatenController {

	private final SchuelerStammdatenService service;

	/**
	 * @param service {@link SchuelerStammdatenService}
	 */
	public SchuelerStammdatenControllerImpl(final SchuelerStammdatenService service) {
		this.service = service;
	}

	@Override
	public Response get(final long id) {
		return Responses.ok(service.get(id));
	}

	@Override
	public Response getList(final List<Long> ids) {
		return Responses.ok(service.getList(ids));
	}

	@Override
	public Response create(final SchuelerImportData dto) {
		BeanValidator.validate(dto);
		final var created = service.create(dto);
		return Responses.created(created);
	}

	@Override
	public Response patch(final long id, final SchuelerStammdatenPatchRequest dto) {
		BeanValidator.validate(dto);
		final var patched = service.patch(id, dto);
		return Responses.ok(patched);
	}

	@Override
	public Response patchMultiple(final List<SchuelerStammdatenBatchPatchRequest> dtos) {
		dtos.forEach(BeanValidator::validate);
		return Responses.ok(service.patchMultiple(dtos));
	}

	@Override
	public Response delete(final List<Long> ids) {
		final var responses = service.delete(ids);
		return Responses.ok(responses);
	}

}
