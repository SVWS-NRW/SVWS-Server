package de.svws_nrw.api.debug;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die Debug-API.
 * Die Debug-API stellt die einzelnen HTML-, CSS- und JS-Dateien für die Swagger-UI zur
 * Verfügung. Diese werden automatisiert so angepasst, dass doe OpenAPI-Spezifikation genutzt
 * wird, die von diesem SVWS-Server generiert wurde.
 */
@Path("")
@Tag(name = "Debug OpenAPI")
public class APIDebug {

	/** Der Pfad der Swagger-UI-Ressourcen in dem Swagger-UI-Jar */
	private static final String pathSwaggerUIDist = "META-INF/resources/webjars/swagger-ui-dist";

	/** Die Version der Swagger-UI. Diese wird hier benötigt, da die Ressourcen in einem entsprechenden Unterverzeichnis liegen. */
	private static final String versionSwaggerUIDist = "4.15.5";
	// TODO determine Swagger UI Dist Version automatically...


	/**
	 * Diese Methode dient als Hilfsmethode, um auf die einzelnen Ressourcen der Swagger-UI zuzugreifen. Die
	 * index.html-Datei wird dabei angepasst, damit die OpenAPI-Datei des SVWS-Servers verwendet wird.
	 *
	 * @param filename   der Dateiname, der angefragt wird.
	 * @param request    der HTTP-Request zur automatischen Bestimmung der URL des SVWS-Servers
	 * @param isYAML     gibt an, ob auf die YAML- oder die JSON-Variante der OpenAPI-Schnittstellenbeschreibung zugegriffen wird
	 *
	 * @return die HTTP-Response für die Ressource
	 */
	private static Response getResource(final String filename, final HttpServletRequest request, final boolean isYAML) {
		String data = null;
    	try {
    		final String resourceName = pathSwaggerUIDist + "/" + versionSwaggerUIDist + "/" + filename;
    		try (InputStream in = ClassLoader.getSystemResourceAsStream(resourceName)) {
	    		data = IOUtils.toString(in, StandardCharsets.UTF_8);
	    		if ("swagger-initializer.js".equalsIgnoreCase(filename)) {
	    			final String openapi_file = isYAML ? "openapi.yaml" : "openapi.json";
	    			final String openapi_url = StringUtils.removeEnd(request.getRequestURL().toString(), request.getRequestURI()) + "/" + openapi_file;
	    			data = data.replace("https://petstore.swagger.io/v2/swagger.json", openapi_url);
	    		}
    		}
    	} catch (NullPointerException | IOException e) {
    		e.printStackTrace();
    	}
    	if (data == null)
    		return Response.status(Status.NOT_FOUND).build();
        return Response.ok(data).build();
	}


	/**
	 * Führt ein Redirect auf die "/debug/index.html" durch, falls auf "/debug" zugegriffen wird und
	 * auf "/debug/yaml/index.html", falls auf "debug/yaml" zugegriffen wird.
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
		if ("/yaml".equals(yaml))
			return Response.temporaryRedirect(UriBuilder.fromPath("/debug/yaml/index.html").build()).build();
		return Response.temporaryRedirect(UriBuilder.fromPath("/debug/index.html").build()).build();
    }


	/**
	 * Diese Methode gibt die für den SVWS-Server angepasste "index.html"-Datei der Swagger-UI zurück.
	 *
	 * @param yaml      ist auf "/yaml" gesetzt, wenn auf die Debug-API mithilfe der yaml-OpenAPI-Datei zugegriffen wird.
	 * @param request   der HTTP-Request
	 *
	 * @return die HTTP-Response
	 */
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/debug{yaml : (/yaml)?}/index.html")
    public Response debugFileIndexHTML(@PathParam("yaml") final String yaml, @Context final HttpServletRequest request) {
        return getResource("index.html", request, "/yaml".equals(yaml));
    }


	/**
	 * Diese Methode gibt die "swagger-initializer.js"-Datei der Swagger-UI zurück.
	 *
	 * @param yaml      ist auf "/yaml" gesetzt, wenn auf die Debug-API mithilfe der yaml-OpenAPI-Datei zugegriffen wird.
	 * @param request   der HTTP-Request
	 *
	 * @return die HTTP-Response
	 */
    @GET
    @Produces("text/javascript")
    @Path("/debug{yaml : (/yaml)?}/swagger-initializer.js")
    public Response debugFileSwaggerInitializerJS(@PathParam("yaml") final String yaml, @Context final HttpServletRequest request) {
        return getResource("swagger-initializer.js", request, "/yaml".equals(yaml));
    }


	/**
	 * Diese Methode gibt die "swagger-ui.js"-Datei der Swagger-UI zurück.
	 *
	 * @param yaml      ist auf "/yaml" gesetzt, wenn auf die Debug-API mithilfe der yaml-OpenAPI-Datei zugegriffen wird.
	 *
	 * @return die HTTP-Response
	 */
    @GET
    @Produces("text/javascript")
    @Path("/debug{yaml : (/yaml)?}/swagger-ui.js")
    public Response debugFileSwaggerUIJS(@PathParam("yaml") final String yaml) {
        return getResource("swagger-ui.js", null, "/yaml".equals(yaml));
    }


	/**
	 * Diese Methode gibt die "swagger-ui-bundle.js"-Datei der Swagger-UI zurück.
	 *
	 * @param yaml      ist auf "/yaml" gesetzt, wenn auf die Debug-API mithilfe der yaml-OpenAPI-Datei zugegriffen wird.
	 *
	 * @return die HTTP-Response
	 */
    @GET
    @Produces("text/javascript")
    @Path("/debug{yaml : (/yaml)?}/swagger-ui-bundle.js")
    public Response debugFileSwaggerUIBundleJS(@PathParam("yaml") final String yaml) {
        return getResource("swagger-ui-bundle.js", null, "/yaml".equals(yaml));
    }


	/**
	 * Diese Methode gibt die "swagger-ui-standalone-preset.js"-Datei der Swagger-UI zurück.
	 *
	 * @param yaml      ist auf "/yaml" gesetzt, wenn auf die Debug-API mithilfe der yaml-OpenAPI-Datei zugegriffen wird.
	 *
	 * @return die HTTP-Response
	 */
    @GET
    @Produces("text/javascript")
    @Path("/debug{yaml : (/yaml)?}/swagger-ui-standalone-preset.js")
    public Response debugFileSwaggerUIStandalonePresetJS(@PathParam("yaml") final String yaml) {
        return getResource("swagger-ui-standalone-preset.js", null, "/yaml".equals(yaml));
    }


	/**
	 * Diese Methode gibt die "index.css"-Datei der Swagger-UI zurück.
	 *
	 * @param yaml      ist auf "/yaml" gesetzt, wenn auf die Debug-API mithilfe der yaml-OpenAPI-Datei zugegriffen wird.
	 *
	 * @return die HTTP-Response
	 */
    @GET
    @Produces("text/css")
    @Path("/debug{yaml : (/yaml)?}/index.css")
    public Response debugFileIndexCSS(@PathParam("yaml") final String yaml) {
        return getResource("index.css", null, "/yaml".equals(yaml));
    }


	/**
	 * Diese Methode gibt die "swagger-ui.css"-Datei der Swagger-UI zurück.
	 *
	 * @param yaml      ist auf "/yaml" gesetzt, wenn auf die Debug-API mithilfe der yaml-OpenAPI-Datei zugegriffen wird.
	 *
	 * @return die HTTP-Response
	 */
    @GET
    @Produces("text/css")
    @Path("/debug{yaml : (/yaml)?}/swagger-ui.css")
    public Response debugFileSwaggerUICSS(@PathParam("yaml") final String yaml) {
        return getResource("swagger-ui.css", null, "/yaml".equals(yaml));
    }


	/**
	 * Diese Methode gibt die "swagger-ui.js.map"-Datei der Swagger-UI zurück.
	 *
	 * @param yaml      ist auf "/yaml" gesetzt, wenn auf die Debug-API mithilfe der yaml-OpenAPI-Datei zugegriffen wird.
	 *
	 * @return die HTTP-Response
	 */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/debug{yaml : (/yaml)?}/swagger-ui.js.map")
    public Response debugFileSwaggerUIJSMap(@PathParam("yaml") final String yaml) {
        return getResource("swagger-ui.js.map", null, "/yaml".equals(yaml));
    }


	/**
	 * Diese Methode gibt die "swagger-ui-bundle.js.map"-Datei der Swagger-UI zurück.
	 *
	 * @param yaml      ist auf "/yaml" gesetzt, wenn auf die Debug-API mithilfe der yaml-OpenAPI-Datei zugegriffen wird.
	 *
	 * @return die HTTP-Response
	 */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/debug{yaml : (/yaml)?}/swagger-ui-bundle.js.map")
    public Response debugFileSwaggerUIBundleJSMap(@PathParam("yaml") final String yaml) {
        return getResource("swagger-ui-bundle.js.map", null, "/yaml".equals(yaml));
    }


	/**
	 * Diese Methode gibt die "swagger-ui-standalone-preset.js.map"-Datei der Swagger-UI zurück.
	 *
	 * @param yaml      ist auf "/yaml" gesetzt, wenn auf die Debug-API mithilfe der yaml-OpenAPI-Datei zugegriffen wird.
	 *
	 * @return die HTTP-Response
	 */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/debug{yaml : (/yaml)?}/swagger-ui-standalone-preset.js.map")
    public Response debugFileSwaggerUIStandalonePresetJSMap(@PathParam("yaml") final String yaml) {
        return getResource("swagger-ui-standalone-preset.js.map", null, "/yaml".equals(yaml));
    }


	/**
	 * Diese Methode gibt die "swagger-ui.css.map"-Datei der Swagger-UI zurück.
	 *
	 * @param yaml      ist auf "/yaml" gesetzt, wenn auf die Debug-API mithilfe der yaml-OpenAPI-Datei zugegriffen wird.
	 *
	 * @return die HTTP-Response
	 */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/debug{yaml : (/yaml)?}/swagger-ui.css.map")
    public Response debugFileSwaggerUICSSMap(@PathParam("yaml") final String yaml) {
        return getResource("swagger-ui.css.map", null, "/yaml".equals(yaml));
    }


	/**
	 * Diese Methode gibt die "favicon-16x16.png"-Datei der Swagger-UI zurück.
	 *
	 * @param yaml      ist auf "/yaml" gesetzt, wenn auf die Debug-API mithilfe der yaml-OpenAPI-Datei zugegriffen wird.
	 *
	 * @return die HTTP-Response
	 */
    @GET
    @Produces("image/png")
    @Path("/debug{yaml : (/yaml)?}/favicon-16x16.png")
    public Response debugFileFavicon16x16PNG(@PathParam("yaml") final String yaml) {
        return getResource("favicon-16x16.png", null, "/yaml".equals(yaml));
    }


	/**
	 * Diese Methode gibt die "favicon-32x32.png"-Datei der Swagger-UI zurück.
	 *
	 * @param yaml      ist auf "/yaml" gesetzt, wenn auf die Debug-API mithilfe der yaml-OpenAPI-Datei zugegriffen wird.
	 *
	 * @return die HTTP-Response
	 */
    @GET
    @Produces("image/png")
    @Path("/debug{yaml : (/yaml)?}/favicon-32x32.png")
    public Response debugFileFavicon32x32PNG(@PathParam("yaml") final String yaml) {
        return getResource("favicon-32x32.png", null, "/yaml".equals(yaml));
    }

}
