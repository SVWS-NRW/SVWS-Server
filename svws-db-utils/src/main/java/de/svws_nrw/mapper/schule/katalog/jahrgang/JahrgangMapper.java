package de.svws_nrw.mapper.schule.katalog.jahrgang;

import java.util.Optional;

import de.svws_nrw.asd.data.schule.BildungsstufeKatalogEintrag;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.schule.Bildungsstufe;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.service.schule.katalog.jahrgang.JahrgangCreateRequest;
import de.svws_nrw.service.schule.katalog.jahrgang.JahrgangPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import static java.util.Optional.ofNullable;

@Mapper(uses = JsonNullableMapper.class)
public interface JahrgangMapper {

	/** jahrgangMapper */
	JahrgangMapper INSTANCE = Mappers.getMapper(JahrgangMapper.class);

	/**
	 * Mappt eine {@link DTOJahrgang}-Entity auf das API-Modell {@link JahrgangsDaten}.
	 *
	 * @param entity die Quell-Entity
	 * @param schuljahr das aktuelle Schuljahr für CoreType-Lookups
	 * @param schulform die Schulform der Schule für die Default-Schulgliederung
	 * @return das befüllte DTO
	 */
	@Mapping(target = "id", source = "ID")
	@Mapping(target = "kuerzel", source = "InternKrz")
	@Mapping(target = "kurzbezeichnung", source = "Kurzbezeichnung")
	@Mapping(target = "idJahrgang", source = "ASDJahrgang", qualifiedByName = "mapJahrgang")
	@Mapping(target = "bezeichnung", source = "ASDBezeichnung", defaultValue = "")
	@Mapping(target = "sortierung", source = "Sortierung")
	@Mapping(target = "idSchulgliederung", source = "GliederungKuerzel", qualifiedByName = "mapGliederung")
	@Mapping(target = "idFolgejahrgang", source = "Folgejahrgang_ID")
	@Mapping(target = "idBildungsstufe", source = "Sekundarstufe", qualifiedByName = "mapSekundarstufe")
	@Mapping(target = "anzahlRestabschnitte", source = "AnzahlRestabschnitte")
	@Mapping(target = "istSichtbar", source = "Sichtbar")
	@Mapping(target = "gueltigVon", source = "GueltigVon")
	@Mapping(target = "gueltigBis", source = "GueltigBis")
	@Mapping(target = "referenziertInAnderenTabellen", ignore = true)
	JahrgangsDaten toApi(DTOJahrgang entity, @Context int schuljahr, @Context Schulform schulform);

	/**
	 * Löst das ASD-Kürzel des Jahrgangs auf die ID des CoreType-Eintrags auf.
	 *
	 * @param kuerzel das ASD-Kürzel des Jahrgangs
	 * @param schuljahr das aktuelle Schuljahr für den {@link Jahrgaenge}-Lookup
	 * @return die ID des Jahrgangs-Eintrags oder {@code null}
	 */
	@Named("mapJahrgang")
	default Long mapJahrgang(final String kuerzel, @Context final int schuljahr) {
		return ofNullable(Jahrgaenge.data().getWertByKuerzel(kuerzel))
				.map(j -> j.daten(schuljahr))
				.map(d -> d.id)
				.orElse(null);
	}

	/**
	 * Löst das Kürzel der Schulgliederung auf die ID des CoreType-Eintrags auf.
	 * Ist kein Kürzel gesetzt, wird die Default-Schulgliederung der Schulform verwendet.
	 *
	 * @param kuerzel das Kürzel der Schulgliederung
	 * @param schuljahr das aktuelle Schuljahr für den {@link Schulgliederung}-Lookup
	 * @param schulform die Schulform der Schule
	 * @return die ID des Schulgliederungs-Eintrags oder {@code null}
	 */
	@Named("mapGliederung")
	default Long mapGliederung(final String kuerzel, @Context final int schuljahr, @Context final Schulform schulform) {
		return ofNullable((kuerzel == null)
						? Schulgliederung.getDefault(schulform)
						: Schulgliederung.data().getWertByKuerzel(kuerzel))
				.map(g -> g.daten(schuljahr))
				.map(d -> d.id)
				.orElse(null);
	}

	/**
	 * Löst das Kürzel der Sekundarstufe auf die ID des Bildungsstufen-Eintrags auf.
	 *
	 * @param sekundarstufe das Kürzel der Sekundarstufe
	 * @param schuljahr das aktuelle Schuljahr für den {@link Bildungsstufe}-Lookup
	 * @return die ID des Bildungsstufen-Eintrags oder {@code null}
	 */
	@Named("mapSekundarstufe")
	default Long mapSekundarstufe(final String sekundarstufe, @Context final int schuljahr) {
		final BildungsstufeKatalogEintrag daten =
				Bildungsstufe.data().getEintragBySchuljahrUndSchluessel(schuljahr, sekundarstufe);
		if (daten == null) {
			return null;
		}
		return daten.id;
	}

	/**
	 * Mappt einen {@link JahrgangCreateRequest} auf eine neue {@link DTOJahrgang}-Entity.
	 *
	 * @param dto
	 * @return die befüllte {@link DTOJahrgang}-Entity
	 */
	@Mapping(target = "InternKrz", source = "kuerzel")
	@Mapping(target = "ASDBezeichnung", source = "bezeichnung")
	@Mapping(target = "Kurzbezeichnung", source = "kurzbezeichnung")
	@Mapping(target = "Sortierung", source = "sortierung")
	@Mapping(target = "Folgejahrgang_ID", source = "idFolgejahrgang")
	@Mapping(target = "AnzahlRestabschnitte", source = "anzahlRestabschnitte")
	@Mapping(target = "Sichtbar", source = "istSichtbar")
	@Mapping(target = "GueltigVon", source = "gueltigVon")
	@Mapping(target = "GueltigBis", source = "gueltigBis")
	@Mapping(target = "ASDJahrgang", source = "idJahrgang", qualifiedByName = "updateIdJahrgang")
	@Mapping(target = "GliederungKuerzel", source = "idSchulgliederung", qualifiedByName = "updateIdSchulgliederung")
	@Mapping(target = "Sekundarstufe", source = "idBildungsstufe", qualifiedByName = "updateIdBildungsstufe")
	@Mapping(target = "ID", ignore = true)
	@Mapping(target = "IstChronologisch", ignore = true)
	DTOJahrgang toDomain(JahrgangCreateRequest dto);

	/**
	 * Wendet die Änderungen aus einem {@link JahrgangPatchRequest} auf eine bestehende Entity an.
	 * Nur Felder, die im Request definiert sind (nicht undefined), werden aktualisiert.
	 *
	 * @param dto    der {@link JahrgangPatchRequest} mit den zu ändernden Feldern
	 * @param entity die zu aktualisierende {@link DTOJahrgang}-Entity
	 */
	@Mapping(target = "InternKrz", source = "kuerzel")
	@Mapping(target = "ASDBezeichnung", source = "bezeichnung")
	@Mapping(target = "Kurzbezeichnung", source = "kurzbezeichnung")
	@Mapping(target = "Sortierung", source = "sortierung")
	@Mapping(target = "Folgejahrgang_ID", source = "idFolgejahrgang")
	@Mapping(target = "AnzahlRestabschnitte", source = "anzahlRestabschnitte")
	@Mapping(target = "Sichtbar", source = "istSichtbar")
	@Mapping(target = "GueltigVon", source = "gueltigVon")
	@Mapping(target = "GueltigBis", source = "gueltigBis")
	@Mapping(target = "ASDJahrgang", source = "idJahrgang", qualifiedByName = "updateIdJahrgang")
	@Mapping(target = "GliederungKuerzel", source = "idSchulgliederung", qualifiedByName = "updateIdSchulgliederung")
	@Mapping(target = "Sekundarstufe", source = "idBildungsstufe", qualifiedByName = "updateIdBildungsstufe")
	@Mapping(target = "ID", ignore = true)
	@Mapping(target = "IstChronologisch", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(JahrgangPatchRequest dto, @MappingTarget DTOJahrgang entity);

	/**
	 * Löst die ID des CoreType-Eintrags auf das ASD-Kürzel des Jahrgangs auf.
	 *
	 * @param idJahrgang die ID des Jahrgangs-Eintrags
	 * @return das ASD-Kürzel des Jahrgangs oder {@code null}
	 */
	@Named("updateIdJahrgang")
	default String updateIdJahrgang(final Long idJahrgang) {
		return Optional.ofNullable(Jahrgaenge.data().getEintragByID(idJahrgang))
				.map(j -> j.kuerzel)
				.orElse(null);
	}

	/**
	 * Löst die ID des CoreType-Eintrags auf das Kürzel der Schulgliederung auf.
	 *
	 * @param idSchulgliederung die ID des Schulgliederungs-Eintrags
	 * @return das Kürzel der Schulgliederung oder {@code null}
	 */
	@Named("updateIdSchulgliederung")
	default String updateIdSchulgliederung(final Long idSchulgliederung) {
		return Optional.ofNullable(Schulgliederung.data().getEintragByID(idSchulgliederung))
				.map(j -> j.kuerzel)
				.orElse(null);
	}

	/**
	 * Löst die ID des CoreType-Eintrags auf den Schlüssel der Bildungsstufe auf.
	 *
	 * @param idBildungsstufe die ID des Bildungsstufen-Eintrags
	 * @return der Schlüssel der Bildungsstufe oder {@code null}
	 */
	@Named("updateIdBildungsstufe")
	default String updateIdBildungsstufe(final Long idBildungsstufe) {
		return Optional.ofNullable(Bildungsstufe.data().getEintragByID(idBildungsstufe))
				.map(b -> b.schluessel)
				.orElse(null);
	}

}
