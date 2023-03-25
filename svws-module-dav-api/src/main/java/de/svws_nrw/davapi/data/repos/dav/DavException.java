package de.svws_nrw.davapi.data.repos.dav;

import de.svws_nrw.davapi.model.dav.Response;

/**
 * Eine Ausnahme für Fehlerbehandlungen im DavProtokoll. Neben der Fehlermeldung
 * besteht diese Ausnahme aus dem HTTP Result Code, der dem Client
 * bereitgestellt werden soll.
 *
 */
public class DavException extends Exception {
	/** default serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Enum für ausgewählte HTTP Statuscodes und ihre Beschreibung.
	 *
	 */
	public enum ErrorCode {
		/** Eine referenzierte Ressource wurde nicht gefunden */
		NOT_FOUND(404, "Not Found"),

		/**
		 * Die Anfrage wurde verstanden, aber nicht authorisiert, bspw. fehlende
		 * Nutzerkompetenz oder fehlende Berechtigung an der Ressource
		 */
		FORBIDDEN(403, "Forbidden"),
		/**
		 * Die Anfrage steht im Konflikt zu den Informationen des Servers, bspw bei
		 * Sync-Token bzw. ETag Differenz.
		 */
		CONFLICT(409, "Conflict"),

		/**
		 * Die Anfrage kann aufgrund der größe des Objekts und/oder mangelnden Speichers
		 * nicht verarbeitet werden.
		 */
		INSUFFICIENT_STORAGE(507, "Insufficient Storage"),

		/** Die Anfrage kann aufgrund interner Fehler nicht verarbeitet werden */
		INTERNAL_SERVER_ERROR(500, "Internal Server Error");

		/** der statusCode */
		private final int statusCode;
		/** die Status Message */
		private final String status;

		/**
		 * Konstruktor für HTTP Status Codes
		 * 
		 * @param statusCode der HTTP Statuscode
		 * @param status     der Name des Status
		 */
		private ErrorCode(int statusCode, String status) {
			this.statusCode = statusCode;
			this.status = status;
		}

		/**
		 * erzeugt aus diesem Error ein {@link Response} zur gegebenen URI
		 * 
		 * @param href die URI für die diese Response gilt
		 * @return das Responseobjekt für diese Ausnahme
		 */
		public Response getDavResponse(String href) {
			Response r = new Response();
			r.getHref().add(href);
			r.setStatus(String.format("HTTP/1.1 %d %s", statusCode, status));
			return r;
		}
	}

	/** der HTTP Result Code dieser Instanz */
	private final ErrorCode errorCode;

	/**
	 * Konstruktor mit HTTP Result Code und Errormessage
	 * 
	 * @param errorCode der ResultCode
	 */
	public DavException(ErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
	}

	/**
	 * Konstruktor mit HTTP Result Code und Errormessage
	 * 
	 * @param errorCode der ResultCode
	 * @param message    die Errormessage
	 */
	public DavException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * Konstruktor mit HTTP Result Code und Cause
	 * 
	 * @param errorCode der ResultCode
	 * @param cause      die Ursache dieser Exception
	 */
	public DavException(ErrorCode errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	/**
	 * Konstruktor mit HTTP Result Code,Errormessage und Cause
	 * 
	 * @param errorCode der ResultCode
	 * @param message    die Errormessage
	 * @param cause      die Ursache dieser Exception
	 */
	public DavException(ErrorCode errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	/**
	 * getter für den ErrorCodecode
	 * 
	 * @return den resultcode
	 */
	public ErrorCode getErrorCode() {
		return errorCode;
	}

	/**
	 * erzeugt aus der Exception ein {@link Response} zur gegebenen URI
	 * 
	 * @param href die URI für die diese Response gilt
	 * @return das Responseobjekt für diese Ausnahme
	 */
	public Response getDavResponse(String href) {
		return this.errorCode.getDavResponse(href);
	}
}
