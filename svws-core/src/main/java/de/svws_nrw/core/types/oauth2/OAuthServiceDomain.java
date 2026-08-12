package de.svws_nrw.core.types.oauth2;

/**
 * Eine Liste der möglichen OAuth2-Domains
 */
public enum OAuthServiceDomain {

	/** Web Noten Manager */
	WENOM(1, "WENOM"),

	/** Schüler Online */
	SCHUELER_ONLINE(2, "SCHUELER_ONLINE"),;


	private final long id;

	private final String dbValue;


	/**
	 * Erstellt eine neue OAuth Service Domäne
	 *
	 * @param id   die ID des OAuth-Domäne
	 * @param dbValue der DB-Wert der OAuth-Domäne
	 */
	OAuthServiceDomain(final long id, final String dbValue) {
		this.id = id;
		this.dbValue = dbValue;
	}


	/**
	 * Gibt die ID der OAuth Domäne wieder
	 *
	 * @return die ID
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * Gibt den DB-Wert des Enums zurück
	 * @return den DB-Wert
	 */
	public String getDbValue() {
		return this.dbValue;
	}


	/**
	 * Diese Methode ermittelt den OAuth-Domäne anhand der übergebenen ID.
	 *
	 * @param id   die ID des OAuth-Domäne
	 *
	 * @return die OAuth Domaine
	 */
	public static OAuthServiceDomain getByID(final long id) {
		for (final OAuthServiceDomain s : OAuthServiceDomain.values()) {
			if (s.id == id) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Diese Methode ermittelt die OAuth Domäne anhand des übergebenen Database Wertes.
	 *
	 * @param dbValue   DB Wert der OAuth Domaine
	 *
	 * @return die OAuth Domaine
	 */
	public static OAuthServiceDomain getByDbValue(final String dbValue) {
		return switch (dbValue) {
			case "WENOM" -> OAuthServiceDomain.WENOM;
			case "SCHUELER_ONLINE" -> OAuthServiceDomain.SCHUELER_ONLINE;
			default -> null;
		};
	}

}

