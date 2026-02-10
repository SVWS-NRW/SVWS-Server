package de.svws_nrw.data.statistik;

import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für die Handhabung der der API-Zugriffe im Bereich der amtlichen Schulstatistik.
 */
public interface StatistikController {

	/**
	 * Aggregiert die Statistik-Relevanten Daten aus der SVWS-DB und gibt diese
	 * als Response zurück. Dabei wird zunächst geprüft, ob der Benutzer die
	 * notwendigen Berechtigungen hat.
	 *
	 * @return die Response
	 */
	Response getStatistikGesamt();

}
