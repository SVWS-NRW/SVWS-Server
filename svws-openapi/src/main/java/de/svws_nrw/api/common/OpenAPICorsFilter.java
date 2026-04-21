package de.svws_nrw.api.common;

import java.io.IOException;

import de.svws_nrw.config.SVWSKonfiguration;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;

/**
 * Implementiert einen Filter, um HTTP-Requests zu prüfen und HTTP-Responses zu erweitern.
 */
public final class OpenAPICorsFilter implements ContainerResponseFilter, ContainerRequestFilter {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public OpenAPICorsFilter() {
		// leer
	}

	@Override
	public void filter(final ContainerRequestContext requestContext) throws IOException {
//		MultivaluedMap<String, String> headers = requestContext.getHeaders();
//		System.out.println("INCOMING: " + requestContext.getUriInfo().getPath());
//		headers.add("Access-Control-Request-Method", null);
//		headers.add("Access-Control-Request-Headers", null);
//		System.out.println("...");
	}

	@Override
	public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext)
			throws IOException {
		if (SVWSKonfiguration.get().useCORSHeader()) {
			final int _ACCESS_CONTROL_MAX_AGE_IN_SECONDS = 12 * 60 * 60;
			final MultivaluedMap<String, Object> headers = responseContext.getHeaders();
			// Hinweis: Die Kombination aus "Access-Control-Allow-Origin: *" und
			// "Access-Control-Allow-Credentials: true" ist laut CORS-Spezifikation unzulässig
			// und wird von aktuellen Browsern abgelehnt. Wir spiegeln daher - sofern vorhanden -
			// den Origin-Header der Anfrage zurück und setzen "Vary: Origin", damit Caches
			// zwischen unterschiedlichen Ursprüngen unterscheiden. Nur wenn ein Origin
			// zurückgegeben wird, wird auch "Allow-Credentials" gesendet.
			final String origin = requestContext.getHeaderString("Origin");
			if ((origin != null) && !origin.isBlank()) {
				headers.add("Access-Control-Allow-Origin", origin);
				headers.add("Vary", "Origin");
				headers.add("Access-Control-Allow-Credentials", "true");
			} else {
				headers.add("Access-Control-Allow-Origin", "*");
			}
			headers.add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
			headers.add("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS, HEAD");
			headers.add("Access-Control-Max-Age", Integer.toString(_ACCESS_CONTROL_MAX_AGE_IN_SECONDS));
			headers.add("Access-Control-Expose-Headers", "Content-Disposition");
		}
	}

}
