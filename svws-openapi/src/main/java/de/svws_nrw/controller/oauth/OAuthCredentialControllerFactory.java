package de.svws_nrw.controller.oauth;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.mapper.oauth.OAuthCredentialsInternalMapper;
import de.svws_nrw.repo.oauth.credential.OAuthCredentialRepositoryFactory;
import de.svws_nrw.service.crypto.secret.SecretCipherFactory;
import de.svws_nrw.service.oauth.credential.OAuthCredentialServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Factory für den {@link OAuthCredentialController}.
 *
 * <p>Hier wird die Verbindung zur Datenbank des Schemas aus dem Benutzerkontext des Requests aufgebaut und die
 * Berechtigung des Benutzers geprüft. Zudem werden die Repository-, Cipher- und Service-Schicht verdrahtet.</p>
 */
public final class OAuthCredentialControllerFactory {

	private final OAuthCredentialServiceFactory serviceFactory;

	/**
	 * Erstellt eine neue Factory mit der übergebenen Service-Factory.
	 *
	 * @param serviceFactory   die Factory zur Erzeugung des {@link de.svws_nrw.service.oauth.credential.OAuthCredentialService}
	 */
	private OAuthCredentialControllerFactory(final OAuthCredentialServiceFactory serviceFactory) {

		this.serviceFactory = serviceFactory;
	}

	/**
	 * Erzeugt eine neue {@link OAuthCredentialControllerFactory}. Dabei wird die Datenbankverbindung des Requests
	 * aufgebaut und geprüft, ob der Benutzer die übergebene Kompetenz besitzt.
	 *
	 * @param request             der Http Request, aus dem der Benutzerkontext und das Schema ermittelt werden
	 * @param benutzerKompetenz   die benötigte {@link BenutzerKompetenz}
	 *
	 * @return die neue {@link OAuthCredentialControllerFactory}
	 *
	 */
	public static OAuthCredentialControllerFactory getNewInstance(final HttpServletRequest request, final BenutzerKompetenz benutzerKompetenz) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, benutzerKompetenz);
		final var repositoryFactory = OAuthCredentialRepositoryFactory.getNewInstance();
		final var cipherFactory = SecretCipherFactory.getNewInstance();
		final var serviceFactory = OAuthCredentialServiceFactory.getNewInstance(repositoryFactory, cipherFactory);
		return new OAuthCredentialControllerFactory(serviceFactory);
	}

	/**
	 * Erzeugt einen neuen {@link OAuthCredentialController}.
	 *
	 * @return der neue {@link OAuthCredentialController}
	 */
	public OAuthCredentialController getCredentialController() {
		return new OAuthCredentialController(serviceFactory.getClientCredentialService(), OAuthCredentialsInternalMapper.INSTANCE);
	}
}
