package de.svws_nrw.mapper.schule.teilstandort;

import de.svws_nrw.core.data.schule.Teilstandort;
import de.svws_nrw.db.dto.current.schild.schule.DTOTeilstandorte;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schule.teilstandort.TeilstandortCreateRequest;
import de.svws_nrw.service.schule.teilstandort.TeilstandortPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;


/**
 * Mapper für die Umwandlung zwischen der Datenbank-Entity {@link DTOTeilstandorte}, der
 * API-Klasse {@link Teilstandort} und den Requests des Teilstandort-Services
 * ({@link TeilstandortCreateRequest}, {@link TeilstandortPatchRequest}).
 * <p>
 * Die Implementierung wird zur Compile-Zeit von MapStruct erzeugt. Für {@code JsonNullable}-Felder
 * wird der {@link JsonNullableMapper} verwendet.
 */
@Mapper(uses = JsonNullableMapper.class)
public interface TeilstandortMapper {

	/** Instanz des Mappers */
	TeilstandortMapper INSTANCE = Mappers.getMapper(TeilstandortMapper.class);

	/**
	 * Mappt eine {@link DTOTeilstandorte}-Entity auf das API-Objekt {@link Teilstandort}.
	 * Das Feld {@code referenziertInAnderenTabellen} wird ignoriert und muss vom Aufrufer gesetzt werden.
	 *
	 * @param entity die {@link DTOTeilstandorte}-Entity
	 *
	 * @return das {@link Teilstandort}-Objekt
	 */
	@Mapping(source = "AdrMerkmal", target = "adrMerkmal")
	@Mapping(source = "PLZ", target = "plz")
	@Mapping(source = "Ort", target = "ort")
	@Mapping(source = "Strassenname", target = "strassenname")
	@Mapping(source = "HausNr", target = "hausNr")
	@Mapping(source = "HausNrZusatz", target = "hausNrZusatz")
	@Mapping(source = "Bemerkung", target = "bemerkung")
	@Mapping(source = "Kuerzel", target = "kuerzel")
	@Mapping(target = "referenziertInAnderenTabellen", ignore = true)
	Teilstandort toApi(DTOTeilstandorte entity);

	/**
	 * Mappt einen {@link TeilstandortCreateRequest} auf eine neue {@link DTOTeilstandorte}-Entity.
	 *
	 * @param dto {@link TeilstandortCreateRequest}
	 *
	 * @return {@link DTOTeilstandorte}
	 */
	@Mapping(source = "adrMerkmal", target = "AdrMerkmal")
	@Mapping(source = "plz", target = "PLZ")
	@Mapping(source = "ort", target = "Ort")
	@Mapping(source = "strassenname", target = "Strassenname")
	@Mapping(source = "hausNr", target = "HausNr")
	@Mapping(source = "hausNrZusatz", target = "HausNrZusatz")
	@Mapping(source = "bemerkung", target = "Bemerkung")
	@Mapping(source = "kuerzel", target = "Kuerzel")
	DTOTeilstandorte toDomain(TeilstandortCreateRequest dto);

	/**
	 * Überträgt die gesetzten Felder eines {@link TeilstandortPatchRequest} auf eine bestehende
	 * {@link DTOTeilstandorte}-Entity. Nicht gesetzte Felder ({@code null}) bleiben unverändert,
	 * das Feld {@code AdrMerkmal} wird nie überschrieben.
	 *
	 * @param dto    der {@link TeilstandortPatchRequest} mit den zu ändernden Werten
	 * @param entity die zu aktualisierende {@link DTOTeilstandorte}-Entity
	 */
	@Mapping(target = "AdrMerkmal", ignore = true)
	@Mapping(source = "plz", target = "PLZ")
	@Mapping(source = "ort", target = "Ort")
	@Mapping(source = "strassenname", target = "Strassenname")
	@Mapping(source = "hausNr", target = "HausNr")
	@Mapping(source = "hausNrZusatz", target = "HausNrZusatz")
	@Mapping(source = "bemerkung", target = "Bemerkung")
	@Mapping(source = "kuerzel", target = "Kuerzel")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(TeilstandortPatchRequest dto, @MappingTarget DTOTeilstandorte entity);
}
