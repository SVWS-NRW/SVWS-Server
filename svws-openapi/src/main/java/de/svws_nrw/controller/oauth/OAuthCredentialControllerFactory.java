package de.svws_nrw.controller.oauth;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.mapper.oauth.OAuthCredentialsInternalMapper;
import de.svws_nrw.oauth.internal.OAuthDomain;
import de.svws_nrw.repo.oauth.credential.OAuthCredentialRepositoryFactory;
import de.svws_nrw.service.crypto.secret.SecretCipherFactory;
import de.svws_nrw.service.oauth.credential.OAuthCredentialServiceFactory;
import de.svws_nrw.service.oauth.credential.OAuthDomainBerechtigung;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Factory für den {@link OAuthCredentialController}.
 *
 * <p>Hier wird die Verbindung zur Datenbank des Schemas aus dem Benutzerkontext des Requests aufgebaut und die
 * Berechtigung des Benutzers geprüft. Zudem werden die Repository-, Cipher- und Service-Schicht verdrahtet.</p>
 *
 * <p>Die Prüfung je {@link OAuthDomain} und Endpunkt erfolgt über die {@link OAuthDomainBerechtigung}.</p>
 */
public final class OAuthCredentialControllerFactory {

	private final OAuthCredentialServiceFactory serviceFactory;
	private final OAuthDomainBerechtigung berechtigung;

	/**
	 * Erstellt eine neue Factory mit der übergebenen Service-Factory.
	 *
	 * @param serviceFactory   die Factory zur Erzeugung des {@link de.svws_nrw.service.oauth.credential.OAuthCredentialService}
	 * @param berechtigung     die {@link OAuthDomainBerechtigung} des angemeldeten Benutzers
	 */
	private OAuthCredentialControllerFactory(final OAuthCredentialServiceFactory serviceFactory, final OAuthDomainBerechtigung berechtigung) {
		this.serviceFactory = serviceFactory;
		this.berechtigung = berechtigung;
	}

	/**
	 * Erzeugt eine neue {@link OAuthCredentialControllerFactory}. Dabei wird die Datenbankverbindung des Requests
	 * aufgebaut und geprüft, ob der Benutzer die Kompetenz für mindestens eine Domäne besitzt. Welche Domänen
	 * der Benutzer im Einzelnen nutzen darf, wird in der {@link OAuthDomainBerechtigung} festgehalten.
	 *
	 * @param request             der Http Request, aus dem der Benutzerkontext und das Schema ermittelt werden
	 *
	 * @return die neue {@link OAuthCredentialControllerFactory}
	 */
	public static OAuthCredentialControllerFactory getNewInstance(final HttpServletRequest request) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, OAuthDomainBerechtigung.alleKompetenzen());
		final var benutzer = DBBenutzerUtils.getSVWSUser(request, ServerMode.DEV, BenutzerKompetenz.KEINE);
		final var repositoryFactory = OAuthCredentialRepositoryFactory.getNewInstance();
		final var cipherFactory = SecretCipherFactory.getNewInstance();
		final var serviceFactory = OAuthCredentialServiceFactory.getNewInstance(repositoryFactory, cipherFactory);
		return new OAuthCredentialControllerFactory(serviceFactory, new OAuthDomainBerechtigung(benutzer));
	}

	/**
	 * Erzeugt einen neuen {@link OAuthCredentialController}.
	 *
	 * @return der neue {@link OAuthCredentialController}
	 */
	public OAuthCredentialController getCredentialController() {
		return new OAuthCredentialController(serviceFactory.getClientCredentialService(), OAuthCredentialsInternalMapper.INSTANCE, berechtigung);
	}
}
