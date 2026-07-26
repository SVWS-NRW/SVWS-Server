package de.svws_nrw.controller.gost.klausuren;

import java.util.Collection;

import de.svws_nrw.service.gost.klausuren.GostKlausurenTerminCreateRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf GOSt-Klausurtermine.
 */
public interface GostKlausurenTerminController {

	/**
	 * Ermittelt einen Klausurtermin.
	 *
	 * @param id die ID
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt Klausurtermine zu den angegebenen IDs.
	 *
	 * @param ids die IDs
	 *
	 * @return die Response
	 */
	Response getListByIds(Collection<Long> ids);

	/**
	 * Erstellt einen Klausurtermin.
	 *
	 * @param createRequest die Daten
	 *
	 * @return die Response
	 */
	Response create(GostKlausurenTerminCreateRequest createRequest);

	/**
	 * Löscht einen Klausurtermin.
	 *
	 * @param id die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere Klausurtermine.
	 *
	 * @param ids die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
