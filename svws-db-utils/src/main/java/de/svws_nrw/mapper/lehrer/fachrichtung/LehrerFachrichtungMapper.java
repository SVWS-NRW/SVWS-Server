package de.svws_nrw.mapper.lehrer.fachrichtung;

import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungEintrag;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtFachrichtung;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.lehrer.fachrichtung.LehrerFachrichtungCreateRequest;
import de.svws_nrw.service.lehrer.fachrichtung.LehrerFachrichtungPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JsonNullableMapper.class)
public interface LehrerFachrichtungMapper {

	/** LehrerFachrichtungMapper */
	LehrerFachrichtungMapper INSTANCE = Mappers.getMapper(LehrerFachrichtungMapper.class);

	/**
	 * Mappt eine {@link DTOLehrerPersonaldatenLehramtFachrichtung}-Entity auf das API-Modell {@link LehrerFachrichtungEintrag}.
	 *
	 * @param entity die Quell-Entity
	 * @return das befüllte DTO
	 */
	LehrerFachrichtungEintrag toApi(DTOLehrerPersonaldatenLehramtFachrichtung entity);

	/**
	 * Mappt einen {@link LehrerFachrichtungCreateRequest} auf eine neue {@link DTOLehrerPersonaldatenLehramtFachrichtung}-Entity.
	 * @param dto {@link LehrerFachrichtungCreateRequest}
	 * @return {@link DTOLehrerPersonaldatenLehramtFachrichtung}
	 */
	@Mapping(target = "id", ignore = true)
	DTOLehrerPersonaldatenLehramtFachrichtung toDomain(LehrerFachrichtungCreateRequest dto);

	/**
	 * Mappt einen {@link LehrerFachrichtungPatchRequest} auf eine {@link DTOLehrerPersonaldatenLehramtFachrichtung}-Entity
	 * @param dto {@link LehrerFachrichtungPatchRequest}
	 * @param entity {@link DTOLehrerPersonaldatenLehramtFachrichtung}
	 */
	@Mapping(target = "id", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(LehrerFachrichtungPatchRequest dto, @MappingTarget DTOLehrerPersonaldatenLehramtFachrichtung entity);

}
