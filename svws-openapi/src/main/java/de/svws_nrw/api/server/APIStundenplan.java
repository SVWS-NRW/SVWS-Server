package de.svws_nrw.api.server;

import java.io.InputStream;

import de.svws_nrw.api.OpenAPIApplication;
import de.svws_nrw.core.data.stundenplan.SchuelerStundenplan;
import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung;
import de.svws_nrw.core.data.stundenplan.StundenplanLehrer;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsicht;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenzeit;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterrichtsverteilung;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.stundenplan.DataSchuelerStundenplan;
import de.svws_nrw.data.stundenplan.DataStundenplan;
import de.svws_nrw.data.stundenplan.DataStundenplanKalenderwochenzuordnung;
import de.svws_nrw.data.stundenplan.DataStundenplanLehrer;
import de.svws_nrw.data.stundenplan.DataStundenplanListe;
import de.svws_nrw.data.stundenplan.DataStundenplanPausenaufsichten;
import de.svws_nrw.data.stundenplan.DataStundenplanPausenzeiten;
import de.svws_nrw.data.stundenplan.DataStundenplanRaeume;
import de.svws_nrw.data.stundenplan.DataStundenplanUnterricht;
import de.svws_nrw.data.stundenplan.DataStundenplanUnterrichtsverteilung;
import de.svws_nrw.data.stundenplan.DataStundenplanZeitraster;
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
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Stundenplandaten aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/stundenplan/...
 */
@Path("/db/{schema}/stundenplan")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIStundenplan {

    /**
     * Die OpenAPI-Methode für die Abfrage der Liste aller Stundenpläne.
     *
     * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request     die Informationen zur HTTP-Anfrage
     *
     * @return die Liste mit den Stundenplänen
     */
    @GET
    @Path("/liste")
    @Operation(summary = "Gibt eine sortierte Übersicht aller Stundenpläne zurück.",
               description = "Erstellt eine Liste der Stundenpläne. Die Stundenpläne sind anhand des Schuljahresabschnitt "
               		       + "und der Gültigkeit sortiert."
               		       + "Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplanlisten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste der Stundenpläne",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StundenplanListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Stundenplanlisten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Stundenpläne gefunden", content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    public Response getStundenplanliste(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
    		return (new DataStundenplanListe(conn).getList());
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Liste aller Stundenpläne eines Schuljahresabschnitts.
     *
     * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abschnitt   die ID des Schuljahresabschnitts
     * @param request     die Informationen zur HTTP-Anfrage
     *
     * @return die Liste mit den Stundenplänen
     */
    @GET
    @Path("/liste/{abschnitt : \\d+}")
    @Operation(summary = "Gibt eine sortierte Übersicht der Stundenpläne des angegebenen Schuljahresabschnitts zurück.",
               description = "Erstellt eine Liste der Stundenpläne des angegebenen Schuljahresabschnitts. Die Stundenpläne "
               		       + "sind anhand der Gültigkeit sortiert."
               		       + "Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplanlisten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste der Stundenpläne",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StundenplanListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Stundenplanlisten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Stundenpläne gefunden")
    public Response getStundenplanlisteFuerAbschnitt(@PathParam("schema") final String schema, @PathParam("abschnitt") final long abschnitt, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
    		return (new DataStundenplanListe(conn).get(abschnitt));
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage von grundlegenden Daten zu einem Stundenplan.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id            die ID des Stundenplans
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Daten für den angegebenen Stundenplan
     */
    @GET
    @Path("/{id : \\d+}")
    @Operation(summary = "Gibt die grundlegenden Daten des Stundeplans mit der angegebenen ID zurück.",
               description = "Gibt die grundlegenden Daten des Stundeplans mit der angegebenen ID zurück. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Daten des Stundenplans",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Stundenplan.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Stundenplandaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Stundenplandaten gefunden")
    public Response getStundenplan(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
    		return (new DataStundenplan(conn)).get(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Patchen der Grunddaten eines Stundenplans.
     *
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation des Stundenplans
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/{id : \\d+}")
    @Operation(summary = "Passt die Stundenplandaten des Stundenplans mit der angebenen ID an.",
    description = "Passt die Stundenplandaten des Stundenplans mit der angebenen ID an. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchStundenplan(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für die Stundenplandaten", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Stundenplan.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ERSTELLEN)) {
    		return (new DataStundenplan(conn).patch(id, is));
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Zeitrasters zu einem Stundenplan.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id            die ID des Stundenplans
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Liste der Zeitraster-Einträge für den angegebenen Stundenplan
     */
    @GET
    @Path("/{id : \\d+}/zeitraster")
    @Operation(summary = "Gibt die Einträge aus dem Zeitraster des angegebenen Stundeplans zurück.",
               description = "Erstellt eine Liste der Einträge aus dem Zeitraster des angegebenen Stundeplans. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Zeitraster-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StundenplanZeitraster.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um das Zeitraster anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Zeitraster-Einträge gefunden")
    public Response getStundenplanZeitraster(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
    		return (new DataStundenplanZeitraster(conn, id)).getList();
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Stundenplans eines Schülers.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id            die ID des Stundenplans
     * @param schuelerID    die ID des Schülers
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              der Stundenplan des Schülers
     */
    @GET
    @Path("/{id : \\d+}/schueler/{schueler_id : \\d+}")
    @Operation(summary = "Gibt den Stundeplan des Schülers zurück.",
               description = "Erstellt den angebebenen Stundeplan in Bezug auf den angegebenen Schüler. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Schüler-Stundenplan",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = SchuelerStundenplan.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keinen Stundenplan gefunden")
    public Response getSchuelerStundenplan(@PathParam("schema") final String schema, @PathParam("id") final long id, @PathParam("schueler_id") final long schuelerID, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)) {
    		return (new DataSchuelerStundenplan(conn, id)).get(schuelerID);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage eines Zeitraster-Eintrags.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id            die ID des Zeitraster-Eintrags
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              der Zeitraster-Eintrag
     */
    @GET
    @Path("/zeitraster/{id : \\d+}")
    @Operation(summary = "Gibt den Zeitraster-Eintrag eines Stundeplans zurück.",
               description = "Gibt den Zeitraster-Eintrag eines Stundeplans zurück. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Zeitraster-Eintrag",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = StundenplanZeitraster.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Zeitraster-Eintrag eines Stundenplans gefunden")
    public Response getStundenplanZeitrasterEintrag(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
    		return (new DataStundenplanZeitraster(conn, null)).get(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Patchen eines Zeitraster-Eintrags eines Stundenplans.
     *
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation des Zeitraster-Eintrags
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/zeitraster/{id : \\d+}")
    @Operation(summary = "Passt den Zeitrastereintrag mit der angebenen ID an.",
    description = "Passt den Zeitrastereintrag mit der angebenen ID an. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchStundenplanZeitrasterEintrag(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für den Zeitrastereintrag", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StundenplanZeitraster.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ERSTELLEN)) {
    		return (new DataStundenplanZeitraster(conn, null).patch(id, is));
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage eines Raumes eines Stundenplans.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id            die ID des Raumes
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              der Raum eines Stundenplans
     */
    @GET
    @Path("/raeume/{id : \\d+}")
    @Operation(summary = "Gibt den Raum eines Stundeplans zurück.",
               description = "Gibt den Raum eines Stundeplans zurück. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Raum",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = StundenplanRaum.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Raum eines Stundenplans gefunden")
    public Response getStundenplanRaum(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
    		return (new DataStundenplanRaeume(conn, null)).get(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Patchen eines Raumes eines Stundenplans.
     *
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation des Raumes
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/raeume/{id : \\d+}")
    @Operation(summary = "Passt den Raum mit der angebenen ID an.",
    description = "Passt den Raum mit der angebenen ID an. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchStundenplanRaum(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für den Raum", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StundenplanRaum.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ERSTELLEN)) {
    		return (new DataStundenplanRaeume(conn, null).patch(id, is));
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage einer Pausenzeit eines Stundenplans.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id            die ID der Pausenzeit
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Pausenzeit eines Stundenplans
     */
    @GET
    @Path("/pausenzeiten/{id : \\d+}")
    @Operation(summary = "Gibt die Pausenzeit eines Stundeplans zurück.",
               description = "Gibt die Pausenzeit eines Stundeplans zurück. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Pausenzeit",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = StundenplanPausenzeit.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Pausenzeit eines Stundenplans gefunden")
    public Response getStundenplanPausenzeit(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
    		return (new DataStundenplanPausenzeiten(conn, null)).get(id);
    	}
    }

    /**
     * Die OpenAPI-Methode für das Patchen einer Pausenzeit eines Stundenplans.
     *
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation der Pausenzeit
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/pausenzeiten/{id : \\d+}")
    @Operation(summary = "Passt die Pausenzeit mit der angebenen ID an.",
    description = "Passt die Pausenzeit mit der angebenen ID an. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchStundenplanPausenzeit(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für die Pausenzeit", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StundenplanPausenzeit.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ERSTELLEN)) {
    		return (new DataStundenplanPausenzeiten(conn, null).patch(id, is));
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage von Kalenderwochen-Zuordnungen eines Stundenplans.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id            die ID der Kalenderwochen-Zuordnung
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Kalenderwochen-Zuordnung eines Stundenplans
     */
    @GET
    @Path("/kalenderwochen/{id : \\d+}")
    @Operation(summary = "Gibt die Kalenderwochen-Zuordnung eines Stundeplans zurück.",
               description = "Gibt die Kalenderwochen-Zuordnung eines Stundeplans zurück. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Kalenderwochen-Zuordnung",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = StundenplanKalenderwochenzuordnung.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Kalenderwochen-Zuordnung eines Stundenplans gefunden")
    public Response getStundenplanKalenderwochenzuordnung(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
    		return (new DataStundenplanKalenderwochenzuordnung(conn, null)).get(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Patchen einer Kalenderwochen-Zuordnung eines Stundenplans.
     *
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation der Kalenderwochen-Zuordnung
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/kalenderwochen/{id : \\d+}")
    @Operation(summary = "Passt die Kalenderwochen-Zuordnung mit der angebenen ID an.",
    description = "Passt die Kalenderwochen-Zuordnung mit der angebenen ID an. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchStundenplanKalenderwochenzuordnung(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für die Kalenderwochen-Zuordnung", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StundenplanKalenderwochenzuordnung.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ERSTELLEN)) {
    		return (new DataStundenplanKalenderwochenzuordnung(conn, null).patch(id, is));
    	}
    }



    /**
     * Die OpenAPI-Methode für die Abfrage aller Pausenaufsichten zu einem Stundenplan.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id            die ID des Stundenplans
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return die Pausenaufsichten für den angegebenen Stundenplan
     */
    @GET
    @Path("/{id : \\d+}/pausenaufsichten")
    @Operation(summary = "Gibt die Pausenaufsichten des Stundeplans mit der angegebenen ID zurück.",
               description = "Gibt die Pausenaufsichten des Stundeplans mit der angegebenen ID zurück. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Pausenaufsichten des Stundenplans",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StundenplanPausenaufsicht.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Stundenplandaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Stundenplandaten gefunden")
    public Response getStundenplanPausenaufsichten(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
    		return (new DataStundenplanPausenaufsichten(conn, id)).getList();
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage einer Pausenaufsicht eines Stundenplans.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id            die ID der Pausenaufsicht
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Pausenaufsicht
     */
    @GET
    @Path("/pausenaufsicht/{id : \\d+}")
    @Operation(summary = "Gibt eine Pausenaufsicht eines Stundeplans zurück.",
               description = "Gibt eine Pausenaufsicht eines Stundeplans zurück. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Pausenaufsicht",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = StundenplanPausenaufsicht.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Pausenaufsicht eines Stundenplans gefunden")
    public Response getStundenplanPausenaufsicht(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
    		return (new DataStundenplanPausenaufsichten(conn, null)).get(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Patchen einer Pausenaufsicht eines Stundenplans.
     *
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation der Pausenaufsicht
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/pausenaufsicht/{id : \\d+}")
    @Operation(summary = "Passt die Pausenaufsicht mit der angebenen ID an.",
    description = "Passt die Pausenaufsicht mit der angebenen ID an. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchStundenplanPausenaufsicht(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für die Kalenderwochen-Zuordnung", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StundenplanPausenaufsicht.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ERSTELLEN)) {
    		return (new DataStundenplanPausenaufsichten(conn, null).patch(id, is));
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage aller Unterrichte zu einem Stundenplan.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id            die ID des Stundenplans
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return die Unterrichte für den angegebenen Stundenplan
     */
    @GET
    @Path("/{id : \\d+}/unterrichte")
    @Operation(summary = "Gibt die Unterrichte des Stundeplans mit der angegebenen ID zurück.",
               description = "Gibt die Unterrichte des Stundeplans mit der angegebenen ID zurück. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Unterrichte des Stundenplans",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StundenplanUnterricht.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Stundenplandaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Stundenplandaten gefunden")
    public Response getStundenplanUnterrichte(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
    		return (new DataStundenplanUnterricht(conn, id)).getList();
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage eines Unterrichts eines Stundenplans.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id            die ID des Unterrichts
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              der Unterricht
     */
    @GET
    @Path("/unterricht/{id : \\d+}")
    @Operation(summary = "Gibt einen Unterricht eines Stundeplans zurück.",
               description = "Gibt einen Unterricht eines Stundeplans zurück. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Unterricht",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = StundenplanUnterricht.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Unterricht eines Stundenplans gefunden")
    public Response getStundenplanUnterricht(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
    		return (new DataStundenplanUnterricht(conn, null)).get(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Patchen eines Unterrichts eines Stundenplans.
     *
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation des Unterrichts
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/unterricht/{id : \\d+}")
    @Operation(summary = "Passt den Unterricht mit der angebenen ID an.",
    description = "Passt den Unterricht mit der angebenen ID an. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchStundenplanUnterricht(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für den Unterricht", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StundenplanUnterricht.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ERSTELLEN)) {
    		return (new DataStundenplanUnterricht(conn, null).patch(id, is));
    	}
    }



    /**
     * Die OpenAPI-Methode für die Abfrage von Daten zu Unterrichtsverteilung zu einem Stundenplan.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id            die ID des Stundenplans
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Daten zur Unterrichtsverteilung für den angegebenen Stundenplan
     */
    @GET
    @Path("/{id : \\d+}/unterrichtsverteilung")
    @Operation(summary = "Gibt die Daten zur Unterrichtsverteilung des Stundenplans mit der angegebenen ID zurück.",
               description = "Gibt die Daten zur Unterrichtsverteilung des Stundenplans mit der angegebenen ID zurück. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Daten zur Unterrichtsverteilung des Stundenplans",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StundenplanUnterrichtsverteilung.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Stundenplandaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Stundenplandaten gefunden")
    public Response getStundenplanUnterrichtsverteilung(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
    		return (new DataStundenplanUnterrichtsverteilung(conn)).get(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage eines Lehrers eines Stundenplans.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id            die ID des Lehrers
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              der Lehrer
     */
    @GET
    @Path("/lehrer/{id : \\d+}")
    @Operation(summary = "Gibt den Lehrer eines Stundenplans zurück.",
               description = "Gibt den Lehrer eines Stundenplans zurück. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Lehrer",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = StundenplanLehrer.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Raum eines Stundenplans gefunden")
    public Response getStundenplanLehrer(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
    		return (new DataStundenplanLehrer(conn, null)).get(id);
    	}
    }



}

