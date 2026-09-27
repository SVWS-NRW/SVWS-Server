package de.svws_nrw.controller.schule.katalog.ankreuzkompetenz;

import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzKonfigurationPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Controller für die Konfiguration der Ankreuzkompetenzen.
 */
public interface AnkreuzkompetenzKonfigurationController {

	/**
	 * Gibt die Konfiguration für die Ankreuzkompetenzen zurück.
	 *
	 * @return die Response
	 */
	Response get();


	/**
	 * Führt einen Patch auf der Konfiguration der Ankreuzkompetenzen aus und gibt das Ergebnis nach dem Patch zurück.
	 *
	 * @param patch   der Patch
	 *
	 * @return die Response
	 */
	Response patch(AnkreuzkompetenzKonfigurationPatchRequest patch);

}
