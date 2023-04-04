package de.svws_nrw.db.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * Diese Aufzählung enthält die Codes für Fehler, die bei Datenbank-Operationen
 * auftreten können. Diese orientieren sich an den HTTP-Statuscodes.
 */
public enum OperationError {

	/** Der Pseudo-Fehlercode -1 signalisiert, dass die Operation nichts bewirkt und deswegen abgebrochen werden kann */
	NOTHING_TO_DO(-1),

	/** Der Fehlercode 400 signalisiert, dass der Operation eine fehlerhafte Anfrage zugrund liegt. */
	BAD_REQUEST(400),

	/** Der Fehlercode 403 signalisiert, dass die Operation aufgrund fehlender Rechte nicht erlaubt ist. */
	FORBIDDEN(403),

	/** Der Fehlercode 404 signalisiert, dass ein Datensatz, der für die Operation notwendig ist, nicht gefunden wurde. */
	NOT_FOUND(404),

	/** Der Fehlercode 409 signalisiert, dass ein Wert, welcher der Operation zugrunde liegt fehlerhaft ist und zu einem
	 * Konflikt mit Constraints in Bezug auf die Daten führt.  */
	CONFLICT(409),

	/** Der Fehlercode 500 signalisiert, dass ein nicht näher spezifizierter Fehler bei der Operation aufgetreten ist. */
	INTERNAL_SERVER_ERROR(500),

	/** Der Fehlercode 503 signalisiert, dass ein Zugriff auf die Datenbank nicht möglich ist, da das Schema zur Zeit vom SVWS-Server gesperrt ist. */
	SERVICE_UNAVAILABLE(503);


	/** Der Integer-Wert des Fehlers bei der Operation - meist ein HTTP-Statuscode */
	int code;


	/**
	 * Erzeugt eine neue Datenbank-Operations-Fehlerart mit dem angegeben Fehlercode.
	 *
	 * @param code   der Integerwert des Fehlercodes
	 */
	OperationError(final int code) {
		this.code = code;
	}

	/**
	 * Liefert den Fehlercode des Datenbank-Operations-Fehlers
	 *
	 * @return der Integerwert des Fehlercodes
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Ermittelt den Datenbank-Operations-Fehler anhand des Fehlercodes.
	 *
	 * @param code   der Integerwert des Fehlercodes
	 *
	 * @return der Datenbank-Operations-Fehler
	 */
	public static OperationError getByCode(final int code) {
		for (final OperationError err : OperationError.values())
			if (err.code == code)
				return err;
		return INTERNAL_SERVER_ERROR;
	}


	/**
	 * Gibt die Antwort (siehe {@link Response}) mit dem zugehörigen Status-Code zurück.
	 *
	 * @return die Antwort
	 */
	public Response getResponse() {
		return Response.status(getCode()).build();
	}


	/**
	 * Gibt die Antwort (siehe {@link Response}) mit dem zugehörigen Status-Code zurück.
	 *
	 * @param body    der zu serialisierende Body der Antwort (z.B. log-Informationen zu einem Fehler)
	 *
	 * @return die Antwort
	 */
	public Response getResponse(final Object body) {
		return Response.status(getCode()).entity(body).build();
	}


	/**
	 * Erzeugt eine Exception basierend auf Fehler der Datenbank-Operation
	 *
	 * @return die WebApplicationException für die Handhabung in der OpenAPI-Schnittstelle
	 */
	public WebApplicationException exception() {
		return new WebApplicationException(getCode());
	}


	/**
	 * Erzeugt eine Exception basierend auf Fehler der Datenbank-Operation
	 *
	 * @param body    der zu serialisierende Body der Exception (z.B. log-Informationen zum Fehler)
	 *
	 * @return die WebApplicationException für die Handhabung in der OpenAPI-Schnittstelle
	 */
	@SuppressWarnings("resource")
	public WebApplicationException exception(final Object body) {
		return new WebApplicationException(getResponse(body));
	}


	/**
	 * Erzeugt eine Exception basierend auf Fehler der Datenbank-Operation
	 *
	 * @param cause   der Grund für diese Exception
	 *
	 * @return die WebApplicationException für die Handhabung in der OpenAPI-Schnittstelle
	 */
	public WebApplicationException exception(final Throwable cause) {
		return new WebApplicationException(cause, getCode());
	}


	/**
	 * Erzeugt eine Exception basierend auf Fehler der Datenbank-Operation
	 *
	 * @param cause   der Grund für diese Exception
	 * @param body    der zu serialisierende Body der Exception (z.B. log-Informationen zum Fehler)
	 *
	 * @return die WebApplicationException für die Handhabung in der OpenAPI-Schnittstelle
	 */
	@SuppressWarnings("resource")
	public WebApplicationException exception(final Throwable cause, final Object body) {
		return new WebApplicationException(cause, getResponse(body));
	}

}
