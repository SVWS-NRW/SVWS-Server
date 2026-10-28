package de.svws_nrw.api.server;

import java.util.List;

import de.svws_nrw.controller.schule.katalog.KatalogControllerFactory;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.kataloge.AnkreuzkompetenzKonfiguration;
import de.svws_nrw.core.data.schule.Ankreuzkompetenz;
import de.svws_nrw.core.data.schule.AnkreuzkompetenzJahrgangszuordnung;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzCreateRequest;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzJahrgangCreateRequest;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzKonfigurationPatchRequest;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzPatchRequest;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Daten der Ankreuzkompetenzen aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/ankreuzkompetenzen/...
 */
@Path("/db/{schema}/schule/ankreuzkompetenzen")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIAnkreuzkompetenzen {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIAnkreuzkompetenzen() {
		// leer
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Konfiguration der Ankreuzkompetenzen im angegebenen Schema.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Konfiguration der Ankreuzkompetenzen
	 */
	@GET
	@Path("/config")
	@Operation(summary = "Gibt die Konfiguration für die Ankreuzkompetenzen zurück.",
			description = "Gibt die Konfiguration für die Ankreuzkompetenzen zurück."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Konfiguration besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Konfiguration für die Ankreuzkompetenzen",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnkreuzkompetenzKonfiguration.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Konfiguration der Ankreuzkompetenzen anzusehen.")
	public Response getAnkreuzkompetenzenKonfiguration(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return KatalogControllerFactory
				.withReadAccessStable(request)
				.getAnkreuzkompetenzKonfigurationController()
				.get();
	}


	/**
	 * Die OpenAPI-Methode für das Patchen der Konfiguration der Ankreuzkompetenzen.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param patch     der Patch
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/config")
	@Operation(summary = "Passt die Konfiguration der Ankreuzkompetenzen an.",
			description = "Passt die Konfiguration der Ankreuzkompetenzen an."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Konfiguration besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response patchAnkreuzkompetenzenKonfiguration(@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für die Ankreuzkompetenz", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = AnkreuzkompetenzKonfiguration.class))) final AnkreuzkompetenzKonfigurationPatchRequest patch,
			@Context final HttpServletRequest request) {
		return KatalogControllerFactory
				.withWriteAccessStable(request)
				.getAnkreuzkompetenzKonfigurationController()
				.patch(patch);
	}



	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste der Ankreuzkompetenzen im angegebenen Schema.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Ankreuzkompetenzen des Datenbankschemas
	 */
	@GET
	@Path("/")
	@Operation(summary = "Gibt eine Übersicht von allen Ankreuzkompetenzen zurück.",
			description = "Erstellt eine Liste aller in der Datenbank vorhanden Ankreuzkompetenzen."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Ankreuzkompetenzen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Ankreuzkompetenz-Listen-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ankreuzkompetenz.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Ankreuzkompetenzen anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Ankreuzkompetenz-Einträge gefunden")
	public Response getAnkreuzkompetenzen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return KatalogControllerFactory
				.withReadAccessStable(request)
				.getAnkreuzkompetenzController()
				.getAll();
	}

	/**
	 * Die OpenAPI-Methode für das Hinzufügen einer Ankreuzkompetenz.
	 *
	 * @param schema    das Datenbankschema, auf welches der Create ausgeführt werden soll
	 * @param dto       die Daten der Ankreuzkompetenz
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der neuen Ankreuzkompetenz
	 */
	@POST
	@Path("/create")
	@Operation(summary = "Erstellt eine neue Ankreuzkompetenz und gibt das zugehörige Objekt zurück.",
			description = "Erstellt eine neue Ankreuzkompetenz und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Ankreuzkompetenzen besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Ankreuzkompetenz wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Ankreuzkompetenz.class)))
	@ApiResponse(responseCode = "400", description = "Die übergebenen Daten sind fehlerhaft.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Das Fach oder die Schulgliederung wurde nicht gefunden.")
	@ApiResponse(responseCode = "409", description = "Die Schulgliederung ist für die Schulform nicht gültig.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response addAnkreuzkompetenz(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten des zu erstellenden Ankreuzkompetenz ohne ID, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = Ankreuzkompetenz.class))) final AnkreuzkompetenzCreateRequest dto,
			@Context final HttpServletRequest request) {
		return KatalogControllerFactory
				.withWriteAccessStable(request)
				.getAnkreuzkompetenzController()
				.create(dto);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen einer Ankreuzkompetenz.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Ankreuzkompetenz
	 * @param patch     der Patch
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der geänderten Ankreuzkompetenz
	 */
	@PATCH
	@Path("/{id : \\d+}")
	@Operation(summary = "Passt die Ankreuzkompetenz mit der angegebenen ID an.",
			description = "Passt die Ankreuzkompetenz mit der angegebenen ID an und gibt die geänderte Ankreuzkompetenz zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Ankreuzkompetenzen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Ankreuzkompetenz.class)))
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine Ankreuzkompetenz, kein Fach oder keine Schulgliederung gefunden")
	@ApiResponse(responseCode = "409", description = "Die Schulgliederung ist für die Schulform nicht gültig.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response patchAnkreuzkompetenz(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Ankreuzkompetenz", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = Ankreuzkompetenz.class))) final AnkreuzkompetenzPatchRequest patch,
			@Context final HttpServletRequest request) {
		return KatalogControllerFactory
				.withWriteAccessStable(request)
				.getAnkreuzkompetenzController()
				.patch(id, patch);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen der Ankreuzkompetenz.
	 *
	 * @param schema    das Datenbankschema
	 * @param ids       die IDs der zu löschenden Ankreuzkompetenzen
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Ergebnis der Löschoperation je ID
	 */
	@DELETE
	@Path("/delete/multiple")
	@Operation(summary = "Entfernt mehrere Ankreuzkompetenzen.",
			description = "Entfernt Ankreuzkompetenzen, insofern die Berechtigungen vorhanden sind. "
					+ "Ankreuzkompetenzen, die bereits bei Schülern verwendet werden, werden nicht gelöscht und in der Antwort als nicht gelöscht gemeldet.")
	@ApiResponse(responseCode = "200", description = "Die Ergebnisse der Löschoperationen je ID",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Ankreuzkompetenzen zu löschen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response deleteAnkreuzkompetenzen(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Ankreuzkompetenzen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> ids,
			@Context final HttpServletRequest request) {
		return KatalogControllerFactory
				.withDeleteAccessStable(request)
				.getAnkreuzkompetenzController()
				.delete(ids);
	}

	/**
	 * Die OpenAPI-Methode für das Hinzufügen mehrerer AnkreuzkompetenzJahrgangszuordnungen.
	 *
	 * @param schema  das Datenbankschema, auf welches der Create ausgeführt werden soll
	 * @param dtos    die Daten der zu erstellenden AnkreuzkompetenzJahrgangszuordnungen
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den neu erstellten AnkreuzkompetenzJahrgangszuordnungen
	 */
	@POST
	@Path("/jahrgangzuordnung/multiple")
	@Operation(summary = "Erstellt mehrere neue AnkreuzkompetenzJahrgangszuordnungen und gibt die zugehörigen Objekte zurück.",
			description = "Erstellt mehrere neue AnkreuzkompetenzJahrgangszuordnungen und gibt die zugehörigen Objekte zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von AnkreuzkompetenzJahrgangszuordnungen besitzt.")
	@ApiResponse(responseCode = "201", description = "Die AnkreuzkompetenzJahrgangszuordnungen wurden erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = AnkreuzkompetenzJahrgangszuordnung.class))))
	@ApiResponse(responseCode = "400", description = "Die Eingabedaten sind fehlerhaft.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response addAnkreuzkompetenzJahrgangszuordnungMultiple(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden AnkreuzkompetenzJahrgangszuordnungen ohne ID, die automatisch generiert werden",
					required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(
									implementation = AnkreuzkompetenzJahrgangszuordnung.class)))) final List<AnkreuzkompetenzJahrgangCreateRequest> dtos,
			@Context final HttpServletRequest request) {
		return KatalogControllerFactory
				.withWriteAccessStable(request)
				.getAnkreuzkompetenzJahrgangController()
				.createMultiple(dtos);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer AnkreuzkompetenzJahrgangszuordnungen.
	 *
	 * @param schema  das Datenbankschema
	 * @param ids     die Datenbank-IDs der zu löschenden AnkreuzkompetenzJahrgangszuordnungen
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die Ergebnisse der Löschoperationen
	 */
	@DELETE
	@Path("/jahrgangzuordnung/delete/multiple")
	@Operation(summary = "Entfernt mehrere AnkreuzkompetenzJahrgangszuordnungen.",
			description = "Entfernt die AnkreuzkompetenzJahrgangszuordnungen mit den angegebenen IDs. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von AnkreuzkompetenzJahrgangszuordnungen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Ergebnisse der Löschoperationen",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response deleteAnkreuzkompetenzJahrgangszuordnungen(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden AnkreuzkompetenzJahrgangszuordnungen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> ids,
			@Context final HttpServletRequest request) {
		return KatalogControllerFactory
				.withDeleteAccessStable(request)
				.getAnkreuzkompetenzJahrgangController()
				.deleteMultiple(ids);
	}

}
