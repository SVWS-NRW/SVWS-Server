package de.svws_nrw.api.client;

import de.svws_nrw.api.common.ResourceFile;
import de.svws_nrw.api.common.ResourceFileManager;
import de.svws_nrw.db.utils.ApiOperationException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

/**
 * Die Klasse spezifiziert die Schnittstelle für den Zugriff auf SVWS-Apps.
 */
@Path("/app")
@Tag(name = "SVWSApps")
public class APIApps {

	private static final String APP_NAME_REGEX = "[a-zA-Z][a-zA-Z0-9]*";

	/** Ein regulärer Ausdruck für die Pfad-Annotationen bei den einzelnen Endpunkten. Der Dateiname ist dabei noch ohne Endung. */
	private static final String PATH_REGEX_BASE = "[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)*(/[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)*)*";

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIApps() {
		// leer
	}

	/**
	 * Greift auf die einzelne Dateien aus dem Resource-Verzeichnis der SVWS-Apps zurück.
	 * Diese Resourcen wurden beim Start des SVWS-Server gecacht und stehen über die Klasse
	 * {@link ResourceFile} zur Verfügung.
	 *
	 * @param app        der Name der SVWS-App
	 * @param filename   der Name der zurückzugebenden Datei
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	private static Response getFile(final String app, final String filename) {
		try {
			final byte[] data = ResourceFileManager.apps().getData(app + "/" + filename);
			if ((data == null) || (data.length == 0)) {
				throw new ApiOperationException(Status.NOT_FOUND);
			}
			return Response.ok(data).build();
		} catch (final ApiOperationException e) {
			return e.getResponse();
		}
	}


	/**
	 * Leitet, den Zugriff auf die Variante mit "/" um, damit der Zugriff auf untergeordnete Ressourcen noch korrekt
	 * funktioniert.
	 *
	 * @param app    der Name der App
	 *
	 * @return eine Redirect-Response zur URL mit abschließendem "/"
	 */
	@GET
	@Path("/{app: " + APP_NAME_REGEX + "}")
	public Response getAppRootRedirect(@PathParam("app") final String app) {
		return Response.temporaryRedirect(java.net.URI.create(app + "/")).build();
	}


	/**
	 * Gibt die "index.html"-Datei für das angegebene Schema zurück.
	 *
	 * @param app       der Name der App
	 * @param uriInfo   die Informationen zur URI
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/{app: " + APP_NAME_REGEX + "}/")
	public Response getAppRoot(@PathParam("app") final String app, @Context final UriInfo uriInfo) {
		// Leite um, falls kein / am Ende des Pfades vorhanden ist, damit Ressourcen mit relativen Pfaden vom Browser korrekt geladen werden
		if (!uriInfo.getRequestUri().getPath().endsWith("/")) {
			return Response.temporaryRedirect(uriInfo.getRequestUriBuilder().path("/").build()).build();
		}
		return getFile(app, "index.html");
	}


	/**
	 * Gibt eine html-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param app    der Name der App
	 * @param path   der Pfad und der Name der Datei
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/{app: " + APP_NAME_REGEX + "}/{path: " + PATH_REGEX_BASE  + "\\.html}")
	public Response getAppHTML(@PathParam("app") final String app, @PathParam("path") final String path) {
		return getFile(app, path);
	}


	/**
	 * Gibt eine js-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param app    der Name der App
	 * @param path   der Pfad und der Name der Datei
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("text/javascript")
	@Path("/{app: " + APP_NAME_REGEX + "}/{path: " + PATH_REGEX_BASE  + "\\.js}")
	public Response getAppfileJS(@PathParam("app") final String app, @PathParam("path") final String path) {
		return getFile(app, path);
	}


	/**
	 * Gibt eine js.map-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param app    der Name der App
	 * @param path   der Pfad und der Name der Datei
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/{app: " + APP_NAME_REGEX + "}/{path: " + PATH_REGEX_BASE  + "\\.js\\.map}")
	public Response getAppFileJSMAP(@PathParam("app") final String app, @PathParam("path") final String path) {
		return getFile(app, path);
	}


	/**
	 * Gibt eine css-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param app    der Name der App
	 * @param path   der Pfad und der Name der Datei
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("text/css")
	@Path("/{app: " + APP_NAME_REGEX + "}/{path: " + PATH_REGEX_BASE  + "\\.css}")
	public Response getAppFileCSS(@PathParam("app") final String app, @PathParam("path") final String path) {
		return getFile(app, path);
	}


	/**
	 * Gibt eine css.map-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param app    der Name der App
	 * @param path   der Pfad und der Name der Datei
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/{app: " + APP_NAME_REGEX + "}/{path: " + PATH_REGEX_BASE  + "\\.css\\.map}")
	public Response getAppFileCSSMAP(@PathParam("app") final String app, @PathParam("path") final String path) {
		return getFile(app, path);
	}


	/**
	 * Gibt eine woff2-Datei aus dem Ordner fonts zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param app    der Name der App
	 * @param path   der Pfad und der Name der Datei
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("font/woff2")
	@Path("/{app: " + APP_NAME_REGEX + "}/{path: " + PATH_REGEX_BASE  + "\\.woff2}")
	public Response getAppFileWoff2(@PathParam("app") final String app, @PathParam("path") final String path) {
		return getFile(app, path);
	}


	/**
	 * Gibt eine ico-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param app    der Name der App
	 * @param path   der Pfad und der Name der Datei
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("image/x-icon")
	@Path("/{app: " + APP_NAME_REGEX + "}/{path: " + PATH_REGEX_BASE  + "\\.ico}")
	public Response getAppFileICO(@PathParam("app") final String app, @PathParam("path") final String path) {
		return getFile(app, path);
	}


	/**
	 * Gibt eine png-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param app    der Name der App
	 * @param path   der Pfad und der Name der Datei
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("image/png")
	@Path("/{app: " + APP_NAME_REGEX + "}/{path: " + PATH_REGEX_BASE  + "\\.png}")
	public Response getAppFilePNG(@PathParam("app") final String app, @PathParam("path") final String path) {
		return getFile(app, path);
	}


	/**
	 * Gibt eine jpg-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param app    der Name der App
	 * @param path   der Pfad und der Name der Datei
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("image/jpeg")
	@Path("/{app: " + APP_NAME_REGEX + "}/{path: " + PATH_REGEX_BASE  + "\\.jpg}")
	public Response getAppFileJPG(@PathParam("app") final String app, @PathParam("path") final String path) {
		return getFile(app, path);
	}


	/**
	 * Gibt eine svg-Datei zurück, welche in den Ressourcen des SVWS-Clients vorhanden ist.
	 *
	 * @param app    der Name der App
	 * @param path   der Pfad und der Name der Datei
	 *
	 * @return die HTTP-Response mit der Datei oder {@link Status#NOT_FOUND}, falls die Datei
	 *         nicht gefunden wurde
	 */
	@GET
	@Produces("image/svg+xml")
	@Path("/{app: " + APP_NAME_REGEX + "}/{path: " + PATH_REGEX_BASE  + "\\.svg}")
	public Response getAppFileSVG(@PathParam("app") final String app, @PathParam("path") final String path) {
		return getFile(app, path);
	}

}
