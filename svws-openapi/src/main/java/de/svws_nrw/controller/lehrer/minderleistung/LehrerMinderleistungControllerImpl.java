package de.svws_nrw.controller.lehrer.minderleistung;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.lehrer.minderleistung.LehrerMinderleistungBatchPatchRequest;
import de.svws_nrw.service.lehrer.minderleistung.LehrerMinderleistungCreateRequest;
import de.svws_nrw.service.lehrer.minderleistung.LehrerMinderleistungPatchRequest;
import de.svws_nrw.service.lehrer.minderleistung.LehrerMinderleistungService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

public final class LehrerMinderleistungControllerImpl implements LehrerMinderleistungController {

	private final LehrerMinderleistungService service;

	/**
	 * Constructor
	 *
	 * @param service {@link LehrerMinderleistungService}
	 */
	public LehrerMinderleistungControllerImpl(final LehrerMinderleistungService service) {
		this.service = service;
	}

	@Override
	public Response get(final long id) {
		final var result = service.get(id);

		return Responses.ok(result);
	}

	@Override
	public Response getList(final List<Long> ids) {
		final var result = service.getList(ids);

		return Responses.ok(result);
	}

	@Override
	public Response patch(final LehrerMinderleistungPatchRequest patch, final Long id) {
		BeanValidator.validate(patch);
		final var result = service.patch(patch, id);

		return Responses.ok(result);
	}

	@Override
	public Response patchMultiple(final List<LehrerMinderleistungBatchPatchRequest> patches) {
		patches.forEach(BeanValidator::validate);
		final var results = service.patchMultiple(patches);

		return Responses.ok(results);
	}

	@Override
	public Response create(final LehrerMinderleistungCreateRequest request) {
		BeanValidator.validate(request);
		final var result = service.create(request);

		return Responses.created(result);
	}

	@Override
	public Response createMultiple(final List<LehrerMinderleistungCreateRequest> requests) {
		requests.forEach(BeanValidator::validate);
		final var results = service.createMultiple(requests);

		return Responses.created(results);
	}

	@Override
	public Response delete(final long id) {
		final var log = service.delete(id);

		return Responses.ok(log);
	}

	@Override
	public Response deleteMultiple(final List<Long> ids) {
		final var logs = service.deleteMultiple(ids);

		return Responses.ok(logs);
	}

}
