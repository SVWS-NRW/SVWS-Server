package de.svws_nrw.base.slf4j;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMDCAdapter;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

/**
 * Der Service-Provider für das Abfangen des SLF4J-Loggings
 */
public final class SvwsSlf4jServiceProvider implements SLF4JServiceProvider {

	// Die API-Version von SLF4J gegen welcher programmiert wurde
	public static String REQUESTED_API_VERSION = "2.0.7";

	private ILoggerFactory _loggerFactory;
	private IMarkerFactory _markerFactory;
	private MDCAdapter _mdcAdapter;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SvwsSlf4jServiceProvider() {
		// leer
	}

	@Override
	public ILoggerFactory getLoggerFactory() {
		return _loggerFactory;
	}

	@Override
	public IMarkerFactory getMarkerFactory() {
		return _markerFactory;
	}

	@Override
	public MDCAdapter getMDCAdapter() {
		return _mdcAdapter;
	}

	@Override
	public String getRequestedApiVersion() {
		return REQUESTED_API_VERSION;
	}

	@Override
	public void initialize() {
		_loggerFactory = new SvwsSlf4jLoggerFactory();
		_markerFactory = new BasicMarkerFactory();
		_mdcAdapter = new BasicMDCAdapter();
	}

}
