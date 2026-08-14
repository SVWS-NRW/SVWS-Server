package de.svws_nrw.controller.schule.katalog.ort;

import java.util.List;

import de.svws_nrw.service.schule.katalog.ort.OrtCreateRequest;
import de.svws_nrw.service.schule.katalog.ort.OrtPatchRequest;
import jakarta.ws.rs.core.Response;

public interface OrtController {

	/**
	 * Ermittelt alle Orte.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Erstellt einen neuen Ort und gibt den erstellten Eintrag zurück.
	 *
	 * @param request das Request-Objekt mit den Daten für den neuen Eintrag
	 * @return die Response
	 */
	Response create(OrtCreateRequest request);

	/**
	 * Führt einen Patch für einen Ort aus.
	 *
	 * @param id    die ID des Eintrags
	 * @param patch der Patch
	 * @return die Response
	 */
	Response patch(long id, OrtPatchRequest patch);

	/**
	 * Löscht mehrere Orte anhand der IDs und gibt die Aktions-Logs zurück.
	 *
	 * @param ids die IDs der Einträge
	 * @return die Response
	 */
	Response delete(List<Long> ids);

}
