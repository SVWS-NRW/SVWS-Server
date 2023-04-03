package org.slf4j.impl;

import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * Eine einfacher Logger für den SVWS-Server zur Unterstützung
 * von SLF4J.
 */
public final class SVWSLoggerAdapter implements Logger {

	private final String _name;

	/** Gibt an, ob Meldungen der Level WARN auf dem Standard-Error- oder dem Standard-Output-Stream ausgegeben werden */
	private final boolean _mapWarnToError = false;

	/** Werden Fehlermeldungen geloggt? */
	private final boolean _enabledError = true;

	/** Werden Warnungen geloggt? */
	private final boolean _enabledWarn = false;

	/** Werden Informationen geloggt? */
	private final boolean _enabledInfo = false;

	/** Werden Meldungen zum Debuggen geloggt? */
	private final boolean _enabledDebug = false;

	/** Werden feinere Meldungen zum Debugging / Tracing geloggt? */
	private final boolean _enabledTrace = false;



	/**
	 * Erstellt einen Logger mit dem übergebenen Namen.
	 *
	 * @param name   der Name des Loggers
	 */
	public SVWSLoggerAdapter(final String name) {
		this._name = name;
	}


	@Override
	public String getName() {
		return _name;
	}


	/**
	 * Loggt die Nachricht auf dem Standard-Error-Stream der Anwendung
	 *
	 * @param msg   die zu loggende Nachricht
	 */
	private static void _logStandardError(final String msg) {
		System.err.println(msg);
	}


	/**
	 * Loggt die Nachricht auf dem Standard-Output-Stream der Anwendung
	 *
	 * @param msg   die zu loggende Nachricht
	 */
	private static void _logStandardOutput(final String msg) {
		System.err.println(msg);
	}


	/**
	 * Ersetzt die Stellen mit "{}" durch die übergebenen Argumente
	 *
	 * @param msg         die Nachricht
	 * @param arguments   die zu ersetzenden Argumente
	 *
	 * @return der String mit den Ersetzungen
	 */
	private static String _format(final String msg, final Object... arguments) {
		String result = msg;
		for (final Object arg : arguments)
			result = result.replaceFirst("\\{\\}", Matcher.quoteReplacement(arg.toString()));
		return result;
	}


	@Override
	public boolean isTraceEnabled() {
		return _enabledTrace;
	}

	@Override
	public void trace(final String msg) {
		if (_enabledTrace)
			_logStandardOutput(msg);
	}

	@Override
	public void trace(final String format, final Object arg) {
		if (_enabledTrace)
			_logStandardOutput(_format(format, arg));
	}

	@Override
	public void trace(final String format, final Object arg1, final Object arg2) {
		if (_enabledTrace)
			_logStandardOutput(_format(format, arg1, arg2));
	}

	@Override
	public void trace(final String format, final Object... arguments) {
		if (_enabledTrace)
			_logStandardOutput(_format(format, arguments));
	}

	@Override
	public void trace(final String msg, final Throwable t) {
		if (_enabledTrace)
			_logStandardOutput(msg + "\n" + t.toString());
	}

	@Override
	public boolean isTraceEnabled(final Marker marker) {
		return isTraceEnabled();  // ignore marker
	}

	@Override
	public void trace(final Marker marker, final String msg) {
		trace(msg);  // ignore marker
	}

	@Override
	public void trace(final Marker marker, final String format, final Object arg) {
		trace(format, arg);  // ignore marker
	}

	@Override
	public void trace(final Marker marker, final String format, final Object arg1, final Object arg2) {
		trace(format, arg1, arg2);  // ignore marker
	}

	@Override
	public void trace(final Marker marker, final String format, final Object... argArray) {
		trace(format, argArray);  // ignore marker
	}

	@Override
	public void trace(final Marker marker, final String msg, final Throwable t) {
		trace(msg, t);  // ignore marker
	}

	@Override
	public boolean isDebugEnabled() {
		return _enabledDebug;
	}

	@Override
	public void debug(final String msg) {
		if (_enabledDebug)
			_logStandardOutput(msg);
	}

	@Override
	public void debug(final String format, final Object arg) {
		if (_enabledDebug)
			_logStandardOutput(_format(format, arg));
	}

	@Override
	public void debug(final String format, final Object arg1, final Object arg2) {
		if (_enabledDebug)
			_logStandardOutput(_format(format, arg1, arg2));
	}

	@Override
	public void debug(final String format, final Object... arguments) {
		if (_enabledDebug)
			_logStandardOutput(_format(format, arguments));
	}

	@Override
	public void debug(final String msg, final Throwable t) {
		if (_enabledDebug)
			_logStandardOutput(msg + "\n" + t.toString());
	}

	@Override
	public boolean isDebugEnabled(final Marker marker) {
		return isDebugEnabled();  // ignore marker
	}

	@Override
	public void debug(final Marker marker, final String msg) {
		debug(msg);  // ignore marker
	}

	@Override
	public void debug(final Marker marker, final String format, final Object arg) {
		debug(format, arg);  // ignore marker
	}

	@Override
	public void debug(final Marker marker, final String format, final Object arg1, final Object arg2) {
		debug(format, arg1, arg2);  // ignore marker
	}

	@Override
	public void debug(final Marker marker, final String format, final Object... arguments) {
		debug(format, arguments);  // ignore marker
	}

	@Override
	public void debug(final Marker marker, final String msg, final Throwable t) {
		debug(msg, t);  // ignore marker
	}

	@Override
	public boolean isInfoEnabled() {
		return _enabledInfo;
	}

	@Override
	public void info(final String msg) {
		if (_enabledInfo)
			_logStandardOutput(msg);
	}

	@Override
	public void info(final String format, final Object arg) {
		if (_enabledInfo)
			_logStandardOutput(_format(format, arg));
	}

	@Override
	public void info(final String format, final Object arg1, final Object arg2) {
		if (_enabledInfo)
			_logStandardOutput(_format(format, arg1, arg2));
	}

	@Override
	public void info(final String format, final Object... arguments) {
		if (_enabledInfo)
			_logStandardOutput(_format(format, arguments));
	}

	@Override
	public void info(final String msg, final Throwable t) {
		if (_enabledInfo)
			_logStandardOutput(msg + "\n" + t.toString());
	}

	@Override
	public boolean isInfoEnabled(final Marker marker) {
		return isInfoEnabled();  // ignore marker
	}

	@Override
	public void info(final Marker marker, final String msg) {
		info(msg);  // ignore marker
	}

	@Override
	public void info(final Marker marker, final String format, final Object arg) {
		info(format, arg);  // ignore marker
	}

	@Override
	public void info(final Marker marker, final String format, final Object arg1, final Object arg2) {
		info(format, arg1, arg2);  // ignore marker
	}

	@Override
	public void info(final Marker marker, final String format, final Object... arguments) {
		info(format, arguments);  // ignore marker
	}

	@Override
	public void info(final Marker marker, final String msg, final Throwable t) {
		info(msg, t);  // ignore marker
	}

	@Override
	public boolean isWarnEnabled() {
		return _enabledWarn;
	}

	@Override
	public void warn(final String msg) {
		if (!_enabledWarn)
			return;
		if (_mapWarnToError)
			_logStandardError(msg);
		else
			_logStandardOutput(msg);
	}

	@Override
	public void warn(final String format, final Object arg) {
		if (!_enabledWarn)
			return;
		if (_mapWarnToError)
			_logStandardError(_format(format, arg));
		else
			_logStandardOutput(_format(format, arg));
	}

	@Override
	public void warn(final String format, final Object... arguments) {
		if (!_enabledWarn)
			return;
		if (_mapWarnToError)
			_logStandardError(_format(format, arguments));
		else
			_logStandardOutput(_format(format, arguments));
	}

	@Override
	public void warn(final String format, final Object arg1, final Object arg2) {
		if (!_enabledWarn)
			return;
		if (_mapWarnToError)
			_logStandardError(_format(format, arg1, arg2));
		else
			_logStandardOutput(_format(format, arg1, arg2));
	}

	@Override
	public void warn(final String msg, final Throwable t) {
		if (!_enabledWarn)
			return;
		if (_mapWarnToError)
			_logStandardError(msg + "\n" + t.toString());
		else
			_logStandardOutput(msg + "\n" + t.toString());
	}

	@Override
	public boolean isWarnEnabled(final Marker marker) {
		return isWarnEnabled();  // ignore marker
	}

	@Override
	public void warn(final Marker marker, final String msg) {
		warn(msg);  // ignore marker
	}

	@Override
	public void warn(final Marker marker, final String format, final Object arg) {
		warn(format, arg);  // ignore marker
	}

	@Override
	public void warn(final Marker marker, final String format, final Object arg1, final Object arg2) {
		warn(format, arg1, arg2);  // ignore marker
	}

	@Override
	public void warn(final Marker marker, final String format, final Object... arguments) {
		warn(format, arguments);  // ignore marker
	}

	@Override
	public void warn(final Marker marker, final String msg, final Throwable t) {
		warn(msg, t);  // ignore marker
	}

	@Override
	public boolean isErrorEnabled() {
		return _enabledError;
	}

	@Override
	public void error(final String msg) {
		if (!_enabledError)
			return;
		_logStandardError(msg);
	}

	@Override
	public void error(final String format, final Object arg) {
		if (!_enabledError)
			return;
		_logStandardError(_format(format, arg));
	}

	@Override
	public void error(final String format, final Object arg1, final Object arg2) {
		if (!_enabledError)
			return;
		_logStandardError(_format(format, arg1, arg2));
	}

	@Override
	public void error(final String format, final Object... arguments) {
		if (!_enabledError)
			return;
		_logStandardError(_format(format, arguments));
	}

	@Override
	public void error(final String msg, final Throwable t) {
		if (!_enabledError)
			return;
		_logStandardError(msg + "\n" + t.toString());
	}

	@Override
	public boolean isErrorEnabled(final Marker marker) {
		return isErrorEnabled();  // ignore marker
	}

	@Override
	public void error(final Marker marker, final String msg) {
		error(msg);  // ignore marker
	}

	@Override
	public void error(final Marker marker, final String format, final Object arg) {
		error(format, arg);  // ignore marker
	}

	@Override
	public void error(final Marker marker, final String format, final Object arg1, final Object arg2) {
		error(format, arg1, arg2);  // ignore marker
	}

	@Override
	public void error(final Marker marker, final String format, final Object... arguments) {
		error(format, arguments);  // ignore marker
	}

	@Override
	public void error(final Marker marker, final String msg, final Throwable t) {
		error(msg, t);  // ignore marker
	}

}
