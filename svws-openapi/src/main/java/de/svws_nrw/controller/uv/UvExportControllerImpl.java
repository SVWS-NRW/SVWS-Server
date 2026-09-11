package de.svws_nrw.controller.uv;

import de.svws_nrw.core.data.uv.export.UVv1Export;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.uv.UvExportService;
import jakarta.ws.rs.core.Response;

/**
 * Default-Implementierung des {@link UvExportController}.
 */
public final class UvExportControllerImpl implements UvExportController {

	private final UvExportService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service   der zugehörige Service
	 */
	public UvExportControllerImpl(final UvExportService service) {
		this.service = service;
	}

	@Override
	public Response getExportV1() {
		final UVv1Export result = service.getExportV1();
		return Responses.ok(result);
	}

}
