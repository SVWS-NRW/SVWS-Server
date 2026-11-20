package de.svws_nrw.mapper.schule.katalog.ort;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.svws_nrw.asd.types.schule.Laender;
import de.svws_nrw.core.data.filter.FilterCatalogValue;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schule.katalog.ort.OrtCreateRequest;
import de.svws_nrw.service.schule.katalog.ort.OrtPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JsonNullableMapper.class)
public interface OrtMapper {

	/** ortMapper */
	OrtMapper INSTANCE = Mappers.getMapper(OrtMapper.class);

	/**
	 * Mappt eine {@link DTOOrt}-Entity auf das API-Modell {@link OrtKatalogEintrag}.
	 *
	 * @param entity die Quell-Entity
	 * @param schuljahr das aktuelle Schuljahr für CoreType-Lookups
	 * @return das befüllte DTO
	 */
	@Mapping(target = "referenziertInAnderenTabellen", ignore = true)
	@Mapping(source = "schluesselBundesland", target = "idBundesland", qualifiedByName = "mapBundesLand")
	@Mapping(target = "sortierung", source = "sortierung", defaultValue = "32000")
	@Mapping(target = "istSichtbar", source = "istSichtbar")
	@Mapping(target = "istAenderbar", source = "istAenderbar")
	OrtKatalogEintrag toApi(DTOOrt entity, @Context int schuljahr);

	/**
	 * Löst den Schlüssel des Bundeslandes auf die ID auf.
	 *
	 * @param schluesselBundesland Schlüssel des Bundeslands
	 * @param schuljahr das aktuelle Schuljahr für den {@link Laender}-Lookup
	 * @return die ID des Bundeslands
	 */
	@Named("mapBundesLand")
	default Long mapBundesLand(final String schluesselBundesland, @Context final int schuljahr) {
		if (schluesselBundesland == null) {
			return null;
		}

		return Optional.ofNullable(Laender.data().getEintragBySchuljahrUndSchluessel(schuljahr, schluesselBundesland))
				.map(s -> s.id)
				.orElse(null);
	}

	/**
	 * Mappt eine {@link DTOOrt}-Entity auf einen {@link FilterCatalogValue}. Die Bezeichnung besteht aus Postleitzahl und Ortsname.
	 *
	 * @param entity die Quell-Entity
	 *
	 * @return der Wert für die Auswahl in Filtern
	 */
	@Mapping(target = "bezeichnung", source = "entity", qualifiedByName = "mapFilterBezeichnung")
	FilterCatalogValue toFilterCatalogValue(DTOOrt entity);

	/**
	 * Ermittelt die Bezeichnung eines Ortes für die Auswahl in Filtern aus Postleitzahl und Ortsname. Fehlende Bestandteile werden ausgelassen.
	 *
	 * @param entity die Entity des Ortes
	 *
	 * @return die Bezeichnung, z. B. {@code 53840 Troisdorf}
	 */
	@Named("mapFilterBezeichnung")
	default String mapFilterBezeichnung(final DTOOrt entity) {
		return Stream.of(entity.plz, entity.ortsname)
				.filter(Objects::nonNull)
				.map(String::strip)
				.filter(teil -> !teil.isEmpty())
				.collect(Collectors.joining(" "));
	}

	/**
	 * Mappt einen {@link OrtCreateRequest} auf eine neue {@link DTOOrt}-Entity.
	 * @param dto {@link OrtCreateRequest}
	 * @return {@link DTOOrt}
	 */
	@Mapping(target = "schluesselBundesland", ignore = true)
	@Mapping(target = "id", ignore = true)
	DTOOrt toDomain(OrtCreateRequest dto);

	/**
	 * Mappt einen {@link OrtPatchRequest} auf eine {@link DTOOrt}-Entity
	 * @param dto {@link OrtPatchRequest}
	 * @param entity {@link DTOOrt}
	 */
	@Mapping(target = "schluesselBundesland", ignore = true)
	@Mapping(target = "id", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(OrtPatchRequest dto, @MappingTarget DTOOrt entity);

}
