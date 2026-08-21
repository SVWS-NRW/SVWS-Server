package de.svws_nrw.core.types.oauth2;

/**
 * Eine Liste der möglichen OAuth2-Domains
 */
public enum OAuthServiceDomain {

	/**
	 * Web Noten Manager
	 */
	WENOM("WENOM"),

	/**
	 * Schüler Online
	 */
	SCHUELER_ONLINE("SCHUELER_ONLINE"),
	/**
	 * IT NRW
	 */
	IT_NRW("IT_NRW");


	private final String dbValue;


	/**
	 * Erstellt eine neue OAuth Service Domäne
	 *
	 * @param dbValue der DB-Wert der OAuth-Domäne
	 */
	OAuthServiceDomain(final String dbValue) {
		this.dbValue = dbValue;
	}

	/**
	 * Gibt den DB-Wert des Enums zurück
	 *
	 * @return den DB-Wert
	 */
	public String getDbValue() {
		return this.dbValue;
	}


	/**
	 * Diese Methode ermittelt die OAuth Domäne anhand des übergebenen Database Wertes.
	 *
	 * @param dbValue DB Wert der OAuth Domaine
	 * @return die OAuth Domaine
	 */
	public static OAuthServiceDomain getByDbValue(final String dbValue) {
		return switch (dbValue) {
			case "WENOM" -> OAuthServiceDomain.WENOM;
			case "SCHUELER_ONLINE" -> OAuthServiceDomain.SCHUELER_ONLINE;
			case "IT_NRW" -> OAuthServiceDomain.IT_NRW;
			default -> null;
		};
	}

}

