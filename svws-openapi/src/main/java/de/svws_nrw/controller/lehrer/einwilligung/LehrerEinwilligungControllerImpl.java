package de.svws_nrw.controller.lehrer.einwilligung;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.lehrer.einwilligung.LehrerEinwilligungPatchRequest;
import de.svws_nrw.service.lehrer.einwilligung.LehrerEinwilligungService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse nimmt Anfragen zu den Einwilligungen von Lehrern entgegen und delegiert sie an den
 * {@link LehrerEinwilligungService}.
 */
public final class LehrerEinwilligungControllerImpl implements LehrerEinwilligungController {

	private final LehrerEinwilligungService service;

	/**
	 * Erstellt einen neuen LehrerEinwilligungController mit dem angegebenen Service.
	 *
	 * @param service   der LehrerEinwilligungService
	 */
	public LehrerEinwilligungControllerImpl(final LehrerEinwilligungService service) {
		this.service = service;
	}

	@Override
	public Response getListByIdLehrer(final long idLehrer) {
		return Responses.ok(service.getListByIdLehrer(idLehrer));
	}

	@Override
	public Response patch(final long idLehrer, final long idEinwilligungsart, final LehrerEinwilligungPatchRequest patch) {
		BeanValidator.validate(patch);
		return Responses.ok(service.patch(idLehrer, idEinwilligungsart, patch));
	}

}
