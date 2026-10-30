package de.svws_nrw.controller.schueler.create;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schueler.create.SchuelerCreateRequest;
import de.svws_nrw.service.schueler.create.SchuelerCreateService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des Controllers für das Neuanlegen eines Schülers.
 */
public final class SchuelerCreateControllerImpl implements SchuelerCreateController {

	private final SchuelerCreateService service;

	/**
	 * Erstellt einen neuen {@code SchuelerCreateController} mit dem angegebenen Service.
	 *
	 * @param service   der {@link SchuelerCreateService}
	 */
	public SchuelerCreateControllerImpl(final SchuelerCreateService service) {
		this.service = service;
	}

	@Override
	public Response create(final SchuelerCreateRequest dto) {
		BeanValidator.validate(dto);
		return Responses.created(service.create(dto));
	}

}
