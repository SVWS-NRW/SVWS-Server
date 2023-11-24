package de.svws_nrw.api.server;

import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursAufteilung;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungListeneintrag;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsdaten;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.gost.DataGostBlockungKurs;
import de.svws_nrw.data.gost.DataGostBlockungKursLehrer;
import de.svws_nrw.data.gost.DataGostBlockungRegel;
import de.svws_nrw.data.gost.DataGostBlockungSchiene;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.data.gost.DataGostBlockungsliste;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.module.pdf.pdf.gost.kursplanung.PdfGostKursplanungKurseMitKursschuelern;
import de.svws_nrw.module.pdf.pdf.gost.kursplanung.PdfGostKursplanungSchuelerMitKursen;
import de.svws_nrw.module.pdf.pdf.gost.kursplanung.PdfGostKursplanungSchuelerMitSchienenKursen;
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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.InputStream;
import java.util.List;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die SVWS-Datenbank in Bezug auf die
 * Kursplanung für die gymnasiale Oberstufe.
 * Ein Zugriff erfolgt über den Pfad https://{hostname}/db/{schema}/gost/.
 */
@Path("/db/{schema}/gost")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIGostKursplanung {

    /**
     * Die OpenAPI-Methode für die Abfrage der Liste der Blockungen der gymnasialen Oberstufe
     * im angegebenen Schema für den angegebenen Abitur-Jahrgang und das angegebene Halbjahr.
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
                           + "welche für den angegebenen Abitur-Jahrgang und das angegebene Halbjahr festgelegt wurden.. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Blockungsdaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Blockungs-Listen-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostBlockungListeneintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Blockungsdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Blockungs-Einträge gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden")
    public Response getGostAbiturjahrgangBlockungsliste(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr, @PathParam("halbjahr") final int halbjahr, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungsliste(conn, abiturjahr)).get(halbjahr);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Erstellen einer neuen Blockung der gymnasialen Oberstufe.
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Blockungen "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Blockung wurde erfolgreich angelegt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = GostBlockungsdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Blockung anzulegen.")
    @ApiResponse(responseCode = "404", description = "Keine Fachwahlinformationen zum Anlegen einer Blockung gefunden")
    @ApiResponse(responseCode = "409", description = "Das Abiturjahr oder das Halbjahr ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response createGostAbiturjahrgangBlockung(
    		@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
    		@PathParam("halbjahr") final int halbjahr,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungsdaten(conn)).create(abiturjahr, halbjahr);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Löschen von Blockungsdaten der gymnasialen Oberstufe.
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
    @ApiResponse(responseCode = "404", description = "Keine Blockung mit der angegebenen ID gefunden.")
    public Response deleteGostBlockung(@PathParam("schema") final String schema, @PathParam("blockungsid") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungsdaten(conn)).delete(id);
    	}
    }


   /**
     * Die OpenAPI-Methode für die Abfrage der Blockungsdaten der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)) {
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
    @ApiResponse(responseCode = "404", description = "Keine Blockung mit der angegebenen ID gefunden.")
    public Response rechneGostBlockung(@PathParam("schema") final String schema, @PathParam("blockungsid") final long id, @PathParam("zeit") final long zeit, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungsdaten(conn)).berechne(id, zeit);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Anpassen der Blockungsdaten der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungsdaten(conn)).patch(id, is);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Hinzufügen eines weiteren Kurses zu einer
     * Blockung der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungKurs(conn)).addKurs(idBlockung, idFach, idKursart);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Entfernen eines weiteren Kurses zu einer
     * Blockung der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungKurs(conn)).deleteKurs(idBlockung, idFach, idKursart);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage eines Kurses einer Blockung der gymnasialen Oberstufe.
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
    @ApiResponse(responseCode = "200", description = "Der Kurs der Blockung der gymnasialen Oberstufe für die angegebene ID",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = GostBlockungKurs.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe auszulesen.")
    @ApiResponse(responseCode = "404", description = "Kein Kurs einer Blockung mit der angebenen ID gefunden.")
    public Response getGostBlockungKurs(@PathParam("schema") final String schema, @PathParam("kursid") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungKurs(conn)).get(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Anpassen eines Kurses einer Blockung der gymnasialen Oberstufe.
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
    @ApiResponse(responseCode = "404", description = "Kein Kurs einer Blockung mit der angegebenen ID gefunden.")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchGostBlockungKurs(
    		@PathParam("schema") final String schema, @PathParam("kursid") final long id,
    		@RequestBody(description = "Der Patch für der Kurs der Blockung", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostBlockungKurs.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungKurs(conn)).patch(id, is);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Aufteilen eines Kurses bei einer
     * Blockung der gymnasialen Oberstufe.
     *
     * @param schema    das Datenbankschema, in welchem der Kurs der Blockung aufgeteilt wird
     * @param idKurs    die ID des aufzuteilenden Kurses
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit den neuen Kursen der Blockung der gymnasialen Oberstufe
     */
    @POST
    @Path("/blockungen/kurse/{kursid : \\d+}/split")
    @Operation(summary = "Teilt einen Kurs einer Blockung der Gymnasialen Oberstufe auf, indem ein zweiter Kurs mit der Hälfte der schüler erzeugt wird.",
               description = "Teilt einen Kurs einer Blockung der Gymnasialen Oberstufe auf, indem ein zweiter Kurs mit der Hälfte der schüler erzeugt wird."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Teilen eines Kurses hat.")
    @ApiResponse(responseCode = "200", description = "Der zusätzliche Kurs der Blockung der gymnasialen Oberstufe",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostBlockungKursAufteilung.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Kurs hinzuzufügen.")
    @ApiResponse(responseCode = "404", description = "Keine Blockung vorhanden")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response splitGostBlockungKurs(
    		@PathParam("schema") final String schema, @PathParam("kursid") final long idKurs,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungKurs(conn)).splitKurs(idKurs);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Zusammenführen eines Kurses bei einer
     * Blockung der gymnasialen Oberstufe mit einem zweiten Kurs des gleichen
     * Faches und Kursart. Der zweite Kurs fällt dabei weg.
     *
     * @param schema       das Datenbankschema, in welchem der Kurs der Blockung aufgeteilt wird
     * @param idKurs1      die ID des ersten Kurses
     * @param idKurs2      die ID des zweiten Kurses
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit dem resultierenden ersten Kurs der Blockung der gymnasialen Oberstufe, wobei der zweite Kurs wegfällt
     */
    @POST
    @Path("/blockungen/kurse/{kursid1 : \\d+}/combine/{kursid2 : \\d+}")
    @Operation(summary = "Führt zwei Kurse einer Blockung der Gymnasialen Oberstufe zusammen, sofern Fach und Kursart zusammenpassen.",
               description = "Führt zwei Kurse einer Blockung der Gymnasialen Oberstufe zusammen, sofern Fach und Kursart zusammenpassen."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Zusammenführen der Kurse hat.")
    @ApiResponse(responseCode = "200", description = "Der zusammengeführte Kurs der Blockung der gymnasialen Oberstufe",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GostBlockungKurs.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um zwei Kurse zusammenzuführen.")
    @ApiResponse(responseCode = "404", description = "Keine Blockung vorhanden")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response combineGostBlockungKurs(
    		@PathParam("schema") final String schema, @PathParam("kursid1") final long idKurs1,
    		@PathParam("kursid2") final long idKurs2, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungKurs(conn)).combineKurs(idKurs1, idKurs2);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Löschen eines Kurses bei einer
     * Blockung der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungKurs(conn)).delete(idKurs);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Daten eines Kurs-Lehrers eines Kurses einer Blockung
     * der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungKursLehrer(conn, idKurs)).get(idLehrer);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Anpassen der Daten eines Kurs-Lehrers eines Kurses einer Blockung
     * der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungKursLehrer(conn, idKurs)).patch(idLehrer, is);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Hinzufügen eines Kurs-Lehrers zu einem Kurs einer Blockung
     * der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungKursLehrer(conn, idKurs)).addKurslehrer(idLehrer);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Entfernen eines Kurs-Lehrers eines Kurses einer Blockung
     * der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungKursLehrer(conn, idKurs)).deleteKurslehrer(idLehrer);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Hinzufügen einer weiteren Schiene zu einer
     * Blockung der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungSchiene(conn)).addSchiene(idBlockung);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Entfernen einer Schiene bei einer
     * Blockung der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungSchiene(conn)).deleteSchiene(idBlockung);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage einer Schiene einer Blockung der gymnasialen Oberstufe.
     *
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id         die ID der Schiene der Blockung
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungSchiene(conn)).get(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Anpassen einer Schiene einer Blockung der gymnasialen Oberstufe.
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
    @ApiResponse(responseCode = "404", description = "Keine Schiene einer Blockung mit der angegebenen ID gefunden.")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchGostBlockungSchiene(
    		@PathParam("schema") final String schema, @PathParam("schienenid") final long id,
    		@RequestBody(description = "Der Patch für die Schiene der Blockung", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GostBlockungSchiene.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungSchiene(conn)).patch(id, is);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Löschen einer Schiene bei einer
     * Blockung der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungSchiene(conn)).delete(idSchiene);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Hinzufügen einer Regel zu einer
     * Blockung der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungRegel(conn)).addRegel(idBlockung, typRegel, regelParameter);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Hinzufügen mehrerer Regeln zu einer
     * Blockung der gymnasialen Oberstufe.
     *
     * @param schema          das Datenbankschema, in welchem die Regel der Blockung erstellt wird
     * @param idBlockung      die ID der Blockung
     * @param regeln          die Regeldefinitionen mit einer Pseudo-ID -1
     * @param request         die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/blockungen/{blockungsid : \\d+}/addregeln")
    @Operation(summary = "Fügt mehrere Regeln zu einer Blockung der Gymnasialen Oberstufe hinzu.",
               description = "Fügt mehrere Regeln zu einer Blockung der Gymnasialen Oberstufe hinzu."
    		               + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen von Regeln hat.")
    @ApiResponse(responseCode = "200", description = "Die Regeln wurden erfolgreich der Blockung hinzugefügt",
    		content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostBlockungRegel.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Regeln hinzuzufügen.")
    @ApiResponse(responseCode = "404", description = "Keine Blockung vorhanden")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addGostBlockungRegeln(
    		@PathParam("schema") final String schema, @PathParam("blockungsid") final long idBlockung,
            @RequestBody(description = "Die Regeln", required = false, content = @Content(mediaType = MediaType.APPLICATION_JSON,
            array = @ArraySchema(schema = @Schema(implementation = GostBlockungRegel.class)))) final List<GostBlockungRegel> regeln,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataGostBlockungRegel(conn).addRegeln(idBlockung, regeln),
    		request, ServerMode.STABLE,
    		BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN);
    }


    /**
     * Die OpenAPI-Methode für die Abfrage einer Regel einer Blockung der gymnasialen Oberstufe.
     *
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id         die ID der Schiene der Blockung
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
    @ApiResponse(responseCode = "200", description = "Die Regel der Blockung der gymnasialen Oberstufe für die angegebene ID",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = GostBlockungRegel.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe auszulesen.")
    @ApiResponse(responseCode = "404", description = "Keine Regel einer Blockung mit der angegebenen ID gefunden.")
    public Response getGostBlockungRegel(@PathParam("schema") final String schema, @PathParam("regelid") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungRegel(conn)).get(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Anpassen einer Regel einer Blockung der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungRegel(conn)).patch(id, is);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Löschen einer Regel bei einer
     * Blockung der gymnasialen Oberstufe.
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
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataGostBlockungRegel(conn).delete(idRegel),
        		request, ServerMode.STABLE,
        		BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN);
    }


    /**
     * Die OpenAPI-Methode für das Löschen mehrerer Regeln bei einer
     * Blockung der gymnasialen Oberstufe.
     *
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param regelIDs   eine Liste der zu löschenden Regel-IDs
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit dem Status
     */
    @DELETE
    @Path("/blockungen/regeln")
    @Operation(summary = "Entfernt mehrere Regeln einer Blockung der Gymnasialen Oberstufe.",
               description = "Entfernt mehrere Regeln einer Blockung der Gymnasialen Oberstufe. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Regeln wurde wurden erfolgreich gelöscht.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Regeln zu löschen.")
    @ApiResponse(responseCode = "404", description = "Mindestens eine Regel wurde nicht bei einer Blockung gefunden.")
    public Response deleteGostBlockungRegelnByID(@PathParam("schema") final String schema,
            @RequestBody(description = "Die Regeln", required = false, content = @Content(mediaType = MediaType.APPLICATION_JSON,
            array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> regelIDs,
           @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataGostBlockungRegel(conn).deleteMultiple(regelIDs),
        		request, ServerMode.STABLE,
        		BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN);
    }


    /**
     * Die OpenAPI-Methode für die Abfrage von Blockungsergebnissen zu Blockungen der gymnasialen Oberstufe.
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
    @ApiResponse(responseCode = "200", description = "Die Blockungsergebnisse der gymnasialen Oberstufe für die angegebene ID",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = GostBlockungsergebnis.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Blockungsergebnisse einer Blockung der Gymnasialen Oberstufe auszulesen.")
    @ApiResponse(responseCode = "404", description = "Keine Blockung mit der angebenen ID gefunden.")
    public Response getGostBlockungsergebnis(@PathParam("schema") final String schema, @PathParam("ergebnisid") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungsergebnisse(conn)).get(id);
    	}
    }


	/**
	 * Die OpenAPI-Methode für die Abfrage der Kurse-Schienen-Zuordnung eines Blockungsergebnisses als PDF-Datei.
	 *
	 * @param schema      			das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param blockungsergebnisid 	ID des Blockungsergebnisses, dessen Kurse-Schienen-Zuordnung ausgegeben werden soll.
	 * @param schuelerids           Liste der IDs der SuS, deren Kurse-Schienen-Zuordnung erstellt werden soll. Werden keine IDs übergeben, so wird die Matrix allgemein für das Blockungsergebnis erstellt.
	 * @param request     			die Informationen zur HTTP-Anfrage
	 *
	 * @return Die zu den übergebenen IDs zugehörige Kurse-Schienen-Zuordnung
	 */
	@POST
	@Produces("application/pdf")
	@Path("/blockungen/pdf/kurse_schienen_zuordnung/{blockungsergebnisid : \\d+}")
	@Operation(
            summary = "Erstellt eine PDF-Datei mit der Kurse-Schienen-Zuordnung.",
            description = "Erstellt eine PDF-Datei mit der Kurse-Schienen-Zuordnung zum angegebenen Ergebnis einer Blockung. "
                        + "Sofern Schüler-IDs übergeben werden, werden für diese die Zuordnungen ausgegeben, andernfalls die allgemeine Zuordnung."
				   		+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Kurse-Schienen-Zuordnung besitzt.")
	@ApiResponse(
            responseCode = "200",
            description = "Die PDF-Datei mit der Kurse-Schienen-Zuordnung zum angegebenen Ergebnis einer Blockung",
            content = @Content(mediaType = "application/pdf", schema = @Schema(type = "string", format = "binary", description = "Kurse-Schienen-Zuordnung")))
	@ApiResponse(
            responseCode = "403",
            description = "Der SVWS-Benutzer hat keine Rechte, um die Kurse-Schienen-Zuordnung für die gymnasialen Oberstufe zu erstellen.")
	@ApiResponse(
            responseCode = "404",
            description = "Kein Eintrag zur Blockung bzw. deren Ergebnissen für die angegebenen IDs gefunden")
	public Response pdfGostKursplanungKurseSchienenZuordnung(
            @PathParam("schema") final String schema,
            @PathParam("blockungsergebnisid") final long blockungsergebnisid,
            @RequestBody(description = "Schüler-IDs, für die die Kurse-Schienen-Zuordnung erstellt werden soll. Ist die Liste leer, so wird die Zuordnung des Blockungsergebnisses zurückgegeben.",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class))))
            final List<Long> schuelerids,
            @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> PdfGostKursplanungSchuelerMitSchienenKursen.query(conn, blockungsergebnisid, schuelerids),
			request, ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage einer Liste von Schülern mit den Kursbelegungen eines Blockungsergebnisses als PDF-Datei.
	 *
	 * @param schema      			das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param blockungsergebnisid 	ID des Blockungsergebnisses, dessen Schüler-Kurse-Liste ausgegeben werden soll.
	 * @param schuelerids           Liste der IDs der SuS, deren Liste der Kursbelegungen erstellt werden soll.
	 * @param request     			die Informationen zur HTTP-Anfrage
	 *
	 * @return Die zu den übergebenen IDs zugehörige Liste der Kursbelegungen
	 */
	@POST
	@Produces("application/pdf")
	@Path("/blockungen/pdf/schueler_mit_kursen/{blockungsergebnisid : \\d+}")
	@Operation(
            summary = "Erstellt eine PDF-Datei mit einer Liste von Schülern und deren belegten Kursen.",
            description = "Erstellt eine PDF-Datei mit einer Liste von Schülern und deren belegten Kursen zum angegebenen Ergebnis einer Blockung."
                        + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Kurse-Liste eines Schülers besitzt.")
	@ApiResponse(
            responseCode = "200",
            description = "Die PDF-Datei mit einer Liste von Schülern und deren belegten Kursen zum angegebenen Ergebnis einer Blockung",
            content = @Content(mediaType = "application/pdf", schema = @Schema(type = "string", format = "binary", description = "Schüler-Kurse-Liste")))
	@ApiResponse(
            responseCode = "403",
            description = "Der SVWS-Benutzer hat keine Rechte, um die Liste der Kurse der Schüler für die gymnasialen Oberstufe zu erstellen.")
	@ApiResponse(
            responseCode = "404",
            description = "Kein Eintrag zur Blockung bzw. deren Ergebnissen für die angegebenen IDs gefunden")
	public Response pdfGostKursplanungSchuelerMitKursen(
            @PathParam("schema") final String schema,
            @PathParam("blockungsergebnisid") final long blockungsergebnisid,
            @RequestBody(
                    description = "Schüler-IDs, deren Kurse-Liste erstellt werden soll. Ist die Liste leer, so wird die Zuordnung des Blockungsergebnisses zurückgegeben.",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class))))
            final List<Long> schuelerids,
            @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> PdfGostKursplanungSchuelerMitKursen.query(conn, blockungsergebnisid, schuelerids),
			request, ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage einer Liste von Kursen mit deren Schülern zu einem Blockungsergebnis als PDF-Datei.
	 *
	 * @param schema      			das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param blockungsergebnisid 	ID des Blockungsergebnisses, dessen Schüler-Kurse-Liste ausgegeben werden soll.
	 * @param kursids           	Liste der IDs der Kurse, deren Liste der Schüler erstellt werden soll.
	 *                              Werden keine Kurs-IDs übergeben, so werden die Listen für alle Kurse des Blockungsergebnisses erstellt.
	 * @param request     			die Informationen zur HTTP-Anfrage
	 *
	 * @return 						Die zu den übergebenen IDs zugehörige Liste der Kurse mit ihren Schülern.
	 */
	@POST
	@Produces("application/pdf")
	@Path("/blockungen/pdf/kurse_mit kursschuelern/{blockungsergebnisid : \\d+}")
	@Operation(
            summary = "Erstellt eine PDF-Datei mit einer Liste von Kursen mit deren Schülern.",
            description = "Erstellt eine PDF-Datei mit einer Liste von Kursen mit deren Schülern zum angegebenen Ergebnis einer Blockung."
                        + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Kurse-Liste eines Schülers besitzt.")
	@ApiResponse(
            responseCode = "200",
            description = "Die PDF-Datei mit einer Liste von Kursen mit deren Schülern zum angegebenen Ergebnis einer Blockung",

            content = @Content(mediaType = "application/pdf", schema = @Schema(type = "string", format = "binary", description = "Kurs-Schüler-Liste")))
	@ApiResponse(
            responseCode = "403",
            description = "Der SVWS-Benutzer hat keine Rechte, um die Liste der Kurse der Schüler für die gymnasialen Oberstufe zu erstellen.")
	@ApiResponse(
            responseCode = "404",
            description = "Kein Eintrag zur Blockung bzw. deren Ergebnissen für die angegebenen IDs gefunden")
	public Response pdfGostKursplanungKurseMitKursschuelern(
            @PathParam("schema") final String schema,
            @PathParam("blockungsergebnisid") final long blockungsergebnisid,
            @RequestBody(
                    description = "Kurs-IDs, deren Schüler-Liste erstellt werden soll.",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class))))
            final List<Long> kursids,
            @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> PdfGostKursplanungKurseMitKursschuelern.query(conn, blockungsergebnisid, kursids),
			request, ServerMode.STABLE,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN);
	}

	/**
     * Die OpenAPI-Methode für das Aktivieren bzw. Persistieren eines Blockungsergebnisses
     * der gymnasialen Oberstufe in der Kursliste und den Leistungsdaten von Schülern.
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
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataGostBlockungsergebnisse(conn).aktiviere(id), request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN);
    }


	/**
     * Die OpenAPI-Methode für das Synchronisieren eines Blockungsergebnisses
     * der gymnasialen Oberstufe mit den Daten in der Kursliste und den
     * Leistungsdaten von Schülern des Abiturjahrgangs.
     *
     * @param schema       das Datenbankschema
     * @param id           die ID des zu synchronisierenden Blockungsergebnisses
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/synchronize")
    @Operation(summary = "Synchronisiert das Blockungsergebnis mit den Kursen und den Leistungsdaten.",
    description = "Synchronisiert das Blockungsergebnis mit den Kursen und den Leistungsdaten. Dies ist nur erlaubt, wenn Leistungsdaten"
    		    + "in der DB vorliegen. Beim Synchronisieren werden die Kursliste und die Leistungsdaten der "
    		    + "Schüler angepasst. Es werden jedoch keine Kurse entfernt und es werden keine Fachwahlen bei Schülern ergänzt."
    		    + "Dies muss ggf. manuell erfolgen."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Aktivieren eines "
    		    + "Blockungsergebnisses besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Zuordnung wurde erfolgreich synchronisiert.")
    @ApiResponse(responseCode = "400", description = "Das Ergebnis ist einem vergangenem Schuljahresabschnitt zugeordnet. Eine Synchronisation wird hier nicht mehr zugelassen.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um ein Blockungsergebnis mit den Leristungsdaten zu synchronisieren.")
    @ApiResponse(responseCode = "404", description = "Keine oder nicht alle Daten zu dem Ergebnis gefunden, um dieses zu synchronisieren")
    @ApiResponse(responseCode = "409", description = "Es sind noch keinerlei Leistungsdaten für eine Synchronisation in dem Schuljahresabschnitt bei den Schülern vorhanden. Verwenden Sie stattdessen das Aktivieren eines Ergebnisses.")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response syncGostBlockungsergebnis(@PathParam("schema") final String schema, @PathParam("ergebnisid") final long id, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataGostBlockungsergebnisse(conn).synchronisiere(id), request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN);
    }


    /**
     * Die OpenAPI-Methode für das Löschen von Blockungsergebnissen einer Blockung der gymnasialen Oberstufe.
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
    @ApiResponse(responseCode = "200", description = "Das Zwischenergebnis einer Blockung der gymnasialen Oberstufe für die angegebene ID wurde erfolgreich gelöscht.",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um das Zwischenergebnis einer Blockung der Gymnasialen Oberstufe zu löschen.")
    @ApiResponse(responseCode = "404", description = "Keine Blockung mit der angegebenen ID gefunden.")
    public Response deleteGostBlockungsergebnis(@PathParam("schema") final String schema, @PathParam("ergebnisid") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
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
    @ApiResponse(responseCode = "200", description = "Die Blockungsdaten der gymnasialen Oberstufe des Duplikats als Vorlage für die Definition von Regeln",
    	content = @Content(mediaType = "application/json",
    	schema = @Schema(implementation = GostBlockungsdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe zu duplizieren.")
    @ApiResponse(responseCode = "404", description = "Kein Blockungsergebnis mit der angegebenen ID gefunden.")
    public Response dupliziereGostBlockungMitErgebnis(@PathParam("schema") final String schema, @PathParam("ergebnisid") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungsdaten(conn)).hochschreiben(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Restaurieren einer Blockung aus den in der DB
     * Leistungsdaten als aktive Blockung.
     *
     * @param schema       das Datenbankschema, auf welchem die Abfrage ausgeführt werden soll
     * @param abiturjahr   das Abiturjahr
     * @param halbjahr     das Halbjahr
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit der neu erstellten Blockung
     */
    @GET
    @Path("/blockungen/{abiturjahr : \\d+}/{halbjahr : \\d+}/restore")
    @Operation(summary = "Restauriert die Blockung aus den Leistungsdaten.",
               description = "Restauriert die Blockung aus den Leistungsdaten. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Restaurieren einer Blockung "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Blockungsdaten der gymnasialen Oberstufe der restaurierten Blockung",
    	content = @Content(mediaType = "application/json",
    	schema = @Schema(implementation = GostBlockungsdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe zu restaurieren.")
    @ApiResponse(responseCode = "404", description = "Keine Daten für das Abiturjahr und das Halbjahr gefunden.")
    public Response restauriereGostBlockung(@PathParam("schema") final String schema, @PathParam("abiturjahr") final int abiturjahr,
    		@PathParam("halbjahr") final int halbjahr, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungsdaten(conn)).restore(abiturjahr, halbjahr);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Erstellen einer Kurszuordnung zu einem Schüler bei einem Blockungsergebnis einer
     * Blockung der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungsergebnisse(conn)).createKursSchuelerZuordnung(idErgebnis, idSchueler, idKurs);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Verschieben eines Schülers zwischen zwei Kursen bei einem Blockungsergebnis einer
     * Blockung der gymnasialen Oberstufe.
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
        try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
        		BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
            return (new DataGostBlockungsergebnisse(conn)).updateKursSchuelerZuordnung(idErgebnis, idSchueler, idKursAlt, idKursNeu);
        }
    }


    /**
     * Die OpenAPI-Methode für das Löschen einer Kurszuordnung zu einem Schüler bei einem Blockungsergebnis einer
     * Blockung der gymnasialen Oberstufe.
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
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
    		return (new DataGostBlockungsergebnisse(conn)).deleteKursSchuelerZuordnung(idErgebnis, idSchueler, idKurs);
    	}
    }


    /**
     * Die OpenAPI-Methode für das Erstellen einer Kurszuordnung zu einer Schiene bei einem Blockungsergebnis einer
     * Blockung der gymnasialen Oberstufe.
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
        try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
        		BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
            return (new DataGostBlockungsergebnisse(conn)).createKursSchieneZuordnung(idErgebnis, idSchiene, idKurs);
        }
    }


    /**
     * Die OpenAPI-Methode für das Verschieben eines Kurses von einer Schiene zu einer anderen Schiene
     * bei einem Blockungsergebnis einer Blockung der gymnasialen Oberstufe.
     *
     * @param schema         das Datenbankschema, in welchem die Blockung erstellt wird
     * @param idErgebnis     die ID des Blockungsergebnisses
     * @param idSchieneAlt   die ID der Schiene der Blockung in Bezug auf die alte Zuordnung
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
        try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
        		BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
            return (new DataGostBlockungsergebnisse(conn)).updateKursSchieneZuordnung(idErgebnis, idKurs, idSchieneAlt, idSchieneNeu);
        }
    }


    /**
     * Die OpenAPI-Methode für das Löschen einer Kurszuordnung zu einer Schiene bei einem Blockungsergebnis einer
     * Blockung der gymnasialen Oberstufe.
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
        try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
        		BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
    			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)) {
            return (new DataGostBlockungsergebnisse(conn)).deleteKursSchieneZuordnung(idErgebnis, idSchiene, idKurs);
        }
    }

}
