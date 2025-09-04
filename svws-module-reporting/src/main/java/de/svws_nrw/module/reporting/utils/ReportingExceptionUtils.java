package de.svws_nrw.module.reporting.utils;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.thymeleaf.exceptions.TemplateProcessingException;


/**
 * Diese Klasse beinhaltet den Code zur Erstellung verschiedener Fehlerausgaben und Fehler-Responses für das Reporting.
 * Dabei werden neben den Daten der Exception auch Daten des Logging ausgegeben.
 */
public final class ReportingExceptionUtils {

	private ReportingExceptionUtils() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zum Fehler-Logging. Initialisierung nicht möglich.");
	}

	/**
	 * Erzeugt eine Fehlerausgabe (als {@link SimpleOperationResponse}) mit den Daten der Exception und des übergebenen Logs.
	 *
	 * @param exception 	Die Exception, die geworfen wurde.
	 * @param logger 		Logger, der den Ablauf protokolliert und Fehlerdaten gesammelt hat.
	 * @param log 			Liste, die Einträge aus dem Logger gesammelt hat.
	 *
	 * @return 				Die SimpleOperationResponse mit der Fehlerdaten.
	 */
	public static SimpleOperationResponse getSimpleOperationResponse(final Exception exception, final Logger logger, final LogConsumerList log) {

		String htmlTemplate = "";

		if (exception != null) {
			if (exception instanceof final TemplateProcessingException tPE) {
				// Wenn ein Fehler in der Template-Verarbeitung auftritt, speichere das verwendete html-Template für einen späteren Log-Eintrag.
				htmlTemplate = tPE.getTemplateName();
			}

			// Sammle alle Fehlerursachen im Log.
			if (exception instanceof final ApiOperationException aoe) {
				// Der Fehler ist eine ApiOperationException, gebe hier den Code und die Nachricht aus.
				logger.logLn(LogLevel.ERROR, 0, "### Fehler vom Typ ApiOperationException - Code: %d".formatted(aoe.getStatus().getStatusCode()));
				logger.modifyIndent(4);
				String message = aoe.getMessage();
				if (message != null) {
					message = message.replace("(template: \"" + htmlTemplate + "\"", "(");
					logger.logLn(LogLevel.ERROR, message);
				}
			} else {
				// Andere Fehlertypen
				logger.logLn(LogLevel.ERROR, 0, "### Fehler");
				logger.modifyIndent(4);
			}
			// Extrahiere für alle Fehler die Fehlerursachen und schreibe sie ins Log
			for (Throwable cause = exception; cause != null; cause = cause.getCause()) {
				String message = cause.getMessage();
				if ((message != null) && !message.isEmpty()) {
					// Entferne das html-Template, falls es in der Message enthalten ist.
					if (!htmlTemplate.isEmpty())
						message = message.replace("(template: \"" + htmlTemplate + "\"", "(");
					logger.logLn(LogLevel.ERROR, message);
				}
			}

			// Hänge das html-Template als weiteren Eintrag hinten an, wenn ein Fehler bei der Template-Verarbeitung aufgetreten ist.
			if (!htmlTemplate.isEmpty()) {
				logger.logLn(LogLevel.ERROR, 0, "### html-Template-Inhalte vorhanden. Gebe Template aus.");
				logger.logLn(LogLevel.ERROR, 0, htmlTemplate);
				logger.logLn(LogLevel.ERROR, 0, "### Template-Ausgabe beendet.");
			}
		} else {
			logger.logLn(LogLevel.ERROR, 0, "### Fehler ohne Exception - Es werden folgend nur Log-Daten ausgegeben.");
			logger.modifyIndent(4);
		}

		// Erstelle eine SimpleOperationResponse mit dem Log zum Fehler und gebe diese zurück.
		final SimpleOperationResponse simpleOperationResponse = new SimpleOperationResponse();
		simpleOperationResponse.success = false;
		simpleOperationResponse.log = log.getStrings();

		return simpleOperationResponse;
	}

	/**
	 * Erzeugt eine Fehlerausgabe (als Response einer {@link SimpleOperationResponse}) mit den Daten der Exception und des übergebenen Logs.
	 *
	 * @param exception 	Die Exception, die geworfen wurde.
	 * @param logger 		Logger, der den Ablauf protokolliert und Fehlerdaten gesammelt hat.
	 * @param log 			Liste, die Einträge aus dem Logger gesammelt hat.
	 *
	 * @return Die Response der SimpleOperationResponse mit den Fehlerdaten.
	 */
	public static Response getResponse(final Exception exception, final Logger logger, final LogConsumerList log) {
		final SimpleOperationResponse simpleOperationResponse = getSimpleOperationResponse(exception, logger, log);
		Status code = Status.INTERNAL_SERVER_ERROR;
		if (exception instanceof final ApiOperationException aoe)
			code = aoe.getStatus();
		return Response.status(code).type(MediaType.APPLICATION_JSON).entity(simpleOperationResponse).build();
	}

	/**
	 * Erzeugt Log-Einträge für die Inhalte des StackTrace der übergebenen Exception
	 *
	 * @param beschreibung		Optionale Beschreibung, die dem Stacktrace im Log vorangestellt wird.
	 * @param exception 		Die Exception, die geworfen wurde.
	 * @param logger 			Logger, der den Ablauf protokolliert und Fehlerdaten gesammelt hat
	 * @param loglevel			Das Level des Logging, auf dem der Eintrag erfolgen soll.
	 * @param relativeIndent 	Einschub der Meldung gegenüber dem bisherigen Logger Einschub (positive und negative Werte möglich)
	 */
	public static void putStacktraceInLog(final String beschreibung, final Exception exception, final Logger logger, final LogLevel loglevel,
			final int relativeIndent) {
		logger.modifyIndent(relativeIndent);
		if ((beschreibung != null) && !beschreibung.isEmpty())
			logger.logLn(loglevel, beschreibung);
		logger.modifyIndent(4);

		final Writer stringWriter = new StringWriter();
		exception.printStackTrace(new PrintWriter(stringWriter));
		final BufferedReader stacktrace = new BufferedReader(new StringReader(stringWriter.toString()));
		stacktrace.lines().forEach(l -> logger.logLn(loglevel, l));
		logger.modifyIndent(-4);
	}

	/**
	 * Erzeugt Log-Einträge für reine Informationen.
	 *
	 * @param beschreibung		Beschreibung, die im Log erfasst wird.
	 * @param logger 			Logger, der den Ablauf protokolliert und Fehlerdaten gesammelt hat
	 * @param loglevel			Das Level des Logging, auf dem der Eintrag erfolgen soll.
	 * @param relativeIndent 	Einschub der Meldung gegenüber dem bisherigen Logger Einschub (positive und negative Werte möglich)
	 */
	public static void putInfoInLog(final String beschreibung, final Logger logger, final LogLevel loglevel, final int relativeIndent) {
		logger.modifyIndent(relativeIndent);
		if ((beschreibung != null) && !beschreibung.isEmpty())
			logger.logLn(loglevel, beschreibung);
	}

}
