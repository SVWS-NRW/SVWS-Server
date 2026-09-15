package de.svws_nrw.controller.gost.klausuren;

import java.util.List;

import de.svws_nrw.core.data.gost.klausuren.GostNachschreibterminblockungKonfiguration;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.gost.klausuren.GostKlausurenNachschreibterminBlockungService;
import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurterminCreateRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurterminPatchRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurterminWorkflowService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden höherwertige Schülerklausurtermin-Workflows gebündelt.
 */
public final class GostKlausurenSchuelerklausurterminWorkflowControllerImpl implements GostKlausurenSchuelerklausurterminWorkflowController {

	private final GostKlausurenSchuelerklausurterminWorkflowService workflowService;
	private final GostKlausurenNachschreibterminBlockungService blockungService;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param workflowService der gemeinsame Create-/Patch-Workflow
	 * @param blockungService der Blockungs-Service
	 */
	public GostKlausurenSchuelerklausurterminWorkflowControllerImpl(final GostKlausurenSchuelerklausurterminWorkflowService workflowService,
			final GostKlausurenNachschreibterminBlockungService blockungService) {
		this.workflowService = workflowService;
		this.blockungService = blockungService;
	}

	@Override
	public Response create(final GostKlausurenSchuelerklausurterminCreateRequest createRequest) {
		return Responses.created(workflowService.create(createRequest));
	}

	@Override
	public Response patch(final GostKlausurenSchuelerklausurterminPatchRequest patchRequest) {
		return Responses.ok(workflowService.patch(patchRequest));
	}

	@Override
	public Response patchMultiple(final List<GostKlausurenSchuelerklausurterminPatchRequest> patchRequests) {
		return Responses.ok(workflowService.patchMultiple(patchRequests));
	}

	@Override
	public Response blocken(final GostNachschreibterminblockungKonfiguration config) {
		return Responses.ok(blockungService.blocken(config));
	}

}
