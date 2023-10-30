package de.svws_nrw.data.schema;

import de.svws_nrw.base.FileUtils;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.db.utils.schema.DBSchemaManager;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.StreamingOutput;

/**
 * Diese Klasse stellt Methoden für den Import und Export von SQLite-Datenbanken
 * zur Verfügung.
 */
public final class DataSQLite {

	private DataSQLite() {
		throw new IllegalStateException("Instantiation of " + DataSQLite.class.getName() + " not allowed");
	}

	/**
	 * Erzeugt eine einfache Anwort mit der Angabe, ob die Operation erfolgreich war und
	 * mit dem Log derOperation.
	 *
	 * @param success   gibt an, ob die Operation erfolgreich war oder nicht
	 * @param log       der Log der Operation
	 *
	 * @return das Response-Objekt
	 */
	private static SimpleOperationResponse simpleResponse(final boolean success, final LogConsumerList log) {
		final SimpleOperationResponse response = new SimpleOperationResponse();
		response.success = success;
		response.log = log.getStrings();
		return response;
	}

    /**
     * Exportiert eine SQLite-Datenbank aus dem aktuellen Schema. Der Aufruf erfordert
     * administrative Rechte.
     *
     * @param conn         die Datenbank-Verbindung zu dem aktuellen Schema
     * @param schemaname   Name des Schemas, in das hinein migriert werden soll
     *
     * @return Die SQLite-Datenbank
     */
    public static Response exportSQLite(final DBEntityManager conn, final String schemaname) {
    	final Logger logger = new Logger();
    	final LogConsumerList log = new LogConsumerList();
    	logger.addConsumer(log);
    	logger.addConsumer(new LogConsumerConsole());

    	// Bestimme den Dateinamen für eine temporäre SQLite-Datei
    	try (APITempDBFile sqlite = new APITempDBFile(DBDriver.SQLITE, conn.getDBSchema(), logger, log, null, null, false)) {
			// Erzeuge einen Schema-Manager, der den Export des DB-Schema durchführt
			final DBSchemaManager srcManager = DBSchemaManager.create(conn.getUser(), true, logger);
			if (srcManager == null)
				throw new WebApplicationException(Status.FORBIDDEN.getStatusCode());

			// Führe den Export mithilfe des Schema-Managers durch.
			logger.modifyIndent(2);
			srcManager.backup.exportDB(sqlite.getFilename(), logger);
			logger.modifyIndent(-2);

	        // Lese die Datenbank in die Response ein
			logger.logLn("Lese die temporären SQLite-Datenbank unter dem Namen \"" + sqlite.getFilename() + "\" ein.");
			final Response response = Response.ok((StreamingOutput) output -> {
				try {
					FileUtils.move(sqlite.getFilename(), output);
					output.flush();
				} catch (final Exception e) {
					throw OperationError.INTERNAL_SERVER_ERROR.exception(e);
				}
	        }).header("Content-Disposition", "attachment; filename=\"" + schemaname + ".sqlite\"").build();
			if (!response.hasEntity())
				logger.logLn(2, "[FEHLER]");
			logger.logLn("Datei eingelesen.");
			return response;
    	}
    }


	/**
	 * Import ein Backup der SVWS-Datenbank in das angegebene bereits vorhandene Schema der
	 * übergebenen Datenbank-Verbindung. Dabei geht der usprüngliche Inhalt des Ziel-Schemas
	 * verloren.
	 *
	 * @param conn            die Datenbank-Verbindung zum Ziel-Schema
	 * @param srcDB           die SQLite-Quell-Datenbank
	 *
	 * @return die HTTP-Response mit dem LOG des Imports
	 */
    public static Response importSQLite(final DBEntityManager conn, final byte[] srcDB) {
    	final Logger logger = new Logger();
    	final LogConsumerList log = new LogConsumerList();
    	logger.addConsumer(log);
    	logger.addConsumer(new LogConsumerConsole());

    	// Erstelle temporär eine SQLite-Datei aus dem übergebenen Byte-Array
    	try (APITempDBFile sqlite = new APITempDBFile(DBDriver.SQLITE, conn.getDBSchema(), logger, log, srcDB, null, true)) {
	    	logger.logLn("Importiere in die " + conn.getDBDriver() + "-Datenbank unter " + conn.getDBLocation() + ":");
	    	logger.logLn(2, "- verwende den Admin-Benutzer: " + conn.getUser().getUsername());
	    	logger.logLn(2, "- verwende das vorhandene DB-Schema: " + conn.getDBSchema());

	    	// Erstelle die Quell-DB-Konfiguration für die übergebene Datei
			final DBConfig srcConfig = sqlite.getConfig();

			// Bestimme die Zielkonfiguration aus der SWVS-Konfiguration
			DBConfig tgtConfig = SVWSKonfiguration.get().getDBConfig(conn.getDBSchema());
			final boolean hatSchemaConfig = (tgtConfig != null);
			// Falls das Schema ist in der SVWS-Konfiguration nicht als SVWS-Schema angelegt wurde, dann verwende die Informationsn aus der aktuellen Datenbank-Verbindung.
			if (tgtConfig == null)
				tgtConfig = SVWSKonfiguration.get().getRootDBConfig(conn.getUser().getUsername(), conn.getUser().getPassword()).switchSchema(conn.getDBSchema());

			try {
				final Benutzer srcUser = Benutzer.create(srcConfig);
				try (DBEntityManager srcConn = srcUser.getEntityManager()) {
					if (srcConn == null) {
						logger.logLn(0, " [Fehler]");
						throw new DBException("Fehler beim Verbinden zur SQLite-Export-Datenbank");
					}
					logger.logLn(0, " [OK]");

					final DBSchemaManager srcManager = DBSchemaManager.create(srcUser, true, logger);
					logger.modifyIndent(2);
					if (!srcManager.backup.importDBInto(tgtConfig, -1, false, logger))
						return OperationError.INTERNAL_SERVER_ERROR.getResponse(simpleResponse(false, log));
					logger.modifyIndent(-2);
				}
			} catch (@SuppressWarnings("unused") final DBException e) {
				return OperationError.INTERNAL_SERVER_ERROR.getResponse(simpleResponse(false, log));
			}

			// Schreibe die Verbindungsinformation für das neu angelegte SVWS-Schema in die SVWS-Konfiguration
			if (!hatSchemaConfig)
				SVWSKonfiguration.get().createOrUpdateSchema(conn.getDBSchema(), conn.getUser().getUsername(), conn.getUser().getPassword(), false);
    	}

		logger.logLn("Import abgeschlossen.");
		final SimpleOperationResponse daten = simpleResponse(true, log);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
    }

}
