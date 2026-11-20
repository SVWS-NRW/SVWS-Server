package de.svws_nrw.controller.filter;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.filter.FilterCatalogService;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des {@link FilterCatalogController}.
 */
public final class FilterCatalogControllerImpl implements FilterCatalogController {

	private final FilterCatalogService service;

	/**
	 * Erzeugt einen neuen Controller.
	 *
	 * @param service der {@link FilterCatalogService}, an den delegiert wird
	 */
	public FilterCatalogControllerImpl(final FilterCatalogService service) {
		this.service = service;
	}

	@Override
	public Response getByName(final String name) {
		return Responses.ok(service.getByName(name));
	}

}
