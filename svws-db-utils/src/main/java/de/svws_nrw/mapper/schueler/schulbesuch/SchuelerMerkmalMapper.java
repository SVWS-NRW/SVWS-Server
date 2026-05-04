package de.svws_nrw.mapper.schueler.schulbesuch;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchMerkmal;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerMerkmale;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerMerkmalCreateRequest;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerMerkmalPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JsonNullableMapper.class)
public interface SchuelerMerkmalMapper {

	/** Instanz des Mappers */
	SchuelerMerkmalMapper INSTANCE = Mappers.getMapper(SchuelerMerkmalMapper.class);

	/**
	 * Mappt eine {@link DTOSchuelerMerkmale}-Entity auf das API-Modell {@link SchuelerSchulbesuchMerkmal}.
	 *
	 * @param entity         die Quell-Entity
	 * @param idMerkmal      die aufgelöste ID des Merkmals
	 * @return das befüllte API-Modell
	 */
	SchuelerSchulbesuchMerkmal toApi(DTOSchuelerMerkmale entity, Long idMerkmal);

	/**
	 * Erstellt eine neue {@link DTOSchuelerMerkmale}-Entity aus einem {@link SchuelerMerkmalCreateRequest}.
	 * <p>
	 * Altdatenfelder (SIM-Export, Abgangsschulform, Beschreibungen) werden nicht gesetzt.
	 * </p>
	 *
	 * @param dto                    die Eingabedaten aus dem Create-Request
	 * @param kuerzelMerkmal         das aufgelöste Kürzel des Merkmals
	 * @return die neu erstellte Entity ohne ID
	 */
	@Mapping(target = "id", ignore = true)
	DTOSchuelerMerkmale toDomain(SchuelerMerkmalCreateRequest dto, String kuerzelMerkmal);


	/**
	 * Wendet die Änderungen eines {@link SchuelerMerkmalPatchRequest} auf eine bestehende
	 * {@link DTOSchuelerMerkmale}-Entity an. Felder mit {@code null}-Wert werden nicht überschrieben.
	 *
	 * @param input                  der Patch-Request mit den zu ändernden Feldern
	 * @param toPatch                die zu aktualisierende Entity
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "idSchueler", ignore = true)
	@Mapping(target = "kuerzelMerkmal", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(SchuelerMerkmalPatchRequest input, @MappingTarget DTOSchuelerMerkmale toPatch);

}
