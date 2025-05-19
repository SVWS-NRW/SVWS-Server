package de.svws_nrw.api.server;

import java.io.InputStream;
import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionHjData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkrsKrsData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenUpdate;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumRich;
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
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausuren;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenKursklausur;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenRaum;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenSchuelerklausur;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenSchuelerklausurTermin;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenSchuelerklausurraumstunde;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenTermin;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenVorgabe;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausurenTermine;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenTermine;
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
	 * Leerer Standardkonstruktor.
	 */
	public APIGostKlausuren() {
		// leer
	}

	/**
	 * Liefert die {@link GostKlausurvorgabe}n eines Abiturjahrgangs der gymnasialen Oberstufe.
	 *
	 * @param schema     das Datenbankschema, auf welchem die Abfrage ausgeführt werden soll
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der {@link GostKlausurvorgabe}n
	 */
	@GET
	@Path("/vorgaben/abiturjahrgang/{abiturjahr : -?\\d+}")
	@Operation(summary = "Liefert die Klausurvorgaben eines Abiturjahrgangs der gymnasialen Oberstufe.",
			description = "Liefert die Klausurvorgaben eines Abiturjahrgangs der gymnasialen Oberstufe. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Klausurvorgaben.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostKlausurvorgabe.class))))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Klausurvorgaben auszulesen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenVorgabenJahrgang(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON)
						.entity(new DataGostKlausurenVorgabe(conn).getKlausurvorgaben(abiturjahr, -1, false)).build(),
				request,
				ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
	}

	/**
	 * Erstellt eine neue {@link GostKlausurvorgabe} und gibt sie zurück.
	 *
	 * @param schema     das Datenbankschema, in welchem die {@link GostKlausurvorgabe} erstellt wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param is         JSON-Objekt mit den Daten
	 *
	 * @return 			 die HTTP-Antwort mit der neuen {@link GostKlausurvorgabe}
	 */
	@POST
	@Path("/vorgaben/new")
	@Operation(summary = "Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück.",
			description = "Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Gost-Klausurvorgabe besitzt.")
	@ApiResponse(responseCode = "201", description = "Gost-Klausurvorgabe wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurvorgabe.class)))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe anzulegen.")
	@ApiResponse(responseCode = "409", description = "Die Gost-Klausurvorgabe ist schon in der Datenbank enthalten.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenVorgabe(@PathParam("schema") final String schema,
			@RequestBody(description = "Der Post für die Klausurvorgabe-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = GostKlausurvorgabe.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenVorgabe(conn).addAsResponse(is),
				request,
				ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Patcht die Daten einer {@link GostKlausurvorgabe}.
	 *
	 * @param schema     das Datenbankschema, auf welchem der Patch ausgeführt werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id		 die ID der {@link GostKlausurvorgabe}
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
	@ApiResponse(responseCode = "409",
			description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenVorgabe(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Klausurvorgabe-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = GostKlausurvorgabe.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenVorgabe(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Löschen einer {@link GostKlausurvorgabe}.
	 *
	 * @param schema     das Datenbankschema, in welchem die {@link GostKlausurvorgabe} gelöscht wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id	     die ID der zu löschenden Klausurvorgabe
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/vorgaben/delete/{id : \\d+}")
	@Operation(summary = "Löscht eine Gost-Klausurvorgabe.", description = "Löscht eine Gost-Klausurvorgabe."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen einer Gost-Klausurvorgabe besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Klausurvorgabe für die angegebene ID wurden erfolgreich gelöscht.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe zu löschen.")
	@ApiResponse(responseCode = "404", description = "Die Gost-Klausurvorgabe wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenVorgabe(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenVorgabe(conn).deleteAsResponse(id),
				request,
				ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Kopiert die {@link GostKlausurvorgabe}-Vorlagen in einen konkreten Abiturjahrgang und gibt sie zurück.
	 *
	 * @param schema       das Datenbankschema, in welchem die {@link GostKlausurvorgabe}n erstellt werden
	 * @param request      die Informationen zur HTTP-Anfrage
	 * @param abiturjahr   das Abiturjahr, für das die {@link GostKlausurvorgabe}n kopiert werden sollen
	 * @param halbjahr     das GostHalbjahr, für das die {@link GostKlausurvorgabe}n kopiert werden sollen
	 * @param quartal      das Quartal, 0 für das gesamte GostHalbjahr
	 *
	 * @return die Liste der neuen {@link GostKlausurvorgabe}n im angegebenen Abiturjahrgang, GostHalbjahr und Quartal
	 */
	@GET
	@Path("/vorgaben/copyVorlagen/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : -?\\d+}/quartal/{quartal : -?\\d+}")
	@Operation(summary = "Kopiert die Klausurvorgabe-Vorlagen in einen konkreten Abiturjahrgang und gibt sie zurück.",
			description = "Kopiert die Klausurvorgabe-Vorlagen in einen konkreten Abiturjahrgang und gibt sie zurück."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anlegen der Gost-Klausurvorgaben besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der neuen Klausurvorgaben.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostKlausurvorgabe.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe anzulegen.")
	@ApiResponse(responseCode = "400", description = "Falsche Parameter")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response copyGostKlausurenVorgaben(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
			@PathParam("halbjahr") final int halbjahr, @PathParam("quartal") final int quartal, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON)
						.entity(new DataGostKlausurenVorgabe(conn, abiturjahr).copyVorgaben(halbjahr, quartal)).build(),
				request,
				ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Legt die Default-{@link GostKlausurvorgabe}n im Vorlagen-Jahrgang an und gibt sie zurück.
	 *
	 * @param schema     das Datenbankschema, in welchem die {@link GostKlausurvorgabe}n erstellt werden
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param halbjahr     das GostHalbjahr, für das die {@link GostKlausurvorgabe}n erstellt werden sollen
	 * @param quartal      das Quartal, 0 für das gesamte GostHalbjahr
	 *
	 * @return die Liste der neuen {@link GostKlausurvorgabe}n im Vorlagen-Jahrgang, GostHalbjahr und Quartal
	 */
	@GET
	@Path("/vorgaben/createDefault/halbjahr/{halbjahr : -?\\d+}/quartal/{quartal : -?\\d+}")
	@Operation(summary = "Legt die Default-Klausurvorgaben im Vorlagen-Jahrgang an und gibt sie zurück.",
			description = "Legt die Default-Klausurvorgaben im Vorlagen-Jahrgang an und gibt sie zurück."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Gost-Klausurvorgabe besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der neuen Klausurvorgaben.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostKlausurvorgabe.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe anzulegen.")
	@ApiResponse(responseCode = "400", description = "Falsche Parameter")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenDefaultVorgaben(@PathParam("schema") final String schema, @PathParam("halbjahr") final int halbjahr,
			@PathParam("quartal") final int quartal, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON)
						.entity(new DataGostKlausurenVorgabe(conn).createDefaultVorgaben(GostHalbjahr.fromIDorException(halbjahr), quartal)).build(),
				request,
				ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Patcht eine {@link GostKursklausur} und gibt die daraufhin geänderten Daten innerhalb des {@link GostKlausurenCollectionSkrsKrsData}-Objekts zurück.
	 *
	 * @param schema        das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 * @param id		    die ID der Kursklausur
	 * @param abiturjahr    das Abiturjahr der Kursklausur
	 * @param halbjahr      das GostHalbjahr der Kursklausur
	 * @param is            JSON-Objekt mit den Daten
	 *
	 * @return die geänderten Raumdaten als {@link GostKlausurenCollectionSkrsKrsData}-Objekt
	 */
	@POST
	@Path("/kursklausuren/{id : \\d+}/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : -?\\d+}")
	@Operation(summary = "Patcht eine Gost-Kursklausur und gibt die daraufhin geänderten Raumdaten zurück.", description = "Patcht eine Gost-Kursklausur und gibt die daraufhin geänderten Raumdaten zurück."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen einer Gost-Kursklausur besitzt.")
	@ApiResponse(responseCode = "201", description = "Der Patch wurde erfolgreich in die Kursklausur integriert.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenCollectionSkrsKrsData.class)))
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Kursklausuren zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Kursklausur-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenKursklausur(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@PathParam("abiturjahr") final int abiturjahr,
			@PathParam("halbjahr") final int halbjahr, @RequestBody(description = "Der Patch für die Kursklausur-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKursklausur.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataGostKlausurenKursklausur(conn, abiturjahr, GostHalbjahr.fromIDorException(halbjahr)).patchAsResponse(id, is),
				request,
				ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Erzeugt die {@link GostKursklausur}en eines Abiturjahrgangs in einem bestimmten GostHalbjahr
	 * und Quartal der Gymnasialen Oberstufe. Schülerklausuren und Haupttermin-Schülerklausurtermine werden ebenfalls erzeugt.
	 *
	 * @param schema       das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param abiturjahr   das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr     das Gost-Halbjahr, für das die Klausuren erzeugt werden sollen
	 * @param quartal      das Quartal, für das die Klausuren erzeugt werden sollen, falls 0 angegeben wird, für das gesamte GostHalbjahr
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return das {@link GostKlausurenCollectionData}-Objekt mit den erzeugten Daten
	 */
	@GET
	@Path("/kursklausuren/create/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}/quartal/{quartal : \\d+}")
	@Operation(summary = "Erzeugt die Kursklausuren eines Abiturjahrgangs in einem bestimmten GostHalbjahr und Quartal der Gymnasialen Oberstufe. Schülerklausuren und Haupttermin-Schülerklausurtermine werden ebenfalls erzeugt.",
			description = "Erzeugt die Kursklausuren eines Abiturjahrgangs in einem bestimmten GostHalbjahr und Quartal der Gymnasialen Oberstufe. Schülerklausuren und Haupttermin-Schülerklausurtermine werden ebenfalls erzeugt."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Kursklausuren besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Kursklausuren.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenCollectionData.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.")
	@ApiResponse(responseCode = "404", description = "Keine Klausurvorgaben definiert oder der Schuljahresabschnitt wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenKursklausurenJahrgangHalbjahrQuartal(@PathParam("schema") final String schema,
			@PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final int halbjahr, @PathParam("quartal") final int quartal,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON)
						.entity(new DataGostKlausuren(conn, abiturjahr).createKlausuren(halbjahr, quartal)).build(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Patcht die Daten einer {@link GostSchuelerklausur}.
	 *
	 * @param schema     das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id		 die ID der {@link GostSchuelerklausur}
	 * @param is         JSON-Objekt mit den Daten
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/schuelerklausuren/{id : \\d+}")
	@Operation(summary = "Patcht eine Gost-Schuelerklausur.", description = "Patcht eine Gost-Schuelerklausur."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen einer Gost-Schuelerklausur besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Schuelerklausur integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuelerklausuren zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Schuelerklausur-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenSchuelerklausur(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Schuelerklausur-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = GostSchuelerklausur.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenSchuelerklausur(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Liefert alle zur Klausurplanung gehörenden Daten in einem {@link GostKlausurenCollectionAllData}-Objekt.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param hjData     die Liste der {@link GostKlausurenCollectionHjData}-Objekte, für die Klausurdaten geladen werden sollen
	 *
	 * @return das {@link GostKlausurenCollectionAllData}-Objekt mit den Daten zur Klausurplanung
	 */
	@POST
	@Path("/collection/alldata")
	@Operation(summary = "Liefert alle zur Klausurplanung gehörenden Daten in einem GostKlausurenCollectionAllData-Objekt.",
			description = "Liefert alle zur Klausurplanung gehörenden Daten in einem GostKlausurenCollectionAllData-Objekt."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Daten besitzt.")
	@ApiResponse(responseCode = "200", description = "Das GostKlausurenCollectionAllData-Objekt mit den Daten der Klausurplanung für den angegebenen Abiturjahrgang und das Halbjahr.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenCollectionAllData.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das GostHalbjahr wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenCollectionAlldata(@PathParam("schema") final String schema,
			@RequestBody(description = "die IDs der GostSchuelerklausuren", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
			array = @ArraySchema(schema = @Schema(implementation = GostKlausurenCollectionHjData.class)))) final List<GostKlausurenCollectionHjData> hjData,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON)
						.entity(DataGostKlausuren.getAllData(conn, hjData)).build(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
	}

	/**
	 * Liefert alle zur Klausurplanung gehörenden Daten in einem komprimierten {@link GostKlausurenCollectionAllData}-Objekt.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param hjData     die Liste der {@link GostKlausurenCollectionHjData}-Objekte, für die Klausurdaten geladen werden sollen
	 *
	 * @return das komprimierte {@link GostKlausurenCollectionAllData}-Objekt mit den Daten zur Klausurplanung
	 */
	@POST
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/collection/alldata/gzip")
	@Operation(summary = "Liefert alle zur Klausurplanung gehörenden Daten in einem komprimierten GostKlausurenCollectionAllData-Objekt.",
			description = "Liefert alle zur Klausurplanung gehörenden Daten in einem komprimierten GostKlausurenCollectionAllData-Objekt. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Daten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die GZip-komprimierten Daten der Klausurplanung für den angegebenen Abiturjahrgang und das Halbjahr.",
			content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM, schema = @Schema(type = "string", format = "binary")))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Klausurplanung der Gymnasialen Oberstufe auszulesen.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Es ist ein unerwarteter interner Fehler aufgetreten.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response getGostKlausurenCollectionAlldataGZip(@PathParam("schema") final String schema,
			@RequestBody(description = "die IDs der GostSchuelerklausuren", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
			array = @ArraySchema(schema = @Schema(implementation = GostKlausurenCollectionHjData.class)))) final List<GostKlausurenCollectionHjData> hjData,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransactionOnErrorSimpleResponse(
				conn -> JSONMapper.gzipFileResponseFromObject(DataGostKlausuren.getAllData(conn, hjData), "klausurdaten.json.gz"),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
	}

	/**
	 * Liefert alle zur Klausurplanung gehörenden Fehler und Probleme in einem {@link GostKlausurenCollectionHjData}-Objekt.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das gesuchte Gost-Halbjahr
	 *
	 * @return das {@link GostKlausurenCollectionHjData}-Objekt mit den Fehlern und Problemen zur Klausurplanung
	 */
	@GET
	@Path("/collection/issues/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}")
	@Operation(summary = "Liefert alle zur Klausurplanung gehörenden Fehler und Probleme in einem GostKlausurenCollectionHjData-Objekt.",
			description = "Liefert alle zur Klausurplanung gehörenden Fehler und Probleme in einem GostKlausurenCollectionHjData-Objekt."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Das GostKlausurenCollectionAllData-Objekt mit den Fehlern und Problemen der Klausurplanung für den angegebenen Abiturjahrgang und das Halbjahr.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenCollectionHjData.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten auszulesen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenCollectionAllIssues(@PathParam("schema") final String schema,
			@PathParam("abiturjahr") final int abiturjahr,
			@PathParam("halbjahr") final int halbjahr,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON)
						.entity(DataGostKlausuren.getFehlendData(conn, abiturjahr, GostHalbjahr.fromIDorException(halbjahr))).build(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
	}

	/**
	 * Liefert alle zur Klausurplanung gehörenden Fehler und Probleme in einem komprimierten {@link GostKlausurenCollectionHjData}-Objekt.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das gesuchte Gost-Halbjahr
	 *
	 * @return das komprimierte {@link GostKlausurenCollectionHjData}-Objekt mit den Fehlern und Problemen zur Klausurplanung
	 */
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/collection/issues/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}/gzip")
	@Operation(summary = "Liefert alle zur Klausurplanung gehörenden Fehler und Probleme in einem komprimierten GostKlausurenCollectionHjData-Objekt.",
			description = "Liefert alle zur Klausurplanung gehörenden Fehler und Probleme in einem komprimierten GostKlausurenCollectionHjData-Objekt."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Blockungsdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Das komprimierte GostKlausurenCollectionHjData-Objekt mit den Fehlern und Problemen der Klausurplanung für den angegebenen Abiturjahrgang und das Halbjahr.",
			content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM, schema = @Schema(type = "string", format = "binary",
					description = "Die GZip-komprimierten Blockungsdaten der gymnasialen Oberstfue für die angegebene ID")))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Klausurdaten der Gymnasialen Oberstufe auszulesen.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Es ist ein unerwarteter interner Fehler aufgetreten.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response getGostKlausurenCollectionAllIssuesGZip(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
			@PathParam("halbjahr") final int halbjahr, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransactionOnErrorSimpleResponse(
				conn -> DataGostKlausuren.getFehlendDataGZip(conn, abiturjahr, GostHalbjahr.fromIDorException(halbjahr)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
	}

	/**
	 * Erstellt einen neuen {@link GostKlausurtermin}.
	 *
	 * @param schema     das Datenbankschema, in welchem der {@link GostKlausurtermin} erstellt wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param is         JSON-Objekt mit den Daten
	 *
	 * @return der neue {@link GostKlausurtermin}
	 */
	@POST
	@Path("/termine/new")
	@Operation(summary = "Erstellt einen neuen Gost-Klausurtermin und gibt ihn zurück.",
			description = "Erstellt einen neuen Gost-Klausurtermin und gibt ihn zurück."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Gost-Klausurtermins " + "besitzt.")
	@ApiResponse(responseCode = "201", description = "Gost-Klausurtermin wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurtermin.class)))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurtermin anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenKlausurtermin(@PathParam("schema") final String schema, @RequestBody(
			description = "Der Post für die Klausurtermin-Daten", required = true,
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurtermin.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenTermin(conn).addAsResponse(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Erstellt mehrere neue {@link GostSchuelerklausur}en inklusive der zugehörigen {@link GostSchuelerklausurTermin}e.
	 *
	 * @param schema     das Datenbankschema, in welchem der {@link GostSchuelerklausur}en erstellt werden
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param is         JSON-Objekt mit den Daten
	 *
	 * @return das {@link GostKlausurenCollectionData}-Objekt mit den neuen {@link GostSchuelerklausur}en inklusive der zugehörigen {@link GostSchuelerklausurTermin}e
	 */
	@POST
	@Path("/schuelerklausuren/new")
	@Operation(summary = "Erstellt mehrere neue GostSchuelerklausuren inklusive der zugehörigen GostSchuelerklausurTermine.",
			description = "Erstellt mehrere neue GostSchuelerklausuren inklusive der zugehörigen GostSchuelerklausurTermine."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Daten besitzt.")
	@ApiResponse(responseCode = "201", description = "Daten wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenCollectionData.class)))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um GostSchuelerklausuren anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenSchuelerklausuren(@PathParam("schema") final String schema, @RequestBody(
			description = "Der Post für die GostSchuelerklausuren", required = true,
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = GostSchuelerklausur.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenSchuelerklausur(conn).addMultipleAsResponse(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Löscht mehrere {@link GostKursklausur}en.
	 *
	 * @param schema     das Datenbankschema, in welchem die {@link GostKursklausur}en gelöscht werden
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param klausurIds	 die IDs der zu löschenden {@link GostKursklausur}en
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. den gelöschten GostKursklausuren
	 */
	@DELETE
	@Path("/kursklausuren/delete")
	@Operation(summary = "Löscht mehrere GostKursklausuren.", description = "Löscht mehrere GostKursklausuren."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen einer GostKursklausur " + "besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Kursklausuren für die angegebenen IDs wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine GostKursklausur zu löschen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenKursklausuren(@PathParam("schema") final String schema,
			@RequestBody(description = "die IDs der GostKursklausuren", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> klausurIds,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenKursklausur(conn).deleteMultipleAsResponse(klausurIds),
				request,
				ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Löscht mehrere {@link GostKlausurtermin}e.
	 *
	 * @param schema     das Datenbankschema, in welchem die {@link GostKlausurtermin}e gelöscht werden
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param terminIds	 die IDs der zu löschenden {@link GostKlausurtermin}e
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. den gelöschten Klausurterminen
	 */
	@DELETE
	@Path("/termine/delete")
	@Operation(summary = "Löscht mehrere GostKlausurtermine.", description = "Löscht mehrere GostKlausurtermine."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen eines GostKlausurtermins " + "besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Klausurtermine für die angegebenen IDs wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen GostKlausurtermin zu löschen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenKlausurtermine(@PathParam("schema") final String schema,
			@RequestBody(description = "die IDs der GostKlausurtermine", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> terminIds,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenTermin(conn).deleteMultipleAsResponse(terminIds),
				request,
				ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Löscht mehrere {@link GostSchuelerklausur}en inkluive der zugehörigen {@link GostSchuelerklausurTermin}e.
	 *
	 * @param schema     das Datenbankschema, in welchem die {@link GostSchuelerklausur}en gelöscht werden
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param terminIds	 die IDs der zu löschenden {@link GostSchuelerklausur}en
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. den gelöschten Schuelerklausuren
	 */
	@DELETE
	@Path("/schuelerklausuren/delete")
	@Operation(summary = "Löscht mehrere GostSchuelerklausuren.", description = "Löscht mehrere GostSchuelerklausuren."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen einer GostSchuelerklausur " + "besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Schülerklausuren für die angegebenen IDs wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine GostSchuelerklausur zu löschen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenSchuelerklausuren(@PathParam("schema") final String schema,
			@RequestBody(description = "die IDs der GostSchuelerklausuren", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> terminIds,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenSchuelerklausur(conn).deleteMultipleAsResponse(terminIds),
				request,
				ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Patcht einen {@link GostKlausurtermin} und liefert die dadurch geänderten Raumdaten als {@link GostKlausurenCollectionSkrsKrsData}-Objekt zurück.
	 *
	 * @param schema     das Datenbankschema, auf welchem der Patch ausgeführt werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id		 die ID des {@link GostKlausurtermin}s
	 * @param abschnittid die ID des Schuljahresabschnitts
	 * @param is         JSON-Objekt mit den Daten
	 *
	 * @return die durch den Patch geänderten Raumdaten als {@link GostKlausurenCollectionSkrsKrsData}-Objekt
	 */
	@POST
	@Path("/termine/{id : \\d+}/abschnitt/{abschnittid : -?\\d+}")
	@Operation(summary = "Patcht einen Gost-Klausurtermin.", description = "Patcht einen GostKlausurtermin."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines GostKlausurtermins besitzt.")
	@ApiResponse(responseCode = "201", description = "Der Patch wurde erfolgreich in den GostKlausurtermin integriert.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenCollectionSkrsKrsData.class)))
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um GostKlausurtermine zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Kursklausur-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenKlausurtermin(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@PathParam("abschnittid") final long abschnittid,
			@RequestBody(description = "Der Patch für den GostKlausurtermin", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = GostKlausurtermin.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenTermin(conn, abschnittid).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Legt einen neuen {@link GostKlausurraum} an.
	 *
	 * @param schema     das Datenbankschema, in welchem der {@link GostKlausurraum} angelegt wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param is         JSON-Objekt mit den Daten
	 *
	 * @return der neue {@link GostKlausurraum}
	 */
	@POST
	@Path("/raeume/new")
	@Operation(summary = "Erstellt einen neue GostKlausurraum und gibt ihn zurück.", description = "Erstellt einen neue GostKlausurraum und gibt ihn zurück."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines GostKlausurraums besitzt.")
	@ApiResponse(responseCode = "201", description = "GostKlausurraum wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurraum.class)))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen GostKlausurraum anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenRaum(@PathParam("schema") final String schema,
			@RequestBody(description = "Der Post für den GostKlausurraum", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = GostKlausurraum.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenRaum(conn).addAsResponse(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Patcht die Daten eines {@link GostKlausurraum}s.
	 *
	 * @param schema     das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id		 die ID des {@link GostKlausurraum}s
	 * @param is         JSON-Objekt mit den Daten
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/raeume/{id : \\d+}")
	@Operation(summary = "Patcht einen GostKlausurraum.", description = "Patcht einen GostKlausurraum."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines GostKlausurraums besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in den GostKlausurraum integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um GostKlausurräume zu ändern.")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenRaum(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für den GostKlausurraum", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurraum.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenRaum(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Löscht einen {@link GostKlausurraum}.
	 *
	 * @param schema     das Datenbankschema, in welchem der GostKlausurraum gelöscht wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id	 die ID des zu löschenden {@link GostKlausurraum}s
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Klausurraum
	 */
	@DELETE
	@Path("/raeume/delete/{id : \\d+}")
	@Operation(summary = "Löscht einen Gost-Klausurraum.", description = "Löscht einen Gost-Klausurraum."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen eines Gost-Klausurraums " + "besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Klausurraum für die angegebene ID wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Boolean.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurraum zu löschen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenRaum(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenRaum(conn).deleteAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Weist die in den {@link GostKlausurraumRich}-Objekten übergebenen IDs der {@link GostSchuelerklausurTermin}e dem jeweiligen {@link GostKlausurraum} zu.
	 *
	 * @param schema                  das Datenbankschema
	 * @param request                 die Informationen zur HTTP-Anfrage
	 * @param abiturjahr              das Abiturjahr des Jahrgangs
	 * @param halbjahr                das GostHalbjahr
	 * @param raumSchuelerZuteilung   die Liste von {@link GostKlausurraumRich}-Objekten, die die IDs der zuzuteilenden {@link GostSchuelerklausurTermin}en enthalten
	 *
	 * @return das {@link GostKlausurenCollectionSkrsKrsData}-Objekt mit den aktualisierten Raumdaten
	 */
	@POST
	@Path("/schuelerklausuren/zuraum/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : -?\\d+}")
	@Operation(summary = "Weist die in den GostKlausurraumRich-Objekten übergebenen IDs der GostSchuelerklausurTermine dem jeweiligen GostKlausurraum zu.",
			description = "Weist die in den GostKlausurraumRich-Objekten übergebenen IDs der GostSchuelerklausurTermine dem jeweiligen GostKlausurraum zu."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Zuweisen eines Klausurraums besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Raumzuweisungen wurden erfolgreich übernommen.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenCollectionSkrsKrsData.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Räume zuzuweisen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response setzeGostSchuelerklausurenZuRaum(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
			@PathParam("halbjahr") final int halbjahr,
			@RequestBody(description = "Die Liste von GostKlausurraumRich-Objekten, die die zuzuweisenden GostSchuelerklausurTermine-IDs enthalten.", required = false, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = GostKlausurraumRich.class)))) final List<GostKlausurraumRich> raumSchuelerZuteilung,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataGostKlausurenSchuelerklausurraumstunde(conn).setzeRaumZuSchuelerklausuren(raumSchuelerZuteilung, abiturjahr, GostHalbjahr.fromIDorException(halbjahr)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Löscht die Raumzuweisungen für alle in den {@link GostKlausurraumRich}-Objekten übergebene {@link GostSchuelerklausurTermin}-IDs.
	 *
	 * @param schema                  das Datenbankschema
	 * @param request                 die Informationen zur HTTP-Anfrage
	 * @param raumSchuelerZuteilung   die Ids der GostSchuelerklausuren
	 *
	 * @return die HTTP-Antwort
	 */
	@POST
	@Path("/schuelerklausuren/ausraum")
	@Operation(summary = "Löscht die Raumzuweisungen für alle in den GostKlausurraumRich-Objekten übergebene GostSchuelerklausurTermin-IDs.",
			description = "Löscht die Raumzuweisungen für alle in den GostKlausurraumRich-Objekten übergebene GostSchuelerklausurTermin-IDs"
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen einer Raumzuweisung besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Raumzuweisungen wurde erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenCollectionSkrsKrsData.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Raumzuweisungen zu löschen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response loescheGostSchuelerklausurenAusRaum(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Liste von GostKlausurraumRich-Objekten, die die aus Räumen zu löschenden GostSchuelerklausurTermine-IDs enthalten.", required = false, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = GostKlausurraumRich.class)))) final List<GostKlausurraumRich> raumSchuelerZuteilung,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataGostKlausurenSchuelerklausurraumstunde(conn).loescheRaumZuSchuelerklausuren(raumSchuelerZuteilung),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Startet den Kursklausur-Blockungsalgorithmus für die übergebenen {@link GostKlausurterminblockungDaten}.
	 *
	 * @param schema          das Datenbankschema, auf dem die Klausurblockung gestartet wird
	 * @param request         die Informationen zur HTTP-Anfrage
	 * @param blockungDaten   das {@link GostKlausurterminblockungDaten}-Objekt
	 *
	 * @return die HTTP-Antwort mit dem Status und dem {@link GostKlausurenCollectionData}-Objekt, das die fertige Klausurblockung enthält
	 */
	@POST
	@Path("/kursklausuren/blocken")
	@Operation(summary = "Startet den Kursklausur-Blockungsalgorithmus für die übergebenen GostKlausurterminblockungDaten.",
			description = "Startet den Kursklausur-Blockungsalgorithmus für die übergebenen GostKlausurterminblockungDaten."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Blocken besitzt.")
	@ApiResponse(responseCode = "200", description = "Klausurblockung wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenCollectionData.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Klausurblockung durchzuführen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response blockenGostKursklausuren(@PathParam("schema") final String schema,
			@RequestBody(description = "Die GostKlausurterminblockungDaten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = GostKlausurterminblockungDaten.class))) final GostKlausurterminblockungDaten blockungDaten,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransactionOnErrorSimpleResponse(
				conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(new DataGostKlausurenKursklausur(conn).blocken(blockungDaten))
						.build(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Startet den Nachschreiber-Blockungsalgorithmus für die übergebene {@link GostNachschreibterminblockungKonfiguration}.
	 *
	 * @param schema     das Datenbankschema, auf dem die Klausurblockung gestartet wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param config     das {@link GostNachschreibterminblockungKonfiguration}-Objekt
	 *
	 * @return die HTTP-Antwort mit dem Status und dem {@link GostKlausurenCollectionData}-Objekt, das die fertige Nachschreibblockung enthält
	 */
	@POST
	@Path("/schuelerklausuren/termine/blocken")
	@Operation(summary = "Startet den Nachschreiber-Blockungsalgorithmus für die übergebenen GostNachschreibterminblockungKonfiguration.",
			description = "Startet den Nachschreiber-Blockungsalgorithmus für die übergebenen GostNachschreibterminblockungKonfiguration."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Blocken besitzt.")
	@ApiResponse(responseCode = "200", description = "Klausurblockung wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenCollectionData.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Klausurblockung durchzuführen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response blockenGostSchuelerklausurtermine(@PathParam("schema") final String schema,
			@RequestBody(description = "Die GostNachschreibterminblockungKonfiguration", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(
							implementation = GostNachschreibterminblockungKonfiguration.class))) final GostNachschreibterminblockungKonfiguration config,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(new DataGostKlausurenSchuelerklausurTermin(conn).blocken(config))
						.build(),
				request,
				ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Fragt die Klausurdaten eines Schülers ab.
	 *
	 * @param schema       das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param sId 		   die Id des Schülers
	 * @param abiturjahr   das Jahr, in welchem der Jahrgang des Schülers Abitur machen wird
	 * @param halbjahr     das Gost-Halbjahr
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return das {@link GostKlausurenCollectionData}-Objekt mit den Klausurdaten des Schülers
	 */
	@GET
	@Path("/schueler/{sid : -?\\d+}/abiturjahrgang/{abiturjahr : -?\\d+}/schuljahr/{halbjahr : \\d+}")
	@Operation(summary = "Fragt die Klausurdaten eines Schülers ab.",
			description = "Fragt die Klausurdaten eines Schülers ab."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Abfrage war erfolgreich.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenCollectionData.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Schüler-ID wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenCollectionBySchuelerid(@PathParam("schema") final String schema, @PathParam("sid") final long sId,
			@PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final int halbjahr, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> Response.status(Status.OK).type(MediaType.APPLICATION_JSON)
						.entity(new DataGostKlausurenSchuelerklausur(conn).getGostKlausurenCollectionBySchuelerid(sId, abiturjahr, halbjahr)).build(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
	}

	/**
	 * Legt einen neuen {@link GostSchuelerklausurTermin} an. Dies ist meist ein Nachschreibtermin.
	 *
	 * @param schema    das Datenbankschema, in welchem der {@link GostSchuelerklausurTermin} erstellt wird
	 * @param request   die Informationen zur HTTP-Anfrage
	 * @param id        die ID der {@link GostSchuelerklausur}, zu der ein neuer {@link GostSchuelerklausurTermin} generiert werden soll.
	 *
	 * @return die HTTP-Antwort mit dem neuen {@link GostSchuelerklausurTermin}
	 */
	@GET
	@Path("/schuelerklausuren/termine/new/{id : \\d+}")
	@Operation(summary = "Erstellt einen neuen GostSchuelerklausurTermin für die als ID übergebene GostSchuelerklausur und gibt ihn zurück.",
			description = "Erstellt einen neuen GostSchuelerklausurTermin für die als ID übergebene GostSchuelerklausur und gibt ihn zurück."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines GostSchuelerklausurTermins " + "besitzt.")
	@ApiResponse(responseCode = "201", description = "GostSchuelerklausurTermin wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostSchuelerklausurTermin.class)))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen GostSchuelerklausurTermin anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenSchuelerklausurtermin(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenSchuelerklausurTermin(conn).create(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Löscht einen {@link GostSchuelerklausurTermin}.
	 *
	 * @param schema     das Datenbankschema, in welchem der {@link GostSchuelerklausurTermin} gelöscht wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id         die ID des zu löschenden {@link GostSchuelerklausurTermin}s.
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Schuelerklausurtermin
	 */
	@DELETE
	@Path("/schuelerklausuren/termine/delete/{id : \\d+}")
	@Operation(summary = "Löscht einen GostSchuelerklausurTermin.", description = "Löscht einen GostSchuelerklausurTermin."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen eines GostSchuelerklausurTermins " + "besitzt.")
	@ApiResponse(responseCode = "200", description = "GostSchuelerklausurTermin wurde erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Boolean.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen GostSchuelerklausurTermin zu löschen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenSchuelerklausurtermin(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenSchuelerklausurTermin(conn).deleteAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Patcht einen {@link GostSchuelerklausurTermin}.
	 *
	 * @param schema     das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id		 die ID des {@link GostSchuelerklausurTermin}s
	 * @param is         JSON-Objekt mit den Daten
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/schuelerklausuren/termine/{id : \\d+}")
	@Operation(summary = "Patcht einen GostSchuelerklausurTermin.", description = "Patcht einen GostSchuelerklausurTermin."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines GostSchuelerklausurTermin besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in den GostSchuelerklausurTermin integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um GostSchuelerklausurTermine zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein GostSchuelerklausurTermin-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenSchuelerklausurtermin(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für den GostSchuelerklausurTermin", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = GostSchuelerklausurTermin.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataGostKlausurenSchuelerklausurTermin(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen einer neuen Klausurraumstunde.
	 *
	 * @param schema     das Datenbankschema
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param update     das GostNachschreibterminblockungKonfiguration-Objekt
	 *
	 * @return die HTTP-Antwort
	 */
	@POST
	@Path("/update")
	@Operation(summary = "Patcht einen Gost-Klausurtermin.", description = "Patcht einen Gost-Klausurtermin."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines Gost-Klausurtermins besitzt.")
	@ApiResponse(responseCode = "200", description = "Gost-Klausurraumstunde wurde erfolgreich angelegt.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einer Gost-Klausurraumstunde anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response updateGostKlausuren(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der Schülerklausuren", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = GostKlausurenUpdate.class))) final GostKlausurenUpdate update,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> {
			final List<DTOGostKlausurenSchuelerklausurenTermine> list =
					DataGostKlausurenSchuelerklausurTermin.getSchuelerklausurterminDTOsById(conn, update.listSchuelerklausurTermineRemoveIdTermin);
			for (final DTOGostKlausurenSchuelerklausurenTermine skt : list)
				skt.Termin_ID = null;
			conn.transactionPersistAll(list);
			final List<DTOGostKlausurenTermine> listTermine =
					DataGostKlausurenTermin.getKlausurterminDTOsZuIds(conn, update.listKlausurtermineNachschreiberZugelassenFalse);
			for (final DTOGostKlausurenTermine ts : listTermine)
				ts.NachschreiberZugelassen = false;
			conn.transactionPersistAll(listTermine);
			return Response.status(Status.NO_CONTENT).build();
		},
				request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
	}

}
