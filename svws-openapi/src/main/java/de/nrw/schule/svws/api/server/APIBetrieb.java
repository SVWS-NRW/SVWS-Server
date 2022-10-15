package de.nrw.schule.svws.api.server;

import java.io.InputStream;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import de.nrw.schule.svws.api.OpenAPIApplication;
import de.nrw.schule.svws.core.data.betrieb.BetriebAnsprechpartner;
import de.nrw.schule.svws.core.data.betrieb.BetriebListeEintrag;
import de.nrw.schule.svws.core.data.betrieb.BetriebStammdaten;
import de.nrw.schule.svws.core.data.kataloge.KatalogEintrag;
import de.nrw.schule.svws.core.data.schueler.SchuelerBetriebsdaten;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.data.betriebe.DataBetriebAnsprechpartner;
import de.nrw.schule.svws.data.betriebe.DataBetriebsStammdaten;
import de.nrw.schule.svws.data.betriebe.DataBetriebsliste;
import de.nrw.schule.svws.data.betriebe.DataKatalogBeschaeftigunsarten;
import de.nrw.schule.svws.data.betriebe.DataKatalogBetriebsarten;
import de.nrw.schule.svws.data.schueler.DataSchuelerBetriebsdaten;
import de.nrw.schule.svws.db.DBEntityManager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Betriebsdaten aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/betrieb/...
 */
@Path("/db/{schema}/betriebe")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")	
public class APIBetrieb {
    
    /**
     * Die OpenAPI-Methode für die Abfrage der Liste aller Betriebe.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste mit den einzelnen Betrieben
     */
    @GET
    @Path("/")
    @Operation(summary = "Gibt eine Übersicht von allen Betrieben zurück.",
               description = "Erstellt eine Liste aller in der Datenbank vorhandenen Betriebe unter Angabe der ID, der Betriebsart , " // TOD0 Beschreibung anpassen.
               		       + "des Betriebnamens, Kontaktdaten, ob sie in der Anwendung "
               		       + "sichtbar bzw. änderbar sein sollen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Betriebsdaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Betrieb-Listen-Einträgen",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = BetriebListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Betriebdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Betrieb-Einträge gefunden")
    public Response getBetriebe(@PathParam("schema") String schema, @Context HttpServletRequest request){
        try(DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)){
            return (new DataBetriebsliste(conn)).getList();
        }        
    }
    
   /**
     * Die OpenAPI-Methode für die Abfrage der Stammdaten eines Betriebs.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation des Betriebs
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die Stammdaten eines Betriebs
     */

    @GET
    @Path("/{id : \\d+}/stammdaten")
    @Operation(summary = "Liefert zu der ID des Betriebs die zugehörigen Stammdaten.",
    description = "Liest die Stammdaten des Betriebs zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Stammdaten eines Betriebs",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON,
                 schema = @Schema(implementation = BetriebStammdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Betriebsdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Betrieb-Eintrag mit der angegebenen ID gefunden")
    public Response getBetriebStammdaten(@PathParam ("schema") String schema, @PathParam("id") long id, @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)){
            return (new DataBetriebsStammdaten(conn)).get(id);            
        }
    }

    /**
     * Die OpenAPI-Methode für das Patchen der Stammdaten eines Betriebs.
     *  
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation des Erziehers
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386 
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/{id : \\d+}/stammdaten")
    @Operation(summary = "Passt die zu der ID des Betriebs zugehörigen Stammdaten an.",
    description = "Passt die Betrieb-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Erzieherdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Betrieb-Stammdaten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Betriebdaten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Betrieb-Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    
    public Response patchBetriebStammdaten(@PathParam("schema") String schema, @PathParam("id") long id,
                                        @RequestBody(description = "Der Patch für die Betrieb-Stammdaten", required = true,
                                        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema=@Schema(implementation = BetriebStammdaten.class)))
                                        InputStream is, @Context HttpServletRequest request){
        try(DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN)) {
            return (new DataBetriebsStammdaten(conn)).patch(id, is);
        }
    }

    /**
     * Die OpenAPI-Methode für das Erstellen eines neuen Schülerbetriebs.
     *  
     * @param schema     		 	das Datenbankschema, in welchem der Schülerbetrieb erstellt wird
     * @param request      			die Informationen zur HTTP-Anfrage
     * @param schueler_id		    die Datanbenk_ID des Schülers des neuen Schülebetriebs
     * @param betrieb_id			die Datenbank_ID  des Betriebes des neuen Schülerbetriebs.
     * @param is							JSON-Objekt mit den Daten
     * @return die HTTP-Antwort mit der neuen Blockung
     */
    @POST
    @Path("/schuelerbetrieb/new/schueler/{schueler_id : \\d+}/betrieb/{betrieb_id: \\d+}")
    @Operation(summary = "Erstellt einen neuen Schülerbetrieb und gibt ihn zurück.",
    description = "Erstellt einen neuen Schülerbetrieb und gibt ihn zurück."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Schülerbetriebs "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Schülerbetrieb wurde erfolgreich angelegt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = SchuelerBetriebsdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Schülerbetrieb anzulegen.")
    @ApiResponse(responseCode = "404", description = "Kein Betrieb  mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response createSchuelerbetrieb(
    		@PathParam("schema") String schema,
    		@PathParam("schueler_id") long schueler_id,
    		@PathParam("betrieb_id") long betrieb_id,
    		@RequestBody(description = "Der Post für die Schülerbetrieb-Daten", required = true, content = 
			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuelerBetriebsdaten.class))) InputStream is, 
    		@Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) { // TODO Anpassung der Benutzerrechte
    		return (new DataSchuelerBetriebsdaten(conn)).create(schueler_id, betrieb_id,is);
    	}
    }
    
    
    
    /**
     * Die OpenAPI-Methode für das Patchen der Stammdaten eines Schülerbetriebs.
     *  
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation des Schülerbetriebs
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386 
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/{id : \\d+}/betrieb")
    @Operation(summary = "Passt die Schüler-Betriebsdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank.",
    description = "Passt die Schüler-Betriebsdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schülerbetreibsdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Schüler-Betriebsdaten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Schülerbetrieb-Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchSchuelerBetriebsdaten(
    		@PathParam("schema") String schema, 
    		@PathParam("id") long id, 
    		@RequestBody(description = "Der Patch für die Schüler-Schulbesuchsdaten", required = true, content = 
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuelerBetriebsdaten.class))) InputStream is, 
    		@Context HttpServletRequest request) 
    {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN)) {
          return (new DataSchuelerBetriebsdaten(conn)).patch(id, is);
    	}
    }

     /**
     * Die OpenAPI-Methode für die Abfrage eines Schülerbetriebs im angegebenen Schema.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * @param id        die Datenbank-ID zur Identifikation des Schülerbetriebs
     * @return              der Schülerbetrieb mit ID des Datenbankschemas
     */
    @GET
    @Path("/{id : \\d+}/betrieb")
    @Operation(summary = "Liefert zu der ID des Schülerbetriebs die zugehörigen Daten..",
               description = "Liest die Daten des Schülerbetriebs zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
                            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen vom Schülerbetrieb"
                            + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Stammdaten des Schülerbetriebs.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerBetriebsdaten.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerbetreibe anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Schülerbetrieb gefunden")
    public Response getSchuelerBetriebsdaten(@PathParam("schema") String schema, @PathParam("id") long id, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN)) {
	    	return (new DataSchuelerBetriebsdaten(conn)).get(id);
	    }
    }
    
    /**
     * Die OpenAPI-Methode für die Abfrage der Liste aller Betriebe.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste mit den einzelnen Betrieben
     */
    @GET
    @Path("/betriebansprechpartner")
    @Operation(summary = "Gibt eine Übersicht von allen Betriebansprechpartnern zurück.",
               description = "Erstellt eine Liste aller in der Datenbank vorhandenen Betriebansprechpartner , "
               		       + "des Ansprechpartnername, Kontaktdaten, ob sie in der Anwendung "
               		       + "sichtbar bzw. änderbar sein sollen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Betriebsansprechpartnern "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Betriebansprechpartnern",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = BetriebAnsprechpartner.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Betriebdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Betrieb-Einträge gefunden")
    public Response getBetriebAnsprechpartner(@PathParam("schema") String schema, @Context HttpServletRequest request){
        try(DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)){
            return (new DataBetriebAnsprechpartner(conn)).getList();
        }        
    }
    
    /**
     * Die OpenAPI-Methode für die Abfrage eines Betriebanpsrechpartners im angegebenen Schema.
     *  
     * @param schema       das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request      	die Informationen zur HTTP-Anfrage
     * @param id        			die Datenbank-ID zur Identifikation des Schülerbetriebs
     * @return              		der Betriebansprechpartner mit ID des Datenbankschemas
     */
    @GET
    @Path("/{id : \\d+}/betriebansprechpartner")
    @Operation(summary = "Liefert zu der ID des  Betriebanpsrechpartners die zugehörigen Daten..",
               description = "Liest die Daten des Betriebanpsrechpartners zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
                            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen vom Betriebanpsrechpartner"
                            + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Stammdaten des Betriebanpsrechpartners.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BetriebAnsprechpartner.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Betriebanpsrechpartner anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Betriebanpsrechpartner gefunden")
    public Response getBetriebAnsprechpartnerdaten(@PathParam("schema") String schema, @PathParam("id") long id, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN)) {
	    	return (new DataBetriebAnsprechpartner(conn)).get(id);
	    }
    }
    
    /**
     * Die OpenAPI-Methode für das Patchen der Stammdaten eines Schülerbetriebs.
     *  
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation des Schülerbetriebs
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386 
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/{id : \\d+}/betriebansprechpartner")
    @Operation(summary = "Passt die Betriebanpsrechpartner-Daten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank.",
    description = "Passt die Betriebanpsrechpartner-Daten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern vom Betriebanpsrechpartner "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Betriebanpsrechpartner-Daten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Betriebanpsrechpartner-Datenn zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Betriebanpsrechpartner-Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchBetriebanpsrechpartnerdaten(
    		@PathParam("schema") String schema, 
    		@PathParam("id") long id, 
    		@RequestBody(description = "Der Patch für die Betriebanpsrechpartner-Daten", required = true, content = 
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BetriebAnsprechpartner.class))) InputStream is, 
    		@Context HttpServletRequest request) 
    {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN)) {
          return (new DataBetriebAnsprechpartner(conn)).patch(id, is);
    	}
    }
    
    /**
     * Die OpenAPI-Methode für das Erstellen eines neuen Betriebansprechpartners.
     *  
     * @param schema       das Datenbankschema, in welchem der Betriebsansprechpartner erstellt wird
     * @param betrieb_id   des Betrieb 
     * @param request      die Informationen zur HTTP-Anfrage
     * @param is           das JSON-Objekt
     * @return die HTTP-Antwort mit der neuen Blockung
     */
    @POST
    @Path("/{betrieb_id : \\d+}/ansprechpartner/new")
    @Operation(summary = "Erstellt einen neuen Betriebansprechpartner und gibt die dazugehörige ID zurück.",
    description = "Erstellt einen neuen Betriebansprechpartner und gibt die dazugehörige ID zurück."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Betriebansprechpartners "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Ansprechpartner wurde erfolgreich angelegt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = BetriebAnsprechpartner.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Ansprechpartner anzulegen.")
    @ApiResponse(responseCode = "404", description = "Kein Betrieb  mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response createBetriebansprechpartner(
    		@PathParam("schema") String schema, @PathParam("betrieb_id") long betrieb_id,     	
    		@RequestBody(description = "Der Post für die Betriebanpsrechpartner-Daten", required = true, content = 
			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BetriebAnsprechpartner.class))) InputStream is, 
    		@Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) { // TODO Anpassung der Benutzerrechte
    		return (new DataBetriebAnsprechpartner(conn)).create(betrieb_id,is);
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

}
