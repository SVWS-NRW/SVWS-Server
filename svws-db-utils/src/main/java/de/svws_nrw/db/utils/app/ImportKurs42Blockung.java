package de.svws_nrw.db.utils.app;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.svws_nrw.base.shell.CommandLineException;
import de.svws_nrw.base.shell.CommandLineOption;
import de.svws_nrw.base.shell.CommandLineParser;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.data.gost.DBUtilsGostBlockung;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBEntityManager;



/**
 * Diese Klasse stellt eine Kommandozeilen-Anwendung zum Import einer Kurs42-Blockung
 * in ein SVWS-Server-Schema zur Verfügung.
 */
public class ImportKurs42Blockung {

	/// Der Parser für die Kommandozeile
	private static CommandLineParser cmdLine;

	/// Der Logger
	private static Logger logger = new Logger();

	/**
	 * Importiert eine Kurs42-Blockung in ein SVWS-Schema.
	 *
	 * @param args  die Optionen für den Import, @see options
	 */
	public static void main(final String[] args) {
		logger.addConsumer(new LogConsumerConsole());

		// Lese die Kommandozeilenparameter ein
		cmdLine = new CommandLineParser(args, logger);
		try {
			cmdLine.addOption(new CommandLineOption("cp", "configPath", true, "Gibt den Pfad zu der SVWS-Konfigurationsdatei an, wenn diese nicht an einem Standardort liegt."));
			cmdLine.addOption(new CommandLineOption("s", "schema", true, "Der Schema-Name für die SVWS-DB (bei \\\"MDB\\\" und \\\"SQLITE\\\" nicht benötigt)"));
			cmdLine.addOption(new CommandLineOption("d", "directory", true, "Das Verzeichnis, in dem sich die Textdateien der Kurs-42-Blockung befinden"));

			// Lade die SVWS-Konfigurationsdatei
			SVWSKonfiguration.getFrom(cmdLine.getValue("cp", null));

		    // Lese das Schema ein und ermittle die Konfiguration für die SVWS-Datenbank
			final String schema = cmdLine.getValue("s", "svwsdb");
			logger.logLn("-> Verbinde zur SVWS-Datenbank-Schema " + schema + "...");
			final DBConfig cfg = SVWSKonfiguration.get().getDBConfig(schema);

			// Lege einen Benutzer für den Datenbank-Zugriff an
			final Benutzer user = Benutzer.create(cfg);

		    // Lese den Pfad für das Verzeichnis der Kurs42-Dateien ein
			final String filename = cmdLine.getValue("d", "");
			final Path path = Paths.get(filename);

			// Führe den Import aus
			try (DBEntityManager conn = user.getEntityManager();) {
				DBUtilsGostBlockung.importKurs42(conn, logger, path);
			}
		} catch (final CommandLineException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		} catch (final IOException e) {
			logger.logLn("FEHLER: Import fehlgeschlagen, Exception: ");
			e.printStackTrace();
		}
	}

}
