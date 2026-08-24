package de.svws_nrw.mapper.schule.katalog.religion;

import java.util.Optional;

import de.svws_nrw.asd.types.schule.Religion;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.db.dto.current.schild.katalog.DTOReligion;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schule.katalog.religion.ReligionCreateRequest;
import de.svws_nrw.service.schule.katalog.religion.ReligionPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JsonNullableMapper.class)
public interface ReligionMapper {

	/** religionMapper */
	ReligionMapper INSTANCE = Mappers.getMapper(ReligionMapper.class);

	/**
	 * Mappt eine {@link DTOReligion}-Entity auf das API-Modell {@link ReligionEintrag}.
	 *
	 * @param entity die Quell-Entity
	 * @param schuljahr das aktuelle Schuljahr für CoreType-Lookups
	 * @return das befüllte DTO
	 */
	@Mapping(target = "referenziertInAnderenTabellen", ignore = true)
	@Mapping(target = "idReligion", source = "schluesselReligion", qualifiedByName = "mapSchluesselReligion")
	@Mapping(target = "sortierung", source = "sortierung", defaultValue = "32000")
	ReligionEintrag toApi(DTOReligion entity, @Context int schuljahr);

	/**
	 * Löst den Schlüssel der Religion auf die ID des CoreType-Eintrags auf.
	 *
	 * @param kuerzel das Schlüssel der Religion
	 * @param schuljahr das aktuelle Schuljahr für den {@link Religion}-Lookup
	 * @return die ID des Religion-Eintrags oder {@code null}
	 */
	@Named("mapSchluesselReligion")
	default Long mapSchluesselReligion(final String kuerzel, @Context final int schuljahr) {
		if (kuerzel == null) {
			return null;
		}

		return Optional.ofNullable(Religion.data().getEintragBySchuljahrUndSchluessel(schuljahr, kuerzel))
				.map(s -> s.id)
				.orElse(null);
	}

	/**
	 * Mappt einen {@link ReligionCreateRequest} auf eine neue {@link DTOReligion}-Entity.
	 *
	 * @param dto {@link ReligionCreateRequest}
	 * @return {@link DTOReligion}
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "istAenderbar", ignore = true)
	@Mapping(target = "bezeichnungExport", ignore = true)
	@Mapping(target = "schluesselReligion", source = "idReligion", qualifiedByName = "mapIdReligion")
	DTOReligion toDomain(ReligionCreateRequest dto);

	/**
	 * Mappt einen {@link ReligionPatchRequest} auf eine {@link DTOReligion}-Entity.
	 *
	 * @param dto {@link ReligionPatchRequest}
	 * @param entity {@link DTOReligion}
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "istAenderbar", ignore = true)
	@Mapping(target = "bezeichnungExport", ignore = true)
	@Mapping(target = "schluesselReligion", source = "idReligion", qualifiedByName = "mapIdReligion")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(ReligionPatchRequest dto, @MappingTarget DTOReligion entity);

	/**
	 * Löst die ID der Religion auf den Schlüssel des CoreType-Eintrags auf.
	 *
	 * @param idReligion die ID der Religion
	 *
	 * @return der Schlüssel des Religion-Eintrags
	 */
	@Named("mapIdReligion")
	default String mapIdReligion(final Long idReligion) {
		return Religion.data().getEintragByID(idReligion).schluessel;
	}
}
