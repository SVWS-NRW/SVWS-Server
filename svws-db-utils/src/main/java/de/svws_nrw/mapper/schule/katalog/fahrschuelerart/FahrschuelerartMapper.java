package de.svws_nrw.mapper.schule.katalog.fahrschuelerart;

import de.svws_nrw.core.data.schule.Fahrschuelerart;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFahrschuelerart;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schule.katalog.fahrschuelerart.FahrschuelerartCreateRequest;
import de.svws_nrw.service.schule.katalog.fahrschuelerart.FahrschuelerartPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * Mapper zwischen dem Datenbank-DTO {@link DTOFahrschuelerart}, dem Core-DTO {@link Fahrschuelerart}
 * und den Requests zum Erstellen und Ändern von Fahrschülerarten.
 */
@Mapper(uses = JsonNullableMapper.class)
public interface FahrschuelerartMapper {

	/** Instanz des Mappers */
	FahrschuelerartMapper INSTANCE = Mappers.getMapper(FahrschuelerartMapper.class);

	/**
	 * Mappt ein {@link DTOFahrschuelerart} auf das Core-DTO {@link Fahrschuelerart}. Das Flag
	 * {@code referenziertInAnderenTabellen} wird nicht gesetzt, sondern vom Service bestimmt.
	 *
	 * @param entity   das Datenbank-DTO
	 *
	 * @return das Core-DTO
	 */
	@Mapping(target = "referenziertInAnderenTabellen", ignore = true)
	Fahrschuelerart toApi(DTOFahrschuelerart entity);

	/**
	 * Mappt einen {@link FahrschuelerartCreateRequest} auf ein neues {@link DTOFahrschuelerart}. Die ID wird
	 * erst beim Persistieren vergeben. Weitere Spalten der Tabelle werden nicht gesetzt.
	 *
	 * @param dto   der Request
	 *
	 * @return das neue Datenbank-DTO
	 */
	@Mapping(target = "id", ignore = true)
	DTOFahrschuelerart toDomain(FahrschuelerartCreateRequest dto);

	/**
	 * Wendet die Änderungen aus einem {@link FahrschuelerartPatchRequest} auf ein bestehendes {@link DTOFahrschuelerart} an.
	 * Nur Felder, die im Request definiert sind, werden aktualisiert.
	 *
	 * @param dto      der Request mit den zu ändernden Feldern
	 * @param entity   das zu aktualisierende Datenbank-DTO
	 */
	@Mapping(target = "id", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(FahrschuelerartPatchRequest dto, @MappingTarget DTOFahrschuelerart entity);
}
