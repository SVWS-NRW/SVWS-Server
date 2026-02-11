package de.svws_nrw.repo;

/**
 * Eine Exception, welche unerwartete Fehler beim Zugriff auf die Datenbank
 * weiterleitet.
 */
public class RepositoryException extends RuntimeException {

	private static final long serialVersionUID = -3812760183504555516L;

	/**
	 * Erstellt eine neue Repository-Exception mit der angegebenen Nachricht
	 * und dem angegeben Grund.
	 *
	 * @param message   die Nachricht
	 * @param cause     der Grund für diese Exception
	 */
	public RepositoryException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Erstellt eine neue Repository-Exception mit der angegebenen Nachricht.
	 *
	 * @param message   die Nachricht
	 */
	public RepositoryException(final String message) {
		super(message);
	}

}
