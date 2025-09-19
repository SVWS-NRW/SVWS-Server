package de.svws_nrw.asd.validate;

/**
 * Diese Exception wird bei Fehlern im {@link DateManager} verwendet und gibt an,
 * dass eine Datumsangabe fehlerhaft ist.
 */
public class InvalidDateException extends Exception {

	private static final long serialVersionUID = 4805266244450065598L;

	/**
	 * Erstellt eine neue Exception
	 */
	public InvalidDateException() {
		super();
	}

	/**
	 * Erstellt eine neue Exception mit der übergebenen Nachricht.
	 *
	 * @param message   die Nachricht der Exception (siehe {@link #getMessage()})
	 */
	public InvalidDateException(final String message) {
		super(message);
	}

	/**
	 * Erstellt eine neue Exception mit der übergebenen Nachricht und
	 * dem übergebenen Grund.
	 *
	 * @param message   die Nachricht der Exception (siehe {@link #getMessage()})
	 * @param cause     der Grund für die Exception oder null
	 */
	public InvalidDateException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Erstellt eine neue Exception mit dem übergebenen Grund.
	 *
	 * @param cause   der Grund für die Exception oder null
	 */
	public InvalidDateException(final Throwable cause) {
		super(cause);
	}

}
