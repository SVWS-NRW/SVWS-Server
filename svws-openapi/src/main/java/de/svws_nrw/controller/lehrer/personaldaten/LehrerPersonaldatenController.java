package de.svws_nrw.controller.lehrer.personaldaten;

import java.util.List;

import de.svws_nrw.service.lehrer.personaldaten.LehrerPersonaldatenBatchPatchRequest;
import de.svws_nrw.service.lehrer.personaldaten.LehrerPersonaldatenPatchRequest;
import jakarta.ws.rs.core.Response;

public interface LehrerPersonaldatenController {

	/**
	 * Ermittelt die LehrerPersonaldaten eines Lehrers anhand der ID.
	 *
	 * @param id   die ID des Eintrags
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Führt einen Patch für LehrerPersonaldaten aus.
	 *
	 * @param id      die ID des Eintrags
	 * @param patch   der Patch
	 * @return die Response
	 */
	Response patch(long id, LehrerPersonaldatenPatchRequest patch);

	/**
	 * Führt einen Patch für die LehrerPersonaldaten von Lehrern aus.
	 * Die Patches enthalten die IDs der Einträge, auf die sie sich beziehen.
	 *
	 * @param patches   die Patches
	 * @return die Response
	 */
	Response patchMultiple(List<LehrerPersonaldatenBatchPatchRequest> patches);

}
