package de.svws_nrw.mapper.schueler.lernabschnittsdaten;

import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittBemerkungen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittBemerkungenPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * Mapper für das Patchen der Bemerkungen eines Schülerlernabschnitts. Die allgemeine Zeugnisbemerkung wird in
 * der Entity {@link DTOSchuelerLernabschnittsdaten} gespeichert, die weiteren Bemerkungen in der Entity
 * {@link DTOSchuelerPSFachBemerkungen}.
 */
@Mapper(uses = JsonNullableMapper.class)
public interface SchuelerLernabschnittBemerkungenMapper {

	/** schuelerLernabschnittBemerkungenMapper */
	SchuelerLernabschnittBemerkungenMapper INSTANCE = Mappers.getMapper(SchuelerLernabschnittBemerkungenMapper.class);


	/**
	 * Konvertiert eine {@link DTOSchuelerPSFachBemerkungen}-Entity in das Core-DTO {@link SchuelerLernabschnittBemerkungen}.
	 * Die allgemeine Zeugnisbemerkung ist nicht enthalten, da sie aus dem Lernabschnitt selbst stammt.
	 *
	 * @param entity     die Entity mit den Bemerkungen
	 *
	 * @return das befüllte Core-DTO {@link SchuelerLernabschnittBemerkungen}
	 */
	@Mapping(target = "zeugnisAllgemein", ignore = true)
	@Mapping(target = "zeugnisASV", source = "ASV")
	@Mapping(target = "zeugnisLELS", source = "LELS")
	@Mapping(target = "zeugnisAUE", source = "AUE")
	@Mapping(target = "uebergangESF", source = "ESF")
	@Mapping(target = "foerderschwerpunkt", source = "BemerkungFSP")
	@Mapping(target = "versetzungsentscheidung", source = "BemerkungVersetzung")
	SchuelerLernabschnittBemerkungen toApi(DTOSchuelerPSFachBemerkungen entity);

	/**
	 * Übernimmt die allgemeine Zeugnisbemerkung aus dem {@link SchuelerLernabschnittBemerkungenPatchRequest}
	 * in die Entity {@link DTOSchuelerLernabschnittsdaten}, sofern sie im Patch enthalten ist.
	 * Ein enthaltener Wert {@code null} entfernt den Wert in der Entity.
	 *
	 * @param input     der Patch-Request mit den zu ändernden Bemerkungen
	 * @param toPatch   die zu aktualisierende Entity mit den Lernabschnittsdaten
	 */
	@Mapping(target = "ZeugnisBem", source = "zeugnisAllgemein")
	@BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patchBemerkungen(SchuelerLernabschnittBemerkungenPatchRequest input, @MappingTarget DTOSchuelerLernabschnittsdaten toPatch);

	/**
	 * Übernimmt die weiteren Bemerkungen aus dem {@link SchuelerLernabschnittBemerkungenPatchRequest}
	 * in die Entity {@link DTOSchuelerPSFachBemerkungen}, sofern sie im Patch enthalten sind.
	 * Ein enthaltener Wert {@code null} entfernt den Wert in der Entity.
	 *
	 * @param input     der Patch-Request mit den zu ändernden Bemerkungen
	 * @param toPatch   die zu aktualisierende Entity mit den Bemerkungen des Lernabschnitts
	 */
	@Mapping(target = "ASV", source = "zeugnisASV")
	@Mapping(target = "LELS", source = "zeugnisLELS")
	@Mapping(target = "AUE", source = "zeugnisAUE")
	@Mapping(target = "ESF", source = "uebergangESF")
	@Mapping(target = "BemerkungFSP", source = "foerderschwerpunkt")
	@Mapping(target = "BemerkungVersetzung", source = "versetzungsentscheidung")
	@BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patchFachBemerkungen(SchuelerLernabschnittBemerkungenPatchRequest input, @MappingTarget DTOSchuelerPSFachBemerkungen toPatch);

}
