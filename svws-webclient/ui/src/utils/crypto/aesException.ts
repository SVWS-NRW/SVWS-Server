export class AESException extends Error {
	/**
	 * Erzeugt eine neue AES-Exception mit der angegebenen Nachricht
	 * und dem angegebenen Grund.
	 *
	 * @param message   die Fehlernachricht
	 * @param cause     der Grund für die Exception
	 */
	public constructor(message: string, cause: Error) {
		super(message, cause);
	}

}
