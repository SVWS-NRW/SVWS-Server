package de.svws_nrw.service.filter;

import java.util.List;
import java.util.Objects;

import de.svws_nrw.core.data.filter.FilterAttributeResponse;
import de.svws_nrw.core.types.filter.FilterDomain;
import de.svws_nrw.mapper.filter.FilterAttributeMapper;

/**
 * Service-Klasse für die Verwaltung von Filterattributen.
 */
public final class FilterAttributeService {

	private final FilterRegistry filterRegistry;

	private final FilterAttributeMapper mapper;

	/**
	 * Erzeugt einen neuen Service. Die Abhängigkeiten werden von der {@link FilterAttributeServiceFactory} übergeben.
	 *
	 * @param filterRegistry die Registry mit den konfigurierten Filterkategorien und Filterattributen
	 * @param mapper der Mapper für die Abbildung der Filterattribute auf die Response-DTOs
	 *
	 * @throws NullPointerException wenn einer der Parameter {@code null} ist
	 */
	FilterAttributeService(final FilterRegistry filterRegistry, final FilterAttributeMapper mapper) {
		this.filterRegistry = Objects.requireNonNull(filterRegistry, "Die FilterRegistry darf nicht null sein.");
		this.mapper = Objects.requireNonNull(mapper, "Der FilterAttributeMapper darf nicht null sein.");
	}

	/**
	 * Liefert eine Liste mit allen Filterattributen zu einer Domain.
	 *
	 * @param domain die Filterdomain
	 *
	 * @return Liste von Filterattributen
	 */
	public List<FilterAttributeResponse> getByDomain(final FilterDomain domain) {
		return filterRegistry.getAttributesByDomain(domain).stream()
				.map(mapper::toApi)
				.toList();
	}

}
