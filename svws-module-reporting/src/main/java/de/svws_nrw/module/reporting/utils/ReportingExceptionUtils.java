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
	 * Erzeugt eine Fehlerausgabe (als {@link SimpleOperationResponse}) mit den Daten des übergebenen Logs.
	 *
	 * @param log 			Liste, die Einträge aus dem Logger gesammelt hat.
	 *
	 * @return 				Die SimpleOperationResponse, die das Log enthält.
	 */
	public static SimpleOperationResponse getLogAsSimpleOperationResponse(final LogConsumerList log) {
		// Es wird eine SimpleOperationResponse mit dem Log zum Fehler erstellt und zurückgegeben.
		final SimpleOperationResponse simpleOperationResponse = new SimpleOperationResponse();
		simpleOperationResponse.success = false;
		simpleOperationResponse.log = log.getStrings();

		return simpleOperationResponse;
	}

	/**
	 * Erzeugt Log-Einträge für die Inhalte der übergebenen Exception, inklusive Causes und StackTrace.
	 * <p>Das übergebene Log-Level gilt für den gesamten Block. Ein Aufrufer, der einen Fehler abfängt und mit einem Rückfallwert weiterarbeitet, protokolliert
	 * damit unterhalb von {@link LogLevel#ERROR}: Dieses Level ist dem Abbruch vorbehalten, und ein erfolgreicher Report hinterlässt keinen solchen Eintrag.</p>
	 * <p>Der Einzug des Loggers ist nach dem Aufruf derselbe wie davor. Andernfalls summierte sich der Einzug aller Aufrufe über die weiteren Log-Einträge des
	 * Reports auf.</p>
	 *
	 * @param beschreibung		Optionale Beschreibung, die im Log vorangestellt wird.
	 * @param exception 		Die Exception, die geworfen wurde.
	 * @param logger 			Logger, der den Ablauf protokolliert und Fehlerdaten gesammelt hat
	 * @param loglevel			Das Level des Logging, auf dem der Eintrag erfolgen soll.
	 * @param relativeIndent 	Einschub der Meldung gegenüber dem bisherigen Logger Einschub (positive und negative Werte möglich)
	 */
	public static void logException(final String beschreibung, final Exception exception, final Logger logger, final LogLevel loglevel,
			final int relativeIndent) {
		final int indentVorAufruf = logger.getIndent();
		try {
			logInfo(beschreibung, logger, loglevel, relativeIndent);

			logger.modifyIndent(4);

			if (exception != null) {
				final String htmlTemplate = (exception instanceof final TemplateProcessingException tpe) ? tpe.getTemplateName() : "";
				final String templateOriginalString = htmlTemplate.isEmpty() ? "" : ("(template: \"" + htmlTemplate + "\"");
				final String templateReplaceString = htmlTemplate.isEmpty() ? "" : "(REMOVED TEMPLATE FROM LOG";

				logExceptionTypeAndMessage(exception, logger, loglevel, templateOriginalString, templateReplaceString);
				logErrorCauses(exception, logger, loglevel, templateOriginalString, templateReplaceString);
				logStackTrace(exception, logger, loglevel, templateOriginalString, templateReplaceString);
			} else {
				logger.logLn(loglevel, 0, "### FEHLER: Fehler ohne Exception - Es werden im Folgenden nur Log-Daten ausgegeben.");
			}
		} finally {
			logger.setIndent(indentVorAufruf);
		}
	}

	/**
	 * Protokolliert eine Informationsnachricht mit einem gegebenen Logger.
	 *
	 * @param beschreibung    Eine optionale Beschreibung, die protokolliert werden soll.
	 * @param logger          Der Logger, der die Nachricht protokolliert.
	 * @param loglevel        Das Level des Loggings, auf dem die Nachricht protokolliert werden soll.
	 * @param relativeIndent  Der relative Einschub für die Protokollierung, der zum aktuellen Logger-Einschub hinzugefügt wird
	 *                        (positive und negative Werte sind möglich).
	 */
	public static void logInfo(final String beschreibung, final Logger logger, final LogLevel loglevel, final int relativeIndent) {
		logger.modifyIndent(relativeIndent);
		if ((beschreibung != null) && !beschreibung.isEmpty()) {
			logger.logLn(loglevel, beschreibung);
		}
	}

	private static void logExceptionTypeAndMessage(final Exception exception, final Logger logger, final LogLevel loglevel,
			final String templateOriginalString, final String templateReplaceString) {
		if (exception instanceof final ApiOperationException aoe) {
			logger.logLn(loglevel, 0, "### FEHLER: Fehler vom Typ ApiOperationException - Code: %d".formatted(aoe.getStatus().getStatusCode()));
		} else {
			logger.logLn(loglevel, 0, "### FEHLER: Fehler vom Typ %s".formatted(exception.getClass().getSimpleName()));
		}
		logger.modifyIndent(4);
		// Die Meldung wird für jeden Exception-Typ ausgegeben. Sie ist die eigentliche Fehlerbeschreibung und steht sonst nur in der Kopfzeile des Stacktrace.
		String message = exception.getMessage();
		if (message != null) {
			if (!templateOriginalString.isEmpty()) {
				message = message.replace(templateOriginalString, templateReplaceString);
			}
			logger.logLn(loglevel, message);
		}
	}

	private static void logErrorCauses(final Exception exception, final Logger logger, final LogLevel loglevel, final String templateOriginalString,
			final String templateReplaceString) {
		// Der Abschnitt zeigt die Ursachenkette und beginnt deshalb erst bei der ersten Ursache: Die Meldung der Exception selbst steht bereits im
		// Abschnitt darüber. Ohne Ursache entfällt der Abschnitt, statt eine Überschrift über eine Kopie zu setzen.
		if (exception.getCause() == null) {
			return;
		}
		logger.logLn(loglevel, 0, "### FEHLERGRÜNDE:");
		logger.modifyIndent(4);
		for (Throwable cause = exception.getCause(); cause != null; cause = cause.getCause()) {
			String message = cause.getMessage();
			if ((message != null) && !message.isEmpty()) {
				if (!templateOriginalString.isEmpty()) {
					message = message.replace(templateOriginalString, templateReplaceString);
				}
				logger.logLn(loglevel, message);
			}
		}
		logger.modifyIndent(-4);
	}

	private static void logStackTrace(final Exception exception, final Logger logger, final LogLevel loglevel, final String templateOriginalString,
			final String templateReplaceString) {
		logger.logLn(loglevel, 0, "### STACKTRACE:");
		logger.modifyIndent(4);

		final Writer stringWriter = new StringWriter();
		if (exception.getCause() == null) {
			exception.printStackTrace(new PrintWriter(stringWriter));
		} else {
			exception.getCause().printStackTrace(new PrintWriter(stringWriter));
		}

		String fullStacktrace = stringWriter.toString();
		if (!templateOriginalString.isEmpty()) {
			fullStacktrace = fullStacktrace.replace(templateOriginalString, templateReplaceString);
		}

		final BufferedReader reader = new BufferedReader(new StringReader(fullStacktrace));
		reader.lines().forEach(l -> logger.logLn(loglevel, l));

		logger.modifyIndent(-4);
	}

}
