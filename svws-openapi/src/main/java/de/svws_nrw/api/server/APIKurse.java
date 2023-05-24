package de.svws_nrw.api.server;

import de.svws_nrw.api.OpenAPIApplication;
import de.svws_nrw.core.data.kurse.KursDaten;
import de.svws_nrw.core.data.kurse.KursListeEintrag;
import de.svws_nrw.core.data.kurse.KursartKatalogEintrag;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.kurse.DataKatalogKursarten;
import de.svws_nrw.data.kurse.DataKursdaten;
import de.svws_nrw.data.kurse.DataKursliste;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Daten von Kursen aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/api/kurse/...
 */
@Path("/db/{schema}/kurse")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIKurse {


    /**
     * Die OpenAPI-Methode für die Abfrage der Liste der Kurse aller Schuljahresabschniite
     * im angegebenen Schema.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return die Liste der Kurse aller Schuljahresabschniite
     */
    @GET
    @Path("/")
    @Operation(summary = "Gibt eine Übersicht von allen Kursen zurück.",
               description = "Erstellt eine Liste aller in der Datenbank vorhanden Kurse unter Angabe der ID, des Kürzels, "
               		       + "der Parallelität, der Kürzel des Klassenlehrers und des zweiten Klassenlehrers, "
               		       + "einer Sortierreihenfolge und ob sie in der Anwendung sichtbar sein sollen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Klassendaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Kurs-Listen-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KursListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Kursdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Kurs-Einträge gefunden")
    public Response getKurse(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KEINE)) {
    		return (new DataKursliste(conn, null)).getList();
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Liste der Kurse im angegebenen Schema.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abschnitt     die ID des Schuljahresabschnitts
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Liste der Kurse mit der jeweiligen ID im Datenbankschema
     */
    @GET
    @Path("/abschnitt/{abschnitt : \\d+}")
    @Operation(summary = "Gibt eine Übersicht von allen Kursen eines Schuljahresabschnittes zurück.",
    description = "Erstellt eine Liste aller in der Datenbank vorhanden Kurse eines Schuljahresabschnittes unter "
    		       + "Angabe der ID, des Kürzels, der Parallelität, der Kürzel des Klassenlehrers und des zweiten "
    		       + "Klassenlehrers, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar sein sollen. "
    		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Klassendaten "
    		       + "besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Kurs-Listen-Einträgen",
	      content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KursListeEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Kursdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Kurs-Einträge gefunden")
    public Response getKurseFuerAbschnitt(@PathParam("schema") final String schema, @PathParam("abschnitt") final long abschnitt, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KEINE)) {
    		return (new DataKursliste(conn, abschnitt)).getList();
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Daten eines Kurses.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation des Kurses
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die Daten des Kurses
     */
    @GET
    @Path("/{id : \\d+}")
    @Operation(summary = "Liefert zu der ID des Kurses die zugehörigen Daten.",
    description = "Liest die Daten des Kurses zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Kursdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Daten des Kurses",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = KursDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Kursdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Kurs-Eintrag mit der angegebenen ID gefunden")
    public Response getKurs(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		                                    @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KEINE)) {
    		return (new DataKursdaten(conn)).get(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Katalogs der gültigen Kursarten.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              der Katalog der gültigen Kursarten
     */
    @GET
    @Path("/allgemein/kursarten")
    @Operation(summary = "Gibt den Katalog der gültigen Kursarten zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden gültigen Kursarten. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Kursart-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KursartKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Kursart-Katalog-Einträge gefunden")
    public Response getKatalogKursarten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KEINE);
        return (new DataKatalogKursarten()).getAll();
    }

}
