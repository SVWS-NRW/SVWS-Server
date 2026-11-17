package de.svws_nrw.api.server;

import org.jboss.resteasy.annotations.GZIP;

import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.data.statistik.ZusatzerhebungenStatistikGesamt;
import de.svws_nrw.controller.statistik.StatistikControllerFactory;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.statistik.ZusatzerhebungenStatistikPatchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
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
	 * @return die aktuellen Statistik-Daten für die Schule
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

	/**
	 * Die OpenAPI-Methode für die Abfrage der Statistik-Daten im Export-Format.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die aktuellen Statistik-Daten für die Schule im Export-Format
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	@GET
	@GZIP
	@Path("/export")
	@Operation(summary = "Gibt die Statistikdaten im Export-Format für die Schule zurück.",
			description = "Gibt die Statistikdaten im Export-Format für die Schule zurück."
					+ "Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Statistikdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Statistikdaten",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Statistikdaten anzusehen.")
	public Response getStatistikExport(@PathParam("schema") final String schema, @Context final HttpServletRequest request)
			throws ApiOperationException {
		return StatistikControllerFactory.getAdmin(request)
				.getControllerStatistikGesamt()
				.getStatistikExport();
	}

	/**
	 * Die OpenAPI-Methode für das Patchen der Daten der Zusatzerhebungen für einen Schuljahresabschnitt.
	 * Existiert für den Schuljahresabschnitt noch kein Datensatz, so wird dieser angelegt.
	 *
	 * @param schema                   das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param patch                    das Patch-Objekt mit den zu ändernden Werten
	 * @param request                  die Informationen zur HTTP-Anfrage
	 *
	 * @return die Daten der Zusatzerhebungen nach dem Patch
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	@PATCH
	@Path("/zusatzerhebungen/{idSchuljahresabschnitt : \\d+}")
	@Operation(summary = "Patched die Daten der Zusatzerhebungen für den angegebenen Schuljahresabschnitt.",
			description = "Patched die Daten der Zusatzerhebungen für den angegebenen Schuljahresabschnitt und legt diese ggf. neu an, "
					+ "insofern die notwendigen Berechtigungen vorliegen.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ZusatzerhebungenStatistikGesamt.class)))
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Schuljahresabschnitt mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response patchStatistikZusatzerhebungen(@PathParam("schema") final String schema,
			@PathParam("idSchuljahresabschnitt") final long idSchuljahresabschnitt,
			@RequestBody(description = "Der Patch für die Daten der Zusatzerhebungen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ZusatzerhebungenStatistikGesamt.class))) final ZusatzerhebungenStatistikPatchRequest patch,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return StatistikControllerFactory.getAdmin(request)
				.getControllerStatistikGesamt()
				.patchZusatzerhebungen(idSchuljahresabschnitt, patch);
	}

}
