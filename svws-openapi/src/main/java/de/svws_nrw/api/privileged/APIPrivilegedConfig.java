package de.svws_nrw.api.privileged;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für priviligierte Datenbankbenutzer
 * in Bezug auf den Zugriff auf die SVWS-Konfiguration des Servers
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/api/privileged/...
 */
@Path("/api/privileged")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Privileged")
public class APIPrivilegedConfig {

	/**
	 * Die OpenAPI-Methode für die Abfrage ob der angemeldete Datenbankuser ein priviligierter Datenbank-Benutzer
	 * mit Rechten zur Anpassung der SVWS-Konfiguration ist.
	 *
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return            Rückmeldung, ob der angegebene User existiert
	 */
	@GET
	@Path("/api/schema/root/privileged/{user}")
	@Operation(summary = "Liefert die Information, ob der angemeldete Benutzer ein priviligierter Benutzer mit Rechten zur Anpassung der"
			+ " SVWS-Konfiguration ist.",
			description = "Liefert die Information, ob der angemeldete Benutzer ein priviligierter Benutzer mit Rechten zur Anpassung der"
					+ " SVWS-Konfiguration ist.")
	@ApiResponse(responseCode = "200", description = "true, wenn der Benutzer die Rechte hat",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
	@ApiResponse(responseCode = "403", description = "Der angegebene Benutzer besitzt nicht die Rechte, um auf die priviligierte API zuzugreifen")
	public Response isPrivilegedUser(@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithoutTransaction(conn -> ApiUtils.getResponse(conn.isPrivilegedDatabaseUser()),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}

}
