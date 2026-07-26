package de.svws_nrw.controller.gost.klausuren;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.gost.klausuren.GostKlausurenVorgabeCreateRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenVorgabePatchRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenVorgabeService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die API-Zugriffe für GOSt-Klausurvorgaben gebündelt.
 */
public final class GostKlausurenVorgabeControllerImpl implements GostKlausurenVorgabeController {

	private final GostKlausurenVorgabeService gostKlausurenVorgabeService;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param gostKlausurenVorgabeService der zugehörige Service
	 */
	public GostKlausurenVorgabeControllerImpl(final GostKlausurenVorgabeService gostKlausurenVorgabeService) {
		this.gostKlausurenVorgabeService = gostKlausurenVorgabeService;
	}

	@Override
	public Response getListByAbiturjahr(final int abiturjahr) {
		return Responses.ok(gostKlausurenVorgabeService.getListByAbiturjahr(abiturjahr, -1, false));
	}

	@Override
	public Response create(final GostKlausurenVorgabeCreateRequest createRequest) {
		return Responses.created(gostKlausurenVorgabeService.create(createRequest));
	}

	@Override
	public Response patch(final GostKlausurenVorgabePatchRequest patch) {
		return Responses.ok(gostKlausurenVorgabeService.patch(patch));
	}

	@Override
	public Response patchMultiple(final Collection<GostKlausurenVorgabePatchRequest> patches) {
		return Responses.ok(gostKlausurenVorgabeService.patchMultiple(patches));
	}

	@Override
	public Response delete(final long id) {
		return Responses.ok(gostKlausurenVorgabeService.delete(id));
	}

	@Override
	public Response deleteMultiple(final Collection<Long> ids) {
		return Responses.ok(gostKlausurenVorgabeService.deleteMultiple(ids));
	}

}
