package de.svws_nrw.module.reporting;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.thymeleaf.exceptions.TemplateProcessingException;


/**
 * Diese Klasse beinhaltet den Code zur Erstellung einer Error-Response für das Reporting inklusive Daten des Loggers
 * in Form einer {@link SimpleOperationResponse}.
 */
public final class ReportingErrorResponse {

	/** Die Exception, die geworfen wurde. */
	private final Exception exception;

	/** Logger, der den Ablauf protokolliert und Fehlerdaten gesammelt hat */
	private Logger logger;

	/** Liste, die Einträge aus dem Logger gesammelt hat. */
	private LogConsumerList log;

	private Status code = Status.INTERNAL_SERVER_ERROR;


	/**
	 * Erzeugt eine neue Reporting-Error-Response.
	 * @param exception Die Exception, die geworfen wurde.
	 * @param logger Logger, der den Ablauf protokolliert und Fehlerdaten gesammelt hat
	 * @param log Liste, die Einträge aus dem Logger gesammelt hat.
	 */
	public ReportingErrorResponse(final Exception exception, final Logger logger, final LogConsumerList log) {

		this.exception = exception;
		this.logger = logger;
		this.log = log;

		if ((logger == null) || (log == null)) {
			this.logger = new Logger();
			this.log = new LogConsumerList();
			this.logger.addConsumer(this.log);
		}
	}


	/**
	 * Erzeugt eine Fehlerausgabe (als {@link SimpleOperationResponse}) mit den Daten der Exception und des Loggers
	 * @return Die SimpleOperationResponse mit der Fehlerdaten.
	 */
	public SimpleOperationResponse getSimpleOperationResponse() {
		String htmlTemplate = "";
		code = Status.INTERNAL_SERVER_ERROR;

		if (exception != null) {
			if (exception instanceof final TemplateProcessingException tPE) {
				// Wenn ein Fehler in der Template-Verarbeitung auftritt, speichere das verwendete html-Template für einen späteren Log-Eintrag.
				htmlTemplate = tPE.getTemplateName();
			}

			// Sammle alle Fehlerursachen im Log.
			if (exception instanceof final ApiOperationException aoe) {
				// Der Fehler ist eine ApiOperationException, gebe hier den Code und die Nachricht aus.
				code = aoe.getStatus();
				logger.logLn(0, "## Fehler vom Typ ApiOperationException - Code: %d".formatted(aoe.getStatus().getStatusCode()));
				final String message = aoe.getMessage();
				if (message != null)
					logger.logLn(4, message);
			} else {
				// Andere Fehlertypen
				logger.logLn(0, "## Fehler");
			}
			// Extrahiere für alle Fehler die Fehlerursachen und schreibe sie ins Log
			for (Throwable cause = exception; cause != null; cause = cause.getCause()) {
				String message = cause.getMessage();
				if ((message != null) && !message.isEmpty()) {
					// Entferne das html-Template, falls es in der Message enthalten ist.
					if ((exception instanceof TemplateProcessingException) && !htmlTemplate.isEmpty())
						message = message.replace("(template: \"" + htmlTemplate + "\"", "(");
					logger.logLn(4, message);
				}
			}

			// Hänge das html-Template als weiteren Eintrag hinten an, wenn ein Fehler bei der Template-Verarbeitung aufgetreten ist.
			if ((exception instanceof TemplateProcessingException) && !htmlTemplate.isEmpty()) {
				logger.logLn(0, "");
				logger.logLn(0, "##  Verwendetes html-Template");
				logger.logLn(4, htmlTemplate);
			}
		} else {
			logger.logLn(0, "## Fehler ohne Exception");
			logger.logLn(4, "Es werden folgend nur Log-Daten ausgegeben.");
		}

		// Erstelle eine SimpleOperationResponse mit dem Log zum Fehler und gebe diese zurück.
		final SimpleOperationResponse simpleOperationResponse = new SimpleOperationResponse();
		simpleOperationResponse.success = false;
		simpleOperationResponse.log = log.getStrings();

		return simpleOperationResponse;
	}

	/**
	 * Erzeugt eine Fehlerausgabe (als Response einer {@link SimpleOperationResponse}) mit den Daten der Exception und des Loggers
	 * @return Die Response der SimpleOperationResponse mit den Fehlerdaten.
	 */
	public Response getResponse() {
		// Generiere die SOP und setze damit auch den Code der Response
		final SimpleOperationResponse simpleOperationResponse = getSimpleOperationResponse();
		return Response.status(code).type(MediaType.APPLICATION_JSON).entity(simpleOperationResponse).build();
	}

}
