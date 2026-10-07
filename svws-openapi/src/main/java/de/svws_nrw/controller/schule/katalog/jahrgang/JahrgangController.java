package de.svws_nrw.controller.schule.katalog.jahrgang;

import java.util.List;

import de.svws_nrw.service.schule.katalog.jahrgang.JahrgangCreateRequest;
import de.svws_nrw.service.schule.katalog.jahrgang.JahrgangPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Controller für den schulinternen Jahrgangs-Katalog.
 */
public interface JahrgangController {

	/**
	 * Ermittelt alle Jahrgänge.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Erstellt einen neuen Jahrgang und gibt den erstellten Eintrag zurück.
	 *
	 * @param request das Request-Objekt mit den Daten des neuen Jahrgangs
	 *
	 * @return die Response
	 */
	Response create(JahrgangCreateRequest request);

	/**
	 * Aktualisiert den Jahrgang zur übergebenen ID und gibt den aktualisierten Eintrag zurück.
	 *
	 * @param id      die ID des Jahrgangs
	 * @param request das Request-Objekt mit den zu ändernden Feldern
	 *
	 * @return die Response
	 */
	Response patch(long id, JahrgangPatchRequest request);

	/**
	 * Löscht mehrere Jahrgänge anhand der IDs und gibt die Aktions-Logs zurück.
	 *
	 * @param ids die IDs der Einträge
	 *
	 * @return die Response
	 */
	Response delete(List<Long> ids);
}
