package de.svws_nrw.db.utils.app;

import java.util.Scanner;

import org.apache.commons.lang3.math.NumberUtils;

import de.svws_nrw.base.shell.CommandLineException;
import de.svws_nrw.base.shell.CommandLineOption;
import de.svws_nrw.base.shell.CommandLineParser;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.utils.schema.DBSchemaManager;



/**
 * Diese Klasse stellt eine Kommandozeilen-Anwendung zum Erstellen eines
 * SVWS-Server-Schema zur Verfügung.
 */
public class CreateSchema {

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
	private static boolean konsoleFrageJaNein(final String text, final Scanner scan) {
		boolean antwort = false;
		logger.log(text);
		boolean habeAntwort = false;
		while (!habeAntwort) {
			final String input = scan.nextLine();
			if (input == null)
				continue;
			habeAntwort = true;
			switch (input.toUpperCase()) {
				case "JA", "J", "YES", "Y":
					antwort = true;
					break;
				case "NEIN", "N", "NO":
					break;
				default:
					habeAntwort = false;
			}
		}
		return antwort;
	}


	/**
	 * Liest von der Kommandozeile die Parameter ein, die für das Erstellen eines Schemas benötigt werden
	 * und erstellt das Schema in der Datenbank.
	 *
	 * @param args  die Optionen für das Erstellen des neuen Schemas, @see options
	 */
	public static void main(final String[] args) {
		logger.addConsumer(new LogConsumerConsole());

		// Lese die Kommandozeilenparameter ein
		final CommandLineParser cmdLine = new CommandLineParser(args, logger);
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

			try (Scanner scan = new Scanner(System.in)) {
				// Frage ggf. nach, ob die Quelldatenbank aufgeräumt werden soll
				boolean hinweis_akzpetiert = cmdLine.isSet("j");
				if (!hinweis_akzpetiert) {
					hinweis_akzpetiert = konsoleFrageJaNein("Die Zieldatenbank wird neu angelegt. Dabei gehen alle Daten in der Ziel-Datenbank verloren. Fortfahren? (J/N) ", scan);
					if (!hinweis_akzpetiert) {
						cmdLine.printOptionsAndExit(2, "Die Zieldatenbank muss neu angelegt werden. Breche die Erstellung ab.");
					}
				}
			}

			// Lade die Konfigurationsdatei für den Datenbankzugriff
			SVWSKonfiguration.getFrom(cmdLine.getValue("cp", null));

			final int maxUpdateRevision = NumberUtils.toInt(cmdLine.getValue("r", "-1"), -1);
			final boolean devMode = cmdLine.isSet("d");

		    // Lese die Optionen für die Ziel-Datenbank ein
		    final DBDriver tgtDrv = DBDriver.fromString(cmdLine.getValue("td", "MARIA_DB"));
		    if (tgtDrv == null)
		    	cmdLine.printOptionsAndExit(4, "Fehlerhafte Angabe bei dem Treiber der Ziel-DB");
			final String tgtLoc = cmdLine.getValue("tl", "localhost");
			final String tgtDB = cmdLine.getValue("ts", "svwsschema");
			final String tgtUser = cmdLine.getValue("tu", "svwsadmin");
			final String tgtPwd = cmdLine.getValue("tp", "svwsadmin");
			final String tgtRootUser = cmdLine.getValue("tq", null);
			final String tgtRootPwd = cmdLine.getValue("tr", "svwsadmin");
			final DBConfig tgtConfig = new DBConfig(tgtDrv, tgtLoc, tgtDB, false, tgtUser, tgtPwd, true, true);

			// Führe das Erstellen mithilfe des Schema-Managers durch.
			DBSchemaManager.createNewSchema(tgtConfig, tgtRootUser, tgtRootPwd, maxUpdateRevision, devMode, logger);
		} catch (final CommandLineException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		}
	}

}
