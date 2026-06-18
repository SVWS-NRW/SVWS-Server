package de.svws_nrw.mapper;

import de.svws_nrw.asd.data.schule.FachklasseKatalogEintrag;
import de.svws_nrw.core.data.schild3.Schild3FachklasseDQRNiveauZuordnung;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface Schild3FachklasseDQRNiveauZuordnungMapper {

	/** Instanz des Mappers */
	Schild3FachklasseDQRNiveauZuordnungMapper INSTANCE = Mappers.getMapper(Schild3FachklasseDQRNiveauZuordnungMapper.class);

	/**
	 * Mappt von {@link FachklasseKatalogEintrag} zu {@link Schild3FachklasseDQRNiveauZuordnung}.
	 *
	 * @param fachklasse {@link FachklasseKatalogEintrag}
	 * @param schulgliederung Kürzel der Schulgliederung
	 *
	 * @return {@link Schild3FachklasseDQRNiveauZuordnung}
	 */
	@Mapping(source = "fachklasse.dqrNiveau", target = "DQR_Niveau", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
	@Mapping(target = "FKS", expression = "java(String.format(\"%s%s\", fachklasse.fkSchluessel, fachklasse.fkSchluessel2))")
	@Mapping(target = "Gliederung", source = "schulgliederung")
	@Mapping(target = "gueltigVon", source = "fachklasse.gueltigVon")
	@Mapping(target = "gueltigBis", source = "fachklasse.gueltigBis")
	Schild3FachklasseDQRNiveauZuordnung toApi(FachklasseKatalogEintrag fachklasse, String schulgliederung);
}
