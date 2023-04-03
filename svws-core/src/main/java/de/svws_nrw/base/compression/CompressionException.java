package de.svws_nrw.base.compression;

/**
 * Eine Exception, die beim Komprimieren oder Dekomprimieren von Daten
 * geworfen wird und den Grund für diese Exception als cause beinhaltet.
 */
public class CompressionException extends Exception {

	private static final long serialVersionUID = 4561281049023715781L;

	/**
	 * Erzeugt eine neue CompressionException mit der angegebenen Nachricht
	 * und dem angegebenen Grund.
	 *
	 * @param message   die Fehlernachricht
	 * @param cause     der Grund für die Exception
	 */
	public CompressionException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
