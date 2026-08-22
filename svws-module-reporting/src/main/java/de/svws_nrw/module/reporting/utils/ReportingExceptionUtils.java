package de.svws_nrw.module.reporting.utils;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

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
	 * Erzeugt eine Fehlerausgabe wie {@link #getLogAsSimpleOperationResponse(LogConsumerList)} und stellt dem Log eine Kopfzeile voran.
	 * <p>Das Log eines abgebrochenen Reports ist lang, und der Abbruchgrund steht an seinem Ende - unterhalb aller Befunde, die der Lauf zuvor gemeldet hat
	 * und die mit dem Abbruch nichts zu tun haben. Wer die Antwort liest, sieht deshalb zuerst einen unbeteiligten Fehler. Die Kopfzeile wiederholt den
	 * Abbruchgrund bewusst an erster Stelle.</p>
	 *
	 * @param log       Liste, die Einträge aus dem Logger gesammelt hat.
	 * @param kopfzeile Die Zeile, die dem Log vorangestellt wird; {@code null} oder leer lässt das Log unverändert.
	 *
	 * @return Die SimpleOperationResponse, die die Kopfzeile und das Log enthält.
	 */
	public static SimpleOperationResponse getLogAsSimpleOperationResponse(final LogConsumerList log, final String kopfzeile) {
		final SimpleOperationResponse simpleOperationResponse = getLogAsSimpleOperationResponse(log);
		if ((kopfzeile != null) && !kopfzeile.isEmpty()) {
			// Die Leerzeile hebt die Kopfzeile vom Ablauf darunter ab.
			simpleOperationResponse.log.add(0, "");
			simpleOperationResponse.log.add(0, kopfzeile);
		}
		return simpleOperationResponse;
	}

	/**
	 * Bildet die Kopfzeile für einen abgebrochenen Report. Sie nennt den Statuscode und die Meldung des Abbruchs.
	 * <p>Trägt die Ursachenkette eine {@link ApiOperationException}, gilt deren Meldung: Sie benennt den Abbruchgrund fachlich, während die Meldung der
	 * äußersten Exception nur sagt, an welcher Stelle der Erzeugung er aufgetreten ist.</p>
	 *
	 * @param statuscode     Der HTTP-Status, mit dem die Anfrage endet.
	 * @param fehlerursache  Die Exception, die zum Abbruch geführt hat; darf {@code null} sein.
	 * @param ersatzmeldung  Die Meldung, die gilt, wenn die Kette keine ApiOperationException trägt.
	 *
	 * @return Die Kopfzeile für das Log der Fehlerantwort.
	 */
	public static String abbruchKopfzeile(final int statuscode, final Exception fehlerursache, final String ersatzmeldung) {
		final String ausDerKette = abbruchmeldungInUrsachenkette(fehlerursache);
		final String meldung = (ausDerKette != null) ? ausDerKette : ersatzmeldung;
		// Auch die Kopfzeile wird maskiert: Einzelne Stellen übernehmen die Meldung eines fremden Fehlers in ihre eigene, und dann trüge sie den
		// Quelltext der Vorlage an die erste Stelle der Fehlerantwort.
		final String ohneQuelltext = ohneTemplateQuelltext(meldung, templateQuelltexteInUrsachenkette(fehlerursache));
		return "ABBRUCH (Status %d): %s".formatted(statuscode, ohneMarker(ohneQuelltext));
	}

	/**
	 * Entfernt den führenden Marker einer Meldung. Die Kopfzeile ist an ihrem eigenen Wort erkennbar; ein zweiter Marker mitten in der Zeile führt in der
	 * schmalen Fehlerleiste des Clients zu nichts.
	 *
	 * @param meldung Die Meldung; darf {@code null} sein.
	 *
	 * @return Die Meldung ohne führenden Marker; ein leerer Text, wenn keine Meldung übergeben wurde.
	 */
	private static String ohneMarker(final String meldung) {
		if (meldung == null) {
			return "";
		}
		final String getrimmt = meldung.strip();
		return getrimmt.startsWith("###") ? getrimmt.substring(3).strip() : getrimmt;
	}

	/**
	 * Sucht in der Ursachenkette die Meldung, die den Abbruch fachlich benennt.
	 * <p>Gesucht wird die erste {@link ApiOperationException} <b>mit</b> Meldung. Eine ApiOperationException trägt nur dann eine Meldung, wenn ihr Body ein
	 * Text ist; wer eine Fehlerantwort als Body mitgibt, hinterlässt eine Exception ohne Meldung. Beim ersten Glied stehenzubleiben hieße, den Abbruchgrund
	 * zu verlieren, obwohl er ein Glied tiefer steht.</p>
	 *
	 * @param fehler Der Fehler, dessen Kette durchsucht wird.
	 *
	 * @return Die Meldung oder {@code null}, wenn die Kette keine trägt.
	 */
	private static String abbruchmeldungInUrsachenkette(final Throwable fehler) {
		Throwable glied = fehler;
		// Die Kette ist begrenzt, damit eine im Kreis verkettete Ursache diese Suche nicht endlos laufen lässt.
		for (int i = 0; (glied != null) && (i < 20); i++) {
			if ((glied instanceof final ApiOperationException aoe) && (aoe.getMessage() != null) && !aoe.getMessage().isEmpty()) {
				return aoe.getMessage();
			}
			glied = glied.getCause();
		}
		return null;
	}

	/**
	 * Erzeugt Log-Einträge für die Inhalte der übergebenen Exception, inklusive Causes und StackTrace.
	 * <p>Das übergebene Log-Level gilt für den gesamten Block. Ein Aufrufer, der einen Fehler abfängt und mit einem Rückfallwert weiterarbeitet, protokolliert
	 * damit unterhalb von {@link LogLevel#ERROR}: Dieses Level ist dem Abbruch vorbehalten, und ein erfolgreicher Report hinterlässt keinen solchen Eintrag.</p>
	 * <p>Der Einzug des Loggers ist nach dem Aufruf derselbe wie davor. Andernfalls summierte sich der Einzug aller Aufrufe über die weiteren Log-Einträge des
	 * Reports auf.</p>
	 * <p>Eingebetteter Template-Quelltext wird durch einen Platzhalter ersetzt. Thymeleaf bekommt die Vorlage als Zeichenkette und führt sie als Namen des
	 * Templates in seinen Meldungen; ohne die Ersetzung stünde die ganze Vorlage mehrfach im Log und in der Fehlerantwort an den Client.</p>
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
				final List<String> templateQuelltexte = templateQuelltexteInUrsachenkette(exception);

				logExceptionTypeAndMessage(exception, logger, loglevel, templateQuelltexte);
				logErrorCauses(exception, logger, loglevel, templateQuelltexte);
				logStackTrace(exception, logger, loglevel, templateQuelltexte);
			} else {
				logger.logLn(loglevel, 0, "### FEHLER: Fehler ohne Exception - Es werden im Folgenden nur Log-Daten ausgegeben.");
			}
		} finally {
			logger.setIndent(indentVorAufruf);
		}
	}

	/**
	 * Sucht in der Ursachenkette des Fehlers die erste statustragende {@link ApiOperationException}. Die Kette ist begrenzt, damit eine im Kreis
	 * verkettete Ursache nicht endlos läuft.
	 *
	 * @param fehler Der Fehler, dessen Kette durchsucht wird.
	 *
	 * @return Die erste ApiOperationException der Kette oder {@code null}, wenn keine enthalten ist.
	 */
	public static ApiOperationException apiOperationExceptionInUrsachenkette(final Throwable fehler) {
		Throwable glied = fehler;
		for (int i = 0; (glied != null) && (i < 20); i++) {
			if (glied instanceof final ApiOperationException aoe) {
				return aoe;
			}
			glied = glied.getCause();
		}
		return null;
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
			final List<String> templateQuelltexte) {
		if (exception instanceof final ApiOperationException aoe) {
			logger.logLn(loglevel, 0, "### FEHLER: Fehler vom Typ ApiOperationException - Code: %d".formatted(aoe.getStatus().getStatusCode()));
		} else {
			logger.logLn(loglevel, 0, "### FEHLER: Fehler vom Typ %s".formatted(exception.getClass().getSimpleName()));
		}
		logger.modifyIndent(4);
		// Die Meldung wird für jeden Exception-Typ ausgegeben. Sie ist die eigentliche Fehlerbeschreibung und steht sonst nur in der Kopfzeile des Stacktrace.
		final String message = ohneTemplateQuelltext(exception.getMessage(), templateQuelltexte);
		if (message != null) {
			logger.logLn(loglevel, message);
		}
	}

	private static void logErrorCauses(final Exception exception, final Logger logger, final LogLevel loglevel, final List<String> templateQuelltexte) {
		// Der Abschnitt zeigt die Ursachenkette und beginnt deshalb erst bei der ersten Ursache: Die Meldung der Exception selbst steht bereits im
		// Abschnitt darüber. Ohne Ursache entfällt der Abschnitt, statt eine Überschrift über eine Kopie zu setzen.
		if (exception.getCause() == null) {
			return;
		}
		logger.logLn(loglevel, 0, "### FEHLERGRÜNDE:");
		logger.modifyIndent(4);
		for (Throwable cause = exception.getCause(); cause != null; cause = cause.getCause()) {
			final String message = ohneTemplateQuelltext(cause.getMessage(), templateQuelltexte);
			if ((message != null) && !message.isEmpty()) {
				logger.logLn(loglevel, message);
			}
		}
		logger.modifyIndent(-4);
	}

	private static void logStackTrace(final Exception exception, final Logger logger, final LogLevel loglevel, final List<String> templateQuelltexte) {
		logger.logLn(loglevel, 0, "### STACKTRACE:");
		logger.modifyIndent(4);

		final Writer stringWriter = new StringWriter();
		if (exception.getCause() == null) {
			exception.printStackTrace(new PrintWriter(stringWriter));
		} else {
			exception.getCause().printStackTrace(new PrintWriter(stringWriter));
		}

		final String fullStacktrace = ohneTemplateQuelltext(stringWriter.toString(), templateQuelltexte);

		final BufferedReader reader = new BufferedReader(new StringReader(fullStacktrace));
		reader.lines().forEach(l -> logger.logLn(loglevel, l));

		logger.modifyIndent(-4);
	}

	/**
	 * Sammelt die Template-Quelltexte aus der Ursachenkette eines Fehlers.
	 * <p>Thymeleaf bekommt die Vorlage als Zeichenkette übergeben und führt sie deshalb als Namen des Templates in seinen Meldungen. Eine gescheiterte
	 * Ausgabe trüge damit die ganze Vorlage mehrfach im Log und in der Fehlerantwort. Gesucht wird in der gesamten Kette, denn eine Thymeleaf-Exception
	 * steht selten außen: Der Renderer verpackt sie in eine {@link ApiOperationException}, damit der Status des Abbruchs erhalten bleibt.</p>
	 * <p>Als Quelltext gilt ein Name mit Zeilenumbrüchen, mit Markup oder von erheblicher Länge. Reine Pfadnamen bleiben stehen - der Name eines Fragments
	 * aus dem Klassenpfad ist eine nützliche Angabe zur Fehlerstelle und kein Quelltext.</p>
	 *
	 * @param fehler Der Fehler, dessen Kette durchsucht wird.
	 *
	 * @return Die gefundenen Quelltexte ohne Wiederholungen; eine leere Liste, wenn die Kette keinen trägt.
	 */
	private static List<String> templateQuelltexteInUrsachenkette(final Throwable fehler) {
		final List<String> quelltexte = new ArrayList<>();
		Throwable glied = fehler;
		// Die Kette ist begrenzt, damit eine im Kreis verkettete Ursache diese Suche nicht endlos laufen lässt.
		for (int i = 0; (glied != null) && (i < 20); i++) {
			if ((glied instanceof final TemplateProcessingException tpe) && tpe.hasTemplateName()) {
				final String name = tpe.getTemplateName();
				if (istEingebetteterQuelltext(name) && !quelltexte.contains(name)) {
					quelltexte.add(name);
				}
			}
			glied = glied.getCause();
		}
		return quelltexte;
	}

	/**
	 * Prüft, ob ein Template-Name in Wahrheit der Quelltext der Vorlage ist. Ein spitzes Klammerzeichen entscheidet die Frage allein: Ein Pfad im
	 * Klassenpfad trägt keines, eine Vorlage beginnt damit.
	 *
	 * @param templateName Der Name aus der Thymeleaf-Exception.
	 *
	 * @return true, wenn der Name Quelltext ist, sonst false.
	 */
	private static boolean istEingebetteterQuelltext(final String templateName) {
		return (templateName != null) && (templateName.contains("<") || templateName.contains("\n") || (templateName.length() > 120));
	}

	/**
	 * Ersetzt die eingebetteten Template-Quelltexte in einem Text durch einen Platzhalter.
	 *
	 * @param text               Der Text, der ausgegeben werden soll; darf {@code null} sein.
	 * @param templateQuelltexte Die Quelltexte, die ersetzt werden.
	 *
	 * @return Der Text ohne Quelltexte; {@code null}, wenn kein Text übergeben wurde.
	 */
	private static String ohneTemplateQuelltext(final String text, final List<String> templateQuelltexte) {
		if ((text == null) || templateQuelltexte.isEmpty()) {
			return text;
		}
		String ergebnis = text;
		for (final String quelltext : templateQuelltexte) {
			ergebnis = ergebnis.replace("(template: \"" + quelltext + "\"", "(REMOVED TEMPLATE FROM LOG");
		}
		return ergebnis;
	}


}
