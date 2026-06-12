package de.svws_nrw.api;

import java.util.Set;

import de.svws_nrw.api.client.APIApps;
import de.svws_nrw.api.common.ApiOperationExceptionMapper;
import de.svws_nrw.api.common.GenericExceptionMapper;
import de.svws_nrw.api.common.OpenAPICorsFilter;
import de.svws_nrw.api.common.PathUtils;
import jakarta.ws.rs.core.Application;


/**
 * Diese Klasse stellt die Ressourcen für die SVWS-Apps zur Verfügung, wenn
 * diese vom SVWS-Server bereitgestellt werden.
 */
public final class RestAppApps extends Application {

	/** Die Pfad-Spezifikation für diese Applikation */
	private static final String[] pathSpec = { "/app/*" };

	/** Enthält alle Klassen, die für die OpenAPI eingebunden werden */
	private final Set<Class<?>> classes = Set.of(
			APIApps.class,
			ApiOperationExceptionMapper.class,
			GenericExceptionMapper.class,
			OpenAPICorsFilter.class);

	/**
	 * Leerer Standardkonstruktor.
	 */
	public RestAppApps() {
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
