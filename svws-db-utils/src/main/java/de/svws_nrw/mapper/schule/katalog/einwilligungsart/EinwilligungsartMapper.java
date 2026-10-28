package de.svws_nrw.mapper.schule.katalog.einwilligungsart;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.core.data.schule.Einwilligungsart;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.db.dto.current.schild.katalog.DTOEinwilligungsart;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schule.katalog.einwilligungsart.EinwilligungsartCreateRequest;
import de.svws_nrw.service.schule.katalog.einwilligungsart.EinwilligungsartPatchRequest;

/**
 * Mapper zwischen dem Datenbank-DTO {@link DTOEinwilligungsart}, dem Core-DTO {@link Einwilligungsart}
 * und den Requests zum Erstellen und Ändern von Einwilligungsarten.
 */
@Mapper(uses = JsonNullableMapper.class)
public interface EinwilligungsartMapper {

	/** Instanz des Mappers */
	EinwilligungsartMapper INSTANCE = Mappers.getMapper(EinwilligungsartMapper.class);

	/**
	 * Mappt ein {@link DTOEinwilligungsart} auf das Core-DTO {@link Einwilligungsart}. Ein fehlender Personentyp
	 * wird als {@code -1} abgebildet. Das Flag {@code referenziertInAnderenTabellen} wird nicht gesetzt,
	 * sondern vom Service bestimmt.
	 *
	 * @param entity   das Datenbank-DTO
	 *
	 * @return das Core-DTO
	 */
	@Mapping(target = "idPersonTyp", source = "personTyp", qualifiedByName = "personTypToId")
	@Mapping(target = "referenziertInAnderenTabellen", ignore = true)
	Einwilligungsart toApi(DTOEinwilligungsart entity);

	/**
	 * Mappt einen {@link EinwilligungsartCreateRequest} auf ein neues {@link DTOEinwilligungsart}. Die ID wird
	 * erst beim Persistieren vergeben, der Personentyp wird vom Service gesetzt. Ein leerer Schlüssel wird als
	 * {@code null} abgebildet.
	 *
	 * @param dto   der Request
	 *
	 * @return das neue Datenbank-DTO
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "personTyp", ignore = true)
	@Mapping(target = "schluessel", qualifiedByName = "blankToNull")
	DTOEinwilligungsart toDomain(EinwilligungsartCreateRequest dto);

	/**
	 * Wendet die Änderungen aus einem {@link EinwilligungsartPatchRequest} auf ein bestehendes {@link DTOEinwilligungsart} an.
	 * Nur Felder, die im Request definiert sind, werden aktualisiert. Ein leerer Schlüssel entfernt den Schlüssel.
	 *
	 * @param dto      der Request mit den zu ändernden Feldern
	 * @param entity   das zu aktualisierende Datenbank-DTO
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "personTyp", ignore = true)
	@Mapping(target = "schluessel", qualifiedByName = "blankToNull")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(EinwilligungsartPatchRequest dto, @MappingTarget DTOEinwilligungsart entity);

	/**
	 * Bildet einen leeren Text auf {@code null} ab.
	 *
	 * @param value   der Text
	 *
	 * @return der Text oder {@code null}, wenn er {@code null} oder leer ist
	 */
	@Named("blankToNull")
	default String blankToNull(final String value) {
		return ((value == null) || value.isBlank()) ? null : value;
	}

	/**
	 * Entpackt einen Text aus einem {@link JsonNullable} und bildet einen leeren Text auf {@code null} ab.
	 *
	 * @param value   der Text im {@link JsonNullable}
	 *
	 * @return der Text oder {@code null}, wenn er nicht definiert, {@code null} oder leer ist
	 */
	@Named("blankToNull")
	default String blankToNull(final JsonNullable<String> value) {
		return ((value == null) || !value.isPresent()) ? null : blankToNull(value.get());
	}

	/**
	 * Bestimmt die ID eines Personentyps.
	 *
	 * @param personTyp   der Personentyp
	 *
	 * @return die ID des Personentyps oder {@code -1}, wenn kein Personentyp vorhanden ist
	 */
	@Named("personTypToId")
	default int personTypToId(final PersonTyp personTyp) {
		return (personTyp == null) ? -1 : personTyp.id;
	}

}
