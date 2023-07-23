package de.svws_nrw.data.schema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

import de.svws_nrw.base.FileUtils;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
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

	private static final Random random = new Random();

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
    	final String tmpDirectory = SVWSKonfiguration.get().getTempPath();
        final String tmpFilename = schemaname +  "_" + random.ints(48, 123)  // from 0 to z
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))  // filter some unicode characters
          .limit(40)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString() + ".sqlite";
        logger.logLn("Erstelle eine SQLite-Datenbank unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\"");

		// Erzeuge einen Schema-Manager, der den Export des DB-Schema durchführt
		final DBSchemaManager srcManager = DBSchemaManager.create(conn.getUser(), true, logger);
		if (srcManager == null)
			throw new WebApplicationException(Status.FORBIDDEN.getStatusCode());

		// Führe den Export mithilfe des Schema-Managers durch.
		logger.modifyIndent(2);
		srcManager.backup.exportDB(tmpDirectory + "/" + tmpFilename, logger);
		logger.modifyIndent(-2);

        // Lese die Datenbank in die Response ein
		logger.logLn("Lese die temporären SQLite-Datenbank unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\" ein.");
		final Response response = Response.ok((StreamingOutput) output -> {
			try {
				FileUtils.move(tmpDirectory + "/" + tmpFilename, output);
				output.flush();
			} catch (final Exception e) {
				e.printStackTrace();
			}
        }).header("Content-Disposition", "attachment; filename=\"" + schemaname + ".sqlite\"").build();
		if (!response.hasEntity())
			logger.logLn(2, "[FEHLER]");
		logger.logLn("Datei eingelesen.");

		return response;
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
    	final String tmpDirectory = SVWSKonfiguration.get().getTempPath();
        final String tmpFilename = conn.getDBSchema() +  "_" + random.ints(48, 123)  // from 0 to z
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))  // filter some unicode characters
          .limit(40)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString() + ".sqlite";
        logger.logLn("Erstelle eine SQLite-Datenbank unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\"");
    	try {
    		Files.createDirectories(Paths.get(tmpDirectory));
			Files.write(Paths.get(tmpDirectory + "/" + tmpFilename), srcDB, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
		} catch (@SuppressWarnings("unused") final IOException e) {
			logger.logLn(2, "Fehler beim Erstellen der temporären SQLite-Datenbank unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\"");
			return OperationError.INTERNAL_SERVER_ERROR.getResponse(simpleResponse(false, log));
		}

    	logger.logLn("Importiere in die " + conn.getDBDriver() + "-Datenbank unter " + conn.getDBLocation() + ":");
    	logger.logLn(2, "- verwende den Admin-Benutzer: " + conn.getUser().getUsername());
    	logger.logLn(2, "- verwende das vorhandene DB-Schema: " + conn.getDBSchema());

    	// Erstelle die Quell-DB-Konfiguration für die übergebene Datei
		final DBConfig srcConfig = new DBConfig(DBDriver.SQLITE, tmpDirectory + "/" + tmpFilename, null, false, null, null, true, false, 0, 0);

		// Bestimme die Zielkonfiguration aus der SWVS-Konfiguration
		final DBConfig tgtConfig = SVWSKonfiguration.get().getDBConfig(conn.getDBSchema());
		if (tgtConfig == null) {
			logger.logLn(LogLevel.ERROR, 2, "Fehler bei der Migration - Ziel-Schema nicht in der Server-Konfiguration gefunden (schema='" + conn.getDBSchema() + "')");
			return OperationError.INTERNAL_SERVER_ERROR.getResponse(simpleResponse(false, log));
		}

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
		} catch (@SuppressWarnings("unused") final DBException e) {
			return OperationError.INTERNAL_SERVER_ERROR.getResponse(simpleResponse(false, log));
		}

		// Entferne die temporär angelegte Datenbank wieder...
		logger.logLn("Löschen der temporären SQLite-Datenbank unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\".");
		try {
			Files.delete(Paths.get(tmpDirectory + "/" + tmpFilename));
		} catch (@SuppressWarnings("unused") final IOException e) {
			logger.logLn(2, "[FEHLER]");
		}

		logger.logLn("Import abgeschlossen.");
		final SimpleOperationResponse daten = simpleResponse(true, log);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
    }

}
