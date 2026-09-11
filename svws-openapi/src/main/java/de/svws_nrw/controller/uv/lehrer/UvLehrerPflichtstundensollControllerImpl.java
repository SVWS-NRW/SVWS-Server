package de.svws_nrw.controller.uv.lehrer;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.lehrer.UvLehrerPflichtstundensollCreateRequest;
import de.svws_nrw.service.uv.lehrer.UvLehrerPflichtstundensollImportService;
import de.svws_nrw.service.uv.lehrer.UvLehrerPflichtstundensollPatchRequest;
import de.svws_nrw.service.uv.lehrer.UvLehrerPflichtstundensollService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die Anrechnungsstunden eines Lehrers gebündelt.
 */
public final class UvLehrerPflichtstundensollControllerImpl implements UvLehrerPflichtstundensollController {

	/** Der zugehörige Get-Service */
	private final UvLehrerPflichtstundensollService service;

	/** Der Service für den Import des Pflichtstundensolls aus den Schild-Personalabschnittsdaten */
	private final UvLehrerPflichtstundensollImportService importService;


	/**
	 * Erstellt für die Datenbank-Verbindung eine neue Controller-Instanz.
	 *
	 * @param service         der zugehörige Get-Service
	 * @param importService   der Service für den Import aus den Schild-Personalabschnittsdaten
	 */
	public UvLehrerPflichtstundensollControllerImpl(final UvLehrerPflichtstundensollService service,
			final UvLehrerPflichtstundensollImportService importService) {
		this.service = service;
		this.importService = importService;
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
	public Response patch(final UvLehrerPflichtstundensollPatchRequest patch) {
		final var daten = service.patch(patch);
		return Responses.ok(daten);
	}

	@Override
	public Response patchMultiple(final Collection<UvLehrerPflichtstundensollPatchRequest> patches) {
		final var daten = service.patchMultiple(patches);
		return Responses.ok(daten);
	}

	@Override
	public Response create(final UvLehrerPflichtstundensollCreateRequest patch) {
		final var daten = service.create(patch);
		return Responses.created(daten);
	}

	@Override
	public Response createMultiple(final Collection<UvLehrerPflichtstundensollCreateRequest> patches) {
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

	@Override
	public Response importFromPersonalabschnittsdaten(final Collection<Long> uvLehrerIds) {
		final var neueDTOs = importService.importFromPersonalabschnittsdaten(uvLehrerIds);
		if (neueDTOs.isEmpty()) {
			return Responses.created(java.util.List.of());
		}
		final var neueIds = neueDTOs.stream().map(d -> d.ID).toList();
		final var daten = service.getList(neueIds);
		return Responses.created(daten);
	}

}
