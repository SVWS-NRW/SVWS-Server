package de.svws_nrw.api;

import java.util.Set;

import de.svws_nrw.api.common.OpenAPICorsFilter;
import de.svws_nrw.api.external.APILernplattformenV1;
import jakarta.ws.rs.core.Application;


/**
 * Diese Klasse stellt die API-Klassen für die OpenAPI-Schnittstelle des Servers
 * zur Verfügung.
 */
public final class RestAppExternal extends Application {

	/** Die Pfad-Spezifikation für diese Applikation */
	private static final String[] pathSpec = { "/api/external/*", "/openapi/external.json", "/openapi/external.yaml" };

	/// Enthält alle Klassen, die für die OpenAPI eingebunden werden
	private final Set<Class<?>> classes = Set.of(
			APILernplattformenV1.class,
			OpenAPICorsFilter.class,
			OpenApiExternal.class);

	/**
	 * Leerer Standardkonstruktor.
	 */
	public RestAppExternal() {
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
