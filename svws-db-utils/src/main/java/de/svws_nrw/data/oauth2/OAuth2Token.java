package de.svws_nrw.data.oauth2;

/**
 * Das DTO für ein OAuth2-Token
 */
public class OAuth2Token {

	/** Der Typ des Tokens */
	public String tokenType;

	/** Die Zeit in Sekunden bis das Token nicht mehr gültig ist ab dem Zeitpunkt der Erstellung des Tokens */
	public long expiresIn;

	/** Das eigentliche Token */
	public String accessToken;

	/** Der Gültigkeitsbereich des Tokens */
	public String scope;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public OAuth2Token() {
		// leer
	}

}
