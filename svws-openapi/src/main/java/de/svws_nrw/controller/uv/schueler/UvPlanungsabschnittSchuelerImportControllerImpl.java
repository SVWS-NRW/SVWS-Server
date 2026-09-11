package de.svws_nrw.controller.uv.schueler;

import de.svws_nrw.core.data.uv.UvSchuelerImportOptions;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.schueler.UvPlanungsabschnittSchuelerImportService;
import jakarta.ws.rs.core.Response;

/**
 * Controller-Implementierung für den Import von Schülern in UV-Planungsabschnitte.
 */
public final class UvPlanungsabschnittSchuelerImportControllerImpl implements UvPlanungsabschnittSchuelerImportController {

	private final UvPlanungsabschnittSchuelerImportService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der Import-Service
	 */
	public UvPlanungsabschnittSchuelerImportControllerImpl(final UvPlanungsabschnittSchuelerImportService service) {
		this.service = service;
	}

	@Override
	public Response importSchueler(final long idPlanungsabschnitt, final UvSchuelerImportOptions options) {
		return Responses.ok(service.importSchueler(idPlanungsabschnitt, options));
	}

}
