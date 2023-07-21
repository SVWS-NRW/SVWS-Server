package de.svws_nrw.api.server;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import de.svws_nrw.api.OpenAPIApplication;
import de.svws_nrw.core.data.abschluss.GEAbschlussFaecher;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.ge.DataGEAbschlussFaecher;
import de.svws_nrw.db.DBEntityManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf Schülerdaten aus der SVWS-Datenbank in Bezug auf die SekI-Daten der Gesamtschule.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/schueler/ge/...
 */
@Path("/db/{schema}/gesamtschule")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIGesamtschule {

    /**
     * Die OpenAPI-Methode für die Abfrage der Leistungsdaten aus dem aktuellen Abschnitt zu der ID eines Schüler im angegebenen Schema, um die Prognose zu berechnen.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id            die ID des Schüler, für den die Leistungsdaten ausgelesen werden sollen
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Liste der Fächer mit ID des Datenbankschemas
     */
    @GET
    @Path("/schueler/{id : \\d+}/prognose_leistungsdaten")
    @Operation(summary = "Liefert zu der ID des Schülers die zugehörigen Leistungsdaten des aktuellen Lernabschnittes in Bezug auf die Prognose- bzw. Abschlussberechnung in der Sek I der Gesamtschule.",
    description = "Liest die Leistungsdaten des aktuellen Lernabschnittes in Bezug auf die Prognose- bzw. Abschlussberechnung in der Sek I "
    		    + "der Gesamtschule des Schülers mit der angegebene ID aus der Datenbank und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Leistungsdaten besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Leistungsdaten des Schülers",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = GEAbschlussFaecher.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Leistungsdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
    public Response getGesamtschuleSchuelerPrognoseLeistungsdaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		                                               @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN)) {
    		return (new DataGEAbschlussFaecher(conn)).get(id);
    	}
    }



    /**
     * Die OpenAPI-Methode für die Abfrage der Leistungsdaten aus dem angegebenen Abschnitt zu der ID eines Schüler im angegebenen Schema, um die Prognose zu berechnen.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id            die ID des Schüler, für den die Leistungsdaten ausgelesen werden sollen
     * @param abschnittID   der Schuljahresabschnitt, für den die Leistungsdaten ausgelesen werden sollen
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Liste der Fächer mit ID des Datenbankschemas
     */
    @GET
    @Path("/schueler/{id : \\d+}/prognose_leistungsdaten/abschnitt/{abschnittID : \\d+}")
    @Operation(summary = "Liefert zu der ID des Schülers die zugehörigen Leistungsdaten des angegegebenen Lernabschnittes in Bezug auf die Prognose- bzw. Abschlussberechnung in der Sek I der Gesamtschule.",
    description = "Liest die Leistungsdaten des angegebenen Lernabschnittes in Bezug auf die Prognose- bzw. Abschlussberechnung in der Sek I "
    		    + "der Gesamtschule des Schülers mit der angegebene ID aus der Datenbank und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Leistungsdaten besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Leistungsdaten des Schülers",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = GEAbschlussFaecher.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Leistungsdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
    public Response getGesamtschuleSchuelerPrognosLeistungsdatenFuerAbschnitt(@PathParam("schema") final String schema, @PathParam("id") final long id,
    																@PathParam("abschnittID") final long abschnittID,
    		                                                        @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN)) {
    		return (new DataGEAbschlussFaecher(conn)).getByAbschnitt(id, abschnittID);
    	}
    }

}
