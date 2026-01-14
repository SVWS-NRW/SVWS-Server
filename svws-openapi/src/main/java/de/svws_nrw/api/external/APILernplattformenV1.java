package de.svws_nrw.api.external;

import de.svws_nrw.core.data.lernplattform.v1.LernplattformV1;
import de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Export;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.lernplattformen.DataLernplattformenV1;
import de.svws_nrw.data.schule.DataLernplattformen;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die Lernplattform-API.
 * Diese Schnittstelle stellt einen Export für externe Lernplattformen zur Verfügung.
 */
@Path("/api/external/{schema}/v1/lernplattformen")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "External")
public class APILernplattformenV1 {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APILernplattformenV1() {
		// leer
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage eines Datenexports aller relevanten Informationen im angefragten Schuljahresabschnitt für die angefragte
	 * Lernplattform im Json-Format.
	 *
	 * @param schema                  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idLernplattform         die ID der Lernplattform
	 * @param idSchuljahresabschnitt  die ID des Schuljahresabschnitts
	 * @param request                 die Informationen zur HTTP-Anfrage
	 *
	 * @return der Lernplattform Datenexport im Json-Format
	 */
	@GET
	@Path("/{idLernplattform : \\d+}/{idSchuljahresabschnitt : \\d+}")
	@Operation(summary = "Liefert einen Datenexport aller relevanten Informationen zum angefragten Schuljahresabschnitt für die angegebene Lernplattform.",
			description = "Es werden alle relevanten Daten zu Jahrgängen, Klassen, Lehrern, Schülern, Fächern und Lerngruppen aus der SVWS-DB geladen und für"
					+ " den Export bezogen auf eine Lernplattform aufbereitet und zurückgegeben.")
	@ApiResponse(responseCode = "200", description = "Die Daten für den Lernplattformen Datenexport",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = LernplattformV1Export.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Lernplattformen Datenexport anzufordern.")
	@ApiResponse(responseCode = "404", description = "Es wurden nicht alle benötigten Ressourcen gefunden.")
	public Response getLernplattformenExport(@PathParam("schema") final String schema, @PathParam("idLernplattform") final long idLernplattform,
			@PathParam("idSchuljahresabschnitt") final int idSchuljahresabschnitt, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLernplattformenV1(conn, idSchuljahresabschnitt, new DataLernplattformen(conn)).getByIdAsResponse(idLernplattform),
				request, ServerMode.STABLE, BenutzerKompetenz.IMPORT_EXPORT_LERNPLATTFORM);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage eines Datenexports aller relevanten Informationen im angefragten Schuljahresabschnitt für die angefragte
	 * Lernplattform im gzip-Format.
	 *
	 * @param schema                  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idLernplattform         die ID der Lernplattform
	 * @param idSchuljahresabschnitt  die ID des Schuljahresabschnitts
	 * @param request                 die Informationen zur HTTP-Anfrage
	 *
	 * @return der Lernplattform Datenexport im gzip-Format
	 */
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/{idLernplattform : \\d+}/{idSchuljahresabschnitt : \\d+}/gzip")
	@Operation(summary = "Liefert einen im gzip-Format komprimierten Datenexport aller relevanten Informationen zum angefragten Schuljahresabschnitt für die "
			+ "angegebene Lernplattform.",
			description = "Es werden alle relevanten Daten zu Jahrgängen, Klassen, Lehrern, Schülern, Fächern und Lerngruppen aus der SVWS-DB geladen und für"
					+ " den Export bezogen auf eine Lernplattform aufbereitet und komprimiert im gzip-Format zurückgegeben.")
	@ApiResponse(responseCode = "200", description = "Die Daten für den Lernplattformen Datenexport",
			content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM, schema = @Schema(type = "string", format = "binary")))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Lernplattformen Datenexport anzufordern.")
	@ApiResponse(responseCode = "404", description = "Es wurden nicht alle benötigten Ressourcen gefunden.")
	public Response getLernplattformenExportAsGzip(@PathParam("schema") final String schema, @PathParam("idLernplattform") final long idLernplattform,
			@PathParam("idSchuljahresabschnitt") final int idSchuljahresabschnitt, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLernplattformenV1(conn, idSchuljahresabschnitt, new DataLernplattformen(conn)).getByIdAsGzipResponse(idLernplattform),
				request, ServerMode.STABLE, BenutzerKompetenz.IMPORT_EXPORT_LERNPLATTFORM);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der schulspezifischen Lernplattformen.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return          die Liste mit dem Katalog der Lernplattformen
	 */
	@GET
	@Path("/")
	@Operation(summary = "Gibt eine Übersicht aller schulspezifischen Lernplattformen zurück.",
			description = "Erstellt eine Liste aller vorhandenen Lernplattformen, insofer die notwendige Berechtigung vorliegen.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Lernplattformen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LernplattformV1.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lernplattformen anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Lernplattformen gefunden")
	public Response getLernplattformen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLernplattformenV1(conn, -1, new DataLernplattformen(conn)).getAllAsResponse(),
				request, ServerMode.STABLE, BenutzerKompetenz.IMPORT_EXPORT_LERNPLATTFORM);
	}

}
