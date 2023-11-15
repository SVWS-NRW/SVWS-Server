package de.svws_nrw.data.schema;

import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.data.BenutzerKennwort;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;

/**
 * Diese Klasse stellt Methoden für den Zugriff auf ein Schema für die
 * SVWS-DB zur Verfügung.
 */
public final class DBUtilsSchema {

	private DBUtilsSchema() {
		throw new IllegalStateException("Instantiation of " + DBUtilsSchema.class.getName() + " not allowed");
	}

	/**
	 * Prüft, ob die übergebenen Credentials den Zugriff auf die Schema-Informationen
	 * der Datenbank zulassen.
	 *
	 * @param credentials   die Benutzer-Credentials
	 *
	 * @return true, falls die Credentials den Zugriff zulassen
	 */
	public static boolean checkDBPassword(final BenutzerKennwort credentials) {
		if (credentials == null)
			return false;
		DBConfig dbconfig = SVWSKonfiguration.get().getRootDBConfig(credentials.user, credentials.password);
		switch (dbconfig.getDBDriver()) {
			case MYSQL, MARIA_DB:
				dbconfig = dbconfig.switchSchema("information_schema");
				break;
			case MDB:
				dbconfig = dbconfig.switchSchema("PUBLIC");
				break;
			case MSSQL:
				dbconfig = dbconfig.switchSchema("master");
				break;
			case SQLITE:
				dbconfig = dbconfig.switchSchema("");
				break;
		}
		final Benutzer user;
		try {
			user = Benutzer.create(dbconfig);
		} catch (@SuppressWarnings("unused") final DBException db) {
			return false;
		}
		try (DBEntityManager em = user.getEntityManager()) {
			return (em != null);
		} catch (@SuppressWarnings("unused") final Exception pe) {
			return false;
		}
	}

}
