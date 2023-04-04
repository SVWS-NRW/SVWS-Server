package de.svws_nrw.api.server;

import java.io.InputStream;
import java.util.List;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import de.svws_nrw.api.OpenAPIApplication;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnis;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungListeneintrag;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsdaten;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.gost.GostJahrgang;
import de.svws_nrw.core.data.gost.GostJahrgangFachkombination;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungDaten;
import de.svws_nrw.core.data.gost.GostLeistungen;
import de.svws_nrw.core.data.gost.GostSchuelerFachwahl;
import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.SimpleBinaryMultipartBody;
import de.svws_nrw.data.gost.DataGostAbiturjahrgangFachwahlen;
import de.svws_nrw.data.gost.DataGostBlockungKurs;
import de.svws_nrw.data.gost.DataGostBlockungKursLehrer;
import de.svws_nrw.data.gost.DataGostBlockungRegel;
import de.svws_nrw.data.gost.DataGostBlockungSchiene;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.data.gost.DataGostBlockungsliste;
import de.svws_nrw.data.gost.DataGostFaecher;
import de.svws_nrw.data.gost.DataGostJahrgangFachkombinationen;
import de.svws_nrw.data.gost.DataGostJahrgangSchuelerliste;
import de.svws_nrw.data.gost.DataGostJahrgangsdaten;
import de.svws_nrw.data.gost.DataGostJahrgangsliste;
import de.svws_nrw.data.gost.DataGostSchuelerLaufbahnplanung;
import de.svws_nrw.data.gost.DataGostSchuelerLaufbahnplanungBeratungsdaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.db.utils.data.Schule;
import de.svws_nrw.db.utils.gost.FaecherGost;
import de.svws_nrw.db.utils.gost.GostSchueler;
import de.svws_nrw.db.utils.gost.GostSchuelerAbitur;
import de.svws_nrw.module.pdf.gost.PDFGostWahlbogen;
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
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die SVWS-Datenbank in Bezug auf die gymnasiale Oberstufe.
 * Ein Zugriff erfolgt über den Pfad https://{hostname}/db/{schema}/gost/...
 */
@Path("/db/{schema}/gost")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIGost {


	/**
	 * Liefert eine Liste aller Abiturjahrgänge, welche in der Datenbank für die Laufbahnplanung angelegt sind.
	 * Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen von Kataloginformationen
	 * besitzt.
	 *
	 * @param schema   das Schema aus dem die Leistungsdaten des Schüler kommen sollen
	 *
	 * @param request  die Informationen zur HTTP-Anfrage
	 *
	 * @return eine Liste der Abiturjahrgänge
	 */
	@GET
	@Path("/abiturjahrgaenge")
	@Operation(summary = "Liefert eine Liste aller Abiturjahrgänge, welche in der Datenbank für die Laufbahnplanung angelegt sind.",
	           description = "Liefert eine Liste aller Abiturjahrgänge, welche in der Datenbank für die Laufbahnplanung angelegt sind."
	           		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen von Kataloginformationen "
	           		       + "besitzt.")
    @ApiResponse(responseCode = "200",
    			 description = "Die Liste der Abiturjahrgänge.",
    			 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostJahrgang.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Liste der Abiturjahrgänge auszulesen.")
	@ApiResponse(responseCode = "404", description = "Kein Abiturjahrgang gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden")
	public Response getGostAbiturjahrgaenge(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataGostJahrgangsliste(conn)).getAll();
    	}
	}


    /**
     * Die OpenAPI-Methode für das Erstellen eines neuen Abiturjahrgangs der Gymnasialen Oberstufe.
     *
     * @param schema       das Datenbankschema, in welchem die Blockung erstellt wird
     * @param jahrgangID   die ID des Jahrgangs, für welchen der Abitur-Jahrgang erstellt werden soll
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit dem neu angelegten Abiturjahr
     */
    @POST
    @Path("/abiturjahrgang/new/{jahrgangid}")
    @Operation(summary = "Erstellt einen neuen Abiturjahrgang und gibt das Abiturjahr zurück.",
    description = "Erstellt einen neuen Abiturjahrgang und gibt das Abiturjahr zurück."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eine Abiturjahrgangs "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Abiturjahrgang wurde erfolgreich angelegt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Integer.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Abiturjahrgang anzulegen.")
    @ApiResponse(responseCode = "404", description = "Keine Daten beim angegebenen Jahrgang gefunden, um einen Abiturjahrgang anzulegen")
    @ApiResponse(responseCode = "409", description = "Der Abiturjahrgang existiert bereits")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response createGostAbiturjahrgang(@PathParam("schema") final String schema, @PathParam("jahrgangid") final long jahrgangID, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerrechte
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostJahrgangsliste(conn)).create(jahrgangID);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Daten eines Jahrgangs der Gymnasialen Oberstufe.
     *
     * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abiturjahr  das Jahr, in welchem der Jahrgang Abitur machen wird
     * @param request     die Informationen zur HTTP-Anfrage
     *
     * @return die Daten des Jahrganges
     */
    @GET
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}")
    @Operation(summary = "Liefert zu dem Jahr, in welchem der Jahrgang Abitur machen wird, die zugehörigen Grunddaten.",
    description = "Liest die Grunddaten des Jahrgangs der gymnasialen Oberstufe zu dem Jahr, in welchem der Jahrgang Abitur "
    		    + "machen wird, aus der Datenbank und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Jahrgangsinformationen "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Grunddaten des Jahrgangs der gymnasialen Oberstufe",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = GostJahrgangsdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Grunddaten der Gymnasialen Oberstufe anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag für einen Jahrgang der gymnasialen Oberstufe mit dem angegebenen Abiturjahr gefunden")
    public Response getGostAbiturjahrgang(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
    		                                    @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeinformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataGostJahrgangsdaten(conn)).get(abiturjahr);
    	}
    }



    /**
     * Die OpenAPI-Methode für das Anpassen der Daten eines Jahrgangs der Gymnasialen Oberstufe.
     *
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param abiturjahr  das Jahr, in welchem der Jahrgang Abitur machen wird
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request     die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @PATCH
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}")
    @Operation(summary = "Passt die Daten des Abiturjahrgangs der Gymnasialen Oberstufe an.",
    description = "Passt die Daten des Jahrganges der gymnasialen Oberstufe zu dem Jahr an, in welchem der Jahrgang Abitur "
    		    + "machen wird. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen der Jahrgangsinformationen "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Jahrgangsdaten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Jahrgangsdaten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Abiturjahrgangs-Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchGostAbiturjahrgang(
    		@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
    		@RequestBody(description = "Der Patch für die Abiturjahrgangsdaten", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostJahrgangsdaten.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) { // TODO Anpassung der Benutzerrechte
    		return (new DataGostJahrgangsdaten(conn)).patch(abiturjahr, is);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Liste der Schüler der gymnasialen Oberstufe
     * im angegebenen Schema für den angegebenen Abitur-Jahrgang.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abiturjahr    der Abitur-Jahrgang
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Liste der Schüler - allerdings ohne Informationen zu Kursen
     */
    @GET
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}/schueler")
    @Operation(summary = "Gibt eine Übersicht von allen Schülern des Abitur-Jahrganges der gymnasialen Oberstufe zurück.",
               description = "Erstellt eine Liste aller Schüler, welche zu dem angebenen Abitur-Jahrgang gehören (ohne Informationen zu Kursen). "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Schülerdaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Schüler-Listen-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Schüler gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden")
    public Response getGostAbiturjahrgangSchueler(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN)) {
    		if (abiturjahr < 0)
    			return OperationError.NOT_FOUND.getResponse("Schüler können dem Vorlagen-Abiturjahrgang nicht zugewiesen sein.");
    		return (new DataGostJahrgangSchuelerliste(conn, abiturjahr)).getList();
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Liste der Fächer der gymnasialen Oberstufe
     * im angegebenen Schema für den angegebenen Abitur-Jahrgang.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abiturjahr    der Abitur-Jahrgang
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Liste der Fächer der gymnasialen Oberstufe mit der ID des Faches
     */
    @GET
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}/faecher")
    @Operation(summary = "Gibt eine Übersicht von allen Fächern des Abitur-Jahrganges der gymnasialen Oberstufe zurück.",
               description = "Erstellt eine Liste aller in der Datenbank vorhanden Fächer der gymnasialen Oberstufe, "
                           + "welche für den angebenen Abitur-Jahrgang festgelegt wurden.. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Fächerdaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Fächer-Listen-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostFach.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Fächerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Fächer-Einträge gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden")
    public Response getGostAbiturjahrgangFaecher(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN)) {
    		return (new DataGostFaecher(conn, abiturjahr)).getList();
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Daten eines Faches in Bezug auf die Gymnasiale Oberstufe
     * und in Bezug auf einen Abiturjahrgang.
     *
     * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abiturjahr  der Abitur-Jahrgang
     * @param id          die ID des Faches
     * @param request     die Informationen zur HTTP-Anfrage
     *
     * @return die Abiturjahrgangs-spezifischen Daten des Faches der gymnasialen Oberstufe
     */
    @GET
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}/fach/{id : \\d+}")
    @Operation(summary = "Liefert die Informationen bezüglich eines Abiturjahrgangs der gymnasialen Oberstufe zu dem Fach mit der angegebenen ID.",
    description = "Liefert die Informationen bezüglich eines Abiturjahrgangs der gymnasialen Oberstufe zu dem Fach mit der angegebenen ID. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Fachinformationen "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Daten des Faches bezüglich eines Abiturjahrgangs der gymnasialen Oberstufe",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = GostFach.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Fachinformationen der Gymnasialen Oberstufe anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag für das Fach der gymnasialen Oberstufe mit der angegebenen ID gefunden")
    public Response getGostAbiturjahrgangFach(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung einer neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeinformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataGostFaecher(conn, abiturjahr)).get(id);
    	}
    }



    /**
     * Die OpenAPI-Methode für das Anpassen der Daten eines Faches in Bezug auf den
     * Abiturjahrgang der Gymnasiale Oberstufe.
     *
     * @param schema       das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param abiturjahr   der Abitur-Jahrgang
     * @param id           die ID des Faches
     * @param is           der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @PATCH
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}/fach/{id : \\d+}")
    @Operation(summary = "Passt die Daten eines Faches in Bezug den Abiturjahrgang der Gymnasiale Oberstufe an.",
    description = "Passt die Daten eines Faches in Bezug auf den Abiturjahrgang der Gymnasiale Oberstufe an. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen der Fachinformationen "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Fachinformationen integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Fachdaten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Fach-Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchGostAbiturjahrgangFach(
    		@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für die Fachdaten", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostFach.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) { // TODO Anpassung der Benutzerrechte
    		return (new DataGostFaecher(conn, abiturjahr)).patch(id, is);
    	}
    }



    /**
     * Die OpenAPI-Methode für die Abfrage der Fachwahlstatistik für den angegebenen Abitur-Jahrgang der
     * gymnasialen Oberstufe im angegebenen Schema.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abiturjahr    der Abitur-Jahrgang
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Fachwahlstatistik
     */
    @GET
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}/fachwahlstatistik")
    @Operation(summary = "Gibt eine Übersicht von allen Fachwahlen des Abitur-Jahrganges der gymnasialen Oberstufe zurück.",
               description = "Erstellt eine Liste aller in der Datenbank für den angebenen Abitur-Jahrgang vorhanden "
               		       + "Fachwahlen der gymnasialen Oberstufe. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Fächerdaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Fachwahlstatistik",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostStatistikFachwahl.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Fachwahlen anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Fachwahlen gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden")
    public Response getGostAbiturjahrgangFachwahlstatistik(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		if (abiturjahr < 0)
    			return OperationError.NOT_FOUND.getResponse("Fachwahlen sind für den Vorlagen-Abiturjahrgang nicht verfügbar.");
    		return (new DataGostAbiturjahrgangFachwahlen(conn, abiturjahr)).getList();
    	}
    }



    /**
     * Die OpenAPI-Methode für die Abfrage der Fachwahlen für den angegebenen Abitur-Jahrgang der
     * gymnasialen Oberstufe im angegebenen Schema.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abiturjahr    der Abitur-Jahrgang
     * @param halbjahr      das Halbjahr, für welches die Fachwahlen ermittelt werden sollen.
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Liste der Fachwahlen der gymnasialen Oberstufe
     */
    @GET
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}/fachwahlen")
    @Operation(summary = "Gibt eine Übersicht von allen Fachwahlen des Abitur-Jahrganges der gymnasialen Oberstufe zurück.",
               description = "Erstellt eine Liste aller in der Datenbank für den angebenen Abitur-Jahrgang vorhanden "
                           + "Fachwahlen der gymnasialen Oberstufe. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Fächerdaten "
                           + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste der Fachwahlen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostFachwahl.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Fachwahlen anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Fachwahlen gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden")
    public Response getGostAbiturjahrgangFachwahlen(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
            @PathParam("halbjahr") final int halbjahr, @Context final HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		if (abiturjahr < 0)
    			return OperationError.NOT_FOUND.getResponse("Fachwahlen sind für den Vorlagen-Abiturjahrgang nicht verfügbar.");
            return (new DataGostAbiturjahrgangFachwahlen(conn, abiturjahr)).getSchuelerFachwahlenResponse(halbjahr);
        }
    }



    /**
     * Die OpenAPI-Methode für die Abfrage der Laufbahnplanungsdaten der Gymnasialen Oberstufe eines Schülers.
     *
     * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id          die ID des Schülers zu dem die Laufbahnplanungsdaten ausgelesen werden
     * @param request     die Informationen zur HTTP-Anfrage
     *
     * @return die Laufbahnplanungsdaten des Schülers
     */
    @GET
    @Path("/schueler/{id : \\d+}/laufbahnplanung")
    @Operation(summary = "Liest die Laufbahnplanungsdaten für die gymnasiale Oberstufe zu dem Schüler mit der angegebenen ID aus.",
               description = "Liest die Laufbahnplanungsdaten für die gymnasiale Oberstufe zu dem Schüler mit der angegebenen ID "
    		    + "aus der Datenbank aus und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Laufbahnplanungsdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Laufbahnplanungsdaten der gymnasialen Oberstufe des angegebenen Schülers",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = Abiturdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Laufbahnplanungsdaten der Gymnasialen Oberstufe eines Schülers auszulesen.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag für einen Schüler mit Laufbahnplanungsdaten der gymnasialen Oberstufe für die angegebene ID gefunden")
    public Response getGostSchuelerLaufbahnplanung(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeinformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN)) {
    		return (new DataGostSchuelerLaufbahnplanung(conn)).get(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Beratungsdaten für die Laufbahnplanung der Gymnasialen Oberstufe eines Schülers.
     *
     * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id          die ID des Schülers zu dem die Beratungsdaten ausgelesen werden
     * @param request     die Informationen zur HTTP-Anfrage
     *
     * @return die Beratungsdaten zum dem Schüler
     */
    @GET
    @Path("/schueler/{id : \\d+}/laufbahnplanung/beratungsdaten")
    @Operation(summary = "Liest die Beratungsdaten für die Laufbahnplanung der gymnasiale Oberstufe zu dem Schüler mit der angegebenen ID aus.",
               description = "Liest die Beratungsdaten für die Laufbahnplanung der gymnasiale Oberstufe zu dem Schüler mit der angegebenen ID "
    		    + "aus der Datenbank aus und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Beratungsdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Beratungsdaten der gymnasialen Oberstufe des angegebenen Schülers",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = GostLaufbahnplanungBeratungsdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Beratungsdaten der Gymnasialen Oberstufe eines Schülers auszulesen.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag für einen Schüler mit Beratungsdaten der gymnasialen Oberstufe für die angegebene ID gefunden")
    public Response getGostSchuelerLaufbahnplanungBeratungsdaten(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeinformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN)) {
    		return (new DataGostSchuelerLaufbahnplanungBeratungsdaten(conn)).get(id);
    	}
    }

    /**
     * Die OpenAPI-Methode für das Anpassen der Beratungsdaten für die Laufbahnplanung der
     * Gymnasialen Oberstufe eines Schülers.
     *
     * @param schema        das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param schueler_id   die ID des Schülers
     * @param is            der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @PATCH
    @Path("/schueler/{id : \\d+}/laufbahnplanung/beratungsdaten")
    @Operation(summary = "Passt die Beratungsdaten für die Laufbahnplanung der gymnasiale Oberstufe zu dem Schüler mit der angegebenen ID an.",
    description = "Passt die Beratungsdaten für die Laufbahnplanung der gymnasiale Oberstufe zu dem Schüler mit der angegebenen ID an. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen der Beratungsdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Beratungsdaten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchGostSchuelerLaufbahnplanungBeratungsdaten(
    		@PathParam("schema") final String schema, @PathParam("schuelerid") final long schueler_id,
    		@RequestBody(description = "Der Patch für die Beratungsdaten", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostLaufbahnplanungBeratungsdaten.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) { // TODO Anpassung der Benutzerrechte
    		return (new DataGostSchuelerLaufbahnplanungBeratungsdaten(conn)).patch(schueler_id, is);
    	}
    }

    /**
     * Die OpenAPI-Methode für die Abfrage des PDF-Wahlbogens für die Gymnasiale Oberstufe eines Schülers.
     *
     * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id          die ID des Schülers zu dem der Wahlbigen erstellt werden soll
     * @param request     die Informationen zur HTTP-Anfrage
     *
     * @return der PDF-Wahlbogen des Schülers
     */
    @GET
    @Produces("application/pdf")
    @Path("/schueler/{id : \\d+}/wahlbogen")
    @Operation(summary = "Erstellt den PDF-Wahlbogen für die gymnasiale Oberstufe zu dem Schüler mit der angegebenen ID.",
               description = "Erstellt den PDF-Wahlbogen für die gymnasiale Oberstufe zu dem Schüler mit der angegebenen ID. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen des Wahlbogens "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der PDF-Wahlbogen für die gymnasialen Oberstufe des angegebenen Schülers",
                 content = @Content(mediaType = "application/pdf",
                 schema = @Schema(type = "string", format = "binary", description = "PDF-Wahlbogen")))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Wahlbogen für die Gymnasialen Oberstufe eines Schülers zu erstellen.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag für einen Schüler mit Laufbahnplanungsdaten der gymnasialen Oberstufe für die angegebene ID gefunden")
    public Response getGostSchuelerPDFWahlbogen(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeinformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN)) {
    		return PDFGostWahlbogen.query(conn, id);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage einer Fachwahl der Gymnasialen Oberstufe eines Schülers.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param schueler_id   die ID des Schülers
     * @param fach_id       die ID des Faches
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return die Fachwahlen des Schülers zu dem Fach
     */
    @GET
    @Path("/schueler/{schuelerid : \\d+}/fachwahl/{fachid : \\d+}")
    @Operation(summary = "Liest für die gymnasiale Oberstufe die Fachwahlen zu einem Fach von dem angegebenen Schüler aus.",
               description = "Liest für die gymnasiale Oberstufe die Fachwahlen zu einem Fach von dem angegebenen Schüler aus. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Fachwahlen "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Fachwahlen der gymnasialen Oberstfue für das angegebene Fach und den angegebenen Schüler",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = Abiturdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Fachwahlen der Gymnasialen Oberstufe eines Schülers auszulesen.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag für einen Schüler mit Laufbahnplanungsdaten der gymnasialen Oberstufe für die angegebene ID gefunden")
    public Response getGostSchuelerFachwahl(@PathParam("schema") final String schema, @PathParam("schuelerid") final long schueler_id, @PathParam("fachid") final long fach_id, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeinformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN)) {
    		return (new DataGostSchuelerLaufbahnplanung(conn)).getFachwahl(schueler_id, fach_id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Anpassen der Fachwahlen eines Schülers
     * zu einem Fach der Gymnasiale Oberstufe.
     *
     * @param schema        das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param schueler_id   die ID des Schülers
     * @param fach_id       die ID des Faches
     * @param is            der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @PATCH
    @Path("/schueler/{schuelerid : \\d+}/fachwahl/{fachid : \\d+}")
    @Operation(summary = "Passt die Fachwahl eines Schüler in Bezug auf ein Fach der Gymnasiale Oberstufe an.",
    description = "Passt die Fachwahl eines Schüler in Bezug auf ein Fach der Gymnasiale Oberstufe an. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen der Fachwahlen "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Fachwahlen integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Fachwahlen zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Schüler- oder Fach-Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchGostSchuelerFachwahl(
    		@PathParam("schema") final String schema, @PathParam("schuelerid") final long schueler_id, @PathParam("fachid") final long fach_id,
    		@RequestBody(description = "Der Patch für die Fachdaten", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostSchuelerFachwahl.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) { // TODO Anpassung der Benutzerrechte
    		return (new DataGostSchuelerLaufbahnplanung(conn)).patchFachwahl(schueler_id, fach_id, is);
    	}
    }



	/**
	 * Liest die Leistungsdaten in Bezug auf die gymnasiale Oberstufe des Schülers mit der angegebene ID aus der Datenbank und liefert diese zurück.
	 * Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Leistungsdaten besitzt.
	 *
	 * @param schema   das Schema aus dem die Leistungsdaten des Schüler kommen sollen
	 * @param id       die ID des Schülers zu dem die Leistungsdaten geliefert werden sollen
	 *
	 * @param request  die Informationen zur HTTP-Anfrage
	 *
	 * @return die Leistungsdaten in der gymnasialen Oberstufe für den Schüler mit der
	 *         angegebenen ID und die Berechtigung des Datenbank Users
	 */
    @GET
    @Path("/schueler/{id : \\d+}/leistungsdaten")
    @Operation(summary = "Liefert zu der ID des Schülers die zugehörigen Leistungsdaten in Bezug auf die gymnasiale Oberstufe.",
    description = "Liest die Leistungsdaten in Bezug auf die gymnasiale Oberstufe des Schülers mit der angegebene ID aus der "
    		    + "Datenbank und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Leistungsdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Leistungsdaten des Schülers",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = GostLeistungen.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Leistungsdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
    public GostLeistungen getGostSchuelerLeistungsdaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		                                            @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN)) {
	    	return GostSchueler.getLeistungsdaten(conn, id);
    	}
    }



	/**
	 * Liest die Abiturdaten aus den Leistungsdaten der gymnasiale Oberstufe des Schülers mit der angegebene ID aus der Datenbank und liefert diese zurück.
	 * Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Leistungsdaten besitzt.
	 *
	 * @param schema   das Schema aus dem die Abiturdaten des Schüler kommen sollen
	 * @param id       die ID des Schülers zu dem die Abiturdaten geliefert werden sollen
	 *
	 * @param request  die Informationen zur HTTP-Anfrage
	 *
	 * @return die Abiturdaten in der gymnasialen Oberstufe für den Schüler mit der
	 *         angegebenen ID und die Berechtigung des Datenbank Users
	 */
    @GET
    @Path("/schueler/{id : \\d+}/abiturdatenAusLeistungsdaten")
    @Operation(summary = "Liefert zu der ID des Schülers die zugehörigen Abiturdaten, die aus den Leistungsdaten der Oberstufe gewonnen werden können.",
    description = "Liest die Abiturdaten aus den Leistungsdaten der gymnasiale Oberstufe des Schülers mit der angegebene ID aus der "
    		    + "Datenbank und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Leistungsdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Abiturdaten des Schülers",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = Abiturdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Leistungsdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
    public Abiturdaten getGostSchuelerAbiturdatenAusLeistungsdaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		                                                       @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN)) {
    		return GostSchuelerAbitur.getAbiturdatenAusLeistungsdaten(conn, id);
    	}
    }



	/**
	 * Liest die Abiturdaten aus den Abiturtabellen des Schülers mit der angegebene ID und liefert diese zurück.
	 * Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Leistungsdaten besitzt.
	 *
	 * @param schema   das Schema aus dem die Abiturdaten des Schüler kommen sollen
	 * @param id       die ID des Schülers zu dem die Abiturdaten geliefert werden sollen
	 *
	 * @param request  die Informationen zur HTTP-Anfrage
	 *
	 * @return die Abiturdaten in der gymnasialen Oberstufe für den Schüler mit der
	 *         angegebenen ID und die Berechtigung des Datenbank Users
	 */
    @GET
    @Path("/schueler/{id : \\d+}/abiturdaten")
    @Operation(summary = "Liefert zu der ID des Schülers die zugehörigen Abiturdaten aus den entsprechenden Abiturtabellen der SVWS-DB.",
    description = "Liest die Abiturdaten aus den Abiturtabellen des Schülers mit der angegebene ID und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Leistungsdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Abiturdaten des Schülers",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = Abiturdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Leistungsdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
    public Abiturdaten getGostSchuelerAbiturdaten(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN)) {
	    	return GostSchuelerAbitur.getAbiturdaten(conn, id);
    	}
    }



    /**
     * Die OpenAPI-Methode für die Prüfung der Belegprüfung der Abiturdaten.
     *
     * @param schema        das Schema aus dem die Fächerdaten kommen sollen
     * @param abidaten      zu übergebende Fächerdaten für die Prüfung der Belegung im Abitur
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              das Ergebis der Prüfung der Belegprüfung der Abiturdaten
     */
    @POST
    @Path("abitur/belegpruefung/gesamt")
    @Operation(summary = "Führt eine Belegprüfung anhand der übergebenen Abiturdaten durch.",
               description = "Prüft anhand der übergeben Abiturdaten, ob die Belegung in den Abiturdaten korrekt ist oder nicht. Es werden ggf. auch "
               		+ "Belegungsfehler und Hinweise zur Belegung zurückgegeben.")
    @ApiResponse(responseCode = "200", description = "Das Ergebnis der Belegprüfung, ggf. mit Belegungsfehlern",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = GostBelegpruefungErgebnis.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Belegprüfung durchzuführen.")
    @ApiResponse(responseCode = "404", description = "Es wurden keine Fächerdaten zur gymnasialen Oberstufe gefunden")
    public GostBelegpruefungErgebnis getGostAbiturBelegpruefungGesamt(@PathParam("schema") final String schema,
    		@RequestBody(description = "Die Abiturdaten für die Belegprüfung", required = true,
    					 content = @Content(mediaType = MediaType.APPLICATION_JSON,
    					 schema = @Schema(implementation = Abiturdaten.class))) final Abiturdaten abidaten,
    		@Context final HttpServletRequest request) {
    	// TODO Benutzerkompetenz
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
	    	if (!Schule.queryCached(conn).getSchulform().daten.hatGymOb)
	    		throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
	    	// Prüfe die Belegung der Kurse mithilfe des Abiturdaten-Managers und gib das Ergebnis der Belegprüfung zurück.
	    	GostFaecherManager gostFaecherManager = FaecherGost.getFaecherListeGost(conn, abidaten.abiturjahr);
	    	if (gostFaecherManager.isEmpty())
	    		gostFaecherManager = FaecherGost.getFaecherListeGost(conn, null);
			final AbiturdatenManager manager = new AbiturdatenManager(abidaten, gostFaecherManager.toVector(), GostBelegpruefungsArt.GESAMT);
			return manager.getBelegpruefungErgebnis();
    	}
    }


    /**
     * Die OpenAPI-Methode für die Prüfung der Belegprüfung der Abiturdaten nur für die EF_1.
     *
	 * @param schema        das Schema aus dem die Fächerdaten kommen sollen
     * @param abidaten      zu übergebende Fächerdaten für die Prüfung der Belegung nur für die EF_1
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              das Ergebis der Prüfung der Belegprüfung nur für die EF_1
     */
    @POST
    @Path("abitur/belegpruefung/EF1")
    @Operation(summary = "Führt eine Belegprüfung nur für die EF.1 anhand der übergebenen Abiturdaten durch.",
               description = "Prüft anhand der übergeben Abiturdaten, ob die Belegung in den Abiturdaten korrekt ist oder nicht. Es werden ggf. auch "
               		+ "Belegungsfehler und Hinweise zur Belegung zurückgegeben.")
    @ApiResponse(responseCode = "200", description = "Das Ergebnis der Belegprüfung, ggf. mit Belegungsfehlern",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = GostBelegpruefungErgebnis.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Belegprüfung durchzuführen.")
    @ApiResponse(responseCode = "404", description = "Es wurden keine Fächerdaten zur gymnasialen Oberstufe gefunden")
    public GostBelegpruefungErgebnis getGostAbiturBelegpruefungEF1(@PathParam("schema") final String schema,
    		@RequestBody(description = "Die Abiturdaten für die Belegprüfung", required = true,
    		             content = @Content(mediaType = MediaType.APPLICATION_JSON,
					     schema = @Schema(implementation = Abiturdaten.class))) final Abiturdaten abidaten,
    		@Context final HttpServletRequest request) {
    	// TODO Benutzerkompetenz
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
	    	if (!Schule.queryCached(conn).getSchulform().daten.hatGymOb)
	    		throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
	    	// Prüfe die Belegung der Kurse mithilfe des Abiturdaten-Managers und gib das Ergebnis der Belegprüfung zurück.
	    	GostFaecherManager gostFaecherManager = FaecherGost.getFaecherListeGost(conn, abidaten.abiturjahr);
	    	if (gostFaecherManager.isEmpty())
	    		gostFaecherManager = FaecherGost.getFaecherListeGost(conn, null);
			final AbiturdatenManager manager = new AbiturdatenManager(abidaten, gostFaecherManager.toVector(), GostBelegpruefungsArt.EF1);
			return manager.getBelegpruefungErgebnis();
    	}
    }

    /**
     * Die OpenAPI-Methode für die Abfrage der Liste der Blockungen der gymnasialen Oberstufe
     * im angegebenen Schema für den angegebenen Abitur-Jahrgang und das angebene Halbjahr.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abiturjahr    der Abitur-Jahrgang
     * @param halbjahr      das Halbjahr der gymnasialen Oberstufe
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Liste der Blockungen der gymnasialen Oberstufe
     */
    @GET
    @Path("/abiturjahrgang/{abiturjahr : \\d+}/{halbjahr : \\d+}/blockungen")
    @Operation(summary = "Gibt eine Übersicht von allen Blockungen des Abitur-Jahrganges in dem angegebenen Halbjahr der gymnasialen Oberstufe zurück.",
               description = "Erstellt eine Liste aller in der Datenbank vorhanden Blockungen der gymnasialen Oberstufe, "
                           + "welche für den angebenen Abitur-Jahrgang und das angegebene Halbjahr festgelegt wurden.. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Blockungsdaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Blockungs-Listen-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostBlockungListeneintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Blockungsdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Blockungs-Einträge gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden")
    public Response getGostAbiturjahrgangBlockungsliste(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final int halbjahr, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN)) {
    		return (new DataGostBlockungsliste(conn, abiturjahr)).get(halbjahr);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Erstellen einer neuen Blockung der Gymnasiale Oberstufe.
     *
     * @param schema       das Datenbankschema, in welchem die Blockung erstellt wird
     * @param abiturjahr   der Abitur-Jahrgang
     * @param halbjahr     das Halbjahr der gymnasialen Oberstufe
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit der neuen Blockung
     */
    @POST
    @Path("/abiturjahrgang/{abiturjahr : \\d+}/{halbjahr : \\d+}/blockungen/new")
    @Operation(summary = "Erstellt eine neue Blockung und gibt die ID dieser Blockung zurück.",
    description = "Erstellt eine neue Blockung und gibt die ID dieser Blockung zurück."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Fachwahlen "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Blockung wurde erfolgreich angelegt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = GostBlockungsdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Blockung anzulegen.")
    @ApiResponse(responseCode = "404", description = "Keine Fachwahlinformationen zum Anlegen einer Blockung gefunden")
    @ApiResponse(responseCode = "409", description = "Das Abitujahr oder das Halbjahr ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response createGostAbiturjahrgangBlockung(
    		@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
    		@PathParam("halbjahr") final int halbjahr,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) { // TODO Anpassung der Benutzerrechte
    		return (new DataGostBlockungsdaten(conn)).create(abiturjahr, halbjahr);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Löschen von Blockungsdaten der Gymnasialen Oberstufe.
     *
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id         die ID der Blockung
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return eine Response mit dem Status-Code und der ID der gelöschten Blockung
     */
    @DELETE
    @Path("/blockungen/{blockungsid : \\d+}")
    @Operation(summary = "Entfernt die angegebene Blockung der gymnasialen Oberstufe.",
               description = "Entfernt die angegebene Blockung der gymnasialen Oberstufe. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen der Blockungsdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Blockungsdaten der gymnasialen Oberstfue für die angegebene ID wurden erfolgreich gelöscht.",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe zu löschen.")
    @ApiResponse(responseCode = "404", description = "Keine Blockung mit der angebenen ID gefunden.")
    public Response deleteGostBlockung(@PathParam("schema") final String schema, @PathParam("blockungsid") final long id, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeinformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungsdaten(conn)).delete(id);
    	}
    }


   /**
     * Die OpenAPI-Methode für die Abfrage der Blockungsdaten der Gymnasialen Oberstufe.
     *
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id         die ID der Blockung
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return die Blockungsdaten
     */
    @GET
    @Path("/blockungen/{blockungsid : \\d+}")
    @Operation(summary = "Liest für die angegebene Blockung der gymnasialen Oberstufe die grundlegenden Daten aus.",
               description = "Liest für die angegebene Blockung der gymnasialen Oberstufe die grundlegenden Daten aus. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Blockungsdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Blockungsdaten der gymnasialen Oberstfue für die angegebene ID",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = GostBlockungsdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe auszulesen.")
    @ApiResponse(responseCode = "404", description = "Keine Blockung mit der angebenen ID gefunden.")
    public Response getGostBlockung(@PathParam("schema") final String schema, @PathParam("blockungsid") final long id, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeinformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungsdaten(conn)).get(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Rechnen einer Blockung mit den in der DB gespeicherten Blockungsdaten.
     * Die Zwischenergebnisse werden in der Datenbank gespeichert.
     *
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id         die ID der Blockung
     * @param zeit       die Zeit in ms, welche der Blockungsalgorithmus maximal zum Rechnen verwenden soll.
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit einer Liste von IDs der Zwischenergebnisse
     */
    @POST
    @Path("/blockungen/{blockungsid : \\d+}/rechne/{zeit : \\d+}")
    @Operation(summary = "Berechnet für die angegebene Blockung der gymnasialen Oberstufe Zwischenergebnisse und speichert diese in der DB.",
               description = "Berechnet für die angegebene Blockung der gymnasialen Oberstufe Zwischenergebnisse und speichert diese in der DB. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Rechnen einer Blockung "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste der IDs der Zwischenergebnisse",
                 content = @Content(mediaType = "application/json",
                		 array = @ArraySchema(schema = @Schema(implementation = Long.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe auf dem Server zu rechnen.")
    @ApiResponse(responseCode = "404", description = "Keine Blockung mit der angebenen ID gefunden.")
    public Response rechneGostBlockung(@PathParam("schema") final String schema, @PathParam("blockungsid") final long id, @PathParam("zeit") final long zeit, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeinformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungsdaten(conn)).berechne(id, zeit);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Anpassen der Blockungsdaten der Gymnasiale Oberstufe.
     *
     * @param schema     das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id         die ID der Blockung
     * @param is         der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @PATCH
    @Path("/blockungen/{blockungsid : \\d+}")
    @Operation(summary = "Passt die Blockungsdaten der Gymnasiale Oberstufe mit der angegebenen ID an.",
    description = "Passt die Blockungsdaten der Gymnasiale Oberstufe mit der angegebenen ID an."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen der Fachwahlen "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Blockungsdaten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Blockungsdaten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Blockungsdaten-Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchGostBlockung(
    		@PathParam("schema") final String schema, @PathParam("blockungsid") final long id,
    		@RequestBody(description = "Der Patch für die Blockungsdaten", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostBlockungsdaten.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerrechte
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungsdaten(conn)).patch(id, is);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Hinzufügen eines weiteren Kurses zu einer
     * Blockung der Gymnasialen Oberstufe.
     *
     * @param schema       das Datenbankschema, in welchem der Kurs der Blockung erstellt wird
     * @param idBlockung   die ID der Blockung
     * @param idFach       die ID des Faches
     * @param idKursart    die ID der Kursart
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit dem Kurs der Blockung der gymnasialen Oberstufe
     */
    @POST
    @Path("/blockungen/{blockungsid : \\d+}/fach/{fachid : \\d+}/kursart/{kursartid : \\d+}/add")
    @Operation(summary = "Fügt einen Kurs zu einer Blockung der Gymnasialen Oberstufe hinzu.",
               description = "Fügt einen Kurs zu einer Blockung der Gymnasialen Oberstufe hinzu."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Kurses hat.")
    @ApiResponse(responseCode = "200", description = "Der Kurs der Blockung der gymnasialen Oberstufe",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostBlockungKurs.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Kurs hinzuzufügen.")
    @ApiResponse(responseCode = "404", description = "Keine Blockung vorhanden")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addGostBlockungKurs(
    		@PathParam("schema") final String schema, @PathParam("blockungsid") final long idBlockung,
            @PathParam("fachid") final long idFach, @PathParam("kursartid") final int idKursart,
    		@Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungKurs(conn)).addKurs(idBlockung, idFach, idKursart);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Entfernen eines weiteren Kurses zu einer
     * Blockung der Gymnasialen Oberstufe.
     *
     * @param schema       das Datenbankschema, in welchem der Kurs der Blockung erstellt wird
     * @param idBlockung   die ID der Blockung
     * @param idFach       die ID des Faches
     * @param idKursart    die ID der Kursart
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Kurs
     */
    @DELETE
    @Path("/blockungen/{blockungsid : \\d+}/fach/{fachid : \\d+}/kursart/{kursartid : \\d+}/delete")
    @Operation(summary = "Entfernt einen Kurs bei einer Blockung der Gymnasialen Oberstufe.",
    description = "Entfernt einen Kurs bei einer Blockung der Gymnasialen Oberstufe."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen eines Kurses hat.")
    @ApiResponse(responseCode = "200", description = "Der Kurs wurde wurde erfolgreich entfernt.",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostBlockungKurs.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Kurs zu entfernen.")
    @ApiResponse(responseCode = "404", description = "Keine Blockung vorhanden")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response deleteGostBlockungKurs(
    		@PathParam("schema") final String schema, @PathParam("blockungsid") final long idBlockung,
            @PathParam("fachid") final long idFach, @PathParam("kursartid") final int idKursart,
    		@Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungKurs(conn)).deleteKurs(idBlockung, idFach, idKursart);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage eines Kurses einer Blockung der Gymnasialen Oberstufe.
     *
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id         die ID des Kurses der Blockung
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return die Informationen zum Kurs der Blockung
     */
    @GET
    @Path("/blockungen/kurse/{kursid : \\d+}")
    @Operation(summary = "Liest den angegebenen Kurs einer Blockung der gymnasialen Oberstufe aus.",
               description = "Liest den angegebenen Kurs einer Blockung der gymnasialen Oberstufe aus. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Blockungsdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Kurs der Blockung der gymnasialen Oberstfue für die angegebene ID",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = GostBlockungKurs.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe auszulesen.")
    @ApiResponse(responseCode = "404", description = "Kein Kurs einer Blockung mit der angebenen ID gefunden.")
    public Response getGostBlockungKurs(@PathParam("schema") final String schema, @PathParam("kursid") final long id, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeinformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungKurs(conn)).get(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Anpassen eines Kurses einer Blockung der Gymnasialen Oberstufe.
     *
     * @param schema     das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id         die ID des Kurses der Blockung
     * @param is         der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @PATCH
    @Path("/blockungen/kurse/{kursid : \\d+}")
    @Operation(summary = "Passt den angegebenen Kurs einer Blockung der gymnasialen Oberstufe an.",
    description = "Passt den angebenene Kurs der Gymnasiale Oberstufe mit der angegebenen ID an."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen von Blockungsdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Blockungsdaten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Blockungsdaten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Kurs einer Blockung mit der angebenen ID gefunden.")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchGostBlockungKurs(
    		@PathParam("schema") final String schema, @PathParam("kursid") final long id,
    		@RequestBody(description = "Der Patch für der Kurs der Blockung", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostBlockungKurs.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerrechte
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungKurs(conn)).patch(id, is);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Löschen eines Kurses bei einer
     * Blockung der Gymnasialen Oberstufe.
     *
     * @param schema       das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param idKurs       die ID des Kurses
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Kurs
     */
    @DELETE
    @Path("/blockungen/kurse/{kursid : \\d+}")
    @Operation(summary = "Entfernt einen Kurs einer Blockung der Gymnasialen Oberstufe.",
               description = "Entfernt einen Kurs einer Blockung der Gymnasialen Oberstufe. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Kurs wurde wurde erfolgreich gelöscht.",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostBlockungKurs.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Kurs zu löschen.")
    @ApiResponse(responseCode = "404", description = "Der Kurs wurde nicht bei einer Blockung gefunden.")
    public Response deleteGostBlockungKursByID(@PathParam("schema") final String schema, @PathParam("kursid") final long idKurs,
    		                                   @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungKurs(conn)).delete(idKurs);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Daten eines Kurs-Lehrers eines Kurses einer Blockung
     * der Gymnasialen Oberstufe.
     *
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param idKurs     die ID des Kurses der Blockung
     * @param idLehrer   die ID des Lehrers
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return die Informationen zum Kurs-Lehrer des Kurses der Blockung
     */
    @GET
    @Path("/blockungen/kurse/{kursid : \\d+}/lehrer/{lehrerid : \\d+}")
    @Operation(summary = "Liest einen Kurs-Lehrer eines Kurses einer Blockung der Gymnasialen Oberstufe aus.",
               description = "Liest einen Kurs-Lehrer eines Kurses einer Blockung der Gymnasialen Oberstufe aus. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Daten zu dem Kurs-Lehrer.",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostBlockungKursLehrer.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kurslehrer auszulesen.")
    @ApiResponse(responseCode = "404", description = "Der Kurs wurde nicht bei einer Blockung gefunden oder der Lehrer mit der ID existiert nicht.")
    public Response getGostBlockungKurslehrer(@PathParam("schema") final String schema, @PathParam("kursid") final long idKurs,
    		@PathParam("lehrerid") final long idLehrer, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungKursLehrer(conn, idKurs)).get(idLehrer);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Anpassen der Daten eines Kurs-Lehrers eines Kurses einer Blockung
     * der Gymnasialen Oberstufe.
     *
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param idKurs     die ID des Kurses der Blockung
     * @param idLehrer   die ID des Lehrers
     * @param is         der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return die Informationen zum Kurs-Lehrer des Kurses der Blockung
     */
    @PATCH
    @Path("/blockungen/kurse/{kursid : \\d+}/lehrer/{lehrerid : \\d+}")
    @Operation(summary = "Passt einen Kurs-Lehrer eines Kurses einer Blockung der Gymnasialen Oberstufe an.",
               description = "Passt einen Kurs-Lehrer eines Kurses einer Blockung der Gymnasialen Oberstufe an. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Daten wurden erfolgreich angepasst.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zum Kurslehrer anzupassen.")
    @ApiResponse(responseCode = "404", description = "Der Kurs wurde nicht bei einer Blockung gefunden oder der Lehrer mit der ID existiert nicht.")
    public Response patchGostBlockungKurslehrer(@PathParam("schema") final String schema, @PathParam("kursid") final long idKurs,
    		@PathParam("lehrerid") final long idLehrer,
    		@RequestBody(description = "Der Patch für der Kurs der Blockung", required = true, content =
				@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostBlockungKursLehrer.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungKursLehrer(conn, idKurs)).patch(idLehrer, is);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Hinzufügen eines Kurs-Lehrers zu einem Kurs einer Blockung
     * der Gymnasialen Oberstufe.
     *
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param idKurs     die ID des Kurses der Blockung
     * @param idLehrer   die ID des Lehrers
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return die Informationen zum Kurs-Lehrer des Kurses der Blockung
     */
    @POST
    @Path("/blockungen/kurse/{kursid : \\d+}/lehrer/{lehrerid : \\d+}/add")
    @Operation(summary = "Fügt einen Kurs-Lehrer zu einem Kurs einer Blockung der Gymnasialen Oberstufe hinzu.",
               description = "Fügt einen Kurs-Lehrer zu einem Kurs einer Blockung der Gymnasialen Oberstufe hinzu. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Hinzufügen besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Daten zu dem hinzugefügten Kurs-Lehrer.",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostBlockungKursLehrer.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Kurslehrer hinzuzufügen.")
    @ApiResponse(responseCode = "404", description = "Der Kurs wurde nicht bei einer Blockung gefunden oder der Lehrer mit der ID existiert nicht.")
    public Response addGostBlockungKurslehrer(@PathParam("schema") final String schema, @PathParam("kursid") final long idKurs,
    		@PathParam("lehrerid") final long idLehrer, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungKursLehrer(conn, idKurs)).addKurslehrer(idLehrer);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Entfernen eines Kurs-Lehrers eines Kurs einer Blockung
     * der Gymnasialen Oberstufe.
     *
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param idKurs     die ID des Kurses der Blockung
     * @param idLehrer   die ID des Lehrers
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Response
     */
    @DELETE
    @Path("/blockungen/kurse/{kursid : \\d+}/lehrer/{lehrerid : \\d+}")
    @Operation(summary = "Entfernt einen Kurs-Lehrer eines Kurses einer Blockung der Gymnasialen Oberstufe.",
               description = "Entfernt einen Kurs-Lehrer eines Kurses einer Blockung der Gymnasialen Oberstufe. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Hinzufügen besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Daten wurden erfolgreich entfernt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Kurslehrer zu entfernen.")
    @ApiResponse(responseCode = "404", description = "Der Kurs wurde nicht bei einer Blockung gefunden oder der Lehrer mit der ID existiert nicht bei dem Kurs.")
    public Response deleteGostBlockungKurslehrer(@PathParam("schema") final String schema, @PathParam("kursid") final long idKurs,
    		@PathParam("lehrerid") final long idLehrer, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungKursLehrer(conn, idKurs)).deleteKurslehrer(idLehrer);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Hinzufügen einer weiteren Schiene zu einer
     * Blockung der Gymnasialen Oberstufe.
     *
     * @param schema       das Datenbankschema, in welchem die Schiene der Blockung erstellt wird
     * @param idBlockung   die ID der Blockung
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit der Schiene der Blockung der gymnasialen Oberstufe
     */
    @POST
    @Path("/blockungen/{blockungsid : \\d+}/addschiene")
    @Operation(summary = "Fügt eine Schiene zu einer Blockung der Gymnasialen Oberstufe hinzu.",
               description = "Fügt eine Schiene zu einer Blockung der Gymnasialen Oberstufe hinzu."
    		               + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Schiene hat.")
    @ApiResponse(responseCode = "200", description = "Die Schiene der Blockung der gymnasialen Oberstufe",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostBlockungSchiene.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Schiene hinzuzufügen.")
    @ApiResponse(responseCode = "404", description = "Keine Blockung vorhanden")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addGostBlockungSchiene(
    		@PathParam("schema") final String schema, @PathParam("blockungsid") final long idBlockung,
    		@Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungSchiene(conn)).addSchiene(idBlockung);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Entfernen einer Schiene bei einer
     * Blockung der Gymnasialen Oberstufe.
     *
     * @param schema       das Datenbankschema, in welchem die Schiene der Blockung erstellt wird
     * @param idBlockung   die ID der Blockung
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return eine Response mit dem Status-Code und ggf. der gelöschten Schiene
     */
    @DELETE
    @Path("/blockungen/{blockungsid : \\d+}/deleteschiene")
    @Operation(summary = "Entfernt eine Schiene bei einer Blockung der Gymnasialen Oberstufe.",
    description = "Entfernt eine Schiene bei einer Blockung der Gymnasialen Oberstufe."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen einer Schiene hat.")
    @ApiResponse(responseCode = "200", description = "Die Schiene wurde wurde erfolgreich entfernt.",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostBlockungSchiene.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Schiene zu entfernen.")
    @ApiResponse(responseCode = "404", description = "Keine Blockung vorhanden")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response deleteGostBlockungSchiene(
    		@PathParam("schema") final String schema, @PathParam("blockungsid") final long idBlockung,
    		@Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungSchiene(conn)).deleteSchiene(idBlockung);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage einer Schiene einer Blockung der Gymnasialen Oberstufe.
     *
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id         die ID der Schiener der Blockung
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return die Informationen zur Schiene der Blockung
     */
    @GET
    @Path("/blockungen/schiene/{schienenid : \\d+}")
    @Operation(summary = "Liest die angegebene Schiene einer Blockung der gymnasialen Oberstufe aus.",
               description = "Liest die angegebene Schiene einer Blockung der gymnasialen Oberstufe aus. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Blockungsdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Schiene der Blockung der gymnasialen Oberstfue für die angegebene ID",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = GostBlockungSchiene.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe auszulesen.")
    @ApiResponse(responseCode = "404", description = "Keine Schiene einer Blockung mit der angebenen ID gefunden.")
    public Response getGostBlockungSchiene(@PathParam("schema") final String schema, @PathParam("schienenid") final long id, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeinformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungSchiene(conn)).get(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Anpassen einer Schiene einer Blockung der Gymnasialen Oberstufe.
     *
     * @param schema     das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id         die ID der Schiene der Blockung
     * @param is         der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @PATCH
    @Path("/blockungen/schiene/{schienenid : \\d+}")
    @Operation(summary = "Passt die angegebene Schiene einer Blockung der gymnasialen Oberstufe an.",
    description = "Passt die angegebene Schiene der Gymnasiale Oberstufe mit der angegebenen ID an."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen von Blockungsdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Blockungsdaten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Blockungsdaten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Keine Schiene einer Blockung mit der angebenen ID gefunden.")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchGostBlockungSchiene(
    		@PathParam("schema") final String schema, @PathParam("schienenid") final long id,
    		@RequestBody(description = "Der Patch für die Schiene der Blockung", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostBlockungSchiene.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerrechte
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungSchiene(conn)).patch(id, is);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Löschen einer Schiene bei einer
     * Blockung der Gymnasialen Oberstufe.
     *
     * @param schema       das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param idSchiene    die ID der Schiene
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit dem Status und ggf. der gelöschten Schiene
     */
    @DELETE
    @Path("/blockungen/schiene/{schienenid : \\d+}")
    @Operation(summary = "Entfernt eine Schiene einer Blockung der Gymnasialen Oberstufe.",
               description = "Entfernt eine Schiene einer Blockung der Gymnasialen Oberstufe. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Schiene wurde wurde erfolgreich gelöscht.",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostBlockungSchiene.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schiene zu löschen.")
    @ApiResponse(responseCode = "404", description = "Die Schiene wurde nicht bei einer Blockung gefunden.")
    public Response deleteGostBlockungSchieneByID(@PathParam("schema") final String schema, @PathParam("schienenid") final long idSchiene,
    		                                   @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungSchiene(conn)).delete(idSchiene);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Hinzufügen einer Regel zu einer
     * Blockung der Gymnasialen Oberstufe.
     *
     * @param schema       das Datenbankschema, in welchem die Regel der Blockung erstellt wird
     * @param idBlockung   die ID der Blockung
     * @param typRegel     der Regel-Typ
     * @param regelParameter  die Parameter der Regel oder null, falls Default-Parameter verwendet werden sollen
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit der Regel der Blockung der gymnasialen Oberstufe
     */
    @POST
    @Path("/blockungen/{blockungsid : \\d+}/addregel/{regeltyp : \\d+}")
    @Operation(summary = "Fügt eine Regel zu einer Blockung der Gymnasialen Oberstufe hinzu.",
               description = "Fügt eine Regel zu einer Blockung der Gymnasialen Oberstufe hinzu."
    		               + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Regel hat.")
    @ApiResponse(responseCode = "200", description = "Die Regel der Blockung der gymnasialen Oberstufe",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostBlockungRegel.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Regel hinzuzufügen.")
    @ApiResponse(responseCode = "404", description = "Keine Blockung vorhanden")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addGostBlockungRegel(
    		@PathParam("schema") final String schema, @PathParam("blockungsid") final long idBlockung,
    		@PathParam("regeltyp") final int typRegel,
            @RequestBody(description = "Die Regel-Parameter", required = false, content = @Content(mediaType = MediaType.APPLICATION_JSON,
            array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> regelParameter,
    		@Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungRegel(conn)).addRegel(idBlockung, typRegel, regelParameter);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage einer Regel einer Blockung der Gymnasialen Oberstufe.
     *
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id         die ID der Schiener der Blockung
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return die Informationen zur Regel der Blockung
     */
    @GET
    @Path("/blockungen/regeln/{regelid : \\d+}")
    @Operation(summary = "Liest die angegebene Regel einer Blockung der gymnasialen Oberstufe aus.",
               description = "Liest die angegebene Regel einer Blockung der gymnasialen Oberstufe aus. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Blockungsdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Regel der Blockung der gymnasialen Oberstfue für die angegebene ID",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = GostBlockungRegel.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe auszulesen.")
    @ApiResponse(responseCode = "404", description = "Keine Regel einer Blockung mit der angebenen ID gefunden.")
    public Response getGostBlockungRegel(@PathParam("schema") final String schema, @PathParam("regelid") final long id, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeinformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungRegel(conn)).get(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Anpassen einer Regel einer Blockung der Gymnasialen Oberstufe.
     *
     * @param schema     das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id         die ID der Regel der Blockung
     * @param is         der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @PATCH
    @Path("/blockungen/regeln/{regelid : \\d+}")
    @Operation(summary = "Passt die angegebene Regel einer Blockung der gymnasialen Oberstufe an.",
    description = "Passt die angegebene Regel der Gymnasiale Oberstufe mit der angegebenen ID an."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen von Blockungsdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Blockungsdaten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Blockungsdaten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Keine Regel einer Blockung mit der angebenen ID gefunden.")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchGostBlockungRegel(
    		@PathParam("schema") final String schema, @PathParam("regelid") final long id,
    		@RequestBody(description = "Der Patch für die Regel der Blockung", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostBlockungRegel.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerrechte
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungRegel(conn)).patch(id, is);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Löschen einer Regel bei einer
     * Blockung der Gymnasialen Oberstufe.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param idRegel   die ID der Regel
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit dem Status und ggf. der gelöschten Regel
     */
    @DELETE
    @Path("/blockungen/regeln/{regelid : \\d+}")
    @Operation(summary = "Entfernt eine Regel einer Blockung der Gymnasialen Oberstufe.",
               description = "Entfernt eine Regel einer Blockung der Gymnasialen Oberstufe. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Regel wurde wurde erfolgreich gelöscht.",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostBlockungRegel.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Regel zu löschen.")
    @ApiResponse(responseCode = "404", description = "Die Regel wurde nicht bei einer Blockung gefunden.")
    public Response deleteGostBlockungRegelByID(@PathParam("schema") final String schema, @PathParam("regelid") final long idRegel,
    		                                   @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungRegel(conn)).delete(idRegel);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage von Blockungsergebnissen zu Blockungen der Gymnasialen Oberstufe.
     *
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id         die ID des Blockungsergebnisses
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return die Daten des Blockungsergebnisses
     */
    @GET
    @Path("/blockungen/zwischenergebnisse/{ergebnisid : \\d+}")
    @Operation(summary = "Liest für das angegebene Blockungsergebnis einer Blockung der gymnasialen Oberstufe die Daten aus.",
               description = "Liest für das angegebene Blockungsergebnis einer Blockung der gymnasialen Oberstufe die Daten aus. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Blockungsergebnisse "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Blockungsergebnisse der gymnasialen Oberstfue für die angegebene ID",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = GostBlockungsergebnis.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Blockungsergebnisse einer Blockung der Gymnasialen Oberstufe auszulesen.")
    @ApiResponse(responseCode = "404", description = "Keine Blockung mit der angebenen ID gefunden.")
    public Response getGostBlockungsergebnis(@PathParam("schema") final String schema, @PathParam("ergebnisid") final long id, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeinformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungsergebnisse(conn)).get(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Aktivieren bzw. Persistieren eines Blockungsergebnisses
     * der Gymnasialen Oberstufe in der Kursliste und den Leistungsdaten von Schülern.
     *
     * @param schema       das Datenbankschema
     * @param id           die ID des zu aktivierenden Blockungsergebnisses
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/activate")
    @Operation(summary = "Aktiviert bzw. persistiert das Blockungsergebnis.",
    description = "Aktiviert bzw. persistiert das Blockungsergebnis. Dies ist nur erlaubt, wenn keine aktivierte "
    		    + "Blockung in der DB vorliegt. Beim Aktivieren wird die Kursliste und die Leistungsdaten der "
    		    + "Schüler entsprechend befüllt."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Aktivieren eines "
    		    + "Blockungsergebnisses besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Zuordnung wurde erfolgreich gelöscht.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um ein Blockungsergebnis zu aktivieren.")
    @ApiResponse(responseCode = "404", description = "Keine oder nicht alle Daten zu dem Ergebnis gefunden, um dieses zu aktiveren")
    @ApiResponse(responseCode = "409", description = "Es wurde bereits eine Blockung aktiviert")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response activateGostBlockungsergebnis(@PathParam("schema") final String schema, @PathParam("ergebnisid") final long id, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerrechte
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungsergebnisse(conn)).aktiviere(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Löschen von Blockungsergebnissen einer Blockung der Gymnasialen Oberstufe.
     *
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id         die ID des Blockungsergebnisses
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return eine Response mit dem Status-Code und ggf. der ID des gelöschten Zwischenergebnisses
     */
    @DELETE
    @Path("/blockungen/zwischenergebnisse/{ergebnisid : \\d+}")
    @Operation(summary = "Entfernt das angegebene Zwischenergebnis einer Blockung der gymnasialen Oberstufe.",
               description = "Entfernt das angegebene Zwischenergebnis einer Blockung der gymnasialen Oberstufe. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen des Zwischenergebnisses "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Das Zwischenergebnis einer Blockung der gymnasialen Oberstfue für die angegebene ID wurde erfolgreich gelöscht.",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um das Zwischenergebnis einer Blockung der Gymnasialen Oberstufe zu löschen.")
    @ApiResponse(responseCode = "404", description = "Keine Blockung mit der angebenen ID gefunden.")
    public Response deleteGostBlockungsergebnis(@PathParam("schema") final String schema, @PathParam("ergebnisid") final long id, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeinformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungsergebnisse(conn)).delete(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Duplizieren der Definition einer Blockung mit den in der DB
     * gespeicherten Blockungsdaten ausgehend von dem angegebenen Zwischenergebnis.
     * Dieses Zwischenergebnis wird als einziges mit dupliziert und dient bei dem Blockungsduplikat
     * als Vorlage für die Definition von Regeln.
     *
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id         die ID des Zwischenergebnisses
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit der Duplizierten Blockung
     */
    @GET
    @Path("/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/dupliziere")
    @Operation(summary = "Dupliziert die zu dem angegebenen Zwischenergebnis gehörende Blockung.",
               description = "Dupliziert zu dem angegebenen Zwischenergebnis gehörende Blockung der gymnasialen Oberstufe. "
               	+ "Das Zwischenergebnis wird als einziges mit dupliziert und dient bei dem Blockungsduplikat. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Duplizieren einer Blockung "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Blockungsdaten der gymnasialen Oberstfue des Duplikats als Vorlage für die Definition von Regeln",
    	content = @Content(mediaType = "application/json",
    	schema = @Schema(implementation = GostBlockungsdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe zu duplizieren.")
    @ApiResponse(responseCode = "404", description = "Kein Blockungsergebnis mit der angebenen ID gefunden.")
    public Response dupliziereGostBlockungMitErgebnis(@PathParam("schema") final String schema, @PathParam("ergebnisid") final long id, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeinformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungsdaten(conn)).dupliziere(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Hochschreiben der Definition einer Blockung mit den in der DB
     * gespeicherten Blockungsdaten ausgehend von dem angegebenen Zwischenergebnis in das nächste Halbjahr.
     * Dieses Zwischenergebnis wird mit hochgeschrieben und dient bei der hochgeschriebenen Blockung
     * auch als Vorlage für die Definition von Regeln. Nicht mehr vorhandene Fachwahlen werden
     * ggf. automatisch entfernt. Es werden aber keine neuen Kurse oder Zuordnung neu generiert.
     *
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id         die ID des Zwischenergebnisses
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit der hochgeschriebenen Blockung
     */
    @GET
    @Path("/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/hochschreiben")
    @Operation(summary = "Schreibt die zum Ergebnis gehörende Blockung mit dem Ergebnis in das nächste Halbjahr hoch.",
               description = "Schreibt die zum Ergebnis gehörende Blockung mit dem Ergebnis in das nächste Halbjahr hoch. "
               	+ "Nicht mehr vorhandene Fachwahlen werden ggf. automatisch entfernt. Es werden aber keine neuen Kurse "
               	+ "oder Zuordnung neu generiert. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Hochschreiben einer Blockung "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Blockung und das Ergebnis wurde erfolgreich hochgeschrieben.",
    	content = @Content(mediaType = "application/json",
    	schema = @Schema(implementation = GostBlockungsdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Blockung der Gymnasialen Oberstufe hochzuschreiben.")
    @ApiResponse(responseCode = "404", description = "Kein Blockungsergebnis mit der angebenen ID gefunden.")
    public Response schreibeGostBlockungsErgebnisHoch(@PathParam("schema") final String schema, @PathParam("ergebnisid") final long id, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeinformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungsdaten(conn)).hochschreiben(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Erstellen einer Kurszuordnung zu einem Schüler bei einem Blockungsergebnis einer
     * Blockung der Gymnasialen Oberstufe.
     *
     * @param schema       das Datenbankschema, in welchem die Blockung erstellt wird
     * @param idErgebnis   die ID des Blockungsergebnisses
     * @param idSchueler   die ID der Schiene der Blockung
     * @param idKurs       die ID des Kurses der Blockung
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/schueler/{schuelerid : \\d+}/kurs/{kursid: \\d+}")
    @Operation(summary = "Erstellt eine Kurszuordnung zu einem Schüler bei einem Blockungsergebnis einer Blockung der Gymnasialen Oberstufe.",
    description = "Erstellt eine Kurszuordnung zu einem Schüler bei einem Blockungsergebnis einer Blockung der Gymnasialen Oberstufe."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Zuordnung "
    		    + "besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Zuordnung wurde erfolgreich angelegt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Zuordnung anzulegen.")
    @ApiResponse(responseCode = "404", description = "Kein geeignetes Zwischenergebnis, Schüler oder Kurs für die Zuordnung vorhanden")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response createGostBlockungsergebnisKursSchuelerZuordnung(
    		@PathParam("schema") final String schema, @PathParam("ergebnisid") final long idErgebnis,
            @PathParam("schuelerid") final long idSchueler, @PathParam("kursid") final long idKurs,
    		@Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungsergebnisse(conn)).createKursSchuelerZuordnung(idErgebnis, idSchueler, idKurs);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Verschieben eines Schüler zwischen zwei Kursen bei einem Blockungsergebnis einer
     * Blockung der Gymnasialen Oberstufe.
     *
     * @param schema       das Datenbankschema, in welchem die Blockung erstellt wird
     * @param idErgebnis   die ID des Blockungsergebnisses
     * @param idSchueler   die ID der Schiene der Blockung
     * @param idKursAlt    die ID des Kurses der Blockung in Bezug auf die alte Zuordnung
     * @param idKursNeu    die ID des Kurses der Blockung in Bezug auf die neue Zuordnung
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/schueler/{schuelerid : \\d+}/kurs/{kursid: \\d+}/zu/{kursidneu: \\d+}")
    @Operation(summary = "Verschiebt einen Schüler zwischen zwei Kursen bei einem Blockungsergebnis einer Blockung der Gymnasialen Oberstufe.",
    description = "Verschiebt einen Schüler zwischen zwei Kursen bei einem Blockungsergebnis einer Blockung der Gymnasialen Oberstufe."
                + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Verschieben besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Zuordnung wurde erfolgreich angelegt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um das Verschieben vorzunehmen.")
    @ApiResponse(responseCode = "404", description = "Kein geeignetes Zwischenergebnis, Schüler oder Kurs für die Zuordnung vorhanden")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response updateGostBlockungsergebnisKursSchuelerZuordnung(
            @PathParam("schema") final String schema, @PathParam("ergebnisid") final long idErgebnis,
            @PathParam("schuelerid") final long idSchueler, @PathParam("kursid") final long idKursAlt, @PathParam("kursidneu") final long idKursNeu,
            @Context final HttpServletRequest request) {
        // TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
            return (new DataGostBlockungsergebnisse(conn)).updateKursSchuelerZuordnung(idErgebnis, idSchueler, idKursAlt, idKursNeu);
        }
    }


    /**
     * Die OpenAPI-Methode für das Löschen einer Kurszuordnung zu einem Schüler bei einem Blockungsergebnis einer
     * Blockung der Gymnasialen Oberstufe.
     *
     * @param schema       das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param idErgebnis   die ID des Blockungsergebnisses
     * @param idSchueler   die ID der Schiene der Blockung
     * @param idKurs       die ID des Kurses der Blockung
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return eine Response mit dem Status-Code
     */
    @DELETE
    @Path("/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/schueler/{schuelerid : \\d+}/kurs/{kursid: \\d+}")
    @Operation(summary = "Entfernt eine Kurszuordnung zu einem Schüler bei einem Blockungsergebniss einer Blockung der Gymnasialen Oberstufe.",
               description = "Entfernt eine Kurszuordnung zu einem Schüler bei einem Blockungsergebniss einer Blockung der Gymnasialen Oberstufe. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Zuordnung wurde erfolgreich gelöscht.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Zuordnung zu löschen.")
    @ApiResponse(responseCode = "404", description = "Das Zwischenergebnis, der Schüler oder der Kurs wurde nicht in einer gültigen Zuordnung gefunden.")
    public Response deleteGostBlockungsergebnisKursSchuelerZuordnung(@PathParam("schema") final String schema, @PathParam("ergebnisid") final long idErgebnis,
    		                                    @PathParam("schuelerid") final long idSchueler, @PathParam("kursid") final long idKurs,
    		                                    @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostBlockungsergebnisse(conn)).deleteKursSchuelerZuordnung(idErgebnis, idSchueler, idKurs);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Erstellen einer Kurszuordnung zu einer Schiene bei einem Blockungsergebnis einer
     * Blockung der Gymnasialen Oberstufe.
     *
     * @param schema       das Datenbankschema, in welchem die Blockung erstellt wird
     * @param idErgebnis   die ID des Blockungsergebnisses
     * @param idSchiene    die ID der Schiene der Blockung
     * @param idKurs       die ID des Kurses der Blockung
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/schiene/{schienenid : \\d+}/kurs/{kursid: \\d+}")
    @Operation(summary = "Erstellt eine Kurszuordnung zu einer Schiene bei einem Blockungsergebnis einer Blockung der Gymnasialen Oberstufe.",
    description = "Erstellt eine Kurszuordnung zu einer Schiene bei einem Blockungsergebnis einer Blockung der Gymnasialen Oberstufe."
                + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Zuordnung "
                + "besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Zuordnung wurde erfolgreich angelegt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Zuordnung anzulegen.")
    @ApiResponse(responseCode = "404", description = "Kein geeignetes Zwischenergebnis, Schiene oder Kurs für die Zuordnung vorhanden")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response createGostBlockungsergebnisKursSchieneZuordnung(
            @PathParam("schema") final String schema, @PathParam("ergebnisid") final long idErgebnis,
            @PathParam("schienenid") final long idSchiene, @PathParam("kursid") final long idKurs,
            @Context final HttpServletRequest request) {
        // TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
            return (new DataGostBlockungsergebnisse(conn)).createKursSchieneZuordnung(idErgebnis, idSchiene, idKurs);
        }
    }


    /**
     * Die OpenAPI-Methode für das Verschieben eines Kurse von einer Schiene zu einer anderen Schiene
     * bei einem Blockungsergebnis einer Blockung der Gymnasialen Oberstufe.
     *
     * @param schema         das Datenbankschema, in welchem die Blockung erstellt wird
     * @param idErgebnis     die ID des Blockungsergebnisses
     * @param idSchieneAlt   die ID der Schiene der Blockung in Bezug auf die alten Zuordnung
     * @param idKurs         die ID des Kurses der Blockung
     * @param idSchieneNeu   die ID der Schiene der Blockung in Bezug auf die neue Zuordnung
     * @param request        die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/schiene/{schienenid: \\d+}/kurs/{kursid: \\d+}/zu/{schienenidneu: \\d+}")
    @Operation(summary = "Verschiebt einen Kurse zwischen zwei Schienen bei einem Blockungsergebnis einer Blockung der Gymnasialen Oberstufe.",
    description = "Verschiebt einen Kurse zwischen zwei Schienen bei einem Blockungsergebnis einer Blockung der Gymnasialen Oberstufe."
                + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Verschieben besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Zuordnung wurde erfolgreich angelegt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Kurs zu verschieben.")
    @ApiResponse(responseCode = "404", description = "Kein geeignetes Zwischenergebnis, Schiene oder Kurs für die Zuordnung vorhanden")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response updateGostBlockungsergebnisKursSchieneZuordnung(
            @PathParam("schema") final String schema, @PathParam("ergebnisid") final long idErgebnis,
            @PathParam("schienenid") final long idSchieneAlt, @PathParam("kursid") final long idKurs, @PathParam("schienenidneu") final long idSchieneNeu,
            @Context final HttpServletRequest request) {
        // TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
            return (new DataGostBlockungsergebnisse(conn)).updateKursSchieneZuordnung(idErgebnis, idKurs, idSchieneAlt, idSchieneNeu);
        }
    }


    /**
     * Die OpenAPI-Methode für das Löschen einer Kurszuordnung zu einer Schiene bei einem Blockungsergebnis einer
     * Blockung der Gymnasialen Oberstufe.
     *
     * @param schema       das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param idErgebnis   die ID des Blockungsergebnisses
     * @param idSchiene    die ID der Schiene der Blockung
     * @param idKurs       die ID des Kurses der Blockung
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return eine Response mit dem Status-Code
     */
    @DELETE
    @Path("/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/schiene/{schienenid : \\d+}/kurs/{kursid: \\d+}")
    @Operation(summary = "Entfernt eine Kurszuordnung zu einer Schiene bei einem Blockungsergebniss einer Blockung der Gymnasialen Oberstufe.",
               description = "Entfernt eine Kurszuordnung zu einer Schiene bei einem Blockungsergebniss einer Blockung der Gymnasialen Oberstufe. "
                + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Zuordnung wurde erfolgreich gelöscht.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Zuordnung zu löschen.")
    @ApiResponse(responseCode = "404", description = "Das Zwischenergebnis, der Schiene oder der Kurs wurde nicht in einer gültigen Zuordnung gefunden.")
    public Response deleteGostBlockungsergebnisKursSchieneZuordnung(@PathParam("schema") final String schema, @PathParam("ergebnisid") final long idErgebnis,
                                                @PathParam("schienenid") final long idSchiene, @PathParam("kursid") final long idKurs,
                                                @Context final HttpServletRequest request) {
        // TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
            return (new DataGostBlockungsergebnisse(conn)).deleteKursSchieneZuordnung(idErgebnis, idSchiene, idKurs);
        }
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Liste der Fachkombinationen für die Laufbahnplanung
     * der gymnasialen Oberstufe für den angegebenen Abitur-Jahrgang.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abiturjahr    der Abitur-Jahrgang
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Liste der Fachkombinationen
     */
    @GET
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}/fachkombinationen")
    @Operation(summary = "Gibt die Informationen zu den Fachkombinationen für die Laufbahnplanung des Abitur-Jahrganges der gymnasialen Oberstufe zurück.",
               description = "Erstellt eine Liste mit den Informationen zu den Fachkombinationen für die Laufbahnplanung des Abitur-Jahrganges der gymnasialen Oberstufe. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Fachkombinationen besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Liste der Fachkombinationen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostJahrgangFachkombination.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Fachkombinationen anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Fachkombinationen gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden")
    public Response getGostAbiturjahrgangFachkombinationen(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostJahrgangFachkombinationen(conn, abiturjahr)).getList();
    	}
    }


    /**
     * Die OpenAPI-Methode für das Anpassen einer Fachkombination für Fächer der Gymnasialen Oberstufe.
     *
     * @param schema     das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id         die ID der Regel für die Fachkombination
     * @param is         der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @PATCH
    @Path("/fachkombination/{id : \\d+}")
    @Operation(summary = "Passt die Fachkombination mit der angegebenen ID an.",
    description = "Passt die Fachkombination mit der angegebenen ID an."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen von Fachkombinationen "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Fachkombination integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Fachkombinationen zu ändern.")
    @ApiResponse(responseCode = "404", description = "Keine Fachkombination mit der angebenen ID gefunden oder es wurden kein gültiges Fach als ID übergeben.")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchGostFachkombination(
    		@PathParam("schema") final String schema, @PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für die Fachkombination", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostJahrgangFachkombination.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerrechte
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostJahrgangFachkombinationen(conn, -1 /*nicht relevant*/)).patch(id, is);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Löschen einer Regel zu einer Fchkombination
     * der Gymnasialen Oberstufe.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id        die ID der Regel der Fachkombination
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit dem Status und ggf. der gelöschten Regel zur Fachkombination
     */
    @DELETE
    @Path("/fachkombination/{id : \\d+}")
    @Operation(summary = "Entfernt eine Regel zu einer Fachkombination in der Gymnasialen Oberstufe.",
               description = "Entfernt eine Regel zu einer Fachkombination in der Gymnasialen Oberstufe. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Regel wurde wurde erfolgreich gelöscht.",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostJahrgangFachkombination.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Regel zu der Fachkombination zu löschen.")
    @ApiResponse(responseCode = "404", description = "Die Regel zu der Fachkombination wurde nicht gefunden.")
    public Response deleteGostFachkombination(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostJahrgangFachkombinationen(conn, -1 /* nicht relevant */)).delete(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Hinzufügen einer weiteren Fachkombination der Gymnasialen Oberstufe mit dem angegebenen Typ
     * bei dem angegebenen Abiturjahrgang.
     *
     * @param schema       das Datenbankschema, in welchem die Fachkombination erstellt wird
     * @param abiturjahr   der Abitur-Jahrgang
     * @param typ          der Typ der Fachkombination (0: Wahl von Fach 2 ist in Kombination mit Fach 1 unzulässig, 1: Wahl von Fach 2 ist bei Wahl von Fach 1 nötig)
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit der Schiene der Blockung der gymnasialen Oberstufe
     */
    @POST
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}/fachkombinationen/add/{typ : \\d+}")
    @Operation(summary = "Fügt eine Regel zu einer Fachkombination der Gymnasialen Oberstufe bei dem angegebenen Abiturjahrgang hinzu.",
               description = "Fügt eine Regel zu einer Fachkombination der Gymnasialen Oberstufe bei dem angegebenen Abiturjahrgang hinzu."
    		               + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer solchen Fachkombination hat.")
    @ApiResponse(responseCode = "200", description = "Die Regel zur Fachkombination bezüglich der gymnasialen Oberstufe wurde erstellt",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostJahrgangFachkombination.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Regel für eine Fachkombination hinzuzufügen.")
    @ApiResponse(responseCode = "404", description = "Abiturjahr nicht vorhanden")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addGostAbiturjahrgangFachkombination(
    		@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("typ") final int typ,
    		@Context final HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN)) {
    		return (new DataGostJahrgangFachkombinationen(conn, abiturjahr)).add(typ);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Laufbahplanungs-Daten der gymnasialen Oberstufe
     * in Bezug auf den angegebenen Schüler als GZIP-Json.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id        die ID des Schülers
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die Laufbahnplanungs-Daten
     */
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/schueler/{id : \\d+}/laufbahnplanung/export")
    @Operation(summary = "Liefert die Laufbahnplanungsdaten der gymnasialen Oberstufe für den angegebenen Schüler (GZip-komprimiert).",
    	description = "Liest die Laufbahnplanungsdaten der gymnasialen Oberstufe für den angegebenen Schüler aus der Datenbank "
    			+ "und liefert diese GZip-komprimiert zurück. Dabei wird geprüft, ob der SVWS-Benutzer die "
    			+ "notwendige Berechtigung zum Auslesen der Daten besitzt.")
    @ApiResponse(responseCode = "200", description = "Die GZip-komprimierten Laufbahndaten der gymnasialen Obertufe",
                 content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM,
                 schema = @Schema(type = "string", format = "binary", description = "Die GZip-komprimierten Laufbahndaten der gymnasialen Obertufe")))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Laufbahndaten auszulesen.")
    @ApiResponse(responseCode = "404", description = "Es wurden nicht alle benötigten Daten für das Erstellen der Laufbahn-Daten gefunden.")
    public Response exportGostSchuelerLaufbahnplanung(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN)) {
	    	return (new DataGostSchuelerLaufbahnplanung(conn)).exportGZip(id);
    	}
    }

    /**
     * Die OpenAPI-Methode für den Import von Laufbahnplanungsdaten eines Schülers der gymnasialen Oberstufe
     * mit der angegebenen ID.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id            die ID des Schülers
     * @param multipart     Die Laufbahnplanungsdatei als GZIP-komprimiertes JSON
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
     */
    @POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/schueler/{id : \\d+}/laufbahnplanung/import")
    @Operation(summary = "Importiert die Laufbahndaten aus der übergebenen Laufbahnplanungsdatei.",
               description = "Importiert die Laufbahndaten aus der übergebenen Laufbahnplanungsdatei")
    @ApiResponse(responseCode = "200", description = "Der Log vom Import der Laufbahndaten",
    			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "409", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
    			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Der Benutzer hat keine Berechtigung, um die Laufbahndaten zu importieren.")
    public Response importGostSchuelerLaufbahnplanung(@PathParam("schema") final String schema,
    		@PathParam("id") final long id,
    		@RequestBody(description = "Die Laufbahnplanungs-Datei", required = true, content =
			@Content(mediaType = MediaType.MULTIPART_FORM_DATA)) @MultipartForm final SimpleBinaryMultipartBody multipart,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN)) {
	    	return (new DataGostSchuelerLaufbahnplanung(conn)).importGZip(id, multipart.data);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Laufbahplanungs-Daten der gymnasialen Oberstufe
     * in Bezug auf den angegebenen Schüler.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id        die ID des Schülers
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die Laufbahnplanungs-Daten
     */
    @GET
    @Path("/schueler/{id : \\d+}/laufbahnplanung/daten")
    @Operation(summary = "Liefert die Laufbahnplanungsdaten der gymnasialen Oberstufe für den angegebenen Schüler.",
    	description = "Liest die Laufbahnplanungsdaten der gymnasialen Oberstufe für den angegebenen Schüler aus der Datenbank "
    			+ "und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die "
    			+ "notwendige Berechtigung zum Auslesen der Daten besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Laufbahndaten der gymnasialen Obertufe",
			content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = GostLaufbahnplanungDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Laufbahndaten auszulesen.")
    @ApiResponse(responseCode = "404", description = "Es wurden nicht alle benötigten Daten für das Erstellen der Laufbahn-Daten gefunden.")
    public Response exportGostSchuelerLaufbahnplanungsdaten(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN)) {
	    	return (new DataGostSchuelerLaufbahnplanung(conn)).exportJSON(id);
    	}
    }

    /**
     * Die OpenAPI-Methode für den Import von Laufbahnplanungsdaten eines Schülers der gymnasialen Oberstufe
     * mit der angegebenen ID.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id            die ID des Schülers
     * @param daten         die Laufbahnplanungsdaten
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
     */
    @POST
    @Path("/schueler/{id : \\d+}/laufbahnplanung/daten")
    @Operation(summary = "Importiert die Laufbahndaten aus den übergebenen Laufbahnplanungsdaten.",
               description = "Importiert die Laufbahndaten aus den übergebenen Laufbahnplanungsdaten")
    @ApiResponse(responseCode = "200", description = "Der Log vom Import der Laufbahndaten",
    			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "409", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
    			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Der Benutzer hat keine Berechtigung, um die Laufbahndaten zu importieren.")
    public Response importGostSchuelerLaufbahnplanungsdaten(@PathParam("schema") final String schema,
    		@PathParam("id") final long id,
            @RequestBody(description = "Die Laufbahnplanungsdaten", required = false, content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = GostLaufbahnplanungDaten.class))) final GostLaufbahnplanungDaten daten,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN)) {
	    	return (new DataGostSchuelerLaufbahnplanung(conn)).importJSON(id, daten);
    	}
    }

}
