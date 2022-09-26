package de.nrw.schule.svws.api.server;

import java.io.InputStream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import de.nrw.schule.svws.api.OpenAPIApplication;
import de.nrw.schule.svws.core.data.fach.FachKatalogEintrag;
import de.nrw.schule.svws.core.data.jahrgang.JahrgangsKatalogEintrag;
import de.nrw.schule.svws.core.data.kataloge.KatalogEintrag;
import de.nrw.schule.svws.core.data.kataloge.KatalogEintragStrassen;
import de.nrw.schule.svws.core.data.kataloge.OrtKatalogEintrag;
import de.nrw.schule.svws.core.data.kataloge.OrtsteilKatalogEintrag;
import de.nrw.schule.svws.core.data.klassen.KlassenartKatalogEintrag;
import de.nrw.schule.svws.core.data.kurse.KursartKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.OrganisationsformKatalogEintrag;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.data.betriebe.DataKatalogBeschaeftigunsarten;
import de.nrw.schule.svws.data.betriebe.DataKatalogBetriebsarten;
import de.nrw.schule.svws.data.faecher.DataKatalogZulaessigeFaecher;
import de.nrw.schule.svws.data.kataloge.DataHaltestellen;
import de.nrw.schule.svws.data.kataloge.DataKatalogJahrgaenge;
import de.nrw.schule.svws.data.kataloge.DataKatalogKlassenarten;
import de.nrw.schule.svws.data.kataloge.DataKatalogKursarten;
import de.nrw.schule.svws.data.kataloge.DataKatalogOrganisationsformen;
import de.nrw.schule.svws.data.kataloge.DataOrte;
import de.nrw.schule.svws.data.kataloge.DataOrtsteile;
import de.nrw.schule.svws.data.kataloge.DataStrassen;
import de.nrw.schule.svws.db.Benutzer;
import de.nrw.schule.svws.db.DBEntityManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf allgemeine Kataloge aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/api/kataloge/...
 */
@Path("/db/{schema}/kataloge")
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
    @Path("/strassen")
    @Operation(summary = "Erstellt eine Liste aller in dem Katalog vorhandenen Strassen.",
               description = "Erstellt eine Liste aller in dem Katalog vorhandenen Strassen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Straßen-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KatalogEintragStrassen.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Straßen-Katalog-Einträge gefunden")
    public Response getKatalogStrassen(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataStrassen(null)).getAll();
    	}
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
    public Response getKatalogOrte(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
        	return (new DataOrte(conn)).getAll();
    	}
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
    public Response getKatalogOrtsteile(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
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
    public Response getKatalogHaltestellen(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataHaltestellen(conn)).getList();
    	}
    }

    /**
     * Die OpenAPI-Methode für die Abfrage der Liste der Beschäftigungsarten im angegebenen Schema.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return              die Liste der Beschäftigungsarten mit ID des Datenbankschemas
     */
    @GET
    @Path("/beschaeftigungsart")
    @Operation(summary = "Gibt eine Übersicht aller Beschäftigungsarten im Katalog zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhandenen Beschäftigungsarten unter Angabe der ID, eines Kürzels und der "
               		       + "textuellen Beschreibung sowie der Information, ob der Eintrag in der Anwendung sichtbar bzw. änderbar sein soll, und"
               		       + "gibt diese zurück. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen zu den Beschäftigungsarten.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogBeschaeftigungsart(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
	    	return (new DataKatalogBeschaeftigunsarten(conn)).getList();
	    }
    }

    /**
     * Die OpenAPI-Methode für die Abfrage einer Beschäftigungsart im angegebenen Schema.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * @param id            die Datenbank-ID zur Identifikation der Beschäftigungsart 
     * @return              die Liste der Beschäftigungsarten mit ID des Datenbankschemas
     */
    @GET
    @Path("/beschaeftigungsart/{id : \\d+}")
    @Operation(summary = "Liefert zu der ID der Beshäftigungsart die zugehörigen Daten..",
               description = "Liest die Daten der Beschäftigunsart zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
                            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Beschäftigungsart"
                            + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Katalog-Eintrag zu den Beschäftigungsarten.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogBeschaeftigungsartmitID(@PathParam("schema") String schema, @PathParam("id") long id, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
	    	return (new DataKatalogBeschaeftigunsarten(conn)).get(id);
	    }
    }

     /**
     * Die OpenAPI-Methode für das Patchen einer Beschäftigungsart im angegebenen Schema
     *  
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation der Beschäftigungsart
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386 
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/beschaeftigungsart/{id : \\d+}")
    @Operation(summary = "Passt die zu der ID der Beschäftigungsart zugehörigen Stammdaten an.",
    description = "Passt die Beschäftigungsart-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Beschäftigungsart "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Beschäftigungsart-Daten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Beschäftigungsart-Daten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Keine Beschäftigungsart mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    
    public Response patchBeschaeftigungsart(@PathParam("schema") String schema, @PathParam("id") long id,
                                        @RequestBody(description = "Der Patch für die Betrieb-Stammdaten", required = true,
                                        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema=@Schema(implementation = KatalogEintrag.class)))
                                        InputStream is, @Context HttpServletRequest request){
        try(DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN)) {
            return (new DataKatalogBeschaeftigunsarten(conn)).patch(id, is);
        }
    }

	/**
     * Die OpenAPI-Methode für die Abfrage der Liste der Beschäftigungsarten im angegebenen Schema.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return              die Liste der Beschäftigungsarten mit ID des Datenbankschemas
     */
    @GET
    @Path("/betriebsart")
    @Operation(summary = "Gibt eine Übersicht aller Betriebsarten im Katalog zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhandenen Betriebsarten unter Angabe der ID, eines Kürzels und der "
               		       + "textuellen Beschreibung sowie der Information, ob der Eintrag in der Anwendung sichtbar bzw. änderbar sein soll, und"
               		       + "gibt diese zurück. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen zu den Betriebsarten.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogBetriebsart(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
            return (new DataKatalogBetriebsarten(conn)).getList();
    	}
    }
    
    /**
     * Die OpenAPI-Methode für die Abfrage einer Betriebsart im angegebenen Schema.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * @param id        die Datenbank-ID zur Identifikation der Betriebsart 
     * @return              die Betriebsart mit ID des Datenbankschemas
     */
    @GET
    @Path("/beschaeftigungsart/{id : \\d+}")
    @Operation(summary = "Liefert zu der ID der Betriebsart die zugehörigen Daten..",
               description = "Liest die Daten der Betriebsart zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
                            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Betriebsarten"
                            + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Katalog-Eintrag zu den Betriebsarten.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogBetriebsartmitID(@PathParam("schema") String schema, @PathParam("id") long id, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
	    	return (new DataKatalogBetriebsarten(conn)).get(id);
	    }
    }

    /**
     * Die OpenAPI-Methode für das Patchen einer Betriebsart im angegebenen Schema
     *  
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation der Betriebsart
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386 
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/betriebsart/{id : \\d+}")
    @Operation(summary = "Passt die zu der ID der Betriebsart zugehörigen Stammdaten an.",
    description = "Passt die Beschäftigungsart-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Betriebssart "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Beschäftigungsart-Daten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Beschäftigungsart-Daten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Keine Beschäftigungsart mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    
    public Response patchBetriebsart(@PathParam("schema") String schema, @PathParam("id") long id,
                                        @RequestBody(description = "Der Patch für die Betrieb-Stammdaten", required = true,
                                        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema=@Schema(implementation = KatalogEintrag.class)))
                                        InputStream is, @Context HttpServletRequest request){
        try(DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN)) {
            return (new DataKatalogBetriebsarten(conn)).patch(id, is);
        }
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
    @Path("/jahrgaenge")
    @Operation(summary = "Gibt den Katalog der in den einzelnen Schulformen gültigen Jahrgänge zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden in den einzelnen Schulformen gültigen Jahrgänge. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Jahrgangs-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = JahrgangsKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Jahrgangs-Katalog-Einträge gefunden")
    public Response getKatalogJahrgaenge(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogJahrgaenge(null)).getAll();
    	}
    }  


    /**
     * Die OpenAPI-Methode für die Abfrage des Katalogs der gültigen Klassenarten.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return              der Katalog der gültigen Klassenarten
     */
    @GET
    @Path("/klassenarten")
    @Operation(summary = "Gibt den Katalog der gültigen Klassenarten zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden gültigen Klassenarten. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Klassenart-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KlassenartKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Klassenart-Katalog-Einträge gefunden")
    public Response getKatalogKlassenarten(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogKlassenarten()).getAll();
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
    @Path("/kursarten")
    @Operation(summary = "Gibt den Katalog der gültigen Kursarten zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden gültigen Kursarten. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Kursart-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KursartKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Kursart-Katalog-Einträge gefunden")
    public Response getKatalogKursarten(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogKursarten()).getAll();
    	}
    }  


    /**
     * Die OpenAPI-Methode für die Abfrage des Katalogs der zulässigen Fächer.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return              der Katalog der zulässigen Fächer
     */
    @GET
    @Path("/faecher")
    @Operation(summary = "Gibt den Katalog der zulässigen Fächer zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden zulässigen Fächer. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Fächer-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FachKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Fach-Katalog-Einträge gefunden")
    public Response getKatalogFaecher(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogZulaessigeFaecher(null)).getAll();
    	}
    }  

    
    /**
     * Die OpenAPI-Methode für die Abfrage des Katalogs der gültigen Organisationsformen.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return              der Katalog der gültigen Organisationsformen
     */
    @GET
    @Path("/organisationsformen")
    @Operation(summary = "Gibt den Katalog der gültigen Organisationsformen zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden gültigen Organisationsformen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Organisationsform-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrganisationsformKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogOrganisationsformen(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogOrganisationsformen()).getAll();
    	}
    }  

}
