package de.svws_nrw.controller.uv.zeitraster;

import java.util.Collection;

import de.svws_nrw.service.uv.zeitraster.UvZeitrasterEintragCreateRequest;
import de.svws_nrw.service.uv.zeitraster.UvZeitrasterEintragPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Zeitraster-Einträge.
 */
public interface UvZeitrasterEintragController {

	/**
	 * Ermittelt einen UV-Zeitraster-Eintrag anhand der ID.
	 *
	 * @param id   die ID
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt alle UV-Zeitraster-Einträge.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Ermittelt alle UV-Zeitraster-Einträge zu einem bestimmten Zeitraster.
	 *
	 * @param idZeitraster   die ID des Zeitrasters
	 *
	 * @return die Response
	 */
	Response getByZeitrasterId(long idZeitraster);

	/**
	 * Erstellt einen neuen UV-Zeitraster-Eintrag.
	 *
	 * @param createRequest   die Daten
	 *
	 * @return die Response
	 */
	Response create(UvZeitrasterEintragCreateRequest createRequest);

	/**
	 * Erstellt mehrere neue UV-Zeitraster-Einträge.
	 *
	 * @param createRequests   die Daten
	 *
	 * @return die Response
	 */
	Response createMultiple(Collection<UvZeitrasterEintragCreateRequest> createRequests);

	/**
	 * Patcht einen UV-Zeitraster-Eintrag.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(UvZeitrasterEintragPatchRequest patch);

	/**
	 * Patcht mehrere UV-Zeitraster-Einträge.
	 *
	 * @param patches   die Patches
	 *
	 * @return die Response
	 */
	Response patchMultiple(Collection<UvZeitrasterEintragPatchRequest> patches);

	/**
	 * Löscht den UV-Zeitraster-Eintrag mit der angegebenen ID.
	 *
	 * @param id   die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere UV-Zeitraster-Einträge.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
