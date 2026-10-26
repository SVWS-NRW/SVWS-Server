package de.svws_nrw.mapper.schule;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schule.SchuljahresabschnittCreateRequest;
import de.svws_nrw.service.schule.SchuljahresabschnittPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct-Mapper für Schuljahresabschnitte: mappt DB-DTOs in API-DTOs, erstellt DB-DTOs aus Create-Requests
 * und wendet Patch-Requests auf bestehende DB-DTOs an.
 */
@Mapper(uses = JsonNullableMapper.class)
public interface SchuljahresabschnittMapper {

	/** Instanz des Mappers */
	SchuljahresabschnittMapper INSTANCE = Mappers.getMapper(SchuljahresabschnittMapper.class);

	/**
	 * Mappt ein {@link DTOSchuljahresabschnitte} in ein {@link Schuljahresabschnitt}.
	 *
	 * @param entity   das {@link DTOSchuljahresabschnitte}
	 *
	 * @return der {@link Schuljahresabschnitt}
	 */
	@Mapping(source = "ID",                  target = "id")
	@Mapping(source = "Jahr",                target = "schuljahr")
	@Mapping(source = "Abschnitt",           target = "abschnitt")
	@Mapping(source = "VorigerAbschnitt_ID", target = "idVorigerAbschnitt")
	@Mapping(source = "FolgeAbschnitt_ID",   target = "idFolgeAbschnitt")
	Schuljahresabschnitt toApi(DTOSchuljahresabschnitte entity);

	/**
	 * Erstellt ein neues {@link DTOSchuljahresabschnitte} aus einem {@link SchuljahresabschnittCreateRequest}.
	 * Die ID wird nicht übernommen, sie wird beim Persistieren vom Repository vergeben.
	 *
	 * @param dto   das {@link SchuljahresabschnittCreateRequest}
	 *
	 * @return das neue {@link DTOSchuljahresabschnitte} (ohne persistierte ID)
	 */
	@Mapping(target = "ID",                  ignore = true)
	@Mapping(source = "schuljahr",           target = "Jahr")
	@Mapping(source = "abschnitt",           target = "Abschnitt")
	@Mapping(source = "idVorigerAbschnitt",  target = "VorigerAbschnitt_ID")
	@Mapping(source = "idFolgeAbschnitt",    target = "FolgeAbschnitt_ID")
	DTOSchuljahresabschnitte toDomain(SchuljahresabschnittCreateRequest dto);

	/**
	 * Wendet die Änderungen aus einem {@link SchuljahresabschnittPatchRequest} auf ein bestehendes Entity an.
	 * Nur Felder, die im Request definiert sind (nicht undefined), werden aktualisiert. Ein explizit gesetztes
	 * null löst die Verknüpfung. Schuljahr und Abschnitt werden nie verändert.
	 *
	 * @param dto      das {@link SchuljahresabschnittPatchRequest} mit den zu ändernden Feldern
	 * @param entity   das zu aktualisierende {@link DTOSchuljahresabschnitte}
	 */
	@Mapping(target = "ID",                  ignore = true)
	@Mapping(target = "Jahr",                ignore = true)
	@Mapping(target = "Abschnitt",           ignore = true)
	@Mapping(source = "idVorigerAbschnitt",  target = "VorigerAbschnitt_ID")
	@Mapping(source = "idFolgeAbschnitt",    target = "FolgeAbschnitt_ID")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(SchuljahresabschnittPatchRequest dto, @MappingTarget DTOSchuljahresabschnitte entity);

}
