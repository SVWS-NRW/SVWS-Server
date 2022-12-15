package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

/**
 * Eine einfacher Implementierung des StaticLoggerBinder für den SVWS-Server 
 * zur Unterstützung von SLF4J.
 */
public class StaticLoggerBinder implements LoggerFactoryBinder {

	/** Die Instanz dieser Binding-Klasse */
    private static final StaticLoggerBinder _instance = new StaticLoggerBinder();

    /** Die API-Version von SLF4J für welche diese Klasse implementiert wurde */
    public static String REQUESTED_API_VERSION = "1.7";

    /** Die Factory-Instanz für das Logging */
    private final SVWSLoggerFactory _factory = new SVWSLoggerFactory();


    /**
     * Gibt die Singleton-Instanz dieser Binding-Klasse zurück.
     *
     * @return die Instanz
     */
    public static final StaticLoggerBinder getSingleton() {
        return _instance;
    }


    @Override
	public ILoggerFactory getLoggerFactory() {
        return _factory;
    }


    @Override
	public String getLoggerFactoryClassStr() {
        return SVWSLoggerFactory.class.getName();
    }

}
