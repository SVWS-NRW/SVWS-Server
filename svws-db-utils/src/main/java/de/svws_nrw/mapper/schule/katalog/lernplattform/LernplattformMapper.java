package de.svws_nrw.mapper.schule.katalog.lernplattform;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import de.svws_nrw.core.data.schule.Lernplattform;
import de.svws_nrw.db.dto.current.svws.auth.DTOLernplattformen;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schule.katalog.lernplattform.LernplattformCreateRequest;
import de.svws_nrw.service.schule.katalog.lernplattform.LernplattformPatchRequest;

/**
 * Mapper zwischen dem Datenbank-DTO {@link DTOLernplattformen}, dem Core-DTO {@link Lernplattform}
 * und den Requests zum Erstellen und Ändern von Lernplattformen.
 */
@Mapper(uses = JsonNullableMapper.class)
public interface LernplattformMapper {

	/** Instanz des Mappers */
	LernplattformMapper INSTANCE = Mappers.getMapper(LernplattformMapper.class);

	/**
	 * Mappt ein {@link DTOLernplattformen} auf das Core-DTO {@link Lernplattform}. Das Flag
	 * {@code referenziertInAnderenTabellen} wird nicht gesetzt, sondern vom Service bestimmt.
	 *
	 * @param entity   das Datenbank-DTO
	 *
	 * @return das Core-DTO
	 */
	@Mapping(target = "referenziertInAnderenTabellen", ignore = true)
	Lernplattform toApi(DTOLernplattformen entity);

	/**
	 * Mappt einen {@link LernplattformCreateRequest} auf ein neues {@link DTOLernplattformen}. Die ID wird
	 * erst beim Persistieren vergeben. Weitere Spalten der Tabelle werden nicht gesetzt.
	 *
	 * @param dto   der Request
	 *
	 * @return das neue Datenbank-DTO
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "benutzernameSuffixLehrer", ignore = true)
	@Mapping(target = "benutzernameSuffixErzieher", ignore = true)
	@Mapping(target = "benutzernameSuffixSchueler", ignore = true)
	@Mapping(target = "konfiguration", ignore = true)
	DTOLernplattformen toDomain(LernplattformCreateRequest dto);

	/**
	 * Wendet die Änderungen aus einem {@link LernplattformPatchRequest} auf ein bestehendes {@link DTOLernplattformen} an.
	 * Nur Felder, die im Request definiert sind, werden aktualisiert.
	 *
	 * @param dto      der Request mit den zu ändernden Feldern
	 * @param entity   das zu aktualisierende Datenbank-DTO
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "benutzernameSuffixLehrer", ignore = true)
	@Mapping(target = "benutzernameSuffixErzieher", ignore = true)
	@Mapping(target = "benutzernameSuffixSchueler", ignore = true)
	@Mapping(target = "konfiguration", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(LernplattformPatchRequest dto, @MappingTarget DTOLernplattformen entity);

}
