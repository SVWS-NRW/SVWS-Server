package de.svws_nrw.controller.gost.klausuren;

import java.util.List;

import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurCreateRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für höherwertige Schülerklausur-Workflows.
 */
public interface GostKlausurenSchuelerklausurWorkflowController {

	/**
	 * Erzeugt mehrere Schülerklausuren.
	 *
	 * @param createRequests die Create-Daten
	 *
	 * @return die Response
	 */
	Response createMultiple(List<GostKlausurenSchuelerklausurCreateRequest> createRequests);

}
