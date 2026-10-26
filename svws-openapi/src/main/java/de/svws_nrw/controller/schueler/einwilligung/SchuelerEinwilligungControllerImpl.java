package de.svws_nrw.controller.schueler.einwilligung;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schueler.einwilligung.SchuelerEinwilligungPatchRequest;
import de.svws_nrw.service.schueler.einwilligung.SchuelerEinwilligungService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse nimmt Anfragen zu den Einwilligungen von Schülern entgegen und delegiert sie an den
 * {@link SchuelerEinwilligungService}.
 */
public final class SchuelerEinwilligungControllerImpl implements SchuelerEinwilligungController {

	private final SchuelerEinwilligungService service;

	/**
	 * Erstellt einen neuen SchuelerEinwilligungController mit dem angegebenen Service.
	 *
	 * @param service   der SchuelerEinwilligungService
	 */
	public SchuelerEinwilligungControllerImpl(final SchuelerEinwilligungService service) {
		this.service = service;
	}

	@Override
	public Response getListByIdSchueler(final long idSchueler) {
		return Responses.ok(service.getListByIdSchueler(idSchueler));
	}

	@Override
	public Response patch(final long idSchueler, final long idEinwilligungsart, final SchuelerEinwilligungPatchRequest patch) {
		BeanValidator.validate(patch);
		return Responses.ok(service.patch(idSchueler, idEinwilligungsart, patch));
	}

}
