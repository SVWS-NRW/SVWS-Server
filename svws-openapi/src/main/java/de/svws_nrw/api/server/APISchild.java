package de.svws_nrw.api.server;

import java.util.List;

import de.svws_nrw.api.OpenAPIApplication;
import de.svws_nrw.core.data.schild3.Schild3KatalogEintragAbiturInfos;
import de.svws_nrw.core.data.schild3.Schild3KatalogEintragDQRNiveaus;
import de.svws_nrw.core.data.schild3.Schild3KatalogEintragDatenart;
import de.svws_nrw.core.data.schild3.Schild3KatalogEintragExportCSV;
import de.svws_nrw.core.data.schild3.Schild3KatalogEintragFilterFehlendeEintraege;
import de.svws_nrw.core.data.schild3.Schild3KatalogEintragLaender;
import de.svws_nrw.core.data.schild3.Schild3KatalogEintragPruefungsordnung;
import de.svws_nrw.core.data.schild3.Schild3KatalogEintragPruefungsordnungOption;
import de.svws_nrw.core.data.schild3.Schild3KatalogEintragUnicodeUmwandlung;
import de.svws_nrw.core.data.schild3.Schild3KatalogEintragVersetzungsvermerke;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.schild3.DataSchildAbiturInfos;
import de.svws_nrw.data.schild3.DataSchildDQRNiveaus;
import de.svws_nrw.data.schild3.DataSchildDatenart;
import de.svws_nrw.data.schild3.DataSchildExportCSV;
import de.svws_nrw.data.schild3.DataSchildFilterFehlendeEintraege;
import de.svws_nrw.data.schild3.DataSchildLaender;
import de.svws_nrw.data.schild3.DataSchildPruefungsordnung;
import de.svws_nrw.data.schild3.DataSchildPruefungsordnungOptionen;
import de.svws_nrw.data.schild3.DataSchildUnicodeUmwandlung;
import de.svws_nrw.data.schild3.DataSchildVersetzungsvermerke;
import de.svws_nrw.data.schild3.reporting.DataSchildReportingDatenquelle;
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
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf allgemeine Kataloge aus der SVWS-Datenbank.
 */
@Path("/db/{schema}/schild3")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APISchild {

    /**
     * Die OpenAPI-Methode für die Abfrage des Schild3-Kataloges AbiturInfos.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return der Katalog
     */
    @GET
    @Path("/abiturinfos")
    @Operation(summary = "Die Liste der Einträge aus dem Schild-Katalog AbiturInfos.",
               description = "Die Liste der Einträge aus dem Schild-Katalog AbiturInfos. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den Schild-Katalog AbiturInfos",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Schild3KatalogEintragAbiturInfos.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogSchild3AbiturInfos(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	OpenAPIApplication.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    	return (new DataSchildAbiturInfos()).getAll();
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Schild3-Kataloges Datenarten.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return der Katalog
     */
    @GET
    @Path("/datenarten")
    @Operation(summary = "Die Liste der Einträge aus dem Schild-Katalog Datenart.",
               description = "Die Liste der Einträge aus dem Schild-Katalog Datenart. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den Schild-Katalog Datenart",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Schild3KatalogEintragDatenart.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogSchild3Datenarten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        OpenAPIApplication.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
        return (new DataSchildDatenart()).getAll();
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Schild3-Kataloges DQR-Niveaus.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return der Katalog
     */
    @GET
    @Path("/dqr_niveaus")
    @Operation(summary = "Die Liste der Einträge aus dem Schild-Katalog DQR-Niveaus.",
               description = "Die Liste der Einträge aus dem Schild-Katalog DQR-Niveaus. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den Schild-Katalog DQR-Niveaus",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Schild3KatalogEintragDQRNiveaus.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogSchild3DQRNiveaus(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        OpenAPIApplication.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
        return (new DataSchildDQRNiveaus()).getAll();
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Schild3-Kataloges zur Konfiguration des CSV-Exportes von Schild.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return der Katalog
     */
    @GET
    @Path("/export/csv")
    @Operation(summary = "Die Liste der Einträge aus dem Schild-Katalog zur Konfiguration des CSV-Exportes von Schild.",
               description = "Die Liste der Einträge aus dem Schild-Katalog zur Konfiguration des CSV-Exportes von Schild. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den Schild-Katalog zur Konfiguration des CSV-Exportes von Schild",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Schild3KatalogEintragExportCSV.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogSchild3ExportCSV(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        OpenAPIApplication.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
        return (new DataSchildExportCSV()).getAll();
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Schild3-Kataloges Filter Fehlende Einträge.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return der Katalog
     */
    @GET
    @Path("/filter/fehlende_eintraege")
    @Operation(summary = "Die Liste der Einträge aus dem Schild-Katalog Filter Fehlende Einträge.",
               description = "Die Liste der Einträge aus dem Schild-Katalog Filter Fehlende Einträge. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den Schild-Katalog Filter Fehlende Einträge",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Schild3KatalogEintragFilterFehlendeEintraege.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogSchild3FilterFehlendeEintraege(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        OpenAPIApplication.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
        return (new DataSchildFilterFehlendeEintraege()).getAll();
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Schild3-Kataloges Bundesländer/Nachbarländer.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return der Katalog
     */
    @GET
    @Path("/laender")
    @Operation(summary = "Die Liste der Einträge aus dem Schild-Katalog Bundesländer/Nachbarländer.",
               description = "Die Liste der Einträge aus dem Schild-Katalog Bundesländer/Nachbarländer. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den Schild-Katalog Bundesländer/Nachbarländer",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Schild3KatalogEintragLaender.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogSchild3Laender(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        OpenAPIApplication.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
        return (new DataSchildLaender()).getAll();
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Schild3-Kataloges Bundesländer/Nachbarländer.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return der Katalog
     */
    @GET
    @Path("/pruefungsordnungen")
    @Operation(summary = "Die Liste der Einträge aus dem Schild-Katalog Prüfungsordnungen.",
               description = "Die Liste der Einträge aus dem Schild-Katalog Prüfungsordnungen. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den Schild-Katalog Prüfungsordnungen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Schild3KatalogEintragPruefungsordnung.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogSchild3Pruefungsordnungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        OpenAPIApplication.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
        return (new DataSchildPruefungsordnung()).getAll();
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Schild3-Kataloges Prüfungsordnung-Optionen.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return der Katalog
     */
    @GET
    @Path("/pruefungsordnungen/optionen")
    @Operation(summary = "Die Liste der Einträge aus dem Schild-Katalog Prüfungsordnung-Optionen.",
               description = "Die Liste der Einträge aus dem Schild-Katalog Prüfungsordnung-Optionen. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den Schild-Katalog Prüfungsordnung-Optionen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Schild3KatalogEintragPruefungsordnungOption.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogSchild3PruefungsordnungOptionen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        OpenAPIApplication.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
        return (new DataSchildPruefungsordnungOptionen()).getAll();
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Schild3-Kataloges für die Unicode-Umwandlung.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return der Katalog
     */
    @GET
    @Path("/unicode/umwandlung")
    @Operation(summary = "Die Liste der Einträge aus dem Schild-Katalog für die Unicode-Umwandlung.",
               description = "Die Liste der Einträge aus dem Schild-Katalog für die Unicode-Umwandlung. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den Schild-Katalog für die Unicode-Umwandlung",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Schild3KatalogEintragUnicodeUmwandlung.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogSchild3UnicodeUmwandlung(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        OpenAPIApplication.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
        return (new DataSchildUnicodeUmwandlung()).getAll();
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Schild3-Kataloges Versetzungsvermerke / PrfSemAbschl.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return der Katalog
     */
    @GET
    @Path("/versetzungsvermerke")
    @Operation(summary = "Die Liste der Einträge aus dem Schild-Katalog Versetzungsvermerke / PrfSemAbschl.",
               description = "Die Liste der Einträge aus dem Schild-Katalog Versetzungsvermerke / PrfSemAbschl. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den Schild-Katalog Versetzungsvermerke / PrfSemAbschl",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Schild3KatalogEintragVersetzungsvermerke.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogSchild3Versetzungsvermerke(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        OpenAPIApplication.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
        return (new DataSchildVersetzungsvermerke()).getAll();
    }



    /**
     * Die OpenAPI-Methode für die Abfrage der im SVWS-Server definierten Schild3-Datenquellen
     * für das Reporting.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return die Definitionen der Schild3-Report-Datenquellen
     */
    @GET
    @Path("/reporting/")
    @Operation(summary = "Die Liste der Einträge im SVWS-Server definierten Schild3-Datenquellen für das Reporting.",
               description = "Die Liste der Einträge im SVWS-Server definierten Schild3-Datenquellen für das Reporting. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung für den Zugriff auf das Reporting besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen der Einträge im SVWS-Server definierten Schild3-Datenquellen für das Reporting",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Schild3KatalogEintragVersetzungsvermerke.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um auf das Reporting zuzigreifen.")
    @ApiResponse(responseCode = "404", description = "Keine Datenquellen gefunden")
    public Response getSchild3ReportingDatenquellen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.BERICHTE_STANDARDFORMULARE_DRUCKEN)) {
            return DataSchildReportingDatenquelle.getDatenquellen(conn);
        }
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Daten einer im SVWS-Server
     * definierten Schild3-Datenquelle des Reportings.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param datenquelle   der Name der Datenquelle
     * @param params        die Parameter der Datenquelle, d.h. die Werte für das Attribut der
     *                      Master-Datenquelle, welche bei dieser Datenquelle berücksichtigt
     *                      werden sollen
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return die Definitionen der Schild3-Report-Datenquellen
     */
    @POST
    @Path("/reporting/{datenquelle}")
    @Operation(summary = "Die Daten einer im SVWS-Server definierten Schild3-Datenquelle für das Reporting.",
               description = "Die Daten einer im SVWS-Server definierten Schild3-Datenquelle für das Reporting. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung für den Zugriff auf das Reporting besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Daten der im SVWS-Server definierten Schild3-Datenquelle für das Reporting",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Object.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um auf die Datenquelle zuzugreifen.")
    @ApiResponse(responseCode = "404", description = "Es wurden keine Daten gefunden oder die Datenquelle wurde nicht gefunden.")
    public Response getSchild3ReportingDaten(@PathParam("schema") final String schema,
            @PathParam("datenquelle") final String datenquelle,
            @RequestBody(description = "Eine Liste der Attribute der Masterdatenquelle. Wurde keine Masterdatenquelle angegeben, so muss die Liste leer sein.", required = true, content =
                    @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Object.class)))) final List<Object> params,
            @Context final HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.BERICHTE_STANDARDFORMULARE_DRUCKEN)) {
            return DataSchildReportingDatenquelle.getDaten(conn, datenquelle, params);
        }
    }

}
