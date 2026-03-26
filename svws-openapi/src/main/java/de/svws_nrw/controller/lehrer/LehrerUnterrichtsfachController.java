package de.svws_nrw.controller.lehrer;

import de.svws_nrw.service.lehrer.LehrerUnterrichtsfachCreateRequest;
import de.svws_nrw.service.lehrer.LehrerUnterrichtsfachPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die Unterrichtsfächer von Lehrern
 */
public interface LehrerUnterrichtsfachController {

	/**
	 * Ermittelt die Unterrichtsfächer eines Lehrers anhand der Lehrer-ID.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die Response
	 */
	Response getListByLehrerId(long idLehrer);

	/**
	 * Ermittelt ein Unterrichtsfach anhand der ID.
	 *
	 * @param id   die ID des Eintrages
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Erstellt ein neues Unterrichtsfach.
	 *
	 * @param createRequest   die Daten für den neuen Eintrag
	 *
	 * @return die Response
	 */
	Response create(LehrerUnterrichtsfachCreateRequest createRequest);

	/**
	 * Führt auf dem Unterrichtsfach mit der angegebenen ID einen Patch aus.
	 *
	 * @param id      die ID des Eintrages
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(long id, LehrerUnterrichtsfachPatchRequest patch);

	/**
	 * Löscht das Unterrichtsfach mit der angegebenen ID.
	 *
	 * @param id   die ID des Eintrags
	 *
	 * @return die Response
	 */
	Response delete(long id);

}
