package de.svws_nrw.controller.schueler.schulbesuch;

import java.util.List;

import de.svws_nrw.service.schueler.schulbesuch.SchuelerMerkmalCreateRequest;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerMerkmalPatchRequest;
import jakarta.ws.rs.core.Response;

public interface SchuelerMerkmalController {

	/**
	 * Erstellt ein neues SchuelerMerkmal und gibt den erstellten Eintrag zurück.
	 *
	 * @param request   das Request-Objekt mit den Daten für den neuen Eintrag
	 * @return die Response
	 */
	Response create(SchuelerMerkmalCreateRequest request);

	/**
	 * Führt einen Patch für ein SchuelerMerkmal aus.
	 *
	 * @param id      die ID des Eintrags
	 * @param patch   der Patch
	 * @return die Response
	 */
	Response patch(long id, SchuelerMerkmalPatchRequest patch);

	/**
	 * Löscht mehrere SchuelerMerkmal-Einträge anhand der IDs und gibt die gelöschten Einträge zurück.
	 *
	 * @param ids   die IDs der Einträge
	 * @return die Response
	 */
	Response delete(List<Long> ids);
}
