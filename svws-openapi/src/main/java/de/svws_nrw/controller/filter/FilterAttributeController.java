package de.svws_nrw.controller.filter;

import de.svws_nrw.core.types.filter.FilterDomain;
import jakarta.ws.rs.core.Response;

/**
 * Der Controller für den Zugriff auf Filterattribute. Er enthält keine Fachlogik, sondern delegiert an den Service.
 */
public interface FilterAttributeController {

	/**
	 * Ermittelt alle Filterattribute der übergebenen Domain.
	 *
	 * @param domain die Filterdomain, für die die Filterattribute ermittelt werden
	 *
	 * @return die Response mit einer Liste der Filterattribute
	 */
	Response getByDomain(FilterDomain domain);

}
