package de.svws_nrw.controller.uv.lehrer;

import java.util.Collection;

import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittLehrerPK;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrerCreateRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Planungsabschnitt-Lehrer-Zuordnungen.
 */
public interface UvPlanungsabschnittLehrerController {

	/**
	 * Erstellt eine neue Planungsabschnitt-Lehrer-Zuordnung.
	 *
	 * @param createRequest   die Daten
	 *
	 * @return die Response
	 */
	Response create(UvPlanungsabschnittLehrerCreateRequest createRequest);

	/**
	 * Erstellt mehrere neue Planungsabschnitt-Lehrer-Zuordnungen.
	 *
	 * @param createRequests   die Daten
	 *
	 * @return die Response
	 */
	Response createMultiple(Collection<UvPlanungsabschnittLehrerCreateRequest> createRequests);

	/**
	 * Löscht eine Planungsabschnitt-Lehrer-Zuordnung.
	 *
	 * @param id   die zusammengesetzte ID
	 *
	 * @return die Response
	 */
	Response delete(DTOUvPlanungsabschnittLehrerPK id);

	/**
	 * Löscht mehrere Planungsabschnitt-Lehrer-Zuordnungen.
	 *
	 * @param ids   die Liste der zusammengesetzten IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<DTOUvPlanungsabschnittLehrerPK> ids);

}
