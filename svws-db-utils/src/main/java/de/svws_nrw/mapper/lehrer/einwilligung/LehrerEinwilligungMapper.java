package de.svws_nrw.mapper.lehrer.einwilligung;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import de.svws_nrw.core.data.lehrer.LehrerEinwilligung;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerDatenschutz;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.lehrer.einwilligung.LehrerEinwilligungPatchRequest;

/**
 * MapStruct-Mapper für die Einwilligung eines Lehrers zu einer Einwilligungsart: mappt DB-DTOs in das Core-DTO,
 * erstellt neue DB-DTOs und wendet Patch-Requests auf bestehende DB-DTOs an.
 */
@Mapper(uses = JsonNullableMapper.class)
public interface LehrerEinwilligungMapper {

	/** Instanz des Mappers */
	LehrerEinwilligungMapper INSTANCE = Mappers.getMapper(LehrerEinwilligungMapper.class);

	/**
	 * Mappt ein {@link DTOLehrerDatenschutz} in das Core-DTO {@link LehrerEinwilligung}. Fehlende Werte
	 * für Status und Abfrage werden als {@code false} abgebildet.
	 *
	 * @param entity   die Einwilligung des Lehrers
	 *
	 * @return das Core-DTO
	 */
	@Mapping(target = "idLehrer", source = "LehrerID")
	@Mapping(target = "idEinwilligungsart", source = "DatenschutzID")
	@Mapping(target = "istZugestimmt", source = "Status")
	@Mapping(target = "istAbgefragt", source = "Abgefragt")
	LehrerEinwilligung toApi(DTOLehrerDatenschutz entity);

	/**
	 * Erstellt ein neues {@link DTOLehrerDatenschutz}, bei dem die Einwilligung weder erteilt noch abgefragt ist.
	 *
	 * @param idLehrer           die ID des Lehrers
	 * @param idEinwilligungsart   die ID der Einwilligungsart
	 *
	 * @return die neue Einwilligung
	 */
	default DTOLehrerDatenschutz toDomain(final long idLehrer, final long idEinwilligungsart) {
		return new DTOLehrerDatenschutz(idLehrer, idEinwilligungsart, false, false);
	}

	/**
	 * Wendet einen Patch-Request auf ein bestehendes {@link DTOLehrerDatenschutz} an. Nicht gesetzte Felder (undefined)
	 * werden nicht überschrieben. Die ID der Einwilligung wird nicht verändert.
	 *
	 * @param input     der Patch-Request mit den zu ändernden Feldern
	 * @param toPatch   die zu aktualisierende Einwilligung
	 */
	@Mapping(target = "LehrerID", ignore = true)
	@Mapping(target = "DatenschutzID", ignore = true)
	@Mapping(target = "Status", source = "istZugestimmt")
	@Mapping(target = "Abgefragt", source = "istAbgefragt")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(LehrerEinwilligungPatchRequest input, @MappingTarget DTOLehrerDatenschutz toPatch);

}
