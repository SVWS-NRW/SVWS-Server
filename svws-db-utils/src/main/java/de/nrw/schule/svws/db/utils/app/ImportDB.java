package de.nrw.schule.svws.db.utils.app;

import java.util.Scanner;

import org.apache.commons.lang3.math.NumberUtils;

import de.nrw.schule.svws.base.shell.CommandLineException;
import de.nrw.schule.svws.base.shell.CommandLineOption;
import de.nrw.schule.svws.base.shell.CommandLineParser;
import de.nrw.schule.svws.config.SVWSKonfiguration;
import de.nrw.schule.svws.core.logger.LogConsumerConsole;
import de.nrw.schule.svws.core.logger.LogLevel;
import de.nrw.schule.svws.core.logger.Logger;
import de.nrw.schule.svws.db.Benutzer;
import de.nrw.schule.svws.db.DBConfig;
import de.nrw.schule.svws.db.DBDriver;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.DBException;
import de.nrw.schule.svws.db.utils.schema.DBSchemaManager;



/**
 * Diese Klasse stellt eine Kommandozeilen-Anwendung zum Import eines SVWS-Server-Schemas
 * in eine SQLite-Datenbank zur Verfügung.
 */
public class ImportDB {

	/// Der Parser für die Kommandozeile
	private static CommandLineParser cmdLine;

	/// Der Logger
	private static Logger logger = new Logger();
	
	
	
	/**
	 * Stellt eine einfache Frage auf der Kommandozeile, welche mit Ja oder Nein
	 * auf dem Scanner beantwortet werden kann.
	 * 
	 * @param text   die Frage
	 * @param scan   der Scanner
	 * 
	 * @return die Antwort - true oder false
	 */
	private static boolean konsoleFrageJaNein(String text, Scanner scan) {
		boolean antwort = false;
		System.out.print(text);
		boolean habeAntwort = false;
		while (!habeAntwort) {
			String input = scan.nextLine();
			if (input == null)
				continue;
			habeAntwort = true;
			switch (input.toUpperCase()) {
				case "JA":
				case "J":
				case "YES":
				case "Y":
					antwort = true;
					break;
				case "NEIN":
				case "N":
				case "NO":
					antwort = false;
					break;
				default:
					habeAntwort = false;
			}
		}
		return antwort;
	}

	
	/**
	 * Importiert eine SQLite-Datenbank in ein SVWS-Schema. 
	 *   
	 * @param args  die Optionen für den Import, @see options
	 */
	public static void main(String[] args) {
		logger.addConsumer(new LogConsumerConsole());
		
		// Lese die Kommandozeilenparameter ein
		cmdLine = new CommandLineParser(args);
		try {
			cmdLine.addOption(new CommandLineOption("r", "revision", true, "Gibt die maximale Revision an, bis zu der die migrierte DB maximal aktualsiert wird (Default: -1 für so weit wie möglich)")); 
			cmdLine.addOption(new CommandLineOption("j", "ja", false, "Beantwortet den Hinweise auf das notwendige Löschen der Ziel-DB automatisch mit \"Ja\""));
			cmdLine.addOption(new CommandLineOption("d", "developerMode", false, "Führt den Import im Developer-Mode durch. Dies bedeutet, dass die Datenbank nur für Testzwecke geeignet ist und entsprechend gekennzeichnet wird."));
			cmdLine.addOption(new CommandLineOption("cp", "configPath", true, "Gibt den Pfad zu der SVWS-Konfigurationsdatei an, wenn diese nicht an einem Standardort liegt."));
			cmdLine.addOption(new CommandLineOption("td", "tgtDrv", true, "Der Treiber für die Ziel-DB (\"MDB\", \"MSSQL\", \"MYSQL\", \"MARIA_DB\" oder \"SQLITE\")"));
			cmdLine.addOption(new CommandLineOption("tl", "tgtLoc", true, "Der Ort, wo die Ziel-DB zu finden ist (Der Pfad einer Datei oder der Ort im Netzwerk, z.B. \"localhost\" )"));
			cmdLine.addOption(new CommandLineOption("ts", "tgtDB", true, "Der Schema-Name für die Ziel-DB (bei \\\"MDB\\\" und \\\"SQLITE\\\" nicht benötigt)"));	
			cmdLine.addOption(new CommandLineOption("tu", "tgtUser", true, "Der DB-Benutzer für die Ziel-DB"));
			cmdLine.addOption(new CommandLineOption("tp", "tgtPwd", true, "Das DB-Kennwort für die Ziel-DB"));
			cmdLine.addOption(new CommandLineOption("tq", "tgtRootUser", true, "Ein DB-Root-User für die Ziel-DB (nur bei \"MSSQL\", \"MYSQL\", \"MARIA_DB\")"));
			cmdLine.addOption(new CommandLineOption("tr", "tgtRootPwd", true, "Das DB-Root-Kennwort für die Ziel-DB (nur bei \"MSSQL\", \"MYSQL\", \"MARIA_DB\")"));
			cmdLine.addOption(new CommandLineOption("f", "file", true, "Der vollständige Dateiname, in welcher der SQLite-Import liegt"));
	    
			try (Scanner scan = new Scanner(System.in)) {
				// Frage ggf. nach, ob die Quelldatenbank aufgeräumt werden soll
				boolean hinweis_akzpetiert = cmdLine.isSet("j");
				if (!hinweis_akzpetiert) {
					hinweis_akzpetiert = konsoleFrageJaNein("Die SQLite-Datei muss für einen Export neu angelegt werden. Dabei gehen alle Daten in der SQLite-Datei verloren. Fortfahren? (J/N) ", scan);
					if (!hinweis_akzpetiert) {
						cmdLine.printOptionsAndExit(2, "Die SQLite-Datei muss für einen problemlosen Export neu angelegt werden. Breche den Export ab.");
					}
				}
				scan.close();
			}
			
			// Lade die Konfigurationsdatei für den Datenbankzugriff
			SVWSKonfiguration.getFrom(cmdLine.getValue("cp", null));
				
			int maxUpdateRevision = NumberUtils.toInt(cmdLine.getValue("r", "-1"), -1);
			boolean devMode = cmdLine.isSet("d");
			
		    // Lese die Optionen für die Quell-Datenbank ein
		    DBDriver tgtDrv = DBDriver.fromString(cmdLine.getValue("td", "MARIA_DB"));
		    if (tgtDrv == null)
		    	cmdLine.printOptionsAndExit(3, "Fehlerhafte Angabe bei dem Treiber der Ziel-DB");
			String tgtLoc = cmdLine.getValue("tl", "localhost");
			String tgtDB = cmdLine.getValue("ts", "svwsdb");
			String tgtUser = cmdLine.getValue("tu", "svwsadmin");
			String tgtPwd = cmdLine.getValue("tp", "svwsadmin");
			String tgtRootUser = cmdLine.getValue("tq", null);
			String tgtRootPwd = cmdLine.getValue("tr", "svwsadmin");
			DBConfig tgtConfig = new DBConfig(tgtDrv, tgtLoc, tgtDB, false, tgtUser, tgtPwd, true, false);
	
		    // Lese den Namen für die SQLite-Datenbank ein
			String filename = cmdLine.getValue("f", "svws_export.sqlite");
	
			logger.log("-> Verbinde zur SQLite-Import-Datenbank " + filename + "...");
			DBConfig srcConfig = new DBConfig(DBDriver.SQLITE, filename, null, false, null, null, true, false);
			Benutzer srcUser = Benutzer.create(srcConfig); 
			try (DBEntityManager srcConn = srcUser.getEntityManager()) {
				if (srcConn == null) {
					logger.logLn(0, " [Fehler]");
					logger.log(LogLevel.ERROR, "Fehler bei der Erstellung der Datenbank-Verbindung (driver='" + srcConfig.getDBDriver() + "', location='" + srcConfig.getDBLocation() + "', user='" + srcConfig.getUsername() + "')" + System.lineSeparator());
					throw new DBException("Fehler beim Verbinden zur SQLite-Export-Datenbank");
				}
				logger.logLn(0, " [OK]");
				logger.log(LogLevel.INFO, "Datenbank-Verbindung erfolgreich aufgebaut (driver='" + srcConfig.getDBDriver() + "', location='" + srcConfig.getDBLocation() + "', user='" + srcConfig.getUsername() + "')" + System.lineSeparator());
				
				DBSchemaManager srcManager = DBSchemaManager.create(srcUser, true, logger);
				
				// Führe die Migration mithilfe des Schema-Managers durch.
				logger.modifyIndent(2);
				srcManager.backup.importDB(tgtConfig, tgtRootUser, tgtRootPwd, maxUpdateRevision, devMode, logger);
				logger.modifyIndent(-2);
			}
		} catch (DBException e) {
			cmdLine.printOptionsAndExit(2, e.getMessage());
		} catch (CommandLineException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		}	
	}

}
