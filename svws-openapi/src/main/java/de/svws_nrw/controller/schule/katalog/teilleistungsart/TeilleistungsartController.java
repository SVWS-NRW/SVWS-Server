package de.svws_nrw.controller.schule.katalog.teilleistungsart;

import java.util.List;

import de.svws_nrw.service.schule.katalog.teilleistungsart.TeilleistungsartCreateRequest;
import de.svws_nrw.service.schule.katalog.teilleistungsart.TeilleistungsartPatchRequest;
import jakarta.ws.rs.core.Response;

public interface TeilleistungsartController {

	/**
	 * Ermittelt alle Teilleistungsarten.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Erstellt eine neue Teilleistungsart und gibt den erstellten Eintrag zurück.
	 *
	 * @param request   das Request-Objekt mit den Daten für den neuen Eintrag
	 * @return die Response
	 */
	Response create(TeilleistungsartCreateRequest request);

	/**
	 * Führt einen Patch für eine Teilleistungsart aus.
	 *
	 * @param id      die ID des Eintrags
	 * @param patch   der Patch
	 * @return die Response
	 */
	Response patch(long id, TeilleistungsartPatchRequest patch);

	/**
	 * Löscht mehrere Teilleistungsarten anhand der IDs und gibt die Aktions-Logs zurück.
	 *
	 * @param ids   die IDs der Einträge
	 * @return die Response
	 */
	Response delete(List<Long> ids);
}
