package de.svws_nrw.controller.schueler.lernplattform;

import de.svws_nrw.service.schueler.lernplattform.SchuelerLernplattformPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Das Interface für den Controller, der Anfragen zu den Lernplattform-Zuordnungen von Schülern entgegennimmt
 * und an den Service delegiert.
 */
public interface SchuelerLernplattformController {

	/**
	 * Liefert alle Lernplattform-Zuordnungen eines Schülers.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Response mit den Zuordnungen
	 */
	Response getListByIdSchueler(long idSchueler);

	/**
	 * Führt einen Patch für die Einwilligungen einer Lernplattform-Zuordnung aus.
	 *
	 * @param idSchueler        die ID des Schülers
	 * @param idLernplattform   die ID der Lernplattform
	 * @param patch             der Patch
	 *
	 * @return die Response mit der aktualisierten Zuordnung
	 */
	Response patch(long idSchueler, long idLernplattform, SchuelerLernplattformPatchRequest patch);

}
