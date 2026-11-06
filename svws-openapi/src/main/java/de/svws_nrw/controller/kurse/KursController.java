package de.svws_nrw.controller.kurse;

import jakarta.ws.rs.core.Response;

/**
 * Der Controller für den lesenden Zugriff auf Kurse.
 */
public interface KursController {

	/**
	 * Ermittelt die Daten eines Kurses einschließlich der Schüler anhand der ID.
	 *
	 * @param id   die ID des Kurses
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt die Kurse eines Schuljahresabschnitts. Ist der Abschnitt null, so werden die Kurse aller
	 * Abschnitte ermittelt.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts oder null
	 *
	 * @return die Response
	 */
	Response getListBySchuljahresabschnitt(Long idSchuljahresabschnitt);

}
