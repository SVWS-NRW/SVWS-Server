package de.svws_nrw.mapper.schueler.leistungsdaten;

import de.svws_nrw.asd.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.mapper.JsonNullableMapper;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * Mapper für die Konvertierung zwischen der Entity {@link DTOSchuelerLeistungsdaten} und dem
 * Core-DTO {@link SchuelerLeistungsdaten}.
 */
@Mapper(uses = JsonNullableMapper.class)
public interface SchuelerLeistungsdatenMapper {

	/** schuelerLeistungsdatenMapper */
	SchuelerLeistungsdatenMapper INSTANCE = Mappers.getMapper(SchuelerLeistungsdatenMapper.class);

	/**
	 * Konvertiert eine {@link DTOSchuelerLeistungsdaten}-Entity in das Core-DTO {@link SchuelerLeistungsdaten}.
	 * Kursart und Noten werden abhängig vom Schuljahr aufgelöst.
	 *
	 * @param entity     die Entity mit den Leistungsdaten
	 * @param schuljahr  das Schuljahr des zugehörigen Schuljahresabschnitts
	 *
	 * @return das befüllte Core-DTO {@link SchuelerLeistungsdaten}
	 */
	@Mapping(target = "id", source = "entity.ID")
	@Mapping(target = "lernabschnittID", source = "entity.Abschnitt_ID")
	@Mapping(target = "fachID", source = "entity.Fach_ID")
	@Mapping(target = "kursID", source = "entity.Kurs_ID")
	@Mapping(target = "kursart", source = "entity.Kursart", qualifiedByName = "mapKursart")
	@Mapping(target = "abifach", source = "entity.AbiFach", qualifiedByName = "mapAbifach")
	@Mapping(target = "istZP10oderZKEF", source = "entity.Prf10Fach", defaultValue = "false")
	@Mapping(target = "koopSchule", source = "entity.SchulNr")
	@Mapping(target = "lehrerID", source = "entity.Fachlehrer_ID")
	@Mapping(target = "wochenstunden", source = "entity.Wochenstunden", defaultValue = "0")
	@Mapping(target = "zusatzkraftID", source = "entity.Zusatzkraft_ID")
	@Mapping(target = "zusatzkraftWochenstunden", source = "entity.WochenstdZusatzkraft", defaultValue = "0")
	@Mapping(target = "aufZeugnis", source = "entity.AufZeugnis", defaultValue = "true")
	@Mapping(target = "note", source = "entity.NotenKrz", qualifiedByName = "mapNote")
	@Mapping(target = "noteQuartal", source = "entity.NotenKrzQuartal", qualifiedByName = "mapNote")
	@Mapping(target = "istGemahnt", source = "entity.Warnung", defaultValue = "false")
	@Mapping(target = "mahndatum", source = "entity.Warndatum")
	@Mapping(target = "istEpochal", source = "entity.VorherAbgeschl", defaultValue = "false")
	@Mapping(target = "geholtJahrgangAbgeschlossen", source = "entity.AbschlussJahrgang")
	@Mapping(target = "gewichtungAllgemeinbildend", source = "entity.Gewichtung", defaultValue = "1")
	@Mapping(target = "noteBerufsabschluss", source = "entity.NoteAbschlussBA")
	@Mapping(target = "textFachbezogeneLernentwicklung", source = "entity.Lernentw", defaultValue = "")
	@Mapping(target = "umfangLernstandsbericht", source = "entity.Umfang", defaultValue = "")
	@Mapping(target = "fehlstundenGesamt", source = "entity.FehlStd", defaultValue = "0")
	@Mapping(target = "fehlstundenUnentschuldigt", source = "entity.uFehlStd", defaultValue = "0")
	SchuelerLeistungsdaten toApi(DTOSchuelerLeistungsdaten entity, @Context int schuljahr);

	/**
	 * Ermittelt die Kursart. Ist keine Kursart gesetzt, so wird das Kürzel von PUK im Schuljahr verwendet.
	 *
	 * @param kursart     das Kürzel der Kursart aus der Datenbank
	 * @param schuljahr   das Schuljahr, für das der Standardwert ermittelt wird
	 *
	 * @return das Kürzel der Kursart
	 */
	@Named("mapKursart")
	default String mapKursart(final String kursart, @Context final int schuljahr) {
		return (kursart == null) ? ZulaessigeKursart.PUK.daten(schuljahr).kuerzel : kursart;
	}

	/**
	 * Wandelt das Abiturfach von String in Integer um.
	 *
	 * @param abifach   das Abiturfach als String
	 *
	 * @return die Nummer des Abiturfachs oder {@code null}, wenn nicht gesetzt oder keine gültige Zahl
	 */
	@Named("mapAbifach")
	default Integer mapAbifach(final String abifach) {
		if (abifach == null) {
			return null;
		}
		try {
			return Integer.parseInt(abifach);
		} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
			return null;
		}
	}

	/**
	 * Löst das Notenkürzel auf das Kürzel der Note im Schuljahr auf. Ist kein Kürzel gesetzt,
	 * so wird {@link Note#KEINE} verwendet.
	 *
	 * @param kuerzel     das Notenkürzel aus der Datenbank
	 * @param schuljahr   das Schuljahr, für das die Note aufgelöst wird
	 *
	 * @return das Kürzel der Note
	 */
	@Named("mapNote")
	default String mapNote(final String kuerzel, @Context final int schuljahr) {
		final Note note = (kuerzel == null) ? Note.KEINE : Note.data().getWertByKuerzel(kuerzel);
		return note.daten(schuljahr).kuerzel;
	}
}
