package de.svws_nrw.mapper.lehrer.stammdaten;

import java.util.Optional;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.lehrer.stammdaten.LehrerStammdatenCreateRequest;
import de.svws_nrw.service.lehrer.stammdaten.LehrerStammdatenPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct-Mapper für die Konvertierung zwischen {@link DTOLehrer} und den zugehörigen API- bzw. Service-Modellen
 * der Lehrer-Stammdaten.
 * <p>
 * <b>Vorbedingung für alle Mapping-Methoden:</b> Die fachliche Validierung aller Eingabedaten (CoreType-IDs,
 * Kürzel, Referenzintegrität) obliegt ausschließlich dem Service vor dem Aufruf des Mappers. Wohnort und Ortsteil
 * werden nicht gemappt, sondern vom Service gesetzt.
 */
@Mapper(uses = JsonNullableMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LehrerStammdatenMapper {

	/** Instanz des Mappers */
	LehrerStammdatenMapper INSTANCE = Mappers.getMapper(LehrerStammdatenMapper.class);

	/** Die ID, die für ein nicht gesetztes Geschlecht geliefert wird. */
	int ID_GESCHLECHT_NICHT_GESETZT = -1;

	/**
	 * Konvertiert einen {@link DTOLehrer} in {@link LehrerStammdaten}. Das Foto und die Leitungsfunktionen werden
	 * bewusst ignoriert und müssen nach dem Mapping separat gesetzt werden.
	 *
	 * @param entity   der Lehrer aus der Datenbank
	 *
	 * @return die befüllten {@link LehrerStammdaten}
	 */
	@Mapping(target = "id", source = "ID")
	@Mapping(target = "kuerzel", source = "Kuerzel")
	@Mapping(target = "kuerzelStatistik", source = "kuerzelLID")
	@Mapping(target = "personalTyp", source = "PersonTyp", qualifiedByName = "mapPersonalTypKuerzel")
	@Mapping(target = "anrede", source = "Anrede", defaultValue = "")
	@Mapping(target = "titel", source = "Titel", defaultValue = "")
	@Mapping(target = "amtsbezeichnung", source = "Amtsbezeichnung", defaultValue = "")
	@Mapping(target = "nachname", source = "Nachname", defaultValue = "")
	@Mapping(target = "vorname", source = "Vorname", defaultValue = "")
	@Mapping(target = "geschlecht", source = "Geschlecht", qualifiedByName = "mapGeschlechtId")
	@Mapping(target = "geburtsdatum", source = "Geburtsdatum")
	@Mapping(target = "idStaatsangehoerigkeit", source = "staatsangehoerigkeit", qualifiedByName = "mapNationalitaetId")
	@Mapping(target = "strassenname", source = "Strassenname")
	@Mapping(target = "hausnummer", source = "HausNr")
	@Mapping(target = "hausnummerZusatz", source = "HausNrZusatz")
	@Mapping(target = "wohnortID", source = "Ort_ID")
	@Mapping(target = "ortsteilID", source = "Ortsteil_ID")
	@Mapping(target = "emailDienstlich", source = "eMailDienstlich")
	@Mapping(target = "emailPrivat", source = "eMailPrivat")
	@Mapping(target = "istSichtbar", source = "Sichtbar", qualifiedByName = "mapNullToTrue")
	@Mapping(target = "istRelevantFuerStatistik", source = "statistikRelevant", qualifiedByName = "mapNullToTrue")
	@Mapping(target = "foto", ignore = true)
	@Mapping(target = "leitungsfunktionen", ignore = true)
	LehrerStammdaten toApi(DTOLehrer entity);

	/**
	 * Mappt einen {@link LehrerStammdatenCreateRequest} auf eine neue {@link DTOLehrer}-Entity. Die ID sowie
	 * Wohnort und Ortsteil werden vom Service gesetzt.
	 *
	 * @param request   der Create-Request mit den zu übernehmenden Feldern
	 *
	 * @return die befüllte {@link DTOLehrer}-Entity
	 */
	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "Kuerzel", source = "kuerzel")
	@Mapping(target = "Nachname", source = "nachname")
	@Mapping(target = "Vorname", source = "vorname")
	@Mapping(target = "PersonTyp", source = "personalTyp", qualifiedByName = "mapPersonalTyp")
	@Mapping(target = "Geschlecht", source = "geschlecht", qualifiedByName = "mapGeschlecht")
	@Mapping(target = "Anrede", source = "anrede")
	@Mapping(target = "Titel", source = "titel")
	@Mapping(target = "Amtsbezeichnung", source = "amtsbezeichnung")
	@Mapping(target = "Geburtsdatum", source = "geburtsdatum")
	@Mapping(target = "staatsangehoerigkeit", source = "idStaatsangehoerigkeit", qualifiedByName = "mapNationalitaet")
	@Mapping(target = "Strassenname", source = "strassenname")
	@Mapping(target = "HausNr", source = "hausnummer")
	@Mapping(target = "HausNrZusatz", source = "hausnummerZusatz")
	@Mapping(target = "telefon", source = "telefon")
	@Mapping(target = "telefonMobil", source = "telefonMobil")
	@Mapping(target = "eMailDienstlich", source = "emailDienstlich")
	@Mapping(target = "eMailPrivat", source = "emailPrivat")
	@Mapping(target = "Sichtbar", source = "istSichtbar")
	@Mapping(target = "statistikRelevant", source = "istRelevantFuerStatistik")
	DTOLehrer toDomain(LehrerStammdatenCreateRequest request);

	/**
	 * Wendet die Änderungen eines {@link LehrerStammdatenPatchRequest} auf eine bestehende {@link DTOLehrer}-Entity an.
	 * Nicht gesetzte Felder werden nicht überschrieben. Wohnort und Ortsteil sowie das Foto werden hier nicht
	 * gemappt, sie verbleiben in der Service-Schicht.
	 *
	 * @param input     der Patch-Request mit den zu ändernden Feldern
	 * @param toPatch   die zu aktualisierende Entity
	 */
	@Mapping(target = "ID", ignore = true)
	@Mapping(target = "Kuerzel", source = "kuerzel")
	@Mapping(target = "Nachname", source = "nachname")
	@Mapping(target = "Vorname", source = "vorname")
	@Mapping(target = "PersonTyp", source = "personalTyp", qualifiedByName = "mapPersonalTyp")
	@Mapping(target = "Geschlecht", source = "geschlecht", qualifiedByName = "mapGeschlecht")
	@Mapping(target = "Anrede", source = "anrede")
	@Mapping(target = "Titel", source = "titel")
	@Mapping(target = "Amtsbezeichnung", source = "amtsbezeichnung")
	@Mapping(target = "Geburtsdatum", source = "geburtsdatum")
	@Mapping(target = "staatsangehoerigkeit", source = "idStaatsangehoerigkeit", qualifiedByName = "mapNationalitaet")
	@Mapping(target = "Strassenname", source = "strassenname")
	@Mapping(target = "HausNr", source = "hausnummer")
	@Mapping(target = "HausNrZusatz", source = "hausnummerZusatz")
	@Mapping(target = "Ort_ID", ignore = true)
	@Mapping(target = "Ortsteil_ID", ignore = true)
	@Mapping(target = "eMailDienstlich", source = "emailDienstlich")
	@Mapping(target = "eMailPrivat", source = "emailPrivat")
	@Mapping(target = "Sichtbar", source = "istSichtbar")
	@Mapping(target = "statistikRelevant", source = "istRelevantFuerStatistik")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(LehrerStammdatenPatchRequest input, @MappingTarget DTOLehrer toPatch);

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
	 * Löst ein Personal-Typ-Kürzel auf den zugehörigen {@link PersonalTyp} auf.
	 *
	 * @param kuerzel   das Kürzel aus dem Request
	 *
	 * @return der zugehörige {@link PersonalTyp}
	 */
	@Named("mapPersonalTyp")
	default PersonalTyp mapPersonalTyp(final String kuerzel) {
		return PersonalTyp.fromKuerzel(kuerzel);
	}

	/**
	 * Liest die numerische ID aus dem {@link Geschlecht}-Enum.
	 *
	 * @param geschlecht   der Geschlecht-Wert aus dem DTO
	 *
	 * @return die ID des Geschlechts oder {@link #ID_GESCHLECHT_NICHT_GESETZT}, wenn kein Geschlecht gesetzt ist
	 */
	@Named("mapGeschlechtId")
	default int mapGeschlechtId(final Geschlecht geschlecht) {
		return (geschlecht == null) ? ID_GESCHLECHT_NICHT_GESETZT : geschlecht.id;
	}

	/**
	 * Löst eine Geschlecht-ID auf den zugehörigen {@link Geschlecht}-Enum auf.
	 *
	 * @param idGeschlecht   die numerische ID des Geschlechts aus dem Request
	 *
	 * @return der zugehörige {@link Geschlecht}-Wert
	 */
	@Named("mapGeschlecht")
	default Geschlecht mapGeschlecht(final Integer idGeschlecht) {
		return Geschlecht.fromValue(idGeschlecht);
	}

	/**
	 * Liest die interne ID aus einem {@link Nationalitaeten}-CoreType.
	 *
	 * @param nationalitaet   der CoreType-Wert aus dem DTO
	 *
	 * @return die interne ID des letzten Historieneintrags oder {@code null}
	 */
	@Named("mapNationalitaetId")
	default Long mapNationalitaetId(final Nationalitaeten nationalitaet) {
		return Optional.ofNullable(nationalitaet)
				.map(n -> n.historie().getLast().id)
				.orElse(null);
	}

	/**
	 * Löst eine Nationalität-ID auf den zugehörigen {@link Nationalitaeten}-Enum auf.
	 *
	 * @param idNationalitaet   die numerische ID der Nationalität aus dem Request
	 *
	 * @return der zugehörige {@link Nationalitaeten}-Wert oder {@code null}
	 */
	@Named("mapNationalitaet")
	default Nationalitaeten mapNationalitaet(final Long idNationalitaet) {
		return (idNationalitaet == null) ? null : Nationalitaeten.data().getWertByIDOrNull(idNationalitaet);
	}

	/**
	 * Wandelt einen {@link Boolean} in einen {@code boolean} um, wobei ein nicht gesetzter Wert als {@code true} gilt.
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
