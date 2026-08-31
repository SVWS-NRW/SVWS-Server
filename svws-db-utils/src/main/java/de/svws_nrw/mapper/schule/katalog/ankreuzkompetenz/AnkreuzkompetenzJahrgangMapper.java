package de.svws_nrw.mapper.schule.katalog.ankreuzkompetenz;

import de.svws_nrw.core.data.schule.AnkreuzkompetenzJahrgangszuordnung;
import de.svws_nrw.db.dto.current.katalog.DTOAnkreuzkompetenzJahrgang;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzJahrgangCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JsonNullableMapper.class)
public interface AnkreuzkompetenzJahrgangMapper {

	/** mapper */
	AnkreuzkompetenzJahrgangMapper INSTANCE = Mappers.getMapper(AnkreuzkompetenzJahrgangMapper.class);

	/**
	 * Mappt eine {@link DTOAnkreuzkompetenzJahrgang}-Entity auf das API-Modell {@link AnkreuzkompetenzJahrgangszuordnung}.
	 *
	 * @param entity die Quell-Entity
	 * @return das befüllte DTO
	 */
	AnkreuzkompetenzJahrgangszuordnung toApi(DTOAnkreuzkompetenzJahrgang entity);

	/**
	 * Mappt einen {@link AnkreuzkompetenzJahrgangCreateRequest} auf eine neue {@link DTOAnkreuzkompetenzJahrgang}-Entity.
	 *
	 * @param dto {@link AnkreuzkompetenzJahrgangCreateRequest}
	 * @return {@link DTOAnkreuzkompetenzJahrgang}
	 */
	@Mapping(target = "id", ignore = true)
	DTOAnkreuzkompetenzJahrgang toDomain(AnkreuzkompetenzJahrgangCreateRequest dto);

}
