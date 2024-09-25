package de.svws_nrw.db;

/**
 * Eine Aufzählung der konfigurierten Persistence-Untis für den Zugriff auf die Datenbank-Schemata.
 */
public enum PersistenceUnits {

	/** Die Persistence-Unit für den Zugriff die Information-Schemata (z.B. beim "root"-Zugriff) */
	SVWS_ROOT,

	/** Die Persistence-Unit für den normalen Zugriff auf die SVWS-DB */
	SVWS_DB;

}
