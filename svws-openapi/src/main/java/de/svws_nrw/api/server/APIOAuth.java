package de.svws_nrw.api.server;

import de.svws_nrw.controller.oauth.OAuthCredentialControllerFactory;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.oauth2.OAuthCredentials;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.service.oauth.credential.OAuthCreateCredential;
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
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf OAuth2-Credentials aus
 * der SVWS-Datenbank.
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
	 * @param schema  das Datenbankschema
	 * @param input   die Daten des zu erstellenden Eintrags
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem neuen Eintrag
	 */
	@POST
	@Path("/credentials")
	@Operation(summary = "Erstellt einen neuen Eintrag für die schulspezifischen OAuth2-Credentials und gibt das zugehörige Objekt zurück.",
			description = "Erstellt einen neuen Eintrag für die schulspezifischen OAuth2-Credentials und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von OAuth2-Credentials besitzt.")
	@ApiResponse(responseCode = "201", description = "Der Eintrag wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = OAuthCredentials.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um ein OAuth2-Client-Secret für die Schule anzulegen.")
	@ApiResponse(responseCode = "400", description = "Der Eintrag enthält Fehler, bspw. eine invalide URL.")
	@ApiResponse(responseCode = "409", description = "Es existiert bereits ein Eintrag für den gegebenen OAuth2-Server.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addClientCredential(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten des zu erstellenden Eintrags.", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = OAuthCredentials.class))) final OAuthCreateCredential input,
			@Context final HttpServletRequest request) {
		return OAuthCredentialControllerFactory.getNewInstance(request, BenutzerKompetenz.ADMIN)
				.getCredentialController()
				.create(input);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen eines OAuth2-Credentials.
	 *
	 * @param schema    das Datenbankschema
	 * @param id        die ID des OAuth2-Credentials
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. OAuth2-Credentials
	 */
	@DELETE
	@Path("/credentials/{id : \\d+}")
	@Operation(summary = "Entfernt ein OAuth2-Credentials.",
			description = "Entfernt ein OAuth2-Credentials. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen von OAuth Client Secrets hat.")
	@ApiResponse(responseCode = "200", description = "Das OAuth2-Credentials wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um OAuth2-Credentials zu entfernen.")
	@ApiResponse(responseCode = "404", description = "OAuth2-Credentials nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteOAuthCredential(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		return OAuthCredentialControllerFactory.getNewInstance(request, BenutzerKompetenz.ADMIN)
				.getCredentialController()
				.delete(id);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der OAuth2-Credentials der Schule.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den Einträgen der {@link OAuthCredentials}
	 */
	@GET
	@Path("/credentials")
	@Operation(summary = "Gibt die OAuth2-Credentials der Schule zurück.",
			description = "Gibt die OAuth2-Credentials der Schule zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der OAuth2-Credentials besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste der OAuth2-Credentials der Schule.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OAuthCredentials.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Berechtigung zum Ansehen der OAuth2-Credentials.")
	public Response getOAuthClientCredentials(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return OAuthCredentialControllerFactory.getNewInstance(request, BenutzerKompetenz.ADMIN)
				.getCredentialController()
				.getAll();
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der OAuth2-Credentials der Schule.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request die Informationen zur HTTP-Anfrage
	 * @param domain  die Domäne, für die die OAuth2-Client-Credentials abgefragt werden sollen
	 *
	 * @return die Liste mit den Einträgen der {@link OAuthCredentials}
	 */
	@GET
	@Path("/domain/{domain}/credentials")
	@Operation(summary = "Gibt die OAuth2-Credentials der Schule zurück.",
			description = "Gibt die OAuth2-Credentials der Schule zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der OAuth2-Credentials besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste der OAuth2-Credentials der Schule.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OAuthCredentials.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Berechtigung zum Ansehen der OAuth2-Credentials.")
	public Response getOAuthCredentialsForDomain(@PathParam("schema") final String schema, @PathParam("domain") final String domain,
												 @Context final HttpServletRequest request) {
		return OAuthCredentialControllerFactory.getNewInstance(request, BenutzerKompetenz.ADMIN)
				.getCredentialController()
				.getAll(domain);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage eines OAuth2-Credentials der Schule anhand
	 * der ID.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id      die ID des OAuth2-Credentials
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return das OAuth2-Credentials
	 */
	@GET
	@Path("/credentials/{id : \\d+}")
	@Operation(summary = "Gibt das OAuth2-Credentials der Schule zurück.",
			description = "Gibt das OAuth2-Credentials der Schule zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von OAuth2-Credentials besitzt.")
	@ApiResponse(responseCode = "200", description = "Das OAuth2-Credentials der Schule",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = OAuthCredentials.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die OAuth2-Credentials anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine OAuth2-Credentials mit der ID bei der Schule gefunden")
	public Response getOAuthCredential(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		return OAuthCredentialControllerFactory.getNewInstance(request, BenutzerKompetenz.ADMIN)
				.getCredentialController()
				.get(id);
	}

}
