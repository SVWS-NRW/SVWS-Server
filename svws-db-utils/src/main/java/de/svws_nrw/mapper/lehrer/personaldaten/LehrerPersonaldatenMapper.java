package de.svws_nrw.mapper.lehrer.personaldaten;

import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.lehrer.personaldaten.LehrerPersonaldatenPatchRequest;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * Mapper für Lehrkräfte-Personaldaten.
 *
 * <p>Die Implementierung dieses Mappers wird zur Laufzeit durch MapStruct
 * erzeugt. Die Abbildung der Personalabschnittsdaten und Lehrämter erfolgt
 * über den {@link LehrerPersonaldatenMappingContext} nach dem eigentlichen
 * MapStruct-Mapping.</p>
 */
@Mapper(uses = JsonNullableMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LehrerPersonaldatenMapper {

	/**
	 * Standardinstanz des Mappers.
	 */
	LehrerPersonaldatenMapper INSTANCE = Mappers.getMapper(LehrerPersonaldatenMapper.class);


	/**
	 * Überträgt die Personaldaten einer Lehrkraft aus dem Datenbankobjekt
	 * in das API-Modell.
	 *
	 * <p>Die Listen mit Personalabschnittsdaten und Lehrämtern werden nach
	 * dem eigentlichen Mapping über den Kontext ergänzt.</p>
	 *
	 * @param entity das Datenbankobjekt der Lehrkraft
	 * @param ctx der Kontext mit den zu ergänzenden
	 *            Personalabschnittsdaten und Lehrämtern
	 * @return das gemappte API-Modell
	 */
	@Mapping(target = "id", source = "entity.ID")
	@Mapping(target = "personalaktennummer", source = "entity.PANr")
	@Mapping(target = "lbvPersonalnummer", source = "entity.personalNrLBV")
	@Mapping(target = "lbvVerguetungsschluessel", source = "entity.verguetungsSchluessel")
	@Mapping(target = "zugangsdatum", source = "entity.DatumZugang")
	@Mapping(target = "zugangsgrund", source = "entity.GrundZugang")
	@Mapping(target = "abgangsdatum", source = "entity.DatumAbgang")
	@Mapping(target = "abgangsgrund", source = "entity.GrundAbgang")
	LehrerPersonaldaten toApi(
			DTOLehrer entity,
			@Context LehrerPersonaldatenMappingContext ctx
	);


	/**
	 * Ergänzt das bereits gemappte API-Modell um die im Kontext enthaltenen
	 * Personalabschnittsdaten und Lehrämter.
	 *
	 * @param ctx der Mapping-Kontext mit den zu ergänzenden Listen
	 * @param target das bereits gemappte Zielobjekt
	 */
	@AfterMapping
	default void mapListen(
			@Context final LehrerPersonaldatenMappingContext ctx,
			@MappingTarget final LehrerPersonaldaten target) {
		target.abschnittsdaten.addAll(ctx.personalabschnittsdaten());
		target.lehraemter.addAll(ctx.lehraemter());
	}

	/**
	 * Überträgt die im Patch-Request enthaltenen Werte in ein bestehendes
	 * Datenbankobjekt.
	 *
	 * <p>Nullwerte werden ignoriert. Dadurch bleiben bestehende Werte des
	 * Datenbankobjekts erhalten, wenn sie im Patch-Request nicht angegeben
	 * wurden.</p>
	 *
	 * <p>Die ID des Datenbankobjekts wird nicht verändert.</p>
	 *
	 * @param dto der Patch-Request mit den zu aktualisierenden Werten
	 * @param entity das zu aktualisierende Datenbankobjekt
	 */
	@Mapping(target = "ID", ignore = true)
	@Mapping(target = "PANr", source = "personalaktennummer")
	@Mapping(target = "personalNrLBV", source = "lbvPersonalnummer")
	@Mapping(target = "verguetungsSchluessel", source = "lbvVerguetungsschluessel")
	@Mapping(target = "DatumZugang", source = "zugangsdatum")
	@Mapping(target = "GrundZugang", source = "zugangsgrund")
	@Mapping(target = "DatumAbgang", source = "abgangsdatum")
	@Mapping(target = "GrundAbgang", source = "abgangsgrund")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(LehrerPersonaldatenPatchRequest dto, @MappingTarget DTOLehrer entity);

}
