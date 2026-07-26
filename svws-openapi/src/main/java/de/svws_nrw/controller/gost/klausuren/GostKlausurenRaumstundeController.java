package de.svws_nrw.controller.gost.klausuren;

import java.util.Collection;

import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf GOSt-Klausurraumstunden.
 */
public interface GostKlausurenRaumstundeController {

	/**
	 * Ermittelt eine Klausurraumstunde.
	 *
	 * @param id die ID
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt Klausurraumstunden zu den angegebenen IDs.
	 *
	 * @param ids die IDs
	 *
	 * @return die Response
	 */
	Response getListByIds(Collection<Long> ids);

	/**
	 * Löscht eine Klausurraumstunde.
	 *
	 * @param id die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

}
