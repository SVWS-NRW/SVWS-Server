package de.svws_nrw.controller.enm;

import java.util.List;

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
	 * Gibt für die angegebenen Lehrer die Initialkennwörter zurück.
	 *
	 * @param idsLehrer   die Ids der Lehrer, deren Initialkennwörter bestimmt werden sollen
	 *
	 * @return die Liste der Initialkennwörter
	 */
	Response getInitialkennwoerter(List<Long> idsLehrer);

	/**
	 * Gibt für den angegebenen Lehrer das Initialkennwort zurück.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return das Initialkennwort
	 */
	Response getInitialkennwort(long idLehrer);

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
	 * Generiert für einen Lehrers ein neues Initialkennwort.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die HTTP-Response
	 */
	Response generateInitialPassword(long idLehrer);


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
