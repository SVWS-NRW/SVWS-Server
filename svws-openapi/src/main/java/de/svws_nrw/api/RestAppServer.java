package de.svws_nrw.api;

import java.util.Set;

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
import de.svws_nrw.api.server.APIKataloge;
import de.svws_nrw.api.server.APIKlassen;
import de.svws_nrw.api.server.APIKurse;
import de.svws_nrw.api.server.APILehrer;
import de.svws_nrw.api.server.APISchema;
import de.svws_nrw.api.server.APISchild;
import de.svws_nrw.api.server.APISchueler;
import de.svws_nrw.api.server.APISchule;
import de.svws_nrw.api.server.APIStundenplan;
import jakarta.ws.rs.core.Application;


/**
 * Diese Klasse stellt die API-Klassen für die OpenAPI-Schnittstelle des Servers
 * zur Verfügung.
 */
public final class RestAppServer extends Application {

	/** Die Pfad-Spezifikation für diese Applikation */
	private static final String[] pathSpec = { "/db/*", "/config/*", "/status/*", "/api/*", "/openapi/server.json", "/openapi/server.yaml" };

	/** Die Pfad-Spezifikationen für diese Applikation, welche auch bei dem Zugriff über andere Ports zur Verfügung stehen sollen */
	private static final String[] pathSpecCommon = { "/config/*", "/status/*" };

	/// Enthält alle Klassen, die für die OpenAPI eingebunden werden
	private final Set<Class<?>> classes = Set.of(
		APIConfig.class,
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
		APIGostKlausuren.class,

		OpenAPICorsFilter.class,
		OpenApiServer.class
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
	 * Gibt die Pfad-Spezifikationen für die App zurück, welche auch über den Zugriff von
	 * anderen Ports zur Verfügung stehen sollen.
	 *
	 * @return die Pfad-Spezifikation
	 */
	public static String[] getPathSpecificationCommon() {
		return pathSpec;
	}


	/**
	 * Prüft, ob der übergebene Pfad in der Pfad-Spezifikation enthalten ist oder nicht
	 *
	 * @param pathSpecs   die Pfad-Spezifikationen
	 * @param path        der zu prüfende Pfad
	 *
	 * @return true, falls er enthalten ist
	 */
	private static boolean checkIsInPathSpecification(final String[] pathSpecs, final String path) {
		if (path == null)
			return false;
		for (final String pathSpec : pathSpecs)
			if (path.equals(pathSpec) || (pathSpec.endsWith("/*") && (path.equals(pathSpec.substring(0, pathSpec.length() - 2))))
					|| (pathSpec.endsWith("*") && (path.startsWith(pathSpec.substring(0, pathSpec.length() - 1)))))
				return true;
		return false;
	}


	/**
	 * Prüft, ob der übergebene Pfad in der Pfad-Spezifikation enthalten ist oder nicht
	 *
	 * @param path   der zu prüfende Pfad
	 *
	 * @return true, falls er enthalten ist
	 */
	public static boolean checkIsInPathSpecification(final String path) {
		return checkIsInPathSpecification(pathSpec, path);
	}


	/**
	 * Prüft, ob der übergebene Pfad in der Pfad-Spezifikation enthalten ist,
	 * welche auch die Pfade spezifiziert, die auch über den Zugriff von
	 * anderen Ports zur Verfügung stehen sollen.
	 *
	 * @param path   der zu prüfende Pfad
	 *
	 * @return true, falls er enthalten ist
	 */
	public static boolean checkIsInPathSpecificationCommon(final String path) {
		return checkIsInPathSpecification(pathSpecCommon, path);
	}

}
