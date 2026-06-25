package de.svws_nrw.core.data.oauth2;

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

	/** CLient ID */
	public String clientId;

	/** Client Secret */
	public String clientSecret;

	/** tokenUrl */
	public String tokenUrl;

	/** defaultScope */
	public String defaultScope;

}
