package de.svws_nrw.api.server;

import de.svws_nrw.core.data.bk.abi.BKGymLeistungen;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.bk.DataBKGymLeistungen;
import io.swagger.v3.oas.annotations.Operation;
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
import jakarta.ws.rs.core.Response.Status;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die SVWS-Datenbank in Bezug auf das berufliche Gymnasium.
 * Ein Zugriff erfolgt über den Pfad https://{hostname}/db/{schema}/bk/gym/.
 */
@Path("/db/{schema}/bk/gym")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIBKGym {

	/**
	 * Liest die Leistungsdaten in Bezug auf das berufliche Gymnasium des Schülers mit der angegebene ID aus der Datenbank und liefert diese zurück.
	 * Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Leistungsdaten besitzt.
	 *
	 * @param schema   das Schema aus dem die Leistungsdaten des Schülers kommen sollen
	 * @param id       die ID des Schülers zu dem die Leistungsdaten geliefert werden sollen
	 *
	 * @param request  die Informationen zur HTTP-Anfrage
	 *
	 * @return die Leistungsdaten für das berufliche Gymnasium für den Schüler mit der angegebenen ID
	 */
	@GET
	@Path("/schueler/{id : \\d+}/leistungsdaten")
	@Operation(summary = "Liefert zu der ID des Schülers die zugehörigen Leistungsdaten in Bezug auf das berufliche Gymnasium.",
			description = "Liest die Leistungsdaten in Bezug auf das berufliche Gymnasium des Schülers mit der angegebene ID aus der Datenbank und liefert "
					+ "diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Leistungsdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Leistungsdaten des Schülers",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = BKGymLeistungen.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Leistungsdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
	public Response getBKGymSchuelerLeistungsdaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(DataBKGymLeistungen.getLeistungsdaten(conn, id)).build(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Abiturdaten des beruflichen Gymnasiums aus den Leistungsdaten eines Schülers.
	 *
	 * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id          die ID des Schülers zu dem Abiturdaten ermittelt werden
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return die Abiturdaten des Schülers
	 */
	@GET
	@Path("/schueler/{id : \\d+}/laufbahn")
	@Operation(summary = "Liest die Abiturdaten des beruflichen Gymnasiums aus den Leistungsdaten des Schülers mit der angegebenen ID aus.",
			description = "Liest die Abiturdaten des beruflichen Gymnasiums aus den Leistungsdaten des Schülers mit der angegebenen ID aus der Datenbank aus und "
					+ "liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Abiturdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Abiturdaten aus der Laufbahn des angegebenen Schülers",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Abiturdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Abiturdaten eines Schülers auszulesen.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag für einen Schüler mit Daten des beruflichen Gymnasiums für die angegebene ID gefunden")
	public Response getBKGymSchuelerAbiturdatenAusLeistungsdaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(DataBKGymLeistungen.getAbiturdatenFromLeistungsdaten(conn, id))
						.build(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN);
	}

}
