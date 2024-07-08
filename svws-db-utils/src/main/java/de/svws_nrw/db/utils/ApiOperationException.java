package de.svws_nrw.db.utils;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

/**
 * Eine Exception, welche im Verlauf einer Operation von der OpenAPI
 * erzeugt wird.
 */
public class ApiOperationException extends Exception {

	private static final long serialVersionUID = -7737733085773788685L;

	/** Der HTTP-Status-Code */
	private final Status status;

	/** Der zu serialisierende Body der Exception */
	private final Object body;

	/** der Mime-Type der Body-Information */
	private final String mimeType;



	/**
	 * Erzeugt eine Exception basierend auf dem Status-Code
	 *
	 * @param status   der Status-Code
	 */
	public ApiOperationException(final @NotNull Status status) {
		this(status, null, null, null);
	}


	/**
	 * Erzeugt eine Exception basierend auf dem Status-Code und dem zu serialisierenden Body.
	 *
	 * @param status   der Status-Code
	 * @param body     der zu serialisierende Body der Exception (z.B. log-Informationen zum Fehler)
	 */
	public ApiOperationException(final @NotNull Status status, final Object body) {
		this(status, null, body, null);
	}


	/**
	 * Erzeugt eine Exception basierend auf dem Status-Code und dem zugrundeliegenden Grund.
	 *
	 * @param status   der Status-Code
	 * @param cause    der Grund für diese Exception
	 */
	public ApiOperationException(final @NotNull Status status, final Throwable cause) {
		this(status, cause, null, null);
	}


	/**
	 * Erzeugt eine Exception basierend auf dem Status-Code, dem zu serialisierenden Body
	 * und dem Mime-Type der Body-Information.
	 *
	 * @param status   der Status-Code
	 * @param cause    der Grund für diese Exception
	 * @param body     der zu serialisierende Body der Exception (z.B. log-Informationen zum Fehler)
	 */
	public ApiOperationException(final @NotNull Status status, final Throwable cause, final Object body) {
		this(status, cause, body, null);
	}


	/**
	 * Erzeugt eine Exception basierend auf dem Status-Code, dem zugrundeliegenden Grund,
	 * dem zu serialisierenden Body und dem Mime-Type der Body-Information.
	 *
	 * @param status     der Status-Code
	 * @param cause      der Grund für diese Exception
	 * @param body       der zu serialisierende Body der Exception (z.B. log-Informationen zum Fehler)
	 * @param mimeType   der Mime-Type der Body-Information
	 */
	public ApiOperationException(final @NotNull Status status, final Throwable cause, final Object body, final String mimeType) {
		super((body instanceof final String message) ? message : null, cause);
		this.status = status;
		this.body = body;
		this.mimeType = mimeType;
	}


	/**
	 * Gibt den HTTP-Response-Code dieser Exception zurück.
	 *
	 * @return der HTTP-Response-Code
	 */
	public @NotNull Status getStatus() {
		return status;
	}


	/**
	 * Der zu serialisierende Body dieser Exception.
	 *
	 * @return der zu serialisierende Body
	 */
	public Object getBody() {
		return body;
	}


	/**
	 * Der Mime-Type für den zu serialisierenden Body
	 *
	 * @return der Mime-Type für den zu serialisierenden Body
	 */
	public String getMimeType() {
		return mimeType;
	}


	/**
	 * Erstellt eine zugehörige HTTP-Response zu dieser Exception
	 *
	 * @return die HTTP-Response
	 */
	public Response getResponse() {
		ResponseBuilder builder = Response.status(this.status);
		if (this.mimeType != null)
			builder = builder.type(this.mimeType);
		if (this.body != null)
			builder = builder.entity(this.body);
		return builder.build();
	}

}
