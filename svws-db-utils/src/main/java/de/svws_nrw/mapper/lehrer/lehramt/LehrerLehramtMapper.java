package de.svws_nrw.mapper.lehrer.lehramt;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramt;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.lehrer.lehramt.LehrerLehramtCreateRequest;
import de.svws_nrw.service.lehrer.lehramt.LehrerLehramtPatchRequest;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JsonNullableMapper.class)
public interface LehrerLehramtMapper {

	/** LehrerLehramtMapper */
	LehrerLehramtMapper INSTANCE = Mappers.getMapper(LehrerLehramtMapper.class);

	/**
	 * Mappt eine {@link DTOLehrerPersonaldatenLehramt}-Entity auf das API-Modell {@link LehrerLehramtEintrag}.
	 *
	 * @param entity die Quell-Entity
	 * @param ctx    der Mapping-Kontext
	 * @return das befüllte DTO
	 */
	LehrerLehramtEintrag toApi(
			DTOLehrerPersonaldatenLehramt entity,
			@Context LehrerLehramtMappingContext ctx
	);

	/**
	 * Setzt nach dem Mapping die Fachrichtungen und Lehrbefaehigungen aus dem {@link LehrerLehramtMappingContext}.
	 *
	 * @param ctx    der Kontext mit den Listen
	 * @param target das bereits gemappte Zielobjekt
	 */
	@AfterMapping
	default void mapListen(
			@Context final LehrerLehramtMappingContext ctx,
			@MappingTarget final LehrerLehramtEintrag target) {
		target.fachrichtungen.addAll(ctx.fachrichtungen());
		target.lehrbefaehigungen.addAll(ctx.lehrbefaehigungen());
	}

	/**
	 * Mappt einen {@link LehrerLehramtCreateRequest} auf eine neue {@link DTOLehrerPersonaldatenLehramt}-Entity.
	 * @param dto {@link LehrerLehramtCreateRequest}
	 * @return {@link DTOLehrerPersonaldatenLehramt}
	 */
	@Mapping(target = "id", ignore = true)
	DTOLehrerPersonaldatenLehramt toDomain(LehrerLehramtCreateRequest dto);

	/**
	 * Mappt einen {@link LehrerLehramtPatchRequest} auf eine {@link DTOLehrerPersonaldatenLehramt}-Entity
	 * @param dto {@link LehrerLehramtPatchRequest}
	 * @param entity {@link DTOLehrerPersonaldatenLehramt}
	 */
	@Mapping(target = "id", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(LehrerLehramtPatchRequest dto, @MappingTarget DTOLehrerPersonaldatenLehramt entity);

}
