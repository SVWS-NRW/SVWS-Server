package de.svws_nrw.api.server;

import java.util.List;

import de.svws_nrw.controller.gost.klausuren.GostKlausurenControllerFactory;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenAlleKlausurdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenHalbjahresdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenKlausurdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenPatchResponseData;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraumRich;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurterminblockungDaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.core.data.gost.klausuren.GostNachschreibterminblockungKonfiguration;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausurtermin;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.service.gost.klausuren.GostKlausurenKursklausurPatchRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenRaumCreateRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenRaumPatchRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurCreateRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurPatchRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurterminCreateRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurterminPatchRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenTerminCreateRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenTerminPatchRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenVorgabeCreateRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenVorgabePatchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
import org.jboss.resteasy.annotations.GZIP;

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
	@GZIP
	@Path("/vorgaben/abiturjahrgang/{abiturjahr : -?\\d+}")
	@Operation(summary = "Liefert die Klausurvorgaben eines Abiturjahrgangs der gymnasialen Oberstufe.",
			description = "Liefert die Klausurvorgaben eines Abiturjahrgangs der gymnasialen Oberstufe. "
					+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Klausurvorgaben.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostKlausurvorgabe.class))))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Klausurvorgaben auszulesen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenVorgabenJahrgang(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withReadAccess(request)
				.getGostKlausurenVorgabeController().getListByAbiturjahr(abiturjahr);
	}

	/**
	 * Erstellt eine neue {@link GostKlausurvorgabe} und gibt sie zurück.
	 *
	 * @param schema     das Datenbankschema, in welchem die {@link GostKlausurvorgabe} erstellt wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param createRequest JSON-Objekt mit den Daten
	 *
	 * @return 			 die HTTP-Antwort mit der neuen {@link GostKlausurvorgabe}
	 */
	@POST
	@Path("/vorgaben")
	@Operation(summary = "Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück.",
			description = "Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück."
					+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Gost-Klausurvorgabe besitzt.")
	@ApiResponse(responseCode = "201", description = "Gost-Klausurvorgabe wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurvorgabe.class)))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe anzulegen.")
	@ApiResponse(responseCode = "409", description = "Die Gost-Klausurvorgabe ist schon in der Datenbank enthalten.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenVorgabe(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Create-Daten für die Klausurvorgabe", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = GostKlausurvorgabe.class))) @Valid final GostKlausurenVorgabeCreateRequest createRequest,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenVorgabeController().create(createRequest);
	}

	/**
	 * Patcht die Daten einer {@link GostKlausurvorgabe}.
	 *
	 * @param schema     das Datenbankschema, auf welchem der Patch ausgeführt werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param patch      JSON-Objekt mit den Patch-Daten
	 *
	 * @return die HTTP-Antwort
	 */
	@PATCH
	@Path("/vorgaben")
	@Operation(summary = "Patcht eine Gost-Klausurvorgabe.", description = "Patcht eine Gost-Klausurvorgabe."
			+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen einer Gost-Klausurvorgabe besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Klausurvorgabe integriert.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurvorgabe.class)))
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Klausurvorgaben zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Klausurvorgabe-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409",
			description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenVorgabe(@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für die Klausurvorgabe-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = GostKlausurvorgabe.class))) @Valid final GostKlausurenVorgabePatchRequest patch,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenVorgabeController().patch(patch);
	}

	/**
	 * Patcht die Daten mehrerer {@link GostKlausurvorgabe}n.
	 *
	 * @param schema     das Datenbankschema, auf welchem der Patch ausgeführt werden soll
	 * @param patches    JSON-Array mit den Patch-Daten
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@PATCH
	@Path("/vorgaben/multiple")
	@Operation(summary = "Patcht mehrere Gost-Klausurvorgaben.", description = "Patcht mehrere Gost-Klausurvorgaben."
			+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen von Gost-Klausurvorgaben besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Patches wurden erfolgreich in die Klausurvorgaben integriert.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = GostKlausurvorgabe.class))))
	@ApiResponse(responseCode = "400", description = "Die Patches sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Klausurvorgaben zu ändern.")
	@ApiResponse(responseCode = "404", description = "Mindestens ein Klausurvorgabe-Eintrag mit der angegebenen ID wurde nicht gefunden")
	@ApiResponse(responseCode = "409",
			description = "Ein Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenVorgabenMultiple(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Patches für die Klausurvorgabe-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(
							schema = @Schema(implementation = GostKlausurvorgabe.class)))) @Valid final List<GostKlausurenVorgabePatchRequest> patches,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenVorgabeController().patchMultiple(patches);
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
	@Path("/vorgaben/{id : \\d+}")
	@Operation(summary = "Löscht eine Gost-Klausurvorgabe.", description = "Löscht eine Gost-Klausurvorgabe."
			+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen einer Gost-Klausurvorgabe besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Klausurvorgabe für die angegebene ID wurde erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurvorgabe.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe zu löschen.")
	@ApiResponse(responseCode = "404", description = "Die Gost-Klausurvorgabe wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenVorgabe(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenVorgabeController().delete(id);
	}

	/**
	 * Die OpenAPI-Methode für das Löschen mehrerer {@link GostKlausurvorgabe}n.
	 *
	 * @param schema     das Datenbankschema, in welchem die {@link GostKlausurvorgabe} gelöscht wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param ids	     die IDs der zu löschenden Klausurvorgaben
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/vorgaben/multiple")
	@Operation(summary = "Löscht mehrere Gost-Klausurvorgaben.", description = "Löscht mehrere Gost-Klausurvorgaben."
			+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen von Gost-Klausurvorgaben besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Einträge wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = GostKlausurvorgabe.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Gost-Klausurvorgaben zu löschen.")
	@ApiResponse(responseCode = "404", description = "Mindestens eine Gost-Klausurvorgabe wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenVorgabenMultiple(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Klausurvorgaben", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> ids,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenVorgabeController().deleteMultiple(ids);
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
	@POST
	@Path("/vorgaben/vorlagen/kopieren/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : -?\\d+}/quartal/{quartal : -?\\d+}")
	@Operation(summary = "Kopiert die Klausurvorgabe-Vorlagen in einen konkreten Abiturjahrgang und gibt sie zurück.",
			description = "Kopiert die Klausurvorgabe-Vorlagen in einen konkreten Abiturjahrgang und gibt sie zurück."
					+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anlegen der Gost-Klausurvorgaben besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der kopierten Klausurvorgaben.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostKlausurvorgabe.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Gost-Klausurvorgaben anzulegen.")
	@ApiResponse(responseCode = "400", description = "Falsche Parameter")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response copyGostKlausurenVorgabenVorlagenToJahrgang(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
			@PathParam("halbjahr") final int halbjahr, @PathParam("quartal") final int quartal, @Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request).getGostKlausurenVorgabeVorlagenController()
				.copyVorlagenToJahrgang(abiturjahr, halbjahr, quartal);
	}

	/**
	 * Legt fehlende {@link GostKlausurvorgabe}-Vorlagen an und gibt sie zurück.
	 *
	 * @param schema     das Datenbankschema, in welchem die {@link GostKlausurvorgabe}n erstellt werden
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param halbjahr     das GostHalbjahr, für das die {@link GostKlausurvorgabe}n erstellt werden sollen
	 * @param quartal      das Quartal, 0 für das gesamte GostHalbjahr
	 *
	 * @return die Liste der neuen {@link GostKlausurvorgabe}-Vorlagen im angegebenen GostHalbjahr und Quartal
	 */
	@POST
	@Path("/vorgaben/vorlagen/fehlende-erstellen/halbjahr/{halbjahr : -?\\d+}/quartal/{quartal : -?\\d+}")
	@Operation(summary = "Legt fehlende Klausurvorgabe-Vorlagen an und gibt sie zurück.",
			description = "Legt fehlende Klausurvorgabe-Vorlagen an und gibt sie zurück."
					+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen von Gost-Klausurvorgabe-Vorlagen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der neuen Klausurvorgabe-Vorlagen.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostKlausurvorgabe.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Gost-Klausurvorgabe-Vorlagen anzulegen.")
	@ApiResponse(responseCode = "400", description = "Falsche Parameter")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createMissingGostKlausurenVorgabenVorlagen(@PathParam("schema") final String schema, @PathParam("halbjahr") final int halbjahr,
			@PathParam("quartal") final int quartal, @Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request).getGostKlausurenVorgabeVorlagenController()
				.createMissingVorlagen(halbjahr, quartal);
	}

	/**
	 * Patcht eine {@link GostKursklausur} und gibt die daraufhin geänderten Daten innerhalb des {@link GostKlausurenPatchResponseData}-Objekts zurück.
	 *
	 * @param schema        das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 * @param patchRequest  JSON-Objekt mit den Patch-Daten und der ID der Kursklausur
	 *
	 * @return die geänderten Raumdaten als {@link GostKlausurenPatchResponseData}-Objekt
	 */
	@PATCH
	@Path("/kursklausuren")
	@Operation(summary = "Patcht eine Gost-Kursklausur und gibt die daraufhin geänderten Raumdaten zurück.",
			description = "Patcht eine Gost-Kursklausur und gibt die daraufhin geänderten Raumdaten zurück."
					+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen einer Gost-Kursklausur besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Kursklausur integriert.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenPatchResponseData.class)))
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Kursklausuren zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Kursklausur-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenKursklausur(@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für die Kursklausur-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = GostKursklausur.class))) @Valid final GostKlausurenKursklausurPatchRequest patchRequest,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenKursklausurWorkflowController().patch(patchRequest);
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
	 * @return das {@link GostKlausurenKlausurdaten}-Objekt mit den erzeugten Daten
	 */
	@POST
	@Path("/kursklausuren/erzeugen/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}/quartal/{quartal : \\d+}")
	@Operation(
			summary = "Erzeugt die Kursklausuren eines Abiturjahrgangs in einem bestimmten GostHalbjahr und Quartal der Gymnasialen Oberstufe. Schülerklausuren und Haupttermin-Schülerklausurtermine werden ebenfalls erzeugt.",
			description = "Erzeugt die Kursklausuren eines Abiturjahrgangs in einem bestimmten GostHalbjahr und Quartal der Gymnasialen Oberstufe. Schülerklausuren und Haupttermin-Schülerklausurtermine werden ebenfalls erzeugt."
					+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Kursklausuren besitzt.")
	@ApiResponse(responseCode = "200", description = "Das Klausurdaten-Objekt mit den erzeugten Kursklausuren, Schülerklausuren und Schülerklausurterminen.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenKlausurdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren zu erzeugen.")
	@ApiResponse(responseCode = "404", description = "Keine Klausurvorgaben definiert oder der Schuljahresabschnitt wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenKursklausurenJahrgangHalbjahrQuartal(@PathParam("schema") final String schema,
			@PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final int halbjahr, @PathParam("quartal") final int quartal,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenKursklausurWorkflowController().create(abiturjahr, halbjahr, quartal);
	}

	/**
	 * Patcht die Daten einer {@link GostSchuelerklausur}.
	 *
	 * @param schema     das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param patchRequest JSON-Objekt mit den Patch-Daten und der ID der {@link GostSchuelerklausur}
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/schuelerklausuren")
	@Operation(summary = "Patcht eine Gost-Schuelerklausur.", description = "Patcht eine Gost-Schuelerklausur."
			+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen einer Gost-Schuelerklausur besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Schuelerklausur integriert.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostSchuelerklausur.class)))
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuelerklausuren zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Schuelerklausur-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenSchuelerklausur(@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für die Schuelerklausur-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = GostSchuelerklausur.class))) @Valid final GostKlausurenSchuelerklausurPatchRequest patchRequest,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenSchuelerklausurController().patch(patchRequest);
	}

	/**
	 * Liefert alle zur Klausurplanung gehörenden Daten in einem {@link GostKlausurenAlleKlausurdaten}-Objekt.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param hjData     die Liste der {@link GostKlausurenHalbjahresdaten}-Objekte, für die Klausurdaten geladen werden sollen
	 *
	 * @return das {@link GostKlausurenAlleKlausurdaten}-Objekt mit den Daten zur Klausurplanung
	 */
	@POST
	@GZIP
	@Path("/daten/alle")
	@Operation(summary = "Liefert alle zur Klausurplanung gehörenden Daten in einem GostKlausurenAlleKlausurdaten-Objekt.",
			description = "Liefert alle zur Klausurplanung gehörenden Daten in einem GostKlausurenAlleKlausurdaten-Objekt."
					+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Daten besitzt.")
	@ApiResponse(responseCode = "200",
			description = "Das GostKlausurenAlleKlausurdaten-Objekt mit den Daten der Klausurplanung für den angegebenen Abiturjahrgang und das Halbjahr.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenAlleKlausurdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das GostHalbjahr wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenAlleKlausurdaten(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Halbjahresdaten, für die Klausurdaten geladen werden sollen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(
									schema = @Schema(implementation = GostKlausurenHalbjahresdaten.class)))) final List<GostKlausurenHalbjahresdaten> hjData,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withReadAccess(request)
				.getGostKlausurenKlausurdatenController().getAllData(hjData);
	}

	/**
	 * Liefert alle zur Klausurplanung gehörenden Daten in einem komprimierten {@link GostKlausurenAlleKlausurdaten}-Objekt.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param hjData     die Liste der {@link GostKlausurenHalbjahresdaten}-Objekte, für die Klausurdaten geladen werden sollen
	 *
	 * @return das komprimierte {@link GostKlausurenAlleKlausurdaten}-Objekt mit den Daten zur Klausurplanung
	 */
	@POST
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/daten/alle/gzip")
	@Operation(summary = "Liefert alle zur Klausurplanung gehörenden Daten in einem komprimierten GostKlausurenAlleKlausurdaten-Objekt.",
			description = "Liefert alle zur Klausurplanung gehörenden Daten in einem komprimierten GostKlausurenAlleKlausurdaten-Objekt. "
					+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Daten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die GZip-komprimierten Daten der Klausurplanung für den angegebenen Abiturjahrgang und das Halbjahr.",
			content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM, schema = @Schema(type = "string", format = "binary")))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Klausurplanung der Gymnasialen Oberstufe auszulesen.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das GostHalbjahr wurde nicht gefunden.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Es ist ein unerwarteter interner Fehler aufgetreten.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response getGostKlausurenAlleKlausurdatenGZip(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Halbjahresdaten, für die Klausurdaten geladen werden sollen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(
									schema = @Schema(implementation = GostKlausurenHalbjahresdaten.class)))) final List<GostKlausurenHalbjahresdaten> hjData,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withReadAccess(request)
				.getGostKlausurenKlausurdatenController().getAllDataGZip(hjData);
	}

	/**
	 * Liefert die Klausurdaten-Issues in einem {@link GostKlausurenHalbjahresdaten}-Objekt.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das gesuchte Gost-Halbjahr
	 *
	 * @return das {@link GostKlausurenHalbjahresdaten}-Objekt mit den Klausurdaten-Issues
	 */
	@GET
	@GZIP
	@Path("/daten/issues/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}")
	@Operation(summary = "Liefert die Klausurdaten-Issues in einem GostKlausurenHalbjahresdaten-Objekt.",
			description = "Liefert die Klausurdaten-Issues in einem GostKlausurenHalbjahresdaten-Objekt."
					+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200",
			description = "Das GostKlausurenHalbjahresdaten-Objekt mit den Klausurdaten-Issues für den angegebenen Abiturjahrgang und das Halbjahr.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenHalbjahresdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten auszulesen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenKlausurdatenIssues(@PathParam("schema") final String schema,
			@PathParam("abiturjahr") final int abiturjahr,
			@PathParam("halbjahr") final int halbjahr,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withReadAccess(request)
				.getGostKlausurenKlausurdatenController().getKlausurdatenIssues(abiturjahr, GostHalbjahr.fromIDorException(halbjahr));
	}

	/**
	 * Liefert die Klausurdaten-Issues in einem komprimierten {@link GostKlausurenHalbjahresdaten}-Objekt.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das gesuchte Gost-Halbjahr
	 *
	 * @return das komprimierte {@link GostKlausurenHalbjahresdaten}-Objekt mit den Klausurdaten-Issues
	 */
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/daten/issues/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}/gzip")
	@Operation(summary = "Liefert die Klausurdaten-Issues in einem komprimierten GostKlausurenHalbjahresdaten-Objekt.",
			description = "Liefert die Klausurdaten-Issues in einem komprimierten GostKlausurenHalbjahresdaten-Objekt."
					+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Klausurdaten-Issues besitzt.")
	@ApiResponse(responseCode = "200",
			description = "Das komprimierte GostKlausurenHalbjahresdaten-Objekt mit den Klausurdaten-Issues für den angegebenen Abiturjahrgang und das Halbjahr.",
			content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM, schema = @Schema(type = "string", format = "binary",
					description = "Die GZip-komprimierten Klausurdaten-Issues der gymnasialen Oberstufe für den angegebenen Abiturjahrgang und das Halbjahr")))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Klausurdaten der Gymnasialen Oberstufe auszulesen.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Es ist ein unerwarteter interner Fehler aufgetreten.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response getGostKlausurenKlausurdatenIssuesGZip(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
			@PathParam("halbjahr") final int halbjahr, @Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withReadAccess(request)
				.getGostKlausurenKlausurdatenController().getKlausurdatenIssuesGZip(abiturjahr, GostHalbjahr.fromIDorException(halbjahr));
	}

	/**
	 * Erstellt einen neuen {@link GostKlausurtermin}.
	 *
	 * @param schema     das Datenbankschema, in welchem der {@link GostKlausurtermin} erstellt wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param createRequest JSON-Objekt mit den Daten
	 *
	 * @return der neue {@link GostKlausurtermin}
	 */
	@POST
	@Path("/termine")
	@Operation(summary = "Erstellt einen neuen Gost-Klausurtermin und gibt ihn zurück.",
			description = "Erstellt einen neuen Gost-Klausurtermin und gibt ihn zurück."
					+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Gost-Klausurtermins " + "besitzt.")
	@ApiResponse(responseCode = "201", description = "Gost-Klausurtermin wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurtermin.class)))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurtermin anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenKlausurtermin(@PathParam("schema") final String schema, @RequestBody(
			description = "Die Create-Daten für den Klausurtermin", required = true,
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = GostKlausurtermin.class))) @Valid final GostKlausurenTerminCreateRequest createRequest,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenTerminController().create(createRequest);
	}

	/**
	 * Erstellt mehrere neue {@link GostSchuelerklausur}en inklusive der zugehörigen {@link GostSchuelerklausurtermin}e.
	 *
	 * @param schema     das Datenbankschema, in welchem die {@link GostSchuelerklausur}en erstellt werden
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param createRequests JSON-Objekte mit den Daten
	 *
	 * @return das {@link GostKlausurenKlausurdaten}-Objekt mit den neuen {@link GostSchuelerklausur}en inklusive der zugehörigen {@link GostSchuelerklausurtermin}e
	 */
	@POST
	@Path("/schuelerklausuren/multiple")
	@Operation(summary = "Erstellt mehrere neue GostSchuelerklausuren inklusive der zugehörigen GostSchuelerklausurtermine.",
			description = "Erstellt mehrere neue GostSchuelerklausuren inklusive der zugehörigen GostSchuelerklausurtermine."
					+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Daten besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Daten wurden erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenKlausurdaten.class)))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um GostSchuelerklausuren anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenSchuelerklausuren(@PathParam("schema") final String schema, @RequestBody(
			description = "Die Create-Daten für die GostSchuelerklausuren", required = true,
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(
							schema = @Schema(
									implementation = GostSchuelerklausur.class)))) @Valid final List<GostKlausurenSchuelerklausurCreateRequest> createRequests,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenSchuelerklausurWorkflowController().createMultiple(createRequests);
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
	@Path("/kursklausuren/multiple")
	@Operation(summary = "Löscht mehrere GostKursklausuren.", description = "Löscht mehrere GostKursklausuren."
			+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen von GostKursklausuren besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Kursklausuren für die angegebenen IDs wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostKursklausur.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um GostKursklausuren zu löschen.")
	@ApiResponse(responseCode = "404", description = "Mindestens eine GostKursklausur wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenKursklausuren(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der GostKursklausuren", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> klausurIds,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenKursklausurController().deleteMultiple(klausurIds);
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
	@Path("/termine/multiple")
	@Operation(summary = "Löscht mehrere GostKlausurtermine.", description = "Löscht mehrere GostKlausurtermine."
			+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen von GostKlausurterminen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Klausurtermine für die angegebenen IDs wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostKlausurtermin.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um GostKlausurtermine zu löschen.")
	@ApiResponse(responseCode = "404", description = "Mindestens ein GostKlausurtermin wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenKlausurtermine(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der GostKlausurtermine", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> terminIds,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenTerminController().deleteMultiple(terminIds);
	}

	/**
	 * Löscht mehrere {@link GostSchuelerklausur}en inklusive der zugehörigen {@link GostSchuelerklausurtermin}e.
	 *
	 * @param schema     das Datenbankschema, in welchem die {@link GostSchuelerklausur}en gelöscht werden
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param schuelerklausurIds	 die IDs der zu löschenden {@link GostSchuelerklausur}en
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. den gelöschten Schuelerklausuren
	 */
	@DELETE
	@Path("/schuelerklausuren/multiple")
	@Operation(summary = "Löscht mehrere GostSchuelerklausuren.", description = "Löscht mehrere GostSchuelerklausuren."
			+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen von GostSchuelerklausuren besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Schülerklausuren für die angegebenen IDs wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = GostSchuelerklausur.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um GostSchuelerklausuren zu löschen.")
	@ApiResponse(responseCode = "404", description = "Mindestens eine GostSchuelerklausur wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenSchuelerklausuren(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der GostSchuelerklausuren", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> schuelerklausurIds,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenSchuelerklausurController().deleteMultiple(schuelerklausurIds);
	}

	/**
	 * Patcht einen {@link GostKlausurtermin} und liefert die dadurch geänderten Raumdaten als {@link GostKlausurenPatchResponseData}-Objekt zurück.
	 *
	 * @param schema     das Datenbankschema, auf welchem der Patch ausgeführt werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param patchRequest JSON-Objekt mit den Patch-Daten und der ID des {@link GostKlausurtermin}s
	 *
	 * @return die durch den Patch geänderten Raumdaten als {@link GostKlausurenPatchResponseData}-Objekt
	 */
	@PATCH
	@Path("/termine")
	@Operation(summary = "Patcht einen Gost-Klausurtermin.", description = "Patcht einen GostKlausurtermin."
			+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines GostKlausurtermins besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in den GostKlausurtermin integriert.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenPatchResponseData.class)))
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um GostKlausurtermine zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Klausurtermin-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenKlausurtermin(@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für den GostKlausurtermin", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = GostKlausurtermin.class))) @Valid final GostKlausurenTerminPatchRequest patchRequest,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenTerminWorkflowController().patch(patchRequest);
	}

	/**
	 * Legt einen neuen {@link GostKlausurraum} an.
	 *
	 * @param schema     das Datenbankschema, in welchem der {@link GostKlausurraum} angelegt wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param createRequest JSON-Objekt mit den Daten
	 *
	 * @return der neue {@link GostKlausurraum}
	 */
	@POST
	@Path("/raeume")
	@Operation(summary = "Erstellt einen neuen GostKlausurraum und gibt ihn zurück.", description = "Erstellt einen neuen GostKlausurraum und gibt ihn zurück."
			+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines GostKlausurraums besitzt.")
	@ApiResponse(responseCode = "201", description = "GostKlausurraum wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurraum.class)))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen GostKlausurraum anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenRaum(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die Create-Daten für den GostKlausurraum", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = GostKlausurraum.class))) @Valid final GostKlausurenRaumCreateRequest createRequest,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenRaumController().create(createRequest);
	}

	/**
	 * Patcht die Daten eines {@link GostKlausurraum}s.
	 *
	 * @param schema     das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param patchRequest JSON-Objekt mit den Patch-Daten und der ID des {@link GostKlausurraum}s
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/raeume")
	@Operation(summary = "Patcht einen GostKlausurraum.", description = "Patcht einen GostKlausurraum."
			+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines GostKlausurraums besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in den GostKlausurraum integriert.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurraum.class)))
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um GostKlausurräume zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein GostKlausurraum-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenRaum(@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für den GostKlausurraum", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = GostKlausurraum.class))) @Valid final GostKlausurenRaumPatchRequest patchRequest,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenRaumController().patch(patchRequest);
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
	@Path("/raeume/{id : \\d+}")
	@Operation(summary = "Löscht einen Gost-Klausurraum.", description = "Löscht einen Gost-Klausurraum."
			+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen eines Gost-Klausurraums " + "besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Klausurraum für die angegebene ID wurde erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurraum.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurraum zu löschen.")
	@ApiResponse(responseCode = "404", description = "Der Gost-Klausurraum wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenRaum(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenRaumController().delete(id);
	}

	/**
	 * Weist die in den {@link GostKlausurraumRich}-Objekten übergebenen IDs der {@link GostSchuelerklausurtermin}e dem jeweiligen {@link GostKlausurraum} zu.
	 *
	 * @param schema                  das Datenbankschema
	 * @param request                 die Informationen zur HTTP-Anfrage
	 * @param raumSchuelerZuteilung   die Liste von {@link GostKlausurraumRich}-Objekten, die die IDs der zuzuteilenden {@link GostSchuelerklausurtermin}en enthalten
	 *
	 * @return das {@link GostKlausurenPatchResponseData}-Objekt mit den aktualisierten Raumdaten
	 */
	@POST
	@Path("/schuelerklausuren/termine/raumzuweisungen")
	@Operation(summary = "Weist die in den GostKlausurraumRich-Objekten übergebenen IDs der GostSchuelerklausurtermine dem jeweiligen GostKlausurraum zu.",
			description = "Weist die in den GostKlausurraumRich-Objekten übergebenen IDs der GostSchuelerklausurtermine dem jeweiligen GostKlausurraum zu."
					+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Zuweisen eines Klausurraums besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Raumzuweisungen wurden erfolgreich übernommen.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenPatchResponseData.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Räume zuzuweisen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response setzeGostSchuelerklausurtermineZuRaum(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Liste von GostKlausurraumRich-Objekten, die die zuzuweisenden GostSchuelerklausurtermine-IDs enthalten.",
					required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(
									schema = @Schema(implementation = GostKlausurraumRich.class)))) final List<GostKlausurraumRich> raumSchuelerZuteilung,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenRaumzuweisungController().setzeRaumzuweisungenFuerSchuelerklausurtermine(raumSchuelerZuteilung);
	}

	/**
	 * Löscht die Raumzuweisungen für alle übergebenen IDs von {@link GostSchuelerklausurtermin}en.
	 *
	 * @param schema                  das Datenbankschema
	 * @param request                 die Informationen zur HTTP-Anfrage
	 * @param sktIds			      die Ids der GostSchuelerklausurtermine
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/schuelerklausuren/termine/raumzuweisungen")
	@Operation(summary = "Löscht die Raumzuweisungen für alle übergebenen GostSchuelerklausurtermin-IDs.",
			description = "Löscht die Raumzuweisungen für alle übergebenen GostSchuelerklausurtermin-IDs. "
					+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen einer Raumzuweisung besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Raumzuweisungen wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenPatchResponseData.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Raumzuweisungen zu löschen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response loescheGostSchuelerklausurtermineAusRaum(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Liste von GostSchuelerklausurtermin-IDs.", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> sktIds,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenRaumzuweisungController().loescheRaumzuweisungenFuerSchuelerklausurtermine(sktIds);
	}

	/**
	 * Startet den Kursklausur-Blockungsalgorithmus für die übergebenen {@link GostKlausurterminblockungDaten}.
	 *
	 * @param schema          das Datenbankschema, auf dem die Klausurblockung gestartet wird
	 * @param request         die Informationen zur HTTP-Anfrage
	 * @param blockungDaten   das {@link GostKlausurterminblockungDaten}-Objekt
	 *
	 * @return die HTTP-Antwort mit dem Status und dem {@link GostKlausurenKlausurdaten}-Objekt, das die fertige Klausurblockung enthält
	 */
	@POST
	@Path("/kursklausuren/blocken")
	@Operation(summary = "Startet den Kursklausur-Blockungsalgorithmus für die übergebenen GostKlausurterminblockungDaten.",
			description = "Startet den Kursklausur-Blockungsalgorithmus für die übergebenen GostKlausurterminblockungDaten."
					+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Blocken besitzt.")
	@ApiResponse(responseCode = "200", description = "Klausurblockung wurde erfolgreich durchgeführt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenKlausurdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Klausurblockung durchzuführen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response blockenGostKursklausuren(@PathParam("schema") final String schema,
			@RequestBody(description = "Die GostKlausurterminblockungDaten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = GostKlausurterminblockungDaten.class))) final GostKlausurterminblockungDaten blockungDaten,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenKursklausurWorkflowController().blocken(blockungDaten);
	}

	/**
	 * Startet den Nachschreiber-Blockungsalgorithmus für die übergebene {@link GostNachschreibterminblockungKonfiguration}.
	 *
	 * @param schema     das Datenbankschema, auf dem die Klausurblockung gestartet wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param config     das {@link GostNachschreibterminblockungKonfiguration}-Objekt
	 *
	 * @return die HTTP-Antwort mit dem Status und dem {@link GostKlausurenKlausurdaten}-Objekt, das die fertige Nachschreibblockung enthält
	 */
	@POST
	@Path("/schuelerklausuren/termine/blocken")
	@Operation(summary = "Startet den Nachschreiber-Blockungsalgorithmus für die übergebenen GostNachschreibterminblockungKonfiguration.",
			description = "Startet den Nachschreiber-Blockungsalgorithmus für die übergebenen GostNachschreibterminblockungKonfiguration."
					+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Blocken besitzt.")
	@ApiResponse(responseCode = "200", description = "Nachschreiber-Blockung wurde erfolgreich durchgeführt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenKlausurdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Klausurblockung durchzuführen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response blockenGostSchuelerklausurtermine(@PathParam("schema") final String schema,
			@RequestBody(description = "Die GostNachschreibterminblockungKonfiguration", required = true, content = @Content(
					mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(
							implementation = GostNachschreibterminblockungKonfiguration.class))) final GostNachschreibterminblockungKonfiguration config,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenSchuelerklausurterminWorkflowController().blocken(config);
	}

	/**
	 * Fragt die Klausurdaten eines Schülers ab.
	 *
	 * @param schema       das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idSchueler   die ID des Schülers
	 * @param abiturjahr   das Jahr, in welchem der Jahrgang des Schülers Abitur machen wird
	 * @param halbjahr     das Gost-Halbjahr
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return das {@link GostKlausurenKlausurdaten}-Objekt mit den Klausurdaten des Schülers
	 */
	@GET
	@GZIP
	@Path("/schueler/{schuelerid : -?\\d+}/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}")
	@Operation(summary = "Fragt die Klausurdaten eines Schülers ab.",
			description = "Fragt die Klausurdaten eines Schülers ab."
					+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
	@ApiResponse(responseCode = "200", description = "Abfrage war erfolgreich.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenKlausurdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten auszulesen.")
	@ApiResponse(responseCode = "404", description = "Der Schüler-ID wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getGostKlausurenKlausurdatenBySchuelerId(@PathParam("schema") final String schema, @PathParam("schuelerid") final long idSchueler,
			@PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final int halbjahr, @Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withReadAccess(request)
				.getGostKlausurenKlausurdatenController().getKlausurdatenBySchuelerId(idSchueler, abiturjahr, halbjahr);
	}

	/**
	 * Legt einen neuen {@link GostSchuelerklausurtermin} an. Dies ist meist ein Nachschreibtermin.
	 *
	 * @param schema    das Datenbankschema, in welchem der {@link GostSchuelerklausurtermin} erstellt wird
	 * @param request   die Informationen zur HTTP-Anfrage
	 * @param createRequest JSON-Objekt mit den Daten
	 *
	 * @return die HTTP-Antwort mit dem neuen {@link GostSchuelerklausurtermin}
	 */
	@POST
	@Path("/schuelerklausuren/termine")
	@Operation(summary = "Erstellt einen neuen GostSchuelerklausurtermin.",
			description = "Erstellt einen neuen GostSchuelerklausurtermin."
					+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines GostSchuelerklausurtermins " + "besitzt.")
	@ApiResponse(responseCode = "201", description = "GostSchuelerklausurtermin wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostSchuelerklausurtermin.class)))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen GostSchuelerklausurtermin anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createGostKlausurenSchuelerklausurtermin(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die Create-Daten für den GostSchuelerklausurtermin", required = true, content = @Content(
					mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(
							implementation = GostSchuelerklausurtermin.class))) @Valid final GostKlausurenSchuelerklausurterminCreateRequest createRequest,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenSchuelerklausurterminWorkflowController().create(createRequest);
	}

	/**
	 * Löscht einen {@link GostSchuelerklausurtermin}.
	 *
	 * @param schema     das Datenbankschema, in welchem der {@link GostSchuelerklausurtermin} gelöscht wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param id         die ID des zu löschenden {@link GostSchuelerklausurtermin}s.
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Schuelerklausurtermin
	 */
	@DELETE
	@Path("/schuelerklausuren/termine/{id : \\d+}")
	@Operation(summary = "Löscht einen GostSchuelerklausurtermin.", description = "Löscht einen GostSchuelerklausurtermin."
			+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen eines GostSchuelerklausurtermins " + "besitzt.")
	@ApiResponse(responseCode = "200", description = "GostSchuelerklausurtermin wurde erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostSchuelerklausurtermin.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen GostSchuelerklausurtermin zu löschen.")
	@ApiResponse(responseCode = "404", description = "Der GostSchuelerklausurtermin wurde nicht gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteGostKlausurenSchuelerklausurtermin(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenSchuelerklausurterminController().delete(id);
	}

	/**
	 * Patcht einen {@link GostSchuelerklausurtermin}.
	 *
	 * @param schema     das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param patchRequest JSON-Objekt mit den Patch-Daten und der ID des {@link GostSchuelerklausurtermin}s
	 *
	 * @return die durch den Patch geänderten Raumdaten als {@link GostKlausurenPatchResponseData}-Objekt
	 */
	@PATCH
	@Path("/schuelerklausuren/termine")
	@Operation(summary = "Patcht einen GostSchuelerklausurtermin.", description = "Patcht einen GostSchuelerklausurtermin."
			+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines GostSchuelerklausurtermins besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in den GostSchuelerklausurtermin integriert.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenPatchResponseData.class)))
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um GostSchuelerklausurtermine zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein GostSchuelerklausurtermin-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenSchuelerklausurtermin(@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für den GostSchuelerklausurtermin", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(
									implementation = GostSchuelerklausurtermin.class))) @Valid final GostKlausurenSchuelerklausurterminPatchRequest patchRequest,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenSchuelerklausurterminWorkflowController().patch(patchRequest);
	}

	/**
	 * Patcht mehrere {@link GostSchuelerklausurtermin}e.
	 *
	 * @param schema        das Datenbankschema, auf welches die Patches ausgeführt werden sollen
	 * @param patchRequests JSON-Array mit den Patch-Daten und den IDs der {@link GostSchuelerklausurtermin}e
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die durch die Patches geänderten Raumdaten als {@link GostKlausurenPatchResponseData}-Objekt
	 */
	@PATCH
	@Path("/schuelerklausuren/termine/multiple")
	@Operation(summary = "Patcht mehrere GostSchuelerklausurtermine.", description = "Patcht mehrere GostSchuelerklausurtermine."
			+ " Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen von GostSchuelerklausurterminen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Patches wurden erfolgreich in die GostSchuelerklausurtermine integriert.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostKlausurenPatchResponseData.class)))
	@ApiResponse(responseCode = "400", description = "Die Patches sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um GostSchuelerklausurtermine zu ändern.")
	@ApiResponse(responseCode = "404", description = "Mindestens ein GostSchuelerklausurtermin-Eintrag mit der angegebenen ID wurde nicht gefunden")
	@ApiResponse(responseCode = "409", description = "Ein Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchGostKlausurenSchuelerklausurtermineMultiple(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Patches für die GostSchuelerklausurtermine", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(
									schema = @Schema(implementation = GostSchuelerklausurtermin.class)))) @Valid final List<GostKlausurenSchuelerklausurterminPatchRequest> patchRequests,
			@Context final HttpServletRequest request) {
		return GostKlausurenControllerFactory.withWriteAccess(request)
				.getGostKlausurenSchuelerklausurterminWorkflowController().patchMultiple(patchRequests);
	}

}
