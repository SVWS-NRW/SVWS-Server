package de.svws_nrw.controller.lehrer.unterrichtsfach;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.lehrer.unterrichtsfach.LehrerUnterrichtsfachCreateRequest;
import de.svws_nrw.service.lehrer.unterrichtsfach.LehrerUnterrichtsfachPatchRequest;
import de.svws_nrw.service.lehrer.unterrichtsfach.LehrerUnterrichtsfachService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die Unterrichtsfächer eines Lehrers gebündelt.
 */
public final class LehrerUnterrichtsfachControllerImpl implements LehrerUnterrichtsfachController {

	/** Der zugehörige Service */
	private final LehrerUnterrichtsfachService service;


	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public LehrerUnterrichtsfachControllerImpl(final LehrerUnterrichtsfachService service) {
		this.service = service;
	}

	@Override
	public Response getListByLehrerId(final long idLehrer) {
		final var daten = service.getListByLehrerId(idLehrer);
		return Responses.ok(daten);
	}

	@Override
	public Response getListByLehrerIds(final Collection<Long> idsLehrer) {
		final var daten = service.getListByLehrerIds(idsLehrer);
		return Responses.ok(daten);
	}

	@Override
	public Response getMapByLehrerIds(final Collection<Long> idsLehrer) {
		final var daten = service.getMapByLehrerIds(idsLehrer);
		return Responses.ok(daten);
	}

	@Override
	public Response get(final long id) {
		final var daten = service.get(id);
		return Responses.ok(daten);
	}

	@Override
	public Response create(final LehrerUnterrichtsfachCreateRequest createRequest) {
		final var daten = service.create(createRequest);
		return Responses.created(daten);
	}

	@Override
	public Response patch(final long id, final LehrerUnterrichtsfachPatchRequest patch) {
		final var daten = service.patch(id, patch);
		return Responses.ok(daten);
	}

	@Override
	public Response delete(final long id) {
		final var daten = service.delete(id);
		return Responses.ok(daten);
	}

}
