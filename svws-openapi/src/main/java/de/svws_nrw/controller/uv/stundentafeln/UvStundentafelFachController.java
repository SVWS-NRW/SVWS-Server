package de.svws_nrw.controller.uv.stundentafeln;

import java.util.Collection;

import de.svws_nrw.service.uv.stundentafeln.UvStundentafelFachCreateRequest;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelFachPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Stundentafel-Fächer.
 */
public interface UvStundentafelFachController {

	/**
	 * Ermittelt ein UV-Stundentafel-Fach anhand der ID.
	 *
	 * @param id   die ID
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt alle UV-Stundentafel-Fächer.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Erstellt ein neues UV-Stundentafel-Fach.
	 *
	 * @param createRequest   die Daten
	 *
	 * @return die Response
	 */
	Response create(UvStundentafelFachCreateRequest createRequest);

	/**
	 * Erstellt mehrere neue UV-Stundentafel-Fächer.
	 *
	 * @param createRequests   die Daten
	 *
	 * @return die Response
	 */
	Response createMultiple(Collection<UvStundentafelFachCreateRequest> createRequests);

	/**
	 * Patcht ein UV-Stundentafel-Fach.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(UvStundentafelFachPatchRequest patch);

	/**
	 * Patcht mehrere UV-Stundentafel-Fächer.
	 *
	 * @param patches   die Patches
	 *
	 * @return die Response
	 */
	Response patchMultiple(Collection<UvStundentafelFachPatchRequest> patches);

	/**
	 * Löscht das UV-Stundentafel-Fach mit der angegebenen ID.
	 *
	 * @param id   die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere UV-Stundentafel-Fächer.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
