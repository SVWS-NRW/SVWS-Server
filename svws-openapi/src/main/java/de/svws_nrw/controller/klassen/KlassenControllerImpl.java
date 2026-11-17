package de.svws_nrw.controller.klassen;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.klassen.KlassenCreateRequest;
import de.svws_nrw.service.klassen.KlassenPatchRequest;
import de.svws_nrw.service.klassen.KlassenService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des {@link KlassenController}. Enthält keine Fachlogik und delegiert ausschließlich an den {@link KlassenService}.
 */
public final class KlassenControllerImpl implements KlassenController {

	private final KlassenService service;

	/**
	 * Erstellt einen neuen Controller.
	 *
	 * @param service   der Service für den Zugriff auf Klassen
	 */
	public KlassenControllerImpl(final KlassenService service) {
		this.service = service;
	}

	@Override
	public Response get(final long id) {
		return Responses.ok(service.getById(id));
	}

	@Override
	public Response getListBySchuljahresabschnitt(final long idSchuljahresabschnitt) {
		return Responses.ok(service.getListBySchuljahresabschnitt(idSchuljahresabschnitt, true));
	}

	@Override
	public Response getList(final List<Long> ids) {
		return Responses.ok(service.getListByIdsMitSchueler(ids));
	}

	@Override
	public Response create(final KlassenCreateRequest request) {
		BeanValidator.validate(request);

		return Responses.created(service.create(request));
	}

	@Override
	public Response patch(final long id, final KlassenPatchRequest request) {
		BeanValidator.validate(request);

		return Responses.ok(service.patch(id, request));
	}

	@Override
	public Response delete(final List<Long> ids) {
		return Responses.ok(service.delete(ids));
	}

	@Override
	public Response setDefaultSortierung(final long idSchuljahresabschnitt) {
		service.setDefaultSortierung(idSchuljahresabschnitt);

		return Responses.noContent();
	}

}
