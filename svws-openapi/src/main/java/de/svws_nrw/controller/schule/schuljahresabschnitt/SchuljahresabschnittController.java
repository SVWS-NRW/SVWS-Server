package de.svws_nrw.controller.schule.schuljahresabschnitt;

import jakarta.ws.rs.core.Response;

/**
 * Controller für das Anlegen von Schuljahresabschnitten.
 */
public interface SchuljahresabschnittController {

	/**
	 * Legt den Schuljahresabschnitt direkt vor dem ersten vorhandenen Schuljahresabschnitt an.
	 *
	 * @return die Response (201 Created) mit dem angelegten Schuljahresabschnitt
	 */
	Response createVorErstem();

	/**
	 * Legt den Schuljahresabschnitt direkt nach dem letzten vorhandenen Schuljahresabschnitt an.
	 *
	 * @return die Response (201 Created) mit dem angelegten Schuljahresabschnitt
	 */
	Response createNachLetztem();

}
