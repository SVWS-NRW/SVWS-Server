package de.svws_nrw.mapper.schulwechsel.abgang;

import de.svws_nrw.core.data.schule.SchulwechselAbgang;
import de.svws_nrw.core.types.schule.StatusSchulwechselAbgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchulwechselAbgang;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schulwechsel.abgang.SchulwechselAbgangCreateRequest;
import de.svws_nrw.service.schulwechsel.abgang.SchulwechselAbgangPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct-Mapper für {@link DTOSchulwechselAbgang} ↔ {@link SchulwechselAbgang}.
 */
@Mapper(uses = JsonNullableMapper.class, imports = StatusSchulwechselAbgang.class)
public interface SchulwechselAbgangMapper {

	/** Instanz des Mappers */
	SchulwechselAbgangMapper INSTANCE = Mappers.getMapper(SchulwechselAbgangMapper.class);

	/**
	 * Mappt eine {@link DTOSchulwechselAbgang}-Entity auf das API-Modell {@link SchulwechselAbgang}.
	 *
	 * @param entity die Quell-Entity
	 * @return das befüllte API-Modell
	 */
	@Mapping(target = "idStatus", source = "status", qualifiedByName = "statusToId")
	SchulwechselAbgang toApi(DTOSchulwechselAbgang entity);

	/**
	 * Erstellt eine neue {@link DTOSchulwechselAbgang}-Entity aus einem {@link SchulwechselAbgangCreateRequest}.
	 * Die ID wird vom Repository gesetzt und hier ignoriert.
	 *
	 * @param dto die Eingabedaten aus dem Create-Request
	 * @return die neu erstellte Entity ohne ID
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "status", source = "idStatus", qualifiedByName = "idToStatus")
	DTOSchulwechselAbgang toDomain(SchulwechselAbgangCreateRequest dto);

	/**
	 * Wendet die Änderungen eines {@link SchulwechselAbgangPatchRequest} auf eine bestehende
	 * {@link DTOSchulwechselAbgang}-Entity an. Felder mit {@code undefined}-Wert werden nicht überschrieben.
	 *
	 * @param input   der Patch-Request mit den zu ändernden Feldern
	 * @param toPatch die zu aktualisierende Entity
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "idSchueler", ignore = true)
	@Mapping(target = "status", source = "idStatus", qualifiedByName = "idToStatus")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(SchulwechselAbgangPatchRequest input, @MappingTarget DTOSchulwechselAbgang toPatch);

	/**
	 * Konvertiert einen {@link StatusSchulwechselAbgang} in seine numerische ID.
	 *
	 * @param status der Status
	 * @return die ID des Status
	 */
	@Named("statusToId")
	default int statusToId(final StatusSchulwechselAbgang status) {
		return status.getId();
	}

	/**
	 * Konvertiert eine numerische Status-ID in den zugehörigen {@link StatusSchulwechselAbgang}.
	 *
	 * @param idStatus die ID des Status
	 * @return der zugehörige Status
	 */
	@Named("idToStatus")
	default StatusSchulwechselAbgang idToStatus(final int idStatus) {
		return StatusSchulwechselAbgang.getByIdOrNull(idStatus);
	}
}
