package de.svws_nrw.controller.uv;

import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die gesammelten Daten eines UV-Planungsabschnitts.
 */
public interface UvPlanungsabschnittsdatenBundleController {

	/**
	 * Ermittelt die gesammelten Daten eines UV-Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt die ID des UV-Planungsabschnitts, zu dem die Daten gesucht werden
	 *
	 * @return die Response mit den Daten
	 */
	Response getPlanungsabschnittsdatenBundle(long idPlanungsabschnitt);

}
