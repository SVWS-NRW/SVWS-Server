package de.svws_nrw.controller.gost.klausuren;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurCreateRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurCreationService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden höherwertige Schülerklausur-Workflows gebündelt.
 */
public final class GostKlausurenSchuelerklausurWorkflowControllerImpl implements GostKlausurenSchuelerklausurWorkflowController {

	private final GostKlausurenSchuelerklausurCreationService creationService;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param creationService der Creation-Service
	 */
	public GostKlausurenSchuelerklausurWorkflowControllerImpl(final GostKlausurenSchuelerklausurCreationService creationService) {
		this.creationService = creationService;
	}

	@Override
	public Response createMultiple(final List<GostKlausurenSchuelerklausurCreateRequest> createRequests) {
		return Responses.created(creationService.addMultiple(createRequests));
	}

}
