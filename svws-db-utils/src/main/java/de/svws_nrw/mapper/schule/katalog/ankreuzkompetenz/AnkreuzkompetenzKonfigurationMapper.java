package de.svws_nrw.mapper.schule.katalog.ankreuzkompetenz;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import de.svws_nrw.core.data.kataloge.AnkreuzkompetenzKonfiguration;
import de.svws_nrw.core.data.schule.AnkreuzkompetenzJahrgangszuordnung;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzdaten;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzKonfigurationPatchRequest;

/**
 * Mapper für die Konfiguration zu den Ankreuzkompetenzen
 */
@Mapper(uses = JsonNullableMapper.class)
public interface AnkreuzkompetenzKonfigurationMapper {

	/** mapper */
	AnkreuzkompetenzKonfigurationMapper INSTANCE = Mappers.getMapper(AnkreuzkompetenzKonfigurationMapper.class);

	/**
	 * Mappt eine {@link DTOAnkreuzdaten}-Entity auf das API-Modell {@link AnkreuzkompetenzJahrgangszuordnung}.
	 *
	 * @param entity die Quell-Entity
	 * @return das befüllte DTO
	 */
	@Mapping(target = "textStufen", ignore = true)
	@Mapping(target = "textSonstiges", source = "BezeichnungSONST")
	AnkreuzkompetenzKonfiguration toApi(DTOAnkreuzdaten entity);

	/**
	 * Aktualisiert eine bestehende Konfiguration für die Ankreuzdaten basierend auf dem PatchRequest.
	 *
	 * @param dto      der Patch-Request {@link AnkreuzkompetenzKonfigurationPatchRequest}
	 * @param entity   die zu aktualisierende Ziel-Entity {@link DTOAnkreuzdaten}
	 */
	@Mapping(target = "ID", ignore = true)
	@Mapping(target = "BezeichnungSONST", source = "textSonstiges")
	@Mapping(target = "TextStufe1", ignore = true)
	@Mapping(target = "TextStufe2", ignore = true)
	@Mapping(target = "TextStufe3", ignore = true)
	@Mapping(target = "TextStufe4", ignore = true)
	@Mapping(target = "TextStufe5", ignore = true)
	void patch(AnkreuzkompetenzKonfigurationPatchRequest dto, @MappingTarget DTOAnkreuzdaten entity);


	/**
	 * Manuelles Mapping für das Array
	 *
	 * @param entity   die Entität
	 * @param target   die Konfiguration der Ankreuzkompetenzen
	 */
	@AfterMapping
	default void mapTextStufenToApi(final DTOAnkreuzdaten entity, @MappingTarget final AnkreuzkompetenzKonfiguration target) {
		if (entity == null) {
			return;
		}
		target.textStufen = new String[] {
			entity.TextStufe1,
			entity.TextStufe2,
			entity.TextStufe3,
			entity.TextStufe4,
			entity.TextStufe5
		};
	}

	/**
	 * Manuelles Mapping für das Array aus dem Patch-Request
	 *
	 * @param dto      der Patch-Request
	 * @param entity   die Entität
	 */
	@AfterMapping
	default void mapTextStufenToEntity(final AnkreuzkompetenzKonfigurationPatchRequest dto, @MappingTarget final DTOAnkreuzdaten entity) {
		if ((dto == null) || (dto.textStufen) == null || (!dto.textStufen.isPresent())) {
			return;
		}
		final String[] stufen = dto.textStufen.get();
		if (stufen != null) {
			entity.TextStufe1 = stufen[0];
			entity.TextStufe2 = stufen[1];
			entity.TextStufe3 = stufen[2];
			entity.TextStufe4 = stufen[3];
			entity.TextStufe5 = stufen[4];
		}
	}
}
