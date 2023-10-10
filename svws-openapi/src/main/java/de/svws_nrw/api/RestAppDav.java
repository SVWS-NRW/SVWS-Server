package de.svws_nrw.api;

import java.util.Set;

import de.svws_nrw.api.dav.APIAdressbuch;
import de.svws_nrw.api.dav.APIKalender;
import jakarta.ws.rs.core.Application;


/**
 * Diese Klasse stellt die API-Klassen für die OpenAPI-Schnittstelle des Servers
 * zur Verfügung.
 */
public final class RestAppDav extends Application {

	/// Enthält alle Klassen, die für die OpenAPI eingebunden werden
	private final Set<Class<?>> classes = Set.of(
		APIAdressbuch.class,
		APIKalender.class,

		OpenAPICorsFilter.class
	);

	@Override
	public Set<Class<?>> getClasses() {
		return this.classes;
	}

}
