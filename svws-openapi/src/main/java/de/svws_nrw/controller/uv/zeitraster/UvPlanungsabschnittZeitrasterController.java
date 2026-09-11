package de.svws_nrw.controller.uv.zeitraster;

import java.util.Collection;

import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittZeitrasterPK;
import de.svws_nrw.service.uv.zeitraster.UvPlanungsabschnittZeitrasterCreateRequest;
import de.svws_nrw.service.uv.zeitraster.UvPlanungsabschnittZeitrasterPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Planungsabschnitt-Zeitraster-Zuordnungen.
 */
public interface UvPlanungsabschnittZeitrasterController {

	/**
	 * Erstellt eine neue Zeitraster-Zuordnung.
	 *
	 * @param createRequest   die Daten
	 *
	 * @return die Response
	 */
	Response create(UvPlanungsabschnittZeitrasterCreateRequest createRequest);

	/**
	 * Erstellt mehrere neue Zeitraster-Zuordnungen.
	 *
	 * @param createRequests   die Daten
	 *
	 * @return die Response
	 */
	Response createMultiple(Collection<UvPlanungsabschnittZeitrasterCreateRequest> createRequests);

	/**
	 * Patcht eine Zeitraster-Zuordnung.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(UvPlanungsabschnittZeitrasterPatchRequest patch);

	/**
	 * Löscht eine Zeitraster-Zuordnung.
	 *
	 * @param pk   der zusammengesetzte Primärschlüssel
	 *
	 * @return die Response
	 */
	Response delete(DTOUvPlanungsabschnittZeitrasterPK pk);

	/**
	 * Löscht mehrere Zeitraster-Zuordnungen eines Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 * @param zeitrasterIds         die IDs der zu löschenden Zeitraster
	 *
	 * @return die Response
	 */
	Response deleteMultiple(long idPlanungsabschnitt, Collection<Long> zeitrasterIds);

}
