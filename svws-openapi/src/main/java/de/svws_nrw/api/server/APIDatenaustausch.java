package de.svws_nrw.api.server;

import java.io.InputStream;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import de.svws_nrw.core.data.LongAndStringLists;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.SimpleBinaryMultipartBody;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.datenaustausch.DataKurs42;
import de.svws_nrw.data.datenaustausch.DataLupo;
import de.svws_nrw.data.datenaustausch.DataUntis;
import de.svws_nrw.data.datenaustausch.UntisGPU001MultipartBody;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.utils.ApiOperationException;
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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Import-/Export von Daten
 * aus der SVWS-Datenbank.
 */
@Path("/db/{schema}/datenaustausch")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIDatenaustausch {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIDatenaustausch() {
		// leer
	}

	/**
	 * Die OpenAPI-Methode für den Import einer LuPO-MDB-Datenbank in ein Schema mit dem angegebenen Namen.
	 *
	 * @param schemaname    Name des Schemas, in welches die LuPO-Daten importiert werden sollen
	 * @param multipart     LuPO-MDB-Datenbank im Binärformat
	 * @param mode          der Modus, wie vorhandene Daten ersetzt werden sollen (node, schueler oder all)
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/gost/lupo/import/mdb/jahrgang/replace/{mode}")
	@Operation(summary = "Importiert die Laufbahndaten der übergebenen LuPO-Datenbank in das Schema mit dem angegebenen Namen.",
			description = "Importiert die Laufbahndaten der übergebenen LuPO-Datenbank in das Schema mit dem angegebenen Namen.")
	@ApiResponse(responseCode = "200", description = "Der Log vom Import der Laufbahndaten",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "400", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "409", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der Benutzer hat keine Berechtigung, um die Laufbahndaten zu importieren.")
	public Response setGostLupoImportMDBFuerJahrgang(@PathParam("schema") final String schemaname,
			@PathParam("mode") final String mode,
			@RequestBody(description = "Die LuPO-Datei", required = true,
					content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA)) @MultipartForm final SimpleBinaryMultipartBody multipart,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> {
			if (!("all".equals(mode) || "schueler".equals(mode) || "none".equals(mode)))
				return new ApiOperationException(Status.BAD_REQUEST, "Der Modus zum Ersetzen von Daten muss auf 'none', 'schueler' oder 'all' gesetzt sein")
						.getResponse();
			final Benutzer user = DBBenutzerUtils.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_LUPO_IMPORT);
			final boolean replaceJahrgang = "all".equals(mode);
			final boolean replaceSchueler = "all".equals(mode) || "schueler".equals(mode);
			return DataLupo.importMDB(user, multipart, replaceJahrgang, replaceSchueler);
		}, request,
				ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_LUPO_IMPORT);
	}



	/**
	 * Die OpenAPI-Methode für den Export einer einer LuPO-MDB-Datenbank aus dem aktuellen Schema.
	 *
	 * @param schemaname    Name des Schemas, in das hinein migriert werden soll
	 * @param jahrgang      der Jahrgang, für den die LuPO-Datei erzeugt werden soll.
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die LUPO-Datei im Erfolgsfall
	 */
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/gost/lupo/export/mdb/jahrgang/{jahrgang}")
	@Operation(summary = "Exportiert die Laufbahndaten für den übergebenen Jahrgang in eine LuPO-Lehrerdatei.",
			description = "Exportiert die Laufbahndaten für den übergebenen Jahrgang in eine LuPO-Lehrerdatei.")
	@ApiResponse(responseCode = "200", description = "Die LuPO-Lehrerdatei", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM,
			schema = @Schema(type = "string", format = "binary", description = "LuPO-MDB-Datei")))
	@ApiResponse(responseCode = "403", description = "Der Benutzer hat keine Rechter zum Export der Laufbahndaten.")
	@ApiResponse(responseCode = "500", description = "Ein interner Server-Fehler beim Erzeugen der LuPO-Datei.")
	public Response getGostLupoExportMDBFuerJahrgang(@PathParam("schema") final String schemaname,
			@PathParam("jahrgang") final String jahrgang,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> {
			final Benutzer user = DBBenutzerUtils.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_LUPO_IMPORT);
			return DataLupo.exportMDB(user, jahrgang);
		}, request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_LUPO_IMPORT);
	}


	/**
	 * Die OpenAPI-Methode für den Import einer Kurs42-Blockung, deren Daten in einer ZIP-Datei vorliegen.
	 *
	 * @param multipart     Die Kurs42-Blockung in einer ZIP-Datei
	 * @param schemaname    Name des Schemas, in welches die Kurs 42-Blockung importiert werden sollen
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/gost/kurs42/import/zip")
	@Operation(summary = "Importiert die Kurs 42-Blockung aus dem übergebenen ZIP-File in das Schema mit dem angegebenen Namen.",
			description = "Importiert die Kurs 42-Blockung aus dem übergebenen ZIP-File in das Schema mit dem angegebenen Namen.")
	@ApiResponse(responseCode = "200", description = "Der Log vom Import der Kurs 42-Blockung",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "400", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der Benutzer hat keine Berechtigung, um die Kurs 42-Blockung zu importieren.")
	@ApiResponse(responseCode = "404", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "409", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response importKurs42Blockung(@PathParam("schema") final String schemaname,
			@RequestBody(description = "Die Zip-Datei mit den Textdateien der Kurs 42-Blockung", required = true,
					content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA)) @MultipartForm final SimpleBinaryMultipartBody multipart,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithoutTransaction(conn -> DataKurs42.importZip(conn, multipart), request,
				ServerMode.STABLE,
				BenutzerKompetenz.IMPORT_EXPORT_DATEN_IMPORTIEREN);
	}


	/**
	 * Die OpenAPI-Methode für den Import des Raum-Katalogs aus Kurs42.
	 *
	 * @param multipart     Die Kurs42-Raumdatei (CSV)
	 * @param schemaname    Name des Schemas, in welches die Räume importiert werden sollen
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/gost/kurs42/import/raeume")
	@Operation(summary = "Importiert die Räume aus Kurs 42 in das Schema mit dem angegebenen Namen.",
			description = "Importiert die Räume aus Kurs 42 in das Schema mit dem angegebenen Namen.")
	@ApiResponse(responseCode = "200", description = "Der Log vom Import der Räume aus Kurs 42",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "400", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der Benutzer hat keine Berechtigung, um die Räume aus Kurs 42 zu importieren.")
	@ApiResponse(responseCode = "404", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "409", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response importKurs42Raeume(@PathParam("schema") final String schemaname,
			@RequestBody(description = "Die CSV-Datei Raeume.txt von Kurs 42", required = true,
					content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA)) @MultipartForm final SimpleBinaryMultipartBody multipart,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithoutTransaction(conn -> DataKurs42.importRaeume(conn, multipart), request,
				ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für den Import eines einfachen Untis-Stundenplans, der im Untis-Format GPU001.txt vorliegt.
	 *
	 * @param multipart       Die GPU001.txt
	 * @param schemaname      Name des Schemas, in welches der Untis-Stundenplan importiert werden soll
	 * @param ignoreMissing   gibt an, ob fehlerhafte Unterrichtseinträge ignoriert werden ohne eine Abbruch der Operation zu Erzeugen
	 * @param request         die Informationen zur HTTP-Anfrage
	 *
	 * @return Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/untis/import/gpu001/{ingore_missing:[01]}")
	@Operation(summary = "Importiert den Untis-Stundenplan aus der übergebenen GPU001.txt in das Schema mit dem angegebenen Namen.",
			description = "Importiert den Untis-Stundenplan aus der übergebenen GPU001.txt in das Schema mit dem angegebenen Namen.")
	@ApiResponse(responseCode = "200", description = "Der Log vom Import des Untis-Stundenplans",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "404", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "409", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der Benutzer hat keine Berechtigung, um den Untis-Stundenplan zu importieren.")
	public Response importStundenplanUntisGPU001(@PathParam("schema") final String schemaname,
			@PathParam("ingore_missing") final int ignoreMissing,
			@RequestBody(description = "Die Textdatei GPU001.txt", required = true,
					content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA)) @MultipartForm final UntisGPU001MultipartBody multipart,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> DataUntis.importGPU001(conn, multipart, (ignoreMissing == 1)),
				request, ServerMode.STABLE, BenutzerKompetenz.IMPORT_EXPORT_DATEN_IMPORTIEREN);
	}


	/**
	 * Die OpenAPI-Methode für den Import des Raum-Katalogs aus Untis (GPU005.txt).
	 *
	 * @param multipart     Die Datei GPU005.txt aus Untis (CSV)
	 * @param schemaname    Name des Schemas, in welches die Räume importiert werden sollen
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/untis/import/gpu005")
	@Operation(summary = "Importiert die Räume aus der Untis-Datei GPU005.txt in das Schema mit dem angegebenen Namen.",
			description = "Importiert die Räume aus der Untis-Datei GPU005.txt in das Schema mit dem angegebenen Namen.")
	@ApiResponse(responseCode = "200", description = "Der Log vom Import der Räume aus der Untis-Datei GPU005.txt",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "400", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der Benutzer hat keine Berechtigung, um die Räume aus der Untis-Datei GPU005.txt zu importieren.")
	@ApiResponse(responseCode = "404", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "409", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response importUntisRaeumeGPU005(@PathParam("schema") final String schemaname,
			@RequestBody(description = "Die Untis-Datei GPU005.txt für die Räume", required = true,
					content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA)) @MultipartForm final SimpleBinaryMultipartBody multipart,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithoutTransaction(conn -> DataUntis.importGPU005(conn, multipart), request,
				ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}



	/**
	 * Die OpenAPI-Methode für den Export einer Klassenliste zu einem Schuljahresabschnitt für Untis. Dabei
	 * wird die GPU-Datei GPU003.txt generiert.
	 *
	 * @param schema   das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id       die ID des Schuljahresabschnittes, für den die Klassendaten exportiert werden sollen
	 * @param request  die Informationen zur HTTP-Anfrage
	 *
	 * @return die GPU003-Datei
	 */
	@POST
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/untis/export/klassen/{id : \\d+}")
	@Operation(summary = "Liefert einen Export für die Klassen eines Schuljahresabschnittes (GPU003.txt).",
			description = "Liefert einen Export für die Klassen eines Schuljahresabschnittes (GPU003.txt)."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Exportieren besitzt.")
	@ApiResponse(responseCode = "200", description = "Die GPU003.txt", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM,
			schema = @Schema(type = "string", format = "binary", description = "Die GPU003.txt")))
	@ApiResponse(responseCode = "404", description = "Es wurden nicht alle benötigten Daten für den Export gefunden.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Es ist ein unerwarteter Fehler aufgetreten.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response exportUntisKlassenGPU003(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		return DBBenutzerUtils.runWithTransaction(conn -> {
			try {
				return DataUntis.exportGPU003(conn, logger, id);
			} catch (final ApiOperationException aoe) {
				final SimpleOperationResponse sor = new SimpleOperationResponse();
				sor.log.addAll(log.getStrings());
				throw new ApiOperationException(aoe.getStatus(), aoe, sor, "application/json");
			}
		}, request, ServerMode.STABLE, BenutzerKompetenz.IMPORT_EXPORT_SCHUELERDATEN_EXPORTIEREN);
	}


	/**
	 * Die OpenAPI-Methode für den Export einer Lehrerliste zu einem Schuljahresabschnitt für Untis. Dabei
	 * wird die GPU-Datei GPU004.txt generiert.
	 *
	 * @param schema   das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id       die ID des Schuljahresabschnittes, für den die Lehrerdaten exportiert werden sollen
	 * @param request  die Informationen zur HTTP-Anfrage
	 *
	 * @return die GPU004-Datei
	 */
	@POST
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/untis/export/lehrer/{id : \\d+}")
	@Operation(summary = "Liefert einen Export für die Lehrkräfte eines Schuljahresabschnittes (GPU004.txt).",
			description = "Liefert einen Export für die Lehrkräfte eines Schuljahresabschnittes (GPU004.txt)."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Exportieren besitzt.")
	@ApiResponse(responseCode = "200", description = "Die GPU004.txt", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM,
			schema = @Schema(type = "string", format = "binary", description = "Die GPU004.txt")))
	@ApiResponse(responseCode = "404", description = "Es wurden nicht alle benötigten Daten für den Export gefunden.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Es ist ein unerwarteter Fehler aufgetreten.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response exportUntisLehrerGPU004(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		return DBBenutzerUtils.runWithTransaction(conn -> {
			try {
				return DataUntis.exportGPU004(conn, logger, id);
			} catch (final ApiOperationException aoe) {
				final SimpleOperationResponse sor = new SimpleOperationResponse();
				sor.log.addAll(log.getStrings());
				throw new ApiOperationException(aoe.getStatus(), aoe, sor, "application/json");
			}
		}, request, ServerMode.STABLE, BenutzerKompetenz.IMPORT_EXPORT_LEHRERDATEN_EXPORTIEREN);
	}


	/**
	 * Die OpenAPI-Methode für den Export einer Fächer- bzw Kursliste zu einem Schuljahresabschnitt für Untis. Dabei
	 * wird die GPU-Datei GPU006.txt generiert.
	 *
	 * @param schema   das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id       die ID des Schuljahresabschnittes, für den die Daten exportiert werden sollen
	 * @param request  die Informationen zur HTTP-Anfrage
	 *
	 * @return die GPU006-Datei
	 */
	@POST
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/untis/export/faecher/{id : \\d+}")
	@Operation(summary = "Liefert einen Export für die Fächer- bzw. Kursdaten eines Schuljahresabschnittes (GPU006.txt).",
			description = "Liefert einen Export für die Fächer- bzw. Kursdaten eines Schuljahresabschnittes (GPU006.txt)."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Exportieren besitzt.")
	@ApiResponse(responseCode = "200", description = "Die GPU006.txt", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM,
			schema = @Schema(type = "string", format = "binary", description = "Die GPU006.txt")))
	@ApiResponse(responseCode = "404", description = "Es wurden nicht alle benötigten Daten für den Export gefunden.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Es ist ein unerwarteter Fehler aufgetreten.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response exportUntisFaecherGPU006(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		return DBBenutzerUtils.runWithTransaction(conn -> {
			try {
				return DataUntis.exportGPU006(conn, logger, id);
			} catch (final ApiOperationException aoe) {
				final SimpleOperationResponse sor = new SimpleOperationResponse();
				sor.log.addAll(log.getStrings());
				throw new ApiOperationException(aoe.getStatus(), aoe, sor, "application/json");
			}
		}, request, ServerMode.STABLE, BenutzerKompetenz.IMPORT_EXPORT_DATEN_IMPORTIEREN);
	}


	/**
	 * Die OpenAPI-Methode für den Export einer Schülerliste zu einem Schuljahresabschnitt für Untis. Dabei
	 * wird die GPU-Datei GPU010.txt generiert.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id            die ID des Schuljahresabschnittes, für den die Schülerdaten exportiert werden sollen
	 * @param sidvariante   die Variante, wie die Schüler-Bezeichner generiert werden (1 - ID, 2 - Nachname, drei Stellen Vorname und Geurtsdatum,
	 *                      3 - Nachname, Vorname, Geburtsdatum)
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die GPU010-Datei
	 */
	@POST
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/untis/export/schueler/{id : \\d+}/{sidvariante : \\d+}")
	@Operation(summary = "Liefert einen Export für die Schüler eines Schuljahresabschnittes (GPU010.txt).",
			description = "Liefert einen Export für die Schüler eines Schuljahresabschnittes (GPU010.txt)."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Exportieren besitzt.")
	@ApiResponse(responseCode = "200", description = "Die GPU010.txt", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM,
			schema = @Schema(type = "string", format = "binary", description = "Die GPU010.txt")))
	@ApiResponse(responseCode = "404", description = "Es wurden nicht alle benötigten Daten für den Export gefunden.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Es ist ein unerwarteter Fehler aufgetreten.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response exportUntisSchuelerGPU010(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@PathParam("sidvariante") final int sidvariante, @Context final HttpServletRequest request) {
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		return DBBenutzerUtils.runWithTransaction(conn -> {
			try {
				return DataUntis.exportGPU010(conn, logger, id, sidvariante);
			} catch (final ApiOperationException aoe) {
				final SimpleOperationResponse sor = new SimpleOperationResponse();
				sor.log.addAll(log.getStrings());
				throw new ApiOperationException(aoe.getStatus(), aoe, sor, "application/json");
			}
		}, request, ServerMode.STABLE, BenutzerKompetenz.IMPORT_EXPORT_SCHUELERDATEN_EXPORTIEREN);
	}

	/**
	 * Die OpenAPI-Methode für den Export der Fachwahlen zu einem Schuljahresabschnitt für Untis. Dabei
	 * wird die GPU-Datei GPU015.txt generiert.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id            die ID des Schuljahresabschnittes, für den die Fachwahlen exportiert werden sollen
	 * @param sidvariante   die Variante, wie die Schüler-Bezeichner generiert werden (1 - ID, 2 - Nachname, drei Stellen Vorname und Geurtsdatum,
	 *                      3 - Nachname, Vorname, Geburtsdatum)
	 * @param gpu002        GPU002-Datei
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die GPU015-Datei
	 */
	@POST
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/untis/export/fachwahlen/{id : \\d+}/{sidvariante : \\d+}")
	@Operation(summary = "Liefert einen Export für die Fachwahlen eines Schuljahresabschnittes (GPU015.txt).",
			description = "Liefert einen Export für die Fachwahlen eines Schuljahresabschnittes (GPU015.txt)."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Exportieren besitzt.")
	@ApiResponse(responseCode = "200", description = "Die GPU015.txt", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM,
			schema = @Schema(type = "string", format = "binary", description = "Die GPU015.txt")))
	@ApiResponse(responseCode = "404", description = "Es wurden nicht alle benötigten Daten für den Export gefunden.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Es ist ein unerwarteter Fehler aufgetreten.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response exportUntisFachwahlenGPU015(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@PathParam("sidvariante") final int sidvariante, @RequestBody(description = "Die Unterrichte von Untis (GPU002.txt)", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class))) final InputStream gpu002,
			@Context final HttpServletRequest request) {
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		return DBBenutzerUtils.runWithTransaction(conn -> {
			try {
				return DataUntis.exportGPU015(conn, logger, id, gpu002, sidvariante);
			} catch (final ApiOperationException aoe) {
				final SimpleOperationResponse sor = new SimpleOperationResponse();
				sor.log.addAll(log.getStrings());
				throw new ApiOperationException(aoe.getStatus(), aoe, sor, "application/json");
			}
		}, request, ServerMode.STABLE, BenutzerKompetenz.IMPORT_EXPORT_SCHUELERDATEN_EXPORTIEREN);
	}

	/**
	 * Die OpenAPI-Methode für den Export einer Klausurplanung zu einem Schuljahresabschnitt für Untis. Dabei
	 * wird die GPU-Datei GPU017.txt generiert.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id            die ID des Schuljahresabschnittes, für den die Klausurdaten exportiert werden sollen
	 * @param sidvariante   die Variante, wie die Schüler-Bezeichner generiert werden (1 - ID, 2 - Nachname, drei Stellen Vorname und Geurtsdatum,
	 *                      3 - Nachname, Vorname, Geburtsdatum)
	 * @param gpu002        GPU002-Datei
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die GPU017-Datei
	 */
	@POST
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/untis/export/klausuren/{id : \\d+}/{sidvariante : \\d+}")
	@Operation(summary = "Liefert einen Export für die Klausurdaten eines Schuljahresabschnittes (GPU017.txt).",
			description = "Liefert einen Export für die Klausurdaten eines Schuljahresabschnittes (GPU017.txt)."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Exportieren besitzt.")
	@ApiResponse(responseCode = "200", description = "Die GPU017.txt", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM,
			schema = @Schema(type = "string", format = "binary", description = "Die GPU017.txt")))
	@ApiResponse(responseCode = "404", description = "Es wurden nicht alle benötigten Daten für den Export gefunden.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Es ist ein unerwarteter Fehler aufgetreten.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response exportUntisKlausurenGPU017(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@PathParam("sidvariante") final int sidvariante, @RequestBody(description = "Die Unterrichte von Untis (GPU002.txt)", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class))) final InputStream gpu002,
			@Context final HttpServletRequest request) {
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		return DBBenutzerUtils.runWithTransaction(conn -> {
			try {
				return DataUntis.exportGPU017(conn, logger, id, gpu002, sidvariante);
			} catch (final ApiOperationException aoe) {
				final SimpleOperationResponse sor = new SimpleOperationResponse();
				sor.log.addAll(log.getStrings());
				throw new ApiOperationException(aoe.getStatus(), aoe, sor, "application/json");
			}
		}, request, ServerMode.STABLE, BenutzerKompetenz.IMPORT_EXPORT_SCHUELERDATEN_EXPORTIEREN);
	}

	/**
	 * Die OpenAPI-Methode für den Export der Schienenzuordnungen eines Schuljahresabschnittes für Untis. Dabei
	 * wird die GPU-Datei GPU019.txt generiert.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id            die ID des Schuljahresabschnittes, für den die Schienenzuordnungen exportiert werden sollen
	 * @param gpu002        GPU002-Datei
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die GPU019-Datei
	 */
	@POST
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/untis/export/schienen/{id : \\d+}")
	@Operation(summary = "Liefert einen Export für die Schienenzuordnungen eines Schuljahresabschnittes (GPU019.txt).",
			description = "Liefert einen Export für die Schienenzuordnungen eines Schuljahresabschnittes (GPU019.txt)."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Exportieren besitzt.")
	@ApiResponse(responseCode = "200", description = "Die GPU019.txt", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM,
			schema = @Schema(type = "string", format = "binary", description = "Die GPU019.txt")))
	@ApiResponse(responseCode = "404", description = "Es wurden nicht alle benötigten Daten für den Export gefunden.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Es ist ein unerwarteter Fehler aufgetreten.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response exportUntisSchienenGPU019(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Die Unterrichte von Untis (GPU002.txt)", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = String.class))) final InputStream gpu002,
			@Context final HttpServletRequest request) {
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		return DBBenutzerUtils.runWithTransaction(conn -> {
			try {
				return DataUntis.exportGPU019(conn, logger, id, gpu002);
			} catch (final ApiOperationException aoe) {
				final SimpleOperationResponse sor = new SimpleOperationResponse();
				sor.log.addAll(log.getStrings());
				throw new ApiOperationException(aoe.getStatus(), aoe, sor, "application/json");
			}
		}, request, ServerMode.STABLE, BenutzerKompetenz.IMPORT_EXPORT_SCHUELERDATEN_EXPORTIEREN);
	}


	/**
	 * Die OpenAPI-Methode für den Export von Blockungen zu den übergebenen Abiturjahrgängen in dem übergebenen Schuljahresabschnitt.
	 * Dabei wird eine GPU002.txt für neue Unterrichte, eine GPU015.txt für die zugrundeliegenden Fachwahlen und eine GPU019.txt für
	 * die genutzten Schienen generiert.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id            die ID des Schuljahresabschnittes, für welche die Blockungsergebnisse exportiert werden sollen
	 * @param params        die für die bisherigen Unterricht übergebene GPU002-Datei und die IDs der Blckungsergebnisse
	 * @param sidvariante   die Variante, wie die Schüler-Bezeichner generiert werden (1 - ID, 2 - Nachname, drei Stellen Vorname und Geurtsdatum,
	 *                      3 - Nachname, Vorname, Geburtsdatum)
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die GPU019-Datei
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/untis/export/blockung/{id : \\d+}/{sidvariante : \\d+}")
	@Operation(summary = "Liefert einen Export für die Blockungsergebnisse als Liste mit drei Strings (GPU002.txt, GPU015.txt, GPU019.txt).",
			description = "Liefert einen Export für die Blockungsergebnisse als Liste mit drei Strings (GPU002.txt, GPU015.txt, GPU019.txt)."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Exportieren besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste mit den Daten der drei GPUs",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class))))
	@ApiResponse(responseCode = "404", description = "Es wurden nicht alle benötigten Daten für den Export gefunden.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Es ist ein unerwarteter Fehler aufgetreten.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response exportUntisBlockungGPU002GPU015GPU019(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Die Unterrichte von Untis (GPU002.txt) und die IDs der Blockungsergebnisse", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LongAndStringLists.class))) final LongAndStringLists params,
			@PathParam("sidvariante") final int sidvariante, @Context final HttpServletRequest request) {
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		return DBBenutzerUtils.runWithTransaction(conn -> {
			try {
				return DataUntis.exportUntisBlockungsergebnisse(conn, logger, id, params, sidvariante);
			} catch (final ApiOperationException aoe) {
				final SimpleOperationResponse sor = new SimpleOperationResponse();
				sor.log.addAll(log.getStrings());
				throw new ApiOperationException(aoe.getStatus(), aoe, sor, "application/json");
			}
		}, request, ServerMode.STABLE, BenutzerKompetenz.IMPORT_EXPORT_DATEN_IMPORTIEREN);
	}

}
