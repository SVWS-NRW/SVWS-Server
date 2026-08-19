package de.svws_nrw.mapper.lehrer.funktion;

import de.svws_nrw.asd.data.lehrer.LehrerFunktion;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerFunktion;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionCreateRequest;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JsonNullableMapper.class)
public interface LehrerFunktionMapper {

	/** Instanz des Mappers */
	LehrerFunktionMapper INSTANCE = Mappers.getMapper(LehrerFunktionMapper.class);

	/**
	 * Mappt eine {@link DTOLehrerFunktion}-Entity auf das API-Modell {@link LehrerFunktion}.
	 *
	 * @param entity die Quell-Entity
	 * @return das befüllte API-Modell
	 */
	LehrerFunktion toApi(DTOLehrerFunktion entity);

	/**
	 * Erstellt eine neue {@link DTOLehrerFunktion}-Entity aus einem {@link LehrerFunktionCreateRequest}.
	 *
	 * @param dto die Eingabedaten aus dem Create-Request
	 * @return die neu erstellte Entity ohne ID
	 */
	@Mapping(target = "id", ignore = true)
	DTOLehrerFunktion toDomain(LehrerFunktionCreateRequest dto);

	/**
	 * Patcht die Änderungen im {@link LehrerFunktionPatchRequest}-DTO auf eine bestehende {@link DTOLehrerFunktion}-Entity.
	 *
	 * @param dto    der Patch-Request mit den zu ändernden Feldern
	 * @param entity die zu aktualisierende Entity
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "idAbschnittsdaten", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(LehrerFunktionPatchRequest dto, @MappingTarget DTOLehrerFunktion entity);

}
