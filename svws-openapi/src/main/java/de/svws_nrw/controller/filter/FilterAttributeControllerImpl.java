package de.svws_nrw.controller.filter;

import java.util.List;

import de.svws_nrw.core.data.filter.FilterAttributeResponse;
import de.svws_nrw.core.types.filter.FilterDomain;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.filter.FilterAttributeService;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des {@link FilterAttributeController}.
 */
public final class FilterAttributeControllerImpl implements FilterAttributeController {

	private final FilterAttributeService service;

	/**
	 * Erzeugt einen neuen Controller.
	 *
	 * @param service der {@link FilterAttributeService}, an den delegiert wird
	 */
	public FilterAttributeControllerImpl(final FilterAttributeService service) {
		this.service = service;
	}

	@Override
	public Response getByDomain(final FilterDomain domain) {
		final List<FilterAttributeResponse> attributes = service.getByDomain(domain);
		return Responses.ok(attributes);
	}
}
