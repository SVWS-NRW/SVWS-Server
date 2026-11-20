package de.svws_nrw.controller.schueler.kaoa;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schueler.kaoa.SchuelerKAoACreateRequest;
import de.svws_nrw.service.schueler.kaoa.SchuelerKAoAPatchRequest;
import de.svws_nrw.service.schueler.kaoa.SchuelerKAoAService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Die Implementierung des Controllers für die KAoA-Daten der Schüler.
 */
public final class SchuelerKAoAControllerImpl implements SchuelerKAoAController {

	private final SchuelerKAoAService service;

	/**
	 * Erstellt einen neuen Controller mit dem angegebenen Service.
	 *
	 * @param service   der Service für die KAoA-Daten der Schüler
	 */
	public SchuelerKAoAControllerImpl(final SchuelerKAoAService service) {
		this.service = service;
	}

	@Override
	public Response getList(final long idSchueler) {
		return Responses.ok(service.getListByIdSchueler(idSchueler));
	}

	@Override
	public Response create(final long idSchueler, final SchuelerKAoACreateRequest request) {
		BeanValidator.validate(request);
		return Responses.created(service.create(idSchueler, request));
	}

	@Override
	public Response patch(final long idSchueler, final long id, final SchuelerKAoAPatchRequest patch) {
		BeanValidator.validate(patch);
		return Responses.ok(service.patch(idSchueler, id, patch));
	}

	@Override
	public Response delete(final long idSchueler, final long id) {
		return Responses.ok(service.delete(idSchueler, id));
	}

}
