package de.svws_nrw.controller.gost.klausuren;

import java.util.Collection;

import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerklausurPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf GOSt-Schülerklausuren.
 */
public interface GostKlausurenSchuelerklausurController {

	/**
	 * Ermittelt eine Schülerklausur.
	 *
	 * @param id die ID
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt Schülerklausuren zu den angegebenen IDs.
	 *
	 * @param ids die IDs
	 *
	 * @return die Response
	 */
	Response getListByIds(Collection<Long> ids);

	/**
	 * Patcht eine Schülerklausur.
	 *
	 * @param patchRequest die Patch-Daten
	 *
	 * @return die Response
	 */
	Response patch(GostKlausurenSchuelerklausurPatchRequest patchRequest);

	/**
	 * Löscht eine Schülerklausur.
	 *
	 * @param id die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere Schülerklausuren.
	 *
	 * @param ids die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
