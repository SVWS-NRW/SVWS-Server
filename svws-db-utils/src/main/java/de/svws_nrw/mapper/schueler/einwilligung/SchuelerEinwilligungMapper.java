package de.svws_nrw.mapper.schueler.einwilligung;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import de.svws_nrw.core.data.schueler.SchuelerEinwilligung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutz;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schueler.einwilligung.SchuelerEinwilligungPatchRequest;

/**
 * MapStruct-Mapper für die Einwilligung eines Schülers zu einer Einwilligungsart: mappt DB-DTOs in das Core-DTO,
 * erstellt neue DB-DTOs und wendet Patch-Requests auf bestehende DB-DTOs an.
 */
@Mapper(uses = JsonNullableMapper.class)
public interface SchuelerEinwilligungMapper {

	/** Instanz des Mappers */
	SchuelerEinwilligungMapper INSTANCE = Mappers.getMapper(SchuelerEinwilligungMapper.class);

	/**
	 * Mappt ein {@link DTOSchuelerDatenschutz} in das Core-DTO {@link SchuelerEinwilligung}. Fehlende Werte
	 * für Status und Abfrage werden als {@code false} abgebildet.
	 *
	 * @param entity   die Einwilligung des Schülers
	 *
	 * @return das Core-DTO
	 */
	@Mapping(target = "idSchueler", source = "Schueler_ID")
	@Mapping(target = "idEinwilligungsart", source = "Datenschutz_ID")
	@Mapping(target = "status", source = "Status")
	@Mapping(target = "abgefragt", source = "Abgefragt")
	SchuelerEinwilligung toApi(DTOSchuelerDatenschutz entity);

	/**
	 * Erstellt ein neues {@link DTOSchuelerDatenschutz}, bei dem die Einwilligung weder erteilt noch abgefragt ist.
	 *
	 * @param idSchueler           die ID des Schülers
	 * @param idEinwilligungsart   die ID der Einwilligungsart
	 *
	 * @return die neue Einwilligung
	 */
	default DTOSchuelerDatenschutz toDomain(final long idSchueler, final long idEinwilligungsart) {
		return new DTOSchuelerDatenschutz(idSchueler, idEinwilligungsart, false, false);
	}

	/**
	 * Wendet einen Patch-Request auf ein bestehendes {@link DTOSchuelerDatenschutz} an. Nicht gesetzte Felder (undefined)
	 * werden nicht überschrieben. Die ID der Einwilligung wird nicht verändert.
	 *
	 * @param input     der Patch-Request mit den zu ändernden Feldern
	 * @param toPatch   die zu aktualisierende Einwilligung
	 */
	@Mapping(target = "Schueler_ID", ignore = true)
	@Mapping(target = "Datenschutz_ID", ignore = true)
	@Mapping(target = "Status", source = "status")
	@Mapping(target = "Abgefragt", source = "abgefragt")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(SchuelerEinwilligungPatchRequest input, @MappingTarget DTOSchuelerDatenschutz toPatch);

}
