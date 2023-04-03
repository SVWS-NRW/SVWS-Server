package de.svws_nrw.base.crypto;

/**
 * Eine Exception, die beim Ver- oder Entschlüsseln von AES-Daten
 * geworfen wird und den Grund für diese Exception als cause beinhaltet.
 */
public class AESException extends Exception {

	private static final long serialVersionUID = -1010497272927648604L;

	/**
	 * Erzeugt eine neue AES-Exception mit der angegebenen Nachricht
	 * und dem angegebenen Grund.
	 *
	 * @param message   die Fehlernachricht
	 * @param cause     der Grund für die Exception
	 */
	public AESException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
