package de.svws_nrw.controller.gost.klausuren;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurterminblockungDaten;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.gost.klausuren.GostKlausurenKursklausurBlockungService;
import de.svws_nrw.service.gost.klausuren.GostKlausurenKursklausurCreationService;
import de.svws_nrw.service.gost.klausuren.GostKlausurenKursklausurPatchRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenKursklausurPatchService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden höherwertige Kursklausur-Workflows gebündelt.
 */
public final class GostKlausurenKursklausurWorkflowControllerImpl implements GostKlausurenKursklausurWorkflowController {

	private final GostKlausurenKursklausurPatchService patchService;
	private final GostKlausurenKursklausurCreationService creationService;
	private final GostKlausurenKursklausurBlockungService blockungService;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param patchService der Patch-Service
	 * @param creationService der Creation-Service
	 * @param blockungService der Blockungs-Service
	 */
	public GostKlausurenKursklausurWorkflowControllerImpl(final GostKlausurenKursklausurPatchService patchService,
			final GostKlausurenKursklausurCreationService creationService,
			final GostKlausurenKursklausurBlockungService blockungService) {
		this.patchService = patchService;
		this.creationService = creationService;
		this.blockungService = blockungService;
	}

	@Override
	public Response patch(final GostKlausurenKursklausurPatchRequest patchRequest) {
		return Responses.ok(patchService.patch(patchRequest));
	}

	@Override
	public Response create(final int abiturjahr, final int halbjahr, final int quartal) {
		return Responses.ok(creationService.createKlausuren(abiturjahr, halbjahr, quartal));
	}

	@Override
	public Response blocken(final GostKlausurterminblockungDaten blockungDaten) {
		return Responses.ok(blockungService.blocken(blockungDaten));
	}

}
