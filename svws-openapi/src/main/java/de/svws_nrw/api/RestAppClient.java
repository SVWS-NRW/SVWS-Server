package de.svws_nrw.api;

import java.util.Set;

import de.svws_nrw.api.client.APIClient;
import jakarta.ws.rs.core.Application;


/**
 * Diese Klasse stellt die Ressourcen für den SVWS-Client zur Verfügung, wenn
 * dieser vom SVWS-Server bereitgestellt wird.
 */
public final class RestAppClient extends Application {

	/// Enthält alle Klassen, die für die OpenAPI eingebunden werden
	private final Set<Class<?>> classes = Set.of(
		APIClient.class,
		OpenAPICorsFilter.class
	);

	@Override
	public Set<Class<?>> getClasses() {
		return this.classes;
	}

}
