package de.svws_nrw.server.jetty;

import java.io.IOException;
import java.util.List;

import de.svws_nrw.api.ResourceFileManager;
import de.svws_nrw.api.SVWSVersion;
import de.svws_nrw.config.LogConsumerLogfile;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.data.db.DBSchemaListeEintrag;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.db.utils.schema.DBSchemaManager;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse stellt die main-Methode für die Kommandozeilen-Applikation
 * des SVWS-Servers zur Verfügung.
 */
public class Main {

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
	 *
	 * @throws Exception   gibt in einem unerwarteten Fehlerfall eine Exception zurück.
	 */
	public static void main(final String[] args) throws Exception {
		// Setze das Default-Encoding auf UTF-8
		System.setProperty("file.encoding", "UTF-8");
		System.setProperty("stdout.encoding", "UTF-8");
		System.setProperty("stderr.encoding", "UTF-8");

		// Erstelle den SVWS-Server und nimm dessen Logger zur Ausgabe der Informationen beim Serverstart
		final SvwsServer server = SvwsServer.instance();
		final Logger logger = server.logger();
		ResourceFileManager.setLogger(logger);

		// Gebe ein paar Status-Informationen beim Start des Servers aus
		logger.logLn("SVWS-Server Version " + SVWSVersion.version());
		logger.logLn("Heap-Speicher: ");
		logger.logLn("  Gesamt: " + (Math.round(Runtime.getRuntime().maxMemory() / 10000000.0) / 100.0) + "G");
		logger.logLn("  Belegt: " + (Math.round(Runtime.getRuntime().totalMemory() / 10000000.0) / 100.0) + "G");
		logger.logLn("  Frei: " + (Math.round(Runtime.getRuntime().freeMemory() / 10000000.0) / 100.0) + "G");

		// Lese Konfiguration
		final SVWSKonfiguration svwsconfig = SVWSKonfiguration.get();
		final boolean devMode = svwsconfig.getServerMode() != ServerMode.STABLE;

		// Lese alle HTML, CSS and Javascript Ressourcen für den Web-Client und den Admin-Web-Client
		ResourceFileManager.client();
		ResourceFileManager.admin();

		// Prüfe alle Datenbankverbindungen beim Server-Start
		if (svwsconfig.isAutoUpdatesDisabled()) {
			logger.logLn("Überspringe Prüfung der Datenbankverbindungen! Automatische Aktualisierung wurde in der Server-Konfiguration deaktiviert.");
		} else {
			final List<DBSchemaListeEintrag> schemata = svwsconfig.getSchemaList();
			logger.logLn("Prüfe Datenbankverbindungen (" + schemata.size() + ")...");
			logger.modifyIndent(2);
			for (final DBSchemaListeEintrag schema : schemata) {
				logger.logLn("-> zu Schema " + schema.name);
				logger.modifyIndent(2);
				final DBConfig dbconfig = svwsconfig.getDBConfig(schema.name);
				boolean schemaOK = true;
				try {
					final Benutzer dbUser = Benutzer.create(dbconfig);
					try (DBEntityManager dbConn = dbUser.getEntityManager()) {
						if (dbConn == null) {
							logger.logLn("Verbindung zu dem Schema " + schema.name + " nicht möglich!");
							continue;
						}
						final DBSchemaManager dbManager = DBSchemaManager.create(dbUser, true, logger);
						if (!dbManager.updater.isUptodate(-1, devMode)) {
							LogConsumerLogfile logfile = null;
							try {
								if (SVWSKonfiguration.get().isLoggingEnabled()) {
									logfile = new LogConsumerLogfile("svws_schema_" + schema.name + ".log", true, true);
									logger.addConsumer(logfile);
								}
							} catch (final IOException e) {
								throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen einer Log-Datei für das Schema");
							}
							logger.logLn("Revision veraltet - führe Update aus...");
							logger.modifyIndent(2);
							if (!dbManager.updater.update(dbUser, -1, devMode, true))
								schemaOK = false;
							logger.modifyIndent(-2);
							if (logfile != null)
								logger.removeConsumer(logfile);
						}
						if (!dbManager.updater.coreTypes.isUptodate()) {
							LogConsumerLogfile logfile = null;
							try {
								if (SVWSKonfiguration.get().isLoggingEnabled()) {
									logfile = new LogConsumerLogfile("svws_schema_" + schema.name + ".log", true, true);
									logger.addConsumer(logfile);
								}
							} catch (final IOException e) {
								throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen einer Log-Datei für das Schema");
							}
							logger.logLn("Core-Types veraltet - führe Update aus...");
							logger.modifyIndent(2);
							if (!dbManager.updater.coreTypes.update(dbUser, true, -1))
								schemaOK = false;
							logger.modifyIndent(-2);
							if (logfile != null)
								logger.removeConsumer(logfile);
						}
					} catch (@SuppressWarnings("unused") final Exception e) {
						schemaOK = false;
					}
				} catch (final DBException e) {
					logger.logLn(e.getMessage());
					schemaOK = false;
				}
				if (!schemaOK) {
					svwsconfig.deactivateSchema(schema.name);
					logger.logLn("Fehler: Schema kann nicht aktualisiert werden. Das Schema wird deaktiviert.");
				}
				logger.modifyIndent(-2);
			}
			logger.modifyIndent(-2);
		}

		// Starte den SVWS-HTTP-Server (v1.1 or v2 in Abhängigkeit von der SVWS-Konfiguration)
		server.start();
	}

}
