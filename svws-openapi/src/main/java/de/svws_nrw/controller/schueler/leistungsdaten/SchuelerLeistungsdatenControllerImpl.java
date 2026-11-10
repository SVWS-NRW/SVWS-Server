package de.svws_nrw.controller.schueler.leistungsdaten;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schueler.leistungsdaten.SchuelerLeistungsdatenCreateRequest;
import de.svws_nrw.service.schueler.leistungsdaten.SchuelerLeistungsdatenPatchRequest;
import de.svws_nrw.service.schueler.leistungsdaten.SchuelerLeistungsdatenService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des Controllers für die Leistungsdaten eines Schülers.
 */
public final class SchuelerLeistungsdatenControllerImpl implements SchuelerLeistungsdatenController {

	private final SchuelerLeistungsdatenService service;

	/**
	 * Erstellt einen neuen {@code SchuelerLeistungsdatenControllerImpl} mit dem angegebenen Service.
	 *
	 * @param service   der {@link SchuelerLeistungsdatenService}
	 */
	public SchuelerLeistungsdatenControllerImpl(final SchuelerLeistungsdatenService service) {
		this.service = service;
	}

	@Override
	public Response get(final long id) {
		return Responses.ok(service.getById(id));
	}

	@Override
	public Response create(final SchuelerLeistungsdatenCreateRequest dto) {
		BeanValidator.validate(dto);

		return Responses.created(service.create(dto));
	}

	@Override
	public Response patch(final long id, final SchuelerLeistungsdatenPatchRequest dto) {
		BeanValidator.validate(dto);

		return Responses.ok(service.patch(id, dto));
	}

	@Override
	public Response deleteMultiple(final List<Long> ids) {
		return Responses.ok(service.deleteMultiple(ids));
	}

}
