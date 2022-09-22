package de.nrw.schule.svws.db.converter.current;

import jakarta.persistence.Converter;

import de.nrw.schule.svws.core.types.Note;
import de.nrw.schule.svws.db.converter.DBAttributeConverter;



/**
 * Diese Klasse dient dem Konvertieren vom Notenkürzel zu der Aufzählung {@link Note}.
 */
@Converter(autoApply = true)
public class NoteConverterFromKuerzel extends DBAttributeConverter<Note, String> {

	/** Die Instanz des Konverters */
	public final static NoteConverterFromKuerzel instance = new NoteConverterFromKuerzel();
	
	@Override
	public String convertToDatabaseColumn(Note note) {
		if (note == null)
			return null;		
		String s = note.kuerzel;
		if ((s != null) && "".contentEquals(s))
			return null;
		return note.kuerzel;
	}

	@Override
	public Note convertToEntityAttribute(String dbData) {
		return Note.fromKuerzel(dbData);
	}

	@Override
	public Class<Note> getResultType() {
		return Note.class;
	}

	@Override
	public Class<String> getDBType() {
		return String.class;
	}

}
