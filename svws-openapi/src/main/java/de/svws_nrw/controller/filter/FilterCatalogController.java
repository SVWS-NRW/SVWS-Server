package de.svws_nrw.controller.filter;

import jakarta.ws.rs.core.Response;

/**
 * Der Controller für den Zugriff auf die auswählbaren Werte eines Katalogs. Er enthält keine Fachlogik, sondern delegiert an den Service.
 */
public interface FilterCatalogController {

	/**
	 * Ermittelt alle auswählbaren Werte des Katalogs mit dem übergebenen Namen.
	 *
	 * @param name der Name des Katalogs, z. B. {@code Orte}
	 *
	 * @return die Response mit der Liste der Werte des Katalogs
	 */
	Response getByName(String name);

}
