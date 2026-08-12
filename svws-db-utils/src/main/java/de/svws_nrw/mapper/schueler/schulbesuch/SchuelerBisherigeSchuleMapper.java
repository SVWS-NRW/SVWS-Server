package de.svws_nrw.mapper.schueler.schulbesuch;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerBisherigeSchuleCreateRequest;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerBisherigeSchulePatchRequest;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerAbgaenge;
import de.svws_nrw.mapper.JsonNullableMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JsonNullableMapper.class)
public interface SchuelerBisherigeSchuleMapper {

	/** Instanz des Mappers */
	SchuelerBisherigeSchuleMapper INSTANCE = Mappers.getMapper(SchuelerBisherigeSchuleMapper.class);

	/**
	 * Mappt eine {@link DTOSchuelerAbgaenge}-Entity auf das API-Modell {@link SchuelerSchulbesuchSchule}.
	 *
	 * @param entity         die Quell-Entity
	 * @param idEntlassgrund die aufgelöste ID des Entlassgrunds
	 * @param idSchule       die aufgelöste ID der Schule
	 * @return das befüllte API-Modell
	 */
	SchuelerSchulbesuchSchule toApi(DTOSchuelerAbgaenge entity, Long idEntlassgrund, Long idSchule);


	/**
	 * Erstellt eine neue {@link DTOSchuelerAbgaenge}-Entity aus einem {@link SchuelerBisherigeSchuleCreateRequest}.
	 * <p>
	 * Altdatenfelder (SIM-Export, Abgangsschulform, Beschreibungen) werden nicht gesetzt.
	 * </p>
	 *
	 * @param dto                    die Eingabedaten aus dem Create-Request
	 * @param bezeichnungEntlassgrund die aufgelöste Bezeichnung des Entlassgrunds
	 * @param schulnummer            die aufgelöste Schulnummer der bisherigen Schule
	 * @return die neu erstellte Entity ohne ID
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "AbgangsSchulform", ignore = true)
	@Mapping(target = "AbgangsBeschreibung", ignore = true)
	@Mapping(target = "AbgangsSchule", ignore = true)
	@Mapping(target = "AbgangsSchuleAnschr", ignore = true)
	@Mapping(target = "LSSchulformSIM", ignore = true)
	@Mapping(target = "LSVersetzung", ignore = true)
	@Mapping(target = "LSFachklKennung", ignore = true)
	@Mapping(target = "LSFachklSIM", ignore = true)
	@Mapping(target = "FuerSIMExport", ignore = true)
	DTOSchuelerAbgaenge toDomain(SchuelerBisherigeSchuleCreateRequest dto, String bezeichnungEntlassgrund, String schulnummer);

	/**
	 * Wendet die Änderungen eines {@link SchuelerBisherigeSchulePatchRequest} auf eine bestehende
	 * {@link DTOSchuelerAbgaenge}-Entity an. Felder mit {@code null}-Wert werden nicht überschrieben.
	 *
	 * @param input                  der Patch-Request mit den zu ändernden Feldern
	 * @param toPatch                die zu aktualisierende Entity
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "idSchueler", ignore = true)
	@Mapping(target = "schulnummer", ignore = true)
	@Mapping(target = "bezeichnungEntlassgrund", ignore = true)
	@Mapping(target = "AbgangsSchulform", ignore = true)
	@Mapping(target = "AbgangsBeschreibung", ignore = true)
	@Mapping(target = "AbgangsSchule", ignore = true)
	@Mapping(target = "AbgangsSchuleAnschr", ignore = true)
	@Mapping(target = "LSSchulformSIM", ignore = true)
	@Mapping(target = "LSVersetzung", ignore = true)
	@Mapping(target = "LSFachklKennung", ignore = true)
	@Mapping(target = "LSFachklSIM", ignore = true)
	@Mapping(target = "FuerSIMExport", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(SchuelerBisherigeSchulePatchRequest input, @MappingTarget DTOSchuelerAbgaenge toPatch);

}
