package de.svws_nrw.controller.uv.unterrichte;

import java.util.Collection;

import de.svws_nrw.service.uv.unterrichte.UvUnterrichtCreateRequest;
import de.svws_nrw.service.uv.unterrichte.UvUnterrichtPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Unterrichte.
 */
public interface UvUnterrichtController {

	/**
	 * Erstellt eine neue Unterricht-Einheit.
	 *
	 * @param createRequest   die Daten
	 *
	 * @return die Response
	 */
	Response create(UvUnterrichtCreateRequest createRequest);

	/**
	 * Führt einen Patch auf einer Unterricht-Einheit aus.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(UvUnterrichtPatchRequest patch);

	/**
	 * Führt mehrere Patches auf Unterricht-Einheiten aus.
	 *
	 * @param patches   die Patches
	 *
	 * @return die Response
	 */
	Response patchMultiple(Collection<UvUnterrichtPatchRequest> patches);

	/**
	 * Löscht eine Unterricht-Einheit.
	 *
	 * @param id   die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere Unterricht-Einheiten.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
