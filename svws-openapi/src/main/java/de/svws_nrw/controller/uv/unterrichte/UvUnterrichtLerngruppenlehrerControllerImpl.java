package de.svws_nrw.controller.uv.unterrichte;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.db.dto.current.uv.DTOUvUnterrichteLerngruppenlehrerPK;
import de.svws_nrw.service.uv.lerngruppen.UvUnterrichtLerngruppenlehrerCreateRequest;
import de.svws_nrw.service.uv.lerngruppen.UvUnterrichtLerngruppenlehrerService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die UV-Unterricht-Lerngruppenlehrer-Zuordnungen gebündelt.
 */
public final class UvUnterrichtLerngruppenlehrerControllerImpl implements UvUnterrichtLerngruppenlehrerController {

	private final UvUnterrichtLerngruppenlehrerService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvUnterrichtLerngruppenlehrerControllerImpl(final UvUnterrichtLerngruppenlehrerService service) {
		this.service = service;
	}

	@Override
	public Response create(final UvUnterrichtLerngruppenlehrerCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response delete(final DTOUvUnterrichteLerngruppenlehrerPK id) {
		return Responses.ok(service.delete(id));
	}

	@Override
	public Response deleteMultiple(final Collection<DTOUvUnterrichteLerngruppenlehrerPK> ids) {
		return Responses.ok(service.deleteMultiple(ids));
	}

}
