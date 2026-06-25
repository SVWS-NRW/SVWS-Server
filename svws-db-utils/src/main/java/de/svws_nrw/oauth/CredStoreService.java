package de.svws_nrw.oauth;

import de.svws_nrw.oauth.internal.CredentialStore;
import de.svws_nrw.oauth.internal.Credentials;

/**
 * Service zum Registrieren von OAuth-Credentials im aktiven Schema.
 *
 * <p>Leitet {@link Credentials} an den zentralen {@link de.svws_nrw.oauth.internal.CredentialStore}
 * weiter und reichert sie automatisch mit dem aktuell aktiven Schema an.
 * Aufrufer muessen das Schema nicht kennen.
 */
public class CredStoreService {

	private final CredentialStore credentialStore;

	/**
	 * Konstruktor.
	 *
	 * @param credentialStore Store für Credentials
	 */
	public CredStoreService(final CredentialStore credentialStore) {
		this.credentialStore = credentialStore;
	}

	/**
	 * Liefert die Credentials für das aktive Schema.
	 *
	 * @param schema DB-Schema / Mandant
	 * @return Credentials für das aktive Schema
	 */
	public Credentials getBySchema(final Schema schema) {
		return credentialStore.forSchema(schema);
	}

}
