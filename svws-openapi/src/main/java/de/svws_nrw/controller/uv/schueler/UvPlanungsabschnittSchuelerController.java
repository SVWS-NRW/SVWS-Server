package de.svws_nrw.controller.uv.schueler;

import java.util.Collection;

import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittSchuelerPK;
import de.svws_nrw.service.uv.schueler.UvPlanungsabschnittSchuelerCreateRequest;
import de.svws_nrw.service.uv.schueler.UvPlanungsabschnittSchuelerPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Ein Controller für den Zugriff auf die UV-Planungsabschnitt-Schüler-Zuordnungen.
 */
public interface UvPlanungsabschnittSchuelerController {

	/**
	 * Erstellt eine neue Planungsabschnitt-Schüler-Zuordnung.
	 *
	 * @param createRequest   die Daten
	 *
	 * @return die Response
	 */
	Response create(UvPlanungsabschnittSchuelerCreateRequest createRequest);

	/**
	 * Erstellt mehrere neue Planungsabschnitt-Schüler-Zuordnungen.
	 *
	 * @param createRequests   die Daten
	 *
	 * @return die Response
	 */
	Response createMultiple(Collection<UvPlanungsabschnittSchuelerCreateRequest> createRequests);

	/**
	 * Führt einen Patch auf einer Planungsabschnitt-Schüler-Zuordnung aus.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(UvPlanungsabschnittSchuelerPatchRequest patch);

	/**
	 * Löscht eine Planungsabschnitt-Schüler-Zuordnung.
	 *
	 * @param id   die zusammengesetzte ID
	 *
	 * @return die Response
	 */
	Response delete(DTOUvPlanungsabschnittSchuelerPK id);

	/**
	 * Löscht mehrere Planungsabschnitt-Schüler-Zuordnungen.
	 *
	 * @param ids   die Liste der zusammengesetzten IDs
	 *
	 * @return die Response
	 */
	Response deleteMultiple(Collection<DTOUvPlanungsabschnittSchuelerPK> ids);

}
