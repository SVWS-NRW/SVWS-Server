package de.nrw.schule.svws.db.converter.current;

import jakarta.persistence.Converter;

import de.nrw.schule.svws.core.types.Note;
import de.nrw.schule.svws.db.converter.DBAttributeConverter;



/**
 * Diese Klasse dient dem Konvertieren von einer ganzen Note als Integer zu der Aufzählung {@link Note}.
 */
@Converter(autoApply = true)
public class NoteConverterFromInteger extends DBAttributeConverter<Note, Integer> {

	/** Die Instanz des Konverters */
	public final static NoteConverterFromInteger instance = new NoteConverterFromInteger();
	
	@Override
	public Integer convertToDatabaseColumn(Note note) {
		if ((note == null) || (!note.istNote()))
			return null;
		try {
			return Integer.parseInt(note.ohneTendenz().kuerzel);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Override
	public Note convertToEntityAttribute(Integer dbData) {
		return Note.fromNoteSekI(dbData);
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
