package de.svws_nrw.api;

import java.util.Set;

import de.svws_nrw.api.client.APIClient;
import de.svws_nrw.api.common.OpenAPICorsFilter;
import jakarta.ws.rs.core.Application;


/**
 * Diese Klasse stellt die Ressourcen für den SVWS-Client zur Verfügung, wenn
 * dieser vom SVWS-Server bereitgestellt wird.
 */
public final class RestAppClient extends Application {

	/** Die Pfad-Spezifikation für diese Applikation */
	private static final String[] pathSpec = { "/*" };

	/// Enthält alle Klassen, die für die OpenAPI eingebunden werden
	private final Set<Class<?>> classes = Set.of(
			APIClient.class,
			OpenAPICorsFilter.class);

	/**
	 * Leerer Standardkonstruktor.
	 */
	public RestAppClient() {
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
