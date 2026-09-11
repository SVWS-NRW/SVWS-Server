package de.svws_nrw.controller.uv.klassen;

import java.util.Collection;

import de.svws_nrw.service.uv.klassen.UvKlasseCreateRequest;
import de.svws_nrw.service.uv.klassen.UvKlassePatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Klassen.
 */
public interface UvKlasseController {

	/**
	 * Erstellt eine neue Klasse.
	 *
	 * @param createRequest   die Daten
	 *
	 * @return die Response
	 */
	Response create(UvKlasseCreateRequest createRequest);

	/**
	 * Führt einen Patch auf einer Klasse aus.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(UvKlassePatchRequest patch);

	/**
	 * Führt mehrere Patches auf Klassen aus.
	 *
	 * @param patches   die Patches
	 *
	 * @return die Response
	 */
	Response patchMultiple(Collection<UvKlassePatchRequest> patches);

	/**
	 * Löscht eine Klasse.
	 *
	 * @param id   die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere Klassen.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
