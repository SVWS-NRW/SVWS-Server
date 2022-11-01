package de.nrw.schule.svws.api.server;

import java.io.InputStream;

import de.nrw.schule.svws.api.OpenAPIApplication;
import de.nrw.schule.svws.core.data.schule.AbgangsartKatalog;
import de.nrw.schule.svws.core.data.schule.AllgemeineMerkmaleKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.BerufskollegAnlageKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.BerufskollegBerufsebeneKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.BerufskollegFachklassenKatalog;
import de.nrw.schule.svws.core.data.schule.EinschulungsartKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.FoerderschwerpunktEintrag;
import de.nrw.schule.svws.core.data.schule.FoerderschwerpunktKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.HerkunftsschulnummerKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.NationalitaetenKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.OrganisationsformKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.PruefungsordnungKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.ReformpaedagogikKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.ReligionEintrag;
import de.nrw.schule.svws.core.data.schule.ReligionKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.SchuelerstatusKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.SchulabschlussAllgemeinbildendKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.SchulabschlussBerufsbildendKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.SchuleStammdaten;
import de.nrw.schule.svws.core.data.schule.SchulenKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.SchulformKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.SchulgliederungKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.Schuljahresabschnitt;
import de.nrw.schule.svws.core.data.schule.SchulstufeKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.SchultraegerKatalogEintrag;
import de.nrw.schule.svws.core.data.schule.VerkehrsspracheKatalogEintrag;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.data.schueler.DataKatalogSchuelerFoerderschwerpunkte;
import de.nrw.schule.svws.data.schule.DataKatalogAbgangsartenAllgemeinbildend;
import de.nrw.schule.svws.data.schule.DataKatalogAbgangsartenBerufsbildend;
import de.nrw.schule.svws.data.schule.DataKatalogAllgemeineMerkmale;
import de.nrw.schule.svws.data.schule.DataKatalogBerufskollegAnlagen;
import de.nrw.schule.svws.data.schule.DataKatalogBerufskollegBerufsebenen;
import de.nrw.schule.svws.data.schule.DataKatalogBerufskollegFachklassen;
import de.nrw.schule.svws.data.schule.DataKatalogEinschulungsarten;
import de.nrw.schule.svws.data.schule.DataKatalogFoerderschwerpunkte;
import de.nrw.schule.svws.data.schule.DataKatalogHerkunftsschulnummern;
import de.nrw.schule.svws.data.schule.DataKatalogNationalitaeten;
import de.nrw.schule.svws.data.schule.DataKatalogOrganisationsformen;
import de.nrw.schule.svws.data.schule.DataKatalogPruefungsordnungen;
import de.nrw.schule.svws.data.schule.DataKatalogReformpaedagogik;
import de.nrw.schule.svws.data.schule.DataKatalogReligionen;
import de.nrw.schule.svws.data.schule.DataKatalogSchulabschluesseAllgemeinbildend;
import de.nrw.schule.svws.data.schule.DataKatalogSchulabschluesseBerufsbildend;
import de.nrw.schule.svws.data.schule.DataKatalogSchulen;
import de.nrw.schule.svws.data.schule.DataKatalogSchulformen;
import de.nrw.schule.svws.data.schule.DataKatalogSchulgliederungen;
import de.nrw.schule.svws.data.schule.DataKatalogSchultraeger;
import de.nrw.schule.svws.data.schule.DataKatalogVerkehrssprachen;
import de.nrw.schule.svws.data.schule.DataReligionen;
import de.nrw.schule.svws.data.schule.DataSchuelerStatus;
import de.nrw.schule.svws.data.schule.DataSchuleStammdaten;
import de.nrw.schule.svws.data.schule.DataSchulstufen;
import de.nrw.schule.svws.db.Benutzer;
import de.nrw.schule.svws.db.DBEntityManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FieldResult;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Daten der Schule aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/schule/...
 */
@Path("/db/{schema}/schule")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")	
@SqlResultSetMapping(name="SQLMappingSchuljahresabschnitte", 
	entities = { @EntityResult(entityClass=Schuljahresabschnitt.class, 
		fields = {
			@FieldResult(name="schuljahr", column="schuljahr"),
			@FieldResult(name="abschnitt", column="abschnitt")
		}
	)}
)
public class APISchule {
	
	
    /**
     * Die OpenAPI-Methode für die Abfrage der Schulnummer der Schule.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die Schulnummer
     */
    @GET
    @Path("/nummer")
    @Operation(summary = "Gibt die Schulnummer der Schule zurück.",
               description = "Liefert die Schulnummer der Schule. Es wird geprüft, ob der SVWS-Benutzer die "
               		       + "notwendige Berechtigung zum Ansehen der Schuldaten besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Schulnummer", 
                 content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                 schema = @Schema(implementation = Integer.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuldaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Schule in der Datenbank vorhanden")
    public Response getSchuleNummer(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN)) {
    		return (new DataSchuleStammdaten(conn).getSchulnummerResponse());	    	
    	}
    }
    
    
    
    /**
     * Die OpenAPI-Methode für die Abfrage der Stammdaten der Schule.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste mit den Stammdaten
     */
	@GET
    @Path("/stammdaten")
    @Operation(summary = "Liefert zu der Schule mit dem angegebenen Schema die zugehörigen Stammdaten.",
    description = "Liest die Stammdaten der Schule zum angegebenen Schema aus der Datenbank und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schuldaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Stammdaten der Schule",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON,
                 schema = @Schema(implementation = SchuleStammdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuldaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag mit dem angegebenen Schema gefunden")
    public Response getSchuleStammdaten(@PathParam("schema") String schema, 
    		                                    @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN)) {
    		return (new DataSchuleStammdaten(conn).get());
    	}
    }
    
    
    /**
     * Die OpenAPI-Methode für das Anpassen der Stammdaten der Schule.
     *  
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386 
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/stammdaten")
    @Operation(summary = "Liefert zu der Schule des Datenbank-Schemas die zugehörigen Stammdaten.",
    description = "Passt die Schul-Stammdaten an und speichert das Ergebnis in der Datenbank. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schuldaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Schul-Stammdaten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuldaten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchSchuleStammdaten(
    		@PathParam("schema") String schema, 
    		@RequestBody(description = "Der Patch für die Schul-Stammdaten", required = true, content = 
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuleStammdaten.class))) InputStream is, 
    		@Context HttpServletRequest request) 
    {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN)) {
    		return (new DataSchuleStammdaten(conn).patch(is));
    	}
    }

    
    /**
     * Die OpenAPI-Methode für die Abfrage des Schullogos der Schule.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return das Schullogo als Base64-kodierter String
     */
	@GET
    @Path("/logo")
    @Operation(summary = "Liefert zu der Schule mit dem angegebenen Schema das zugehörige Schullogo.",
    description = "Liest das Logo der Schule zum angegebenen Schema aus der Datenbank und liefert dieses zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schuldaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Das Logo der Schule",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON,
                 schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuldaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag mit dem angegebenen Schema gefunden")
    public Response getSchullogo(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN)) {
    		return (new DataSchuleStammdaten(conn).getSchullogo());			
    	}
    }
    
    
    
    /**
     * Die OpenAPI-Methode zum Setzen des Schullogos der Schule.
     *  
     * @param schema    das Datenbankschema, auf welches die Anfrage ausgeführt werden soll
     * @param is        der JSON-String mit dem Schullogo als Base64-kodiertem String
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return das Schullogo als Base64-kodierter String
     */
	@PUT
    @Path("/logo")
    @Operation(summary = "Setzt das Logo der Schule.",
    description = "Setzt das Logo der Schule. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schuldaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Das Logo der Schule wurde gesetzt")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuldaten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag für die Schule gefunden")
    public Response putSchullogo(@PathParam("schema") String schema,
    		@RequestBody(description = "Das Logo der Schule", required = false, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class))) InputStream is,
    		@Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN)) {
    		return (new DataSchuleStammdaten(conn).putSchullogo(is));
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges für die allgemeinbildenden Schulabschlüsse.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste mit dem Katalog der allgemeinbildenden Schulabschlüssen
     */
    @GET
    @Path("/allgemein/schulabschluesse/allgemeinbildend")
    @Operation(summary = "Gibt den Katalog der allgemeinbildenden Schulabschlüsse zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden allgemeinbildenden Schulabschlüsse. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von allgemeinbildenden Abschlussart-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchulabschlussAllgemeinbildendKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Abschlussart-Katalog-Einträge gefunden")
    public Response getSchulabschluesseAllgemeinbildend(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogSchulabschluesseAllgemeinbildend(null)).getList();
		}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges für die berufsbildenden Schulabschlüsse.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste mit dem Katalog der berufsbildenden Schulabschlüssen
     */
    @GET
    @Path("/allgemein/schulabschluesse/berufsbildend")
    @Operation(summary = "Gibt den Katalog der berufsbildenden Schulabschlüsse zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden berufsbildenden Schulabschlüsse. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von berufsbildenden Abschlussart-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchulabschlussBerufsbildendKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Abschlussart-Katalog-Einträge gefunden")
    public Response getSchulabschluesseBerufsbildend(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogSchulabschluesseBerufsbildend(null)).getList();
		}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges für die Schulformen.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste mit dem Katalog der Schulformen
     */
    @GET
    @Path("/allgemein/schulformen")
    @Operation(summary = "Gibt den Katalog der Schulformen zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden Schulformen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Schulform-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchulformKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Schulform-Katalog-Einträge gefunden")
    public Response getSchulformen(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogSchulformen(null)).getList();
		}
    }
    
	
    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges für die Schulgliederungen.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste mit dem Katalog der Schulgliederungen
     */
    @GET
    @Path("/allgemein/schulgliederungen")
    @Operation(summary = "Gibt den Katalog der Schulgliederungen zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden Schulgliederungen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Schulgliederung-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchulgliederungKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Schulform-Katalog-Einträge gefunden")
    public Response getSchulgliederungen(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogSchulgliederungen(null)).getList();
		}
    }
 
    
    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges für die Anlagen am Berufskolleg.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste mit dem Katalog der Anlagen am Berufskolleg
     */
    @GET
    @Path("/allgemein/berufskolleg/anlagen")
    @Operation(summary = "Gibt den Katalog der Anlagen am Berufskolleg zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden Anlagen am Berufskolleg. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Berufskolleg-Anlagen-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BerufskollegAnlageKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Berufskolleg-Anlagen-Katalog-Einträge gefunden")
    public Response getBerufskollegAnlagen(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogBerufskollegAnlagen(null)).getList();
		}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges für die Berufsebenen am Berufskolleg.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste mit dem Katalog der Berufsebenen am Berufskolleg
     */
    @GET
    @Path("/allgemein/berufskolleg/berufsebenen")
    @Operation(summary = "Gibt den Katalog der Berufsebenen am Berufskolleg zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden Berufsebenen am Berufskolleg. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Berufskolleg-Berufsebenen-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BerufskollegBerufsebeneKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Berufskolleg-Berufsebenen-Katalog-Einträge gefunden")
    public Response getBerufskollegBerufsebenen(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogBerufskollegBerufsebenen(null)).getList();
		}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges für die Fachklassen am Berufskolleg.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return der Katalog der Fachklassen am Berufskolleg
     */
    @GET
    @Path("/allgemein/berufskolleg/fachklassen")
    @Operation(summary = "Gibt den Katalog der Fachklassen am Berufskolleg zurück.",
               description = "Gibt den Katalog der Fachklassen am Berufskolleg zurück. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Fachklassen-Katalog für berufsbildende Schulen",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = BerufskollegFachklassenKatalog.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Berufskolleg-Fachklassen-Katalog gefunden")
    public Response getBerufskollegFachklassen(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogBerufskollegFachklassen(null)).getList();
		}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges der Ausbildungs- und Prüfungsordnungen.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste mit dem Katalog der Ausbildungs- und Prüfungsordnungen
     */
    @GET
    @Path("/allgemein/pruefungsordnungen")
    @Operation(summary = "Gibt den Katalog der Ausbildungs- und Prüfungsordnungen zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden Ausbildungs- und Prüfungsordnungen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PruefungsordnungKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getPruefungsordnungen(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogPruefungsordnungen(null)).getList();
		}
    }

    
    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges der Verkehrssprachen in einer Familie.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste mit dem Katalog der Verkehrssprachen in einer Familie
     */
    @GET
    @Path("/allgemein/verkehrssprachen")
    @Operation(summary = "Gibt den Katalog der Verkehrssprachen in einer Familie zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden der Verkehrssprachen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = VerkehrsspracheKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getVerkehrssprachen(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogVerkehrssprachen(null)).getList();
		}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Liste der Nationalitäten im angegebenen Schema.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return              die Liste der Nationalitäten mit ID des Datenbankschemas
     */
    @GET
    @Path("/allgemein/nationalitaeten")
    @Operation(summary = "Gibt den Katalog der Nationalitäten zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden Nationalitäten. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Nationalitäten-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = NationalitaetenKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Nationalitäten-Katalog-Einträge gefunden")
    public Response getNationaelitaeten(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogNationalitaeten(null)).getAll();
    	}
    }  


    /**
     * Die OpenAPI-Methode für die Abfrage des Katalogs der Einschulungsarten.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return der Katalog der Einschulungsarten
     */
    @GET
    @Path("/allgemein/einschulungsarten")
    @Operation(summary = "Gibt den Katalog der Einschulungsarten zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden Einschulungsarten. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Einschulungsart-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EinschulungsartKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Einschulungsart-Katalog-Einträge gefunden")
    public Response getEinschulungsarten(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogEinschulungsarten(null)).getAll();
    	}
    }  


    /**
     * Die OpenAPI-Methode für die Abfrage des Katalogs der Allgemeinen Merkmale bei Schulen und Schülern.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return der Katalog der Allgemeinen Merkmale bei Schulen und Schülern
     */
    @GET
    @Path("/allgemein/allgemeine_merkmale")
    @Operation(summary = "Gibt den Katalog der Allgemeinen Merkmale bei Schulen und Schülern zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden Allgemeinen Merkmale bei Schulen und Schülern. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Allgemeinen-Merkmal-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AllgemeineMerkmaleKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Allgemeine-Merkmal-Katalog-Einträge gefunden")
    public Response getAllgemeineMerkmale(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogAllgemeineMerkmale(null)).getAll();
    	}
    }  



    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges für die Förderschwerpunkte.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste mit dem Katalog der Förderschwerpunkte
     */
    @GET
    @Path("/allgemein/foerderschwerpunkte")
    @Operation(summary = "Gibt den Katalog der Förderschwerpunkte zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden Förderschwerpunkte. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Förderschwerpunkt-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FoerderschwerpunktKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Förderschwerpunkt-Katalog-Einträge gefunden")
    public Response getFoerderschwerpunkte(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogFoerderschwerpunkte()).getList();
		}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges für die Förderschwerpunkte.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste mit dem Katalog der Förderschwerpunkte
     */
    @GET
    @Path("/foerderschwerpunkte")
    @Operation(summary = "Gibt den Katalog der Förderschwerpunkte zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden Förderschwerpunkte unter Angabe der ID, eines Kürzels und der Bezeichnung. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Förderschwerpunkte-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FoerderschwerpunktEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Förderschwerpunkt-Katalog-Einträge gefunden")
    public Response getSchuelerFoerderschwerpunkte(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogSchuelerFoerderschwerpunkte(conn).getList());
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage eines Förderschwerpunktes.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation des Förderschwerpunktes
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die Daten zum Förderschwerpunkt
     */
    @GET
    @Path("/foerderschwerpunkt/{id : \\d+}")
    @Operation(summary = "Liefert zu der ID des Förderschwerpunktes die zugehörigen Daten.",
    description = "Liest die Daten des Förderschwerpunktes zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Daten des Förderschwerpunktes",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = FoerderschwerpunktEintrag.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Förderschwerpunkt-Eintrag mit der angegebenen ID gefunden")
    public Response getSchuelerFoerderschwerpunkt(@PathParam("schema") String schema, @PathParam("id") long id, 
    		                                        @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN)) {
    		return (new DataKatalogSchuelerFoerderschwerpunkte(conn).get(id));
    	}
    }



    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges für die Relgionen bzw. Konfessionen,
     * welche im Rahmen der amtlichen Schulstatistik verwendet werden.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste mit dem Katalog der Relgionen bzw. Konfessionen
     */
    @GET
    @Path("/allgemein/religionen")
    @Operation(summary = "Gibt den Katalog der Relgionen bzw. Konfessionen zurück, welche im Rahmen der amtlichen Schulstatistik verwendet werden.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden Relgionen bzw. Konfessionen, welche "
               		       + "im Rahmen der amtlichen Schulstatistik verwendet werden. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReligionKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogReligionen(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogReligionen()).getList();
		}
    }

    
    /**
     * Die OpenAPI-Methode für die Abfrage einer Religion.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id              die Datenbank-ID zur Identifikation der Religion
     * @param request    die Informationen zur HTTP-Anfrage
     * 
     * @return die Daten zur Religion
     */
    @GET
    @Path("/religionen/{id : \\d+}")
    @Operation(summary = "Liefert zu der ID der Religion die zugehörigen Daten.",
    description = "Liest die Daten der Religion zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Daten der Religion",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ReligionEintrag.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Religion mit der angegebenen ID gefunden")
    public Response getReligion(@PathParam("schema") String schema, @PathParam("id") long id, 
    		                                        @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN)) {
    		return (new DataReligionen(conn).get(id));
    	}
    }
    
    /**
     * Die OpenAPI-Methode für das Erstellen einer neuen Religion.
     *  
     * @param schema       das Datenbankschema, in welchem die Religion erstellt wird
     * @param request      die Informationen zur HTTP-Anfrage
     * @param is           das JSON-Objekt
     * @return die HTTP-Antwort mit der neuen Religion
     */
    @POST
    @Path("/religionen/new")
    @Operation(summary = "Erstellt eine neue Religion und gibt sie zurück.",
    description = "Erstellt eine neue Religion und gibt sie zurück."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Religion "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Religion wurde erfolgreich angelegt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ReligionEintrag.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Religion anzulegen.")
    @ApiResponse(responseCode = "404", description = "Keine Religion  mit dem eingegebenen Kuerzel gefunden")
    @ApiResponse(responseCode = "409", description = "Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response createReligion(
    		@PathParam("schema") String schema,     	
    		@RequestBody(description = "Der Post für die Religion-Daten", required = true, content = 
			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ReligionEintrag.class))) InputStream is, 
    		@Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN)) {
    		return (new DataReligionen(conn)).create(is);
    	}
    }
    
    /**
     * Die OpenAPI-Methode für das Patchen einer Betriebsart im angegebenen Schema
     *  
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation der Religion
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386 
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/religionen/{id : \\d+}")
    @Operation(summary = "Passt die zu der ID der Religion zugehörigen Stammdaten an.",
    description = "Passt die Religion-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Religion "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Religion-Daten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Religion-Daten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Keine Religion mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    
    public Response patchReligion(@PathParam("schema") String schema, @PathParam("id") long id,
                                        @RequestBody(description = "Der Patch für die Religion-Stammdaten", required = true,
                                        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema=@Schema(implementation = ReligionEintrag.class)))
                                        InputStream is, @Context HttpServletRequest request){
        try(DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN)) {
            return (new DataReligionen(conn)).patch(id, is);
        }
    }
    /**
     * Die OpenAPI-Methode für die Abfrage des schulspezifischen Kataloges für die Religionen bzw. Konfessionen.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return          die Liste mit dem Katalog der Religionen bzw. Konfessionen
     */
    @GET
    @Path("/religionen")
    @Operation(summary = "Gibt eine Übersicht aller Religionen bzw. Konfessionen im Katalog zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden Religionen bzw. Konfessionen unter Angabe der ID, der Bezeichnung sowie der Bezeichnung, "
               		       + "welche auf dem Zeugnis erscheint, einem Statistik-Kürzel, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar bzw. "
               		       + "änderbar sein sollen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReligionEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getReligionen(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
        	return (new DataReligionen(conn)).getAll();
    	}
    }

    
    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges der Abgangsarten für
     * allgemeinbildende Schulformen, welche im Rahmen der amtlichen 
     * Schulstatistik zulässig sind.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return der Katalog der Abgangsarten für allgemeinbildende Schulformen 
     */
    @GET
    @Path("/allgemein/abgangsarten/allgemeinbildend")
    @Operation(summary = "Gibt den Katalog der Katalog der Abgangsarten für allgemeinbildende Schulformen zurück.",
               description = "Gibt den Katalog der Abgangsarten für allgemeinbildende Schulformen zurück. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Katalog",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = AbgangsartKatalog.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.")
    @ApiResponse(responseCode = "404", description = "Katalog nicht gefunden")
    public Response getKatalogAbgangsartenAllgemeinbildend(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogAbgangsartenAllgemeinbildend()).getList();
		}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges der Abgangsarten für
     * berufsbildende Schulformen, welche im Rahmen der amtlichen 
     * Schulstatistik zulässig sind.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return der Katalog der Abgangsarten für berufsbildende Schulformen 
     */
    @GET
    @Path("/allgemein/abgangsarten/berufsbildend")
    @Operation(summary = "Gibt den Katalog der Katalog der Abgangsarten für berufsbildende Schulformen zurück.",
               description = "Gibt den Katalog der Abgangsarten für berufsbildende Schulformen zurück. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Katalog",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = AbgangsartKatalog.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.")
    @ApiResponse(responseCode = "404", description = "Katalog nicht gefunden")
    public Response getKatalogAbgangsartenBerufsbildend(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKatalogAbgangsartenBerufsbildend()).getList();
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
    @Path("/allgemein/organisationsformen")
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


    
    
    /**
     * Die OpenAPI-Methode für die Abfrage des Katalogs der gültigen Schulstufen.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return              der Katalog der gültigen Schulstufen
     */
    @GET
    @Path("/allgemein/schulstufen")
    @Operation(summary = "Gibt den Katalog der gültigen Schulstufen zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden gültigen Schulstufen. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Schulstufen-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchulstufeKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getSchulstufen(@PathParam("schema") String schema, @Context HttpServletRequest request) {
        try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
            return (new DataSchulstufen()).getAll();
        }
    }  


    /**
     * Die OpenAPI-Methode für die Abfrage des Schulen-Kataloges.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste der Schulen
     */
    @GET
    @Path("/allgemein/schulen")
    @Operation(summary = "Erstellt eine Liste aller in dem Katalog vorhandenen Schulen.",
               description = "Erstellt eine Liste aller in dem Katalog vorhandenen Schulen. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Schulen-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchulenKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Schulen-Katalog-Einträge gefunden")
    public Response getKatalogSchulen(@PathParam("schema") String schema, @Context HttpServletRequest request) {
        try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
            return (new DataKatalogSchulen()).getAll();
        }
    }

    
    /**
     * Die OpenAPI-Methode für die Abfrage des Schulträger-Kataloges.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste der Schulträger
     */
    @GET
    @Path("/allgemein/schultraeger")
    @Operation(summary = "Erstellt eine Liste aller in dem Katalog vorhandenen Schulträger.",
               description = "Erstellt eine Liste aller in dem Katalog vorhandenen Schulträger. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Schulträger-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchultraegerKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Schulträger-Katalog-Einträge gefunden")
    public Response getKatalogSchultraeger(@PathParam("schema") String schema, @Context HttpServletRequest request) {
        try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
            return (new DataKatalogSchultraeger()).getAll();
        }
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges Schüler-Status.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return der Katalog
     */
    @GET
    @Path("/schueler/status")
    @Operation(summary = "Die Liste der Einträge aus dem Katalog Schüler-Status.",
               description = "Die Liste der Einträge aus dem Katalog Schüler-Status. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den Katalog Schüler-Status",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerstatusKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogSchuelerStatus(@PathParam("schema") String schema, @Context HttpServletRequest request) {
        try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
            return (new DataSchuelerStatus()).getAll();
        }
    }

    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges der zusätzlichen Herkunftsschulnummern
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return der Katalog
     */
    @GET
    @Path("/allgemein/herkunftsschulnummern")
    @Operation(summary = "Die Liste der Einträge aus dem Katalog der zusätzlichen Herkunftsschulnummern.",
               description = "Die Liste der Einträge aus dem Katalog der zusätzlichen Herkunftsschulnummern. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den Katalog der zusätzlichen Herkunftsschulnummern",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = HerkunftsschulnummerKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogHerkunftsschulnummern(@PathParam("schema") String schema, @Context HttpServletRequest request) {
        try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
            return (new DataKatalogHerkunftsschulnummern()).getAll();
        }
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Reformpädagogik-Kataloges aller Schulformen.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return der Reformpädagogik-Katalog aller Schulformen
     */
    @GET
    @Path("/allgemein/reformpaedagogik/alle")
    @Operation(summary = "Gibt den Reformpädagogik-Katalog aller Schulformen zurück.",
               description = "Gibt den Reformpädagogik-Katalog aller Schulformen zurück. "
                       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Reformpädagogik-Katalog aller Schulformen.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReformpaedagogikKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
    @ApiResponse(responseCode = "404", description = "Keine Fachgruppen gefunden.")
    public Response getKatalogReformpaedagogikAlle(@PathParam("schema") String schema, @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
            return (new DataKatalogReformpaedagogik(conn)).getAll();
        }
    }

    /**
     * Die OpenAPI-Methode für die Abfrage des Reformpädagogik-Kataloges für die Schulform dieser Schule.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste mit den Einträgen des Reformpädagogik-Kataloges für die Schulform dieser Schule
     */
    @GET
    @Path("/allgemein/reformpaedagogik")
    @Operation(summary = "Gibt den Reformpädagogik-Katalog für die Schulform dieser Schule zurück.",
               description = "Gibt den Reformpädagogik-Katalog für die Schulform dieser Schule zurück. "
                       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Reformpädagogik-Katalog für die Schulform dieser Schule.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReformpaedagogikKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
    @ApiResponse(responseCode = "404", description = "Keine Reformpädagogik-Einträge für die Schulform dieser Schule gefunden.")
    public Response getKatalogReformpaedagogik(@PathParam("schema") String schema, @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
            return (new DataKatalogReformpaedagogik(conn)).getList();
        }
    }

    /**
     * Die OpenAPI-Methode für die Abfrage des Reformpädagogik-Katalog-Eintrags für die angegebene ID.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id        die ID des Reformpädagogik-Katalog-Eintrags
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return der Reformpädagogik-Katalog-Eintrag
     */
    @GET
    @Path("/allgemein/reformpaedagogik/{id : \\d+}")
    @Operation(summary = "Gibt den Reformpädagogik-Katalog-Eintrag für die angegebene ID zurück.",
               description = "Gibt den Reformpädagogik-Katalog-Eintrag für die angegebene ID zurück. "
                       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Reformpädagogik-Katalog-Eintrag für die angegebene ID.",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReformpaedagogikKatalogEintrag.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
    @ApiResponse(responseCode = "404", description = "Kein Reformpädagogik-Katalog-Eintrag für die angegebene ID gefunden.")
    public Response getKatalogReformpaedagogikEintrag(@PathParam("schema") String schema, @PathParam("id") long id, @Context HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
            return (new DataKatalogReformpaedagogik(conn)).get(id);
        }
    }
    
}
