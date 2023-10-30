package de.svws_nrw.api;

import java.util.Set;

import de.svws_nrw.api.privileged.APISchemaPrivileged;
import jakarta.ws.rs.core.Application;


/**
 * Diese Applikation stellt die Rest-API für DB-Schema-Zugriffe bereit,
 * welche root-Rechte auf der Datenbank erfordern.
 */
public final class RestAppSchemaRoot extends Application {

	/** Die Pfad-Spezifikation für diese Applikation */
	private static final String[] pathSpec = { "/api/schema/root/*", "/api/schema/create/*", "/api/schema/export/*",
			"/api/schema/import/*", "/api/schema/migrate/*", "/openapi/privileged.json", "/openapi/privileged.yaml" };

	/// Enthält die API-Klassen für diese Applikation
    private final Set<Class<?>> classes = Set.of(
    	OpenAPICorsFilter.class,
    	APISchemaPrivileged.class,
    	OpenApiSchemaRoot.class
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
