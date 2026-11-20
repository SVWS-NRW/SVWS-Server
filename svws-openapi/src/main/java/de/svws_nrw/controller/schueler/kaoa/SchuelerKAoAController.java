package de.svws_nrw.controller.schueler.kaoa;

import de.svws_nrw.service.schueler.kaoa.SchuelerKAoACreateRequest;
import de.svws_nrw.service.schueler.kaoa.SchuelerKAoAPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Der Controller für die KAoA-Daten der Schüler. Er enthält keine Fachlogik und delegiert an den Service.
 */
public interface SchuelerKAoAController {

	/**
	 * Liefert alle KAoA-Daten eines Schülers.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Response mit den KAoA-Daten
	 */
	Response getList(long idSchueler);

	/**
	 * Legt KAoA-Daten für einen Schüler an.
	 *
	 * @param idSchueler   die ID des Schülers
	 * @param request      die Daten der neuen KAoA-Daten
	 *
	 * @return die Response mit den angelegten KAoA-Daten
	 */
	Response create(long idSchueler, SchuelerKAoACreateRequest request);

	/**
	 * Ändert KAoA-Daten eines Schülers teilweise.
	 *
	 * @param idSchueler   die ID des Schülers
	 * @param id           die ID der KAoA-Daten
	 * @param patch        der Patch
	 *
	 * @return die Response mit den aktualisierten KAoA-Daten
	 */
	Response patch(long idSchueler, long id, SchuelerKAoAPatchRequest patch);

	/**
	 * Löscht KAoA-Daten eines Schülers.
	 *
	 * @param idSchueler   die ID des Schülers
	 * @param id           die ID der KAoA-Daten
	 *
	 * @return die Response mit der SimpleOperationResponse des gelöschten Eintrags
	 */
	Response delete(long idSchueler, long id);

}
