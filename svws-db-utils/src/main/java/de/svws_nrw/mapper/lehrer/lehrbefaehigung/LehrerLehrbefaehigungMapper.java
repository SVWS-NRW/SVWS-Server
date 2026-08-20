package de.svws_nrw.mapper.lehrer.lehrbefaehigung;

import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtBefaehigung;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.lehrer.lehrbefaehigung.LehrerLehrbefaehigungCreateRequest;
import de.svws_nrw.service.lehrer.lehrbefaehigung.LehrerLehrbefaehigungPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JsonNullableMapper.class)
public interface LehrerLehrbefaehigungMapper {

	/** LehrerLehrbefaehigungMapper */
	LehrerLehrbefaehigungMapper INSTANCE = Mappers.getMapper(LehrerLehrbefaehigungMapper.class);

	/**
	 * Mappt eine {@link DTOLehrerPersonaldatenLehramtBefaehigung}-Entity auf das API-Modell {@link LehrerLehrbefaehigungEintrag}.
	 *
	 * @param entity die Quell-Entity
	 * @return das befüllte DTO
	 */
	LehrerLehrbefaehigungEintrag toApi(DTOLehrerPersonaldatenLehramtBefaehigung entity);

	/**
	 * Mappt einen {@link LehrerLehrbefaehigungCreateRequest} auf eine neue {@link DTOLehrerPersonaldatenLehramtBefaehigung}-Entity.
	 * @param dto {@link LehrerLehrbefaehigungCreateRequest}
	 * @return {@link DTOLehrerPersonaldatenLehramtBefaehigung}
	 */
	@Mapping(target = "id", ignore = true)
	DTOLehrerPersonaldatenLehramtBefaehigung toDomain(LehrerLehrbefaehigungCreateRequest dto);

	/**
	 * Mappt einen {@link LehrerLehrbefaehigungPatchRequest} auf eine {@link DTOLehrerPersonaldatenLehramtBefaehigung}-Entity
	 * @param dto {@link LehrerLehrbefaehigungPatchRequest}
	 * @param entity {@link DTOLehrerPersonaldatenLehramtBefaehigung}
	 */
	@Mapping(target = "id", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(LehrerLehrbefaehigungPatchRequest dto, @MappingTarget DTOLehrerPersonaldatenLehramtBefaehigung entity);

}
