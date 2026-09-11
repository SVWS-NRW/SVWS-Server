package de.svws_nrw.controller.uv.lerngruppen;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppeSchienePK;
import de.svws_nrw.core.data.uv.UvLerngruppenSchieneCreateRequest;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppenSchieneService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die UV-Lerngruppen-Schienen-Zuordnungen gebündelt.
 */
public final class UvLerngruppenSchieneControllerImpl implements UvLerngruppenSchieneController {

	/** Der zugehörige Service */
	private final UvLerngruppenSchieneService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvLerngruppenSchieneControllerImpl(final UvLerngruppenSchieneService service) {
		this.service = service;
	}

	@Override
	public Response create(final UvLerngruppenSchieneCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response delete(final DTOUvLerngruppeSchienePK id) {
		return Responses.ok(service.delete(id));
	}

	@Override
	public Response deleteMultiple(final Collection<DTOUvLerngruppeSchienePK> ids) {
		return Responses.ok(service.deleteMultiple(ids));
	}

}
