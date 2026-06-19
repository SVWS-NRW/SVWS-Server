package de.svws_nrw.mapper.schule.schulleitung;

import de.svws_nrw.asd.data.schule.Schulleitung;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOSchulleitung;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schule.schulleitung.SchulleitungCreateRequest;
import de.svws_nrw.service.schule.schulleitung.SchulleitungPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JsonNullableMapper.class)
public interface SchulleitungMapper {

	/** Instanz des Mappers */
	SchulleitungMapper INSTANCE = Mappers.getMapper(SchulleitungMapper.class);

	/**
	 * Map {@link DTOSchulleitung} to {@link Schulleitung}
	 *
	 * @param entity {@link DTOSchulleitung}
	 *
	 * @return {@link Schulleitung}
	 */
	@Mapping(source = "ID",                 target = "id")
	@Mapping(source = "LeitungsfunktionID", target = "idLeitungsfunktion")
	@Mapping(source = "Funktionstext",      target = "bezeichnung")
	@Mapping(source = "LehrerID",           target = "idLehrer")
	@Mapping(source = "Von",                target = "datumBeginnLeitungsfunktion")
	@Mapping(source = "Bis",                target = "datumEndeLeitungsfunktion")
	Schulleitung toApi(DTOSchulleitung entity);

	/**
	 * Map {@link SchulleitungCreateRequest} to {@link Schulleitung}
	 *
	 * @param dto {@link SchulleitungCreateRequest}
	 *
	 * @return {@link Schulleitung}
	 */
	@Mapping(target = "ID",                 ignore = true)
	@Mapping(source = "idLeitungsfunktion", target = "LeitungsfunktionID")
	@Mapping(source = "bezeichnung",        target = "Funktionstext")
	@Mapping(source = "idLehrer",           target = "LehrerID")
	@Mapping(source = "datumBeginnLeitungsfunktion",                target = "Von")
	@Mapping(source = "datumEndeLeitungsfunktion",                target = "Bis")
	DTOSchulleitung toDomain(SchulleitungCreateRequest dto);

	/**
	 * Wendet die Änderungen aus einem {@link SchulleitungPatchRequest} auf ein bestehendes Entity an.
	 * Nur Felder, die im Request definiert sind (nicht undefined), werden aktualisiert.
	 *
	 * @param dto    das {@link SchulleitungPatchRequest} mit den zu ändernden Feldern
	 * @param entity das zu aktualisierende {@link DTOSchulleitung} Entity
	 */
	@Mapping(target = "ID",                 ignore = true)
	@Mapping(source = "idLeitungsfunktion", target = "LeitungsfunktionID")
	@Mapping(source = "bezeichnung",        target = "Funktionstext")
	@Mapping(target = "LehrerID",           ignore = true)
	@Mapping(source = "datumBeginnLeitungsfunktion",                target = "Von")
	@Mapping(source = "datumEndeLeitungsfunktion",           		target = "Bis")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(SchulleitungPatchRequest dto, @MappingTarget DTOSchulleitung entity);
}
