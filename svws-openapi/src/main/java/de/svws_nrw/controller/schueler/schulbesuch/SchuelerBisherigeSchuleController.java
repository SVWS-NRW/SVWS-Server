package de.svws_nrw.controller.schueler.schulbesuch;

import java.util.List;

import de.svws_nrw.service.schueler.schulbesuch.SchuelerBisherigeSchuleCreateRequest;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerBisherigeSchulePatchRequest;
import jakarta.ws.rs.core.Response;

public interface SchuelerBisherigeSchuleController {

	/**
	 * Erstellt eine neue BisherigeSchule und gibt den erstellten Eintrag zurück.
	 *
	 * @param request   das Request-Objekt mit den Daten für den neuen Eintrag
	 * @return die Response
	 */
	Response create(SchuelerBisherigeSchuleCreateRequest request);

	/**
	 * Führt einen Patch für eine BisherigeSchule aus.
	 *
	 * @param id      die ID des Eintrags
	 * @param patch   der Patch
	 * @return die Response
	 */
	Response patch(long id, SchuelerBisherigeSchulePatchRequest patch);

	/**
	 * Löscht mehrere BisherigeSchule-Einträge anhand der IDs und gibt die gelöschten Einträge zurück.
	 *
	 * @param ids   die IDs der Einträge
	 * @return die Response
	 */
	Response delete(List<Long> ids);
}
