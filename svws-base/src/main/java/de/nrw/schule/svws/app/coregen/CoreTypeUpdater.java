package de.nrw.schule.svws.app.coregen;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import de.nrw.schule.svws.logger.LogLevel;
import de.nrw.schule.svws.logger.Logger;

/**
 * Diese Klasse ist die Basis-Klasse für das Aktualisieren von Core-Types.
 */
public abstract class CoreTypeUpdater {

	/** Der Pfad, in dem sich die Java-Dateien des SVWS-Core-Teilprojektes befinden. */
	private static String path = null;
	
	/** Der Pfad des Pakackes, in dem sich die Core-types befinden. */
	private final static String packagePath = "de/nrw/schule/svws/core/types/";
	
	/** Der Name des Core-Types */
	protected final String name;
	
	/** Der genutzte Logger */
	protected static Logger logger;
	
	/** der Name der zu aktualisierenden Datei */
	protected final String filename;
	
	
	/**
	 * Aktualisiert alle Core-Types in dem übergebenen Verzeichnis
	 * 
	 * @param logger   der Logger für Statusinformationen beim Aktualisieren der Core-Types
	 * @param path     der Pfad, in dem sich die Java-Dateien des SVWS-Core-Teilprojektes befinden
	 */
	public static void updateAll(final Logger logger, final String path) {
		if (logger == null)
			return;
		CoreTypeUpdater.logger = logger;
		CoreTypeUpdater.setPath(path);
		// new GenSchuelerVerkehrssprache();
	}
	
	
	/**
	 * Erzeugt einen neuen Updater für Core-Types, welcher die übergebene Datei aktualisiert
	 * 
	 * @param name        der Name des Core-Types
	 * @param filename    der Name der zu aktualisierenden Java-Datei 
	 */
	protected CoreTypeUpdater(final String name, final String filename) {
		this.name = name;
		this.filename = filename;
		doUpdate();
	}
	
	
	/**
	 * Generiert den Java-Code für den Core-Type
	 * 
	 * @param sb   der StringBuilder, in den die neuen Daten für den 
	 *             Core-Type geschrieben werden sollen 
	 */
	protected abstract void generate(StringBuilder sb);
	
	
	/**
	 * Aktualisiert die angegebene Datei in dem Bereich, der mit den Kommentaren
	 * "// GENERIERTER CODE ANFANG" und "\t// GENERIERTER CODE ENDE" gekennzeichnet ist,
	 * mit dem übergebenen Quellcode.
	 */
	protected void doUpdate() { 
		logger.logLn("Aktualisiere CoreType " + name + ":");
		logger.modifyIndent(2);
		
		StringBuilder sb = new StringBuilder();
		sb.append(System.lineSeparator());
		generate(sb);
		final String newData = System.lineSeparator() + sb.toString();

		logger.logLn("- Passe Datei " + filename + " an...");
		if (CoreTypeUpdater.path == null) {
			logger.logLn(LogLevel.ERROR, "FEHLER: Der Pfad, wo die Core-Types liegen wurde nicht gesetzt - Die Methode CoreTypeUpdater.setPath(String path) muss vorher aufgerufen werden!");
		}
		String filename = CoreTypeUpdater.path + CoreTypeUpdater.packagePath + this.filename;
		var path = Paths.get(filename);
		if (!Files.exists(path)) {
			logger.logLn(LogLevel.ERROR, "FEHLER: Datei " + filename + " existiert nicht!");
		} else {
			try {
				String data = Files.readString(path, StandardCharsets.UTF_8);
				final String strStart = "// GENERIERTER CODE ANFANG";
				final String strEnde = "\t// GENERIERTER CODE ENDE";
				int start = data.indexOf(strStart) + strStart.length();
				start += System.lineSeparator().length();
				int ende = data.indexOf(strEnde);
				Files.writeString(path, data.substring(0, start) + newData + data.substring(ende), StandardCharsets.UTF_8);
			} catch (IOException e) {
				logger.logLn(LogLevel.ERROR, "FEHLER: Fehler beim Zugriff auf die Datei " + filename + "!");
			}
		}

		logger.logLn("Aktualisierung beendet.");
		logger.modifyIndent(-2);
	}


	/**
	 * Setzt den Pfad, in dem sich die Java-Dateien des SVWS-Core-Teilprojektes befinden
	 *  
	 * @param path    der Pfad, in dem sich die Java-Dateien des SVWS-Core-Teilprojektes befinden
	 * 
	 * @return true, falls der Pfad existiert und ein Verzeichnis ist und ansonsten false
	 */
	public static boolean setPath(String path) {
		try {
			if (!Files.isDirectory(Paths.get(path))) {
				return false;
			}
		} catch (InvalidPathException e) {
			return false;
		}
		CoreTypeUpdater.path = path + ((path.endsWith("/") ? "" : "/"));
		return true;
	}
	
}
