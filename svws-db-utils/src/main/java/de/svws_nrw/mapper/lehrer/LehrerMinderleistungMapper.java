package de.svws_nrw.mapper.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerEntlastungsstunde;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.lehrer.anrechnung.LehrerAnrechnungsstundeCreateRequest;
import de.svws_nrw.service.lehrer.minderleistung.LehrerMinderleistungCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JsonNullableMapper.class)
public interface LehrerMinderleistungMapper {

	/** Instanz des Mappers */
	LehrerMinderleistungMapper INSTANCE = Mappers.getMapper(LehrerMinderleistungMapper.class);

	/**
	 * Map {@link DTOLehrerEntlastungsstunde} auf {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}
	 *
	 * @param entity {@link DTOLehrerEntlastungsstunde}
	 * @param idGrund die aufgelöste Katalog-ID des Mehrleistungsgrundes (Kürzel → ID wird vom Service vorher aufgelöst)
	 *
	 * @return {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}
	 */
	@Mapping(source = "idGrund", target = "idGrund")
	@Mapping(source = "entity.anzahl", target = "anzahl", defaultValue = "0.0")
	LehrerPersonalabschnittsdatenAnrechnungsstunden toApi(DTOLehrerEntlastungsstunde entity, Long idGrund);

	/**
	 * Mappt {@link LehrerAnrechnungsstundeCreateRequest}
	 *
	 * @param request {@link LehrerMinderleistungCreateRequest}
	 * @param id der PK
	 * @param kuerzel das Kuerzel des Entlastungs-Grundes
	 *
	 * @return {@link DTOLehrerEntlastungsstunde}
	 */
	@Mapping(target = "id", source = "id")
	@Mapping(target = "entlastungsgrundKrz", source = "kuerzel")
	DTOLehrerEntlastungsstunde toDomain(LehrerMinderleistungCreateRequest request, Long id, String kuerzel);
}
