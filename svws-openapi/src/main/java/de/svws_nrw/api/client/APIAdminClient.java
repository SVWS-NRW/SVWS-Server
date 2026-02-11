package de.svws_nrw.api.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.api.common.ResourceFile;
import de.svws_nrw.api.common.ResourceFileManager;
import de.svws_nrw.db.utils.ApiOperationException;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Die Klasse spezifiziert die Schnittstelle für den Zugriff auf den SVWS-Admin-Client.
 */
@Path("/admin")
@Tag(name = "SVWSAdminClient")
public class APIAdminClient {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIAdminClient() {
		// leer
	}

	/**
	 * Greift auf die einzelne Dateien aus dem Resource-Verzeichnis des SVWS-Admin-Clients zurück.
	 * Diese Resourcen wurden beim Start des SVWS-Server gecacht und stehen über die Klasse
	 * {@link ResourceFile} zur Verfügung.
	 *
	 * @param filename   der Name der zurückzugebenden Datei
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	private static Response getFile(final String filename) {
		try {
			final byte[] data = ResourceFileManager.admin().getData(filename);
			if ((data == null) || (data.length == 0))
				throw new ApiOperationException(Status.NOT_FOUND);
			return Response.ok(data).build();
		} catch (final ApiOperationException e) {
			return e.getResponse();
		}
	}


	/**
	 * Gibt die "index.html"-Datei für das angegebene Schema zurück.
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/")
	public Response getClientRoot() {
		return getFile("index.html");
	}


	/**
	 * Gibt eine html-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".html"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/{name}.html")
	public Response getClientHTML(@PathParam("name") final String name) {
		return getFile(name + ".html");
	}


	/**
	 * Gibt eine js-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".js"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("text/javascript")
	@Path("/{name}.js")
	public Response getClientfileJS(@PathParam("name") final String name) {
		return getFile(name + ".js");
	}


	/**
	 * Gibt eine js-Datei aus dem Ordner js zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".js"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("text/javascript")
	@Path("/js/{name}.js")
	public Response getClientFileJSSubdir(@PathParam("name") final String name) {
		return getFile("js/" + name + ".js");
	}


	/**
	 * Gibt eine js-Datei aus dem Ordner assets zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".js"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("text/javascript")
	@Path("/assets/{name}.js")
	public Response getClientFileAssetsSubdir(@PathParam("name") final String name) {
		return getFile("assets/" + name + ".js");
	}


	/**
	 * Gibt eine js.map-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".js.map"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/{name}.js.map")
	public Response getClientFileJSMAP(@PathParam("name") final String name) {
		return getFile(name + ".js.map");
	}


	/**
	 * Gibt eine js.map-Datei aus dem Ordner js zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".js.map"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/js/{name}.js.map")
	public Response getClientFileJSMAPSubdir(@PathParam("name") final String name) {
		return getFile("js/" + name + ".js.map");
	}


	/**
	 * Gibt eine js.map-Datei aus dem Ordner assets zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".js.map"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/assets/{name}.js.map")
	public Response getClientFileAssetJSMAPSubdir(@PathParam("name") final String name) {
		return getFile("assets/" + name + ".js.map");
	}


	/**
	 * Gibt eine css-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".css"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("text/css")
	@Path("/{name}.css")
	public Response getClientFileCSS(@PathParam("name") final String name) {
		return getFile(name + ".css");
	}


	/**
	 * Gibt eine css-Datei aus dem Ordner css zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".css"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("text/css")
	@Path("/css/{name}.css")
	public Response getClientFileCSSSubdir(@PathParam("name") final String name) {
		return getFile("css/" + name + ".css");
	}


	/**
	 * Gibt eine css-Datei aus dem Ordner assets zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".css"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("text/css")
	@Path("/assets/{name}.css")
	public Response getClientFileCSSAssetsSubdir(@PathParam("name") final String name) {
		return getFile("assets/" + name + ".css");
	}


	/**
	 * Gibt eine css.map-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".css.map"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/{name}.css.map")
	public Response getClientFileCSSMAP(@PathParam("name") final String name) {
		return getFile(name + ".css.map");
	}


	/**
	 * Gibt eine css.map-Datei aus dem Ordner css zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".css.map"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/css/{name}.css.map")
	public Response getClientFileCSSMAPSubdir(@PathParam("name") final String name) {
		return getFile("css/" + name + ".css.map");
	}


	/**
	 * Gibt eine css.map-Datei aus dem Ordner assets zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".css.map"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/assets/{name}.css.map")
	public Response getClientFileAssetsCSSMAPSubdir(@PathParam("name") final String name) {
		return getFile("assets/" + name + ".css.map");
	}


	/**
	 * Gibt eine css-Datei aus dem Ordner fonts zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".css"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("text/css")
	@Path("/fonts/{name}.css")
	public Response getClientFileFontsCSS(@PathParam("name") final String name) {
		return getFile("fonts/" + name + ".css");
	}


	/**
	 * Gibt eine woff2-Datei aus dem Ordner fonts zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".woff2"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("font/woff2")
	@Path("/fonts/{name}.woff2")
	public Response getClientFileFontsWoff2(@PathParam("name") final String name) {
		return getFile("fonts/" + name + ".woff2");
	}


	/**
	 * Gibt eine ico-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".ico"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("image/x-icon")
	@Path("/{name}.ico")
	public Response getClientFileICO(@PathParam("name") final String name) {
		return getFile(name + ".ico");
	}


	/**
	 * Gibt eine ico-Datei zurück, welche im Ordner assets in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".ico"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("image/x-icon")
	@Path("/assets/{name}.ico")
	public Response getClientFileAssetsICO(@PathParam("name") final String name) {
		return getFile("assets/" + name + ".ico");
	}


	/**
	 * Gibt eine png-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".png"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("image/png")
	@Path("/{name}.png")
	public Response getClientFilePNG(@PathParam("name") final String name) {
		return getFile(name + ".png");
	}


	/**
	 * Gibt eine png-Datei aus dem Ordner "/img/icons" zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".png"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("image/png")
	@Path("/img/icons/{name}.png")
	public Response getClientFileImgIconsPNG(@PathParam("name") final String name) {
		return getFile("img/icons/" + name + ".png");
	}


	/**
	 * Gibt eine png-Datei aus dem Ordner "/assets" zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".png"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("image/png")
	@Path("/assets/{name}.png")
	public Response getClientFileAssetsPNG(@PathParam("name") final String name) {
		return getFile("assets/" + name + ".png");
	}


	/**
	 * Gibt eine avif-Datei aus dem Ordner "/assets" zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".avif"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("image/avif")
	@Path("/assets/{name}.avif")
	public Response getClientFileAssetsAVIF(@PathParam("name") final String name) {
		return getFile("assets/" + name + ".avif");
	}


	/**
	 * Gibt eine jpg-Datei aus dem Ordner "/assets" zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".jpg"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("image/jpeg")
	@Path("/assets/{name}.jpg")
	public Response getClientFileAssetsJPG(@PathParam("name") final String name) {
		return getFile("assets/" + name + ".jpg");
	}


	/**
	 * Gibt eine svg-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".svg"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("image/svg+xml")
	@Path("/{name}.svg")
	public Response getClientFileSVG(@PathParam("name") final String name) {
		return getFile(name + ".svg");
	}


	/**
	 * Gibt eine svg-Datei aus dem Ordner "/assets" zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param name   der Name der Datei ohne ".svg"
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("image/svg+xml")
	@Path("/assets/{name}.svg")
	public Response getClientFileAssetsSVG(@PathParam("name") final String name) {
		return getFile("assets/" + name + ".svg");
	}

}
