package de.svws_nrw.mapper.lehrer.liste;

import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct-Mapper für die Konvertierung eines {@link DTOLehrer} in einen {@link LehrerListeEintrag}.
 * <p>
 * Der Mapper kennt weder einen Schuljahresabschnitt noch die Referenzierung in anderen Tabellen. Die Felder
 * {@code istAktiv} und {@code referenziertInAnderenTabellen} werden daher nicht gemappt, sondern vom Service gesetzt.
 * </p>
 */
@Mapper
public interface LehrerListeEintragMapper {

	/** Instanz des Mappers */
	LehrerListeEintragMapper INSTANCE = Mappers.getMapper(LehrerListeEintragMapper.class);

	/** Die Sortierung, die für einen Lehrer ohne Sortierung geliefert wird. */
	int DEFAULT_SORTIERUNG = 32000;

	/**
	 * Konvertiert einen {@link DTOLehrer} in einen {@link LehrerListeEintrag}. Fehlende Texte werden als leerer
	 * String geliefert, eine fehlende Sortierung als {@link #DEFAULT_SORTIERUNG}. Fehlende Angaben zur Sichtbarkeit
	 * und zur Relevanz für die Statistik gelten als {@code true}.
	 *
	 * @param entity   der Lehrer aus der Datenbank
	 *
	 * @return der Eintrag der Lehrerliste
	 */
	@Mapping(target = "id", source = "ID")
	@Mapping(target = "kuerzel", source = "Kuerzel")
	@Mapping(target = "titel", source = "Titel", defaultValue = "")
	@Mapping(target = "nachname", source = "Nachname", defaultValue = "")
	@Mapping(target = "vorname", source = "Vorname", defaultValue = "")
	@Mapping(target = "personTyp", source = "PersonTyp", qualifiedByName = "mapPersonalTypKuerzel")
	@Mapping(target = "sortierung", source = "Sortierung", qualifiedByName = "mapSortierung")
	@Mapping(target = "istSichtbar", source = "Sichtbar", qualifiedByName = "mapNullToTrue")
	@Mapping(target = "istRelevantFuerStatistik", source = "statistikRelevant", qualifiedByName = "mapNullToTrue")
	@Mapping(target = "istAktiv", ignore = true)
	@Mapping(target = "referenziertInAnderenTabellen", ignore = true)
	LehrerListeEintrag toApi(DTOLehrer entity);

	/**
	 * Liest das Kürzel aus einem {@link PersonalTyp}.
	 *
	 * @param personalTyp   der Personal-Typ aus dem DTO
	 *
	 * @return das Kürzel oder ein leerer String, wenn kein Personal-Typ gesetzt ist
	 */
	@Named("mapPersonalTypKuerzel")
	default String mapPersonalTypKuerzel(final PersonalTyp personalTyp) {
		return (personalTyp == null) ? "" : personalTyp.kuerzel;
	}

	/**
	 * Bestimmt die Sortierung des Lehrers.
	 *
	 * @param sortierung   die Sortierung aus dem DTO
	 *
	 * @return die Sortierung oder {@link #DEFAULT_SORTIERUNG}, wenn keine Sortierung gesetzt ist
	 */
	@Named("mapSortierung")
	default int mapSortierung(final Integer sortierung) {
		return (sortierung == null) ? DEFAULT_SORTIERUNG : sortierung;
	}

	/**
	 * Wandelt einen optionalen Wahrheitswert um.
	 *
	 * @param wert   der Wert aus dem DTO
	 *
	 * @return {@code true}, wenn der Wert {@code null} ist oder {@code true} lautet, sonst {@code false}
	 */
	@Named("mapNullToTrue")
	default boolean mapNullToTrue(final Boolean wert) {
		return (wert == null) || wert;
	}

}
