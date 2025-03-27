package de.svws_nrw.api.server;

import java.io.InputStream;

import de.svws_nrw.core.data.oauth2.OAuth2ClientConnection;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.oauth2.DataOauthClientSecrets;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf OAuth2-Client-Secrets aus
 * der SVWS-Datenbank. Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/oauth/...
 */
@Path("/db/{schema}/oauth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIOAuth {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIOAuth() {
		// leer
	}

	/**
	 * Die OpenAPI-Methode für das Hinzufügen eines Eintrags zu den OAuth Client Secrets der Schule.
	 *
	 * @param schema    das Datenbankschema
	 * @param is        der Input-Stream mit den Daten des Eintrags
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem neuen Eintrag
	 */
	@POST
	@Path("/secrets/create")
	@Operation(summary = "Erstellt einen neuen Eintrag für die schulspezifischen OAuth2-Client-Secrets und gibt das zugehörige Objekt zurück.",
			description = "Erstellt einen neuen Eintrag für die schulspezifischen OAuth2-Client-Secrets und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von OAuth2-Client-Secrets besitzt.")
	@ApiResponse(responseCode = "201", description = "Der Eintrag wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = OAuth2ClientConnection.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um ein OAuth2-Client-Secret für die Schule anzulegen.")
	@ApiResponse(responseCode = "400", description = "Der Eintrag enthält Fehler, bspw. eine invalide URL.")
	@ApiResponse(responseCode = "409", description = "Es existiert bereits ein Eintrag für den gegebenen OAuth2-Server.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addOAuthClientSecret(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten des zu erstellenden Eintrags.", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = OAuth2ClientConnection.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataOauthClientSecrets(conn).add(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.ADMIN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen der OAuth2-Client-Secrets.
	 *
	 * @param schema  das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id      die Datenbank-ID zur Identifikation der OAuth2-Client-Secrets
	 * @param is      der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/secrets/{id : \\d+}")
	@Operation(summary = "Patcht die zur ID gehörenden OAuth2-Client-Secrets an.",
			description = "Passt die OAuth2-Client-Secrets zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von OAuth2-Client-Secrets besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die OAuth2-Client-Secrets der Schule integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die OAuth2-Client-Secrets zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein OAuth2-Client-Secrets mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409",
			description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchOAuthSecret(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die OAuth2-Client-Secrets der Schule", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = OAuth2ClientConnection.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataOauthClientSecrets(conn).patch(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.ADMIN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen eines OAuth2-Client-Secrets.
	 *
	 * @param schema    das Datenbankschema
	 * @param id        die ID des OAuth2-Client-Secrets
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. OAuth2-Client-Secrets
	 */
	@DELETE
	@Path("/secrets/{id : \\d+}")
	@Operation(summary = "Entfernt ein OAuth2-Client-Secrets.",
			description = "Entfernt ein OAuth2-Client-Secrets. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen von OAuth Client Secrets hat.")
	@ApiResponse(responseCode = "200", description = "Das OAuth2-Client-Secrets wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = OAuth2ClientConnection.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um OAuth2-Client-Secrets zu entfernen.")
	@ApiResponse(responseCode = "404", description = "OAuth2-Client-Secrets nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteOAuthSecret(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataOauthClientSecrets(conn).delete(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.ADMIN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der OAuth2-Client-Secrets der Schule.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den Einträgen der {@link OAuth2ClientConnection}
	 */
	@GET
	@Path("/secrets")
	@Operation(summary = "Gibt die OAuth2-Client-Secrets der Schule zurück.",
			description = "Gibt die OAuth2-Client-Secrets der Schule zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der OAuth2-Client-Secrets besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste der OAuth2-Client-Secrets der Schule.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OAuth2ClientConnection.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Berechtigung zum Ansehen der OAuth2-Client-Secrets.")
	public Response getOAuthClientSecrets(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataOauthClientSecrets(conn).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.ADMIN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage eines OAuth2-Client-Secrets der Schule anhand
	 * der ID.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id      die ID des OAuth2-Client-Secrets
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return das OAuth2-Client-Secrets
	 */
	@GET
	@Path("/secrets/{id : \\d+}")
	@Operation(summary = "Gibt das OAuth2-Client-Secrets der Schule zurück.",
			description = "Gibt das OAuth2-Client-Secrets der Schule zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von OAuth2-Client-Secrets besitzt.")
	@ApiResponse(responseCode = "200", description = "Das OAuth2-Client-Secrets der Schule",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = OAuth2ClientConnection.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die OAuth2-Client-Secrets anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein OAuth2-Client-Secrets mit der ID bei der Schule gefunden")
	public Response getOAuthClientSecret(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataOauthClientSecrets(conn).get(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.ADMIN);
	}

}
