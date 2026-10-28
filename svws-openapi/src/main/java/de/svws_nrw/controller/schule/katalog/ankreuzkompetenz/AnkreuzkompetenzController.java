package de.svws_nrw.controller.schule.katalog.ankreuzkompetenz;

import java.util.List;

import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzCreateRequest;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Controller für den Katalog der Ankreuzkompetenzen.
 */
public interface AnkreuzkompetenzController {

	/**
	 * Ermittelt alle Ankreuzkompetenzen.
	 *
	 * @return die Response
	 */
	Response getAll();

	/**
	 * Erstellt eine neue Ankreuzkompetenz und gibt den erstellten Eintrag zurück.
	 *
	 * @param request das Request-Objekt mit den Daten der neuen Ankreuzkompetenz
	 *
	 * @return die Response
	 */
	Response create(AnkreuzkompetenzCreateRequest request);

	/**
	 * Führt einen Patch für eine Ankreuzkompetenz aus und gibt den geänderten Eintrag zurück.
	 *
	 * @param id    die ID des Eintrags
	 * @param patch der Patch
	 *
	 * @return die Response
	 */
	Response patch(long id, AnkreuzkompetenzPatchRequest patch);

	/**
	 * Löscht mehrere Ankreuzkompetenzen anhand der IDs und gibt die Ergebnisse je ID zurück.
	 *
	 * @param ids die IDs der Einträge
	 *
	 * @return die Response
	 */
	Response delete(List<Long> ids);

}
