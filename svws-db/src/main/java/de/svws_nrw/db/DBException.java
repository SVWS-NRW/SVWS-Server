package de.svws_nrw.db;

/**
 * Diese Exception dient der Rückmeldung von Fehlern, die bei
 * Datenbank-Operationen auftreten können.
 */
public class DBException extends Exception {

	private static final long serialVersionUID = -8074736305905403042L;

	/**
	 * Erzeugt eine {@link DBException} mit der angegebenen textuellen Beschreibung.
	 *
	 * @param message   die textuelle Beschreiung zu der Exception
	 */
	public DBException(final String message) {
		super(message);
	}

}
