package de.svws_nrw.data.schema;

import java.io.IOException;

import de.svws_nrw.config.LogConsumerLogfile;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.config.SVWSKonfigurationException;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schema.DatenbankVerbindungsdaten;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.PersistenceUnits;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.db.utils.schema.DBMigrationManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse stellt Methoden für die Migration von Schild2-Datenbanken
 * zur Verfügung.
 */
public final class DataMigration {

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
	 * Migriert eine Access-MDB-Datenbank in das Schema der übergebenen Datenbank-
	 * Verbindung
	 *
	 * @param conn            die Datenbank-Verbindung zum Ziel-Schema
	 * @param srcDB           die MDB-Quell-Datenbank
	 *
	 * @return die HTTP-Response mit dem LOG der Migration
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static Response migrateMDB(final DBEntityManager conn, final byte[] srcDB) throws ApiOperationException {
		final Logger logger = new Logger();
		logger.copyConsumer(Logger.global());
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		try {
			if (SVWSKonfiguration.get().isLoggingEnabled())
				logger.addConsumer(new LogConsumerLogfile("svws_schema_" + conn.getDBSchema() + ".log", true, true));
		} catch (final IOException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen einer Log-Datei für das Schema");
		}

		// Erstelle temporär eine MDB-Datei aus dem übergebenen Byte-Array
		try (APITempDBFile mdb = new APITempDBFile(DBDriver.MDB, conn.getDBSchema(), logger, log, srcDB, true)) {
			logger.logLn("Migriere in die " + conn.getDBDriver() + "-Datenbank unter " + conn.getDBLocation() + ":");
			logger.logLn(2, "- verwende den Admin-Benutzer: " + conn.getUser().getUsername());
			logger.logLn(2, "- verwende das vorhandene DB-Schema: " + conn.getDBSchema());

			// Erstelle die Quell-DB-Konfiguration für die übergebene Datei
			final DBConfig srcConfig = mdb.getConfig();

			// Bestimme die Zielkonfiguration aus der SWVS-Konfiguration
			DBConfig tgtConfig = SVWSKonfiguration.get().getDBConfig(conn.getDBSchema());
			final boolean hatSchemaConfig = (tgtConfig != null);
			// Falls das Schema in der SVWS-Konfiguration nicht als SVWS-Schema angelegt wurde, dann verwende die Informationen aus der aktuellen Datenbank-Verbindung.
			if (tgtConfig == null)
				tgtConfig = SVWSKonfiguration.get().getRootDBConfig(conn.getUser().getUsername(), conn.getUser().getPassword())
						.switchSchema(PersistenceUnits.SVWS_DB, conn.getDBSchema());

			// Führe die Migration durch
			if (!DBMigrationManager.migrateInto(srcConfig, tgtConfig, -1, false, null, logger)) {
				logger.logLn(LogLevel.ERROR, 2, "Fehler bei der Migration (driver='" + tgtConfig.getDBDriver() + "', location='" + tgtConfig.getDBLocation()
						+ "', user='" + tgtConfig.getUsername() + "')");
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, simpleResponse(false, log));
			}

			// Schreibe die Verbindungsinformation für das neu angelegte SVWS-Schema in die SVWS-Konfiguration
			try {
				if (!hatSchemaConfig)
					SVWSKonfiguration.get().createOrUpdateSchema(conn.getDBSchema(), conn.getUser().getUsername(), conn.getUser().getPassword(), false);
			} catch (final SVWSKonfigurationException e) {
				logger.logLn(LogLevel.ERROR, 2, "Fehler bei dem Erstellen bzw. Anpassen der SVWS-Konfiguration (" + e.getMessage() + ")");
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, simpleResponse(false, log));
			}
		}
		logger.logLn("Migration abgeschlossen.");
		final SimpleOperationResponse daten = simpleResponse(true, log);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}



	/**
	 * Migriert eine Datenbank aus dem angegebenen DBMS in das Schema der übergebenen Datenbank-Verbindung
	 *
	 * @param conn               die Datenbank-Verbindung zum Ziel-Schema
	 * @param srcDBDriver        das DBMS der Quell-Datenbank (nicht MDB oder SQLITE)
	 * @param verbindungsdaten   die Verbindungsdaten für den Zugriff auf die Quell-Datenbank
	 * @param schulnummer        die Schulnummer, für die die Migration durchgeführt wird oder null, falls keine Filterung bezüglich
	 *                           der Schulnummer erfolgen soll
	 *
	 * @return die HTTP-Response mit dem LOG der Migration
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static Response migrateDBMS(final DBEntityManager conn, final DBDriver srcDBDriver, final DatenbankVerbindungsdaten verbindungsdaten,
			final Integer schulnummer) throws ApiOperationException {
		final Logger logger = new Logger();
		logger.copyConsumer(Logger.global());
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		try {
			if (SVWSKonfiguration.get().isLoggingEnabled())
				logger.addConsumer(new LogConsumerLogfile("svws_schema_" + conn.getDBSchema() + ".log", true, true));
		} catch (final IOException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen einer Log-Datei für das Schema");
		}

		if ((srcDBDriver == null) || (srcDBDriver == DBDriver.MDB) || (srcDBDriver == DBDriver.SQLITE)) {
			logger.logLn("Eine Migration aus dem angegebenen Datenbankformat '" + srcDBDriver + "' wird über diese Schnittstelle nicht unterstützt.");
			final SimpleOperationResponse daten = simpleResponse(false, log);
			return Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(daten).build();
		}

		final DBConfig srcConfig = new DBConfig(PersistenceUnits.SVWS_DB, srcDBDriver, verbindungsdaten.location, verbindungsdaten.schema, false,
				verbindungsdaten.username, verbindungsdaten.password, true, false);
		return migrateInto(conn, srcConfig, schulnummer);
	}


	/**
	 * Führt eine Migration in das Ziel-Schema der übergebenen Datenbank-Verbindung mit den übergebenen Migrations-Informationen durch.
	 *
	 * @param conn               die Datenbank-Verbindung zum Ziel-Schema
	 * @param srcConfig          die Konfiguration für die Quell-Datenbank
	 * @param schulnummer        die Schulnummer, für die die Migration durchgeführt wird oder null, falls keine Filterung bezüglich
	 *                           der Schulnummer erfolgen soll
	 *
	 * @return die HTTP-Response mit dem LOG der Migration
	 */
	private static Response migrateInto(final DBEntityManager conn, final DBConfig srcConfig, final Integer schulnummer) {
		final Logger logger = new Logger();
		logger.copyConsumer(Logger.global());
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);

		// Prüfe das angegebene Datenbanksystem für die Quelldatenbank
		if ((srcConfig.getDBDriver() == null) || (srcConfig.getDBDriver() == DBDriver.MDB) || (srcConfig.getDBDriver() == DBDriver.SQLITE)) {
			logger.logLn("Eine Migration aus dem angegebenen Datenbankformat '" + srcConfig.getDBDriver()
					+ "' wird über diese Schnittstelle nicht unterstützt.");
			final SimpleOperationResponse daten = simpleResponse(false, log);
			return Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(daten).build();
		}
		logger.logLn("Es wird aus dem Datenbankformat '" + srcConfig.getDBDriver() + "' migriert.");

		logger.logLn("Migriere in die " + conn.getDBDriver() + "-Datenbank unter " + conn.getDBLocation() + ":");
		logger.logLn(2, "- verwende den Admin-Benutzer: " + conn.getUser().getUsername());
		logger.logLn(2, "- verwende das vorhandene DB-Schema: " + conn.getDBSchema());

		// Bestimme die Zielkonfiguration aus der SWVS-Konfiguration
		DBConfig tgtConfig = SVWSKonfiguration.get().getDBConfig(conn.getDBSchema());
		final boolean hatSchemaConfig = (tgtConfig != null);
		// Falls das Schema ist in der SVWS-Konfiguration nicht als SVWS-Schema angelegt wurde, dann verwende die Informationsn aus der aktuellen Datenbank-Verbindung.
		if (tgtConfig == null)
			tgtConfig = SVWSKonfiguration.get().getRootDBConfig(conn.getUser().getUsername(), conn.getUser().getPassword())
					.switchSchema(PersistenceUnits.SVWS_ROOT, conn.getDBSchema());

		// Führe die Migration durch
		if (!DBMigrationManager.migrateInto(srcConfig, tgtConfig, -1, false, schulnummer, logger)) {
			logger.logLn(LogLevel.ERROR, 2, "Fehler bei der Migration (driver='" + tgtConfig.getDBDriver() + "', location='" + tgtConfig.getDBLocation()
					+ "', user='" + tgtConfig.getUsername() + "')");
			final SimpleOperationResponse daten = simpleResponse(false, log);
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity(daten).build();
		}

		// Schreibe die Verbindungsinformation für das neu angelegte SVWS-Schema in die SVWS-Konfiguration
		try {
			if (!hatSchemaConfig)
				SVWSKonfiguration.get().createOrUpdateSchema(conn.getDBSchema(), conn.getUser().getUsername(), conn.getUser().getPassword(), false);
		} catch (final SVWSKonfigurationException e) {
			logger.logLn(LogLevel.ERROR, 2, "Fehler bei dem Erstellen bzw. Anpassen der SVWS-Konfiguration (" + e.getMessage() + ")");
			final SimpleOperationResponse daten = simpleResponse(false, log);
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity(daten).build();
		}

		logger.logLn("Migration abgeschlossen.");
		final SimpleOperationResponse daten = simpleResponse(true, log);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

}
