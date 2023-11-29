package de.svws_nrw.api.privileged;

import java.util.List;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import de.svws_nrw.core.data.BenutzerKennwort;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.benutzer.BenutzerListeEintrag;
import de.svws_nrw.core.data.db.MigrateBody;
import de.svws_nrw.core.data.db.SchemaListeEintrag;
import de.svws_nrw.core.data.schema.DatenbankVerbindungsdaten;
import de.svws_nrw.core.data.schule.SchuleInfo;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.schema.APITempDBFile;
import de.svws_nrw.data.schema.DBUtilsSchema;
import de.svws_nrw.data.schema.DataMigration;
import de.svws_nrw.data.schema.DataSQLite;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.dto.DTOInformationSchema;
import de.svws_nrw.db.schema.dto.DTOInformationUser;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.db.utils.schema.DBMigrationManager;
import de.svws_nrw.db.utils.schema.DBRootManager;
import de.svws_nrw.db.utils.schema.DBSchemaManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf Schemaoperationen mit Root-Rechten.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/api/schema/root/...
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "SchemaPrivileged")
public class APISchemaPrivileged {

	/**
	 * Erzeugt eine einfache Anwort mit der Angabe, ob die Operation erfolgreich war und
	 * mit dem Log derOperation.
	 *
	 * @param success   gibt an, ob die Operation erfolgreich war oder nicht
	 * @param log       der Log der Operation
	 *
	 * @return das Response-Objekt
	 */
	private static SimpleOperationResponse simpleResponse(final boolean success, final LogConsumerList log) {
		final SimpleOperationResponse response = new SimpleOperationResponse();
		response.success = success;
		response.log = log.getStrings();
		return response;
	}


    /**
     * Die OpenAPI-Methode für die Abfrage der Liste aller SVWS-Schemata im DBMS.
     *
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die Liste der vorhandenen SVWS-Schemata in der Datenbank
     */
    @GET
    @Path("/api/schema/liste/svws")
    @Operation(summary = "Liefert eine Liste der SVWS-Schemata.",
    description = "Liefert eine Liste der SVWS-Schemata. Hierfür werden root-Rechte auf der Datenbank benötigt.")
    @ApiResponse(responseCode = "200", description = "Die Schema-Liste mit den Namen und den Versionsinformationen des Schemas",
    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchemaListeEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der angegebene Benutzer besitzt nicht die Rechte, um die SVWS-Schema-Liste der Datenbank auszulesen. Hierfür werden root-Rechte benötigt")
    public List<SchemaListeEintrag> getSVWSSchemaListe(@Context final HttpServletRequest request) {
    	final Benutzer user = DBBenutzerUtils.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    	try (DBEntityManager conn = user.getEntityManager()) {
    		return DBUtilsSchema.getSVWSSchemaListe(conn);
    	}
    }



    /**
     * Die OpenAPI-Methode für die Abfrage der Liste aller Schemata im DBMS.
     *
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return          die Liste der vorhandenen Schemata in der Datenbank
     */
    @GET
    @Path("/api/schema/liste/alle")
    @Operation(summary = "Liefert eine Liste der Schemata.",
    description = "Liefert eine Liste der Schemata. Hierfür werden root-Rechte auf der Datenbank benötigt.")
    @ApiResponse(responseCode = "200", description = "Die Liste mit allen sichtbaren Schema-Namen in der Datenbank",
    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class))))
	@ApiResponse(responseCode = "403", description = "Der angegebene Benutzer besitzt nicht die Rechte, um die Schema-Liste der Datenbank auszulesen. Hierfür werden root-Rechte benötigt")
    public List<String> getSchemaListe(@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
			return DTOInformationSchema.queryNames(conn);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Informationen eines SVWS-Schema bezüglich
     * der Schule.
     *
     * @param schemaname   der Name des SVWS-Schema
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return          die Informationen zur Schule
     */
    @GET
    @Path("/api/schema/liste/info/{schema}/schule")
    @Operation(summary = "Liefert die Informationen zu einer Schule eines SVWS-Schema.",
    	description = "Liefert die Informationen zu einer Schule eines SVWS-Schema. Hierfür werden Datenbank-Rechte auf dem Schema benötigt.")
    @ApiResponse(responseCode = "200", description = "Die Informationen zur Schule",
    	content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchuleInfo.class)))
	@ApiResponse(responseCode = "400", description = "Das angegebene Schema ist kein SVWS-Schema")
	@ApiResponse(responseCode = "403", description = "Der angegebene Benutzer besitzt nicht die Rechte, um die Schul-Informationen abzufragen.")
    public SchuleInfo getSchuleInfo(@PathParam("schema") final String schemaname, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
			return DBUtilsSchema.getSchuleInfo(conn, schemaname);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der administrativen Benutzer in einem aktuellen SVWS-Schema.
     *
     * @param schemaname   der Name des SVWS-Schema
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return          die Liste der administrativen Benutzer
     */
    @GET
    @Path("/api/schema/liste/info/{schema}/admins")
    @Operation(summary = "Liefert die Informationen zu den administrativen Benutzern in einem aktuellen SVWS-Schema.",
    	description = "Liefert die Informationen zu den administrativen Benutzern in einem aktuellen SVWS-Schema.")
    @ApiResponse(responseCode = "200", description = "Die Informationen zu den administrativen Benutzern",
    	content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BenutzerListeEintrag.class))))
	@ApiResponse(responseCode = "400", description = "Das angegebene Schema ist kein aktuelles SVWS-Schema")
	@ApiResponse(responseCode = "403", description = "Der angegebene Benutzer besitzt nicht die Rechte, um die Schul-Informationen abzufragen.")
    public List<BenutzerListeEintrag> getSchemaAdmins(@PathParam("schema") final String schemaname, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
			return DBUtilsSchema.getAdmins(conn, schemaname);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage ob ein Datenbankschema mit bestimmtem Namen bereits existiert.
     *
     * @param schemaname    das Datenbankschema, auf das geprüft werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              Rückmeldung, ob das angegebene Schema existiert
     */
	@GET
	@Path("/api/schema/root/exists/{schema}")
	@Operation(summary = "Liefert die Information, ob ein Schema existiert.",
		description = "Liefert die Information, ob ein Schema existiert. Hierfür werden root-Rechte auf der Datenbank benötigt.")
	@ApiResponse(responseCode = "200", description = "true, wenn das Schema existiert",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
	@ApiResponse(responseCode = "403", description = "Der angegebene Benutzer besitzt nicht die Rechte, um die Schema-Liste der Datenbank auszulesen. Hierfür werden root-Rechte benötigt")
	public boolean existsSchema(@PathParam("schema") final String schemaname, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
	    	final List<String> schemata = DTOInformationSchema.queryNames(conn);
			return schemata.contains(schemaname.toLowerCase());
    	}
	}



    /**
     * Die OpenAPI-Methode für die Abfrage ob ein Datenbankuser mit bestimmtem Namen bereits existiert.
     *
     * @param username    der Datenbankusername, auf den geprüft werden soll
     * @param request     die Informationen zur HTTP-Anfrage
     *
     * @return            Rückmeldung, ob der angegebene User existiert
     */
    @GET
    @Path("/api/schema/root/user/{user}/exists")
    @Operation(summary = "Liefert die Information, ob ein DBMS-User existiert.",
    description = "Liefert die Information, ob ein DBMS-User existiert. Hierfür werden root-Rechte auf der Datenbank benötigt.")
    @ApiResponse(responseCode = "200", description = "true, wenn der Benutzer existiert",
    	content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
	@ApiResponse(responseCode = "403", description = "Der angegebene Benutzer besitzt nicht die Rechte, um die Schema-Liste der Datenbank auszulesen. Hierfür werden root-Rechte benötigt")
    public boolean existsUser(@PathParam("user") final String username, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
	    	final List<String> userlist = DTOInformationUser.queryNames(conn);
			return userlist.contains(username);
    	}
    }

    /**
     * Die OpenAPI-Methode für das angegebene Password für einen Datenbankuser korrekt ist.
     *
     * @param kennwort    der Username und das Kennwort im json, das überprüft werden soll
     * @param request     die Informationen zur HTTP-Anfrage
     *
     * @return            Rückmeldung, ob das angegebene Kennwort korrekt ist
     */
	@POST
	@Path("/api/schema/root/user/checkpwd")
	@Operation(summary = "Prüft, ob das übergebene Kennwort für den Datenbankbenutzer gültig ist.",
		description = "Prüft, ob das übergebene Kennwort für den Datenbankbenutzer gültig ist. Zur Prüfung werden root-Rechte auf der Datenbank benötigt")
	@ApiResponse(responseCode = "200", description = "true, wenn das Kennwort und der Benutzername korrekt sind und den priviligierten Zugriff auf die Datenbankschema erlauben.",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
	@ApiResponse(responseCode = "403", description = "Der angegebene Benutzer besitzt nicht die Rechte, um die Schema-Liste der Datenbank auszulesen. Hierfür werden root-Rechte benötigt")
    public boolean checkDBPassword(@RequestBody(description = "Der Benutzername und das Kennwort für den Datenbankbenutzer", required = true, content =
			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BenutzerKennwort.class))) final BenutzerKennwort kennwort,
    		@Context final HttpServletRequest request) {
    	return DBUtilsSchema.checkDBPassword(kennwort);
    }



    /**
     * Die OpenAPI-Methode für das Anlegen eines Schemas mit angegebenem Namen und in der angegebenen Revision.
     *
     * @param schemaname    der Name des Schemas, das angelegt werden soll
     * @param revision      die Revisionsnummer, auf die das Schema angehoben werden soll
     * @param kennwort      Benutzername und Kennwort mit Root-Rechten
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              Rückmeldung, ob das angegebene Kennwort korrekt ist
     */
    @POST
    @Path("/api/schema/root/create/{schema}/{revision : \\d+}")
    @Operation(summary = "Erstellt ein neues Schema der angegebenen Revision und dem angegebenen Namen.",
		description = "Erstellt ein neues Schema der angegebenen Revision und dem angegebenen Namen, falls keine Schema mit dem angebenen Namen bereits existiert.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Anlegen des Schemas",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "400", description = "Es wurde ein nicht erlaubter Schema-Name, Benutzername oder eine ungültige Revision angegeben.")
	@ApiResponse(responseCode = "403", description = "Der angemeldete Benutzer verfügt nicht über die notwendigen Rechte zum Anlegen eines Schemas.")
	@ApiResponse(responseCode = "404", description = "Die Schema-Datenbank konnte nicht geladen werden. Die Server-Konfiguration ist fehlerhaft.")
    @ApiResponse(responseCode = "500", description = "Der Datenbankzugriff auf das neue Schema mit dem neuen zugehörigen Admin-Benutzer ist fehlgeschlagen oder das SVWS-Schema mit der Revision konnte nicht angelegt werden.",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public SimpleOperationResponse createSchema(@PathParam("schema") final String schemaname,
    						 @PathParam("revision") final long revision,
    						 @RequestBody(description = "Der Benutzername und das Kennwort für den administrativen Zugang zum Schema", required = true, content =
    									@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BenutzerKennwort.class))) final BenutzerKennwort kennwort,
    						 @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
	    	final Logger logger = new Logger();
	    	final LogConsumerList log = new LogConsumerList();
	    	logger.addConsumer(log);
	    	logger.addConsumer(new LogConsumerConsole());

			final long max_revision = SchemaRevisionen.maxRevision.revision;
			long rev = revision;
			if (rev < 0)
				rev = max_revision;
			if (rev > max_revision)
				throw OperationError.BAD_REQUEST.exception(simpleResponse(false, log));

			final DBRootManager root_manager = DBRootManager.create(conn);
			if (root_manager == null)
				throw OperationError.FORBIDDEN.exception(simpleResponse(false, log));

			if ((DBRootManager.isReservedSchemaName(schemaname)) || DBRootManager.isReservedUserName(kennwort.user))
				throw OperationError.BAD_REQUEST.exception(simpleResponse(false, log));

			logger.logLn("Prüfe, ob das Schema bereits existiert...");
			logger.modifyIndent(2);
			if (root_manager.dbSchemaExists(schemaname)) {
				logger.logLn("Fehler: Schema ist bereits vorhanden und kann deswegen nicht neu angelegt werden!");
				return simpleResponse(false, log);
			}
			logger.logLn("ist noch nicht vorhanden");
			logger.modifyIndent(-2);

			final DBConfig dbconfig = new DBConfig(conn.getDBDriver(), conn.getDBLocation(), schemaname, conn.useDBLogin(), kennwort.user, kennwort.password, true, true, 0, 0);
			final boolean success = DBSchemaManager.createNewSchema(dbconfig, conn.getUser().getUsername(), conn.getUser().getPassword(), revision, logger);
			return simpleResponse(success, log);
    	}
    }



    /**
     * Die OpenAPI-Methode für das Anlegen eines Schemas mit angegebenem Namen und in der aktuellen Revision.
     *
     * @param schemaname    der Name des Schemas, das angelegt werden soll
     * @param kennwort      Benutzername und Kennwort mit Root-Rechten
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              Rückmeldung, ob die Operation erfolgreich war
     */
    @POST
    @Path("/api/schema/root/create/{schema}")
    @Operation(summary = "Erstellt ein neues Schema der aktuellen Revision mit dem angegebenen Namen.",
               description = "Erstellt ein neues Schema der aktuellen Revision mit dem angegebenen Namen, falls keines mit dem angebenen Namen bereits existiert.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Anlegen des Schemas",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "400", description = "Es wurde ein nicht erlaubter Schema-Name oder Benutzername angegeben.")
	@ApiResponse(responseCode = "403", description = "Der angemeldete Benutzer verfügt nicht über die notwendigen Rechte zum Anlegen eines Schemas.")
    @ApiResponse(responseCode = "404", description = "Die Schema-Datenbank konnte nicht geladen werden. Die Server-Konfiguration ist fehlerhaft.")
    @ApiResponse(responseCode = "500", description = "Der Datenbankzugriff auf das neue Schema mit dem neuen zugehörigen Admin-Benutzer ist fehlgeschlagen oder das SVWS-Schema mit der aktuellen Revision konnte nicht angelegt werden.")
    public SimpleOperationResponse createSchemaCurrent(@PathParam("schema") final String schemaname,
    						        @RequestBody(description = "Der Benutzername und das Kennwort für den administrativen Zugang zum Schema", required = true, content =
    						    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BenutzerKennwort.class))) final BenutzerKennwort kennwort,
    						        @Context final HttpServletRequest request) {
    	return createSchema(schemaname, -1, kennwort, request);
    }



    /**
     * Die OpenAPI-Methode für das Löschen eines Schemas mit angegebenem Namen, wenn es existiert.
     *
     * @param schemaname    der Name des Schemas, das gelöscht werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              Rückmeldung, ob die Operation erfolgreich war
     */
    @POST
    @Path("/api/schema/root/destroy/{schema}")
    @Operation(summary = "Entfernt das bestehende Schema mit dem angegebenen Namen.",
               description = "Entfernt das Schema mit dem angegebenen Namen, falls es existiert.")
    @ApiResponse(responseCode = "204", description = "Der Log vom Löschen des Schemas")
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht gelöscht werden.")
    @ApiResponse(responseCode = "404", description = "Das angegebene Schema wurde nicht gefunden.")
    public Response destroySchema(@PathParam("schema") final String schemaname, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
	    	// Erzeuge einen Root-Manager zum Löschen des Schemas
			final DBRootManager root_manager = DBRootManager.create(conn);
			if (root_manager == null)
				return OperationError.FORBIDDEN.getResponse();

	    	// Prüfe ob das Schema existiert und lösche das Schema mit dem Root-Manager
			if (!root_manager.dropDBSchemaIfExists(schemaname))
				return OperationError.NOT_FOUND.getResponse();

			return Response.status(Status.NO_CONTENT).build();
    	}
    }



    /**
     * Die OpenAPI-Methode für das Migrieren einer MDB in ein Schema mit angegebenen Namen.
     *
     * @param multipart     Daten der MDB, MDB-Datenbankkennwort, DB-Username und Passwort
     * @param schemaname    Name des Schemas, in das hinein migriert werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              Rückmeldung, ob die Operation erfolgreich war
     */
    @POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/api/schema/root/migrate/mdb/{schema}")
    @Operation(summary = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Sollte "
               		       + "ein Schema mit dem Namen bereits bestehen, so wird es ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der Access-MDB-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
				 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    public SimpleOperationResponse migrateMDB2Schema(@PathParam("schema") final String schemaname,
    		@RequestBody(description = "Die MDB-Datei", required = true, content =
			@Content(mediaType = MediaType.MULTIPART_FORM_DATA)) @MultipartForm final DBMultipartBody multipart,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
	    	final Logger logger = new Logger();
	    	final LogConsumerList log = new LogConsumerList();
	    	logger.addConsumer(log);
	    	logger.addConsumer(new LogConsumerConsole());

	    	try (APITempDBFile mdb = new APITempDBFile(DBDriver.MDB, conn.getDBSchema(), logger, log, multipart.database, multipart.databasePassword, true)) {
		    	logger.logLn("Migriere in die " + conn.getDBDriver() + "-Datenbank unter " + conn.getDBLocation() + ":");
		    	logger.logLn(2, "- verwende den root-benutzer: " + conn.getUser().getUsername());
		    	logger.logLn(2, "- erstelle das DB-Schema: " + schemaname);
		    	logger.logLn(2, "- erstelle den Benutzer \"" + multipart.schemaUsername + "\" für den administrativen Zugriff auf das DB-Schema.");

				final DBConfig srcConfig = mdb.getConfig();
				final DBConfig tgtConfig = new DBConfig(conn.getDBDriver(), conn.getDBLocation(), schemaname, false, multipart.schemaUsername, multipart.schemaUserPassword, true, true, 0, 0);
				if (!DBMigrationManager.migrate(srcConfig, tgtConfig, conn.getUser().getUsername(), conn.getUser().getPassword(), -1, false, null, logger)) {
					logger.logLn(LogLevel.ERROR, 2, "Fehler bei der Migration (driver='" + tgtConfig.getDBDriver() + "', location='" + tgtConfig.getDBLocation() + "', user='" + tgtConfig.getUsername() + "')");
					throw OperationError.INTERNAL_SERVER_ERROR.exception(simpleResponse(false, log));
				}
	    	}

			logger.logLn("Migration abgeschlossen.");
			return simpleResponse(true, log);
    	}
    }


    /**
     * Die OpenAPI-Methode für den Import einer SQLite-Datenbank in ein Schema mit dem angegebenen Namen.
     *
     * @param multipart     SQLite-Datenbank im Binärformat, DB-Username und Passwort für das neue Schema
     * @param schemaname    Name des Schemas, in das hinein migriert werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
     */
    @POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/api/schema/root/import/sqlite/{schema}")
    @Operation(summary = "Importiert die übergebene SQLite-Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Importiert die übergebene SQLite-Datenbank in das Schema mit dem angegebenen Namen. Sollte "
               		       + "ein Schema mit dem Namen bereits bestehen, so wird es ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Import der SQLite-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht importiert werden.")
    @ApiResponse(responseCode = "500", description = "Fehler bei dem Import der SQLite-Datenbank mit dem Log des fehlgeschlagenen Imports.",
	 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public SimpleOperationResponse importSQLite2Schema(@PathParam("schema") final String schemaname,
    		@RequestBody(description = "Die SQLite-Datei", required = true, content =
			@Content(mediaType = MediaType.MULTIPART_FORM_DATA)) @MultipartForm final DBMultipartBodyWithoutDBPassword multipart,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
	    	final Logger logger = new Logger();
	    	final LogConsumerList log = new LogConsumerList();
	    	logger.addConsumer(log);
	    	logger.addConsumer(new LogConsumerConsole());

	    	// Erstelle temporär eine SQLite-Datei aus dem übergebenen Byte-Array
	    	try (APITempDBFile sqlite = new APITempDBFile(DBDriver.SQLITE, conn.getDBSchema(), logger, log, multipart.database, null, true)) {
		    	logger.logLn("Importiere in die " + conn.getDBDriver() + "-Datenbank unter " + conn.getDBLocation() + ":");
		    	logger.logLn(2, "- verwende den root-benutzer: " + conn.getUser().getUsername());
		    	logger.logLn(2, "- erstelle das DB-Schema: " + schemaname);
		    	logger.logLn(2, "- erstelle den Benutzer \"" + multipart.schemaUsername + "\" für den administrativen Zugriff auf das DB-Schema.");

				final int maxUpdateRevision = 3;
				final DBConfig srcConfig = sqlite.getConfig();
				final DBConfig tgtConfig = new DBConfig(conn.getDBDriver(), conn.getDBLocation(), schemaname, false, multipart.schemaUsername, multipart.schemaUserPassword, true, true, 0, 0);

				try {
					final Benutzer srcUser = Benutzer.create(srcConfig);
					try (DBEntityManager srcConn = srcUser.getEntityManager()) {
						if (srcConn == null) {
							logger.logLn(0, " [Fehler]");
							throw new DBException("Fehler beim Verbinden zur SQLite-Export-Datenbank");
						}
						logger.logLn(0, " [OK]");

						final DBSchemaManager srcManager = DBSchemaManager.create(srcUser, true, logger);
						logger.modifyIndent(2);
						if (!srcManager.backup.importDB(tgtConfig, conn.getUser().getUsername(), conn.getUser().getPassword(), maxUpdateRevision, false, logger))
							throw OperationError.INTERNAL_SERVER_ERROR.exception(simpleResponse(false, log));
						logger.modifyIndent(-2);
					}
				} catch (@SuppressWarnings("unused") final DBException e) {
					throw OperationError.INTERNAL_SERVER_ERROR.exception(simpleResponse(false, log));
				}
	    	}

			logger.logLn("Import abgeschlossen.");
			return simpleResponse(true, log);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Migrieren einer bestehenden MariaDB in ein Schema mit angegebenen Namen.
     *
     * @param dbMigrationInfos   Zugangsdaten zur MariaDB, Name des Schema, das angelegt werden soll, Schmea-Username und Passwort
     * @param schemaname         Name des Schemas, in das hinein migriert werden soll
     * @param request            die Informationen zur HTTP-Anfrage
     *
     * @return                   Rückmeldung, ob die Operation erfolgreich war
     */
    @POST
    @Path("/api/schema/root/migrate/mariadb/{schema}")
    @Operation(summary = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Sollte "
               		       + "ein Schema mit dem Namen bereits bestehen, so wird es ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der MariaDB-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
	 			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public SimpleOperationResponse migrateMariaDB2Schema(@PathParam("schema") final String schemaname,
    		@RequestBody(description = "Die Informationen zum Zugriff auf die Quell- und Zieldatenbank bei der Migration", required = true, content =
    				@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = MigrateBody.class))) final MigrateBody dbMigrationInfos,
    		@Context final HttpServletRequest request) {
    	return migrate2Schema(schemaname, DBDriver.MARIA_DB, dbMigrationInfos, request, null);
    }


    /**
     * Die OpenAPI-Methode für das Migrieren aus einer Schild2-MariaDB-Datenbank
     * in ein Schema mit angegebenen Namen, wobei nur Daten für die angegebene Schulnummer
     * übertragen werden.
     *
     * @param dbMigrationInfos   Zugangsdaten zur MariaDB, Name des Schema, das angelegt werden soll, Schmea-Username und Passwort
     * @param schemaname         Name des Schemas, in das hinein migriert werden soll
     * @param request            die Informationen zur HTTP-Anfrage
     * @param schulnummer        die Schulnummer, für die die Migration durchgeführt wird.
     *
     * @return                   Rückmeldung, ob die Operation erfolgreich war
     */
    @POST
    @Path("/api/schema/root/migrate/mariadb/{schema}/{schulnummer:\\d{6}}")
    @Operation(summary = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Sollte "
               		       + "ein Schema mit dem Namen bereits bestehen, so wird es ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der MariaDB-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
	 			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public SimpleOperationResponse migrateMariaDB2SchemaSchulnummer(@PathParam("schema") final String schemaname, @PathParam("schulnummer") final int schulnummer,
    		@RequestBody(description = "Die Informationen zum Zugriff auf die Quell- und Zieldatenbank bei der Migration", required = true, content =
    				@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = MigrateBody.class))) final MigrateBody dbMigrationInfos,
    		@Context final HttpServletRequest request) {
    	return migrate2Schema(schemaname, DBDriver.MARIA_DB, dbMigrationInfos, request, schulnummer);
    }


    /**
     * Die OpenAPI-Methode für das Migrieren einer bestehenden MySQL in ein Schema mit angegebenen Namen.
     *
     * @param dbMigrationInfos   Zugangsdaten zur MySQL, Name des Schema, das angelegt werden soll, Schmea-Username und Passwort
     * @param schemaname         Name des Schemas, in das hinein migriert werden soll
     * @param request            die Informationen zur HTTP-Anfrage
     *
     * @return                   Rückmeldung, ob die Operation erfolgreich war
     */
    @POST
    @Path("/api/schema/root/migrate/mysql/{schema}")
    @Operation(summary = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Sollte "
               		       + "ein Schema mit dem Namen bereits bestehen, so wird es ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der MySQL-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
	 			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public SimpleOperationResponse migrateMySQL2Schema(@PathParam("schema") final String schemaname,
    		@RequestBody(description = "Die Informationen zum Zugriff auf die Quell- und Zieldatenbank bei der Migration", required = true, content =
    				@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = MigrateBody.class))) final MigrateBody dbMigrationInfos,
    		@Context final HttpServletRequest request) {
    	return migrate2Schema(schemaname, DBDriver.MYSQL, dbMigrationInfos, request, null);
    }


    /**
     * Die OpenAPI-Methode für das Migrieren aus einer Schild2-MySQL-Datenbank
     * in ein Schema mit angegebenen Namen, wobei nur Daten für die angegebene Schulnummer
     * übertragen werden.
     *
     * @param dbMigrationInfos   Zugangsdaten zur MySQL, Name des Schema, das angelegt werden soll, Schmea-Username und Passwort
     * @param schemaname         Name des Schemas, in das hinein migriert werden soll
     * @param request            die Informationen zur HTTP-Anfrage
     * @param schulnummer        die Schulnummer, für die die Migration durchgeführt wird.
     *
     * @return                   Rückmeldung, ob die Operation erfolgreich war
     */
    @POST
    @Path("/api/schema/root/migrate/mysql/{schema}/{schulnummer:\\d{6}}")
    @Operation(summary = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Sollte "
               		       + "ein Schema mit dem Namen bereits bestehen, so wird es ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der MySQL-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
	 			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public SimpleOperationResponse migrateMySQL2SchemaSchulnummer(@PathParam("schema") final String schemaname, @PathParam("schulnummer") final int schulnummer,
    		@RequestBody(description = "Die Informationen zum Zugriff auf die Quell- und Zieldatenbank bei der Migration", required = true, content =
    				@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = MigrateBody.class))) final MigrateBody dbMigrationInfos,
    		@Context final HttpServletRequest request) {
    	return migrate2Schema(schemaname, DBDriver.MYSQL, dbMigrationInfos, request, schulnummer);
    }


    /**
     * Die OpenAPI-Methode für das Migrieren einer bestehenden MSSQL in ein Schema mit angegebenen Namen.
     *
     * @param dbMigrationInfos   Zugangsdaten zur MSSQL, Name des Schema, das angelegt werden soll, Schmea-Username und Passwort
     * @param schemaname         Name des Schemas, in das hinein migriert werden soll
     * @param request            die Informationen zur HTTP-Anfrage
     *
     * @return                   Rückmeldung, ob die Operation erfolgreich war
     */
    @POST
    @Path("/api/schema/root/migrate/mssql/{schema}")
    @Operation(summary = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Sollte "
               		       + "ein Schema mit dem Namen bereits bestehen, so wird es ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der MS-SQL-Server-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
	 			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public SimpleOperationResponse migrateMSSQL2Schema(@PathParam("schema") final String schemaname,
    		@RequestBody(description = "Die Informationen zum Zugriff auf die Quell- und Zieldatenbank bei der Migration", required = true, content =
    				@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = MigrateBody.class))) final MigrateBody dbMigrationInfos,
    		@Context final HttpServletRequest request) {
    	return migrate2Schema(schemaname, DBDriver.MSSQL, dbMigrationInfos, request, null);
    }



    /**
     * Die OpenAPI-Methode für das Migrieren aus einer Schild2-MSSQL-Datenbank
     * in ein Schema mit angegebenen Namen, wobei nur Daten für die angegebene Schulnummer
     * übertragen werden.
     *
     * @param dbMigrationInfos   Zugangsdaten zur MSSQL, Name des Schema, das angelegt werden soll, Schmea-Username und Passwort
     * @param schemaname         Name des Schemas, in das hinein migriert werden soll
     * @param request            die Informationen zur HTTP-Anfrage
     * @param schulnummer        die Schulnummer, für die die Migration durchgeführt wird.
     *
     * @return                   Rückmeldung, ob die Operation erfolgreich war
     */
    @POST
    @Path("/api/schema/root/migrate/mssql/{schema}/{schulnummer:\\d{6}}")
    @Operation(summary = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Sollte "
               		       + "ein Schema mit dem Namen bereits bestehen, so wird es ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der MS-SQL-Server-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
	 			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public SimpleOperationResponse migrateMSSQL2SchemaSchulnummer(@PathParam("schema") final String schemaname, @PathParam("schulnummer") final int schulnummer,
    		@RequestBody(description = "Die Informationen zum Zugriff auf die Quell- und Zieldatenbank bei der Migration", required = true, content =
    				@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = MigrateBody.class))) final MigrateBody dbMigrationInfos,
    		@Context final HttpServletRequest request) {
    	return migrate2Schema(schemaname, DBDriver.MSSQL, dbMigrationInfos, request, schulnummer);
    }



    /**
     * Führt eine Migration in das angegebene Ziel-Schema mit den übergebenen Migrations-Informtionen durch.
     *
     * @param schemaname         der Name des Ziel-Schemas, in welches migriert wird
     * @param srcDbDriver        das DBMS der Quell-Datenbank
     * @param dbMigrationInfos   die Informationen zum Zugriff auf die Quell- und Zieldatenbank bei der Migration
     * @param request            der HTTP-Request
     * @param schulnummer        die Schulnummer, für die die Migration durchgeführt wird oder null, falls keine Filterung bezüglich
     *                           der Schulnummer erfolgen soll
     *
     * @return die Antwort auf die Migrationsanfrage, mit der Information, ob die Migration erfolgreich war oder nicht und dem Log zur Migration
     */
	private static SimpleOperationResponse migrate2Schema(final String schemaname, final DBDriver srcDbDriver, final MigrateBody dbMigrationInfos, @Context final HttpServletRequest request, final Integer schulnummer) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
	    	final Logger logger = new Logger();
	    	final LogConsumerList log = new LogConsumerList();
	    	logger.addConsumer(log);
	    	logger.addConsumer(new LogConsumerConsole());

	    	// Prüfe das angegebene Datenbanksystem für die Quelldatenbank
	    	switch (srcDbDriver) {
				case MARIA_DB, MSSQL, MYSQL:
					logger.logLn("Es wird aus dem Datenbankformat '" + srcDbDriver + "' migriert.");
					break;
				case MDB, SQLITE:
				default:
					logger.logLn("Eine Migration aus dem angegebenen Datenbankformat '" + srcDbDriver + "' wird über diese Schnittstelle nicht unterstützt.");
					return simpleResponse(false, log);
	    	}

	    	logger.logLn("Migriere in die " + conn.getDBDriver() + "-Datenbank unter " + conn.getDBLocation() + ":");
	    	logger.logLn(2, "- verwende den root-benutzer: " + conn.getUser().getUsername());
	    	logger.logLn(2, "- erstelle das DB-Schema: " + schemaname);
	    	logger.logLn(2, "- erstelle den Benutzer \"" + dbMigrationInfos.schemaUsername + "\" für den administrativen Zugriff auf das DB-Schema.");

			final DBConfig srcConfig = new DBConfig(srcDbDriver, dbMigrationInfos.srcLocation, dbMigrationInfos.srcSchema, false, dbMigrationInfos.srcUsername, dbMigrationInfos.srcPassword, true, false, 0, 0);
			final DBConfig tgtConfig = new DBConfig(conn.getDBDriver(), conn.getDBLocation(), schemaname, false, dbMigrationInfos.schemaUsername, dbMigrationInfos.schemaUserPassword, true, true, 0, 0);
			if (!DBMigrationManager.migrate(srcConfig, tgtConfig, conn.getUser().getUsername(), conn.getUser().getPassword(), -1, false, schulnummer, logger)) {
				logger.logLn(LogLevel.ERROR, 2, "Fehler bei der Migration (driver='" + tgtConfig.getDBDriver() + "', location='" + tgtConfig.getDBLocation() + "', user='" + tgtConfig.getUsername() + "')");
				throw OperationError.INTERNAL_SERVER_ERROR.exception(simpleResponse(false, log));
			}

			logger.logLn("Migration abgeschlossen.");
			return simpleResponse(true, log);
    	}
    }



    /**
     * Die OpenAPI-Methode für den Export einer SQLite-Datenbank aus einem Schema. Der Aufruf erfordert
     * einen Datenbank-Benutzer mit den entsprechenden Rechten.
     *
     * @param schemaname    Name des Schemas, welches exportiert werden soll.
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return Die SQLite-Datenbank
     */
    @GET
    @Produces("application/vnd.sqlite3")
    @Path("/api/schema/export/{schema}/sqlite")
    @Operation(summary = "Exportiert das angegebene Schema in eine neu erstellte SQLite-Datenbank.",
               description = "Exportiert das angegebene Schema in eine neu erstellte SQLite-Datenbank. Der Aufruf erfordert "
               		+ "einen Datenbank-Benutzer mit den entsprechenden Rechten.")
    @ApiResponse(responseCode = "200", description = "Der Export der SQLite-Datenbank",
    			 content = @Content(mediaType = "application/vnd.sqlite3",
    			 schema = @Schema(type = "string", format = "binary", description = "Die SQLite-Datei")))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht exportiert werden.")
    public Response exportSQLiteFrom(@PathParam("schema") final String schemaname, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
    		return DataSQLite.exportSQLite(conn, schemaname);
    	}
    }


    /**
     * Die OpenAPI-Methode für den Import einer SQLite-Datenbank aus dem angegebenen Schema. Der Aufruf erfordert
     * einen Datenbank-Benutzer mit den entsprechenden Rechten. Die existierenden Daten in diesem Schema werden dabei entfernt
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
    @Path("/api/schema/import/{schema}/sqlite")
    @Operation(summary = "Importiert die übergebene Datenbank in dieses Schema.",
               description = "Importiert die übergebene Datenbank in dieses Schema. Das "
               		       + "Schema wird dabei zunächst geleert und vorhanden Daten gehen dabei verloren.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Importieren der SQLite-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "500", description = "Fehler beim Importieren mit dem Log des fehlgeschlagenen Imports.",
				 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "In das Schema darf nicht importiert werden.")
    public Response importSQLiteInto(@PathParam("schema") final String schemaname,
    		@RequestBody(description = "Die SQLite-Datenbank-Datei", required = true, content =
			@Content(mediaType = MediaType.MULTIPART_FORM_DATA)) @MultipartForm final DBMultipartBodyDataOnly multipart,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
    		return DataSQLite.importSQLite(conn, multipart.database);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Migrieren einer MDB in das angegebene Schema. Die existierenden Daten in diesem Schema
     * werden dabei entfernt. Der Aufruf erfordert einen Datenbank-Benutzer mit den entsprechenden Rechten.
     *
     * @param schemaname    Name des Schemas, auf welches die Abfrage ausgeführt wird und in das hinein migriert werden soll
     * @param multipart     die Daten der MDB und das zugehörige Datenbankkennwort
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return die Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
     */
    @POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/api/schema/migrate/{schema}/mdb")
    @Operation(summary = "Migriert die übergebene Datenbank in das angegebene Schema.",
               description = "Migriert die übergebene Datenbank in das angegebene Schema. Das "
               		       + "Schema wird dabei geleert und vorhanden Daten gehen dabei verloren.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der Access-MDB-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
				 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    public Response migrateMDBInto(@PathParam("schema") final String schemaname,
    		@RequestBody(description = "Die MDB-Datei", required = true, content =
			@Content(mediaType = MediaType.MULTIPART_FORM_DATA)) @MultipartForm final DBMultipartBodyDefaultSchema multipart,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
    		return DataMigration.migrateMDB(conn, multipart.database, multipart.databasePassword);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Migrieren einer bestehenden MariaDB in ein Schema mit angegebenen Namen.
     * Die existierenden Daten in diesem Schema werden dabei entfernt.
     * Der Aufruf erfordert einen Datenbank-Benutzer mit den entsprechenden Rechten.
     *
     * @param schemaname         Name des Schemas, auf welches die Abfrage ausgeführt wird und in das hinein migriert werden soll
     * @param verbindungsdaten   Die Verbindungsdaten zur MariaDB
     * @param request            die Informationen zur HTTP-Anfrage
     *
     * @return die Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
     */
    @POST
    @Path("/api/schema/migrate/{schema}/mariadb")
    @Operation(summary = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. "
	           		       + "Die Daten in diesem Schema werden ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der MariaDB-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
	 			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public Response migrateMariaDBInto(@PathParam("schema") final String schemaname,
    		@RequestBody(description = "Die Informationen zum Zugriff auf die Quell- und Zieldatenbank bei der Migration", required = true, content =
        			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DatenbankVerbindungsdaten.class))) final DatenbankVerbindungsdaten verbindungsdaten,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
    		return DataMigration.migrateDBMS(conn, DBDriver.MARIA_DB, verbindungsdaten, null);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Migrieren einer bestehenden MariaDB in das angebenene Schema,
     * wobei nur Daten für die angegebene Schulnummer übertragen werden.
     * Die existierenden Daten in diesem Schema werden dabei entfernt.
     * Der Aufruf erfordert einen Datenbank-Benutzer mit den entsprechenden Rechten.
     *
     * @param schemaname         Name des Schemas, auf welches die Abfrage ausgeführt wird und in das hinein migriert werden soll
     * @param verbindungsdaten   Die Verbindungsdaten zur MariaDB
     * @param schulnummer        die Schulnummer, für die die Migration durchgeführt wird.
     * @param request            die Informationen zur HTTP-Anfrage
     *
     * @return die Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
     */
    @POST
    @Path("/api/schema/migrate/{schema}/mariadb/{schulnummer:\\d{6}}")
    @Operation(summary = "Migriert die Daten für die übergebene Schulnummer aus der übergebenen Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Migriert die Daten für die übergebene Schulnummer aus der übergebenen Datenbank in das Schema mit dem angegebenen Namen. "
               		       + "Die Daten in diesem Schema werden ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der MariaDB-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
	 			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public Response migrateMariaDBSchulnummerInto(@PathParam("schema") final String schemaname,
    		@PathParam("schulnummer") final int schulnummer,
    		@RequestBody(description = "Die Informationen zum Zugriff auf die Quell- und Zieldatenbank bei der Migration", required = true, content =
        			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DatenbankVerbindungsdaten.class))) final DatenbankVerbindungsdaten verbindungsdaten,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
    		return DataMigration.migrateDBMS(conn, DBDriver.MARIA_DB, verbindungsdaten, schulnummer);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Migrieren einer bestehenden MySQL-Datenbank in das angebenene Schema.
     * Die existierenden Daten in diesem Schema werden dabei entfernt.
     * Der Aufruf erfordert einen Datenbank-Benutzer mit den entsprechenden Rechten.
     *
     * @param schemaname         Name des Schemas, auf welches die Abfrage ausgeführt wird und in das hinein migriert werden soll
     * @param verbindungsdaten   Die Verbindungsdaten zur MySQL-Datenbank
     * @param request            die Informationen zur HTTP-Anfrage
     *
     * @return die Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
     */
    @POST
    @Path("/api/schema/migrate/{schema}/mysql")
    @Operation(summary = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. "
	           		       + "Die Daten in diesem Schema werden ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der MySQL-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
	 			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public Response migrateMySqlInto(@PathParam("schema") final String schemaname,
    		@RequestBody(description = "Die Informationen zum Zugriff auf die Quell- und Zieldatenbank bei der Migration", required = true, content =
        			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DatenbankVerbindungsdaten.class))) final DatenbankVerbindungsdaten verbindungsdaten,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
    		return DataMigration.migrateDBMS(conn, DBDriver.MYSQL, verbindungsdaten, null);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Migrieren einer bestehenden MySQL-Datenbank in das angebene Schema,
     * wobei nur Daten für die angegebene Schulnummer übertragen werden.
     * Die existierenden Daten in diesem Schema werden dabei entfernt.
     * Der Aufruf erfordert einen Datenbank-Benutzer mit den entsprechenden Rechten.
     *
     * @param schemaname         Name des Schemas, auf welches die Abfrage ausgeführt wird und in das hinein migriert werden soll
     * @param verbindungsdaten   Die Verbindungsdaten zur MySQL-Datenbank
     * @param schulnummer        die Schulnummer, für die die Migration durchgeführt wird.
     * @param request            die Informationen zur HTTP-Anfrage
     *
     * @return die Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
     */
    @POST
    @Path("/api/schema/migrate/{schema}/mysql/{schulnummer:\\d{6}}")
    @Operation(summary = "Migriert die Daten für die übergebene Schulnummer aus der übergebenen Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Migriert die Daten für die übergebene Schulnummer aus der übergebenen Datenbank in das Schema mit dem angegebenen Namen. "
               		       + "Die Daten in diesem Schema werden ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der MySQL-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
	 			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public Response migrateMySqlSchulnummerInto(@PathParam("schema") final String schemaname,
    		@PathParam("schulnummer") final int schulnummer,
    		@RequestBody(description = "Die Informationen zum Zugriff auf die Quell- und Zieldatenbank bei der Migration", required = true, content =
        			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DatenbankVerbindungsdaten.class))) final DatenbankVerbindungsdaten verbindungsdaten,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
    		return DataMigration.migrateDBMS(conn, DBDriver.MYSQL, verbindungsdaten, schulnummer);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Migrieren einer bestehenden SQL-Server-Datenbank in das angegebene Schema.
     * Die existierenden Daten in diesem Schema werden dabei entfernt.
     * Der Aufruf erfordert einen Datenbank-Benutzer mit den entsprechenden Rechten.
     *
     * @param schemaname         Name des Schemas, auf welches die Abfrage ausgeführt wird und in das hinein migriert werden soll
     * @param verbindungsdaten   Die Verbindungsdaten zur SQL-Server-Datenbank
     * @param request            die Informationen zur HTTP-Anfrage
     *
     * @return die Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
     */
    @POST
    @Path("/api/schema/migrate/{schema}/mssql")
    @Operation(summary = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. "
	           		       + "Die Daten in diesem Schema werden ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der SQL-Server-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
	 			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public Response migrateMsSqlServerInto(@PathParam("schema") final String schemaname,
    		@RequestBody(description = "Die Informationen zum Zugriff auf die Quell- und Zieldatenbank bei der Migration", required = true, content =
        			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DatenbankVerbindungsdaten.class))) final DatenbankVerbindungsdaten verbindungsdaten,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
    		return DataMigration.migrateDBMS(conn, DBDriver.MSSQL, verbindungsdaten, null);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Migrieren einer bestehenden SQL-Server-Datenbank in ein Schema mit angegebenen Namen,
     * wobei nur Daten für die angegebene Schulnummer übertragen werden.
     * Die existierenden Daten in diesem Schema werden dabei entfernt.
     * Der Aufruf erfordert einen Datenbank-Benutzer mit den entsprechenden Rechten.
     *
     * @param schemaname         Name des Schemas, auf welches die Abfrage ausgeführt wird und in das hinein migriert werden soll
     * @param verbindungsdaten   Die Verbindungsdaten zur SQL-Server-Datenbank
     * @param schulnummer        die Schulnummer, für die die Migration durchgeführt wird.
     * @param request            die Informationen zur HTTP-Anfrage
     *
     * @return die Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
     */
    @POST
    @Path("/api/schema/migrate/{schema}/mssql/{schulnummer:\\d{6}}")
    @Operation(summary = "Migriert die Daten für die übergebene Schulnummer aus der übergebenen Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Migriert die Daten für die übergebene Schulnummer aus der übergebenen Datenbank in das Schema mit dem angegebenen Namen. "
               		       + "Die Daten in diesem Schema werden ersetzt.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Migrieren der SQL-Server-Datenbank",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht migriert werden.")
    @ApiResponse(responseCode = "500", description = "Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.",
	 			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public Response migrateMsSqlServerSchulnummerInto(@PathParam("schema") final String schemaname,
    		@PathParam("schulnummer") final int schulnummer,
    		@RequestBody(description = "Die Informationen zum Zugriff auf die Quell- und Zieldatenbank bei der Migration", required = true, content =
        			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DatenbankVerbindungsdaten.class))) final DatenbankVerbindungsdaten verbindungsdaten,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
    		return DataMigration.migrateDBMS(conn, DBDriver.MSSQL, verbindungsdaten, schulnummer);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Anlegen eines SVWSM-Schemas in der angegebenen Revision in dem Schema mit angegebenem Namen.
     * Der zur Authentifizierung verwendete Datenbank-Benutzer muss die nötigen Rechte haben.
     *
     * @param schemaname    der Name des Schemas, das angelegt werden soll
     * @param revision      die Revisionsnummer, auf die das Schema angehoben werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Response
     */
    @POST
    @Path("/api/schema/create/{schema}/{revision : \\d+}")
    @Operation(summary = "Erstellt ein neues leeres SVWS-Schema der angegebenen Revision in dem angegebenen existierenden Schema.",
		description = "Erstellt ein neues leeres SVWS-Schema der angegebenen Revision in dem angegebenen existierenden Schema.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Anlegen des Schemas",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "400", description = "Es wurde eine ungültige Revision angegeben.")
	@ApiResponse(responseCode = "403", description = "Der angemeldete Benutzer verfügt nicht über die notwendigen Rechte zum Anlegen eines Schemas.")
	@ApiResponse(responseCode = "404", description = "Die Schema-Datenbank konnte nicht geladen werden. Die Server-Konfiguration ist fehlerhaft.")
    @ApiResponse(responseCode = "500", description = "Der Datenbankzugriff auf das neue Schema mit dem neuen zugehörigen Admin-Benutzer ist fehlgeschlagen oder das SVWS-Schema mit der Revision konnte nicht angelegt werden.",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public SimpleOperationResponse createSchemaInto(@PathParam("schema") final String schemaname,
    						 @PathParam("revision") final long revision,
    						 @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
	    	final Logger logger = new Logger();
	    	final LogConsumerList log = new LogConsumerList();
	    	logger.addConsumer(log);
	    	logger.addConsumer(new LogConsumerConsole());

			final long max_revision = SchemaRevisionen.maxRevision.revision;
			long rev = revision;
			if (rev < 0)
				rev = max_revision;
			if (rev > max_revision)
				throw OperationError.BAD_REQUEST.exception(simpleResponse(false, log));

			final DBConfig dbconfig = new DBConfig(conn.getDBDriver(), conn.getDBLocation(), conn.getDBSchema(), conn.useDBLogin(), conn.getUser().getUsername(), conn.getUser().getPassword(), true, true, 0, 0);
			final boolean success = DBSchemaManager.recycleSchema(dbconfig, revision, logger);
			return simpleResponse(success, log);
    	}
    }



    /**
     * Die OpenAPI-Methode für das Anlegen eines SVWSM-Schemas in der aktuellen Revision in dem Schema mit angegebenem Namen.
     * Der zur Authentifizierung verwendete Datenbank-Benutzer muss die nötigen Rechte haben.
     *
     * @param schemaname    der Name des Schemas, das angelegt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Response
     */
    @POST
    @Path("/api/schema/create/{schema}")
    @Operation(summary = "Erstellt ein neues leeres SVWS-Schema der aktuellen Revision in dem angegebenen existierenden Schema.",
		description = "Erstellt ein neues leeres SVWS-Schema der aktuellen Revision in dem angegebenen existierenden Schema.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Anlegen des Schemas",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der angemeldete Benutzer verfügt nicht über die notwendigen Rechte zum Anlegen eines Schemas.")
    @ApiResponse(responseCode = "404", description = "Die Schema-Datenbank konnte nicht geladen werden. Die Server-Konfiguration ist fehlerhaft.")
    @ApiResponse(responseCode = "500", description = "Der Datenbankzugriff auf das neue Schema mit dem neuen zugehörigen Admin-Benutzer ist fehlgeschlagen oder das SVWS-Schema mit der aktuellen Revision konnte nicht angelegt werden.")
    public SimpleOperationResponse createSchemaCurrentInto(@PathParam("schema") final String schemaname, @Context final HttpServletRequest request) {
    	return createSchemaInto(schemaname, -1, request);
    }


    /**
     * Die OpenAPI-Methode um ein Datenbankschema auf eine bestimmte Revision upzudaten.
     *
     * @param schemaname    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param revision      die Revision, auf die angehoben werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return Logmeldung über den Updatevorgang
     */
    @POST
    @Path("/api/schema/update/{schema}/{revision : \\d+}")
    @Operation(summary = "Aktualisiert das angegebene Schema auf die angegebene Revision.",
    		   description = "Prüft das Schema bezüglich der aktuellen Revision und aktualisiert das Schema ggf. auf die übergebene Revision, sofern "
    		   		       + "diese in der Schema-Definition existiert.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Verlauf des Updates",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "400", description = "Es wurde ein ungültiger Schema-Name oder eine ungültige Revision angegeben.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "404", description = "Die Schema-Datenbank konnte nicht geladen werden. Die Server-Konfiguration ist fehlerhaft.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Es ist ein interner-Server-Fehler aufgetreten.",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public Response updateSchema(@PathParam("schema") final String schemaname, @PathParam("revision") final long revision, @Context final HttpServletRequest request) {
    	final SimpleOperationResponse response = new SimpleOperationResponse();
    	response.success = true;
    	ResponseBuilder rb;
    	try {
	    	// Bestimme den angemeldeten priviligierten Benutzer ...
	    	final Benutzer user = DBBenutzerUtils.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
	    	// ... führe das Update aus ...
	    	final LogConsumerList log = DBUtilsSchema.updateSchema(user, revision);
	    	// ... und gebe den Log zurück
	    	response.log = log.getStrings();
	    	rb = Response.status(Status.OK);
    	} catch (final WebApplicationException wae) {
        	response.success = false;
        	response.log.add("Fehler beim Aktualisieren des Schemas.");
        	@SuppressWarnings("resource")
			final Response resp = wae.getResponse();
        	rb = (resp == null) ? Response.status(Status.INTERNAL_SERVER_ERROR) : Response.fromResponse(resp);
        	wae.printStackTrace();
    	}
    	return rb.type(MediaType.APPLICATION_JSON).entity(response).build();
    }


    /**
     * Die OpenAPI-Methode um ein Datenbankschema auf die neueste Revision upzudaten.
     *
     * @param schemaname    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return Logmeldung über den Updatevorgang
     */
    @POST
    @Path("/api/schema/update/{schema}")
    @Operation(summary = "Aktualisiert das angegebene Schema auf die neueste Revision.",
	    description = "Prüft das Schema bezüglich der aktuellen Revision und aktualisiert das Schema ggf. auf die neueste Revision.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Verlauf des Updates",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "400", description = "Es wurde ein ungültiger Schema-Name oder eine ungültige Revision angegeben.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "404", description = "Die Schema-Datenbank konnte nicht geladen werden. Die Server-Konfiguration ist fehlerhaft.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Es ist ein interner-Server-Fehler aufgetreten.",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    public Response updateSchemaToCurrent(@PathParam("schema") final String schemaname, @Context final HttpServletRequest request) {
    	return updateSchema(schemaname, -1, request);
    }

}
