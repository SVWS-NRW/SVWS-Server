package de.svws_nrw.api.server;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.controller.uv.UvExportControllerFactory;
import de.svws_nrw.controller.uv.UvGrunddatenControllerFactory;
import de.svws_nrw.controller.uv.UvPlanungsabschnittControllerFactory;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.lehrer.LehrerUnterrichtsfach;
import de.svws_nrw.core.data.uv.UvFach;
import de.svws_nrw.core.data.uv.UvGrunddatenBundle;
import de.svws_nrw.core.data.uv.UvKlasse;
import de.svws_nrw.core.data.uv.UvKlassenLehrer;
import de.svws_nrw.core.data.uv.UvKurs;
import de.svws_nrw.core.data.uv.UvKursImportDaten;
import de.svws_nrw.core.data.uv.UvLehrer;
import de.svws_nrw.core.data.uv.UvLehrerAnrechnungsstunden;
import de.svws_nrw.core.data.uv.UvLehrerPflichtstundensoll;
import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.core.data.uv.UvLerngruppenLehrer;
import de.svws_nrw.core.data.uv.UvLerngruppenSchiene;
import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrer;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittSchueler;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittZeitraster;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittsdatenBundle;
import de.svws_nrw.core.data.uv.UvRaum;
import de.svws_nrw.core.data.uv.UvRaumgruppe;
import de.svws_nrw.core.data.uv.UvSchiene;
import de.svws_nrw.core.data.uv.UvSchuelerImportOptions;
import de.svws_nrw.core.data.uv.UvSchuelergruppe;
import de.svws_nrw.core.data.uv.UvSchuelergruppeSchueler;
import de.svws_nrw.core.data.uv.UvStundentafel;
import de.svws_nrw.core.data.uv.UvStundentafelFach;
import de.svws_nrw.core.data.uv.UvUnterricht;
import de.svws_nrw.core.data.uv.UvUnterrichtLerngruppenlehrer;
import de.svws_nrw.core.data.uv.UvUnterrichtRaum;
import de.svws_nrw.core.data.uv.UvZeitraster;
import de.svws_nrw.core.data.uv.UvZeitrasterEintrag;
import de.svws_nrw.core.data.uv.export.UVv1Export;
import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppeSchienePK;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittLehrerPK;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittSchuelerPK;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittZeitrasterPK;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeSchuelerPK;
import de.svws_nrw.db.dto.current.uv.DTOUvUnterrichtRaumPK;
import de.svws_nrw.db.dto.current.uv.DTOUvUnterrichteLerngruppenlehrerPK;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.uv.faecher.UvFachCreateRequest;
import de.svws_nrw.service.uv.faecher.UvFachPatchRequest;
import de.svws_nrw.service.uv.klassen.UvKlasseCreateRequest;
import de.svws_nrw.service.uv.klassen.UvKlassePatchRequest;
import de.svws_nrw.service.uv.klassen.UvKlassenLehrerCreateRequest;
import de.svws_nrw.service.uv.klassen.UvKlassenLehrerPatchRequest;
import de.svws_nrw.core.data.uv.UvKursCreateRequest;
import de.svws_nrw.service.uv.kurse.UvKursPatchRequest;
import de.svws_nrw.service.uv.lehrer.UvLehrerAnrechnungsstundenCreateRequest;
import de.svws_nrw.service.uv.lehrer.UvLehrerAnrechnungsstundenPatchRequest;
import de.svws_nrw.service.uv.lehrer.UvLehrerCreateRequest;
import de.svws_nrw.service.uv.lehrer.UvLehrerPatchRequest;
import de.svws_nrw.service.uv.lehrer.UvLehrerPflichtstundensollCreateRequest;
import de.svws_nrw.service.uv.lehrer.UvLehrerPflichtstundensollPatchRequest;
import de.svws_nrw.service.uv.lehrer.UvLehrerUnterrichtsfachCreateRequest;
import de.svws_nrw.service.uv.lehrer.UvLehrerUnterrichtsfachPatchRequest;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrerCreateRequest;
import de.svws_nrw.core.data.uv.UvLerngruppeCreateRequest;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppePatchRequest;
import de.svws_nrw.core.data.uv.UvLerngruppenLehrerCreateRequest;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppenLehrerPatchRequest;
import de.svws_nrw.core.data.uv.UvLerngruppenSchieneCreateRequest;
import de.svws_nrw.service.uv.lerngruppen.UvUnterrichtLerngruppenlehrerCreateRequest;
import de.svws_nrw.service.uv.planungsabschnitte.UvPlanungsabschnittCreateRequest;
import de.svws_nrw.service.uv.planungsabschnitte.UvPlanungsabschnittPatchRequest;
import de.svws_nrw.service.uv.raeume.UvRaumCreateRequest;
import de.svws_nrw.service.uv.raeume.UvRaumPatchRequest;
import de.svws_nrw.service.uv.raeume.UvRaumgruppeCreateRequest;
import de.svws_nrw.service.uv.raeume.UvRaumgruppePatchRequest;
import de.svws_nrw.core.data.uv.UvSchieneCreateRequest;
import de.svws_nrw.service.uv.schienen.UvSchienePatchRequest;
import de.svws_nrw.service.uv.schueler.UvPlanungsabschnittSchuelerCreateRequest;
import de.svws_nrw.service.uv.schueler.UvPlanungsabschnittSchuelerPatchRequest;
import de.svws_nrw.core.data.uv.UvSchuelergruppeCreateRequest;
import de.svws_nrw.service.uv.schueler.UvSchuelergruppePatchRequest;
import de.svws_nrw.core.data.uv.UvSchuelergruppeSchuelerCreateRequest;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelCreateRequest;
import de.svws_nrw.core.data.uv.UvStundentafelImportOptions;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelFachCreateRequest;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelFachPatchRequest;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelPatchRequest;
import de.svws_nrw.service.uv.unterrichte.UvUnterrichtCreateRequest;
import de.svws_nrw.service.uv.unterrichte.UvUnterrichtPatchRequest;
import de.svws_nrw.service.uv.unterrichte.UvUnterrichtRaumCreateRequest;
import de.svws_nrw.service.uv.zeitraster.UvPlanungsabschnittZeitrasterCreateRequest;
import de.svws_nrw.service.uv.zeitraster.UvPlanungsabschnittZeitrasterPatchRequest;
import de.svws_nrw.service.uv.zeitraster.UvZeitrasterCreateRequest;
import de.svws_nrw.service.uv.zeitraster.UvZeitrasterEintragCreateRequest;
import de.svws_nrw.service.uv.zeitraster.UvZeitrasterEintragPatchRequest;
import de.svws_nrw.service.uv.zeitraster.UvZeitrasterPatchRequest;
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

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die
 * Unterrichtsverteilung aus der SVWS-Datenbank. Ein Zugriff erfolgt
 * über den Pfad https://{Hostname}/db/{schema}/unterrichtsverteilung/ ...
 */
@Path("/db/{schema}/unterrichtsverteilung")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
@SuppressWarnings({ "unused" })
public class APIUnterrichtsverteilung {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIUnterrichtsverteilung() {
		// leer
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste aller UV-Planungsabschnitte eines Schuljahres.
	 *
	 * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id   das Schuljahr, für welches die Planungsabschnitte abgefragt werden sollen
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den Planungsabschnitten
	 */
	@GET
	@Path("/collections/planungsabschnitte/{id: \\d+}")
	@Operation(summary = "Gibt eine sortierte Übersicht der Stundenpläne des angegebenen Schuljahresabschnitts zurück.",
			description = "Erstellt eine Liste der Stundenpläne des angegebenen Schuljahresabschnitts. Die Stundenpläne sind anhand der Gültigkeit sortiert."
					+ "Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplanlisten besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste der Stundenpläne",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = UvPlanungsabschnittsdatenBundle.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Stundenplanlisten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Stundenpläne gefunden")
	public Response getUvPlanungsabschnittsdaten(@PathParam("schema") final String schema, @PathParam("id") final int id,
	@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withReadAccess(request).getUvPlanungsabschnittsdatenBundleController().getPlanungsabschnittsdatenBundle(id);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste aller UV-Planungsabschnitte eines Schuljahres.
	 *
	 * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den Planungsabschnitten
	 */
	@GET
	@Path("/collections/grunddaten")
	@Operation(summary = "Gibt eine sortierte Übersicht der Stundenpläne des angegebenen Schuljahresabschnitts zurück.",
			description = "Erstellt eine Liste der Stundenpläne des angegebenen Schuljahresabschnitts. Die Stundenpläne sind anhand der Gültigkeit sortiert."
					+ "Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplanlisten besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste der Stundenpläne",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = UvGrunddatenBundle.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Stundenplanlisten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Stundenpläne gefunden")
	public Response getUvGrunddaten(@PathParam("schema") final String schema,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvGrunddatenControllerFactory.withReadAccess(request).getUvGrunddatenBundleController().getGrunddatenBundle();
	}

	/**
	 * Die OpenAPI-Methode für den Export der Unterrichtsverteilung in einem versionierten JSON-Format.
	 *
	 * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return der Export der Unterrichtsverteilung
	 */
	@GET
	@Path("/v1/export")
	@Operation(summary = "Gibt den Export der Unterrichtsverteilung im Format v1 zurück.",
			description = "Liefert die Unterrichtsverteilung als versionierten JSON-Export im Format v1 zurück."
					+ "Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Unterrichtsverteilung besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Export der Unterrichtsverteilung",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = UVv1Export.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Unterrichtsverteilung anzusehen.")
	public Response getUvExportV1(@PathParam("schema") final String schema,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvExportControllerFactory.withReadAccess(request).getUvExportController().getExportV1();
	}

	// ============================================================
	//   PLANUNGSABSCHNITT (get -> create -> patch -> patchMultiple -> delete -> deleteMultiple)
	// ============================================================

	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste aller UV-Planungsabschnitte eines Schuljahres.
	 *
	 * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param schuljahr   das Schuljahr, für welches die Planungsabschnitte abgefragt werden sollen
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den Planungsabschnitten
	 */
	@GET
	@Path("/planungsabschnitte/schuljahr/{schuljahr : \\d+}")
	@Operation(summary = "Gibt eine sortierte Übersicht der Stundenpläne des angegebenen Schuljahresabschnitts zurück.",
			description = "Erstellt eine Liste der Stundenpläne des angegebenen Schuljahresabschnitts. Die Stundenpläne sind anhand der Gültigkeit sortiert."
					+ "Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplanlisten besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste der Stundenpläne",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UvPlanungsabschnitt.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Stundenplanlisten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Stundenpläne gefunden")
	public Response getUvPlanungsabschnitte(@PathParam("schema") final String schema, @PathParam("schuljahr") final int schuljahr,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withReadAccess(request).getUvPlanungsabschnittController().getListBySchuljahr(schuljahr);
	}

	/**
	 * Erstellt einen neuen {@link UvPlanungsabschnitt} und gibt ihn zurück.
	 *
	 * @param schema     das Datenbankschema, in welchem der {@link UvPlanungsabschnitt} erstellt wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param createRequest         JSON-Objekt mit den Daten
	 *
	 * @return 			 die HTTP-Antwort mit dem neuen {@link UvPlanungsabschnitt}
	 */
	@POST
	@Path("/planungsabschnitte/create")
	@Operation(summary = "Erstellt einen neuen UvPlanungsabschnitt und gibt ihn zurück.",
			description = "Erstellt einen neuen UvPlanungsabschnitt und gibt ihn zurück."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines UvPlanungsabschnitts besitzt.")
	@ApiResponse(responseCode = "201", description = "UvPlanungsabschnitt wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvPlanungsabschnitt.class)))
	@ApiResponse(responseCode = "400", description = "Die Daten sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen GUvPlanungsabschnitt anzulegen.")
	@ApiResponse(responseCode = "409", description = "Der UvPlanungsabschnitt ist schon in der Datenbank enthalten.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createUvPlanungsabschnitt(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Der Post für die UvPlanungsabschnitt-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvPlanungsabschnitt.class))) final @Valid UvPlanungsabschnittCreateRequest createRequest,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvPlanungsabschnittController().create(createRequest);
	}

	/**
	 * Patcht einen bestehenden {@link UvPlanungsabschnitt} anhand der ID.
	 *
	 * @param schema     das Datenbankschema
	 * @param patch         JSON-Objekt mit den Patch-Daten
	 * @param request    die HTTP-Anfrage
	 * @return           die HTTP-Antwort
	 */
	@PATCH
	@Path("/planungsabschnitte")
	@Operation(summary = "Patcht einen bestehenden UvPlanungsabschnitt.", description = "Patcht einen bestehenden UvPlanungsabschnitt.")
	@ApiResponse(responseCode = "200", description = "UvPlanungsabschnitt wurde erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvPlanungsabschnitt.class)))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvPlanungsabschnitt(@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für den UvPlanungsabschnitt", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvPlanungsabschnitt.class))) final @Valid UvPlanungsabschnittPatchRequest patch,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvPlanungsabschnittController().patch(patch);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen mehrerer {@link UvPlanungsabschnitt}-Einträge. Die IDs in dem Patch
	 * müssen vorhanden sein, damit die zu patchenden Daten in der DB gefunden werden können.
	 *
	 * @param schema     das Datenbankschema
	 * @param patches         JSON-Objekt mit den Patch-Daten
	 * @param request    die HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/planungsabschnitte/patch/multiple")
	@Operation(summary = "Patcht mehrere bestehende UvPlanungsabschnitt-Einträge.",
			description = "Patcht mehrere bestehende UvPlanungsabschnitt-Einträge. Die IDs müssen vorhanden sein, damit die entsprechenden Datensätze"
					+ " gefunden werden können.")
	@ApiResponse(responseCode = "200", description = "UvPlanungsabschnitt-Einträge wurden erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = UvPlanungsabschnitt.class))))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvPlanungsabschnitte(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für mehrere UvPlanungsabschnitt-Einträge", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(
									implementation = UvPlanungsabschnitt.class)))) final @Valid Collection<UvPlanungsabschnittPatchRequest> patches,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvPlanungsabschnittController().patchMultiple(patches);
	}

	/**
	 * Löscht einen bestehenden {@link UvPlanungsabschnitt} anhand der ID.
	 *
	 * @param schema     das Datenbankschema
	 * @param id         die ID des Planungsabschnitts
	 * @param request    die HTTP-Anfrage
	 * @return           die HTTP-Antwort
	 */
	@DELETE
	@Path("/planungsabschnitte/{id : \\d+}")
	@Operation(summary = "Löscht einen bestehenden UvPlanungsabschnitt.", description = "Löscht einen bestehenden UvPlanungsabschnitt anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvPlanungsabschnitt wurde erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvPlanungsabschnitt.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvPlanungsabschnitt(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvPlanungsabschnittController().delete(id);
	}

	/**
	 * Löscht mehrere bestehende {@link UvPlanungsabschnitt}.
	 *
	 * @param schema     das Datenbankschema
	 * @param ids        der InputStream, mit der Liste von zu löschenden IDs
	 * @param request    die HTTP-Anfrage
	 * @return           die HTTP-Antwort
	 */
	@DELETE
	@Path("/planungsabschnitte/delete/multiple-listsor")
	@Operation(summary = "Löscht mehrere bestehende UvPlanungsabschnitte.", description = "Löscht mehrere bestehende UvPlanungsabschnitte anhand ihrer IDs.")
	@ApiResponse(responseCode = "200", description = "UvPlanungsabschnitte wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um UvPlanungsabschnitte zu löschen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff).")
	public Response deleteUvPlanungsabschnitteAsListSimpleOperationResponse(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden UvPlanungsabschnitte", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> ids,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvPlanungsabschnittController()
				.deleteMultipleAsListSimpleOperationResponse(ids);
	}

	// ============================================================
	//   SCHÜLERGRUPPE (create -> patch -> patchMultiple -> delete -> deleteMultiple)
	// ============================================================

	/**
	 * Erstellt eine neue {@link UvSchuelergruppe} und gibt sie zurück.
	 *
	 * @param schema         das Datenbankschema, in welchem die {@link UvSchuelergruppe} erstellt wird
	 * @param request        die Informationen zur HTTP-Anfrage
	 * @param createRequest  die Daten für die neue {@link UvSchuelergruppe}
	 * @return               die HTTP-Antwort mit der neuen {@link UvSchuelergruppe}
	 */
	@POST
	@Path("/schueler/gruppen/create")
	@Operation(summary = "Erstellt eine neue UvSchuelergruppe und gibt sie zurück.",
			description = "Erstellt eine neue UvSchuelergruppe und gibt sie zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen besitzt.")
	@ApiResponse(responseCode = "201", description = "UvSchuelergruppe wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvSchuelergruppe.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine UvSchuelergruppe anzulegen.")
	@ApiResponse(responseCode = "409", description = "Die UvSchuelergruppe ist schon in der Datenbank enthalten.")
	public Response createUvSchuelergruppe(@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvSchuelergruppe-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvSchuelergruppe.class))) final @Valid UvSchuelergruppeCreateRequest createRequest)
			throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvSchuelergruppeController().create(createRequest);
	}

	/**
	 * Patcht eine bestehende {@link UvSchuelergruppe}.
	 *
	 * @param schema   das Datenbankschema
	 * @param patch    die Patch-Daten
	 * @param request  die HTTP-Anfrage
	 * @return         die HTTP-Antwort
	 */
	@PATCH
	@Path("/schueler/gruppen")
	@Operation(summary = "Patcht eine bestehende UvSchuelergruppe.", description = "Patcht eine bestehende UvSchuelergruppe.")
	@ApiResponse(responseCode = "200", description = "Patch erfolgreich.")
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvSchuelergruppe(@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für die UvSchuelergruppe", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvSchuelergruppe.class))) final @Valid UvSchuelergruppePatchRequest patch,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvSchuelergruppeController().patch(patch);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen mehrerer {@link UvSchuelergruppe}-Einträge. Die IDs in dem Patch
	 * müssen vorhanden sein, damit die zu patchenden Daten in der DB gefunden werden können.
	 *
	 * @param schema   das Datenbankschema
	 * @param patches  die Patch-Daten
	 * @param request  die HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/schueler/gruppen/patch/multiple")
	@Operation(summary = "Patcht einen bestehenden UvSchuelergruppe.", description = "Patcht einen bestehenden UvSchuelergruppe.")
	@ApiResponse(responseCode = "200", description = "Patch erfolgreich.")
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvSchuelergruppen(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für die UvSchuelergruppen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = UvSchuelergruppe.class)))) final @Valid Collection<UvSchuelergruppePatchRequest> patches,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvSchuelergruppeController().patchMultiple(patches);
	}

	/**
	 * Löscht eine bestehende {@link UvSchuelergruppe} anhand der ID.
	 *
	 * @param schema     das Datenbankschema
	 * @param id         die ID der Schülergruppe
	 * @param request    die HTTP-Anfrage
	 * @return           die HTTP-Antwort
	 */
	@DELETE
	@Path("/schueler/gruppen/{id : \\d+}")
	@Operation(summary = "Löscht eine bestehende UvSchuelergruppe.", description = "Löscht eine bestehende UvSchuelergruppe.")
	@ApiResponse(responseCode = "200", description = "Löschung erfolgreich.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvSchuelergruppe(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvSchuelergruppeController().delete(id);
	}

	/**
	 * Löscht mehrere bestehende {@link UvSchuelergruppe}.
	 *
	 * @param schema     das Datenbankschema
	 * @param ids        die IDs der zu löschenden UvSchuelergruppen
	 * @param request    die HTTP-Anfrage
	 * @return           die HTTP-Antwort
	 */
	@DELETE
	@Path("/schueler/gruppen/multiple")
	@Operation(summary = "Löscht mehrere bestehende UvSchuelergruppen.", description = "Löscht mehrere bestehende UvSchuelergruppen anhand ihrer IDs.")
	@ApiResponse(responseCode = "200", description = "Die UvSchuelergruppen für die angegebenen IDs wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um UvSchuelergruppen zu löschen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteUvSchuelergruppen(@PathParam("schema") final String schema,
			@RequestBody(description = "die IDs der UvSchuelergruppen", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> ids,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvSchuelergruppeController().deleteMultiple(ids);
	}

	// ============================================================
	//   SCHUELERGRUPPEN-SCHUELER (create -> createMultiple -> delete -> deleteMultiple)
	// ============================================================

	/**
	 * Erstellt eine neue Schüler-Schülergruppen-Zuordnung.
	 *
	 * @param schema         das Datenbankschema
	 * @param request        die HTTP-Anfrage
	 * @param createRequest  die Zuordnungsdaten
	 * @return               die HTTP-Antwort mit der neuen Zuordnung
	 */
	@POST
	@Path("/schueler/gruppen/schueler/create")
	@Operation(summary = "Erstellt eine neue Schüler-Schülergruppen-Zuordnung.",
			description = "Erstellt eine neue Schüler-Schülergruppen-Zuordnung. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "Zuordnung wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvSchuelergruppeSchueler.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "409", description = "Die Zuordnung ist bereits vorhanden.")
	public Response createUvSchuelergruppeSchueler(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Die Zuordnungsdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(
									implementation = UvSchuelergruppeSchueler.class))) final @Valid UvSchuelergruppeSchuelerCreateRequest createRequest)
			throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvSchuelergruppeSchuelerController().create(createRequest);
	}

	/**
	 * Erstellt mehrere neue Schüler-Schülergruppen-Zuordnungen.
	 *
	 * @param schema          das Datenbankschema
	 * @param request         die HTTP-Anfrage
	 * @param createRequests  die Liste der Zuordnungsdaten
	 * @return                die HTTP-Antwort mit den neuen Zuordnungen
	 */
	@POST
	@Path("/schueler/gruppen/schueler/create/multiple")
	@Operation(summary = "Erstellt mehrere neue Schüler-Schülergruppen-Zuordnungen.",
			description = "Erstellt mehrere neue Schüler-Schülergruppen-Zuordnungen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "Zuordnungen wurden erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvSchuelergruppeSchueler.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	public Response createUvSchuelergruppeSchuelerMultiple(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Die Liste der Zuordnungsdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(
									implementation = UvSchuelergruppeSchueler.class)))) final @Valid Collection<UvSchuelergruppeSchuelerCreateRequest> createRequests)
			throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvSchuelergruppeSchuelerController().createMultiple(createRequests);
	}

	/**
	 * Löscht eine bestehende Schüler-Schülergruppen-Zuordnung.
	 *
	 * @param schema             das Datenbankschema
	 * @param idSchuelergruppe   die ID der Schülergruppe
	 * @param idSchueler         die ID des Schülers
	 * @param request            die HTTP-Anfrage
	 * @return                   die HTTP-Antwort
	 */
	@DELETE
	@Path("/schueler/gruppen/{idSchuelergruppe : \\d+}/schueler/{idSchueler : \\d+}")
	@Operation(summary = "Löscht eine bestehende Schüler-Schülergruppen-Zuordnung.",
			description = "Löscht eine bestehende Schüler-Schülergruppen-Zuordnung.")
	@ApiResponse(responseCode = "200", description = "Löschen erfolgreich.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvSchuelergruppeSchueler(
			@PathParam("schema") final String schema,
			@PathParam("idSchuelergruppe") final long idSchuelergruppe,
			@PathParam("idSchueler") final long idSchueler,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvSchuelergruppeSchuelerController()
				.delete(new DTOUvSchuelergruppeSchuelerPK(idSchuelergruppe, idSchueler));
	}

	/**
	 * Löscht mehrere bestehende Schüler-Schülergruppen-Zuordnungen einer Schülergruppe.
	 *
	 * @param schema             das Datenbankschema
	 * @param idSchuelergruppe   die ID der Schülergruppe
	 * @param ids                die Liste der Schüler-IDs
	 * @param request            die HTTP-Anfrage
	 * @return                   die HTTP-Antwort
	 */
	@DELETE
	@Path("/schueler/gruppen/{idSchuelergruppe : \\d+}/schueler/delete/multiple")
	@Operation(summary = "Löscht mehrere bestehende Schüler-Schülergruppen-Zuordnungen.",
			description = "Löscht mehrere bestehende Schüler-Schülergruppen-Zuordnungen.")
	@ApiResponse(responseCode = "200", description = "Löschen erfolgreich.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	public Response deleteUvSchuelergruppeSchuelerMultiple(
			@PathParam("schema") final String schema,
			@PathParam("idSchuelergruppe") final long idSchuelergruppe,
			@RequestBody(description = "Die Liste der zu löschenden Schüler-IDs", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> ids,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvSchuelergruppeSchuelerController()
				.deleteMultiple(ids.stream().map(id -> new DTOUvSchuelergruppeSchuelerPK(idSchuelergruppe, id)).toList());
	}

	// ============================================================
	//   KLASSEN (create -> patch -> patchMultiple -> delete -> deleteMultiple)
	// ============================================================

	/**
	 * Erstellt eine neue {@link UvKlasse} und gibt sie zurück.
	 *
	 * @param schema         das Datenbankschema, in welchem die {@link UvKlasse} erstellt wird
	 * @param request        die Informationen zur HTTP-Anfrage
	 * @param createRequest  JSON-Objekt mit den Daten
	 * @return               die HTTP-Antwort mit der neuen {@link UvKlasse}
	 */
	@POST
	@Path("/klassen/create")
	@Operation(summary = "Erstellt eine neue UvKlasse und gibt sie zurück.",
			description = "Erstellt eine neue UvKlasse und gibt sie zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvKlasse wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvKlasse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine UvKlasse anzulegen.")
	@ApiResponse(responseCode = "409", description = "Die UvKlasse ist schon in der Datenbank enthalten.")
	public Response createUvKlasse(@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvKlasse-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvKlasse.class))) final @Valid UvKlasseCreateRequest createRequest)
			throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvKlasseController().create(createRequest);
	}

	/**
	 * Patcht eine bestehende {@link UvKlasse}.
	 *
	 * @param schema   das Datenbankschema
	 * @param patch    JSON-Objekt mit den Patch-Daten
	 * @param request  die HTTP-Anfrage
	 * @return         die HTTP-Antwort
	 */
	@PATCH
	@Path("/klassen")
	@Operation(summary = "Patcht eine bestehende UvKlasse.", description = "Patcht eine bestehende UvKlasse.")
	@ApiResponse(responseCode = "200", description = "Patch erfolgreich.")
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvKlasse(@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für die UvKlasse", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvKlasse.class))) final @Valid UvKlassePatchRequest patch,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvKlasseController().patch(patch);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen mehrerer {@link UvKlasse}-Einträge. Die IDs in dem Patch
	 * müssen vorhanden sein, damit die zu patchenden Daten in der DB gefunden werden können.
	 *
	 * @param schema     das Datenbankschema
	 * @param patches    JSON-Objekt mit den Patch-Daten
	 * @param request    die HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/klassen/patch/multiple")
	@Operation(summary = "Patcht mehrere bestehende UvKlassen.", description = "Patcht mehrere bestehende UvKlassen anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "Patch erfolgreich.")
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvKlassen(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für die UvKlassen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = UvKlasse.class)))) final @Valid Collection<UvKlassePatchRequest> patches,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvKlasseController().patchMultiple(patches);
	}

	/**
	 * Löscht eine bestehende {@link UvKlasse} anhand der ID.
	 *
	 * @param schema     das Datenbankschema
	 * @param id         die ID der Klasse
	 * @param request    die HTTP-Anfrage
	 * @return           die HTTP-Antwort
	 */
	@DELETE
	@Path("/klassen/{id : \\d+}")
	@Operation(summary = "Löscht eine bestehende UvKlasse.", description = "Löscht eine bestehende UvKlasse anhand der ID.")
	@ApiResponse(responseCode = "200", description = "Löschung erfolgreich.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvKlasse(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvKlasseController().delete(id);
	}

	/**
	 * Löscht mehrere bestehende {@link UvKlasse}.
	 *
	 * @param schema     das Datenbankschema
	 * @param ids        die IDs der zu löschenden UvKlassen
	 * @param request    die HTTP-Anfrage
	 * @return           die HTTP-Antwort
	 */
	@DELETE
	@Path("/klassen/multiple")
	@Operation(summary = "Löscht mehrere bestehende UvKlassen.", description = "Löscht mehrere bestehende UvKlassen anhand ihrer IDs.")
	@ApiResponse(responseCode = "200", description = "Die UvKlassen für die angegebenen IDs wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um UvKlassen zu löschen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteUvKlassen(@PathParam("schema") final String schema,
			@RequestBody(description = "die IDs der UvKlassen", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> ids,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvKlasseController().deleteMultiple(ids);
	}

	// ============================================================
	//   KLASSENLEHRER (create -> patch -> delete -> deleteMultiple)
	// ============================================================

	/**
	 * Erstellt eine neue {@link UvKlassenLehrer}-Zuordnung und gibt sie zurück.
	 *
	 * @param schema   das Datenbankschema, in welchem die {@link UvKlassenLehrer}-Zuordnung erstellt wird
	 * @param request  die Informationen zur HTTP-Anfrage
	 * @param createRequest       JSON-Objekt mit den Daten
	 *
	 * @return die HTTP-Antwort mit der neuen {@link UvKlassenLehrer}-Zuordnung
	 */
	@POST
	@Path("/klassen/lehrer/create")
	@Operation(summary = "Erstellt eine neue UvKlassenLehrer-Zuordnung und gibt sie zurück.",
			description = "Erstellt eine neue UvKlassenLehrer-Zuordnung und gibt sie zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvKlassenLehrer-Zuordnung wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvKlassenLehrer.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte, um eine Zuordnung anzulegen.")
	@ApiResponse(responseCode = "409", description = "Die Zuordnung ist bereits vorhanden.")
	public Response createUvKlassenLehrer(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvKlassenLehrer-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvKlassenLehrer.class))) final @Valid UvKlassenLehrerCreateRequest createRequest)
			throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvKlassenLehrerController().create(createRequest);
	}


	/**
	 * Patcht eine bestehende {@link UvKlassenLehrer}-Zuordnung anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param patch       JSON-Objekt mit den Patch-Daten
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@PATCH
	@Path("/klassen/lehrer")
	@Operation(summary = "Patcht eine bestehende UvKlassenLehrer-Zuordnung.",
			description = "Patcht eine bestehende UvKlassenLehrer-Zuordnung anhand der ID.")
	@ApiResponse(responseCode = "200", description = "Patch erfolgreich.")
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvKlassenLehrer(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für die UvKlassenLehrer-Zuordnung", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvKlassenLehrer.class))) final @Valid UvKlassenLehrerPatchRequest patch,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvKlassenLehrerController().patch(patch);
	}


	/**
	 * Löscht eine bestehende {@link UvKlassenLehrer}-Zuordnung anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param id       die ID der Zuordnung
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/klassen/lehrer/{id : \\d+}")
	@Operation(summary = "Löscht eine bestehende UvKlassenLehrer-Zuordnung.",
			description = "Löscht eine bestehende UvKlassenLehrer-Zuordnung anhand der ID.")
	@ApiResponse(responseCode = "204", description = "Löschung erfolgreich.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvKlassenLehrer(
			@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvKlassenLehrerController().delete(id);
	}


	/**
	 * Löscht mehrere bestehende {@link UvKlassenLehrer}-Zuordnungen anhand ihrer IDs.
	 *
	 * @param schema   das Datenbankschema
	 * @param ids      die IDs der zu löschenden Zuordnungen
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/klassen/lehrer/delete/multiple")
	@Operation(summary = "Löscht mehrere bestehende UvKlassenLehrer-Zuordnungen.",
			description = "Löscht mehrere bestehende UvKlassenLehrer-Zuordnungen anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "Die Zuordnungen wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff).")
	public Response deleteUvKlassenLehrerMultiple(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden UvKlassenLehrer-Zuordnungen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final java.util.List<Long> ids,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvKlassenLehrerController().deleteMultiple(ids);
	}

	// ============================================================
	//   KURSE (create -> patch -> patchMultiple -> delete -> deleteMultiple)
	// ============================================================

	/**
	 * Erstellt einen neuen {@link UvKurs} und gibt ihn zurück.
	 *
	 * @param schema         das Datenbankschema, in welchem der {@link UvKurs} erstellt wird
	 * @param request        die Informationen zur HTTP-Anfrage
	 * @param createRequest  die Daten für den neuen {@link UvKurs}
	 * @return           die HTTP-Antwort mit dem neuen {@link UvKurs}
	 */
	@POST
	@Path("/kurse/create")
	@Operation(summary = "Erstellt einen neuen UvKurs und gibt ihn zurück.",
			description = "Erstellt einen neuen UvKurs und gibt ihn zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvKurs wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvKurs.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen UvKurs anzulegen.")
	@ApiResponse(responseCode = "409", description = "Der UvKurs ist schon in der Datenbank enthalten.")
	public Response createUvKurs(@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvKurs-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvKurs.class))) final @Valid UvKursCreateRequest createRequest)
			throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvKursController().create(createRequest);
	}

	/**
	 * Patcht einen bestehenden {@link UvKurs} anhand der ID.
	 *
	 * @param schema     das Datenbankschema
	 * @param patch      die Patch-Daten für den UvKurs
	 * @param request    die HTTP-Anfrage
	 * @return           die HTTP-Antwort mit dem gepatchten {@link UvKurs}
	 */
	@PATCH
	@Path("/kurse")
	@Operation(summary = "Patcht einen bestehenden UvKurs.", description = "Patcht einen bestehenden UvKurs anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvKurs wurde erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvKurs.class)))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvKurs(@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für den UvKurs", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvKurs.class))) final @Valid UvKursPatchRequest patch,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvKursController().patch(patch);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen mehrerer {@link UvKurs}-Einträge. Die IDs in dem Patch
	 * müssen vorhanden sein, damit die zu patchenden Daten in der DB gefunden werden können.
	 *
	 * @param schema     das Datenbankschema
	 * @param patches    die Patch-Daten für die UvKurse
	 * @param request    die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den gepatchten {@link UvKurs}-Einträgen
	 */
	@PATCH
	@Path("/kurse/patch/multiple")
	@Operation(summary = "Patcht mehrere bestehende UvKurse.", description = "Patcht mehrere bestehende UvKurse anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "UvKurse wurden erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvKurs.class))))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvKurse(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für die UvKurse", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = UvKurs.class)))) final @Valid java.util.List<UvKursPatchRequest> patches,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvKursController().patchMultiple(patches);
	}

	/**
	 * Löscht einen bestehenden {@link UvKurs} anhand der ID.
	 *
	 * @param schema     das Datenbankschema
	 * @param id         die ID des Kurses
	 * @param request    die HTTP-Anfrage
	 * @return           die HTTP-Antwort mit dem gelöschten {@link UvKurs}
	 */
	@DELETE
	@Path("/kurse/{id : \\d+}")
	@Operation(summary = "Löscht einen bestehenden UvKurs.", description = "Löscht einen bestehenden UvKurs anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvKurs wurde erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvKurs.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvKurs(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvKursController().delete(id);
	}

	/**
	 * Löscht mehrere bestehende {@link UvKurs}-Einträge anhand der IDs.
	 *
	 * @param schema     das Datenbankschema
	 * @param kursIds    die IDs der zu löschenden UV-Kurse
	 * @param request    die HTTP-Anfrage
	 * @return           die HTTP-Antwort mit den gelöschten {@link UvKurs}-Einträgen
	 */
	@DELETE
	@Path("/kurse/multiple")
	@Operation(summary = "Löscht mehrere bestehende UV-Kurse.", description = "Löscht mehrere bestehende UV-Kurse anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvKurse wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvKurs.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen UV-Kurs zu löschen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteUvKurse(@PathParam("schema") final String schema,
			@RequestBody(description = "die IDs der UV-Kurse", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> kursIds,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvKursController().deleteMultiple(kursIds);
	}

	/**
	 * Persistiert die vom Client bestätigten Kursimportdaten atomar.
	 *
	 * @param schema das Datenbankschema
	 * @param idPlanungsabschnitt die ID des Zielplanungsabschnitts
	 * @param daten die anzulegenden Importdaten
	 * @param request die HTTP-Anfrage
	 * @return die angelegten UV-Daten
	 */
	@POST
	@Path("/planungsabschnitt/{idPlanungsabschnitt : \\d+}/kurse/import")
	@Operation(summary = "Importiert vom Client bestätigte Kurse in den UV-Planungsabschnitt.",
			description = "Persistiert die vom Client erzeugten und bestätigten Kursimportdaten atomar in einem UV-Planungsabschnitt.")
	@ApiResponse(responseCode = "200", description = "Die UV-Daten wurden erfolgreich importiert.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvPlanungsabschnittsdatenBundle.class)))
	public Response importUvKurse(
			@PathParam("schema") final String schema,
			@PathParam("idPlanungsabschnitt") final long idPlanungsabschnitt,
			@RequestBody(description = "Die bestätigten Kursimportdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvKursImportDaten.class)))
			final @Valid UvKursImportDaten daten,
			@Context final HttpServletRequest request) {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvKursImportController()
				.importDaten(idPlanungsabschnitt, daten);
	}

	// ============================================================
	//   STUNDENTAFEL (get -> create -> patch -> patchMultiple -> delete -> deleteMultiple)
	// ============================================================

	/**
	 * Erstellt eine neue {@link UvStundentafel} und gibt sie zurück.
	 *
	 * @param schema         das Datenbankschema, in welchem die {@link UvStundentafel} erstellt wird
	 * @param request        die Informationen zur HTTP-Anfrage
	 * @param createRequest  JSON-Objekt mit den Daten der neuen UvStundentafel
	 *
	 * @return die HTTP-Antwort mit der neuen {@link UvStundentafel}
	 */
	@POST
	@Path("/stundentafeln/create")
	@Operation(summary = "Erstellt eine neue UvStundentafel und gibt sie zurück.",
			description = "Erstellt eine neue UvStundentafel und gibt sie zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen besitzt.")
	@ApiResponse(responseCode = "201", description = "UvStundentafel wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvStundentafel.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine UvStundentafel anzulegen.")
	@ApiResponse(responseCode = "409", description = "Die UvStundentafel ist schon in der Datenbank enthalten.")
	public Response createUvStundentafel(@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvStundentafel-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvStundentafel.class))) final @Valid UvStundentafelCreateRequest createRequest) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvStundentafelController().create(createRequest);
	}

	/**
	 * Erstellt eine neue {@link UvStundentafel} einschließlich ihrer Fächer aus den Leistungsdaten
	 * der Schülerinnen und Schüler einer Klasse in beiden Abschnitten eines Schuljahres.
	 *
	 * @param schema   das Datenbankschema, in welchem die {@link UvStundentafel} erstellt wird
	 * @param importRequest  die Importdaten für die neue Stundentafel
	 * @param request  die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der importierten Stundentafel, den angelegten Stundentafel-Fächern
	 *         und gegebenenfalls zusätzlich angelegten UV-Fächern
	 */
	@POST
	@Path("/stundentafeln/import")
	@Operation(summary = "Importiert eine UvStundentafel aus Schüler-Leistungsdaten.",
			description = "Erstellt eine UvStundentafel und deren Fächer aus den Leistungsdaten der Schülerinnen und Schüler einer Klasse "
					+ "in beiden Abschnitten des angegebenen Schuljahres. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvStundentafel wurde erfolgreich importiert.", content = @Content(mediaType = MediaType.APPLICATION_JSON,
			schema = @Schema(implementation = UvGrunddatenBundle.class)))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Importdaten oder keine passenden Klassen- beziehungsweise Schülerdaten vorhanden.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine UvStundentafel anzulegen.")
	@ApiResponse(responseCode = "404", description = "Die angegebene Klasse oder ein Schuljahresabschnitt wurde nicht gefunden.")
	public Response importUvStundentafel(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Importdaten für die UvStundentafel", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvStundentafelImportOptions.class))) final @Valid UvStundentafelImportOptions importRequest,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvStundentafelController().importiere(importRequest);
	}

	/**
	 * Patcht eine bestehende {@link UvStundentafel}. Die ID wird aus dem Patch-Objekt gelesen.
	 *
	 * @param schema   das Datenbankschema
	 * @param patch    die Patch-Daten für die UvStundentafel (inklusive ID)
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@PATCH
	@Path("/stundentafeln")
	@Operation(summary = "Patcht eine bestehende UvStundentafel.", description = "Patcht eine bestehende UvStundentafel.")
	@ApiResponse(responseCode = "200", description = "UvStundentafel wurde erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvStundentafel.class)))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvStundentafel(@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für die UvStundentafel", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvStundentafel.class))) final @Valid UvStundentafelPatchRequest patch,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvStundentafelController().patch(patch);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen mehrerer {@link UvStundentafel}-Einträge.
	 * Die IDs in dem Patch müssen vorhanden sein, damit die zu patchenden Daten gefunden werden können.
	 *
	 * @param schema   das Datenbankschema
	 * @param patches  die Patch-Daten für die UvStundentafeln (jeweils inklusive ID)
	 * @param request  die HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/stundentafeln/patch/multiple")
	@Operation(summary = "Patcht mehrere bestehende UvStundentafeln.", description = "Patcht mehrere bestehende UvStundentafeln.")
	@ApiResponse(responseCode = "200", description = "UvStundentafeln wurden erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = UvStundentafel.class))))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvStundentafeln(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für die UvStundentafeln", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(
									schema = @Schema(implementation = UvStundentafel.class)))) final @Valid Collection<UvStundentafelPatchRequest> patches,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvStundentafelController().patchMultiple(patches);
	}

	/**
	 * Löscht eine bestehende {@link UvStundentafel} anhand der ID.
	 *
	 * @param schema     das Datenbankschema
	 * @param id         die ID der Stundentafel
	 * @param request    die HTTP-Anfrage
	 * @return           die HTTP-Antwort
	 */
	@DELETE
	@Path("/stundentafeln/{id : \\d+}")
	@Operation(summary = "Löscht eine bestehende UvStundentafel.", description = "Löscht eine bestehende UvStundentafel.")
	@ApiResponse(responseCode = "200", description = "UvStundentafel wurde erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvStundentafel.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvStundentafel(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvStundentafelController().delete(id);
	}

	/**
	 * Löscht mehrere bestehende {@link UvStundentafel}.
	 *
	 * @param schema     das Datenbankschema
	 * @param ids        die IDs der zu löschenden UvStundentafeln
	 * @param request    die HTTP-Anfrage
	 * @return           die HTTP-Antwort
	 */
	@DELETE
	@Path("/stundentafeln/delete/multiple")
	@Operation(summary = "Löscht mehrere bestehende UvStundentafeln.", description = "Löscht mehrere bestehende UvStundentafeln anhand ihrer IDs.")
	@ApiResponse(responseCode = "200", description = "UvStundentafeln wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = UvStundentafel.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um UvStundentafeln zu löschen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteUvStundentafeln(@PathParam("schema") final String schema,
			@RequestBody(description = "die IDs der UvStundentafeln", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> ids,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvStundentafelController().deleteMultiple(ids);
	}

	/**
	 * Erstellt ein neues {@link UvFach} und gibt es zurück.
	 *
	 * @param schema     das Datenbankschema, in welchem das {@link UvFach} erstellt wird
	 * @param request    die Informationen zur HTTP-Anfrage
	 * @param createRequest       JSON-Objekt mit den Daten
	 *
	 * @return           die HTTP-Antwort mit dem neuen {@link UvFach}
	 */
	@POST
	@Path("/faecher/create")
	@Operation(summary = "Erstellt ein neues UvFach und gibt es zurück.",
			description = "Erstellt ein neues UvFach und gibt es zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvFach wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvFach.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um ein UvFach anzulegen.")
	@ApiResponse(responseCode = "409", description = "Das UvFach ist schon in der Datenbank enthalten.")
	public Response createUvFach(@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvFach-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvFach.class))) final @Valid UvFachCreateRequest createRequest) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvFachController().create(createRequest);
	}

	/**
	 * Erstellt neue {@link UvFach}-Objekte und gibt sie zurück.
	 *
	 * @param schema   das Datenbankschema, in welchem die {@link UvFach}-Objekte erstellt werden
	 * @param request  die Informationen zur HTTP-Anfrage
	 * @param createRequests       JSON-Objekt mit den Daten
	 *
	 * @return die HTTP-Antwort mit den neuen {@link UvFach}-Objekten
	 */
	@POST
	@Path("/faecher/create/multiple")
	@Operation(summary = "Erstellt mehrere neue UvFach-Objekte und gibt sie zurück.",
			description = "Erstellt mehrere neue UvFach-Objekte und gibt sie zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvFach-Objekte wurden erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = UvFach.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um ein UvFach anzulegen.")
	@ApiResponse(responseCode = "409", description = "Das UvFach ist schon in der Datenbank enthalten.")
	public Response createUvFaecher(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvFach-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(
									schema = @Schema(implementation = UvFach.class)))) final @Valid Collection<UvFachCreateRequest> createRequests) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvFachController().createMultiple(createRequests);
	}

	/**
	 * Patcht ein bestehendes {@link UvFach}. Die ID wird aus dem Patch-Objekt gelesen.
	 *
	 * @param schema     das Datenbankschema
	 * @param patch      die Patch-Daten für das UvFach (inklusive ID)
	 * @param request    die HTTP-Anfrage
	 *
	 * @return           die HTTP-Antwort
	 */
	@PATCH
	@Path("/faecher")
	@Operation(summary = "Patcht ein bestehendes UvFach.", description = "Patcht ein bestehendes UvFach.")
	@ApiResponse(responseCode = "200", description = "UvFach wurde erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvFach.class)))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvFach(@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für das UvFach", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvFach.class))) final @Valid UvFachPatchRequest patch,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvFachController().patch(patch);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen mehrerer {@link UvFach}-Einträge.
	 * Die IDs in dem Patch müssen vorhanden sein, damit die zu patchenden Daten in der DB gefunden werden können.
	 *
	 * @param schema     das Datenbankschema
	 * @param request    die HTTP-Anfrage
	 * @param patches       JSON-Objekt mit den Daten
	 *
	 * @return           das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/faecher/patch/multiple")
	@Operation(summary = "Patcht mehrere bestehende UvFach-Einträge.",
			description = "Patcht mehrere bestehende UvFach-Einträge. Die IDs müssen vorhanden sein, damit die entsprechenden Datensätze gefunden werden können.")
	@ApiResponse(responseCode = "200", description = "UvFach-Einträge wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = UvFach.class))))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvFaecher(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für mehrere UvFach-Einträge", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = UvFach.class)))) final @Valid Collection<UvFachPatchRequest> patches,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvFachController().patchMultiple(patches);
	}

	/**
	 * Löscht ein bestehendes {@link UvFach} anhand der ID.
	 *
	 * @param schema     das Datenbankschema
	 * @param id         die ID des Faches
	 * @param request    die HTTP-Anfrage
	 *
	 * @return           die HTTP-Antwort
	 */
	@DELETE
	@Path("/faecher/{id : \\d+}")
	@Operation(summary = "Löscht ein bestehendes UvFach.", description = "Löscht ein bestehendes UvFach anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvFach wurde erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvFach.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvFach(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvFachController().delete(id);
	}

	/**
	 * Löscht mehrere bestehende {@link UvFach}-Einträge anhand ihrer IDs.
	 *
	 * @param schema     das Datenbankschema
	 * @param fachIds    die IDs der zu löschenden UV-Fächer
	 * @param request    die HTTP-Anfrage
	 *
	 * @return           die HTTP-Antwort
	 */
	@DELETE
	@Path("/faecher/delete/multiple")
	@Operation(summary = "Löscht mehrere bestehende UV-Fächer.",
			description = "Löscht mehrere bestehende UV-Fächer anhand ihrer IDs.")
	@ApiResponse(responseCode = "200", description = "UV-Fächer wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = UvFach.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um UV-Fächer zu löschen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff).")
	public Response deleteUvFaecher(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden UV-Fächer", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> fachIds,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvFachController().deleteMultiple(fachIds);
	}

	// ============================================================
	//   STUNDENTAFEL-FACH (get -> create -> patch -> patchMultiple -> delete -> deleteMultiple)
	// ============================================================

	/**
	 * Erstellt ein neues {@link UvStundentafelFach} und gibt es zurück.
	 *
	 * @param schema         das Datenbankschema, in welchem das {@link UvStundentafelFach} erstellt wird
	 * @param request        die Informationen zur HTTP-Anfrage
	 * @param createRequest  JSON-Objekt mit den Daten
	 *
	 * @return die HTTP-Antwort mit dem neuen {@link UvStundentafelFach}
	 */
	@POST
	@Path("/stundentafeln/faecher/create")
	@Operation(summary = "Erstellt ein neues UvStundentafelFach und gibt es zurück.",
			description = "Erstellt ein neues UvStundentafelFach und gibt es zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen besitzt.")
	@ApiResponse(responseCode = "201", description = "UvStundentafelFach wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvStundentafelFach.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um ein UvStundentafelFach anzulegen.")
	@ApiResponse(responseCode = "409", description = "Das UvStundentafelFach ist schon in der Datenbank enthalten.")
	public Response createUvStundentafelFach(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvStundentafelFach-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvStundentafelFach.class))) final @Valid UvStundentafelFachCreateRequest createRequest) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvStundentafelFachController().create(createRequest);
	}


	/**
	 * Patcht ein bestehendes {@link UvStundentafelFach}. Die ID wird aus dem Patch-Objekt gelesen.
	 *
	 * @param schema   das Datenbankschema
	 * @param patch    die Patch-Daten für das UvStundentafelFach (inklusive ID)
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@PATCH
	@Path("/stundentafeln/faecher")
	@Operation(summary = "Patcht ein bestehendes UvStundentafelFach.",
			description = "Patcht ein bestehendes UvStundentafelFach.")
	@ApiResponse(responseCode = "200", description = "UvStundentafelFach wurde erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvStundentafelFach.class)))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvStundentafelFach(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für das UvStundentafelFach", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvStundentafelFach.class))) final @Valid UvStundentafelFachPatchRequest patch,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvStundentafelFachController().patch(patch);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen mehrerer {@link UvStundentafelFach}-Einträge.
	 * Die IDs in dem Patch müssen vorhanden sein, damit die zu patchenden Daten gefunden werden können.
	 *
	 * @param schema   das Datenbankschema
	 * @param patches  die Patch-Daten für die UvStundentafelFach-Einträge (jeweils inklusive ID)
	 * @param request  die HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/stundentafeln/faecher/patch/multiple")
	@Operation(summary = "Patcht mehrere bestehende UvStundentafelFach-Einträge.",
			description = "Patcht mehrere bestehende UvStundentafelFach-Einträge.")
	@ApiResponse(responseCode = "200", description = "UvStundentafelFach wurde erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = UvStundentafelFach.class))))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvStundentafelFaecher(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für die UvStundentafelFach-Einträge", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(
									implementation = UvStundentafelFach.class)))) final @Valid Collection<UvStundentafelFachPatchRequest> patches,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvStundentafelFachController().patchMultiple(patches);
	}


	/**
	 * Löscht ein bestehendes {@link UvStundentafelFach} anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param id       die ID des Stundentafel-Fachs
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/stundentafeln/faecher/{id : \\d+}")
	@Operation(summary = "Löscht ein bestehendes UvStundentafelFach.",
			description = "Löscht ein bestehendes UvStundentafelFach anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvStundentafelFach wurde erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvStundentafelFach.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvStundentafelFach(
			@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvStundentafelFachController().delete(id);
	}


	/**
	 * Löscht mehrere bestehende {@link UvStundentafelFach}-Einträge anhand der IDs.
	 *
	 * @param schema   das Datenbankschema
	 * @param ids      die IDs der zu löschenden Stundentafel-Fächer
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/stundentafeln/faecher/delete/multiple")
	@Operation(summary = "Löscht mehrere bestehende UvStundentafelFach-Einträge.",
			description = "Löscht mehrere bestehende UvStundentafelFach-Einträge anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "UvStundentafelFach-Einträge wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = UvStundentafelFach.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response deleteUvStundentafelFaecher(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden UV-Stundentafel-Fächer", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> ids,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvStundentafelFachController().deleteMultiple(ids);
	}

	// ============================================================
	//   SCHIENE (create -> patch -> patchMultiple -> delete -> deleteMultiple)
	// ============================================================

	/**
	 * Erstellt eine neue {@link UvSchiene} und gibt sie zurück.
	 *
	 * @param schema         das Datenbankschema, in welchem die {@link UvSchiene} erstellt wird
	 * @param request        die Informationen zur HTTP-Anfrage
	 * @param createRequest  die Daten für die neue {@link UvSchiene}
	 *
	 * @return die HTTP-Antwort mit der neuen {@link UvSchiene}
	 */
	@POST
	@Path("/schienen/create")
	@Operation(summary = "Erstellt eine neue UvSchiene und gibt sie zurück.",
			description = "Erstellt eine neue UvSchiene und gibt sie zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvSchiene wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvSchiene.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine UvSchiene anzulegen.")
	@ApiResponse(responseCode = "409", description = "Die UvSchiene ist schon in der Datenbank enthalten.")
	public Response createUvSchiene(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvSchiene-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvSchiene.class))) final @Valid UvSchieneCreateRequest createRequest)
			throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvSchieneController().create(createRequest);
	}


	/**
	 * Patcht eine bestehende {@link UvSchiene} anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param patch    die Patch-Daten für die UvSchiene
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der gepatchten {@link UvSchiene}
	 */
	@PATCH
	@Path("/schienen")
	@Operation(summary = "Patcht eine bestehende UvSchiene.",
			description = "Patcht eine bestehende UvSchiene anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvSchiene wurde erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvSchiene.class)))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvSchiene(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für die UvSchiene", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvSchiene.class))) final @Valid UvSchienePatchRequest patch,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvSchieneController().patch(patch);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen mehrerer {@link UvSchiene}-Einträge.
	 * Die IDs in dem Patch müssen vorhanden sein, damit die zu patchenden Daten gefunden werden können.
	 *
	 * @param schema    das Datenbankschema
	 * @param patches   die Patch-Daten für die UvSchienen
	 * @param request   die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den gepatchten {@link UvSchiene}-Einträgen
	 */
	@PATCH
	@Path("/schienen/patch/multiple")
	@Operation(summary = "Patcht mehrere bestehende UvSchienen.",
			description = "Patcht mehrere bestehende UvSchienen anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "UvSchienen wurden erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvSchiene.class))))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvSchienen(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für die UvSchienen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = UvSchiene.class)))) final @Valid Collection<UvSchienePatchRequest> patches,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvSchieneController().patchMultiple(patches);
	}


	/**
	 * Löscht eine bestehende {@link UvSchiene} anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param id       die ID der Schiene
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der gelöschten {@link UvSchiene}
	 */
	@DELETE
	@Path("/schienen/{id : \\d+}")
	@Operation(summary = "Löscht eine bestehende UvSchiene.",
			description = "Löscht eine bestehende UvSchiene anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvSchiene wurde erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvSchiene.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvSchiene(
			@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvSchieneController().delete(id);
	}


	/**
	 * Löscht mehrere bestehende {@link UvSchiene}-Einträge anhand der IDs.
	 *
	 * @param schema   das Datenbankschema
	 * @param ids      die IDs der zu löschenden Schienen
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den gelöschten {@link UvSchiene}-Einträgen
	 */
	@DELETE
	@Path("/schienen/delete/multiple")
	@Operation(summary = "Löscht mehrere bestehende UvSchienen.",
			description = "Löscht mehrere bestehende UvSchienen anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "UvSchienen wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvSchiene.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response deleteUvSchienen(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Schienen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final Collection<Long> ids,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvSchieneController().deleteMultiple(ids);
	}

	// ============================================================
	//   LERNGRUPPE (create -> patch -> patchMultiple -> delete -> deleteMultiple)
	// ============================================================

	/**
	 * Erstellt eine neue {@link UvLerngruppe} und gibt sie zurück.
	 *
	 * @param schema         das Datenbankschema, in welchem die {@link UvLerngruppe} erstellt wird
	 * @param request        die Informationen zur HTTP-Anfrage
	 * @param createRequest  JSON-Objekt mit den Daten
	 *
	 * @return die HTTP-Antwort mit der neuen {@link UvLerngruppe}
	 */
	@POST
	@Path("/lerngruppen/create")
	@Operation(summary = "Erstellt eine neue UvLerngruppe und gibt sie zurück.",
			description = "Erstellt eine neue UvLerngruppe und gibt sie zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvLerngruppe wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvLerngruppe.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine UvLerngruppe anzulegen.")
	@ApiResponse(responseCode = "409", description = "Die UvLerngruppe ist schon in der Datenbank enthalten.")
	public Response createUvLerngruppe(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvLerngruppe-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvLerngruppe.class))) final @Valid UvLerngruppeCreateRequest createRequest)
			throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvLerngruppeController().create(createRequest);
	}


	/**
	 * Erstellt für die angegebenen Klassen Lerngruppen
	 * anhand der jeweils zugeordneten Stundentafel.
	 *
	 * @param schema      das Datenbankschema
	 * @param idsKlassen  die IDs der Klassen
	 * @param request     die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den neu erstellten Lerngruppen
	 */
	@POST
	@Path("/lerngruppen/create/from/klassen")
	@Operation(summary = "Erstellt Klassen-Lerngruppen aus den Stundentafeln der angegebenen Klassen.",
			description = "Erstellt für die angegebenen UV-Klassen Lerngruppen anhand der jeweils zugeordneten UV-Stundentafel.")
	@ApiResponse(responseCode = "201", description = "UvLerngruppen wurden erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvLerngruppe.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um UvLerngruppen anzulegen.")
	public Response createUvLerngruppenFromKlassen(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der Klassen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final java.util.List<Long> idsKlassen,
			@Context final HttpServletRequest request) {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvLerngruppeKlassenImportController().createByKlassen(idsKlassen);
	}


	/**
	 * Patcht eine bestehende {@link UvLerngruppe}.
	 *
	 * @param schema   das Datenbankschema
	 * @param patch    JSON-Objekt mit den Patch-Daten
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@PATCH
	@Path("/lerngruppen")
	@Operation(summary = "Patcht eine bestehende UvLerngruppe.",
			description = "Patcht eine bestehende UvLerngruppe.")
	@ApiResponse(responseCode = "200", description = "Patch erfolgreich.")
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvLerngruppe(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für die UvLerngruppe", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvLerngruppe.class))) final @Valid UvLerngruppePatchRequest patch,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvLerngruppeController().patch(patch);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen mehrerer {@link UvLerngruppe}-Einträge.
	 * Die IDs in dem Patch müssen vorhanden sein, damit die zu patchenden Daten gefunden werden können.
	 *
	 * @param schema   das Datenbankschema
	 * @param patches  JSON-Objekt mit den Patch-Daten
	 * @param request  die HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/lerngruppen/patch/multiple")
	@Operation(summary = "Patcht mehrere bestehende UvLerngruppen.",
			description = "Patcht mehrere bestehende UvLerngruppen anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "Patch erfolgreich.")
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvLerngruppen(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für die UvLerngruppen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = UvLerngruppe.class)))) final @Valid Collection<UvLerngruppePatchRequest> patches,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvLerngruppeController().patchMultiple(patches);
	}


	/**
	 * Löscht eine bestehende {@link UvLerngruppe} anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param id       die ID der Lerngruppe
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/lerngruppen/{id : \\d+}")
	@Operation(summary = "Löscht eine bestehende UvLerngruppe.",
			description = "Löscht eine bestehende UvLerngruppe anhand der ID.")
	@ApiResponse(responseCode = "200", description = "Löschung erfolgreich.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvLerngruppe(
			@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvLerngruppeController().delete(id);
	}


	/**
	 * Löscht mehrere bestehende {@link UvLerngruppe}-Einträge anhand der IDs.
	 *
	 * @param schema   das Datenbankschema
	 * @param ids      die IDs der zu löschenden Lerngruppen
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/lerngruppen/delete/multiple")
	@Operation(summary = "Löscht mehrere bestehende UvLerngruppen.",
			description = "Löscht mehrere bestehende UvLerngruppen anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "Die UvLerngruppen wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response deleteUvLerngruppen(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Lerngruppen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final java.util.List<Long> ids,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvLerngruppeController().deleteMultiple(ids);
	}

	// ============================================================
	//   RAUMGRUPPE (get -> create -> patch -> patchMultiple -> delete -> deleteMultiple)
	// ============================================================

	/**
	 * Erstellt eine neue {@link UvRaumgruppe} und gibt sie zurück.
	 *
	 * @param schema         das Datenbankschema, in welchem die {@link UvRaumgruppe} erstellt wird
	 * @param request        die Informationen zur HTTP-Anfrage
	 * @param createRequest  JSON-Objekt mit den Daten der neuen UvRaumgruppe
	 *
	 * @return die HTTP-Antwort mit der neuen {@link UvRaumgruppe}
	 */
	@POST
	@Path("/raeume/gruppen/create")
	@Operation(summary = "Erstellt eine neue UvRaumgruppe und gibt sie zurück.",
			description = "Erstellt eine neue UvRaumgruppe und gibt sie zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvRaumgruppe wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvRaumgruppe.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine UvRaumgruppe anzulegen.")
	@ApiResponse(responseCode = "409", description = "Die UvRaumgruppe ist schon in der Datenbank enthalten.")
	public Response createUvRaumgruppe(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvRaumgruppe-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvRaumgruppe.class))) final @Valid UvRaumgruppeCreateRequest createRequest) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvRaumgruppeController().create(createRequest);
	}


	/**
	 * Patcht eine bestehende {@link UvRaumgruppe}. Die ID wird aus dem Patch-Objekt gelesen.
	 *
	 * @param schema   das Datenbankschema
	 * @param patch    die Patch-Daten für die UvRaumgruppe (inklusive ID)
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@PATCH
	@Path("/raeume/gruppen")
	@Operation(summary = "Patcht eine bestehende UvRaumgruppe.",
			description = "Patcht eine bestehende UvRaumgruppe.")
	@ApiResponse(responseCode = "201", description = "UvRaumgruppe wurde erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvRaumgruppe.class)))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvRaumgruppe(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für die UvRaumgruppe", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvRaumgruppe.class))) final @Valid UvRaumgruppePatchRequest patch,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvRaumgruppeController().patch(patch);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen mehrerer {@link UvRaumgruppe}-Einträge.
	 * Die IDs in dem Patch müssen vorhanden sein, damit die zu patchenden Daten gefunden werden können.
	 *
	 * @param schema   das Datenbankschema
	 * @param patches  die Patch-Daten für die UvRaumgruppen (jeweils inklusive ID)
	 * @param request  die HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/raeume/gruppen/patch/multiple")
	@Operation(summary = "Patcht mehrere bestehende UvRaumgruppen.",
			description = "Patcht mehrere bestehende UvRaumgruppen anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "UvRaumgruppen wurden erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = UvRaumgruppe.class))))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvRaumgruppen(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für die UvRaumgruppen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(
									schema = @Schema(implementation = UvRaumgruppe.class)))) final @Valid Collection<UvRaumgruppePatchRequest> patches,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvRaumgruppeController().patchMultiple(patches);
	}


	/**
	 * Löscht eine bestehende {@link UvRaumgruppe} anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param id       die ID der Raumgruppe
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/raeume/gruppen/{id : \\d+}")
	@Operation(summary = "Löscht eine bestehende UvRaumgruppe.",
			description = "Löscht eine bestehende UvRaumgruppe anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvStundentafelFach wurde erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvRaumgruppe.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvRaumgruppe(
			@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvRaumgruppeController().delete(id);
	}


	/**
	 * Löscht mehrere bestehende {@link UvRaumgruppe}-Einträge anhand der IDs.
	 *
	 * @param schema   das Datenbankschema
	 * @param ids      die IDs der zu löschenden Raumgruppen
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/raeume/gruppen/delete/multiple")
	@Operation(summary = "Löscht mehrere bestehende UvRaumgruppen.",
			description = "Löscht mehrere bestehende UvRaumgruppen anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "Die UvRaumgruppen wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvRaumgruppe.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response deleteUvRaumgruppen(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden UvRaumgruppen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final java.util.List<Long> ids,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvRaumgruppeController().deleteMultiple(ids);
	}

	// ============================================================
	//   RAUM (get -> create -> patch -> patchMultiple -> delete -> deleteMultiple)
	// ============================================================

	/**
	 * Erstellt einen neuen {@link UvRaum} und gibt ihn zurück.
	 *
	 * @param schema         das Datenbankschema, in welchem der {@link UvRaum} erstellt wird
	 * @param request        die Informationen zur HTTP-Anfrage
	 * @param createRequest  JSON-Objekt mit den Daten des neuen UvRaums
	 *
	 * @return die HTTP-Antwort mit dem neuen {@link UvRaum}
	 */
	@POST
	@Path("/raeume/create")
	@Operation(summary = "Erstellt einen neuen UvRaum und gibt ihn zurück.",
			description = "Erstellt einen neuen UvRaum und gibt ihn zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvRaum wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvRaum.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen UvRaum anzulegen.")
	@ApiResponse(responseCode = "409", description = "Der UvRaum ist schon in der Datenbank enthalten.")
	public Response createUvRaum(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvRaum-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvRaum.class))) final @Valid UvRaumCreateRequest createRequest) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvRaumController().create(createRequest);
	}

	/**
	 * Erstellt mehrere neue {@link UvRaum}-Objekte und gibt sie zurück.
	 *
	 * @param schema          das Datenbankschema, in welchem die {@link UvRaum}-Objekte erstellt werden
	 * @param request         die Informationen zur HTTP-Anfrage
	 * @param createRequests  JSON-Objekt mit den Daten der neuen UvRaeume
	 *
	 * @return die HTTP-Antwort mit den neuen {@link UvRaum}-Objekten
	 */
	@POST
	@Path("/raeume/create/multiple")
	@Operation(summary = "Erstellt mehrere neue UvRaum-Objekte und gibt sie zurück.",
			description = "Erstellt mehrere neue UvRaum-Objekte und gibt sie zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvRaum-Objekte wurden erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = UvRaum.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen UvRaum anzulegen.")
	@ApiResponse(responseCode = "409", description = "Der UvRaum ist schon in der Datenbank enthalten.")
	public Response createUvRaeume(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvRaum-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(
									schema = @Schema(implementation = UvRaum.class)))) final @Valid Collection<UvRaumCreateRequest> createRequests) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvRaumController().createMultiple(createRequests);
	}

	/**
	 * Patcht einen bestehenden {@link UvRaum}. Die ID wird aus dem Patch-Objekt gelesen.
	 *
	 * @param schema   das Datenbankschema
	 * @param patch    die Patch-Daten für den UvRaum (inklusive ID)
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@PATCH
	@Path("/raeume")
	@Operation(summary = "Patcht einen bestehenden UvRaum.",
			description = "Patcht einen bestehenden UvRaum.")
	@ApiResponse(responseCode = "200", description = "UvRaum wurde erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvRaum.class)))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvRaum(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für den UvRaum", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvRaum.class))) final @Valid UvRaumPatchRequest patch,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvRaumController().patch(patch);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen mehrerer {@link UvRaum}-Einträge.
	 * Die IDs in dem Patch müssen vorhanden sein, damit die zu patchenden Daten gefunden werden können.
	 *
	 * @param schema   das Datenbankschema
	 * @param patches  die Patch-Daten für die UvRaeume (jeweils inklusive ID)
	 * @param request  die HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/raeume/patch/multiple")
	@Operation(summary = "Patcht mehrere bestehende UvRaeume.",
			description = "Patcht mehrere bestehende UvRaeume anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "UvRaeume wurden erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = UvRaum.class))))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvRaeume(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für die UvRaeume", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = UvRaum.class)))) final @Valid Collection<UvRaumPatchRequest> patches,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvRaumController().patchMultiple(patches);
	}


	/**
	 * Löscht einen bestehenden {@link UvRaum} anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param id       die ID des Raums
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/raeume/{id : \\d+}")
	@Operation(summary = "Löscht einen bestehenden UvRaum.",
			description = "Löscht einen bestehenden UvRaum anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvRaum wurde erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvRaum.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvRaum(
			@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvRaumController().delete(id);
	}


	/**
	 * Löscht mehrere bestehende {@link UvRaum}-Einträge anhand der IDs.
	 *
	 * @param schema   das Datenbankschema
	 * @param ids      die IDs der zu löschenden Räume
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/raeume/delete/multiple")
	@Operation(summary = "Löscht mehrere bestehende UvRaeume.",
			description = "Löscht mehrere bestehende UvRaeume anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "Die UvRaeume wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvRaum.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response deleteUvRaeume(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden UvRaeume", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final java.util.List<Long> ids,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvRaumController().deleteMultiple(ids);
	}

	// ============================================================
	//   LEHRER (get -> create -> patch -> patchMultiple -> delete -> deleteMultiple)
	// ============================================================

	/**
	 * Erstellt einen neuen {@link UvLehrer} und gibt ihn zurück.
	 *
	 * @param schema   das Datenbankschema, in welchem der {@link UvLehrer} erstellt wird
	 * @param patch     die Daten der allgemeinen Anrechnung
	 * @param request  die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem neuen {@link UvLehrer}
	 */
	@POST
	@Path("/lehrer/create")
	@Operation(summary = "Erstellt einen neuen UvLehrer und gibt ihn zurück.",
			description = "Erstellt einen neuen UvLehrer und gibt ihn zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvLehrer wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvLehrer.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen UvLehrer anzulegen.")
	@ApiResponse(responseCode = "409", description = "Der UvLehrer ist schon in der Datenbank enthalten.")
	public Response createUvLehrer(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Der Post für die UvLehrer-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvLehrer.class))) final @Valid UvLehrerCreateRequest patch,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvLehrerController().create(patch);
	}

	/**
	 * Erstellt einen neuen {@link UvLehrer} und gibt ihn zurück.
	 *
	 * @param schema   das Datenbankschema, in welchem der {@link UvLehrer} erstellt wird
	 * @param patch     die Daten der allgemeinen Anrechnung
	 * @param request  die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem neuen {@link UvLehrer}
	 */
	@POST
	@Path("/lehrer/create/multiple")
	@Operation(summary = "Erstellt einen neuen UvLehrer und gibt ihn zurück.",
			description = "Erstellt einen neuen UvLehrer und gibt ihn zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvLehrer wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvLehrer.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen UvLehrer anzulegen.")
	@ApiResponse(responseCode = "409", description = "Der UvLehrer ist schon in der Datenbank enthalten.")
	public Response createUvLehrerMultiple(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Der Post für die UvLehrer-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = UvLehrer.class)))) final @Valid Collection<UvLehrerCreateRequest> patch,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvLehrerController().createMultiple(patch);
	}


	/**
	 * Patcht einen bestehenden {@link UvLehrer} anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param patch       der Patch
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@PATCH
	@Path("/lehrer")
	@Operation(summary = "Patcht einen bestehenden UvLehrer.",
			description = "Patcht einen bestehenden UvLehrer anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvLehrer wurde erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvLehrer.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine allgemeine Anrechnung anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchUvLehrer(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für den UvLehrer", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvLehrer.class))) final @Valid UvLehrerPatchRequest patch,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvLehrerController().patch(patch);
	}

	/**
	 * Löscht einen bestehenden {@link UvLehrer} anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param id       die ID des Lehrers
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/lehrer/{id : \\d+}")
	@Operation(summary = "Löscht einen bestehenden UvLehrer.",
			description = "Löscht einen bestehenden UvLehrer anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvLehrer wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = UvLehrer.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine allgemeine Anrechnung zu löschen.")
	@ApiResponse(responseCode = "404", description = "Keine allgemeine Anrechnung mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteUvLehrer(
			@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvLehrerController().delete(id);
	}


	/**
	 * Löscht mehrere bestehende {@link UvLehrer}-Einträge anhand der IDs.
	 *
	 * @param schema   das Datenbankschema
	 * @param ids      die IDs der zu löschenden Lehrer
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/lehrer/delete/multiple")
	@Operation(summary = "Löscht mehrere bestehende UvLehrer.",
			description = "Löscht mehrere bestehende UvLehrer anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "Die UvLehrer wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvLehrer.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Keine allgemeine Anrechnung mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response deleteUvLehrerMultiple(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden UvLehrer", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final java.util.Collection<Long> ids,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvLehrerController().deleteMultiple(ids);
	}

	/**
	 * Erstellt eine neue UV-Lehrer-Zuordnung zu einem Planungsabschnitt und gibt sie zurück.
	 *
	 * @param schema   das Datenbankschema
	 * @param request  die Informationen zur HTTP-Anfrage
	 * @param createRequest       JSON-Objekt mit den Daten
	 *
	 * @return die HTTP-Antwort mit der neuen Zuordnung
	 */
	@POST
	@Path("/planungsabschnitt/lehrer/create")
	@Operation(summary = "Erstellt eine neue UV-Lehrer-Zuordnung zu einem Planungsabschnitt und gibt sie zurück.",
			description = "Erstellt eine neue UV-Lehrer-Zuordnung zu einem Planungsabschnitt und gibt sie zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "Zuordnung wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvPlanungsabschnittLehrer.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "409", description = "Die Zuordnung ist bereits vorhanden.")
	public Response createUvPlanungsabschnittLehrer(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Die Zuordnungsdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(
									implementation = UvPlanungsabschnittLehrer.class))) final @Valid UvPlanungsabschnittLehrerCreateRequest createRequest)
			throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvPlanungsabschnittLehrerController().create(createRequest);
	}


	/**
	 * Erstellt mehrere neue UV-Lehrer-Zuordnungen zu Planungsabschnitten und gibt sie zurück.
	 *
	 * @param schema   das Datenbankschema
	 * @param request  die Informationen zur HTTP-Anfrage
	 * @param createRequests       JSON-Objekt mit den Daten
	 *
	 * @return die HTTP-Antwort mit den neuen Zuordnungen
	 */
	@POST
	@Path("/planungsabschnitt/lehrer/create/multiple")
	@Operation(summary = "Erstellt mehrere neue UV-Lehrer-Zuordnungen zu Planungsabschnitten.",
			description = "Erstellt mehrere neue UV-Lehrer-Zuordnungen zu Planungsabschnitten. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "Zuordnungen wurden erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvPlanungsabschnittLehrer.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	public Response createUvPlanungsabschnittLehrerMultiple(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Die Liste der Zuordnungsdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(
									implementation = UvPlanungsabschnittLehrer.class)))) final @Valid Collection<UvPlanungsabschnittLehrerCreateRequest> createRequests)
			throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvPlanungsabschnittLehrerController().createMultiple(createRequests);
	}


	/**
	 * Löscht eine UV-Lehrer-Zuordnung zu einem Planungsabschnitt.
	 *
	 * @param schema               das Datenbankschema
	 * @param idPlanungsabschnitt  die ID des Planungsabschnitts
	 * @param idLehrer             die ID des UV-Lehrers
	 * @param request              die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/planungsabschnitt/{idPlanungsabschnitt : \\d+}/lehrer/{idLehrer : \\d+}")
	@Operation(summary = "Löscht eine UV-Lehrer-Zuordnung zu einem Planungsabschnitt.",
			description = "Löscht eine UV-Lehrer-Zuordnung zu einem Planungsabschnitt.")
	@ApiResponse(responseCode = "200", description = "Löschen erfolgreich.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvPlanungsabschnittLehrer(
			@PathParam("schema") final String schema,
			@PathParam("idPlanungsabschnitt") final long idPlanungsabschnitt,
			@PathParam("idLehrer") final long idLehrer,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvPlanungsabschnittLehrerController()
				.delete(new DTOUvPlanungsabschnittLehrerPK(idPlanungsabschnitt, idLehrer));
	}


	/**
	 * Löscht mehrere UV-Lehrer-Zuordnungen zu Planungsabschnitten.
	 *
	 * @param schema   das Datenbankschema
	 * @param idPlanungsabschnitt  die ID des Planungsabschnitts
	 * @param ids      die Liste der zu löschenden Lehrer
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@POST
	@Path("/planungsabschnitt/{idPlanungsabschnitt : \\d+}/lehrer/delete/multiple")
	@Operation(summary = "Löscht mehrere UV-Lehrer-Zuordnungen zu Planungsabschnitten.",
			description = "Löscht mehrere UV-Lehrer-Zuordnungen zu Planungsabschnitten.")
	@ApiResponse(responseCode = "200", description = "Löschen erfolgreich.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	public Response deleteUvPlanungsabschnittLehrerMultiple(
			@PathParam("schema") final String schema,
			@PathParam("idPlanungsabschnitt") final long idPlanungsabschnitt,
			@RequestBody(description = "Die Liste der zu löschenden Zuordnungen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final java.util.List<Long> ids,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvPlanungsabschnittLehrerController()
				.deleteMultiple(ids.stream().map(id -> new DTOUvPlanungsabschnittLehrerPK(idPlanungsabschnitt, id)).toList());
	}


	// ============================================================
	//   PLANUNGSABSCHNITT ZEITRASTER (get -> create -> createMultiple -> delete -> deleteMultiple)
	// ============================================================


	/**
	 * Erstellt eine neue UV-Zeitraster-Zuordnung zu einem Planungsabschnitt und gibt sie zurück.
	 *
	 * @param schema         das Datenbankschema
	 * @param request        die Informationen zur HTTP-Anfrage
	 * @param createRequest  die Daten für die neue Zeitraster-Zuordnung
	 *
	 * @return die HTTP-Antwort mit der neuen Zuordnung
	 */
	@POST
	@Path("/planungsabschnitt/zeitraster/create")
	@Operation(summary = "Erstellt eine neue UV-Zeitraster-Zuordnung zu einem Planungsabschnitt und gibt sie zurück.",
			description = "Erstellt eine neue UV-Zeitraster-Zuordnung zu einem Planungsabschnitt und gibt sie zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "Zuordnung wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvPlanungsabschnittZeitraster.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "409", description = "Die Zuordnung ist bereits vorhanden.")
	public Response createUvPlanungsabschnittZeitraster(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Die Zuordnungsdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(
									implementation = UvPlanungsabschnittZeitraster.class))) final @Valid UvPlanungsabschnittZeitrasterCreateRequest createRequest)
			throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvPlanungsabschnittZeitrasterController().create(createRequest);
	}


	/**
	 * Erstellt mehrere neue UV-Zeitraster-Zuordnungen zu Planungsabschnitten und gibt sie zurück.
	 *
	 * @param schema          das Datenbankschema
	 * @param request         die Informationen zur HTTP-Anfrage
	 * @param createRequests  die Daten für die neuen Zeitraster-Zuordnungen
	 *
	 * @return die HTTP-Antwort mit den neuen Zuordnungen
	 */
	@POST
	@Path("/planungsabschnitt/zeitraster/create/multiple")
	@Operation(summary = "Erstellt mehrere neue UV-Zeitraster-Zuordnungen zu Planungsabschnitten.",
			description = "Erstellt mehrere neue UV-Zeitraster-Zuordnungen zu Planungsabschnitten. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "Zuordnungen wurden erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvPlanungsabschnittZeitraster.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	public Response createUvPlanungsabschnittZeitrasterMultiple(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Die Liste der Zuordnungsdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(
									implementation = UvPlanungsabschnittZeitraster.class)))) final @Valid Collection<UvPlanungsabschnittZeitrasterCreateRequest> createRequests)
			throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvPlanungsabschnittZeitrasterController().createMultiple(createRequests);
	}

	/**
	 * Patcht ein bestehendes {@link UvPlanungsabschnittZeitraster} anhand der zusammengesetzten ID.
	 *
	 * @param schema               das Datenbankschema
	 * @param patch                die Patch-Daten für das UvPlanungsabschnittZeitraster
	 * @param request              die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem gepatchten {@link UvPlanungsabschnittZeitraster}
	 */
	@PATCH
	@Path("/planungsabschnitte/{idPlanungsabschnitt : \\d+}/zeitraster/{idZeitraster : \\d+}")
	@Operation(summary = "Patcht ein bestehendes UvPlanungsabschnittZeitraster.",
			description = "Patcht ein bestehendes UvPlanungsabschnittZeitraster.")
	@ApiResponse(responseCode = "200", description = "UvPlanungsabschnittZeitraster wurde erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvPlanungsabschnittZeitraster.class)))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvPlanungsabschnittZeitraster(@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für das UvPlanungsabschnittZeitraster", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(
									implementation = UvPlanungsabschnittZeitraster.class))) final @Valid UvPlanungsabschnittZeitrasterPatchRequest patch,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvPlanungsabschnittZeitrasterController().patch(patch);
	}

	/**
	 * Löscht eine UV-Zeitraster-Zuordnung zu einem Planungsabschnitt.
	 *
	 * @param schema               das Datenbankschema
	 * @param idPlanungsabschnitt  die ID des Planungsabschnitts
	 * @param idZeitraster         die ID des Zeitrasters
	 * @param request              die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der gelöschten {@link UvPlanungsabschnittZeitraster}-Zuordnung
	 */
	@DELETE
	@Path("/planungsabschnitt/{idPlanungsabschnitt : \\d+}/zeitraster/{idZeitraster : \\d+}")
	@Operation(summary = "Löscht eine UV-Zeitraster-Zuordnung zu einem Planungsabschnitt.",
			description = "Löscht eine UV-Zeitraster-Zuordnung zu einem Planungsabschnitt.")
	@ApiResponse(responseCode = "200", description = "UvPlanungsabschnittZeitraster wurde erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvPlanungsabschnittZeitraster.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvPlanungsabschnittZeitraster(
			@PathParam("schema") final String schema,
			@PathParam("idPlanungsabschnitt") final long idPlanungsabschnitt,
			@PathParam("idZeitraster") final long idZeitraster,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvPlanungsabschnittZeitrasterController()
				.delete(new DTOUvPlanungsabschnittZeitrasterPK(idPlanungsabschnitt, idZeitraster));
	}


	/**
	 * Löscht mehrere UV-Zeitraster-Zuordnungen zu Planungsabschnitten.
	 *
	 * @param schema               das Datenbankschema
	 * @param idPlanungsabschnitt  die ID des Planungsabschnitts
	 * @param ids                  die IDs der zu löschenden Zeitraster
	 * @param request              die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den gelöschten {@link UvPlanungsabschnittZeitraster}-Zuordnungen
	 */
	@POST
	@Path("/planungsabschnitt/{idPlanungsabschnitt : \\d+}/zeitraster/delete/multiple")
	@Operation(summary = "Löscht mehrere UV-Zeitraster-Zuordnungen zu Planungsabschnitten.",
			description = "Löscht mehrere UV-Zeitraster-Zuordnungen zu Planungsabschnitten.")
	@ApiResponse(responseCode = "200", description = "UvPlanungsabschnittZeitraster wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvPlanungsabschnittZeitraster.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	public Response deleteUvPlanungsabschnittZeitrasterMultiple(
			@PathParam("schema") final String schema,
			@PathParam("idPlanungsabschnitt") final long idPlanungsabschnitt,
			@RequestBody(description = "Die IDs der zu löschenden Zeitraster", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final Collection<Long> ids,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvPlanungsabschnittZeitrasterController()
				.deleteMultiple(idPlanungsabschnitt, ids);
	}


	// #####################################################################
	// ################## UvPlanungsabschnittSchueler ######################
	// #####################################################################


	/**
	 * Erstellt eine neue UV-Schüler-Zuordnung zu einem Planungsabschnitt und gibt sie zurück.
	 *
	 * @param schema   das Datenbankschema
	 * @param request  die Informationen zur HTTP-Anfrage
	 * @param createRequest das JSON-Objekt mit den Daten
	 *
	 * @return die HTTP-Antwort mit der neuen Zuordnung
	 */
	@POST
	@Path("/planungsabschnitt/schueler/create")
	@Operation(summary = "Erstellt eine neue UV-Schüler-Zuordnung zu einem Planungsabschnitt und gibt sie zurück.",
			description = "Erstellt eine neue UV-Schüler-Zuordnung zu einem Planungsabschnitt und gibt sie zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "Zuordnung wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvPlanungsabschnittSchueler.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "409", description = "Die Zuordnung ist bereits vorhanden.")
	public Response createUvPlanungsabschnittSchueler(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Die Zuordnungsdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvPlanungsabschnittSchueler.class)))
			@Valid final UvPlanungsabschnittSchuelerCreateRequest createRequest) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvPlanungsabschnittSchuelerController().create(createRequest);
	}


	/**
	 * Erstellt mehrere neue UV-Schüler-Zuordnungen zu Planungsabschnitten und gibt sie zurück.
	 *
	 * @param schema   das Datenbankschema
	 * @param request  die Informationen zur HTTP-Anfrage
	 * @param createRequests die JSON-Objekte mit den Daten
	 *
	 * @return die HTTP-Antwort mit den neuen Zuordnungen
	 */
	@POST
	@Path("/planungsabschnitt/schueler/create/multiple")
	@Operation(summary = "Erstellt mehrere neue UV-Schüler-Zuordnungen zu Planungsabschnitten.",
			description = "Erstellt mehrere neue UV-Schüler-Zuordnungen zu Planungsabschnitten. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "Zuordnungen wurden erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvPlanungsabschnittSchueler.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	public Response createUvPlanungsabschnittSchuelerMultiple(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Die Liste der Zuordnungsdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = UvPlanungsabschnittSchueler.class))))
			@Valid final Collection<UvPlanungsabschnittSchuelerCreateRequest> createRequests) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvPlanungsabschnittSchuelerController().createMultiple(createRequests);
	}

	/**
	 * Importiert Schüler anhand der übergebenen Importoptionen in den UV-Planungsabschnitt.
	 *
	 * @param schema               das Datenbankschema
	 * @param idPlanungsabschnitt  die ID des UV-Planungsabschnitts
	 * @param options              die Importoptionen
	 * @param request              die HTTP-Anfrage
	 * @return die neu importierten UV-Schüler-Zuordnungen
	 */
	@POST
	@Path("/planungsabschnitt/{idPlanungsabschnitt : \\d+}/schueler/import")
	@Operation(summary = "Importiert Schüler anhand von Importoptionen in den UV-Planungsabschnitt.",
			description = "Importiert Schüler anhand der übergebenen Importoptionen in den UV-Planungsabschnitt "
					+ "und gibt die dabei neu erzeugten UV-Planungsabschnittsdaten zurück.")
	@ApiResponse(responseCode = "200", description = "Die UV-Daten wurden erfolgreich importiert.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvPlanungsabschnittsdatenBundle.class)))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Importoptionen.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Schuljahresabschnitt oder Planungsabschnitt nicht gefunden.")
	public Response importUvPlanungsabschnittSchueler(
			@PathParam("schema") final String schema,
			@PathParam("idPlanungsabschnitt") final long idPlanungsabschnitt,
			@RequestBody(description = "Die Importoptionen für den Schülerimport", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvSchuelerImportOptions.class))) final @Valid UvSchuelerImportOptions options,
			@Context final HttpServletRequest request) {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request)
				.getUvPlanungsabschnittSchuelerImportController()
				.importSchueler(idPlanungsabschnitt, options);
	}

	/**
	 * Patcht einen bestehenden {@link UvPlanungsabschnittSchueler}.
	 *
	 * @param schema   das Datenbankschema
	 * @param patch    das JSON-Objekt mit den Patch-Daten
	 * @param request  die HTTP-Anfrage
	 * @return         die HTTP-Antwort
	 */
	@PATCH
	@Path("/planungsabschnitt/schueler")
	@Operation(summary = "Patcht ein bestehendes UvPlanungsabschnittSchueler.", description = "Patcht ein bestehendes UvPlanungsabschnittSchueler.")
	@ApiResponse(responseCode = "200", description = "Patch erfolgreich.")
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvPlanungsabschnittSchueler(@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für das UvPlanungsabschnittSchueler", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvPlanungsabschnittSchueler.class)))
			@Valid final UvPlanungsabschnittSchuelerPatchRequest patch,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvPlanungsabschnittSchuelerController().patch(patch);
	}

	/**
	 * Löscht eine UV-Schüler-Zuordnung zu einem Planungsabschnitt.
	 *
	 * @param schema               das Datenbankschema
	 * @param idPlanungsabschnitt  die ID des Planungsabschnitts
	 * @param idSchueler           die ID des Schülers
	 * @param request              die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/planungsabschnitt/{idPlanungsabschnitt : \\d+}/schueler/{idSchueler : \\d+}")
	@Operation(summary = "Löscht eine UV-Schüler-Zuordnung zu einem Planungsabschnitt.",
			description = "Löscht eine UV-Schüler-Zuordnung zu einem Planungsabschnitt.")
	@ApiResponse(responseCode = "200", description = "Löschen erfolgreich.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvPlanungsabschnittSchueler(
			@PathParam("schema") final String schema,
			@PathParam("idPlanungsabschnitt") final long idPlanungsabschnitt,
			@PathParam("idSchueler") final long idSchueler,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvPlanungsabschnittSchuelerController()
				.delete(new DTOUvPlanungsabschnittSchuelerPK(idPlanungsabschnitt, idSchueler));
	}


	/**
	 * Löscht mehrere UV-Schüler-Zuordnungen zu Planungsabschnitten.
	 *
	 * @param schema               das Datenbankschema
	 * @param idPlanungsabschnitt  die ID des Planungsabschnitts
	 * @param ids                  die Liste der zu löschenden Schüler
	 * @param request              die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@POST
	@Path("/planungsabschnitt/{idPlanungsabschnitt : \\d+}/schueler/delete/multiple")
	@Operation(summary = "Löscht mehrere UV-Schüler-Zuordnungen zu Planungsabschnitten.",
			description = "Löscht mehrere UV-Schüler-Zuordnungen zu Planungsabschnitten.")
	@ApiResponse(responseCode = "200", description = "Löschen erfolgreich.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	public Response deleteUvPlanungsabschnittSchuelerMultiple(
			@PathParam("schema") final String schema,
			@PathParam("idPlanungsabschnitt") final long idPlanungsabschnitt,
			@RequestBody(description = "Die Liste der zu löschenden Zuordnungen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final java.util.List<Long> ids,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvPlanungsabschnittSchuelerController()
				.deleteMultiple(ids.stream().map(id -> new DTOUvPlanungsabschnittSchuelerPK(idPlanungsabschnitt, id)).toList());
	}


	/**
	 * Erstellt eine neue {@link UvLehrerAnrechnungsstunden}-Zuordnung und gibt sie zurück.
	 *
	 * @param schema   das Datenbankschema, in welchem die {@link UvLehrerAnrechnungsstunden}-Zuordnung erstellt wird
	 * @param request  die Informationen zur HTTP-Anfrage
	 * @param patch       JSON-Objekt mit den Daten
	 *
	 * @return die HTTP-Antwort mit der neuen {@link UvLehrerAnrechnungsstunden}-Zuordnung
	 */
	@POST
	@Path("/lehrer/anrechnungsstunden/create")
	@Operation(summary = "Erstellt eine neue UvLehrerAnrechnungsstunden-Zuordnung und gibt sie zurück.",
			description = "Erstellt eine neue UvLehrerAnrechnungsstunden-Zuordnung und gibt sie zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvLehrerAnrechnungsstunden-Zuordnung wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvLehrerAnrechnungsstunden.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte, um eine Zuordnung anzulegen.")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createUvLehrerAnrechnungsstunde(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvLehrerAnrechnungsstunden-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvLehrerAnrechnungsstunden.class))) final @Valid UvLehrerAnrechnungsstundenCreateRequest patch) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvLehrerAnrechnungsstundenController().create(patch);
	}


	/**
	 * Patcht eine bestehende {@link UvLehrerAnrechnungsstunden}-Zuordnung anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param patch       JSON-Objekt mit den Patch-Daten
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@PATCH
	@Path("/lehrer/anrechnungsstunden")
	@Operation(summary = "Patcht eine bestehende UvLehrerAnrechnungsstunden-Zuordnung.",
			description = "Patcht eine bestehende UvLehrerAnrechnungsstunden-Zuordnung anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvLehrerAnrechnungsstunden wurde erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvLehrerAnrechnungsstunden.class)))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvLehrerAnrechnungsstunde(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für die UvLehrerAnrechnungsstunden-Zuordnung", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvLehrerAnrechnungsstunden.class))) final @Valid UvLehrerAnrechnungsstundenPatchRequest patch,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvLehrerAnrechnungsstundenController().patch(patch);
	}


	/**
	 * Löscht eine bestehende {@link UvLehrerAnrechnungsstunden}-Zuordnung anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param id       die ID der Zuordnung
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/lehrer/anrechnungsstunden/{id : \\d+}")
	@Operation(summary = "Löscht eine bestehende UvLehrerAnrechnungsstunden-Zuordnung.",
			description = "Löscht eine bestehende UvLehrerAnrechnungsstunden-Zuordnung anhand der ID.")
	@ApiResponse(responseCode = "200", description = "Die Lehrerfunktion wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = UvLehrerAnrechnungsstunden.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvLehrerAnrechnungsstunde(
			@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvLehrerAnrechnungsstundenController().delete(id);
	}


	/**
	 * Löscht mehrere bestehende {@link UvLehrerAnrechnungsstunden}-Zuordnungen anhand ihrer IDs.
	 *
	 * @param schema   das Datenbankschema
	 * @param ids      die IDs der zu löschenden Zuordnungen
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/lehrer/anrechnungsstunden/delete/multiple")
	@Operation(summary = "Löscht mehrere bestehende UvLehrerAnrechnungsstunden-Zuordnungen.",
			description = "Löscht mehrere bestehende UvLehrerAnrechnungsstunden-Zuordnungen anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "Die Zuordnungen wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvLehrerAnrechnungsstunden.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff).")
	public Response deleteUvLehrerAnrechnungsstunden(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden UvLehrerAnrechnungsstunden-Zuordnungen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final java.util.List<Long> ids,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvLehrerAnrechnungsstundenController().deleteMultiple(ids);
	}


	/**
	 * Importiert die Anrechnungsstunden für die angegebenen UV-Lehrer aus den Schild-Personalabschnittsdaten
	 * (Anrechnungen, Mehrleistungen und Entlastungen) und gibt die dabei neu erzeugten
	 * {@link UvLehrerAnrechnungsstunden}-Einträge zurück.
	 *
	 * @param schema       das Datenbankschema
	 * @param uvLehrerIds  die IDs der UV-Lehrer
	 * @param request      die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den neu erstellten {@link UvLehrerAnrechnungsstunden}-Einträgen
	 */
	@POST
	@Path("/lehrer/anrechnungsstunden/import/personalabschnittsdaten")
	@Operation(summary = "Importiert UV-Lehrer-Anrechnungsstunden aus den Schild-Personalabschnittsdaten.",
			description = "Importiert für die angegebenen UV-Lehrer-IDs die Anrechnungs-, Mehrleistungs- und Entlastungsstunden "
					+ "aus den Schild-Personalabschnittsdaten in die UV-Anrechnungsstunden-Tabelle und gibt die neu erzeugten Einträge zurück. "
					+ "Hat einer der UV-Lehrer bereits Anrechnungsstunden, wird der Import abgebrochen.")
	@ApiResponse(responseCode = "201", description = "Die neu erstellten Anrechnungsstunden-Einträge wurden erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvLehrerAnrechnungsstunden.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte, um den Import durchzuführen.")
	@ApiResponse(responseCode = "404", description = "Mindestens einer der angefragten UV-Lehrer wurde nicht gefunden.")
	@ApiResponse(responseCode = "409", description = "Mindestens einer der UV-Lehrer hat bereits Anrechnungsstunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff).")
	public Response importUvLehrerAnrechnungsstundenFromPersonalabschnittsdaten(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der UV-Lehrer, für die der Import durchgeführt werden soll", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final java.util.List<Long> uvLehrerIds,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvLehrerAnrechnungsstundenController()
				.importFromPersonalabschnittsdaten(uvLehrerIds);
	}


	/**
	 * Erstellt einen neuen {@link UvLehrerPflichtstundensoll}-Eintrag und gibt ihn zurück.
	 *
	 * @param schema   das Datenbankschema, in welchem der Eintrag erstellt wird
	 * @param request  die Informationen zur HTTP-Anfrage
	 * @param patch       JSON-Objekt mit den Daten
	 *
	 * @return die HTTP-Antwort mit dem neuen {@link UvLehrerPflichtstundensoll}-Eintrag
	 */
	@POST
	@Path("/lehrer/pflichtstundensoll/create")
	@Operation(summary = "Erstellt einen neuen UvLehrerPflichtstundensoll-Eintrag und gibt ihn zurück.",
			description = "Erstellt einen neuen UvLehrerPflichtstundensoll-Eintrag und gibt ihn zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvLehrerPflichtstundensoll wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvLehrerPflichtstundensoll.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte, um den Eintrag anzulegen.")
	@ApiResponse(responseCode = "409", description = "Der Eintrag ist bereits vorhanden.")
	public Response createUvLehrerPflichtstundensoll(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Die Daten für den neuen UvLehrerPflichtstundensoll-Eintrag", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvLehrerPflichtstundensoll.class))) final @Valid UvLehrerPflichtstundensollCreateRequest patch) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvLehrerPflichtstundensollController().create(patch);
	}


	/**
	 * Patcht einen bestehenden {@link UvLehrerPflichtstundensoll}-Eintrag anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param patch       JSON-Objekt mit den Patch-Daten
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@PATCH
	@Path("/lehrer/pflichtstundensoll")
	@Operation(summary = "Patcht einen bestehenden UvLehrerPflichtstundensoll-Eintrag.",
			description = "Patcht einen bestehenden UvLehrerPflichtstundensoll-Eintrag anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvLehrerPflichtstundensoll wurde erfolgreich gepatched.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvLehrerPflichtstundensoll.class)))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvLehrerPflichtstundensoll(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für den UvLehrerPflichtstundensoll-Eintrag", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvLehrerPflichtstundensoll.class))) final @Valid UvLehrerPflichtstundensollPatchRequest patch,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvLehrerPflichtstundensollController().patch(patch);
	}


	/**
	 * Löscht einen bestehenden {@link UvLehrerPflichtstundensoll}-Eintrag anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param id       die ID des Eintrags
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/lehrer/pflichtstundensoll/{id : \\d+}")
	@Operation(summary = "Löscht einen bestehenden UvLehrerPflichtstundensoll-Eintrag.",
			description = "Löscht einen bestehenden UvLehrerPflichtstundensoll-Eintrag anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvLehrerPflichtstundensoll wurde erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvLehrerPflichtstundensoll.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvLehrerPflichtstundensoll(
			@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvLehrerPflichtstundensollController().delete(id);
	}


	/**
	 * Löscht mehrere bestehende {@link UvLehrerPflichtstundensoll}-Einträge anhand ihrer IDs.
	 *
	 * @param schema   das Datenbankschema
	 * @param ids      die IDs der zu löschenden Einträge
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/lehrer/pflichtstundensoll/delete/multiple")
	@Operation(summary = "Löscht mehrere bestehende UvLehrerPflichtstundensoll-Einträge.",
			description = "Löscht mehrere bestehende UvLehrerPflichtstundensoll-Einträge anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "Die Einträge wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvLehrerPflichtstundensoll.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff).")
	public Response deleteUvLehrerPflichtstundensollMultiple(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden UvLehrerPflichtstundensoll-Einträge", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final java.util.List<Long> ids,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvLehrerPflichtstundensollController().deleteMultiple(ids);
	}


	/**
	 * Importiert das Pflichtstundensoll für die angegebenen UV-Lehrer aus den Schild-Personalabschnittsdaten
	 * und gibt die dabei neu erzeugten {@link UvLehrerPflichtstundensoll}-Einträge zurück.
	 *
	 * @param schema       das Datenbankschema
	 * @param uvLehrerIds  die IDs der UV-Lehrer
	 * @param request      die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den neu erstellten {@link UvLehrerPflichtstundensoll}-Einträgen
	 */
	@POST
	@Path("/lehrer/pflichtstundensoll/import/personalabschnittsdaten")
	@Operation(summary = "Importiert UV-Lehrer-Pflichtstundensoll aus den Schild-Personalabschnittsdaten.",
			description = "Importiert für die angegebenen UV-Lehrer-IDs das Pflichtstundensoll aus den Schild-Personalabschnittsdaten "
					+ "in die UV-Pflichtstundensoll-Tabelle und gibt die neu erzeugten Einträge zurück. "
					+ "Hat einer der UV-Lehrer bereits Pflichtstundensoll-Einträge, wird der Import abgebrochen.")
	@ApiResponse(responseCode = "201", description = "Die neu erstellten Pflichtstundensoll-Einträge wurden erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvLehrerPflichtstundensoll.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte, um den Import durchzuführen.")
	@ApiResponse(responseCode = "404", description = "Mindestens einer der angefragten UV-Lehrer wurde nicht gefunden.")
	@ApiResponse(responseCode = "409", description = "Mindestens einer der UV-Lehrer hat bereits Pflichtstundensoll-Einträge.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff).")
	public Response importUvLehrerPflichtstundensollFromPersonalabschnittsdaten(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der UV-Lehrer, für die der Import durchgeführt werden soll", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final java.util.List<Long> uvLehrerIds,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvLehrerPflichtstundensollController()
				.importFromPersonalabschnittsdaten(uvLehrerIds);
	}


	/**
	 * Erstellt einen neuen {@link LehrerUnterrichtsfach}-Eintrag für eine UV-Lehrkraft und gibt ihn zurück.
	 *
	 * @param schema   das Datenbankschema, in welchem der Eintrag erstellt wird
	 * @param request  die Informationen zur HTTP-Anfrage
	 * @param patch    JSON-Objekt mit den Daten
	 *
	 * @return die HTTP-Antwort mit dem neuen {@link LehrerUnterrichtsfach}-Eintrag
	 */
	@POST
	@Path("/lehrer/unterrichtsfach/create")
	@Operation(summary = "Erstellt einen neuen UvLehrerUnterrichtsfach-Eintrag und gibt ihn zurück.",
			description = "Erstellt einen neuen UvLehrerUnterrichtsfach-Eintrag und gibt ihn zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvLehrerUnterrichtsfach-Eintrag wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = LehrerUnterrichtsfach.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte, um einen Eintrag anzulegen.")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createUvLehrerUnterrichtsfach(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Die Daten für den neuen UvLehrerUnterrichtsfach-Eintrag", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LehrerUnterrichtsfach.class))) final @Valid UvLehrerUnterrichtsfachCreateRequest patch) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvLehrerUnterrichtsfachController().create(patch);
	}


	/**
	 * Patcht einen bestehenden {@link LehrerUnterrichtsfach}-Eintrag einer UV-Lehrkraft anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param patch    JSON-Objekt mit den Patch-Daten
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@PATCH
	@Path("/lehrer/unterrichtsfach")
	@Operation(summary = "Patcht einen bestehenden UvLehrerUnterrichtsfach-Eintrag.",
			description = "Patcht einen bestehenden UvLehrerUnterrichtsfach-Eintrag anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvLehrerUnterrichtsfach wurde erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LehrerUnterrichtsfach.class)))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvLehrerUnterrichtsfach(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für den UvLehrerUnterrichtsfach-Eintrag", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LehrerUnterrichtsfach.class))) final @Valid UvLehrerUnterrichtsfachPatchRequest patch,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvLehrerUnterrichtsfachController().patch(patch.id, patch);
	}


	/**
	 * Löscht einen bestehenden {@link LehrerUnterrichtsfach}-Eintrag einer UV-Lehrkraft anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param id       die ID des Eintrags
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/lehrer/unterrichtsfach/{id : \\d+}")
	@Operation(summary = "Löscht einen bestehenden UvLehrerUnterrichtsfach-Eintrag.",
			description = "Löscht einen bestehenden UvLehrerUnterrichtsfach-Eintrag anhand der ID.")
	@ApiResponse(responseCode = "200", description = "Der UvLehrerUnterrichtsfach-Eintrag wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = LehrerUnterrichtsfach.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvLehrerUnterrichtsfach(
			@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvLehrerUnterrichtsfachController().delete(id);
	}

	// ============================================================
	//   LERNGRUPPEN-LEHRER (create -> patch -> patchMultiple -> delete -> deleteMultiple)
	// ============================================================

	/**
	 * Erstellt eine neue {@link UvLerngruppenLehrer}-Zuordnung und gibt sie zurück.
	 *
	 * @param schema   das Datenbankschema, in welchem die {@link UvLerngruppenLehrer}-Zuordnung erstellt wird
	 * @param request  die Informationen zur HTTP-Anfrage
	 * @param createRequest       JSON-Objekt mit den Daten
	 *
	 * @return die HTTP-Antwort mit der neuen {@link UvLerngruppenLehrer}-Zuordnung
	 */
	@POST
	@Path("/lerngruppen/lehrer/create")
	@Operation(summary = "Erstellt eine neue UvLerngruppenLehrer-Zuordnung und gibt sie zurück.",
			description = "Erstellt eine neue UvLerngruppenLehrer-Zuordnung und gibt sie zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvLerngruppenLehrer-Zuordnung wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvLerngruppenLehrer.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte, um eine Zuordnung anzulegen.")
	@ApiResponse(responseCode = "409", description = "Die Zuordnung ist bereits vorhanden.")
	public Response createUvLerngruppenLehrer(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvLerngruppenLehrer-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvLerngruppenLehrer.class))) final @Valid UvLerngruppenLehrerCreateRequest createRequest)
			throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvLerngruppenLehrerController().create(createRequest);
	}


	/**
	 * Erstellt mehrere neue {@link UvLerngruppenLehrer}-Zuordnungen und gibt sie zurück.
	 *
	 * @param schema   das Datenbankschema, in welchem die {@link UvLerngruppenLehrer}-Zuordnungen erstellt werden
	 * @param request  die Informationen zur HTTP-Anfrage
	 * @param createRequests       JSON-Objekt mit den Daten
	 *
	 * @return die HTTP-Antwort mit den neuen {@link UvLerngruppenLehrer}-Zuordnungen
	 */
	@POST
	@Path("/lerngruppen/lehrer/create/multiple")
	@Operation(summary = "Erstellt mehrere neue UvLerngruppenLehrer-Zuordnungen und gibt sie zurück.",
			description = "Erstellt mehrere neue UvLerngruppenLehrer-Zuordnungen und gibt sie zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "Die UvLerngruppenLehrer-Zuordnungen wurden erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvLerngruppenLehrer.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte, um Zuordnungen anzulegen.")
	@ApiResponse(responseCode = "409", description = "Mindestens eine Zuordnung ist bereits vorhanden.")
	public Response createUvLerngruppenLehrerMultiple(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für mehrere UvLerngruppenLehrer-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(
									implementation = UvLerngruppenLehrer.class)))) final @Valid Collection<UvLerngruppenLehrerCreateRequest> createRequests)
			throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvLerngruppenLehrerController().createMultiple(createRequests);
	}


	/**
	 * Patcht eine bestehende {@link UvLerngruppenLehrer}-Zuordnung anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param patch       JSON-Objekt mit den Patch-Daten
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@PATCH
	@Path("/lerngruppen/lehrer/{id : \\d+}")
	@Operation(summary = "Patcht eine bestehende UvLerngruppenLehrer-Zuordnung.",
			description = "Patcht eine bestehende UvLerngruppenLehrer-Zuordnung anhand der ID.")
	@ApiResponse(responseCode = "200", description = "Patch erfolgreich.")
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvLerngruppenLehrer(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für die UvLerngruppenLehrer-Zuordnung", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvLerngruppenLehrer.class))) final @Valid UvLerngruppenLehrerPatchRequest patch,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvLerngruppenLehrerController().patch(patch);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen mehrerer {@link UvLerngruppenLehrer}-Einträge.
	 * Die IDs in dem Patch müssen vorhanden sein, damit die zu patchenden Daten gefunden werden können.
	 *
	 * @param schema   das Datenbankschema
	 * @param patches       JSON-Objekt mit den Patch-Daten
	 * @param request  die HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/lerngruppen/lehrer/patch/multiple")
	@Operation(summary = "Patcht mehrere bestehende UvLerngruppenLehrer-Zuordnungen.",
			description = "Patcht mehrere bestehende UvLerngruppenLehrer-Zuordnungen anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "Patch erfolgreich.")
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response patchUvLerngruppenLehrerMultiple(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für mehrere UvLerngruppenLehrer-Zuordnungen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(
									implementation = UvLerngruppenLehrer.class)))) final @Valid Collection<UvLerngruppenLehrerPatchRequest> patches,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvLerngruppenLehrerController().patchMultiple(patches);
	}


	/**
	 * Löscht eine bestehende {@link UvLerngruppenLehrer}-Zuordnung anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param id       die ID der Zuordnung
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/lerngruppen/lehrer/{id : \\d+}")
	@Operation(summary = "Löscht eine bestehende UvLerngruppenLehrer-Zuordnung.",
			description = "Löscht eine bestehende UvLerngruppenLehrer-Zuordnung anhand der ID.")
	@ApiResponse(responseCode = "204", description = "Löschung erfolgreich.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvLerngruppenLehrer(
			@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvLerngruppenLehrerController().delete(id);
	}


	/**
	 * Löscht mehrere bestehende {@link UvLerngruppenLehrer}-Zuordnungen anhand ihrer IDs.
	 *
	 * @param schema   das Datenbankschema
	 * @param ids      die IDs der zu löschenden Zuordnungen
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/lerngruppen/lehrer/delete/multiple")
	@Operation(summary = "Löscht mehrere bestehende UvLerngruppenLehrer-Zuordnungen.",
			description = "Löscht mehrere bestehende UvLerngruppenLehrer-Zuordnungen anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "Die Zuordnungen wurden erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff).")
	public Response deleteUvLerngruppenLehrerMultiple(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden UvLerngruppenLehrer-Zuordnungen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final java.util.List<Long> ids,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvLerngruppenLehrerController().deleteMultiple(ids);
	}

	// ============================================================
	//   LERNGRUPPEN-SCHIENEN (create -> delete -> deleteMultiple)
	// ============================================================

	/**
	 * Erstellt eine neue {@link UvLerngruppenSchiene}-Zuordnung und gibt sie zurück.
	 *
	 * @param schema   das Datenbankschema, in welchem die {@link UvLerngruppenSchiene}-Zuordnung erstellt wird
	 * @param request  die Informationen zur HTTP-Anfrage
	 * @param createRequest       JSON-Objekt mit den Daten
	 *
	 * @return die HTTP-Antwort mit der neuen {@link UvLerngruppenSchiene}-Zuordnung
	 */
	@POST
	@Path("/lerngruppen/schienen/create")
	@Operation(summary = "Erstellt eine neue UvLerngruppenSchiene-Zuordnung und gibt sie zurück.",
			description = "Erstellt eine neue UvLerngruppenSchiene-Zuordnung und gibt sie zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvLerngruppenSchiene-Zuordnung wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvLerngruppenSchiene.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte, um eine Zuordnung anzulegen.")
	@ApiResponse(responseCode = "409", description = "Die Zuordnung ist bereits vorhanden.")
	public Response createUvLerngruppenSchiene(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvLerngruppenSchiene-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvLerngruppenSchiene.class))) final @Valid UvLerngruppenSchieneCreateRequest createRequest)
			throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvLerngruppenSchieneController().create(createRequest);
	}


	/**
	 * Löscht eine bestehende {@link UvLerngruppenSchiene}-Zuordnung anhand der IDs.
	 *
	 * @param schema   das Datenbankschema
	 * @param idLerngruppe  die ID der Lerngruppe
	 * @param idSchiene     die ID der Schiene
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/lerngruppen/schienen/{idLerngruppe : \\d+}/{idSchiene : \\d+}")
	@Operation(summary = "Löscht eine bestehende UvLerngruppenSchiene-Zuordnung.",
			description = "Löscht eine bestehende UvLerngruppenSchiene-Zuordnung anhand der IDs.")
	@ApiResponse(responseCode = "204", description = "Löschung erfolgreich.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvLerngruppenSchiene(
			@PathParam("schema") final String schema,
			@PathParam("idLerngruppe") final long idLerngruppe,
			@PathParam("idSchiene") final long idSchiene,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvLerngruppenSchieneController()
				.delete(new DTOUvLerngruppeSchienePK(idLerngruppe, idSchiene));
	}


	/**
	 * Löscht mehrere bestehende {@link UvLerngruppenSchiene}-Zuordnungen.
	 *
	 * @param schema   das Datenbankschema
	 * @param zuordnungen  die Liste der zu löschenden Zuordnungen
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@POST
	@Path("/lerngruppen/schienen/delete/multiple")
	@Operation(summary = "Löscht mehrere bestehende UvLerngruppenSchiene-Zuordnungen.",
			description = "Löscht mehrere bestehende UvLerngruppenSchiene-Zuordnungen anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "Die Zuordnungen wurden erfolgreich gelöscht.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	public Response deleteUvLerngruppenSchienen(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die Zuordnungen, die gelöscht werden sollen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(
									schema = @Schema(implementation = UvLerngruppenSchiene.class)))) final java.util.List<UvLerngruppenSchiene> zuordnungen,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvLerngruppenSchieneController().deleteMultiple(
				zuordnungen.stream().map(z -> new DTOUvLerngruppeSchienePK(z.idLerngruppe, z.idSchiene)).toList());
	}

	// ============================================================
	//   UNTERRICHTE-RAEUME (create -> delete -> deleteMultiple)
	// ============================================================

	/**
	 * Erstellt eine neue {@link UvUnterrichtRaum}-Zuordnung und gibt sie zurück.
	 *
	 * @param schema         das Datenbankschema, in welchem die {@link UvUnterrichtRaum}-Zuordnung erstellt wird
	 * @param request        die Informationen zur HTTP-Anfrage
	 * @param createRequest  die Daten der Zuordnung
	 *
	 * @return die HTTP-Antwort mit der neuen {@link UvUnterrichtRaum}-Zuordnung
	 */
	@POST
	@Path("/unterrichte/raeume/create")
	@Operation(summary = "Erstellt eine neue UvUnterrichtRaum-Zuordnung und gibt sie zurück.",
			description = "Erstellt eine neue UvUnterrichtRaum-Zuordnung und gibt sie zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvUnterrichtRaum-Zuordnung wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvUnterrichtRaum.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte, um eine Zuordnung anzulegen.")
	@ApiResponse(responseCode = "409", description = "Die Zuordnung ist bereits vorhanden.")
	public Response createUvUnterrichtRaum(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvUnterrichtRaum-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvUnterrichtRaum.class))) final @Valid UvUnterrichtRaumCreateRequest createRequest)
			throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvUnterrichtRaumController().create(createRequest);
	}


	/**
	 * Löscht eine bestehende {@link UvUnterrichtRaum}-Zuordnung anhand der IDs.
	 *
	 * @param schema   das Datenbankschema
	 * @param idUnterricht  die ID des Unterrichts
	 * @param idRaum     die ID des Raums
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/unterrichte/raeume/{idUnterricht : \\d+}/{idRaum : \\d+}")
	@Operation(summary = "Löscht eine bestehende UvUnterrichtRaum-Zuordnung.",
			description = "Löscht eine bestehende UvUnterrichtRaum-Zuordnung anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "Löschung erfolgreich.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvUnterrichtRaum(
			@PathParam("schema") final String schema,
			@PathParam("idUnterricht") final long idUnterricht,
			@PathParam("idRaum") final long idRaum,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvUnterrichtRaumController()
				.delete(new DTOUvUnterrichtRaumPK(idUnterricht, idRaum));
	}


	/**
	 * Löscht mehrere bestehende {@link UvUnterrichtRaum}-Zuordnungen.
	 *
	 * @param schema   das Datenbankschema
	 * @param zuordnungen  die Liste der zu löschenden Zuordnungen
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@POST
	@Path("/unterrichte/raeume/delete/multiple")
	@Operation(summary = "Löscht mehrere bestehende UvUnterrichtRaum-Zuordnungen.",
			description = "Löscht mehrere bestehende UvUnterrichtRaum-Zuordnungen anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "Die Zuordnungen wurden erfolgreich gelöscht.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	public Response deleteUvUnterrichtRaeume(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die Zuordnungen, die gelöscht werden sollen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(
									schema = @Schema(implementation = UvUnterrichtRaum.class)))) final java.util.List<UvUnterrichtRaum> zuordnungen,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvUnterrichtRaumController()
				.deleteMultiple(zuordnungen.stream().map(z -> new DTOUvUnterrichtRaumPK(z.idUnterricht, z.idRaum)).toList());
	}

	// ============================================================
	//   UNTERRICHTE-LERNGRUPPENLEHRER (create -> delete -> deleteMultiple)
	// ============================================================

	/**
	 * Erstellt eine neue {@link UvUnterrichtLerngruppenlehrer}-Zuordnung und gibt sie zurück.
	 *
	 * @param schema         das Datenbankschema, in welchem die {@link UvUnterrichtLerngruppenlehrer}-Zuordnung erstellt wird
	 * @param request        die Informationen zur HTTP-Anfrage
	 * @param createRequest  die Daten der Zuordnung
	 *
	 * @return die HTTP-Antwort mit der neuen {@link UvUnterrichtLerngruppenlehrer}-Zuordnung
	 */
	@POST
	@Path("/unterrichte/lerngruppenlehrer/create")
	@Operation(summary = "Erstellt eine neue UvUnterrichtLerngruppenlehrer-Zuordnung und gibt sie zurück.",
			description = "Erstellt eine neue UvUnterrichtLerngruppenlehrer-Zuordnung und gibt sie zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "UvUnterrichtLerngruppenlehrer-Zuordnung wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvUnterrichtLerngruppenlehrer.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte, um eine Zuordnung anzulegen.")
	@ApiResponse(responseCode = "409", description = "Die Zuordnung ist bereits vorhanden.")
	public Response createUvUnterrichtLerngruppenlehrer(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvUnterrichtLerngruppenlehrer-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvUnterrichtLerngruppenlehrer.class)))
			final @Valid UvUnterrichtLerngruppenlehrerCreateRequest createRequest) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvUnterrichtLerngruppenlehrerController().create(createRequest);
	}


	/**
	 * Löscht eine bestehende {@link UvUnterrichtLerngruppenlehrer}-Zuordnung anhand der IDs.
	 *
	 * @param schema   das Datenbankschema
	 * @param idUnterricht  die ID des Unterrichts
	 * @param idLerngruppenLehrer     die ID des Lerngruppenlehrers
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/unterrichte/lerngruppenlehrer/{idUnterricht : \\d+}/{idLerngruppenLehrer : \\d+}")
	@Operation(summary = "Löscht eine bestehende UvUnterrichtLerngruppenlehrer-Zuordnung.",
			description = "Löscht eine bestehende UvUnterrichtLerngruppenlehrer-Zuordnung anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "Löschung erfolgreich.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Nicht gefunden.")
	public Response deleteUvUnterrichtLerngruppenlehrer(
			@PathParam("schema") final String schema,
			@PathParam("idUnterricht") final long idUnterricht,
			@PathParam("idLerngruppenLehrer") final long idLerngruppenLehrer,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvUnterrichtLerngruppenlehrerController()
				.delete(new DTOUvUnterrichteLerngruppenlehrerPK(idUnterricht, idLerngruppenLehrer));
	}


	/**
	 * Löscht mehrere bestehende {@link UvUnterrichtLerngruppenlehrer}-Zuordnungen.
	 *
	 * @param schema   das Datenbankschema
	 * @param zuordnungen  die Liste der zu löschenden Zuordnungen
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@POST
	@Path("/unterrichte/lerngruppenlehrer/delete/multiple")
	@Operation(summary = "Löscht mehrere bestehende UvUnterrichtLerngruppenlehrer-Zuordnungen.",
			description = "Löscht mehrere bestehende UvUnterrichtLerngruppenlehrer-Zuordnungen anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "Die Zuordnungen wurden erfolgreich gelöscht.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	public Response deleteUvUnterrichtLerngruppenlehrerMultiple(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Die Zuordnungen, die gelöscht werden sollen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(
									implementation = UvUnterrichtLerngruppenlehrer.class)))) final java.util.List<UvUnterrichtLerngruppenlehrer> zuordnungen,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvUnterrichtLerngruppenlehrerController()
				.deleteMultiple(zuordnungen.stream().map(z -> new DTOUvUnterrichteLerngruppenlehrerPK(z.idUnterricht, z.idLerngruppenLehrer)).toList());
	}

	// ============================================================
	//   ZEITRASTER (get -> create -> patch -> patchMultiple -> delete -> deleteMultiple)
	// ============================================================


	/**
	 * Erstellt ein neues {@link UvZeitraster}-Objekt und gibt dieses zurück.
	 *
	 * @param schema   das Datenbankschema, in welchem das {@link UvZeitraster}-Objekt erstellt wird
	 * @param request  die HTTP-Anfrage
	 * @param createRequest       JSON-Objekt mit den zu erstellenden Daten
	 *
	 * @return die HTTP-Antwort mit dem neu erstellten {@link UvZeitraster}-Objekt
	 */
	@POST
	@Path("/zeitraster/create")
	@Operation(summary = "Erstellt ein neues Zeitraster und gibt es zurück.",
			description = "Erstellt ein neues Zeitraster und gibt es zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "Zeitraster wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvZeitraster.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte, um ein Zeitraster anzulegen.")
	@ApiResponse(responseCode = "409", description = "Das Zeitraster ist bereits vorhanden.")
	public Response createUvZeitraster(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvZeitraster-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvZeitraster.class))) final @Valid UvZeitrasterCreateRequest createRequest) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvZeitrasterController().create(createRequest);
	}


	/**
	 * Patcht ein bestehendes {@link UvZeitraster}-Objekt anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param patch       JSON-Objekt mit den Patch-Daten
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/zeitraster")
	@Operation(summary = "Patcht ein bestehendes Zeitraster anhand der ID.",
			description = "Patcht ein bestehendes Zeitraster anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvZeitraster wurde erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvZeitraster.class)))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Zeitraster nicht gefunden.")
	public Response patchUvZeitraster(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für das UvZeitraster", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvZeitraster.class))) final @Valid UvZeitrasterPatchRequest patch,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvZeitrasterController().patch(patch);
	}


	/**
	 * Patcht mehrere bestehende {@link UvZeitraster}-Objekte.
	 * Die IDs müssen vorhanden sein, damit die zu patchenden Datensätze gefunden werden können.
	 *
	 * @param schema   das Datenbankschema
	 * @param patches       JSON-Array mit den Patch-Daten
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/zeitraster/patch/multiple")
	@Operation(summary = "Patcht mehrere bestehende Zeitraster.",
			description = "Patcht mehrere bestehende Zeitraster anhand der angegebenen IDs.")
	@ApiResponse(responseCode = "200", description = "UvZeitraster wurden erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = UvZeitraster.class))))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Ein oder mehrere Zeitraster nicht gefunden.")
	public Response patchUvZeitrasterMultiple(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für die UvZeitraster", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(
									schema = @Schema(implementation = UvZeitraster.class)))) final @Valid Collection<UvZeitrasterPatchRequest> patches,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvZeitrasterController().patchMultiple(patches);
	}


	/**
	 * Löscht ein bestehendes {@link UvZeitraster}-Objekt anhand der ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param id       die ID des zu löschenden Zeitrasters
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Ergebnis der Lösch-Operation
	 */
	@DELETE
	@Path("/zeitraster/{id : \\d+}")
	@Operation(summary = "Löscht ein bestehendes Zeitraster anhand der ID.",
			description = "Löscht ein bestehendes Zeitraster anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvZeitraster wurde erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvZeitraster.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Zeitraster nicht gefunden.")
	public Response deleteUvZeitraster(
			@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvZeitrasterController().delete(id);
	}


	/**
	 * Löscht mehrere bestehende {@link UvZeitraster}-Objekte anhand ihrer IDs.
	 *
	 * @param schema   das Datenbankschema
	 * @param ids      Liste der IDs der zu löschenden Zeitraster
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Ergebnis der Lösch-Operation
	 */
	@DELETE
	@Path("/zeitraster/delete/multiple")
	@Operation(summary = "Löscht mehrere bestehende Zeitraster anhand der IDs.",
			description = "Löscht mehrere bestehende Zeitraster anhand der IDs.")
	@ApiResponse(responseCode = "201", description = "Löschung erfolgreich.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvZeitraster.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "500", description = "Fehler beim Löschen der Datensätze.")
	public Response deleteUvZeitrasterMultiple(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Liste der IDs der zu löschenden Zeitraster", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final java.util.List<Long> ids,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvZeitrasterController().deleteMultiple(ids);
	}

	// ============================================================
	//   ZEITRASTEREINTRAG (get -> create -> patch -> patchMultiple -> delete -> deleteMultiple)
	// ============================================================

	/**
	 * Erstellt einen neuen {@link UvZeitrasterEintrag}-Eintrag und gibt diesen zurück.
	 *
	 * @param schema   das Datenbankschema
	 * @param request  die HTTP-Anfrage
	 * @param createRequest       JSON-Objekt mit den zu erstellenden Daten
	 *
	 * @return die HTTP-Antwort mit dem neu erstellten Eintrag
	 */
	@POST
	@Path("/zeitraster/eintraege/create")
	@Operation(summary = "Erstellt einen neuen Zeitrastereintrag.",
			description = "Erstellt einen neuen Zeitrastereintrag und gibt diesen zurück.")
	@ApiResponse(responseCode = "201", description = "Zeitrastereintrag erfolgreich erstellt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvZeitrasterEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte zum Erstellen.")
	public Response createUvZeitrasterEintrag(@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvStundentafel-Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvZeitrasterEintrag.class))) final @Valid UvZeitrasterEintragCreateRequest createRequest) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvZeitrasterEintragController().create(createRequest);
	}

	/**
	 * Erstellt mehrere neue {@link UvZeitrasterEintrag}-Einträge und gibt diese zurück.
	 *
	 * @param schema   das Datenbankschema
	 * @param request  die HTTP-Anfrage
	 * @param createRequests       JSON-Objekt mit den zu erstellenden Daten
	 *
	 * @return die HTTP-Antwort mit den neu erstellten Einträgen
	 */
	@POST
	@Path("/zeitraster/eintraege/create/multiple")
	@Operation(summary = "Erstellt mehrere neue Zeitrastereinträge.",
			description = "Erstellt mehrere neue Zeitrastereinträge und gibt diese zurück.")
	@ApiResponse(responseCode = "201", description = "Zeitrastereinträge erfolgreich erstellt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvZeitrasterEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte zum Erstellen.")
	public Response createUvZeitrasterEintraege(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Der Post für die UvZeitrasterEintrag-Daten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(
									schema = @Schema(
											implementation = UvZeitrasterEintrag.class)))) final @Valid Collection<UvZeitrasterEintragCreateRequest> createRequests) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvZeitrasterEintragController().createMultiple(createRequests);
	}


	/**
	 * Patcht einen bestehenden {@link UvZeitrasterEintrag}-Eintrag.
	 *
	 * @param schema   das Datenbankschema
	 * @param patch       JSON-Objekt mit den Patch-Daten
	 * @param request  die HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/zeitraster/eintraege")
	@Operation(summary = "Patcht einen bestehenden Zeitrastereintrag.",
			description = "Patcht einen bestehenden Zeitrastereintrag anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvZeitrasterEintrag wurde erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvZeitrasterEintrag.class)))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Zeitrastereintrag nicht gefunden.")
	public Response patchUvZeitrasterEintrag(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für das UvZeitraster", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvZeitrasterEintrag.class))) final @Valid UvZeitrasterEintragPatchRequest patch,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvZeitrasterEintragController().patch(patch);
	}


	/**
	 * Patcht mehrere bestehende {@link UvZeitrasterEintrag}-Einträge.
	 *
	 * @param schema   das Datenbankschema
	 * @param patches       JSON-Array mit den Patch-Daten
	 * @param request  die HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/zeitraster/eintraege/patch/multiple")
	@Operation(summary = "Patcht mehrere Zeitrastereinträge.",
			description = "Patcht mehrere Zeitrastereinträge gleichzeitig anhand ihrer IDs.")
	@ApiResponse(responseCode = "200", description = "UvFach wurde erfolgreich gepatcht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = UvZeitrasterEintrag.class))))
	@ApiResponse(responseCode = "400", description = "Fehlerhafte Daten.")
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Ein oder mehrere Einträge nicht gefunden.")
	public Response patchUvZeitrasterEintraege(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für die UvZeitraster", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(
									schema = @Schema(
											implementation = UvZeitrasterEintrag.class)))) final @Valid Collection<UvZeitrasterEintragPatchRequest> patches,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvZeitrasterEintragController().patchMultiple(patches);
	}


	/**
	 * Löscht einen bestehenden {@link UvZeitrasterEintrag}.
	 *
	 * @param schema   das Datenbankschema
	 * @param id       die ID des zu löschenden Eintrags
	 * @param request  die HTTP-Anfrage
	 *
	 * @return das Ergebnis der Lösch-Operation
	 */
	@DELETE
	@Path("/zeitraster/eintraege/{id : \\d+}")
	@Operation(summary = "Löscht einen bestehenden Zeitrastereintrag.",
			description = "Löscht einen bestehenden Zeitrastereintrag anhand der ID.")
	@ApiResponse(responseCode = "200", description = "UvZeitrasterEintrag wurde erfolgreich gelöscht.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UvZeitrasterEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "404", description = "Zeitrastereintrag nicht gefunden.")
	public Response deleteUvZeitrasterEintrag(
			@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvZeitrasterEintragController().delete(id);
	}


	/**
	 * Löscht mehrere bestehende {@link UvZeitrasterEintrag}-Einträge anhand ihrer IDs.
	 *
	 * @param schema   das Datenbankschema
	 * @param ids      Liste der IDs der zu löschenden Einträge
	 * @param request  die HTTP-Anfrage
	 *
	 * @return das Ergebnis der Lösch-Operation
	 */
	@DELETE
	@Path("/zeitraster/eintraege/delete/multiple")
	@Operation(summary = "Löscht mehrere bestehende Zeitrastereinträge.",
			description = "Löscht mehrere bestehende Zeitrastereinträge anhand der IDs.")
	@ApiResponse(responseCode = "200", description = "Löschung erfolgreich.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvZeitrasterEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Keine Rechte.")
	@ApiResponse(responseCode = "500", description = "Fehler beim Löschen der Datensätze.")
	public Response deleteUvZeitrasterEintraege(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Liste der IDs der zu löschenden Zeitrastereinträge", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final java.util.List<Long> ids,
			@Context final HttpServletRequest request) {
		return UvGrunddatenControllerFactory.withWriteAccess(request).getUvZeitrasterEintragController().deleteMultiple(ids);
	}

// ============================================================
//   UNTERRICHTE (create -> patch -> patchMultiple -> delete -> deleteMultiple)
// ============================================================

	/**
	 * Erstellt eine neue {@link UvUnterricht}-Einheit und gibt sie zurück.
	 *
	 * @param schema         das Datenbankschema, in welchem die {@link UvUnterricht}-Einheit erstellt wird
	 * @param request        die Informationen zur HTTP-Anfrage
	 * @param createRequest  JSON-Objekt mit den Daten der zu erstellenden {@link UvUnterricht}-Einheit
	 *
	 * @return die HTTP-Antwort mit der neu erstellten {@link UvUnterricht}-Einheit
	 */
	@POST
	@Path("/unterrichte/create")
	@Operation(summary = "Erstellt eine neue Unterrichtseinheit.",
			description = "Erstellt eine neue Unterrichtseinheit und gibt diese zurück.")
	@ApiResponse(responseCode = "201", description = "Unterricht erfolgreich erstellt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UvUnterricht.class)))
	public Response createUvUnterricht(
			@PathParam("schema") final String schema,
			@Context final HttpServletRequest request,
			@RequestBody(description = "Die zu erstellende Unterrichtseinheit", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvUnterricht.class))) final @Valid UvUnterrichtCreateRequest createRequest)
			throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvUnterrichtController().create(createRequest);
	}


	/**
	 * Patcht eine bestehende {@link UvUnterricht}-Einheit.
	 *
	 * @param schema   das Datenbankschema
	 * @param patch    JSON-Objekt mit den Patch-Daten
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/unterrichte")
	@Operation(summary = "Patcht eine bestehende Unterrichtseinheit.",
			description = "Patcht eine bestehende Unterrichtseinheit.")
	@ApiResponse(responseCode = "200", description = "Patch erfolgreich.")
	public Response patchUvUnterricht(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für den Unterricht", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = UvUnterricht.class))) final @Valid UvUnterrichtPatchRequest patch,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvUnterrichtController().patch(patch);
	}


	/**
	 * Patcht mehrere bestehende {@link UvUnterricht}-Einheiten anhand ihrer IDs.
	 * Die im Patch enthaltenen IDs müssen vorhanden sein.
	 *
	 * @param schema   das Datenbankschema
	 * @param patches  JSON-Array mit den Patch-Daten für mehrere {@link UvUnterricht}-Einheiten
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/unterrichte/patch/multiple")
	@Operation(summary = "Patcht mehrere Unterrichtseinheiten.",
			description = "Patcht mehrere Unterrichtseinheiten gleichzeitig anhand ihrer IDs.")
	@ApiResponse(responseCode = "200", description = "Patch erfolgreich.")
	public Response patchUvUnterrichte(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Patch-Daten für mehrere Unterrichtseinheiten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = UvUnterricht.class)))) final @Valid Collection<UvUnterrichtPatchRequest> patches,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvUnterrichtController().patchMultiple(patches);
	}


	/**
	 * Löscht eine bestehende {@link UvUnterricht}-Einheit anhand ihrer ID.
	 *
	 * @param schema   das Datenbankschema
	 * @param id       die ID der zu löschenden {@link UvUnterricht}-Einheit
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort
	 */
	@DELETE
	@Path("/unterrichte/{id : \\d+}")
	@Operation(summary = "Löscht eine bestehende Unterrichtseinheit.",
			description = "Löscht eine bestehende Unterrichtseinheit anhand der ID.")
	@ApiResponse(responseCode = "200", description = "Löschung erfolgreich.")
	public Response deleteUvUnterricht(
			@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvUnterrichtController().delete(id);
	}


	/**
	 * Löscht mehrere bestehende {@link UvUnterricht}-Einheiten anhand ihrer IDs.
	 *
	 * @param schema   das Datenbankschema
	 * @param ids      die Liste der IDs der zu löschenden {@link UvUnterricht}-Einheiten
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den IDs der gelöschten Einträge oder Fehlerdetails
	 */
	@DELETE
	@Path("/unterrichte/delete/multiple")
	@Operation(summary = "Löscht mehrere bestehende Unterrichtseinheiten.",
			description = "Löscht mehrere bestehende Unterrichtseinheiten anhand ihrer IDs.")
	@ApiResponse(responseCode = "200", description = "Löschung erfolgreich.")
	public Response deleteUvUnterrichte(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Liste der IDs der zu löschenden Unterrichtseinheiten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final java.util.List<Long> ids,
			@Context final HttpServletRequest request) throws ApiOperationException {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvUnterrichtController().deleteMultiple(ids);
	}


	/**
	 * Erstellt für die Lerngruppe mit der angegebenen ID {@link UvUnterricht}-Einheiten
	 * entsprechend dem Attribut {@code WochenstundenUnterrichtet} der Lerngruppe.
	 *
	 * @param schema         das Datenbankschema
	 * @param idLerngruppe   die ID der Lerngruppe
	 * @param request        die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den neu erstellten {@link UvUnterricht}-Einheiten
	 */
	@POST
	@Path("/unterrichte/create/lerngruppe/{id : \\d+}")
	@Operation(summary = "Erstellt Unterrichtseinheiten für eine Lerngruppe.",
			description = "Erstellt für die Lerngruppe mit der angegebenen ID so viele Unterrichtseinheiten, wie es das Attribut WochenstundenUnterrichtet hergibt.")
	@ApiResponse(responseCode = "201", description = "Unterrichte erfolgreich erstellt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvUnterricht.class))))
	public Response createUvUnterrichteByLerngruppe(
			@PathParam("schema") final String schema,
			@PathParam("id") final long idLerngruppe,
			@Context final HttpServletRequest request) {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvUnterrichtLerngruppenCreateController().createByLerngruppe(idLerngruppe);
	}


	/**
	 * Erstellt für alle Lerngruppen mit den angegebenen IDs {@link UvUnterricht}-Einheiten
	 * entsprechend dem jeweiligen Attribut {@code WochenstundenUnterrichtet}.
	 *
	 * @param schema   das Datenbankschema
	 * @param ids      die Liste der Lerngruppen-IDs
	 * @param request  die HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den neu erstellten {@link UvUnterricht}-Einheiten
	 */
	@POST
	@Path("/unterrichte/create/lerngruppe/multiple")
	@Operation(summary = "Erstellt Unterrichtseinheiten für mehrere Lerngruppen.",
			description = "Erstellt für alle Lerngruppen mit den angegebenen IDs Unterrichtseinheiten entsprechend WochenstundenUnterrichtet.")
	@ApiResponse(responseCode = "201", description = "Unterrichte erfolgreich erstellt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = UvUnterricht.class))))
	public Response createUvUnterrichteByLerngruppen(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Liste der Lerngruppen-IDs", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> ids,
			@Context final HttpServletRequest request) {
		return UvPlanungsabschnittControllerFactory.withWriteAccess(request).getUvUnterrichtLerngruppenCreateController().createByLerngruppen(ids);
	}


}
