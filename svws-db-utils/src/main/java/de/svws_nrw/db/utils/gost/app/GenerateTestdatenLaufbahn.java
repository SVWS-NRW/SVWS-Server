package de.svws_nrw.db.utils.gost.app;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.svws_nrw.base.shell.CommandLineException;
import de.svws_nrw.base.shell.CommandLineOption;
import de.svws_nrw.base.shell.CommandLineParser;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnis;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.utils.data.Schule;
import de.svws_nrw.db.utils.gost.FaecherGost;
import de.svws_nrw.db.utils.gost.GostSchuelerLaufbahn;

/**
 * Diese Klasse stellt eine Kommandozeilen-Anwendung zur Verfügung, die dem
 * Generieren von Testfällen für die Gymnasiale Oberstufe aus den Laufbahnplanungsdaten
 * der Gymnasialen Oberstufe einer SVWS-DB dient. Diese können im Server-Teilprojekt
 * svws-test-libcore verwendet werden.
 */
public class GenerateTestdatenLaufbahn {

	private static HashMap<Integer, String> mapAbiJahrgangToJahrgangID = new HashMap<>();

	private static HashMap<String, GostFaecherManager> mapJahrgangIDToGostFaecher = new HashMap<>();

	private static HashMap<String, String> mapJahrgangIDToJsonGostFaecher = new HashMap<>();

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
	public static void writeTo(final String filename, final String data) throws IOException {
		System.out.print("  Schreibe " + filename + "... ");
		final Path path = Paths.get(filename);
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
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		logger.addConsumer(new LogConsumerConsole());

		// Lese die Kommandozeilenparameter ein
		cmdLine = new CommandLineParser(args);
		try {
			cmdLine.addOption(new CommandLineOption("js", "jahrgangStart", true, "Die ID bei der die Nummerierung der Jahrgänge startet (Default: 1)."));
			cmdLine.addOption(new CommandLineOption("cp", "configPath", true, "Gibt den Pfad zu der SVWS-Konfigurationsdatei an, wenn diese nicht an einem Standardort liegt."));
			cmdLine.addOption(new CommandLineOption("s", "schema", true, "Der Schema-Name für Datenbank, aus der die Testdaten eingelesen werden sollen."));

			// Lade die Konfigurationsdatei für den Datenbankzugriff
			logger.logLn("Lese SVWS Konfiguration ein...");
			final String svwsconfigPath = cmdLine.getValue("cp", "../svws-server-app/");
			final SVWSKonfiguration svwsconfig = SVWSKonfiguration.getFrom(svwsconfigPath);

		    // Lese das Schema ein und erstelle den Datenbankbenutzer für den Zugriff auf das Schema
			final String dbSchema = cmdLine.getValue("s", svwsconfig.getDefaultSchema());
			if (dbSchema == null)
				throw new IOException("Es wurde kein gültiges Datenbank-Schema zum Einlesen der Laufbahndaten angegeben.");
			final DBConfig dbConfig = svwsconfig.getDBConfig(dbSchema);
			final Benutzer user = Benutzer.create(dbConfig);
			try (DBEntityManager conn = user.getEntityManager()) {

				// Lese die ID für den ersten generierten Jahrgang ein
				int jahrgangID;
				try {
					jahrgangID = Integer.parseInt(cmdLine.getValue("js", "1"));
				} catch (@SuppressWarnings("unused") final NumberFormatException e) {
					jahrgangID = 1;
				}

				// Prüfe die Schulform
				final Schulform schulform = Schule.queryCached(conn).getSchulform();
		    	if ((schulform.daten == null) || (!schulform.daten.hatGymOb))
		    		throw new DeveloperNotificationException("Datenbank-Schema enthält keine Daten für die Gymnasiale Oberstufe (Unzulässige Schulform)");

		    	final String outPath = "../svws-core/src/test/resources/de/svws_nrw/abschluesse/gost/test";
		    	// Files.createDirectories(Paths.get(outPath));

				final ObjectMapper mapper = new ObjectMapper()
						.enable(SerializationFeature.INDENT_OUTPUT);

				// Lese die Fächerdaten aus der Datenbank und generiere die Testdateien
				final List<DTOGostJahrgangsdaten> jahrgaenge = conn.queryAll(DTOGostJahrgangsdaten.class);
				for (final DTOGostJahrgangsdaten jahrgang : jahrgaenge) {
					final GostFaecherManager gostFaecher = FaecherGost.getFaecherListeGost(conn, jahrgang.Abi_Jahrgang);
					if (gostFaecher.isEmpty())
						continue; // Lasse Jahrgänge ohne Fächerdaten aus
					final String strJahrgangID = String.format("%02d", jahrgangID++);
					mapAbiJahrgangToJahrgangID.put(jahrgang.Abi_Jahrgang, strJahrgangID);
					mapJahrgangIDToGostFaecher.put(strJahrgangID, gostFaecher);

					final String jsonGostFaecher = mapper.writeValueAsString(gostFaecher);
					mapJahrgangIDToJsonGostFaecher.put(strJahrgangID, jsonGostFaecher);
					writeTo(outPath + "/Jahrgang_" + strJahrgangID + "_GostFaecher.json", jsonGostFaecher);
				}

				// Lese die Laufbahndaten aus der Datenbank und generiere die Testdateien für die Belegpruefung
				final List<DTOGostSchueler> schuelerListe = conn.queryAll(DTOGostSchueler.class);
				for (final DTOGostSchueler schueler : schuelerListe) {
					final String strSchuelerID = String.format("%04d", schueler.Schueler_ID);
					Abiturdaten abiturdaten;
					try {
						abiturdaten = GostSchuelerLaufbahn.get(conn, schueler.Schueler_ID);
					} catch (@SuppressWarnings("unused") final Exception e) {
						abiturdaten = null;
					}
					if (abiturdaten == null)
						continue;
					final String strJahrgangID = mapAbiJahrgangToJahrgangID.get(abiturdaten.abiturjahr);
					final GostFaecherManager gostFaecher = mapJahrgangIDToGostFaecher.get(strJahrgangID);
					System.out.println("Generiere Daten für " + strSchuelerID + " des Jahrgangs " + strJahrgangID);

					AbiturdatenManager manager = new AbiturdatenManager(abiturdaten, gostFaecher.toVector(), GostBelegpruefungsArt.EF1);
					final GostBelegpruefungErgebnis ergebnisEF1 = manager.getBelegpruefungErgebnis();
					manager = new AbiturdatenManager(abiturdaten, gostFaecher.toVector(), GostBelegpruefungsArt.GESAMT);
					final GostBelegpruefungErgebnis ergebnisGesamt = manager.getBelegpruefungErgebnis();

					writeTo(outPath + "/Jahrgang_" + strJahrgangID + "_" + strSchuelerID + "_Abiturdaten.json", mapper.writeValueAsString(abiturdaten));
					writeTo(outPath + "/Jahrgang_" + strJahrgangID + "_" + strSchuelerID + "_Belegpruefungsergebnis_EF1.json", mapper.writeValueAsString(ergebnisEF1));
					writeTo(outPath + "/Jahrgang_" + strJahrgangID + "_" + strSchuelerID + "_Belegpruefungsergebnis_Gesamt.json", mapper.writeValueAsString(ergebnisGesamt));
				}
				System.out.println("Fertig!");
			}
		} catch (final CommandLineException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		} catch (final IOException e) {
			cmdLine.printOptionsAndExit(2, e.getMessage());
		}
	}

}
