package de.svws_nrw.data.gost;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

import de.svws_nrw.base.FileUtils;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.data.SimpleBinaryMultipartBody;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.utils.lupo.mdb.LupoMDB;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.StreamingOutput;

/**
 * Diese Klasse stellt Methoden für den Import und Export von LuPO-MDB-Datenbanken
 * zur Vefügung.
 */
public final class DataLupo {

	private DataLupo() {
		throw new IllegalStateException("Instantiation of " + DataLupo.class.getName() + " not allowed");
	}

	private static final Random random = new Random();

    /**
     * Importiert die in dem Multipart übergebene Datei.
     *
     * @param user               der Datenbank-Benutzer
     * @param multipart          der Multipart-Body mmit der Datei
	 * @param replaceJahrgang    gibt an, ob alte Daten für den Jahrgang der LuPO-Datei ersetzt werden
	 *                           sollen, sofern sie bereits vorhanden sind.
	 * @param replaceSchueler    gibt an, ob alte Daten für die Schüler der LuPO-Datei ersetzt werden
	 *                           sollen, sofern sie bereits vorhanden sind.
     *
     * @return die HTTP-Response mit dem Log
     */
    public static Response importMDB(final Benutzer user, final SimpleBinaryMultipartBody multipart, final boolean replaceJahrgang, final boolean replaceSchueler) {
    	final Logger logger = new Logger();
    	final LogConsumerList log = new LogConsumerList();
    	logger.addConsumer(log);
    	logger.addConsumer(new LogConsumerConsole());

    	// Erstelle temporär eine LuPO-MDB-Datei aus dem übergebenen Byte-Array
    	final String tmpDirectory = SVWSKonfiguration.get().getTempPath();
        final String tmpFilename = user.connectionManager.getConfig().getDBSchema() +  "_" + random.ints(48, 123)  // from 0 to z
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))  // filter some unicode characters
          .limit(40)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString() + ".lup";
        logger.logLn("Erstelle eine temporäre LuPO-Datenbank unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\"");
    	try {
    		Files.createDirectories(Paths.get(tmpDirectory));
			Files.write(Paths.get(tmpDirectory + "/" + tmpFilename), multipart.data, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
		} catch (@SuppressWarnings("unused") final IOException e) {
			logger.logLn(2, "Fehler beim Erstellen der temporären LuPO-Datenbank unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\"");
			final SimpleOperationResponse daten = new SimpleOperationResponse();
			daten.log = log.getStrings();
			return Response.status(Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(daten).build();
		}

    	logger.logLn("Importiere in die temporäre LuPO-Datenbank:");
		final LupoMDB lupoMDB = new LupoMDB(tmpDirectory + "/" + tmpFilename);
		lupoMDB.logger.copyConsumer(logger);
		try {
			lupoMDB.importFrom();
			// Schreibe in die LuPO-Datenbank
			lupoMDB.setLUPOTables(user, replaceJahrgang, replaceSchueler);
			logger.logLn("  Import beendet");
		} catch (@SuppressWarnings("unused") final IOException e1) {
			logger.logLn("  [FEHLER] beim Zugriff auf die temporäre LuPO-Datenbank.");
		} finally {
			// Entferne die temporär angelegte Datenbank wieder...
			logger.logLn("Löschen der temporären LuPO-Datenbank unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\".");
			try {
				Files.delete(Paths.get(tmpDirectory + "/" + tmpFilename));
				logger.logLn(2, "[OK]");
			} catch (@SuppressWarnings("unused") final IOException e) {
				logger.logLn(2, "[FEHLER]");
			}
		}
		logger.logLn("Fertig!");
		final SimpleOperationResponse daten = new SimpleOperationResponse();
		daten.success = true;
		daten.log = log.getStrings();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
    }


    /**
     * Export eine LuPO-MDB für den angegebenen Jahrgang
     *
     * @param user       der Datenbank-Benutzer
     * @param jahrgang   der Jahrgang, für den die LuPO-Datenbank erstellt werden soll
     *
     * @return die HTTP-Response
     */
    public static Response exportMDB(final Benutzer user, final String jahrgang) {
    	final Logger logger = new Logger();
    	final LogConsumerList log = new LogConsumerList();
    	logger.addConsumer(log);
    	logger.addConsumer(new LogConsumerConsole());

    	// Bestimme den Dateinamen für eine temporäre LuPO-Datei
    	final String tmpDirectory = SVWSKonfiguration.get().getTempPath();
        final String tmpFilename = user.connectionManager.getConfig().getDBSchema() +  "_" + random.ints(48, 123)  // from 0 to z
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))  // filter some unicode characters
          .limit(40)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString() + ".lup";

        // Exportiere
        logger.logLn("Exportiere die Laufbahndaten in eine temporäre LuPO-Datenbank unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\": ");
		final LupoMDB lupoMDB = new LupoMDB(tmpDirectory + "/" + tmpFilename);
		lupoMDB.logger.copyConsumer(logger);
		lupoMDB.getFromLeistungsdaten(user, jahrgang);
		try {
			lupoMDB.exportTo();
		} catch (@SuppressWarnings("unused") final IOException e1) {
			// TODO Stelle das Löschen einer ggf. schon erstellten temporären LuPO-Datei sicher
			try {
				Files.deleteIfExists(Paths.get(tmpDirectory + "/" + tmpFilename));
			} catch (@SuppressWarnings("unused") final IOException e) {
				logger.logLn("Kann die temporäre Datei nicht entfernen");
			}
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		}

        // Lese die Datenbank in die Response ein
		logger.logLn("Lese die temporären LuPO-Datei unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\" ein.");
		final Response response = Response.ok((StreamingOutput) output -> {
			try {
				FileUtils.move(tmpDirectory + "/" + tmpFilename, output);
				output.flush();
			} catch (final Exception e) {
				e.printStackTrace();
			}
        }).header("Content-Disposition", "attachment; filename=\"" + user.connectionManager.getConfig().getDBSchema() + jahrgang + ".lup\"").build();
		if (!response.hasEntity())
			logger.logLn(2, "[FEHLER]");
		logger.logLn("LuPO-Datei für die Antwort eingelesen.");
		return response;
    }

}
