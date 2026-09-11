package de.svws_nrw.controller.uv.planungsabschnitte;

import java.util.Collection;

import de.svws_nrw.service.uv.planungsabschnitte.UvPlanungsabschnittCreateRequest;
import de.svws_nrw.service.uv.planungsabschnitte.UvPlanungsabschnittPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Planungsabschnitte.
 */
public interface UvPlanungsabschnittController {

	/**
	 * Ermittelt einen UV-Planungsabschnitt anhand der ID.
	 *
	 * @param id   die ID
	 *
	 * @return die Response
	 */
	Response get(long id);

	/**
	 * Ermittelt die UV-Planungsabschnitte eines Schuljahres. Ist das Schuljahr {@code null},
	 * so werden alle UV-Planungsabschnitte zurückgegeben.
	 *
	 * @param schuljahr   das Schuljahr oder {@code null} für alle
	 *
	 * @return die Response
	 */
	Response getListBySchuljahr(Integer schuljahr);

	/**
	 * Erstellt einen neuen UV-Planungsabschnitt.
	 *
	 * @param createRequest   die Daten
	 *
	 * @return die Response
	 */
	Response create(UvPlanungsabschnittCreateRequest createRequest);

	/**
	 * Patcht einen UV-Planungsabschnitt.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(UvPlanungsabschnittPatchRequest patch);

	/**
	 * Patcht mehrere UV-Planungsabschnitte.
	 *
	 * @param patches   die Patches
	 *
	 * @return die Response
	 */
	Response patchMultiple(Collection<UvPlanungsabschnittPatchRequest> patches);

	/**
	 * Löscht einen UV-Planungsabschnitt.
	 *
	 * @param id   die ID
	 *
	 * @return die Response
	 */
	Response delete(long id);

	/**
	 * Löscht mehrere UV-Planungsabschnitte.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<Long> ids);

	/**
	 * Löscht mehrere UV-Planungsabschnitte und gibt eine Liste von SimpleOperationResponse-Objekten zurück
	 * @param ids die IDs
	 * @return die Response
	 */
	Response deleteMultipleAsListSimpleOperationResponse(Collection<Long> ids);

}
