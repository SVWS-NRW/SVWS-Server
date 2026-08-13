package de.svws_nrw.controller.lehrer.funktion;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionBatchPatchRequest;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionCreateRequest;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionPatchRequest;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

public final class LehrerFunktionControllerImpl implements LehrerFunktionController {

	private final LehrerFunktionService service;

	/**
	 * Erstellt einen neuen {@code LehrerFunktionController} mit dem angegebenen Service.
	 *
	 * @param service der {@link LehrerFunktionService}
	 */
	public LehrerFunktionControllerImpl(final LehrerFunktionService service) {
		this.service = service;
	}

	@Override
	public Response get(final long id) {
		return Responses.ok(service.get(id));
	}

	@Override
	public Response getAll() {
		return Responses.ok(service.getAll());
	}

	@Override
	public Response getListByIdAbschnitt(final long idAbschnitt) {
		return Responses.ok(service.getListByIdAbschnitt(idAbschnitt));
	}

	@Override
	public Response create(final LehrerFunktionCreateRequest dto) {
		BeanValidator.validate(dto);
		return Responses.created(service.create(dto));
	}

	@Override
	public Response createMultiple(final List<LehrerFunktionCreateRequest> dtos) {
		dtos.forEach(BeanValidator::validate);
		return Responses.created(service.createMultiple(dtos));
	}

	@Override
	public Response patch(final long id, final LehrerFunktionPatchRequest dto) {
		BeanValidator.validate(dto);
		return Responses.ok(service.patch(id, dto));
	}

	@Override
	public Response patchMultiple(final List<LehrerFunktionBatchPatchRequest> dtos) {
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
