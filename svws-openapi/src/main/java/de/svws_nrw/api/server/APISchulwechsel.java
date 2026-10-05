package de.svws_nrw.api.server;

import java.util.List;

import de.svws_nrw.controller.schulwechsel.SchulwechselControllerFactory;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.SchulwechselAbgang;
import de.svws_nrw.core.data.schule.SchulwechselDokument;
import de.svws_nrw.service.schulwechsel.abgang.SchulwechselAbgangCreateRequest;
import de.svws_nrw.service.schulwechsel.abgang.SchulwechselAbgangPatchRequest;
import de.svws_nrw.service.schulwechsel.dokument.SchulwechselDokumentCreateRequest;
import de.svws_nrw.service.schulwechsel.dokument.SchulwechselDokumentPatchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf Daten zu Wechselvorgängen aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/schulwechsel/...
 */
@Path("/db/{schema}/schulwechsel")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APISchulwechsel {

	/**
	 * Die OpenAPI-Methode für die Abfrage des Wechselvorgangs zum Abgang eines Schülers.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id      die Datenbank-ID zur Identifikation des Wechselvorgangs
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return der Wechselvorgang mit der übergebenen ID
	 */
	@GET
	@Path("/abgaenge/{id : \\d+}")
	@Operation(summary = "Liefert zur übergebenen ID den zugehörigen Wechselvorgang.",
			description = "Liest die Wechselvorgangsdaten zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt, um Wechselvorgänge anzusehen.")
	@ApiResponse(responseCode = "200", description = "Der Wechselvorgang mit der übergebenen ID.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchulwechselAbgang.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Wechselvorgänge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Wechselvorgang mit der angegebenen ID gefunden.")
	public Response getSchulwechselAbgang(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return SchulwechselControllerFactory.withReadAccess(request)
				.getSchulwechselAbgangController()
				.getById(id);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage aller Wechselvorgänge abgehender Schüler.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Wechselvorgänge
	 */
	@GET
	@Path("/abgaenge/")
	@Operation(summary = "Gibt eine Übersicht aller Wechselvorgänge abgehender Schüler.",
			description = "Erstellt eine Liste aller Wechselvorgänge abgehender Schüler und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt, um Wechselvorgänge anzusehen.")
	@ApiResponse(responseCode = "200", description = "Die Liste aller Wechselvorgänge abgehender Schüler.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchulwechselAbgang.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Wechselvorgänge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Wechselvorgänge gefunden.")
	public Response getSchulwechselAbgaenge(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return SchulwechselControllerFactory.withReadAccess(request)
				.getSchulwechselAbgangController()
				.getAll();
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen eines neuen Wechselvorgangs eines abgehenden Schülers.
	 *
	 * @param schema       das Datenbankschema, in welchem der Wechselvorgang erstellt wird
	 * @param dto          das Request-Objekt mit den benötigten Informationen zum Erstellen des Wechelvorgangs
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Erstell-Operation
	 */
	@POST
	@Path("/abgaenge/")
	@Operation(summary = "Erstellt einen Wechselvorgang für den Abgang eines Schülers",
			description = "Erstellt einen Wechselvorgang für den Abgang eines Schülers."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung hat, um Wechselvorgänge zu erstellen.")
	@ApiResponse(responseCode = "200", description = "Wechselvorgang wurde erfolgreich erstellt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Wechselvorgänge zu erstellen.")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff).")
	public Response createSchulwechselAbgang(@PathParam("schema") final String schema, @RequestBody(description = "Initiale Daten des Wechselvorgangs",
			required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchulwechselAbgang.class)))
			final SchulwechselAbgangCreateRequest dto, @Context final HttpServletRequest request) {
		return SchulwechselControllerFactory.withWriteAccess(request)
				.getSchulwechselAbgangController()
				.create(dto);
	}

	/**
	 * Die OpenAPI-Methode für das Ändern des Wechselvorgangs eines abgehenden Schülers.
	 *
	 * @param schema       das Datenbankschema, in welchem der Wechselvorgang geändert wird
	 * @param id 		   die Datenbank-ID zur Identifikation des Wechselvorgangs
	 * @param dto           das JSON-Objekt mit den benötigten Informationen zum Ändern des Wechelvorgangs
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Änderunsgsoperation
	 */
	@PATCH
	@Path("/abgaenge/{id : \\d+}/")
	@Operation(summary = "Passt die zu der ID des Wechselvorgangs zugehörigen Daten an.",
			description = "Passt die Wechselvorgangsdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Wechselvorgängen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Wechselvorgangsdaten integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Wechselvorgänge zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Wechselvorgang mit der angegebenen ID gefunden.")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID).")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff).")
	public Response patchSchulwechselAbgang(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für den Wechselvorgang", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = SchulwechselAbgang.class)))
			final SchulwechselAbgangPatchRequest dto, @Context final HttpServletRequest request) {
		return SchulwechselControllerFactory.withWriteAccess(request)
				.getSchulwechselAbgangController()
				.patch(id, dto);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen des Wechselvorgangs eines abgehenden Schülers.
	 *
	 * @param schema       das Datenbankschema, aus welchem der Wechselvorgang entfernt wird
	 * @param ids 		   die Datenbank-IDs zur Identifikation der Wechselvorgänge
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Lösch-Operation
	 */
	@DELETE
	@Path("/abgaenge")
	@Operation(summary = "Entfernt den Wechselvorgang zum Abgang eines Schülers.",
			description = "Entfernt den Wechselvorgang zum Abgang eines Schülers."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung hat, um Wechselvorgänge zu entfernen.")
	@ApiResponse(responseCode = "200", description = "Der Wechselvorgang wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchulwechselAbgang.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat nicht die erforderlichen Rechte, um Wechselvorgänge zu entfernen.")
	@ApiResponse(responseCode = "404", description = "Wechselvorgang nicht vorhanden.")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff).")
	public Response deleteSchulwechselAbgang(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Wechseldokumente", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class))))
			final List<Long> ids,
			@Context final HttpServletRequest request) {
		return SchulwechselControllerFactory.withDeleteAccess(request)
				.getSchulwechselAbgangController()
				.delete(ids);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage des Wechseldokuments zu einem Wechselvorgang.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id      die Datenbank-ID zur Identifikation des Wechseldokuments
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return das Wechseldokument mit der übergebenen ID
	 */
	@GET
	@Path("/dokumente/{id : \\d+}")
	@Operation(summary = "Liefert zur übergebenen ID das zugehörige Wechseldokument.",
			description = "Liest die Wechseldokumentsdaten zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt, um Wechselvorgänge anzusehen.")
	@ApiResponse(responseCode = "200", description = "Das Wechseldokument mit der übergebenen ID.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchulwechselDokument.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Wechseldokumente anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Wechseldokument mit der angegebenen ID gefunden.")
	public Response getSchulwechselDokument(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return SchulwechselControllerFactory.withReadAccess(request)
				.getSchulwechselDokumentController()
				.getById(id);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage aller Wechselvorgänge abgehender Schüler.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Wechselvorgänge
	 */
	@GET
	@Path("/dokumente/")
	@Operation(summary = "Gibt eine Übersicht aller Wechseldokumente wechselnder Schüler.",
			description = "Erstellt eine Liste aller Wechseldokumente wechselnder Schüler und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt, um Wechseldokumente anzusehen.")
	@ApiResponse(responseCode = "200", description = "Die Liste aller Wechseldokumente wechselnder Schüler.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchulwechselDokument.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Wechseldokumente anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Wechseldokumente gefunden.")
	public Response getSchulwechselDokumente(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return SchulwechselControllerFactory.withReadAccess(request)
				.getSchulwechselDokumentController()
				.getAll();
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen eines neuen Wechselvorgangs eines abgehenden Schülers.
	 *
	 * @param schema       das Datenbankschema, in welchem der Wechselvorgang erstellt wird
	 * @param dto          {@link SchulwechselDokumentCreateRequest}
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Erstell-Operation
	 */
	@POST
	@Path("/dokumente/")
	@Operation(summary = "Erstellt ein Wechseldokument zum Wechselvorgang eines Schülers",
			description = "Erstellt ein Wechseldokument für den Wechselvorgang eines Schülers. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung hat, um Wechseldokumente zu erstellen.")
	@ApiResponse(responseCode = "200", description = "Wechseldokument wurde erfolgreich erstellt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchulwechselDokument.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Wechseldokumente zu erstellen.")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff).")
	public Response createSchulwechselDokument(@PathParam("schema") final String schema,
			@RequestBody(description = "Initiale Daten des Wechseldokuments", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = SchulwechselDokument.class)))
			final SchulwechselDokumentCreateRequest dto,
			@Context final HttpServletRequest request) {
		return SchulwechselControllerFactory.withWriteAccess(request)
				.getSchulwechselDokumentController()
				.create(dto);
	}

	/**
	 * Die OpenAPI-Methode für das Ändern des Wechselvorgangs eines abgehenden Schülers.
	 *
	 * @param schema       	das Datenbankschema, in welchem der Wechselvorgang geändert wird
	 * @param id 		   	die Datenbank-ID zur Identifikation des Wechselvorgangs
	 * @param dto			das partielle Update als {@link SchulwechselDokumentPatchRequest}
	 * @param request      	die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Änderunsgsoperation
	 */
	@PATCH
	@Path("/dokumente/{id : \\d+}/")
	@Operation(summary = "Passt die zu der ID des Wechseldokuments zugehörigen Daten an.",
			description = "Passt die Wechseldokumentsdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Wechseldokumenten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Wechseldokumentsdaten integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Wechseldokumente zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Wechseldokument mit der angegebenen ID gefunden.")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID).")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff).")
	public Response patchSchulwechselDokument(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für das Wechseldokument", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = SchulwechselDokument.class)))
			final SchulwechselDokumentPatchRequest dto,
			@Context final HttpServletRequest request) {
		return SchulwechselControllerFactory.withWriteAccess(request)
				.getSchulwechselDokumentController()
				.patch(id, dto);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen der Wechselvorgänge.
	 *
	 * @param schema       das Datenbankschema, aus welchem der Wechselvorgang entfernt wird
	 * @param ids 		   die Datenbank-IDs zur Identifikation der Wechselvorgänge
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Lösch-Operation
	 */
	@DELETE
	@Path("/dokumente/")
	@Operation(summary = "Entfernt mehrere Wechseldokumente.",
			description = "Entfernt die Wechseldokumente mit den übergebenen IDs. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung hat, um Wechseldokumente zu entfernen.")
	@ApiResponse(responseCode = "200", description = "Die Wechseldokumente wurden erfolgreich entfernt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat nicht die erforderlichen Rechte, um Wechseldokumente zu entfernen.")
	@ApiResponse(responseCode = "404", description = "Mindestens ein Wechseldokument wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff).")
	public Response deleteSchulwechselDokumente(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Wechseldokumente", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class))))
			final List<Long> ids,
			@Context final HttpServletRequest request) {
		return SchulwechselControllerFactory.withDeleteAccess(request)
				.getSchulwechselDokumentController()
				.delete(ids);
	}
}
