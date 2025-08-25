package de.svws_nrw.api.server;

import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.List;

import de.svws_nrw.api.common.SVWSVersion;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.data.db.DBSchemaListeEintrag;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.utils.ApiOperationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import jakarta.ws.rs.core.Response.Status;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die Konfiguration
 * des SVWS-Servers.
 * Die Config-API stellt neben einer Schnittstelle zum Prüfen, ob ein SVWS-Server vorhanden
 * ist (alive), weitere Konfigurationsmöglichkteiten zur Verfügung.
 */
@Path("")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
@Tag(name = "Server")
public class APIConfig {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIConfig() {
		// leer
	}

	/**
	 * Diese Methode kann genutzt werden, um die Verfügbarkeit des SVWS-Server zu testen.
	 *
	 * @return die HTTP-Response
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/status/alive")
	@Operation(summary = "Eine Test-Methode zum Prüfen, ob der Server erreichbar ist.",
			description = "Eine Test-Methode zum Prüfen, ob der Server erreichbar ist.")
	@ApiResponse(responseCode = "200", description = "Der Server ist erreichbar!",
			content = @Content(mediaType = MediaType.TEXT_PLAIN, schema = @Schema(implementation = String.class)))
	public Response isAlive() {
		return Response.ok("SVWS-Server erreichbar").build();
	}


	/**
	 * Diese Methode kann genutzt werden, um die Verfügbarkeit des SVWS-Server über die Privileged-API zu testen.
	 *
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit true oder false
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/status/alive/privileged")
	@Operation(summary = "Eine Test-Methode zum Prüfen, ob die Privileged-API des Serves erreichbar ist oder nicht.",
			description = "Eine Test-Methode zum Prüfen, ob die Privileged-API des Serves erreichbar ist oder nicht.")
	@ApiResponse(responseCode = "200", description = "Der Server ist über die Privileged API erreichbar!",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Boolean.class)))
	public Response isAlivePrivileged(@Context final HttpServletRequest request) {
		final var config = SVWSKonfiguration.get();
		if (config.isDBRootAccessDisabled())
			return Response.ok(false).build();
		if ((config.hatPortHTTPPrivilegedAccess()) && (config.getPortHTTPPrivilegedAccess() != request.getLocalPort()))
			return Response.ok(false).build();
		return Response.ok(true).build();
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Version des SVWS-Servers.
	 *
	 * @return die Version des SVWS-Servers
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/status/version")
	@Operation(summary = "Gibt die Version des SVWS-Servers zurück.",
			description = "Gibt die Version des SVWS-Servers zurück.")
	@ApiResponse(responseCode = "200", description = "Die SVWS-Server-Version",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class)))
	public Response getServerVersion() {
		try {
			final String version = SVWSVersion.version();
			if (version == null)
				throw new ApiOperationException(Status.NOT_FOUND);
			return JSONMapper.fromString(version);
		} catch (final ApiOperationException e) {
			return e.getResponse();
		}
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Betriebsmodus des SVWS-Servers.
	 *
	 * @return der Betriebsmodus des SVWS-Servers
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/status/mode")
	@Operation(summary = "Gibt den Betriebsmodus (stable, alpha, beta oder dev) des SVWS-Servers zurück.",
			description = "Gibt den Betriebsmodus (stable, alpha, beta oder dev) des SVWS-Servers zurück.")
	@ApiResponse(responseCode = "200", description = "Der Betriebsmodus (stable, alpha, beta oder dev)",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class)))
	public Response getServerModus() {
		try {
			final ServerMode mode = SVWSKonfiguration.get().getServerMode();
			return JSONMapper.fromString(mode.text);
		} catch (final ApiOperationException e) {
			return e.getResponse();
		}
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der ersten kompatiblen Schild-Version
	 *
	 * @return die erste kompatible Schild-Version
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/status/schild/minversion")
	@Operation(summary = "Gibt die erste kompatible Schild-Version zurück.",
			description = "Gibt die erste kompatible Schild-Version zurück.")
	@ApiResponse(responseCode = "200", description = "Die erste kompatible Schild-Version",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class)))
	public Response getSchildMinVersion() {
		try {
			final String version = SVWSVersion.getSchildMinVersion();
			if (version == null)
				throw new ApiOperationException(Status.NOT_FOUND);
			return JSONMapper.fromString(version);
		} catch (final ApiOperationException e) {
			return e.getResponse();
		}
	}


	/**
	 * Diese Methode liefert den öffentlichen Schlüssel des Servers in Base64-Kodierung
	 *
	 * @return der öffentlichen Schlüssel des Servers in Base64-Kodierung
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/config/publickey_base64")
	@Operation(summary = "Gibt den öffentlichen Schlüssel des Server in Base64-Kodierung zurück.",
			description = "Gibt den öffentlichen Schlüssel des Server in Base64-Kodierung zurück.")
	@ApiResponse(responseCode = "200", description = "Der Base-64-kodierte, öffentliche Schlüssel des Servers",
			content = @Content(mediaType = MediaType.TEXT_PLAIN, schema = @Schema(implementation = String.class)))
	@ApiResponse(responseCode = "500", description = "Der öffentliche Schlüssel wurde nicht gefunden")
	public Response getConfigPublicKeyBase64() {
		try {
			return Response.ok(SVWSKonfiguration.getPublicKeyBase64()).build();
		} catch (final KeyStoreException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}


	/**
	 * Diese Methode liefert das Zertifikat des Servers in Base64-Kodierung
	 *
	 * @return das Zertifikat des Servers in Base64-Kodierung
	 */
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/config/certificate_base64")
	@Operation(summary = "Gibt das Zertifikat des Server in Base64-Kodierung zurück.",
			description = "Gibt das Zertifikat des Server in Base64-Kodierung zurück.")
	@ApiResponse(responseCode = "200", description = "Das Base-64-kodierte Zertifikat des Servers",
			content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM,
					schema = @Schema(type = "string", format = "binary", description = "Die Zertifkatsdatei")))
	@ApiResponse(responseCode = "500", description = "Das Zertifikat wurde nicht gefunden")
	public Response getConfigCertificateBase64() {
		try {
			return Response.ok(SVWSKonfiguration.getCertificateBase64()).header("Content-Disposition", "attachment; filename=\"SVWS.cer\"").build();
		} catch (final KeyStoreException e) {
			return new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e).getResponse();
		}
	}


	/**
	 * Diese Methode liefert das Zertifikat des Servers
	 *
	 * @return das Zertifikat des Servers
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/config/certificate")
	@Operation(summary = "Gibt das Zertifikat des Server zurück.",
			description = "Gibt das Zertifikat des Server zurück.")
	@ApiResponse(responseCode = "200", description = "Das Zertifikat des Servers",
			content = @Content(mediaType = MediaType.TEXT_PLAIN, schema = @Schema(implementation = String.class)))
	@ApiResponse(responseCode = "500", description = "Das Zertifikat wurde nicht gefunden")
	public Response getConfigCertificate() {
		try {
			final Certificate cert = SVWSKonfiguration.getCertificate();
			return Response.ok(cert.toString()).build();
		} catch (final KeyStoreException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}



	/**
	 * Diese Methode liefert das Zertifikat des Servers als .cer-Datei
	 *
	 * @return die HTTP-Response mit der Zertifikats-Datei
	 */
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/config/certificate_file")
	@Operation(summary = "Gibt die Zertifikatsdatei des Server zurück.",
			description = "Gibt die Zertifikatsdatei des Server zurück.")
	@ApiResponse(responseCode = "200", description = "Die Zertifikatsdatei des Servers",
			content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM,
					schema = @Schema(type = "string", format = "binary", description = "Die Zertifkatsdatei")))
	@ApiResponse(responseCode = "404", description = "Das Zertifikat wurde nicht gefunden")
	@ApiResponse(responseCode = "500", description = "Es konnte nicht auf das Zertifikat zugegriffen werden")
	public Response getConfigCertificateFile() {
		try {
			return Response.ok(SVWSKonfiguration.getCertificate().getEncoded()).header("Content-Disposition", "attachment; filename=\"SVWS.cer\"").build();
		} catch (final CertificateEncodingException e) {
			return new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e).getResponse();
		} catch (final KeyStoreException e) {
			return new ApiOperationException(Status.NOT_FOUND, e).getResponse();
		}
	}



	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste aller konfigurierten DB-Schemata.
	 *
	 * @return die Liste mit den einzelnen DB-Schemata
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/config/db/schemata")
	@Operation(summary = "Gibt eine sortierte Übersicht von allen konfigurierten DB-Schemata zurück.",
			description = "Gibt eine sortierte Übersicht von allen konfigurierten DB-Schemata zurück.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von DB-Schema-Listen-Einträgen",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = DBSchemaListeEintrag.class))))
	public List<DBSchemaListeEintrag> getConfigDBSchemata() {
		return SVWSKonfiguration.get().getSchemaList();
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Datenbank-Revision, welche
	 * der SVWS-Server unterstützt.
	 * Diese muss zwischenzeitlich nicht mit den Revisionen der einzelnen
	 * DB-Schemata übereinstimmen!
	 *
	 * @return die Datenbank-Revision des SVWS-Servers
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/status/db/revision")
	@Operation(summary = "Gibt Datenbank-Revision zurück, welche der SVWS-Server unterstützt.",
			description = "Gibt Datenbank-Revision zurück, welche der SVWS-Server unterstützt.")
	@ApiResponse(responseCode = "200", description = "Die Datenbank-Revision",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Long.class)))
	public Response getServerDBRevision() {
		final long rev = (SVWSKonfiguration.get().getServerMode() == ServerMode.DEV)
				? SchemaRevisionen.maxDeveloperRevision.revision
				: SchemaRevisionen.maxRevision.revision;
		return JSONMapper.fromLong(rev);
	}

}
