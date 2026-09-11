package de.svws_nrw.controller.uv.schienen;

import java.util.Collection;

import de.svws_nrw.core.data.uv.UvSchieneCreateRequest;
import de.svws_nrw.service.uv.schienen.UvSchienePatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Schienen.
 */
public interface UvSchieneController {

	/**
	 * Erstellt eine neue Schiene.
	 *
	 * @param createRequest   die Daten
	 *
	 * @return die Response
	 */
	Response create(UvSchieneCreateRequest createRequest);

	/**
	 * Patcht eine Schiene.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(UvSchienePatchRequest patch);

	/**
	 * Patcht mehrere Schienen.
	 *
	 * @param patches   die Patches
	 *
	 * @return die Response
	 */
	Response patchMultiple(Collection<UvSchienePatchRequest> patches);

	/**
	 * Löscht eine Schiene.
	 *
	 * @param id   die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere Schienen.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
