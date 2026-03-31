package de.svws_nrw.controller.enm;

import de.svws_nrw.base.compression.CompressionException;
import de.svws_nrw.core.data.enm.v2.ENMv2Daten;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.Responses;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.enm.EnmV2GetService;
import de.svws_nrw.service.enm.EnmV2ImportService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe im Bereich der ENM-Daten in Version 2 gebündelt
 */
public final class EnmV2ControllerImpl implements EnmV2Controller {

	/** Der zugehörige Service */
	private final EnmV2GetService getService;

	/** Der zugehörige Import-Service */
	private final EnmV2ImportService importService;


	/**
	 * Erstellt für  die Datenbank-Verbindung eine neue Controller-Instanz.
	 *
	 * @param service        der zugehörige Service
	 * @param importService   der Service für den Import von ENM-Daten
	 */
	public EnmV2ControllerImpl(final EnmV2GetService service, final EnmV2ImportService importService) {
		this.getService = service;
		this.importService = importService;
	}

	@Override
	public Response get(final Long id) {
		final ENMv2Daten daten = getService.get(id);
		return Responses.ok(daten);
	}

	@Override
	public Response getGZip(final Long id) {
		try {
			final ENMv2Daten daten = getService.get(id);
			final byte[] encoded = JSONMapper.gzipByteArrayFromObject(daten);
			return Responses.okFile(encoded, "enm.json.gz");
		} catch (final CompressionException ce) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, ce, "Fehler beim Komprimieren der ENM-Daten.");
		}
	}

	@Override
	public Response applyLatest(final ENMv2Daten daten) {
		this.importService.applyLatest(daten);
		return Responses.noContent();
	}

	@Override
	public Response applyLatestGZip(final byte[] daten) {
		try {
			this.importService.applyLatest(JSONMapper.toObjectGZip(daten, ENMv2Daten.class));
			return Responses.noContent();
		} catch (final CompressionException e) {
			throw new ApiOperationException(Status.BAD_REQUEST, e, "Die ENM-Daten konnten nicht mit GZip entpackt werden.");
		}
	}

}
