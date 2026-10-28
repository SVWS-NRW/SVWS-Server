package de.svws_nrw.controller.schule.katalog.einwilligungsart;

import java.util.List;

import de.svws_nrw.service.schule.katalog.einwilligungsart.EinwilligungsartCreateRequest;
import de.svws_nrw.service.schule.katalog.einwilligungsart.EinwilligungsartPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Controller für den schulspezifischen Katalog der Einwilligungsarten.
 */
public interface EinwilligungsartController {

	/**
	 * Ermittelt alle Einwilligungsarten.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Erstellt eine neue Einwilligungsart und gibt den erstellten Eintrag zurück.
	 *
	 * @param request   das Request-Objekt mit den Daten für den neuen Eintrag
	 *
	 * @return die Response
	 */
	Response create(EinwilligungsartCreateRequest request);

	/**
	 * Führt einen Patch für eine Einwilligungsart aus.
	 *
	 * @param id      die ID des Eintrags
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(long id, EinwilligungsartPatchRequest patch);

	/**
	 * Löscht mehrere Einwilligungsarten anhand der IDs und gibt den Status der Löschoperationen zurück.
	 *
	 * @param ids   die IDs der Einträge
	 *
	 * @return die Response
	 */
	Response deleteMultiple(List<Long> ids);

}
