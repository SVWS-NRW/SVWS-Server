package de.svws_nrw.db.utils.gost.app;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.core.ResteasyContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.base.shell.CommandLineException;
import de.svws_nrw.base.shell.CommandLineOption;
import de.svws_nrw.base.shell.CommandLineParser;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnis;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostJahrgangFachkombination;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.gost.DataGostJahrgangFachkombinationen;
import de.svws_nrw.data.gost.DataGostJahrgangsdaten;
import de.svws_nrw.data.schule.DataSchuleStammdaten;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.gost.GostRepositoryFactory;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.service.benutzer.BenutzerServiceFactory;
import de.svws_nrw.service.crypto.CryptoServiceFactory;
import de.svws_nrw.service.gost.GostAbiturdatenService;
import de.svws_nrw.service.gost.GostServiceFactory;
import de.svws_nrw.service.schueler.SchuelerServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt eine Kommandozeilen-Anwendung zur Verfügung, die dem
 * Generieren von Testfällen für die Gymnasiale Oberstufe aus den Laufbahnplanungsdaten
 * der Gymnasialen Oberstufe einer SVWS-DB dient. Diese können im Server-Teilprojekt
 * svws-test-libcore verwendet werden.
 */
public class GenerateTestdatenLaufbahn {

	private static final HashMap<Integer, String> mapAbiJahrgangToJahrgangID = new HashMap<>();

	private static final HashMap<String, @NotNull GostJahrgangsdaten> mapJahrgangIDToGostJahrgangsdaten = new HashMap<>();

	private static final HashMap<String, GostFaecherManager> mapJahrgangIDToGostFaecher = new HashMap<>();

	private static final HashMap<String, @NotNull List<@NotNull GostJahrgangFachkombination>> mapJahrgangIDToGostFaecherkombinationen = new HashMap<>();

	private static final HashMap<String, String> mapJahrgangIDToJsonGostFaecher = new HashMap<>();

	/** Der Logger */
	private static final Logger logger = new Logger();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GenerateTestdatenLaufbahn() {
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
	 ** Hauptmethode zum Generieren von Testfällen für die Gymnasiale Oberstufe aus den Laufbahnplanungsdaten
     * der Gymnasialen Oberstufe einer SVWS-DB. Diese können im Server-Teilprojekt svws-test-libcore verwendet werden.
	 *
	 * @param args  die Optionen für die Codegenerierung, @see options
	 * @throws ApiOperationException    im Fehlerfall.
	 */
	public static void main(final String[] args) throws ApiOperationException {
		logger.addConsumer(new LogConsumerConsole());
		ASDCoreTypeUtils.initAll();

		// Lese die Kommandozeilenparameter ein
		final CommandLineParser cmdLine = new CommandLineParser(args, logger);
		try {
			cmdLine.addOption(new CommandLineOption("js", "jahrgangStart", true, "Die ID bei der die Nummerierung der Jahrgänge startet (Default: 1)."));
			cmdLine.addOption(new CommandLineOption("cp", "configPath", true,
					"Gibt den Pfad zu der SVWS-Konfigurationsdatei an, wenn diese nicht an einem Standardort liegt."));
			cmdLine.addOption(new CommandLineOption("s", "schema", true, "Der Schema-Name für Datenbank, aus der die Testdaten eingelesen werden sollen."));

			// Lade die Konfigurationsdatei für den Datenbankzugriff
			logger.logLn("Lese SVWS Konfiguration ein...");
			final String svwsconfigPath = cmdLine.getValue("cp", "../svws-server-app/");
			final SVWSKonfiguration svwsconfig = SVWSKonfiguration.getFrom(svwsconfigPath);

			// Lese das Schema ein und erstelle den Datenbankbenutzer für den Zugriff auf das Schema
			final String dbSchema = cmdLine.getValue("s", svwsconfig.getDefaultSchema());
			if (dbSchema == null) {
				throw new IOException("Es wurde kein gültiges Datenbank-Schema zum Einlesen der Laufbahndaten angegeben.");
			}
			final DBConfig dbConfig = svwsconfig.getDBConfig(dbSchema);
			final Benutzer user = Benutzer.create(dbConfig);
			try (DBEntityManager conn = user.getEntityManager()) {
				user.schuleSetStammdaten(DataSchuleStammdaten.getStammdaten(conn));

				final GostAbiturdatenService gostAbiturdatenService = createGostAbiturdatenService(conn);

				// Lese die ID für den ersten generierten Jahrgang ein
				int jahrgangID;
				try {
					jahrgangID = Integer.parseInt(cmdLine.getValue("js", "1"));
				} catch (@SuppressWarnings("unused") final NumberFormatException e) {
					jahrgangID = 1;
				}

				// Prüfe die Schulform
				final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
				if (schule == null) {
					throw new DeveloperNotificationException("Keine Schule angelegt.");
				}
				final DTOSchuljahresabschnitte schuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
				if (schuljahresabschnitt == null) {
					throw new DeveloperNotificationException("Keine gültiger Schuljahresabschnitt vorhanden.");
				}
				final Schulform schulform = Schulform.data().getWertByKuerzel(schule.SchulformKuerzel);
				if ((schulform.daten(schuljahresabschnitt.Jahr) == null) || (!schulform.daten(schuljahresabschnitt.Jahr).hatGymOb)) {
					throw new DeveloperNotificationException("Datenbank-Schema enthält keine Daten für die Gymnasiale Oberstufe (Unzulässige Schulform)");
				}

				final String outPath = "../svws-core/src/test/resources/de/svws_nrw/core/abschluss/gost/belegpruefung/abi2030";
				// Files.createDirectories(Paths.get(outPath));

				final ObjectMapper mapper = new ObjectMapper()
						.enable(SerializationFeature.INDENT_OUTPUT);

				// Lese die Fächerdaten aus der Datenbank und generiere die Testdateien
				final List<DTOGostJahrgangsdaten> jahrgaenge = conn.queryAll(DTOGostJahrgangsdaten.class);
				for (final DTOGostJahrgangsdaten jahrgang : jahrgaenge) {
					if (jahrgang.Abi_Jahrgang < 0) {
						continue;
					}
					try {
						final @NotNull GostJahrgangsdaten gostJahrgangsdaten = DataGostJahrgangsdaten.getJahrgangsdaten(conn, jahrgang.Abi_Jahrgang);
						final GostFaecherManager gostFaecher = DBUtilsFaecherGost.getFaecherManager(schuljahresabschnitt.Jahr, conn, jahrgang.Abi_Jahrgang);
						if (gostFaecher.isEmpty()) {
							continue; // Lasse Jahrgänge ohne Fächerdaten aus
						}
						final @NotNull List<@NotNull GostJahrgangFachkombination> gostFaecherkombinationen =
								DataGostJahrgangFachkombinationen.getFachkombinationen(conn, jahrgang.Abi_Jahrgang);
						final String strJahrgangID = String.format("%02d", jahrgangID++);
						mapAbiJahrgangToJahrgangID.put(jahrgang.Abi_Jahrgang, strJahrgangID);
						mapJahrgangIDToGostJahrgangsdaten.put(strJahrgangID, gostJahrgangsdaten);
						mapJahrgangIDToGostFaecher.put(strJahrgangID, gostFaecher);
						mapJahrgangIDToGostFaecherkombinationen.put(strJahrgangID, gostFaecherkombinationen);

						final String jsonGostFaecher = mapper.writeValueAsString(gostFaecher);
						mapJahrgangIDToJsonGostFaecher.put(strJahrgangID, jsonGostFaecher);
						writeTo(outPath + "/Jahrgang_" + strJahrgangID + "_GostFaecher.json", jsonGostFaecher);
					} catch (@SuppressWarnings("unused") final Exception e) {
						// ignoriere fehlerhafte Jahrgänge
					}
				}

				// Lese die Laufbahndaten aus der Datenbank und generiere die Testdateien für die Belegpruefung
				final List<DTOGostSchueler> schuelerListe = conn.queryAll(DTOGostSchueler.class);
				for (final DTOGostSchueler schueler : schuelerListe) {
					final String strSchuelerID = String.format("%04d", schueler.Schueler_ID);
					Abiturdaten abiturdaten;
					try {
						abiturdaten = gostAbiturdatenService.get(schueler.Schueler_ID);
					} catch (@SuppressWarnings("unused") final Exception e) {
						abiturdaten = null;
					}
					if (abiturdaten == null) {
						continue;
					}
					final String strJahrgangID = mapAbiJahrgangToJahrgangID.get(abiturdaten.abiturjahr);
					final GostJahrgangsdaten gostJahrgangsdaten = mapJahrgangIDToGostJahrgangsdaten.get(strJahrgangID);
					final GostFaecherManager faecherManager = mapJahrgangIDToGostFaecher.get(strJahrgangID);
					if (faecherManager == null) {
						continue;
					}
					faecherManager.addFachkombinationenAll(mapJahrgangIDToGostFaecherkombinationen.get(strJahrgangID));
					logger.logLn("Generiere Daten für " + strSchuelerID + " des Jahrgangs " + strJahrgangID);

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
			}
		} catch (final CommandLineException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		} catch (final IOException e) {
			cmdLine.printOptionsAndExit(2, e.getMessage());
		} catch (final DBException e) {
			cmdLine.printOptionsAndExit(3, e.getMessage());
		}
	}


	/**
	 * Erstellt und initialisiert einen GostAbiturdatenService über Mocking.
	 *
	 * @param conn   die Datenbank-Verbindung
	 *
	 * @return der initialisierte GostAbiturdatenService
	 */
	public static GostAbiturdatenService createGostAbiturdatenService(final DBEntityManager conn) {
		if (conn == null) {
			throw new IllegalArgumentException("Die DB-Verbindung darf nicht null sein.");
		}

		// Erzeuge einen Mocked Request für den RestEasy-Kontext, um die Repositories mit der Verbindung initialisieren zu können
		final HttpServletRequest mockedRequest = (HttpServletRequest) Proxy.newProxyInstance(
				HttpServletRequest.class.getClassLoader(),
				new Class<?>[] { HttpServletRequest.class },
				(proxy, method, args) -> {
					if ("getAttribute".equals(method.getName()) && (args.length == 1) && ("connection".equals(args[0]))) {
						return conn;
					}
					return null;
				}
		);

		try {
			// Erzeuge die einzelnen Repository-Klassen mit dem angepassten Zugriff über den Mocked Request in den Context-Daten
			ResteasyContext.pushContext(HttpServletRequest.class, mockedRequest);
			final BenutzerRepositoryFactory benutzerRepositoryFactory = BenutzerRepositoryFactory.getNewInstance();
			final SchuelerRepositoryFactory schuelerRepositoryFactory = SchuelerRepositoryFactory.getNewInstance();
			final GostServiceFactory gostServiceFactory = GostServiceFactory.getNewInstance(
					GostRepositoryFactory.getNewInstance(),
					schuelerRepositoryFactory,
					LehrerRepositoryFactory.getNewInstance(),
					benutzerRepositoryFactory,
					KatalogRepositoryFactory.getNewInstance(),
					EigeneSchuleRepositoryFactory.getNewInstance(),
					BenutzerServiceFactory.getNewInstance(benutzerRepositoryFactory),
					CryptoServiceFactory.getNewInstance(benutzerRepositoryFactory, schuelerRepositoryFactory),
					SchuelerServiceFactory.getNewInstance(benutzerRepositoryFactory, schuelerRepositoryFactory),
					GostKlausurenRepositoryFactory.getNewInstance()
			);
			return gostServiceFactory.getGostAbiturdatenService();
		} finally {
			// Entferne den Mocked Request wieder aus den Context-Daten - das Repository kennt jetzt die Verbindung
			ResteasyContext.clearContextData();
		}
	}

}
