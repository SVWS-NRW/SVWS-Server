package de.svws_nrw.mapper.oauth;

import de.svws_nrw.db.dto.current.svws.auth.DTOSchuleOAuthSecrets;
import de.svws_nrw.mapper.UriMapper;
import de.svws_nrw.oauth.internal.Credentials;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper für OAuth-Zugangsdaten.
 */
@Mapper(uses = UriMapper.class)
public interface OAuthCredentialMapper {

	/**
	 * Singleton Instanz
	 */
	OAuthCredentialMapper INSTANCE = Mappers.getMapper(OAuthCredentialMapper.class);

	/**
	 * Wandelt Zugangsdaten in eine Datenbankentität um.
	 *
	 * @param credentials OAuth-Zugangsdaten
	 *
	 * @return zugehörige Datenbankentität
	 */
	@Mapping(target = "authServerUrl", source = "authServerUrl", qualifiedByName = "fromUri")
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "tokenScope", ignore = true)
	@Mapping(target = "tokenTimestamp", ignore = true)
	@Mapping(target = "tokenExpiresIn", ignore = true)
	@Mapping(target = "token", ignore = true)
	@Mapping(target = "tlsCertIsTrusted", ignore = true)
	@Mapping(target = "tlsCertIsKnown", ignore = true)
	@Mapping(target = "tlsCert", ignore = true)
	@Mapping(target = "tokenType", ignore = true)
	DTOSchuleOAuthSecrets toDomain(Credentials credentials);

	/**
	 * Wandelt eine Datenbankentität in Zugangsdaten um.
	 *
	 * @param credentials Datenbankentität
	 *
	 * @return zugehörige OAuth-Zugangsdaten
	 */
	@Mapping(target = "authServerUrl", source = "authServerUrl", qualifiedByName = "toUri")
	Credentials fromDomain(DTOSchuleOAuthSecrets credentials);
}
