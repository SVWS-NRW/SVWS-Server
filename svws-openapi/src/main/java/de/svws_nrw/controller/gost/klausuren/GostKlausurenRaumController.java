package de.svws_nrw.controller.gost.klausuren;

import java.util.Collection;

import de.svws_nrw.service.gost.klausuren.GostKlausurenRaumCreateRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenRaumPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf GOSt-Klausurräume.
 */
public interface GostKlausurenRaumController {

	/**
	 * Ermittelt einen Klausurraum.
	 *
	 * @param id die ID
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt Klausurräume zu den angegebenen IDs.
	 *
	 * @param ids die IDs
	 *
	 * @return die Response
	 */
	Response getListByIds(Collection<Long> ids);

	/**
	 * Erstellt einen Klausurraum.
	 *
	 * @param createRequest die Create-Daten
	 *
	 * @return die Response
	 */
	Response create(GostKlausurenRaumCreateRequest createRequest);

	/**
	 * Patcht einen Klausurraum.
	 *
	 * @param patchRequest die Patch-Daten
	 *
	 * @return die Response
	 */
	Response patch(GostKlausurenRaumPatchRequest patchRequest);

	/**
	 * Löscht einen Klausurraum.
	 *
	 * @param id die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

}
