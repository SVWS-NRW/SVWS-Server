package de.svws_nrw.asd.validate;

/**
 * Diese Exception wird verwendet, wenn bei Verwendung der Validatoren Fehler auftreten.
 */
public class ValidatorException extends RuntimeException {

	private static final long serialVersionUID = 4841861661278456913L;

	/**
	 * Erstellt eine neue Validator-Exception
	 */
	public ValidatorException() {
		super();
	}

	/**
	 * Erstellt eine neue Validator-Exception mit der übergebenen Nachricht.
	 *
	 * @param message   die Nachricht der Exception (siehe {@link #getMessage()})
	 */
	public ValidatorException(final String message) {
		super(message);
	}

	/**
	 * Erstellt eine neue Validator-Exception mit der übergebenen Nachricht und
	 * dem übergebenen Grund.
	 *
	 * @param message   die Nachricht der Exception (siehe {@link #getMessage()})
	 * @param cause     der Grund für die Exception oder null
	 */
	public ValidatorException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Erstellt eine neue Validator-Exception mit dem übergebenen Grund.
	 *
	 * @param cause   der Grund für die Exception oder null
	 */
	public ValidatorException(final Throwable cause) {
		super(cause);
	}

}
