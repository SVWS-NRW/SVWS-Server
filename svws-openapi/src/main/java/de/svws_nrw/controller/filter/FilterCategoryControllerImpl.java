package de.svws_nrw.controller.filter;

import java.util.List;

import de.svws_nrw.core.data.filter.FilterCategoryResponse;
import de.svws_nrw.core.types.filter.FilterDomain;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.filter.FilterCategoryService;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des {@link FilterCategoryController}.
 */
public final class FilterCategoryControllerImpl implements FilterCategoryController {

	private final FilterCategoryService service;

	/**
	 * Erzeugt einen neuen Controller.
	 *
	 * @param service der {@link FilterCategoryService}, an den delegiert wird
	 */
	public FilterCategoryControllerImpl(final FilterCategoryService service) {
		this.service = service;
	}

	@Override
	public Response getByDomain(final FilterDomain domain) {
		final List<FilterCategoryResponse> categories = service.getByDomain(domain);
		return Responses.ok(categories);
	}
}
