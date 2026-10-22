package de.svws_nrw.mapper.lehrer.lernplattform;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import de.svws_nrw.core.data.lehrer.LehrerLernplattform;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLernplattform;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentialsLernplattformen;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.lehrer.lernplattform.LehrerLernplattformPatchRequest;

/**
 * MapStruct-Mapper für die Zuordnung eines Lehrers zu einer Lernplattform: mappt DB-DTOs in das Core-DTO,
 * erstellt neue DB-DTOs und wendet Patch-Requests auf bestehende DB-DTOs an.
 */
@Mapper(uses = JsonNullableMapper.class)
public interface LehrerLernplattformMapper {

	/** Instanz des Mappers */
	LehrerLernplattformMapper INSTANCE = Mappers.getMapper(LehrerLernplattformMapper.class);

	/**
	 * Mappt ein {@link DTOLehrerLernplattform} in das Core-DTO {@link LehrerLernplattform}. Fehlende Einwilligungen
	 * werden als {@code false} und eine fehlende Credential-ID als 0 abgebildet. Benutzername und Initialkennwort werden nur übernommen, wenn Zugangsdaten
	 * übergeben werden.
	 *
	 * @param entity        die Zuordnung des Lehrers zur Lernplattform
	 * @param credentials   die Zugangsdaten zur Zuordnung oder {@code null}, wenn keine existieren
	 *
	 * @return das Core-DTO
	 */
	@Mapping(target = "idLehrer", source = "entity.LehrerID")
	@Mapping(target = "idLernplattform", source = "entity.LernplattformID")
	@Mapping(target = "idCredential", source = "entity.CredentialID")
	@Mapping(target = "einwilligungAbgefragt", source = "entity.EinwilligungAbgefragt")
	@Mapping(target = "einwilligungNutzung", source = "entity.EinwilligungNutzung")
	@Mapping(target = "einwilligungAudiokonferenz", source = "entity.EinwilligungAudiokonferenz")
	@Mapping(target = "einwilligungVideokonferenz", source = "entity.EinwilligungVideokonferenz")
	@Mapping(target = "benutzername", source = "credentials.Benutzername")
	@Mapping(target = "initialKennwort", source = "credentials.Initialkennwort")
	LehrerLernplattform toApi(DTOLehrerLernplattform entity, DTOCredentialsLernplattformen credentials);

	/**
	 * Erstellt ein neues {@link DTOLehrerLernplattform} ohne Einwilligungen und ohne Zugangsdaten.
	 *
	 * @param idLehrer        die ID des Lehrers
	 * @param idLernplattform   die ID der Lernplattform
	 *
	 * @return die neue Zuordnung
	 */
	default DTOLehrerLernplattform toDomain(final long idLehrer, final long idLernplattform) {
		return new DTOLehrerLernplattform(idLehrer, idLernplattform, false, false, false, false);
	}

	/**
	 * Wendet einen Patch-Request auf ein bestehendes {@link DTOLehrerLernplattform} an. Nicht gesetzte Felder (undefined)
	 * werden nicht überschrieben. Die ID der Zuordnung und die Zugangsdaten werden nicht verändert.
	 *
	 * @param input     der Patch-Request mit den zu ändernden Feldern
	 * @param toPatch   die zu aktualisierende Zuordnung
	 */
	@Mapping(target = "LehrerID", ignore = true)
	@Mapping(target = "LernplattformID", ignore = true)
	@Mapping(target = "CredentialID", ignore = true)
	@Mapping(target = "EinwilligungAbgefragt", source = "einwilligungAbgefragt")
	@Mapping(target = "EinwilligungNutzung", source = "einwilligungNutzung")
	@Mapping(target = "EinwilligungAudiokonferenz", source = "einwilligungAudiokonferenz")
	@Mapping(target = "EinwilligungVideokonferenz", source = "einwilligungVideokonferenz")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(LehrerLernplattformPatchRequest input, @MappingTarget DTOLehrerLernplattform toPatch);

}
