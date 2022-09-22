package de.nrw.schule.svws.db.utils.lupo.app;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.nrw.schule.svws.core.abschluss.gost.AbiturdatenManager;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefungErgebnis;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefungsArt;
import de.nrw.schule.svws.core.data.gost.Abiturdaten;
import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.types.statkue.Schulform;
import de.nrw.schule.svws.db.utils.lupo.mdb.LupoMDB;
import de.nrw.schule.svws.logger.LogConsumerConsole;
import de.nrw.schule.svws.logger.Logger;
import de.nrw.schule.svws.shell.CommandLineException;
import de.nrw.schule.svws.shell.CommandLineOption;
import de.nrw.schule.svws.shell.CommandLineParser;

/**
 * Diese Klasse stellt eine Kommandozeilen-Anwendung zur Verfügung, die dem 
 * Generieren von Testfällen für die Gymnasiale Oberstufe aus den Laufbahnplanungsdaten 
 * eine LuPO-MDB-Datei dient. Diese können im Server-Teilprojekt svws-test-libcore 
 * verwendet werden.
 */
public class GenerateTestdatenLaufbahnFromLupoMDB {
	
	/// Der Parser für die Kommandozeile
	private static CommandLineParser cmdLine;

	/// Der Logger
	private static Logger logger = new Logger();


	/**
	 * Diese Methode schreibt die übergebenen Daten in die angebene Datei.
	 * 
	 * @param filename    der Dateiname der Datei, in welche geschrieben werden soll
	 * @param data        die zu schreibenden Daten
	 * 
	 * @throws IOException    tritt auf, wenn die Daten nicht erfolgreich geschrieben werden konnten
	 */
	public static void writeTo(String filename, String data) throws IOException {
		System.out.print("  Schreibe " + filename + "... ");
		Path path = Paths.get(filename);
		try (InputStream in = IOUtils.toInputStream(data, StandardCharsets.UTF_8)) {
			Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
			in.close();
		}
		System.out.println("[OK]");
	}


	/**
	 * 
	 *   
	 * @param args  die Optionen für die Codegenerierung, @see options
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.addConsumer(new LogConsumerConsole());
		
		// Lese die Kommandozeilenparameter ein
		cmdLine = new CommandLineParser(args);
		try {
			cmdLine.addOption(new CommandLineOption("js", "jahrgangStart", true, "Die ID bei der die Nummerierung der Jahrgänge startet (Default: 1)."));
			cmdLine.addOption(new CommandLineOption("f", "file", true, "Der vollständige Dateiname, wo die LuPO-Datei liegt"));
			
		    // Lese den Namen für der LuPO-Datei ein und öffne die Datei
			String lupofilename = cmdLine.getValue("f", "Laufbahnplanung.lup");
			LupoMDB lupoMDB = new LupoMDB(lupofilename); 
			lupoMDB.logger.copyConsumer(logger);
			lupoMDB.importFrom();
			
			// Lese die ID für den ersten generierten Jahrgang ein
			int jahrgangID = 1; 
			try {
				jahrgangID = Integer.parseInt(cmdLine.getValue("js", "1"));
			} catch (NumberFormatException e) {
			}
			
			// Prüfe die Schulform
			Schulform schulform = lupoMDB.retrieveSchulform();
			if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
	    		throw new RuntimeException("Datenbank-Schema enthält keine Daten für die Gymnasiale Oberstufe (Unzulässige Schulform)");
	    	
	    	String outPath = "../svws-core/src/test/resources/de/nrw/schule/svws/abschluesse/gost/test";
	    	// Files.createDirectories(Paths.get(outPath));

			ObjectMapper mapper = new ObjectMapper()
					.enable(SerializationFeature.INDENT_OUTPUT);
	    	
			// Lese die Fächerdaten aus der Lupo-Datei und generiere die Testdateien - Fasse dabei alle Daten der Datei zu einem Jahrgang zusammen 
			List<GostFach> gostFaecher = lupoMDB.retrieveGostFaecher();
			if ((gostFaecher == null) || (gostFaecher.size() == 0))
				throw new RuntimeException("Die Lupo-Datei enthält keine Fächerdefinitionen.");
			String strJahrgangID = String.format("%02d", jahrgangID++);
			
			writeTo(outPath + "/Jahrgang_" + strJahrgangID + "_GostFaecher.json", mapper.writeValueAsString(gostFaecher));
			
			// Lese die Laufbahndaten aus der Lupo-Datei und generiere die Testdateien für die Belegpruefung
			List<Abiturdaten> alleAbiturdaten = lupoMDB.retrieveAbiturdaten();
			for (Abiturdaten abiturdaten : alleAbiturdaten) {
				String strSchuelerID = String.format("%04d", abiturdaten.schuelerID);
				System.out.println("Generiere Daten für " + strSchuelerID + " des Jahrgangs " + strJahrgangID);
				
				AbiturdatenManager manager = new AbiturdatenManager(abiturdaten, gostFaecher, GostBelegpruefungsArt.EF1);
				GostBelegpruefungErgebnis ergebnisEF1 = manager.getBelegpruefungErgebnis();
				manager = new AbiturdatenManager(abiturdaten, gostFaecher, GostBelegpruefungsArt.GESAMT);
				GostBelegpruefungErgebnis ergebnisGesamt = manager.getBelegpruefungErgebnis();
				
				writeTo(outPath + "/Jahrgang_" + strJahrgangID + "_" + strSchuelerID + "_Abiturdaten.json", mapper.writeValueAsString(abiturdaten));
				writeTo(outPath + "/Jahrgang_" + strJahrgangID + "_" + strSchuelerID + "_Belegpruefungsergebnis_EF1.json", mapper.writeValueAsString(ergebnisEF1));
				writeTo(outPath + "/Jahrgang_" + strJahrgangID + "_" + strSchuelerID + "_Belegpruefungsergebnis_Gesamt.json", mapper.writeValueAsString(ergebnisGesamt));
			}
			System.out.println("Fertig!");
		} catch (CommandLineException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		} catch (IOException e) {
			cmdLine.printOptionsAndExit(2, e.getMessage());
		}
	}	

}
