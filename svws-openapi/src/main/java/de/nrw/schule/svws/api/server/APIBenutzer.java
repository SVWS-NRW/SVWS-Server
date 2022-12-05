package de.nrw.schule.svws.api.server;

import java.io.InputStream;
import java.util.List;

import de.nrw.schule.svws.api.JSONMapper;
import de.nrw.schule.svws.api.OpenAPIApplication;
import de.nrw.schule.svws.core.data.benutzer.BenutzerDaten;
import de.nrw.schule.svws.core.data.benutzer.BenutzerKompetenzGruppenKatalogEintrag;
import de.nrw.schule.svws.core.data.benutzer.BenutzerKompetenzKatalogEintrag;
import de.nrw.schule.svws.core.data.benutzer.BenutzerListeEintrag;
import de.nrw.schule.svws.core.data.benutzer.BenutzergruppeDaten;
import de.nrw.schule.svws.core.data.benutzer.BenutzergruppeListeEintrag;
import de.nrw.schule.svws.core.data.gost.GostBlockungSchiene;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.data.benutzer.DataBenutzerDaten;
import de.nrw.schule.svws.data.benutzer.DataBenutzergruppeDaten;
import de.nrw.schule.svws.data.benutzer.DataBenutzergruppeliste;
import de.nrw.schule.svws.data.benutzer.DataBenutzerkompetenzGruppenliste;
import de.nrw.schule.svws.data.benutzer.DataBenutzerkompetenzliste;
import de.nrw.schule.svws.data.benutzer.DataBenutzerliste;
import de.nrw.schule.svws.db.DBEntityManager;
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
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die
 * angelegten Benutzer und die den Benutzern
 * zugeordneten Kompetenezen.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/benutzer/...
 */
@Path("/db/{schema}/benutzer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIBenutzer {

    /**
     * Die OpenAPI-Methode für die Abfrage der Liste aller im System vorhandenen
     * Benutzer.
     * 
     * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden
     *                soll
     * @param request die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste mit den Benutzern
     */
    @GET
    @Path("/")
    @Operation(summary = "Gibt eine sortierte Übersicht von allen Benutzern zurück.", description = "Erstellt eine Liste aller Benutzer."
            + "Es wird geprüft, ob der SVWS-Benutzer die notwendige Administrator-Berechtigung besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Benutzer-Einträgen", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BenutzerListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Benutzerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Benutzer-Einträge gefunden")
    public Response getBenutzerliste(@PathParam("schema") String schema, @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADMIN)) {
            return (new DataBenutzerliste(conn).getList());
        }
    }

    /**
     * Die OpenAPI-Methode für die Abfrage der Daten eines Benutzers.
     * 
     * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden
     *                soll
     * @param id      die Datenbank-ID zur Identifikation des Benutzers
     * @param request die Informationen zur HTTP-Anfrage
     * 
     * @return die Daten des Benutzers
     */
    @GET
    @Path("/{id : \\d+}")
    @Operation(summary = "Liefert zu der ID des Benutzers die zugehörigen Daten.", description = "Liest die Daten des Benutzers zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Benutzerdaten "
            + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Daten des Benutzers", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BenutzerDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Benutzerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Benutzer-Eintrag mit der angegebenen ID gefunden")
    public Response getBenutzerDaten(@PathParam("schema") String schema, @PathParam("id") long id,
            @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnectionAllowSelf(request, BenutzerKompetenz.ADMIN, id)) {
            return (new DataBenutzerDaten(conn).get(id));
        }
    }

    /**
     * Die OpenAPI-Methode für die Abfrage der Liste aller im System vorhandenen
     * Benutzergruppen.
     * 
     * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden
     *                soll
     * @param request die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste mit den Benutzergruppen
     */
    @GET
    @Path("/gruppe")
    @Operation(summary = "Gibt eine sortierte Übersicht von allen Benutzergruppen zurück.", description = "Erstellt eine Liste aller Benutzergruppen."
            + "Es wird geprüft, ob der SVWS-Benutzer die notwendige Administrator-Berechtigung besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Benutzergruppen-Einträgen", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BenutzergruppeListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Benutzergruppendaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Benutzergruppen-Einträge gefunden")
    public Response getBenutzergruppenliste(@PathParam("schema") String schema, @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADMIN)) {
            return (new DataBenutzergruppeliste(conn).getList());
        }
    }

    /**
     * Die OpenAPI-Methode für die Abfrage der Daten einer Benutzergruppe.
     * 
     * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden
     *                soll
     * @param id      die Datenbank-ID zur Identifikation der Benutzergruppe
     * @param request die Informationen zur HTTP-Anfrage
     * 
     * @return die Daten der Benutzergruppe
     */
    @GET
    @Path("/gruppe/{id : \\d+}")
    @Operation(summary = "Liefert zu der ID der Benutzergruppe die zugehörigen Daten.", description = "Liest die Daten der Benutzergruppe zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Benutzergruppendaten "
            + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Daten der Benutzergruppe", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BenutzergruppeDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Benutzergruppendaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Benutzergruppen-Eintrag mit der angegebenen ID gefunden")
    public Response getBenutzergruppeDaten(@PathParam("schema") String schema, @PathParam("id") long id,
            @Context HttpServletRequest request) {
       try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADMIN)) {
            return (new DataBenutzergruppeDaten(conn).get(id));
        }
    }
    
    
    /**
     * Die OpenAPI-Methode für die Abfrage der Benutzer einer Gruppe.
     * 
     * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden
     *                soll
     * @param id      die Datenbank-ID zur Identifikation der Benutzergruppe
     * @param request die Informationen zur HTTP-Anfrage
     * 
     * @return die Benutzer der Benutzergruppe
     */
    @GET
    @Path("/gruppe/{id : \\d+}/benutzer")
    @Operation(summary = "Liefert zu der ID der Benutzergruppe die zugehörigen Benutzer.", description = "Liest die Benutzer der Benutzergruppe zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Benutzergruppendaten "
            + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Benutzer der Benutzergruppe", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BenutzerListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Benutzergruppendaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Benutzergruppen-Eintrag mit der angegebenen ID gefunden")
    public Response getBenutzerMitGruppenID(@PathParam("schema") String schema, @PathParam("id") long id,
            @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADMIN)) {
            return (new DataBenutzerliste(conn).getListMitGruppenID(id));
        }
    }
    
    
    /**
     * Die OpenAPI-Methode für Setzen eines Anzeigenamens.
     * 
     * @param schema  das Datenbankschema, in welchem der Benutzer ist.
     * @param id      die ID des Benutzers
     * @param is      der Input-Stream mit dem Azeigenamen
     * @param request die Informationen zur HTTP-Anfrage
     * 
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/{id : \\d+}/anzeigename")
    @Operation(summary = "Setzt den Anzeigenamen eines Benutzers.", description = "Setzt den Anzeigenamen eines Benutzers."
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Setzen des Anzeigenamens besitzt.")
    @ApiResponse(responseCode = "204", description = "Der Anzeigename wurde erfolgreich gesetzt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Anzeigenamen zu setzen.")
    @ApiResponse(responseCode = "404", description = "Der Anzeigename zu dem Benutzer sind nicht vorhanden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response setAnzeigename(
            @PathParam("schema") String schema, @PathParam("id") long id,
            @RequestBody(description = "Der Anzeigename", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class))) InputStream is,
            @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnectionAllowSelf(request, BenutzerKompetenz.ADMIN, id)) {
            return (new DataBenutzerDaten(conn)).setAnzeigename(id, JSONMapper.toString(is));
        }
    }
    
    
    /**
     * Die OpenAPI-Methode für Setzen eines Anmeldenamens.
     * 
     * @param schema  das Datenbankschema, in welchem der Benutzer ist.
     * @param id      die ID des Benutzers
     * @param is      der Input-Stream mit dem Anmeldenamen
     * @param request die Informationen zur HTTP-Anfrage
     * 
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/{id : \\d+}/anmeldename")
    @Operation(summary = "Setzt den Anmeldenamen eines Benutzers.", description = "Setzt den Anmeldenamen eines Benutzers."
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Setzen des Kennwortes besitzt.")
    @ApiResponse(responseCode = "204", description = "Der Anmeldename wurde erfolgreich gesetzt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um das Kennwort zu setzen.")
    @ApiResponse(responseCode = "404", description = "Der Anmeldename zu dem Benutzer sind nicht vorhanden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response setAnmeldename(
            @PathParam("schema") String schema, @PathParam("id") long id,
            @RequestBody(description = "Der Anmeldename", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class))) InputStream is,
            @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnectionAllowSelf(request, BenutzerKompetenz.ADMIN, id)) {
            return (new DataBenutzerDaten(conn)).setAnmeldename(id, JSONMapper.toString(is));
        }
    }
    
    /**
     * Die OpenAPI-Methode für Setzen des Admin-Flags bei einem Benutzer.
     * 
     * @param schema    das Datenbankschema
     * @param id        die ID des Benutzers
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * 
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/{id : \\d+}/addAdmin")
    @Operation(summary = "Setzt Admin-Berechtiung für den Benutzer", 
                          description = "Setzt Admin-Berechtigung für den Benutzer."
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Information wurde erfolgreich gesetzt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Benutzer als administrativer Benutzer zu setzen")
    @ApiResponse(responseCode = "404", description = "Der Benutzer ist nicht vorhanden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addBenutzerAdmin(
            @PathParam("schema") String schema, @PathParam("id") long id,
            @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADMIN)) {
            return (new DataBenutzerDaten(conn)).addAdmin(id);
        }
    }
    
    /**
     * Die OpenAPI-Methode für das Löschen der Admin-Berechtiung eines Benutzers
     *  
     * @param schema       das Datenbankschema
     * @param id   die ID des Benutzers
     * @param request      die Informationen zur HTTP-Anfrage
     * 
     * @return die HTTP-Antwort
     */
    @DELETE
    @Path("/{id : \\d+}/removeAdmin")
    @Operation(summary = "Entfernt die Admin-Berechtigung des Benutzers mit der id",
    description = "Entfernt die Admin-Berechtigung des Benutzers mit der id."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen  der Admin-Berechtigung hat.")
    @ApiResponse(responseCode = "200", description = "Die Admin-Berechtigung wurde erfolgreich entfernt.",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostBlockungSchiene.class)))    
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Admin-Berechtigung zu entfernen.")
    @ApiResponse(responseCode = "404", description = "Der Benutzer ist nicht vorhanden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response removeBenutzerAdmin(
    		@PathParam("schema") String schema, @PathParam("id") long id, 
    		@Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADMIN)) {
    		return (new DataBenutzerDaten(conn)).removeAdmin(id);
    	}
    }

    /**
     * Die OpenAPI-Methode zum Hinzufügen von einer oder mehreren Kompetenzen bei einem Benutzer
     * 
     * @param schema    das Datenbankschema, in welchem die Blockung erstellt wird
     * @param id        die ID des Benutzers
     * @param kids      die IDs der Kompetenzen
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/{id : \\d+}/kompetenz/add")
    @Operation(summary = "Fügt Kompetenzen bei einem Benutzer hinzu.", description = "Fügt Kompetenzen bei einem Benutzer hinzu."
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Setzen der Kompetenzen besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Kompetenzen wurden erfolgreich hinzugefügt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kompetenzen zu hinzuzufügen.")
    @ApiResponse(responseCode = "404", description = "Benötigte Information zum Benutzer wurden nicht in der DB gefunden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addBenutzerKompetenzen(
            @PathParam("schema") String schema, @PathParam("id") long id,
            @RequestBody(description = "Die Kompetenzen", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class)))) List<Long> kids,
            @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnectionAllowSelf(request, BenutzerKompetenz.ADMIN, id)) {
            return (new DataBenutzerDaten(conn)).addKompetenzen(id, kids);
        }
    }


    /**
     * Die OpenAPI-Methode zum Entfernen von einer oder mehreren Kompetenzen bei einem Benutzer
     * 
     * @param schema   das Datenbankschema, in welchem die Blockung erstellt wird
     * @param id       die ID des Benutzers
     * @param kids     die IDs der Kompetenzen
     * @param request  die Informationen zur HTTP-Anfrage
     * 
     * @return die HTTP-Antwort
     */
    @DELETE
    @Path("/{id : \\d+}/kompetenz/remove")
    @Operation(summary = "Entfernt Kompetenzen bei einem Benutzer.", description = "Entfernt Kompetenzen bei einem Benutzer."
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen der Kompetenzen besitzt.")
    
    @ApiResponse(responseCode = "200", description = "Die Kompetenzen wurden erfolgreich entfernt.",
    			 content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)))  
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kompetenzen zu entfernen.")
    @ApiResponse(responseCode = "404", description = "Benötigte Information zum Benutzer wurden nicht in der DB gefunden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response removeBenutzerKompetenzen(
            @PathParam("schema") String schema, @PathParam("id") long id,
            @RequestBody(description = "Die Kompetenzen", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class)))) List<Long> kids,
            @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnectionAllowSelf(request, BenutzerKompetenz.ADMIN, id)) {
            return (new DataBenutzerDaten(conn)).removeKompetenzen(id, kids);
        }
    }

    
    /**
     * Die OpenAPI-Methode für Setzen eines Benutzerkennwortes.
     * 
     * @param schema  das Datenbankschema, in welchem die Blockung erstellt wird
     * @param id      die ID des Benutzers
     * @param is      der Input-Stream mit dem Kennwort
     * @param request die Informationen zur HTTP-Anfrage
     * 
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/{id : \\d+}/password")
    @Operation(summary = "Setzt das Kennwort eines Benutzers.", description = "Setzt das Kennwort eines Benutzers."
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Setzen des Kennwortes besitzt.")
    @ApiResponse(responseCode = "204", description = "Das Kennwort wurde erfolgreich gesetzt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um das Kennwort zu setzen.")
    @ApiResponse(responseCode = "404", description = "Die Kennwortinformationen zu dem Benutzer sind nicht vorhanden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response setBenutzerPasswort(
            @PathParam("schema") String schema, @PathParam("id") long id,
            @RequestBody(description = "Das Kennwort", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class))) InputStream is,
            @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnectionAllowSelf(request, BenutzerKompetenz.ADMIN, id)) {
            return (new DataBenutzerDaten(conn)).setPassword(id, JSONMapper.toString(is));
        }
    }
    
    
    
    
    /**
     * Die OpenAPI-Methode für die Abfrage des Katalogs der Benutzerkompetenzen.
     * 
     * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden
     *                soll
     * @param request die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste der Benutzerkompetenzen
     */
    @GET
    @Path("/allgemein/kompetenzen")
    @Operation(summary = "Liefert den Katalog der Benutzerkompetenzen.", description = "Liefert den Katalog der Benutzerkompetenzen. "
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Kataloge besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Katalog der Benutzerkompetenzen", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BenutzerKompetenzKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.")
    public Response getKatalogBenutzerkompetenzen(@PathParam("schema") String schema,
            @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KEINE)) {
            return (new DataBenutzerkompetenzliste().getList());
        }
    }
    
    /**
     * Die OpenAPI-Methode für die Abfrage des Katalogs der
     * Benutzerkompetenzgruppen.
     * 
     * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden
     *                soll
     * @param request die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste der Benutzerkompetenzgruppen
     */
    @GET
    @Path("/allgemein/kompetenzgruppen")
    @Operation(summary = "Liefert den Katalog der Benutzerkompetenzgruppen.", description = "Liefert den Katalog der Benutzerkompetenzgruppen. "
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Kataloge besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Katalog der Benutzerkompetenzgruppen", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BenutzerKompetenzGruppenKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.")
    public Response getKatalogBenutzerkompetenzgruppen(@PathParam("schema") String schema,
            @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KEINE)) {
            return (new DataBenutzerkompetenzGruppenliste().getList());
        }
    }
    
    
    /**
     * Die OpenAPI-Methode für das Setzen der Bezeichnung einer Benutzergruppen.
     * 
     * @param schema   das Datenbankschema
     * @param id       die ID der Benutzergruppe
     * @param is       der Input-Stream mit der Bezeichnung der Benutzergruppe
     * @param request  die Informationen zur HTTP-Anfrage
     * 
     * 
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/gruppe/{id : \\d+}/bezeichnung")
    @Operation(summary = "Setzt die Bezeichnung der Benutzergruppe.", 
                          description = "Setzt die Bezeichnung der Benutzergruppe."
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Information wurde erfolgreich gesetzt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Bezeichnung der Gruppe zu setzen")
    @ApiResponse(responseCode = "404", description = "Die Benutzergruppe ist nicht vorhanden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response setBenutzergruppeBezeichnung(
            @PathParam("schema") String schema, @PathParam("id") long id,
            @RequestBody(description = "Die Bezeichnung der Benutzergruppe.", required = true, 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class))) InputStream is,
            @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADMIN)) {
            return (new DataBenutzergruppeDaten(conn)).setBezeichnung(id, JSONMapper.toString(is));
        }
    }


    /**
     * Die OpenAPI-Methode für Setzen des Admin-Flags bei einer Benutzergruppen.
     * 
     * @param schema    das Datenbankschema
     * @param id        die ID der Benutzergruppe
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * 
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/gruppe/{id : \\d+}/addAdmin")
    @Operation(summary = "Setzt ob die Benutzergruppe eine administrative Benutzergruppe ist oder nicht.", 
                          description = "Setzt ob die Benutzergruppe eine administrative Benutzergruppe ist oder nicht."
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Information wurde erfolgreich gesetzt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Gruppe als administrative Gruppe zu setzen")
    @ApiResponse(responseCode = "404", description = "Die Benutzergruppe ist nicht vorhanden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addBenutzergruppeAdmin(
            @PathParam("schema") String schema, @PathParam("id") long id,
           @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADMIN)) {
            return (new DataBenutzergruppeDaten(conn)).addAdmin(id);
        }
    }

    
    /**
     * Die OpenAPI-Methode für das Löschen der Admin-Berechtiung einer Benutzergruppe
     *  
     * @param schema       das Datenbankschema
     * @param id   die ID der Benutzergruppe
     * @param request      die Informationen zur HTTP-Anfrage
     * 
     * @return die HTTP-Antwort
     */
    @DELETE
    @Path("/gruppe/{id : \\d+}/removeAdmin")
    @Operation(summary = "Entfernt die Admin-Berechtigung er Benutzergruppe mit der id",
    description = "Entfernt die Admin-Berechtigung er Benutzergruppe mit der id"
                + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen  der Admin-Berechtigung hat.")
    @ApiResponse(responseCode = "204", description = "Die Information wurde erfolgreich gesetzt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Admin-Berechtigung zu entfernen.")
    @ApiResponse(responseCode = "404", description = "Die Benutzergruppe ist nicht vorhanden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response removeBenutzergruppeAdmin(
            @PathParam("schema") String schema, @PathParam("id") long id, 
            @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADMIN)) {
            return (new DataBenutzergruppeDaten(conn)).removeAdmin(id);
        }
    }

    /**
     * Die OpenAPI-Methode zum Hinzufügen von einer oder mehreren Kompetenzen bei einer Benutzergruppe
     * 
     * @param schema    das Datenbankschema
     * @param id        die ID der Benutzergruppe
     * @param kids      die IDs der Kompetenzen
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/gruppe/{id : \\d+}/kompetenz/add")
    @Operation(summary = "Fügt Kompetenzen bei einer Benutzergruppe hinzu.", description = "Fügt Kompetenzen bei einer Benutzergruppe hinzu."
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Setzen der Kompetenzen besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Kompetenzen wurden erfolgreich hinzugefügt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kompetenzen zu hinzuzufügen.")
    @ApiResponse(responseCode = "404", description = "Benötigte Information zum Benutzer wurden nicht in der DB gefunden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addBenutzergruppeKompetenzen(
            @PathParam("schema") String schema, @PathParam("id") long id,
            @RequestBody(description = "Die Kompetenzen", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class)))) List<Long> kids,
            @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADMIN)) {
            return (new DataBenutzergruppeDaten(conn)).addKompetenzen(id, kids);
        }
    }
    
    /**
     * Die OpenAPI-Methode zum Entfernen von einer oder mehreren Kompetenzen bei einer Benutzergruppe
     * 
     * @param schema    das Datenbankschema
     * @param id        die ID der Benutzergruppe
     * @param kids      die IDs der Kompetenzen
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die HTTP-Antwort
     */
    @DELETE
    @Path("/gruppe/{id : \\d+}/kompetenz/remove")
    @Operation(summary = "Entfernt Kompetenzen bei einer Benutzergruppe.", description = "Entfernt Kompetenzen bei einer Benutzergruppe hinzu."
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entferen der Kompetenzen besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Kompetenzen wurden erfolgreich hinzugefügt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kompetenzen zu entfernen.")
    @ApiResponse(responseCode = "404", description = "Benötigte Information zum Benutzer wurden nicht in der DB gefunden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response removeBenutzergruppeKompetenzen(
            @PathParam("schema") String schema, @PathParam("id") long id,
            @RequestBody(description = "Die Kompetenzen", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class)))) List<Long> kids,
            @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADMIN)) {
            return (new DataBenutzergruppeDaten(conn)).removeKompetenzen(id, kids);
        }
    }
    
    /**
     * Die OpenAPI-Methode zum Hinzufügen von einen oder mehreren Benutzern bei einer Benutzergruppe
     * 
     * @param schema    das Datenbankschema
     * @param id        die ID der Benutzergruppe
     * @param bids      die IDs der Benutzer
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/gruppe/{id : \\d+}/benutzer/add")
    @Operation(summary = "Fügt Benutzer bei einer Benutzergruppe hinzu.", description = "Fügt Benutzer bei einer Benutzergruppe hinzu."
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Hinzufügen der Benutzer besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Benutzer wurden erfolgreich hinzugefügt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um neue Benutzer hinzuzufügen.")
    @ApiResponse(responseCode = "404", description = "Benötigte Information zum Benutzer wurden nicht in der DB gefunden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addBenutzergruppeBenutzer(
            @PathParam("schema") String schema, @PathParam("id") long id,
            @RequestBody(description = "Die Benutzer", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class)))) List<Long> bids,
            @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADMIN)) {
            return (new DataBenutzergruppeDaten(conn)).addBenutzer(id, bids);
        }
    }

    /**
     * Die OpenAPI-Methode zum Entfernen von einem oder mehreren Benutzern bei einer Benutzergruppe
     * 
     * @param schema    das Datenbankschema
     * @param id        die ID der Benutzergruppe
     * @param bids      die IDs der Benutzer
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die HTTP-Antwort
     */
    @DELETE
    @Path("/gruppe/{id : \\d+}/benutzer/remove")
    @Operation(summary = "Entfernt Benutzer bei einer Benutzergruppe hinzu.", description = "Entfernt Benutzer bei einer Benutzergruppe hinzu."
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen der Benutzer besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Benutzer wurden erfolgreich hinzugefügt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um neue Benutzer zu entfernen.")
    @ApiResponse(responseCode = "404", description = "Benötigte Information zum Benutzer wurden nicht in der DB gefunden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response removeBenutzergruppeBenutzer(
            @PathParam("schema") String schema, @PathParam("id") long id,
            @RequestBody(description = "Die Benutzer", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class)))) List<Long> bids,
            @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADMIN)) {
            return (new DataBenutzergruppeDaten(conn)).removeBenutzer(id, bids);
        }
    }

    // TODO Methode setBenutzergruppeKompetenz aufteilen (siehe bei Benutzer) in zwei API-Methoden: addBenutzergruppeKompetenz (POST) und removeBenutzergruppeKompetenz (DELETE)

}
