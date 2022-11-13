package de.nrw.schule.svws.server.jetty;

import java.util.List;

import de.nrw.schule.svws.api.OpenAPIApplication;
import de.nrw.schule.svws.api.ResourceFile;
import de.nrw.schule.svws.api.SVWSVersion;
import de.nrw.schule.svws.config.SVWSKonfiguration;
import de.nrw.schule.svws.core.data.db.DBSchemaListeEintrag;
import de.nrw.schule.svws.core.logger.LogConsumerConsole;
import de.nrw.schule.svws.core.logger.Logger;
import de.nrw.schule.svws.db.Benutzer;
import de.nrw.schule.svws.db.DBConfig;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.utils.DBDefaultData;
import de.nrw.schule.svws.db.utils.schema.DBSchemaManager;

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
	public static void main(String[] args) throws Exception {
		// Der Logger zur Ausgabe der Informationen beim Serverstart
		Logger logger = new Logger();
		logger.addConsumer(new LogConsumerConsole(false, false));
		
		// Gebe ein paar Status-Informationen beim Start des Servers aus
		logger.logLn("SVWS-Server Version " + SVWSVersion.version());
		logger.logLn("Heap-Speicher: ");
		logger.logLn("  Gesamt: " + (Math.round(Runtime.getRuntime().maxMemory() / 10000000.0) / 100.0) + "G");
		logger.logLn("  Belegt: " + (Math.round(Runtime.getRuntime().totalMemory() / 10000000.0) / 100.0) + "G");		
		logger.logLn("  Frei: " + (Math.round(Runtime.getRuntime().freeMemory() / 10000000.0) / 100.0) + "G");
		
		// Lese Konfiguration
		SVWSKonfiguration svwsconfig = SVWSKonfiguration.get();
		
		// Lese den Default-Daten-Cache
		DBDefaultData.reload(logger);
		
		// Read all HTML, CSS and Javascript resource files from the Web-Client
		ResourceFile.add(svwsconfig.getClientPath());
		
		// Prüfe alle Datenbankverbindungen beim Server-Start
		if (svwsconfig.isAutoUpdatesDisabled()) {
			logger.logLn("Überspringe Prüfung der Datenbankverbindungen! Automatische Aktualisierung wurde in der Server-Konfiguration deaktiviert.");
		} else {
			List<DBSchemaListeEintrag> schemata = svwsconfig.getSchemaList();
			logger.logLn("Prüfe Datenbankverbindungen (" + schemata.size() + ")...");
			logger.modifyIndent(2);
			for (DBSchemaListeEintrag schema : schemata) {
				logger.logLn("-> zu Schema " + schema.name); 
				logger.modifyIndent(2);
				DBConfig dbconfig = svwsconfig.getDBConfig(schema.name);
				try (Benutzer dbUser = Benutzer.create(dbconfig)) {
					try (DBEntityManager dbConn = dbUser.getEntityManager()) {
						if (dbConn == null) {
							logger.logLn("Verbindung zu dem Schema "+ schema.name + " nicht möglich!");
							continue;
						}
						DBSchemaManager dbManager = DBSchemaManager.create(dbConn, true, logger);
						if (!dbManager.updater.isUptodate(-1, false)) {
							logger.logLn("Revision veraltet - führe Update aus...");
							logger.modifyIndent(2);
							dbManager.updater.update(-1, false, true);
							logger.modifyIndent(-2);				
						}
						if (!dbManager.updater.coreTypes.isUptodate()) {
							logger.logLn("Core-Types veraltet - führe Update aus...");
							logger.modifyIndent(2);
							dbManager.updater.coreTypes.update(true, -1);
							logger.modifyIndent(-2);											
						}
						logger.modifyIndent(-2);
					}
				}
			}
			logger.modifyIndent(-2);
		}
		
		// Initialize the HTTP Server (v1.1 or v2 depending on the current configuration)
		HttpServer.init();
		HttpServer.addOpenAPIApplication(OpenAPIApplication.class);
		HttpServer.start();
	}

}
