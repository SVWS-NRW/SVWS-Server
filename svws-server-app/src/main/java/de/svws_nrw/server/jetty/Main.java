package de.svws_nrw.server.jetty;

import java.util.List;

import de.svws_nrw.api.OpenAPIApplication;
import de.svws_nrw.api.ResourceFile;
import de.svws_nrw.api.SVWSVersion;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.data.db.DBSchemaListeEintrag;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.schema.DBSchemaManager;

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

		// Der Logger zur Ausgabe der Informationen beim Serverstart
		final Logger logger = new Logger();
		logger.addConsumer(new LogConsumerConsole(false, false));

		// Gebe ein paar Status-Informationen beim Start des Servers aus
		logger.logLn("SVWS-Server Version " + SVWSVersion.version());
		logger.logLn("Heap-Speicher: ");
		logger.logLn("  Gesamt: " + (Math.round(Runtime.getRuntime().maxMemory() / 10000000.0) / 100.0) + "G");
		logger.logLn("  Belegt: " + (Math.round(Runtime.getRuntime().totalMemory() / 10000000.0) / 100.0) + "G");
		logger.logLn("  Frei: " + (Math.round(Runtime.getRuntime().freeMemory() / 10000000.0) / 100.0) + "G");

		// Lese Konfiguration
		final SVWSKonfiguration svwsconfig = SVWSKonfiguration.get();
		final boolean devMode = svwsconfig.getServerMode() != ServerMode.STABLE;

		// Read all HTML, CSS and Javascript resource files from the Web-Client
		ResourceFile.add(svwsconfig.getClientPath());

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
				final Benutzer dbUser = Benutzer.create(dbconfig);
				boolean schemaOK = true;
				try (DBEntityManager dbConn = dbUser.getEntityManager()) {
					if (dbConn == null) {
						logger.logLn("Verbindung zu dem Schema " + schema.name + " nicht möglich!");
						continue;
					}
					final DBSchemaManager dbManager = DBSchemaManager.create(dbUser, true, logger);
					if (!dbManager.updater.isUptodate(-1, devMode)) {
						logger.logLn("Revision veraltet - führe Update aus...");
						logger.modifyIndent(2);
						if (!dbManager.updater.update(dbUser, -1, devMode, true))
							schemaOK = false;
						logger.modifyIndent(-2);
					}
					if (!dbManager.updater.coreTypes.isUptodate()) {
						logger.logLn("Core-Types veraltet - führe Update aus...");
						logger.modifyIndent(2);
						if (!dbManager.updater.coreTypes.update(dbUser, true, -1))
							schemaOK = false;
						logger.modifyIndent(-2);
					}
				} catch (@SuppressWarnings("unused") final Exception e) {
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

		// Initialize the HTTP Server (v1.1 or v2 depending on the current configuration)
		HttpServer.init();
		HttpServer.addOpenAPIApplication(OpenAPIApplication.class);
		HttpServer.start();
	}

}
