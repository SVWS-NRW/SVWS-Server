package de.svws_nrw.controller.uv.zeitraster;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittZeitrasterPK;
import de.svws_nrw.service.uv.zeitraster.UvPlanungsabschnittZeitrasterCreateRequest;
import de.svws_nrw.service.uv.zeitraster.UvPlanungsabschnittZeitrasterPatchRequest;
import de.svws_nrw.service.uv.zeitraster.UvPlanungsabschnittZeitrasterService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die
 * UV-Planungsabschnitt-Zeitraster-Zuordnungen gebündelt.
 */
public final class UvPlanungsabschnittZeitrasterControllerImpl implements UvPlanungsabschnittZeitrasterController {

	/** Der zugehörige Service */
	private final UvPlanungsabschnittZeitrasterService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvPlanungsabschnittZeitrasterControllerImpl(final UvPlanungsabschnittZeitrasterService service) {
		this.service = service;
	}

	@Override
	public Response create(final UvPlanungsabschnittZeitrasterCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response createMultiple(final Collection<UvPlanungsabschnittZeitrasterCreateRequest> createRequests) {
		return Responses.created(service.createMultiple(createRequests));
	}

	@Override
	public Response patch(final UvPlanungsabschnittZeitrasterPatchRequest patch) {
		return Responses.ok(service.patch(patch));
	}

	@Override
	public Response delete(final DTOUvPlanungsabschnittZeitrasterPK pk) {
		return Responses.ok(service.delete(pk));
	}

	@Override
	public Response deleteMultiple(final long idPlanungsabschnitt, final Collection<Long> zeitrasterIds) {
		return Responses.ok(service.deleteMultiple(idPlanungsabschnitt, zeitrasterIds));
	}

}
