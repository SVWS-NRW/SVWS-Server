package de.svws_nrw.controller.schueler.einwilligung;

import de.svws_nrw.service.schueler.einwilligung.SchuelerEinwilligungPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Das Interface für den Controller, der Anfragen zu den Einwilligungen von Schülern entgegennimmt
 * und an den Service delegiert.
 */
public interface SchuelerEinwilligungController {

	/**
	 * Liefert alle Einwilligungen eines Schülers.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Response mit den Einwilligungen
	 */
	Response getListByIdSchueler(long idSchueler);

	/**
	 * Führt einen Patch für eine Einwilligung eines Schülers aus.
	 *
	 * @param idSchueler           die ID des Schülers
	 * @param idEinwilligungsart   die ID der Einwilligungsart
	 * @param patch                der Patch
	 *
	 * @return die Response mit der aktualisierten Einwilligung
	 */
	Response patch(long idSchueler, long idEinwilligungsart, SchuelerEinwilligungPatchRequest patch);

}
