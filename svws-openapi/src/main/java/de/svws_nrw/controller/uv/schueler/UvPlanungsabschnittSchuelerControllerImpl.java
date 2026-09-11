package de.svws_nrw.controller.uv.schueler;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittSchuelerPK;
import de.svws_nrw.service.uv.schueler.UvPlanungsabschnittSchuelerCreateRequest;
import de.svws_nrw.service.uv.schueler.UvPlanungsabschnittSchuelerPatchRequest;
import de.svws_nrw.service.uv.schueler.UvPlanungsabschnittSchuelerService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die UV-Planungsabschnitt-Schüler-Zuordnungen gebündelt.
 */
public final class UvPlanungsabschnittSchuelerControllerImpl implements UvPlanungsabschnittSchuelerController {

	/** Der zugehörige Service */
	private final UvPlanungsabschnittSchuelerService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvPlanungsabschnittSchuelerControllerImpl(final UvPlanungsabschnittSchuelerService service) {
		this.service = service;
	}

	@Override
	public Response create(final UvPlanungsabschnittSchuelerCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response createMultiple(final Collection<UvPlanungsabschnittSchuelerCreateRequest> createRequests) {
		return Responses.created(service.createMultiple(createRequests));
	}

	@Override
	public Response patch(final UvPlanungsabschnittSchuelerPatchRequest patch) {
		return Responses.ok(service.patch(patch));
	}

	@Override
	public Response delete(final DTOUvPlanungsabschnittSchuelerPK id) {
		return Responses.ok(service.delete(id));
	}

	@Override
	public Response deleteMultiple(final Collection<DTOUvPlanungsabschnittSchuelerPK> ids) {
		return Responses.ok(service.deleteMultiple(ids));
	}

}
