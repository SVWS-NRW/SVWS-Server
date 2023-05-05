package de.svws_nrw.db.utils.lupo.app;

import java.io.IOException;

import de.svws_nrw.base.shell.CommandLineException;
import de.svws_nrw.base.shell.CommandLineOption;
import de.svws_nrw.base.shell.CommandLineParser;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.utils.lupo.mdb.LupoMDB;


/**
 * Diese Klasse stellt eine Kommandozeilen-Anwendung zum Import einer LuPO-MDB-Datenbank
 * in ein SVWS-Schema zur Verfügung.
 */
public class ImportLupoMDB {

	/// Der Logger
	private static Logger logger = new Logger();


	/**
	 *
	 *
	 * @param args  die Optionen für die Codegenerierung, @see options
	 */
	public static void main(final String[] args) {
		logger.addConsumer(new LogConsumerConsole());

		// Lese die Kommandozeilenparameter ein
		final CommandLineParser cmdLine = new CommandLineParser(args, logger);
		try {
			cmdLine.addOption(new CommandLineOption("j", "ja", false, "Beantwortet alle Fragen beim Import automatisch mit \"Ja\""));
			cmdLine.addOption(new CommandLineOption("f", "file", true, "Der vollständige Dateiname, wo die LuPO-Datei liegt"));
			cmdLine.addOption(new CommandLineOption("cp", "configPath", true, "Gibt den Pfad zu der SVWS-Konfigurationsdatei an, wenn diese nicht an einem Standardort liegt."));
			cmdLine.addOption(new CommandLineOption("td", "tgtDrv", true, "Der Treiber für die Ziel-DB (\"MDB\", \"MSSQL\", \"MYSQL\", \"MARIA_DB\" oder \"SQLITE\")"));
			cmdLine.addOption(new CommandLineOption("tl", "tgtLoc", true, "Der Ort, wo die Ziel-DB zu finden ist (Der Pfad einer Datei oder der Ort im Netzwerk, z.B. \"localhost\" )"));
			cmdLine.addOption(new CommandLineOption("ts", "tgtDB", true, "Der Schema-Name für die Ziel-DB (bei \\\"MDB\\\" und \\\"SQLITE\\\" nicht benötigt)"));
			cmdLine.addOption(new CommandLineOption("tu", "tgtUser", true, "Der DB-Benutzer für die Ziel-DB"));
			cmdLine.addOption(new CommandLineOption("tp", "tgtPwd", true, "Das DB-Kennwort für die Ziel-DB"));

			final boolean antwort_ja = cmdLine.isSet("j");

			// Lade die Konfigurationsdatei für den Datenbankzugriff
			logger.logLn("Lese SVWS Konfiguration ein...");
			SVWSKonfiguration.getFrom(cmdLine.getValue("cp", null));

		    // Lese die Optionen für die Ziel-Datenbank ein
		    final DBDriver tgtDrv = DBDriver.fromString(cmdLine.getValue("td", "MARIA_DB"));
		    if (tgtDrv == null)
		    	cmdLine.printOptionsAndExit(3, "Fehlerhafte Angabe bei dem Treiber der Ziel-DB");
			final String tgtLoc = cmdLine.getValue("tl", "localhost");
			final String tgtDB = cmdLine.getValue("ts", "svwsdb");
			final String tgtUser = cmdLine.getValue("tu", "svwsadmin");
			final String tgtPwd = cmdLine.getValue("tp", "svwsadmin");
			final DBConfig tgtConfig = new DBConfig(tgtDrv, tgtLoc, tgtDB, false, tgtUser, tgtPwd, true, false);
			final Benutzer user = Benutzer.create(tgtConfig);

		    // Lese den Namen für der LuPO-Datei ein und öffne die Datei
			final String lupofilename = cmdLine.getValue("f", "Laufbahnplanung.lup");
			final LupoMDB lupoMDB = new LupoMDB(lupofilename);
			lupoMDB.logger.copyConsumer(logger);
			lupoMDB.importFrom();

			// Schreibe in die LuPO-Datenbank
			lupoMDB.setLUPOTables(user, antwort_ja);

			logger.logLn("Fertig!");
		} catch (final CommandLineException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		} catch (final IOException e) {
			cmdLine.printOptionsAndExit(2, e.getMessage());
		}
	}

}
