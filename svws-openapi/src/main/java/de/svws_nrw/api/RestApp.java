package de.svws_nrw.api;

import java.util.Set;

import de.svws_nrw.api.client.APISVWSClient;
import de.svws_nrw.api.server.APIAdressbuch;
import de.svws_nrw.api.server.APIAlgoGesamtschuleAbschluss;
import de.svws_nrw.api.server.APIAlgoGostAbschluss;
import de.svws_nrw.api.server.APIBenutzer;
import de.svws_nrw.api.server.APIBetrieb;
import de.svws_nrw.api.server.APIClientConfig;
import de.svws_nrw.api.server.APIConfig;
import de.svws_nrw.api.server.APIDatabase;
import de.svws_nrw.api.server.APIENM;
import de.svws_nrw.api.server.APIErzieher;
import de.svws_nrw.api.server.APIFaecher;
import de.svws_nrw.api.server.APIGesamtschule;
import de.svws_nrw.api.server.APIGost;
import de.svws_nrw.api.server.APIGostDatenaustausch;
import de.svws_nrw.api.server.APIGostKlausuren;
import de.svws_nrw.api.server.APIGostKursplanung;
import de.svws_nrw.api.server.APIJahrgaenge;
import de.svws_nrw.api.server.APIKAOA;
import de.svws_nrw.api.server.APIKalender;
import de.svws_nrw.api.server.APIKataloge;
import de.svws_nrw.api.server.APIKlassen;
import de.svws_nrw.api.server.APIKurse;
import de.svws_nrw.api.server.APILehrer;
import de.svws_nrw.api.server.APISchema;
import de.svws_nrw.api.server.APISchild;
import de.svws_nrw.api.server.APISchueler;
import de.svws_nrw.api.server.APISchule;
import de.svws_nrw.api.server.APIStundenplan;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;


/**
 * Diese Klasse dient als Grundlage für einen OpenAPI-Server und dient der Initialisierung
 * der OpenAPI-Schnittstelle auf Basis der zugeordneten OpenAPI-Klassen. Zunächst können
 * OpenAPI-Klassen über die Methode addAPI hinzugefügt werden, bevor das
 * Servlet später mit dem zugehörigen Init-Parameter (jakarta.ws.rs.Application)
 * und dieser Klasse initialisiert wird.
 */
@ApplicationPath("/")
public final class RestApp extends Application {

	/// Enthält alle Klassen, die für die OpenAPI eingebunden werden
	private final Set<Class<?>> classes = Set.of(
		APIConfig.class,
		APISVWSClient.class,
		APIAlgoGesamtschuleAbschluss.class,
		APIAlgoGostAbschluss.class,
		APIBenutzer.class,
		APIClientConfig.class,
		APISchule.class,
		APIJahrgaenge.class,
		APIKlassen.class,
		APIKurse.class,
		APIFaecher.class,
		APISchueler.class,
		APIGesamtschule.class,
		APIGostDatenaustausch.class,
		APIGost.class,
		APIGostKursplanung.class,
		APILehrer.class,
		APIErzieher.class,
		APIBetrieb.class,
		APIKataloge.class,
		APISchema.class,
		APIDatabase.class,
		APIStundenplan.class,
		APISchild.class,
		APIKAOA.class,
		APIENM.class,
		APIAdressbuch.class,
		APIKalender.class,
		APIGostKlausuren.class,

		OpenAPICorsFilter.class,
		OpenApiMain.class
	);

	@Override
	public Set<Class<?>> getClasses() {
		return this.classes;
	}

}
