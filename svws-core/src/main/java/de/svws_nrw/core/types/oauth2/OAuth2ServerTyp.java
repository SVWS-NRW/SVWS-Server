package de.svws_nrw.core.types.oauth2;

import de.svws_nrw.core.data.oauth2.OAuth2ClientSecret;

/**
 * Eine Liste der möglichen OAuth2-Servertypen, für welche die {@link OAuth2ClientSecret}
 * in der Datenbank hinterlegt werden können.
 */
public enum OAuth2ServerTyp {

	/** Web Noten Manager */
	WENOM(1),

	/** Schüler Online */
	SCHUELER_ONLINE(2);


	/** Die ID des Server-Typs */
	private final long id;


	/**
	 * Erstellt einen neuen OAuth2-Servertyp
	 *
	 * @param id   die ID des OAuth2-Server-Typs
	 */
	OAuth2ServerTyp(final long id) {
		this.id = id;
	}


	/**
	 * Gibt die ID dieses OAuth Servers wieder
	 *
	 * @return die ID
	 */
	public long getId() {
		return this.id;
	}


	/**
	 * Diese Methode ermittelt den OAuth2-Servertyp anhand der übergebenen ID.
	 *
	 * @param id   die ID des OAuth2-Servertyps
	 *
	 * @return der OAuth2-Servertyp
	 */
	public static OAuth2ServerTyp getByID(final long id) {
		for (final OAuth2ServerTyp s : OAuth2ServerTyp.values())
			if (s.id == id)
				return s;
		return null;
	}

}
