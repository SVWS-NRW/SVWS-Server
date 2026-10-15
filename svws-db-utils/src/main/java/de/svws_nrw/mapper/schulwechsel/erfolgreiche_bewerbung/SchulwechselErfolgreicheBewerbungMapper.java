package de.svws_nrw.mapper.schulwechsel.erfolgreiche_bewerbung;

import de.svws_nrw.core.data.schule.SchulwechselErfolgreicheBewerbung;
import de.svws_nrw.core.types.schule.StatusSchulwechselErfolgreicheBewerbung;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchulwechselErfolgreicheBewerbung;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schulwechsel.erfolgreiche_bewerbung.SchulwechselErfolgreicheBewerbungCreateRequest;
import de.svws_nrw.service.schulwechsel.erfolgreiche_bewerbung.SchulwechselErfolgreicheBewerbungPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct-Mapper für {@link DTOSchulwechselErfolgreicheBewerbung} ↔ {@link SchulwechselErfolgreicheBewerbung}.
 */
@Mapper(uses = JsonNullableMapper.class, imports = StatusSchulwechselErfolgreicheBewerbung.class)
public interface SchulwechselErfolgreicheBewerbungMapper {

	/** Instanz des Mappers */
	SchulwechselErfolgreicheBewerbungMapper INSTANCE = Mappers.getMapper(SchulwechselErfolgreicheBewerbungMapper.class);

	/**
	 * Mappt eine {@link DTOSchulwechselErfolgreicheBewerbung}-Entity auf das API-Modell {@link SchulwechselErfolgreicheBewerbung}.
	 *
	 * @param entity die Quell-Entity
	 * @return das befüllte API-Modell
	 */
	@Mapping(target = "idStatus", source = "status", qualifiedByName = "statusToId")
	SchulwechselErfolgreicheBewerbung toApi(DTOSchulwechselErfolgreicheBewerbung entity);

	/**
	 * Erstellt eine neue {@link DTOSchulwechselErfolgreicheBewerbung}-Entity aus einem {@link SchulwechselErfolgreicheBewerbungCreateRequest}.
	 * Die ID wird vom Repository gesetzt und hier ignoriert.
	 *
	 * @param dto die Eingabedaten aus dem Create-Request
	 * @return die neu erstellte Entity ohne ID
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "idSchueler", ignore = true)
	@Mapping(target = "status", source = "idStatus", qualifiedByName = "idToStatus")
	DTOSchulwechselErfolgreicheBewerbung toDomain(SchulwechselErfolgreicheBewerbungCreateRequest dto);

	/**
	 * Wendet die Änderungen eines {@link SchulwechselErfolgreicheBewerbungPatchRequest} auf eine bestehende
	 * {@link DTOSchulwechselErfolgreicheBewerbung}-Entity an. Felder mit {@code undefined}-Wert werden nicht überschrieben.
	 *
	 * @param input   der Patch-Request mit den zu ändernden Feldern
	 * @param toPatch die zu aktualisierende Entity
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "idSchueler", ignore = true)
	@Mapping(target = "status", source = "idStatus", qualifiedByName = "idToStatus")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(SchulwechselErfolgreicheBewerbungPatchRequest input, @MappingTarget DTOSchulwechselErfolgreicheBewerbung toPatch);

	/**
	 * Konvertiert einen {@link StatusSchulwechselErfolgreicheBewerbung} in seine numerische ID.
	 *
	 * @param status der Status
	 * @return die ID des Status
	 */
	@Named("statusToId")
	default int statusToId(final StatusSchulwechselErfolgreicheBewerbung status) {
		return status.getId();
	}

	/**
	 * Konvertiert eine numerische Status-ID in den zugehörigen {@link StatusSchulwechselErfolgreicheBewerbung}.
	 *
	 * @param idStatus die ID des Status
	 * @return der zugehörige Status
	 */
	@Named("idToStatus")
	default StatusSchulwechselErfolgreicheBewerbung idToStatus(final int idStatus) {
		return StatusSchulwechselErfolgreicheBewerbung.getByIdOrNull(idStatus);
	}
}
