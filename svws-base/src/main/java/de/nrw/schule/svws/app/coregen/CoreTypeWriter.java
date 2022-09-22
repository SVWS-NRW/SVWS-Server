package de.nrw.schule.svws.app.coregen;

import java.nio.file.Paths;

import de.nrw.schule.svws.logger.LogConsumerConsole;
import de.nrw.schule.svws.logger.Logger;
import de.nrw.schule.svws.shell.CommandLineException;
import de.nrw.schule.svws.shell.CommandLineOption;
import de.nrw.schule.svws.shell.CommandLineParser;

/**
 * Diese Klasse generiert den Java-Code für die Default-Daten der Core-Types.
 */
public class CoreTypeWriter {

	/** Der Parser für die Kommandozeile */
	private static CommandLineParser cmdLine;

	/** Der intern genutzte Logger */
	private static Logger logger = new Logger();

	/** Die Konsole als Ziel der Log-Informationen */
	private static LogConsumerConsole logConsumerConsole = new LogConsumerConsole();
		
	
	/**
	 * Liest die CSV-Dateien mit den Default-Daten ein und erzeugt die Daten in den Java-Core-Types. Als Parameter
	 * kann der Pfad zu dem Ordner eingelesen werden, in dem sich die Java-Dateien der Core-Types befinden.
	 *   
	 * @param args  die Optionen für die Codegenerierung, @see options
	 */
	public static void main(String[] args) {
		logger.addConsumer(logConsumerConsole);
		
		// Gib das aktuelle Verzeichnis aus
		logger.logLn("Aktuelles Verzeichnis: " + Paths.get("").toAbsolutePath().toString());
		
		cmdLine = new CommandLineParser(args);
		try {
			cmdLine.addOption(new CommandLineOption("o", "output", true, "Der Source-Ordner, wo die Java-DTO-Klassen abgelegt werden"));
			String path = cmdLine.getValue("o", "../svws-core/src/main/java");

			CoreTypeUpdater.updateAll(logger, path);
		} catch (CommandLineException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		}
	}

}
