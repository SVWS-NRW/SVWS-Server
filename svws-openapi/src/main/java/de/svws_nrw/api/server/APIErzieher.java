package de.svws_nrw.api.server;

import java.io.InputStream;

import de.svws_nrw.core.data.erzieher.ErzieherListeEintrag;
import de.svws_nrw.core.data.erzieher.ErzieherStammdaten;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.erzieher.DataErzieherStammdaten;
import de.svws_nrw.data.erzieher.DataErzieherliste;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Erzieherdaten aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/erzieher/...
 */
@Path("/db/{schema}/erzieher")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIErzieher {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIErzieher() {
		// leer
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste aller Erzieher.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den einzelnen Erziehern
	 */
	@GET
	@Path("/")
	@Operation(summary = "Gibt eine Übersicht von allen Erziehern zurück.",
			description = "Erstellt eine Liste aller in der Datenbank vorhandenen Erzieher unter Angabe der ID, des Kürzels, "
					+ "des Vor- und Nachnamens, Erzieherart, Kontaktdaten, ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Erzieherdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Erzieher-Listen-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ErzieherListeEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Erzieherdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Erzieher-Einträge gefunden")
	public Response getErzieher(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataErzieherliste(conn).getAll(),
				request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Stammdaten eines Erziehers.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param tmpid        die Datenbank-ID zur Identifikation des Erziehers
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Stammdaten des Erziehers
	 */
	@GET
	@Path("/{id : \\d+}/stammdaten")
	@Operation(summary = "Liefert zu der ID des Erziehers die zugehörigen Stammdaten.",
			description = "Liest die Stammdaten des Erziehers zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Erzieherdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Stammdaten des Erziehers",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErzieherStammdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Erzieherdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Erzieher-Eintrag mit der angegebenen ID gefunden")
	public Response getErzieherStammdaten(@PathParam("schema") final String schema, @PathParam("id") final long tmpid,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataErzieherStammdaten(conn).get(tmpid),
				request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen der Stammdaten eines Erziehers.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param tmpid        die Datenbank-ID zur Identifikation des Erziehers
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{id : \\d+}/stammdaten")
	@Operation(summary = "Passt die zu der ID des Erziehers zugehörigen Stammdaten an.",
			description = "Passt die Erzieher-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Erzieherdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Erzieher-Stammdaten integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Erzieherdaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Erzieher-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchErzieherStammdaten(@PathParam("schema") final String schema, @PathParam("id") final long tmpid,
			@RequestBody(description = "Der Patch für die Erzieher-Stammdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErzieherStammdaten.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataErzieherStammdaten(conn).patch(tmpid, is),
				request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
	}

}
