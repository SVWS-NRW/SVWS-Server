package de.svws_nrw.data.schema;

import java.util.Random;

import de.svws_nrw.base.FileUtils;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.LogConsumerVector;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.utils.schema.DBSchemaManager;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.StreamingOutput;

/**
 * Diese Klasse stellt Methoden für den Import und Export von SQLite-Datenbanken 
 * zur Vefügung.   
 */
public class DataSQLite {

	
    /**
     * Exportiert eine SQLite-Datenbank aus dem aktuellen Schema. Der Aufruf erfordert
     * administrative Rechte.
     *  
     * @param user         der Datenbank-Benutzer
     * @param schemaname   Name des Schemas, in das hinein migriert werden soll
     * 
     * @return Die SQLite-Datenbank
     */
    public static Response exportSQLite(final Benutzer user, String schemaname) {
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
		DBSchemaManager srcManager = DBSchemaManager.create(user, true, logger); 
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
