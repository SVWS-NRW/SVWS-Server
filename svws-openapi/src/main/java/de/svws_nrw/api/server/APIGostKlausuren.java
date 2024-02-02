package de.svws_nrw.api.server;

import java.io.InputStream;
import java.util.List;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkSkt;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkrsKrs;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenDataCollection;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungDaten;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostNachschreibterminblockungKonfiguration;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenKursklausur;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenRaum;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenSchuelerklausur;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenSchuelerklausurTermin;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenSchuelerklausurraumstunde;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenTermin;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenVorgabe;
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


	/**
	 * Die OpenAPI-Methode für die Abfrage der Klausurvorgaben eines Abiturjahrgangs der Gymnasialen Oberstufe.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Gost-Klausurvorgaben
	 */
	@GET
	@Path("/vorgaben/abiturjahrgang/{abiturjahr : -?\\d+}")
	@Operation(summary = "Liest eine Liste der Klausurvorgaben eines Abiturjahrgangs der Gymnasialen Oberstufe aus.", description = "Liest eine Liste der Klausurvorgaben eines Abiturjahrgangs der Gymnasialen Oberstufe aus. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Klausurvorgaben.", content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostKlausurvorgabe.class))))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenVorgabenJahrgang(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
			@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(DataGostKlausurenVorgabe.getKlausurvorgaben(conn, abiturjahr, -1, false)).build(),
    		request,
    		ServerMode.STABLE,
    		BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
    		BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Klausurvorgaben eines Abiturjahrgangs in
	 * einem bestimmten Schuljahr der Gymnasialen Oberstufe.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das Gost-Halbjahr, das im gesuchten Schuljahr liegt
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Gost-Klausurvorgaben
	 */
	@GET
	@Path("/vorgaben/abiturjahrgang/{abiturjahr : -?\\d+}/schuljahr/{halbjahr : \\d+}")
	@Operation(summary = "Liest eine Liste der Klausurvorgaben eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus.", description = "Liest eine Liste der Klausurvorgaben eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Klausurvorgaben.", content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostKlausurvorgabe.class))))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenVorgabenJahrgangSchuljahr(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final int halbjahr,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(DataGostKlausurenVorgabe.getKlausurvorgaben(conn, abiturjahr, halbjahr, true)).build(),
    		request,
    		ServerMode.STABLE,
    		BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
    		BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Klausurvorgaben eines Abiturjahrgangs in
	 * einem bestimmten Halbjahr der Gymnasialen Oberstufe.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das Gost-Halbjahr
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Gost-Klausurvorgaben
	 */
	@GET
	@Path("/vorgaben/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}")
	@Operation(summary = "Liest eine Liste der Klausurvorgaben eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus.", description = "Liest eine Liste der Klausurvorgaben eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Klausurvorgaben.", content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostKlausurvorgabe.class))))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenVorgabenJahrgangHalbjahr(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final int halbjahr,
			@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(DataGostKlausurenVorgabe.getKlausurvorgaben(conn, abiturjahr, halbjahr, false)).build(),
    		request,
    		ServerMode.STABLE,
    		BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
    		BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen einer neuen Klausurvorgabe.
	 *
	 * @param schema     das Datenbankschema, in welchem die Klausurvorgabe erstellt
	 *                   wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param is         JSON-Objekt mit den Daten
	 * @return 			 die HTTP-Antwort mit der neuen Klausurvorgabe
	 */
	@POST
	@Path("/vorgaben/new")
	@Operation(summary = "Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück.", description = "Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Gost-Klausurvorgabe " + "besitzt.")
	@ApiResponse(responseCode = "201", description = "Gost-Klausurvorgabe wurde erfolgreich angelegt.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurvorgabe.class)))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe anzulegen.")
	@ApiResponse(responseCode = "409", description = "Die Gost-Klausurvorgabe ist schon in der Datenbank enthalten.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenVorgabe(@PathParam("schema") final String schema, @RequestBody(description = "Der Post für die Klausurvorgabe-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurvorgabe.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenVorgabe(conn, -1).create(is),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
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
	 * @return die HTTP-Antwort
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
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenVorgabe(conn, -1).patch(id, is),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Löschen einer Gost-Klausurvorgabe.
	 *
	 * @param schema     das Datenbankschema, in welchem die Klausurvorgabe gelöscht wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id	 die ID der zu löschenden Klausurvorgabe
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/vorgaben/delete/{id : \\d+}")
	@Operation(summary = "Löscht eine Gost-Klausurvorgabe.", description = "Löscht eine Gost-Klausurvorgabe."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen einer Gost-Klausurvorgabe " + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Klausurvorgabe für die angegebene ID wurden erfolgreich gelöscht.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe zu löschen.")
	@ApiResponse(responseCode = "404", description = "Die Gost-Klausurvorgabe wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenVorgabe(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenVorgabe(conn, -1).delete(id),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Kopieren von Klausurvorgaben-Vorlagen in einen konkreten Abiturjahrgang.
	 *
	 * @param schema     das Datenbankschema, in welchem die Klausurvorgaben erstellt werden
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param abiturjahr         das Abiturjahr
	 * @param halbjahr         das Halbjahr
	 * @param quartal         das Quartal, 0 für das gesamte Halbjahr
	 * @return true, falls die Vorgaben erfolgreich angelegt wurden, sonst false
	 */
	@GET
	@Path("/vorgaben/copyVorlagen/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : -?\\d+}/quartal/{quartal : -?\\d+}")
	@Operation(summary = "Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück.", description = "Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Gost-Klausurvorgabe " + "besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der neuen Klausurvorgaben.", content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostKlausurvorgabe.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe anzulegen.")
	@ApiResponse(responseCode = "400", description = "Falsche Parameter")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response copyGostKlausurenVorgaben(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final int halbjahr, @PathParam("quartal") final int quartal, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(new DataGostKlausurenVorgabe(conn, abiturjahr).copyVorgaben(halbjahr, quartal)).build(),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Kopieren von Klausurvorgaben-Vorlagen in einen konkreten Abiturjahrgang.
	 *
	 * @param schema     das Datenbankschema, in welchem die Klausurvorgaben erstellt werden
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param halbjahr         das Halbjahr
	 * @param quartal         das Quartal, 0 für das gesamte Halbjahr
	 * @return true, falls die Vorgaben erfolgreich angelegt wurden, sonst false
	 */
	@GET
	@Path("/vorgaben/createDefault/halbjahr/{halbjahr : -?\\d+}/quartal/{quartal : -?\\d+}")
	@Operation(summary = "Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück.", description = "Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Gost-Klausurvorgabe " + "besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der neuen Klausurvorgaben.", content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostKlausurvorgabe.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe anzulegen.")
	@ApiResponse(responseCode = "400", description = "Falsche Parameter")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createDefaultGostKlausurenVorgaben(@PathParam("schema") final String schema, @PathParam("halbjahr") final int halbjahr, @PathParam("quartal") final int quartal, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(DataGostKlausurenVorgabe.createDefaultVorgaben(conn, GostHalbjahr.fromIDorException(halbjahr), quartal)).build(),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Kursklausuren eines Abiturjahrgangs in
	 * einem bestimmten Schuljahr der Gymnasialen Oberstufe.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das Gost-Halbjahr, das im gesuchten Schuljahr liegt
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Gost-Kursklausuren
	 */
	@GET
	@Path("/kursklausuren/abiturjahrgang/{abiturjahr : -?\\d+}/schuljahr/{halbjahr : \\d+}")
	@Operation(summary = "Liest eine Liste der Kursklausuren eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus.", description = "Liest eine Liste der Kursklausuren eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Kursklausuren.", content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostKursklausur.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenKursklausurenJahrgangSchuljahr(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final int halbjahr,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(DataGostKlausurenKursklausur.getKursKlausuren(conn, abiturjahr, halbjahr, true)).build(),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
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
	@ApiResponse(responseCode = "200", description = "Die Liste der Kursklausuren.", content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostKursklausur.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenKursklausurenJahrgangHalbjahr(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final int halbjahr,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(DataGostKlausurenKursklausur.getKursKlausuren(conn, abiturjahr, halbjahr, false)).build(),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen der Daten einer Gost-Kursklausur.
	 *
	 * @param schema     das Datenbankschema, auf welches der Patch ausgeführt
	 *                   werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id		 die ID der Kursklausur
	 * @param abschnittid die ID des Schuljahresabschnitts
	 * @param is         JSON-Objekt mit den Daten
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@POST
	@Path("/kursklausuren/{id : \\d+}/abschnitt/{abschnittid : -?\\d+}")
	@Operation(summary = "Patcht einen Gost-Kursklausur.", description = "Patcht einen Gost-Kursklausur."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen einer Gost-Kursklausur besitzt.")
	@ApiResponse(responseCode = "201", description = "Der Patch wurde erfolgreich in die Kursklausur integriert.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenCollectionSkrsKrs.class)))
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Kursklausuren zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Kursklausur-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenKursklausur(@PathParam("schema") final String schema,
		@PathParam("id") final long id,
		@PathParam("abschnittid") final long abschnittid,
		@RequestBody(description = "Der Patch für die Kursklausur-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKursklausur.class))) final InputStream is,
		@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenKursklausur(conn, -1, abschnittid).patch(id, is),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
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
	@GET
	@Path("/kursklausuren/create/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}/quartal/{quartal : \\d+}")
	@Operation(summary = "Liest eine Liste der Kursklausuren eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus.", description = "Liest eine Liste der Kursklausuren eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Kursklausuren.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenDataCollection.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.")
	@ApiResponse(responseCode = "404", description = "Keine Klausurvorgaben definiert oder der Schuljahresabschnitt wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenKursklausurenJahrgangHalbjahrQuartal(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final int halbjahr, @PathParam("quartal") final int quartal,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(new DataGostKlausurenVorgabe(conn, abiturjahr).createKlausuren(halbjahr, quartal)).build(),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Schuelerklausuren zu einem Klausurtermin.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id		 die ID des Klausurtermins
	 *
	 * @return die Liste der Gost-SchuelerklausurenTermine
	 */
	@GET
	@Path("/schuelerklausuren/termin/{id : -?\\d+}")
	@Operation(summary = "Liest eine Liste der Schuelerklausuren zu einem Klausurtermin aus.", description = "Liest eine Liste der Schuelerklausuren zu einem Klausurtermin aus. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Schuelerklausuren.", content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostSchuelerklausurTermin.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schuelerklausuren auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenSchuelerklausurenTermine(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenSchuelerklausur(conn).get(id),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Schuelerklausuren zu einem Klausurtermin.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das Gost-Halbjahr, für das die Klausuren erzeugt werden sollen

	 *
	 * @return die Liste der Gost-SchuelerklausurenTermine
	 */
	@GET
	@Path("/schuelerklausuren/nachschreiber/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}")
	@Operation(summary = "Liest eine Liste der Schuelerklausuren zu einem Klausurtermin aus.", description = "Liest eine Liste der Schuelerklausuren zu einem Klausurtermin aus. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Schuelerklausuren.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenCollectionSkSkt.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schuelerklausuren auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenSchuelerklausurenNachschreiber(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final int halbjahr,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(DataGostKlausurenSchuelerklausur.getCollectionSkSktNachschreiber(conn, abiturjahr, GostHalbjahr.fromIDorException(halbjahr))).build(),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
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
	// TODO
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
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenSchuelerklausur(conn).patch(id, is),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Schuelerklausuren zu einem Klausurtermin.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das Gost-Halbjahr, für das die Klausuren erzeugt werden sollen

	 *
	 * @return die Liste der Gost-SchuelerklausurenTermine
	 */
	@GET
	@Path("/collection/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}")
	@Operation(summary = "Liest eine Liste der Schuelerklausuren zu einem Klausurtermin aus.", description = "Liest eine Liste der Schuelerklausuren zu einem Klausurtermin aus. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Schuelerklausuren.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenDataCollection.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schuelerklausuren auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenCollection(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final int halbjahr,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(DataGostKlausurenKursklausur.getKlausurDataCollection(conn, abiturjahr, halbjahr, false)).build(),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Klausurtermine eines Abiturjahrgangs
	 * in einem bestimmten Schuljahr der Gymnasialen Oberstufe.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das Gost-Halbjahr, das im gesuchten Schuljahr liegt
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Gost-Klausurtermine
	 */
	@GET
	@Path("/termine/abiturjahrgang/{abiturjahr : -?\\d+}/schuljahr/{halbjahr : \\d+}")
	@Operation(summary = "Liest eine Liste der Kurstermine eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus.", description = "Liest eine Liste der Kurstermine eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Klausurtermine.", content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostKlausurtermin.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Klausurtermine auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenKlausurtermineJahrgangSchuljahr(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final int halbjahr,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(DataGostKlausurenTermin.getKlausurtermine(conn, abiturjahr, halbjahr, true)).build(),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
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
	@ApiResponse(responseCode = "200", description = "Die Liste der Klausurtermine.", content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostKlausurtermin.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Klausurtermine auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenKlausurtermineJahrgangHalbjahr(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final int halbjahr,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(DataGostKlausurenTermin.getKlausurtermine(conn, abiturjahr, halbjahr, false)).build(),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen eines neuen Klausurtermins.
	 *
	 * @param schema     das Datenbankschema, in welchem der Klausurtermin erstellt
	 *                   wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param is         JSON-Objekt mit den Daten
	 * @return die HTTP-Antwort mit der neuen Blockung
	 */
	@POST
	@Path("/termine/new")
	@Operation(summary = "Erstellt einen neuen Gost-Klausurtermin und gibt ihn zurück.", description = "Erstellt einen neuen Gost-Klausurtermin und gibt ihn zurück."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Gost-Klausurtermins " + "besitzt.")
	@ApiResponse(responseCode = "201", description = "Gost-Klausurtermin wurde erfolgreich angelegt.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurtermin.class)))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurtermin anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenKlausurtermin(@PathParam("schema") final String schema, @RequestBody(description = "Der Post für die Klausurtermin-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurtermin.class))) final InputStream is, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenTermin(conn).create(is),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
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
    content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurtermin anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenKlausurtermine(
			@PathParam("schema") final String schema,
			@RequestBody(description = "die IDs der Klausurtermine", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
            array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> terminIds,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenTermin(conn).delete(terminIds),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
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
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenTermin(conn).patch(id, is),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

//	/**
//	 * Die OpenAPI-Methode für die Abfrage der Kalenderinformationen für die Gost-Klausurplanung
//	 *
//	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
//	 *                   werden soll
//	 * @param request    die Informationen zur HTTP-Anfrage
//	 *
//	 * @return die Liste der Gost-KlausurenKalenderinformationen
//	 */
//	@GET
//	@Path("/kalenderinformationen")
//	@Operation(summary = "Liest eine Liste der Klausurvorgaben eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus.", description = "Liest eine Liste der Klausurvorgaben eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. "
//			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
//	@ApiResponse(responseCode = "200", description = "Die Liste der Klausurvorgaben.", content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostKlausurenKalenderinformation.class))))
//	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.")
//	@ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.")
//	public Response getGostKlausurenKalenderinformationen(@PathParam("schema") final String schema,
//			@Context final HttpServletRequest request) {
//		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenKalenderinformation(conn).getList(),
//			request,
//			ServerMode.STABLE,
//			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
//			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
//	}
//
//	/**
//	 * Die OpenAPI-Methode für das Erstellen einer neuen Klausurvorgabe.
//	 *
//	 * @param schema     das Datenbankschema, in welchem die Klausurvorgabe erstellt
//	 *                   wird
//	 * @param request    die Informationen zur HTTP-Anfrage
//	 * @param is         JSON-Objekt mit den Daten
//	 * @return die HTTP-Antwort mit der neuen Blockung
//	 */
//	@POST
//	@Path("/kalenderinformationen/new")
//	@Operation(summary = "Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück.", description = "Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück."
//			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Gost-Klausurvorgabe " + "besitzt.")
//	@ApiResponse(responseCode = "200", description = "Gost-Klausurvorgabe wurde erfolgreich angelegt.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurvorgabe.class)))
//	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe anzulegen.")
//	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
//	public Response createGostKlausurenKalenderinformation(@PathParam("schema") final String schema, @RequestBody(description = "Der Post für die Klausurvorgabe-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenKalenderinformation.class))) final InputStream is,
//			@Context final HttpServletRequest request) {
//		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenKalenderinformation(conn).create(is),
//				request,
//				ServerMode.STABLE,
//				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
//	}
//
//	/**
//	 * Die OpenAPI-Methode für das Patchen der Daten einer Gost-KlausurenKalenderinformation.
//	 *
//	 * @param schema     das Datenbankschema, auf welches der Patch ausgeführt
//	 *                   werden soll
//	 * @param request    die Informationen zur HTTP-Anfrage
//	 * @param id		 die ID der KlausurenKalenderinformation
//	 * @param is         JSON-Objekt mit den Daten
//	 *
//	 * @return das Ergebnis der Patch-Operation
//	 */
//	@PATCH
//	@Path("/kalenderinformationen/{id : \\d+}")
//	@Operation(summary = "Patcht einen Gost-Klausurtermin.", description = "Patcht einen Gost-Klausurtermin."
//			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines Gost-Klausurtermins besitzt.")
//	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in den Klausurtermin integriert.")
//	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
//	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Klausurtermine zu ändern.")
//	@ApiResponse(responseCode = "404", description = "Kein Klausurtermin-Eintrag mit der angegebenen ID gefunden")
//	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
//	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
//	public Response patchGostKlausurenKalenderinformation(@PathParam("schema") final String schema, @PathParam("id") final long id,
//			@RequestBody(description = "Der Patch für die KlausurenKalenderinformation-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenKalenderinformation.class))) final InputStream is,
//			@Context final HttpServletRequest request) {
//		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenKalenderinformation(conn).patch(id, is),
//			request,
//			ServerMode.STABLE,
//			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
//	}
//
//	/**
//	 * Die OpenAPI-Methode für das Löschen eines Klausurtermins.
//	 *
//	 * @param schema     das Datenbankschema, in welchem der Klausurtermin gelöscht wird
//	 * @param request    die Informationen zur HTTP-Anfrage
//	 * @param id	 die ID des zu löschenden Termins
//	 * @return die HTTP-Antwort mit der neuen Blockung
//	 */
//	@DELETE
//	@Path("/kalenderinformationen/delete/{id : \\d+}")
//	@Operation(summary = "Löscht einen Gost-Klausurtermin.", description = "Löscht einen Gost-Klausurtermin."
//			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Gost-Klausurtermins " + "besitzt.")
//    @ApiResponse(responseCode = "200", description = "Der Klausurtermin für die angegebene ID wurden erfolgreich gelöscht.",
//    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Long.class)))
//	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurtermin anzulegen.")
//	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
//	public Response deleteGostKlausurenKalenderinformation(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
//		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenKalenderinformation(conn).delete(id),
//			request,
//			ServerMode.STABLE,
//			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
//	}

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
	@ApiResponse(responseCode = "200", description = "Die Liste der Klausurräume.", content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostKlausurraum.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Klausurräume auszulesen.")
	@ApiResponse(responseCode = "404", description = "Die Id des Klausurtermins wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenRaeumeTermin(@PathParam("schema") final String schema, @PathParam("termin") final long termin,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenRaum(conn).get(termin),
			request, ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen eines neuen Klausurraums.
	 *
	 * @param schema     das Datenbankschema, in welchem die Klausurvorgabe erstellt
	 *                   wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param is         JSON-Objekt mit den Daten
	 * @return das neue Klausurraum-Objekt
	 */
	@POST
	@Path("/raeume/new")
	@Operation(summary = "Erstellt einen neue Gost-Klausurraum und gibt ihn zurück.", description = "Erstellt einen neue Gost-Klausurraum und gibt ihn zurück."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Gost-Klausurraums " + "besitzt.")
	@ApiResponse(responseCode = "201", description = "Gost-Klausurraum wurde erfolgreich angelegt.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurraum.class)))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurraum anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenRaum(@PathParam("schema") final String schema, @RequestBody(description = "Der Post für die Klausurraum-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurraum.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenRaum(conn).create(is),
			request, ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
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
	@ApiResponse(responseCode = "404", description = "Kein Termin mit der übergebenen ID oder Stundenplanraum-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenRaum(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Klausurraum-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurraum.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenRaum(conn).patch(id, is),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Löschen eines Gost-Klausurraums.
	 *
	 * @param schema     das Datenbankschema, in welchem der Klausurraum gelöscht wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id	 die ID des zu löschenden Klausurraum
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/raeume/delete/{id : \\d+}")
	@Operation(summary = "Löscht einen Gost-Klausurraum.", description = "Löscht einen Gost-Klausurraum."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen eines Gost-Klausurraums " + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Klausurraum für die angegebene ID wurden erfolgreich gelöscht.",
    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Boolean.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurraum zu löschen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenRaum(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenRaum(conn).delete(id),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der für den Klausurraummanager benötigten Daten.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param termin 	die Id des Gost-Klausurtermins
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Collection mit benötigten Daten
	 */
	@GET
	@Path("/raumstunden/{termin : -?\\d+}")
	@Operation(summary = "Liest eine Liste der Klausurraumstunden eines Gost-Klausurtermins aus.", description = "Liest eine Liste der Klausurraumstunden eines Gost-Klausurtermins aus. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Gost-Klausurraumstunde wurde erfolgreich angelegt.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenCollectionSkrsKrs.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Klausurraumstunden auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Termin-ID wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenSchuelerraumstundenTermin(@PathParam("schema") final String schema, @PathParam("termin") final long termin,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenSchuelerklausurraumstunde(conn).get(termin),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen einer neuen Klausurraumstunde.
	 *
	 * @param schema     das Datenbankschema
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param raumid         die Id des Klausurraums
	 * @param abschnittid	die Id des Schuljahresabschnitts
	 * @param schuelerklausurIds         die Ids der GostSchuelerklausuren
	 * @return die HTTP-Antwort
	 */
	@POST
	@Path("/schuelerklausuren/zuraum/{raumid : -?\\d+}/abschnitt/{abschnittid : -?\\d+}")
	@Operation(summary = "Weist die angegebenen Schülerklausuren dem Klausurraum zu.", description = "Weist die angegebenen Schülerklausuren dem Klausurraum zu."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Zuweisen eines Klausurraums besitzt.")
	@ApiResponse(responseCode = "200", description = "Gost-Klausurraumstunde wurde erfolgreich angelegt.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenCollectionSkrsKrs.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einer Gost-Klausurraumstunde anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response setzeGostSchuelerklausurenZuRaum(
			@PathParam("schema") final String schema,
			@PathParam("raumid") final long raumid,
			@PathParam("abschnittid") final long abschnittid,
			@RequestBody(description = "Die IDs der Schülerklausuren", required = false, content = @Content(mediaType = MediaType.APPLICATION_JSON,
            array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> schuelerklausurIds,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> {
			if (raumid == -1)
				return DataGostKlausurenSchuelerklausurraumstunde.loescheRaumZuSchuelerklausuren(conn, schuelerklausurIds);
			return DataGostKlausurenSchuelerklausurraumstunde.setzeRaumZuSchuelerklausuren(conn, raumid, schuelerklausurIds, abschnittid);
			},
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen einer neuen Klausurraumstunde.
	 *
	 * @param schema     das Datenbankschema
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param blockungDaten         die Ids der GostSchuelerklausuren
	 * @return die HTTP-Antwort
	 */
	@POST
	@Path("/blocken/kursklausuren")
	@Operation(summary = "Weist die angegebenen Schülerklausuren dem Klausurraum zu.", description = "Weist die angegebenen Schülerklausuren dem Klausurraum zu."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Zuweisen eines Klausurraums besitzt.")
	@ApiResponse(responseCode = "200", description = "Gost-Klausurraumstunde wurde erfolgreich angelegt.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Boolean.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einer Gost-Klausurraumstunde anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response blockenGostKursklausuren(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der Schülerklausuren", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurterminblockungDaten.class))) final GostKlausurterminblockungDaten blockungDaten,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(DataGostKlausurenKursklausur.blocken(conn, blockungDaten)).build(),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen einer neuen Klausurraumstunde.
	 *
	 * @param schema     das Datenbankschema
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param config     das GostNachschreibterminblockungKonfiguration-Objekt
	 * @return die HTTP-Antwort
	 */
	@POST
	@Path("/blocken/schuelerklausurtermine")
	@Operation(summary = "Weist die angegebenen Schülerklausuren dem Klausurraum zu.", description = "Weist die angegebenen Schülerklausuren dem Klausurraum zu."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Zuweisen eines Klausurraums besitzt.")
	@ApiResponse(responseCode = "200", description = "Gost-Klausurraumstunde wurde erfolgreich angelegt.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenDataCollection.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einer Gost-Klausurraumstunde anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response blockenGostSchuelerklausurtermine(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der Schülerklausuren", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostNachschreibterminblockungKonfiguration.class))) final GostNachschreibterminblockungKonfiguration config,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(DataGostKlausurenSchuelerklausurTermin.blocken(conn, config)).build(),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Klausurdaten zu einem Schüler.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param sId 		die Id des Schülers
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das Gost-Halbjahr
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die SkKkKv-Collection
	 */
	@GET
	@Path("/schueler/{sid : -?\\d+}/abiturjahrgang/{abiturjahr : -?\\d+}/schuljahr/{halbjahr : \\d+}")
	@Operation(summary = "Liest eine Liste der Klausurraumstunden eines Gost-Klausurtermins aus.", description = "Liest eine Liste der Klausurraumstunden eines Gost-Klausurtermins aus. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Gost-Klausurraumstunde wurde erfolgreich angelegt.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenDataCollection.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Klausurraumstunden auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Termin-ID wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenCollectionBySchuelerid(
			@PathParam("schema") final String schema,
			@PathParam("sid") final long sId,
			@PathParam("abiturjahr") final int abiturjahr,
			@PathParam("halbjahr") final int halbjahr,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(DataGostKlausurenSchuelerklausur.getGostKlausurenCollectionBySchuelerid(conn, sId, abiturjahr, halbjahr)).build(),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen eines neuen Klausurtermins.
	 *
	 * @param schema     das Datenbankschema, in welchem der Klausurtermin erstellt
	 *                   wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id       die ID der Schülerklausur, zu der ein neuer Termin generiert werden soll.
	 * @return die HTTP-Antwort mit der neuen Blockung
	 */
	@GET
	@Path("/schuelerklausuren/termine/new/{id : \\d+}")
	@Operation(summary = "Erstellt einen neuen Gost-Klausurtermin und gibt ihn zurück.", description = "Erstellt einen neuen Gost-Klausurtermin und gibt ihn zurück."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Gost-Klausurtermins " + "besitzt.")
	@ApiResponse(responseCode = "201", description = "Gost-Klausurtermin wurde erfolgreich angelegt.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostSchuelerklausurTermin.class)))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurtermin anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")

		public Response createGostKlausurenSchuelerklausurtermin(
			@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenSchuelerklausurTermin(conn).create(id),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Löschen eines Klausurtermins.
	 *
	 * @param schema     das Datenbankschema, in welchem der Klausurtermin gelöscht wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id         die ID des zu löschenden Schülerklausurtermins.
	 * @return die HTTP-Antwort mit der neuen Blockung
	 */
	@DELETE
	@Path("/schuelerklausuren/termine/delete/{id : \\d+}")
	@Operation(summary = "Löscht Gost-Schülerklausurtermine.", description = "Löscht Gost-Schülerklausurtermine."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Gost-Schülerklausurtermins " + "besitzt.")
	@ApiResponse(responseCode = "200", description = "Gost-Schülerklausurtermin wurde erfolgreich angelegt.",
	content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Boolean.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Gost-Schülerklausurtermin zu löschen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenSchuelerklausurtermin(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenSchuelerklausurTermin(conn).delete(id),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
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
	@Path("/schuelerklausuren/termine/{id : \\d+}")
	@Operation(summary = "Patcht einen Gost-Klausurtermin.", description = "Patcht einen Gost-Klausurtermin."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines Gost-Klausurtermins besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in den Klausurtermin integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Klausurtermine zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Klausurtermin-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenSchuelerklausurtermin(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Klausurtermin-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostSchuelerklausurTermin.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenSchuelerklausurTermin(conn).patch(id, is),
			request,
			ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

}
