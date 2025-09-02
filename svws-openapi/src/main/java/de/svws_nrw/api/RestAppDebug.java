package de.svws_nrw.api;

import java.util.Set;

import de.svws_nrw.api.common.OpenAPICorsFilter;
import de.svws_nrw.api.common.PathUtils;
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
			APIDebug.class);

	/**
	 * Leerer Standardkonstruktor.
	 */
	public RestAppDebug() {
		// leer
	}

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
		return PathUtils.checkIsInPathSpecification(pathSpec, path);
	}

}
