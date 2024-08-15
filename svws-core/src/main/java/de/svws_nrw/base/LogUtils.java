package de.svws_nrw.base;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Hilfsmethoden für das Logging des SVWS-Servers
 * zur Verfügung, welche nicht transpiliert werden.
 */
public final class LogUtils {

	private LogUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}

	/**
	 * Schreibt den Stacktrace der übergebenen Exception in den übergebenen Logger.
	 *
	 * @param logger   der Logger
	 * @param e   die Exception
	 */
	public static void logStacktrace(final @NotNull Logger logger, final @NotNull Exception e) {
		try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
			e.printStackTrace(pw);
			sw.toString().lines().forEach(logger::logLn);
		} catch (@SuppressWarnings("unused") final IOException ioe) {
			// do nothing
		}
	}

}
