package de.svws_nrw.controller.gost.klausuren;

import de.svws_nrw.service.gost.klausuren.GostKlausurenTerminPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für höherwertige Klausurtermin-Workflows.
 */
public interface GostKlausurenTerminWorkflowController {

	/**
	 * Patcht einen Klausurtermin.
	 *
	 * @param patchRequest die Patch-Daten
	 *
	 * @return die Response
	 */
	Response patch(GostKlausurenTerminPatchRequest patchRequest);

}
