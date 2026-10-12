package de.svws_nrw.mapper.filter;

import de.svws_nrw.core.data.filter.CatalogFilterAttributeResponse;
import de.svws_nrw.core.data.filter.CoreTypeFilterAttributeResponse;
import de.svws_nrw.core.data.filter.EnumFilterAttributeResponse;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.filter.CatalogFilterAttribute;
import de.svws_nrw.service.filter.CoreTypeFilterAttribute;
import de.svws_nrw.service.filter.EnumFilterAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper für die Abbildung von Filterattributen auf die zugehörigen Response-DTOs.
 */
@Mapper(uses = JsonNullableMapper.class)
public interface FilterAttributeMapper {

	/** Die Singleton-Instanz des Mappers */
	FilterAttributeMapper INSTANCE = Mappers.getMapper(FilterAttributeMapper.class);

	/**
	 * Mappt ein {@link EnumFilterAttribute} Objekt zu einem {@link EnumFilterAttributeResponse} Objekt.
	 *
	 * @param domain Das Domain-Objekt
	 * @return Das API-Objekt
	 */
	@Mapping(target = "key", source = "key.value")
	@Mapping(target = "keyCategory", source = "keyCategory.value")
	EnumFilterAttributeResponse toApi(EnumFilterAttribute domain);

	/**
	 * Mappt ein {@link CatalogFilterAttribute} Objekt zu einem {@link CatalogFilterAttributeResponse} Objekt.
	 *
	 * @param domain Das Domain-Objekt
	 * @return Das API-Objekt
	 */
	@Mapping(target = "key", source = "key.value")
	@Mapping(target = "keyCategory", source = "keyCategory.value")
	CatalogFilterAttributeResponse toApi(CatalogFilterAttribute domain);

	/**
	 * Mappt ein {@link CoreTypeFilterAttribute} Objekt zu einem {@link CoreTypeFilterAttributeResponse} Objekt.
	 *
	 * @param domain Das Domain-Objekt
	 * @return Das API-Objekt
	 */
	@Mapping(target = "key", source = "key.value")
	@Mapping(target = "keyCategory", source = "keyCategory.value")
	CoreTypeFilterAttributeResponse toApi(CoreTypeFilterAttribute domain);

}
