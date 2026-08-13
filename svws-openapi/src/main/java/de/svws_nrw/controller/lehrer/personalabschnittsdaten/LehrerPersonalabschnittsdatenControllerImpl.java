package de.svws_nrw.controller.lehrer.personalabschnittsdaten;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenBatchPatchRequest;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenCreateRequest;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenPatchRequest;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

public final class LehrerPersonalabschnittsdatenControllerImpl implements LehrerPersonalabschnittsdatenController {

	private final LehrerPersonalabschnittsdatenService service;

	/**
	 * Erstellt einen neuen {@code LehrerPersonalAbschnittsdatenController} mit dem angegebenen Service.
	 *
	 * @param service der {@link LehrerPersonalabschnittsdatenService}
	 */
	public LehrerPersonalabschnittsdatenControllerImpl(final LehrerPersonalabschnittsdatenService service) {
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
	public Response create(final LehrerPersonalabschnittsdatenCreateRequest dto) {
		BeanValidator.validate(dto);
		return Responses.created(service.create(dto));
	}

	@Override
	public Response createMultiple(final List<LehrerPersonalabschnittsdatenCreateRequest> dtos) {
		dtos.forEach(BeanValidator::validate);
		return Responses.created(service.createMultiple(dtos));
	}

	@Override
	public Response patch(final long id, final LehrerPersonalabschnittsdatenPatchRequest dto) {
		BeanValidator.validate(dto);
		return Responses.ok(service.patch(id, dto));
	}

	@Override
	public Response patchMultiple(final List<LehrerPersonalabschnittsdatenBatchPatchRequest> dtos) {
		dtos.forEach(BeanValidator::validate);
		return Responses.ok(service.patchMultiple(dtos));
	}

	@Override
	public Response delete(final long id) {
		return Responses.ok(service.delete(id));
	}

	@Override
	public Response deleteMultiple(final Collection<Long> ids) {
		return Responses.ok(service.deleteMultiple(ids));
	}
}
