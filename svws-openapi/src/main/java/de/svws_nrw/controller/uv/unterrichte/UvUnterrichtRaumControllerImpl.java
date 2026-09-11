package de.svws_nrw.controller.uv.unterrichte;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.db.dto.current.uv.DTOUvUnterrichtRaumPK;
import de.svws_nrw.service.uv.unterrichte.UvUnterrichtRaumCreateRequest;
import de.svws_nrw.service.uv.unterrichte.UvUnterrichtRaumService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die UV-Unterricht-Raum-Zuordnungen gebündelt.
 */
public final class UvUnterrichtRaumControllerImpl implements UvUnterrichtRaumController {

	private final UvUnterrichtRaumService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvUnterrichtRaumControllerImpl(final UvUnterrichtRaumService service) {
		this.service = service;
	}

	@Override
	public Response create(final UvUnterrichtRaumCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response delete(final DTOUvUnterrichtRaumPK id) {
		return Responses.ok(service.delete(id));
	}

	@Override
	public Response deleteMultiple(final Collection<DTOUvUnterrichtRaumPK> ids) {
		return Responses.ok(service.deleteMultiple(ids));
	}

}
