package de.svws_nrw.controller.uv.lehrer;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.lehrer.UvLehrerCreateRequest;
import de.svws_nrw.service.uv.lehrer.UvLehrerPatchRequest;
import de.svws_nrw.service.uv.lehrer.UvLehrerService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die Anrechnungsstunden eines Lehrers gebündelt.
 */
public final class UvLehrerControllerImpl implements UvLehrerController {

	/** Der zugehörige Get-Service */
	private final UvLehrerService service;


	/**
	 * Erstellt für die Datenbank-Verbindung eine neue Controller-Instanz.
	 *
	 * @param service         der zugehörige Get-Service
	 */
	public UvLehrerControllerImpl(final UvLehrerService service) {
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
	public Response patch(final UvLehrerPatchRequest patch) {
		final var daten = service.patch(patch);
		return Responses.ok(daten);
	}

	@Override
	public Response patchMultiple(final Collection<UvLehrerPatchRequest> patches) {
		final var daten = service.patchMultiple(patches);
		return Responses.ok(daten);
	}

	@Override
	public Response create(final UvLehrerCreateRequest patch) {
		final var daten = service.create(patch);
		return Responses.created(daten);
	}

	@Override
	public Response createMultiple(final Collection<UvLehrerCreateRequest> patches) {
		final var daten = service.createMultiple(patches);
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
