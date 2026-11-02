package de.svws_nrw.mapper.gost;

import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.service.gost.SchulJahrgang;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct-Mapper für die Bestimmung der Liste der Abiturjahrgänge: mappt DB-DTOs auf die Daten, welche der
 * {@link de.svws_nrw.service.gost.GostJahrgangslisteService} verwendet.
 */
@Mapper
public interface GostJahrgangMapper {

	/** Instanz des Mappers */
	GostJahrgangMapper INSTANCE = Mappers.getMapper(GostJahrgangMapper.class);

	/**
	 * Mappt ein {@link DTOJahrgang} in einen {@link SchulJahrgang}.
	 *
	 * @param entity   das {@link DTOJahrgang}
	 *
	 * @return der {@link SchulJahrgang}
	 */
	@Mapping(source = "ASDJahrgang",       target = "kuerzelASDJahrgang")
	@Mapping(source = "GliederungKuerzel", target = "gliederung", qualifiedByName = "mapGliederung")
	SchulJahrgang toSchulJahrgang(DTOJahrgang entity);

	/**
	 * Löst das Kürzel der Schulgliederung auf den CoreType-Eintrag auf.
	 *
	 * @param kuerzel   das Kürzel der Schulgliederung
	 *
	 * @return die Schulgliederung oder {@code null}, falls das Kürzel keiner Schulgliederung entspricht
	 */
	@Named("mapGliederung")
	default Schulgliederung mapGliederung(final String kuerzel) {
		return Schulgliederung.data().getWertByKuerzel(kuerzel);
	}

}
