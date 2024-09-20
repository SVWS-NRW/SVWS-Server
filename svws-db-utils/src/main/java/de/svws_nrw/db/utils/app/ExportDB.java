package de.svws_nrw.db.utils.app;

import java.util.Scanner;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.base.shell.CommandLineException;
import de.svws_nrw.base.shell.CommandLineOption;
import de.svws_nrw.base.shell.CommandLineParser;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.utils.schema.DBSchemaManager;



/**
 * Diese Klasse stellt eine Kommandozeilen-Anwendung zum Export eines SVWS-Server-Schemas
 * in eine SQLite-Datenbank zur Verfügung.
 */
public class ExportDB {

	/// Der Logger
	private static final Logger logger = new Logger();



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
	 * Exportiert ein SVWS-Schema in eine SQLite-Datenbank.
	 *
	 * @param args  die Optionen für den Export, @see options
	 */
	public static void main(final String[] args) {
		logger.addConsumer(new LogConsumerConsole());
		ASDCoreTypeUtils.initAll();

		// Lese die Kommandozeilenparameter ein
		final CommandLineParser cmdLine = new CommandLineParser(args, logger);
		try {
			cmdLine.addOption(
					new CommandLineOption("j", "ja", false, "Beantwortet den Hinweise auf das notwendige Löschen der Ziel-DB automatisch mit \"Ja\""));
			cmdLine.addOption(new CommandLineOption("cp", "configPath", true,
					"Gibt den Pfad zu der SVWS-Konfigurationsdatei an, wenn diese nicht an einem Standardort liegt."));
			cmdLine.addOption(
					new CommandLineOption("sd", "srcDrv", true, "Der Treiber für die Quell-DB (\"MDB\", \"MSSQL\", \"MYSQL\", \"MARIA_DB\" oder \"SQLITE\")"));
			cmdLine.addOption(new CommandLineOption("sl", "srcLoc", true,
					"Der Ort, wo die Quell-DB zu finden ist (Der Pfad einer Datei oder der Ort im Netzwerk, z.B. \"localhost\" )"));
			cmdLine.addOption(
					new CommandLineOption("ss", "srcDB", true, "Der Schema-Name für die Quell-DB (bei \\\"MDB\\\" und \\\"SQLITE\\\" nicht benötigt)"));
			cmdLine.addOption(new CommandLineOption("su", "srcUser", true, "Der DB-Benutzer für die Quell-DB"));
			cmdLine.addOption(new CommandLineOption("sp", "srcPwd", true, "Das DB-Kennwort für die Quell-DB"));
			cmdLine.addOption(new CommandLineOption("f", "file", true, "Der vollständige Dateiname, in welcher der SQLite-Export abgelegt wird"));

			try (Scanner scan = new Scanner(System.in)) {
				// Frage ggf. nach, ob die SQLite-Datenbank ggf. überschrieben werden soll
				boolean hinweis_akzpetiert = cmdLine.isSet("j");
				if (!hinweis_akzpetiert) {
					hinweis_akzpetiert = konsoleFrageJaNein(
							"Die SQLite-Datei muss für einen Export neu angelegt werden. Dabei gehen alle Daten in der SQLite-Datei verloren. Fortfahren? (J/N) ",
							scan);
					if (!hinweis_akzpetiert) {
						cmdLine.printOptionsAndExit(2, "Die SQLite-Datei muss für einen problemlosen Export neu angelegt werden. Breche den Export ab.");
					}
				}
			}

			// Lade die Konfigurationsdatei für den Datenbankzugriff
			SVWSKonfiguration.getFrom(cmdLine.getValue("cp", null));

			// Lese die Optionen für die Quell-Datenbank ein
			final DBDriver srcDrv = DBDriver.fromString(cmdLine.getValue("sd", "MARIA_DB"));
			if (srcDrv == null)
				cmdLine.printOptionsAndExit(3, "Fehlerhafte Angabe bei dem Treiber der Quell-DB");
			final String srcLoc = cmdLine.getValue("sl", "localhost");
			final String srcDB = cmdLine.getValue("ss", "svwsdb");
			final String srcUsername = cmdLine.getValue("su", "svwsadmin");
			final String srcPwd = cmdLine.getValue("sp", "svwsadmin");
			final DBConfig srcConfig = new DBConfig(srcDrv, srcLoc, srcDB, false, srcUsername, srcPwd, true, false);

			// Lese den Namen für die SQLite-Datenbank ein
			final String filename = cmdLine.getValue("f", "svws_export.sqlite");

			logger.logLn("-> Verbinde zur Quell-Datenbank (" + srcConfig.getDBDriver().toString() + ":" + srcLoc + "/" + srcLoc + ")... ");
			final Benutzer srcUser = Benutzer.create(srcConfig);
			try (DBEntityManager srcConn = srcUser.getEntityManager()) {
				if (srcConn == null) {
					logger.logLn(0, " [Fehler]");
					logger.log(LogLevel.ERROR,
							"Fehler bei der Erstellung der Datenbank-Verbindung (driver='" + srcConfig.getDBDriver() + "', schema='" + srcConfig.getDBSchema()
									+ "', location='" + srcConfig.getDBLocation() + "', user='" + srcConfig.getUsername() + "')" + System.lineSeparator());
					throw new DBException("Fehler beim Verbinden zur Quelldatenbank");
				}
				logger.logLn(0, " [OK]");
				logger.log(LogLevel.INFO,
						"Datenbank-Verbindung erfolgreich aufgebaut (driver='" + srcConfig.getDBDriver() + "', schema='" + srcConfig.getDBSchema()
								+ "', location='" + srcConfig.getDBLocation() + "', user='" + srcConfig.getUsername() + "')" + System.lineSeparator());

				final DBSchemaManager srcManager = DBSchemaManager.create(srcUser, true, logger);

				// Führe den Export mithilfe des Schema-Managers durch.
				logger.modifyIndent(2);
				srcManager.backup.exportDB(filename, logger);
				logger.modifyIndent(-2);
			}
		} catch (final DBException e) {
			cmdLine.printOptionsAndExit(2, e.getMessage());
		} catch (final CommandLineException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		}
	}

}
