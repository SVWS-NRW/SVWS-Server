package de.svws_nrw.controller.lehrer.lernplattform;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.lehrer.lernplattform.LehrerLernplattformPatchRequest;
import de.svws_nrw.service.lehrer.lernplattform.LehrerLernplattformService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse nimmt Anfragen zu den Lernplattform-Zuordnungen von Lehrern entgegen und delegiert sie an den
 * {@link LehrerLernplattformService}.
 */
public final class LehrerLernplattformControllerImpl implements LehrerLernplattformController {

	private final LehrerLernplattformService service;

	/**
	 * Erstellt einen neuen LehrerLernplattformController mit dem angegebenen Service.
	 *
	 * @param service   der LehrerLernplattformService
	 */
	public LehrerLernplattformControllerImpl(final LehrerLernplattformService service) {
		this.service = service;
	}

	@Override
	public Response getListByIdLehrer(final long idLehrer) {
		return Responses.ok(service.getListByIdLehrer(idLehrer));
	}

	@Override
	public Response patch(final long idLehrer, final long idLernplattform, final LehrerLernplattformPatchRequest patch) {
		BeanValidator.validate(patch);
		return Responses.ok(service.patch(idLehrer, idLernplattform, patch));
	}

}
