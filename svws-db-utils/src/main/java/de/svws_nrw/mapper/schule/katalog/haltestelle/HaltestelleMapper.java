package de.svws_nrw.mapper.schule.katalog.haltestelle;

import de.svws_nrw.core.data.schule.Haltestelle;
import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schule.katalog.haltestelle.HaltestelleCreateRequest;
import de.svws_nrw.service.schule.katalog.haltestelle.HaltestellePatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JsonNullableMapper.class)
public interface HaltestelleMapper {

	/** Instanz des Mappers */
	HaltestelleMapper INSTANCE = Mappers.getMapper(HaltestelleMapper.class);

	/**
	 * Mappt eine {@link DTOHaltestellen}-Entity auf das API-Modell {@link Haltestelle}.
	 *
	 * @param entity die Quell-Entity
	 *
	 * @return das befüllte Core-DTO
	 */
	@Mapping(target = "referenziertInAnderenTabellen", ignore = true)
	@Mapping(target = "sortierung", source = "sortierung")
	Haltestelle toApi(DTOHaltestellen entity);

	/**
	 * Mappt einen {@link HaltestelleCreateRequest} auf eine neue {@link DTOHaltestellen}-Entity.
	 *
	 * @param dto der {@link HaltestelleCreateRequest}
	 *
	 * @return die neue {@link DTOHaltestellen}-Entity
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "istAenderbar", ignore = true)
	DTOHaltestellen toDomain(HaltestelleCreateRequest dto);

	/**
	 * Wendet die Änderungen aus einem {@link HaltestellePatchRequest} auf eine bestehende {@link DTOHaltestellen}-Entity an.
	 * Nur Felder, die im Request definiert sind (nicht undefined), werden aktualisiert.
	 *
	 * @param dto    der {@link HaltestellePatchRequest} mit den zu ändernden Feldern
	 * @param entity die zu aktualisierende {@link DTOHaltestellen}-Entity
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "istAenderbar", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(HaltestellePatchRequest dto, @MappingTarget DTOHaltestellen entity);

}
