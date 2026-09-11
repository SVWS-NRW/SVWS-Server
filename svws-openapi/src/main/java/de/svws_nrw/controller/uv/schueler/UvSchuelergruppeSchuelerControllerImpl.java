package de.svws_nrw.controller.uv.schueler;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeSchuelerPK;
import de.svws_nrw.core.data.uv.UvSchuelergruppeSchuelerCreateRequest;
import de.svws_nrw.service.uv.schueler.UvSchuelergruppeSchuelerService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die UV-Schülergruppe-Schüler-Zuordnungen gebündelt.
 */
public final class UvSchuelergruppeSchuelerControllerImpl implements UvSchuelergruppeSchuelerController {

	/** Der zugehörige Service */
	private final UvSchuelergruppeSchuelerService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvSchuelergruppeSchuelerControllerImpl(final UvSchuelergruppeSchuelerService service) {
		this.service = service;
	}

	@Override
	public Response create(final UvSchuelergruppeSchuelerCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response createMultiple(final Collection<UvSchuelergruppeSchuelerCreateRequest> createRequests) {
		return Responses.created(service.createMultiple(createRequests));
	}

	@Override
	public Response delete(final DTOUvSchuelergruppeSchuelerPK id) {
		return Responses.ok(service.delete(id));
	}

	@Override
	public Response deleteMultiple(final Collection<DTOUvSchuelergruppeSchuelerPK> ids) {
		return Responses.ok(service.deleteMultiple(ids));
	}

}
