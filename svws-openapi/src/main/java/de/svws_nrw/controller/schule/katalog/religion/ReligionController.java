package de.svws_nrw.controller.schule.katalog.religion;

import java.util.List;

import de.svws_nrw.service.schule.katalog.religion.ReligionCreateRequest;
import de.svws_nrw.service.schule.katalog.religion.ReligionPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Controller für den schulinternen Religionen-Katalog.
 */
public interface ReligionController {

	/**
	 * Ermittelt alle Religionen.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Erstellt eine neue Religion und gibt den erstellten Eintrag zurück.
	 *
	 * @param request das Request-Objekt mit den Daten der neuen Religion
	 * @return die Response
	 */
	Response create(ReligionCreateRequest request);

	/**
	 * Führt einen Patch für eine Religion aus.
	 *
	 * @param id    die ID des Eintrags
	 * @param patch der Patch
	 * @return die Response
	 */
	Response patch(long id, ReligionPatchRequest patch);

	/**
	 * Löscht mehrere Religionen anhand der IDs und gibt die Aktions-Logs zurück.
	 *
	 * @param ids die IDs der Einträge
	 * @return die Response
	 */
	Response delete(List<Long> ids);

}
