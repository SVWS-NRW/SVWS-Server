package de.svws_nrw.controller.uv.lehrer;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.lehrer.UvLehrerUnterrichtsfachCreateRequest;
import de.svws_nrw.service.uv.lehrer.UvLehrerUnterrichtsfachPatchRequest;
import de.svws_nrw.service.uv.lehrer.UvLehrerUnterrichtsfachService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die Unterrichtsfächer
 * eines UV-Lehrers gebündelt.
 */
public final class UvLehrerUnterrichtsfachControllerImpl implements UvLehrerUnterrichtsfachController {

	/** Der zugehörige Service */
	private final UvLehrerUnterrichtsfachService service;


	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvLehrerUnterrichtsfachControllerImpl(final UvLehrerUnterrichtsfachService service) {
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
	public Response create(final UvLehrerUnterrichtsfachCreateRequest createRequest) {
		final var daten = service.create(createRequest);
		return Responses.created(daten);
	}

	@Override
	public Response patch(final long id, final UvLehrerUnterrichtsfachPatchRequest patch) {
		patch.id = id;
		final var daten = service.patch(id, patch);
		return Responses.ok(daten);
	}

	@Override
	public Response delete(final long id) {
		final var daten = service.delete(id);
		return Responses.ok(daten);
	}

}
