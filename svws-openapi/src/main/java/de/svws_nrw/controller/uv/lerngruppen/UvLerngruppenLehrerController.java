package de.svws_nrw.controller.uv.lerngruppen;

import java.util.Collection;

import de.svws_nrw.core.data.uv.UvLerngruppenLehrerCreateRequest;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppenLehrerPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Lerngruppen-Lehrer-Zuordnungen.
 */
public interface UvLerngruppenLehrerController {

	/**
	 * Erstellt eine neue Lerngruppen-Lehrer-Zuordnung.
	 *
	 * @param createRequest   die Daten
	 *
	 * @return die Response
	 */
	Response create(UvLerngruppenLehrerCreateRequest createRequest);

	/**
	 * Erstellt mehrere neue Lerngruppen-Lehrer-Zuordnungen.
	 *
	 * @param createRequests   die Daten
	 *
	 * @return die Response
	 */
	Response createMultiple(Collection<UvLerngruppenLehrerCreateRequest> createRequests);

	/**
	 * Patcht eine Lerngruppen-Lehrer-Zuordnung.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(UvLerngruppenLehrerPatchRequest patch);

	/**
	 * Patcht mehrere Lerngruppen-Lehrer-Zuordnungen.
	 *
	 * @param patches   die Patches
	 *
	 * @return die Response
	 */
	Response patchMultiple(Collection<UvLerngruppenLehrerPatchRequest> patches);

	/**
	 * Löscht eine Lerngruppen-Lehrer-Zuordnung.
	 *
	 * @param id   die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere Lerngruppen-Lehrer-Zuordnungen.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
