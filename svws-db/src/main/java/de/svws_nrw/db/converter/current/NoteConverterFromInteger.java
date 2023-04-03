package de.svws_nrw.db.converter.current;

import jakarta.persistence.Converter;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.db.converter.DBAttributeConverter;



/**
 * Diese Klasse dient dem Konvertieren von einer ganzen Note als Integer zu der Aufzählung {@link Note}.
 */
@Converter(autoApply = true)
public final class NoteConverterFromInteger extends DBAttributeConverter<Note, Integer> {

	/** Die Instanz des Konverters */
	public static final NoteConverterFromInteger instance = new NoteConverterFromInteger();

	@Override
	public Integer convertToDatabaseColumn(final Note note) {
		if ((note == null) || (!note.istNote()))
			return null;
		try {
			return Integer.parseInt(note.ohneTendenz().kuerzel);
		} catch (@SuppressWarnings("unused") final NumberFormatException e) {
			return null;
		}
	}

	@Override
	public Note convertToEntityAttribute(final Integer dbData) {
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
