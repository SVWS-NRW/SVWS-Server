package de.svws_nrw.api;

import java.util.Set;

import de.svws_nrw.api.debug.APIDebug;
import jakarta.ws.rs.core.Application;


/**
 * Diese Applikation stellt die Dateien der Swagger-UI zur Verfügung und
 * verwendet die openapi.json vom SVWS-Server.
 */
public final class RestAppDebug extends Application {

	/** Die Pfad-Spezifikation für diese Applikation */
	private static final String[] pathSpec = { "/debug/*" };

	/// Enthält die API-Klasse für den Zugriff auf die Swagger-UI-Dateien
    private final Set<Class<?>> classes = Set.of(
    	OpenAPICorsFilter.class,
    	APIDebug.class
    );

	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}

	/**
	 * Gibt die Pfad-Spezifikation für die App zurück
	 *
	 * @return die Pfad-Spezifikation
	 */
	public static String[] getPathSpecification() {
		return pathSpec;
	}

	/**
	 * Prüft, ob der übergebene Pfad in der Pfad-Spezifikation enthalten ist oder nicht
	 *
	 * @param path   der zu prüfende Pfad
	 *
	 * @return true, falls er enthalten ist
	 */
	public static boolean checkIsInPathSpecification(final String path) {
		if (path == null)
			return false;
		for (final String pathSpec : pathSpec)
			if (path.equals(pathSpec) || (pathSpec.endsWith("/*") && (path.equals(pathSpec.substring(0, pathSpec.length() - 2))))
					|| (pathSpec.endsWith("*") && (path.startsWith(pathSpec.substring(0, pathSpec.length() - 1)))))
				return true;
		return false;
	}

}
