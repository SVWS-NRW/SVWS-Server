package de.svws_nrw.controller.uv.lerngruppen;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppeKlassenImportService;
import jakarta.ws.rs.core.Response;

/**
 * Controller-Implementierung für das Erstellen von UV-Lerngruppen aus UV-Klassen.
 */
public final class UvLerngruppeKlassenImportControllerImpl implements UvLerngruppeKlassenImportController {

	private final UvLerngruppeKlassenImportService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der Import-Service
	 */
	public UvLerngruppeKlassenImportControllerImpl(final UvLerngruppeKlassenImportService service) {
		this.service = service;
	}

	@Override
	public Response createByKlassen(final Collection<Long> idsKlassen) {
		return Responses.created(service.createByKlassen(idsKlassen));
	}

}
