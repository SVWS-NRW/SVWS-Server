package de.svws_nrw.controller.uv.lehrer;

import java.util.Collection;

import de.svws_nrw.service.uv.lehrer.UvLehrerUnterrichtsfachCreateRequest;
import de.svws_nrw.service.uv.lehrer.UvLehrerUnterrichtsfachPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die Unterrichtsfächer von UV-Lehrkräften.
 */
public interface UvLehrerUnterrichtsfachController {

	/**
	 * Ermittelt die Unterrichtsfächer eines UV-Lehrers anhand der Lehrer-ID.
	 *
	 * @param idLehrer   die ID des UV-Lehrers
	 *
	 * @return die Response
	 */
	Response getListByLehrerId(long idLehrer);

	/**
	 * Ermittelt die Unterrichtsfächer für mehrere UV-Lehrer anhand ihrer IDs.
	 *
	 * @param idsLehrer   die IDs der UV-Lehrer
	 *
	 * @return die Response
	 */
	Response getListByLehrerIds(Collection<Long> idsLehrer);

	/**
	 * Ermittelt die Unterrichtsfächer für mehrere UV-Lehrer anhand ihrer IDs, gruppiert nach Lehrer-ID.
	 *
	 * @param idsLehrer   die IDs der UV-Lehrer
	 *
	 * @return die Response
	 */
	Response getMapByLehrerIds(Collection<Long> idsLehrer);

	/**
	 * Ermittelt ein Unterrichtsfach anhand der ID.
	 *
	 * @param id   die ID des Eintrages
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Erstellt ein neues Unterrichtsfach für einen UV-Lehrer.
	 *
	 * @param createRequest   die Daten für den neuen Eintrag
	 *
	 * @return die Response
	 */
	Response create(UvLehrerUnterrichtsfachCreateRequest createRequest);

	/**
	 * Führt auf dem Unterrichtsfach mit der angegebenen ID einen Patch aus.
	 *
	 * @param id      die ID des Eintrages
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(long id, UvLehrerUnterrichtsfachPatchRequest patch);

	/**
	 * Löscht das Unterrichtsfach mit der angegebenen ID.
	 *
	 * @param id   die ID des Eintrags
	 *
	 * @return die Response
	 */
	Response delete(long id);

}
