package de.svws_nrw.controller.schule.katalog.merkmal;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalCreateRequest;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalPatchRequest;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;


public final class MerkmalControllerImpl implements MerkmalController {

	private final MerkmalService merkmalService;

	/**
	 * Erstellt einen neuen MerkmalController mit dem angegebenen Service.
	 *
	 * @param merkmalService der Service zur Verarbeitung der Merkmal-Geschäftslogik
	 */
	public MerkmalControllerImpl(final MerkmalService merkmalService) {
		this.merkmalService = merkmalService;
	}

	@Override
	public Response getAll() {
		final var merkmale = this.merkmalService.getAll();
		return Responses.ok(merkmale);
	}

	@Override
	public Response create(final MerkmalCreateRequest dto) {
		BeanValidator.validate(dto);
		final var created = merkmalService.create(dto);
		return Responses.created(created);
	}

	@Override
	public Response delete(final List<Long> ids) {
		final var responses = this.merkmalService.delete(ids);
		return Responses.ok(responses);
	}


	@Override
	public Response patch(final long id, final MerkmalPatchRequest dto) {
		BeanValidator.validate(dto);
		final var patched = this.merkmalService.patch(id, dto);
		return Responses.ok(patched);
	}

}
