package de.svws_nrw.api.external;

import de.svws_nrw.core.data.schuljahresabschnitt.v1.SchuljahresabschnittV1;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.schule.DataSchuljahresabschnitte;
import de.svws_nrw.data.schule.DataSchuljahresabschnitteV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den externen Zugriff auf Schuljahresabschnittsinformationen.
 */
@Path("/api/external/{schema}/v1/schuljahresabschnitte")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "External")
public class APISchuljahresabschnitteV1 {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APISchuljahresabschnitteV1() {
		// leer
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der verfügbaren Schuljahresabschnitte.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return          eine Liste von Schuljahresabschnitten
	 */
	@GET
	@Path("/")
	@Operation(summary = "Gibt eine Liste aller verfügbaren Schuljahresabschnitte zurück.",
			description = "Erstellt eine Liste aller vorhandenen Schuljahresabschnitte, insofern die notwendige Berechtigung vorliegt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Schuljahresabschnitten",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuljahresabschnittV1.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuljahresabschnitte anzusehen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response getSchuljahresabschnitte(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuljahresabschnitteV1(new DataSchuljahresabschnitte(conn)).getAllAsResponse(),
				request, ServerMode.STABLE, BenutzerKompetenz.IMPORT_EXPORT_LERNPLATTFORM);
	}

}
