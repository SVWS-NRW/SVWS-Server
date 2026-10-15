package de.svws_nrw.controller.schulwechsel.erfolgreiche_bewerbung;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schulwechsel.erfolgreiche_bewerbung.SchulwechselErfolgreicheBewerbungCreateRequest;
import de.svws_nrw.service.schulwechsel.erfolgreiche_bewerbung.SchulwechselErfolgreicheBewerbungPatchRequest;
import de.svws_nrw.service.schulwechsel.erfolgreiche_bewerbung.SchulwechselErfolgreicheBewerbungService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Controller für CRUD-Operationen und Synchronisation auf {@code SchulwechselErfolgreicheBewerbung}-Einträgen.
 */
public final class SchulwechselErfolgreicheBewerbungControllerImpl implements SchulwechselErfolgreicheBewerbungController {

	private final SchulwechselErfolgreicheBewerbungService service;

	/**
	 * Erstellt einen neuen {@code SchulwechselErfolgreicheBewerbungControllerImpl}.
	 *
	 * @param service der zugehörige Service
	 */
	public SchulwechselErfolgreicheBewerbungControllerImpl(final SchulwechselErfolgreicheBewerbungService service) {
		this.service = service;
	}

	@Override
	public Response getById(final long id) {
		return Responses.ok(this.service.getById(id));
	}

	@Override
	public Response getAll() {
		return Responses.ok(this.service.getAll());
	}

	@Override
	public Response create(final SchulwechselErfolgreicheBewerbungCreateRequest dto) {
		BeanValidator.validate(dto);
		return Responses.created(this.service.create(dto));
	}

	@Override
	public Response patch(final long id, final SchulwechselErfolgreicheBewerbungPatchRequest dto) {
		BeanValidator.validate(dto);
		return Responses.ok(this.service.patch(id, dto));
	}

	@Override
	public Response delete(final List<Long> ids) {
		return Responses.ok(this.service.delete(ids));
	}

	@Override
	public Response sync() {
		this.service.sync();
		return Responses.noContent();
	}

	@Override
	public Response syncPreview() {
		return Responses.ok(this.service.syncPreview());
	}

}
