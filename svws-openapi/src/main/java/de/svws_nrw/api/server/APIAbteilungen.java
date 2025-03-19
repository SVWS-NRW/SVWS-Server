package de.svws_nrw.api.server;

import java.io.InputStream;

import de.svws_nrw.core.data.schule.Abteilung;
import de.svws_nrw.core.data.schule.AbteilungKlassenzuordnung;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.schule.DataAbteilungen;
import de.svws_nrw.data.schule.DataAbteilungenKlassenzuordnungen;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Daten der Abteilungen aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/abteilungen/...
 */
@Path("/db/{schema}/schule/abteilungen")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIAbteilungen {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIAbteilungen() {
		// leer
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste der Abteilungen für die Id des Jahresabschnittes im angegebenen Schema.
	 *
	 * @param schema        			das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       			die Informationen zur HTTP-Anfrage
	 * @param idSchuljahresabschnitts 	die Id des Schuljahresabschnitts für die Abteilung
	 *
	 * @return              			die Liste der Abteilungen im Jahresabschnitts des Datenbankschemas
	 */
	@GET
	@Path("/{idSchuljahresabschnitts : \\d+}")
	@Operation(summary = "Gibt eine Übersicht von allen Abteilungen im abgefragten Jahresabschnittes zurück.",
			description = "Erstellt eine Liste aller in der Datenbank vorhanden Abteilungen für die angegebene Id des Schuljahresabschnittes unter Angabe der ID, "
					+ "der Bezeichnung, der ID des Schuljahresabschnitts, der Lehrer-ID des Abteilungsleiters, die Bezeichnung des Raums des "
					+ "Abteilungsleiters, "
					+ "die eMail-Adresse des Abteilungsleiters, die interne telefonische Durchwahl des Abteilungsleiters, einer Sortierreihenfolge und "
					+ "und die Zuordnungen der Klassen zu der Abteilung."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Abteilungen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Abteilung-Listen-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Abteilung.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Abteilungen anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Abteilung-Einträge gefunden")
	public Response getAbteilungenByIdJahresAbschnitt(@PathParam("schema") final String schema, @PathParam("idSchuljahresabschnitts") final long idSchuljahresabschnitts,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn ->  new DataAbteilungen(conn, idSchuljahresabschnitts).getAllAsResponse(),
				request, ServerMode.DEV, BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN);
	}

	/**
	 * Die OpenAPI-Methode für das Hinzufügen einer Abteilung.
	 *
	 * @param schema    					das Datenbankschema, auf welches der Create ausgeführt werden soll
	 * @param is        					der Input-Stream mit den Daten der Abteilung
	 * @param idSchuljahresabschnitts		die Id des Schuljahresabschnitts für die Abteilung
	 * @param request   					die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der neuen Abteilung
	 */
	@POST
	@Path("/{idSchuljahresabschnitts : \\d+}")
	@Operation(summary = "Erstellt eine neue Abteilung und gibt das zugehörige Objekt zurück.",
			description = "Erstellt eine neue Abteilung und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Abteilungen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Abteilung wurde erfolgreich hinzugefügt.",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Abteilung.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response addAbteilung(@PathParam("schema") final String schema, @PathParam("idSchuljahresabschnitts") final long idSchuljahresabschnitts,
			@RequestBody(description = "Die Daten des zu erstellenden Abteilung ohne ID, welche automatisch generiert wird", required = true, content =
			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Abteilung.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataAbteilungen(conn, idSchuljahresabschnitts).addAsResponse(is), request, ServerMode.DEV, BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen einer Abteilung.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Abteilung
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{id : \\d+}")
	@Operation(summary = "Passt die Abteilung mit der angegebenen ID an.",
			description = "Passt die Abteilung mit der angegebenen ID an. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Abteilungen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response patchAbteilung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Abteilung", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Abteilung.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataAbteilungen(conn).patchAsResponse(id, is), request, ServerMode.DEV, BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen der Abteilung.
	 *
	 * @param schema    das Datenbankschema
	 * @param is        der InputStream, mit der Liste von zu löschenden IDs
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. der gelöschten Abteilung
	 */
	@DELETE
	@Path("/multiple")
	@Operation(summary = "Entfernt mehrere Abteilungen.",
			description = "Entfernt mehrere Abteilungen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen eines Abteilung hat.")
	@ApiResponse(responseCode = "200", description = "Die Abteilung wurde erfolgreich entfernt.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Abteilung.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine Abteilung vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response deleteAbteilungen(@PathParam("schema") final String schema, @RequestBody(description = "Die IDs der zu löschenden Klassen", required = true,
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataAbteilungen(conn).deleteMultipleAsResponse(JSONMapper.toListOfLong(is)), request, ServerMode.DEV,
				BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen der Abteilung.
	 *
	 * @param schema    das Datenbankschema
	 * @param id        der InputStream, mit der Liste von zu löschenden IDs
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. der gelöschten Abteilung
	 */
	@DELETE
	@Path("/{id : \\d+}")
	@Operation(summary = "Entfernt eine Abteilung anhand der mitgelieferten ID.",
			description = "Entfernt eine Abteilung. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen eines Abteilung hat.")
	@ApiResponse(responseCode = "200", description = "Die Abteilung wurde erfolgreich entfernt.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Abteilung.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine Abteilung vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response deleteAbteilung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataAbteilungen(conn).deleteAsResponse(id), request, ServerMode.DEV,
				BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Hinzufügen einer Abteilung.
	 *
	 * @param schema    das Datenbankschema, auf welches der Create ausgeführt werden soll
	 * @param is        der Input-Stream mit den Daten der Abteilung
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der neuen Abteilung
	 */
	@POST
	@Path("/klassenzuordnung")
	@Operation(summary = "Erstellt eine neue AbteilungenKlassenzuordnungen und gibt das zugehörige Objekt zurück.",
			description = "Erstellt eine neue AbteilungenKlassenzuordnungen und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von AbteilungKlassenzuordnungen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die AbteilungenKlassenzuordnungen wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = AbteilungKlassenzuordnung.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response addAbteilungKlassenzuordnung(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten des zu erstellenden AbteilungenKlassenzuordnungen ohne ID, welche automatisch generiert wird", required = true, content =
			@Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = AbteilungKlassenzuordnung.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataAbteilungenKlassenzuordnungen(conn).addMultipleAsResponse(is), request, ServerMode.DEV, BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen der AbteilungKlassenzuordnung.
	 *
	 * @param schema    das Datenbankschema
	 * @param id        der InputStream, mit der Liste von zu löschenden IDs
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. der gelöschten AbteilungenKlassenzuordnung
	 */
	@DELETE
	@Path("/klassenzuordnung/{id : \\d+}")
	@Operation(summary = "Entfernt eine AbteilungenKlassenzuordnung anhand der mitgelieferten ID.",
			description = "Entfernt eine AbteilungenKlassenzuordnung. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen einer "
					+ "AbteilungenKlassenzuordnungen hat.")
	@ApiResponse(responseCode = "200", description = "Die AbteilungenKlassenzuordnung wurde erfolgreich entfernt.", content = @Content(mediaType =
			"application/json", schema = @Schema(implementation = AbteilungKlassenzuordnung.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine AbteilungenKlassenzuordnung vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response deleteAbteilungKlassenzuordnung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataAbteilungenKlassenzuordnungen(conn).deleteAsResponse(id), request, ServerMode.DEV,
				BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}

}
