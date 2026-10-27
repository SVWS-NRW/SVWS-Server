package de.svws_nrw.controller.schule.katalog.lernplattform;

import java.util.List;

import de.svws_nrw.service.schule.katalog.lernplattform.LernplattformCreateRequest;
import de.svws_nrw.service.schule.katalog.lernplattform.LernplattformPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Controller für den schulspezifischen Katalog der Lernplattformen.
 */
public interface LernplattformController {

	/**
	 * Ermittelt alle Lernplattformen.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Erstellt eine neue Lernplattform und gibt den erstellten Eintrag zurück.
	 *
	 * @param request   das Request-Objekt mit den Daten für den neuen Eintrag
	 *
	 * @return die Response
	 */
	Response create(LernplattformCreateRequest request);

	/**
	 * Führt einen Patch für eine Lernplattform aus.
	 *
	 * @param id      die ID des Eintrags
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(long id, LernplattformPatchRequest patch);

	/**
	 * Löscht mehrere Lernplattformen anhand der IDs und gibt den Status der Löschoperationen zurück.
	 *
	 * @param ids   die IDs der Einträge
	 *
	 * @return die Response
	 */
	Response deleteMultiple(List<Long> ids);

}
