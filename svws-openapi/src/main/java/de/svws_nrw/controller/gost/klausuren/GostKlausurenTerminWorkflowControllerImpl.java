package de.svws_nrw.controller.gost.klausuren;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.gost.klausuren.GostKlausurenTerminPatchRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenTerminPatchService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden höherwertige Klausurtermin-Workflows gebündelt.
 */
public final class GostKlausurenTerminWorkflowControllerImpl implements GostKlausurenTerminWorkflowController {

	private final GostKlausurenTerminPatchService patchService;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param patchService der Patch-Service
	 */
	public GostKlausurenTerminWorkflowControllerImpl(final GostKlausurenTerminPatchService patchService) {
		this.patchService = patchService;
	}

	@Override
	public Response patch(final GostKlausurenTerminPatchRequest patchRequest) {
		return Responses.ok(patchService.patch(patchRequest));
	}

}
