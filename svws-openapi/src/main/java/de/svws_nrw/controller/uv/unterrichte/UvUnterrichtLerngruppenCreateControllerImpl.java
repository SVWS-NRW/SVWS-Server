package de.svws_nrw.controller.uv.unterrichte;

import java.util.Collection;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.unterrichte.UvUnterrichtLerngruppenCreateService;
import jakarta.ws.rs.core.Response;

/**
 * Controller-Implementierung für das Erstellen von UV-Unterrichten auf Basis von Lerngruppen.
 */
public final class UvUnterrichtLerngruppenCreateControllerImpl implements UvUnterrichtLerngruppenCreateController {

	private final UvUnterrichtLerngruppenCreateService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der Fachservice
	 */
	public UvUnterrichtLerngruppenCreateControllerImpl(final UvUnterrichtLerngruppenCreateService service) {
		this.service = service;
	}

	@Override
	public Response createByLerngruppe(final long idLerngruppe) {
		return Responses.created(service.createByLerngruppe(idLerngruppe));
	}

	@Override
	public Response createByLerngruppen(final Collection<Long> idsLerngruppen) {
		return Responses.created(service.createByLerngruppen(idsLerngruppen));
	}

}
