package de.svws_nrw.api.server;

import de.svws_nrw.api.OpenAPIApplication;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.jahrgang.JahrgangsKatalogEintrag;
import de.svws_nrw.core.data.jahrgang.JahrgangsListeEintrag;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.jahrgaenge.DataJahrgangsdaten;
import de.svws_nrw.data.jahrgaenge.DataJahrgangsliste;
import de.svws_nrw.data.jahrgaenge.DataKatalogJahrgaenge;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Jahrgangsdaten aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/jahrgaenge/...
 */
@Path("/db/{schema}/jahrgaenge")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIJahrgaenge {

    /**
     * Die OpenAPI-Methode für die Abfrage der Liste der Jahrgänge im angegebenen Schema.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Liste der Jahrgänge mit ID des Datenbankschemas
     */
    @GET
    @Path("/")
    @Operation(summary = "Gibt eine Übersicht von allen Jahrgängen zurück.",
               description = "Erstellt eine Liste aller in der Datenbank vorhanden Jahrgänge unter Angabe der ID, des Kürzels, "
               		       + "des verwendeten Statistik-Kürzels, der Bezeichnung des Jahrgangs, die Schulgliederung zu der der "
               		       + "Jahrgang gehört, die ID eines Folgejahrgangs, sofern definiert, "
               		       + "einer Sortierreihenfolge und ob sie in der Anwendung sichtbar sein sollen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Jahrgangsdaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Jahrgangs-Listen-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = JahrgangsListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Jahrgangsdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Jahrgangs-Einträge gefunden")
    public Response getJahrgaenge(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataJahrgangsliste(conn).getList(),
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }



    /**
     * Die OpenAPI-Methode für die Abfrage der Daten eines Jahrgangs.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation des Jahrgangs
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die Daten des Jahrgangs
     */
    @GET
    @Path("/{id : \\d+}")
    @Operation(summary = "Liefert zu der ID des Jahrgangs die zugehörigen Daten.",
    description = "Liest die Daten des Jahrgangs zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Jahrgangsdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Daten des Jahrgangs",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = JahrgangsDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Jahrgangsdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Jahrgangs-Eintrag mit der angegebenen ID gefunden")
    public Response getJahrgang(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		                                    @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataJahrgangsdaten(conn).get(id),
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Katalogs der in den einzelnen Schulformen gültigen Jahrgänge.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              der Katalog der in den einzelnen Schulformen gültigen Jahrgänge
     */
    @GET
    @Path("/allgemein/jahrgaenge")
    @Operation(summary = "Gibt den Katalog der in den einzelnen Schulformen gültigen Jahrgänge zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden in den einzelnen Schulformen gültigen Jahrgänge. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Jahrgangs-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = JahrgangsKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Jahrgangs-Katalog-Einträge gefunden")
    public Response getKatalogJahrgaenge(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        OpenAPIApplication.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
        return (new DataKatalogJahrgaenge()).getAll();
    }

}
