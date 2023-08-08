package de.svws_nrw.api.server;

import java.io.InputStream;
import java.util.List;

import de.svws_nrw.api.OpenAPIApplication;
import de.svws_nrw.core.data.benutzer.BenutzerConfig;
import de.svws_nrw.core.data.benutzer.BenutzerConfigElement;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.client.DTOClientKonfigurationBenutzer;
import de.svws_nrw.db.dto.current.client.DTOClientKonfigurationGlobal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf Client-spezifische
 * Informationen.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/client/...
 */
@Path("/db/{schema}/client")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIClientConfig {


    /**
     * Gibt die Konfigurationseinträge der angegebenen Anwendung zurück.
     *
     * @param schema  das Schema aus der Konfiguration
     * @param app     die Applikation in der Konfiguration
     * @param request   der HTTP-request
     *
     * @return die Key-Value-Paare der Konfigurationserinträge
     *
     */
    @GET
    @Path("/config/{app}")
    @Operation(summary = "Gibt den Konfigurationseinträge der angegebenen Anwendung zurück.",
               description = "Liest die Konfigurationseinträge der angegebenen Client-Anwendung aus.")
    @ApiResponse(responseCode = "200", description = "Die Key-Value-Paare der Konfigurationseinträge als Liste",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = BenutzerConfig.class)))
    public Response getClientConfig(@PathParam("schema") final String schema, @PathParam("app") final String app, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
	    	final List<DTOClientKonfigurationBenutzer> configUser = conn.queryList("SELECT e FROM DTOClientKonfigurationBenutzer e WHERE e.Benutzer_ID = ?1 AND e.AppName = ?2", DTOClientKonfigurationBenutzer.class, conn.getUser().getId(), app);
	    	final List<DTOClientKonfigurationGlobal> configGlobal = conn.queryNamed("DTOClientKonfigurationGlobal.appname", app, DTOClientKonfigurationGlobal.class);
	    	// Ansonsten: Lese aus der globalen Konfiguration
	    	final BenutzerConfig config = new BenutzerConfig();
	    	for (final DTOClientKonfigurationBenutzer cfg : configUser) {
	    		final BenutzerConfigElement elem = new BenutzerConfigElement();
	    		elem.key = cfg.Schluessel;
	    		elem.value = cfg.Wert;
	    		config.user.add(elem);
	    	}
	    	for (final DTOClientKonfigurationGlobal cfg: configGlobal) {
	    		final BenutzerConfigElement elem = new BenutzerConfigElement();
	    		elem.key = cfg.Schluessel;
	    		elem.value = cfg.Wert;
	    		config.global.add(elem);
	    	}
	    	return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(config).build();
    	}
    }


    /**
     * Gibt den Konfigurationseintrag der angegebenen Anwendung für den angebenen Schlüsselwert zurück.
     * Liest den Schlüsselwert aus der Konfiguration für den Client aus
     * Ist sowohl ein globaler als auch eine benutzerspezifischer Konfigurationseintrag unter den Name vorhanden,
     * so wird der benutzerspezifische Eintrag zurückgegeben. Die benutzerspezifische Konfiguratio
     * kann somit globale Einstellungen 'überschreiben'. Ist kein Wert vorhanden, so wird ein leerer String zurückgegeben.
     *
     * @param schema  das Schema aus der Konfiguration
     *
     * @param app     die Applikation in der Konfiguration
     *
     * @param key	  der Schlüsselwert aus der Konfiguration
     *
     * @param request Benutzerkonfiguration
     *
     * @return config
     *
     */
    @GET
    @Path("/config/{app}/user/{key}")
    @Operation(summary = "Gibt den Konfigurationseintrag der angegebenen Anwendung für den angebenen Schlüsselwert zurück.",
               description = "Liest den Schlüsselwert aus der Konfiguration für den Client aus. "
               		       + "Ist sowohl ein globaler als auch eine benutzerspezifischer Konfigurationseintrag unter den Name vorhanden,"
               		       + "so wird der benutzerspezifische Eintrag zurückgegeben. Die benutzerspezifische Konfiguration "
               		       + "kann somit globale Einstellungen 'überschreiben'. Ist kein Wert vorhanden, so wird ein "
               		       + "leerer String zurückgegeben.")
    @ApiResponse(responseCode = "200", description = "Der Wert des Konfigurationseintrags",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    public Response getClientConfigUserKey(@PathParam("schema") final String schema, @PathParam("app") final String app, @PathParam("key") final String key, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
	    	// Prüfe, ob ein benutzerspezifischer Konfigurationseintrag vorliegt und gebe diesen ggf. zurück
	    	final DTOClientKonfigurationBenutzer config = conn.queryByKey(DTOClientKonfigurationBenutzer.class, conn.getUser().getId(), app, key);
	    	if (config != null)
	    		return JSONMapper.fromString(config.Wert);
	    	// Ansonsten: Lese aus der globalen Konfiguration
	    	final DTOClientKonfigurationGlobal configGlobal = conn.queryByKey(DTOClientKonfigurationGlobal.class, app, key);
	    	return JSONMapper.fromString((configGlobal == null) ? null : configGlobal.Wert);
    	}
    }



    /**
     *
     * Schreibt den Konfigurationseintrag der angegebenen Anwendung
     * für den angebenen Schlüsselwert in die benutzerspezifische Konfiguration.
     *
     * @param schema  das Schema aus der Konfiguration
     *
     * @param app     die Applikation in der Konfiguration
     *
     * @param key	  der Schlüsselwert aus der Konfiguration
     *
     * @param data   der Wert der für die benutzerspezifische Clientkonfiguration gesetzt werden soll
     *
     * @param request Benutzerkonfiguration
     *
     */
    @PUT
    @Path("/config/{app}/user/{key}")
    @Operation(summary = "Schreibt den Konfigurationseintrag der angegebenen Anwendung für den angebenen Schlüsselwert in die benutzerspezifische Konfiguration",
               description = "Schreibt den Konfigurationseintrag der angegebenen Anwendung für den angebenen Schlüsselwert in die "
               		       + "benutzerspezifische Konfiguration.")
    @ApiResponse(responseCode = "200", description = "Der Konfigurationseintrag wurde erfolgreich geschrieben")
    public void setClientConfigUserKey(@PathParam("schema") final String schema, @PathParam("app") final String app, @PathParam("key") final String key,
    		                          @RequestBody(description = "Der Wert des Konfigurationseintrags", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class))) final InputStream data,
    		                          @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KEINE)) {
	    	// Prüfe, ob ein benutzerspezifischer Konfigurationseintrag vorliegt und lege einen neuen an oder aktualisiee den bestehenden Eintrag
	    	DTOClientKonfigurationBenutzer config = conn.queryByKey(DTOClientKonfigurationBenutzer.class, conn.getUser().getId(), app, key);
	    	final String strData = JSONMapper.toString(data);
	    	if (config == null) {
	    		config = new DTOClientKonfigurationBenutzer(conn.getUser().getId(), app, key, strData);
	    	} else {
	    		config.Wert = strData;
	    	}
			if (!conn.persist(config))
				throw new WebApplicationException(Status.BAD_REQUEST.getStatusCode());
    	}
    }


    //TODO Getter für getClientConfigGlobalKey
    /**
     * Schreibt den Konfigurationseintrag der angegebenen Anwendung für den angebenen Schlüsselwert in die
     * globale Konfiguration. Dabei wird geprüft, ob der angemeldete Benutzer administrative Rechte hat.
     *
     * @param schema  das Schema aus der globalen Konfiguration
     *
     * @param app     die Applikation in der globalen Konfiguration
     *
     * @param key	  der Schlüsselwert aus der globalen Konfiguration
     *
     * @param data   der Wert der in die globale Clientkonfiguration geschrieben werden soll
     *
     * @param request Benutzerkonfiguration
     *
     */
    @PUT
    @Path("/config/{app}/global/{key}")
    @Operation(summary = "Schreibt den Konfigurationseintrag der angegebenen Anwendung für den angebenen Schlüsselwert in die globale Konfiguration.",
               description = "Schreibt den Konfigurationseintrag der angegebenen Anwendung für den angebenen Schlüsselwert in die "
               		       + "globale Konfiguration. Dabei wird geprüft, ob der angemeldete Benutzer administrative Rechte hat.")
    @ApiResponse(responseCode = "200", description = "Der Konfigurationseintrag wurde erfolgreich geschrieben")
    public void setClientConfigGlobalKey(@PathParam("schema") final String schema, @PathParam("app") final String app, @PathParam("key") final String key,
    		                                @RequestBody(description = "Der Wert des Konfigurationseintrags", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class))) final InputStream data,
    		                                @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.ADMIN)) {
	    	// Prüfe, ob ein globaler Konfigurationseintrag vorliegt und lege einen neuen an oder aktualisiee den bestehenden Eintrag
	    	DTOClientKonfigurationGlobal config = conn.queryByKey(DTOClientKonfigurationGlobal.class, app, key);
	    	final String strData = JSONMapper.toString(data);
	    	if (config == null) {
	    		config = new DTOClientKonfigurationGlobal(app, key, strData);
	    	} else {
	    		config.Wert = strData;
	    	}
			if (!conn.persist(config))
				throw new WebApplicationException(Status.BAD_REQUEST.getStatusCode());
    	}
    }

}
