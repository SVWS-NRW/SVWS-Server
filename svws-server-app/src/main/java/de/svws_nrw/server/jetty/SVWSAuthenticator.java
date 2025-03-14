package de.svws_nrw.server.jetty;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.security.AuthenticationState;
import org.eclipse.jetty.security.Authenticator;
import org.eclipse.jetty.security.ServerAuthException;
import org.eclipse.jetty.security.UserIdentity;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.security.authentication.LoginAuthenticator;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;

import de.svws_nrw.api.RestAppAdminClient;
import de.svws_nrw.api.RestAppDebug;
import de.svws_nrw.api.RestAppSchemaRoot;
import de.svws_nrw.api.RestAppServer;
import de.svws_nrw.config.SVWSKonfiguration;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Implementiert eine Variante des {@link BasicAuthenticator} für den
 * SVWS-Server, der auch unauthorisierte Zugriff an den Login-Service
 * weiterleitet, wo diese geprüft werden.
 */
public final class SVWSAuthenticator extends LoginAuthenticator {

	/**
	 * Erstellt den LoginAuthenticator für den SVWS-Server.
	 */
	public SVWSAuthenticator() {
		super();
	}

	@Override
	public String getAuthenticationType() {
		return Authenticator.BASIC_AUTH;
	}

	@Override
	public AuthenticationState validateRequest(final Request req, final Response res, final Callback callback) throws ServerAuthException {
		// Prüfe, ob der Port zu dem Zugriffsbereich passt, falls in der SVWS-Konfiguration mehrere Ports verwendet werden
		final SVWSKonfiguration config = SVWSKonfiguration.get();
		if (config.hatPortHTTPPrivilegedAccess()) {
			final String pathInfo = Request.getPathInContext(req);
			final boolean isCommonAccess = RestAppDebug.checkIsInPathSpecification(pathInfo)
					|| RestAppServer.checkIsInPathSpecificationCommon(pathInfo);
			final boolean needsPriviledgedAccess = RestAppSchemaRoot.checkIsInPathSpecification(pathInfo)
					|| RestAppAdminClient.checkIsInPathSpecification(pathInfo);
			if (!isCommonAccess && needsPriviledgedAccess && (Request.getServerPort(req) != config.getPortHTTPPrivilegedAccess()))
				throw new ServerAuthException("Zugriff auf diese API wurde in der Serverkonfiguration unterbunden.");
			if (!isCommonAccess && !needsPriviledgedAccess && (Request.getServerPort(req) == config.getPortHTTPPrivilegedAccess()))
				throw new ServerAuthException("Zugriff auf diese API wurde in der Serverkonfiguration unterbunden.");
		}
		// Prüfe die Anmeldenamen...
		final String auth = req.getHeaders().get(HttpHeader.AUTHORIZATION);
		String username = "";
		String password = "";
		String usernameISO_8859_1 = "";
		String passwordISO_8859_1 = "";
		if (auth != null) {
			final int space = auth.indexOf(' ');
			if ((space > 0) && ("basic".equalsIgnoreCase(auth.substring(0, space)))) {
				final String authUTF_8 = new String(Base64.getDecoder().decode(auth.substring(space + 1)), StandardCharsets.UTF_8);
				final int colonUTF8 = authUTF_8.indexOf(':');
				if (colonUTF8 > 0) {
					username = authUTF_8.substring(0, colonUTF8);
					password = authUTF_8.substring(colonUTF8 + 1);
				}
				final String authISO_8859_1 = new String(Base64.getDecoder().decode(auth.substring(space + 1)), StandardCharsets.ISO_8859_1);
				final int colonISO_8859_1 = authISO_8859_1.indexOf(':');
				if (colonISO_8859_1 > 0) {
					usernameISO_8859_1 = authISO_8859_1.substring(0, colonISO_8859_1);
					passwordISO_8859_1 = authISO_8859_1.substring(colonISO_8859_1 + 1);
				}
			}
		}
		/* FIXME: Workaround. DAV-API muss im OPTIONS-Request den DAV-Header ausgeben. Zudem unterstützt DAV weitere HTTP-Methods.
		Die Option-Requests werden in der HttpServer-Klasse als Ausnahme behandelt und nicht weiter an das API durchgeleitet.
		Options-Requests und Header werde hier in dieser Klasse gesetzt. In Hinblick auf eine lose Kopplung ist die folgende Implementierung
		keine gute Lösung. Eine Alternativlösung muss diskutiert werden.
		*/
		if ("OPTIONS".equals(req.getMethod()) && req.getHttpURI().getPath().contains("/dav")) {
			res.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS, HEAD, PROPFIND, REPORT");
			res.getHeaders().add("DAV", "addressbook, calendar-access");
		}
		//Workaround Ende

		if (((username == null) || (username.isBlank())) && (RestAppSchemaRoot.checkIsInPathSpecification(Request.getPathInContext(req)))) {
			// Anmeldung ist nicht möglich, da hier ein anonymer Zugriff prinzipiell nicht möglich ist
		} else {
			try {
				UserIdentity user = login(username, password, req, res);
				if (user != null)
					return new UserAuthenticationSucceeded(getAuthenticationType(), user);
				user = login(usernameISO_8859_1, passwordISO_8859_1, req, res);
				if (user != null)
					return new UserAuthenticationSucceeded(getAuthenticationType(), user);
			} catch (final WebApplicationException wae) {
				try (var r = wae.getResponse()) {
					res.setStatus(r.getStatus());
					final String msg = (r.getEntity() == null) ? wae.getMessage() : r.getEntity().toString();
					res.write(true, ByteBuffer.wrap(msg.getBytes()), callback);
					return AuthenticationState.SEND_FAILURE;
				}
			}
		}
		res.getHeaders().add(HttpHeader.WWW_AUTHENTICATE.asString(), "basic realm=\"" + _loginService.getName() + "\", charset=\"UTF-8\"");
		final int _ACCESS_CONTROL_MAX_AGE_IN_SECONDS = 12 * 60 * 60;
		final String origin = req.getHeaders().get("Origin");
		res.getHeaders().add("Vary", "Origin");
		res.getHeaders().add("Access-Control-Allow-Origin", ((origin == null) || ("".equals(origin))) ? "*" : origin);
		res.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
		res.getHeaders().add("Access-Control-Allow-Credentials", "true");
		res.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS, HEAD");
		res.getHeaders().add("Access-Control-Max-Age", _ACCESS_CONTROL_MAX_AGE_IN_SECONDS);
		// An OPTIONS-Http-Request must be OK for CORS-Preflight-Requests
		Response.writeError(req, res, callback, "OPTIONS".equals(req.getMethod()) ? Status.OK.getStatusCode() : Status.UNAUTHORIZED.getStatusCode());
		return AuthenticationState.SEND_SUCCESS;
	}

}
