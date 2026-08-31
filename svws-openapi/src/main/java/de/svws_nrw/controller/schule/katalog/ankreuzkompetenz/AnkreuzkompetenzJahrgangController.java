package de.svws_nrw.controller.schule.katalog.ankreuzkompetenz;

import java.util.List;

import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzJahrgangCreateRequest;
import jakarta.ws.rs.core.Response;

/**
 * Controller für den schulinternen Ankreuzkompetenz-Jahrgang-Katalog.
 */
public interface AnkreuzkompetenzJahrgangController {

	/**
	 * Erstellt neue AnkreuzkompetenzJahrgangzuordnungen und gibt die erstellten Einträge zurück.
	 *
	 * @param request das Request-Objekt mit den Daten der neuen AnkreuzkompetenzJahrgangzuordnungen
	 * @return die Response
	 */
	Response createMultiple(List<AnkreuzkompetenzJahrgangCreateRequest> request);


	/**
	 * Löscht mehrere Ankreuzkompetenz-Jahrgänge anhand der IDs und gibt die Aktions-Logs zurück.
	 *
	 * @param ids die IDs der Einträge
	 * @return die Response
	 */
	Response deleteMultiple(List<Long> ids);

}
