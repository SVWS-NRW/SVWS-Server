package de.svws_nrw.controller.enm;

import de.svws_nrw.core.data.enm.ENMServerConfigElement;
import de.svws_nrw.data.Responses;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.service.enm.NotenmodulLocalAnkreuzkompetenzPatchRequest;
import de.svws_nrw.service.enm.NotenmodulLocalLeistungBemerkungenPatchRequest;
import de.svws_nrw.service.enm.NotenmodulLocalLeistungPatchRequest;
import de.svws_nrw.service.enm.NotenmodulLocalLernabschnittPatchRequest;
import de.svws_nrw.service.enm.NotenmodulLocalService;
import de.svws_nrw.service.enm.NotenmodulLocalTeilleistungPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden für Zugriffe auf das lokalen Notenmodul gebündelt.
 */
public final class NotenmodulLocalControllerImpl implements NotenmodulLocalController {

	private final NotenmodulLocalService service;
	private final Benutzer authenticatedUser;

	/**
	 * Erstellt für  die Datenbank-Verbindung eine neue Controller-Instanz.
	 *
	 * @param service             der Service
	 * @param authenticatedUser   der authentifizierte Benutzer
	 */
	public NotenmodulLocalControllerImpl(final NotenmodulLocalService service, final Benutzer authenticatedUser) {
		this.service = service;
		this.authenticatedUser = authenticatedUser;
	}


	@Override
	public Response getClientConfig() {
		return Responses.ok(service.getClientConfig());
	}

	@Override
	public Response getConfig() {
		return Responses.ok(service.getConfig());
	}

	@Override
	public Response setConfigElement(final ENMServerConfigElement elem) {
		service.setConfigElement(elem);
		return Responses.noContent();
	}


	@Override
	public Response patchLeistung(final NotenmodulLocalLeistungPatchRequest patch) {
		service.patchLeistung(patch, authenticatedUser);
		return Responses.noContent();
	}


	@Override
	public Response patchTeilleistung(final NotenmodulLocalTeilleistungPatchRequest patch) {
		service.patchTeilleistung(patch, authenticatedUser);
		return Responses.noContent();
	}


	@Override
	public Response patchBemerkungen(final long id, final NotenmodulLocalLeistungBemerkungenPatchRequest patch) {
		service.patchBemerkungen(id, patch, authenticatedUser);
		return Responses.noContent();
	}


	@Override
	public Response patchLernabschnitt(final NotenmodulLocalLernabschnittPatchRequest patch) {
		service.patchLernabschnitt(patch, authenticatedUser);
		return Responses.noContent();
	}

	@Override
	public Response patchAnkreuzkompetenz(final NotenmodulLocalAnkreuzkompetenzPatchRequest patch) {
		service.patchAnkreuzkompetenz(patch, authenticatedUser);
		return Responses.noContent();
	}

}
