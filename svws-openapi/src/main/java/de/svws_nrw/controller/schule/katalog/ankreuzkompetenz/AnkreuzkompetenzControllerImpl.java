package de.svws_nrw.controller.schule.katalog.ankreuzkompetenz;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzCreateRequest;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzPatchRequest;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des Controllers für den Katalog der Ankreuzkompetenzen.
 */
public final class AnkreuzkompetenzControllerImpl implements AnkreuzkompetenzController {

	private final AnkreuzkompetenzService ankreuzkompetenzService;

	/**
	 * Initialisiert einen neuen Controller.
	 *
	 * @param ankreuzkompetenzService {@link AnkreuzkompetenzService}
	 */
	public AnkreuzkompetenzControllerImpl(final AnkreuzkompetenzService ankreuzkompetenzService) {
		this.ankreuzkompetenzService = ankreuzkompetenzService;
	}

	@Override
	public Response getAll() {
		final var daten = ankreuzkompetenzService.getAll();

		return Responses.ok(daten);
	}

	@Override
	public Response create(final AnkreuzkompetenzCreateRequest request) {
		BeanValidator.validate(request);

		final var created = ankreuzkompetenzService.create(request);

		return Responses.created(created);
	}

	@Override
	public Response patch(final long id, final AnkreuzkompetenzPatchRequest patch) {
		BeanValidator.validate(patch);

		final var updated = ankreuzkompetenzService.patch(id, patch);

		return Responses.ok(updated);
	}

	@Override
	public Response delete(final List<Long> ids) {
		final var deleted = ankreuzkompetenzService.delete(ids);

		return Responses.ok(deleted);
	}

}
