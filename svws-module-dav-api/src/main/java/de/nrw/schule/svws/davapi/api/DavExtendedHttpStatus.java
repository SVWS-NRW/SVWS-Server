package de.nrw.schule.svws.davapi.api;

import javax.ws.rs.core.Response;

/**
 * Erweiterung der StatusType-Enumeration um die DAV-spezifischen
 * HTTP-Status-Codes.
 */
public enum DavExtendedHttpStatus implements Response.StatusType {

	/** Konstante für den HTTPStatus 207 MULTISTATUS */
	MULTISTATUS(207, "Multi-Status");

	/** HTTP-Response-Code */
	private final int code;
	/** Beschreibung des HTTP-Response-Codes */
	private final String reason;
	/** Status-Familie des HTTP-Response-Codes */
	private final Response.Status.Family family;

	/**
	 * Erstellt einen neuen DavExtendedHttpStatus
	 * 
	 * @param statusCode   HTTP-Status-Code
	 * @param reasonPhrase Beschreibung des HTTP-Response-Codes
	 */
	DavExtendedHttpStatus(final int statusCode, final String reasonPhrase) {
		this.code = statusCode;
		this.reason = reasonPhrase;
		this.family = Response.Status.Family.familyOf(statusCode);
	}

	@Override
	public int getStatusCode() {
		return code;
	}

	@Override
	public Response.Status.Family getFamily() {
		return family;
	}

	@Override
	public String getReasonPhrase() {
		return reason;
	}
}
