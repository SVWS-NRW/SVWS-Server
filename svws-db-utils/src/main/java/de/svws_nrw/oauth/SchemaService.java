package de.svws_nrw.oauth;


import de.svws_nrw.db.DBEntityManager;

/**
 * Liefert das aktive DB-Schema des aktuellen Requests.
 *
 * <p>Kapselt den Zugriff auf {@link DBEntityManager#getDBSchema()} und
 * entkoppelt so OAuth-Komponenten von der konkreten Persistenzschicht.
 */
public class SchemaService {

	private final DBEntityManager conn;

	/**
	 * Konstruktor.
	 *
	 * @param conn aktive Datenbankverbindung, aus der das Schema ausgelesen wird
	 */
	public SchemaService(final DBEntityManager conn) {
		this.conn = conn;
	}

	/**
	 * Gibt das aktive DB-Schema der aktuellen Verbindung zurueck.
	 *
	 * @return Schema-Name, nie {@code null}
	 */
	public String getActiveSchema() {
		return conn.getDBSchema();
	}
}
