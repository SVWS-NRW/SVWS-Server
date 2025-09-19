package de.svws_nrw.api;

import java.util.Set;

import de.svws_nrw.api.common.OpenAPICorsFilter;
import de.svws_nrw.api.dav.APIDav;
import jakarta.ws.rs.core.Application;


/**
 * Diese Klasse stellt die API-Klassen für die OpenAPI-Schnittstelle des Servers
 * zur Verfügung.
 */
public final class RestAppDav extends Application {

	/** Die Pfad-Spezifikation für diese Applikation */
	private static final String[] pathSpec = { "/dav/*" };

	/// Enthält alle Klassen, die für die OpenAPI eingebunden werden
	private final Set<Class<?>> classes = Set.of(
			APIDav.class,
			OpenAPICorsFilter.class);

	/**
	 * Leerer Standardkonstruktor.
	 */
	public RestAppDav() {
		// leer
	}

	@Override
	public Set<Class<?>> getClasses() {
		return this.classes;
	}


	/**
	 * Gibt die Pfad-Spezifikation für die App zurück
	 *
	 * @return die Pfad-Spezifikation
	 */
	public static String[] getPathSpecification() {
		return pathSpec;
	}

}
