package de.svws_nrw.controller.uv.kurse;

import de.svws_nrw.core.data.uv.UvKursImportDaten;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.kurse.UvKursImportService;
import jakarta.ws.rs.core.Response;

/**
 * Controller-Implementierung für den Import bestätigter Kursdaten in UV-Planungsabschnitte.
 */
public final class UvKursImportControllerImpl implements UvKursImportController {

	private final UvKursImportService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der Import-Service
	 */
	public UvKursImportControllerImpl(final UvKursImportService service) {
		this.service = service;
	}

	@Override
	public Response importDaten(final long idPlanungsabschnitt, final UvKursImportDaten daten) {
		return Responses.ok(service.importDaten(idPlanungsabschnitt, daten));
	}

}
