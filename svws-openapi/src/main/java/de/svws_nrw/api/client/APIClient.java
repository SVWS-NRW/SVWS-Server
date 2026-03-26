package de.svws_nrw.api.client;

import java.nio.charset.StandardCharsets;

import org.jboss.resteasy.annotations.GZIP;

import de.svws_nrw.api.common.ResourceCoreTypeJson;
import de.svws_nrw.api.common.ResourceFile;
import de.svws_nrw.api.common.ResourceFileManager;
import de.svws_nrw.db.utils.ApiOperationException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Die Klasse spezifiziert die Schnittstelle für den Zugriff auf den SVWS-Client.
 */
@Path("")
@Tag(name = "SVWSClient")
public class APIClient {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIClient() {
		// leer
	}

	/**
	 * Greift auf die einzelne Dateien aus dem Resource-Verzeichnis des SVWS-Client zurück. Diese
	 * Resourcen wurden beim Start des SVWS-Server gecacht und stehen über die Klasse
	 * {@link ResourceFile} zur Verfügung. Die Datei wird dabei mit GZIP komprimiert.
	 *
	 * @param filename   der Name der zurückzugebenden Datei
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	private static Response getFile(final String filename, final HttpServletRequest request) {
		try {
			final byte[] data = ResourceFileManager.client().getData(filename);
			if ((data == null) || (data.length == 0)) {
				throw new ApiOperationException(Status.NOT_FOUND);
			}
			return Response.ok(data).build();
		} catch (final ApiOperationException e) {
			return e.getResponse();
		}
	}

	/**
	 * Gibt die "index.html"-Datei für das angegebene Schema zurück.
	 *
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces(MediaType.TEXT_HTML)
	@Path("/")
	public Response getClientRoot(@Context final HttpServletRequest request) {
		return getFile("index.html", request);
	}


	/**
	 * Gibt eine html-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name      der Name der Datei ohne ".html"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces(MediaType.TEXT_HTML)
	@Path("/{name}.html")
	public Response getClientHTML(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile(name + ".html", request);
	}


	/**
	 * Gibt eine js-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name      der Name der Datei ohne ".js"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces("text/javascript")
	@Path("/{name}.js")
	public Response getClientfileJS(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile(name + ".js", request);
	}


	/**
	 * Gibt eine js-Datei aus dem Ordner js zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name      der Name der Datei ohne ".js"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces("text/javascript")
	@Path("/js/{name}.js")
	public Response getClientFileJSSubdir(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile("js/" + name + ".js", request);
	}


	/**
	 * Gibt eine js-Datei aus dem Ordner assets zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name      der Name der Datei ohne ".js"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces("text/javascript")
	@Path("/assets/{name}.js")
	public Response getClientFileAssetsSubdir(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile("assets/" + name + ".js", request);
	}


	/**
	 * Gibt eine js.map-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name      der Name der Datei ohne ".js.map"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/{name}.js.map")
	public Response getClientFileJSMAP(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile(name + ".js.map", request);
	}


	/**
	 * Gibt eine js.map-Datei aus dem Ordner js zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name      der Name der Datei ohne ".js.map"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/js/{name}.js.map")
	public Response getClientFileJSMAPSubdir(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile("js/" + name + ".js.map", request);
	}


	/**
	 * Gibt eine js.map-Datei aus dem Ordner assets zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name      der Name der Datei ohne ".js.map"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/assets/{name}.js.map")
	public Response getClientFileAssetJSMAPSubdir(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile("assets/" + name + ".js.map", request);
	}


	/**
	 * Gibt eine css-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name      der Name der Datei ohne ".css"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces("text/css")
	@Path("/{name}.css")
	public Response getClientFileCSS(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile(name + ".css", request);
	}


	/**
	 * Gibt eine css-Datei aus dem Ordner css zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".css"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces("text/css")
	@Path("/css/{name}.css")
	public Response getClientFileCSSSubdir(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile("css/" + name + ".css", request);
	}


	/**
	 * Gibt eine css-Datei aus dem Ordner assets zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".css"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces("text/css")
	@Path("/assets/{name}.css")
	public Response getClientFileCSSAssetsSubdir(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile("assets/" + name + ".css", request);
	}


	/**
	 * Gibt eine css.map-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".css.map"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/{name}.css.map")
	public Response getClientFileCSSMAP(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile(name + ".css.map", request);
	}


	/**
	 * Gibt eine css.map-Datei aus dem Ordner css zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".css.map"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/css/{name}.css.map")
	public Response getClientFileCSSMAPSubdir(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile("css/" + name + ".css.map", request);
	}


	/**
	 * Gibt eine css.map-Datei aus dem Ordner assets zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".css.map"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/assets/{name}.css.map")
	public Response getClientFileAssetsCSSMAPSubdir(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile("assets/" + name + ".css.map", request);
	}


	/**
	 * Gibt eine css-Datei aus dem Ordner fonts zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".css"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces("text/css")
	@Path("/fonts/{name}.css")
	public Response getClientFileFontsCSS(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile("fonts/" + name + ".css", request);
	}


	/**
	 * Gibt eine woff2-Datei aus dem Ordner fonts zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".woff2"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces("font/woff2")
	@Path("/fonts/{name}.woff2")
	public Response getClientFileFontsWoff2(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile("fonts/" + name + ".woff2", request);
	}


	/**
	 * Gibt eine ico-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".ico"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces("image/x-icon")
	@Path("/{name}.ico")
	public Response getClientFileICO(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile(name + ".ico", request);
	}


	/**
	 * Gibt eine ico-Datei zurück, welche im Ordner assets in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".ico"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces("image/x-icon")
	@Path("/assets/{name}.ico")
	public Response getClientFileAssetsICO(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile("assets/" + name + ".ico", request);
	}


	/**
	 * Gibt eine png-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".png"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces("image/png")
	@Path("/{name}.png")
	public Response getClientFilePNG(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile(name + ".png", request);
	}


	/**
	 * Gibt eine png-Datei aus dem Ordner "/img/icons" zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".png"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces("image/png")
	@Path("/img/icons/{name}.png")
	public Response getClientFileImgIconsPNG(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile("img/icons/" + name + ".png", request);
	}


	/**
	 * Gibt eine png-Datei aus dem Ordner "/assets" zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".png"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces("image/png")
	@Path("/assets/{name}.png")
	public Response getClientFileAssetsPNG(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile("assets/" + name + ".png", request);
	}


	/**
	 * Gibt eine jpg-Datei aus dem Ordner "/assets" zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".jpg"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces("image/jpeg")
	@Path("/assets/{name}.jpg")
	public Response getClientFileAssetsJPG(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile("assets/" + name + ".jpg", request);
	}


	/**
	 * Gibt eine svg-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".svg"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces("image/svg+xml")
	@Path("/{name}.svg")
	public Response getClientFileSVG(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile(name + ".svg", request);
	}


	/**
	 * Gibt eine svg-Datei aus dem Ordner "/assets" zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".svg"
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@GZIP
	@Produces("image/svg+xml")
	@Path("/assets/{name}.svg")
	public Response getClientFileAssetsSVG(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		return getFile("assets/" + name + ".svg", request);
	}


	/**
	 * Gib eine JSON-Datei für die Core-Type-Daten zurück.
	 *
	 * @param name  der name des Core-Types
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit dem JSON-Katalog des Core-Types
	 */
	@GET
	@GZIP
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/types/{name}.json")
	public Response getJSONKatalog(@PathParam("name") final String name, @Context final HttpServletRequest request) {
		try {
			final String json = ResourceCoreTypeJson.get(name);
			final byte[] data = json.getBytes(StandardCharsets.UTF_8);
			return Response.ok(data).build();
		} catch (final ApiOperationException e) {
			return e.getResponse();
		}
	}

}
