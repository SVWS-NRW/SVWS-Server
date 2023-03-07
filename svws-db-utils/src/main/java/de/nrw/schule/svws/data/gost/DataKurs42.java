package de.nrw.schule.svws.data.gost;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import de.nrw.schule.svws.config.SVWSKonfiguration;
import de.nrw.schule.svws.core.data.SimpleOperationResponse;
import de.nrw.schule.svws.core.logger.LogConsumerConsole;
import de.nrw.schule.svws.core.logger.LogConsumerVector;
import de.nrw.schule.svws.core.logger.Logger;
import de.nrw.schule.svws.data.SimpleBinaryMultipartBody;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.utils.gost.GostBlockungUtils;
import de.nrw.schule.svws.db.utils.lupo.mdb.LupoMDB;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse stellt Methoden für den Import und Export von Kurs 42 - Blockungen 
 * zur Vefügung.   
 */
public class DataKurs42 {

    /**
     * Importiert die in dem Multipart übergebene Datei. 
     * 
     * @param conn        die Datenbank-Verbindung
     * @param multipart   der Multipart-Body mmit der Datei
     * 
     * @return die HTTP-Response mit dem Log
     */
    public static Response importZip(final DBEntityManager conn, SimpleBinaryMultipartBody multipart) {
    	Logger logger = new Logger();
    	LogConsumerVector log = new LogConsumerVector();
    	logger.addConsumer(log);
    	logger.addConsumer(new LogConsumerConsole());
    	
    	// Erstelle temporär eine Zip-Datei aus dem übergebenen Byte-Array
    	Random random = new Random();
    	String tmpDirectory = SVWSKonfiguration.get().getTempPath();
        String tmpFilename = conn.getUser().connectionManager.getConfig().getDBSchema() +  "_" + random.ints(48, 123)  // from 0 to z
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))  // filter some unicode characters
          .limit(40)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString() + ".zip";
        logger.logLn("Erstelle eine temporäres Verzechnis mit dem Inhalt der Zip-Datei unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\"");
		Path path = Paths.get(tmpDirectory + "/" + tmpFilename);
    	try {
    		Files.createDirectories(path);
    		try (ZipInputStream zipInput = new ZipInputStream(new ByteArrayInputStream(multipart.data))) {
	    		ZipEntry zipFile;
	    		while ((zipFile = zipInput.getNextEntry()) != null) {
	    			String filename = switch (zipFile.getName()) {
	    				case "Schueler.txt", "Faecher.txt", "Kurse.txt", "Schienen.txt", "Blockplan.txt", "Fachwahlen.txt" -> zipFile.getName();
	    				default -> null;
	    			};
	    			if (filename != null)
	    				Files.writeString(path, zipFile.toString(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
	    			zipInput.closeEntry();
	    		}
    		}
		} catch (@SuppressWarnings("unused") IOException e) {
			logger.logLn(2, "Fehler beim Erstellen der temporären Zip-Datei unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\"");
			SimpleOperationResponse daten = new SimpleOperationResponse();
			daten.log = log.getStrings();
			return Response.status(Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(daten).build();
		}

    	logger.logLn("Importiere die Blockung mithilfe der extrahierten Daten:");
    	
		LupoMDB lupoMDB = new LupoMDB(tmpDirectory + "/" + tmpFilename); 
		lupoMDB.logger.copyConsumer(logger);
		try {
			GostBlockungUtils.importKurs42(conn, logger, path);
			logger.logLn("  Import beendet");
		} catch (@SuppressWarnings("unused") IOException e1) {
			logger.logLn("  [FEHLER] beim Zugriff auf die Text-Datien.");
		} finally {
			// Entferne die temporär angelegten Dateien wieder...
			logger.logLn("Löschen des temporären Verzeichnis \"" + tmpDirectory + "/" + tmpFilename + "\".");
			try {
				Path pSchueler = path.resolve("Schueler.txt");
				if (Files.exists(pSchueler))
					Files.delete(pSchueler);
				Path pFaecher = path.resolve("Faecher.txt");
				if (Files.exists(pFaecher))
					Files.delete(pFaecher);
				Path pKurse = path.resolve("Kurse.txt");
				if (Files.exists(pKurse))
					Files.delete(pKurse);
				Path pSchienen = path.resolve("Schienen.txt");
				if (Files.exists(pSchienen))
					Files.delete(pSchienen);
				Path pBlockplan = path.resolve("Blockplan.txt");
				if (Files.exists(pBlockplan))
					Files.delete(pBlockplan);
				Path pFachwahlen = path.resolve("Fachwahlen.txt");
				if (Files.exists(pFachwahlen))
					Files.delete(pFachwahlen);
				Files.delete(path);
				logger.logLn(2, "[OK]");
			} catch (@SuppressWarnings("unused") IOException e) {
				logger.logLn(2, "[FEHLER]");
			}			
		}
		SimpleOperationResponse daten = new SimpleOperationResponse();
		daten.success = true;
		daten.log = log.getStrings();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
    }

}
