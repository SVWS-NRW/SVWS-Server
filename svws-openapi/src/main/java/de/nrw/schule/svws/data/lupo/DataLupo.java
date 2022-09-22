package de.nrw.schule.svws.data.lupo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import de.nrw.schule.svws.config.SVWSKonfiguration;
import de.nrw.schule.svws.core.data.SimpleOperationResponse;
import de.nrw.schule.svws.data.SimpleBinaryMultipartBody;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.utils.lupo.mdb.LupoMDB;
import de.nrw.schule.svws.logger.LogConsumerConsole;
import de.nrw.schule.svws.logger.LogConsumerVector;
import de.nrw.schule.svws.logger.Logger;
import de.nrw.schule.svws.utils.FileUtils;

/**
 * Diese Klasse stellt Methoden für den Import und Export von LuPO-MDB-Datenbanken 
 * zur Vefügung.   
 */
public class DataLupo {
	
	
    /**
     * Importiert die in dem Multipart übergebene Datei. 
     * 
     * @param conn        die Verbindung zur Datenbank
     * @param multipart   der Multipart-Body mmit der Datei
     * 
     * @return die HTTP-Response mit dem Log
     */
    public static Response importMDB(final DBEntityManager conn, SimpleBinaryMultipartBody multipart) {
    	Logger logger = new Logger();
    	LogConsumerVector log = new LogConsumerVector();
    	logger.addConsumer(log);
    	logger.addConsumer(new LogConsumerConsole());
    	
    	// Erstelle temporär eine LuPO-MDB-Datei aus dem übergebenen Byte-Array
    	Random random = new Random();
    	String tmpDirectory = SVWSKonfiguration.get().getTempPath();
        String tmpFilename = conn.getDBSchema() +  "_" + random.ints(48, 123)  // from 0 to z
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))  // filter some unicode characters
          .limit(40)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString() + ".lup";
        logger.logLn("Erstelle eine temporäre LuPO-Datenbank unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\"");
    	try {
    		Files.createDirectories(Paths.get(tmpDirectory));
			Files.write(Paths.get(tmpDirectory + "/" + tmpFilename), multipart.data, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);			
		} catch (IOException e) {
			logger.logLn(2, "Fehler beim Erstellen der temporären LuPO-Datenbank unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\"");
			SimpleOperationResponse daten = new SimpleOperationResponse();
			daten.log = log.getStrings();
			return Response.status(Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(daten).build();
		}

    	logger.logLn("Importiere in die temporäre LuPO-Datenbank:");
		LupoMDB lupoMDB = new LupoMDB(tmpDirectory + "/" + tmpFilename); 
		lupoMDB.logger.copyConsumer(logger);
		try {
			lupoMDB.importFrom();
			
			// Schreibe in die LuPO-Datenbank
			lupoMDB.setLUPOTables(conn, false);   // TODO konfigurierbar machen !
			logger.logLn("  Import beendet");
		} catch (IOException e1) {
			logger.logLn("  [FEHLER] beim Zugriff auf die temporäre LuPO-Datenbank.");
		} finally {
			// Entferne die temporär angelegte Datenbank wieder...
			logger.logLn("Löschen der temporären LuPO-Datenbank unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\".");
			try {
				Files.delete(Paths.get(tmpDirectory + "/" + tmpFilename));
				logger.logLn(2, "[OK]");
			} catch (IOException e) {
				logger.logLn(2, "[FEHLER]");
			}			
		}
		SimpleOperationResponse daten = new SimpleOperationResponse();
		daten.success = true;
		daten.log = log.getStrings();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
    }
	

    /**
     * Export eine LuPO-MDB für den angegebenen Jahrgang
     * 
     * @param conn       die Verbindung für den Datenbank-Zugriff
     * @param jahrgang   der Jahrgang, für den die LuPO-Datenbank erstellt werden soll
     * 
     * @return die HTTP-Response
     */
    public static Response exportMDB(final DBEntityManager conn, String jahrgang) {
    	Logger logger = new Logger();
    	LogConsumerVector log = new LogConsumerVector();
    	logger.addConsumer(log);
    	logger.addConsumer(new LogConsumerConsole());

    	// Bestimme den Dateinamen für eine temporäre LuPO-Datei
    	Random random = new Random();
    	String tmpDirectory = SVWSKonfiguration.get().getTempPath();
        String tmpFilename = conn.getDBSchema() +  "_" + random.ints(48, 123)  // from 0 to z
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))  // filter some unicode characters
          .limit(40)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString() + ".lup";
        
        // Exportiere
        logger.logLn("Exportiere die Laufbahndaten in eine temporäre LuPO-Datenbank unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\": ");
		LupoMDB lupoMDB = new LupoMDB(tmpDirectory + "/" + tmpFilename);
		lupoMDB.logger.copyConsumer(logger);
		lupoMDB.getFromLeistungsdaten(conn, jahrgang);
		try {
			lupoMDB.exportTo();
		} catch (IOException e1) {
			// TODO Stelle das Löschen einer ggf. schon erstellten temporären LuPO-Datei sicher
			try {
				Files.deleteIfExists(Paths.get(tmpDirectory + "/" + tmpFilename));
			} catch (IOException e) {
			}
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		}
        
        // Lese die Datenbank in die Response ein
		logger.logLn("Lese die temporären LuPO-Datei unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\" ein.");
		Response response = Response.ok((StreamingOutput) output -> {
			try {
				FileUtils.move(tmpDirectory + "/" + tmpFilename, output);
				output.flush();
			} catch (Exception e) { e.printStackTrace(); }
        }).header("Content-Disposition", "attachment; filename=\"" + conn.getDBSchema() + jahrgang + ".lup\"").build();
		if (!response.hasEntity())
			logger.logLn(2, "[FEHLER]");
		logger.logLn("LuPO-Datei für die Antwort eingelesen.");
		return response;
    }	

}
