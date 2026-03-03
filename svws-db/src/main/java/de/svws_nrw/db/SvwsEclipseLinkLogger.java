package de.svws_nrw.db;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;

/**
 * Lenkt das Logging von Eclipse-Link in das globale Log des SVWS-Servers um.
 */
public final class SvwsEclipseLinkLogger extends AbstractSessionLog {

	@Override
	public void log(final SessionLogEntry entry) {
		if (!shouldLog(entry.getLevel(), entry.getNameSpace())) {
			return;
		}

		final LogLevel level = switch (entry.getLevel()) {
			case SEVERE -> LogLevel.ERROR;
			case WARNING -> LogLevel.WARNING;
			case INFO, CONFIG -> LogLevel.INFO;
			default -> LogLevel.DEBUG;
		};

		final StringBuilder sb = new StringBuilder();
		sb.append("EclipseLink [").append(entry.getNameSpace()).append("]: ");
		sb.append(formatMessage(entry));

		// SQL-Parameter hinzufügen, falls vorhanden
		if (entry.getParameters() != null && entry.getParameters().length > 0) {
			sb.append(" | Parameter: ");
			for (int i = 0; i < entry.getParameters().length; i++) {
				final Object param = entry.getParameters()[i];
				sb.append("[").append(i).append("] ").append(param).append(" ");
			}
		}

		// Exception ausgeben
		if (entry.getException() != null) {
			sb.append("\nException: ").append(entry.getException().toString());
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw);
			entry.getException().printStackTrace(pw);
			sb.append("\n").append(sw.toString());
		}

		Logger.global().logLn(level, sb.toString());
	}
}
