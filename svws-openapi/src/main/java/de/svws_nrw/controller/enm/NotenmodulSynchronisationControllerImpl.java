package de.svws_nrw.controller.enm;

import java.io.InputStream;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.enm.NotenmodulSynchronisationService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die Synchronisation mit einem externen Notenmodul-Server gebündelt.
 */
public final class NotenmodulSynchronisationControllerImpl implements NotenmodulSynchronisationController {

	private final NotenmodulSynchronisationService service;

	/**
	 * Erstellt für  die Datenbank-Verbindung eine neue Controller-Instanz.
	 *
	 * @param service   der Service
	 */
	public NotenmodulSynchronisationControllerImpl(final NotenmodulSynchronisationService service) {
		this.service = service;
	}

	@Override
	public Response synchronize(final long idVerbindung) {
		return Responses.ok(service.synchronize(idVerbindung));
	}


	@Override
	public Response upload(final long idVerbindung) {
		return Responses.ok(service.upload(idVerbindung));
	}


	@Override
	public Response download(final long idVerbindung) {
		return Responses.ok(service.download(idVerbindung));
	}


	@Override
	public Response truncate(final long idVerbindung) {
		return Responses.ok(service.truncate(idVerbindung));
	}

	@Override
	public Response reset(final long idVerbindung) {
		return Responses.ok(service.reset(idVerbindung));
	}

	@Override
	public Response check(final long idVerbindung) {
		return Responses.ok(service.check(idVerbindung));
	}

	@Override
	public Response getENMServerConfig(final long idVerbindung) {
		return Responses.ok(service.getENMServerConfig(idVerbindung));
	}

	@Override
	public Response setENMServerConfigElement(final long idVerbindung, final InputStream is) {
		return Responses.ok(service.setENMServerConfigElement(idVerbindung, is));
	}

	@Override
	public Response setup(final long idVerbindung) {
		return Responses.ok(service.setup(idVerbindung));
	}

}
