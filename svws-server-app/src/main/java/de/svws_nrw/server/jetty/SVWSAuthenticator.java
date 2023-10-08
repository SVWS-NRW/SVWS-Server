package de.svws_nrw.server.jetty;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.WebApplicationException;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.security.ServerAuthException;
import org.eclipse.jetty.security.UserAuthentication;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.security.authentication.LoginAuthenticator;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.server.Authentication.User;
import org.eclipse.jetty.util.security.Constraint;

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
	}

    @Override
    public String getAuthMethod() {
        return Constraint.__BASIC_AUTH;
    }

    @Override
    public Authentication validateRequest(final ServletRequest req, final ServletResponse res, final boolean mandatory) throws ServerAuthException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        final String auth = request.getHeader(HttpHeader.AUTHORIZATION.asString());
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
        if ("OPTIONS".equals(request.getMethod()) && request.getRequestURI().contains("/dav")) {
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS, HEAD, PROPFIND, REPORT");
            response.setHeader("DAV", "addressbook, calendar-access");
        }
        //Workaround Ende

        try {
	        UserIdentity user = login(username, password, request);
	        if (user != null)
	            return new UserAuthentication(getAuthMethod(), user);
	        user = login(usernameISO_8859_1, passwordISO_8859_1, request);
	        if (user != null)
	            return new UserAuthentication(getAuthMethod(), user);
        } catch (final WebApplicationException wae) {
    		try (var r = wae.getResponse(); var writer = response.getWriter()) {
    			response.setStatus(r.getStatus());
    			writer.print(r.getEntity());
        		return Authentication.SEND_FAILURE;
            } catch (final IOException e) {
                throw new ServerAuthException(e);
            }
        }
        try {
            response.setHeader(HttpHeader.WWW_AUTHENTICATE.asString(), "basic realm=\"" + _loginService.getName() + "\", charset=\"UTF-8\"");
			final int _ACCESS_CONTROL_MAX_AGE_IN_SECONDS = 12 * 60 * 60;
			final String origin = request.getHeader("Origin");
			response.setHeader("Vary", "Origin");
			response.setHeader("Access-Control-Allow-Origin", ((origin == null) || ("".equals(origin))) ? "*" : origin);
			response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS, HEAD");
			response.setIntHeader("Access-Control-Max-Age", _ACCESS_CONTROL_MAX_AGE_IN_SECONDS);
			// An OPTIONS-Http-Request must be OK for CORS-Preflight-Requests
            response.sendError("OPTIONS".equals(request.getMethod()) ? HttpServletResponse.SC_OK : HttpServletResponse.SC_UNAUTHORIZED);
            return Authentication.SEND_CONTINUE;
        } catch (final IOException e) {
            throw new ServerAuthException(e);
        }
    }

    @Override
    public boolean secureResponse(final ServletRequest req, final ServletResponse res, final boolean mandatory, final User validatedUser) throws ServerAuthException {
        return true;
    }

}
