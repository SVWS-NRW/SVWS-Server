package de.svws_nrw.data.gost;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.data.SimpleBinaryMultipartBody;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.lupo.mdb.LupoMDB;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse stellt Methoden für den Import und Export von Kurs 42 - Blockungen
 * zur Vefügung.
 */
public final class DataKurs42 {

	private DataKurs42() {
		throw new IllegalStateException("Instantiation of " + DataKurs42.class.getName() + " not allowed");
	}

	private static final Random _random = new Random();

    /**
     * Importiert die in dem Multipart übergebene Datei.
     *
     * @param conn        die Datenbank-Verbindung
     * @param multipart   der Multipart-Body mmit der Datei
     *
     * @return die HTTP-Response mit dem Log
     */
    public static Response importZip(final DBEntityManager conn, final SimpleBinaryMultipartBody multipart) {
    	final Logger logger = new Logger();
    	final LogConsumerList log = new LogConsumerList();
    	logger.addConsumer(log);
    	logger.addConsumer(new LogConsumerConsole());

    	// Erstelle temporär eine Zip-Datei aus dem übergebenen Byte-Array
    	final String tmpDirectory = SVWSKonfiguration.get().getTempPath();
        final String tmpFilename = conn.getUser().connectionManager.getConfig().getDBSchema() +  "_" + _random.ints(48, 123)  // from 0 to z
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))  // filter some unicode characters
          .limit(40)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString() + ".zip";
        logger.logLn("Erstelle eine temporäres Verzechnis mit dem Inhalt der Zip-Datei unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\"");
		final Path path = Paths.get(tmpDirectory).resolve(tmpFilename);
    	try {
    		Files.createDirectories(path);
    		try (ZipInputStream zipInput = new ZipInputStream(new ByteArrayInputStream(multipart.data))) {
	    		ZipEntry zipFile;
	    		while ((zipFile = zipInput.getNextEntry()) != null) {
	    			final String filename = switch (zipFile.getName()) {
	    				case "Blockung.txt", "Schueler.txt", "Faecher.txt", "Kurse.txt", "Schienen.txt", "Blockplan.txt", "Fachwahlen.txt" -> zipFile.getName();
	    				default -> null;
	    			};
	    			if (filename != null) {
	    				final Path filePath = path.resolve(filename);
	    				Files.writeString(filePath, zipFile.toString(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
	    			}
	    			zipInput.closeEntry();
	    		}
    		}
		} catch (@SuppressWarnings("unused") final IOException e) {
			logger.logLn(2, "Fehler beim Erstellen der temporären Zip-Datei unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\"");
			final SimpleOperationResponse daten = new SimpleOperationResponse();
			daten.log = log.getStrings();
			return Response.status(Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(daten).build();
		}

    	logger.logLn("Importiere die Blockung mithilfe der extrahierten Daten:");

		final LupoMDB lupoMDB = new LupoMDB(tmpDirectory + "/" + tmpFilename);
		lupoMDB.logger.copyConsumer(logger);
		try {
			DBUtilsGostBlockung.importKurs42(conn, logger, path);
			logger.logLn("  Import beendet");
		} catch (@SuppressWarnings("unused") final IOException e1) {
			logger.logLn("  [FEHLER] beim Zugriff auf die Text-Datien.");
		} finally {
			// Entferne die temporär angelegten Dateien wieder...
			logger.logLn("Löschen des temporären Verzeichnis \"" + tmpDirectory + "/" + tmpFilename + "\".");
			try {
				final Path pSchueler = path.resolve("Schueler.txt");
				if (Files.exists(pSchueler))
					Files.delete(pSchueler);
				final Path pFaecher = path.resolve("Faecher.txt");
				if (Files.exists(pFaecher))
					Files.delete(pFaecher);
				final Path pKurse = path.resolve("Kurse.txt");
				if (Files.exists(pKurse))
					Files.delete(pKurse);
				final Path pSchienen = path.resolve("Schienen.txt");
				if (Files.exists(pSchienen))
					Files.delete(pSchienen);
				final Path pBlockplan = path.resolve("Blockplan.txt");
				if (Files.exists(pBlockplan))
					Files.delete(pBlockplan);
				final Path pFachwahlen = path.resolve("Fachwahlen.txt");
				if (Files.exists(pFachwahlen))
					Files.delete(pFachwahlen);
				Files.delete(path);
				logger.logLn(2, "[OK]");
			} catch (@SuppressWarnings("unused") final IOException e) {
				logger.logLn(2, "[FEHLER]");
			}
		}
		final SimpleOperationResponse daten = new SimpleOperationResponse();
		daten.success = true;
		daten.log = log.getStrings();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
    }

}
