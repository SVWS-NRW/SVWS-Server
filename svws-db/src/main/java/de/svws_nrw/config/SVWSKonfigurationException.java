package de.svws_nrw.config;

/**
 * Diese Exception tritt im Zusammenhang mit dem Zugriff auf die SVWS-Konfiguration auf.
 * Die Message beinhaltet die jeweilige Fehlermeldung.
 */
public class SVWSKonfigurationException extends Exception {

	private static final long serialVersionUID = -799188619361035581L;

	/**
	 * Erstellt eine neue Exception mit der übergebenen Nachricht und dem übergebenen
	 * Grund.
	 *
	 * @param message   die Nachricht
	 * @param cause     der Grund für die Exception
	 */
	public SVWSKonfigurationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Erstellt eine neue Exception mit der übergebenen Nachricht.
	 *
	 * @param message   die Nachricht
	 */
	public SVWSKonfigurationException(final String message) {
		super(message);
	}

}
