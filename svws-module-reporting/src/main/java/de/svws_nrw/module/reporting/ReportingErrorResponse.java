package de.svws_nrw.module.reporting;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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

		if (logger == null || log == null) {
			this.logger = new Logger();
			this.log = new LogConsumerList();
			this.logger.addConsumer(this.log);
		}
	}


	/**
	 * Erzeugt eine Fehlerausgabe (als {@link SimpleOperationResponse}) mit den Daten der Exception und des Logger
	 * @return Die Response mit der Fehlerdaten.
	 */
	public Response getResponse() {
		String htmlTemplate = "";
		String webAppExBody = "";

		logger.logLn("###  FEHLER  ####################################");

		if (exception != null) {
			if (exception instanceof TemplateProcessingException tPE) {
				// Wenn ein Fehler in der Template-Verarbeitung auftritt, speichere das verwendete html-Template für einen späteren Log-Eintrag.
				htmlTemplate = tPE.getTemplateName();
			}

			// Sammle alle Fehlerursachen im Log.
			for (Throwable cause = exception; cause != null; cause = cause.getCause()) {
				String message = cause.getMessage();
				// Entferne das html-Template, falls es in der Message enthalten ist.
				if (exception instanceof TemplateProcessingException && !htmlTemplate.isEmpty())
					message = message.replace("(template: \"" + htmlTemplate + "\"", "(");
				logger.logLn(4, message);
			}

			// Hänge das html-Template als weiteren Eintrag hinten an, wenn ein Fehler bei der Template-Verarbeitung aufgetreten ist.
			if (exception instanceof TemplateProcessingException && !htmlTemplate.isEmpty()) {
				logger.logLn(0, "");
				logger.logLn(0, "###  Verwendetes html-Template  #################");
				logger.logLn(0, htmlTemplate);
			}
		} else {
			logger.logLn(0, "Keine Exception übergeben, es stehen daher nur Log-Daten zur Verfügung.");
		}


		// Erstelle eine SimpleOperationResponse mit dem Log zum Fehler und gebe diese zurück.
		SimpleOperationResponse simpleOperationResponse = new SimpleOperationResponse();
		simpleOperationResponse.success = false;
		simpleOperationResponse.log = log.getStrings();

		int code = OperationError.INTERNAL_SERVER_ERROR.getCode();
		if (exception != null && exception instanceof WebApplicationException wAE) {
			code = wAE.getResponse().getStatus();
			// Wenn eine WebApplicationException auftritt, gebe den Body der Response als Text aus.
			webAppExBody = wAE.getResponse().readEntity(String.class);
			if (webAppExBody != null)
				logger.logLn(4, "WebApplicationException - Code: %d - Message: %s".formatted(wAE.getResponse().getStatus(), webAppExBody));
			else
				logger.logLn(4, "WebApplicationException - Code: %d".formatted(wAE.getResponse().getStatus()));
		}

		return Response.status(code).type(MediaType.APPLICATION_JSON).entity(simpleOperationResponse).build();
	}
}
