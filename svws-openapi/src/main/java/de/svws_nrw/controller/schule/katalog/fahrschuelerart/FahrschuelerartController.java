package de.svws_nrw.controller.schule.katalog.fahrschuelerart;

import java.util.List;

import de.svws_nrw.service.schule.katalog.fahrschuelerart.FahrschuelerartCreateRequest;
import de.svws_nrw.service.schule.katalog.fahrschuelerart.FahrschuelerartPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Controller für den schulspezifischen Katalog der Fahrschülerarten.
 */
public interface FahrschuelerartController {

	/**
	 * Ermittelt alle Fahrschülerarten.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Erstelle eine neue Fahrschülerart und gibt den erstellen Eintrag zurück.
	 *
	 * @param request   das Request-Objekt mit den Daten für den neuen Eintrag
	 *
	 * @return die Response
	 */
	Response create(FahrschuelerartCreateRequest request);

	/**
	 * Führt einen Patch für eine Fahrschülerart aus.
	 *
	 * @param id        die ID des Eintrags
	 * @param patch     der Patch
	 *
	 * @return die Response
	 */
	Response patch(long id, FahrschuelerartPatchRequest patch);

	/**
	 * Löscht mehrere Fahrschülerarten anhand der IDs und gibt den Status der Löschoperationen zurück.
	 *
	 * @param ids    die IDs der Einträge
	 *
	 * @return die Response
	 */
	Response deleteMultiple(List<Long> ids);
}
