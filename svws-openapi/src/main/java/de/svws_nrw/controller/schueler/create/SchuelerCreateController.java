package de.svws_nrw.controller.schueler.create;

import de.svws_nrw.service.schueler.create.SchuelerCreateRequest;
import jakarta.ws.rs.core.Response;

/**
 * Controller für das Neuanlegen eines Schülers.
 */
public interface SchuelerCreateController {

	/**
	 * Legt einen neuen Schüler mit Lernabschnitt, Einwilligungen und Lernplattformen an.
	 *
	 * @param dto   die Daten des neuen Schülers
	 *
	 * @return die Response (201 Created) mit den Stammdaten des angelegten Schülers
	 */
	Response create(SchuelerCreateRequest dto);

}
