package de.nrw.schule.svws.db.utils.app;

import java.util.Scanner;

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
 * Diese Klasse stellt eine Kommandozeilen-Anwendung zum Export eines SVWS-Server-Schemas
 * in eine SQLite-Datenbank zur Verfügung.
 */
public class ExportDB {

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
	 * Exportiert ein SVWS-Schema in eine SQLite-Datenbank. 
	 *   
	 * @param args  die Optionen für den Export, @see options
	 */
	public static void main(String[] args) {
		logger.addConsumer(new LogConsumerConsole());
		
		// Lese die Kommandozeilenparameter ein
		cmdLine = new CommandLineParser(args);
		try {
			cmdLine.addOption(new CommandLineOption("j", "ja", false, "Beantwortet den Hinweise auf das notwendige Löschen der Ziel-DB automatisch mit \"Ja\""));
			cmdLine.addOption(new CommandLineOption("cp", "configPath", true, "Gibt den Pfad zu der SVWS-Konfigurationsdatei an, wenn diese nicht an einem Standardort liegt."));
			cmdLine.addOption(new CommandLineOption("sd", "srcDrv", true, "Der Treiber für die Quell-DB (\"MDB\", \"MSSQL\", \"MYSQL\", \"MARIA_DB\" oder \"SQLITE\")"));	
			cmdLine.addOption(new CommandLineOption("sl", "srcLoc", true, "Der Ort, wo die Quell-DB zu finden ist (Der Pfad einer Datei oder der Ort im Netzwerk, z.B. \"localhost\" )"));	
			cmdLine.addOption(new CommandLineOption("ss", "srcDB", true, "Der Schema-Name für die Quell-DB (bei \\\"MDB\\\" und \\\"SQLITE\\\" nicht benötigt)"));	
			cmdLine.addOption(new CommandLineOption("su", "srcUser", true, "Der DB-Benutzer für die Quell-DB"));	
			cmdLine.addOption(new CommandLineOption("sp", "srcPwd", true, "Das DB-Kennwort für die Quell-DB"));	
			cmdLine.addOption(new CommandLineOption("f", "file", true, "Der vollständige Dateiname, in welcher der SQLite-Export abgelegt wird"));
	    
			try (Scanner scan = new Scanner(System.in)) {
				// Frage ggf. nach, ob die SQLite-Datenbank ggf. überschrieben werden soll
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
				
		    // Lese die Optionen für die Quell-Datenbank ein
		    DBDriver srcDrv = DBDriver.fromString(cmdLine.getValue("sd", "MARIA_DB"));
		    if (srcDrv == null)
		    	cmdLine.printOptionsAndExit(3, "Fehlerhafte Angabe bei dem Treiber der Quell-DB");
			String srcLoc = cmdLine.getValue("sl", "localhost");
			String srcDB = cmdLine.getValue("ss", "svwsdb");
			String srcUsername = cmdLine.getValue("su", "svwsadmin");
			String srcPwd = cmdLine.getValue("sp", "svwsadmin");
			DBConfig srcConfig = new DBConfig(srcDrv, srcLoc, srcDB, false, srcUsername, srcPwd, true, false);
	
		    // Lese den Namen für die SQLite-Datenbank ein
			String filename = cmdLine.getValue("f", "svws_export.sqlite");
	
			logger.logLn("-> Verbinde zur Quell-Datenbank (" + srcConfig.getDBDriver().toString() + ":" + srcLoc + "/" + srcLoc + ")... ");
			Benutzer srcUser = Benutzer.create(srcConfig); 
			try (DBEntityManager srcConn = srcUser.getEntityManager()) {
				if (srcConn == null) {
					logger.logLn(0, " [Fehler]");
					logger.log(LogLevel.ERROR, "Fehler bei der Erstellung der Datenbank-Verbindung (driver='" + srcConfig.getDBDriver() + "', schema='" + srcConfig.getDBSchema() + "', location='" + srcConfig.getDBLocation() + "', user='" + srcConfig.getUsername() + "')" + System.lineSeparator());
					throw new DBException("Fehler beim Verbinden zur Quelldatenbank");
				}
				logger.logLn(0, " [OK]");
				logger.log(LogLevel.INFO, "Datenbank-Verbindung erfolgreich aufgebaut (driver='" + srcConfig.getDBDriver() + "', schema='" + srcConfig.getDBSchema() + "', location='" + srcConfig.getDBLocation() + "', user='" + srcConfig.getUsername() + "')" + System.lineSeparator());
		
				DBSchemaManager srcManager = DBSchemaManager.create(srcUser, true, logger); 
				
				// Führe den Export mithilfe des Schema-Managers durch.
				logger.modifyIndent(2);
				srcManager.backup.exportDB(filename, logger);
				logger.modifyIndent(-2);
			}
		} catch (DBException e) {
			cmdLine.printOptionsAndExit(2, e.getMessage());
		} catch (CommandLineException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		}	
	}

}
