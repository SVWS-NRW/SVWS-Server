package de.svws_nrw.api.debug;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.api.common.SwaggerUIResourceUtils;
import de.svws_nrw.config.SVWSKonfiguration;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die Debug-API.
 * Die Debug-API stellt die einzelnen HTML-, CSS- und JS-Dateien für die Swagger-UI zur
 * Verfügung. Diese werden automatisiert so angepasst, dass die OpenAPI-Spezifikation genutzt
 * wird, die von diesem SVWS-Server generiert wurde.
 */
@Path("")
@Tag(name = "Debug OpenAPI")
public class APIDebug {

	private static final String SWAGGER_UI_DIST_RESOURCE_PATH = SwaggerUIResourceUtils.getSwaggerUIDistResourcePath();

	private static final String PATH_TO_OPENAPI_FILE = "/openapi/";

	private static final String DEFAULT_API_DEFINITION = "server";

	/** Die Liste der zulässigen Dateien */
	private static final Map<String, String> mapMediaType = Map.ofEntries(
			Map.entry("index.html", MediaType.TEXT_HTML),
			Map.entry("swagger-initializer.js", "text/javascript"),
			Map.entry("swagger-ui.js", "text/javascript"),
			Map.entry("swagger-ui-bundle.js", "text/javascript"),
			Map.entry("swagger-ui-standalone-preset.js", "text/javascript"),
			Map.entry("index.css", "text/css"),
			Map.entry("swagger-ui.css", "text/css"),
			Map.entry("swagger-ui.js.map", MediaType.TEXT_PLAIN),
			Map.entry("swagger-ui-bundle.js.map", MediaType.TEXT_PLAIN),
			Map.entry("swagger-ui-standalone-preset.js.map", MediaType.TEXT_PLAIN),
			Map.entry("swagger-ui.css.map", MediaType.TEXT_PLAIN),
			Map.entry("favicon-16x16.png", "image/png"),
			Map.entry("favicon-32x32.png", "image/png"));

	/** Die Lister der verfügbaren APIs */
	private static final Map<String, Boolean> mapApiIsPrivileged = Map.ofEntries(
			Map.entry("server", false),
			Map.entry("external", false),
			Map.entry("privileged", true));

	/**
	 * Standardkonstruktor.
	 */
	public APIDebug() {
		// Prüfen, ob eine verfügbare SwaggerUI Dist Version gefunden wurde, ansonsten wäre der Path null
		Objects.requireNonNull(SWAGGER_UI_DIST_RESOURCE_PATH);
	}


	/**
	 * Diese Methode dient als Hilfsmethode, um auf die einzelnen Ressourcen der Swagger-UI zuzugreifen. Die
	 * index.html-Datei wird dabei angepasst, damit die OpenAPI-Datei des SVWS-Servers verwendet wird.
	 *
	 * @param filename    der Dateiname, der angefragt wird.
	 * @param request     der HTTP-Request zur automatischen Bestimmung der URL des SVWS-Servers
	 * @param isYAML      gibt an, ob auf die YAML- oder die JSON-Variante der OpenAPI-Schnittstellenbeschreibung zugegriffen wird
	 * @param api         gibt die API, di ausgewählt wurde
	 *
	 * @return die HTTP-Response für die Ressource
	 */
	private static Response getResource(final String filename, final HttpServletRequest request, final boolean isYAML, final String api) {
		// Prüfe, ob der Dateiname und die Api gültig sind.
		final String mediaType = mapMediaType.get(filename);
		if ((mediaType == null) || (!mapApiIsPrivileged.containsKey(api)))
			return Response.status(Status.NOT_FOUND).build();
		// Prüfe, ob für die API ein privilegierter Zugriff benötigt wird und ob dieser so zulässig ist.
		final SVWSKonfiguration config = SVWSKonfiguration.get();
		final boolean apiIsPrivileged = mapApiIsPrivileged.get(api);
		if (config.isDBRootAccessDisabled() && apiIsPrivileged)
			return Response.status(Status.FORBIDDEN).entity("Der Zugriff auf die API für privilegierte Anfragen wurde beim Server gesperrt.")
					.type(MediaType.TEXT_PLAIN).build();
		if (config.hatPortHTTPPrivilegedAccess()) {
			final boolean portIsPrivileged = (request.getServerPort() == config.getPortHTTPPrivilegedAccess());
			if (apiIsPrivileged && (!portIsPrivileged))
				return Response.status(Status.FORBIDDEN).entity("Der Zugriff auf die API für privilegierte Anfragen wurde beim Server gesperrt.")
						.type(MediaType.TEXT_PLAIN).build();
			if (!apiIsPrivileged && (portIsPrivileged)) { // Redirect
				final URI uri = UriBuilder.fromPath(request.getServletPath() + request.getPathInfo())
						.scheme(request.getScheme())
						.host(request.getServerName())
						.port(config.isTLSDisabled() ? config.getPortHTTP() : config.getPortHTTPS())
						.build();
				return Response.temporaryRedirect(uri).build();
			}
		}

		// Lese die zugehörige Datei aus der Swagger-UI ein
		String data = null;
		try {
			final String resourceName = SWAGGER_UI_DIST_RESOURCE_PATH + "/" + filename;
			try (InputStream in = ClassLoader.getSystemResourceAsStream(resourceName)) {
				data = IOUtils.toString(in, StandardCharsets.UTF_8);
				if ("swagger-initializer.js".equalsIgnoreCase(filename)) {
					final List<String> openApiPaths = mapApiIsPrivileged.keySet().stream().map(apiName -> {
						final String openApiFile = apiName + (isYAML ? ".yaml" : ".json");
						return Strings.CS.removeEnd(request.getRequestURL().toString(), request.getRequestURI()) + PATH_TO_OPENAPI_FILE + openApiFile;
					}).toList();

					data = SwaggerUIResourceUtils.customizeSwaggerInitializerJs(data, openApiPaths);
				}
			}
		} catch (NullPointerException | IOException e) {
			e.printStackTrace();
		}

		if (data == null)
			return Response.status(Status.NOT_FOUND).build();
		return Response.ok(data).type(mediaType).build();
	}


	/**
	 * Führt ein Redirect auf die "/debug/server/index.html" durch, falls auf "/debug" zugegriffen wird und
	 * auf "/debug/server/yaml/index.html", falls auf "debug/yaml" zugegriffen wird.
	 *
	 * @param yaml       ist auf "/yaml" gesetzt, wenn auf die Debug-API mithilfe der yaml-OpenAPI-Datei zugegriffen wird.
	 * @param request    der HTTP-Request
	 *
	 * @return die HTTP-Response für den Redirect
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/debug{yaml : (/yaml)?}")
	public Response debugRootWrong(@PathParam("yaml") final String yaml, @Context final HttpServletRequest request) {
		return redirectToDefault(yaml);
	}


	/**
	 * Diese Methode gibt die für den SVWS-Server angepasste Datei der Swagger-UI zurück.
	 *
	 * @param yaml           ist auf "/yaml" gesetzt, wenn auf die Debug-API mithilfe der yaml-OpenAPI-Datei zugegriffen wird.
	 * @param apiDefinition  die ApiDefinition, auf welche zugegriffen werden soll
	 * @param filename       der Dateiname
	 * @param request        der HTTP-Request
	 *
	 * @return die HTTP-Response
	 */
	@GET
	@Produces({ MediaType.TEXT_HTML, "text/javascript", "text/css", MediaType.TEXT_PLAIN })
	@Path("/debug{yaml : (/yaml)?}/{filename}")
	public Response debugFile(@PathParam("yaml") final String yaml, @QueryParam("urls.primaryName") final String apiDefinition,
			@PathParam("filename") final String filename, @Context final HttpServletRequest request) {
		if ("index.html".equals(filename) && StringUtils.isBlank(apiDefinition))
			return redirectToDefault(yaml);
		return getResource(filename, request, "/yaml".equals(yaml), StringUtils.isBlank(apiDefinition) ? DEFAULT_API_DEFINITION : apiDefinition);
	}

	/**
	 * Die Methode macht einen Redirect auf die standardmäßige OpenAPI-Definition.
	 *
	 * @param yaml ist auf "/yaml" gesetzt, wenn auf die Debug-API mithilfe der yaml-OpenAPI-Datei zugegriffen wird.
	 *
	 * @return Redirect HTTP-Response
	 */
	private Response redirectToDefault(final String yaml) {
		if ("/yaml".equals(yaml))
			return Response.temporaryRedirect(UriBuilder.fromPath("/debug/yaml/index.html").queryParam("urls.primaryName", DEFAULT_API_DEFINITION).build()).build();
		return Response.temporaryRedirect(UriBuilder.fromPath("/debug/index.html").queryParam("urls.primaryName", DEFAULT_API_DEFINITION).build()).build();
	}

}
