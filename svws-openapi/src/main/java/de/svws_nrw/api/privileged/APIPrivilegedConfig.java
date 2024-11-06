package de.svws_nrw.api.privileged;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.db.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

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
	 * Leerer Standardkonstruktor.
	 */
	public APIPrivilegedConfig() {
		// leer
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage ob der angemeldete Datenbankuser ein priviligierter Datenbank-Benutzer
	 * mit Rechten zur Anpassung der SVWS-Konfiguration ist.
	 *
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return            Rückmeldung, ob der angegebene User existiert
	 */
	@GET
	@Path("/user/isprivileged")
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


	/**
	 * Die OpenAPI-Methode für das Setzen des Default-Schemas in der SVWS-Konfiguration.
	 * Der angemeldete Datenbankbenutzer muss dafür priviligierte Rechte für die Bearbeitung
	 * der SVWS-Konfiguration haben.
	 *
	 * @param schemaname    der Name des Schemas, das als Default-Schema gesetzt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return Rückmeldung, ob die Operation erfolgreich war
	 */
	@POST
	@Path("/config/default/schema/{schema}")
	@Operation(summary = "Setze das angegebene Schema als Default-Schema, sofern es in der SVWS-Konfiguration eingetragen ist.",
			description = "Setze das angegebene Schema als Default-Schema, sofern es in der SVWS-Konfiguration eingetragen ist und der angemeldete Benutzer"
					+ " die benötigten Rechte besitzt.")
	@ApiResponse(responseCode = "204", description = "Das Schema wurde erfolgreich als Default-Schema gesetzt.")
	@ApiResponse(responseCode = "400", description = "Der Schema-Name darf nicht null oder leer sein.")
	@ApiResponse(responseCode = "403", description = "Das Default-Schema darf nicht gesetzt werden.")
	@ApiResponse(responseCode = "404", description = "Das angegebene Schema wurde nicht gefunden.")
	public Response setDefaultSchemaInConfig(@PathParam("schema") final String schemaname, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithoutTransaction(conn -> {
			// Prüfe, ob der Datenbank-Benutzer priviligiert ist
			if (!conn.isPrivilegedDatabaseUser())
				throw new ApiOperationException(Status.FORBIDDEN);

			// Prüfe, ob der Schema-Name gültig ist
			if ((schemaname == null) || schemaname.isBlank())
				throw new ApiOperationException(Status.BAD_REQUEST, "Der Schema-Name darf nicht null oder leer sein.");

			// Prüfe, ob das Schema in der Konfiguration vorhanden ist oder nicht
			final SVWSKonfiguration config = SVWSKonfiguration.get();
			final String schemanameConfig = config.getSchemanameCaseConfig(schemaname);
			if ((schemanameConfig == null) || (!config.setDefaultschema(schemanameConfig)))
				throw new ApiOperationException(Status.NOT_FOUND,
						"Das Schema mit dem Namen %s konnte in der Konfiguration nicht gefunden werden.".formatted(schemaname));
			SVWSKonfiguration.write();
			return Response.status(Status.NO_CONTENT).build();
		},
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für das Zurücksetzen des Default-Schemas in der SVWS-Konfiguration.
	 * Der angemeldete Datenbankbenutzer muss dafür priviligierte Rechte für die Bearbeitung
	 * der SVWS-Konfiguration haben.
	 *
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return Rückmeldung, ob die Operation erfolgreich war
	 */
	@POST
	@Path("/config/default/schema/")
	@Operation(summary = "Setzt Default-Schema zurück.",
			description = "Setzt Default-Schema zurück, sofern der angemeldete Benutzer die benötigten Rechte besitzt.")
	@ApiResponse(responseCode = "204", description = "Das Default-Schema wurde zuürckgesetzt.")
	@ApiResponse(responseCode = "403", description = "Das Default-Schema darf nicht zurückgesetzt werden.")
	@ApiResponse(responseCode = "404", description = "Die Konfiguration wurde nicht gefunden.")
	public Response unsetDefaultSchemaInConfig(@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithoutTransaction(conn -> {
			// Prüfe, ob der Datenbank-Benutzer priviligiert ist
			if (!conn.isPrivilegedDatabaseUser())
				throw new ApiOperationException(Status.FORBIDDEN);

			// Prüfe, ob das Schema in der Konfiguration vorhanden ist oder nicht
			final SVWSKonfiguration config = SVWSKonfiguration.get();
			if (!config.setDefaultschema(null))
				throw new ApiOperationException(Status.NOT_FOUND, "Die Konfiguration wurde nicht gefunden.");
			SVWSKonfiguration.write();
			return Response.status(Status.NO_CONTENT).build();
		},
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen eines Schemas aus der SVWS-Konfiguration. Der angemeldete Datenbankbenutzer
	 * muss dafür priviligierte Rechte für die Bearbeitung der SVWS-Konfiguration haben.
	 *
	 * @param schemaname    der Name des Schemas, das aus der Konfiguration entfernt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return Rückmeldung, ob die Operation erfolgreich war
	 */
	@POST
	@Path("/config/schema/{schema}/remove")
	@Operation(summary = "Entfernt das bestehende Schema mit dem angegebenen Namen aus der SVWS-Konfiguration.",
			description = "Entfernt das bestehende Schema mit dem angegebenen Namen aus der SVWS-Konfiguration, falls es existiert und der angemeldete Benutzer"
					+ " die benötigten Rechte besitzt.")
	@ApiResponse(responseCode = "204", description = "Das Schema wurde erfolgreich aus der Konfiguration entfernt.")
	@ApiResponse(responseCode = "400", description = "Der Schema-Name darf nicht null oder leer sein.")
	@ApiResponse(responseCode = "403", description = "Das Schema darf nicht gelöscht werden.")
	@ApiResponse(responseCode = "404", description = "Das angegebene Schema wurde nicht gefunden.")
	public Response removeSchemaFromConfig(@PathParam("schema") final String schemaname, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithoutTransaction(conn -> {
			// Prüfe, ob der Datenbank-Benutzer priviligiert ist
			if (!conn.isPrivilegedDatabaseUser())
				throw new ApiOperationException(Status.FORBIDDEN);

			// Prüfe, ob der Schema-Name gültig ist
			if ((schemaname == null) || schemaname.isBlank())
				throw new ApiOperationException(Status.BAD_REQUEST, "Der Schema-Name darf nicht null oder leer sein.");

			// Prüfe, ob das Schema in der Konfiguration vorhanden ist oder nicht
			final SVWSKonfiguration config = SVWSKonfiguration.get();
			final String schemanameConfig = config.getSchemanameCaseConfig(schemaname);
			if (schemanameConfig == null)
				throw new ApiOperationException(Status.NOT_FOUND,
						"Das Schema mit dem Namen %s konnte in der Konfiguration nicht gefunden werden.".formatted(schemaname));
			config.removeSchema(schemanameConfig);
			SVWSKonfiguration.write();
			return Response.status(Status.NO_CONTENT).build();
		},
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Importiert den Private-Key und das Zertifikat im Base 64-Formate für die TLS-Konfiguration
	 * des SVWS-Server.
	 *
	 * @param alias       der Keystore-Alias unter welcher der Schlüssel und das Zertifikat abgelegt werden
	 * @param multipart   der private Schlüssel und das vom privaten Schlüssel erstellte Zertifikat
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return die Rückmeldung, ob die Operation erfolgreich war
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/config/privatekey_cert_base64/{alias}")
	@Operation(summary = "Importiert den Private-Key und das Zertifikat im Base 64-Formate für die TLS-Konfiguration.",
			description = "Importiert den Private-Key und das Zertifikat im Base 64-Formate für die TLS-Konfiguration.")
	@ApiResponse(responseCode = "204", description = "Der Import des privaten Schlüssels und des Zertifikats war erfolgreich.")
	@ApiResponse(responseCode = "400", description = "Es ist ein Fehler beim Import aufgetreten.")
	@ApiResponse(responseCode = "403", description = "Der Benutzer hat keine Berechtigung, um die TLS-Zertifikatsinformationen zu setzen.")
	public Response setConfigPrivateKeyCertificateBase64(@PathParam("alias") final String alias, @RequestBody(description = "Die LuPO-Datei", required = true,
			content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA)) @MultipartForm final PrivateKeyCertificateMultipartBody multipart,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithoutTransaction(conn -> {
			SVWSKonfiguration.setPrivateKeyCertificateBase64(alias, multipart.key, multipart.certificate);
			return Response.status(Status.NO_CONTENT).build();
		}, request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
	}

}
