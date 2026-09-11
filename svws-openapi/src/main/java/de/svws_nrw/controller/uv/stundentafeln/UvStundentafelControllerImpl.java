package de.svws_nrw.controller.uv.stundentafeln;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelCreateRequest;
import de.svws_nrw.core.data.uv.UvStundentafelImportOptions;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelImportService;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelPatchRequest;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die UV-Stundentafeln gebündelt.
 */
public final class UvStundentafelControllerImpl implements UvStundentafelController {

	/** Der zugehörige Service */
	private final UvStundentafelService service;

	/** Der zugehörige Import-Service. */
	private final UvStundentafelImportService importService;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 * @param importService  der zugehörige Import-Service
	 */
	public UvStundentafelControllerImpl(final UvStundentafelService service, final UvStundentafelImportService importService) {
		this.service = service;
		this.importService = importService;
	}

	@Override
	public Response get(final long id) {
		return Responses.ok(service.get(id));
	}

	@Override
	public Response getAll() {
		return Responses.ok(service.getAll());
	}

	@Override
	public Response create(final UvStundentafelCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response importiere(final UvStundentafelImportOptions importRequest) {
		return Responses.created(importService.importiere(importRequest));
	}

	@Override
	public Response createMultiple(final Collection<UvStundentafelCreateRequest> createRequests) {
		return Responses.created(service.createMultiple(createRequests));
	}

	@Override
	public Response patch(final UvStundentafelPatchRequest patch) {
		return Responses.ok(service.patch(patch));
	}

	@Override
	public Response patchMultiple(final Collection<UvStundentafelPatchRequest> patches) {
		return Responses.ok(service.patchMultiple(patches));
	}

	@Override
	public Response delete(final long id) {
		return Responses.ok(service.delete(id));
	}

	@Override
	public Response deleteMultiple(final Collection<Long> ids) {
		return Responses.ok(service.deleteMultiple(ids));
	}

}
