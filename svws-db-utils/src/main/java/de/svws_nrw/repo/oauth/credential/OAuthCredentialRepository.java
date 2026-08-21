package de.svws_nrw.repo.oauth.credential;

import java.util.List;
import java.util.Optional;

import de.svws_nrw.core.types.oauth2.OAuthServiceDomain;
import de.svws_nrw.db.dto.current.svws.auth.DTOSchuleOAuthSecrets;
import de.svws_nrw.repo.Repository;

public interface OAuthCredentialRepository extends Repository<DTOSchuleOAuthSecrets> {

	/**
	 * Gibt DTOSchuleOAuthSecrets basierend auf der übergebenen {@link OAuthServiceDomain} als Optional zurück
	 *
	 * @param domain {@link OAuthServiceDomain}
	 *
	 * @return Optional of DTOSchuleOAuthSecrets
	 */
	Optional<DTOSchuleOAuthSecrets> findByServiceDomain(OAuthServiceDomain domain);

	/**
	 * Gibt die Liste von DTOSchuleOAuthSecrets basierend auf der übergebenen {@link OAuthServiceDomain} zurück
	 *
	 * @param domain {@link OAuthServiceDomain}
	 *
	 * @return Liste von DTOSchuleOAuthSecrets
	 */
	List<DTOSchuleOAuthSecrets> findAllByServiceDomain(OAuthServiceDomain domain);
}
