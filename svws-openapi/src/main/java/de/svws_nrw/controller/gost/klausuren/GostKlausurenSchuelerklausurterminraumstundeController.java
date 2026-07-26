package de.svws_nrw.controller.gost.klausuren;

import java.util.Collection;

import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf GOSt-Schülerklausurtermin-Raumstunden-Zuordnungen.
 */
public interface GostKlausurenSchuelerklausurterminraumstundeController {

	/**
	 * Ermittelt eine Zuordnung.
	 *
	 * @param idSchuelerklausurtermin die ID des Schülerklausurtermins
	 * @param idRaumstunde die ID der Klausurraumstunde
	 *
	 * @return die Response
	 */
	Response get(long idSchuelerklausurtermin, long idRaumstunde);

	/**
	 * Ermittelt Zuordnungen zu den angegebenen Schülerklausurterminen.
	 *
	 * @param ids die IDs der Schülerklausurtermine
	 *
	 * @return die Response
	 */
	Response getListBySchuelerklausurterminIds(Collection<Long> ids);

	/**
	 * Löscht eine Zuordnung.
	 *
	 * @param idSchuelerklausurtermin die ID des Schülerklausurtermins
	 * @param idRaumstunde die ID der Klausurraumstunde
	 *
	 * @return die Response
	 */
	Response delete(long idSchuelerklausurtermin, long idRaumstunde);

}
