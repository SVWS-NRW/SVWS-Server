package de.svws_nrw.api.server;

import java.io.InputStream;
import java.util.List;

import de.svws_nrw.core.data.benutzer.BenutzerAllgemeinCredentials;
import de.svws_nrw.core.data.benutzer.BenutzerDaten;
import de.svws_nrw.core.data.benutzer.BenutzerEMailDaten;
import de.svws_nrw.core.data.benutzer.BenutzerKompetenzGruppenKatalogEintrag;
import de.svws_nrw.core.data.benutzer.BenutzerKompetenzKatalogEintrag;
import de.svws_nrw.core.data.benutzer.BenutzerListeEintrag;
import de.svws_nrw.core.data.benutzer.BenutzergruppeDaten;
import de.svws_nrw.core.data.benutzer.BenutzergruppeListeEintrag;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.benutzer.DataBenutzerDaten;
import de.svws_nrw.data.benutzer.DataBenutzerEMailDaten;
import de.svws_nrw.data.benutzer.DataBenutzergruppeDaten;
import de.svws_nrw.data.benutzer.DataBenutzergruppeliste;
import de.svws_nrw.data.benutzer.DataBenutzerkompetenzGruppenliste;
import de.svws_nrw.data.benutzer.DataBenutzerkompetenzliste;
import de.svws_nrw.data.benutzer.DataBenutzerliste;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.utils.OperationError;
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
    public Response getBenutzerliste(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataBenutzerliste(conn).getList(),
        	request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
    }

    /**
     * Die OpenAPI-Methode für die Abfrage der Daten des angemeldeten Benutzers, der
     * die Abfrage ausführt.
     *
     * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden
     *                soll
     * @param request die Informationen zur HTTP-Anfrage
     *
     * @return die Daten des angemeldeten Benutzers, der diese Abfrage ausführt.
     */
    @GET
    @Path("/angemeldet/daten")
    @Operation(summary = "Liefert zu dem angemeldeten Benutzer, der diese Abfrage ausführt, die zugehörigen Daten.",
    	description = "Liefert zu dem angemeldeten Benutzer, der diese Abfrage ausführt, die zugehörigen Daten. "
            + "Dabei wird geprüft, ob der SVWS-Benutzer angemeldet ist.")
    @ApiResponse(responseCode = "200", description = "Die Daten des Benutzers", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BenutzerDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Benutzerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Benutzer-Eintrag gefunden")
    public Response getBenutzerDatenEigene(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> {
        	final Benutzer user = conn.getUser();
        	if (user == null)
        		throw OperationError.NOT_FOUND.exception("Kein Benutzer angemeldet.");
            return (new DataBenutzerDaten(conn).get(user.getId()));
        }, request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
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
    public Response getBenutzerDaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
            @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransactionAllowSelf(conn -> new DataBenutzerDaten(conn).get(id),
        	request, ServerMode.STABLE, id, BenutzerKompetenz.ADMIN);
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
    public Response getBenutzergruppenliste(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataBenutzergruppeliste(conn).getList(),
        	request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
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
    public Response getBenutzergruppeDaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
            @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataBenutzergruppeDaten(conn).get(id),
			request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
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
    public Response getBenutzerMitGruppenID(@PathParam("schema") final String schema, @PathParam("id") final long id,
            @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataBenutzerliste(conn).getListMitGruppenID(id),
        	request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
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
    public Response setAnzeigename(@PathParam("schema") final String schema, @PathParam("id") final long id,
            @RequestBody(description = "Der Anzeigename", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class))) final InputStream is,
            @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransactionAllowSelf(conn -> new DataBenutzerDaten(conn).setAnzeigename(id, JSONMapper.toString(is)),
    		request, ServerMode.STABLE, id, BenutzerKompetenz.ADMIN);
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
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Setzen des Anmeldenamenss besitzt.")
    @ApiResponse(responseCode = "204", description = "Der Anmeldename wurde erfolgreich gesetzt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um das Kennwort zu setzen.")
    @ApiResponse(responseCode = "404", description = "Der Anmeldename zu dem Benutzer sind nicht vorhanden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response setBenutzername(@PathParam("schema") final String schema, @PathParam("id") final long id,
            @RequestBody(description = "Der Anmeldename", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class))) final InputStream is,
            @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransactionAllowSelf(conn -> new DataBenutzerDaten(conn).setBenutzername(id, JSONMapper.toString(is)),
        	request, ServerMode.STABLE, id, BenutzerKompetenz.ADMIN);
    }

    /**
     * Die OpenAPI-Methode für Setzen eines neuen Passworts.
     *
     * @param schema  das Datenbankschema, in welchem der Benutzer ist.
     * @param id      die ID des Benutzers
     * @param is      der Input-Stream mit dem neuen Passwort
     * @param request die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @POST
    @Path("/{id : \\d+}/password")
    @Operation(summary = "Setzt das neue Passwort eines Benutzers.", description = "Setzt das neue Passwort eines Benutzers."
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Setzen des Kennwortes besitzt.")
    @ApiResponse(responseCode = "204", description = "Das Passwort wurde erfolgreich gesetzt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um das Kennwort zu setzen.")
    @ApiResponse(responseCode = "404", description = "Das Passwort zu dem Benutzer sind nicht vorhanden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response setPassword(
            @PathParam("schema") final String schema, @PathParam("id") final long id,
            @RequestBody(description = "Das Kennwort", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class))) final InputStream is,
            @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransactionAllowSelf(conn -> new DataBenutzerDaten(conn).setPassword(id, JSONMapper.toString(is)),
        	request, ServerMode.STABLE, id, BenutzerKompetenz.ADMIN);
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
            @PathParam("schema") final String schema, @PathParam("id") final long id,
            @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataBenutzerDaten(conn).addAdmin(id),
        	request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
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
    @ApiResponse(responseCode = "204", description = "Die Admin-Berechtigung wurde erfolgreich entfernt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Admin-Berechtigung zu entfernen.")
    @ApiResponse(responseCode = "404", description = "Der Benutzer ist nicht vorhanden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response removeBenutzerAdmin(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataBenutzerDaten(conn).removeAdmin(id),
        	request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
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
            @PathParam("schema") final String schema, @PathParam("id") final long id,
            @RequestBody(description = "Die Kompetenzen", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> kids,
            @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransactionAllowSelf(conn -> new DataBenutzerDaten(conn).addKompetenzen(id, kids),
        	request, ServerMode.STABLE, id, BenutzerKompetenz.ADMIN);
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

    @ApiResponse(responseCode = "204", description = "Die Kompetenzen wurden erfolgreich entfernt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kompetenzen zu entfernen.")
    @ApiResponse(responseCode = "404", description = "Benötigte Information zum Benutzer wurden nicht in der DB gefunden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response removeBenutzerKompetenzen(
            @PathParam("schema") final String schema, @PathParam("id") final long id,
            @RequestBody(description = "Die Kompetenzen", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> kids,
            @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransactionAllowSelf(conn -> new DataBenutzerDaten(conn).removeKompetenzen(id, kids),
        	request, ServerMode.STABLE, id, BenutzerKompetenz.ADMIN);
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
    public Response getKatalogBenutzerkompetenzen(@PathParam("schema") final String schema,
            @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataBenutzerkompetenzliste().getList(),
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
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
    public Response getKatalogBenutzerkompetenzgruppen(@PathParam("schema") final String schema,
            @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataBenutzerkompetenzGruppenliste().getList(),
        	request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
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
            @PathParam("schema") final String schema, @PathParam("id") final long id,
            @RequestBody(description = "Die Bezeichnung der Benutzergruppe.", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class))) final InputStream is,
            @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataBenutzergruppeDaten(conn).setBezeichnung(id, JSONMapper.toString(is)),
			request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
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
    public Response addBenutzergruppeAdmin(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataBenutzergruppeDaten(conn).addAdmin(id),
        	request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
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
    public Response removeBenutzergruppeAdmin(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataBenutzergruppeDaten(conn).removeAdmin(id),
        	request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
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
    public Response addBenutzergruppeKompetenzen(@PathParam("schema") final String schema, @PathParam("id") final long id,
            @RequestBody(description = "Die Kompetenzen", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> kids,
            @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataBenutzergruppeDaten(conn).addKompetenzen(id, kids),
        	request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
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
    public Response removeBenutzergruppeKompetenzen(@PathParam("schema") final String schema, @PathParam("id") final long id,
            @RequestBody(description = "Die Kompetenzen", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> kids,
            @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataBenutzergruppeDaten(conn).removeKompetenzen(id, kids),
        	request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
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
    @ApiResponse(responseCode = "200", description = "Die Benutzer wurden erfolgreich hinzugefügt.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BenutzergruppeDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um neue Benutzer hinzuzufügen.")
    @ApiResponse(responseCode = "404", description = "Benötigte Information zum Benutzer wurden nicht in der DB gefunden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addBenutzergruppeBenutzer(
            @PathParam("schema") final String schema, @PathParam("id") final long id,
            @RequestBody(description = "Die Benutzer", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> bids,
            @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataBenutzergruppeDaten(conn).addBenutzer(id, bids),
    		request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
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
    @ApiResponse(responseCode = "200", description = "Die Benutzer wurden erfolgreich hinzugefügt.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BenutzergruppeDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um neue Benutzer zu entfernen.")
    @ApiResponse(responseCode = "404", description = "Benötigte Information zum Benutzer wurden nicht in der DB gefunden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response removeBenutzergruppeBenutzer(
            @PathParam("schema") final String schema, @PathParam("id") final long id,
            @RequestBody(description = "Die Benutzer", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> bids,
            @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataBenutzergruppeDaten(conn).removeBenutzer(id, bids),
        	request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
    }

    // TODO Methode setBenutzergruppeKompetenz aufteilen (siehe bei Benutzer) in zwei API-Methoden: addBenutzergruppeKompetenz (POST) und removeBenutzergruppeKompetenz (DELETE)


    /**
     * Die OpenAPI-Methode für das Erstellen eines neuen Benutzers mit seinen Credentials und einem Anzeigenamen.
     *
     * @param schema    das Datenbankschema, in welchem der Benutzer erstellt wird
     * @param cred      die Benutzer-Credentials mit dem Anzeigenamen des allgemeinen Benutzers
     * @param request   die Informationen zur HTTP-Anfrage
     * @return die HTTP-Antwort mit dem Daten-Objekt zum neu angelegten Benutzer
     */
    @POST
    @Path("/new")
    @Operation(summary = "Erstellt einen neuen Benutzer und gibt ihn zurück.",
    description = "Erstellt einen neuen Benutzer und gibt ihn zurück."
                + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Benutzers "
                + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Benutzer wurde erfolgreich angelegt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = BenutzerDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Benutzer anzulegen.")
    @ApiResponse(responseCode = "409", description = "Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response createBenutzerAllgemein(@PathParam("schema") final String schema,
    		@RequestBody(description = "Der Post für die Benutzer-Daten", required = true, content =
    		@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BenutzerAllgemeinCredentials.class)))
    		final BenutzerAllgemeinCredentials cred, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataBenutzerDaten(conn).createBenutzerAllgemein(cred),
    			request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
    }

    /**
     * Die OpenAPI-Methode zum Löschen von einer oder mehreren Benutzern
     *
     * @param schema    das Datenbankschema
     * @param bids      die IDs der Benutzer
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @DELETE
    @Path("/remove")
    @Operation(summary = "Löscht einen oder mehrere Benutzer.", description = "Löscht einen oder mehrere Benutzer."
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Benutzer wurden erfolgreich gelöscht.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Benutzer zu löschen.")
    @ApiResponse(responseCode = "404", description = "Benötigte Information zum Benutzer wurden nicht in der DB gefunden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response removeBenutzerMenge(
            @PathParam("schema") final String schema,
            @RequestBody(description = "Die IDs der Benutzer", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> bids,
            @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataBenutzerDaten(conn).removeBenutzerMenge(bids),
        	request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
    }

    /**
     * Die OpenAPI-Methode für das Erstellen eines neuen Benutzers.
     *
     * @param schema                das Datenbankschema, in welchem der Benutzer erstellt wird
     * @param request                die Informationen zur HTTP-Anfrage
     * @param is                          JSON-Objekt mit den Daten
     * @return die HTTP-Antwort mit der neuen Blockung
     */
    @POST
    @Path("/benutzergruppe/new")
    @Operation(summary = "Erstellt eine neue Benutzergruppe und gibt sie zurück.",
    description = "Erstellt eine neue Benutzergruppe und gibt sie zurück."
                + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Benutzergruppe "
                + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Benutzergruppe wurde erfolgreich angelegt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = BenutzergruppeDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Benutzer anzulegen.")
    @ApiResponse(responseCode = "409", description = "Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response createBenutzergruppe(@PathParam("schema") final String schema,
            @RequestBody(description = "Der Post für die Benutzergruppe-Daten", required = true, content =
            @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BenutzergruppeDaten.class))) final InputStream is,
            @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataBenutzergruppeDaten(conn).create(is),
        	request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
    }

    /**
     * Die OpenAPI-Methode zum Löschen von einer oder mehreren Benutzergruppen
     *
     * @param schema    das Datenbankschema
     * @param bgids      die IDs der Benutzergruppen
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @DELETE
    @Path("/guppe/remove")
    @Operation(summary = "Löscht eine oder mehrere Benutzergruppe.", description = "Löscht eine oder mehrere Benutzergruppe."
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Benutzergruppen wurden erfolgreich gelöscht.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Benutzergruppen zu löschen.")
    @ApiResponse(responseCode = "404", description = "Benötigte Information zur Benutzergruppe wurden nicht in der DB gefunden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response removeBenutzerGruppe(@PathParam("schema") final String schema,
            @RequestBody(description = "Die IDs der Benutzergruppen", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> bgids,
            @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataBenutzergruppeDaten(conn).remove(bgids),
        	request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
    }



    /**
     * Die OpenAPI-Methode für die Abfrage der EMail-Daten des angemeldeten Benutzers, der
     * die Abfrage ausführt.
     *
     * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request die Informationen zur HTTP-Anfrage
     *
     * @return die EMail-Daten des angemeldeten Benutzers, der diese Abfrage ausführt.
     */
    @GET
    @Path("/angemeldet/daten/email")
    @Operation(summary = "Liefert zu dem angemeldeten Benutzer, der diese Abfrage ausführt, die zugehörigen EMail-Daten.",
    	description = "Liefert zu dem angemeldeten Benutzer, der diese Abfrage ausführt, die zugehörigen EMail-Daten. "
            + "Dabei wird geprüft, ob der SVWS-Benutzer angemeldet ist.")
    @ApiResponse(responseCode = "200", description = "Die EMail-Daten des Benutzers", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BenutzerEMailDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Benutzerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Benutzer-Eintrag gefunden")
    @ApiResponse(responseCode = "500", description = "Ein interner Fehler ist aus dem Server aufgetreten")
    public Response getBenutzerEmailDaten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> {
        	final Benutzer user = conn.getUser();
        	if (user == null)
        		throw OperationError.NOT_FOUND.exception("Kein Benutzer angemeldet.");
            return (new DataBenutzerEMailDaten(conn).get(user.getId()));
        }, request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }


    /**
     * Die OpenAPI-Methode für das Patchen der EMail-Daten des angemeldeten Benutzers, der
     * die Abfrage ausführt.
     *
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/angemeldet/daten/email")
    @Operation(summary = "Passt die EMail-Daten des angemeldeten Benutzers an.",
    	description = "Passt die EMail-Daten des angemeldeten Benutzers an.")
    @ApiResponse(responseCode = "204", description = "Der Patch wurde erfolgreich integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchBenutzerEmailDaten(@PathParam("schema") final String schema,
    		@RequestBody(description = "Der Patch für die EMail-Daten", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BenutzerEMailDaten.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> {
        	final Benutzer user = conn.getUser();
        	if (user == null)
        		throw OperationError.NOT_FOUND.exception("Kein Benutzer angemeldet.");
    		return new DataBenutzerEMailDaten(conn).patch(user.getId(), is);
    	}, request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }


}
