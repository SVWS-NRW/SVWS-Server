package de.svws_nrw.mapper.schule.katalog.merkmal;

import de.svws_nrw.core.data.schule.Merkmal;
import de.svws_nrw.db.dto.current.schild.schule.DTOMerkmale;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalCreateRequest;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JsonNullableMapper.class)
public interface MerkmalMapper {

	/** Instanz des Mappers */
	MerkmalMapper INSTANCE = Mappers.getMapper(MerkmalMapper.class);

	/**
	 * Map {@link DTOMerkmale} to {@link Merkmal}
	 *
	 * @param entity {@link DTOMerkmale}
	 *
	 * @return {@link Merkmal}
	 */
	Merkmal toApi(DTOMerkmale entity);

	/**
	 * Map {@link MerkmalCreateRequest} to {@link Merkmal}
	 *
	 * @param dto {@link MerkmalCreateRequest}
	 *
	 * @return {@link Merkmal}
	 */
	@Mapping(target = "id", ignore = true)
	DTOMerkmale toDomain(MerkmalCreateRequest dto);

	/**
	 * Wendet die Änderungen aus einem {@link MerkmalPatchRequest} auf ein bestehendes Entity an.
	 * Nur Felder, die im Request definiert sind (nicht undefined), werden aktualisiert.
	 *
	 * @param dto    das {@link MerkmalPatchRequest} mit den zu ändernden Feldern
	 * @param entity das zu aktualisierende {@link DTOMerkmale} Entity
	 */
	@Mapping(target = "id", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(MerkmalPatchRequest dto, @MappingTarget DTOMerkmale entity);
}
