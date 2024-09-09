package de.svws_nrw.asd.data;

/**
 * Diese Exception wird verwendet, wenn bei dem Zugriff auf Core-Types
 * Fehler auftreten.
 */
public class CoreTypeException extends RuntimeException {

	private static final long serialVersionUID = 4271506360574149190L;

    /**
     * Erstellt eine neue Core-Type-Exception
     */
    public CoreTypeException() {
        super();
    }

    /**
     * Erstellt eine neue Core-Type-Exception mit der übergebenen Nachricht.
     *
     * @param message   die Nachricht der Exception (siehe {@link #getMessage()})
     */
    public CoreTypeException(final String message) {
        super(message);
    }

    /**
     * Erstellt eine neue Core-Type-Exception mit der übergebenen Nachricht und
     * dem übergebenen Grund.
     *
     * @param message   die Nachricht der Exception (siehe {@link #getMessage()})
     * @param cause     der Grund für die Exception oder null
     */
    public CoreTypeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Erstellt eine neue Core-Type-Exception mit dem übergebenen Grund.
     *
     * @param cause     der Grund für die Exception oder null
     */
    public CoreTypeException(final Throwable cause) {
        super(cause);
    }

}
