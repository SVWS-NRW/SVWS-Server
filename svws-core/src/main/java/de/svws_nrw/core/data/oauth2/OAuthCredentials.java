package de.svws_nrw.core.data.oauth2;

import de.svws_nrw.core.types.oauth2.OAuthServiceDomain;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * OAuth-Client-Konfiguration fuer ein einzelnes Schema.
 *
 */
@XmlRootElement
@Schema(description = "OAuth Credentials.")
@TranspilerDTO
public final class OAuthCredentials {

	/** Die ID des Datensatzes */
	@Schema(description = "Die id", example = "1")
	public long id;

	/** Die Client ID */
	@Schema(description = "Die client-id der Credentials", example = "test-secret")
	public String clientId;

	/** Das Client Secret */
	@Schema(description = "Das secret der Credentials", example = "test-secret")
	public String clientSecret;

	/** Die URL des Auth Servers */
	@Schema(description = "Die URL des authorization servers", example = "https://xyz.de")
	public String tokenUrl;

	/** Das Scope das Requested wird */
	@Schema(description = "Der requested scope der Credentials", example = "xzy")
	public String requestedScope;

	/**
	 * Die Domäne der Credentials
	 * @see OAuthServiceDomain
	 */
	@Schema(description = "Die Domaine der Credentials", example = "IT_NRW")
	public String domain;
}
