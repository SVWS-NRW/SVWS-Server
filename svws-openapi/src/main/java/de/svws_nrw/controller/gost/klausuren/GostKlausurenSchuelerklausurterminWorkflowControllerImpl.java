package de.svws_nrw.controller.gost.klausuren;

import de.svws_nrw.core.data.gost.klausuren.GostNachschreibterminblockungKonfiguration;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.gost.klausuren.GostKlausurenNachschreibterminBlockungService;
import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurterminCreateRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurterminCreationService;
import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurterminPatchRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurterminPatchService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden höherwertige Schülerklausurtermin-Workflows gebündelt.
 */
public final class GostKlausurenSchuelerklausurterminWorkflowControllerImpl implements GostKlausurenSchuelerklausurterminWorkflowController {

	private final GostKlausurenSchuelerklausurterminCreationService creationService;
	private final GostKlausurenSchuelerklausurterminPatchService patchService;
	private final GostKlausurenNachschreibterminBlockungService blockungService;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param creationService der Creation-Service
	 * @param patchService der Patch-Service
	 * @param blockungService der Blockungs-Service
	 */
	public GostKlausurenSchuelerklausurterminWorkflowControllerImpl(final GostKlausurenSchuelerklausurterminCreationService creationService,
			final GostKlausurenSchuelerklausurterminPatchService patchService,
			final GostKlausurenNachschreibterminBlockungService blockungService) {
		this.creationService = creationService;
		this.patchService = patchService;
		this.blockungService = blockungService;
	}

	@Override
	public Response create(final GostKlausurenSchuelerklausurterminCreateRequest createRequest) {
		return Responses.created(creationService.create(createRequest));
	}

	@Override
	public Response patch(final GostKlausurenSchuelerklausurterminPatchRequest patchRequest) {
		return Responses.ok(patchService.patch(patchRequest));
	}

	@Override
	public Response blocken(final GostNachschreibterminblockungKonfiguration config) {
		return Responses.ok(blockungService.blocken(config));
	}

}
