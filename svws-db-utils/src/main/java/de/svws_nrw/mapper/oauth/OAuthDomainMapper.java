package de.svws_nrw.mapper.oauth;

import de.svws_nrw.core.types.oauth2.OAuthServiceDomain;
import de.svws_nrw.oauth.internal.OAuthDomain;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper für OAuth-Domänen und OAuth2-Servertypen.
 */
@Mapper
public interface OAuthDomainMapper {

	/**
	 * Singleton Instanz
	 */
	OAuthDomainMapper INSTANCE = Mappers.getMapper(OAuthDomainMapper.class);

	/**
	 * Wandelt eine OAuth-Domäne in einen OAuth2-Servertyp um.
	 *
	 * @param input OAuth-Domäne
	 * @return zugehöriger OAuth2-Servertyp
	 */
	OAuthServiceDomain toDomain(OAuthDomain input);
}
