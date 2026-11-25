package de.svws_nrw.controller.schule.schuljahresabschnitt;

import jakarta.ws.rs.core.Response;

/**
 * Controller für den lesenden Zugriff auf Schuljahresabschnitte in der externen API (Version 1).
 */
public interface SchuljahresabschnittV1Controller {

	/**
	 * Gibt alle Schuljahresabschnitte zurück, sortiert nach Schuljahr und Abschnitt absteigend (neuester zuerst).
	 *
	 * @return die Response (200 OK) mit der Liste der Schuljahresabschnitte
	 */
	Response getList();

}
