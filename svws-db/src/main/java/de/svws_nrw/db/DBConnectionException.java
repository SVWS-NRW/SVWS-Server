package de.svws_nrw.db;

/**
 * Diese Exception dient der Rückmeldung von Fehlern, welche bei einer 
 * Datenbankverbindung auftreten.
 */
public class DBConnectionException extends Exception {

	private static final long serialVersionUID = 2785197324548859243L;

	/**
	 * Erzeugt eine {@link DBConnectionException} und bettet den Grund für die 
	 * Exception in diese ein.
	 * 
	 * @param e   der Grund für diese Exception
	 */
	public DBConnectionException(Exception e) {
		super("Datenbank-Verbindung kann nicht hergestellt werden.", e);
	}

}
