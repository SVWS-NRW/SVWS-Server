package de.svws_nrw.controller.gost.klausuren;

import java.util.Collection;

import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf GOSt-Kursklausuren.
 */
public interface GostKlausurenKursklausurController {

	/**
	 * Ermittelt eine Kursklausur.
	 *
	 * @param id die ID
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt Kursklausuren zu den angegebenen IDs.
	 *
	 * @param ids die IDs
	 *
	 * @return die Response
	 */
	Response getListByIds(Collection<Long> ids);

	/**
	 * Löscht eine Kursklausur.
	 *
	 * @param id die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere Kursklausuren.
	 *
	 * @param ids die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
