package de.svws_nrw.controller.schueler.lernplattform;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schueler.lernplattform.SchuelerLernplattformPatchRequest;
import de.svws_nrw.service.schueler.lernplattform.SchuelerLernplattformService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse nimmt Anfragen zu den Lernplattform-Zuordnungen von Schülern entgegen und delegiert sie an den
 * {@link SchuelerLernplattformService}.
 */
public final class SchuelerLernplattformControllerImpl implements SchuelerLernplattformController {

	private final SchuelerLernplattformService service;

	/**
	 * Erstellt einen neuen SchuelerLernplattformController mit dem angegebenen Service.
	 *
	 * @param service   der SchuelerLernplattformService
	 */
	public SchuelerLernplattformControllerImpl(final SchuelerLernplattformService service) {
		this.service = service;
	}

	@Override
	public Response getListByIdSchueler(final long idSchueler) {
		return Responses.ok(service.getListByIdSchueler(idSchueler));
	}

	@Override
	public Response patch(final long idSchueler, final long idLernplattform, final SchuelerLernplattformPatchRequest patch) {
		BeanValidator.validate(patch);
		return Responses.ok(service.patch(idSchueler, idLernplattform, patch));
	}

}
