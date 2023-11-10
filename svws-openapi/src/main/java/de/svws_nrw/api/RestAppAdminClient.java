package de.svws_nrw.api;

import java.util.Set;

import de.svws_nrw.api.client.APIAdminClient;
import jakarta.ws.rs.core.Application;


/**
 * Diese Klasse stellt die Ressourcen für den SVWS-Client zur Verfügung, wenn
 * dieser vom SVWS-Server bereitgestellt wird.
 */
public final class RestAppAdminClient extends Application {

	/** Die Pfad-Spezifikation für diese Applikation */
	private static final String[] pathSpec = { "/admin/*" };

	/// Enthält alle Klassen, die für die OpenAPI eingebunden werden
	private final Set<Class<?>> classes = Set.of(
		APIAdminClient.class,
		OpenAPICorsFilter.class
	);

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
