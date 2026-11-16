package de.svws_nrw.mapper.filter;

import de.svws_nrw.core.data.filter.FilterAttributeResponse;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.filter.FilterAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper für die Abbildung von Filterattributen auf das zugehörige Response-DTO.
 */
@Mapper(uses = JsonNullableMapper.class)
public interface FilterAttributeMapper {

	/** Die Singleton-Instanz des Mappers */
	FilterAttributeMapper INSTANCE = Mappers.getMapper(FilterAttributeMapper.class);

	/**
	 * Mappt ein {@link FilterAttribute} Objekt zu einem {@link FilterAttributeResponse} Objekt.
	 *
	 * @param domain Das Domain-Objekt
	 * @return Das API-Objekt
	 */
	@Mapping(target = "key", source = "key.value")
	@Mapping(target = "keyCategory", source = "keyCategory.value")
	@Mapping(target = "type", source = "type.key")
	FilterAttributeResponse toApi(FilterAttribute domain);

}
