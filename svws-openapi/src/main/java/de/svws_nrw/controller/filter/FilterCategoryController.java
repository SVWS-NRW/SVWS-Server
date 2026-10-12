package de.svws_nrw.controller.filter;

import de.svws_nrw.core.types.filter.FilterDomain;
import jakarta.ws.rs.core.Response;

/**
 * Der Controller für den Zugriff auf Filterkategorien. Er enthält keine Fachlogik, sondern delegiert an den Service.
 */
public interface FilterCategoryController {

	/**
	 * Ermittelt alle Filterkategorien der übergebenen Domain.
	 *
	 * @param domain die Filterdomain, für die die Filterkategorien ermittelt werden
	 *
	 * @return die Response mit einer Liste der Filterkategorien
	 */
	Response getByDomain(FilterDomain domain);

}
