package de.nrw.schule.svws.db.converter.current;

import jakarta.persistence.Converter;

import de.nrw.schule.svws.core.types.Note;
import de.nrw.schule.svws.db.converter.DBAttributeConverter;



/**
 * Diese Klasse dient dem Konvertieren von Notenpunkte zur Note.
 */
@Converter(autoApply = true)
public class NoteConverterFromNotenpunkte extends DBAttributeConverter<Note, Integer> {

	/** Die Instanz des Konverters */
	public final static NoteConverterFromNotenpunkte instance = new NoteConverterFromNotenpunkte();

	@Override
	public Integer convertToDatabaseColumn(Note note) {
		return note.notenpunkte;
	}

	@Override
	public Note convertToEntityAttribute(Integer dbData) {
		return Note.fromNotenpunkte(dbData);
	}

	@Override
	public Class<Note> getResultType() {
		return Note.class;
	}

	@Override
	public Class<Integer> getDBType() {
		return Integer.class;
	}

}