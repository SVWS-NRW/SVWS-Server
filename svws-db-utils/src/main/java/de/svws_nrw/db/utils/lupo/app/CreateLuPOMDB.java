package de.svws_nrw.db.utils.lupo.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import de.svws_nrw.base.shell.CommandLineException;
import de.svws_nrw.base.shell.CommandLineOption;
import de.svws_nrw.base.shell.CommandLineParser;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.utils.lupo.mdb.LupoMDB;

/**
 * Diese Klasse stellt eine Kommandozeilen-Anwendung zum Erstellen einer neuen
 * leeren LuPO-MDB-Datenbank aus einem SVWS-Schema zur Verfügung.
 */
public class CreateLuPOMDB {

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
		logger.logLn(text);
		while (true) {
			final String input = scan.nextLine();
			if (input == null)
				continue;
			switch (input.toUpperCase()) {
				case "JA", "J", "YES", "Y":
					return true;
				case "NEIN", "N", "NO":
					return false;
				default:
			}
		}
	}



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

		    // Lese den Namen für der LuPO-Datei ein und öffne die Datei
			final String lupofilename = cmdLine.getValue("f", "Laufbahnplanung.lup");
			if (Files.exists(Paths.get(lupofilename))) {
				try (Scanner scan = new Scanner(System.in)) {
					// Frage ggf. nach, ob die bestehende LuPO-Datei gelöscht und überschrieben werden soll
					boolean antwort_ja = cmdLine.isSet("j");
					if (!antwort_ja) {
						antwort_ja = konsoleFrageJaNein("Die LuPO-Datei \"" + lupofilename + "\" wird überschrieben. Dabei gehen alle Daten darin verloren. Fortfahren? (J/N) ", scan);
						if (!antwort_ja) {
							cmdLine.printOptionsAndExit(2, "Die LuPO-Datei darf nicht überschrieben werden und kann daher nicht neu erzeugt werden. Breche ab.");
							System.exit(1);
						}
					}
				}
			}

			// Schreibe die Daten für die neue LuPO-Datei
			final LupoMDB lupoMDB = new LupoMDB(lupofilename);
			lupoMDB.getEmpty();
			lupoMDB.exportTo();
		} catch (final CommandLineException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		} catch (final IOException e) {
			cmdLine.printOptionsAndExit(2, e.getMessage());
		}
	}

}
