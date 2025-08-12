package de.svws_nrw.davapi.data.dav;

import de.svws_nrw.davapi.model.dav.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Eine Exception für die Handhabung von Fehlern in der Implementierung des DAV-Protokolls. Diese beinhaltet
 * den HTTP-Status-Code für die zu generierende Response.
 */
public class DavException extends Exception {

	private static final long serialVersionUID = -8300987741470320994L;


	/** der HTTP-Status-Code */
	private final Status status;


	/**
	 * Erzeugt eine neue Exception für die Fehlerbehandlung in der DAV-Protokoll-Implementierung
	 *
	 * @param status   der HTTP-Status-Code
	 */
	public DavException(final Status status) {
		super();
		this.status = status;
	}


	/**
	 * Gibt den HTTP-Status-Code zurück.
	 *
	 * @return der HTTP-Status-Code
	 */
	public Status getStatus() {
		return status;
	}


	/**
	 * Erzeugt eine {@link Response} für das DAV-Protoll mit Bezug auf das betroffene Objekt
	 * anhand der übergebenen URI.
	 *
	 * @param uri   die URI für die diese Response gilt
	 *
	 * @return das Responseobjekt für diese Ausnahme
	 */
	public Response getDavResponse(final String uri) {
		final Response r = new Response();
		r.getHref().add(uri);
		r.setStatus(String.format("HTTP/1.1 %d %s", this.status.getStatusCode(), this.status.getReasonPhrase()));
		return r;
	}

}
