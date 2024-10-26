package de.svws_nrw.db.utils.lupo.app;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.base.shell.CommandLineException;
import de.svws_nrw.base.shell.CommandLineOption;
import de.svws_nrw.base.shell.CommandLineParser;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnis;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostJahrgangFachkombination;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.db.utils.lupo.mdb.LupoMDB;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt eine Kommandozeilen-Anwendung zur Verfügung, die dem
 * Generieren von Testfällen für die Gymnasiale Oberstufe aus den Laufbahnplanungsdaten
 * eine LuPO-MDB-Datei dient. Diese können im Server-Teilprojekt svws-test-libcore
 * verwendet werden.
 */
public class GenerateTestdatenLaufbahnFromLupoMDB {

	/// Der Logger
	private static final Logger logger = new Logger();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GenerateTestdatenLaufbahnFromLupoMDB() {
		// leer
	}

	/**
	 * Diese Methode schreibt die übergebenen Daten in die angebene Datei.
	 *
	 * @param filename    der Dateiname der Datei, in welche geschrieben werden soll
	 * @param data        die zu schreibenden Daten
	 *
	 * @throws IOException    tritt auf, wenn die Daten nicht erfolgreich geschrieben werden konnten
	 */
	public static void writeTo(final String filename, final String data) throws IOException {
		logger.log("  Schreibe " + filename + "... ");
		final Path path = Paths.get(filename);
		try (InputStream in = IOUtils.toInputStream(data, StandardCharsets.UTF_8)) {
			Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
		}
		logger.logLn("[OK]");
	}


	/**
	 * Generiert Testdaten aus einer LuPO-MDB
	 *
	 * @param args  die Optionen für die Codegenerierung, @see options
	 */
	public static void main(final String[] args) {
		logger.addConsumer(new LogConsumerConsole());
		ASDCoreTypeUtils.initAll();

		// Lese die Kommandozeilenparameter ein
		final CommandLineParser cmdLine = new CommandLineParser(args, logger);
		try {
			cmdLine.addOption(new CommandLineOption("js", "jahrgangStart", true, "Die ID bei der die Nummerierung der Jahrgänge startet (Default: 1)."));
			cmdLine.addOption(new CommandLineOption("f", "file", true, "Der vollständige Dateiname, wo die LuPO-Datei liegt"));

			// Lese den Namen für der LuPO-Datei ein und öffne die Datei
			final String lupofilename = cmdLine.getValue("f", "Laufbahnplanung.lup");
			final LupoMDB lupoMDB = new LupoMDB(lupofilename);
			lupoMDB.logger.copyConsumer(logger);
			lupoMDB.importFrom();

			// Lese die ID für den ersten generierten Jahrgang ein
			int jahrgangID;
			try {
				jahrgangID = Integer.parseInt(cmdLine.getValue("js", "1"));
			} catch (@SuppressWarnings("unused") final NumberFormatException e) {
				jahrgangID = 1;
			}

			// Prüfe die Schulform
			final Schulform schulform = lupoMDB.retrieveSchulform();
			final int schuljahr = 2024; // Nehme das Schuljahr 2024 für den Zugriff auf die Statistikdaten
			if ((schulform == null) || (schulform.daten(schuljahr) == null) || (!schulform.daten(schuljahr).hatGymOb))
				throw new DeveloperNotificationException("Datenbank-Schema enthält keine Daten für die Gymnasiale Oberstufe (Unzulässige Schulform)");

			final String outPath = "../svws-core/src/test/resources/de/svws_nrw/abschluesse/gost/test";
			// Files.createDirectories(Paths.get(outPath));

			final ObjectMapper mapper = new ObjectMapper()
					.enable(SerializationFeature.INDENT_OUTPUT);

			// Lese die Fächerdaten aus der Lupo-Datei und generiere die Testdateien - Fasse dabei alle Daten der Datei zu einem Jahrgang zusammen
			final @NotNull GostJahrgangsdaten gostJahrgangsdaten = new GostJahrgangsdaten();
			final List<GostFach> gostFaecher = lupoMDB.retrieveGostFaecher();
			if ((gostFaecher == null) || (gostFaecher.isEmpty()))
				throw new DeveloperNotificationException("Die Lupo-Datei enthält keine Fächerdefinitionen.");
			final @NotNull List<@NotNull GostJahrgangFachkombination> gostFaecherkombinationen = new ArrayList<>();
			final String strJahrgangID = String.format("%02d", jahrgangID);

			writeTo(outPath + "/Jahrgang_" + strJahrgangID + "_GostFaecher.json", mapper.writeValueAsString(gostFaecher));

			// Lese die Laufbahndaten aus der Lupo-Datei und generiere die Testdateien für die Belegpruefung
			final List<Abiturdaten> alleAbiturdaten = lupoMDB.retrieveAbiturdaten();
			for (final Abiturdaten abiturdaten : alleAbiturdaten) {
				final String strSchuelerID = String.format("%04d", abiturdaten.schuelerID);
				logger.logLn("Generiere Daten für " + strSchuelerID + " des Jahrgangs " + strJahrgangID);

				final GostFaecherManager faecherManager = new GostFaecherManager(schuljahr, gostFaecher, gostFaecherkombinationen);
				AbiturdatenManager manager = new AbiturdatenManager(abiturdaten, gostJahrgangsdaten, faecherManager, GostBelegpruefungsArt.EF1);
				final GostBelegpruefungErgebnis ergebnisEF1 = manager.getBelegpruefungErgebnis();
				manager = new AbiturdatenManager(abiturdaten, gostJahrgangsdaten, faecherManager, GostBelegpruefungsArt.GESAMT);
				final GostBelegpruefungErgebnis ergebnisGesamt = manager.getBelegpruefungErgebnis();

				writeTo(outPath + "/Jahrgang_" + strJahrgangID + "_" + strSchuelerID + "_Abiturdaten.json", mapper.writeValueAsString(abiturdaten));
				writeTo(outPath + "/Jahrgang_" + strJahrgangID + "_" + strSchuelerID + "_Belegpruefungsergebnis_EF1.json",
						mapper.writeValueAsString(ergebnisEF1));
				writeTo(outPath + "/Jahrgang_" + strJahrgangID + "_" + strSchuelerID + "_Belegpruefungsergebnis_Gesamt.json",
						mapper.writeValueAsString(ergebnisGesamt));
			}
			logger.logLn("Fertig!");
		} catch (final CommandLineException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		} catch (final IOException e) {
			cmdLine.printOptionsAndExit(2, e.getMessage());
		}
	}

}
