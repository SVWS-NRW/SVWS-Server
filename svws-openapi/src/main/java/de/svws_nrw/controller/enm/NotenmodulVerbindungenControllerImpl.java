package de.svws_nrw.controller.enm;

import java.util.Collection;
import java.util.Map;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.enm.NotenmodulVerbindungenCreateRequest;
import de.svws_nrw.service.enm.NotenmodulVerbindungenPatchRequest;
import de.svws_nrw.service.enm.NotenmodulVerbindungenService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für das Notenmodul gebündelt.
 */
public final class NotenmodulVerbindungenControllerImpl implements NotenmodulVerbindungenController {

	private final NotenmodulVerbindungenService service;

	/**
	 * Erstellt für  die Datenbank-Verbindung eine neue Controller-Instanz.
	 *
	 * @param service   der Service
	 */
	public NotenmodulVerbindungenControllerImpl(final NotenmodulVerbindungenService service) {
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
	public Response getAll() {
		final var daten = service.getAll();
		return Responses.ok(daten);
	}

	@Override
	public Response patch(final long id, final NotenmodulVerbindungenPatchRequest patch) {
		final var daten = service.patch(id, patch);
		return Responses.ok(daten);
	}

	@Override
	public Response patchMultiple(final Map<Long, NotenmodulVerbindungenPatchRequest> patches) {
		final var daten = service.patchMultiple(patches);
		return Responses.ok(daten);
	}

	@Override
	public Response create(final NotenmodulVerbindungenCreateRequest createRequest) {
		final var daten = service.create(createRequest);
		return Responses.created(daten);
	}

	@Override
	public Response createMultiple(final Collection<NotenmodulVerbindungenCreateRequest> createRequests) {
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
