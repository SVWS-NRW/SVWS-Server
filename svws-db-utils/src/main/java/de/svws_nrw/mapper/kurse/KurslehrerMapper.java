package de.svws_nrw.mapper.kurse;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import de.svws_nrw.asd.data.kurse.KursLehrer;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.kurse.KurslehrerCreateRequest;
import de.svws_nrw.service.kurse.KurslehrerPatchRequest;

/**
 * MapStruct-Mapper für die Zusatzkräfte eines Kurses: mappt DB-DTOs in das Core-DTO, erstellt neue DB-DTOs
 * und wendet Patch-Requests auf bestehende DB-DTOs an.
 */
@Mapper(uses = JsonNullableMapper.class)
public interface KurslehrerMapper {

	/** Die Wochenstunden einer Zusatzkraft, wenn kein Anteil gesetzt ist. */
	double ANTEIL_DEFAULT = 0.0;

	/** Instanz des Mappers */
	KurslehrerMapper INSTANCE = Mappers.getMapper(KurslehrerMapper.class);

	/**
	 * Mappt ein {@link DTOKursLehrer} in das Core-DTO {@link KursLehrer}. Ist kein Anteil gesetzt, so werden
	 * 0 Wochenstunden verwendet.
	 *
	 * @param dto   die Zuordnung der Zusatzkraft zum Kurs
	 *
	 * @return das Core-DTO
	 */
	@Mapping(target = "idKurs", source = "Kurs_ID")
	@Mapping(target = "idLehrer", source = "Lehrer_ID")
	@Mapping(target = "wochenstundenLehrer", source = "Anteil", qualifiedByName = "mapAnteil")
	KursLehrer toApi(DTOKursLehrer dto);

	/**
	 * Erstellt ein neues {@link DTOKursLehrer} für die Zusatzkraft aus dem Create-Request.
	 *
	 * @param idKurs    die ID des Kurses
	 * @param request   die Daten der Zusatzkraft
	 *
	 * @return die neue Zuordnung der Zusatzkraft zum Kurs
	 */
	default DTOKursLehrer toDomain(final long idKurs, final KurslehrerCreateRequest request) {
		final var dto = new DTOKursLehrer(idKurs, request.idLehrer);
		dto.Anteil = request.wochenstundenLehrer;
		return dto;
	}

	/**
	 * Wendet einen Patch-Request auf ein bestehendes {@link DTOKursLehrer} an. Nicht gesetzte Felder (undefined)
	 * werden nicht überschrieben. Die ID der Zuordnung wird nicht verändert.
	 *
	 * @param input     der Patch-Request mit den zu ändernden Feldern
	 * @param toPatch   die zu aktualisierende Zuordnung
	 */
	@Mapping(target = "Kurs_ID", ignore = true)
	@Mapping(target = "Lehrer_ID", ignore = true)
	@Mapping(target = "Anteil", source = "wochenstundenLehrer")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(KurslehrerPatchRequest input, @MappingTarget DTOKursLehrer toPatch);

	/**
	 * Bildet einen nicht gesetzten Anteil einer Zusatzkraft auf 0 Wochenstunden ab.
	 *
	 * @param anteil   der Anteil, kann null sein
	 *
	 * @return die Wochenstunden der Zusatzkraft
	 */
	@Named("mapAnteil")
	default double mapAnteil(final Double anteil) {
		return (anteil == null) ? ANTEIL_DEFAULT : anteil;
	}

}
