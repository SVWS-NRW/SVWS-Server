package de.svws_nrw.mapper.oauth;

import de.svws_nrw.core.data.oauth2.OAuthCredentials;
import de.svws_nrw.mapper.UriMapper;
import de.svws_nrw.oauth.internal.Credentials;
import de.svws_nrw.oauth.internal.OAuthDomain;
import de.svws_nrw.service.oauth.credential.OAuthCreateCredential;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * Mapper für interne und externe OAuth-Zugangsdaten.
 */
@Mapper(uses = UriMapper.class)
public interface OAuthCredentialsInternalMapper {

	/**
	 * Singleton Instanz
	 */
	OAuthCredentialsInternalMapper INSTANCE = Mappers.getMapper(OAuthCredentialsInternalMapper.class);

	/**
	 * Wandelt externe OAuth-Zugangsdaten in das interne Modell um.
	 *
	 * @param input externe OAuth-Zugangsdaten
	 *
	 * @return internes Zugangsdaten-Modell
	 */
	@Mapping(target = "authServerUrl", source = "tokenUrl", qualifiedByName = "toUri")
	@Mapping(target = "serviceDomain", source = "domain", qualifiedByName = "mapToDomain")
	@Mapping(target = "id", ignore = true)
	Credentials toInternal(OAuthCreateCredential input);

	/**
	 * Wandelt das interne Modell in externe OAuth-Zugangsdaten um.
	 *
	 * @param input internes Zugangsdaten-Modell
	 *
	 * @return externe OAuth-Zugangsdaten
	 */
	@Mapping(target = "tokenUrl", source = "authServerUrl", qualifiedByName = "fromUri")
	@Mapping(target = "domain", source = "serviceDomain", qualifiedByName = "mapFromDomain")
	@Mapping(target = "clientSecret", constant = "*******")
	OAuthCredentials fromInternal(Credentials input);

	/**
	 * Mappt den Namen zu {@link OAuthDomain}
	 *
	 * @param name der Name des {@link OAuthDomain}
	 *
	 * @return {@link OAuthDomain}
	 */
	@Named("mapToDomain")
	default OAuthDomain mapToDomain(final String name) {
		return OAuthDomain.valueOf(name);
	}

	/**
	 * Mappt {@link OAuthDomain} zu dessen Namen
	 *
	 * @param input {@link OAuthDomain}
	 *
	 * @return der Name des {@link OAuthDomain}
	 */
	@Named("mapFromDomain")
	default String mapFromDomain(final OAuthDomain input) {
		return input.name();
	}

}
