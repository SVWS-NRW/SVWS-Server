package de.svws_nrw.controller.schule.katalog.haltestelle;

import java.util.List;

import de.svws_nrw.service.schule.katalog.haltestelle.HaltestelleCreateRequest;
import de.svws_nrw.service.schule.katalog.haltestelle.HaltestellePatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Controller für den schulinternen Haltestellen-Katalog.
 */
public interface HaltestelleController {

	/**
	 * Ermittelt alle Haltestellen.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Erstellt eine neue Haltestelle und gibt den erstellten Eintrag zurück.
	 *
	 * @param request das Request-Objekt mit den Daten der neuen Haltestelle
	 *
	 * @return die Response
	 */
	Response create(HaltestelleCreateRequest request);

	/**
	 * Führt einen Patch für eine Haltestelle aus.
	 *
	 * @param id    die ID des Eintrags
	 * @param patch der Patch
	 *
	 * @return die Response
	 */
	Response patch(long id, HaltestellePatchRequest patch);

	/**
	 * Löscht mehrere Haltestellen anhand der IDs und gibt die Aktions-Logs zurück.
	 *
	 * @param ids die IDs der Einträge
	 *
	 * @return die Response
	 */
	Response delete(List<Long> ids);

}
