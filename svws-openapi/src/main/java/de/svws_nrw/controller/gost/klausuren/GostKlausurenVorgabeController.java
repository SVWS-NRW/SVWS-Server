package de.svws_nrw.controller.gost.klausuren;

import java.util.Collection;

import de.svws_nrw.service.gost.klausuren.GostKlausurenVorgabeCreateRequest;
import de.svws_nrw.service.gost.klausuren.GostKlausurenVorgabePatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf GOSt-Klausurvorgaben.
 */
public interface GostKlausurenVorgabeController {

	/**
	 * Ermittelt Klausurvorgaben eines Abiturjahrgangs.
	 *
	 * @param abiturjahr das Abiturjahr
	 *
	 * @return die Response
	 */
	Response getListByAbiturjahr(int abiturjahr);

	/**
	 * Erstellt eine neue Klausurvorgabe.
	 *
	 * @param createRequest die Daten
	 *
	 * @return die Response
	 */
	Response create(GostKlausurenVorgabeCreateRequest createRequest);

	/**
	 * Patcht eine Klausurvorgabe.
	 *
	 * @param patch der Patch
	 *
	 * @return die Response
	 */
	Response patch(GostKlausurenVorgabePatchRequest patch);

	/**
	 * Patcht mehrere Klausurvorgaben.
	 *
	 * @param patches die Patches
	 *
	 * @return die Response
	 */
	Response patchMultiple(Collection<GostKlausurenVorgabePatchRequest> patches);

	/**
	 * Löscht eine Klausurvorgabe.
	 *
	 * @param id die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere Klausurvorgaben.
	 *
	 * @param ids die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
