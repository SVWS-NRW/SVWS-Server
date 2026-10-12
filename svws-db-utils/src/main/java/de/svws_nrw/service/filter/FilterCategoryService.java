package de.svws_nrw.service.filter;

import java.util.List;
import java.util.Objects;

import de.svws_nrw.core.data.filter.FilterCategoryResponse;
import de.svws_nrw.core.types.filter.FilterDomain;
import de.svws_nrw.mapper.filter.FilterCategoryMapper;

/**
 * Service-Klasse für die Verwaltung von Filterkategorien.
 */
public final class FilterCategoryService {

	private final FilterRegistry filterRegistry;

	private final FilterCategoryMapper mapper;

	/**
	 * Erzeugt einen neuen Service. Die Abhängigkeiten werden von der {@link FilterCategoryServiceFactory} übergeben.
	 *
	 * @param filterRegistry die Registry mit den konfigurierten Filterkategorien und Filterattributen
	 * @param mapper der Mapper für die Abbildung der Filterkategorien auf die Response-DTOs
	 *
	 * @throws NullPointerException wenn einer der Parameter {@code null} ist
	 */
	FilterCategoryService(final FilterRegistry filterRegistry, final FilterCategoryMapper mapper) {
		this.filterRegistry = Objects.requireNonNull(filterRegistry, "Die FilterRegistry darf nicht null sein.");
		this.mapper = Objects.requireNonNull(mapper, "Der FilterCategoryMapper darf nicht null sein.");
	}

	/**
	 * Liefert eine Liste mit allen Filterkategorien zu einer Domain.
	 *
	 * @param domain die Filterdomain
	 *
	 * @return Liste von Filterkategorien
	 */
	public List<FilterCategoryResponse> getByDomain(final FilterDomain domain) {
		return filterRegistry.getCategoriesByDomain(domain).stream()
				.map(mapper::toApi)
				.toList();
	}

}
