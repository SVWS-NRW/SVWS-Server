package de.svws_nrw.db.converter.current;

import jakarta.persistence.Converter;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.db.converter.DBAttributeConverter;



/**
 * Diese Klasse dient dem Konvertieren von Notenpunkte zur Note.
 */
@Converter(autoApply = true)
public final class NoteConverterFromNotenpunkte extends DBAttributeConverter<Note, Integer> {

	/** Die Instanz des Konverters */
	public static final NoteConverterFromNotenpunkte instance = new NoteConverterFromNotenpunkte();

	@Override
	public Integer convertToDatabaseColumn(final Note note) {
		return note.notenpunkte;
	}

	@Override
	public Note convertToEntityAttribute(final Integer dbData) {
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
