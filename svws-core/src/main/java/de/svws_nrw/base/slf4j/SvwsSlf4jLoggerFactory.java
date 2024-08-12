package de.svws_nrw.base.slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/**
 * Die Logger-Factory für das Umleiten des SLF4J-Loggings auf das Logging des SVWS-Servers.
 */
public final class SvwsSlf4jLoggerFactory implements ILoggerFactory {

	// Eine Map mit den einzelnen SLF4J-Loggern und ihren Namen.
	private final ConcurrentMap<String, Logger> _mapLogger = new ConcurrentHashMap<>();

	@Override
	public Logger getLogger(final String name) {
		return _mapLogger.computeIfAbsent(name, k -> new SvwsSlf4jLoggerAdapter(name));
	}

}
