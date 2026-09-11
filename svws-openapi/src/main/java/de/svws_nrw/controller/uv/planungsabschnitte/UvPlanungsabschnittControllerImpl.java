package de.svws_nrw.controller.uv.planungsabschnitte;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.UvPlanungsabschnittServiceFactory;
import de.svws_nrw.service.uv.planungsabschnitte.UvPlanungsabschnittCreateRequest;
import de.svws_nrw.service.uv.planungsabschnitte.UvPlanungsabschnittPatchRequest;
import de.svws_nrw.service.uv.planungsabschnitte.UvPlanungsabschnittService;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die UV-Planungsabschnitte gebündelt.
 */
public final class UvPlanungsabschnittControllerImpl implements UvPlanungsabschnittController {

	private final UvPlanungsabschnittServiceFactory serviceFactory;
	private final UvPlanungsabschnittService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param serviceFactory   die zugehörige ServiceFactory
	 */
	public UvPlanungsabschnittControllerImpl(final UvPlanungsabschnittServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
		this.service = serviceFactory.getUvPlanungsabschnittService();
	}

	@Override
	public Response get(final long id) {
		return Responses.ok(service.get(id));
	}

	@Override
	public Response getListBySchuljahr(final Integer schuljahr) {
		return Responses.ok(service.getListBySchuljahr(schuljahr));
	}

	@Override
	public Response create(final UvPlanungsabschnittCreateRequest createRequest) {
		return Responses.created(service.create(createRequest));
	}

	@Override
	public Response patch(final UvPlanungsabschnittPatchRequest patch) {
		return Responses.ok(service.patch(patch));
	}

	@Override
	public Response patchMultiple(final Collection<UvPlanungsabschnittPatchRequest> patches) {
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

	@Override
	public Response deleteMultipleAsListSimpleOperationResponse(final Collection<Long> ids) {
		final Map<Long, SimpleOperationResponse> mapResponses = new HashMap<>();
		service.deleteMultiple(ids);
		// Erstelle die SimpleOperationResponse-Objekte und füge sie in die Map ein.
		for (final Long id : ids) {
			final SimpleOperationResponse operationResponse = new SimpleOperationResponse();
			operationResponse.id = id;
			operationResponse.success = true;
			mapResponses.put(id, operationResponse);
		}
		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(mapResponses.values()).build();
	}

}
