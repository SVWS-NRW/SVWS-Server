package de.svws_nrw.controller.lehrer.stammdaten;

import java.util.List;

import de.svws_nrw.service.lehrer.stammdaten.LehrerStammdatenCreateRequest;
import de.svws_nrw.service.lehrer.stammdaten.LehrerStammdatenPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Der Controller für die Stammdaten von Lehrern.
 */
public interface LehrerStammdatenController {

	/**
	 * Ermittelt die Stammdaten eines Lehrers anhand der ID.
	 *
	 * @param id   die ID des Lehrers
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt die Stammdaten der Lehrer zu den übergebenen IDs.
	 *
	 * @param ids   die IDs der Lehrer
	 *
	 * @return die Response
	 */
	Response getList(List<Long> ids);

	/**
	 * Legt einen neuen Lehrer an.
	 *
	 * @param request   die Daten des neuen Lehrers
	 *
	 * @return die Response
	 */
	Response create(LehrerStammdatenCreateRequest request);

	/**
	 * Führt einen Patch für die Stammdaten eines Lehrers aus.
	 *
	 * @param id        die ID des Lehrers
	 * @param request   der Patch
	 *
	 * @return die Response
	 */
	Response patch(long id, LehrerStammdatenPatchRequest request);

}
