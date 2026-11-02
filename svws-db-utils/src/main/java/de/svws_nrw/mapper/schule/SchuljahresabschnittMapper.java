package de.svws_nrw.mapper.schule;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct-Mapper für Schuljahresabschnitte: mappt DB-DTOs in API-DTOs.
 */
@Mapper
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

}
