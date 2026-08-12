package de.svws_nrw.controller.lehrer;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.lehrer.anrechnung.LehrerAnrechnungsstundeCreateRequest;
import de.svws_nrw.service.lehrer.anrechnung.LehrerAnrechnungsstundePatchRequest;
import de.svws_nrw.service.lehrer.anrechnung.LehrerAnrechnungsstundeService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die Anrechnungsstunden eines Lehrers gebündelt.
 */
public final class LehrerAnrechnungsstundenControllerImpl implements LehrerAnrechnungsstundenController {

	/** Der zugehörige Get-Service */
	private final LehrerAnrechnungsstundeService service;


	/**
	 * Erstellt für  die Datenbank-Verbindung eine neue Controller-Instanz.
	 *
	 * @param service         der zugehörige Get-Service
	 */
	public LehrerAnrechnungsstundenControllerImpl(final LehrerAnrechnungsstundeService service) {
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
	public Response patch(final LehrerAnrechnungsstundePatchRequest patch) {
		final var daten = service.patch(patch);
		return Responses.ok(daten);
	}

	@Override
	public Response patchMultiple(final Collection<LehrerAnrechnungsstundePatchRequest> patches) {
		final var daten = service.patchMultiple(patches);
		return Responses.ok(daten);
	}

	@Override
	public Response create(final LehrerAnrechnungsstundeCreateRequest patch) {
		final var daten = service.create(patch);
		return Responses.created(daten);
	}

	@Override
	public Response createMultiple(final Collection<LehrerAnrechnungsstundeCreateRequest> patches) {
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
