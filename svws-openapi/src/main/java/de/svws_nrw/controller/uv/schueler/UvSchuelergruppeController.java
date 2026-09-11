package de.svws_nrw.controller.uv.schueler;

import java.util.Collection;

import de.svws_nrw.core.data.uv.UvSchuelergruppeCreateRequest;
import de.svws_nrw.service.uv.schueler.UvSchuelergruppePatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Schülergruppen.
 */
public interface UvSchuelergruppeController {

	/**
	 * Erstellt eine neue Schülergruppe.
	 *
	 * @param createRequest   die Daten
	 *
	 * @return die Response
	 */
	Response create(UvSchuelergruppeCreateRequest createRequest);

	/**
	 * Erstellt mehrere neue Schülergruppen.
	 *
	 * @param createRequests   die Daten
	 *
	 * @return die Response
	 */
	Response createMultiple(Collection<UvSchuelergruppeCreateRequest> createRequests);

	/**
	 * Führt einen Patch auf einer Schülergruppe aus.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(UvSchuelergruppePatchRequest patch);

	/**
	 * Führt mehrere Patches auf Schülergruppen aus.
	 *
	 * @param patches   die Patches
	 *
	 * @return die Response
	 */
	Response patchMultiple(Collection<UvSchuelergruppePatchRequest> patches);

	/**
	 * Löscht eine Schülergruppe.
	 *
	 * @param id   die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere Schülergruppen.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
