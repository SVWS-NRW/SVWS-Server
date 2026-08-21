package de.svws_nrw.controller.oauth;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.mapper.oauth.OAuthCredentialsInternalMapper;
import de.svws_nrw.repo.oauth.credential.OAuthCredentialRepositoryFactory;
import de.svws_nrw.service.oauth.credential.OAuthCredentialServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

public final class OAuthCredentialControllerFactory {

	private final OAuthCredentialServiceFactory serviceFactory;

	private OAuthCredentialControllerFactory(final OAuthCredentialServiceFactory serviceFactory) {

		this.serviceFactory = serviceFactory;
	}

	/**
	 * Erzeugt neue {@link OAuthCredentialControllerFactory}
	 *
	 * @param request der Http Request
	 * @param benutzerKompetenz die benötigte {@link BenutzerKompetenz}
	 *
	 * @return {@link OAuthCredentialControllerFactory}
	 */
	public static OAuthCredentialControllerFactory getNewInstance(final HttpServletRequest request, final BenutzerKompetenz benutzerKompetenz) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, benutzerKompetenz);
		final var repositoryFactory = OAuthCredentialRepositoryFactory.getNewInstance();
		final var serviceFactory = OAuthCredentialServiceFactory.getNewInstance(repositoryFactory);
		return new OAuthCredentialControllerFactory(serviceFactory);
	}

	/**
	 * Erzeugt neuen {@link OAuthCredentialController}
	 *
	 * @return {@link OAuthCredentialController}
	 */
	public OAuthCredentialController getCredentialController() {
		return new OAuthCredentialController(serviceFactory.getClientCredentialService(), OAuthCredentialsInternalMapper.INSTANCE);
	}
}
