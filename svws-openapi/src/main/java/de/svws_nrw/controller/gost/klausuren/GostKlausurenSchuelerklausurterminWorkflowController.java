package de.svws_nrw.controller.gost.klausuren;

import java.util.List;

import de.svws_nrw.core.data.gost.klausuren.GostNachschreibterminblockungKonfiguration;
import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurterminCreateRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurterminPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für höherwertige Schülerklausurtermin-Workflows.
 */
public interface GostKlausurenSchuelerklausurterminWorkflowController {

	/**
	 * Erstellt einen Schülerklausurtermin.
	 *
	 * @param createRequest die Create-Daten
	 *
	 * @return die Response
	 */
	Response create(GostKlausurenSchuelerklausurterminCreateRequest createRequest);

	/**
	 * Patcht einen Schülerklausurtermin.
	 *
	 * @param patchRequest die Patch-Daten
	 *
	 * @return die Response
	 */
	Response patch(GostKlausurenSchuelerklausurterminPatchRequest patchRequest);

	/**
	 * Patcht mehrere Schülerklausurtermine.
	 *
	 * @param patchRequests die Patch-Daten
	 *
	 * @return die Response
	 */
	Response patchMultiple(List<GostKlausurenSchuelerklausurterminPatchRequest> patchRequests);

	/**
	 * Blockt Nachschreibtermine.
	 *
	 * @param config die Blockungskonfiguration
	 *
	 * @return die Response
	 */
	Response blocken(GostNachschreibterminblockungKonfiguration config);

}
