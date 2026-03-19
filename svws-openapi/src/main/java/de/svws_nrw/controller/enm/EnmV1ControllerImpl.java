package de.svws_nrw.controller.enm;

import de.svws_nrw.base.compression.CompressionException;
import de.svws_nrw.core.data.enm.ENMDaten;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.Responses;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.enm.EnmV1GetService;
import de.svws_nrw.service.enm.EnmV1ImportService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe im Bereich der ENM-Daten in Version gebündelt
 */
public final class EnmV1ControllerImpl implements EnmV1Controller {

	/** Der zugehörige Service */
	private final EnmV1GetService getService;

	/** Der zugehörige Import-Service */
	private final EnmV1ImportService importService;


	/**
	 * Erstellt für  die Datenbank-Verbindung eine neue Controller-Instanz.
	 *
	 * @param service        der zugehörige Service
	 * @param importService   der Service für den Import von ENM-Daten
	 */
	public EnmV1ControllerImpl(final EnmV1GetService service, final EnmV1ImportService importService) {
		this.getService = service;
		this.importService = importService;
	}

	@Override
	public Response get(final Long id) {
		final ENMDaten daten = getService.get(id);
		return Responses.ok(daten);
	}

	@Override
	public Response getGZip(final Long id) {
		try {
			final ENMDaten daten = getService.get(id);
			final byte[] encoded = JSONMapper.gzipByteArrayFromObject(daten);
			return Responses.okFile(encoded, "enm.json.gz");
		} catch (final CompressionException ce) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, ce, "Fehler beim Komprimieren der ENM-Daten.");
		}
	}

	@Override
	public Response applyLatest(final ENMDaten daten) {
		this.importService.applyLatest(daten);
		return Responses.noContent();
	}

	@Override
	public Response applyLatestGZip(final byte[] daten) {
		try {
			this.importService.applyLatest(JSONMapper.toObjectGZip(daten, ENMDaten.class));
			return Responses.noContent();
		} catch (final CompressionException e) {
			throw new ApiOperationException(Status.BAD_REQUEST, e, "Die ENM-Daten konnten nicht mit GZip entpackt werden.");
		}
	}

}
