package de.svws_nrw.mapper.schule.logoverwaltung;


import de.svws_nrw.core.data.schule.Logo;
import de.svws_nrw.core.types.reporting.ReportingBildDefinition;
import de.svws_nrw.db.dto.current.schild.schule.DTOLogo;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schule.logoverwaltung.LogoCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JsonNullableMapper.class)
public interface LogoverwaltungMapper {

	/** Instanz des Mappers */
	LogoverwaltungMapper INSTANCE = Mappers.getMapper(LogoverwaltungMapper.class);

	/**
	 * Mappt eine {@link DTOLogo}-Entity auf das API-DTO {@link Logo}.
	 *
	 * @param entity die Quell-Entity
	 * @return das befüllte API-DTO
	 */
	@Mapping(source = "id", target = "id")
	@Mapping(source = "kennung", target = "kennung", qualifiedByName = "kennung")
	@Mapping(source = "logoBase64", target = "logoBase64")
	@Mapping(source = "hinzugefuegtAm", target = "hinzugefuegtAm")
	@Mapping(target = "bezeichnung", ignore = true)
	@Mapping(target = "beschreibung", ignore = true)
	Logo toApi(DTOLogo entity);

	/**
	 * Mappt einen {@link LogoCreateRequest} auf eine neue {@link DTOLogo}-Entity.
	 *
	 * @param create der Create-Request mit den zu übernehmenden Feldern
	 *
	 * @return die befüllte {@link DTOLogo}-Entity
	 */
	@Mapping(source = "kennung", target = "kennung", qualifiedByName = "kennung")
	@Mapping(source = "logoBase64", target = "logoBase64")
	@Mapping(source = "hinzugefuegtAm", target = "hinzugefuegtAm")
	@Mapping(target = "id", ignore = true)
	DTOLogo toDomain(LogoCreateRequest create);

	/**
	 * Mappt eine {@link ReportingBildDefinition} auf die Kennung des Logos.
	 * @param kennung die Kennung des Logos
	 * @return die ReportingBildDefinition
	 */
	@Named("kennung")
	default ReportingBildDefinition mapKennung(final String kennung) {
		return ReportingBildDefinition.getByKennung(kennung);
	}

	/**
	 * Mappt eine Kennung des Logos auf eine {@link ReportingBildDefinition}.
	 * @param reportingBildDefinition die ReportingBildDefinition
	 * @return die Kennung des Logos
	 */
	@Named("kennung")
	default String mapReportingBildDefinition(final ReportingBildDefinition reportingBildDefinition) {
		return reportingBildDefinition.getKennung();
	}

}
