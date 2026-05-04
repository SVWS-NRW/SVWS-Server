package de.svws_nrw.transpiler.test.api;

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
 * Eine API-Klasse für die Transpiler-Tests
 */
@Path("/testapi")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "TestApi")
public class TestApi {

	/**
	 * Eine Test-Methode für eine typische API_Methode
	 *
	 * @param schema    das Datenbankschema
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return ein String
	 */
	@GET
	@Path("/test/string")
	@Operation(summary = "TestMethode, Rückgabe string oder null.",
			description = "TestMethode, Rückgabe string oder null.")
	@ApiResponse(responseCode = "200", description = "Ein String oder null",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class, nullable = true)))
	@ApiResponse(responseCode = "403", description = "Fehlende Rechte.")
	@ApiResponse(responseCode = "404", description = "Keine Daten.")
	public Response getStringOrNull(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return Response.ok("Ein Testergebnis", MediaType.TEXT_PLAIN).build();
	}

	/**
	 * Eine Test-Methode für eine typische API_Methode
	 *
	 * @param schema    das Datenbankschema
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return ein String
	 */
	@GET
	@Path("/test/string")
	@Operation(summary = "TestMethode, Rückgabe string.",
			description = "TestMethode, Rückgabe string.")
	@ApiResponse(responseCode = "200", description = "Ein String, aber nicht null",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class)))
	@ApiResponse(responseCode = "403", description = "Fehlende Rechte.")
	@ApiResponse(responseCode = "404", description = "Keine Daten.")
	public Response getString(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return Response.ok("Ein Testergebnis", MediaType.TEXT_PLAIN).build();
	}


	/**
	 * Eine Test-Methode für eine typische API_Methode
	 *
	 * @param schema    das Datenbankschema
	 * @param body      der Body der Methode
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return ein String
	 */
	@POST
	@Path("/test/post/json/html")
	@Operation(summary = "TestMethode, Rückgabe string.",
			description = "TestMethode, Rückgabe string.")
	@ApiResponse(responseCode = "200", description = "Ein String, aber nicht null",
			content = @Content(mediaType = MediaType.TEXT_HTML, schema = @Schema(implementation = String.class)))
	@ApiResponse(responseCode = "403", description = "Fehlende Rechte.")
	@ApiResponse(responseCode = "404", description = "Keine Daten.")
	public Response postJsonToHtml(@PathParam("schema") final String schema,
			@RequestBody(description = "Der Body", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
			schema = @Schema(implementation = String.class))) final String body,
			@Context final HttpServletRequest request) {
		return Response.ok("Ein Testergebnis", MediaType.TEXT_HTML).build();
	}

}
