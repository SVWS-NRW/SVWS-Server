package de.svws_nrw.controller.uv.lerngruppen;

import java.util.Collection;

import de.svws_nrw.core.data.uv.UvLerngruppeCreateRequest;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppePatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Lerngruppen.
 */
public interface UvLerngruppeController {

	/**
	 * Erstellt eine neue Lerngruppe.
	 *
	 * @param createRequest   die Daten
	 *
	 * @return die Response
	 */
	Response create(UvLerngruppeCreateRequest createRequest);

	/**
	 * Führt einen Patch auf einer Lerngruppe aus.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(UvLerngruppePatchRequest patch);

	/**
	 * Führt mehrere Patches auf Lerngruppen aus.
	 *
	 * @param patches   die Patches
	 *
	 * @return die Response
	 */
	Response patchMultiple(Collection<UvLerngruppePatchRequest> patches);

	/**
	 * Löscht eine Lerngruppe.
	 *
	 * @param id   die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere Lerngruppen.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
