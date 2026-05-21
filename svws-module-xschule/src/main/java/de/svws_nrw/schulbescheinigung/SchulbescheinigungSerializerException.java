package de.svws_nrw.schulbescheinigung;

public final class SchulbescheinigungSerializerException extends RuntimeException {

	/**
	 * Erstellt eine neue {@code SchulbescheinigungSerializerException}.
	 *
	 * @param message die Fehlermeldung
	 * @param cause   die ursprüngliche Ursache
	 */
	public SchulbescheinigungSerializerException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
