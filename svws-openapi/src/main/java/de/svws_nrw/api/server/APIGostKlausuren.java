package de.svws_nrw.api.server;

import java.io.InputStream;
import java.util.List;

import de.svws_nrw.api.OpenAPIApplication;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenCollectionSkrsKrs;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenKalenderinformation;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausur;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenKalenderinformation;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenKursklausur;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenRaum;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenRaumstunde;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenSchuelerklausur;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenSchuelerklausurraumstunde;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenTermin;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenVorgabe;
import de.svws_nrw.db.DBEntityManager;
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
import jakarta.ws.rs.core.Response.Status;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die
 * grundlegenden Gost-Klausurdaten aus der SVWS-Datenbank. Ein Zugriff erfolgt
 * über den Pfad https://{Hostname}/db/{schema}/gost/klausuren/...
 */
@Path("/db/{schema}/gost/klausuren")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIGostKlausuren {


//	/**
//	 * Die OpenAPI-Methode für die Erzeugung der Klausuren eines Abiturjahrgangs in
//	 * einem bestimmten Halbjahr und Quartal der Gymnasialen Oberstufe.
//	 *
//	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
//	 *                   werden soll
//	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
//	 * @param halbjahr   das Gost-Halbjahr, für das die Klausuren erzeugt werden sollen
//	 * @param quartal   das Quartal, für das die Klausuren erzeugt werden sollen, falls 0 angegeben wird, für das gesamte Halbjahr
//	 * @param request    die Informationen zur HTTP-Anfrage
//	 *
//	 * @return die Liste der Gost-Kursklausuren
//	 */
//	@GET // TODO oder POST???
//	@Path("/blockung/create/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}/quartal/{quartal : \\d+}")
//	@Operation(summary = "Liest eine Liste der Kursklausuren eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus.", description = "Liest eine Liste der Kursklausuren eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. "
//			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
//	@ApiResponse(responseCode = "200", description = "Die Liste der Kursklausuren.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostKursklausur.class))))
//	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.")
//	@ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.")
//	public Response createGostKlausurenKursklausurBlockungJahrgangHalbjahrQuartal(@PathParam("schema") String schema, @PathParam("abiturjahr") int abiturjahr, @PathParam("halbjahr") int halbjahr, @PathParam("quartal") int quartal,
//			@Context HttpServletRequest request) {
//		// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen
//		// Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
//		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) {
//			return (new DataGostKlausurenVorgabe(conn, abiturjahr)).createKlausuren(halbjahr, quartal);
//		}
//	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Klausuren eines Abiturjahrgangs in
	 * einem bestimmten Halbjahr der Gymnasialen Oberstufe.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das Gost-Halbjahr
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Gost-Kursklausuren
	 */
	@GET
	@Path("/vorgaben/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}")
	@Operation(summary = "Liest eine Liste der Klausurvorgaben eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus.", description = "Liest eine Liste der Klausurvorgaben eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Klausurvorgaben.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostKlausurvorgabe.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.")
	public Response getGostKlausurenVorgabenJahrgangHalbjahr(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final long halbjahr,
			@Context final HttpServletRequest request) {
		// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen
		// Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION)) {
			return (new DataGostKlausurenVorgabe(conn, abiturjahr)).get(halbjahr);
		}
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen einer neuen Klausurvorgabe.
	 *
	 * @param schema     das Datenbankschema, in welchem die Klausurvorgabe erstellt
	 *                   wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param is         JSON-Objekt mit den Daten
	 * @return die HTTP-Antwort mit der neuen Blockung
	 */
	@POST
	@Path("/vorgaben/new")
	@Operation(summary = "Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück.", description = "Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Gost-Klausurvorgabe " + "besitzt.")
	@ApiResponse(responseCode = "200", description = "Gost-Klausurvorgabe wurde erfolgreich angelegt.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurvorgabe.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenVorgabe(@PathParam("schema") final String schema, @RequestBody(description = "Der Post für die Klausurvorgabe-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurvorgabe.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) {
			return (new DataGostKlausurenVorgabe(conn, -1)).create(is);
		}
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen einer neuen Klausurvorgabe.
	 *
	 * @param schema     das Datenbankschema, in welchem die Klausurvorgabe erstellt
	 *                   wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param abiturjahr         das Abiturjahr
	 * @param halbjahr         das Halbjahr
	 * @param quartal         das Quartal, 0 für das gesamte Halbjahr
	 * @return die HTTP-Antwort mit der neuen Blockung
	 */
	@POST
	@Path("/vorgaben/copyVorlagen/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : -?\\d+}/quartal/{quartal : -?\\d+}")
	@Operation(summary = "Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück.", description = "Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Gost-Klausurvorgabe " + "besitzt.")
	@ApiResponse(responseCode = "200", description = "Gost-Klausurvorgabe wurde erfolgreich angelegt.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Boolean.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response copyGostKlausurenVorgaben(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final int halbjahr, @PathParam("quartal") final int quartal, @Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) {
			if (new DataGostKlausurenVorgabe(conn, abiturjahr).copyVorgabenToJahrgang(GostHalbjahr.fromID(halbjahr), quartal)) {
				return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(true).build();
			}
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(false).build();
		}
	}

	/**
	 * Die OpenAPI-Methode für das Patchen der Daten einer Gost-Klausurvorgabe.
	 *
	 * @param schema     das Datenbankschema, auf welches der Patch ausgeführt
	 *                   werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id		 die ID der Klausurvorgabe
	 * @param is         JSON-Objekt mit den Daten
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/vorgaben/{id : \\d+}")
	@Operation(summary = "Patcht eine Gost-Klausurvorgabe.", description = "Patcht eine Gost-Klausurvorgabe."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines Gost-Klausurtermins besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Klausurvorgabe integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Klausurvorgaben zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Klausurvorgabe-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenVorgabe(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Klausurvorgabe-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurvorgabe.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) {
			return (new DataGostKlausurenVorgabe(conn, -1).patch(id, is));
		}
	}

	/**
	 * Die OpenAPI-Methode für das Löschen einer Gost-Klausurvorgabe.
	 *
	 * @param schema     das Datenbankschema, in welchem die Klausurvorgabe gelöscht wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id	 die ID der zu löschenden Klausurvorgabe
	 * @return die HTTP-Antwort mit der neuen Blockung
	 */
	@DELETE
	@Path("/vorgaben/delete/{id : \\d+}")
	@Operation(summary = "Löscht eine Gost-Klausurvorgabe.", description = "Löscht eine Gost-Klausurvorgabe."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen einer Gost-Klausurvorgabe " + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Klausurvorgabe für die angegebene ID wurden erfolgreich gelöscht.",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe zu löschen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenVorgabe(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) { // TODO Anpassung der Benutzerrechte
			return (new DataGostKlausurenVorgabe(conn, -1)).delete(id);
		}
	}

	/**
	 * Die OpenAPI-Methode für die Erzeugung der Klausuren eines Abiturjahrgangs in
	 * einem bestimmten Halbjahr und Quartal der Gymnasialen Oberstufe.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das Gost-Halbjahr, für das die Klausuren erzeugt werden sollen
	 * @param quartal   das Quartal, für das die Klausuren erzeugt werden sollen, falls 0 angegeben wird, für das gesamte Halbjahr
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Gost-Kursklausuren
	 */
	@GET // TODO oder POST???
	@Path("/kursklausuren/create/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}/quartal/{quartal : \\d+}")
	@Operation(summary = "Liest eine Liste der Kursklausuren eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus.", description = "Liest eine Liste der Kursklausuren eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Kursklausuren.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostKursklausur.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.")
	public Response createGostKlausurenKursklausurenJahrgangHalbjahrQuartal(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final int halbjahr, @PathParam("quartal") final int quartal,
			@Context final HttpServletRequest request) {
		// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen
		// Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) {
			return (new DataGostKlausurenVorgabe(conn, abiturjahr)).createKlausuren(halbjahr, quartal);
		}
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Klausuren eines Abiturjahrgangs in
	 * einem bestimmten Halbjahr der Gymnasialen Oberstufe.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das Gost-Halbjahr
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Gost-Kursklausuren
	 */
	@GET
	@Path("/kursklausuren/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}")
	@Operation(summary = "Liest eine Liste der Kursklausuren eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus.", description = "Liest eine Liste der Kursklausuren eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Kursklausuren.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostKursklausur.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.")
	public Response getGostKlausurenKursklausurenJahrgangHalbjahr(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final long halbjahr,
			@Context final HttpServletRequest request) {
		// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen
		// Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION)) {
			return (new DataGostKlausurenKursklausur(conn, abiturjahr)).get(halbjahr);
		}
	}

	/**
	 * Die OpenAPI-Methode für das Patchen der Daten einer Gost-Kursklausur.
	 *
	 * @param schema     das Datenbankschema, auf welches der Patch ausgeführt
	 *                   werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id		 die ID der Kursklausur
	 * @param is         JSON-Objekt mit den Daten
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/kursklausuren/{id : \\d+}")
	@Operation(summary = "Patcht einen Gost-Kursklausur.", description = "Patcht einen Gost-Kursklausur."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen einer Gost-Kursklausur besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Kursklausur integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Kursklausuren zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Kursklausur-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenKursklausur(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Kursklausur-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKursklausur.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) {
			return (new DataGostKlausurenKursklausur(conn, -1).patch(id, is));
		}
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Schuelerklausuren zu einer Menge von Kursklausuren.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id		 die ID des Klausurtermins
	 *
	 * @return die Liste der Gost-Schuelerklausuren
	 */
	@GET
	@Path("/schuelerklausuren/termin/{id : -?\\d+}")
	@Operation(summary = "Liest eine Liste der Schuelerklausuren zu einem Klausurtermin aus.", description = "Liest eine Liste der Schuelerklausuren zu einem Klausurtermin aus. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Schuelerklausuren.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostSchuelerklausur.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schuelerklausuren auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.")
	public Response getGostKlausurenSchuelerklausuren(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen
		// Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION)) {
			return (new DataGostKlausurenSchuelerklausur(conn)).get(id);
		}
	}

	/**
	 * Die OpenAPI-Methode für das Patchen der Daten einer Gost-Schuelerklausur.
	 *
	 * @param schema     das Datenbankschema, auf welches der Patch ausgeführt
	 *                   werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id		 die ID der Schuelerklausur
	 * @param is         JSON-Objekt mit den Daten
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/schuelerklausuren/{id : \\d+}")
	@Operation(summary = "Patcht einen Gost-Schuelerklausur.", description = "Patcht einen Gost-Schuelerklausur."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen einer Gost-Schuelerklausur besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Schuelerklausur integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuelerklausuren zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Schuelerklausur-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenSchuelerklausur(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Schuelerklausur-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostSchuelerklausur.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) {
			return (new DataGostKlausurenSchuelerklausur(conn).patch(id, is));
		}
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Klausurtermine eines Abiturjahrgangs
	 * in einem bestimmten Halbjahr der Gymnasialen Oberstufe.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das Gost-Halbjahr
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Gost-Klausurtermine
	 */
	@GET
	@Path("/termine/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}")
	@Operation(summary = "Liest eine Liste der Kurstermine eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus.", description = "Liest eine Liste der Kurstermine eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Klausurtermine.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostKlausurtermin.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Klausurtermine auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.")
	public Response getGostKlausurenKlausurtermineJahrgangHalbjahr(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final long halbjahr,
			@Context final HttpServletRequest request) {
		// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen
		// Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION)) {
			return (new DataGostKlausurenTermin(conn, abiturjahr)).get(halbjahr);
		}
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen eines neuen Klausurtermins.
	 *
	 * @param schema     das Datenbankschema, in welchem der Klausurtermin erstellt
	 *                   wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das Gost-Halbjahr
	 * @param quartal    das Quartal
	 * param is         JSON-Objekt mit den Daten
	 * @return die HTTP-Antwort mit der neuen Blockung
	 */
	@POST
	@Path("/termine/new/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}/quartal/{quartal : \\d+}")
	@Operation(summary = "Erstellt einen neuen Gost-Klausurtermin und gibt ihn zurück.", description = "Erstellt einen neuen Gost-Klausurtermin und gibt ihn zurück."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Gost-Klausurtermins " + "besitzt.")
	@ApiResponse(responseCode = "200", description = "Gost-Klausurtermin wurde erfolgreich angelegt.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurtermin.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurtermin anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenKlausurtermin(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final int halbjahr, @PathParam("quartal") final int quartal,
			/*@RequestBody(description = "Der Post für die Klausurtermin-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurtermin.class))) InputStream is,*/
			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) { // TODO Anpassung der Benutzerrechte
			return (new DataGostKlausurenTermin(conn, abiturjahr)).create(halbjahr, quartal/*, is*/);
		}
	}

	/**
	 * Die OpenAPI-Methode für das Löschen eines Klausurtermins.
	 *
	 * @param schema     das Datenbankschema, in welchem der Klausurtermin gelöscht wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param terminIds	 die IDs der zu löschenden Termine
	 * @return die HTTP-Antwort mit der neuen Blockung
	 */
	@DELETE
	@Path("/termine/delete")
	@Operation(summary = "Löscht Gost-Klausurtermine.", description = "Löscht Gost-Klausurtermine."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Gost-Klausurtermins " + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Klausurtermin für die angegebene ID wurden erfolgreich gelöscht.",
    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Long.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurtermin anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenKlausurtermine(
			@PathParam("schema") final String schema,
			@RequestBody(description = "die IDs der Klausurtermine", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
            array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> terminIds,
			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) {
			return (new DataGostKlausurenTermin(conn, -1)).delete(terminIds);
		}
	}

	/**
	 * Die OpenAPI-Methode für das Patchen der Daten eines Gost-Klausurtermins.
	 *
	 * @param schema     das Datenbankschema, auf welches der Patch ausgeführt
	 *                   werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id		 die ID des Klausurtermins
	 * @param is         JSON-Objekt mit den Daten
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/termine/{id : \\d+}")
	@Operation(summary = "Patcht einen Gost-Klausurtermin.", description = "Patcht einen Gost-Klausurtermin."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines Gost-Klausurtermins besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in den Klausurtermin integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Klausurtermine zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Klausurtermin-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenKlausurtermin(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Klausurtermin-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurtermin.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) {
			return (new DataGostKlausurenTermin(conn, -1).patch(id, is));
		}
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Kalenderinformationen für die Gost-Klausurplanung
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Gost-KlausurenKalenderinformationen
	 */
	@GET
	@Path("/kalenderinformationen")
	@Operation(summary = "Liest eine Liste der Klausurvorgaben eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus.", description = "Liest eine Liste der Klausurvorgaben eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Klausurvorgaben.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostKlausurenKalenderinformation.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.")
	public Response getGostKlausurenKalenderinformationen(@PathParam("schema") final String schema,
			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION)) {
			return (new DataGostKlausurenKalenderinformation(conn)).getList();
		}
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen einer neuen Klausurvorgabe.
	 *
	 * @param schema     das Datenbankschema, in welchem die Klausurvorgabe erstellt
	 *                   wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param is         JSON-Objekt mit den Daten
	 * @return die HTTP-Antwort mit der neuen Blockung
	 */
	@POST
	@Path("/kalenderinformationen/new")
	@Operation(summary = "Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück.", description = "Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Gost-Klausurvorgabe " + "besitzt.")
	@ApiResponse(responseCode = "200", description = "Gost-Klausurvorgabe wurde erfolgreich angelegt.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurvorgabe.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenKalenderinformation(@PathParam("schema") final String schema, @RequestBody(description = "Der Post für die Klausurvorgabe-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenKalenderinformation.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) { // TODO Anpassung der Benutzerrechte
			return (new DataGostKlausurenKalenderinformation(conn)).create(is);
		}
	}

	/**
	 * Die OpenAPI-Methode für das Patchen der Daten einer Gost-KlausurenKalenderinformation.
	 *
	 * @param schema     das Datenbankschema, auf welches der Patch ausgeführt
	 *                   werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id		 die ID der KlausurenKalenderinformation
	 * @param is         JSON-Objekt mit den Daten
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/kalenderinformationen/{id : \\d+}")
	@Operation(summary = "Patcht einen Gost-Klausurtermin.", description = "Patcht einen Gost-Klausurtermin."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines Gost-Klausurtermins besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in den Klausurtermin integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Klausurtermine zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Klausurtermin-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenKalenderinformation(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die KlausurenKalenderinformation-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenKalenderinformation.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) {
			return (new DataGostKlausurenKalenderinformation(conn).patch(id, is));
		}
	}

	/**
	 * Die OpenAPI-Methode für das Löschen eines Klausurtermins.
	 *
	 * @param schema     das Datenbankschema, in welchem der Klausurtermin gelöscht wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id	 die ID des zu löschenden Termins
	 * @return die HTTP-Antwort mit der neuen Blockung
	 */
	@DELETE
	@Path("/kalenderinformationen/delete/{id : \\d+}")
	@Operation(summary = "Löscht einen Gost-Klausurtermin.", description = "Löscht einen Gost-Klausurtermin."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Gost-Klausurtermins " + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Klausurtermin für die angegebene ID wurden erfolgreich gelöscht.",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurtermin anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenKalenderinformation(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) { // TODO Anpassung der Benutzerrechte
			return (new DataGostKlausurenKalenderinformation(conn)).delete(id);
		}
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Klausurräume eines Klausurtermins.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param termin 	die Id des Gost-Klausurtermins
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Gost-Klausurräume
	 */
	@GET
	@Path("/raeume/{termin : -?\\d+}")
	@Operation(summary = "Liest eine Liste der Klausurräume eines Gost-Klausurtermins aus.", description = "Liest eine Liste der Klausurräume eines Gost-Klausurtermins aus. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Klausurräume.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostKlausurraum.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Klausurräume auszulesen.")
	@ApiResponse(responseCode = "404", description = "Die Id des Klausurtermins wurde nicht gefunden.")
	public Response getGostKlausurenRaeumeTermin(@PathParam("schema") final String schema, @PathParam("termin") final long termin,
			@Context final HttpServletRequest request) {
		// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen
		// Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION)) {
			return (new DataGostKlausurenRaum(conn)).get(termin);
		}
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen eines neuen Klausurraums.
	 *
	 * @param schema     das Datenbankschema, in welchem die Klausurvorgabe erstellt
	 *                   wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param is         JSON-Objekt mit den Daten
	 * @return die HTTP-Antwort mit der neuen Blockung
	 */
	@POST
	@Path("/raeume/new")
	@Operation(summary = "Erstellt einen neue Gost-Klausurraum und gibt ihn zurück.", description = "Erstellt einen neue Gost-Klausurraum und gibt ihn zurück."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Gost-Klausurraums " + "besitzt.")
	@ApiResponse(responseCode = "200", description = "Gost-Klausurraum wurde erfolgreich angelegt.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurraum.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurraum anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenRaum(@PathParam("schema") final String schema, @RequestBody(description = "Der Post für die Klausurraum-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurraum.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) {
			return (new DataGostKlausurenRaum(conn)).create(is);
		}
	}

	/**
	 * Die OpenAPI-Methode für das Patchen der Daten eines Gost-Klausurraums.
	 *
	 * @param schema     das Datenbankschema, auf welches der Patch ausgeführt
	 *                   werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id		 die ID des Klausurraum
	 * @param is         JSON-Objekt mit den Daten
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/raeume/{id : \\d+}")
	@Operation(summary = "Patcht einen Gost-Klausurraum.", description = "Patcht einen Gost-Klausurraum."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines Gost-Klausurraums besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in den Klausurraum integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Klausurräume zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Klausurraum-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenRaum(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Klausurraum-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurraum.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) {
			return (new DataGostKlausurenRaum(conn).patch(id, is));
		}
	}

	/**
	 * Die OpenAPI-Methode für das Löschen eines Gost-Klausurraums.
	 *
	 * @param schema     das Datenbankschema, in welchem der Klausurraum gelöscht wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id	 die ID des zu löschenden Klausurraum
	 * @return die HTTP-Antwort mit der neuen Blockung
	 */
	@DELETE
	@Path("/raeume/delete/{id : \\d+}")
	@Operation(summary = "Löscht einen Gost-Klausurraum.", description = "Löscht einen Gost-Klausurraum."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen eines Gost-Klausurraums " + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Klausurraum für die angegebene ID wurden erfolgreich gelöscht.",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurraum zu löschen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenRaum(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) { // TODO Anpassung der Benutzerrechte
			return (new DataGostKlausurenRaum(conn)).delete(id);
		}
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Klausurräume eines Klausurtermins.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param termin 	die Id des Gost-Klausurtermins
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Gost-Klausurräume
	 */
	@GET
	@Path("/raumstunden/{termin : -?\\d+}")
	@Operation(summary = "Liest eine Liste der Klausurraumstunden eines Gost-Klausurtermins aus.", description = "Liest eine Liste der Klausurraumstunden eines Gost-Klausurtermins aus. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Gost-Klausurraumstunde wurde erfolgreich angelegt.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostKlausurenCollectionSkrsKrs.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Klausurraumstunden auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Termin-ID wurde nicht gefunden.")
	public Response getGostKlausurenSchuelerraumstundenTermin(@PathParam("schema") final String schema, @PathParam("termin") final long termin,
			@Context final HttpServletRequest request) {
		// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen
		// Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION)) {
			return (new DataGostKlausurenSchuelerklausurraumstunde(conn)).get(termin);
		}
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen einer neuen Klausurraumstunde.
	 *
	 * @param schema     das Datenbankschema, in welchem die Klausurraumstunde erstellt
	 *                   wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param is         JSON-Objekt mit den Daten
	 * @return die HTTP-Antwort mit der neuen Blockung
	 */
	@POST
	@Path("/raumstunden/new")
	@Operation(summary = "Erstellt ein neue Gost-Klausurraumstunde und gibt sie zurück.", description = "Erstellt eine neue Gost-Klausurraumstunde und gibt sie zurück."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Gost-Klausurraumstunde " + "besitzt.")
	@ApiResponse(responseCode = "200", description = "Gost-Klausurraumstunde wurde erfolgreich angelegt.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurraumstunde.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einer Gost-Klausurraumstunde anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenRaumStunde(@PathParam("schema") final String schema, @RequestBody(description = "Der Post für die Klausurraumstunde-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurraumstunde.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) {
			return (new DataGostKlausurenRaumstunde(conn)).create(is);
		}
	}

	/**
	 * Die OpenAPI-Methode für das Löschen einer Gost-Klausurraumstunde.
	 *
	 * @param schema     das Datenbankschema, in welchem die Klausurraumstunde gelöscht wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id	 die ID der zu löschenden Klausurraumstunde
	 * @return die HTTP-Antwort mit der neuen Blockung
	 */
	@DELETE
	@Path("/raumstunden/delete/{id : \\d+}")
	@Operation(summary = "Löscht ein Gost-Klausurraumstunde.", description = "Löscht eine Gost-Klausurraumstunde."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen einer Gost-Klausurraumstunde " + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Klausurraumstunde für die angegebene ID wurden erfolgreich gelöscht.",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurraumstunde zu löschen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenRaumStunde(@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) { // TODO Anpassung der Benutzerrechte
			return (new DataGostKlausurenRaumstunde(conn)).delete(id);
		}
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen einer neuen Klausurraumstunde.
	 *
	 * @param schema     das Datenbankschema
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param raumid         die Id des Klausurraums
	 * @param schuelerklausurIds         die Ids der GostSchuelerklausuren
	 * @return die HTTP-Antwort
	 */
	@POST
	@Path("/schuelerklausuren/zuraum/{raumid : -?\\d+}")
	@Operation(summary = "Weist die angegebenen Schülerklausuren dem Klausurraum zu.", description = "Weist die angegebenen Schülerklausuren dem Klausurraum zu."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Zuweisen eines Klausurraums besitzt.")
	@ApiResponse(responseCode = "200", description = "Gost-Klausurraumstunde wurde erfolgreich angelegt.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenCollectionSkrsKrs.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einer Gost-Klausurraumstunde anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response setzeGostSchuelerklausurenZuRaum(
			@PathParam("schema") final String schema,
			@PathParam("raumid") final long raumid,
			@RequestBody(description = "Die IDs der Schülerklausuren", required = false, content = @Content(mediaType = MediaType.APPLICATION_JSON,
            array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> schuelerklausurIds,
			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN)) {
			return (new DataGostKlausurenSchuelerklausurraumstunde(conn)).setzeRaumZuSchuelerklausuren(raumid, schuelerklausurIds);
		}
	}

}
