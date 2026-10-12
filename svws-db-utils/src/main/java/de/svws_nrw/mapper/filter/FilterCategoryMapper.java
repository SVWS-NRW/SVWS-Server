package de.svws_nrw.mapper.filter;

import java.util.List;

import de.svws_nrw.core.data.filter.FilterCategoryResponse;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.filter.FilterAttributeKey;
import de.svws_nrw.service.filter.FilterCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * Mapper für die Abbildung von Filterkategorien auf das zugehörige Response-DTO.
 */
@Mapper(uses = JsonNullableMapper.class)
public interface FilterCategoryMapper {

	/** Die Singleton-Instanz des Mappers */
	FilterCategoryMapper INSTANCE = Mappers.getMapper(FilterCategoryMapper.class);

	/**
	 * Mappt ein {@link FilterCategory} Objekt zu einem {@link FilterCategoryResponse} Objekt.
	 *
	 * @param domain Das Domain-Objekt
	 * @return Das API-Objekt
	 */
	@Mapping(target = "key", source = "key.value")
	@Mapping(target = "attributes", source = "attributes", qualifiedByName = "fromAttributeKeys")
	FilterCategoryResponse toApi(FilterCategory domain);

	/**
	 * Mappt eine Liste mit {@link FilterAttributeKey} zu einer Liste mit {@link String} als Schlüssel.
	 *
	 * @param attributes {@link List<FilterAttributeKey>}
	 * @return Liste mit Attribut Schlüsseln als {@link String}
	 */
	@Named("fromAttributeKeys")
	default List<String> fromAttributeKeys(final List<FilterAttributeKey> attributes) {
		return attributes.stream().map(FilterAttributeKey::value).toList();
	}

}
