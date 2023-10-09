package de.svws_nrw.api;

import java.util.Set;

import de.svws_nrw.api.schema.APISchemaRoot;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;


/**
 * Diese Applikation stellt die Rest-API für DB-Schema-Zugriffe bereit,
 * welche root-Rechte auf der Datenbank erfordern.
 */
@ApplicationPath("/api/schema/root")
public final class RestAppSchemaRoot extends Application {

	/// Enthält die API-Klassen für diese Applikation
    private final Set<Class<?>> classes = Set.of(
    	OpenAPICorsFilter.class,
    	APISchemaRoot.class,
    	OpenApiSchemaRoot.class
    );

	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}

}
