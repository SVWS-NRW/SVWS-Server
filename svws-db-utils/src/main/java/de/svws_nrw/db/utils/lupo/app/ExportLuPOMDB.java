package de.svws_nrw.db.utils.lupo.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.base.shell.CommandLineException;
import de.svws_nrw.base.shell.CommandLineOption;
import de.svws_nrw.base.shell.CommandLineParser;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.PersistenceUnits;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.db.utils.lupo.mdb.LupoMDB;

/**
 * Diese Klasse stellt eine Kommandozeilen-Anwendung zum Erstellen einer neuen
 * leeren LuPO-MDB-Datenbank aus einem SVWS-Schema zur Verfügung.
 */
public class ExportLuPOMDB {

	/// Der Logger
	private static final Logger logger = new Logger();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ExportLuPOMDB() {
		// leer
	}

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
	 * Hauptmethode zum Erstellen einer neuen leeren LuPO-MDB-Datenbank aus einem SVWS-Schema.
	 *
	 * @param args  die Optionen für die Codegenerierung, @see options
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static void main(final String[] args) throws ApiOperationException {
		logger.addConsumer(new LogConsumerConsole());
		ASDCoreTypeUtils.initAll();

		// Lese die Kommandozeilenparameter ein
		final CommandLineParser cmdLine = new CommandLineParser(args, logger);
		try {
			cmdLine.addOption(new CommandLineOption("j", "ja", false, "Beantwortet alle Fragen beim Import automatisch mit \"Ja\""));
			cmdLine.addOption(new CommandLineOption("k", "jahrgang", true, "Der Jahrgang, für den die LuPO-Datei erzeugt werden soll."));
			cmdLine.addOption(new CommandLineOption("f", "file", true, "Der vollständige Dateiname, wo die LuPO-Datei erzeugt wird"));
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

			// Lese den Namen für der LuPO-Datei ein und öffne die Datei
			final String lupofilename = cmdLine.getValue("f", "Laufbahnplanung.lup");
			if (Files.exists(Paths.get(lupofilename))) {
				try (Scanner scan = new Scanner(System.in)) {
					// Frage ggf. nach, ob die bestehende LuPO-Datei gelöscht und überschrieben werden soll
					boolean antwortJa = cmdLine.isSet("j");
					if (!antwortJa) {
						antwortJa = konsoleFrageJaNein("Die LuPO-Datei \"" + lupofilename + "\" wird überschrieben. Dabei gehen alle Daten darin verloren."
								+ " Fortfahren? (J/N) ", scan);
						if (!antwortJa) {
							cmdLine.printOptionsAndExit(2, "Die LuPO-Datei darf nicht überschrieben werden und kann daher nicht neu erzeugt werden."
									+ " Breche ab.");
							System.exit(1);
						}
					}
				}
			}

			// Lade die Konfigurationsdatei für den Datenbankzugriff
			logger.logLn("Lese SVWS Konfiguration ein...");
			SVWSKonfiguration.getFrom(cmdLine.getValue("cp", null));

			// Lese die Optionen für die Quell-Datenbank ein
			final DBDriver srcDrv = DBDriver.fromString(cmdLine.getValue("sd", "MARIA_DB"));
			if (srcDrv == null)
				cmdLine.printOptionsAndExit(3, "Fehlerhafte Angabe bei dem Treiber der Quell-DB");
			final String srcLoc = cmdLine.getValue("sl", "localhost");
			final String srcDB = cmdLine.getValue("ss", "svwsdb");
			final String srcUser = cmdLine.getValue("su", "svwsadmin");
			final String srcPwd = cmdLine.getValue("sp", "svwsadmin");
			final DBConfig srcConfig = new DBConfig(PersistenceUnits.SVWS_DB, srcDrv, srcLoc, srcDB, false, srcUser, srcPwd, true, false);
			final Benutzer user = Benutzer.create(srcConfig);

			final String jahrgang = cmdLine.getValue("k", "Q2");

			// Schreibe die Daten für die neue LuPO-Datei
			final LupoMDB lupoMDB = new LupoMDB(lupofilename);
			lupoMDB.logger.copyConsumer(logger);
			lupoMDB.getFromLeistungsdaten(user, jahrgang);
			lupoMDB.exportTo();
		} catch (final CommandLineException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		} catch (final IOException e) {
			cmdLine.printOptionsAndExit(2, e.getMessage());
		}
	}

}
