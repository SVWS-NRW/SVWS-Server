package de.svws_nrw.controller.uv.lehrer;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittLehrerPK;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrerCreateRequest;
import de.svws_nrw.service.uv.lehrer.UvPlanungsabschnittLehrerService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die UV-Planungsabschnitt-Lehrer-Zuordnungen gebündelt.
 */
public final class UvPlanungsabschnittLehrerControllerImpl implements UvPlanungsabschnittLehrerController {

	/** Der zugehörige Service */
	private final UvPlanungsabschnittLehrerService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvPlanungsabschnittLehrerControllerImpl(final UvPlanungsabschnittLehrerService service) {
		this.service = service;
	}

	@Override
	public Response create(final UvPlanungsabschnittLehrerCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response createMultiple(final Collection<UvPlanungsabschnittLehrerCreateRequest> createRequests) {
		return Responses.created(service.createMultiple(createRequests));
	}

	@Override
	public Response delete(final DTOUvPlanungsabschnittLehrerPK id) {
		return Responses.ok(service.delete(id));
	}

	@Override
	public Response deleteMultiple(final Collection<DTOUvPlanungsabschnittLehrerPK> ids) {
		return Responses.ok(service.deleteMultiple(ids));
	}

}
