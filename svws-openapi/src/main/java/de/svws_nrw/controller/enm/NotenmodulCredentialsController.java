package de.svws_nrw.controller.enm;

import jakarta.ws.rs.core.Response;

/**
 * Das Interface für die API-Zugriffe für die Synchronisation mit einem externen Notenmodul-Server
 */
public interface NotenmodulCredentialsController {

	/**
	 * Gibt für alle Lehrer, welche bei den ENM-Daten vorkommen die Initialkennwörter zurück.
	 *
	 * @return die HTTP-Response
	 */
	Response getInitialkennwoerter();


	/**
	 * Erstellt für alle Lehrer initiale Credentials, sofern ein Lehrer nicht bereits welche besitzt.
	 *
	 * @return die HTTP-Response
	 */
	Response generateMissingCredentials();


	/**
	 * Setzt das Kennwort des Lehrers auf das Initialkennwort zurück. Ist kein Initialkennwort vorhanden,
	 * so wird ein neues generiert.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die HTTP-Response
	 */
	Response resetPassword(long idLehrer);


	/**
	 * Setzt das Kennwort des Lehrers auf das übergebene Kennwort. Das Initialkennwort bleibt dabei
	 * bestehen oder wird durch ein generiertes gesetzt, wenn der Lehrer vorher kein Initialkennwort hatte.
	 *
	 * @param idLehrer   die ID des Lehrers
	 * @param password   das neu zu setzende Kennwort
	 *
	 * @return die HTTP-Response
	 */
	Response setPassword(long idLehrer, String password);


	/**
	 * Ersetzt ein vorhandenes TOTP-Secret durch ein neues TOTP-Secret oder erzuegt ggf. ein neues. Danach
	 * wird erneut von einer Erstanmeldung für die 2FA ausgegangen.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die HTTP-Response
	 */
	Response resetTotpSecret(long idLehrer);

	/**
	 * Setzt die Methode für die Zwei-Faktor-Authentifizierung für einen Lehrer.
	 *
	 * @param idLehrer   die ID des Lehrers
	 * @param art2FA     die zu verwendende Methode für die Zwei-Faktor-Authentifizierung
	 *
	 * @return die HTTP-Response
	 */
	Response setArt2FA(long idLehrer, Integer art2FA);

}
