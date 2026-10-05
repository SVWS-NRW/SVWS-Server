package de.svws_nrw.mapper.schulwechsel.dokument;

import de.svws_nrw.core.data.schule.SchulwechselDokument;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchulwechselDokument;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schulwechsel.dokument.SchulwechselDokumentCreateRequest;
import de.svws_nrw.service.schulwechsel.dokument.SchulwechselDokumentPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct-Mapper für {@link DTOSchulwechselDokument} ↔ {@link SchulwechselDokument}.
 */
@Mapper(uses = JsonNullableMapper.class)
public interface SchulwechselDokumentMapper {

	/** Instanz des Mappers */
	SchulwechselDokumentMapper INSTANCE = Mappers.getMapper(SchulwechselDokumentMapper.class);

	/**
	 * Mappt eine {@link DTOSchulwechselDokument}-Entity auf das API-Modell {@link SchulwechselDokument}.
	 *
	 * @param entity die Quell-Entity
	 * @return das befüllte API-Modell
	 */
	SchulwechselDokument toApi(DTOSchulwechselDokument entity);

	/**
	 * Erstellt eine neue {@link DTOSchulwechselDokument}-Entity aus einem {@link SchulwechselDokumentCreateRequest}.
	 * Die ID wird vom Repository gesetzt und hier ignoriert.
	 *
	 * @param dto die Eingabedaten aus dem Create-Request
	 * @return die neu erstellte Entity ohne ID
	 */
	@Mapping(target = "id", ignore = true)
	DTOSchulwechselDokument toDomain(SchulwechselDokumentCreateRequest dto);

	/**
	 * Wendet die Änderungen eines {@link SchulwechselDokumentPatchRequest} auf eine bestehende
	 * {@link DTOSchulwechselDokument}-Entity an. Felder mit {@code undefined}-Wert werden nicht überschrieben.
	 *
	 * @param input   der Patch-Request mit den zu ändernden Feldern
	 * @param toPatch die zu aktualisierende Entity
	 */
	@Mapping(target = "id", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(SchulwechselDokumentPatchRequest input, @MappingTarget DTOSchulwechselDokument toPatch);
}
