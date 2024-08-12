package de.svws_nrw.base.slf4j;

import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.LegacyAbstractLogger;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;

/**
 * Die Adapter-Klasse für das Umleiten des SLF4J-Loggings auf das Logging des SVWS-Servers
 */
public final class SvwsSlf4jLoggerAdapter extends LegacyAbstractLogger {

	private static final long serialVersionUID = 7876756220542311972L;

	// Der SVWS-Logger
	private final Logger _logger;


	/**
	 * Erstellt einen neuen Adapter für SLF4J zum Logging des SVWS-Servers
	 *
	 * @param name    der Name des SLF4J-Loggers
	 */
	SvwsSlf4jLoggerAdapter(final String name) {
		this.name = name;
		_logger = Logger.global();
	}

	@Override
	public boolean isTraceEnabled() {
		return _logger.getDefaultLevel().compareTo(LogLevel.DEBUG) >= 0;
	}

	@Override
	public boolean isDebugEnabled() {
		return _logger.getDefaultLevel().compareTo(LogLevel.DEBUG) >= 0;
	}

	@Override
	public boolean isInfoEnabled() {
		return _logger.getDefaultLevel().compareTo(LogLevel.INFO) >= 0;
	}

	@Override
	public boolean isWarnEnabled() {
		return _logger.getDefaultLevel().compareTo(LogLevel.WARNING) >= 0;
	}

	@Override
	public boolean isErrorEnabled() {
		return _logger.getDefaultLevel().compareTo(LogLevel.ERROR) >= 0;
	}

	@Override
	protected String getFullyQualifiedCallerName() {
		return SvwsSlf4jLoggerAdapter.class.getName();
	}

	@Override
	protected void handleNormalizedLoggingCall(final Level level, final Marker marker, final String messagePattern, final Object[] arguments,
			final Throwable throwable) {
		String message = messagePattern;
		if (arguments != null)
			for (final Object arg : arguments)
				message = message.replaceFirst("\\{\\}", (arg == null) ? "null" : arg.toString());
		final LogLevel logLevel = switch (level) {
			case ERROR -> LogLevel.ERROR;
			case WARN -> LogLevel.WARNING;
			case INFO -> LogLevel.INFO;
			case DEBUG -> LogLevel.DEBUG;
			case TRACE -> LogLevel.DEBUG;
		};
		_logger.logLn(logLevel, name + " - " + message);
	}

}
