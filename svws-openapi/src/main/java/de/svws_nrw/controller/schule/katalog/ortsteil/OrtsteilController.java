package de.svws_nrw.controller.schule.katalog.ortsteil;

import java.util.List;

import de.svws_nrw.service.schule.katalog.ortsteil.OrtsteilCreateRequest;
import de.svws_nrw.service.schule.katalog.ortsteil.OrtsteilPatchRequest;
import jakarta.ws.rs.core.Response;

public interface OrtsteilController {

	/**
	 * Ermittelt alle Ortsteile.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Erstellt einen neuen Ortsteil und gibt den erstellten Eintrag zurück.
	 *
	 * @param request das Request-Objekt mit den Daten für den neuen Eintrag
	 * @return die Response
	 */
	Response create(OrtsteilCreateRequest request);

	/**
	 * Führt einen Patch für einen Ortsteil aus.
	 *
	 * @param id    die ID des Eintrags
	 * @param patch der Patch
	 * @return die Response
	 */
	Response patch(long id, OrtsteilPatchRequest patch);

	/**
	 * Löscht mehrere Ortsteile anhand der IDs und gibt die Aktions-Logs zurück.
	 *
	 * @param ids die IDs der Einträge
	 * @return die Response
	 */
	Response delete(List<Long> ids);

}
