package de.svws_nrw.controller.lehrer.einwilligung;

import de.svws_nrw.service.lehrer.einwilligung.LehrerEinwilligungPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Das Interface für den Controller, der Anfragen zu den Einwilligungen von Lehrern entgegennimmt
 * und an den Service delegiert.
 */
public interface LehrerEinwilligungController {

	/**
	 * Liefert alle Einwilligungen eines Lehrers.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die Response mit den Einwilligungen
	 */
	Response getListByIdLehrer(long idLehrer);

	/**
	 * Führt einen Patch für eine Einwilligung eines Lehrers aus.
	 *
	 * @param idLehrer           die ID des Lehrers
	 * @param idEinwilligungsart   die ID der Einwilligungsart
	 * @param patch                der Patch
	 *
	 * @return die Response mit der aktualisierten Einwilligung
	 */
	Response patch(long idLehrer, long idEinwilligungsart, LehrerEinwilligungPatchRequest patch);

}
