package de.svws_nrw.controller.gost.klausuren;

import java.util.Collection;

import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurterminCreateRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf GOSt-Schülerklausurtermine.
 */
public interface GostKlausurenSchuelerklausurterminController {

	/**
	 * Ermittelt einen Schülerklausurtermin.
	 *
	 * @param id die ID
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt Schülerklausurtermine zu den angegebenen IDs.
	 *
	 * @param ids die IDs
	 *
	 * @return die Response
	 */
	Response getListByIds(Collection<Long> ids);

	/**
	 * Erstellt einen Schülerklausurtermin.
	 *
	 * @param createRequest die Create-Daten
	 *
	 * @return die Response
	 */
	Response create(GostKlausurenSchuelerklausurterminCreateRequest createRequest);

	/**
	 * Löscht einen Schülerklausurtermin.
	 *
	 * @param id die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere Schülerklausurtermine.
	 *
	 * @param ids die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
