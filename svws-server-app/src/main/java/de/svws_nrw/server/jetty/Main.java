package de.svws_nrw.server.jetty;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import de.svws_nrw.api.common.ResourceFileManager;
import de.svws_nrw.api.common.SVWSVersion;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.config.LogConsumerLogfile;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.data.db.DBSchemaListeEintrag;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.db.utils.schema.DBSchemaManager;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse stellt die main-Methode für die Kommandozeilen-Applikation
 * des SVWS-Servers zur Verfügung.
 */
public class Main {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Main() {
		// leer
	}

	/**
	 * Aktualisiert das Schema, mit welchem der Manager initialisiert wurde.
	 *
	 * @param dbManager   der Schema-Manager
	 * @param logger   der Logger
	 *
	 * @return true, falls die Aktualisierung erfolgreich war, und ansonsten false
	 *
	 * @throws ApiOperationException falls ein interner Fehler aufgetreten ist
	 */
	private static boolean updateSchema(final DBSchemaManager dbManager, final Logger logger) throws ApiOperationException {
		final SVWSKonfiguration svwsconfig = SVWSKonfiguration.get();
		final boolean devMode = (svwsconfig.getServerMode() != ServerMode.STABLE);
		LogConsumerLogfile logfile = null;
		try {
			if (SVWSKonfiguration.get().isLoggingEnabled()) {
				logfile = new LogConsumerLogfile("svws_schema_" + dbManager.getSchemaname() + ".log", true, true);
				logger.addConsumer(logfile);
			}
		} catch (final IOException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen einer Log-Datei für das Schema");
		}
		logger.logLn("Revision veraltet - führe Update aus...");
		logger.modifyIndent(2);
		final boolean success = dbManager.updater.update(dbManager.getConnection(), -1, devMode, true);
		logger.modifyIndent(-2);
		if (logfile != null)
			logger.removeConsumer(logfile);
		return success;
	}


	/**
	 * Aktualisiert die Core-Type-Daten im Datenbank-Schema
	 *
	 * @param dbManager   der Schema-Manager
	 * @param logger   der Logger
	 *
	 * @return true, falls die Aktualisierung erfolgreich war, und ansonsten false
	 *
	 * @throws ApiOperationException falls ein interner Fehler aufgetreten ist
	 */
	private static boolean updateSchemaCoreTypes(final DBSchemaManager dbManager, final Logger logger) throws ApiOperationException {
		LogConsumerLogfile logfile = null;
		try {
			if (SVWSKonfiguration.get().isLoggingEnabled()) {
				logfile = new LogConsumerLogfile("svws_schema_" + dbManager.getSchemaname() + ".log", true, true);
				logger.addConsumer(logfile);
			}
		} catch (final IOException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen einer Log-Datei für das Schema");
		}
		logger.logLn("Core-Types veraltet - führe Update aus...");
		logger.modifyIndent(2);

		final boolean success = dbManager.updater.coreTypes.updateNewTransaction(dbManager.getConnection(), true, -1);
		logger.modifyIndent(-2);
		if (logfile != null)
			logger.removeConsumer(logfile);
		return success;
	}


	/**
	 * Prüft das Default-Charset und die Default-Collation und gibt ggf. Fehlemeldungen über den Logger
	 * aus.
	 *
	 * @param dbConn   die Datenbankverbindung
	 * @param logger   der Logger
	 */
	private static void pruefeCharsetAndCollation(final DBEntityManager dbConn, final Logger logger) {
		if (dbConn.getDBDriver() == DBDriver.MARIA_DB) {
			final List<?> result = dbConn
					.getNativeQuery("SELECT DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME=?")
					.setParameter(1, dbConn.getDBSchema())
					.getResultList();
			if ((result == null) || result.isEmpty()) {
				logger.logLn("Konnte das Default-Charset und die Default-Collation des Schemas nicht bestimmen.");
			} else {
				final Object[] tmp = (Object[]) result.get(0);
				final String charset = (String) tmp[0];
				final String collation = (String) tmp[1];
				if (!"utf8mb4".equals(charset))
					logger.logLn("Warnung: Das Datenbank-Schema hat nicht 'utf8mb4' als Default-Charset. Dies kann zu Kompatibilitätsproblemen führen.");
				if (!"utf8mb4_bin".equals(collation))
					logger.logLn("Warnung: Das Datenbank-Schema hat nicht 'utf8mb4_bin' als Default-Collation. Dies kann zu Kompatibilitätsproblemen führen.");
			}
		}
	}


	/**
	 * Prüft das übergebene Schema und führt ggf. Updates durch.
	 *
	 * @param schema     das Schema
	 * @param logger     der Logger
	 * @param doUpdate   gibt an, ob Updates auf das Schema ausgeführt werden sollen oder nicht
	 * @param versuch    die Nummer des Versuchs, das Schema zu prüfen
	 *
	 * @return true, wenn eine Verbindung aufgebaut wurde und eine Prüfung stattgefunden hat, und ansonsten false
	 */
	private static boolean pruefeSchema(final DBSchemaListeEintrag schema, final Logger logger, final boolean doUpdate, final int versuch) {
		final SVWSKonfiguration svwsconfig = SVWSKonfiguration.get();
		final boolean devMode = (svwsconfig.getServerMode() != ServerMode.STABLE);

		logger.logLn("Schema " + schema.name + ": Prüfung der Datenbankverbindung" + (versuch > 1 ? " (" + versuch + ". Versuch) " : " ") + "... ");
		logger.modifyIndent(2);
		final DBConfig dbconfig = svwsconfig.getDBConfig(schema.name);
		boolean schemaOK = true;
		boolean connected = false;
		final Benutzer dbUser = Benutzer.create(dbconfig);
		if (dbUser != null) {
			try (DBEntityManager dbConn = dbUser.getEntityManager()) {
				if (dbConn == null) {
					logger.logLn("Verbindung zu dem Schema " + schema.name + " nicht möglich!");
					logger.modifyIndent(-2);
					return connected;
				}
				connected = true;
				pruefeCharsetAndCollation(dbConn, logger);
				if (doUpdate) {
					final DBSchemaManager dbManager = DBSchemaManager.create(dbConn, true, logger);
					if (!dbManager.updater.isUptodate(-1, devMode) && !updateSchema(dbManager, logger))
						schemaOK = false;
					if (!dbManager.updater.coreTypes.isUptodate() && !updateSchemaCoreTypes(dbManager, logger))
						schemaOK = false;
				} else {
					logger.logLn("Die Automatische Schema-Aktualisierung wurde in der Server-Konfiguration deaktiviert.");
				}
			} catch (@SuppressWarnings("unused") final Exception e) {
				schemaOK = false;
			}
		}
		if (schemaOK) {
			svwsconfig.activateSchema(schema.name);
			logger.logLn("Prüfung von " + schema.name + " erfolgreich. Schema ist aktiv.");
		} else {
			svwsconfig.deactivateSchema(schema.name);
			logger.logLn("Fehler: Schema " + schema.name + " kann nicht aktualisiert werden. Das Schema wird deaktiviert.");
		}
		logger.modifyIndent(-2);
		return connected;
	}


	/**
	 * Diese Methode ist der Einsprungspunkt für die Java-Kommandozeilen-Applikation
	 * des SVWS-Servers.
	 * Hierbei wird zunächst die SVWS-Konfiguration geladen, dann basierend auf dem
	 * konfigurierten Pfad die Ressourcen des SVWS-Client, welche über die
	 * Open-API-Schnittstelle zur Verfügung gestellt werden.
	 * Anschließend wird dann der Jetty-Embedded Http-Server mit der
	 * OpenAPI-Schnittstellen-Web-Applikation gestartet.
	 *
	 * @param args   die Kommandozeilenargumente zum Starten des Servers.
	 */
	public static void main(final String[] args) {
		// Setze das Default-Encoding auf UTF-8
		System.setProperty("file.encoding", "UTF-8");
		System.setProperty("stdout.encoding", "UTF-8");
		System.setProperty("stderr.encoding", "UTF-8");

		// Erstelle den SVWS-Server und nimm dessen Logger zur Ausgabe der Informationen beim Serverstart
		final SvwsServer server = SvwsServer.instance();
		final Logger logger = new Logger();
		logger.copyConsumer(Logger.global());

		// Gebe ein paar Status-Informationen beim Start des Servers aus
		logger.logLn("SVWS-Server Version " + SVWSVersion.version());
		logger.logLn("Heap-Speicher: ");
		logger.logLn("  Gesamt: " + (Math.round(Runtime.getRuntime().maxMemory() / 10000000.0) / 100.0) + "G");
		logger.logLn("  Belegt: " + (Math.round(Runtime.getRuntime().totalMemory() / 10000000.0) / 100.0) + "G");
		logger.logLn("  Frei: " + (Math.round(Runtime.getRuntime().freeMemory() / 10000000.0) / 100.0) + "G");

		// Initialisiere die Core-Types
		ASDCoreTypeUtils.initAll();

		// Lese Konfiguration
		final SVWSKonfiguration svwsconfig = SVWSKonfiguration.get();

		// Lese alle HTML, CSS and Javascript Ressourcen für den Web-Client und den Admin-Web-Client
		ResourceFileManager.client();
		ResourceFileManager.admin();

		// Sperre zunächst alle konfigurierten Datenbank-Verbindungen und aktiviere diese erste nachdem die Prüfung erfolgreich war
		final List<DBSchemaListeEintrag> schemata = svwsconfig.getSchemaList();
		for (final DBSchemaListeEintrag schema : schemata)
			svwsconfig.deactivateSchema(schema.name);

		// Starte den SVWS-HTTP-Server (v1.1 or v2 in Abhängigkeit von der SVWS-Konfiguration) in einem eigenen Task
		Thread.ofPlatform().start(() -> {
			try {
				server.start();
			} catch (final Exception e) {
				logger.logLn("Fehler beim Starten des Servers: " + e.getMessage());
				e.printStackTrace();
			}
		});

		// Prüfe die einzelen Schemata in eigenen Threads und aktiviere diese nachdem die Prüfung erfolgreich war
		for (final DBSchemaListeEintrag schema : schemata) {
			Thread.ofPlatform().start(() -> {
				final Logger threadLogger = new Logger();
				final LogConsumerList logConsumer = new LogConsumerList();
				threadLogger.addConsumer(logConsumer);
				for (int i = 1; i <= 5; i++) {
					final boolean geprueft = pruefeSchema(schema, threadLogger, !svwsconfig.isAutoUpdatesDisabled(), i);
					for (final String str : logConsumer.getStrings())
						logger.logLn(str);
					if (geprueft)
						break;
					try {
						Thread.sleep(Duration.ofSeconds(30));
					} catch (final InterruptedException e) {
						e.printStackTrace();
						Thread.currentThread().interrupt();
					}
				}
			});
		}

	}

}
