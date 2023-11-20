package de.svws_nrw.data.schema;

import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.data.BenutzerKennwort;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.utils.schema.DBSchemaManager;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

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



	/**
	 * Aktualisiert das Schema, mit dem der angebenene Benutzer angemeldet ist auf die angegebene Revision.
	 *
	 * @param user       der angemeldete Benutzer mit der zugehörigen Datenbank-Verbindung
	 * @param revision   die Datenbank-Revision, auf die aktualisiert werden soll
	 *
	 * @return der Log der Update-Operation
	 */
    public static LogConsumerList updateSchema(final Benutzer user, final long revision) {
		// Ermittle die Revision, auf die aktualisiert werden soll. Hier wird ggf. eine negative Revision als neueste Revision interpretiert
		final long max_revision = SVWSKonfiguration.get().getServerMode() == ServerMode.STABLE
		        ? SchemaRevisionen.maxRevision.revision
		        : SchemaRevisionen.maxDeveloperRevision.revision;
		long rev = revision;
		if (rev < 0)
			rev = max_revision;
		if (rev > max_revision)
			throw new WebApplicationException(Status.BAD_REQUEST.getStatusCode());

		// Erzeuge einen Logger für das Update
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);

		// Erzeuge einen Schema-Manager, der die Aktualisierung des DB-Schema durchführt
		final DBSchemaManager manager = DBSchemaManager.create(user, true, logger);
		if (manager == null)
			throw new WebApplicationException(Status.FORBIDDEN.getStatusCode());

		// Prüfe, ob das Schema aktuell ist
		if (!manager.updater.isUptodate(rev, false)) {
			// Prüfe, ob die angegebene Revision überhaupt ein Update erlaubt -> wenn nicht, dann liegt ein BAD_REQUEST (400) vor
			if (!manager.updater.isUpdatable(rev, false))
				throw new WebApplicationException(Status.BAD_REQUEST.getStatusCode());

			// Führe die Aktualisierung durch
			final boolean success = manager.updater.update(user, rev, false, true);
			if (!success)
				throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		}

		// return log from logger
		return log;
    }


}
