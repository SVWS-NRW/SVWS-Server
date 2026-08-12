package de.svws_nrw.controller.lehrer;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.lehrer.mehrleistung.LehrerMehrleistungCreateRequest;
import de.svws_nrw.service.lehrer.mehrleistung.LehrerMehrleistungPatchRequest;
import de.svws_nrw.service.lehrer.mehrleistung.LehrerMehrleistungService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die Mehrleistungen eines Lehrers gebündelt.
 */
public final class LehrerMehrleistungControllerImpl implements LehrerMehrleistungController {

	private final LehrerMehrleistungService service;

	/**
	 * Erstellt für die Datenbank-Verbindung eine neue Controller-Instanz
	 *
	 * @param service  der zugehörige LehrerMehrleistungService
	 */
	public LehrerMehrleistungControllerImpl(final LehrerMehrleistungService service) {
		this.service = service;
	}

	@Override
	public Response get(final long id) {
		final var daten = service.get(id);
		return Responses.ok(daten);
	}

	@Override
	public Response getList(final Collection<Long> ids) {
		final var daten = service.getList(ids);
		return Responses.ok(daten);
	}

	@Override
	public Response patch(final LehrerMehrleistungPatchRequest patch) {
		final var daten = service.patch(patch);
		return Responses.ok(daten);
	}

	@Override
	public Response patchMultiple(final Collection<LehrerMehrleistungPatchRequest> patches) {
		final var daten = service.patchMultiple(patches);
		return Responses.ok(daten);
	}

	@Override
	public Response create(final LehrerMehrleistungCreateRequest createRequest) {
		final var daten = service.create(createRequest);
		return Responses.created(daten);
	}

	@Override
	public Response createMultiple(final List<LehrerMehrleistungCreateRequest> createRequests) {
		final var daten = service.createMultiple(createRequests);
		return Responses.created(daten);
	}

	@Override
	public Response delete(final long id) {
		final var daten = service.delete(id);
		return Responses.ok(daten);
	}

	@Override
	public Response deleteMultiple(final Collection<Long> ids) {
		final var daten = service.deleteMultiple(ids);
		return Responses.ok(daten);
	}
}
