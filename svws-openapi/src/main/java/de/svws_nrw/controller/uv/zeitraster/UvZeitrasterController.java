package de.svws_nrw.controller.uv.zeitraster;

import java.util.Collection;

import de.svws_nrw.service.uv.zeitraster.UvZeitrasterCreateRequest;
import de.svws_nrw.service.uv.zeitraster.UvZeitrasterPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Zeitraster.
 */
public interface UvZeitrasterController {

	/**
	 * Ermittelt ein UV-Zeitraster anhand der ID.
	 *
	 * @param id   die ID
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt alle UV-Zeitraster.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Erstellt ein neues UV-Zeitraster.
	 *
	 * @param createRequest   die Daten
	 *
	 * @return die Response
	 */
	Response create(UvZeitrasterCreateRequest createRequest);

	/**
	 * Erstellt mehrere neue UV-Zeitraster.
	 *
	 * @param createRequests   die Daten
	 *
	 * @return die Response
	 */
	Response createMultiple(Collection<UvZeitrasterCreateRequest> createRequests);

	/**
	 * Patcht ein UV-Zeitraster.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(UvZeitrasterPatchRequest patch);

	/**
	 * Patcht mehrere UV-Zeitraster.
	 *
	 * @param patches   die Patches
	 *
	 * @return die Response
	 */
	Response patchMultiple(Collection<UvZeitrasterPatchRequest> patches);

	/**
	 * Löscht das UV-Zeitraster mit der angegebenen ID.
	 *
	 * @param id   die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere UV-Zeitraster.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
