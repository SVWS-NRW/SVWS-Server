package de.svws_nrw.api.server;

import de.svws_nrw.core.data.SimpleOperationResponse;
import java.util.List;

import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.asd.data.jahrgang.JahrgaengeKatalogEintrag;
import de.svws_nrw.controller.schule.katalog.KatalogControllerFactory;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.jahrgaenge.DataKatalogJahrgaenge;
import de.svws_nrw.service.schule.katalog.jahrgang.JahrgangCreateRequest;
import de.svws_nrw.service.schule.katalog.jahrgang.JahrgangPatchRequest;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Jahrgangsdaten aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/jahrgaenge/...
 */
@Path("/db/{schema}/jahrgaenge")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIJahrgaenge {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIJahrgaenge() {
		// leer
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste der Jahrgänge im angegebenen Schema.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return              die Liste der Jahrgänge mit ID des Datenbankschemas
	 */
	@GET
	@Path("/")
	@Operation(summary = "Gibt eine Übersicht von allen Jahrgangsdaten zurück.",
			description = "Erstellt eine Liste aller in der Datenbank vorhanden Jahrgangsdaten insofern der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Jahrgangs-Listen-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = JahrgangsDaten.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Jahrgangsdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Jahrgangs-Einträge gefunden")
	public Response getJahrgaenge(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return KatalogControllerFactory
				.withReadAccessStable(request)
				.getJahrgangController()
				.getAll();
	}



	/**
	 * Die OpenAPI-Methode für die Abfrage des Katalogs der in den einzelnen Schulformen gültigen Jahrgänge.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return              der Katalog der in den einzelnen Schulformen gültigen Jahrgänge
	 */
	@GET
	@Path("/allgemein/jahrgaenge")
	@Operation(summary = "Gibt den Katalog der in den einzelnen Schulformen gültigen Jahrgänge zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden in den einzelnen Schulformen gültigen Jahrgänge. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Jahrgangs-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = JahrgaengeKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Jahrgangs-Katalog-Einträge gefunden")
	public Response getKatalogJahrgaenge(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> (new DataKatalogJahrgaenge()).getAll(), request,
				ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen eines Jahrgangs.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Jahrgangs
	 * @param dto       die zu ändernden Felder des Jahrgangs
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{id : \\d+}")
	@Operation(summary = "Passt den Jahrgang mit der angebenen ID an.",
			description = "Passt den Jahrgang mit der angebenen ID an. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Jahrgangsdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchJahrgang(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für den Jahrgang", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = JahrgangsDaten.class))) final JahrgangPatchRequest dto,
			@Context final HttpServletRequest request) {
		return KatalogControllerFactory
				.withWriteAccessStable(request)
				.getJahrgangController()
				.patch(id, dto);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen eines neuen Jahrgangs.
	 *
	 * @param schema       das Datenbankschema
	 * @param input        der Input-Stream mit den Daten des Jahrgangs
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem neuen Jahrgang
	 */
	@POST
	@Path("/create")
	@Operation(summary = "Erstellt einen neuen Jahrgang und gibt das zugehörige Objekt zurück.",
			description = "Erstellt einen neuen Jahrgang und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten der Jahrgänge besitzt.")
	@ApiResponse(responseCode = "201", description = "Der Jahrgang wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = JahrgangsDaten.class)))
	@ApiResponse(responseCode = "400", description = "Die Eingabedaten sind fehlerhaft.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Jahrgang für die Schule anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addJahrgang(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten des zu erstellenden Jahrgangs ohne ID, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = JahrgangsDaten.class))) final JahrgangCreateRequest input,
			@Context final HttpServletRequest request) {
		return KatalogControllerFactory
				.withWriteAccessStable(request)
				.getJahrgangController()
				.create(input);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Jahrgänge.
	 *
	 * @param schema       das Datenbankschema
	 * @param ids          die IDs der Jahrgänge
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. den gelöschten Jahrgängen
	 */
	@DELETE
	@Path("/delete/multiple")
	@Operation(summary = "Entfernt mehrere Jahrgänge.",
			description = "Entfernt mehrere Jahrgänge, insofern die notwendigen Berechtigungen vorhanden sind. Referenzierte Jahrgänge werden nicht entfernt.")
	@ApiResponse(responseCode = "200", description = "Die Lösch-Operationen wurden ausgeführt. Das Ergebnis jeder einzelnen Operation ist in der Liste enthalten.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Jahrgänge zu entfernen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteJahrgaenge(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Jahrgänge", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> ids,
			@Context final HttpServletRequest request) {
		return KatalogControllerFactory
				.withDeleteAccessStable(request)
				.getJahrgangController()
				.delete(ids);
	}
}
