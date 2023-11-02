package de.svws_nrw.api.server;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnis;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostBelegpruefungsErgebnisse;
import de.svws_nrw.core.data.gost.GostBeratungslehrer;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostJahrgang;
import de.svws_nrw.core.data.gost.GostJahrgangFachkombination;
import de.svws_nrw.core.data.gost.GostJahrgangFachwahlen;
import de.svws_nrw.core.data.gost.GostJahrgangFachwahlenHalbjahr;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungDaten;
import de.svws_nrw.core.data.gost.GostLeistungen;
import de.svws_nrw.core.data.gost.GostSchuelerFachwahl;
import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.SimpleBinaryMultipartBody;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.data.gost.DBUtilsGostAbitur;
import de.svws_nrw.data.gost.DataGostAbiturjahrgangFachwahlen;
import de.svws_nrw.data.gost.DataGostBeratungslehrer;
import de.svws_nrw.data.gost.DataGostFaecher;
import de.svws_nrw.data.gost.DataGostJahrgangFachkombinationen;
import de.svws_nrw.data.gost.DataGostJahrgangLaufbahnplanung;
import de.svws_nrw.data.gost.DataGostJahrgangSchuelerliste;
import de.svws_nrw.data.gost.DataGostJahrgangsdaten;
import de.svws_nrw.data.gost.DataGostJahrgangsliste;
import de.svws_nrw.data.gost.DataGostSchuelerLaufbahnplanung;
import de.svws_nrw.data.gost.DataGostSchuelerLaufbahnplanungBeratungsdaten;
import de.svws_nrw.data.schule.SchulUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.pdf.gost.PDFGostErgebnisseLaufbahnpruefung;
import de.svws_nrw.module.pdf.gost.PDFGostWahlbogen;
import de.svws_nrw.module.pdf.gost.laufbahnplanung.PDFDateiGostLaufbahnplanungSchuelerWahlbogen;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
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
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import java.io.InputStream;
import java.util.List;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die SVWS-Datenbank in Bezug auf die gymnasiale Oberstufe.
 * Ein Zugriff erfolgt über den Pfad https://{hostname}/db/{schema}/gost/.
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
	 * @param schema   das Schema aus dem die Leistungsdaten des Schülers kommen sollen
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
    		return (new DataGostJahrgangsliste(conn)).getAll();
    	}
	}


    /**
     * Die OpenAPI-Methode für das Erstellen eines neuen Abiturjahrgangs der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN)) {
    		return (new DataGostJahrgangsliste(conn)).create(jahrgangID);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Daten eines Jahrgangs der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
    		return (new DataGostJahrgangsdaten(conn)).get(abiturjahr);
    	}
    }



    /**
     * Die OpenAPI-Methode für das Anpassen der Daten eines Jahrgangs der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.ABITUR_AENDERN_ALLGEMEIN,
    			BenutzerKompetenz.ABITUR_AENDERN_FUNKTIONSBEZOGEN)) {
    		return (new DataGostJahrgangsdaten(conn)).patch(abiturjahr, is);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Hinzufügen eines Beratungslehrers zu einem Abiturjahrgang.
     *
     * @param schema       das Datenbankschema
     * @param abiturjahr   das Jahr, in welchem der Jahrgang Abitur machen wird
     * @param idLehrer     die ID des Lehrers, der hinzugefügt werden soll
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit dem Beratungslehrer
     */
    @POST
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}/beratungslehrer/add")
    @Operation(summary = "Fügt einen Lehrer als Beratungslehrer zu einem Abiturjahrgang der Gymnasialen Oberstufe hinzu.",
               description = "Fügt einen Lehrer als Beratungslehrer zu einem Abiturjahrgang der Gymnasialen Oberstufe hinzu."
    		               + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Hinzufügen eines Beratungslehrers hat.")
    @ApiResponse(responseCode = "200", description = "Der hinzugefügte Beratungslehrer",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostBeratungslehrer.class)))
    @ApiResponse(responseCode = "400", description = "Der Lehrer ist bereits als Beratungslehrer eingetragen.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Beratungslehrer hinzuzufügen.")
    @ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder der Lehrer ist nicht vorhanden")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addGostAbiturjahrgangBeratungslehrer(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
            @RequestBody(description = "Die ID des Lehrers", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Long.class))) final long idLehrer,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataGostBeratungslehrer(conn, abiturjahr).add(idLehrer),
        		request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN);
    }


    /**
     * Die OpenAPI-Methode für das Erntfernen eines Beratungslehrers aus einem Abiturjahrgang.
     *
     * @param schema       das Datenbankschema
     * @param abiturjahr   das Jahr, in welchem der Jahrgang Abitur machen wird
     * @param idLehrer     die ID des Lehrers, der entfernt werden soll
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit dem entfernten Beratungslehrer
     */
    @POST
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}/beratungslehrer/remove")
    @Operation(summary = "Entfernt einen Lehrer als Beratungslehrer aus einem Abiturjahrgang der Gymnasialen Oberstufe.",
               description = "Entfernt einen Lehrer als Beratungslehrer aus einem Abiturjahrgang der Gymnasialen Oberstufe."
    		               + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen eines Beratungslehrers hat.")
    @ApiResponse(responseCode = "200", description = "Der entfernte Beratungslehrer",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostBeratungslehrer.class)))
    @ApiResponse(responseCode = "400", description = "Der Lehrer ist nicht als Beratungslehrer eingetragen.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Beratungslehrer zu entfernen.")
    @ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder der Lehrer ist nicht vorhanden")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response removeGostAbiturjahrgangBeratungslehrer(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
            @RequestBody(description = "Die ID des Lehrers", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Long.class))) final long idLehrer,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataGostBeratungslehrer(conn, abiturjahr).remove(idLehrer),
        		request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN);
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
               description = "Erstellt eine Liste aller Schüler, welche zu dem angegebenen Abitur-Jahrgang gehören (ohne Informationen zu Kursen). "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Schülerdaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Schüler-Listen-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Schüler gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden")
    public Response getGostAbiturjahrgangSchueler(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
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
                           + "welche für den angegebenen Abitur-Jahrgang festgelegt wurden.. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Fächerdaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Fächer-Listen-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostFach.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Fächerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Fächer-Einträge gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden")
    public Response getGostAbiturjahrgangFaecher(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
    		return (new DataGostFaecher(conn, abiturjahr)).getList();
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Daten eines Faches in Bezug auf die gymnasiale Oberstufe
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
    		return (new DataGostFaecher(conn, abiturjahr)).get(id);
    	}
    }



    /**
     * Die OpenAPI-Methode für das Anpassen der Daten eines Faches in Bezug auf den
     * Abiturjahrgang der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN)) {
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
               description = "Erstellt eine Liste aller in der Datenbank für den angegebenen Abitur-Jahrgang vorhanden "
               		       + "Fachwahlen der gymnasialen Oberstufe. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Fächerdaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Fachwahlstatistik",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostStatistikFachwahl.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Fachwahlen anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Fachwahlen gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden")
    public Response getGostAbiturjahrgangFachwahlstatistik(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
    		if (abiturjahr < 0)
    			return OperationError.NOT_FOUND.getResponse("Fachwahlen sind für den Vorlagen-Abiturjahrgang nicht verfügbar.");
    		return (new DataGostAbiturjahrgangFachwahlen(conn, abiturjahr)).getList();
    	}
    }



    /**
     * Die OpenAPI-Methode für die Abfrage aller Fachwahlen für den angegebenen Abitur-Jahrgang der
     * gymnasialen Oberstufe im angegebenen Schema.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abiturjahr    der Abitur-Jahrgang
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Fachwahlen der gymnasialen Oberstufe
     */
    @GET
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}/fachwahlen")
    @Operation(summary = "Gibt eine Übersicht von allen Fachwahlen des Abitur-Jahrganges der gymnasialen Oberstufe zurück.",
               description = "Erstellt eine Liste aller in der Datenbank für den angegebenen Abitur-Jahrgang vorhanden "
                           + "Fachwahlen der gymnasialen Oberstufe. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Fächerdaten "
                           + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste der Fachwahlen",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostJahrgangFachwahlen.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Fachwahlen anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Fachwahlen gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden")
    public Response getGostAbiturjahrgangFachwahlen(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
            @Context final HttpServletRequest request) {
        try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
        		BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
        		BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
        		BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
        		BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
        		BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
        		BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
    		if (abiturjahr < 0)
    			return OperationError.NOT_FOUND.getResponse("Fachwahlen sind für den Vorlagen-Abiturjahrgang nicht verfügbar.");
            return (new DataGostAbiturjahrgangFachwahlen(conn, abiturjahr)).getSchuelerFachwahlenResponse();
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
               description = "Erstellt eine Liste aller in der Datenbank für den angegebenen Abitur-Jahrgang vorhanden "
                           + "Fachwahlen der gymnasialen Oberstufe. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Fächerdaten "
                           + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste der Fachwahlen",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostJahrgangFachwahlenHalbjahr.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Fachwahlen anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Fachwahlen gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden")
    public Response getGostAbiturjahrgangHalbjahrFachwahlen(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
            @PathParam("halbjahr") final int halbjahr, @Context final HttpServletRequest request) {
        try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
        		BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
        		BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
        		BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
        		BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
        		BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
        		BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
    		if (abiturjahr < 0)
    			return OperationError.NOT_FOUND.getResponse("Fachwahlen sind für den Vorlagen-Abiturjahrgang nicht verfügbar.");
            return (new DataGostAbiturjahrgangFachwahlen(conn, abiturjahr)).getSchuelerFachwahlenResponseHalbjahr(halbjahr);
        }
    }


    /**
     * Die OpenAPI-Methode für die Abfrage alle Laufbahnplanungsfehler/-rückmeldungen
     * der Gesamt-Belegprüfungen zu den Schüler-Laufbahnen eines Abitur-Jahrgangs der
     * gymnasialen Oberstufe im angegebenen Schema.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abiturjahr    der Abitur-Jahrgang
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Belegprüfungsergebnisse
     */
    @GET
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}/belegpruefung/gesamt")
    @Operation(summary = "Gibt die (Fehler-)Rückmeldungen der Gesamt-Belegprüfung zu den Schüler-Laufbahnen eines Abitur-Jahrganges der gymnasialen Oberstufe zurück.",
               description = "Gibt die (Fehler-)Rückmeldungen der Gesamt-Belegprüfung zu den Schüler-Laufbahnen eines "
                           + "Abitur-Jahrganges der gymnasialen Oberstufe zurück."
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung dazu hat.")
    @ApiResponse(responseCode = "200", description = "Eine Liste mit den Schülern und den zugehörigen Belegprüfungsfehlern.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostBelegpruefungsErgebnisse.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Rückmeldungen abzufragen.")
    @ApiResponse(responseCode = "404", description = "Keine und unvollständige Daten für die Belegprüfung gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden")
    public Response getGostAbiturjahrgangBelegpruefungsergebnisseGesamt(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
    		@Context final HttpServletRequest request) {
        try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
        		BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
        		BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
        		BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
        		BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
    		if (abiturjahr < 0)
    			return OperationError.NOT_FOUND.getResponse("Eine Belegprüfung ist für den Vorlagen-Abiturjahrgang nicht möglich.");
            return (new DataGostSchuelerLaufbahnplanung(conn)).pruefeBelegungAbitujahrgang(abiturjahr, GostBelegpruefungsArt.GESAMT);
        }
    }


    /**
     * Die OpenAPI-Methode für die Abfrage alle Laufbahnplanungsfehler/-rückmeldungen
     * der EF1-Belegprüfungen zu den Schüler-Laufbahnen eines Abitur-Jahrgangs der
     * gymnasialen Oberstufe im angegebenen Schema.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abiturjahr    der Abitur-Jahrgang
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Belegprüfungsergebnisse
     */
    @GET
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}/belegpruefung/EF1")
    @Operation(summary = "Gibt die (Fehler-)Rückmeldungen der EF1-Belegprüfung zu den Schüler-Laufbahnen eines Abitur-Jahrganges der gymnasialen Oberstufe zurück.",
               description = "Gibt die (Fehler-)Rückmeldungen der EF1-Belegprüfung zu den Schüler-Laufbahnen eines "
                           + "Abitur-Jahrganges der gymnasialen Oberstufe zurück."
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung dazu hat.")
    @ApiResponse(responseCode = "200", description = "Eine Liste mit den Schülern und den zugehörigen Belegprüfungsfehlern.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostBelegpruefungsErgebnisse.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Rückmeldungen abzufragen.")
    @ApiResponse(responseCode = "404", description = "Keine und unvollständige Daten für die Belegprüfung gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden")
    public Response getGostAbiturjahrgangBelegpruefungsergebnisseEF1(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
    		@Context final HttpServletRequest request) {
        try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
        		BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
        		BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
        		BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
        		BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
    		if (abiturjahr < 0)
    			return OperationError.NOT_FOUND.getResponse("Eine Belegprüfung ist für den Vorlagen-Abiturjahrgang nicht möglich.");
            return (new DataGostSchuelerLaufbahnplanung(conn)).pruefeBelegungAbitujahrgang(abiturjahr, GostBelegpruefungsArt.EF1);
        }
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der PDF-Wahlbögen für die gymnasiale Oberstufe eines Abiturjahrgangs.
     *
     * @param schema       das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param abiturjahr   das Abiturjahr, zu welchem die Wahlbögen erstellt werden sollen
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return das PDF mit den Wahlbögen des Abiturjahrgangs
     */
    @GET
    @Produces("application/pdf")
    @Path("/abiturjahrgang/pdf/wahlboegen/{abiturjahr : -?\\d+}")
    @Operation(summary = "Erstellt die PDF-Wahlbögen für die gymnasiale Oberstufe zu dem angegebenen Abiturjahrgang.",
               description = "Erstellt die PDF-Wahlbögen für die gymnasiale Oberstufe zu dem angegebenen Abiturjahrgang. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen des PDFs besitzt.")
    @ApiResponse(responseCode = "200", description = "Die PDF-Wahlbögen für die gymnasialen Oberstufe des angegebenen Abiturjahrgangs",
                 content = @Content(mediaType = "application/pdf",
                 schema = @Schema(type = "string", format = "binary", description = "PDF-Wahlbögen")))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Wahlbögen für die Gymnasialen Oberstufe des Abiturjahrgangs zu erstellen.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag für Laufbahnplanungsdaten des Abiturjahrgangs der gymnasialen Oberstufe gefunden")
    public Response getGostAbiturjahrgangPDFWahlboegen(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
    		return PDFGostWahlbogen.queryJahrgang(conn, abiturjahr);
    	}
    }



	/**
	 * Die OpenAPI-Methode für die Abfrage einer PDF-Liste mit den Kurs- und Wochenstundensummen sowie den Laufbahnfehlern der Schüler eines Abiturjahrgangs.
	 *
	 * @param schema       	das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param abiturjahr   	das Abiturjahr, zu welchem die Liste erstellt werden sollen
	 * @param detaillevel	gibt an, welche Detailinformationen die Liste enthalten soll: 0 = Summen, 1 = Summen und Fehler, 2 = Summen, Fehler und Hinweise
	 * @param request      	die Informationen zur HTTP-Anfrage
	 *
	 * @return das PDF mit der Liste des Abiturjahrgangs
	 */
	@GET
	@Produces("application/pdf")
	@Path("/abiturjahrgang/pdf/ergebnisse_laufbahnpruefung/{abiturjahr : -?\\d+}/{detaillevel : \\d+}")
	@Operation(summary = "Erstellt eine PDF-Liste mit den Ergebnissen der Laufbahnprüfung in der gymnasialen Oberstufe zu dem angegebenen Abiturjahrgang.",
			   description = "Erstellt eine PDF-Liste mit den Schülern und ihren Kurs- und Wochenstunden sowie ihren Laufbahnfehlern in der gymnasialen Oberstufe zu dem angegebenen Abiturjahrgang. "
				   		     + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen des PDFs besitzt.")
	@ApiResponse(responseCode = "200", description = "Die PDF-Liste mit Summen und Fehlern des angegebenen Abiturjahrgangs",
				 content = @Content(mediaType = "application/pdf",
				 schema = @Schema(type = "string", format = "binary", description = "PDF-Ergebnisse-Laufbahnprüfung")))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Wahlbögen für die Gymnasialen Oberstufe des Abiturjahrgangs zu erstellen.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag für Laufbahnplanungsdaten des Abiturjahrgangs der gymnasialen Oberstufe gefunden")
	public Response getGostAbiturjahrgangPDFErgebnisseLaufbahnpruefung(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("detaillevel") final int detaillevel, @Context final HttpServletRequest request) {
		try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
			return PDFGostErgebnisseLaufbahnpruefung.queryJahrgang(conn, abiturjahr, detaillevel);
		}
	}


	/**
     * Die OpenAPI-Methode für die Abfrage der Default-Laufbahnplanungsdaten der gymnasialen Oberstufe für einen
     * Abiturjahrgang.
     *
     * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param abiturjahr  das Abiturjahr
     * @param request     die Informationen zur HTTP-Anfrage
     *
     * @return die Laufbahnplanungsdaten des Abiturjahrgangs
     */
    @GET
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}/laufbahnplanung")
    @Operation(summary = "Liest die Laufbahnplanungsdaten für die gymnasiale Oberstufe zu dem Abiturjahrgang aus.",
               description = "Liest die Laufbahnplanungsdaten für die gymnasiale Oberstufe zu dem Abiturjahrgang "
    		    + "aus der Datenbank aus und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Laufbahnplanungsdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Laufbahnplanungsdaten der gymnasialen Oberstufe des angegebenen Abiturjahrgangs",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = Abiturdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Laufbahnplanungsdaten der Gymnasialen Oberstufe eines Abiturjahrgangs auszulesen.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag für den angegebenen Abiturjahrgangs mit Laufbahnplanungsdaten der gymnasialen Oberstufe gefunden")
    public Response getGostAbiturjahrgangLaufbahnplanung(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostJahrgangLaufbahnplanung(conn)).get(abiturjahr);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage einer Fachwahl der gymnasialen Oberstufe in der Vorlage
     * eines Abiturjahrgangs.
     *
     * @param schema       das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param abiturjahr   das Abiturjahr
     * @param fach_id      die ID des Faches
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die Fachwahlen des Abiturjahrgangs zu dem Fach
     */
    @GET
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}/fachwahl/{fachid : \\d+}")
    @Operation(summary = "Liest für die gymnasiale Oberstufe die Fachwahlen zu einem Fach von dem angegebenen Abiturjahrgang aus.",
               description = "Liest für die gymnasiale Oberstufe die Fachwahlen zu einem Fach von dem angegebenen Abiturjahrgang aus. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Fachwahlen "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Fachwahlen der gymnasialen Oberstufe für das angegebene Fach und den angegebenen Abiturjahrgang",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = GostSchuelerFachwahl.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Fachwahlen der Gymnasialen Oberstufe eines Abiturjahrgang auszulesen.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag für einen Abiturjahrgang mit Laufbahnplanungsdaten der gymnasialen Oberstufe für die angegebene ID gefunden")
    public Response getGostAbiturjahrgangFachwahl(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("fachid") final long fach_id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostJahrgangLaufbahnplanung(conn)).getFachwahl(abiturjahr, fach_id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Anpassen der Fachwahlen zu einem Fach der gymnasialen Oberstufe
     * für die Vorlage des angegebenen Abiturjahrgangs.
     *
     * @param schema       das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param abiturjahr   das Abiturjahr
     * @param fach_id      die ID des Faches
     * @param is           der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @PATCH
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}/fachwahl/{fachid : \\d+}")
    @Operation(summary = "Passt die Fachwahl der Vorlage des angegebenen Abiturjahrgangs in Bezug auf ein Fach der Gymnasiale Oberstufe an.",
    description = "Passt die Fachwahl der Vorlage des angegebenen Abiturjahrgangs in Bezug auf ein Fach der Gymnasiale Oberstufe an. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen der Fachwahlen "
    		    + "besitzt.")
    @ApiResponse(responseCode = "203", description = "Der Patch wurde erfolgreich in die Fachwahlen integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Fachwahlen zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag für einen Abiturjahrgang mit Laufbahnplanungsdaten der gymnasialen Oberstufe oder für das Fach gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchGostAbiturjahrgangFachwahl(
    		@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("fachid") final long fach_id,
    		@RequestBody(description = "Der Patch für die Fachwahl", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostSchuelerFachwahl.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostJahrgangLaufbahnplanung(conn)).patchFachwahl(abiturjahr, fach_id, is);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Zurücksetzen der Fachwahlen in der Vorlage
     * für die Laufbahnplanung in dem angegebenen Abiturjahrgang.
     *
     * @param schema       das Datenbankschema
     * @param abiturjahr   der Abitur-Jahrgang dessen Fachwahlen zurückgesetzt werden
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}/fachwahl/reset")
    @Operation(summary = "Setzt die Fachwahlen in der Vorlage für eine Laufbahnplanung in dem angegebenen Abiturjahrgang zurück.",
    description = "Setzt die Fachwahlen in der Vorlage für eine Laufbahnplanung in dem angegebenen Abiturjahrgang zurück."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Zurücksetzen der Fachwahlen besitzt.")
    @ApiResponse(responseCode = "203", description = "Die Fachwahlen wurden erfolgreich zurückgesetzt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Fachwahlen zurückzusetzen.")
    @ApiResponse(responseCode = "404", description = "Der Abiturjahrgang wurde nicht gefunden.")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response resetGostAbiturjahrgangFachwahlen(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostJahrgangLaufbahnplanung(conn)).reset(abiturjahr);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Zurücksetzen aller Fachwahlen von allen Schülern
     * des Abiturjahrgangs. Hierbei bleiben die Fachwahlen für Halbjahre erhalten,
     * für die bereits Leistungsdaten vorhanden sind. Alle anderen Halbjahre werden
     * geleert.
     * Für den Fall, dass noch keine Leistungsdaten vorhanden sind, so wird die
     * Laufbahnplanung mit den Fachwahlen aus der Vorlage initialisiert.
     *
     * @param schema       das Datenbankschema
     * @param abiturjahr   der Abitur-Jahrgang, bei dessen Schüler die Fachwahlen zurückgesetzt werden
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/abiturjahrgang/{abiturjahr : -?\\d+}/fachwahl/schueler/reset")
    @Operation(summary = "Setzt die Fachwahlen aller Schüler des angegebenen Abiturjahrgang zurück.",
    description = "Setzt die Fachwahlen aller Schüler des angegebenen Abiturjahrgang zurück. "
    			+ "Die Fachwahlen werden von allen Halbjahren ohn Leistungsdaten entfernt. Sollten danach "
    			+ "keine Fachwahlen bei einem Schüler vorhanden sein, so wird die Laufbahnplanung mit der Fachwahlen-Vorlage des "
    			+ "Abiturjahrgangs initialisiert."
    		    + "Außerdem wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Zurücksetzen der Fachwahlen besitzt.")
    @ApiResponse(responseCode = "203", description = "Die Fachwahlen wurden erfolgreich zurückgesetzt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Fachwahlen zurückzusetzen.")
    @ApiResponse(responseCode = "404", description = "Der Abiturjahrgang wurde nicht gefunden.")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response resetGostAbiturjahrgangSchuelerFachwahlen(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostJahrgangLaufbahnplanung(conn)).resetSchuelerAlle(abiturjahr);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Laufbahnplanungsdaten der gymnasialen Oberstufe eines Schülers.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostSchuelerLaufbahnplanung(conn)).get(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Beratungsdaten für die Laufbahnplanung der gymnasialen Oberstufe eines Schülers.
     *
     * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id          die ID des Schülers zu dem die Beratungsdaten ausgelesen werden
     * @param request     die Informationen zur HTTP-Anfrage
     *
     * @return die Beratungsdaten zu dem Schüler
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostSchuelerLaufbahnplanungBeratungsdaten(conn)).get(id);
    	}
    }

    /**
     * Die OpenAPI-Methode für das Anpassen der Beratungsdaten für die Laufbahnplanung der
     * gymnasialen Oberstufe eines Schülers.
     *
     * @param schema        das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param schueler_id   die ID des Schülers
     * @param is            der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @PATCH
    @Path("/schueler/{schuelerid : \\d+}/laufbahnplanung/beratungsdaten")
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostSchuelerLaufbahnplanungBeratungsdaten(conn)).patch(schueler_id, is);
    	}
    }

    /**
     * Die OpenAPI-Methode für die Abfrage des PDF-Wahlbogens für die gymnasiale Oberstufe eines Schülers.
     *
     * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param schuelerid  die ID des Schülers zu dem der Wahlbogen erstellt werden soll
     * @param request     die Informationen zur HTTP-Anfrage
     *
     * @return der PDF-Wahlbogen des Schülers
     */
    @GET
    @Produces("application/pdf")
    @Path("/schueler/pdf/wahlbogen/{schuelerid : \\d+}")
    @Operation(summary = "Erstellt den PDF-Wahlbogen für die gymnasiale Oberstufe zu dem Schüler mit der angegebenen ID.",
               description = "Erstellt den PDF-Wahlbogen für die gymnasiale Oberstufe zu dem Schüler mit der angegebenen ID. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen des Wahlbogens "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der PDF-Wahlbogen für die gymnasialen Oberstufe des angegebenen Schülers",
                 content = @Content(mediaType = "application/pdf",
                 schema = @Schema(type = "string", format = "binary", description = "PDF-Wahlbogen")))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Wahlbogen für die Gymnasialen Oberstufe eines Schülers zu erstellen.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag für einen Schüler mit Laufbahnplanungsdaten der gymnasialen Oberstufe für die angegebene ID gefunden")
    public Response getGostSchuelerPDFWahlbogen(@PathParam("schema") final String schema, @PathParam("schuelerid") final long schuelerid, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
    		return PDFGostWahlbogen.query(conn, schuelerid);
    	}
    }


	/**
	 * Die OpenAPI-Methode für die Abfrage von Wahlbögen zur Laufbahnplanung der gymnasialen Oberstufe als PDF-Datei.
	 *
	 * @param schema      			das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param schuelerids           Liste der IDs der SuS, deren Kurse-Schienen-Zuordnung erstellt werden soll. Werden keine IDs übergeben, so wird die Matrix allgemein für das Blockungsergebnis erstellt.
	 * @param request     			die Informationen zur HTTP-Anfrage
	 *
	 * @return Die zu den übergebenen IDs zugehörige Kurse-Schienen-Zuordnung
	 */
	@POST
	@Produces("application/pdf")
	@Path("/schueler/pdf/laufbahnplanungwahlbogen}")
	@Operation(summary = "Erstellt die Wahlbögen für die gymnasiale Oberstufe zu den Schülern mit den angegebenen IDs.",
		       description = "Erstellt die Wahlbogen für die Laufbahnplanung der gymnasialen Oberstufe zu den Schülern mit der angegebenen IDs als PDF-Datei. "
							+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen des Wahlbogens besitzt.")
	@ApiResponse(responseCode = "200", 	description = "Die PDF-Datei mit den Wahlbögen zur Laufbahnplanung der gymnasialen Oberstufe.",
				content = @Content(mediaType = "application/pdf",
				schema = @Schema(type = "string", format = "binary", description = "Wahlbogen Laufbahnplanung")))
	@ApiResponse(responseCode = "403", 	description = "Der SVWS-Benutzer hat keine Rechte, um die Kurse-Schienen-Zuordnung für die gymnasialen Oberstufe zu erstellen.")
	@ApiResponse(responseCode = "404", 	description = "Kein Eintrag zu den angegebenen IDs gefunden.")
	public Response pdfGostLaufbahnplanungSchuelerWahlbogen(@PathParam("schema") final String schema,
															 @RequestBody(description = "Schüler-IDs, für die die Wahlbögen erstellt werden soll.", required = true, content =
															 @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> schuelerids,
															 @Context final HttpServletRequest request) {
		try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
			return PDFDateiGostLaufbahnplanungSchuelerWahlbogen.query(conn, schuelerids, false);
		}
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage von Wahlbögen zur Laufbahnplanung der gymnasialen Oberstufe als PDF-Datei.
	 *
	 * @param schema      			das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param schuelerids           Liste der IDs der SuS, deren Kurse-Schienen-Zuordnung erstellt werden soll. Werden keine IDs übergeben, so wird die Matrix allgemein für das Blockungsergebnis erstellt.
	 * @param request     			die Informationen zur HTTP-Anfrage
	 *
	 * @return Die zu den übergebenen IDs zugehörige Kurse-Schienen-Zuordnung
	 */
	@POST
	@Produces("application/pdf")
	@Path("/schueler/pdf/laufbahnplanungwahlbogennurbelegung}")
	@Operation(summary = "Erstellt die Wahlbögen, reduziert auf die Belegung des Schülers, für die gymnasiale Oberstufe zu den Schülern mit den angegebenen IDs.",
		description = "Erstellt die Wahlbogen, reduziert auf die Belegung des Schülers, für die Laufbahnplanung der gymnasialen Oberstufe zu den Schülern mit der angegebenen IDs als PDF-Datei. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen des Wahlbogens besitzt.")
	@ApiResponse(responseCode = "200", 	description = "Die PDF-Datei mit den Wahlbögen, reduziert auf die Belegung des Schülers, zur Laufbahnplanung der gymnasialen Oberstufe.",
		content = @Content(mediaType = "application/pdf",
			schema = @Schema(type = "string", format = "binary", description = "Reduzierter Wahlbogen Laufbahnplanung")))
	@ApiResponse(responseCode = "403", 	description = "Der SVWS-Benutzer hat keine Rechte, um die Kurse-Schienen-Zuordnung für die gymnasialen Oberstufe zu erstellen.")
	@ApiResponse(responseCode = "404", 	description = "Kein Eintrag zu den angegebenen IDs gefunden.")
	public Response pdfGostLaufbahnplanungSchuelerWahlbogenNurBelegung(@PathParam("schema") final String schema,
																		@RequestBody(description = "Schüler-IDs, für die die reduzierten Wahlbögen erstellt werden soll.", required = true, content =
																		@Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> schuelerids,
																		@Context final HttpServletRequest request) {
		try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
			return PDFDateiGostLaufbahnplanungSchuelerWahlbogen.query(conn, schuelerids, true);
		}
	}


    /**
     * Die OpenAPI-Methode für die Abfrage einer Fachwahl der gymnasialen Oberstufe eines Schülers.
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
    @ApiResponse(responseCode = "200", description = "Die Fachwahlen der gymnasialen Oberstufe für das angegebene Fach und den angegebenen Schüler",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = Abiturdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Fachwahlen der Gymnasialen Oberstufe eines Schülers auszulesen.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag für einen Schüler mit Laufbahnplanungsdaten der gymnasialen Oberstufe für die angegebene ID gefunden")
    public Response getGostSchuelerFachwahl(@PathParam("schema") final String schema, @PathParam("schuelerid") final long schueler_id, @PathParam("fachid") final long fach_id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostSchuelerLaufbahnplanung(conn)).getFachwahl(schueler_id, fach_id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Anpassen der Fachwahlen eines Schülers
     * zu einem Fach der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostSchuelerLaufbahnplanung(conn)).patchFachwahl(schueler_id, fach_id, is);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Zurücksetzen der Fachwahlen eines Schülers.
     *
     * @param schema       das Datenbankschema
     * @param schuelerid   die ID des Schülers, dessen Fachwahlen zurückgesetzt werden
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/schueler/{schuelerid : \\d+}/fachwahl/reset")
    @Operation(summary = "Setzt die Fachwahlen des Schülers mit der angegebenen ID zurück.",
    description = "Setzt die Fachwahlen des Schülers mit der angegebenen ID zurück."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Zurücksetzen der Fachwahlen besitzt.")
    @ApiResponse(responseCode = "203", description = "Die Fachwahlen wurden erfolgreich zurückgesetzt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Fachwahlen zurückzusetzen.")
    @ApiResponse(responseCode = "404", description = "Der Schüler bzw. der zugehörige Abiturjahrgang wurde nicht gefunden.")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response resetGostSchuelerFachwahlen(@PathParam("schema") final String schema, @PathParam("schuelerid") final long schuelerid,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostSchuelerLaufbahnplanung(conn)).reset(schuelerid);
    	}
    }


	/**
	 * Liest die Leistungsdaten in Bezug auf die gymnasiale Oberstufe des Schülers mit der angegebene ID aus der Datenbank und liefert diese zurück.
	 * Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Leistungsdaten besitzt.
	 *
	 * @param schema   das Schema aus dem die Leistungsdaten des Schülers kommen sollen
	 * @param id       die ID des Schülers zu dem die Leistungsdaten geliefert werden sollen
	 *
	 * @param request  die Informationen zur HTTP-Anfrage
	 *
	 * @return die Leistungsdaten in der gymnasialen Oberstufe für den Schüler mit der
	 *         angegebenen ID und die Berechtigung des Datenbank-Users
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
	    	return DBUtilsGost.getLeistungsdaten(conn, id);
    	}
    }



	/**
	 * Liest die Abiturdaten aus den Leistungsdaten der gymnasialen Oberstufe des Schülers mit der angegebene ID aus der Datenbank und liefert diese zurück.
	 * Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Leistungsdaten besitzt.
	 *
	 * @param schema   das Schema aus dem die Abiturdaten des Schülers kommen sollen
	 * @param id       die ID des Schülers zu dem die Abiturdaten geliefert werden sollen
	 *
	 * @param request  die Informationen zur HTTP-Anfrage
	 *
	 * @return die Abiturdaten in der gymnasialen Oberstufe für den Schüler mit der
	 *         angegebenen ID und die Berechtigung des Datenbank-Users
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
    			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN)) {
    		return DBUtilsGostAbitur.getAbiturdatenAusLeistungsdaten(conn, id);
    	}
    }



	/**
	 * Liest die Abiturdaten aus den Abiturtabellen des Schülers mit der angegebene ID und liefert diese zurück.
	 * Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Leistungsdaten besitzt.
	 *
	 * @param schema   das Schema aus dem die Abiturdaten des Schülers kommen sollen
	 * @param id       die ID des Schülers zu dem die Abiturdaten geliefert werden sollen
	 *
	 * @param request  die Informationen zur HTTP-Anfrage
	 *
	 * @return die Abiturdaten in der gymnasialen Oberstufe für den Schüler mit der
	 *         angegebenen ID und die Berechtigung des Datenbank-Users
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
    			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN)) {
	    	return DBUtilsGostAbitur.getAbiturdaten(conn, id);
    	}
    }



    /**
     * Die OpenAPI-Methode für die Prüfung der Belegprüfung der Abiturdaten.
     *
     * @param schema        das Schema aus dem die Fächerdaten kommen sollen
     * @param abidaten      zu übergebende Fächerdaten für die Prüfung der Belegung im Abitur
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              das Ergebnis der Prüfung der Belegprüfung der Abiturdaten
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
    		final @NotNull DTOEigeneSchule schule = SchulUtils.getDTOSchule(conn);
	    	if (!schule.Schulform.daten.hatGymOb)
	    		throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
	    	final @NotNull GostJahrgangsdaten jahrgangsdaten = DataGostJahrgangsdaten.getJahrgangsdaten(conn, abidaten.abiturjahr);
	    	// Prüfe die Belegung der Kurse mithilfe des Abiturdaten-Managers und gib das Ergebnis der Belegprüfung zurück.
	    	GostFaecherManager faecherManager = DBUtilsFaecherGost.getFaecherListeGost(conn, abidaten.abiturjahr);
	    	if (faecherManager.isEmpty())
	    		faecherManager = DBUtilsFaecherGost.getFaecherListeGost(conn, null);
	    	faecherManager.addFachkombinationenAll(DataGostJahrgangFachkombinationen.getFachkombinationen(conn, abidaten.abiturjahr));
			final AbiturdatenManager manager = new AbiturdatenManager(abidaten, jahrgangsdaten, faecherManager, GostBelegpruefungsArt.GESAMT);
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
     * @return              das Ergebnis der Prüfung der Belegprüfung nur für die EF_1
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
    		final @NotNull DTOEigeneSchule schule = SchulUtils.getDTOSchule(conn);
	    	if (!schule.Schulform.daten.hatGymOb)
	    		throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
	    	final @NotNull GostJahrgangsdaten jahrgangsdaten = DataGostJahrgangsdaten.getJahrgangsdaten(conn, abidaten.abiturjahr);
	    	// Prüfe die Belegung der Kurse mithilfe des Abiturdaten-Managers und gib das Ergebnis der Belegprüfung zurück.
	    	GostFaecherManager faecherManager = DBUtilsFaecherGost.getFaecherListeGost(conn, abidaten.abiturjahr);
	    	if (faecherManager.isEmpty())
	    		faecherManager = DBUtilsFaecherGost.getFaecherListeGost(conn, null);
	    	faecherManager.addFachkombinationenAll(DataGostJahrgangFachkombinationen.getFachkombinationen(conn, abidaten.abiturjahr));
			final AbiturdatenManager manager = new AbiturdatenManager(abidaten, jahrgangsdaten, faecherManager, GostBelegpruefungsArt.EF1);
			return manager.getBelegpruefungErgebnis();
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostJahrgangFachkombinationen(conn, abiturjahr)).getList();
    	}
    }


    /**
     * Die OpenAPI-Methode für das Anpassen einer Fachkombination für Fächer der gymnasialen Oberstufe.
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
    @ApiResponse(responseCode = "404", description = "Keine Fachkombination mit der angegebenen ID gefunden oder es wurden kein gültiges Fach als ID übergeben.")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchGostFachkombination(
    		@PathParam("schema") final String schema, @PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für die Fachkombination", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostJahrgangFachkombination.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostJahrgangFachkombinationen(conn, -1 /*nicht relevant*/)).patch(id, is);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Löschen einer Regel zu einer Fächkombination
     * der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostJahrgangFachkombinationen(conn, -1 /* nicht relevant */)).delete(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Hinzufügen einer weiteren Fachkombination der gymnasialen Oberstufe mit dem angegebenen Typ
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostJahrgangFachkombinationen(conn, abiturjahr)).add(typ);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Laufbahnplanungsdaten der gymnasialen Oberstufe
     * in Bezug auf den angegebenen Schüler als GZIP-Json.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id        die ID des Schülers
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die Laufbahnplanungsdaten
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN)) {
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
    		@RequestBody(description = "Die Laufbahnplanungsdatei", required = true, content =
			@Content(mediaType = MediaType.MULTIPART_FORM_DATA)) @MultipartForm final SimpleBinaryMultipartBody multipart,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN)) {
	    	return (new DataGostSchuelerLaufbahnplanung(conn)).importGZip(id, multipart.data);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Laufbahnplanungsdaten der gymnasialen Oberstufe
     * in Bezug auf den angegebenen Schüler.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id        die ID des Schülers
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die Laufbahnplanungsdaten
     */
    @GET
    @Path("/schueler/{id : \\d+}/laufbahnplanung/daten")
    @Operation(summary = "Liefert die Laufbahnplanungsdaten der gymnasialen Oberstufe für den angegebenen Schüler.",
    	description = "Liest die Laufbahnplanungsdaten der gymnasialen Oberstufe für den angegebenen Schüler aus der Datenbank "
    			+ "und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die "
    			+ "notwendige Berechtigung zum Auslesen der Daten besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Laufbahndaten der gymnasialen Oberstufe",
			content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = GostLaufbahnplanungDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Laufbahndaten auszulesen.")
    @ApiResponse(responseCode = "404", description = "Es wurden nicht alle benötigten Daten für das Erstellen der Laufbahn-Daten gefunden.")
    public Response exportGostSchuelerLaufbahnplanungsdaten(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN)) {
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN)) {
	    	return (new DataGostSchuelerLaufbahnplanung(conn)).importJSON(id, daten);
    	}
    }

}
