package de.svws_nrw.controller.uv.kurse;

import java.util.Collection;

import de.svws_nrw.core.data.uv.UvKursCreateRequest;
import de.svws_nrw.service.uv.kurse.UvKursPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Kurse.
 */
public interface UvKursController {

	/**
	 * Erstellt einen neuen Kurs.
	 *
	 * @param createRequest   die Daten
	 *
	 * @return die Response
	 */
	Response create(UvKursCreateRequest createRequest);

	/**
	 * Patcht einen Kurs.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(UvKursPatchRequest patch);

	/**
	 * Patcht mehrere Kurse.
	 *
	 * @param patches   die Patches
	 *
	 * @return die Response
	 */
	Response patchMultiple(Collection<UvKursPatchRequest> patches);

	/**
	 * Löscht einen Kurs.
	 *
	 * @param id   die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere Kurse.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
