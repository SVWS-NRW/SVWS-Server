package org.slf4j.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/**
 * Die Logger-Factory für den SVWS-Server zur Unterstützung
 * von SLF4J.
 */
public final class SVWSLoggerFactory implements ILoggerFactory {

	private final Map<String, SVWSLoggerAdapter> logger = new HashMap<>();

	@Override
	public Logger getLogger(final String name) {
		synchronized (logger) {
			SVWSLoggerAdapter adapter = logger.get(name);
            if (adapter == null) {
            	adapter = new SVWSLoggerAdapter(name);
            	logger.put(name, adapter);
            }
            return logger.get(name);
        }
    }

}
