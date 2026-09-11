package de.svws_nrw.controller.uv.raeume;

import java.util.Collection;

import de.svws_nrw.service.uv.raeume.UvRaumgruppeCreateRequest;
import de.svws_nrw.service.uv.raeume.UvRaumgruppePatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Raumgruppen.
 */
public interface UvRaumgruppeController {

	/** @param id   die ID
	 *  @return die Response */
	Response get(long id);

	/** @return die Response */
	Response getAll();

	/** @param createRequest   die Daten
	 *  @return die Response */
	Response create(UvRaumgruppeCreateRequest createRequest);

	/** @param createRequests   die Daten
	 *  @return die Response */
	Response createMultiple(Collection<UvRaumgruppeCreateRequest> createRequests);

	/** @param patch   der Patch
	 *  @return die Response */
	Response patch(UvRaumgruppePatchRequest patch);

	/** @param patches   die Patches
	 *  @return die Response */
	Response patchMultiple(Collection<UvRaumgruppePatchRequest> patches);

	/** @param id   die ID
	 *  @return die Response */
	Response delete(long id);

	/** @param ids   die IDs
	 *  @return die Response */
	Response deleteMultiple(Collection<Long> ids);

}
