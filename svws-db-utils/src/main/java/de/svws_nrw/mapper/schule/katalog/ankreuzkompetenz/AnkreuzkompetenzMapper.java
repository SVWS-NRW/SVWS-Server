package de.svws_nrw.mapper.schule.katalog.ankreuzkompetenz;

import de.svws_nrw.core.data.schule.Ankreuzkompetenz;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzkompetenz;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzCreateRequest;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Mapper zwischen dem DB-DTO {@link DTOAnkreuzkompetenz}, dem Core-DTO {@link Ankreuzkompetenz} und den Request-DTOs.
 * Die Jahrgangszuordnungen und das Flag, ob die Ankreuzkompetenz referenziert wird, setzt der Service.
 */
@Mapper(uses = JsonNullableMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnkreuzkompetenzMapper {

	/** Instanz des Mappers */
	AnkreuzkompetenzMapper INSTANCE = Mappers.getMapper(AnkreuzkompetenzMapper.class);

	/**
	 * Mappt eine {@link DTOAnkreuzkompetenz}-Entity auf das API-Modell {@link Ankreuzkompetenz}.
	 * Bei einer ASV-Ankreuzkompetenz wird kein Fach ausgegeben, fehlende Werte werden durch die Default-Werte ersetzt.
	 *
	 * @param entity die Quell-Entity
	 *
	 * @return das befüllte API-Modell ohne Jahrgangszuordnungen und ohne Referenz-Kennzeichen
	 */
	@Mapping(target = "istASV", source = "istASV", qualifiedByName = "mapIntToBoolean")
	@Mapping(target = "idFach", source = "entity", qualifiedByName = "mapIdFachOhneAsv")
	@Mapping(target = "floskelText", source = "floskelText", defaultValue = "")
	@Mapping(target = "fachSortierung", source = "fachSortierung", defaultValue = "0")
	@Mapping(target = "sortierung", source = "sortierung", defaultValue = "32000")
	@Mapping(target = "referenziertInAnderenTabellen", ignore = true)
	Ankreuzkompetenz toApi(DTOAnkreuzkompetenz entity);

	/**
	 * Mappt einen {@link AnkreuzkompetenzCreateRequest} auf eine neue {@link DTOAnkreuzkompetenz}-Entity.
	 * Nicht gesetzte optionale Felder bleiben {@code null}.
	 *
	 * @param dto der {@link AnkreuzkompetenzCreateRequest}
	 *
	 * @return die befüllte {@link DTOAnkreuzkompetenz}-Entity ohne ID
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "istASV", source = "istASV", qualifiedByName = "mapBooleanToInt")
	DTOAnkreuzkompetenz toDomain(AnkreuzkompetenzCreateRequest dto);

	/**
	 * Wendet die Änderungen aus einem {@link AnkreuzkompetenzPatchRequest} auf eine bestehende Entity an.
	 * Nur Felder, die im Request definiert sind (nicht undefined), werden aktualisiert, ein explizites {@code null} wird übernommen.
	 *
	 * @param dto    der {@link AnkreuzkompetenzPatchRequest} mit den zu ändernden Feldern
	 * @param entity die zu aktualisierende {@link DTOAnkreuzkompetenz}-Entity
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "istASV", source = "istASV", qualifiedByName = "mapIstASVToInt")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(AnkreuzkompetenzPatchRequest dto, @MappingTarget DTOAnkreuzkompetenz entity);

	/**
	 * Wandelt den int-Wert der Datenbank in einen Boolean-Wert um.
	 *
	 * @param wert der int-Wert der Datenbank
	 *
	 * @return {@code true} für den Wert 1, sonst {@code false}
	 */
	@Named("mapIntToBoolean")
	default boolean mapIntToBoolean(final int wert) {
		return wert == 1;
	}

	/**
	 * Bestimmt die ID des Faches für das API-Modell. Bei einer ASV-Ankreuzkompetenz wird kein Fach ausgegeben.
	 *
	 * @param entity die Quell-Entity
	 *
	 * @return die ID des Faches oder {@code null} bei einer ASV-Ankreuzkompetenz oder ohne Fach
	 */
	@Named("mapIdFachOhneAsv")
	default Long mapIdFachOhneAsv(final DTOAnkreuzkompetenz entity) {
		return mapIntToBoolean(entity.istASV) ? null : entity.idFach;
	}

	/**
	 * Wandelt einen Boolean-Wert in den int-Wert der Datenbank um.
	 *
	 * @param wert der Boolean-Wert
	 *
	 * @return 1 für {@code true}, sonst 0
	 */
	@Named("mapBooleanToInt")
	default int mapBooleanToInt(final Boolean wert) {
		return Boolean.TRUE.equals(wert) ? 1 : 0;
	}

	/**
	 * Wandelt den Wert für ASV aus dem Patch in den int-Wert der Datenbank um.
	 *
	 * @param istASV der Wert aus dem Patch
	 *
	 * @return 1 für eine ASV-Ankreuzkompetenz, sonst 0
	 */
	@Named("mapIstASVToInt")
	default int mapIstASVToInt(final JsonNullable<Boolean> istASV) {
		return mapBooleanToInt(istASV.orElse(null));
	}

}
