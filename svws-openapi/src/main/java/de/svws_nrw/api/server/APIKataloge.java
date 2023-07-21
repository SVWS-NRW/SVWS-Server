package de.svws_nrw.api.server;

import de.svws_nrw.api.OpenAPIApplication;
import de.svws_nrw.core.data.kataloge.KatalogEintrag;
import de.svws_nrw.core.data.kataloge.KatalogEintragOrte;
import de.svws_nrw.core.data.kataloge.KatalogEintragOrtsteile;
import de.svws_nrw.core.data.kataloge.KatalogEintragStrassen;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.kataloge.DataHaltestellen;
import de.svws_nrw.data.kataloge.DataKatalogOrte;
import de.svws_nrw.data.kataloge.DataKatalogOrtsteile;
import de.svws_nrw.data.kataloge.DataOrte;
import de.svws_nrw.data.kataloge.DataOrtsteile;
import de.svws_nrw.data.kataloge.DataStrassen;
import de.svws_nrw.db.DBEntityManager;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf allgemeine Kataloge aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/api/kataloge/...
 */
@Path("/db/{schema}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIKataloge {


    /**
     * Die OpenAPI-Methode für die Abfrage des Strassen-Kataloges.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return die Liste der Strassen
     */
    @GET
    @Path("/allgemein/strassen")
    @Operation(summary = "Erstellt eine Liste aller in dem Katalog vorhandenen Strassen.",
               description = "Erstellt eine Liste aller in dem Katalog vorhandenen Strassen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Straßen-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KatalogEintragStrassen.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Straßen-Katalog-Einträge gefunden")
    public Response getKatalogStrassen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	OpenAPIApplication.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    	return (new DataStrassen()).getAll();
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Orts-Kataloges von IT.NRW.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return die die Orts-Katalog-Einträge
     */
    @GET
    @Path("/allgemein/orte")
    @Operation(summary = "Erstellt eine Liste aller in dem Katalog vorhandenen Orte.",
               description = "Erstellt eine Liste aller in dem Katalog vorhandenen Orte. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Orts-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KatalogEintragOrte.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Orts-Katalog-Einträge gefunden")
    public Response getKatalogOrte(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        OpenAPIApplication.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
        return (new DataKatalogOrte()).getAll();
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Liste der Orte im angegebenen Schema.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Liste der Orte mit ID des Datenbankschemas
     */
    @GET
    @Path("/orte")
    @Operation(summary = "Gibt eine Übersicht alle Orte im Katalog zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden Orte unter Angabe der ID, der PLZ, des Ortes, "
               		       + "ggf. des Kreises, dem Bundesland, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar bzw. "
               		       + "änderbar sein sollen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Orts-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrtKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Ort-Katalog-Einträge gefunden")
    public Response getOrte(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
        	return (new DataOrte(conn)).getAll();
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Ortsteil-Kataloges von IT.NRW.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return die die Ortsteil-Katalog-Einträge
     */
    @GET
    @Path("/allgemein/ortsteile")
    @Operation(summary = "Erstellt eine Liste aller in dem Katalog vorhandenen Ortsteile.",
               description = "Erstellt eine Liste aller in dem Katalog vorhandenen Ortsteile. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Ortsteil-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KatalogEintragOrtsteile.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Ortsteil-Katalog-Einträge gefunden")
    public Response getKatalogOrtsteile(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        OpenAPIApplication.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
        return (new DataKatalogOrtsteile()).getAll();
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Liste der Ortsteile im angegebenen Schema.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Liste der Ortsteile mit ID des Datenbankschemas
     */
    @GET
    @Path("/ortsteile")
    @Operation(summary = "Gibt eine Übersicht aller Ortsteile im Katalog zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden Ortsteile unter Angabe der ID, der zugehörigen"
               		       + "Ort-ID, dem Namen des Ortsteils, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar bzw. "
               		       + "änderbar sein sollen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Ortsteil-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrtsteilKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Ortsteil-Katalog-Einträge gefunden")
    public Response getOrtsteile(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
        	return (new DataOrtsteile(conn)).getAll();
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Liste der Haltestellen im angegebenen Schema.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Liste der Haltestellen mit ID des Datenbankschemas
     */
    @GET
    @Path("/haltestellen")
    @Operation(summary = "Gibt eine Übersicht der Haltestellen im Katalog zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden Haltestellen unter Angabe der ID, eines Kürzels und der "
               		       + "textuellen Beschreibung sowie der Information, ob der Eintrag in der Anwendung sichtbar bzw. änderbar sein soll, und"
               		       + "gibt diese zurück. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen zu den Haltestellen.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getHaltestellen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
    		return (new DataHaltestellen(conn)).getList();
    	}
    }

}
