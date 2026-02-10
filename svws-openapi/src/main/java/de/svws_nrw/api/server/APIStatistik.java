package de.svws_nrw.api.server;

import org.jboss.resteasy.annotations.GZIP;

import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.data.statistik.StatistikControllerFactory;
import de.svws_nrw.db.utils.ApiOperationException;
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

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die Daten für die amtliche Schulstatistik aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/statistik/...
 */
@Path("/db/{schema}/statistik")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIStatistik {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIStatistik() {
		// leer
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Statistik-Daten.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die aktuellen Statistikdaten für die Schule
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	@GET
	@GZIP
	@Path("/gesamt")
	@Operation(summary = "Gibt die Statistikdaten für die Schule zurück.",
			description = "Gibt die Statistikdaten für die Schule zurück."
					+ "Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Statistikdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Statistikdaten",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatistikGesamt.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Statistikdaten anzusehen.")
	public Response getStatistikGesamt(@PathParam("schema") final String schema, @Context final HttpServletRequest request) throws ApiOperationException {
		return StatistikControllerFactory.getAdmin(request)
				.getControllerStatistikGesamt()
				.getStatistikGesamt();
	}

}
