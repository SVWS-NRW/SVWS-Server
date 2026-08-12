package de.svws_nrw.mapper.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerMehrleistung;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.lehrer.mehrleistung.LehrerMehrleistungPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JsonNullableMapper.class)
public interface LehrerMehrleistungMapper {

	/** Instanz des Mappers */
	LehrerMehrleistungMapper INSTANCE = Mappers.getMapper(LehrerMehrleistungMapper.class);

	/**
	 * Map {@link DTOLehrerMehrleistung} to {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}
	 *
	 * @param entity {@link DTOLehrerMehrleistung}
	 * @param idGrund die aufgelöste Katalog-ID des Mehrleistungsgrundes (Kürzel → ID wird vom Service vorher aufgelöst)
	 *
	 * @return {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}
	 */
	@Mapping(source = "idGrund", target = "idGrund")
	@Mapping(source = "entity.anzahl", target = "anzahl", defaultValue = "0.0")
	LehrerPersonalabschnittsdatenAnrechnungsstunden toApi(DTOLehrerMehrleistung entity, Long idGrund);

	/**
	 * Wendet die Änderungen aus einem {@link LehrerMehrleistungPatchRequest} auf ein bestehendes Entity an.
	 * Nur Felder, die im Request definiert sind (nicht undefined), werden aktualisiert.
	 *
	 * @param dto das {@link LehrerMehrleistungPatchRequest} mit den zu ändernden Feldern
	 * @param entity das zu aktualisierende {@link DTOLehrerMehrleistung} Entity
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "idAbschnittsdaten", ignore = true)
	@Mapping(target = "idGrund", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(LehrerMehrleistungPatchRequest dto, @MappingTarget DTOLehrerMehrleistung entity);

}
