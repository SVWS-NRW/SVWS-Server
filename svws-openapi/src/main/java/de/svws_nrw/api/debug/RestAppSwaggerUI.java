package de.svws_nrw.api.debug;

import java.util.Set;

import de.svws_nrw.api.OpenAPICorsFilter;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;


/**
 * Diese Applikation stellt die Dateien der Swagger-UI zur Verfügung und
 * verwendet die openapi.json vom SVWS-Server.
 */
@ApplicationPath("/debug")
public final class RestAppSwaggerUI extends Application {

	/// Enthält die API-Klasse für den Zugriff auf die Swagger-UI-Dateien
    private final Set<Class<?>> classes = Set.of(
    	OpenAPICorsFilter.class,
    	APIDebug.class
    );

	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}

}
