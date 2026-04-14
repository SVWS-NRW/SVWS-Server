package de.svws_nrw.controller.enm;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.enm.NotenmodulCredentialsService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe für die Synchronisation mit einem externen Notenmodul-Server gebündelt.
 */
public final class NotenmodulCredentialsControllerImpl implements NotenmodulCredentialsController {

	private final NotenmodulCredentialsService service;

	/**
	 * Erstellt für  die Datenbank-Verbindung eine neue Controller-Instanz.
	 *
	 * @param service   der Service
	 */
	public NotenmodulCredentialsControllerImpl(final NotenmodulCredentialsService service) {
		this.service = service;
	}

	/**
	 * Gibt für alle Lehrer, welche bei den ENM-Daten vorkommen die Initialkennwörter zurück.
	 *
	 * @return die HTTP-Response
	 */
	@Override
	public Response getInitialkennwoerter() {
		return Responses.ok(service.getInitialkennwoerter());
	}


	/**
	 * Erstellt für alle Lehrer initiale Credentials, sofern ein Lehrer nicht bereits welche besitzt.
	 */
	@Override
	public Response generateMissingCredentials() {
		service.generateMissingCredentials();
		return Responses.noContent();
	}


	/**
	 * Setzt das Kennwort des Lehrers auf das Initialkennwort zurück. Ist kein Initialkennwort vorhanden,
	 * so wird ein neues generiert.
	 *
	 * @param idLehrer   die ID des Lehrers
	 */
	@Override
	public Response resetPassword(final long idLehrer) {
		service.resetPassword(idLehrer);
		return Responses.noContent();
	}


	/**
	 * Setzt das Kennwort des Lehrers auf das übergebene Kennwort. Das Initialkennwort bleibt dabei
	 * bestehen oder wird durch ein generiertes gesetzt, wenn der Lehrer vorher kein Initialkennwort hatte.
	 *
	 * @param idLehrer   die ID des Lehrers
	 * @param password   das neu zu setzende Kennwort
	 */
	@Override
	public Response setPassword(final long idLehrer, final String password) {
		service.setPassword(idLehrer, password);
		return Responses.noContent();
	}


	/**
	 * Ersetzt ein vorhandenes TOTP-Secret durch ein neues TOTP-Secret oder erzuegt ggf. ein neues. Danach
	 * wird erneut von einer Erstanmeldung für die 2FA ausgegangen.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die HTTP-Response
	 */
	@Override
	public Response resetTotpSecret(final long idLehrer) {
		service.resetTotpSecret(idLehrer);
		return Responses.noContent();
	}

	/**
	 * Setzt die Methode für die Zwei-Faktor-Authentifizierung für einen Lehrer.
	 *
	 * @param idLehrer   die ID des Lehrers
	 * @param art2FA     die zu verwendende Methode für die Zwei-Faktor-Authentifizierung
	 *
	 * @return die HTTP-Response
	 */
	@Override
	public Response setArt2FA(final long idLehrer, final Integer art2FA) {
		service.setArt2FA(idLehrer, art2FA);
		return Responses.noContent();
	}

}
