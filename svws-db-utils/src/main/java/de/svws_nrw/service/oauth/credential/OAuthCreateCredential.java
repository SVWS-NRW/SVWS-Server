package de.svws_nrw.service.oauth.credential;

import de.svws_nrw.validation.constraints.ValidUrl;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Die Daten zum Anlegen neuer OAuth-Zugangsdaten.
 */
public final class OAuthCreateCredential {
	/** Die ID des Clients */
	@NotBlank
	public String clientId;

	/** Das Client Secret */
	@NotBlank
	public String clientSecret;

	/** Die URL des Auth Servers */
	@NotBlank
	@ValidUrl
	public String tokenUrl;

	/** Das Scope das Requested wird */
	public String requestedScope;

	/**
	 * Die Domäne der Credentials
	 */
	public @NotNull String domain;

}
