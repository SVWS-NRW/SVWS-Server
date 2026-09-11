package de.svws_nrw.controller.uv.faecher;

import java.util.Collection;

import de.svws_nrw.service.uv.faecher.UvFachCreateRequest;
import de.svws_nrw.service.uv.faecher.UvFachPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Fächer.
 */
public interface UvFachController {

	/**
	 * Ermittelt ein UV-Fach anhand der ID.
	 *
	 * @param id   die ID des Eintrages
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt alle UV-Fächer.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Erstellt ein neues UV-Fach.
	 *
	 * @param createRequest   die Daten für den neuen Eintrag
	 *
	 * @return die Response
	 */
	Response create(UvFachCreateRequest createRequest);

	/**
	 * Erstellt mehrere neue UV-Fächer.
	 *
	 * @param createRequests   die Daten für die neuen Einträge
	 *
	 * @return die Response
	 */
	Response createMultiple(Collection<UvFachCreateRequest> createRequests);

	/**
	 * Führt auf dem UV-Fach mit der angegebenen ID einen Patch aus.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(UvFachPatchRequest patch);

	/**
	 * Führt auf mehreren UV-Fächern Patches aus.
	 *
	 * @param patches   die Patches
	 *
	 * @return die Response
	 */
	Response patchMultiple(Collection<UvFachPatchRequest> patches);

	/**
	 * Löscht das UV-Fach mit der angegebenen ID.
	 *
	 * @param id   die ID des Eintrags
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere UV-Fächer anhand ihrer IDs.
	 *
	 * @param ids   die IDs der zu löschenden Einträge
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

}
