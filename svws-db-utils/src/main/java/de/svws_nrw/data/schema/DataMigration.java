package de.svws_nrw.data.schema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.db.utils.schema.DBMigrationManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse stellt Methoden für die Migration von Schild2-Datenbanken
 * zur Verfügung.
 */
public final class DataMigration {

	private static final Random random = new Random();

	private DataMigration() {
		throw new IllegalStateException("Instantiation of " + DataMigration.class.getName() + " not allowed");
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
	 * Migriert eine Access-MDB-Datenbank in das angegeben Schema der übergebenen Datenbank-
	 * Verbindung
	 *
	 * @param conn            die Datenbank-Verbindung zum Ziel-Schema
	 * @param srcDB           die MDB-Quell-Datenbank
	 * @param srcDBPassword   das Kennwort für den Zugriff auf die Quell-Datenbank
	 *
	 * @return die HTTP-Response mit dem LOG der Migration
	 */
    public static Response migrateMDB(final DBEntityManager conn, final byte[] srcDB, final String srcDBPassword) {
    	final Logger logger = new Logger();
    	final LogConsumerList log = new LogConsumerList();
    	logger.addConsumer(log);
    	logger.addConsumer(new LogConsumerConsole());

    	// Erstelle temporär eine MDB-Datei aus dem übergebenen Byte-Array
    	final String mdbdirectory = SVWSKonfiguration.get().getTempPath();
        final String mdbFilename = conn.getDBSchema() +  "_" + random.ints(48, 123)  // from 0 to z
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))  // filter some unicode characters
          .limit(40)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString() + ".mdb";
        logger.logLn("Erstelle eine temporäre Access-Datenbank unter dem Namen \"" + mdbdirectory + "/" + mdbFilename + "\"");
    	try {
    		Files.createDirectories(Paths.get(mdbdirectory));
			Files.write(Paths.get(mdbdirectory + "/" + mdbFilename), srcDB, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
		} catch (@SuppressWarnings("unused") final IOException e) {
			logger.logLn(2, "Fehler beim Erstellen der temporären Access-Datenbank unter dem Namen \"" + mdbdirectory + "/" + mdbFilename + "\"");
			throw OperationError.INTERNAL_SERVER_ERROR.exception(simpleResponse(false, log));
		}

    	logger.logLn("Migriere in die " + conn.getDBDriver() + "-Datenbank unter " + conn.getDBLocation() + ":");
    	logger.logLn(2, "- verwende den Admin-Benutzer: " + conn.getUser().getUsername());
    	logger.logLn(2, "- verwende das vorhandene DB-Schema: " + conn.getDBSchema());

    	// Erstelle die Quell-DB-Konfiguration für die übergebene Datei
		final DBConfig srcConfig = new DBConfig(DBDriver.MDB, mdbdirectory + "/" + mdbFilename, "PUBLIC", false, "admin", srcDBPassword, true, false);

		// Bestimme die Zielkonfiguration aus der SWVS-Konfiguration
		final DBConfig tgtConfig = SVWSKonfiguration.get().getDBConfig(conn.getDBSchema());
		if (tgtConfig == null) {
			logger.logLn(LogLevel.ERROR, 2, "Fehler bei der Migration - Ziel-Schema nicht in der Server-Konfiguration gefunden (schema='" + conn.getDBSchema() + "')");
			throw OperationError.INTERNAL_SERVER_ERROR.exception(simpleResponse(false, log));
		}

		// Führe die Migration durch
		if (!DBMigrationManager.migrateInto(srcConfig, tgtConfig, -1, false, null, logger)) {
			logger.logLn(LogLevel.ERROR, 2, "Fehler bei der Migration (driver='" + tgtConfig.getDBDriver() + "', location='" + tgtConfig.getDBLocation() + "', user='" + tgtConfig.getUsername() + "')");
			throw OperationError.INTERNAL_SERVER_ERROR.exception(simpleResponse(false, log));
		}

		// Entferne die temporär angelegte Datenbank wieder...
		logger.logLn("Löschen der temporären Access-Datenbank unter dem Namen \"" + mdbdirectory + "/" + mdbFilename + "\".");
		try {
			Files.delete(Paths.get(mdbdirectory + "/" + mdbFilename));
		} catch (@SuppressWarnings("unused") final IOException e) {
			logger.logLn(2, "[FEHLER]");
		}

		logger.logLn("Migration abgeschlossen.");
		final SimpleOperationResponse daten = simpleResponse(true, log);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
    }

}
