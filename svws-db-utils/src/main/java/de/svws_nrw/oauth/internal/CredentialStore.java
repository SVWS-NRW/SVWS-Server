package de.svws_nrw.oauth.internal;

import de.svws_nrw.oauth.Schema;

/**
 * Store fuer schema-spezifische OAuth-Credentials.
 *
 * <p>Definiert die Naht zwischen Token-Verwaltung und Credential-Beschaffung.
 * Die konkrete Implementierung ist austauschbar (z.B. In-Memory, JDBC, Vault).
 */
public interface CredentialStore {

	/**
	 * Liefert die Credentials fuer ein Schema.
	 *
	 * @param schema DB-Schema / Mandant
	 * @return Credentials fuer das Schema
	 * @throws UnknownSchemaException wenn fuer das Schema keine Credentials hinterlegt sind
	 */
	Credentials forSchema(Schema schema);

	/**
	 * Wird geworfen wenn keine Credentials fuer ein Schema registriert sind.
	 */
	final class UnknownSchemaException extends RuntimeException {
		/**
		 * Konstruktor
		 * @param schema {@link Schema}
		 */
		public UnknownSchemaException(final Schema schema) {
			super(String.format("No credentials registered for schema: %s", ((schema == null) ? "<null>" : schema.name())));
		}
	}
}
