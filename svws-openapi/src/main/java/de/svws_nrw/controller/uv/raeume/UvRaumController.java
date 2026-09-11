package de.svws_nrw.controller.uv.raeume;

import java.util.Collection;

import de.svws_nrw.service.uv.raeume.UvRaumCreateRequest;
import de.svws_nrw.service.uv.raeume.UvRaumPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Räume.
 */
public interface UvRaumController {

	/** @param id   die ID
	 *  @return die Response */
	Response get(long id);

	/** @return die Response */
	Response getAll();

	/** @param createRequest   die Daten
	 *  @return die Response */
	Response create(UvRaumCreateRequest createRequest);

	/** @param createRequests   die Daten
	 *  @return die Response */
	Response createMultiple(Collection<UvRaumCreateRequest> createRequests);

	/** @param patch   der Patch
	 *  @return die Response */
	Response patch(UvRaumPatchRequest patch);

	/** @param patches   die Patches
	 *  @return die Response */
	Response patchMultiple(Collection<UvRaumPatchRequest> patches);

	/** @param id   die ID
	 *  @return die Response */
	Response delete(long id);

	/** @param ids   die IDs
	 *  @return die Response */
	Response deleteMultiple(Collection<Long> ids);

}
