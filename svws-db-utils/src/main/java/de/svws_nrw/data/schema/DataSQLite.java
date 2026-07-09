package de.svws_nrw.data.schema;

import de.svws_nrw.base.FileUtils;
import de.svws_nrw.config.LogConsumerLogfile;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.config.SVWSKonfigurationException;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.PersistenceUnits;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.db.utils.schema.DBSchemaManager;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.StreamingOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
	 * @param schemaname   Name des Schemas, welches exportiert werden soll
	 *
	 * @return Die SQLite-Datenbank
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static Response exportSQLite(final DBEntityManager conn, final String schemaname) throws ApiOperationException {
		return exportSqliteOrZip(conn, schemaname, "SQLITE-Datei erstellt.",
				(logger, sqlite) -> createSQLiteResponse(logger, sqlite, getExportFilename(schemaname)));
	}


	private static Response createSQLiteResponse(final Logger logger, final APITempDBFile sqlite, final String filename) {
		logger.logLn("Lese die temporären SQLite-Datenbank unter dem Namen \"" + sqlite.getFilename() + "\" ein.");
		return Response.ok((StreamingOutput) output -> {
			try {
				FileUtils.move(sqlite.getFilename(), output);
				output.flush();
			} catch (final Exception e) {
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e);
			}
		}).header("Content-Disposition", "attachment; filename=\"" + filename + ".sqlite\"").build();
	}


	/**
	 * Exportiert eine SQLite-Datenbank aus dem aktuellen Schema als ZIP-Datei. Der Aufruf erfordert
	 * administrative Rechte.
	 *
	 * @param conn         die Datenbank-Verbindung zu dem aktuellen Schema
	 * @param schemaname   Name des Schemas, welches exportiert werden soll
	 *
	 * @return Die ZIP-Datei mit der SQLite-Datenbank
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static Response exportZip(final DBEntityManager conn, final String schemaname) throws ApiOperationException {
		return exportSqliteOrZip(conn, schemaname, "ZIP-Datei erstellt.", (logger, sqlite) -> createZipResponse(logger, sqlite, getExportFilename(schemaname)));
	}


	private static Response createZipResponse(final Logger logger, final APITempDBFile sqlite, final String filename) {
		logger.logLn("Erstelle ZIP-Archiv mit der SQLite-Datenbank unter dem Namen \"" + sqlite.getFilename() + "\".");
		return Response.ok((StreamingOutput) output -> {
			try (java.util.zip.ZipOutputStream zipOut = new java.util.zip.ZipOutputStream(output)) {
				zipOut.setLevel(java.util.zip.Deflater.DEFAULT_COMPRESSION);

				// Füge die SQLite-Datei zum ZIP-Archiv hinzu
				final java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(filename + ".sqlite");
				zipOut.putNextEntry(zipEntry);

				// Kopiere die SQLite-Datei in das ZIP-Archiv
				try (java.io.FileInputStream fis = new java.io.FileInputStream(sqlite.getFilename())) {
					final byte[] buffer = new byte[8192];
					int length;
					while ((length = fis.read(buffer)) > 0) {
						zipOut.write(buffer, 0, length);
					}
				}

				zipOut.closeEntry();
				zipOut.flush();
			} catch (final Exception e) {
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e);
			}
		}).header("Content-Disposition", "attachment; filename=\"" + filename + ".zip\"").build();
	}


	private static String getExportFilename(final String schemaname) {
		// Bestimme den Dateinamen mit Zeitstempel
		final ZoneId berlin = ZoneId.of("Europe/Berlin");
		final ZonedDateTime jetzt = ZonedDateTime.now(berlin);
		return schemaname + String.format("_%02d%02d%02d_%02d%02d", jetzt.getYear(), jetzt.getMonthValue(),
				jetzt.getDayOfMonth(), jetzt.getHour(), jetzt.getMinute());
	}


	/**
	 * Exportiert eine SQLite-Datenbank aus dem aktuellen Schema, optional als ZIP-Datei.
	 *
	 * @param conn             die Datenbank-Verbindung zu dem aktuellen Schema
	 * @param schemaname       Name des Schemas, welches exportiert werden soll
	 * @param successMessage   die Nachricht für das Log im Erfolgsfall
	 * @param exportTask       der Task für die eigentliche Durchführung des Exports.
	 *
	 * @return Die SQLite-Datenbank oder ZIP-Datei
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private static Response exportSqliteOrZip(final DBEntityManager conn, final String schemaname, final String successMessage,
			final BiFunction<Logger, APITempDBFile, Response> exportTask) throws ApiOperationException {

		final Logger logger = new Logger();
		logger.copyConsumer(Logger.global());
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		try {
			if (SVWSKonfiguration.get().isLoggingEnabled()) {
				logger.addConsumer(new LogConsumerLogfile("svws_schema_" + schemaname + ".log", true, true));
			}
		} catch (final IOException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen einer Log-Datei für das Schema");
		}

		// Bestimme den Dateinamen für eine temporäre SQLite-Datei
		try (APITempDBFile sqlite = new APITempDBFile(DBDriver.SQLITE, conn.getDBSchema(), logger, log, null, false)) {
			// Erzeuge einen Schema-Manager, der den Export des DB-Schema durchführt
			final DBSchemaManager srcManager = DBSchemaManager.create(conn, true, logger);
			if (srcManager == null) {
				throw new ApiOperationException(Status.FORBIDDEN);
			}

			// Führe den Export mithilfe des Schema-Managers durch.
			logger.modifyIndent(2);
			srcManager.backup.exportDB(sqlite.getFilename(), logger);
			logger.modifyIndent(-2);

			final Response response = exportTask.apply(logger, sqlite);
			if (!response.hasEntity()) {
				logger.logLn(2, "[FEHLER]");
			}
			logger.logLn(successMessage);
			return response;
		} catch (final DBException e) {
			throw new ApiOperationException(Status.FORBIDDEN, e, "Fehler bei der Datenbank-Verbindung.");
		}
	}


	/**
	 * Import ein Backup der SVWS-Datenbank in das angegebene bereits vorhandene Schema der
	 * übergebenen Datenbank-Verbindung. Dabei geht der ursprüngliche Inhalt des Ziel-Schemas
	 * verloren. Die Quell-Datenbank kann entweder eine SQLite-Datei oder eine ZIP-Datei sein,
	 * die eine SQLite-Datei enthält.
	 *
	 * @param conn         die Datenbank-Verbindung zum Ziel-Schema
	 * @param srcDB        die SQLite-Quell-Datenbank oder ZIP-Datei
	 *
	 * @return die HTTP-Response mit dem LOG des Imports
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static Response importSQLite(final DBEntityManager conn, final byte[] srcDB) throws ApiOperationException {
		// Prüfe, ob es eine ZIP-Datei ist (Magic Bytes: 50 4B 03 04 = "PK..")
		final boolean isZip = (srcDB.length >= 4) && (srcDB[0] == 0x50) && (srcDB[1] == 0x4B) && (srcDB[2] == 0x03) && (srcDB[3] == 0x04);

		if (!isZip) {
			// Es ist eine normale SQLite-Datei --> Importieren
			return importSQLiteInternal(conn, srcDB);
		}

		// Es ist eine ZIP-Datei --> Entpacken --> Importieren
		return importSQLiteInternal(conn, extractSQLiteFromZip(srcDB));
	}


	/**
	 * Extrahiert die SQLite-Datei aus einer ZIP-Datei.
	 *
	 * @param zipData      die ZIP-Datei als Byte-Array
	 *
	 * @return die extrahierte SQLite-Datei als Byte-Array
	 *
	 * @throws ApiOperationException wenn die ZIP-Datei nicht gelesen werden kann, keine Datei enthält
	 *                               oder keine SQLite-Datei gefunden wurde
	 */
	private static byte[] extractSQLiteFromZip(final byte[] zipData) throws ApiOperationException {
		try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipData))) {

			final ZipEntry firstEntry = zis.getNextEntry();
			if (firstEntry == null) {
				throw new ApiOperationException(Status.BAD_REQUEST, "ZIP-Datei enthält keine Dateien");
			}

			// Prüfe, ob es eine SQLite-Datei ist (case-insensitive)
			final String filename = firstEntry.getName().toLowerCase();
			if (!filename.endsWith(".sqlite")) {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die Datei im ZIP ist keine SQLite-Datenbank (erwartet: .sqlite): " + firstEntry.getName());
			}

			// Extrahiere die SQLite-Datei
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final byte[] buffer = new byte[8192];
			int length;
			while ((length = zis.read(buffer)) > 0) {
				baos.write(buffer, 0, length);
			}

			// Prüfe, ob weitere Dateien im ZIP vorhanden sind.
			final ZipEntry nextEntry = zis.getNextEntry();
			if (nextEntry != null) {
				throw new ApiOperationException(Status.BAD_REQUEST,
					"ZIP-Datei enthält mehr als eine Datei. Es wird genau eine SQLite-Datei erwartet.");
			}

			return baos.toByteArray();
		} catch (final IOException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e,
					"Fehler beim Entpacken der ZIP-Datei");
		}
	}


	/**
	 * Interne Methode zum Import einer SQLite-Datenbank.
	 *
	 * @param conn         die Datenbank-Verbindung zum Ziel-Schema
	 * @param srcDB        die SQLite-Quell-Datenbank als Byte-Array
	 *
	 * @return die HTTP-Response mit dem LOG des Imports
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private static Response importSQLiteInternal(final DBEntityManager conn, final byte[] srcDB) throws ApiOperationException {
		final Logger logger = new Logger();
		logger.copyConsumer(Logger.global());
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		try {
			if (SVWSKonfiguration.get().isLoggingEnabled()) {
				logger.addConsumer(new LogConsumerLogfile("svws_schema_" + conn.getDBSchema() + ".log", true, true));
			}
		} catch (final IOException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen einer Log-Datei für das Schema");
		}

		// Erstelle temporär eine SQLite-Datei aus dem übergebenen Byte-Array
		try (APITempDBFile sqlite = new APITempDBFile(DBDriver.SQLITE, conn.getDBSchema(), logger, log, srcDB, true)) {
			logger.logLn("Importiere in die " + conn.getDBDriver() + "-Datenbank unter " + conn.getDBLocation() + ":");
			logger.logLn(2, "- verwende den Admin-Benutzer: " + conn.getUser().getUsername());
			logger.logLn(2, "- verwende das vorhandene DB-Schema: " + conn.getDBSchema());

			// Erstelle die Quell-DB-Konfiguration für die übergebene Datei
			final DBConfig srcConfig = sqlite.getConfig();

			// Bestimme die Zielkonfiguration aus der SWVS-Konfiguration
			DBConfig tgtConfig = SVWSKonfiguration.get().getDBConfig(conn.getDBSchema());
			final boolean hatSchemaConfig = (tgtConfig != null);
			// Falls das Schema ist in der SVWS-Konfiguration nicht als SVWS-Schema angelegt wurde, dann verwende die Informationen aus der aktuellen Datenbank-Verbindung.
			if (tgtConfig == null) {
				tgtConfig = SVWSKonfiguration.get().getRootDBConfig(conn.getUser().getUsername(), conn.getUser().getPassword())
						.switchSchema(PersistenceUnits.SVWS_ROOT, conn.getDBSchema());
			}

			try {
				final Benutzer srcUser = Benutzer.create(srcConfig);
				try (DBEntityManager srcConn = srcUser.getEntityManager()) {
					if (srcConn == null) {
						logger.logLn(0, " [Fehler]");
						throw new DBException("Fehler beim Verbinden zur SQLite-Export-Datenbank");
					}
					logger.logLn(0, " [OK]");

					final DBSchemaManager srcManager = DBSchemaManager.create(srcConn, true, logger);
					logger.modifyIndent(2);
					if (!srcManager.backup.importDBInto(tgtConfig, -1, false, logger)) {
						throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, simpleResponse(false, log));
					}
					logger.modifyIndent(-2);
				}
			} catch (@SuppressWarnings("unused") final DBException e) {
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, simpleResponse(false, log));
			}

			// Schreibe die Verbindungsinformation für das neu angelegte SVWS-Schema in die SVWS-Konfiguration
			try {
				if (!hatSchemaConfig) {
					SVWSKonfiguration.get().createOrUpdateSchema(conn.getDBSchema(), conn.getUser().getUsername(), conn.getUser().getPassword(), false);
				}
			} catch (final SVWSKonfigurationException e) {
				logger.logLn(LogLevel.ERROR, 2, "Fehler bei dem Erstellen bzw. Anpassen der SVWS-Konfiguration (" + e.getMessage() + ")");
				final SimpleOperationResponse daten = simpleResponse(false, log);
				return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity(daten).build();
			}
		}

		logger.logLn("Import abgeschlossen.");
		final SimpleOperationResponse daten = simpleResponse(true, log);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

}
