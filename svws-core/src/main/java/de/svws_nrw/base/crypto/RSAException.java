package de.svws_nrw.base.crypto;

/**
 * Eine Exception, die beim Erzeugen eines RSA-Schlüsselpaares, oder beim Ver- bzw.
 * Entschlüsseln von RSA-Daten geworfen wird und den Grund für diese Exception
 * als cause beinhaltet.
 */
public class RSAException extends Exception {

	private static final long serialVersionUID = -1010497272927648604L;

	/**
	 * Erzeugt eine neue RSA-Exception mit der angegebenen Nachricht.
	 *
	 * @param message   die Fehlernachricht
	 */
	public RSAException(final String message) {
		super(message);
	}

	/**
	 * Erzeugt eine neue RSA-Exception mit der angegebenen Nachricht
	 * und dem angegebenen Grund.
	 *
	 * @param message   die Fehlernachricht
	 * @param cause     der Grund für die Exception
	 */
	public RSAException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
