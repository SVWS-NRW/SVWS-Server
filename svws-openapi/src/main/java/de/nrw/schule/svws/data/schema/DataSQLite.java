package de.nrw.schule.svws.data.schema;

import java.util.Random;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.StreamingOutput;

import de.nrw.schule.svws.config.SVWSKonfiguration;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.utils.schema.DBSchemaManager;
import de.nrw.schule.svws.logger.LogConsumerConsole;
import de.nrw.schule.svws.logger.LogConsumerVector;
import de.nrw.schule.svws.logger.Logger;
import de.nrw.schule.svws.utils.FileUtils;

/**
 * Diese Klasse stellt Methoden für den Import und Export von SQLite-Datenbanken 
 * zur Vefügung.   
 */
public class DataSQLite {

	
    /**
     * Exportiert eine SQLite-Datenbank aus dem aktuellen Schema. Der Aufruf erfordert
     * administrative Rechte.
     *  
     * @param conn         die Verbindung zur Datenbank
     * @param schemaname   Name des Schemas, in das hinein migriert werden soll
     * 
     * @return Die SQLite-Datenbank
     */
    public static Response exportSQLite(final DBEntityManager conn, String schemaname) {
    	Logger logger = new Logger();
    	LogConsumerVector log = new LogConsumerVector();
    	logger.addConsumer(log);
    	logger.addConsumer(new LogConsumerConsole());

    	// Bestimme den Dateinamen für eine temporäre SQLite-Datei
    	Random random = new Random();
    	String tmpDirectory = SVWSKonfiguration.get().getTempPath();
        String tmpFilename = schemaname +  "_" + random.ints(48, 123)  // from 0 to z
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))  // filter some unicode characters
          .limit(40)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString() + ".sqlite";
        logger.logLn("Erstelle eine SQLite-Datenbank unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\"");
    	
		// Erzeuge einen Schema-Manager, der den Export des DB-Schema durchführt
		DBSchemaManager srcManager = DBSchemaManager.create(conn, true, logger); 
		if (srcManager == null)
			throw new WebApplicationException(Status.FORBIDDEN.getStatusCode());
		
		// Führe den Export mithilfe des Schema-Managers durch.
		logger.modifyIndent(2);
		srcManager.backup.exportDB(tmpDirectory + "/" + tmpFilename, logger);
		logger.modifyIndent(-2);
        
        // Lese die Datenbank in die Response ein
		logger.logLn("Lese die temporären SQLite-Datenbank unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\" ein.");
		Response response = Response.ok((StreamingOutput) output -> {
			try {
				FileUtils.move(tmpDirectory + "/" + tmpFilename, output);
				output.flush();
			} catch (Exception e) { e.printStackTrace(); }
        }).header("Content-Disposition", "attachment; filename=\"" + schemaname + ".sqlite\"").build();
		if (!response.hasEntity())
			logger.logLn(2, "[FEHLER]");			
		logger.logLn("Datei eingelesen.");
        
		return response;
    }
	
}
