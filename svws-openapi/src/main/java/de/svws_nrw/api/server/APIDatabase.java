package de.svws_nrw.api.server;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import de.svws_nrw.api.OpenAPIApplication;
import de.svws_nrw.api.schema.DBMultipartBodyDataOnly;
import de.svws_nrw.api.schema.DBMultipartBodyDefaultSchema;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schema.DatenbankVerbindungsdaten;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.schema.DataMigration;
import de.svws_nrw.data.schema.DataSQLite;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf Datenbankoperationen ohne Root-Rechte.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/export/... ,
 * https://{Hostname}/db/{schema}/import/...
 * oder https://{Hostname}/db/{schema}/migrate/...
 */
@Path("/db/{schema}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIDatabase {

    /**
     * Die OpenAPI-Methode für den Export einer SQLite-Datenbank aus dem aktuellen Schema. Der Aufruf erfordert
     * administrative Rechte.
     *
     * @param schemaname    Name des Schemas, in das hinein migriert werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return Die SQLite-Datenbank
     */
    @GET
    @Produces("application/vnd.sqlite3")
    @Path("/export/sqlite")
    @Operation(summary = "Exportiert das aktuelle Schema in eine neu erstellte SQLite-Datenbank.",
               description = "Exportiert das aktuelle Schema in eine neu erstellte SQLite-Datenbank. Der Aufruf erfordert "
               		       + "administrative Rechte.")
    @ApiResponse(responseCode = "200", description = "Der Export der SQLite-Datenbank",
    			 content = @Content(mediaType = "application/vnd.sqlite3",
    			 schema = @Schema(type = "string", format = "binary", description = "LuPO-MDB-Datei")))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht exportiert werden.")
    public Response exportSQLite(@PathParam("schema") final String schemaname, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.ADMIN)) {
    		return DataSQLite.exportSQLite(conn, schemaname);
    	}
    }


    /**
     * Die OpenAPI-Methode für den Import einer SQLite-Datenbank in das aktuellen Schema. Der Aufruf erfordert
     * administrative Rechte auf dem Schema. Die existierenden Daten in diesem Schema werden dabei entfernt
     * und durch die Daten der SQLite-Datenbank ersetzt.
     *
     * @param schemaname    Name des Schemas, auf welches die Abfrage ausgeführt wird und in das importiert werden soll
     * @param multipart     Die SQLite-Datenbank
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return die Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
     */
    @POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/import/sqlite")
    @Operation(summary = "Importiert die übergebene Datenbank in dieses Schema.",
               description = "Importiert die übergebene Datenbank in dieses Schema. Das "
               		       + "Schema wird dabei zunächst geleert und vorhanden Daten gehen dabei verloren.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Importieren der SQLite-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "500", description = "Fehler beim Importieren mit dem Log des fehlgeschlagenen Imports.",
				 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "In das Schema darf nicht importiert werden.")
    public Response importSQLite(@PathParam("schema") final String schemaname,
    		@RequestBody(description = "Die SQLite-Datenbank-Datei", required = true, content =
			@Content(mediaType = MediaType.MULTIPART_FORM_DATA)) @MultipartForm final DBMultipartBodyDataOnly multipart,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.ADMIN)) {
    		return DataSQLite.importSQLite(conn, multipart.database);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Migrieren einer MDB in dieses Schema. Die existierenden Daten in diesem Schema
     * werden dabei entfernt.
     *
     * @param schemaname    Name des Schemas, auf welches die Abfrage ausgeführt wird und in das hinein migriert werden soll
     * @param multipart     die Daten der MDB und das zugehörige Datenbankkennwort
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return die Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
     */
    @POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/migrate/mdb")
    @Operation(summary = "Migriert die übergebene Datenbank in dieses Schema.",
               description = "Migriert die übergebene Datenbank in dieses Schema. Das "
               		       + "Schema wird dabei geleert und vorhanden Daten gehen dabei verloren.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der Access-MDB-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
				 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    public Response migrateMDB(@PathParam("schema") final String schemaname,
    		@RequestBody(description = "Die MDB-Datei", required = true, content =
			@Content(mediaType = MediaType.MULTIPART_FORM_DATA)) @MultipartForm final DBMultipartBodyDefaultSchema multipart,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.ADMIN)) {
    		return DataMigration.migrateMDB(conn, multipart.database, multipart.databasePassword);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Migrieren einer bestehenden MariaDB in ein Schema mit angegebenen Namen.
     * Die existierenden Daten in diesem Schema werden dabei entfernt.
     *
     * @param schemaname         Name des Schemas, auf welches die Abfrage ausgeführt wird und in das hinein migriert werden soll
     * @param verbindungsdaten   Die Verbindungsdaten zur MariaDB
     * @param request            die Informationen zur HTTP-Anfrage
     *
     * @return die Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
     */
    @POST
    @Path("/migrate/mariadb")
    @Operation(summary = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. "
	           		       + "Die Daten in diesem Schema werden ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der MariaDB-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
	 			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public Response migrateMariaDB(@PathParam("schema") final String schemaname,
    		@RequestBody(description = "Die Informationen zum Zugriff auf die Quell- und Zieldatenbank bei der Migration", required = true, content =
        			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DatenbankVerbindungsdaten.class))) final DatenbankVerbindungsdaten verbindungsdaten,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.ADMIN)) {
    		return DataMigration.migrateDBMS(conn, schemaname, DBDriver.MARIA_DB, verbindungsdaten, null);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Migrieren einer bestehenden MariaDB in ein Schema mit angegebenen Namen,
     * wobei nur Daten für die angegebene Schulnummer übertragen werden.
     * Die existierenden Daten in diesem Schema werden dabei entfernt.
     *
     * @param schemaname         Name des Schemas, auf welches die Abfrage ausgeführt wird und in das hinein migriert werden soll
     * @param verbindungsdaten   Die Verbindungsdaten zur MariaDB
     * @param schulnummer        die Schulnummer, für die die Migration durchgeführt wird.
     * @param request            die Informationen zur HTTP-Anfrage
     *
     * @return die Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
     */
    @POST
    @Path("/migrate/mariadb/{schulnummer:\\d{6}}")
    @Operation(summary = "Migriert die Daten für die übergebene Schulnummer aus der übergebenen Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Migriert die Daten für die übergebene Schulnummer aus der übergebenen Datenbank in das Schema mit dem angegebenen Namen. "
               		       + "Die Daten in diesem Schema werden ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der MariaDB-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
	 			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public Response migrateMariaDBSchulnummer(@PathParam("schema") final String schemaname,
    		@PathParam("schulnummer") final int schulnummer,
    		@RequestBody(description = "Die Informationen zum Zugriff auf die Quell- und Zieldatenbank bei der Migration", required = true, content =
        			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DatenbankVerbindungsdaten.class))) final DatenbankVerbindungsdaten verbindungsdaten,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.ADMIN)) {
    		return DataMigration.migrateDBMS(conn, schemaname, DBDriver.MARIA_DB, verbindungsdaten, schulnummer);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Migrieren einer bestehenden MySQL-Datenbank in ein Schema mit angegebenen Namen.
     * Die existierenden Daten in diesem Schema werden dabei entfernt.
     *
     * @param schemaname         Name des Schemas, auf welches die Abfrage ausgeführt wird und in das hinein migriert werden soll
     * @param verbindungsdaten   Die Verbindungsdaten zur MySQL-Datenbank
     * @param request            die Informationen zur HTTP-Anfrage
     *
     * @return die Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
     */
    @POST
    @Path("/migrate/mysql")
    @Operation(summary = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. "
	           		       + "Die Daten in diesem Schema werden ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der MySQL-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
	 			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public Response migrateMySql(@PathParam("schema") final String schemaname,
    		@RequestBody(description = "Die Informationen zum Zugriff auf die Quell- und Zieldatenbank bei der Migration", required = true, content =
        			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DatenbankVerbindungsdaten.class))) final DatenbankVerbindungsdaten verbindungsdaten,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.ADMIN)) {
    		return DataMigration.migrateDBMS(conn, schemaname, DBDriver.MYSQL, verbindungsdaten, null);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Migrieren einer bestehenden MySQL-Datenbank in ein Schema mit angegebenen Namen,
     * wobei nur Daten für die angegebene Schulnummer übertragen werden.
     * Die existierenden Daten in diesem Schema werden dabei entfernt.
     *
     * @param schemaname         Name des Schemas, auf welches die Abfrage ausgeführt wird und in das hinein migriert werden soll
     * @param verbindungsdaten   Die Verbindungsdaten zur MySQL-Datenbank
     * @param schulnummer        die Schulnummer, für die die Migration durchgeführt wird.
     * @param request            die Informationen zur HTTP-Anfrage
     *
     * @return die Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
     */
    @POST
    @Path("/migrate/mysql/{schulnummer:\\d{6}}")
    @Operation(summary = "Migriert die Daten für die übergebene Schulnummer aus der übergebenen Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Migriert die Daten für die übergebene Schulnummer aus der übergebenen Datenbank in das Schema mit dem angegebenen Namen. "
               		       + "Die Daten in diesem Schema werden ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der MySQL-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
	 			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public Response migrateMySqlSchulnummer(@PathParam("schema") final String schemaname,
    		@PathParam("schulnummer") final int schulnummer,
    		@RequestBody(description = "Die Informationen zum Zugriff auf die Quell- und Zieldatenbank bei der Migration", required = true, content =
        			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DatenbankVerbindungsdaten.class))) final DatenbankVerbindungsdaten verbindungsdaten,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.ADMIN)) {
    		return DataMigration.migrateDBMS(conn, schemaname, DBDriver.MYSQL, verbindungsdaten, schulnummer);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Migrieren einer bestehenden SQL-Server-Datenbank in ein Schema mit angegebenen Namen.
     * Die existierenden Daten in diesem Schema werden dabei entfernt.
     *
     * @param schemaname         Name des Schemas, auf welches die Abfrage ausgeführt wird und in das hinein migriert werden soll
     * @param verbindungsdaten   Die Verbindungsdaten zur SQL-Server-Datenbank
     * @param request            die Informationen zur HTTP-Anfrage
     *
     * @return die Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
     */
    @POST
    @Path("/migrate/mssql")
    @Operation(summary = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. "
	           		       + "Die Daten in diesem Schema werden ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der SQL-Server-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
	 			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public Response migrateMsSqlServer(@PathParam("schema") final String schemaname,
    		@RequestBody(description = "Die Informationen zum Zugriff auf die Quell- und Zieldatenbank bei der Migration", required = true, content =
        			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DatenbankVerbindungsdaten.class))) final DatenbankVerbindungsdaten verbindungsdaten,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.ADMIN)) {
    		return DataMigration.migrateDBMS(conn, schemaname, DBDriver.MSSQL, verbindungsdaten, null);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Migrieren einer bestehenden SQL-Server-Datenbank in ein Schema mit angegebenen Namen,
     * wobei nur Daten für die angegebene Schulnummer übertragen werden.
     * Die existierenden Daten in diesem Schema werden dabei entfernt.
     *
     * @param schemaname         Name des Schemas, auf welches die Abfrage ausgeführt wird und in das hinein migriert werden soll
     * @param verbindungsdaten   Die Verbindungsdaten zur SQL-Server-Datenbank
     * @param schulnummer        die Schulnummer, für die die Migration durchgeführt wird.
     * @param request            die Informationen zur HTTP-Anfrage
     *
     * @return die Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
     */
    @POST
    @Path("/migrate/mssql/{schulnummer:\\d{6}}")
    @Operation(summary = "Migriert die Daten für die übergebene Schulnummer aus der übergebenen Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Migriert die Daten für die übergebene Schulnummer aus der übergebenen Datenbank in das Schema mit dem angegebenen Namen. "
               		       + "Die Daten in diesem Schema werden ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der SQL-Server-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
	 			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public Response migrateMsSqlServerSchulnummer(@PathParam("schema") final String schemaname,
    		@PathParam("schulnummer") final int schulnummer,
    		@RequestBody(description = "Die Informationen zum Zugriff auf die Quell- und Zieldatenbank bei der Migration", required = true, content =
        			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DatenbankVerbindungsdaten.class))) final DatenbankVerbindungsdaten verbindungsdaten,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.ADMIN)) {
    		return DataMigration.migrateDBMS(conn, schemaname, DBDriver.MSSQL, verbindungsdaten, schulnummer);
    	}
    }

}
