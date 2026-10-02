package de.svws_nrw.api.server;

import de.svws_nrw.controller.abschluesse.AbschlussControllerFactory;
import de.svws_nrw.core.data.abschluss.Abschlussdaten;
import de.svws_nrw.service.abschluesse.AbschlussPatchRequest;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Schülerdaten aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/abschluesse/...
 */
@Path("/db/{schema}/abschluesse/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIAbschluesse {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIAbschluesse() {
		// leer
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Informationen zum Schulabschluss in einem Lernabschnitt eines Schülers.
	 *
	 * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id          die ID des Schülers
	 * @param abschnitt   die ID des Schuljahresabschnitt für den auszulesenden Lernabschnitt (Wechsel-Nr 0)
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return die Informationen zum Schulabschluss des Schülers in dem Lernabschnitt
	 */
	@GET
	@Path("schueler/{id : \\d+}/abschnitt/{abschnitt : \\d+}")
	@Operation(summary = "Liefert zu der ID des Schülers und des Schuljahresabschnittes die zugehörigen Informationen zum erreichten Schulabschluss.",
			description = "Liest die Informationen zum erreichten Schulabschluss des Schülers zu der angegebenen ID und dem angegeben Schuljahresabschnitt"
					+ "aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Informationen zum erreichten Schulabschluss des Schülers in dem Schuljahresabschnitt",
			content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = Abschlussdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404",
			description = "Kein Schüler-Eintrag mit der angegebenen ID bzw. kein Lernabschnitt in dem angegeben Schujahresabschnitt gefunden")
	public Response getSchuelerAbschlussinformationen(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@PathParam("abschnitt") final long abschnitt, @Context final HttpServletRequest request) {
		return AbschlussControllerFactory
				.withReadAccess(request)
				.getAbschlussController()
				.getByIdSchuelerAndIdSchuljahresabschnitt(id, abschnitt);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Informationen zum Schulabschluss in einem Lernabschnitt eines Schülers.
	 *
	 * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param abschnitt   die ID des Schülerlernabschnitts
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return die Informationen zum Schulabschluss des Schülers in dem Lernabschnitt
	 */
	@GET
	@Path("schueler/lernabschnittsdaten/{abschnitt : \\d+}")
	@Operation(summary = "Liefert zu der ID des Schülerlernabschnittes die zugehörigen Informationen zum erreichten Schulabschluss.",
			description = "Liest die Informationen zum erreichten Schulabschluss des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Informationen zum erreichten Schulabschluss in dem Lernabschnitt des Schülers",
			content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = Abschlussdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit Schüler-Lernabschnittsdaten mit der angegebenen ID gefunden")
	public Response getSchuelerAbschlussinformationenByLernabschnittID(@PathParam("schema") final String schema, @PathParam("abschnitt") final long abschnitt,
			@Context final HttpServletRequest request) {
		return AbschlussControllerFactory
				.withReadAccess(request)
				.getAbschlussController()
				.getByIdLernabschnitt(abschnitt);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen von Informationen zum Schulabschluss in einem Lernabschnitt eines Schülers.
	 *
	 * @param schema      das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param abschnitt   die ID des Schülerlernabschnitts
	 * @param patch       der Patch
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("schueler/lernabschnittsdaten/{abschnitt : \\d+}")
	@Operation(summary = "Passt die Informationen zum Schulabschluss in dem Lernabschnitt mit der angegebenen ID an.",
			description = "Passt die Informationen zum Schulabschluss in dem Lernabschnitt mit der angegebenen ID an. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Abschlussinformationen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.", content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = Abschlussdaten.class)))
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchSchuelerAbschlussinformationen(@PathParam("schema") final String schema, @PathParam("abschnitt") final long abschnitt,
			@RequestBody(description = "Der Patch für die Schülerlernabschnittsdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = Abschlussdaten.class))) final AbschlussPatchRequest patch,
			@Context final HttpServletRequest request) {
		return AbschlussControllerFactory
				.withWriteAccess(request)
				.getAbschlussController()
				.patch(abschnitt, patch);
	}

}
