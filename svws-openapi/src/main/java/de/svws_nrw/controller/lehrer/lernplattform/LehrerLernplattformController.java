package de.svws_nrw.controller.lehrer.lernplattform;

import de.svws_nrw.service.lehrer.lernplattform.LehrerLernplattformPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Das Interface für den Controller, der Anfragen zu den Lernplattform-Zuordnungen von Lehrern entgegennimmt
 * und an den Service delegiert.
 */
public interface LehrerLernplattformController {

	/**
	 * Liefert alle Lernplattform-Zuordnungen eines Lehrers.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die Response mit den Zuordnungen
	 */
	Response getListByIdLehrer(long idLehrer);

	/**
	 * Führt einen Patch für die Einwilligungen einer Lernplattform-Zuordnung aus.
	 *
	 * @param idLehrer        die ID des Lehrers
	 * @param idLernplattform   die ID der Lernplattform
	 * @param patch             der Patch
	 *
	 * @return die Response mit der aktualisierten Zuordnung
	 */
	Response patch(long idLehrer, long idLernplattform, LehrerLernplattformPatchRequest patch);

}
