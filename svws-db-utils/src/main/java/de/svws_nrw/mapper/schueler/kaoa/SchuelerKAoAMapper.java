package de.svws_nrw.mapper.schueler.kaoa;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.core.data.schueler.SchuelerKAoADaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerKAoADaten;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schueler.kaoa.SchuelerKAoACreateRequest;
import de.svws_nrw.service.schueler.kaoa.SchuelerKAoAPatchRequest;

/**
 * MapStruct-Mapper für die KAoA-Daten eines Schülers: mappt das Datenbank-DTO in das Core-DTO, erstellt neue Datenbank-DTOs
 * und überträgt gepatchte Daten auf bestehende Datenbank-DTOs. Der Mapper prüft nichts, die Prüfung gegen die KAoA-Kataloge
 * übernimmt der Service mit dem SchuelerKAoAValidator.
 */
@Mapper(uses = JsonNullableMapper.class)
public interface SchuelerKAoAMapper {

	/** Die Instanz des Mappers. */
	SchuelerKAoAMapper INSTANCE = Mappers.getMapper(SchuelerKAoAMapper.class);

	/** Der Wert, den die API für ein nicht gesetztes Merkmal oder Zusatzmerkmal liefert. */
	long ID_NICHT_GESETZT = -1L;

	/**
	 * Mappt ein {@link DTOSchuelerKAoADaten} in das Core-DTO {@link SchuelerKAoADaten}. Ein fehlendes Merkmal oder Zusatzmerkmal
	 * wird als {@code -1} abgebildet. Schuljahresabschnitt und Schuljahr des Jahrgangs stammen aus dem übergebenen Schuljahresabschnitt.
	 *
	 * @param entity     die KAoA-Daten aus der Datenbank
	 * @param abschnitt  der Schuljahresabschnitt des Lernabschnitts der KAoA-Daten
	 *
	 * @return das Core-DTO
	 */
	@Mapping(target = "id", source = "entity.id")
	@Mapping(target = "idSchuljahresabschnitt", source = "abschnitt.id")
	@Mapping(target = "idJahrgang", expression = "java(mapIdJahrgang(entity.jahrgang, abschnitt.schuljahr))")
	@Mapping(target = "idMerkmal", source = "entity.idMerkmal", defaultExpression = "java(ID_NICHT_GESETZT)")
	@Mapping(target = "idZusatzmerkmal", source = "entity.idZusatzmerkmal", defaultExpression = "java(ID_NICHT_GESETZT)")
	SchuelerKAoADaten toApi(DTOSchuelerKAoADaten entity, Schuljahresabschnitt abschnitt);

	/**
	 * Löst den ASD-Schlüssel eines Jahrgangs in die ID des Jahrgangs im angegebenen Schuljahr auf. Der Service hat den Jahrgang
	 * zuvor im Validator geprüft.
	 *
	 * @param asdJahrgang   der ASD-Schlüssel des Jahrgangs
	 * @param schuljahr     das Schuljahr, in dem der Jahrgang gültig ist
	 *
	 * @return die ID des Jahrgangs
	 */
	default long mapIdJahrgang(final String asdJahrgang, final int schuljahr) {
		return Jahrgaenge.data().getEintragBySchuljahrUndSchluessel(schuljahr, asdJahrgang).id;
	}

	/**
	 * Löst die ID eines Jahrgangs in dessen ASD-Schlüssel auf. Der Service hat den Jahrgang zuvor im Validator geprüft.
	 *
	 * @param idJahrgang   die ID des Jahrgangs
	 *
	 * @return der ASD-Schlüssel des Jahrgangs
	 */
	@Named("mapAsdJahrgang")
	default String mapAsdJahrgang(final long idJahrgang) {
		return Jahrgaenge.data().getEintragByID(idJahrgang).schluessel;
	}

	/**
	 * Überträgt einen Create-Request in das Core-DTO {@link SchuelerKAoADaten}. Die ID bleibt ungesetzt.
	 *
	 * @param request   der Create-Request
	 *
	 * @return das Core-DTO mit den Werten des Requests
	 */
	@Mapping(target = "id", ignore = true)
	SchuelerKAoADaten toApi(SchuelerKAoACreateRequest request);

	/**
	 * Erstellt ein neues {@link DTOSchuelerKAoADaten} ohne ID aus den bereits geprüften Daten. Die ID vergibt das Repository.
	 *
	 * @param idLernabschnitt   die ID des Lernabschnitts, zu dem die KAoA-Daten gehören
	 * @param daten             die geprüften KAoA-Daten
	 *
	 * @return die neue Entität
	 */
	@Mapping(target = "id", constant = "0L")
	@Mapping(target = "idLernabschnitt", source = "idLernabschnitt")
	@Mapping(target = "idKategorie", source = "daten.idKategorie")
	@Mapping(target = "jahrgang", source = "daten.idJahrgang", qualifiedByName = "mapAsdJahrgang")
	DTOSchuelerKAoADaten toDomain(long idLernabschnitt, SchuelerKAoADaten daten);

	/**
	 * Wendet die gesetzten Felder eines Patch-Requests auf das Core-DTO an. Nicht gesetzte Felder (undefined) bleiben
	 * unverändert, ein gesetztes {@code null} leert das Feld.
	 *
	 * @param input     der Patch-Request mit den zu ändernden Feldern
	 * @param toPatch   das zu aktualisierende Core-DTO
	 */
	@Mapping(target = "id", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patchDaten(SchuelerKAoAPatchRequest input, @MappingTarget SchuelerKAoADaten toPatch);

	/**
	 * Überträgt die geprüften KAoA-Daten nach dem Zusammenführen mit dem Patch vollständig auf die bestehenden KAoA-Daten.
	 * Die ID bleibt unverändert, der Jahrgang wird in seinen ASD-Schlüssel aufgelöst.
	 *
	 * @param idLernabschnitt   die ID des Lernabschnitts, zu dem die KAoA-Daten gehören
	 * @param daten             die geprüften KAoA-Daten nach dem Zusammenführen mit dem Patch
	 * @param toPatch           die zu aktualisierenden KAoA-Daten
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "idLernabschnitt", source = "idLernabschnitt")
	@Mapping(target = "idKategorie", source = "daten.idKategorie")
	@Mapping(target = "jahrgang", source = "daten.idJahrgang", qualifiedByName = "mapAsdJahrgang")
	void patch(long idLernabschnitt, SchuelerKAoADaten daten, @MappingTarget DTOSchuelerKAoADaten toPatch);

}
