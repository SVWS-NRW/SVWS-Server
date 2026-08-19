package de.svws_nrw.mapper.schule.katalog.ortsteil;


import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schule.katalog.ortsteil.OrtsteilCreateRequest;
import de.svws_nrw.service.schule.katalog.ortsteil.OrtsteilPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JsonNullableMapper.class)
public interface OrtsteilMapper {

	/** ortMapper */
	OrtsteilMapper INSTANCE = Mappers.getMapper(OrtsteilMapper.class);

	/**
	 * Mappt eine {@link DTOOrtsteil}-Entity auf das API-Modell {@link OrtKatalogEintrag}.
	 *
	 * @param ortsteil die Quell-Entity
	 * @param ort der zugehörige Ort
	 * @return das befüllte DTorttei
	 */
	@Mapping(target = "id", source = "ortsteil.id")
	@Mapping(target = "sortierung", source = "ortsteil.sortierung", defaultValue = "32000")
	@Mapping(target = "bezeichnungOrt", source = "ort.ortsname")
	@Mapping(target = "istSichtbar", source = "ortsteil.istSichtbar")
	@Mapping(target = "istAenderbar", source = "ortsteil.istAenderbar")
	@Mapping(target = "plzOrt", source = "ort.plz")
	@Mapping(target = "referenziertInAnderenTabellen", ignore = true)
	OrtsteilKatalogEintrag toApi(DTOOrtsteil ortsteil, DTOOrt ort);

	/**
	 * Mappt einen {@link OrtsteilCreateRequest} auf eine neue {@link DTOOrtsteil}-Entity.
	 * @param dto {@link OrtsteilCreateRequest}
	 * @return {@link DTOOrtsteil}
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "schluesselOrtsteil", ignore = true)
	DTOOrtsteil toDomain(OrtsteilCreateRequest dto);

	/**
	 * Mappt einen {@link OrtsteilPatchRequest} auf eine {@link DTOOrtsteil}-Entity
	 * @param dto {@link OrtsteilPatchRequest}
	 * @param entity {@link DTOOrtsteil}
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "schluesselOrtsteil", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(OrtsteilPatchRequest dto, @MappingTarget DTOOrtsteil entity);

}
