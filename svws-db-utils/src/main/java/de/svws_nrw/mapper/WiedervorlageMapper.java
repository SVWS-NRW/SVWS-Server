package de.svws_nrw.mapper;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import de.svws_nrw.core.data.schule.WiedervorlageEintrag;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.dto.current.schule.DTOWiedervorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.wiedervorlage.WiedervorlageCreateRequest;
import de.svws_nrw.service.wiedervorlage.WiedervorlagePatchRequest;
import jakarta.ws.rs.core.Response;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct-Mapper fuer Wiedervorlage: erstellt Domain-DTOs, mappt DB-DTOs in API-DTOs
 * und wendet Patch-Requests auf bestehende DB-DTOs an.
 */
@Mapper(uses = JsonNullableMapper.class)
public interface WiedervorlageMapper {

	/**
	 * Erstellt ein neues {@link DTOWiedervorlage} aus einem Create-Request.
	 * Setzt Zeitstempel fuer "angelegt" auf die aktuelle Zeit und uebernimmt Benutzer-/Gruppeninfos.
	 *
	 * @param input       Create-Request mit den zu uebernehmenden Feldern
	 * @param idBenutzer  ID des anlegenden Benutzers
	 *
	 * @return neu initialisiertes {@link DTOWiedervorlage} (ohne persistierte ID)
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "idBenutzerErledigt", ignore = true)
	@Mapping(target = "idSchueler", ignore = true)
	@Mapping(target = "idLehrer", ignore = true)
	@Mapping(target = "idErzieher", ignore = true)
	@Mapping(target = "tsErledigt", ignore = true)
	@Mapping(target = "personTyp", ignore = true)
	@Mapping(target = "tsAngelegt", expression = "java(mapCurrentTime())")
	DTOWiedervorlage createDomain(WiedervorlageCreateRequest input, long idBenutzer);

	/**
	 * After-Mapping Hook: uebertraegt Person-Typ und Personen-ID aus dem Create-Request
	 * in die passenden Ziel-Felder (Lehrer/Schueler/Erzieher).
	 *
	 * @param input   Create-Request mit Person-Informationen
	 * @param target  Ziel-DTO, das nachtraeglich ergaenzt wird
	 */
	@AfterMapping
	default void mapPerson(final WiedervorlageCreateRequest input, @MappingTarget final DTOWiedervorlage target) {
		if (input.typPerson == null) {
			return;
		}
		target.personTyp = PersonTyp.getByID(input.typPerson);
		switch (target.personTyp) {
			case LEHRER -> target.idLehrer = input.idPerson;
			case SCHUELER -> target.idSchueler = input.idPerson;
			case ERZIEHER -> target.idErzieher = input.idPerson;
			case null -> throw new ApiOperationException(Response.Status.BAD_REQUEST, "Invalider Personentyp");
		}
	}

	/**
	 * Mappt ein {@link DTOWiedervorlage} in einen API-DTO {@link WiedervorlageEintrag}.
	 *
	 * @param entity DTOWiedervorlage
	 *
	 * @return WiedervorlageEintrag
	 */
	@Mapping(target = "typPerson", source = "personTyp.id")
	@Mapping(target = "idPerson", source = "entity", qualifiedByName = "mapPersonenId")
	WiedervorlageEintrag toApi(DTOWiedervorlage entity);

	/**
	 * Ermittelt die Personen-ID passend zum {@code personTyp} aus dem DB-DTO.
	 *
	 * @param entity DB-DTO mit gesetztem {@code personTyp}
	 *
	 * @return Personen-ID (Lehrer/Schueler/Erzieher) oder {@code null}
	 */
	@Named("mapPersonenId")
	default Long mapPersonenId(final DTOWiedervorlage entity) {
		return switch (entity.personTyp) {
			case null -> null;
			case LEHRER -> entity.idLehrer;
			case SCHUELER -> entity.idSchueler;
			case ERZIEHER -> entity.idErzieher;
		};
	}

	/**
	 * Wendet einen Patch-Request auf ein bestehendes {@link DTOWiedervorlage} an.
	 * {@code null}-Werte im Input werden ignoriert (kein Ueberschreiben).
	 *
	 * @param input    Patch-Request mit zu aendernden Feldern
	 * @param toPatch  Ziel-DTO, das aktualisiert wird
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "idSchueler", ignore = true)
	@Mapping(target = "idLehrer", ignore = true)
	@Mapping(target = "idErzieher", ignore = true)
	@Mapping(target = "idBenutzer", ignore = true)
	@Mapping(target = "idBenutzerErledigt", ignore = true)
	@Mapping(target = "tsErledigt", ignore = true)
	@Mapping(target = "tsAngelegt", ignore = true)
	@Mapping(target = "personTyp", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(WiedervorlagePatchRequest input, @MappingTarget DTOWiedervorlage toPatch);

	/**
	 * Liefert den aktuellen Zeitstempel im SVWS-Format (Europe/Berlin).
	 *
	 * @return formatierter Zeitstempel als String
	 */
	default String mapCurrentTime() {
		return JSONMapper.tsFormatter.format(ZonedDateTime.now(ZoneId.of("Europe/Berlin")));
	}
}
