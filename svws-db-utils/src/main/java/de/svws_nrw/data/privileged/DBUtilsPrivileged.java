package de.svws_nrw.data.privileged;

import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.data.BenutzerKennwort;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBEntityManager;

/**
 * Diese Klasse stellt Methoden für den priviligierten Zugriff auf die SVWS-DB
 * zur Verfügung.
 */
public final class DBUtilsPrivileged {

	private DBUtilsPrivileged() {
		throw new IllegalStateException("Instantiation of " + DBUtilsPrivileged.class.getName() + " not allowed");
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
		final Benutzer user = Benutzer.create(dbconfig);
		try (DBEntityManager em = user.getEntityManager()) {
			return (em != null);
		}
	}

}
